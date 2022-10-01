package cn.hruit.rabbitmq.spring.callback.config;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.SettableListenableFuture;

/**
 * @author HONGRRY
 * @description 确认回调
 * @date 2022/10/01 22:50
 **/
@Component
public class DefaultConfirmCallback implements RabbitTemplate.ConfirmCallback {
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (correlationData != null) {
            String correlationId = correlationData.getId();
            SettableListenableFuture<String> future = FutureHolder.get(correlationId);
            if (future != null) {
                boolean success = ack ? future.set("success") : future.setException(new RuntimeException(cause));
                if (!success) {
                    throw new RuntimeException(String.format("set rabbitmq result failed,id:%s ,cause:%s", correlationId, cause));
                }
                FutureHolder.remove(correlationId);
            }
        }

    }
}
