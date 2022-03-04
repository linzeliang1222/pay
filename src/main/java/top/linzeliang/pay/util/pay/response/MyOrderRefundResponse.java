package top.linzeliang.pay.util.pay.response;

import lombok.Data;

/**
 * TODO
 *
 * @Author: linzeliang
 * @Date: 2022/2/27
 */
@Data
public class MyOrderRefundResponse {

    private String code;

    private String msg;

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