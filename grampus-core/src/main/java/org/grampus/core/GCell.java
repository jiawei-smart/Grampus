package org.grampus.core;

import org.grampus.core.message.GMessage;
import org.grampus.core.message.GMsgType;
import org.grampus.log.GLogger;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

public class GCell {
    private GCellOptions options;
    private GAdaptor adaptor;
    private GCellController controller;
    private BlockingQueue<GMessage> messageQueue = new LinkedBlockingDeque<>();
    private Set<String> parallelConsumerTopics = new HashSet<>();

    public GCell() {
    }
    public GCell(GCellOptions options) {
        this.options = options;
    }
    private enum CELL_HANDLE_ACTION {DATA_PUSH, TIMER_OFFSET}
    private Random pnoCreator = new Random();

    void initCell(GCellController controller) {
        this.controller = controller;
        if(this.options == null){
            this.options = new GCellOptions();
        }
        adaptor.consume((message)->{
            GMsgType msgType = message.header.msgType();
            if(msgType == GMsgType.BUSINESS_MESSAGE){
                offset(CELL_HANDLE_ACTION.DATA_PUSH, message);
            }else if(msgType == GMsgType.HEARTBEAT_MESSAGE){
                fromHeartbeat(message);
            }else if(msgType == GMsgType.ADMIN_MESSAGE){
                fromAdmin(message);
            }
        });
        setParallel(options.getParallel());
    }

    void cellStart(){
        controller.addBlockingTask(this::start);
        startHeartbeat();
        if(options.getTimerIntervalMills() > 0){
            this.getController().createTimer(()->offset(CELL_HANDLE_ACTION.TIMER_OFFSET,null)).schedule(options.getTimerIntervalMills(), TimeUnit.MILLISECONDS);
        }
    }

    private void fromAdmin(GMessage message) {

    }

    private void fromHeartbeat(GMessage message) {
        long interval = now()-(Long)message.getPayload();
        if(interval > options.getHeartbeatMsgTimeoutMills()){
            GLogger.warn("Cell [{}] heartbeat timeout [{}]",this.adaptor.getId(), interval);
        }
    }

    private  synchronized void offset(CELL_HANDLE_ACTION cellAction, GMessage message) {
        if (cellAction == CELL_HANDLE_ACTION.DATA_PUSH) {
            this.messageQueue.offer(message);
            if (messageQueue.size() == options.getBatchSize()) {
                this.drainTo(options.getBatchSize());
            }
        } else if (cellAction == CELL_HANDLE_ACTION.TIMER_OFFSET) {
            int size = messageQueue.size();
            this.drainTo(size > options.getBatchSize() ? options.getBatchSize() : size);
        }
    }

    private void drainTo(int batchSize) {
        List<GMessage> messages = new ArrayList<>();
        this.messageQueue.drainTo(messages, batchSize);
        messages.forEach(message -> {
            int pno = parallelBy(message);
            this.adaptor.toMessageBus(this.adaptor.getId() + "_pno_" + pno, message);
        });
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
        }
    }

    private void startHeartbeat() {
        GMessage message = new GMessage(this.adaptor.getId(), GMsgType.HEARTBEAT_MESSAGE);
        message.setPayload(now());
        this.adaptor.toMessageBus(this.adaptor.getId(),message);
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

    public void start() {
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
