package netty.handler;


import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.netty.api.RedisService;
import com.netty.bean.Response;
import com.netty.bean.ServerRequest;
import com.netty.command.Command;
import com.netty.msg.RedisMsg;
import netty.utils.SpringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

//处理整个注册中心的业务逻辑
public class RedisHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (msg instanceof ByteBuf) {
            ByteBuf req = (ByteBuf) msg;
            String content = req.toString(Charset.defaultCharset());
            System.out.println("Redis服务端开始读取客户端的请求数据:" + content);

            //获取客户端的请求信息
            ServerRequest<RedisMsg> request = JSONObject.parseObject(content, new TypeReference<ServerRequest<RedisMsg>>() {
            });

            Command command = Command.codeOf(request.getCommand());

            RedisService redisService = SpringUtils.getBean(RedisService.class);

            List<Object> objects = new LinkedList<>();
            RedisMsg redisMsg = request.getContent();

            objects.add(redisMsg.getKey());

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
            Response res = new Response();
            res.setId(request.getId());
            res.setContent(invoke);
            //先写入
            ctx.channel().write(JSONObject.toJSONString(res));
            //再一起刷新
            ctx.channel().writeAndFlush("\r\n");
            System.out.println("      ");
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
