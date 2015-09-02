package com.lepacheco.nettyexamples.echo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

/**
 * Created by pacheco on 8/31/15.
 */
public class EchoClient {
    public String host;
    public int port;

    public EchoClient(String host, int port) {
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
                                // break stream into "lines"
                                new LineBasedFrameDecoder(EchoServerHandler.LINE_MAX, false, true),
                                new StringDecoder(CharsetUtil.UTF_8),
                                new EchoClientHandler(),
                                new StringEncoder(CharsetUtil.UTF_8));
                    }
                });

        ChannelFuture f = bootstrap.connect().sync();
        System.out.println("Connected!");
        f.channel().writeAndFlush(msg).sync();
        System.out.print("Sent: " + msg);
        f.channel().closeFuture().sync();
        eventGroup.shutdownGracefully().sync();
        System.out.println("Done.");
    }

    public static void main(String[] args) throws InterruptedException {
        if (args.length < 2) {
            System.err.println("usage: EchoClient <host> <port>");
            System.exit(1);
        }
        String host = new String(args[0]);
        int port = Integer.parseInt(args[1]);
        EchoClient client = new EchoClient(host, port);
        client.send("Hello world!\n");
    }
}
