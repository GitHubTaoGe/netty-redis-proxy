package com.netty.config;

import com.netty.handler.TcpClientInitalizer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class NettyClientConfig {

    @Resource
    private NettyClientConfigProperties properties;

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
            future = client.connect(properties.getHots(), properties.getPort()).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return future;
    }
}
