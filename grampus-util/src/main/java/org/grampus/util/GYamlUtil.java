package org.grampus.util;

import org.grampus.log.GLogger;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.env.EnvScalarConstructor;
import org.yaml.snakeyaml.inspector.TrustedTagInspector;
import org.yaml.snakeyaml.inspector.UnTrustedTagInspector;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

public class GYamlUtil {

    public static Map load(String configYaml){
        String configYamlPath = GYamlUtil.class.getClassLoader().getResource(configYaml).getPath();
        File file = new File(configYamlPath);
        if(file.exists()){
            try {
                InputStream inputStream = new FileInputStream(configYamlPath);
                Yaml yaml = new Yaml(buildConstructor());
                return yaml.load(inputStream);
            } catch (FileNotFoundException e) {
                GLogger.error("Failure to load yaml file: [{}], {}",configYaml,e);
            }
        }else {
            GLogger.warn("Cannot found the workflow config yaml file");
        }
        return null;

    }

    public static <T> T load(String yamlFile, Class<T> type){
        String configYamlPath = GYamlUtil.class.getClassLoader().getResource(yamlFile).getPath();
        try {
            InputStream inputStream = new FileInputStream(configYamlPath);
            Yaml yaml = new Yaml(buildConstructor());
            return yaml.loadAs(inputStream,type);
        } catch (FileNotFoundException e) {
            GLogger.error("Failure to load yaml file: [{}], with {}",yamlFile,e);
        }
        return null;
    }

    private static <T> EnvScalarConstructor buildConstructor() {
        LoaderOptions loaderOptions = new LoaderOptions();
        loaderOptions.setTagInspector(new TrustedTagInspector());
        return new EnvScalarConstructor(new TypeDescription(Object.class),null,loaderOptions){
            @Override
            public String getEnv(String key) {
                String result = super.getEnv(key);
                if(GStringUtil.isEmpty(result)){
                    result = System.getProperty(key);
                }
                return result;
            }
        };
    }
}
