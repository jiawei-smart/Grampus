package org.grampus.core.message;

import java.util.HashMap;
import java.util.Map;

public class GMessage<T> {
    public final GMessageHeader header;
    private final Map<String, Object> meta = new HashMap<>();
    private T payload;

    private GMessage() {
        this.header = new GMessageHeader(GMsgType.BUSINESS_MESSAGE);
    }

    private GMessage(GMsgType msgType) {
        this.header = new GMessageHeader(msgType);
    }

    public GMessageHeader header() {
        return header;
    }

    public GMessage meta(String key, Object value){
        this.meta.put(key, value);
        return this;
    }

    public GMessage meta(Map<String, Object> meta){
        if(meta != null){
            this.meta.putAll(meta);
        }
        return this;
    }

    public Object meta(String key){
        return this.meta.get(key);
    }

    public Map<String, Object> meta(){
        return this.meta;
    }

    public T payload() {
        return payload;
    }

    public GMessage setPayload(T payload) {
        this.payload = payload;
        return this;
    }

    public String messageId(){
        return this.header.getId();
    }

    public GMessage clone(){
        return this;
    }

    public static GMessage newHbMessage(){
        return new GMessage<>(GMsgType.HEARTBEAT_MESSAGE);
    }

    public static GMessage newAdminMessage(){
        return new GMessage<>(GMsgType.ADMIN_MESSAGE);
    }
    public static GMessage newBusinessMessage(){
        return new GMessage<>(GMsgType.BUSINESS_MESSAGE);
    }

}
