package org.grampus.core.monitor;

import java.util.Map;

public interface GMonitor {
    MonitorMap monitorMap = new MonitorMap();
    default void onMonitorListener(String cellId, MonitorMap monitorMap){};

    default void monitor(MonitorMap monitorMap){};

    default MonitorMap monitorMap(){
        return monitorMap;
    }
}
