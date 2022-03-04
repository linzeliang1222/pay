package top.linzeliang.pay.service.impl;

import org.junit.Test;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import top.linzeliang.pay.PayApplicationTests;

public class PayServiceImplTest extends PayApplicationTests {

    @Autowired
    private PayServiceImpl payServiceImpl;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Test
    public void create() {
//        payServiceImpl.create("16283485587938823882341", BigDecimal.valueOf(0.01), BestPayTypeEnum.WXPAY_NATIVE);
    }

    @Test
    public void sendMessage() {
        amqpTemplate.convertAndSend("payNotify", "hello");
    }
}