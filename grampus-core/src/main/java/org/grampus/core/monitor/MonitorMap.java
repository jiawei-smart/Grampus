package org.grampus.core.monitor;

import java.util.concurrent.ConcurrentHashMap;

public class MonitorMap {
    private ConcurrentHashMap<String, Object> monitorMap = new ConcurrentHashMap<String, Object>();

    public void put(String key, Object value){
        this.monitorMap.put(key, value);
    }
}
