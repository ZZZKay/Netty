package zhonglin.nio.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;

import java.nio.ByteBuffer;

/*
    自定义handler需要继承netty中一个handler
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    /**
     *
     * @param ctx 上下文
     * @param evt 事件
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent) evt;
            String eventType = null;
            switch (event.state()){
                case READER_IDLE:
                    eventType = "读空闲";
                    break;
                case WRITER_IDLE:
                    eventType = "写空闲";
                    break;
                case ALL_IDLE:
                    eventType = "读写空闲";
                    break;
            }
            System.out.println(ctx.channel().remoteAddress() + "---发生事件---" + eventType);
        }
    }

    /*
            读取数据为例
            ChannelHandlerContext：上下文对象，含有管道pipeline，通道channle，地址
            Object：客户端发送的数据，默认为Object
         */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("server ctx: " + ctx);
        // 使用Netty提供的ByteBuf，性能比java.nio.ByteBuffer更高
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("client send: " + buf.toString(CharsetUtil.UTF_8));
        System.out.println("client address: " + ctx.channel().remoteAddress());
    }
    /*
        数据读取完毕，返回信息给客户端
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 将数据写入缓存并刷新，一定要刷新不然得不到，对发送的数据需要编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("Hello, Client", CharsetUtil.UTF_8));
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
