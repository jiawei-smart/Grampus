package org.grampus.util;

import com.google.gson.GsonBuilder;
import com.google.gson.Gson;

import java.util.Map;

public class GJsonUtil {
    private static Gson GSON = new GsonBuilder().setPrettyPrinting().create();;

    public static Object fromJson(String payload, Class type) {
        return GSON.fromJson(payload,type);
    }

    public static String map2json(Map map){
        return GSON.toJson(map);
    }
    public static Map json2map(String mapJsonString){
        return GSON.fromJson(mapJsonString, Map.class);
    }


}
