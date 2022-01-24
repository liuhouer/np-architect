package com.imooc.springcloud;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * Created by 半仙.
 */
@EnableDiscoveryClient
@SpringBootApplication
@RestController
@Slf4j
public class SleuthTraceAMain {

    @LoadBalanced
    @Bean
    public RestTemplate lb() {
        return new RestTemplate();
    }

    @Autowired
    private RestTemplate restTemplate;


    @GetMapping(value = "/traceA")
    public String traceA() {
        log.info("-------Trace A");
        return restTemplate.getForEntity("http://sleuth-traceB/traceB", String.class)
                .getBody();
    }


    public static void main(String[] args) {
        new SpringApplicationBuilder(SleuthTraceAMain.class)
                .web(WebApplicationType.SERVLET)
                .run(args);
    }

}
