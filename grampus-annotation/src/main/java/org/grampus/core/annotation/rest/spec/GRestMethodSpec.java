package org.grampus.core.annotation.rest.spec;

import org.grampus.core.annotation.rest.*;
import org.grampus.log.GLogger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class GRestMethodSpec {
    String path;
    String description;

    Method method;

    GRestMethodFunction function;

    GRestMethodType restMethodType;

    List<GRestParamSpec> paramSpecList;

    GRestParamSpec bodySpec;
    public GRestMethodSpec(GRestMethod restMethod) {
        this.path = restMethod.path();
        this.description = restMethod.description();
        this.restMethodType = restMethod.requestType();
    }
    public GRestMethodSpec(GRestMethodGet restMethodGet) {
        this.path = restMethodGet.path();
        this.description = restMethodGet.description();
        this.restMethodType = GRestMethodType.GET;
    }
    public GRestMethodSpec(GRestMethodPost restMethodPost) {
        this.path = restMethodPost.path();
        this.description = restMethodPost.description();
        this.restMethodType = GRestMethodType.POST;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public GRestMethodType getRestMethodType() {
        return restMethodType;
    }

    public void setRestMethodType(GRestMethodType restMethodType) {
        this.restMethodType = restMethodType;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
        parseParams();
    }

    public boolean parseParams() {
        paramSpecList = new ArrayList<>();
        Class[] paramTypes = method.getParameterTypes();
        Annotation[][] paramAnnotations = method.getParameterAnnotations();
        for(int i= 0; i < paramTypes.length ; i++){
           Annotation paramAnnotation = paramAnnotations[i][0];
           if(paramAnnotation instanceof  GRestParam){
               GRestParam restParam = (GRestParam) paramAnnotation;
               paramSpecList.add(new GRestParamSpec(restParam, paramTypes[i]));
           }else if(paramAnnotation instanceof GRestBody){
               GRestBody restBody = (GRestBody) paramAnnotation;
               if(bodySpec != null){
                   GLogger.error("GRest: invalid rest method [{}] with duplicated body param [{}]", this.method.getName(),paramTypes[i]);
                   return false;
               }else {
                   bodySpec =  new GRestParamSpec(restBody, paramTypes[i]);
                   paramSpecList.add(bodySpec);
               }
           }else {
               GLogger.error("GRest: invalid rest method [{}] with un-declared rest param [{}] ", method,paramTypes[i]);
               return false;
           }
        }
        return true;
    }

    public GRestMethodFunction getFunction() {
        return function;
    }

    public void setFunction(GRestMethodFunction function) {
        this.function = function;
    }

    public List<GRestParamSpec> getParamSpecList() {
        return paramSpecList;
    }

    public void setParamSpecList(List<GRestParamSpec> paramSpecList) {
        this.paramSpecList = paramSpecList;
    }

    public GRestParamSpec getBodySpec() {
        return bodySpec;
    }

    public void setBodySpec(GRestParamSpec bodySpec) {
        this.bodySpec = bodySpec;
    }
}
