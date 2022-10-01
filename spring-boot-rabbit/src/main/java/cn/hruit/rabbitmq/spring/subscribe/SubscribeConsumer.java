package cn.hruit.rabbitmq.spring.subscribe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author HONGRRY
 * @description
 * @date 2022/10/01 17:08
 **/
@Component
public class SubscribeConsumer {
    private final Logger logger = LoggerFactory.getLogger(SubscribeConsumer.class);

    @RabbitListener(queues = "subscribeQueue1")
    public void subscribeConsumer1(String msg) {
        logger.info("subscribeQueue1 收到消息：{}", msg);
    }

    @RabbitListener(queues = "subscribeQueue2")
    public void subscribeConsumer2(String msg) {
        logger.info("subscribeQueue2 收到消息：{}", msg);

    }
}
