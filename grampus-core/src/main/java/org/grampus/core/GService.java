package org.grampus.core;

import org.grampus.core.message.GMessage;
import org.grampus.core.messagebus.GMessageConsumer;
import org.grampus.core.util.GStringUtil;

import java.util.*;
import java.util.stream.Collectors;

public class GService implements GCellController{
    private String name;
    private GContext context;
    private Map<String, List<GCell>> cells = new HashMap<>();

    public GService() {
    }

    public GService(String name) {
        this.name = name;
    }

    protected void initService(){
       startEventConsume();
       initCells();
    }

    private void initCells() {
        cells.keySet().forEach(rawCellEventStr->{
//            List<String> cellEventStrList = Arrays.asList(GStringUtil.split(rawCellEventStr,GConstant.CHAIN_SPLIT_CHAR));
//            GEvent event = new GEvent(this.name,cellEventStrList.get(0));
//            Set<GEvent> nextEvents;
//            if(cellEventStrList.size() > 1){
//                nextEvents = cellEventStrList.subList(1,cellEventStrList.size()).stream().map(eventStr->new GEvent(eventStr)).collect(Collectors.toSet());
//            }else {
//                nextEvents = new HashSet<>();
//                nextEvents.add(event);
//            }
            List<GCell> eventCells = cells.get(rawCellEventStr);
            for(int i =0; i< eventCells.size(); i++){
                GCell cell = eventCells.get(i);
                cell.initCell(this);
            }
        });
    }


    private void startEventConsume() {
        cells.keySet().forEach(cellEventStr->{
            context.router.consume(this.name+GConstant.EVENT_SPLIT_CHAR+cellEventStr, message -> {
                String nextDL = context.router.getServiceNextPathValue(this.name, cellEventStr);
                context.router.toMessageBus(nextDL, message);
            }, false);
        });
    }

    public void init(){
    }

    public GService cell(GCell cell){
        return this.cell(GConstant.DEFAULT_EVENT,cell);
    }

    public GService cell(String event, GCell cell){
        if(cells.containsKey(event)){
            cells.get(event).add(cell);
        }else {
            List<GCell> cellList = new ArrayList<>();
            cellList.add(cell);
            this.cells.put(event,cellList);
        }
        return this;
    }

    public GService cell(String event,int index, GCell cell){
        if(cells.containsKey(event)){
            List<GCell> cellList = cells.get(event);
            if(cellList.size() > index){
                cellList.set(index,cell);
                return this;
            }
        }
        return this;
    }

    public void setCells(Map<String, List<GCell>> cells) {
        this.cells = cells;
    }

    protected Map<String, List<GCell>> getCells(){
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
    public void consumeMessage(String topic, GMessageConsumer consumer, boolean isWorker) {
        this.context.router.consume(topic,consumer, isWorker);
    }

    @Override
    public String buildCellId(String event, int eventSeq) {
        return this.name+GConstant.CELL_ID_SPLIT_CHAR+event+GConstant.CELL_ID_SPLIT_CHAR+eventSeq;
    }

    public void openEvent(String event) {
        this.context.router.addGlobalEvent(this.name, event);
    }

    public void setContext(GContext context) {
        this.context = context;
    }
}
