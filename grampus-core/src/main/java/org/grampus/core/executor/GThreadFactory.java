package org.grampus.core.executor;

import java.lang.reflect.Method;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class GThreadFactory implements ThreadFactory {
    private GThreadChecker threadChecker;
    private Boolean isSupportVirtualThread = null;
    private ThreadFactory vthreadFactory = null;

    public GThreadFactory(GThreadChecker threadChecker) {
        this.threadChecker = threadChecker;
    }

    @Override
    public Thread newThread(Runnable runnable) {
        GThread thread = new GThread(runnable,new GThreadInfo(threadChecker.getProcessTimeoutMills(), TimeUnit.MILLISECONDS));
        threadChecker.registerThread(thread);
        return thread;
    }

    public Thread newVirtualThread(Runnable runnable) {
       return this.vthreadFactory.newThread(runnable);
    }

    public boolean isSupportVThread() {
        if (isSupportVirtualThread == null) {
            vthreadFactory = virtualThreadFactory();
        }
        return isSupportVirtualThread;
    }

    private ThreadFactory virtualThreadFactory() {
        try {
            Class<?> builderClass = ClassLoader.getSystemClassLoader().loadClass("java.lang.Thread$Builder");
            Class<?> ofVirtualClass = ClassLoader.getSystemClassLoader().loadClass("java.lang.Thread$Builder$OfVirtual");
            Method ofVirtualMethod = Thread.class.getDeclaredMethod("ofVirtual");
            Object builder = ofVirtualMethod.invoke(null);
            Method nameMethod = ofVirtualClass.getDeclaredMethod("name", String.class, long.class);
            Method factoryMethod = builderClass.getDeclaredMethod("factory");
            builder = nameMethod.invoke(builder, "grampus-virtual-thread-", 0L);
            return (ThreadFactory) factoryMethod.invoke(builder);
        } catch (Exception e) {
            isSupportVirtualThread = false;
            return null;
        }
    }
}
