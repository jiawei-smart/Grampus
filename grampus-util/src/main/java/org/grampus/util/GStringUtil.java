package org.grampus.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrSubstitutor;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GStringUtil {
    public static boolean isEmpty(String string){
       return StringUtils.isEmpty(string);
    }

    public static boolean isNotEmpty(String string){
        return StringUtils.isNotEmpty(string);
    }

    public static boolean equals(String one, String another) {
        return StringUtils.equals(one,another);
    }
    public static List<String> splitAsList(String target, String separatorChars){
        return Arrays.asList(StringUtils.split(target,separatorChars));
    }

    public static final String EMPTY_STRING = "";
    public static String emptyString() {
        return EMPTY_STRING;
    }

    public static String substitute(String target, Map<String, String> replaces){
        return StrSubstitutor.replace(target,replaces);
    }
}
