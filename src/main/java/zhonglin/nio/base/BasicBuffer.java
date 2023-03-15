package zhonglin.nio.base;

import java.nio.IntBuffer;

public class BasicBuffer {
    public static void main(String[] args) {
        // 创建一个buffer，可以存放5个整数
        IntBuffer intBuffer = IntBuffer.allocate(5);
        for(int i=0; i<intBuffer.capacity(); i++){
            intBuffer.put(i);
        }
        // buffer.position归零
        intBuffer.flip();
        // 设置读写位置
        //intBuffer.position(int i);
        // 设置缓冲区读写限制
        //intBuffer.limit(int i);
        while(intBuffer.hasRemaining()){
            System.out.print(intBuffer.get());
        }
        // 清楚缓冲区
        //intBuffer.clear();
    }
}
