package top.linzeliang.pay;

import com.lly835.bestpay.enums.BestPayTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.linzeliang.pay.util.pay.MyPayService;
import top.linzeliang.pay.util.pay.request.MyOrderRefundRequest;
import top.linzeliang.pay.util.pay.response.MyOrderRefundResponse;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class PayApplicationTests {

    @Autowired
    private MyPayService payService;

    @Test
    public void test() {
        MyOrderRefundRequest myOrderRefundRequest = new MyOrderRefundRequest();
        myOrderRefundRequest.setOrderId("202202271437598995");
        myOrderRefundRequest.setPayTypeEnum(BestPayTypeEnum.ALIPAY_PC);
        myOrderRefundRequest.setRefundAmount(0.01);
        myOrderRefundRequest.setRefundReason("请不要重复支付订单");
        MyOrderRefundResponse result = payService.refund(myOrderRefundRequest);
        log.debug("-----result------>{}", result);
    }
}
