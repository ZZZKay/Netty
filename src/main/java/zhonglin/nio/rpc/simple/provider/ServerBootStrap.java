package zhonglin.nio.rpc.simple.provider;

import zhonglin.nio.rpc.simple.netty.NettyServer;

public class ServerBootStrap {
    public static void main(String[] args) {
        NettyServer.startServer("127.0.0.1", 6666);
    }
}
