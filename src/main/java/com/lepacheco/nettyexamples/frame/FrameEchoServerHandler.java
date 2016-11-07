package com.lepacheco.nettyexamples.frame;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;

/**
 * Created by pacheco on 8/31/15.
 */
public class FrameEchoServerHandler extends ChannelInboundHandlerAdapter {
    public static int LINE_MAX = 1024;

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client disconnected.");
        super.channelUnregistered(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object linebuf) throws Exception {
        byte[] bytes = new byte[((ByteBuf) linebuf).readableBytes()];

//        String line = ((ByteBuf) linebuf).toString(Charset.defaultCharset());
//        line = line.trim();
//        System.out.println("Got msg of size " + line.length() + ": " + line);
        // reuse the ByteBuf, no need to release as it is released on writing to wire
        ctx.write(linebuf);
        // force write
        ctx.flush();
    }
}
