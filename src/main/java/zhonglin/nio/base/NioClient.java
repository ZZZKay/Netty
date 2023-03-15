package zhonglin.nio.base;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NioClient {
    public static void main(String[] args) throws IOException {
        // 获取一个网络通道
        SocketChannel socketChannel = SocketChannel.open();
        // 设置为非阻塞状态
        socketChannel.configureBlocking(false);
        // 提供服务器ip和端口
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 6666);
        //连接客户端
        if(!socketChannel.connect(inetSocketAddress)){
            while(!socketChannel.finishConnect()){
                System.out.println("还没有成功连接服务端，客户端可以做其他事");
            }
        }
        // 连接成功后
        String str = "服务端，你好！";
        // ByteBuffer.wrap根据str字节大小放入ByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.wrap(str.getBytes());
        socketChannel.write(byteBuffer);
    }
}
