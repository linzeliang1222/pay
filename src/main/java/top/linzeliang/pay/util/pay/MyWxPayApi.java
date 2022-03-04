package top.linzeliang.pay.util.pay;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import top.linzeliang.pay.util.pay.response.MyWxOrderCloseResponse;

/**
 * TODO
 *
 * @Author: linzeliang
 * @Date: 2022/2/26
 */
public interface MyWxPayApi {

    @POST("/pay/closeorder")
    Call<MyWxOrderCloseResponse> orderClose(@Body RequestBody body);

}