package top.linzeliang.pay.util.pay.impl;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import com.lly835.bestpay.config.AliPayConfig;
import com.lly835.bestpay.constants.AliPayConstants;
import com.lly835.bestpay.service.impl.alipay.AliPaySignature;
import com.lly835.bestpay.utils.JsonUtil;
import com.lly835.bestpay.utils.MapUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import top.linzeliang.pay.util.pay.MyAliPayApi;
import top.linzeliang.pay.util.pay.MyPayService;
import top.linzeliang.pay.util.pay.request.MyAliOrderRefundRequest;
import top.linzeliang.pay.util.pay.request.MyOrderCloseRequest;
import top.linzeliang.pay.util.pay.request.MyOrderRefundRequest;
import top.linzeliang.pay.util.pay.response.MyAliOrderRefundResponse;
import top.linzeliang.pay.util.pay.response.MyOrderCloseResponse;
import top.linzeliang.pay.util.pay.response.MyOrderRefundResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * TODO
 *
 * @Author: linzeliang
 * @Date: 2022/2/26
 */
@Data
@Slf4j
public class MyAliPayServiceImpl implements MyPayService {

    private AliPayConfig aliPayConfig;

    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(AliPayConstants.ALIPAY_GATEWAY_OPEN)
            .addConverterFactory(GsonConverterFactory.create(
                    //下划线驼峰互转
                    new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()
            ))
            .client(new OkHttpClient.Builder()
                    .addInterceptor((new HttpLoggingInterceptor()
                            .setLevel(HttpLoggingInterceptor.Level.BODY)))
                    .build()
            )
            .build();

    @Override
    public MyOrderCloseResponse close(MyOrderCloseRequest request) {
        return null;
    }

    @Override
    public MyOrderRefundResponse refund(MyOrderRefundRequest request) {
        MyAliOrderRefundRequest aliOrderCloseRequest = new MyAliOrderRefundRequest();
        aliOrderCloseRequest.setAppId(aliPayConfig.getAppId());
        aliOrderCloseRequest.setTimestamp(LocalDateTime.now().format(formatter));
        MyAliOrderRefundRequest.BizContent bizContent = new MyAliOrderRefundRequest.BizContent();
        bizContent.setOutTradeNo(request.getOrderId());
        bizContent.setRefundAmount(request.getRefundAmount());
        aliOrderCloseRequest.setBizContent(JsonUtil.toJsonWithUnderscores(bizContent).replaceAll("\\s*",""));
        aliOrderCloseRequest.setSign(AliPaySignature.sign(MapUtil.object2MapWithUnderline(aliOrderCloseRequest), aliPayConfig.getPrivateKey()));

        Call<MyAliOrderRefundResponse> call = retrofit.create(MyAliPayApi.class).orderRefund((MapUtil.object2MapWithUnderline(aliOrderCloseRequest)));
        Response<MyAliOrderRefundResponse> retrofitResponse = null;
        try {
            retrofitResponse = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert retrofitResponse != null;
        if (!retrofitResponse.isSuccessful()) {
            throw new RuntimeException("【退款支付宝订单】网络异常");
        }
        assert retrofitResponse.body() != null;
        MyAliOrderRefundResponse.MyAliPayOrderRefundResponse response = retrofitResponse.body().getAlipayTradeRefundResponse();
        if (!response.getCode().equals(AliPayConstants.RESPONSE_CODE_SUCCESS)) {
            throw new RuntimeException("【退款支付宝订单】code=" + response.getCode() + ", returnMsg=" + response.getMsg() + String.format("|%s|%s", response.getSubCode(), response.getSubMsg()));
        }

        return buildMyOrderCloseResponse(response);
    }

    private MyOrderRefundResponse buildMyOrderCloseResponse(MyAliOrderRefundResponse.MyAliPayOrderRefundResponse response) {
        MyOrderRefundResponse orderCloseResponse = new MyOrderRefundResponse();
        orderCloseResponse.setCode(response.getCode());
        orderCloseResponse.setMsg(response.getMsg());
        orderCloseResponse.setOutTradeNo(response.getTradeNo());
        orderCloseResponse.setTradeNo(response.getTradeNo());
        orderCloseResponse.setRefundFee(response.getRefundFee());
        return orderCloseResponse;
    }
}