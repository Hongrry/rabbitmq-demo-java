package cn.hruit.rabbitmq.spring.simple;

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
 * @date 2022/10/01 15:27
 **/
@Component
public class SimpleConsumer {
    private final Logger logger = LoggerFactory.getLogger(SimpleConsumer.class);

    @RabbitListener(queues = SimpleConfig.SIMPLE_QUEUE, ackMode = "MANUAL")
    public void process1(String content, Message message, Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        logger.info("1 收到消息:{},{}", content, message);
        channel.basicAck(deliveryTag, false);
    }

    @RabbitListener(queues = SimpleConfig.SIMPLE_QUEUE, ackMode = "MANUAL")
    public void process2(String content, Message message, Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        logger.info("2 收到消息:{},{}", content, message);
        channel.basicAck(deliveryTag, false);
    }
}
