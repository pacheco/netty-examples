package com.lepacheco.nettyexamples;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created by pacheco on 8/31/15.
 */
public class DiscardServer {
    public int port;

    public DiscardServer(int port) {
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
                        socketChannel.pipeline().addLast(new DiscardServerHandler());
                    }
                })
                // Channel options for the server socket and children can be set
                .option(ChannelOption.SO_RCVBUF, 128*1024)
                .childOption(ChannelOption.TCP_NODELAY, true);

        ChannelFuture f = bootstrap.bind(port).sync();
        f.channel().closeFuture().sync(); // block until the server channel is closed
    }

    public static void main(String[] args) throws InterruptedException {
        if (args.length < 1) {
            System.err.println("usage: DiscardServer <port>");
            System.exit(1);
        }
        int port = Integer.parseInt(args[0]);
        DiscardServer server = new DiscardServer(port);
        System.out.println("Starting discard server...");
        server.run();
    }
}
