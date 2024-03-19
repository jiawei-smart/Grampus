package org.grampus.core.util;

import org.grampus.log.GLogger;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.env.EnvScalarConstructor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

public class GYaml {

    public static Map load(String configYaml){
        String configYamlPath = GYaml.class.getClassLoader().getResource(configYaml).getPath();
        try {
            InputStream inputStream = new FileInputStream(configYamlPath);
            Yaml yaml = new Yaml(buildConstructor(Map.class));
            return yaml.load(inputStream);
        } catch (FileNotFoundException e) {
            GLogger.error("Failure to load yaml file: [{}], {}",configYaml,e);
        }
        return null;
    }

    public static <T> T load(String yamlFile, Class<T> type){
        String configYamlPath = GYaml.class.getClassLoader().getResource(yamlFile).getPath();
        try {
            InputStream inputStream = new FileInputStream(configYamlPath);
            Yaml yaml = new Yaml(buildConstructor(type));
            return yaml.load(inputStream);
        } catch (FileNotFoundException e) {
            GLogger.error("Failure to load yaml file: [{}], with {}",yamlFile,e);
        }
        return null;
    }

    private static <T> EnvScalarConstructor buildConstructor(Class<T> type) {
        return new EnvScalarConstructor(new TypeDescription(type),null,new LoaderOptions()){
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
