package org.grampus.replay;

import java.io.*;

public class SerializableMarsheller implements GMarsheller<Serializable, Serializable> {
    @Override
    public byte[] encode(Serializable input) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(input);
        return baos.toByteArray();
    }

    @Override
    public Serializable decode(byte[] bytes) throws Exception {
        ByteArrayInputStream bytesInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(bytesInputStream);
        return (Serializable) objectInputStream.readObject();
    }
}
