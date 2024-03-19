package org.grampus.core;

import org.grampus.log.GLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class GTester {
    private CountDownLatch countDownLatch;
    private List<Runnable> assertTasks = new ArrayList<>();

    public GTester(Integer count) {
        this.countDownLatch = new CountDownLatch(count);
    }

    public void start(Integer timeoutSeconds){
        try {
            countDownLatch.await(timeoutSeconds.longValue(), TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            GLogger.error("testcase timeout, over than [{}] seconds",timeoutSeconds);
            throw new RuntimeException(e);
        }
    }

    public void addAssertTask(Runnable assertTask){
        assertTasks.add(assertTask);
        countDownLatch.countDown();
    }
}
