package org.grampus.core.executor;


import org.grampus.log.GLogger;
import org.grampus.util.GDateTimeUtil;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class GThreadChecker {
    private Timer timer;
    private final Set<GThread> threads = new HashSet<>();
    private final long processTimeoutMills;

    private static final String TIMEOUT_WARING = "Thread {} was blocked for {} ms, time limit is {} ms, {}";


    public GThreadChecker(long interval, TimeUnit intervalTimeUnit, long processTimeout, TimeUnit processTimeoutTimeUnit) {
        timer = new Timer("grampus-thead-blocking-checker",true);
        processTimeoutMills = processTimeoutTimeUnit.toMillis(processTimeout);
        Consumer<GThreadTimeoutInfo> theadTimeoutHandler = getTheadTimeoutHandler();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                long now = GDateTimeUtil.now();
                List<GThreadTimeoutInfo> timeouts = new ArrayList<>();
                synchronized (threads){
                    for (GThread thread : threads){
                        GThreadInfo threadInfo = thread.threadInfo;
                        long duration = now - threadInfo.startTime;
                        if(threadInfo.startTime != 0 && duration > processTimeoutMills){
                            timeouts.add(new GThreadTimeoutInfo(thread,duration));
                        }
                    }
                }
                timeouts.forEach(timeoutInfo -> theadTimeoutHandler.accept(timeoutInfo));
            }
        }, intervalTimeUnit.toMillis(interval), intervalTimeUnit.toMillis(interval));
    }

    public Consumer<GThreadTimeoutInfo> getTheadTimeoutHandler() {
        return (timeoutInfo -> GLogger.warn(TIMEOUT_WARING,timeoutInfo.thread.getName(), timeoutInfo.duration, processTimeoutMills, timeoutInfo.thread.getStackTrace()));
    }

    public void registerThread(GThread thread) {
        synchronized (threads){
            threads.add(thread);
        }
    }

    public long getProcessTimeoutMills() {
        return processTimeoutMills;
    }
}

class GThreadTimeoutInfo {

    GThread thread;
    Long duration;

    public GThreadTimeoutInfo(GThread thread, Long duration) {
        this.thread = thread;
        this.duration = duration;
    }

}


