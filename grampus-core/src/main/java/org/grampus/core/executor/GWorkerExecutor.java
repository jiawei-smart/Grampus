package org.grampus.core.executor;

import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;

import java.util.concurrent.Executor;

public class GWorkerExecutor implements Executor {
    private GTaskQueue taskQueue = new GTaskQueue();
    private EventLoopGroup executorPool;

    public GWorkerExecutor(EventLoopGroup executorPool) {
        this.executorPool = executorPool;
    }

    public void execute(Runnable runnable, Executor executor) {
        taskQueue.execute(()->{
            if(Thread.currentThread() instanceof GThread){
                GThread thread = (GThread)Thread.currentThread();
                try{
                    thread.taskStart();
                    runnable.run();
                }finally {
                    thread.taskEnd();
                }
            }
        }, executor);
    }

    public void execute(Runnable runnable) {
        this.execute(runnable, executorPool.next());
    }

    public EventLoop getExecutor() {
        return executorPool.next();
    }

    void close(){
        if(executorPool != null){
            executorPool.shutdownGracefully();
        }
    }
}
