package org.grampus.core;

import org.grampus.core.annotation.rest.spec.GRestGroupSpec;
import org.grampus.core.annotation.rest.spec.GRestMethodSpec;
import org.grampus.core.annotation.rest.spec.GRestStaticFilesSpec;
import org.grampus.core.message.GMessage;
import org.grampus.core.message.GMsgType;
import org.grampus.core.monitor.GMonitor;
import org.grampus.core.monitor.MonitorMap;
import org.grampus.log.GLogger;
import org.grampus.util.GDateTimeUtil;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

public class GCell<T> implements GMonitor {
    private GCellOptions options;
    private GAdaptor adaptor;
    private GCellController controller;
    private BlockingQueue<GMessage> messageQueue = new LinkedBlockingDeque<>();
    private Set<String> parallelConsumerTopics = new HashSet<>();
    private Long lastHeartbeatTimeCost = 0L;
    private boolean isRunning = true;

    public GCell() {
    }

    public GCell(GCellOptions options) {
        this.options = options;
    }

    private enum CELL_HANDLE_ACTION {DATA_PUSH, TIMER_OFFSET}

    private Random random = new Random();

    void initCell(GCellController controller) {
        this.controller = controller;
        if (this.options == null) {
            this.options = new GCellOptions();
        }
        adaptor.consume((message) -> {
            GMsgType msgType = message.header.msgType();
            if (msgType == GMsgType.BUSINESS_MESSAGE) {
                offset(CELL_HANDLE_ACTION.DATA_PUSH, message);
            } else if (msgType == GMsgType.HEARTBEAT_MESSAGE) {
                fromHeartbeat(message);
            } else if (msgType == GMsgType.ADMIN_MESSAGE) {
                fromAdmin(message);
            }
        });
        setParallel(options.getParallel());
    }

    void cellStart() {
        controller.submitBlockingTask(this::start);
        controller.submitBlockingTask(() -> onPlugin(GConstant.REST_PLUGIN, new GRestAdaptor(this).getController()));
        startHeartbeat();
        startBatchTimer();
    }

    private void fromAdmin(GMessage message) {

    }

    private void fromHeartbeat(GMessage message) {
        lastHeartbeatTimeCost = now() - (Long) message.getPayload();
        if (lastHeartbeatTimeCost > options.getHeartbeatMsgTimeoutMills()) {
            GLogger.warn("Cell [{}] heartbeat timeout [{}]", this.adaptor.getId(), lastHeartbeatTimeCost);
        }
    }

    protected void onStatus(String statusAudit, boolean status) {
        this.isRunning = status;
        if (this.isRunning) {
            clearMessageQueue();
        }
    }

    private void clearMessageQueue() {
        while (this.messageQueue.size() >= options.getBatchSize()) {
            this.drainTo(options.getBatchSize());
        }
    }

    private synchronized void offset(CELL_HANDLE_ACTION cellAction, GMessage message) {
        if (cellAction == CELL_HANDLE_ACTION.DATA_PUSH) {
            this.messageQueue.offer(message);
            if (messageQueue.size() == options.getBatchSize() && this.isRunning) {
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
        return random.nextInt(this.options.getParallel());
    }

    public void setParallel(int i) {
        this.options.setParallel(i);
        initParallelConsumer();
    }

    private void initParallelConsumer() {
        for (int i = 0; i < this.options.getParallel(); i++) {
            String parallelConsumeTopic = this.adaptor.getId() + "_pno_" + i;
            if (!this.parallelConsumerTopics.contains(parallelConsumeTopic)) {
                this.adaptor.consume(parallelConsumeTopic, (message) -> {
                    try {
                        this.handle(message);
                    } catch (Exception e) {
                        GLogger.error("failure to handle message [{}], with [{}]", message, e);
                    }
                }, false);
            }
        }
    }

    private void startHeartbeat() {
        GMessage message = new GMessage(this.adaptor.getId(), GMsgType.HEARTBEAT_MESSAGE);
        message.setPayload(now());
        this.adaptor.toMessageBus(this.adaptor.getId(), message);
    }

    private void startBatchTimer() {
        if (options.getTimerIntervalMills() > 0) {
            this.getController().createTimer(() -> offset(CELL_HANDLE_ACTION.TIMER_OFFSET, null)).schedule(options.getTimerIntervalMills(), TimeUnit.MILLISECONDS);
        }
    }

    protected void handle(GMessage message) {
        Object out = handle(message.getHeader().getSourceCellId(), (T) message.getPayload(), message.meta());
        message.setPayload(out);
        onEvent(null, message);
    }

    public void onEvent(String event, Object message) {
        this.onEvent(event, message,null);
    }

    public void onEvent(String event, Object message, Map<String, Object> meta) {
        this.adaptor.publishMessage(event, message, meta);
    }


    public void onPlugin(String event, Object msg) {
        GMessage message = new GMessage(this.adaptor.getId());
        message.setPayload(msg);
        this.adaptor.toMessageBus(GAdaptor.buildAdaptorId(GConstant.PLUGIN_SERVICE, event), message);
    }

    public Object handle(String from, T payload, Map meta) {
        handle(payload, meta);
        return payload;
    }

    public void handle(T payload, Map meta) {
    }

    public void start() {
    }

    public void assertTask(Runnable runnable) {
        this.controller.submitAssertTask(runnable);
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

    public Long now() {
        return GDateTimeUtil.now();
    }

    public GRestGroupSpec getRestGroupSpec() {
        return null;
    }

    public List<GRestMethodSpec> getRestMethodSpec() {
        return null;
    }

    public GRestStaticFilesSpec getRestStaticFilesSpec() {
        return null;
    }
    public String getId() {
        return adaptor.getId();
    }
    public Map meta(String key, Object value){
        Map meta = new HashMap();
        meta.put(key,value);
        return meta;
    }
}
