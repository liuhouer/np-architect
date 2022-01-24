package com.imooc.springcloud.hystrix;

import com.imooc.springcloud.Friend;
import com.imooc.springcloud.MyService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by 半仙.
 */
@Slf4j
@Component
public class Fallback implements MyService {

    @Override
    @HystrixCommand(fallbackMethod = "fallback2")
    public String error() {
        log.info("Fallback: I'm not a black sheep any more");
        throw new RuntimeException("first fallback");
    }

    @HystrixCommand(fallbackMethod = "fallback3")
    public String fallback2() {
        log.info("fallback again");
        throw new RuntimeException("fallback again");
    }

    public String fallback3() {
        log.info("fallback again and again");
        return "success";
    }


    @Override
    public String sayHi() {
        return null;
    }

    @Override
    public Friend sayHiPost(@RequestBody Friend friend) {
        return null;
    }

    @Override
    public String retry(@RequestParam(name = "timeout") int timeout) {
        return "You are late !";
    }

}
