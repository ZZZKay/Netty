package zhonglin.nio.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    // 当通道就绪就会触发该方法
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client" + ctx);
        ctx.writeAndFlush(Unpooled.copiedBuffer("Hello, Serevr!", CharsetUtil.UTF_8));
    }
    // 当通道有读取事件时会触发该方法
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("Message from Server: " + buf.toString(CharsetUtil.UTF_8));
        System.out.println("Server address: " + ctx.channel().remoteAddress());
    }
    /*
        处理异常，一般需要关闭通道
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //ctx.channel().close();
        ctx.close();
    }
}
