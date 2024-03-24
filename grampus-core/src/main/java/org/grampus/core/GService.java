package org.grampus.core;

import org.grampus.log.GLogger;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class GService implements GCellController {
    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(getScheduledTheadPoolSize());
    private ExecutorService executorBlockingService = Executors.newCachedThreadPool();
    private String name;
    private GContext context;
    private Map<GEvent, List<GCell>> cells = new HashMap<>();

    public GService() {
    }

    public GService(String name) {
        this.name = name;
    }

    protected void initService() {
    }

    void initCells() {
        cells.values().forEach(eventCells -> {
            eventCells.forEach(cell -> cell.initCell(this));
        });
    }

    public void startCells() {
        cells.values().forEach(eventCells -> {
            eventCells.forEach(cell -> cell.cellStart());
        });
    }

    public GService cell(GCell cell) {
        return this.cell(GConstant.DEFAULT_EVENT, cell);
    }

    public GService cell(String eventStr, GCell cell) {
        GEvent event = new GEvent(this.name, eventStr);
        if (cells.containsKey(event)) {
            cells.get(event).add(cell);
        } else {
            List<GCell> cellList = new ArrayList<>();
            cellList.add(cell);
            this.cells.put(event, cellList);
        }
        return this;
    }

    public GService cell(String eventStr, int index, GCell cell) {
        GEvent event = new GEvent(this.name, eventStr);
        if (cells.containsKey(event)) {
            List<GCell> cellList = cells.get(event);
            if (cellList.size() > index) {
                cellList.set(index, cell);
                return this;
            } else {
                GLogger.warn("failure to replace GCell event:[{}], with index: [{}]", event, index);
            }
        }
        return this;
    }

    public void setCells(Map<String, List<GCell>> sourceCells) {
        if (sourceCells != null) {
            sourceCells.keySet().forEach(eventStr -> this.cells.put(new GEvent(this.name, eventStr), sourceCells.get(eventStr)));
        }
    }

    protected Map<GEvent, List<GCell>> getCells() {
        return cells;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }


//    @Override
//    public void addTask(Runnable runnable) {
//        this.context.submitTask(runnable);
//    }

    public GTimer createTimer(Runnable runnable) {
        return new GTimer(runnable,(runner, time, timeUnit)->this.scheduledExecutorService.scheduleAtFixedRate(runner,0,time,timeUnit));
    }

    public void addBlockingTask(Runnable runnable) {
        this.executorBlockingService.submit(runnable);
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
    public void addAssertTask(Runnable runnable) {
        this.context.addAssertTask(runnable);
    }

    public void openEvent(String event) {
        this.context.router.addGlobalEvent(this.name, event);
    }

    public void setContext(GContext context) {
        this.context = context;
    }

    public int getScheduledTheadPoolSize() {
        return GConstant.DEFAULT_SCHEDULE_WORKER_POOL_SIZE;
    }
}
