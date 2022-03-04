package top.linzeliang.pay.util.pay.impl;

import com.lly835.bestpay.config.WxPayConfig;
import com.lly835.bestpay.constants.WxPayConstants;
import com.lly835.bestpay.service.impl.WxPaySignature;
import com.lly835.bestpay.utils.MapUtil;
import com.lly835.bestpay.utils.RandomUtil;
import com.lly835.bestpay.utils.XmlUtil;
import lombok.Data;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import top.linzeliang.pay.util.pay.MyPayService;
import top.linzeliang.pay.util.pay.MyWxPayApi;
import top.linzeliang.pay.util.pay.request.MyOrderCloseRequest;
import top.linzeliang.pay.util.pay.request.MyOrderRefundRequest;
import top.linzeliang.pay.util.pay.request.MyWxOrderCloseRequest;
import top.linzeliang.pay.util.pay.response.MyOrderCloseResponse;
import top.linzeliang.pay.util.pay.response.MyOrderRefundResponse;
import top.linzeliang.pay.util.pay.response.MyWxOrderCloseResponse;

import java.io.IOException;

/**
 * TODO
 *
 * @Author: linzeliang
 * @Date: 2022/2/26
 */
@Data
public class MyWxPayServiceImpl implements MyPayService {

    private WxPayConfig wxPayConfig;

    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(WxPayConstants.WXPAY_GATEWAY)
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .client(new OkHttpClient.Builder()
                    .addInterceptor((new HttpLoggingInterceptor()
                            .setLevel(HttpLoggingInterceptor.Level.BODY)))
                    .build()
            )
            .build();

    @Override
    public MyOrderCloseResponse close(MyOrderCloseRequest request) {
        MyWxOrderCloseRequest wxRequest = new MyWxOrderCloseRequest();
        wxRequest.setAppid(wxPayConfig.getAppId());
        wxRequest.setMchId(wxPayConfig.getMchId());
        // 关闭订单的id
        wxRequest.setOutTradeNo(request.getOrderId());
        wxRequest.setNonceStr(RandomUtil.getRandomStr());
        wxRequest.setSign(WxPaySignature.sign(MapUtil.buildMap(wxRequest), wxPayConfig.getMchKey()));

        RequestBody body = RequestBody.create(MediaType.parse("application/xml; charset=utf-8"), XmlUtil.toString(wxRequest));
        Call<MyWxOrderCloseResponse> call = retrofit.create(MyWxPayApi.class).orderClose(body);
        Response<MyWxOrderCloseResponse> retrofitResponse  = null;
        try{
            retrofitResponse = call.execute();
        }catch (IOException e) {
            e.printStackTrace();
        }
        assert retrofitResponse != null;
        if (!retrofitResponse.isSuccessful()) {
            throw new RuntimeException("【微信订单关闭】网络异常");
        }
        MyWxOrderCloseResponse response = retrofitResponse.body();

        assert response != null;
        if(!response.getReturnCode().equals(WxPayConstants.SUCCESS)) {
            throw new RuntimeException("【微信订单查询】returnCode != SUCCESS, returnMsg = " + response.getReturnMsg());
        }
        if (!response.getResultCode().equals(WxPayConstants.SUCCESS)) {
            throw new RuntimeException("【微信订单查询】resultCode != SUCCESS, err_code = " + response.getErrCode() + ", err_code_des=" + response.getErrCodeDes());
        }

        return buildMyOrderCloseResponse(response);
    }

    @Override
    public MyOrderRefundResponse refund(MyOrderRefundRequest request) {
        return null;
    }

    private MyOrderCloseResponse buildMyOrderCloseResponse(MyWxOrderCloseResponse response) {
        MyOrderCloseResponse orderCloseResponse = new MyOrderCloseResponse();
        orderCloseResponse.setResultCode(response.getResultCode());
        orderCloseResponse.setResultMsg(response.getResultMsg());
        orderCloseResponse.setErrCode(response.getErrCode());
        orderCloseResponse.setErrCodeDes(response.getErrCodeDes());
        return orderCloseResponse;
    }

}