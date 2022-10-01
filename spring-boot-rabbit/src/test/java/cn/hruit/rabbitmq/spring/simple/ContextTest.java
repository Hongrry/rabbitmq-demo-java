package cn.hruit.rabbitmq.spring.simple;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author HONGRRY
 * @description
 * @date 2022/10/01 15:01
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class ContextTest {
    public static final String SIMPLE_QUEUE = "simple";

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testSendSimpleMsg() throws InterruptedException {
         while (true)
        {
            rabbitTemplate.convertAndSend(SIMPLE_QUEUE, "你好" + new Date());
            Thread.sleep(500);
        }

    }

}
