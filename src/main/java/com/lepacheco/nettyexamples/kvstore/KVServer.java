package com.lepacheco.nettyexamples.kvstore;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;

import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple KV store uses ByteToMessageDecoded and MessageToByteEncoder
 * Created by pacheco on 9/3/15.
 */
public class KVServer {
    private final ConcurrentHashMap<ByteBuffer, byte[]> map;
    private final int port;
    private EventLoopGroup evGroup;

    public KVServer(int port) {
        this.port = port;
        this.map = new ConcurrentHashMap<>();
    }

    public void run() throws InterruptedException {
        evGroup = new EpollEventLoopGroup(); // Epoll is linux only
        ServerBootstrap bootstrap = new ServerBootstrap()
                .channel(EpollServerSocketChannel.class)
                .group(evGroup) // use same evgroup for child and parent
                .childHandler(new KVServerInitializer(map));
        ChannelFuture f = bootstrap.bind(port).sync();
        f.channel().closeFuture().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                evGroup.shutdownGracefully();
            }
        }).sync();
    }

    public static void main(String[] args) throws InterruptedException {
        if (args.length < 1) {
            System.err.println("usage: KVServer <port>");
            System.exit(1);
        }
        int port = Integer.parseInt(args[0]);
        KVServer server = new KVServer(port);
        System.out.println("Starting kv server...");
        server.run();
    }
}
