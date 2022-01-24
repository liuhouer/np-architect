package com.imooc.architect.controller;


import com.imooc.architect.service.ZooService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 */
@RestController
@RequestMapping("zoo")
public class ZooController {

    @Autowired
    private ZooService zooService;

    @RequestMapping("takeAway")
    public String takeAway(@RequestParam("id") Long id){
        if (zooService.takeAway(id)) {
            return "success";
        }
        // 拿不走直接抛异常
        throw new IllegalStateException("No way sir!!!");
    }
}
