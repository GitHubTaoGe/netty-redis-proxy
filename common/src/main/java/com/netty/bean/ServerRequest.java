package com.netty.bean;

/**
 * Created by ranjt on 2021/6/1.
 * 封装服务端的请求
 */
public class ServerRequest<T> {
    private String command;

    private T content;

    private  long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public T getContent() {

        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    @Override
    public String toString() {
        System.out.println("command:"+command+","+"id:"+id+","+"content:"+content);
        return super.toString();
    }
}