package com.lepacheco.nettyexamples;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;

/**
 * Created by pacheco on 8/31/15.
 */
public class EchoServer {
    public int port;

    public EchoServer(int port) {
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
                                // break stream into "lines"
                                new LineBasedFrameDecoder(EchoServerHandler.LINE_MAX, false, true),
                                // could use a StringDecoder here, but EchoServerHandler will reuse the ByteBuf
                                new EchoServerHandler());
                    }
                });

        ChannelFuture f = bootstrap.bind(port).sync();
        f.channel().closeFuture().sync(); // block until the server channel is closed
    }

    public static void main(String[] args) throws InterruptedException {
        if (args.length < 1) {
            System.err.println("usage: EchoServer <port>");
            System.exit(1);
        }
        int port = Integer.parseInt(args[0]);
        EchoServer server = new EchoServer(port);
        System.out.println("Starting echo server...");
        server.run();
    }
}
