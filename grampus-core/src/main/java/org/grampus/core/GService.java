package org.grampus.core;

import lombok.extern.slf4j.Slf4j;
import org.grampus.core.executor.GTimer;
import org.grampus.core.monitor.GMonitor;
import org.grampus.core.monitor.GMonitorMap;
import org.grampus.log.GLogger;
import org.grampus.util.GDateTimeUtil;

import java.util.*;
import java.util.concurrent.Executor;

@Slf4j
public class GService implements GProcessorController, GMonitor {
    private String name;
    private GContext context;
    private final Map<String, GEvent> events = new HashMap<>();
    private GMonitorMap monitorMap;

    public GService() {
        this(null);
    }

    public GService(String name) {
        this.name = name;
        monitorMap = new GMonitorMap(this);
    }

    protected void init() {

    }

    void start(){
        initProcessors();
        initMonitorMap();
    }

    @Override
    public Executor getSingleExecutor(){
        return this.context.getSingleExecutor();
    }

    private void initMonitorMap() {
        this.monitorMap.put(GConstant.MONITOR_SERVICE_NAME,this.name);
        this.monitorMap.put(GConstant.MONITOR_SERVICE_START_TIME, GDateTimeUtil.now());
        this.monitorMap.put(GConstant.MONITOR_SERVICE_START_TIME,this.events.keySet());
    }

    @Override
    public void monitor(GMonitorMap monitorMap){
    }

    private void initProcessors() {
        this.events.values().forEach(event->{
            log.debug("starting event [{}] processors", event.getEventStem());
            try{
                event.handler().getCells().forEach(processor ->{
                    if(processor.state().value() < GProcessorState.REGISTERED.value()){
                        processor.initCell(this);
                    }
                });
            }catch (Exception e){
                log.error("error occurred while init event [{}] processors with{}", event.getEventStem(), e);
            }
        });
    }

    public void cellsBeforeStart() {
        this.events.values().forEach(event->event.handler().getCells().forEach(processor ->{
            if(processor.state().value() < GProcessorState.READY_TO_START.value()){
                processor.onStarting();
                processor.state(GProcessorState.READY_TO_START);
            }
        }));
    }

    public void startCells() {
        this.events.values().forEach(event->event.handler().getCells().forEach(processor ->{
            if(!processor.isEndStarted()){
                processor.processorStart();
            }
        }));
    }

    public GService process(GProcessor processor) {
        return this.process(GConstant.DEFAULT_EVENT, processor);
    }
    public GService source(GProcessor processor) {
        return this.process(GConstant.DEFAULT_EVENT,processor);
    }

    public GService source(String event, GProcessor processor) {
        return this.process(event,processor);
    }


    public GService process(String eventStr, GProcessor processor) {
        String eventKey =GEvent.id(this.name, eventStr);;
        if (!events.containsKey(eventKey)) {
            GEvent event = new GEvent(getName(),eventStr);
            events.put(eventKey,event);
            event.handler().then(processor);
        }else {
            GEvent event = events.get(eventKey);
            event.handler().then(processor);
        }

        return this;
    }

    public GService process(String eventStr, int index, GProcessor processor) {
        String eventKey =GEvent.id(this.name, eventStr);
        if (events.containsKey(eventKey)) {
            List<GProcessor> cellList = events.get(eventKey).handler().getCells();
            if (cellList.size() > index) {
                cellList.set(index, processor);
                return this;
            } else {
                GLogger.warn("failure to replace GProcessor event:[{}], with index: [{}]", eventKey, index);
            }
        }
        return this;
    }

    public void setProcessors(Map<String, List<GProcessor>> sourceCells) {
        if (sourceCells != null) {
            sourceCells.forEach((eventStr, cells) -> {
                cells.forEach(processor -> {
                    this.process(eventStr,processor);
                });
            });
        }
    }

    public Map<String, GEvent> getEvents() {
        return events;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GTimer createTimer(Runnable runnable) {
        return new GTimer(runnable, this.context.getScheduledExecutor());
    }
    @Override
    public void submitBlockingTask(Runnable runnable) {
        context.getBlockingExecutor().execute(runnable);
    }
    @Override
    public void submitTask(Runnable runnable) {
        this.context.getWorkerExecutor().execute(runnable);
    }

    @Override
    public <T> T getConfig(Object key, Class<T> type) {
        Map<Object, Object> serviceConfig = this.context.getServiceConfig(this.name);
        if(serviceConfig != null){
            return (T) serviceConfig.get(key);
        }else if(context.getGlobalConfig().containsKey(key)){
            return (T) context.getGlobalConfig().get(key);
        }
        return null;
    }

    @Override
    public void submitAssertTask(Runnable runnable) {
        this.context.addAssertTask(runnable);
    }

    public void openEvent(String... event) {
        if (event != null) {
            this.context.router.addGlobalEvent(this.name, event);
        }
    }

    public void setContext(GContext context) {
        this.context = context;
    }

    @Override
    public GMonitorMap monitorMap() {
        return monitorMap;
    }

    public GEventHandler listen(String event){
        GEvent gEvent = new GEvent(this.name, event);
        this.events.put(gEvent.toString(),gEvent);
        return gEvent.handler();
    }

    public void stop(){}
}
