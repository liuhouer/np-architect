package com.imooc.architect.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

/**
 * @author 姚半仙
 */
@FeignClient(value = "ZOO-SERVICE")
@RequestMapping("zoo")
public interface ZooService {

    @RequestMapping("takeAway")
    public String takeAway(@RequestParam("id") Long id);

}
