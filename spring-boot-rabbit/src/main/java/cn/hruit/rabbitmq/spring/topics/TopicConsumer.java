package cn.hruit.rabbitmq.spring.topics;


import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.SettableListenableFuture;

import java.io.IOException;

/**
 * @author HONGRRY
 * @description
 * @date 2022/10/01 21:05
 **/
@Component
public class TopicConsumer {
    private final Logger logger = LoggerFactory.getLogger(TopicConsumer.class);

    @RabbitListener(queues = "topic.queue", ackMode = "MANUAL")
    public void process(String content, Message message, Channel channel) throws IOException {
        long tag = message.getMessageProperties().getDeliveryTag();
        TopicEvent topicEvent = JSON.parseObject(content, TopicEvent.class);
        logger.info(JSON.toJSONString(topicEvent));
        channel.basicAck(tag, false);
    }
}
