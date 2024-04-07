package org.grampus.core.monitor;

import java.util.concurrent.ConcurrentHashMap;

public class GMonitorMap {
    private GMonitor monitor;

    public GMonitorMap(GMonitor monitor) {
        this.monitor = monitor;
    }

    private ConcurrentHashMap<String, Object> monitorMap = new ConcurrentHashMap<String, Object>();

    public void put(String key, Object value){
        this.monitorMap.put(key, value);
    }

    public void update(){
        this.monitor.monitor(this);
    }
}
