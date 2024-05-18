package org.grampus.core.executor;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class GTimer {
    private Runnable runnable;
    private ScheduledExecutorService executorService;

    private ScheduledFuture scheduledFuture;

    public GTimer(Runnable runnable, ScheduledExecutorService executorService) {
        this.runnable = runnable;
        this.executorService = executorService;
    }

    public GTimer schedule(Long times, TimeUnit timeUnit){
        this.scheduledFuture = executorService.scheduleAtFixedRate(this.runnable,0, times,timeUnit);
        return this;
    }

    public GTimer scheduleMills(Long times){
        return schedule(times, TimeUnit.MILLISECONDS);
    }

    public GTimer schedule(Long times, TimeUnit timeUnit, Integer count){
        this.scheduledFuture = executorService.scheduleAtFixedRate(wrapperCountRunner(count),0, times,timeUnit);
        return this;
    }

    public GTimer scheduleMills(Long times, Integer count){
        return schedule(times,TimeUnit.MILLISECONDS,count);
    }

    private Runnable wrapperCountRunner(Integer count) {
        AtomicInteger executedCount = new AtomicInteger();
        return ()->{
            if(executedCount.get() < count){
                runnable.run();
                executedCount.incrementAndGet();
            }else {
                scheduledFuture.cancel(true);
            }
        };
    }

    public GTimer schedule(Long times, Long delay, TimeUnit timeUnit){
        this.scheduledFuture = executorService.scheduleAtFixedRate(runnable,delay,times,timeUnit);
        return this;
    }

    public void cancel(){
        this.scheduledFuture.cancel(true);
    }
}
