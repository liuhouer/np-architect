package com.imooc.live.seata.tcc.starter;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import com.imooc.live.seata.tcc.service.TccConsumerService;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ImportResource;

import java.util.concurrent.locks.ReentrantLock;

@SpringBootApplication
@ImportResource("classpath:consumer/*.xml")
@ComponentScan("com.imooc.live.seata.tcc")
public class TccServiceConsumer {


    public static void main(String[] args) throws Exception {
        ApplicationContext context = SpringApplication.run(TccServiceConsumer.class, args);
        TccConsumerService tccConsumerService = (TccConsumerService) context.getBean(TccConsumerService.class);

        tccConsumerService.fail();
    }
}

