package top.linzeliang.pay.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * TODO
 *
 * @Author: linzeliang
 * @Date: 2022/2/26
 */
public class PayUtil {

    /**
     * 根据当前时间生成订单号
     *
     * @return java.lang.Long
     * @date 2022/2/26
     */
    public static Long generateOrderId() {
        StringBuilder orderNo = new StringBuilder();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        orderNo.append(simpleDateFormat.format(new Date()));
        orderNo.append((int) (Math.random() * 10));
        return Long.valueOf(orderNo.toString());
    }

}