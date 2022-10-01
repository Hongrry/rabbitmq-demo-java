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
public class EmitLogTopic implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(EmitLogTopic.class);
    private static final String EXCHANGE_NAME = "topic_logs";
    ConnectionFactory factory = null;
    private SecureRandom random = new SecureRandom();
    private String routeKey;

    public EmitLogTopic() {
        factory = new ConnectionFactory();
        factory.setHost("192.168.80.130");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("admin");
        factory.setVirtualHost("my_vhost");
    }

    private void send(String routeKey) throws IOException, TimeoutException, InterruptedException {
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            // 声明交换类型 主题模式
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
            String message = getMessage(routeKey);
            // 使用匿名队列
            channel.basicPublish(EXCHANGE_NAME, routeKey,
                    null,
                    message.getBytes(StandardCharsets.UTF_8));
        }
    }

    private String getMessage(String routeKey) {
        return "[ message]:" + routeKey;
    }

    @Override
    public void run() {
        String[] keys = new String[]{
                "quick.orange.rabbit",
                "quick.orange.fox",
                "lazy.orange.elephant",
                "lazy.brown.fox",
                "lazy.pink.rabbit",
                "quick.brown.fox",
                "lazy.orange.male.rabbit"
        };
        try {
            while (true) {
                int idx = Math.abs(random.nextInt()) % 7;
                send(keys[idx]);
                Thread.sleep(500);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
