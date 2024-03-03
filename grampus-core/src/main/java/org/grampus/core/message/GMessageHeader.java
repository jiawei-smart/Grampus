package org.grampus.core.message;

import java.util.ArrayList;
import java.util.List;

public class GMessageHeader {
    private List<String> history = new ArrayList<String>();
    private String id;
    private GMsgType msgType;
    private String currentService;
    private String currentCellId;

    public GMessageHeader(String serviceName, String cellId,GMsgType msgType) {
        update(serviceName,cellId);
        this.msgType = msgType;
    }

    public GMessageHeader() {
        this(GMsgType.BUSINESS_MESSAGE);
    }

    public GMessageHeader(GMsgType msgType) {
        this.msgType = msgType;
    }

    protected void update(String serviceName, String cellId){
        this.currentService = serviceName;
        this.currentCellId = cellId;
        this.history.add(this.currentService+"."+this.currentCellId);
    }

    public String getCurrentService() {
        return currentService;
    }

    public String getCurrentCellId() {
        return currentCellId;
    }
}
