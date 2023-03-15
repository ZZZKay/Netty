package zhonglin.nio.base;

import java.nio.ByteBuffer;

public class NioBufferPutGet {
    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(32);

        // 类型化数据存入ByteBuffer缓冲区
        byteBuffer.putInt(10);
        byteBuffer.putLong(20);
        //byteBuffer.put("hello".getBytes());

        byteBuffer.flip();

        System.out.println(byteBuffer.getInt());
        System.out.println(byteBuffer.getLong());
        //System.out.println(new String(byteBuffer.array()));
    }
}
