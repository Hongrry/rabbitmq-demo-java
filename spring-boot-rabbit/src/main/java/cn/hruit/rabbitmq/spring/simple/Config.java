package cn.hruit.rabbitmq.spring.simple;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author HONGRRY
 * @description
 * @date 2022/10/01 15:27
 **/
@Configuration
public class Config {
    public static final String SIMPLE_QUEUE = "simple";

    @Bean
    public Queue simpleQueue() {
        return new Queue(SIMPLE_QUEUE);
    }
}
