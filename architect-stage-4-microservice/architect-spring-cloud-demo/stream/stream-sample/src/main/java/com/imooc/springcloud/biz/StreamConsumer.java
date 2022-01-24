package com.imooc.springcloud.biz;

import com.imooc.springcloud.topic.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by 半仙.
 */
@Slf4j
@EnableBinding(value = {
        Sink.class,
        MyTopic.class,
        GroupTopic.class,
        DelayedTopic.class,
        ErrorTopic.class,
        RequeueTopic.class,
        DlqTopic.class,
        FallbackTopic.class
}
)
public class StreamConsumer {

    private AtomicInteger count = new AtomicInteger(1);

    @StreamListener(Sink.INPUT)
    public void consume(Object payload) {
        log.info("message consumed successfully, payload={}", payload);
    }

    // 自定义消息广播
    @StreamListener(MyTopic.INPUT)
    public void consumeMyMessage(Object payload) {
        log.info("My message consumed successfully, payload={}", payload);
    }

    // 消息分组 & 消费分区示例
    @StreamListener(GroupTopic.INPUT)
    public void consumeGroupMessage(Object payload) {
        log.info("Group message consumed successfully, payload={}", payload);
    }

    // 延迟消息示例
    @StreamListener(DelayedTopic.INPUT)
    public void consumeDelayedMessage(MessageBean bean) {
        log.info("Delayed message consumed successfully, payload={}", bean.getPayload());
    }

    // 异常重试（单机版）
    @StreamListener(ErrorTopic.INPUT)
    public void consumeErrorMessage(MessageBean bean) {
        log.info("Are you OK?");

        if (count.incrementAndGet() % 3 == 0) {
            log.info("Fine, thank you. And you?");
            count.set(0);
        } else {
            log.info("What's your problem?");
            throw new RuntimeException("I'm not OK");
        }
    }

    // 异常重试（联机版-重新入列）
    @StreamListener(RequeueTopic.INPUT)
    public void requeueErrorMessage(MessageBean bean) {
        log.info("Are you OK?");
        try {
            Thread.sleep(3000L);
        } catch (Exception e) {
        }
//        throw new RuntimeException("I'm not OK");
    }

    // 死信队列
    @StreamListener(DlqTopic.INPUT)
    public void consumeDlqMessage(MessageBean bean) {
        log.info("Dlq - Are you OK?");
        if (count.incrementAndGet() % 3 == 0) {
            log.info("Dlq - Fine, thank you. And you?");
        } else {
            log.info("Dlq - What's your problem?");
            throw new RuntimeException("I'm not OK");
        }
    }


    // Fallback + 升级版本
    @StreamListener(FallbackTopic.INPUT)
    public void goodbyeBadGuy(MessageBean bean,
                              @Header("version") String version) {
        log.info("Fallback - Are you OK?");

        if ("1.0".equalsIgnoreCase(version)) {
            log.info("Fallback - Fine, thank you. And you?");

        } else if ("2.0".equalsIgnoreCase(version)) {
            log.info("unsupported version");
            throw new RuntimeException("I'm not OK");
        } else {
            log.info("Fallback - version={}", version);
        }
    }

    // 降级流程
    @ServiceActivator(inputChannel = "fallback-topic.fallback-group.errors")
    public void fallback(Message<?> message) {
        log.info("fallback entered");
    }

}
