package com.lepacheco.nettyexamples.frame;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;

/**
 * Created by pacheco on 8/31/15.
 */
public class FrameEchoClient {
    public String host;
    public int port;

    public FrameEchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void send(String msg) throws InterruptedException {
        EventLoopGroup eventGroup = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventGroup)
                .remoteAddress(host, port)
                .channel(NioSocketChannel.class) // TCP server socket
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(
                                // inbound
                                new LengthFieldBasedFrameDecoder(1024, 0, 4, 0, 4),
                                new StringDecoder(CharsetUtil.UTF_8),
                                // outbound
                                new LengthFieldPrepender(4),
                                new StringEncoder(CharsetUtil.UTF_8),
                                // app
                                new FrameEchoClientHandler());
                    }
                });

        ChannelFuture f = bootstrap.connect().sync();
        f.channel().closeFuture().sync();
        eventGroup.shutdownGracefully().sync();
        System.out.println("Done.");
    }

    public static void main(String[] args) throws InterruptedException {
        if (args.length < 2) {
            System.err.println("usage: FrameEchoClient <host> <port>");
            System.exit(1);
        }
        String host = new String(args[0]);
        int port = Integer.parseInt(args[1]);
        FrameEchoClient client = new FrameEchoClient(host, port);
        client.send("Hello world!");
    }
}
