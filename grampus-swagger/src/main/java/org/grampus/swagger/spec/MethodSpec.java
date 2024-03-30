package org.grampus.swagger.spec;


import org.grampus.swagger.model.HttpMethod;
import org.grampus.swagger.model.Response;

import java.util.*;

/**
 * @author manusant
 */
public class MethodSpec {

    private HttpMethod method;
    private String path;
    private String summary;
    private String description;
    private Class<?> requestType;
    private boolean requestAsCollection;
    private ParameterSpec body;
    private Class<?> responseType;
    private boolean responseAsCollection;
    private boolean responseAsMap;
    private String operationId;
    private List<String> consumes;
    private List<String> produces;
    private List<ParameterSpec> parameters = new ArrayList<>();
    private Map<String, Response> responses;
    private Boolean deprecated;

    private List<Map<String, List<String>>> security;

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Class<?> getRequestType() {
        return requestType;
    }

    public void setRequestType(Class<?> requestType) {
        this.requestType = requestType;
    }

    public boolean isRequestAsCollection() {
        return requestAsCollection;
    }

    public void setRequestAsCollection(boolean requestAsCollection) {
        this.requestAsCollection = requestAsCollection;
    }

    public ParameterSpec getBody() {
        return body;
    }

    public void setBody(ParameterSpec body) {
        this.body = body;
    }

    public Class<?> getResponseType() {
        return responseType;
    }

    public void setResponseType(Class<?> responseType) {
        this.responseType = responseType;
    }

    public boolean isResponseAsCollection() {
        return responseAsCollection;
    }

    public void setResponseAsCollection(boolean responseAsCollection) {
        this.responseAsCollection = responseAsCollection;
    }

    public boolean isResponseAsMap() {
        return responseAsMap;
    }

