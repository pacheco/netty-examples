package com.lepacheco.nettyexamples.frame;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

/**
 * Echo server that echoes back whatever "messages" it receives. The server expects length prepended messages
 * Created by pacheco on 8/31/15.
 */
public class FrameEchoServer {
    public int port;

    public FrameEchoServer(int port) {
        this.port = port;
    }

    public void run() throws InterruptedException {
        EventLoopGroup serverGroup = new NioEventLoopGroup();
        EventLoopGroup childrenGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(serverGroup, childrenGroup)
                .channel(NioServerSocketChannel.class) // TCP server socket
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(
                                new LengthFieldBasedFrameDecoder(1024, 0, 4, 0, 4),
                                new LengthFieldPrepender(4),
                                new FrameEchoServerHandler());
                    }
                });

        ChannelFuture f = bootstrap.bind(port).sync();
        f.channel().closeFuture().sync(); // block until the server channel is closed
    }

    public static void main(String[] args) throws InterruptedException {
        if (args.length < 1) {
            System.err.println("usage: FrameEcho <port>");
            System.exit(1);
        }
        int port = Integer.parseInt(args[0]);
        FrameEchoServer server = new FrameEchoServer(port);
        System.out.println("Starting FrameEcho server...");
        server.run();
    }
}
