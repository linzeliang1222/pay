package top.linzeliang.pay.util.pay.response;

import lombok.Data;

/**
 * TODO
 *
 * @Author: linzeliang
 * @Date: 2022/2/26
 */
@Data
public class MyAliOrderRefundResponse {

    private MyAliOrderRefundResponse.MyAliPayOrderRefundResponse alipayTradeRefundResponse;

    private String sign;

    @Data
    public static class MyAliPayOrderRefundResponse {

        private String code;

        private String msg;

        private String subCode;

        private String subMsg;

        /**
         * 支付宝交易号
         */
        private String tradeNo;

        /**
         * 商家订单号
         */
        private String outTradeNo;

        private Double refundFee;

    }
}