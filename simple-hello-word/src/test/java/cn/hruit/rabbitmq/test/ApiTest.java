package cn.hruit.rabbitmq.test;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeoutException;

/**
 * @author HONGRRY
 * @description
 * @date 2022/09/29 22:04
 **/
public class ApiTest {
    private final Logger logger = LoggerFactory.getLogger(ApiTest.class);
    private final static String QUEUE_NAME = "hello";
    ConnectionFactory factory = null;

    @Before
    public void init() {
        factory = new ConnectionFactory();
        factory.setHost("192.168.80.130");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("admin");
        factory.setVirtualHost("my_vhost");
    }

    @Test
    public void testSend() throws IOException, TimeoutException {
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            String message = "Hello World!";
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
            logger.info(" [x] Sent '" + message + "'");
        }
    }

    @Test
    public void testReceive() throws IOException, TimeoutException, InterruptedException {
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        CountDownLatch latch = new CountDownLatch(1);
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        logger.info(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            logger.info("tag:{},deliver:{}", consumerTag, delivery);
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            logger.info(" [x] Received '" + message + "'");
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

            latch.countDown();
        };
        channel.basicConsume(QUEUE_NAME, false, deliverCallback, consumerTag -> {

        });

        latch.await();
    }
}