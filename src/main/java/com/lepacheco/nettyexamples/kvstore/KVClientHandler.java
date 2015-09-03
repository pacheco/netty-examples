package com.lepacheco.nettyexamples.kvstore;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by pacheco on 9/3/15.
 */
public class KVClientHandler extends ChannelInboundHandlerAdapter {
    private int reqId = 0;
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("PUT(foo, bar)");
        ctx.writeAndFlush(new KVMessage(reqId++, KVMessage.PUT, "foo".getBytes(), "bar".getBytes()));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object in) throws Exception {
        KVMessage msg = (KVMessage) in;
        if (msg.reqId == 0) { // reply for put(foo, bar)
            assert msg.type == KVMessage.REPLY_OK;
            System.out.println("PUT OK!");
            // do a get(foo)
            ctx.writeAndFlush(new KVMessage(reqId++, KVMessage.GET, "foo".getBytes(), new byte[0]));
        } else if (msg.reqId == 1) { // reply for get(foo)
            assert msg.type == KVMessage.REPLY_OK;
            System.out.println("GET(foo) => " + new String(msg.value));
            // do a get(foo2)
            ctx.writeAndFlush(new KVMessage(reqId++, KVMessage.GET, "foo2".getBytes(), new byte[0]));
        } else if (msg.reqId == 2) { // reply for get(foo2)
            assert msg.type == KVMessage.REPLY_NOK;
            System.out.println("GET(foo2) => key not present" + new String(msg.value));
            ctx.close();
        } else {
            System.err.println("Unexpected message!");
            ctx.close();
        }
    }
}
