package org.grampus.util;

import com.google.gson.GsonBuilder;
import com.google.gson.Gson;

import java.io.Reader;
import java.io.Writer;
import java.util.Map;

public class GJsonUtil {
    private static Gson GSON = new GsonBuilder().setPrettyPrinting().create();;

    public static  <T> T fromJson(String payload, Class<T>  type) {
        return GSON.fromJson(payload,type);
    }
    public static <T> T fromJson(Reader reader, Class<T> type) {
        return GSON.fromJson(reader,type);
    }
    public static String toJson(Object value) {
        return GSON.toJson(value);
    }
    public static void toJson(Object value,Writer writer) {
        GSON.toJson(value,writer);
    }
    public static String map2json(Map map){
        return GSON.toJson(map);
    }
    public static Map json2map(String mapJsonString){
        return GSON.fromJson(mapJsonString, Map.class);
    }


}
