package com.imooc.springcloud;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by 半仙.
 */
@RestController
@Slf4j
public class Controller {

    @Autowired
    private IService service;

    @GetMapping("/sayHi")
    public String sayHi() {
        return service.sayHi();
    }

//    @PostMapping("/sayHi")
//    public Friend sayHi2() {
//        Friend friend = new Friend();
//        friend.setName("test");
//        return service.sayHiPost(friend);
//    }
//
//    @GetMapping("/retry")
//    public String retry(Integer timeout) {
//        return service.retry(timeout);
//    }

}
