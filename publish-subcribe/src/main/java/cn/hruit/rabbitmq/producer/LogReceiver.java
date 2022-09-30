package cn.hruit.rabbitmq.producer;

import cn.hruit.rabbitmq.comsumer.LogSender;
import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeoutException;

/**
 * @author HONGRRY
 * @description
 * @date 2022/09/30 11:15
 **/
public class LogReceiver implements Runnable {
    private final Logger logger = LoggerFactory.getLogger(LogReceiver.class);
    private static final String EXCHANGE_NAME = "logs";
    ConnectionFactory factory = null;
    private String id;

    public LogReceiver(final String id) {
        factory = new ConnectionFactory();
        factory.setHost("192.168.80.130");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("admin");
        factory.setVirtualHost("my_vhost");
        this.id = id;
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
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
        // 获取队列
        String queueName = channel.queueDeclare().getQueue();
        // 绑定交换机
        channel.queueBind(queueName, EXCHANGE_NAME, "");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);

            logger.info(" [x] Received '" + message + "'");
            try {
                doWork(message);
            } finally {
                logger.info(" [x] Done:{}", id);
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });
    }

    private static void doWork(String task) {
        for (char ch : task.toCharArray()) {
            if (ch == '.') {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException _ignored) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
