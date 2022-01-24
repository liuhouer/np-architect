package com.example.tccdemo.config;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RocketMQConfig {


    @Bean(initMethod = "start",destroyMethod = "shutdown")
    public DefaultMQProducer producer() {
        DefaultMQProducer producer = new
                DefaultMQProducer("paymentGroup");
        // Specify name server addresses.
        producer.setNamesrvAddr("localhost:9876");
        return producer;
    }

    @Bean(initMethod = "start",destroyMethod = "shutdown")
    public DefaultMQPushConsumer consumer(@Qualifier("messageListener") MessageListenerConcurrently messageListener) throws MQClientException {
        DefaultMQPushConsumer consumer = new
                DefaultMQPushConsumer("paymentConsumerGroup");

        // Specify name server addresses.
        consumer.setNamesrvAddr("localhost:9876");

        // Subscribe one more more topics to consume.
        consumer.subscribe("payment", "*");

        consumer.registerMessageListener(messageListener);

        return consumer;
    }

}
