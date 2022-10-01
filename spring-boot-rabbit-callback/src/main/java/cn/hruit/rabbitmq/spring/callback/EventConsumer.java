package cn.hruit.rabbitmq.spring.callback;


import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author HONGRRY
 * @description
 * @date 2022/10/01 21:05
 **/
@Component
public class EventConsumer {
    private final Logger logger = LoggerFactory.getLogger(EventConsumer.class);

    @RabbitListener(queues = "topic.queue", ackMode = "MANUAL")
    public void process(String content, Message message, Channel channel) throws IOException {
        long tag = message.getMessageProperties().getDeliveryTag();
        Event event = JSON.parseObject(content, Event.class);
        logger.info(JSON.toJSONString(event));
        channel.basicAck(tag, false);
    }
}
