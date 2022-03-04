package top.linzeliang.pay.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * WxProperties
 *
 * @Author: linzeliang
 * @Date: 2022/2/20
 */
@Component
@ConfigurationProperties(prefix = "wx")
@Data
public class WxProperties {

    private String appId;

    private String mchId;

    private String mchKey;

    private String notifyUrl;

    private String returnUrl;

}