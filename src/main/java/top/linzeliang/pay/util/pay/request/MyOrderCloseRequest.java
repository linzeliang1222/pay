package top.linzeliang.pay.util.pay.request;

import com.lly835.bestpay.enums.BestPayTypeEnum;
import lombok.Data;

/**
 * TODO
 *
 * @Author: linzeliang
 * @Date: 2022/2/26
 */
@Data
public class MyOrderCloseRequest {

    /**
     * 支付平台
     */
    private BestPayTypeEnum payTypeEnum;

    /**
     * 订单号
     */
    private String orderId;

}