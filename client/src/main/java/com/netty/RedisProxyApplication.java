package com.netty;

import com.netty.api.Redis;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@SpringBootApplication
public class RedisProxyApplication implements CommandLineRunner {

    @Resource
    private Redis<String,Person> redis;


    public static void main(String[] args) {
        SpringApplication.run(RedisProxyApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        List<Person> people = new LinkedList<>();
        Person a = new Person();
        a.setA("a");
        a.setB("b");

        people.add(a);
        Person b = new Person();
        b.setA("a");
        b.setB("b");

        people.add(b);

        redis.set("list", b);

        Person list = redis.get("list");


//        long begin = System.currentTimeMillis();
//        log.info("begin:{}", begin);
//        redis.set("ping", "pong",10000);
//
//        String pong = redis.get("ping");
//        long end = System.currentTimeMillis();
//
//        log.info("end:{}", end);
//
//        log.info("end-begin:{}", end - begin);
//
//        log.info("ping:{}", pong);
        log.info("Hello, Spring Boot gives many options;");
    }
}