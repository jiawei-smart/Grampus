package org.grampus.core;

import org.grampus.core.message.GMessage;
import org.grampus.core.messagebus.GMessageConsumer;
import org.grampus.core.util.GStringUtil;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

public class GService {
    private String name;
    private GContext context;
    private Map<String, List<GCell>> cells = new HashMap<>();

    public GService() {
    }

    public GService(String name) {
        this.name = name;
    }

    protected void initService(GContext context){
        this.context = context;
       startEventConsume();
       initCells();
    }

    private void initCells() {
        cells.keySet().forEach(rawCellEventStr->{
            List<String> cellEventStrList = Arrays.asList(GStringUtil.split(rawCellEventStr,GConstant.CHAIN_SPLIT_CHAR));
            GEvent event = new GEvent(this.name,cellEventStrList.get(0));
            Set<GEvent> nextEvents;
            if(cellEventStrList.size() > 1){
                nextEvents = cellEventStrList.subList(1,cellEventStrList.size()).stream().map(eventStr->new GEvent(eventStr)).collect(Collectors.toSet());
            }else {
                nextEvents = new HashSet<>();
                nextEvents.add(event);
            }
            List<GCell> eventCells = cells.get(rawCellEventStr);
            for(int i =0; i< eventCells.size(); i++){
                GCell cell = eventCells.get(i);
                cell.setId(buildCellId(event,i));
                cell.setEvent(event);
                cell.initCell(context);
                cell.setNextEvents(nextEvents);
            }
        });
    }

    private String buildCellId(GEvent event, int i) {
        return event.toString()+GConstant.CELL_ID_SPLIT_CHAR+i;
    }

    private void startEventConsume() {
        cells.keySet().forEach(cellEventStr->{
            GEvent event = new GEvent(this.name,cellEventStr);
            context.messageBus.consume(event.toString(), message -> {
                String nextDL = context.nextCellId(event,GConstant.EVENT_FIRST_CELL_ID);
                context.messageBus.publish(nextDL, message);
            });
        });
    }

    public void init(){
    }

    public void cell(GCell cell){
        this.cell(GConstant.DEFAULT_EVENT,cell);
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

}
