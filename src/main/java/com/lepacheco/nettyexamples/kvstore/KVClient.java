package com.lepacheco.nettyexamples.kvstore;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;

/**
 * Created by pacheco on 9/3/15.
 */
public class KVClient {
    private final String host;
    private final int port;
    private EventLoopGroup evGroup;

    public KVClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() throws InterruptedException {
        evGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap()
                .remoteAddress(host, port)
                .group(evGroup)
                .channel(NioSocketChannel.class)
                .handler(new KVClientInitializer());
        ChannelFuture f = bootstrap.connect().sync();
        f.channel().closeFuture().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                evGroup.shutdownGracefully();
            }
        });
    }

    public static void main(String[] args) throws InterruptedException {
        if (args.length < 2) {
            System.err.println("usage: KVClient <host> <port>");
            System.exit(1);
        }

        KVClient c = new KVClient(args[0], Integer.parseInt(args[1]));
        c.run();
    }
}
