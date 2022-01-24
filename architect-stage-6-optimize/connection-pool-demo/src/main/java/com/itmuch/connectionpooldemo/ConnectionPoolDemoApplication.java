package com.itmuch.connectionpooldemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class ConnectionPoolDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConnectionPoolDemoApplication.class, args);
    }
}
