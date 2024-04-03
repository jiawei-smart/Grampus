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

    private final long startNanoTime;

    public GMessageHeader(String sourceCellId,GMsgType msgType) {
        this.msgType = msgType;
        this.id = sourceCellId +"_"+UUID.randomUUID();
        this.startNanoTime = System.nanoTime();
    }

//    public GMessageHeader() {
//        this(GMsgType.BUSINESS_MESSAGE);
//    }
//
//    public GMessageHeader(GMsgType msgType) {
//        this(null,msgType);
//    }

    public void update(String event, String cellId){
        this.history.add(cellId);
        updateTimestamp(cellId,event);
    }

    public String getSourceCellId() {
        return sourceCellId;
    }

    public void updateTimestamp(String cellId, String event){
        this.timestamps.put(cellId+":"+event, GDateTimeUtil.now());
    }

    public GMsgType msgType() {
        return msgType;
    }

    public void end(){
        histogram.recordValue(System.nanoTime()-startNanoTime);
    }

    public static void achieveHistogram(PrintStream printStream){
        histogram.outputPercentileDistribution(printStream,1000.0);
    }
}
