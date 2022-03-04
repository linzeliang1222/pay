package top.linzeliang.pay.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * AlipayProperties
 *
 * @Author: linzeliang
 * @Date: 2022/2/20
 */
@Component
@ConfigurationProperties(prefix = "alipay")
@Data
public class AlipayProperties {

    private String appId;

    private String privateKey;

    private String alipayPublicKey;

    private String notifyUrl;

    private String returnUrl;

}