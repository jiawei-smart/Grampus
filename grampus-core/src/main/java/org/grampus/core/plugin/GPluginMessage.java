package org.grampus.core.plugin;

import java.lang.reflect.Method;

public class GPluginMessage {
    private final Method method;
    private final Object[] args;

    public GPluginMessage(Method method, Object[] args) {
        this.method = method;
        this.args = args;
    }

    public Method getMethod() {
        return method;
    }

    public Object[] getArgs() {
        return args;
    }
}
