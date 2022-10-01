package cn.hruit.rabbitmq.spring.topics;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.UUID;

/**
 * @author HONGRRY
 */
@Component
public class RabbitProducer {
    private RabbitTemplate rabbitTemplate;

    //配置confirm监听具体处理，确认消息到达MQ,根据实际业务场景处理
    final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
        @Override
        public void confirm(@Nullable CorrelationData correlationData, boolean ack, @Nullable String cause) {
            System.out.println("=============confirmCallBack触发。消息到达MQ broker===========");
            System.out.println("correlationDataID=" + correlationData.getId());
            System.out.println("ack=" + ack);
            System.out.println("cause=" + cause);
            if (!ack) {
                System.err.println("异常处理。。。。");
            }
        }
    };

    //配置return监听处理，消息无法路由到queue,根据实际业务操作
    final RabbitTemplate.ReturnCallback returnCallback = new RabbitTemplate.ReturnCallback() {
        @Override
        public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
            System.out.println("=============returnCallback触发。消息路由到queue失败===========");
            System.out.println("msg=" + new String(message.getBody()));
            System.out.println("replyCode=" + replyCode);
            System.out.println("replyText=" + replyText);
            System.out.println("exchange=" + exchange);
            System.out.println("routingKey=" + routingKey);
        }
    };

    public RabbitProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        //设置消息的confirm监听，监听消息是否到达exchange
        rabbitTemplate.setConfirmCallback(confirmCallback);
        //设置消息的return监听，当消息无法路由到queue时候，会触发这个监听。
        rabbitTemplate.setReturnCallback(returnCallback);
    }

    public void sendMessage(String exchange, String routingKey, String message) {
        //correlationDataId相当于消息的唯一表示
        UUID correlationDataId = UUID.randomUUID();
        CorrelationData correlationData = new CorrelationData(correlationDataId.toString());
        rabbitTemplate.convertAndSend(exchange, routingKey, message, correlationData);

    }

}
