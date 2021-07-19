package com.netty.handler;


import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.netty.api.Redis;
import com.netty.api.RedisService;
import com.netty.bean.Response;
import com.netty.bean.ServerRequest;
import com.netty.command.Command;
import com.netty.command.Constant;
import com.netty.msg.RedisMsg;
import com.netty.utils.SpringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

//处理整个注册中心的业务逻辑
@Slf4j
public class RedisHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (msg instanceof ByteBuf) {
            ByteBuf req = (ByteBuf) msg;
            String content = req.toString(Charset.defaultCharset());
            log.info("Redis服务端开始读取客户端的请求数据:{}", content);
            //获取客户端的请求信息
            ServerRequest<RedisMsg> request = JSONObject.parseObject(content, new TypeReference<ServerRequest<RedisMsg>>() {
            });

            Command command = Command.codeOf(request.getCommand());

            Redis redisService = SpringUtils.getBean(Redis.class);

            List<Object> objects = new LinkedList<>();
            RedisMsg redisMsg = request.getContent();

            StringBuilder key = new StringBuilder();

            key.append(request.getClientId()).append(Constant.REDIS_SEQ).append(redisMsg.getKey());

            objects.add(key.toString());

            if (!StringUtils.isEmpty(redisMsg.getData())) {
                objects.add(redisMsg);
            }
            if (!StringUtils.isEmpty(redisMsg.getMillisecond())) {
                objects.add(redisMsg.getMillisecond());
            }
            Class[] argsClass = new Class[objects.size()];

            for (int i = 0, j = objects.size(); i < j; i++) {
                argsClass[i] = Object.class;
            }
            if (objects.size() == 3) {
                argsClass[objects.size() - 1] = Integer.TYPE;
            }

            Method m = redisService.getClass().getMethod(command.getCommand(), argsClass);

            Object invoke = m.invoke(redisService, objects.toArray());

            //写入解析请求之后结果对应的响应信息
            Response response = new Response();

            response.setId(request.getId());

            if (invoke instanceof Boolean) {
                RedisMsg res = new RedisMsg();
                res.setData(invoke);
                response.setContent(res);

            } else if (invoke instanceof RedisMsg) {

                response.setContent(invoke);
            }

            String result = JSONObject.toJSONString(response);
            log.info("Redis服务端向客户端响应请求数据:{}", result);
            //先写入
            ctx.channel().write(result);
            //再一起刷新
            ctx.channel().writeAndFlush("\r\n");
        }
    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
            throws Exception {

        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.equals(IdleState.READER_IDLE)) {
                System.out.println("读空闲====");
                ctx.close();
            } else if (event.equals(IdleState.WRITER_IDLE)) {
                System.out.println("写空闲====");
            } else if (event.equals(IdleState.WRITER_IDLE)) {
                System.out.println("读写空闲====");
                ctx.channel().writeAndFlush("ping\r\n");
            }

        }

        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
