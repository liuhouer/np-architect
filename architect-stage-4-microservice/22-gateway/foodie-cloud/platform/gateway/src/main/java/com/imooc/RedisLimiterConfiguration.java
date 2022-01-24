package com.imooc;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Mono;

/**
 * Created by 半仙.
 */
@Configuration
public class RedisLimiterConfiguration {

    // HostAddress的Key
    @Bean
    @Primary
    public KeyResolver remoteAddrKeyResolver() {
        return exchange -> Mono.just(
                exchange.getRequest()
                        .getRemoteAddress()
                        .getAddress()
                        .getHostAddress()
        );
    }

    @Bean("redisLimiterUser")
    @Primary
    public RedisRateLimiter redisLimiterUser() {
        return new RedisRateLimiter(10, 20);
    }

    @Bean("redisLimiterItem")
    public RedisRateLimiter redisLimiterItem() {
        return new RedisRateLimiter(20, 50);
    }

    // TODO 尝试实现一个in-memory限流器

}
