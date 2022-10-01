package cn.hruit.rabbitmq.spring.test;

import cn.hruit.rabbitmq.spring.RabbitApplication;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

/**
 * @author HONGRRY
 * @description
 * @date 2022/10/01 15:01
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class ContextTest {
    @Resource
    private RabbitTemplate rabbitTemplate;

    @Test
    public void test() throws InterruptedException {
        rabbitTemplate.convertAndSend("test", "你好");
        Thread.sleep(1000);
    }

}
