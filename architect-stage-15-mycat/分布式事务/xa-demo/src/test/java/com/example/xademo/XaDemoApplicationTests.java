package com.example.xademo;

import com.example.xademo.service.XAService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class XaDemoApplicationTests {
    @Autowired
    private XAService xaService;

    @Test
    public void contextLoads() {
    }

    @Test
    public void testXA() {

        xaService.testXA();

    }
}
