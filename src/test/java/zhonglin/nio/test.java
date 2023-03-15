package zhonglin.nio;

import io.netty.util.NettyRuntime;
import org.junit.Test;

public class test {
    @Test
    public void testCPU(){
        System.out.println(NettyRuntime.availableProcessors());
    }
}
