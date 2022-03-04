package top.linzeliang.pay.util.pay;

import top.linzeliang.pay.util.pay.request.MyOrderCloseRequest;
import top.linzeliang.pay.util.pay.request.MyOrderRefundRequest;
import top.linzeliang.pay.util.pay.response.MyOrderCloseResponse;
import top.linzeliang.pay.util.pay.response.MyOrderRefundResponse;

/**
 * TODO
 *
 * @Author: linzeliang
 * @Date: 2022/2/26
 */
public interface MyPayService {

    /**
     * 关闭订单
     */
    MyOrderCloseResponse close(MyOrderCloseRequest request);

    /**
     * 退款
     */
    MyOrderRefundResponse refund(MyOrderRefundRequest request);

}