package org.grampus.core;

import org.grampus.core.util.GStringUtil;

import java.util.Objects;

public class GEvent {
    private String service;
    private String eventStem;
    public GEvent(String eventStr) {
        String[] rowPointValues = GStringUtil.split(eventStr,GConstant.EVENT_SPLIT_CHAR);
        this.service = rowPointValues[0];
        if(rowPointValues.length > 1){
            this.eventStem = rowPointValues[1];
        }else {
            this.eventStem = GConstant.DEFAULT_EVENT;
        }
    }

    public GEvent(String service, String event) {
        this.service = service;
        this.eventStem = event;
    }

    public String getService() {
        return service;
    }

    public String getEventStem() {
        return eventStem;
    }

    public void setEventStem(String eventStem) {
        if(GStringUtil.isNotEmpty(eventStem)){
            this.eventStem = eventStem;
        }else {
            this.eventStem = GConstant.EVENT_ALL;
        }
    }

    @Override
    public String toString() {
        return this.service+GConstant.EVENT_SPLIT_CHAR+this.eventStem;
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
}
