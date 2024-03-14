package org.grampus.core.util;

import java.time.Instant;

public class GDateTimeUtil {
    public static long now(){
        return Instant.now().toEpochMilli();
    }
}
