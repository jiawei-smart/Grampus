package org.grampus.redis;

import io.lettuce.core.AbstractRedisClient;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulConnection;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.codec.RedisCodec;
import org.grampus.core.client.GAPIClient;
import org.grampus.log.GLogger;
import org.grampus.util.GStringUtil;

import java.util.List;

public class GRedisClient implements GAPIClient<GRedisOptions> {
    private RedisClient redisClient;
    private  RedisClusterClient redisClusterClient;
    @Override
    public boolean start(GRedisOptions config) {
        if(config.getClusterNodes() == null){
             redisClient = RedisClient.create(config.getHost()+":"+config.getPort());
            return true;
        }else {
             redisClusterClient = RedisClusterClient.create((RedisURI) config.getClusterNodes().stream().map(nodeStr-> createRedisURI(nodeStr)));
            return true;
        }
    }

    private RedisURI createRedisURI(String nodeStr) {
        List<String> nodeInfo = GStringUtil.splitAsList(nodeStr,":");
        return RedisURI.create(nodeInfo.get(0),Integer.valueOf(nodeInfo.get(1)));
    }

    public <K,V> StatefulConnection getConnection(RedisCodec<K, V> codec){
        if(redisClient != null){
            return redisClient.connect(codec);
        }else if(redisClusterClient != null){
            return redisClusterClient.connect(codec);
        }else {
            GLogger.warn("GRedis cannot found any a client, pleasure make it has init");
            return null;
        }
    }

    @Override
    public boolean stop() {
        return false;
    }
}
