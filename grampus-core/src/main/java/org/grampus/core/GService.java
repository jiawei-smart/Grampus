package org.grampus.core;

import org.grampus.core.message.GMessage;
import org.grampus.core.messagebus.GMessageConsumer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
       startEventConsume();
       initCells();
    }

    private void initCells() {
        cells.keySet().forEach(cellEventStr->{
            GEvent event = new GEvent(this.name,cellEventStr);
            List<GCell> eventCells = cells.get(cellEventStr);
            for(int i =0; i< eventCells.size(); i++){
                GCell cell = eventCells.get(i);
                cell.setId(buildCellId(event,i));
                cell.setEvent(event);
                cell.initCell(context);
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
                String nextDL = context.nextCellId(getName(),event,GConstant.EVENT_FIRST_CELL_ID);
                context.messageBus.publish(nextDL, message);
            });
        });
    }

    public void init(){
    }

    public void cell(GCell cell){
        this.cell(GConstant.DEFAULT_EVENT,cell);
    }

    public void cell(String event, GCell cell){
        if(cells.containsKey(event)){
            cells.get(event).add(cell);
        }else {
            List<GCell> cellList = new ArrayList<>();
            cellList.add(cell);
            this.cells.put(event,cellList);
        }
    }

    public void cell(String event,int index, GCell cell){
        if(cells.containsKey(event)){
            List<GCell> cellList = cells.get(event);
            if(cellList.size() > index){
                cellList.set(index,cell);
                return;
            }
        }
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

    public String nextCellId(GEvent event, String currentCellId){
       return context.nextCellId(this.name,event,currentCellId);
    }
}
