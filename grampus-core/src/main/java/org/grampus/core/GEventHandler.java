package org.grampus.core;

import org.grampus.core.customized.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

public class GEventHandler {
    private final GEvent event;
    private List<GCell> cells = new ArrayList<>();

    public GEventHandler(GEvent event) {
        this.event = event;
    }

    public GEventHandler then(GCell cell){
        this.cells.add(cell);
        return this;
    }
    public GEventHandler then(GCellEventHandler handler) {
        this.cells.add(new GCell(handler));
        return this;
    }

    public<T> GEventHandler sink(BiConsumer<T, Map> handler) {
        GCell cell = new GCell(handler);
        cell.setSink(true);
        this.cells.add(cell);
        return this;
    }

    public<T> GEventHandler sink(GCell cell) {
        this.cells.add(cell);
        return this;
    }

    public <T> GEventHandler then(BiConsumer<T, Map> handler) {
        this.cells.add(new GCell(handler));
        return this;
    }

    public List<GCell> getCells() {
        return this.cells;
    }

    public <I,O> GEventHandler map(BiFunction<I,Map,O> mapper){
        this.cells.add(new Mapper(mapper));
        return this;
    }

    public <I> GEventHandler filter(BiPredicate<I,Map> predicate){
        cells.add(new Filter(predicate));
        return this;
    }

    public <I> GEventHandler redirect(String event){
        cells.add(new Redirector(event));
        return this;
    }

    public <I> GEventHandler redirect(){
        cells.add(new Redirector());
        return this;
    }

    public <T,C> GEventHandler route(BiFunction<T, Map, C> predictorCondition, Map<C, String> map){
        cells.add(new Router(predictorCondition, map));
        return this;
    }

    public GEventHandler dispatch(Set<String> targetEvents){
        cells.add(new Dispatcher(targetEvents));
        return this;
    }
}
