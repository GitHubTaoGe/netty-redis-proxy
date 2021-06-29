package com.netty.bean;

/**
 * Created by ranjt on 2021/6/1.
 * 封装响应
 */
public class Response<T> {
    private long id;//请求ID
    private int status;//响应状态
    private T content;//响应内容
    private String msg;//请求返回信息

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}