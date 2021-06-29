package com.netty.config;

import com.netty.handler.TcpClientInitalizer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "redis")
public class NettyClientConfig {

    private String secret;

    private String hots;

    private int port;

    private EventLoopGroup group = new NioEventLoopGroup();
    private Bootstrap client = new Bootstrap();
    private ChannelFuture future = null;

    @Bean
    public ChannelFuture createChannelFuture() {
        client.group(group);
        client.channel(NioSocketChannel.class);
        client.option(ChannelOption.SO_KEEPALIVE, true);
        client.handler(new TcpClientInitalizer());
        try {
            future = client.connect(hots, port).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return future;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public void setHots(String hots) {
        this.hots = hots;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
