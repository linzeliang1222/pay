package top.linzeliang.pay.util.pay.response;

import lombok.Data;
import org.simpleframework.xml.Element;

@Data
public class MyWxOrderCloseResponse {

    @Element(name = "return_code")
    private String returnCode;

    @Element(name = "return_msg", required = false)
    private String returnMsg;

    /** 以下字段在return_code为SUCCESS的时候有返回. */
    @Element(name = "appid", required = false)
    private String appid;

    @Element(name = "mch_id", required = false)
    private String mchId;

    @Element(name = "nonce_str", required = false)
    private String nonceStr;

    @Element(name = "sign", required = false)
    private String sign;

    @Element(name = "result_code", required = false)
    private String resultCode;

    @Element(name = "result_msg", required = false)
    private String resultMsg;

    @Element(name = "err_code", required = false)
    private String errCode;

    @Element(name = "err_code_des", required = false)
    private String errCodeDes;

}