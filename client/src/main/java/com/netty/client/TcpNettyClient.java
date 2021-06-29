package com.netty.client;

import com.alibaba.fastjson.JSONObject;
import com.netty.bean.ClientRequest;
import com.netty.bean.DefaultFuture;
import com.netty.bean.Response;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Created by ranjt on 2021/6/1.
 */
@Slf4j
@Component
public class TcpNettyClient  {

    private static ChannelFuture future;


    public TcpNettyClient(ChannelFuture future) {
        TcpNettyClient.future = future;
    }

    //发送数据的方法
    public static Object send(ClientRequest request){
        try{
            log.debug("Redis客户端向服务端发送请求数据:{}", JSONObject.toJSONString(request));

            //客户端直接发送请求数据到服务端
            future.channel().writeAndFlush(JSONObject.toJSONString(request));

            //根据\r\n进行换行
            future.channel().writeAndFlush("\r\n");

            //通过请求实例化请求和响应之间的关系
            DefaultFuture defaultFuture = new DefaultFuture(request);

            //通过请求ID，获取对应的响应处理结果
            Response response = defaultFuture.get(10);
            return response.getContent();
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

}