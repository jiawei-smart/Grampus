package org.grampus.core.message;

import org.HdrHistogram.Histogram;
import org.grampus.util.GDateTimeUtil;

import java.io.PrintStream;
import java.util.*;

public class GMessageHeader {
    private static Histogram histogram = new Histogram(3600000000000L, 3);
    private List<String> history = new ArrayList<String>();
    private Map<String, Long> timestamps = new HashMap<>();
    private String id;
    private GMsgType msgType;
    private String sourceCellId;
    private String lastEvent;

    private final long startNanoTime;

    public GMessageHeader(GMsgType msgType) {
        this.msgType = msgType;
        this.startNanoTime = System.nanoTime();
    }

    public void update(String event, String cellId){
        if(this.id == null){
            this.id = cellId+"_"+UUID.randomUUID();
        }
        this.history.add(cellId);
        updateTimestamp(cellId,event);
    }

    public String getSourceCellId() {
        return sourceCellId;
    }

    public void updateTimestamp(String cellId, String event){
        this.timestamps.put(cellId+":"+event, GDateTimeUtil.now());
        this.lastEvent = event;
    }

    public GMsgType msgType() {
        return msgType;
    }

    public String getLastEvent() {
        return lastEvent;
    }

    public void end(){
        histogram.recordValue(System.nanoTime()-startNanoTime);
    }

    public static void achieveHistogram(PrintStream printStream){
        histogram.outputPercentileDistribution(printStream,1000.0);
    }
}
