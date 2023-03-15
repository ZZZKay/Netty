package zhonglin.nio.rpc.simple.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import zhonglin.nio.rpc.simple.provider.HelloServiceImpl;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 获取客户端发送的消息并调用相应服务
        System.out.println("收到的消息: "+ msg);
        // 传过来的消息必须满足预先约定好的协议，才能调用相应服务
        // 比如必须以某个字符串开头
        if(msg.toString().startsWith("HelloServer##")){
            String result = new HelloServiceImpl().hello(msg.toString().substring(msg.toString()
                    .lastIndexOf("#") + 1));
            ctx.writeAndFlush(result);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
