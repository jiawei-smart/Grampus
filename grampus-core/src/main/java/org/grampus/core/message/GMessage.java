package org.grampus.core.message;

import java.util.HashMap;
import java.util.Map;

public class GMessage<T> {
    private final GMessageHeader header;
    private final Map<String, Object> meta = new HashMap<>();
    private T payload;

    public GMessage() {
        this.header = new GMessageHeader();
    }

    public GMessageHeader getHeader() {
        return header;
    }

    public void meta(String key, Object value){
        this.meta.put(key, value);
    }

    public void meta(Map<String, Object> meta){
        this.meta.putAll(meta);
    }

    public Object meta(String key){
        return this.meta.get(key);
    }

    public Map<String, Object> meta(){
        return this.meta;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    public GMessage clone(){
        return this;
    }
}
