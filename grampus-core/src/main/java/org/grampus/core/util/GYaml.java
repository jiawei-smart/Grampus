package org.grampus.core.util;

import org.grampus.log.GLogger;
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
            Yaml yaml = new Yaml(new EnvScalarConstructor());
            return yaml.load(inputStream);
        } catch (FileNotFoundException e) {
            GLogger.error("Failure to load yaml file: [{}], {}",configYaml,e);
        }
        return null;
    }
}
