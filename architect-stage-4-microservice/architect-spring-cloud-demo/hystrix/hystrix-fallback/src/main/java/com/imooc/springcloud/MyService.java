package com.imooc.springcloud;

import com.imooc.springcloud.hystrix.Fallback;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by 半仙.
 */
@FeignClient(name = "feign-client", fallback = Fallback.class)
public interface MyService extends IService {

}
