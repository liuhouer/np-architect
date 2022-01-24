package com.imooc.auth;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * Created by 半仙.
 */
@SpringBootApplication
@EnableEurekaClient
public class AuthApplication {

    // 这里提供了很多种加密算法，生产环境可以用更高等级的加密算法，比如
    // 【最常用】采用非对称密钥加密，auth-service只负责生成jwt-token
    //  由各个业务方（或网关层）在自己的代码里用key校验token的正确性
    //  优点：符合规范，并且节约了一次HTTP Call
    //
    //  咱这里用了简单的token生成方式，同学们可以试着把上面的场景在本地实现
    public static void main(String[] args) {
        new SpringApplicationBuilder(AuthApplication.class)
                .web(WebApplicationType.SERVLET)
                .run(args);
    }
}
