package org.grampus.core;

import org.grampus.log.GLogger;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class GTester {
    private static GTester current;
    private CountDownLatch countDownLatch;
    private Integer timeoutSeconds;
    private Queue<Runnable> assertTasks = new ConcurrentLinkedDeque();

    private GTester() {
    }

    GTester(Integer count, Integer timeoutSeconds) {
        this.countDownLatch = new CountDownLatch(count);
        this.timeoutSeconds = timeoutSeconds;
        current = this;
    }

    void start(){
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
        GLogger.info("Grampus test execute [{}] assert successfully", assertTasks.size());
        GLogger.info("==Grampus test end==");
    }

    public void addAssertTask(Runnable assertTask){
        assertTasks.add(assertTask);
        countDownLatch.countDown();
    }

    public static GTester getCurrent() {
        if(current == null){
            GLogger.error("Only Test model has been enabled the assert feature after workflow test called !");
            return null;
        }else {
            return current;
        }
    }
}
