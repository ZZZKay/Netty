package zhonglin.nio.base;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NioServer {
    public static void main(String[] args) throws IOException {
        // 创建ServerSocketChannel对象
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 创建Selector对象
        Selector selector = Selector.open();
        // 服务端绑定监听端口，设置为非阻塞状态
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));
        serverSocketChannel.configureBlocking(false);
        // 将ServerSocketChannel对象注册到selector中，并设置关心事件为客户端建立连接
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while(true){
            if(selector.select(1000) == 0){
                System.out.println("服务器等待了1s，无事件发生");
                continue;
            }
            // 返回关心事件的集合，通过selectionKeys反向获取通道
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
            while(keyIterator.hasNext()){
                SelectionKey key = keyIterator.next();
                if(key.isAcceptable()){
                    // 给该客户端生成一个SocketChannel
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    // 设置成非阻塞
                    socketChannel.configureBlocking(false);
                    System.out.println("客户端连接成功，生成了一个SocketChannel" + socketChannel.hashCode());
                    // 将当前socketChannel注册到selector，设置关心事件，关联一个buffer
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }
                if(key.isReadable()){
                    SocketChannel channel = (SocketChannel) key.channel();
                    ByteBuffer byteBuffer = (ByteBuffer) key.attachment();
                    channel.read(byteBuffer);
                    System.out.println("来自客户端：" + new String(byteBuffer.array()));
                }
                // 手动移除关心事件的集合，防止重复操作
                keyIterator.remove();
            }
        }
    }
}
