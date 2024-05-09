package org.grampus.core.plugin;

import java.util.function.Function;

public class GAsyncResult<T> {
    private T result;
    private Throwable throwable;
    private boolean succeed = false;
    public T result(){
        return result;
    }


    public GAsyncResult<T> otherwise(T value) {
        return this.otherwise((err) -> {
            return value;
        });
    }

    public GAsyncResult<T> otherwiseEmpty() {
        return this.otherwise((err) -> {
            return null;
        });
    }


    public GAsyncResult<T> otherwise(final Function<Throwable, T> mapper) {
        if (mapper == null) {
            throw new NullPointerException();
        } else {
            return new GAsyncResult<T>() {
                public T result() {
                    if (GAsyncResult.this.succeeded()) {
                        return GAsyncResult.this.result();
                    } else {
                        return GAsyncResult.this.failed() ? mapper.apply(GAsyncResult.this.cause()) : null;
                    }
                }

                public Throwable cause() {
                    return null;
                }

                public boolean succeeded() {
                    return GAsyncResult.this.succeeded() || GAsyncResult.this.failed();
                }

                public boolean failed() {
                    return false;
                }
            };
        }
    }
    public GAsyncResult result(T result) {
        this.result = result;
        this.succeed = true;
        return this;
    }

    public Throwable cause(){
        return throwable;
    };

    public boolean succeeded(){
        return succeed;
    }

    public boolean failed(){
        return !succeed;
    }

    public GAsyncResult cause(Throwable throwable) {
        this.throwable = throwable;
        return this;
    }

    public boolean isSucceed() {
        return succeed;
    }

    public void setSucceed(boolean succeed) {
        this.succeed = succeed;
    }
}
