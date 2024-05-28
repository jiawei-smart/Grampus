package org.grampus.core;

import org.grampus.core.executor.GTimer;
import org.grampus.core.monitor.GMonitor;
import org.grampus.core.monitor.GMonitorMap;
import org.grampus.log.GLogger;
import org.grampus.util.GDateTimeUtil;

import java.util.*;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;


public class GService implements GCellController, GMonitor {
    private String name;
    private GContext context;
//    private Map<GEvent, List<GCell>> cells = new HashMap<>();
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
        initCells();
        initMonitorMap();
    }

    @Override
    public Executor getSingleExecutor(){
        return this.context.getSingleExecutor();
    }

    private void initMonitorMap() {
        this.monitorMap.put(GConstant.MONITOR_SERVICE_NAME,this.name);
        this.monitorMap.put(GConstant.MONITOR_SERVICE_START_TIME, GDateTimeUtil.now());
        this.monitorMap.put(GConstant.MONITOR_SERVICE_START_TIME,this.events.values().stream().map(GEvent::getEventStem).collect(Collectors.joining()));
    }

    private void initCells() {
        this.events.values().forEach(event->event.getCells().forEach(cell ->{
            if(cell.state().value() < GCellState.REGISTERED.value()){
                cell.initCell(this);
            }
        }));
    }

    public void cellsBeforeStart() {
        this.events.values().forEach(event->event.getCells().forEach(cell ->{
            if(cell.state().value() < GCellState.READY_TO_START.value()){
                cell.beforeStart();
                cell.state(GCellState.READY_TO_START);
            }
        }));
    }

    public void startCells() {
        this.events.values().forEach(event->event.getCells().forEach(cell ->{
            if(!cell.isEndStarted()){
                cell.cellStart();
            }
        }));
    }

    public GService cell(GCell cell) {
        return this.cell(GConstant.DEFAULT_EVENT, cell);
    }
    public GService source(GCell cell) {
        return this.cell(GConstant.DEFAULT_EVENT,cell);
    }

    public GService source(String event, GCell cell) {
        return this.cell(event,cell);
    }


    public GService cell(String eventStr, GCell cell) {
        String eventKey =GEvent.id(this.name, eventStr);;
        if (!events.containsKey(eventKey)) {
            GEvent event = new GEvent(getName(),eventStr);
            events.put(eventKey,event);
            event.then(cell);
        }else {
            GEvent event = events.get(eventKey);
            event.then(cell);
        }

        return this;
    }

    public GService cell(String eventStr, int index, GCell cell) {
        String eventKey =GEvent.id(this.name, eventStr);
        if (events.containsKey(eventKey)) {
            List<GCell> cellList = events.get(eventKey).getCells();
            if (cellList.size() > index) {
                cellList.set(index, cell);
                return this;
            } else {
                GLogger.warn("failure to replace GCell event:[{}], with index: [{}]", eventKey, index);
            }
        }
        return this;
    }

    public void setCells(Map<String, List<GCell>> sourceCells) {
        if (sourceCells != null) {
            sourceCells.forEach((eventStr, cells) -> {
                cells.forEach(cell -> {
                    this.cell(eventStr,cell);
                });
            });
        }
    }

//    protected Map<GEvent, List<GCell>> getCells() {
//        return cells;
//    }


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

    public GEvent listen(String event){
        GEvent gEvent = new GEvent(this.name, event);
        this.events.put(gEvent.toString(),gEvent);
        return gEvent;
    }

    public void stop(){}
}
