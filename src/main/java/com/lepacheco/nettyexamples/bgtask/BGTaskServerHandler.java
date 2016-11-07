package com.lepacheco.nettyexamples.bgtask;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * Expects to receive an integer (encoded as a string).
 * Submits a task to another executor that will sleep for this amount of milliseconds then reply.
 *
 * Created by pacheco on 9/4/15.
 */
public class BGTaskServerHandler extends ChannelInboundHandlerAdapter {
    private final EventExecutorGroup bgTaskGroup;

    public BGTaskServerHandler(EventExecutorGroup bgTaskGroup) {
        this.bgTaskGroup = bgTaskGroup;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            Integer sleepMillis = Integer.parseInt((String) msg);
            System.out.println("Submitting task (" + sleepMillis + ")");
            bgTaskGroup.submit(new Sleeper(sleepMillis, ctx.channel()));
        } catch (NumberFormatException f) {
            ctx.writeAndFlush("I was expecting an integer!\n");
        }
    }

    /**
     * Sleep for the given milliseconds then reply to the client
     */
    private class Sleeper implements Runnable {
        private final Channel out;
        private long millis;

        private Sleeper(long millis, Channel out) {
            this.millis = millis;
            this.out = out;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(millis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            out.writeAndFlush("Done (" + millis + ")!\n");
        }
    }
}