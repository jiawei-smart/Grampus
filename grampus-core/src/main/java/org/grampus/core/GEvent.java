package org.grampus.core;

import org.grampus.core.customized.*;
import org.grampus.util.GStringUtil;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

public class GEvent {
    private String service;
    private String eventStem;
    private List<GCell> cells = new ArrayList<>();
    private final String id;

    public GEvent(String eventStr) {
        List<String> rowPointValues = GStringUtil.splitAsList(eventStr, GConstant.EVENT_SPLIT_CHAR);
        this.service = rowPointValues.get(0);
        if (rowPointValues.size() > 1) {
            this.eventStem = rowPointValues.get(1);
        } else {
            this.eventStem = GConstant.DEFAULT_EVENT;
        }
        this.id = id(service, eventStem);
    }

    public void initDefaultEventListener(GAdaptor adaptor) {
        adaptor.consume(message -> {
            adaptor.publishMessage(null, message);
        }, false);
    }

    public GEvent(String service, String event) {
        this.service = service;
        this.eventStem = event;
        this.id = id(service, eventStem);
    }

    public String getService() {
        return service;
    }

    public String getEventStem() {
        return eventStem;
    }

    public void setEventStem(String eventStem) {
        if (GStringUtil.isNotEmpty(eventStem)) {
            this.eventStem = eventStem;
        } else {
            this.eventStem = GConstant.EVENT_ALL;
        }
    }

    @Override
    public String toString() {
        return  id(this.service,this.eventStem);
    }

    public String id() {
        return  id;
    }


    public static String id(String service, String eventStem) {
        return  service + GConstant.EVENT_SPLIT_CHAR + eventStem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GEvent gEvent = (GEvent) o;
        return Objects.equals(service, gEvent.service) && Objects.equals(eventStem, gEvent.eventStem);
    }

    @Override
    public int hashCode() {
        return Objects.hash(service, eventStem);
    }

    public GEvent then(GCell cell){
        this.cells.add(cell);
        return this;
    }
    public GEvent then(GCellEventHandler handler) {
        this.cells.add(new GCell(handler));
        return this;
    }

    public<T> GEvent sink(BiConsumer<T, Map> handler) {
        GCell cell = new GCell(handler);
        cell.setSink(true);
        this.cells.add(cell);
        return this;
    }

    public<T> GEvent sink(GCell cell) {
        this.cells.add(cell);
        return this;
    }

    public <T> GEvent then(BiConsumer<T, Map> handler) {
        this.cells.add(new GCell(handler));
        return this;
    }

    public List<GCell> getCells() {
        return this.cells;
    }

    public <I,O> GEvent map(BiFunction<I,Map,O> mapper){
        this.cells.add(new Mapper(mapper));
        return this;
    }

    public <I> GEvent filter(BiPredicate<I,Map> predicate){
        cells.add(new Filter(predicate));
        return this;
    }

    public <I> GEvent redirect(String event){
        cells.add(new Redirector(event));
        return this;
    }

    public <I> GEvent redirect(){
        cells.add(new Redirector());
        return this;
    }

    public <T,C> GEvent route(BiFunction<T, Map, C> predictorCondition, Map<C, String> map){
        cells.add(new Router(predictorCondition, map));
        return this;
    }

    public GEvent dispatch(Set<String> targetEvents){
        cells.add(new Dispatcher(targetEvents));
        return this;
    }
}
