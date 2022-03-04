package top.linzeliang.pay.util.pay.impl;

import com.lly835.bestpay.config.AliPayConfig;
import com.lly835.bestpay.config.WxPayConfig;
import com.lly835.bestpay.enums.BestPayPlatformEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.linzeliang.pay.util.pay.MyPayService;
import top.linzeliang.pay.util.pay.request.MyOrderCloseRequest;
import top.linzeliang.pay.util.pay.request.MyOrderRefundRequest;
import top.linzeliang.pay.util.pay.response.MyOrderCloseResponse;
import top.linzeliang.pay.util.pay.response.MyOrderRefundResponse;

import java.util.Objects;

/**
 * TODO
 *
 * @Author: linzeliang
 * @Date: 2022/2/26
 */
@Component
@Slf4j
public class MyPayServiceImpl implements MyPayService {

    @Autowired
    private WxPayConfig wxPayConfig;

    @Autowired
    private AliPayConfig aliPayConfig;

    public void setWxPayConfig(WxPayConfig wxPayConfig) {
        this.wxPayConfig = wxPayConfig;
    }

    public void setAliPayConfig(AliPayConfig aliPayConfig) {
        this.aliPayConfig = aliPayConfig;
    }

    @Override
    public MyOrderCloseResponse close(MyOrderCloseRequest request) {
        Objects.requireNonNull(request,"request params must not be null");
        if (BestPayPlatformEnum.WX == request.getPayTypeEnum().getPlatform()) {
            //微信支付
            MyWxPayServiceImpl myWxPayService = new MyWxPayServiceImpl();
            myWxPayService.setWxPayConfig(this.wxPayConfig);
            return myWxPayService.close(request);
        } else if (BestPayPlatformEnum.ALIPAY == request.getPayTypeEnum().getPlatform()) {
            // 支付宝支付
            return null;
        }
        throw new RuntimeException("错误的支付方式");
    }

    @Override
    public MyOrderRefundResponse refund(MyOrderRefundRequest request) {
        Objects.requireNonNull(request,"request params must not be null");
        if (BestPayPlatformEnum.WX == request.getPayTypeEnum().getPlatform()) {
            return null;
        } else if (BestPayPlatformEnum.ALIPAY == request.getPayTypeEnum().getPlatform()) {
            // 支付宝支付
            MyAliPayServiceImpl myAliPayService = new MyAliPayServiceImpl();
            myAliPayService.setAliPayConfig(aliPayConfig);
            return myAliPayService.refund(request);
        }
        throw new RuntimeException("错误的支付方式");
    }
}