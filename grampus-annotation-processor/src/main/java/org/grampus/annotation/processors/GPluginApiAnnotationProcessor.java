package org.grampus.annotation.processors;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.*;
import javassist.compiler.ast.Symbol;
import org.grampus.core.GAdaptor;
import org.grampus.core.annotation.plugin.GPluginApi;
import org.grampus.core.annotation.plugin.GPluginMethod;

import javax.annotation.processing.*;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@SupportedAnnotationTypes("org.grampus.core.annotation.plugin.GPluginApi")
public class GPluginApiAnnotationProcessor extends AbstractProcessor {
    private Messager messager;
    private Filer filer;
    private Types typeUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        //processingEnvironment提供各种工具类  如Elements Filer Types SourceVersion等
        super.init(processingEnvironment);
        messager = processingEnv.getMessager();
        filer = processingEnv.getFiler();
        typeUtils = processingEnv.getTypeUtils();
    }
//    public String process(Class interfaceName) throws IOException {
//        return javaFile.toString();
//    }

//    public static void main(String[] args) throws IOException {
//        System.out.println(new GPluginApiAnnotationProcessor().process(TestInterface.class));
//    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
//        for(Element element: roundEnv.getElementsAnnotatedWith(GPluginApi.class)){
//            messager.printMessage(Diagnostic.Kind.NOTE, "Current element: " + element.getSimpleName());
//            if (element.getKind() != ElementKind.CLASS && element.getKind() != ElementKind.INTERFACE) {
//                messager.printMessage(
//                        Diagnostic.Kind.ERROR,
//                        String.format("Only class can be annotated with @%s", GPluginApi.class.getSimpleName()),
//                        element);
//                return false;
//            }
//
//            if (!element.getModifiers().contains(Modifier.PUBLIC)) {
//                messager.printMessage(Diagnostic.Kind.ERROR, "Subscriber class must be public", element);
//                return false;
//            }
//
//            Elements elementUtils = processingEnv.getElementUtils();
//            String packageName = elementUtils.getPackageOf(element).getQualifiedName().toString();
//            String elementName = element.getSimpleName().toString();
//            ClassName builderClassName = ClassName.get(packageName, String.format("%sBuilder", elementName));
//
////            TypeSpec typeSpec = createTypeSpec(element, builderClassName, elementName);
//            JavaFile javaFile = JavaFile.builder(packageName, typeSpec).build();
//            try {
//                javaFile.writeTo(filer);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

//        MethodSpec sendMethod = MethodSpec.methodBuilder("send")
//                .addModifiers(Modifier.PRIVATE)
//                .returns(void.class)
//                .addParameter(Object.class, "payload")
//                .addStatement("this.adaptor.publishMessage($S,payload, null)", "pluginEvent")
//                .build();
//        MethodSpec setGAdaptorMethod = MethodSpec.methodBuilder("setGAdaptor")
//                .addModifiers(Modifier.PUBLIC)
//                .returns(void.class)
//                .addParameter(GAdaptor.class, "adaptor")
//                .addStatement("this.adaptor=adaptor")
//                .build();
//        String interfaceName = "test";
//        TypeSpec.Builder helloWorldClassBuilder = TypeSpec.classBuilder(interfaceName + "Wrapper")
//                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
//                .addAnnotation(AnnotationSpec.builder(AutoService.class).addMember("value", "$T.class", interfaceName).build())
////                .addSuperinterface(interfaceName)
//                .addField(FieldSpec.builder(GAdaptor.class, "adaptor", Modifier.PRIVATE).build())
//                .addMethod(sendMethod)
//                .addMethod(setGAdaptorMethod);
//
//        Method[] methods = interfaceName.getDeclaredMethods();
//        if(methods != null && methods.length > 0){
//            for(Method method : methods){
//                if(method.getAnnotation(GPluginApiMethod.class) != null){
//                    MethodSpec.Builder methodSpecBuidler = MethodSpec.methodBuilder(method.getName()).addAnnotation(GPluginApiMethod.class);
//                    methodSpecBuidler.returns(method.getReturnType());
//                    if(method.getParameterCount() > 0){
//                        Arrays.stream(method.getParameters()).map(parameter->methodSpecBuidler.addParameter(parameter.getType(),parameter.getName()));
//                    }
//                    if(method.getExceptionTypes() != null){
//                        Arrays.stream(method.getExceptionTypes()).map(exception->methodSpecBuidler.addException(exception));
//                    }
//                    if (method.getReturnType()!=null){
//                        methodSpecBuidler.returns(method.getReturnType());
//                    }
//                    switch (method.getModifiers()){
//                        case java.lang.reflect.Modifier.PUBLIC: methodSpecBuidler.addModifiers(Modifier.PUBLIC);break;
//                        case java.lang.reflect.Modifier.PRIVATE: methodSpecBuidler.addModifiers(Modifier.PRIVATE);break;
//                        case java.lang.reflect.Modifier.PROTECTED: methodSpecBuidler.addModifiers(Modifier.PROTECTED);break;
//                        case java.lang.reflect.Modifier.SYNCHRONIZED: methodSpecBuidler.addModifiers(Modifier.SYNCHRONIZED);break;
//                        default:methodSpecBuidler.addModifiers(Modifier.PUBLIC);break;
//                    }
//                    helloWorldClassBuilder.addMethod(methodSpecBuidler.build());
//                }
//
//            }
//        }
//
//        JavaFile javaFile = JavaFile.builder("com.example", helloWorldClassBuilder.build())
//                .build();
//        try {
//            javaFile.writeTo(Path.of("D:\\git\\Grampus\\grampus-plugin\\target\\generated-sources\\annotations"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        messager.printMessage(Diagnostic.Kind.NOTE, "----------start----------");

        return true;
    }

