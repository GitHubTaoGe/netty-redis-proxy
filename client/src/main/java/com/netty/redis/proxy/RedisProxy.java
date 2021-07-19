package com.netty.redis.proxy;

import com.netty.api.Redis;
import com.netty.api.RedisService;
import com.netty.bean.ClientRequest;
import com.netty.bean.Response;
import com.netty.client.TcpNettyClient;
import com.netty.command.Command;
import com.netty.msg.RedisMsg;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class RedisProxy {
    public static <T> T create(String clientId, Class<?> clazz) {

        MethodProxy methodProxy = new MethodProxy(clientId, clazz);

        T result = (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, methodProxy);

        return result;
    }
}


class MethodProxy implements InvocationHandler, RedisService {


    private Class<?> clazz;

    private String clientId;

    public MethodProxy(String clientId, Class<?> clazz) {
        this.clientId = clientId;
        this.clazz = clazz;
    }


    //代理，调用IRpcHello接口中每一个方法的时候，实际上就是发起一次网络请求


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //如果传进来的是一个已经实现的具体的类（直接忽略）
        return method.invoke(this, args);


//        if (Object.class.equals(method.getDeclaringClass())) {
//            return method.invoke(this, args);
//        }
//
//        //如果传进来的是一个接口，我们就走远程调用
//        else {
//            return rpcInvoke(method, args);
//        }

    }


    public Object rpcInvoke(ClientRequest clientRequest) {
        clientRequest.setClientId(this.clientId);

        Response<RedisMsg> response = TcpNettyClient.send(clientRequest);

        RedisMsg content = response.getContent();

        return content.getData();

    }

    @Override
    public Object get(Object key) {
        ClientRequest clientRequest = new ClientRequest();

        clientRequest.setCommand(Command.GET.getCommand());

        RedisMsg redisMsg = new RedisMsg();

        redisMsg.setKey(key);

        clientRequest.setContent(redisMsg);

        return rpcInvoke(clientRequest);
    }

    @Override
    public boolean del(Object key) {
        ClientRequest clientRequest = new ClientRequest();

        clientRequest.setCommand(Command.DEL.getCommand());

        RedisMsg redisMsg = new RedisMsg();

        redisMsg.setKey(key);

        clientRequest.setContent(redisMsg);

        return (boolean) rpcInvoke(clientRequest);
    }

    @Override
    public boolean set(Object key, Object value) {
        ClientRequest clientRequest = new ClientRequest();

        clientRequest.setCommand(Command.SET.getCommand());

        RedisMsg redisMsg = new RedisMsg();

        redisMsg.setKey(key);
        redisMsg.setData(value);
        redisMsg.setType(value.getClass().getTypeName());

        clientRequest.setContent(redisMsg);

        return (boolean) rpcInvoke(clientRequest);
    }

    @Override
    public boolean set(Object key, Object value, int milliseconds) {
        ClientRequest clientRequest = new ClientRequest();

        clientRequest.setCommand(Command.SETEX.getCommand());

        RedisMsg redisMsg = new RedisMsg();

        redisMsg.setKey(key);
        redisMsg.setData(value);
        redisMsg.setMillisecond(milliseconds);
        redisMsg.setType(value.getClass().getTypeName());

        clientRequest.setContent(redisMsg);

        return (boolean) rpcInvoke(clientRequest);
    }
}