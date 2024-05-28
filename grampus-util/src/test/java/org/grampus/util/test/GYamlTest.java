package org.grampus.util.test;

import org.grampus.util.GYamlUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class GYamlTest {

    @Test
    public void loadTest(){
        Map config = GYamlUtil.load("app.yaml");
        Assertions.assertTrue(config.get("S1") instanceof Map);
        Assertions.assertEquals(((Map)config.get("S1")).get("key"),"value");
    }

    @Test
    public void loadTypeTest(){
        YamlTestModel type = GYamlUtil.load("typeConfig.yaml", YamlTestModel.class);
        Assertions.assertTrue(type != null);
        Assertions.assertEquals(type.getAge(), 5);
        Assertions.assertEquals(type.getName(),"test");
    }
    @Test
    public void envTest(){
        System.setProperty("myEnv","my.env.value");
        Map config = GYamlUtil.load("app.yaml");
        Assertions.assertEquals(((Map)config.get("S1")).get("env"),"my.env.value");
    }

}
