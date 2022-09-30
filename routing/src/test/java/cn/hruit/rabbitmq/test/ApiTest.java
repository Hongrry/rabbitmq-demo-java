package cn.hruit.rabbitmq.test;


import cn.hruit.rabbitmq.comsumer.EmitLogDirect;
import cn.hruit.rabbitmq.producer.ReceiveLogsDirect;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
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
    public void testLogRouting() throws InterruptedException {
        String[] listenLevel = new String[]{
                "debug",
                "info",
                "warn",
                "error",
        };
        // 发布日志
        threadPool.submit(new EmitLogDirect());
        // 监听日志
        threadPool.submit(new ReceiveLogsDirect("all", listenLevel));
        threadPool.submit(new ReceiveLogsDirect("error", "error"));
        threadPool.submit(new ReceiveLogsDirect("info", "info"));
        threadPool.submit(new ReceiveLogsDirect("warn", "warn"));
        threadPool.submit(new ReceiveLogsDirect("debug", "debug"));

        Thread.sleep(Long.MAX_VALUE);
    }
}