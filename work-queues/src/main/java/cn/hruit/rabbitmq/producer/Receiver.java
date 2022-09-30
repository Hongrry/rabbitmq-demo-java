package cn.hruit.rabbitmq.producer;

import cn.hruit.rabbitmq.comsumer.Sender;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
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
public class Receiver implements Runnable {
    private final Logger logger = LoggerFactory.getLogger(Sender.class);
    private final static String TASK_QUEUE_NAME = "task_queue";
    ConnectionFactory factory = null;
    private SecureRandom random = new SecureRandom();
    private String id;
    private CountDownLatch downLatch;

    public Receiver(final String id, CountDownLatch latch) {
        factory = new ConnectionFactory();
        factory.setHost("192.168.80.130");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("admin");
        factory.setVirtualHost("my_vhost");
        this.id = id;
        this.downLatch = latch;
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
        CountDownLatch latch = new CountDownLatch(1);
        channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
        logger.info(" [*] Waiting for messages. To exit press CTRL+C");

        channel.basicQos(1);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);

            logger.info(" [x] Received '" + message + "'");
            try {
                doWork(message);
            } finally {
                logger.info(" [x] Done:{}", id);
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                latch.countDown();
                downLatch.countDown();
            }
        };
        channel.basicConsume(TASK_QUEUE_NAME, false, deliverCallback, consumerTag -> {
        });
        latch.await();
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
