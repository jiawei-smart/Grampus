package org.grampus.swagger;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.grampus.log.GLogger;
import org.grampus.swagger.model.ApiEndpoint;
import org.grampus.swagger.descriptor.EndpointDescriptor;
import org.grampus.swagger.model.Endpoint;
import org.grampus.swagger.model.Info;
import org.grampus.swagger.model.Scheme;
import org.grampus.util.GFileUtil;
import org.grampus.util.GStringUtil;
import spark.ExceptionHandler;
import spark.Filter;
import spark.HaltException;
import spark.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class GSparkSwagger {
    private final Swagger swagger;
    private final Service spark;
    private final GSwaggerOptions options;
    private final String apiPath;

    private GSparkSwagger(final Service spark, final GSwaggerOptions options) {
        this.spark = spark;
        this.swagger = new Swagger();
        this.options = options;
        this.apiPath = this.options.getBasePath();
        this.swagger.setBasePath(this.apiPath);
        this.swagger.setHost(getHostPort());
        this.swagger.setInfo(getInfo());
        this.swagger.schemes(Arrays.asList(Scheme.HTTP,Scheme.HTTPS));
        configDocRoute();
    }

    public String getApiPath() {
        return apiPath;
    }

    public Service getSpark() {
        return spark;
    }

    public static GSparkSwagger of(final Service spark) {
        return new GSparkSwagger(spark, GSwaggerOptions.defaultOptions());
    }

    public static GSparkSwagger of(final Service spark, final GSwaggerOptions options) {
        Objects.requireNonNull(options);
        return new GSparkSwagger(spark, options);
    }

    private void configDocRoute() {
        String uiFolder = getUiFolder(this.apiPath);
        createDir(getSwaggerUiFolder());
        createDir(uiFolder);

        if (options.isEnableStaticMapping()) {
            enableStaticMapping(uiFolder);
        }

        if (options.isEnableCors()) {
//            enableCors();
        }
    }

    private void enableCors() {
        spark.options("/*",
                (request, response) -> {

                    String accessControlRequestHeaders = request
                            .headers("Access-Control-Request-Headers");
                    if (accessControlRequestHeaders != null) {
                        response.header("Access-Control-Allow-Headers",
                                accessControlRequestHeaders);
                    }

                    String accessControlRequestMethod = request
                            .headers("Access-Control-Request-Method");
                    if (accessControlRequestMethod != null) {
                        response.header("Access-Control-Allow-Methods",
                                accessControlRequestMethod);
                    }
                    return "OK";
                });

        spark.before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));
        GLogger.debug("Spark-Swagger: CORS enabled and allow Origin *");
    }

    private void enableStaticMapping(String uiFolder) {
        // Configure static mapping
        spark.externalStaticFileLocation(uiFolder);
        GLogger.debug("Spark-Swagger: UI folder deployed at {}", uiFolder);
    }

    public void generateDoc() throws IOException {
        new GSwaggerSpecBuilder(swagger).build();
        prepareUi(this.options, swagger);
    }

    public ApiEndpoint getEndpoint(final String name) {
        if (swagger.getApiEndpoints() == null) return null;
        for (final ApiEndpoint ep : swagger.getApiEndpoints()) {
            if (name.equals(ep.getEndpointDescriptor().getTag().getName())) return ep;
        }
        return null;
    }

    public ApiEndpoint endpoint(final EndpointDescriptor.Builder descriptorBuilder) {
        return endpoint(descriptorBuilder, null);
    }

    public ApiEndpoint endpoint(final EndpointDescriptor.Builder descriptorBuilder, final Filter filter) {
        Optional.ofNullable(apiPath).orElseThrow(() -> new IllegalStateException("API Path must be specified in order to build REST endpoint"));
        EndpointDescriptor descriptor = descriptorBuilder.build();
        if (filter != null) spark.before(apiPath + descriptor.getPath() + "/*", filter);
        ApiEndpoint apiEndpoint = new ApiEndpoint(this, descriptor);
        this.swagger.addApiEndpoint(apiEndpoint);
        return apiEndpoint;
    }

    public GSparkSwagger endpoint(final EndpointDescriptor.Builder descriptorBuilder, final Filter filter, Consumer<ApiEndpoint> endpointDef) {
        Optional.ofNullable(apiPath).orElseThrow(() -> new IllegalStateException("API Path must be specified in order to build REST endpoint"));
        EndpointDescriptor descriptor = descriptorBuilder.build();
        spark.before(apiPath + descriptor.getPath() + "/*", filter);
        ApiEndpoint apiEndpoint = new ApiEndpoint(this, descriptor);
        endpointDef.accept(apiEndpoint);
        this.swagger.addApiEndpoint(apiEndpoint);
        return this;
    }

    public GSparkSwagger endpoint(final Endpoint endpoint) {
        Optional.ofNullable(endpoint).orElseThrow(() -> new IllegalStateException("API Endpoint cannot be null"));
        endpoint.bind(this);
        return this;
    }

    public GSparkSwagger endpoints(final Supplier<List<Endpoint>> resolver) {
        Optional.ofNullable(resolver).orElseThrow(() -> new IllegalStateException("API Endpoint Resolver cannot be null"));
        resolver.get().forEach(this::endpoint);
        return this;
    }

    public GSparkSwagger before(Filter filter) {
        spark.before(apiPath + "/*", filter);
        return this;
    }

    public GSparkSwagger after(Filter filter) {
        spark.after(apiPath + "/*", filter);
        return this;
    }

    public synchronized GSparkSwagger exception(Class<? extends Exception> exceptionClass, final ExceptionHandler handler) {
        spark.exception(exceptionClass, handler);
        return this;
    }

    public HaltException halt() {
        return spark.halt();
    }

    public HaltException halt(int status) {
        return spark.halt(status);
    }

    public HaltException halt(String body) {
        return spark.halt(body);
    }

    public HaltException halt(int status, String body) {
        return spark.halt(status, body);
    }

    private String getHostPort() {
        String host = this.options.getHost();
        Integer port = this.options.getPort();
        if (GStringUtil.equals(host, "localhost")) {
            host = GSwaggerOptions.resolvePublicIp();
        }
        GLogger.debug("Spark-Swagger: Host resolved to {}", host);
        return host+":"+port;
    }

    private Info getInfo() {
        Info info = new Info();
        info.description(this.options.getDescription());
        info.version(this.options.getVersion());
        info.title(this.options.getTitle());
        info.setContact(this.options.getContact());
        return info;
    }

    public void prepareUi(final GSwaggerOptions config, Swagger swagger) throws IOException {
        GLogger.debug("Spark-Swagger: Start compiling Swagger UI");

        String uiFolder = getUiFolder(config.getBasePath());

        // 1 - Extract UI/Templates folder to a temporary folder
        extractUi(uiFolder);

        // 2 - Decorate index.html according to configurations
        decorateIndex(uiFolder, swagger.getHost());

        // 3 - Save new Index to UI folder
//        saveFile(uiFolder, "index.html", newIndex);

        // 4 - Parse Swagger definitions and save it to UI folder
//        parseJs(swagger, uiFolder + "swagger-spec.js");
//        parseYaml(swagger, uiFolder + "doc.yaml");
        parseJson(swagger, uiFolder + "doc.json");

    }

    private void decorateIndex(String uiFolder, String host) {
        try{
            String fileContent = readFile(uiFolder, "swagger-initializer.js", StandardCharsets.UTF_8);
            String updatedFileContent = fileContent.replace("${HOST_PORT}", host);
            saveFile(uiFolder,"swagger-initializer.js",updatedFileContent);
        } catch (IOException e) {
            GLogger.debug("Spark-Swagger: UI resources and templates successfully extracted");
        }
    }

    private void extractUi(String uiFolder) throws IOException {
        CodeSource src = GSparkSwagger.class.getProtectionDomain().getCodeSource();
        if (src != null) {
            URL location = src.getLocation();
            GFileUtil.extractFileFromJar(location.toString().replace("file:/", ""), "swagger-ui",uiFolder);

        }
        GLogger.debug("Spark-Swagger: UI resources and templates successfully extracted");
    }


    public List<String> filesFromDir(String prefix, String dir) throws IOException {
        String rootPath = dir.replace("file:/", "");

        try (Stream<Path> stream = Files.walk(Paths.get(rootPath).toAbsolutePath())) {
            return stream
                    .filter(path -> !Files.isDirectory(path))
                    .map(path -> path.toString().replace(rootPath, ""))
                    .filter(name -> !name.equals(prefix) && name.startsWith(prefix))
                    .collect(Collectors.toList());
        }
    }

    private List<String> filesFromJar(String prefix, URL location)  {
        List<String> uiFiles = new ArrayList<>();
        try (ZipInputStream zip = new ZipInputStream(location.openStream())) {
            while (true) {
                ZipEntry e = zip.getNextEntry();
                if (e == null) break;
                String name = e.getName();
                if (name.startsWith(prefix)) {
                    uiFiles.add("/"+name);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return uiFiles;
    }

    private String readFile(String uiFolder, String name, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(uiFolder + name));
        return new String(encoded, encoding);
    }

    private void saveFile(String uiFolder, String fileName, String content) throws IOException {
        File file = new File(uiFolder + fileName);
        file.delete();

        FileWriter f2 = new FileWriter(file, false);
        f2.write(content);
        f2.close();
        GLogger.debug("Spark-Swagger: Swagger UI file " + fileName + " successfully saved");
    }

    public static String getUiFolder(String basePath) {
        if (basePath.isEmpty()) {
            return getSwaggerUiFolder();
        }
        String formattedPath = (basePath.startsWith("/") ? basePath.replaceFirst("/", "") : basePath) + (basePath.endsWith("/") ? "" : "/");
        return getSwaggerUiFolder() + formattedPath;
    }

    public static String getSwaggerUiFolder() {
        return GFileUtil.getResourcesPath()+File.separator+ "swagger-ui" +File.separator;
    }

    public static void createDir(final String path) {
        File uiFolder = new File(path);
        if (!uiFolder.exists()) {
            uiFolder.mkdir();
        }
    }

    public static void parseYaml(final Swagger swagger, final String filePath) throws IOException {
        GLogger.debug("Spark-Swagger: Start parsing Swagger definitions");
        // Create an ObjectMapper mapper for YAML
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        // Write object as YAML file
        mapper.writeValue(new File(filePath), swagger);
        GLogger.debug("Spark-Swagger: Swagger definitions saved as {} [YAML]", filePath);
    }

    public static void parseJson(final Swagger swagger, final String filePath) throws IOException {
        GLogger.debug("Spark-Swagger: Start parsing Swagger definitions");
        // Create an ObjectMapper mapper for JSON
        ObjectMapper mapper = new ObjectMapper(new JsonFactory());
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        mapper.writeValue(new File(filePath), swagger);
        GLogger.debug("Spark-Swagger: Swagger definitions saved as " + filePath + " [JSON]");
    }

    public static void parseJs(final Swagger swagger, final String filePath) throws IOException {
        GLogger.debug("Spark-Swagger: Start parsing Swagger definitions");
        // Create an ObjectMapper mapper for JSON
        ObjectMapper mapper = new ObjectMapper(new JsonFactory());
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        String js = mapper.writeValueAsString(swagger);
        js = "window.swaggerSpec=" + js;

        File uiFolder = new File(filePath);
        uiFolder.delete();

        try (PrintWriter out = new PrintWriter(filePath)) {
            out.println(js);
        }
        GLogger.debug("Spark-Swagger: Swagger definitions saved as " + filePath + " [JS]");
    }
}
