package zhonglin.nio.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class NettyServer {
    public static void main(String[] args) throws InterruptedException {
        /*
            创建两个线程组BossGroup和WorkerGroup
            BossGroup处理连接请求 WorkerGroup处理业务 两个都是无限循环
            BossGroup和WorkerGroup含有的子线程个数默认是 实际cpu核数*2
         */
        EventLoopGroup bossGroup = new NioEventLoopGroup(1); // 设置bossGroup子线程个数为1
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // 创建服务器启动对象ServerBootstrap，配置参数
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // 使用NioSocketChannel作为服务器通道实现
                    .option(ChannelOption.SO_BACKLOG, 128) // 设置线程队列得到连接个数
                    .handler(new LoggingHandler(LogLevel.INFO)) // .handler对应给bossGroup设置日志处理器，.childHandler对应workerGroup
                    .childOption(ChannelOption.SO_KEEPALIVE, true) // 设置保持活动连接状态，给接收到的通道进行配置
                    .childHandler(new ChannelInitializer<SocketChannel>() { // 创建一个通道初始化对象（匿名对象）
                        // 给pipeline设置处理器
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //ch.pipeline().addLast(new NettyServerHandler()); // 自定义NettyServerHandler
                            /* 加入一个netty提供的IdleStateHandler 处理空闲状态的处理器
                            long readerIdleTime 表示多长时间没有读，就会发送一个心跳检测包检测是否连接
                            long writerIdleTime 表示多长时间没有写，就会发送一个心跳检测包检测是否连接
                            long allIdleTime 表示多长时间没有读也没有写，就会发送一个心跳检测包检测是否连接
                            TimeUnit unit 时间单位
                            当IdleStateEvent触发后，就会传递给管道的下一个handler处理，调用其中的userEventTriggered方法处理

                            */
                            ch.pipeline().addLast(new IdleStateHandler(3, 5, 7, TimeUnit.SECONDS));
                            // 加入一个对空闲检测进一步处理的handler（自定义）
                            ch.pipeline().addLast(new NettyServerHandler());
                        }
                    });// 给workerGroup的EventLoop对应的管道设置处理器
            System.out.println("...Server is ready...");
            // 绑定一个端口，生成一个ChannelFuture对象
            // sync()等待异步操作完成
            ChannelFuture cf = bootstrap.bind(6666).sync();
            // 给ChannelFuture对象注册监听器，监听关心事件
            cf.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if(cf.isSuccess()){
                        System.out.println("post is in listening");
                    }
                    else{
                        System.out.println("listening in port error!");
                    }
                }
            });
            // 对关闭通道进行监听
            cf.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
