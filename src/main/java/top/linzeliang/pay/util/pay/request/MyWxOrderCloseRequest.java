package top.linzeliang.pay.util.pay.request;


import lombok.Data;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Data
@Root(name = "xml", strict = false)
public class MyWxOrderCloseRequest {

    @Element(name = "appid")
    private String appid;

    @Element(name = "mch_id")
    private String mchId;

    @Element(name = "out_trade_no", required = false)
    private String outTradeNo;

    @Element(name = "nonce_str")
    private String nonceStr;

    @Element(name = "sign")
    private String sign;

    @Element(name = "sign_type", required = false)
    private String signType;

}

