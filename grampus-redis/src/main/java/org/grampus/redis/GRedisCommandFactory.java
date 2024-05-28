package org.grampus.redis;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.reactive.RedisReactiveCommands;
import io.lettuce.core.api.sync.RedisCommands;

import java.util.HashMap;
import java.util.Map;

public class GRedisCommandFactory {
   private static GRedisCommandFactory instance = new GRedisCommandFactory();
    private Map<String, RedisClient> redisClients = new HashMap<>();
    private Map<RedisClient, StatefulRedisConnection> connectionMap = new HashMap<>();

    private GRedisCommandFactory() {}

    public static GRedisCommandFactory instance() {
        return instance;
    }

    public RedisAsyncCommands getAsyncCommand(String redisUrl) {
        if (redisClients.containsKey(redisUrl)) {
            RedisClient redisClient = redisClients.get(redisUrl);
            return connectionMap.get(redisClient).async();
        } else {
            RedisClient redisClient = RedisClient.create(redisUrl);
            StatefulRedisConnection connection = redisClient.connect();
            connectionMap.put(redisClient, connection);
            redisClients.put(redisUrl, redisClient);
            return connection.async();
        }
    }

    public RedisCommands getSyncCommand(String redisUrl) {
        if (redisClients.containsKey(redisUrl)) {
            RedisClient redisClient = redisClients.get(redisUrl);
            return connectionMap.get(redisClient).sync();
        } else {
            RedisClient redisClient = RedisClient.create(redisUrl);
            StatefulRedisConnection connection = redisClient.connect();
            connectionMap.put(redisClient, connection);
            redisClients.put(redisUrl, redisClient);
            return connection.sync();
        }
    }

    public RedisReactiveCommands getReactiveCommand(String redisUrl) {
        if (redisClients.containsKey(redisUrl)) {
            RedisClient redisClient = redisClients.get(redisUrl);
            return connectionMap.get(redisClient).reactive();
        } else {
            RedisClient redisClient = RedisClient.create(redisUrl);
            StatefulRedisConnection connection = redisClient.connect();
            connectionMap.put(redisClient, connection);
            redisClients.put(redisUrl, redisClient);
            return connection.reactive();
        }
    }

    void close(){
        connectionMap.values().forEach(StatefulRedisConnection::close);
        redisClients.values().forEach(RedisClient::close);
        connectionMap.clear();
        redisClients.clear();
    }
}
