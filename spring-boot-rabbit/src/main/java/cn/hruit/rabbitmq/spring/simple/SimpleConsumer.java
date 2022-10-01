package cn.hruit.rabbitmq.spring.simple;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author HONGRRY
 * @description
 * @date 2022/10/01 15:27
 **/
@Component
public class SimpleConsumer {
    private final Logger logger = LoggerFactory.getLogger(SimpleConsumer.class);

    @RabbitListener(queues = Config.SIMPLE_QUEUE, ackMode = "MANUAL")
    public void process(String content, Message message, Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        logger.info("收到消息:{},{}", content, message);
        channel.basicAck(deliveryTag, false);
    }
}
