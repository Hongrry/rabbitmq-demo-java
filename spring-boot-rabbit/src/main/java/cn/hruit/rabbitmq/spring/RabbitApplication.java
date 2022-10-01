package cn.hruit.rabbitmq.spring;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author HONGRRY
 * @description
 * @date 2022/10/01 14:59
 **/
@SpringBootApplication
public class RabbitApplication {
    public static void main(String[] args) {
        SpringApplication.run(RabbitApplication.class);
    }

    @Bean
    Queue queue() {
        return new Queue("test");
    }
}
