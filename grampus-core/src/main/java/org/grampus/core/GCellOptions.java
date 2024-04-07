package org.grampus.core;

public class GCellOptions {
    private int batchSize = 1;
    private int parallel = 1;

    private int timerIntervalMills = 0;

    private int heartbeatIntervalMills = 1000;

    private int heartbeatMsgTimeoutMills = 1000;

    private long monitorIntervalMills = 3000;

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public int getParallel() {
        return parallel;
    }

    public void setParallel(int parallel) {
        this.parallel = parallel;
    }

    public long getTimerIntervalMills() {
        return timerIntervalMills;
    }

    public void setTimerIntervalMills(int timerIntervalMills) {
        this.timerIntervalMills = timerIntervalMills;
    }

    public int getHeartbeatIntervalMills() {
        return heartbeatIntervalMills;
    }

    public void setHeartbeatIntervalMills(int heartbeatIntervalMills) {
        this.heartbeatIntervalMills = heartbeatIntervalMills;
    }

    public int getHeartbeatMsgTimeoutMills() {
        return heartbeatMsgTimeoutMills;
    }

    public void setHeartbeatMsgTimeoutMills(int heartbeatMsgTimeoutMills) {
        this.heartbeatMsgTimeoutMills = heartbeatMsgTimeoutMills;
    }

    public long getMonitorIntervalMills() {
        return monitorIntervalMills;
    }

    public void setMonitorIntervalMills(long monitorIntervalMills) {
        this.monitorIntervalMills = monitorIntervalMills;
    }
}
