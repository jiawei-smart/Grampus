package org.grampus.redis;

import io.lettuce.core.api.sync.RedisCommands;
import org.grampus.core.annotation.plugin.GPlugin;
import org.grampus.core.plugin.GAsyncResult;
import org.grampus.core.plugin.GPluginCell;
import org.grampus.core.plugin.GPromise;

@GPlugin(event = GRedisConstant.REDIS_EVENT)
public class GRedisCell extends GPluginCell<Object> implements GRedisApi {
    private RedisCommands syncCommands;
    @Override
    public void start() {
        onStatus("redis start",false);
        GRedisOptions options = getConfig(GRedisOptions.class);
        GRedisCommandFactory redisFactory = GRedisCommandFactory.instance();
        if(options != null){
            syncCommands = redisFactory.getSyncCommand(options.getRedisUrl());
        }
        onStatus("redis start",true);
    }

    @Override
    public void get(String key, GPromise<String> promise) {
        promise.handle(GAsyncResult.resultAs(syncCommands.get(key)));
    }

    @Override
    public void set(String key, String value) {
        syncCommands.set(key, value);
    }

    @Override
    public void set(String key, String value, GPromise<Boolean> promise) {
        promise.handle(GAsyncResult.resultAs(syncCommands.set(key,value)));
    }

    @Override
    public void del(String key) {
        syncCommands.del(key);
    }

    @Override
    public void del(String key, GPromise<Boolean> promise) {
        promise.handle(GAsyncResult.resultAs(syncCommands.del(key)));
    }

    @Override
    public void expire(String key, int seconds) {
        syncCommands.expire(key,seconds);
    }

    @Override
    public void exists(String key, GPromise<String> promise) {
        promise.handle(GAsyncResult.resultAs(syncCommands.exists(key)));
    }

    @Override
    public void hset(String key, String field, String value) {
        syncCommands.hset(key,field,value);
    }

    @Override
    public void hget(String key, String field, GPromise<String> promise) {
        promise.handle(GAsyncResult.resultAs(syncCommands.hget(key,field)));
    }

    @Override
    public void hdel(String key, String field, GPromise<String> promise) {
        promise.handle(GAsyncResult.resultAs(syncCommands.hdel(key,field)));
    }

    @Override
    public void hexists(String key, String field, GPromise<String> promise) {
        promise.handle(GAsyncResult.resultAs(syncCommands.hexists(key,field)));
    }

    @Override
    public void sismember(String key, String values, GPromise<Boolean> promise) {
        promise.handle(GAsyncResult.resultAs(syncCommands.sismember(key,values)));
    }

    @Override
    public void close(){
        GRedisCommandFactory.instance().close();
    }
}
