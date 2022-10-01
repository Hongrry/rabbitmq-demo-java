package cn.hruit.rabbitmq.spring.callback;

import cn.hruit.rabbitmq.spring.callback.config.FutureHolder;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.SettableListenableFuture;

import java.util.UUID;

/**
 * @author HONGRRY
 */
@Component
public class EventProducer {
    private RabbitTemplate rabbitTemplate;

    public EventProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public ListenableFuture<String> sendMessage(String exchange, String routingKey, String message) {
        //correlationDataId相当于消息的唯一表示
        UUID correlationDataId = UUID.randomUUID();
        CorrelationData correlationData = new CorrelationData(correlationDataId.toString());
        final SettableListenableFuture<String> future = new SettableListenableFuture<>();
        buildCallback(correlationDataId.toString(), future);
        rabbitTemplate.convertAndSend(exchange, routingKey, message, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                MessageProperties properties = message.getMessageProperties();
                properties.setCorrelationId(correlationDataId.toString());
                return message;
            }
        }, correlationData);
        return future;
    }

    private void buildCallback(String correlationDataId, SettableListenableFuture<String> future) {
        FutureHolder.set(correlationDataId, future);
    }

}
