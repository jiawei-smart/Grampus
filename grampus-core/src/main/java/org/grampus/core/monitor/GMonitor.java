package org.grampus.core.monitor;

public interface GMonitor {
    default void onMonitorListener(String cellId, GMonitorMap monitorMap){};

    default void monitor(GMonitorMap monitorMap){};

    GMonitorMap monitorMap();
}
