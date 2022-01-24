package com.imooc.springcloud.hystrix;

import com.imooc.springcloud.Friend;
import com.imooc.springcloud.MyService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheKey;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by 半仙.
 */
@Slf4j
@Service
public class RequestCacheService {

    @Autowired
    private MyService service;

    @CacheResult
    @HystrixCommand(commandKey = "cacheKey")
    public Friend requestCache(@CacheKey String name) {
        log.info("request cache " + name);
        Friend friend = new Friend();
        friend.setName(name);
        friend = service.sayHiPost(friend);
        log.info("after requesting cache " + name);
        return friend;
    }

}
