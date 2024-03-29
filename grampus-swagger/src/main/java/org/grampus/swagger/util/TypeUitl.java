package org.grampus.swagger.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TypeUitl {
    private static Gson GSON;

    public static Gson json() {
        if (GSON == null) {
            GsonBuilder builder =new GsonBuilder().setPrettyPrinting();
            GSON = builder.create();
        }
        return GSON;
    }
}
