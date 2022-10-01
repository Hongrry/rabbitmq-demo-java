package cn.hruit.rabbitmq.spring.test.routing;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.security.SecureRandom;
import java.util.Date;

/**
 * @author HONGRRY
 * @description
 * @date 2022/10/01 15:01
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class RoutingTest {
    public static final String SIMPLE_QUEUE = "simple";

    @Resource
    private RabbitTemplate rabbitTemplate;
    private SecureRandom random = new SecureRandom();

    @Test
    public void testSendRoutingMsg() throws InterruptedException {
        String[] logLevel = new String[]{
                "debug",
                "info",
                "warn",
                "error",
        };
        while (true) {
            int idx = Math.abs(random.nextInt()) % 4;
            rabbitTemplate.convertAndSend("log-exchange", logLevel[idx], logLevel[idx] + "消息：" + new Date());
            Thread.sleep(500);
        }

    }

}
