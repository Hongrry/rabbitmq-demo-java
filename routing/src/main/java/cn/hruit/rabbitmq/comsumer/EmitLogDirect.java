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
public class EmitLogDirect implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(EmitLogDirect.class);
    private static final String EXCHANGE_NAME = "direct_logs";
    ConnectionFactory factory = null;
    private SecureRandom random = new SecureRandom();
    private String[] level = new String[]{"debug", "info", "warn", "error"};

    public EmitLogDirect() {
        factory = new ConnectionFactory();
        factory.setHost("192.168.80.130");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("admin");
        factory.setVirtualHost("my_vhost");
    }

    private void send() throws IOException, TimeoutException, InterruptedException {
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            // 声明交换类型 直接模式
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);


            while (true) {
                // 获取日志级别
                String severity = getSeverity();
                String message = getMessage(severity);
                // 使用匿名队列
                channel.basicPublish(EXCHANGE_NAME, severity,
                        null,
                        message.getBytes(StandardCharsets.UTF_8));
               // logger.info(" [x] Sent '" + message + "'");
                Thread.sleep(500);
            }
        }
    }

    private String getMessage(String severity) {
        return "[" + severity + "]:" +
                "message: " + random.nextLong();
    }

    private String getSeverity() {
        int idx = Math.abs(random.nextInt()) % 4;
        return level[idx];
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
