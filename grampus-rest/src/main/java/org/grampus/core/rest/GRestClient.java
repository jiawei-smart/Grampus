package org.grampus.core.rest;

import org.grampus.core.annotation.rest.GRestController;
import org.grampus.core.annotation.rest.GRestDispatcher;
import org.grampus.core.annotation.rest.GRestMethodType;
import org.grampus.core.annotation.rest.GRestResp;
import org.grampus.core.annotation.rest.spec.GRestGroupSpec;
import org.grampus.core.annotation.rest.spec.GRestParamSpec;
import org.grampus.core.client.GClient;
import org.grampus.log.GLogger;
import org.grampus.swagger.GSwagger;
import org.grampus.swagger.spec.MethodSpec;
import org.grampus.swagger.spec.ParameterSpec;
import org.grampus.swagger.endpoint.ApiEndpoint;
import org.grampus.swagger.endpoint.Endpoint;
import org.grampus.swagger.model.HttpMethod;
import org.grampus.util.GJsonUtil;
import org.grampus.util.GStringUtil;
import spark.Request;
import spark.Route;
import spark.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.grampus.swagger.spec.EndpointSpec.endpointPath;
import static org.grampus.swagger.spec.MethodSpec.path;

public class GRestClient implements GClient<GRestOptions> {
    private List<GRestController> controllers = new ArrayList<>();
    private GRestOptions config;

    private Service spark;

    @Override
    public boolean start(GRestOptions config) {
        this.config =  config;
        try {
            spark = Service.ignite()
                    .ipAddress(config.getHost())
                    .port(config.getPort());
            GSwagger.of(spark, config.getSwaggerOptions())
                    .endpoints(() -> parseControllers())
                    .generateDoc();
            return true;
        } catch (IOException e) {
            GLogger.error("GRest: failure to start rest client, {}",e);
            return false;
        }
    }

    private List<Endpoint> parseControllers() {
        List<Endpoint> endpointList = new ArrayList<>();
        this.controllers.forEach(restController->{
            Endpoint endpoint = new Endpoint() {
                @Override
                public void bind(GSwagger swagger) {
                    GRestGroupSpec groupSpec = restController.getGroupSpec();
                    ApiEndpoint endpoint = swagger.endpoint(endpointPath(groupSpec.getId()).withDescription(groupSpec.getDescription()));
                    restController.getMethodsSpec().forEach((path, gRestMethodSpec) -> {
                        MethodSpec.Builder methodDescriptorBuilder = path(path)
                                .withDescription(gRestMethodSpec.getDescription());

                        enrichParams(methodDescriptorBuilder, gRestMethodSpec.getParamSpecList(), gRestMethodSpec.getBodySpec());
                        MethodSpec methodSpec = methodDescriptorBuilder.build();
                        Route route = buildRoute(restController.getDispatcher(),path, gRestMethodSpec.getParamSpecList());
                        if (gRestMethodSpec.getRestMethodType() == GRestMethodType.GET) {
                            methodSpec.setMethod(HttpMethod.GET);
                            endpoint.get(methodSpec, route);
                        }else if(gRestMethodSpec.getRestMethodType() == GRestMethodType.POST){
                            methodSpec.setMethod(HttpMethod.POST);
                            endpoint.post(methodSpec, route);
                        }else if(gRestMethodSpec.getRestMethodType() == GRestMethodType.DELETE){
                            methodSpec.setMethod(HttpMethod.DELETE);
                            endpoint.delete(methodSpec,route);
                        }
                    });
                }
            };
            endpointList.add(endpoint);
        });
        return endpointList;
    }

    private void enrichParams(MethodSpec.Builder methodDescriptorBuilder, List<GRestParamSpec> paramSpecList, GRestParamSpec bodySpec) {
        if(bodySpec != null){
            methodDescriptorBuilder.withBody(ParameterSpec.newBuilder()
                    .withName(bodySpec.getName())
                    .withExample(bodySpec.getExample())
                    .withDescription(bodySpec.getDescription())
                            .withRequired(true)
                    .build())
                    .withRequestType(bodySpec.getType());

        }
        if(paramSpecList != null && !paramSpecList.isEmpty()){
            paramSpecList.forEach(gRestParamSpec -> {
                if(!gRestParamSpec.isBody()){
                    methodDescriptorBuilder
                            .withRequestType(gRestParamSpec.getType())
                            .withFormParam(ParameterSpec.newBuilder()
                            .withName(gRestParamSpec.getName())
                            .withDescription(gRestParamSpec.getDescription())
                            .withExample(gRestParamSpec.getExample()).build());
                }
            });
        }
    }

    private Route buildRoute(GRestDispatcher dispatcher, String path, List<GRestParamSpec> paramSpecs) {
        return  (request, response) -> {
            Object[] params = formatParams(paramSpecs,request);
            GRestResp restResp = dispatcher.dispatch(path, params);
            if (!restResp.hasError()) {
                response.status(200);
                return restResp.getPayload();
            } else {
                response.status(500);
                return restResp.getPayload();
            }
        };
    }

    public Object[] formatParams(List<GRestParamSpec> parameters,Request request) {
        Object[] result = new Object[parameters.size()];
        for (int i = 0;i<parameters.size();i++){
            GRestParamSpec gRestParamSpec = parameters.get(i);
            if(gRestParamSpec.isBody()){
                String body = request.body();
                if( body == null || GStringUtil.isEmpty(body)){
                    result[i] = GStringUtil.EMPTY_STRING;
                }else {
                    result[i] = GJsonUtil.fromJson(body, gRestParamSpec.getType());
                }
            }else {
                result[i] = GJsonUtil.fromJson(request.queryParams(gRestParamSpec.getName()), gRestParamSpec.getType());
            }
        }
        return result;
    }

    @Override
    public boolean stop() {
        this.spark.stop();;
        return true;
    }

    public void registerGRestController(GRestController controller) {
        this.controllers.add(controller);
    }


}
