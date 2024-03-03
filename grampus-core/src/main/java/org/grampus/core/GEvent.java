package org.grampus.core;

import org.grampus.core.util.GStringUtil;

import java.util.Objects;

public class GEvent {
    private String service;
    private String event;
    public GEvent(String eventStr) {
        String[] rowPointValues = GStringUtil.split(eventStr,GConstant.EVENT_SPLIT_CHAR);
        this.service = rowPointValues[0];
        if(rowPointValues.length > 1){
            this.event = rowPointValues[1];
        }
    }

    public GEvent(String service, String event) {
        this.service = service;
        this.event = event;
    }

    public String getService() {
        return service;
    }

    public void setEvent(String event) {
        if(GStringUtil.isNotEmpty(event)){
            this.event = event;
        }else {
            this.event = GConstant.EVENT_ALL;
        }
    }

    @Override
    public String toString() {
        return this.service+GConstant.EVENT_SPLIT_CHAR+this.event;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GEvent gEvent = (GEvent) o;
        return Objects.equals(service, gEvent.service) && Objects.equals(event, gEvent.event);
    }

    @Override
    public int hashCode() {
        return Objects.hash(service, event);
    }
}
