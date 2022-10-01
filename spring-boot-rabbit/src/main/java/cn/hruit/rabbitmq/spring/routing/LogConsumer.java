package cn.hruit.rabbitmq.spring.routing;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author HONGRRY
 * @description
 * @date 2022/10/01 16:21
 **/
@Component
public class LogConsumer {
    private final Logger logger = LoggerFactory.getLogger(LogConsumer.class);

    @RabbitListener(queues = "error-level-logs")
    public void errorLogHandler(String content, Message message, Channel channel) {
        logger.error(content);
    }

    @RabbitListener(queues = "all-level-logs")
    public void allLogHandler(String content, Message message, Channel channel) {
        logger.info(content);
    }
}
