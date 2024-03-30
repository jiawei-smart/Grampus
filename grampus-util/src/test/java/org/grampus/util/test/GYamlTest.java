package org.grampus.util.test;

import org.grampus.util.GYamlUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class GYamlTest {

    @Test
    public void loadTest(){
        Map config = GYamlUtil.load("app.yaml");
        Assert.assertTrue(config.get("S1") instanceof Map);
        Assert.assertEquals(((Map)config.get("S1")).get("key"),"value");
    }

    @Test
    public void loadTypeTest(){
        YamlTestModel type = GYamlUtil.load("typeConfig.yaml", YamlTestModel.class);
        Assert.assertTrue(type != null);
        Assert.assertEquals(type.getAge(), 5);
        Assert.assertEquals(type.getName(),"test");
    }
    @Test
    public void envTest(){
        System.setProperty("myEnv","my.env.value");
        Map config = GYamlUtil.load("app.yaml");
        Assert.assertEquals(((Map)config.get("S1")).get("env"),"my.env.value");
    }

}
