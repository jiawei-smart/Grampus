package org.grampus.core.monitor;

import org.grampus.core.executor.GTimer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GMonitorSupervisor {
    public static final Long DEFAULT_MONITOR_UPDATE_INTERVAL_MILLS = 60000l;
    private Long monitorMapUpdateIntervalMills;
    private boolean started = false;
    private Set<GMonitor> monitors;
    private GTimer timer;

    public GMonitorSupervisor() {
        this(DEFAULT_MONITOR_UPDATE_INTERVAL_MILLS);
    }

    public GMonitorSupervisor(Long monitorMapUpdateIntervalMills) {
        this(new HashSet<>(),monitorMapUpdateIntervalMills);
    }

    public GMonitorSupervisor(Set<GMonitor>  monitors, Long monitorMapUpdateIntervalMills) {
        this.monitors = monitors;
        this.monitorMapUpdateIntervalMills = monitorMapUpdateIntervalMills;
    }

    public void addMonitor(GMonitor monitor){
        if(!monitors.contains(monitor)){
            this.monitors.add(monitor);
        }
    }

    public void broadcast(String ownerId, GMonitorMap monitorMap){
        this.monitors.forEach(gMonitor -> {
            gMonitor.onMonitorListener(ownerId,monitorMap);
        });
    }

    public void startUpdateMonitorMap( ScheduledExecutorService scheduledExecutor){
        if(!started){
            started = true;
            timer = new GTimer(this::updateMonitorMap, scheduledExecutor).scheduleMills(monitorMapUpdateIntervalMills);
        }
    }

    private void updateMonitorMap() {
        this.monitors.forEach(monitor -> monitor.monitorMap().update());
    }

    public void close(){
        this.timer.cancel();
        this.started = false;
    }
}
