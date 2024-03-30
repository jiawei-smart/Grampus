package org.grampus.swagger.factory;

import org.grampus.swagger.config.model.parameters.*;
import org.grampus.swagger.spec.ParameterSpec;
import org.grampus.swagger.model.properties.*;
import spark.utils.SparkUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class ParamsFactory {

    public static List<Parameter> create(final String pathUri, final List<ParameterSpec> parameterSpecs) {
        List<Parameter> parameters = new ArrayList<>();
        List<Parameter> queryParams = createQueryParams(parameterSpecs);
        parameters.addAll(queryParams);
        List<Parameter> headerParams = createHeaderParams(parameterSpecs);
        parameters.addAll(headerParams);
        List<Parameter> formParams = createFormParams(parameterSpecs);
        parameters.addAll(formParams);
        List<Parameter> cookieParams = createCookieParams(parameterSpecs);
        parameters.addAll(cookieParams);
        return parameters;
    }

    private static List<Parameter> createQueryParams(List<ParameterSpec> parameterSpecs) {
        return filter(parameterSpecs, ParameterSpec.ParameterType.QUERY)
                .stream()
                .map(ParamsFactory::toQuery)
                .collect(Collectors.toList());
    }

    private static List<Parameter> createHeaderParams(List<ParameterSpec> parameterSpecs) {
        return filter(parameterSpecs, ParameterSpec.ParameterType.HEADER)
                .stream()
                .map(ParamsFactory::toHeader)
                .collect(Collectors.toList());
    }

    private static List<Parameter> createFormParams(List<ParameterSpec> parameterSpecs) {
        return filter(parameterSpecs, ParameterSpec.ParameterType.FORM)
                .stream()
                .map(ParamsFactory::toForm)
                .collect(Collectors.toList());
    }

    private static List<Parameter> createCookieParams(List<ParameterSpec> parameterSpecs) {
        return filter(parameterSpecs, ParameterSpec.ParameterType.COOKIE)
                .stream()
                .map(ParamsFactory::toCookie)
                .collect(Collectors.toList());
    }

    public static String formatPath(String pathUri) {
        if (containsParam(pathUri)) {
            StringBuilder formatted = new StringBuilder();

            List<String> uriParts = SparkUtils.convertRouteToList(pathUri);
            for (String uriPart : uriParts) {
                try {
                    formatted.append("/");
                    if (SparkUtils.isParam(uriPart)) {
                        final String param = URLDecoder.decode(uriPart, "UTF-8");
                        final String decodedParam = param.contains(":") ? param.substring(param.indexOf(":") + 1) : param;
                        formatted.append("{").append(decodedParam).append("}");
                    } else {
                        formatted.append(uriPart);
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            String path = formatted.toString();
            return path.isEmpty() ? "/" : path;
        } else {
            return pathUri;
        }
    }

    public static boolean containsParam(String routePart) {
        return routePart.contains(":");
    }

    private static QueryParameter toQuery(final ParameterSpec parameterSpec) {
        QueryParameter parameter = new QueryParameter();
        bindAttributes(parameterSpec, parameter);
        return parameter;
    }

    private static HeaderParameter toHeader(final ParameterSpec parameterSpec) {
        HeaderParameter parameter = new HeaderParameter();
        bindAttributes(parameterSpec, parameter);
        return parameter;
    }

    private static FormParameter toForm(final ParameterSpec parameterSpec) {
        FormParameter parameter = new FormParameter();
        bindAttributes(parameterSpec, parameter);
        return parameter;
    }

    private static CookieParameter toCookie(final ParameterSpec parameterSpec) {
        CookieParameter parameter = new CookieParameter();
        bindAttributes(parameterSpec, parameter);
        return parameter;
    }

    private static void bindAttributes(ParameterSpec parameterSpec, AbstractSerializableParameter parameter) {
        parameter.setName(parameterSpec.getName());
        parameter.setDescription(parameterSpec.getDescription());
        parameter.setPattern(parameterSpec.getPattern());
        parameter.setRequired(parameterSpec.isRequired());
        parameter.setExample(parameterSpec.getExample());
        parameter.setAllowEmptyValue(parameterSpec.getAllowEmptyValue());
        parameter.setDefaultValue(parameterSpec.getDefaultValue());
        if (parameterSpec.getObject() != null) {
            parameter.setProperty(createProperty(parameterSpec.getObject()));
        } else if (parameterSpec.getCollectionOf() != null) {
            Property itemProperty = createProperty(parameterSpec.getCollectionOf());
            ArrayProperty collectionProperty = new ArrayProperty();
            collectionProperty.setItems(itemProperty);
            parameter.setProperty(collectionProperty);
        } else {
            parameter.setProperty(createProperty(String.class));
        }
    }

    private static Property createProperty(Class type) {
        if (type.isEnum()) {
            StringProperty property = new StringProperty();
            property._enum(Stream.of(type.getEnumConstants()).map(o -> ((Enum) o).name()).collect(Collectors.toList()));
            return property;
        } else if (type.equals(boolean.class) || type.equals(Boolean.class)) {
            return new BooleanProperty();
        } else if (type == byte[].class) {
            return new ByteArrayProperty();
        } else if (type.equals(Number.class)) {
            return new DecimalProperty();
        } else if (type.equals(Double.class) || type.equals(double.class)) {
            return new DoubleProperty();
        } else if (type.equals(Float.class) || type.equals(float.class)) {
            return new FloatProperty();
        } else if (type.equals(Integer.class) || type.equals(int.class)) {
            return new IntegerProperty();
        } else if (type.equals(Long.class) || type.equals(long.class)) {
            return new LongProperty();
        } else if (type.equals(String.class)) {
            return new StringProperty();
        } else if (type.equals(UUID.class)) {
            return new UUIDProperty();
        }
        throw new UnsupportedOperationException("Only 'String' or 'primitive' types can be used as parameters");
    }

    private static List<ParameterSpec> filter(List<ParameterSpec> params, ParameterSpec.ParameterType type) {
        return params.stream().filter(parameterDescriptor -> parameterDescriptor.getType() == type).collect(Collectors.toList());
    }
}
