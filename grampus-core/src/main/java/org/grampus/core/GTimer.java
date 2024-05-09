package org.grampus.core;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class GTimer {
    private Runnable runnable;
    private ScheduleAcceptor scheduleAcceptor;

    private ScheduledFuture scheduledFuture;

    public GTimer(Runnable runnable, ScheduleAcceptor scheduleAcceptor) {
        this.runnable = runnable;
        this.scheduleAcceptor = scheduleAcceptor;
    }

    public GTimer schedule(Long times, TimeUnit timeUnit){
        this.scheduledFuture = scheduleAcceptor.schedule(this.runnable, times,timeUnit);
        return this;
    }

    public GTimer schedule(Long times, Long delay, TimeUnit timeUnit){
        this.scheduledFuture = scheduleAcceptor.schedule(this.runnable, times,timeUnit,delay);
        return this;
    }

    public void cancel(){
        this.scheduledFuture.cancel(true);
    }
}
interface ScheduleAcceptor{
    ScheduledFuture schedule(Runnable runnable, Long time, TimeUnit timeUnit);
    ScheduledFuture schedule(Runnable runnable, Long time, TimeUnit timeUnit, Long delay);
}
