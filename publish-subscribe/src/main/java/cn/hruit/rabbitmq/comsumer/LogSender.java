package cn.hruit.rabbitmq.comsumer;

import com.rabbitmq.client.*;
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
public class LogSender implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(LogSender.class);
    private static final String EXCHANGE_NAME = "logs";
    ConnectionFactory factory = null;
    private SecureRandom random = new SecureRandom();

    public LogSender() {
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
            // 声明交换机器 扇形模式
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
            StringBuilder builder = new StringBuilder("[INFO]: ");
            for (int i = 0; i < (random.nextInt() % 10) + 1; i++) {
                builder.append("test: ").append(random.nextInt());
            }
            String message = builder.toString();
            // 使用匿名队列
            channel.basicPublish(EXCHANGE_NAME, "",
                    null,
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
