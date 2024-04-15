package org.grampus.redis;

import org.grampus.core.plugin.GPluginCell;

public class GRedisCell<T> extends GPluginCell<T> {
    private GRedisClient client;
    @Override
    public void start() {
        GRedisOptions config = getConfig(GRedisOptions.class);
        if(config != null){
            client = new GRedisClient();
            client.start(config);
        }
    }
}
