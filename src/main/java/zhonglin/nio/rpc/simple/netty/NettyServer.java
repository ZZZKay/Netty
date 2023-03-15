package zhonglin.nio.rpc.simple.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class NettyServer {
    // 对外暴露的启动方式
    public static void startServer(String hostName, int port){
        startServer0(hostName, port);
    }
    // 初始化和启动一个NettyServer
    private static void startServer0(String hostName, int port){
        EventLoopGroup bossGroup = new NioEventLoopGroup(1); // 设置bossGroup子线程个数为1
        EventLoopGroup workerGroup = new NioEventLoopGroup(8);
        try {
            ServerBootstrap serverbootstrap = new ServerBootstrap();
            serverbootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new StringDecoder());
                            pipeline.addLast(new StringEncoder());
                            pipeline.addLast(new NettyServerHandler()); // 自定义业务处理器
                        }
                    });

            ChannelFuture channelFuture = serverbootstrap.bind(hostName, port).sync();
            System.out.println("...Server is ready...");
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
