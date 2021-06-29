package com.netty;

import com.netty.api.Redis;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

@Slf4j
@SpringBootApplication
public class RedisProxyApplication implements CommandLineRunner {

    @Resource
    private Redis<String,String> redis;


    public static void main(String[] args) {
        SpringApplication.run(RedisProxyApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        long begin = System.currentTimeMillis();
        log.info("begin:{}", begin);
        redis.set("ping", "pong");

        String pong = redis.get("ping");
        long end = System.currentTimeMillis();

        log.info("end:{}", end);

        log.info("end-begin:{}", end - begin);

        log.info("ping:{}", pong);
        log.info("Hello, Spring Boot gives many options;");
    }
}