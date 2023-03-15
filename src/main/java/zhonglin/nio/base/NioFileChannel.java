package zhonglin.nio.base;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NioFileChannel {
    public static void main(String[] args) throws IOException {
        String str = "test";
        // 创建一个io输出流 -> channel
        FileOutputStream fileOutputStream = new FileOutputStream("test.txt");
        // 通过FileOutputStream.getChannel获取对应的FileChannel接口的实现类FileChannelImpl
        FileChannel fileChannel = fileOutputStream.getChannel();
        // 创建一个缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        // buffer.position归零
        byteBuffer.put(str.getBytes());
        byteBuffer.flip();
        fileChannel.write(byteBuffer);
        // 关闭io输出流
        fileOutputStream.close();
    }
}
