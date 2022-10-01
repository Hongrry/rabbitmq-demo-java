package cn.hruit.rabbitmq.spring.callback.config;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.SettableListenableFuture;

/**
 * @author HONGRRY
 * @description 返还消息回调
 * @date 2022/10/01 22:49
 **/
@Component
public class DefaultReturnCallback implements RabbitTemplate.ReturnCallback {
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        String correlationId = message.getMessageProperties().getCorrelationId();
        if (correlationId != null) {
            SettableListenableFuture<String> future = FutureHolder.get(correlationId);
            if (future != null) {
                future.setException(new RuntimeException(
                        String.format("return message,replyCode:%d ,replyText:%s ,exchange:%s ,routingKey:%s",
                                replyCode, replyText, exchange, replyCode)
                ));
            }
        }
        // TODO 其他处理方法
    }
}
