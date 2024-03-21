package org.grampus.core;

import org.grampus.log.GLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class GTester {
    private CountDownLatch countDownLatch;
    private Integer timeoutSeconds;
    private List<Runnable> assertTasks = new ArrayList<>();

    public GTester(Integer count, Integer timeoutSeconds) {
        this.countDownLatch = new CountDownLatch(count);
        this.timeoutSeconds = timeoutSeconds;
    }

    public void start(){
        try {
            if(!countDownLatch.await(timeoutSeconds.longValue(), TimeUnit.SECONDS)){
                GLogger.error("testcase timeout, than [{}] seconds", this.timeoutSeconds);
                throw new RuntimeException("testcase timeout");
            }
        } catch (InterruptedException e) {
            GLogger.error("testcase timeout, over than [{}] seconds",timeoutSeconds);
            throw new RuntimeException(e);
        }
        this.assertTasks.forEach(task->task.run());
    }

    public void addAssertTask(Runnable assertTask){
        assertTasks.add(assertTask);
        countDownLatch.countDown();
    }
}
