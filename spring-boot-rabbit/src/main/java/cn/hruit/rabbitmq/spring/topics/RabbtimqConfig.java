package cn.hruit.rabbitmq.spring.topics;


import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author HONGRRY
 */
@Configuration
public class RabbtimqConfig {
    @Bean("topicQueue")
    public Queue queue() {
        return new Queue("topic.queue", false, false, false);
    }

    @Bean("topicExchange")
    public Exchange exchange() {
        return new TopicExchange("topic.exchange", false, false);
    }

    @Bean
    public Binding topicQueueBindingTopicExchange(Queue topicQueue, Exchange topicExchange) {
        return BindingBuilder.bind(topicQueue).to(topicExchange).with("topic.#").noargs();

    }
}