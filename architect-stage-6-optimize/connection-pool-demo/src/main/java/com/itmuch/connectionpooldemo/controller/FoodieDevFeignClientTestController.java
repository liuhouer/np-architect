package com.itmuch.connectionpooldemo.controller;

import com.itmuch.connectionpooldemo.feignclient.FoodieDevFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FoodieDevFeignClientTestController {
    @Autowired
    FoodieDevFeignClient foodieDevFeignClient;

    @GetMapping("/test-feign")
    public String index() {
        return this.foodieDevFeignClient.cats();
    }
}
