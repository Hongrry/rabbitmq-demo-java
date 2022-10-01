package cn.hruit.rabbitmq.spring.subscribe;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * @author HONGRRY
 * @description
 * @date 2022/10/01 16:52
 **/
@Configuration
public class SubscribeConfig {
    @Bean
    public Exchange subscribeExchange() {
        return new FanoutExchange("subscribeExchange");
    }

    @Bean
    public Queue subscribeQueue1() {
        return new Queue("subscribeQueue1");
    }

    @Bean
    public Queue subscribeQueue2() {
        return new Queue("subscribeQueue2");
    }

    @Bean
    public Binding bindingQueue1ToExchange(Queue subscribeQueue1, Exchange subscribeExchange) {
        return new Binding(subscribeQueue1.getName(), Binding.DestinationType.QUEUE, subscribeExchange.getName(), "", null);
    }

    @Bean
    public Binding bindingQueue2ToExchange(Queue subscribeQueue2, Exchange subscribeExchange) {
        return new Binding(subscribeQueue2.getName(), Binding.DestinationType.QUEUE, subscribeExchange.getName(), "", null);

    }
}
