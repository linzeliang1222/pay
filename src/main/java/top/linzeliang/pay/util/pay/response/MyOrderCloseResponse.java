package top.linzeliang.pay.util.pay.response;

import lombok.Data;

/**
 * TODO
 *
 * @Author: linzeliang
 * @Date: 2022/2/26
 */
@Data
public class MyOrderCloseResponse {

    private String resultCode;

    private String resultMsg;

    private String errCode;

    private String errCodeDes;

    /**
     * 商户请求的订单号
     */
    private String orderNo;

    /**
     * 支付系统的交易流水号
     */
    private String outOrderNo;

}