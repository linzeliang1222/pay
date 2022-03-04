package top.linzeliang.pay.enums;

import com.lly835.bestpay.enums.BestPayTypeEnum;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * 支付类型枚举类
 * 1-支付宝
 * 2-微信
 *
 * @Author: linzeliang
 * @Date: 2022/2/19
 */
@Getter
@Slf4j
public enum PayPlatformEnum {

    ALIPAY(1),
    WX(2);

    private Integer code;

    PayPlatformEnum(Integer code) {
        this.code = code;
    }

    /**
     * 根据 BestPayTypeEnum 支付类型枚举值获取对应的支付类型数字
     *
     * @param bestPayTypeEnum 支付类型
     * @return top.linzeliang.pay.enums.PayPlatformEnum
     * @date 2022/2/20
     */
    public static PayPlatformEnum getByBestPayTypeEnum(BestPayTypeEnum bestPayTypeEnum) {
        // 遍历所有的支付类型
        for (PayPlatformEnum payPlatformEnum : PayPlatformEnum.values()) {
            // 如果匹配，则返回对应的 PayPlatformEnum 枚举值
            if (bestPayTypeEnum.getPlatform().name().equals(payPlatformEnum.name())) {
                return payPlatformEnum;
            }
        }
        // 支付类型都不匹配抛出异常
        log.debug("错误的支付平台: {}", bestPayTypeEnum.name());
        throw new RuntimeException("错误的支付平台: " + bestPayTypeEnum.name());
    }
}