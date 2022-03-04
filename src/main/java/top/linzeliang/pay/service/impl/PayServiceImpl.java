package top.linzeliang.pay.service.impl;

import com.google.gson.Gson;
import com.lly835.bestpay.enums.BestPayPlatformEnum;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.enums.OrderStatusEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.service.BestPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.linzeliang.pay.dao.PayInfoMapper;
import top.linzeliang.pay.dto.PayInfoDto;
import top.linzeliang.pay.enums.PayPlatformEnum;
import top.linzeliang.pay.pojo.PayInfo;
import top.linzeliang.pay.service.IPayService;
import top.linzeliang.pay.util.PayUtil;
import top.linzeliang.pay.util.pay.MyPayService;
import top.linzeliang.pay.util.pay.request.MyOrderCloseRequest;
import top.linzeliang.pay.util.pay.request.MyOrderRefundRequest;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * PayService
 *
 * @Author: linzeliang
 * @Date: 2022/2/17
 */
@Slf4j
@Service
public class PayServiceImpl implements IPayService {

    private static final String QUEUE_PAY_NOTIFY = "payNotify";

    @Autowired
    private BestPayService bestPayService;

    @Autowired
    private PayInfoMapper payInfoMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private MyPayService myPayService;

    @Override
    public PayResponse create(String orderId, BigDecimal amount, Integer userId, String orderName, BestPayTypeEnum bestPayTypeEnum) {
        // 支付前，先查询当前订单号是否存在未关闭的订单
        List<PayInfo> notPayPayInfoList = payInfoMapper.selectByOrderNoAndStatus(Long.valueOf(orderId), OrderStatusEnum.NOTPAY.name());
        if (null != notPayPayInfoList && !notPayPayInfoList.isEmpty()) {
            // 将未支付的订单关闭
            for (PayInfo payInfo : notPayPayInfoList) {
                // 只更新微信订单状态
                if (PayPlatformEnum.getByBestPayTypeEnum(BestPayTypeEnum.WXPAY_NATIVE).getCode().equals(payInfo.getPayPlatform())) {
                    MyOrderCloseRequest orderCloseRequest = new MyOrderCloseRequest();
                    orderCloseRequest.setPayTypeEnum(bestPayTypeEnum);
                    orderCloseRequest.setOrderId(String.valueOf(payInfo.getMapOrderNo()));
                    myPayService.close(orderCloseRequest);

                    // 也要更新数据库中的
                    payInfo.setPlatformStatus(OrderStatusEnum.CLOSED.name());
                }
            }

            // 批量更新
            int i = payInfoMapper.batchUpdate(notPayPayInfoList);
            if (0 >= i) {
                throw new RuntimeException("将未支付状态修改为关闭状态失败");
            }
        }

        // 查询订单是否支已经支付
        List<PayInfo> payInfoList = payInfoMapper.selectByOrderNoAndStatus(Long.valueOf(orderId), OrderStatusEnum.SUCCESS.name());
        if (null != payInfoList && !payInfoList.isEmpty()) {
            return null;
        }

        // 根据orderId生成对应订单号
        Long mapOrderNo = PayUtil.generateOrderId();

        // 将订单信息写入数据库
        PayInfo payInfo = new PayInfo(
                userId,
                Long.valueOf(orderId),
                mapOrderNo,
                PayPlatformEnum.getByBestPayTypeEnum(bestPayTypeEnum).getCode(),
                OrderStatusEnum.NOTPAY.name(),
                amount);
        payInfoMapper.insertSelective(payInfo);

        // 生成支付请求
        PayRequest payRequest = new PayRequest();
        payRequest.setOrderName(orderName);
        payRequest.setOrderId(String.valueOf(mapOrderNo));
        payRequest.setOrderAmount(amount.doubleValue());
        payRequest.setPayTypeEnum(bestPayTypeEnum);

        // 发起支付
        PayResponse payResponse = bestPayService.pay(payRequest);
//        log.debug("发起支付: payResponse={}", payResponse);

        return payResponse;
    }

