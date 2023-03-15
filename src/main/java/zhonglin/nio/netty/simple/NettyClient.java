package zhonglin.nio.netty.simple;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {
    public static void main(String[] args) throws InterruptedException {
        // 客户端的事件循环组
        EventLoopGroup eventExecutors = new NioEventLoopGroup();
        //创建客户端启动对象Bootstrap，不是ServerBootstrap
        try {
            Bootstrap bootstrap = new Bootstrap();
            // 设置参数
            bootstrap.group(eventExecutors) // 设置线程组
                    .channel(NioSocketChannel.class) // 设置客户端通道的实现类
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyClientHandler()); // 设置自己的处理器
                        }
                    });
            System.out.println("...Client is ready...");
            ChannelFuture cf = bootstrap.connect("127.0.0.1", 6666).sync();
            // 对关闭通道进行监听
            cf.channel().closeFuture().sync();
        } finally {
            eventExecutors.shutdownGracefully();
        }
    }
}
