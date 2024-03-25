package org.grampus.core.annotation.rest;

import org.grampus.core.annotation.rest.spec.GRestGroupSpec;
import org.grampus.core.annotation.rest.spec.GRestMethodSpec;
import org.grampus.core.annotation.rest.spec.GRestStaticFilesSpec;

import java.util.Map;

public class GRestController {
    private String sessionId;
    private String implement;
    private Map<String, GRestMethodSpec> methodsSpec;
    private GRestGroupSpec groupSpec;

    private GRestDispatcher dispatcher;

    private GRestStaticFilesSpec staticFilesSpec;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getImplement() {
        return implement;
    }

    public void setImplement(String implement) {
        this.implement = implement;
    }

    public Map<String, GRestMethodSpec> getMethodsSpec() {
        return methodsSpec;
    }

    public void setMethodsSpec(Map<String, GRestMethodSpec> methodsSpec) {
        this.methodsSpec = methodsSpec;
    }

    public GRestGroupSpec getGroupSpec() {
        return groupSpec;
    }

    public void setGroupSpec(GRestGroupSpec groupSpec) {
        this.groupSpec = groupSpec;
    }

    public GRestStaticFilesSpec getStaticFilesSpec() {
        return staticFilesSpec;
    }

    public void setStaticFilesSpec(GRestStaticFilesSpec staticFilesSpec) {
        this.staticFilesSpec = staticFilesSpec;
    }

    public GRestDispatcher getDispatcher() {
        return dispatcher;
    }

    public void setDispatcher(GRestDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }
}
