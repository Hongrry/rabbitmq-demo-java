package cn.hruit.rabbitmq.test;

import cn.hruit.rabbitmq.comsumer.Sender;
import cn.hruit.rabbitmq.producer.Receiver;
import org.junit.AfterClass;
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
    public void testSend() throws IOException, TimeoutException {
        for (int i = 0; i < 10; i++) {
            threadPool.submit(new Sender());
            threadPool.submit(new Sender());
        }

    }

    @Test
    public void testReceive() throws IOException, TimeoutException, InterruptedException {
        threadPool.submit(new Receiver("0001", latch));
        threadPool.submit(new Receiver("0002", latch));
        latch.await();
    }

}