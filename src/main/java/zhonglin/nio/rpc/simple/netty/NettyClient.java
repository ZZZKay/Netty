package zhonglin.nio.rpc.simple.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NettyClient {
    // 创建线程池
    private static ExecutorService executor =   Executors.newFixedThreadPool(4);
    private static NettyClientHandler clientHandler;
    // 代理模式，获取一个代理对象
    public Object getBean(final Class<?> serviceClass, final String prex){
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class<?>[] {serviceClass}, (proxy, method, args) -> {
                    if(clientHandler == null){
                        initClient();
                    }
                    // prex 是协议头 HelloServer##
                    clientHandler.setPara(prex + args[0]);
                    return executor.submit(clientHandler).get();
                });
    }
    private static void initClient() throws InterruptedException {
        clientHandler = new NettyClientHandler();
        NioEventLoopGroup clientGroup = new NioEventLoopGroup(1);
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(clientGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new StringDecoder());
                        pipeline.addLast(new StringEncoder());
                        pipeline.addLast(clientHandler); // 自定义业务处理器
                    }
                });
        bootstrap.connect("127.0.0.1", 6666).sync();
    }
}
