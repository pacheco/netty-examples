package com.lepacheco.nettyexamples.kvstore;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

/**
 * Created by pacheco on 9/3/15.
 */
public class KVClientInitializer extends ChannelInitializer {
    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline().addLast(
                // inbound
                new LengthFieldBasedFrameDecoder(1024*1024*1024, 0, 4, 0, 4),
                new KVMessageDecoder(),
                // outbound
                new LengthFieldPrepender(4),
                new KVMessageEncoder(),
                // app
                new KVClientHandler()
        );
    }
}
