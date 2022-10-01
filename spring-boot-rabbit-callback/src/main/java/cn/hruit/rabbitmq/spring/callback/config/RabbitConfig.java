package cn.hruit.rabbitmq.spring.callback.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author HONGRRY
 * @description Rabbit配置
 * @date 2022/10/01 22:35
 **/
@Configuration
public class RabbitConfig {
    private RabbitTemplate rabbitTemplate;
    private DefaultConfirmCallback defaultConfirmCallback;
    private DefaultReturnCallback defaultReturnCallback;

    public RabbitConfig(RabbitTemplate rabbitTemplate, DefaultConfirmCallback defaultConfirmCallback, DefaultReturnCallback defaultReturnCallback) {
        this.rabbitTemplate = rabbitTemplate;
        this.defaultConfirmCallback = defaultConfirmCallback;
        this.defaultReturnCallback = defaultReturnCallback;
        this.rabbitTemplate.setConfirmCallback(defaultConfirmCallback);
        this.rabbitTemplate.setReturnCallback(defaultReturnCallback);
    }

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