    public void setResponseAsMap(boolean responseAsMap) {
        this.responseAsMap = responseAsMap;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public List<String> getConsumes() {
        return consumes;
    }

    public void setConsumes(List<String> consumes) {
        this.consumes = consumes;
    }

    public List<String> getProduces() {
        return produces;
    }

    public void setProduces(List<String> produces) {
        this.produces = produces;
    }

    public List<ParameterSpec> getParameters() {
        return parameters;
    }

    public void setParameters(List<ParameterSpec> parameters) {
        this.parameters = parameters;
    }

    public Map<String, Response> getResponses() {
        return responses;
    }

    public void setResponses(Map<String, Response> responses) {
        this.responses = responses;
    }

    public Boolean getDeprecated() {
        return deprecated;
    }

    public void setDeprecated(Boolean deprecated) {
        this.deprecated = deprecated;
    }

    public List<Map<String, List<String>>> getSecurity() {
        return security;
    }

    public void setSecurity(List<Map<String, List<String>>> security) {
        this.security = security;
    }

    public static Builder path(String path) {
        return new Builder().withPath(path);
    }

    public static Builder path() {
        return new Builder().withPath("");
    }

    public static final class Builder {
        private HttpMethod method;
        private String path;
        private String summary;
        private String description;
        private Class requestType;
        private boolean requestAsCollection;
        private ParameterSpec body;
        private Class responseType;
        private boolean responseAsCollection;
        private boolean responseAsMap;
        private String operationId;
        private List<String> consumes;
        private List<String> produces;
        private List<ParameterSpec> parameters = new ArrayList<>();
        private Map<String, Response> responses;
        private Boolean deprecated;

        private List<Map<String, List<String>>> security;

        private Builder() {
        }

        public static Builder newBuilder() {
            return new Builder();
        }

        public Builder withMethod(HttpMethod method) {
            this.method = method;
            return this;
        }

        public Builder withPath(String path) {
            this.path = path;
            return this;
        }

        public Builder withSummary(String summary) {
            this.summary = summary;
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder withRequestType(Class requestType) {
            this.requestType = requestType;
            return this;
        }

        public Builder withBody(ParameterSpec body) {
            this.body = body;
            return this;
        }

        public Builder withRequestAsCollection(Class itemType) {
            this.requestAsCollection = true;
            this.requestType = itemType;
            return this;
        }

        public Builder withResponseType(Class responseType) {
            this.responseType = responseType;
            return this;
        }

        public Builder withResponseAsCollection(Class itemType) {
            this.responseAsCollection = true;
            this.responseType = itemType;
            return this;
        }

        public Builder withResponseAsMap(Class itemType) {
            this.responseAsMap = true;
            this.responseType = itemType;
            return this;
        }

        public Builder withOperationId(String operationId) {
            this.operationId = operationId;
            return this;
        }

        public Builder withConsumes(List<String> consumes) {
            this.consumes = consumes;
            return this;
        }

        public Builder withProduces(List<String> produces) {
            this.produces = produces;
            return this;
        }

        public Builder withParams(List<ParameterSpec> parameters) {
            this.parameters = parameters;
            return this;
        }

        public Builder withParam(ParameterSpec parameter) {
            this.parameters.add(parameter);
            return this;
        }

        public Builder withParam(ParameterSpec.ParameterType type, ParameterSpec param) {
            param.setType(type);
            this.parameters.add(param);
            return this;
        }

        public ParameterSpec.Builder withPathParam() {
            return ParameterSpec.newBuilder(this).withType(ParameterSpec.ParameterType.PATH);
        }

        public Builder withPathParam(ParameterSpec param) {
            param.setType(ParameterSpec.ParameterType.PATH);
            this.parameters.add(param);
            return this;
        }

        public ParameterSpec.Builder withQueryParam() {
            return ParameterSpec.newBuilder(this).withType(ParameterSpec.ParameterType.QUERY);
        }

        public Builder withQueryParam(ParameterSpec param) {
            param.setType(ParameterSpec.ParameterType.QUERY);
            this.parameters.add(param);
            return this;
        }

        public ParameterSpec.Builder withHeaderParam() {
            return ParameterSpec.newBuilder(this).withType(ParameterSpec.ParameterType.HEADER);
        }

        public Builder withHeaderParam(ParameterSpec param) {
            param.setType(ParameterSpec.ParameterType.HEADER);
            this.parameters.add(param);
            return this;
        }

        public ParameterSpec.Builder withFormParam() {
            return ParameterSpec.newBuilder(this).withType(ParameterSpec.ParameterType.FORM);
        }

        public Builder withFormParam(ParameterSpec param) {
            param.setType(ParameterSpec.ParameterType.FORM);
            this.parameters.add(param);
            return this;
        }

        public ParameterSpec.Builder withCookieParam() {
            return ParameterSpec.newBuilder(this).withType(ParameterSpec.ParameterType.COOKIE);
        }

        public Builder withCookieParam(ParameterSpec param) {
            param.setType(ParameterSpec.ParameterType.COOKIE);
            this.parameters.add(param);
            return this;
        }

        public Builder withResponses(Map<String, Response> responses) {
            this.responses = responses;
            return this;
        }

        public Builder withDeprecated(Boolean deprecated) {
            this.deprecated = deprecated;
            return this;
        }

        public Builder withSecurity(List<Map<String, List<String>>> security) {
            this.security = security;
            return this;
        }

        public Builder withSecurity(String name, List<String> scopes) {
            if (this.security == null) {
                this.security = new ArrayList<>();
            }
            Map<String, List<String>> req = new LinkedHashMap<>();
            if (scopes == null) {
                scopes = new ArrayList<>();
            }
            req.put(name, scopes);
            this.security.add(req);
            return this;
        }

        public Builder withSecurity(String name) {
            this.withSecurity(name, Collections.emptyList());
            return this;
        }

        public MethodSpec build() {
            MethodSpec methodSpec = new MethodSpec();
            methodSpec.setMethod(method);
            methodSpec.setPath(path);
            methodSpec.setSummary(summary);
            methodSpec.setDescription(description);
            methodSpec.setRequestType(requestType);
            methodSpec.setRequestAsCollection(requestAsCollection);
            methodSpec.setBody(body);
            methodSpec.setResponseType(responseType);
            methodSpec.setResponseAsCollection(responseAsCollection);
            methodSpec.setResponseAsMap(responseAsMap);
            methodSpec.setOperationId(operationId);
            methodSpec.setConsumes(consumes);
            methodSpec.setProduces(produces);
            methodSpec.setParameters(parameters);
            methodSpec.setResponses(responses);
            methodSpec.setDeprecated(deprecated);
            methodSpec.setSecurity(security);
            return methodSpec;
        }
    }
}
