package org.grampus.redis;

import org.grampus.core.annotation.plugin.GPluginApi;
import org.grampus.core.plugin.GPromise;

@GPluginApi(event = GRedisConstant.REDIS_EVENT)
public interface GRedisApi {
    void get(String key, GPromise<String> promise);
    void set(String key, String value);
    void set(String key, String value, GPromise<Boolean> promise);
    void del(String key);
    void del(String key, GPromise<Boolean> promise);
    void expire(String key, int seconds);
    void exists(String key, GPromise<String> promise);
    void hset(String key, String field, String value);
    void hget(String key, String field, GPromise<String> promise);
    void hdel(String key, String field, GPromise<String> promise);
    void hexists(String key, String field, GPromise<String> promise);
    void sismember(String key, String values, GPromise<Boolean> promise);
}
