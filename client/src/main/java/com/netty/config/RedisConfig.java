package com.netty.config;

import com.netty.api.Redis;
import com.netty.redis.ProxyRedis;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class RedisConfig {

    @Resource
    private NettyClientConfigProperties properties;

    @Bean
    public Redis createRedis() {
        return new ProxyRedis(properties.getClientId());
    }
}
