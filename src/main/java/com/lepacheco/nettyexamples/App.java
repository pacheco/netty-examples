package com.lepacheco.nettyexamples;

import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 *
 */
public class App implements Runnable {
    public static void main(String[] args) throws InterruptedException {
        EventExecutorGroup g = new DefaultEventExecutorGroup(2);
        g.submit(new App());
        g.submit(new App());
        g.submit(new App());
        g.shutdownGracefully().sync();
    }

    public void run() {
        try {
            System.out.println("0");
            Thread.sleep(1000);
            System.out.println("1");
            Thread.sleep(1000);
            System.out.println("Done");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}