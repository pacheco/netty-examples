package com.lepacheco.nettyexamples.bgtask;

import com.lepacheco.nettyexamples.echo.EchoClientHandler;
import com.lepacheco.nettyexamples.echo.EchoServerHandler;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Submit a task (sleep for X milliseconds) to the server and wait for a reply.
 * Created by pacheco on 8/31/15.
 */
public class BGTaskClient {
    public String host;
    public int port;

    public BGTaskClient(String host, int port) {
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
                                new LineBasedFrameDecoder(256, true, true),
                                new StringDecoder(CharsetUtil.UTF_8),
                                new StringEncoder(CharsetUtil.UTF_8),
                                new BGTaskClientHandler());
                    }
                });

        ChannelFuture f = bootstrap.connect().sync();
        System.out.println("Connected!");
        f.channel().writeAndFlush(msg + "\n").sync();
        System.out.println("Sent: " + msg);
        f.channel().closeFuture().sync();
        eventGroup.shutdownGracefully().sync();
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        if (args.length < 3) {
            System.err.println("usage: BGTaskClient <host> <port> <sleepMillis>");
            System.exit(1);
        }
        String host = new String(args[0]);
        int port = Integer.parseInt(args[1]);
        String msg = args[2];
        BGTaskClient client = new BGTaskClient(host, port);
//        System.out.print("Task time (ms): ");
//        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
//        String msg = r.readLine();
        client.send(msg);
    }
}
