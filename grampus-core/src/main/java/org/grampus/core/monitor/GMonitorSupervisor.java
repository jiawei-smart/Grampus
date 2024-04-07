package org.grampus.core.monitor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class GMonitorSupervisor {
    public static final Long DEFAULT_MONITOR_UPDATE_INTERVAL_MILLS = 60000l;
    private Long monitorMapUpdateIntervalMills;
    private boolean started = false;
    private List<GMonitor> monitors;

    public GMonitorSupervisor() {
        this(DEFAULT_MONITOR_UPDATE_INTERVAL_MILLS);
    }

    public GMonitorSupervisor(Long monitorMapUpdateIntervalMills) {
        this(new ArrayList<>(),monitorMapUpdateIntervalMills);
    }

    public GMonitorSupervisor(List<GMonitor> monitors, Long monitorMapUpdateIntervalMills) {
        this.monitors = monitors;
        this.monitorMapUpdateIntervalMills = monitorMapUpdateIntervalMills;
    }

    public void addMonitor(GMonitor monitor){
        this.monitors.add(monitor);
    }

    public void broadcast(String ownerId, GMonitorMap monitorMap){
        this.monitors.forEach(gMonitor -> {
            gMonitor.onMonitorListener(ownerId,monitorMap);
        });
    }

    public void startUpdateMonitorMap(){
        if(!started){
            started = true;
            Executors.newScheduledThreadPool(1).scheduleAtFixedRate(this::updateMonitorMap,monitorMapUpdateIntervalMills,this.monitorMapUpdateIntervalMills, TimeUnit.MILLISECONDS);
        }
    }

    private void updateMonitorMap() {
        this.monitors.forEach(monitor -> monitor.monitorMap().update());
    }
}
