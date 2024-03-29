package org.grampus.core.rest;

import org.grampus.core.GConstant;
import org.grampus.core.annotation.plugin.GPlugin;
import org.grampus.core.annotation.rest.GRestController;
import org.grampus.core.annotation.rest.GRestDispatcher;
import org.grampus.core.annotation.rest.GRestMethodType;
import org.grampus.core.annotation.rest.GRestResp;
import org.grampus.core.annotation.rest.spec.GRestGroupSpec;
import org.grampus.core.annotation.rest.spec.GRestParamSpec;
import org.grampus.core.plugin.GPluginCell;
import org.grampus.swagger.GSparkSwagger;
import org.grampus.swagger.descriptor.MethodDescriptor;
import org.grampus.swagger.descriptor.ParameterDescriptor;
import org.grampus.swagger.model.ApiEndpoint;
import org.grampus.swagger.model.Endpoint;
import org.grampus.swagger.model.HttpMethod;
import org.grampus.swagger.util.TypeUitl;
import spark.Request;
import spark.Route;

import java.io.File;
import java.util.List;
import java.util.Map;

import static org.grampus.swagger.descriptor.EndpointDescriptor.endpointPath;
import static org.grampus.swagger.descriptor.MethodDescriptor.path;
@GPlugin(event = GConstant.REST_PLUGIN)
public class GRestCell extends GPluginCell<GRestController> {
    public static final String REST_DEFAULT_CONFIG_YAML = "plugin/rest.yaml";
    private GRestClient client;
    private Integer registeredCount = 0;

    @Override
    public void start() {
        onStatus("START",false);
        GRestOptions config = getController().getConfig(GRestOptions.GREST_CONFIG_KEY, GRestOptions.class);
        if (config == null) {
            File file = new File(this.getClass().getClassLoader().getResource(REST_DEFAULT_CONFIG_YAML).getFile());
            if (file.exists()) {
                config = getController().loadConfig(REST_DEFAULT_CONFIG_YAML, GRestOptions.class);
            }
        }
        client = new GRestClient();
        onStatus("START",true);
        while (true) {
            if(isAllCellInitRest()){
                client.start(config);
                break;
            }
        }
    }

    private boolean isAllCellInitRest() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    @Override
    public void handle(GRestController restController, Map meta) {
        registeredCount++;
        if (restController.isValidController()) {
            this.client.registerEndpoint(new Endpoint() {
                @Override
                public void bind(GSparkSwagger swagger) {
                    GRestGroupSpec groupSpec = restController.getGroupSpec();
                    ApiEndpoint endpoint = swagger.endpoint(endpointPath(groupSpec.getId()).withDescription(groupSpec.getDescription()));
                    restController.getMethodsSpec().forEach((path, gRestMethodSpec) -> {
                        MethodDescriptor.Builder methodDescriptorBuilder = path(path)
                                .withDescription(gRestMethodSpec.getDescription());

                        enrichParams(methodDescriptorBuilder, gRestMethodSpec.getParamSpecList(), gRestMethodSpec.getBodySpec());
                        MethodDescriptor methodDescriptor = methodDescriptorBuilder.build();
                        Route route = buildRoute(restController.getDispatcher(),path, gRestMethodSpec.getParamSpecList());
                        if (gRestMethodSpec.getRestMethodType() == GRestMethodType.GET) {
                            methodDescriptor.setMethod(HttpMethod.GET);
                            endpoint.get(methodDescriptor, route);
                        }else if(gRestMethodSpec.getRestMethodType() == GRestMethodType.POST){
                            methodDescriptor.setMethod(HttpMethod.POST);
                            endpoint.post(methodDescriptor, route);
                        }else if(gRestMethodSpec.getRestMethodType() == GRestMethodType.DELETE){
                            methodDescriptor.setMethod(HttpMethod.DELETE);
                            endpoint.delete(methodDescriptor,route);
                        }
                    });
                }
            });
        }
    }

    private void enrichParams(MethodDescriptor.Builder methodDescriptorBuilder, List<GRestParamSpec> paramSpecList, GRestParamSpec bodySpec) {
        if(bodySpec != null){
            methodDescriptorBuilder.withBody(ParameterDescriptor.newBuilder()
                    .withName(bodySpec.getName())
                    .withExample(bodySpec.getExample())
                    .withDescription(bodySpec.getDescription())
                    .build());

        }
        if(paramSpecList != null && !paramSpecList.isEmpty()){
            paramSpecList.forEach(gRestParamSpec -> {
                if(!gRestParamSpec.isBody()){
                    methodDescriptorBuilder.withHeaderParam(ParameterDescriptor.newBuilder()
                            .withName(gRestParamSpec.getName())
                            .withDescription(gRestParamSpec.getDescription())
                            .withExample(gRestParamSpec.getExample()).build());
                }
            });
        }
    }

    private Route buildRoute(GRestDispatcher dispatcher, String path, List<GRestParamSpec> paramSpecs) {
        return  (request, response) -> {
            Object[] params = formatParams(paramSpecs, request.body(), request);
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

    public Object[] formatParams(List<GRestParamSpec> parameters, String body, Request request) {
        Object[] result = new Object[parameters.size()];
        for (int i = 0;i<parameters.size();i++){
            GRestParamSpec gRestParamSpec = parameters.get(i);
            if(gRestParamSpec.isBody()){
                result[i] = TypeUitl.json().fromJson(request.body(), gRestParamSpec.getType());

            }else {
                String value = request.attribute(gRestParamSpec.getName());
                result[i] = TypeUitl.json().fromJson(value, gRestParamSpec.getType());
            }
        }
        return result;
    }
}
