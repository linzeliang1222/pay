package top.linzeliang.pay.service;

import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayResponse;
import top.linzeliang.pay.pojo.PayInfo;

import java.math.BigDecimal;

/**
 * PayService 接口
 *
 * @Author: linzeliang
 * @Date: 2022/2/17
 */
public interface IPayService {

    /**
     * 创建/发起支付
     *
     * @param orderId         订单id
     * @param amount          金额
     * @param userId          用户id
     * @param orderName       订单名称
     * @param bestPayTypeEnum 支付类型
     * @return com.lly835.bestpay.model.PayResponse
     * @date 2022/2/17
     */
    PayResponse create(String orderId, BigDecimal amount, Integer userId, String orderName, BestPayTypeEnum bestPayTypeEnum);

    /**
     * 异步通知处理
     *
     * @param notifyData 收到的通知
     * @return java.lang.String
     * @date 2022/2/18
     */
    String asyncNotify(String notifyData);

    /**
     * 根据订单号查询支付信息
     *
     * @param orderId 订单号
     * @return top.linzeliang.pay.pojo.PayInfo
     * @date 2022/2/19
     */
    PayInfo queryByOrderId(String orderId);
}