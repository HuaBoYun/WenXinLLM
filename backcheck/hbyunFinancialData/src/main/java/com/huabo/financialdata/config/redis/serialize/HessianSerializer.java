package com.huabo.financialdata.config.redis.serialize;


import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;


/**
 * @author lee
 * @version 1.0.0
 */
public class HessianSerializer implements ISerializer {
    @Override
    public byte[] serialize(Object obj) throws SerializeException {
        if (obj == null) {
            return new byte[0];
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        HessianOutput ho = new HessianOutput(os);
        try {
            ho.writeObject(obj);
        } catch (IOException e) {
            throw new SerializeException(e.getMessage(), e);
        }
        return os.toByteArray();
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> tpl) throws SerializeException {
        if (data == null || data.length == 0) {
            return null;
        }
        ByteArrayInputStream is = new ByteArrayInputStream(data);
        HessianInput hi = new HessianInput(is);
        try {
            Object o = hi.readObject();
            return (T) o;
        } catch (IOException e) {
            throw new SerializeException(e.getMessage(), e);
        }
    }

    @Override
    public <T> List<T> deserializeList(byte[] data, Class<?> elementClasses) throws SerializeException {
        if (data == null || data.length == 0) {
            return null;
        }
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        HessianInput deserialize = new HessianInput(byteArrayInputStream);

        try {
            return (List<T>) deserialize.readObject();
        } catch (IOException e) {
            throw new SerializeException(e.getMessage(), e);
        }
    }

}
