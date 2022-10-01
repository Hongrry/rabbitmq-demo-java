package cn.hruit.rabbitmq.test;

import cn.hruit.rabbitmq.comsumer.LogSender;
import cn.hruit.rabbitmq.producer.LogReceiver;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

/**
 * @author HONGRRY
 * @description
 * @date 2022/09/29 22:04
 **/
public class ApiTest {
    ExecutorService threadPool = Executors.newFixedThreadPool(10);
    public static CountDownLatch latch = new CountDownLatch(20);

    @Test
    public void testSend() throws IOException, TimeoutException, InterruptedException {
        for (int i = 0; i < 2; i++) {
            threadPool.submit(new LogSender());
            threadPool.submit(new LogSender());
        }
        //Thread.sleep(Long.MAX_VALUE);
    }

    @Test
    public void testReceive() throws IOException, TimeoutException, InterruptedException {
        threadPool.submit(new LogReceiver("0001"));
        threadPool.submit(new LogReceiver("0002"));
        Thread.sleep(Long.MAX_VALUE);
    }

    @Test
    public void testAll() throws InterruptedException {

        threadPool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    testReceive();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        Thread.sleep(10000);
        threadPool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    testSend();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        Thread.sleep(Long.MAX_VALUE);
    }
}