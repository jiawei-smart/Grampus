package org.grampus.core.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;

public class GSingleTaskQueue {
    private static Logger logger = LoggerFactory.getLogger(GSingleTaskQueue.class);
    private LinkedList<Runnable> tasks = new LinkedList();
    private Executor executor;
    private Runnable runner;
    public GSingleTaskQueue(Executor executor) {
        this.executor = executor;
        runner = ()->{
            while (true) {
                synchronized (tasks) {
                    Runnable task = tasks.poll();
                    if (task == null) {
                        return;
                    }
                    try {
                        if (Thread.currentThread() instanceof GThread){
                            GThread thread = (GThread) Thread.currentThread();
                            try {
                                thread.taskEnd();
                                executor.execute(task);
                            }finally {
                                thread.taskEnd();
                            }
                        }else {
                            executor.execute(task);
                        }
                    }catch (RejectedExecutionException e){
                        tasks.addFirst(task);
                    }
                    return;
                }
            }
        };
    }

    public void execute(Runnable task) {
        synchronized (tasks) {
            tasks.add(task);
            try {
                executor.execute(runner);
            } catch (RejectedExecutionException e) {
                logger.warn("GSingleTaskQueue task queue rejected, will try again");
            }
        }
    }
}
