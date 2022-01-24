package com.imooc.springcloud;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.imooc.springcloud.AuthResponseCode.SUCCESS;
import static com.imooc.springcloud.AuthResponseCode.USER_NOT_FOUND;

/**
 * Created by 半仙.
 */
@RestController
@Slf4j
public class Controller {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping("/login")
    @ResponseBody
    public AuthResponse login(@RequestParam String username,
                              @RequestParam String password) {

        Account account = Account.builder()
                .username(username)
                .build();

        // TODO 验证username + password

        String token = jwtService.token(account);
        account.setToken(token);
        account.setRefreshToken(UUID.randomUUID().toString());

        redisTemplate.opsForValue().set(account.getRefreshToken(), account);

        return AuthResponse.builder()
                .account(account)
                .code(SUCCESS)
                .build();
    }

    @PostMapping("/refresh")
    @ResponseBody
    public AuthResponse refresh(@RequestParam String refreshToken) {
        Account account = (Account) redisTemplate.opsForValue().get(refreshToken);
        if (account == null) {
            return AuthResponse.builder()
                    .code(USER_NOT_FOUND)
                    .build();
        }

        String jwt = jwtService.token(account);
        account.setToken(jwt);
        account.setRefreshToken(UUID.randomUUID().toString());

        redisTemplate.delete(refreshToken);
        redisTemplate.opsForValue().set(account.getRefreshToken(), account);

        return AuthResponse.builder()
                .account(account)
                .code(SUCCESS)
                .build();
    }

    @GetMapping("/verify")
    public AuthResponse verify(@RequestParam String token,
                               @RequestParam String username) {
        boolean success = jwtService.verify(token, username);
        return AuthResponse.builder()
                // TODO 此处最好用invalid token之类的错误信息
                .code(success ? SUCCESS : USER_NOT_FOUND)
                .build();
    }
}
