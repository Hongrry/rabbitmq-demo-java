package cn.hruit.rabbitmq.spring.test.subscribe;

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
 * @date 2022/10/01 17:10
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class SubscribeTest {
    @Resource
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testPublish() throws InterruptedException {
        while (true) {
            rabbitTemplate.convertAndSend("subscribeExchange", "", "订阅消息：" + new Date());
            Thread.sleep(500);
        }
    }
}
