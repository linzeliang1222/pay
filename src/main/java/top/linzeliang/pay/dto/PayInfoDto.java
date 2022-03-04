package top.linzeliang.pay.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * TODO
 *
 * @Author: linzeliang
 * @Date: 2022/2/24
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PayInfoDto {

    private Integer userId;

    private Long orderNo;

    private Integer payPlatform;

    private String platformStatus;

    private BigDecimal payAmount;

    private Date payTime;

}