//    private TypeSpec createTypeSpec(Element element, ClassName builderClassName, String elementName) {
//        Set<ExecutableElement> pluginAPIMethodElement = getPluginAPIMethod(element);

//        Set<Element> fieldElements = getFields(element);
//        List<FieldSpec> fieldSpecs = new ArrayList<>(fieldElements.size());
//        List<MethodSpec> setterSpecs = new ArrayList<>(fieldElements.size());
//        for (Element field : fieldElements) {
//            TypeName fieldType = TypeName.get(field.asType());
//            String fieldName = field.getSimpleName().toString();
//            FieldSpec fieldSpec = FieldSpec.builder(fieldType, fieldName, Modifier.PRIVATE).build();
//            fieldSpecs.add(fieldSpec);
//            MethodSpec setterSpec = MethodSpec
//                    .methodBuilder(fieldName)
//                    .addModifiers(Modifier.PUBLIC)
//                    .returns(builderClassName)
//                    .addParameter(fieldType, fieldName)
//                    .addStatement("this.$N = $N", fieldName, fieldName)
//                    .addStatement("return this")
//                    .build();
//            setterSpecs.add(setterSpec);
//        }
//        TypeName elementType = TypeName.get(element.asType());
//        String instanceName = instanceName(elementName);
//        MethodSpec.Builder buildMethodBuilder = MethodSpec
//                .methodBuilder("build")
//                .addModifiers(Modifier.PUBLIC)
//                .returns(elementType)
//                .addStatement("$1T $2N = new $1T()", elementType, instanceName);
//        for (FieldSpec fieldSpec : fieldSpecs) {
//            buildMethodBuilder.addStatement("$1N.$2N = $2N", instanceName, fieldSpec);
//        }
//        buildMethodBuilder.addStatement("return $N", instanceName);
//        MethodSpec buildMethod = buildMethodBuilder.build();
//        return TypeSpec
//                .classBuilder(builderClassName)
//                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
//                .addFields(fieldSpecs)
//                .addMethods(setterSpecs)
//                .addMethod(buildMethod)
//                .build();
//    }

    private String instanceName(String type) {
        if (type == null || type.isEmpty()) {
            return "";
        }
        if (type.length() == 1) {
            return type.toLowerCase();
        }
        return type.substring(0, 1).toLowerCase() + type.substring(1);
    }

    private Set<ExecutableElement> getPluginAPIMethod(Element element) {
        Set<ExecutableElement> methods = new LinkedHashSet<>();
        for (Element enclosedElement : element.getEnclosedElements()) {
            if (enclosedElement.getKind() == ElementKind.METHOD) {
                ExecutableElement executableElement = (ExecutableElement)enclosedElement;
                if(enclosedElement.getAnnotation(GPluginMethod.class)!=null){
                    methods.add(executableElement);
                }
            }
        }
        return methods;
    }
}

