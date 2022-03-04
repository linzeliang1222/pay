package top.linzeliang.pay.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PayInfo {

    private Integer id;

    private Integer userId;

    private Long orderNo;

    private Long mapOrderNo;

    private Integer payPlatform;

    private String platformNumber;

    private String platformStatus;

    private BigDecimal payAmount;

    private Date createTime;

    private Date updateTime;

    public PayInfo(Integer userId, Long orderNo, Long mapOrderNo, Integer payPlatform, String platformStatus, BigDecimal payAmount) {
        this.userId = userId;
        this.orderNo = orderNo;
        this.mapOrderNo = mapOrderNo;
        this.payPlatform = payPlatform;
        this.platformStatus = platformStatus;
        this.payAmount = payAmount;
    }
}