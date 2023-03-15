package zhonglin.nio.rpc.simple.provider;

import zhonglin.nio.rpc.simple.publicInterface.HelloService;

public class HelloServiceImpl implements HelloService {
    // 当消费者调用这个方法时，返回一个结果
    @Override
    public String hello(String msg) {
        System.out.println("msg from Client: " + msg);
        if (msg != null) {
            return "Hello Client, I have recieved your message [" + msg + "]";
        }
        else{
            return "Hello Client, I have recieved your message, but it's null!";
        }
    }
}
