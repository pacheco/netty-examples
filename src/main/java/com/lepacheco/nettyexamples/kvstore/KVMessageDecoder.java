package com.lepacheco.nettyexamples.kvstore;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Created by pacheco on 9/3/15.
 */
public class KVMessageDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() > 0) {
            long reqId = in.readLong();
            byte type = in.readByte();
            // read key
            long keyLength = in.readUnsignedInt();
            if (keyLength > 1024 || keyLength == 0) {
                System.err.println("Invalid key length");
                return;
            }
            byte[] key = new byte[(int) keyLength];
            in.readBytes(key);
            // read value
            long valueLength = in.readUnsignedInt();
            byte[] value = new byte[(int) valueLength];
            in.readBytes(value);
            out.add(new KVMessage(reqId, type, key, value));
        }
    }
}
