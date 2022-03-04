package top.linzeliang.pay.config;

import com.lly835.bestpay.config.AliPayConfig;
import com.lly835.bestpay.config.WxPayConfig;
import com.lly835.bestpay.service.BestPayService;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 支付配置信息
 *
 * @Author: linzeliang
 * @Date: 2022/2/18
 */
@Configuration
public class BestPayConfig {

    @Autowired
    private WxProperties wxProperties;

    @Autowired
    private AlipayProperties alipayProperties;

    /**
     * 提供支付服务类
     *
     * @param wxPayConfig  微信支付配置信息
     * @param aliPayConfig 支付宝支付配置信息
     * @return com.lly835.bestpay.service.BestPayService
     * @date 2022/2/20
     */
    @Bean
    public BestPayService bestPayService(WxPayConfig wxPayConfig, AliPayConfig aliPayConfig) {
        BestPayServiceImpl bestPayService = new BestPayServiceImpl();
        bestPayService.setWxPayConfig(wxPayConfig);
        bestPayService.setAliPayConfig(aliPayConfig);
        return bestPayService;
    }

    /**
     * 微信支付配置信息
     *
     * @return com.lly835.bestpay.config.WxPayConfig
     * @date 2022/2/20
     */
    @Bean
    public WxPayConfig wxPayConfig() {
        WxPayConfig wxPayConfig = new WxPayConfig();
        wxPayConfig.setAppId(wxProperties.getAppId());
        wxPayConfig.setMchId(wxProperties.getMchId());
        wxPayConfig.setMchKey(wxProperties.getMchKey());
        wxPayConfig.setNotifyUrl(wxProperties.getNotifyUrl());
        wxPayConfig.setReturnUrl(wxProperties.getReturnUrl());
        return wxPayConfig;
    }

    /**
     * 支付宝支付配置信息
     *
     * @return com.lly835.bestpay.config.AliPayConfig
     * @date 2022/2/20
     */
    @Bean
    public AliPayConfig aliPayConfig() {
        AliPayConfig aliPayConfig = new AliPayConfig();
        aliPayConfig.setAppId(alipayProperties.getAppId());
        aliPayConfig.setPrivateKey(alipayProperties.getPrivateKey());
        aliPayConfig.setAliPayPublicKey(alipayProperties.getAlipayPublicKey());
        aliPayConfig.setNotifyUrl(alipayProperties.getNotifyUrl());
        aliPayConfig.setReturnUrl(alipayProperties.getReturnUrl());
        return aliPayConfig;
    }
}