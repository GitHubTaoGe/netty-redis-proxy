package com.netty.config;

import com.netty.api.Redis;
import com.netty.redis.ProxyRedis;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfig {

    @Bean
    public Redis createRedis() {
        return new ProxyRedis();
    }
}
