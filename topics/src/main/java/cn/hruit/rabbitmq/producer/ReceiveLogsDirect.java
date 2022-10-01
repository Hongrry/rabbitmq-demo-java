package cn.hruit.rabbitmq.producer;

import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @author HONGRRY
 * @description
 * @date 2022/09/30 11:15
 **/
public class ReceiveLogsDirect implements Runnable {
    private final Logger logger = LoggerFactory.getLogger(ReceiveLogsDirect.class);
    private static final String EXCHANGE_NAME = "topic_logs";
    ConnectionFactory factory = null;
    private String id;
    private String[] routeKeys;

    public ReceiveLogsDirect(final String id, String... routeKeys) {
        factory = new ConnectionFactory();
        factory.setHost("192.168.80.130");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("admin");
        factory.setVirtualHost("my_vhost");
        this.id = id;
        this.routeKeys = routeKeys;
    }

    @Override
    public void run() {
        try {
            receive();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private void receive() throws IOException, TimeoutException, InterruptedException {
        final Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();
        // 声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        // 获取队列 默认创建一个匿名队列
        String queueName = channel.queueDeclare().getQueue();
        // 绑定交换机
        for (String bindingKey : routeKeys) {
            channel.queueBind(queueName, EXCHANGE_NAME, bindingKey);
        }
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            logger.info(" [{}] Received '" + message + "'", id);
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });
        Thread.sleep(Long.MAX_VALUE);
    }
}
