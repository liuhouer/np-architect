package com.imooc.springcloud;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by 半仙.
 */
@FeignClient("auth-service")
public interface AuthService {

    @PostMapping("/login")
    @ResponseBody
    public AuthResponse login(@RequestParam("username") String username,
                              @RequestParam("password") String password);

    @GetMapping("/verify")
    public AuthResponse verify(@RequestParam("token") String token,
                               @RequestParam("username") String username);

    @PostMapping("/refresh")
    @ResponseBody
    public AuthResponse refresh(@RequestParam("refresh") String refresh);

}
