package org.grampus.core.test;

import org.grampus.core.executor.GSingleTaskDisruptorQueue;

import java.time.Instant;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class GSingleTaskDisruptorQueueTest {


    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Random random = new Random();
        Executor executor =Executors.newSingleThreadExecutor();
        final GSingleTaskDisruptorQueue queue = new GSingleTaskDisruptorQueue(executor);
        AtomicLong sendCounter = new AtomicLong();
        AtomicLong receivedCounter = new AtomicLong();
        long start = Instant.now().toEpochMilli();
        int count = 10000;
        while (sendCounter.get() < count){
            try{
                queue.execute(()->{
                    System.out.println(receivedCounter.incrementAndGet());
//                        Thread.sleep(10);
                    if(receivedCounter.get()==count){
                        System.out.println("-------receive-------avg-time-cost(ms/msg):"+((Instant.now().toEpochMilli()-start)/count));
                        countDownLatch.countDown();
                    }
//                    try {
//
//                    }
//                    catch (InterruptedException e) {
//                        throw new RuntimeException(e);
//                    }
                });
            }catch (Exception e){
                System.out.println(e);
            }
            sendCounter.incrementAndGet();
        }
        System.out.println("-------send-------avg-time-cost(ml):"+((Instant.now().toEpochMilli()-start)/count));
        try {
            countDownLatch.await(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
