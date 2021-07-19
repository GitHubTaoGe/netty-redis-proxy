package com.netty.bean;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by ranjt on 2021/6/1.
 * 封装客户端的请求
 */
public class ClientRequest {
    //请求唯一标识
    private final long id;

    //客户端id
    private String clientId;

    //请求命令
    private String command = "test";

    //请求参数
    private Object content;

    //使用原子技术
    private static final AtomicLong al = new AtomicLong(0);

    public ClientRequest() {
        //请求唯一标识id 每次都会自增加1
        id = al.incrementAndGet();
    }

    public ClientRequest(String clientId) {
        //请求唯一标识id 每次都会自增加1
        id = al.incrementAndGet();
        this.clientId = clientId;
    }

    public long getId() {
        return id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
}