package com.imooc.springcloud;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by 半仙.
 */
@RestController
@RequestMapping("/refresh")
@RefreshScope
public class RefreshController {

    @Value("${words}")
    private String words;

    @Value("${food}")
    private String food;


    @GetMapping("/words")
    public String getWords() {
        return words;
    }

    @GetMapping("/dinner")
    public String dinner() {
        return "May I have one " + food;
    }

}
