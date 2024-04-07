package org.grampus.replay;

public interface GMarsheller<I, O> {
    byte[] encode(I input) throws Exception;

    O decode(byte[] bytes) throws Exception;
}
