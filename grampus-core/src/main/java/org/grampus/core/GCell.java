package org.grampus.core;

import org.grampus.core.message.GMessage;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class GCell {
    private GCellOptions options;
    private GAdaptor adaptor;
    private GCellController controller;
    private BlockingQueue<GMessage> messageQueue = new LinkedBlockingDeque<>();
    private Set<String> parallelConsumerTopics = new HashSet<>();

    private Map<Integer, GCellProcessor> parallelProcessors = new HashMap<>();
//    private Set<GEvent> nextEvents;

    public GCell() {
    }
    public GCell(GCellOptions options) {
        this.options = options;
    }
    private enum CELL_ACTION {DATA_PUSH, TIMER_OFFSET}

    private Random pnoCreator = new Random();

    void initCell(GCellController controller) {
        this.controller = controller;
        if(this.options == null){
            this.options = new GCellOptions();
        }
        controller.addBlockingTask(this::init);
        adaptor.consume((message)->offset(CELL_ACTION.DATA_PUSH, message));
        setParallel(options.getParallel());
    }

    private void offset(CELL_ACTION cellAction, GMessage message) {
        if (cellAction == CELL_ACTION.DATA_PUSH) {
            this.messageQueue.offer(message);
            if (messageQueue.size() == options.getBatchSize()) {
                this.drainTo(options.getBatchSize());
            }
        } else if (cellAction == CELL_ACTION.TIMER_OFFSET) {
            int size = messageQueue.size();
            this.drainTo(size > options.getBatchSize() ? options.getBatchSize() : size);
        }
    }

    private void drainTo(int batchSize) {
//        List<GMessage> messages = new ArrayList<>();
//        this.messageQueue.drainTo(messages, batchSize);
        for(int i=0;i<batchSize;i++){
           GMessage message = this.messageQueue.poll();
           if(message != null){
               int pno = parallelBy(message);
               this.parallelProcessors.get(pno).acceptMessage(message);
           }
        }
//        messages.forEach(message -> {
//            int pno = parallelBy(message);
//            this.parallelProcessors.get(pno).acceptMessage(message);
////            this.adaptor.toMessageBus(this.adaptor.getId() + "_pno_" + pno, message);
//        });
    }

    public int parallelBy(GMessage message) {
        return pnoCreator.nextInt(this.options.getParallel());
    }

    public void setParallel(int i) {
        this.options.setParallel(i);
        initParallelConsumer();
    }

    private void initParallelConsumer() {
        for (int i = 0; i < this.options.getParallel(); i++) {
            String parallelConsumeTopic = this.adaptor.getId() + "_pno_" + i;
            if (!this.parallelConsumerTopics.contains(parallelConsumeTopic)) {
                this.adaptor.consume(parallelConsumeTopic, this::handle, false);
            }
            parallelProcessors.put(i,new GCellProcessor(this.controller.getTaskExecutor(),this::handle));
        }
    }

    protected void handle(GMessage message) {
        Object out = handle(message.getHeader().getSourceCellId(), message.getPayload(), message.meta());
        message.setPayload(out);
        onEvent(null, message);
    }

    public void onEvent(String event, Object message){
        this.adaptor.publishMessage(event,message);
    }

    public Object handle(String from, Object payload, Map meta) {
        handle(payload, meta);
        return payload;
    }

    public void handle(Object payload, Map meta) {
    }

    public void init() {
    }
    public void assertTask(Runnable runnable){
        this.controller.addAssertTask(runnable);
    }

    public void setOptions(GCellOptions options) {
        this.options = options;
    }

    public void setAdaptor(GAdaptor adaptor) {
        this.adaptor = adaptor;
    }

    public GCellController getController() {
        return controller;
    }

    public Long now(){
        return Instant.now().toEpochMilli();
    }
}
