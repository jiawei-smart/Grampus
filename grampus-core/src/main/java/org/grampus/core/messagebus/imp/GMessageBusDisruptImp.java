package org.grampus.core.messagebus.imp;

import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.vertx.core.impl.ConcurrentHashSet;
import org.grampus.core.GWorkflowOptions;
import org.grampus.core.executor.GSingleTaskDisruptorQueue;
import org.grampus.core.executor.GThreadChecker;
import org.grampus.core.executor.GThreadFactory;
import org.grampus.core.message.GMessage;
import org.grampus.core.messagebus.GMessageBus;
import org.grampus.core.messagebus.GMessageConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

public class GMessageBusDisruptImp implements GMessageBus {
    private static Logger logger = LoggerFactory.getLogger(GMessageBusDisruptImp.class);
    private static Integer DEFAULT_RING_BUFFER_SIZE = 1024;
    private Map<String,Disruptor<GBusDisruptMessage>> disrupts = new HashMap<>();
    private GEventFactory eventFactory = new GEventFactory();
    private Map<String, ConcurrentHashSet<GBusDisruptConsumer>> consumers = new ConcurrentHashMap<>();
    private NioEventLoopGroup executor;
    private NioEventLoopGroup workerExecutor;

    public GMessageBusDisruptImp(GWorkflowOptions options) {
        GThreadChecker threadChecker = new GThreadChecker(options.getThreadCheckInterval(),options.getThreadCheckTimeUnit(),options.getThreadProcessTimeout(),options.getThreadProcessTimeoutTimeUnit());
        GThreadFactory threadFactory = new GThreadFactory(threadChecker);
        executor = new NioEventLoopGroup(options.getWorkerPoolSize(), threadFactory);
        workerExecutor= new NioEventLoopGroup(options.getWorkerPoolSize(), threadFactory);
    }

    @Override
    public void consume(String topic, GMessageConsumer consumer, boolean isWorker) {
        if(!disrupts.containsKey(topic)) {
            getDisruptor(topic);
        }
        if(isWorker) {
            consumers.get(topic).add(new GBusDisruptConsumer(workerExecutor.next(),consumer));
        }else {
            consumers.get(topic).add(new GBusDisruptConsumer(executor.next(),consumer));
        }
    }

    private Disruptor getDisruptor(String topic) {
        if (disrupts.containsKey(topic)) {
            return disrupts.get(topic);
        }else {
            Disruptor<GBusDisruptMessage> disruptor;
            synchronized (disrupts) {
                disruptor = new Disruptor<>(eventFactory, DEFAULT_RING_BUFFER_SIZE, Executors.newSingleThreadExecutor(), ProducerType.MULTI,
                        new BusySpinWaitStrategy());
                disruptor.handleEventsWith((gBusDisruptMessage, l, b)-> {
                        for(GBusDisruptConsumer disruptConsumer : consumers.get(topic)){
                            try {
                                disruptConsumer.handle(gBusDisruptMessage.getMessage());
                            }catch (Exception e) {
                                logger.error("failed to handle message {}, with {}",gBusDisruptMessage.getMessage(),e);
                            }
                        }
                    });
                consumers.put(topic, new ConcurrentHashSet<>());
                disrupts.put(topic, disruptor);
            }
            disruptor.start();
            return disruptor;
        }
    }

    @Override
    public void publish(String topic, GMessage message) {
        Disruptor<GBusDisruptMessage> disruptor = getDisruptor(topic);
        RingBuffer<GBusDisruptMessage> ringBuffer = disruptor.getRingBuffer();
        long sequence = ringBuffer.next();
        GBusDisruptMessage busMessage = ringBuffer.get(sequence);
        busMessage.setMessage(message);
        ringBuffer.publish(sequence);
    }

    class GBusDisruptConsumer{
        private GSingleTaskDisruptorQueue taskQueue;
        private GMessageConsumer consumer;

        public GBusDisruptConsumer(EventLoop executor, GMessageConsumer consumer) {
            this.taskQueue = new GSingleTaskDisruptorQueue(executor);
            this.consumer = consumer;
        }

        public void handle(GMessage message){
            taskQueue.execute(()->consumer.handle(message));
        }
    }
}

class GEventFactory implements EventFactory<GBusDisruptMessage> {
    @Override
    public GBusDisruptMessage newInstance() {
        return new GBusDisruptMessage();
    }
}

class GBusDisruptMessage{
    private GMessage message;

    public GMessage getMessage() {
        return message;
    }

    public void setMessage(GMessage message) {
        this.message = message;
    }
}


