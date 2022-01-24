package com.itmuch.connectionpooldemo.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "foodie-dev", url = "http://localhost:8088")
public interface FoodieDevFeignClient {
    @GetMapping("/index/cats")
    String cats();
}
