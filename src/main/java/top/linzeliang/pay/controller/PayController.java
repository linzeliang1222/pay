package top.linzeliang.pay.controller;

import com.lly835.bestpay.config.WxPayConfig;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import top.linzeliang.pay.pojo.PayInfo;
import top.linzeliang.pay.service.IPayService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * PayController
 *
 * @Author: linzeliang
 * @Date: 2022/2/17
 */
@Controller
@RequestMapping("/pay")
@Slf4j
public class PayController {

    @Autowired
    private IPayService payService;

    @Autowired
    private WxPayConfig wxPayConfig;

    @GetMapping("/create")
    public ModelAndView create(@RequestParam("orderId") String orderId,
                               @RequestParam("amount") BigDecimal amount,
                               @RequestParam(value = "userId", required = false, defaultValue = "-1") Integer userId,
                               @RequestParam(value = "orderName", required = false, defaultValue = "未命名的订单") String orderName,
                               @RequestParam("payType") BestPayTypeEnum bestPayTypeEnum) {
        // 发送支付请求
        PayResponse response = payService.create(orderId, amount, userId, orderName, bestPayTypeEnum);

        if (null != response) {
            // 支付方式不同，渲染就不同
            // WXPAY_NATIVE使用codeUrl，ALIPAY_PC使用body
            Map<String, Object> map = new HashMap<>();
            if (BestPayTypeEnum.WXPAY_NATIVE == bestPayTypeEnum) {
                map.put("codeUrl", response.getCodeUrl());
                map.put("returnUrl", wxPayConfig.getReturnUrl());
                map.put("orderId", orderId);
                return new ModelAndView("createForWxNative", map);
            } else if (BestPayTypeEnum.ALIPAY_PC == bestPayTypeEnum) {
                map.put("body", response.getBody());
                return new ModelAndView("createForAlipayPc", map);
            }
        } else {
            throw new RuntimeException("该订单已支付");
        }

        throw new RuntimeException("暂不支持的支付类型");
    }

    @PostMapping("/notify")
    @ResponseBody
    public String asyncNotify(@RequestBody String notifyData) {
        return payService.asyncNotify(notifyData);
    }

    @GetMapping("/queryByOrderId")
    @ResponseBody
    public String queryByOrderId(@RequestParam("orderId") String orderId) {
        // 返回订单号对应的订单状态，如果没查到则返回空
        PayInfo payInfo = payService.queryByOrderId(orderId);
        if (null == payInfo) {
            return null;
        } else {
            return payInfo.getPlatformStatus();
        }
    }

}