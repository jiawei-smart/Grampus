package org.grampus.core.message;

import org.grampus.core.util.GDateTimeUtil;

import java.util.*;

public class GMessageHeader {
    private List<String> history = new ArrayList<String>();
    private Map<String, Long> timestamps = new HashMap<>();
    private String id;
    private GMsgType msgType;
    private String sourceCellId;

    public GMessageHeader(String sourceCellId,GMsgType msgType) {
        this.msgType = msgType;
        this.id = sourceCellId +"_"+UUID.randomUUID();
    }

    public GMessageHeader() {
        this(GMsgType.BUSINESS_MESSAGE);
    }

    public GMessageHeader(GMsgType msgType) {
        this.msgType = msgType;
    }

    public void update(String cellId){
        this.history.add(cellId);
    }

    public String getSourceCellId() {
        return sourceCellId;
    }

    public void updateTimestamp(String cellId, String tag){
        this.timestamps.put(cellId+":"+tag, GDateTimeUtil.now());
    }

    public GMsgType msgType() {
        return msgType;
    }
}
