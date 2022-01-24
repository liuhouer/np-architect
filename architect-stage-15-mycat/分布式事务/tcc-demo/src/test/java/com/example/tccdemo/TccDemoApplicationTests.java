package com.example.tccdemo;

import com.example.tccdemo.service.AccountService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TccDemoApplicationTests {
    @Autowired
    private AccountService accountService;

    @Test
    public void contextLoads() {
    }

    @Test
    public void testAccount() {
        accountService.transferAccount();
    }

}
