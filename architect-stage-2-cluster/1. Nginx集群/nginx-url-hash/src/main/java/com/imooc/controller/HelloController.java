package com.imooc.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public Object hello() {
        return "192.168.1.175 的 hello 方法";
    }

    @GetMapping("/course/list")
    public Object courseList() {
        return "192.168.1.175 的 courseList 方法";
    }

    @GetMapping("/user/info")
    public Object userInfo() {
        return "192.168.1.175 的 userInfo 方法";
    }

    @GetMapping("/account")
    public Object account() {
        return "192.168.1.175 的 account 方法";
    }

}
