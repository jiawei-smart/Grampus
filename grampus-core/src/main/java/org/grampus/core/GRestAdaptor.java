package org.grampus.core;

import org.grampus.core.annotation.rest.*;
import org.grampus.core.annotation.rest.spec.GRestGroupSpec;
import org.grampus.core.annotation.rest.spec.GRestMethodFunction;
import org.grampus.core.annotation.rest.spec.GRestMethodSpec;
import org.grampus.core.annotation.rest.spec.GRestStaticFilesSpec;
import org.grampus.util.GStringUtil;
import org.grampus.log.GLogger;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GRestAdaptor  implements GRestDispatcher {
    private GRestController controller;

    private GCell cell;

    public GRestAdaptor(GCell cell) {
        this.cell = cell;
        this.controller = new GRestController();
    }

    public GRestController getController() {
        GRestGroup gRestGroup = this.cell.getClass().getAnnotation(GRestGroup.class);
        GRestGroupSpec cellRestGroupSpec = this.cell.getRestGroupSpec();
        if(cellRestGroupSpec != null || gRestGroup != null){
            enrichMethodSpec();
            enrichGroupSpec(gRestGroup,cellRestGroupSpec);
            enrichStaticFileSpec();
        }
        this.controller.setSessionId(this.cell.getId());
        this.controller.setImplement(this.cell.getClass().getSimpleName());
        this.controller.setDispatcher(this::dispatch);
        return this.controller;
    }

    private void enrichStaticFileSpec() {
        GRestStaticFiles staticFiles = this.cell.getClass().getAnnotation(GRestStaticFiles.class);
        GRestStaticFilesSpec cellRestStaticFilesSpec = this.cell.getRestStaticFilesSpec();
        if(cellRestStaticFilesSpec != null){
            this.controller.setStaticFilesSpec(cellRestStaticFilesSpec);
        }else if(staticFiles != null){
            this.controller.setStaticFilesSpec(new GRestStaticFilesSpec(staticFiles));
        }
    }

    private void enrichGroupSpec(GRestGroup gRestGroup, GRestGroupSpec cellRestGroupSpec) {
        GRestGroupSpec groupSpec = cellRestGroupSpec != null ? cellRestGroupSpec : new GRestGroupSpec(gRestGroup);
        this.controller.setGroupSpec(groupSpec);
    }

    private void enrichMethodSpec() {
        Method[] methods = this.cell.getClass().getMethods();
        Map<String, GRestMethodSpec> methodSpecs = new HashMap<>();
        for(Method method : methods){
            GRestMethod gRestMethod = method.getAnnotation(GRestMethod.class);
            if(gRestMethod != null){
                parseRestMethod(gRestMethod.path(), new GRestMethodSpec(gRestMethod),method,methodSpecs);
            }
            GRestPost gRestPost = method.getAnnotation(GRestPost.class);
            if(gRestPost != null){
                parseRestMethod(gRestPost.path(), new GRestMethodSpec(gRestPost),method,methodSpecs);
            }
            GRestGet gRestGet = method.getAnnotation(GRestGet.class);
            if(gRestGet != null){
                parseRestMethod(gRestGet.path(), new GRestMethodSpec(gRestGet),method,methodSpecs);
            }
        }
        List<GRestMethodSpec> cellRestMethodsSpec = this.cell.getRestMethodSpec();
        if(cellRestMethodsSpec != null){
            cellRestMethodsSpec.forEach(restMethodSpec->{
                if(GStringUtil.isNotEmpty(restMethodSpec.getPath())){
                    methodSpecs.put(restMethodSpec.getPath(),restMethodSpec);
                }else {
                    GLogger.error("GRest: rest method should be have a path. [{}]",restMethodSpec);
                }
            });
        }
        this.controller.setMethodsSpec(methodSpecs);
    }

    private void parseRestMethod(String path, GRestMethodSpec methodSpec, Method method, Map<String, GRestMethodSpec> methodSpecs) {
        if(methodSpecs.containsKey(path)){
            GLogger.error("GRest: same method path cannot be accepted, [{}], [{}]",path,this.cell.getId());
        }else {
            methodSpec.setMethod(method);
            if(methodSpec.parseParams()){
                methodSpecs.put(methodSpec.getPath(),methodSpec);
            }
        }
    }

    @Override
    public GRestResp dispatch(String methodPath, Object[] params) {
        GRestMethodSpec methodSpec = this.getController().getMethodsSpec().get(methodPath);
        if(methodSpec != null){
            try{
                if (methodSpec.getMethod() != null){
                    return dispatch(methodSpec.getMethod(), params);
                }else if(methodSpec.getFunction() != null){
                    return dispatch(methodSpec.getFunction(),params);
                }
            }catch (Exception e){
              return   GRestResp.newInstance().error("GRest: dispatch failure for ["+methodPath+"] , with "+e.getMessage());
            }
        }
        return GRestResp.newInstance().error("GRest: cannot found the path methods ["+methodPath+"]");
    }

    private GRestResp dispatch(GRestMethodFunction function, Object[] params) {
        return function.apply(params);
    }

    private GRestResp dispatch(Method method, Object[] params) throws Exception{
        method.setAccessible(true);
        Object result = method.invoke(this.cell,params);
        if(result instanceof GRestResp){
            return (GRestResp) result;
        }else {
            return GRestResp.newInstance().response(result);
        }
    }
}
