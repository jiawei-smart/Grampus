package org.grampus.core.executor;

import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;

public class GSingleTaskDisruptorQueue {
    private static Logger logger = LoggerFactory.getLogger(GSingleTaskDisruptorQueue.class);
    public static Integer DEFAULT_BUFFER_SIZE = 1024;
    private Disruptor<RunnableWrapper> disruptor;

    public GSingleTaskDisruptorQueue(Executor executor) {
        this(executor,DEFAULT_BUFFER_SIZE);
    }

    public GSingleTaskDisruptorQueue(Executor executor, int bufferSize) {
        disruptor = new Disruptor<>(()->new RunnableWrapper(), bufferSize, executor, ProducerType.SINGLE,
                new BusySpinWaitStrategy());
        disruptor.handleEventsWith((runnableWrapper, l, b)->{
              try{
                  runnableWrapper.getRunnable().run();
              }catch(Exception e){
                  logger.error("GSingleTaskQueue failure to execute task with {}",e);
              }
            });
        disruptor.start();
    }

    public void execute(Runnable task) {
        RingBuffer<RunnableWrapper> ringBuffer = disruptor.getRingBuffer();
        long next = ringBuffer.next();
        RunnableWrapper runnableWrapper = ringBuffer.get(next);
        runnableWrapper.setRunnable(task);
        ringBuffer.publish(next);
    }

    class RunnableWrapper{
        private Runnable runnable;

        public Runnable getRunnable() {
            return runnable;
        }

        public void setRunnable(Runnable runnable) {
            this.runnable = runnable;
        }
    }
}
