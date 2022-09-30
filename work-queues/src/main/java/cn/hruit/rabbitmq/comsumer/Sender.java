package cn.hruit.rabbitmq.comsumer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.concurrent.TimeoutException;

/**
 * @author HONGRRY
 * @description
 * @date 2022/09/30 11:11
 **/
public class Sender implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(Sender.class);
    private final static String TASK_QUEUE_NAME = "task_queue";
    ConnectionFactory factory = null;
    private SecureRandom random = new SecureRandom();

    public Sender() {
        factory = new ConnectionFactory();
        factory.setHost("192.168.80.130");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("admin");
        factory.setVirtualHost("my_vhost");
    }

    private void send() throws IOException, TimeoutException {
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            // durable=true 队列持久化
            channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);

            StringBuilder builder = new StringBuilder("new message");
            for (int i = 0; i < random.nextInt() % 10; i++) {
                builder.append(".");
            }
            String message = builder.toString();
            // MessageProperties.PERSISTENT_TEXT_PLAIN 消息持久化
            channel.basicPublish("", TASK_QUEUE_NAME,
                    MessageProperties.PERSISTENT_TEXT_PLAIN,
                    message.getBytes(StandardCharsets.UTF_8));
            logger.info(" [x] Sent '" + message + "'");
        }
    }

    @Override
    public void run() {
        try {
            send();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
