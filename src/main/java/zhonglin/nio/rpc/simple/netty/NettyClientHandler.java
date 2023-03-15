package zhonglin.nio.rpc.simple.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

public class NettyClientHandler extends ChannelInboundHandlerAdapter implements Callable {
    private ChannelHandlerContext context; // 上下文信息
    private String result; // 返回结果
    private String para; // 客户端调用服务传入的参数

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.context = ctx;
    }

    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        this.result = msg.toString();
        notify(); // 唤醒等待的线程
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    // 被代理对象调用，发送数据给服务器 -> 等待唤醒(channelRead) -> 返回result
    @Override
    public synchronized Object call() throws Exception {
        this.context.writeAndFlush(para);
        wait();
        return this.result;
    }

    void setPara(String para){
        this.para = para;
    }
}
