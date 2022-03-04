package top.linzeliang.pay.util.pay;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import top.linzeliang.pay.util.pay.response.MyAliOrderRefundResponse;

import java.util.Map;

/**
 * TODO
 *
 * @Author: linzeliang
 * @Date: 2022/2/26
 */
public interface MyAliPayApi {

    @FormUrlEncoded
    @POST("gateway.do")
    Call<MyAliOrderRefundResponse> orderRefund(@FieldMap Map<String, String> map);

}