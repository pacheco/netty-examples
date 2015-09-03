package com.lepacheco.nettyexamples.kvstore;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by pacheco on 9/3/15.
 */
public class KVMessageEncoder extends MessageToByteEncoder {
    @Override
    protected void encode(ChannelHandlerContext ctx, Object in, ByteBuf out) throws Exception {
        KVMessage msg = (KVMessage) in;
        out.writeLong(msg.reqId);
        out.writeByte(msg.type);
        out.writeInt(msg.key.length);
        out.writeBytes(msg.key);
        out.writeInt(msg.value.length);
        out.writeBytes(msg.value);
    }
}
