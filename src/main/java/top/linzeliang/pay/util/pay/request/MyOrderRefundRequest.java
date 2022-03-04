package top.linzeliang.pay.util.pay.request;

import com.lly835.bestpay.enums.BestPayTypeEnum;
import lombok.Data;

/**
 * TODO
 *
 * @Author: linzeliang
 * @Date: 2022/2/27
 */
@Data
public class MyOrderRefundRequest {

    /**
     * 支付平台
     */
    private BestPayTypeEnum payTypeEnum;

    /**
     * 订单号
     */
    private String orderId;

    /**
     * 退款金额
     */
    private Double refundAmount;

    /**
     * 退款原因
     */
    private String refundReason;

}