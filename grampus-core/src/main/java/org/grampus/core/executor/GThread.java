package org.grampus.core.executor;

import io.netty.util.concurrent.FastThreadLocalThread;
import org.grampus.util.GDateTimeUtil;

import java.util.concurrent.TimeUnit;

public class GThread extends FastThreadLocalThread {
    final GThreadInfo threadInfo;

    public GThread(Runnable runnable, GThreadInfo threadInfo) {
        super(runnable);
        this.threadInfo = threadInfo;
    }

    void taskStart(){
        threadInfo.startTime = GDateTimeUtil.now();
    }

    void taskEnd(){
        threadInfo.startTime = 0;
    }
}

class GThreadInfo {
    public long startTime;
    public final long maxProcessMills;

    public GThreadInfo(long maxProcessTime, TimeUnit timeUnit) {
        this.maxProcessMills = timeUnit.convert(maxProcessTime,TimeUnit.MILLISECONDS);
    }
}
