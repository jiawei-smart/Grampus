package org.grampus.core.annotation.rest;

import java.util.HashMap;
import java.util.Map;

public class GRestResp {
    public static final String ERROR = "ERROR";
    public static final String RESULT = "RESULT";
    private Map payload =new HashMap();

    public static GRestResp newInstance(){
        return new GRestResp();
    }

    public GRestResp put(String key, Object value){
        this.payload.put(key,value);
        return this;
    }

    public GRestResp error(Object errorInfo){
        this.payload.put(ERROR,errorInfo);
        return this;
    }

    public GRestResp response(Object result){
        this.payload.put(RESULT,result);
        return this;
    }

    public Map getPayload() {
        return payload;
    }
}
