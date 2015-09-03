package com.lepacheco.nettyexamples.kvstore;

/**
 * Created by pacheco on 9/3/15.
 */
public class KVMessage {
    public static final byte PUT = 1;
    public static final byte GET = 2;
    public static final byte DELETE = 3;
    public static final byte REPLY_OK = 4;
    public static final byte REPLY_NOK = 4;
    public final long reqId;
    public final byte type;
    public final byte[] key;
    public final byte[] value;

    public KVMessage(final long reqId, final byte type, final byte[] key, final byte[] value) {
        this.reqId = reqId;
        this.type = type;
        this.key = key;
        this.value = value;
    }
}
