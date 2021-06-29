package com.netty.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.netty.bean.DefaultFuture;
import com.netty.bean.Response;
import com.netty.msg.RedisMsg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by ranjt on 2021/6/1.
 */
@Slf4j
public class TcpClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        //判断服务端和客户端是在能够正常通信的情况下
        if(msg.toString().equals("ping")){ 
            ctx.channel().writeAndFlush("ping\r\n");
            return ;
        }
        log.debug("Redis客户端获取到服务端响应数据:{}", msg.toString());

        String str = getJSONObject(msg.toString()).toString();

        //读取服务端的响应结果
        Response res = JSONObject.parseObject(str, Response.class);


        Object data = res.getContent();

        if (data == null) {
            DefaultFuture.recive(res);
            return;
        }

        if (isPrimitive(data.getClass())) {
            DefaultFuture.recive(res);
            return;
        }

        try {
            RedisMsg redisMsg = JSONObject.parseObject(JSON.toJSONBytes(data), RedisMsg.class);

            Class<?> responseType = Class.forName(redisMsg.getType());

            Object parseObject = JSONObject.parseObject(JSONObject.toJSONString(redisMsg.getData()), responseType);

            res.setContent(parseObject);
            DefaultFuture.recive(res);
            return;
        } catch (ClassNotFoundException e) {

        }


        //存储响应结果
        DefaultFuture.recive(res);
    }

    private JSONObject getJSONObject(String str){
        JSONObject json = JSONObject.parseObject(str);
//        json.put("msg","保存用户信息成功");
        return json;
    }

    /**判断一个对象是否是基本类型或基本类型的封装类型*/
    private boolean isPrimitive(Class obj) {
        try {
            return ((Class<?>)obj.getField("TYPE").get(null)).isPrimitive();
        } catch (Exception e) {
            return false;
        }
    }
}