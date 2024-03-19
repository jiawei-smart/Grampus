package org.grampus.core;

import org.grampus.core.message.GMessage;
import org.grampus.core.messagebus.GMessageConsumer;
import org.grampus.core.util.GStringUtil;
import org.grampus.log.GLogger;

import java.util.*;
import java.util.stream.Collectors;

public class GService implements GCellController {
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

    public void initCells() {
        cells.values().forEach(eventCells -> {
            eventCells.forEach(cell -> cell.initCell(this));
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


    @Override
    public void addTask(Runnable runnable) {
        this.context.submitTask(runnable);
    }

    @Override
    public void addBlockingTask(Runnable runnable) {
        this.context.submitBlockingTask(runnable);
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

    public void openEvent(String event) {
        this.context.router.addGlobalEvent(this.name, event);
    }

    public void setContext(GContext context) {
        this.context = context;
    }
}
