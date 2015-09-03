package com.lepacheco.nettyexamples.kvstore;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by pacheco on 9/3/15.
 */
public class KVServerHandler extends ChannelInboundHandlerAdapter {
    private final ConcurrentHashMap<ByteBuffer, byte[]> map;

    public KVServerHandler(ConcurrentHashMap<ByteBuffer, byte[]> map) {
        this.map = map;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("New client connected!");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object in) throws Exception {
        KVMessage msg = (KVMessage) in;
        ByteBuffer key = ByteBuffer.wrap(msg.key);
        switch (msg.type) {
            case KVMessage.GET:
                byte[] value = map.get(key);
                System.out.println("GET(" + new String(key.array()) + ")");
                if (value == null) {
                    ctx.writeAndFlush(new KVMessage(msg.reqId, KVMessage.REPLY_NOK, msg.key, new byte[0]));
                } else {
                    ctx.writeAndFlush(new KVMessage(msg.reqId, KVMessage.REPLY_OK, msg.key, value));
                }
                break;
            case KVMessage.PUT:
                map.put(key, msg.value);
                System.out.println("PUT(" + new String(key.array()) + ", " + new String(msg.value) + ")");
                ctx.writeAndFlush(new KVMessage(msg.reqId, KVMessage.REPLY_OK, msg.key, new byte[0]));
                break;
            case KVMessage.DELETE:
                map.remove(key);
                System.out.println("DELETE(" + new String(key.array()) + ")");
                ctx.writeAndFlush(new KVMessage(msg.reqId, KVMessage.REPLY_OK, msg.key, new byte[0]));
                break;
            default:
                System.err.println("Unknown request type");
                ctx.close();
                return;
        }
    }
}
