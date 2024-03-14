package org.grampus.core.message;

import org.grampus.core.GEvent;
import org.grampus.core.util.GDateTimeUtil;

import java.util.*;

public class GMessageHeader {
    private List<String> history = new ArrayList<String>();
    private Map<String, Long> timestamps = new HashMap<>();
    private String id;
    private GMsgType msgType;
    private String currentCellId;

    public GMessageHeader(GEvent event, String cellId,GMsgType msgType) {
        update(event,cellId);
        this.msgType = msgType;
        this.id = event.toString()+"_"+UUID.randomUUID().toString();
    }

    public GMessageHeader() {
        this(GMsgType.BUSINESS_MESSAGE);
    }

    public GMessageHeader(GMsgType msgType) {
        this.msgType = msgType;
    }

    public void update(GEvent event, String cellId){
        this.currentCellId = cellId;
        this.history.add(event.toString());
    }

    public String getCurrentCellId() {
        return currentCellId;
    }

    public void updateTimestamp(String cellId, String tag){
        this.timestamps.put(cellId+":"+tag, GDateTimeUtil.now());
    }
}
