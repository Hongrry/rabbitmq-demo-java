package cn.hruit.rabbitmq.test;


import cn.hruit.rabbitmq.comsumer.EmitLogTopic;
import cn.hruit.rabbitmq.producer.ReceiveLogsDirect;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author HONGRRY
 * @description
 * @date 2022/09/29 22:04
 **/
public class ApiTest {
    ExecutorService threadPool = Executors.newFixedThreadPool(10);

    @Test
    public void testLogTopic() throws InterruptedException {
        // 监听日志
        threadPool.submit(new ReceiveLogsDirect("*.orange.*", "*.orange.*"));
        threadPool.submit(new ReceiveLogsDirect("*.*.rabbit", "*.*.rabbit"));
        threadPool.submit(new ReceiveLogsDirect("lazy.#", "lazy.#","*.*.rabbit"));
        Thread.sleep(500);
        // 发布日志
        threadPool.submit(new EmitLogTopic());

        Thread.sleep(Long.MAX_VALUE);
    }
}