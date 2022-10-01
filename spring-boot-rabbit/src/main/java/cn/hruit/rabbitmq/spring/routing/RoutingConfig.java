package cn.hruit.rabbitmq.spring.routing;


import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author HONGRRY
 * @description
 * @date 2022/10/01 16:05
 **/
@Configuration
public class RoutingConfig {
    @Bean
    public Exchange logExchange() {
        return new DirectExchange("log-exchange");
    }

    @Bean
    public Queue errorLevelQueue() {
        return new Queue("error-level-logs");
    }

    @Bean
    public Queue allLevelQueue() {
        return new Queue("all-level-logs");
    }

    @Bean
    public Binding bingToErrorQueue(Queue errorLevelQueue, Exchange logExchange) {
        return new Binding(errorLevelQueue.getActualName(), Binding.DestinationType.QUEUE, logExchange.getName(), "error", null);
    }

    @Bean
    public Binding bingInfoToAllQueue(Queue allLevelQueue, Exchange logExchange) {
        return new Binding(allLevelQueue.getActualName(), Binding.DestinationType.QUEUE, logExchange.getName(), "info", null);
    }

    @Bean
    public Binding bingWarnToAllQueue(Queue allLevelQueue, Exchange logExchange) {
        return new Binding(allLevelQueue.getActualName(), Binding.DestinationType.QUEUE, logExchange.getName(), "warn", null);
    }

    @Bean
    public Binding bingDebugToAllQueue(Queue allLevelQueue, Exchange logExchange) {
        return new Binding(allLevelQueue.getActualName(), Binding.DestinationType.QUEUE, logExchange.getName(), "debug", null);
    }
}
