package com.huabo.financialdata.config.redis.serialize;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * @author lee
 * @version 1.0.0
 */
public class JavaOriginalISerializer implements ISerializer {

    @Override
    public byte[] serialize(Object object) throws SerializeException {
        if (object == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
        try {
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            oos.flush();
        } catch (Exception ex) {
            throw new SerializeException(ex.getMessage(), ex);
        }
        return baos.toByteArray();
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> tpl) throws SerializeException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try {
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
            return (T) ois.readObject();
        } catch (Exception ex) {
            throw new SerializeException(ex.getMessage(), ex);
        }
    }

    @Override
    public <T> List<T> deserializeList(byte[] data, Class<?> elementClasses) throws SerializeException {
        if (data == null || data.length == 0) {
            return null;
        }
        try {
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
            return (List<T>) ois.readObject();
        } catch (Exception ex) {
            throw new SerializeException(ex.getMessage(), ex);
        }
    }

}
