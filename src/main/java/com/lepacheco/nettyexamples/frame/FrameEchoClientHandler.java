package com.lepacheco.nettyexamples.frame;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by pacheco on 9/1/15.
 */
public class FrameEchoClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Sending: Hello world!");
        ctx.writeAndFlush("Hello world!");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("Got reply: " + msg.toString().trim());
        ctx.disconnect();
    }
}