    @Override
    public String asyncNotify(String notifyData) {
        // 签名校验
        PayResponse payResponse = bestPayService.asyncNotify(notifyData);
//        log.debug("异步通知 -> payResponse={}", payResponse);

        // 先从数据库查到是否支付
        PayInfo payInfo = payInfoMapper.selectByMapOrderNo(Long.parseLong(payResponse.getOrderId()));
        if (null == payInfo) {
            // 发出告警（钉钉、短信）
            throw new RuntimeException("通过orderNo查询到的结果为null, orderNo=" + payResponse.getOrderId());
        }

        // 支付宝返回的响应
        if (PayPlatformEnum.getByBestPayTypeEnum(BestPayTypeEnum.ALIPAY_PC).getCode().equals(payInfo.getPayPlatform())) {
            Long orderNo = payInfo.getOrderNo();
            List<PayInfo> payInfoList = payInfoMapper.selectByOrderNoAndStatus(orderNo, OrderStatusEnum.SUCCESS.name());
            // 如果存在支付成功的订单记录，就将当前订单退款
            if (!payInfoList.isEmpty()) {
                // 退款
                MyOrderRefundRequest myOrderRefundRequest = new MyOrderRefundRequest();
                myOrderRefundRequest.setOrderId(String.valueOf(payInfo.getMapOrderNo()));
                myOrderRefundRequest.setPayTypeEnum(BestPayTypeEnum.ALIPAY_PC);
                myOrderRefundRequest.setRefundAmount(payInfo.getPayAmount().doubleValue());
                myPayService.refund(myOrderRefundRequest);
                // 修改订单状态为退款
                payInfo.setPlatformStatus(OrderStatusEnum.REFUND.name());
                payInfo.setPlatformNumber(payResponse.getOutTradeNo());
                payInfoMapper.updateByPrimaryKeySelective(payInfo);
                return null;
            }
        }

        // 如果订单状态不是 "已支付"
        if (!OrderStatusEnum.SUCCESS.name().equals(payInfo.getPlatformStatus())) {
            // 如果实际支付金额和数据库的支付金额不同，则告警
            if (payInfo.getPayAmount().compareTo(BigDecimal.valueOf(payResponse.getOrderAmount())) != 0) {
                throw new RuntimeException("异步通知中的金额和数据库里的不一致, orderNo=" + payInfo.getOrderNo());
            }
            // 若金额正确，那么修改支付状态为 SUCCESS，更新数据库记录
            payInfo.setPlatformStatus(OrderStatusEnum.SUCCESS.name());
            payInfo.setPlatformNumber(payResponse.getOutTradeNo());
            payInfoMapper.updateByPrimaryKeySelective(payInfo);
        }

        // 支付成功，发送MQ消息
        // pay发送消息，mall接收消息
        Gson gson = new Gson();
        PayInfoDto payInfoDto = new PayInfoDto(payInfo.getUserId(),
                payInfo.getOrderNo(),
                payInfo.getPayPlatform(),
                payInfo.getPlatformStatus(),
                payInfo.getPayAmount(),
                new Date());
        amqpTemplate.convertAndSend(QUEUE_PAY_NOTIFY, gson.toJson(payInfoDto));

        // 通知支付商不要再发送异步通知了
        if (BestPayPlatformEnum.WX == payResponse.getPayPlatformEnum()) {
            return "<xml>\n" +
                    "  <return_code><![CDATA[SUCCESS]]></return_code>\n" +
                    "  <return_msg><![CDATA[OK]]></return_msg>\n" +
                    "</xml>";
        } else if (BestPayPlatformEnum.ALIPAY == payResponse.getPayPlatformEnum()) {
            return "success";
        }

        throw new RuntimeException("异步通知中错误的支付平台");
    }

    @Override
    public PayInfo queryByOrderId(String orderId) {
        List<PayInfo> payInfoList = payInfoMapper.selectByOrderNoAndStatus(Long.valueOf(orderId), OrderStatusEnum.SUCCESS.name());
        return payInfoList.isEmpty() ? null : payInfoList.get(0);
    }

}