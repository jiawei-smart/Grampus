package org.grampus.core.plugin;

import java.util.function.Consumer;

public class GPromise<T>{
    private Consumer<T> consumer;
    private Consumer<Throwable> throwableConsumer;

    public GPromise() {
    }

    public GPromise(Consumer<T> consumer) {
    }

    public void handle(GAsyncResult<T> ar) {
        if (ar.succeeded()) {
            this.onSuccess(ar.result());
        } else {
            this.onFailure(ar.cause());
        }

    }

    void onSuccess(T value) {
        if(this.consumer != null){
            consumer.accept(value);
        }
    }

    public static GPromise newInstance() {
        return new GPromise();
    }

    public static GPromise newInstance(Consumer successConsumer) {
        return new GPromise(successConsumer);
    }

    public GPromise<T> onSuccess(Consumer<T> consumer) {
        this.consumer = consumer;
        return this;
    }

    void onFailure(Throwable failure){
        if(this.throwableConsumer != null){
            this.throwableConsumer.accept(failure);
        }
     }

    public GPromise onFailure(Consumer<Throwable> throwableConsumer){
        this.throwableConsumer = throwableConsumer;
        return this;
    }
}
