package com.lepacheco.nettyexamples;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Created by pacheco on 8/31/15.
 */
public class EchoServerHandler extends ChannelInboundHandlerAdapter {
    public static int LINE_MAX = 1024;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object linebuf) throws Exception {
        byte[] bytes = new byte[((ByteBuf) linebuf).readableBytes()];

        String line = ((ByteBuf) linebuf).toString(Charset.defaultCharset());
        line = line.trim();
        if (line.length() == 0) {
            ((ByteBuf) linebuf).release();
            ctx.channel().close();
        } else {
            System.out.println("Got msg: " + line);
            // reuse the ByteBuf, no need to release as it is released on writing to wire
            ctx.write(linebuf);
            // force write
            ctx.flush();
        }
    }
}
