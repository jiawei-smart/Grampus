package org.grampus.swagger;

import org.grampus.swagger.config.model.Operation;
import org.grampus.swagger.config.model.Swagger;
import org.grampus.swagger.model.properties.ArrayProperty;
import org.grampus.swagger.model.properties.MapProperty;
import org.grampus.swagger.spec.MethodSpec;
import org.grampus.swagger.spec.ParameterSpec;
import org.grampus.swagger.model.*;
import org.grampus.swagger.factory.DefinitionsFactory;
import org.grampus.swagger.factory.ParamsFactory;
import org.grampus.swagger.config.model.parameters.BodyParameter;
import org.grampus.swagger.config.model.parameters.Parameter;
import org.grampus.swagger.model.properties.Property;
import org.grampus.log.GLogger;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class GSwaggerSpecBuilder {

    private final Swagger swagger;

    public GSwaggerSpecBuilder(Swagger swagger) {
        this.swagger = swagger;
    }

    public void build() {
        GLogger.debug("GSwagger: Start parsing metadata");
        if (swagger.getApiEndpoints() != null) {
            swagger.getApiEndpoints().forEach(endpoint -> {
                swagger.tag(endpoint.getEndpointDescriptor().getTag());
                endpoint.getMethodDescriptors()
                        .forEach(methodDescriptor -> {

                            Operation op = new Operation();
                            op.tag(endpoint.getEndpointDescriptor().getTag().getName());
                            op.description(methodDescriptor.getDescription());

                            Optional.ofNullable(methodDescriptor.getSummary()).ifPresent(op::summary);
                            Optional.ofNullable(methodDescriptor.getOperationId()).ifPresent(op::operationId);

                            List<Parameter> parameters = ParamsFactory.create(methodDescriptor.getPath(), methodDescriptor.getParameters());
                            op.setParameters(parameters);

                            final ParameterSpec methodBody = methodDescriptor.getBody();
                            if (methodBody != null) {
                                buildRequestFromType(methodDescriptor, op, methodBody);
                            }

                            if (methodDescriptor.getResponseType() != null) {
                                Response responseBody = buildResponseFromType(methodDescriptor);
                                op.addResponse("200", responseBody);
                            } else {
                                Response responseBody = new Response();
                                responseBody.description("successful operation");
                                op.addResponse("200", responseBody);
                            }

                            swagger.addOperation(methodDescriptor.getPath(), methodDescriptor.getMethod(), op);
                        });
            });
            GLogger.debug("GSwagger: metadata successfully parsed");
        } else {
            GLogger.debug("GSwagger: No metadata to parse. Please check your SparkSwagger configurations and Endpoints Resolver");
        }
    }
    private void buildRequestFromType(MethodSpec methodSpec, Operation op, ParameterSpec methodBody) {
        Property property = DefinitionsFactory.createProperty(null, methodSpec.getRequestType());
        Model model = new PropertyModelConverter().propertyToModel(property);
        BodyParameter requestBody = createBodyParameter(methodBody, model);
        op.addParameter(requestBody);
    }

    private Response buildResponseFromType(MethodSpec methodDescriptor) {
        PropertyModelConverter propertyModelConverter = new PropertyModelConverter();

        final Property property = DefinitionsFactory.createProperty(null, methodDescriptor.getResponseType());

        Response responseBody = new Response();
        responseBody.description("successful operation");

        if (methodDescriptor.isResponseAsCollection()) {
            ArrayProperty arrayProperty = new ArrayProperty();
            arrayProperty.setItems(property);
            responseBody.setSchema(arrayProperty);
            responseBody.setResponseSchema(propertyModelConverter.propertyToModel(arrayProperty));
        } else if (methodDescriptor.isResponseAsMap()) {
            MapProperty mapProperty = new MapProperty(property);
            mapProperty.additionalProperties(property);
            responseBody.setSchema(mapProperty);
            responseBody.setResponseSchema(propertyModelConverter.propertyToModel(mapProperty));
        } else {
            responseBody.setSchema(property);
            responseBody.setResponseSchema(propertyModelConverter.propertyToModel(property));
        }
        return responseBody;
    }
    private BodyParameter createBodyParameter(ParameterSpec methodBody, Model model) {
        BodyParameter requestBody = new BodyParameter();
        requestBody.name(methodBody != null ? methodBody.getName() : "body");
        requestBody.description(methodBody != null ? methodBody.getDescription() : "Body object description");
        requestBody.setRequired(methodBody == null || methodBody.isRequired());
        requestBody.setSchema(model);
        return requestBody;
    }
}
