package com.imooc.springcloud;

import com.imooc.springcloud.rules.MyRule;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by 半仙.
 */
@Configuration
//@RibbonClient(name = "eureka-client", configuration = MyRule.class)
public class RibbonConfiguration {

//    @Bean
//    public IRule defaultLBStrategy() {
//        return new RandomRule();
//    }

}
