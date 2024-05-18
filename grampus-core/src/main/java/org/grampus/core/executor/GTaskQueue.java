package org.grampus.core.executor;

import org.grampus.log.GLogger;

import java.util.LinkedList;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;

public class GTaskQueue {
    private LinkedList<GExecutorTask> tasks = new LinkedList();
    private Executor currentExecutor;
    private Runnable runner;
    public GTaskQueue() {
        runner = ()->{
            while (true) {
                final GExecutorTask currentTask;
                synchronized (tasks) {
                    GExecutorTask task = tasks.poll();
                    if (task == null) {
                        currentExecutor = null;
                        return;
                    }

                    if (task.executor != currentExecutor) {
                        tasks.addFirst(task);
                        task.executor.execute(runner);
                        currentExecutor = task.executor;
                        return;
                    }else {
                        currentTask = task;
                    }
                }
                try {
                    currentTask.runnable.run();
                } catch (Throwable t) {
                    GLogger.error("TaskQueue Caught unexpected Throwable  {}", t);
                }
            }
        };
    }

    public void execute(Runnable task, Executor executor) {
        synchronized (tasks) {
            tasks.add(new GExecutorTask(executor, task));
            if (currentExecutor == null) {
                currentExecutor = executor;
                try {
                    executor.execute(runner);
                } catch (RejectedExecutionException e) {
                    currentExecutor = null;
                    throw e;
                }
            }
        }
    }
}

class GExecutorTask {
    Executor executor;
    Runnable runnable;

    public GExecutorTask(Executor executor, Runnable runnable) {
        this.executor = executor;
        this.runnable = runnable;
    }
}

