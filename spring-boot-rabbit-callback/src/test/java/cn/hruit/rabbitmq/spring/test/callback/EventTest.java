package cn.hruit.rabbitmq.spring.test.callback;

import cn.hruit.rabbitmq.spring.callback.Event;
import cn.hruit.rabbitmq.spring.callback.EventProducer;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

/**
 * @author HONGRRY
 * @description
 * @date 2022/10/01 21:12
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class EventTest {
    private final Logger logger = LoggerFactory.getLogger(EventTest.class);
    @Resource
    private EventProducer eventProducer;

    @Test
    public void testSendEvent() throws InterruptedException {
        String id = UUID.randomUUID().toString();
        Event topicEvent = new Event(id, new Date(), "消息");
        String string = JSON.toJSONString(topicEvent);
        ListenableFuture<String> future = eventProducer.sendMessage("topic.exchange", "topic.aaa", string);
        future.addCallback(new ListenableFutureCallback<String>() {
            @Override
            public void onSuccess(String String) {
                logger.info("成功发送消息");
            }

            @Override
            public void onFailure(Throwable ex) {
                logger.info(ex.getMessage());
            }
        });
        Thread.sleep(Long.MAX_VALUE);
    }
}
