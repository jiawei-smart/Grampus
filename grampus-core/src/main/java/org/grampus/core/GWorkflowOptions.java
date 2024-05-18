package org.grampus.core;

import org.grampus.log.GLogger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class GWorkflowOptions {
    private Map<String, Object> config;
    private int workerPoolSize = GConstant.DEFAULT_WORKER_POOL_SIZE;
    private int scheduledTheadPoolSize = GConstant.DEFAULT_SCHEDULE_WORKER_POOL_SIZE;
    private long threadCheckInterval = GConstant.DEFAULT_THREAD_CHECK_INTERVAL_MILLS;
    private TimeUnit threadCheckTimeUnit = TimeUnit.MILLISECONDS;
    private long threadProcessTimeout = 25l;
    private TimeUnit threadProcessTimeoutTimeUnit = TimeUnit.MILLISECONDS;

    public GWorkflowOptions() {
        config = new HashMap<>();
    }

    public int getWorkerPoolSize() {
        return workerPoolSize;
    }

    public void setWorkerPoolSize(int workerPoolSize) {
        this.workerPoolSize = workerPoolSize;
    }

    public Map<String, Object> config() { return config;}
    public void setConfig(Map<String, Object> config) { this.config = config;}

    public Object config(String key) { return config.get(key);}

    public Map configService(String name) {
        Object value = config.get(name);
        if(value != null ){
            if(value instanceof Map){
               return (Map) value;
            }else{
               GLogger.error("Invalid config type for service: [{}] ", name);
            }
        }
        return null;
    }

    public int getScheduledTheadPoolSize() {
        return scheduledTheadPoolSize;
    }

    public void setScheduledTheadPoolSize(int scheduledTheadPoolSize) {
        this.scheduledTheadPoolSize = scheduledTheadPoolSize;
    }

    public long getThreadCheckInterval() {
        return threadCheckInterval;
    }

    public void setThreadCheckInterval(long threadCheckInterval) {
        this.threadCheckInterval = threadCheckInterval;
    }

    public TimeUnit getThreadCheckTimeUnit() {
        return threadCheckTimeUnit;
    }

    public void setThreadCheckTimeUnit(TimeUnit threadCheckTimeUnit) {
        this.threadCheckTimeUnit = threadCheckTimeUnit;
    }

    public long getThreadProcessTimeout() {
        return threadProcessTimeout;
    }

    public void setThreadProcessTimeout(long threadProcessTimeout) {
        this.threadProcessTimeout = threadProcessTimeout;
    }

    public TimeUnit getThreadProcessTimeoutTimeUnit() {
        return threadProcessTimeoutTimeUnit;
    }

    public void setThreadProcessTimeoutTimeUnit(TimeUnit threadProcessTimeoutTimeUnit) {
        this.threadProcessTimeoutTimeUnit = threadProcessTimeoutTimeUnit;
    }
}
