package com.netty.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "proxy.redis")
public class NettyClientConfigProperties {

    private String clientId;

    private String hots;

    private int port;

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setHots(String hots) {
        this.hots = hots;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getClientId() {
        return clientId;
    }

    public String getHots() {
        return hots;
    }

    public int getPort() {
        return port;
    }
}
