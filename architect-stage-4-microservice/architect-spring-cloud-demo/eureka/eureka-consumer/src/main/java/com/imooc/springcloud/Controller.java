package com.imooc.springcloud;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * Created by 半仙.
 */

@RestController
@Slf4j
public class Controller {

    @Autowired
    private LoadBalancerClient client;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/hello")
    public String hello() {
        ServiceInstance instance = client.choose("eureka-client");

        if (instance == null) {
            return "No available instances";
        }

        String target = String.format("http://%s:%s/sayHi",
                instance.getHost(),
                instance.getPort());
        log.info("url is {}", target);

        return restTemplate.getForObject(target, String.class);
    }

    @PostMapping("/hello")
    public Friend helloPost() {
        ServiceInstance instance = client.choose("eureka-client");

        if (instance == null) {
            return null;
        }

        String target = String.format("http://%s:%s/sayHi",
                instance.getHost(),
                instance.getPort());
        log.info("url is {}", target);

        Friend friend = new Friend();
        friend.setName("Eureka Consumer");

        return restTemplate.postForObject(target, friend, Friend.class);
    }

}
