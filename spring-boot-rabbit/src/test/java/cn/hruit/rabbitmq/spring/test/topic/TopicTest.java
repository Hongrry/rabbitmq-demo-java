package cn.hruit.rabbitmq.spring.test.topic;

import cn.hruit.rabbitmq.spring.topics.RabbitProducer;
import cn.hruit.rabbitmq.spring.topics.TopicEvent;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

/**
 * @author HONGRRY
 * @description
 * @date 2022/10/01 21:12
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class TopicTest {
    @Resource
    private RabbitProducer rabbitProducer;

    @Test
    public void testSendTopic() throws InterruptedException {
        String id = UUID.randomUUID().toString();
        TopicEvent topicEvent = new TopicEvent(id, new Date(), "消息");
        String string = JSON.toJSONString(topicEvent);
        rabbitProducer.sendMessage("topic.exchange", "aaa.aaa", string);
        Thread.sleep(Long.MAX_VALUE);
    }
}
