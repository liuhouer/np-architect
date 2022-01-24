package com.imooc.springcloud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by 半仙.
 */
@RestController
public class Controller {

    @Autowired
    private IService service;

    @GetMapping("/sayHi")
    public String sayHi() {
        return service.sayHi();
    }

}
