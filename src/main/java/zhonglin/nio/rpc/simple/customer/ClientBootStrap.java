package zhonglin.nio.rpc.simple.customer;

import zhonglin.nio.rpc.simple.netty.NettyClient;
import zhonglin.nio.rpc.simple.publicInterface.HelloService;

public class ClientBootStrap {
    public static final String prex = "HelloServer##";
    public static void main(String[] args) {
        NettyClient customer = new NettyClient();
        // 创建代理对象
        HelloService service = (HelloService) customer.getBean(HelloService.class, prex);
        String result = service.hello("test");
        System.out.println("result from Server: " + result);
    }
}
