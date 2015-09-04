package com.lepacheco.nettyexamples.echo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by pacheco on 9/1/15.
 */
public class EchoClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("Got reply: " + msg.toString().trim());
        ctx.disconnect();
    }
}
