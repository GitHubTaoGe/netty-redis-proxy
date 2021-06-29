package com.netty;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
@SpringBootApplication
public class RedisProxyApplicationServer implements CommandLineRunner {


    public static void main(String[] args) {
        SpringApplicationBuilder applicationBuilder = new SpringApplicationBuilder(RedisProxyApplicationServer.class);

        applicationBuilder.web(WebApplicationType.NONE);

        SpringApplication application = applicationBuilder.build(args);

        ConfigurableApplicationContext applicationContext = application.run(args);

        applicationContext.addApplicationListener(new ApplicationPidFileWriter());
    }

    @Override
    public void run(String... args) throws Exception {

//        RpcRegistry rpcRegistry = new RpcRegistry(8080);

        new Thread(new TcpNettyServer(8080)).start();

       log.info("Hello, Spring Boot gives many options;");
    }
}