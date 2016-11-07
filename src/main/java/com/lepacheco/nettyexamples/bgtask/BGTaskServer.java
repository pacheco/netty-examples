package com.lepacheco.nettyexamples.bgtask;

import com.lepacheco.nettyexamples.echo.EchoServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * The server waits for a "task" from clients: a number of milliseconds to sleep. The server replies after that time.
 * The "sleeping" is done in another event executor with 2 threads, so there can be at most 2 tasks running concurrently
 * (following concurrent tasks have to wait for some previous one to finish).
 * Created by pacheco on 8/31/15.
 */
public class BGTaskServer {
    public int port;

    public BGTaskServer(int port) {
        this.port = port;
    }

    public void run() throws InterruptedException {
        final EventExecutorGroup bgTaskGroup = new DefaultEventExecutorGroup(2);
        EventLoopGroup serverGroup = new NioEventLoopGroup();
        EventLoopGroup childrenGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(serverGroup, childrenGroup)
                .channel(NioServerSocketChannel.class) // TCP server socket
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(
                                new LineBasedFrameDecoder(20, true, true),
                                new StringDecoder(),
                                new StringEncoder(),
                                new BGTaskServerHandler(bgTaskGroup));
                    }
                });

        ChannelFuture f = bootstrap.bind(port).sync();
        f.channel().closeFuture().sync(); // block until the server channel is closed
    }

    public static void main(String[] args) throws InterruptedException {
        if (args.length < 1) {
            System.err.println("usage: BGTaskServer <port>");
            System.exit(1);
        }
        int port = Integer.parseInt(args[0]);
        BGTaskServer server = new BGTaskServer(port);
        System.out.println("Starting BGTask server...");
        server.run();
    }
}
