package com.lepacheco.nettyexamples.bgtask;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by pacheco on 9/4/15.
 */
public class BGTaskClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("Got reply: " + msg);
        ctx.disconnect();
    }
}
