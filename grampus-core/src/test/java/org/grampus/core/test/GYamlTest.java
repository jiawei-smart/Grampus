package org.grampus.core.test;

import org.grampus.core.GCellOptions;
import org.grampus.core.util.GYaml;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class GYamlTest {

    @Test
    public void loadTest(){
        Map config = GYaml.load("app.yaml");
        Assert.assertTrue(config.get("S1") instanceof Map);
        Assert.assertEquals(((Map)config.get("S1")).get("key"),"value");
    }

    @Test
    public void loadTypeTest(){
        GCellOptions cellOptions = GYaml.load("cellOptions.yaml", GCellOptions.class);
        Assert.assertTrue(cellOptions != null);
        Assert.assertEquals(cellOptions.getBatchSize(), 5);
        Assert.assertEquals(cellOptions.getParallel(),2);
    }
    @Test
    public void envTest(){
        System.setProperty("myEnv","my.env.value");
        Map config = GYaml.load("app.yaml");
        Assert.assertEquals(((Map)config.get("S1")).get("env"),"my.env.value");
    }

}
