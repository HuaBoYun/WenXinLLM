package com.huabo.financialdata.config.redis.serialize;


import com.alibaba.fastjson.JSON;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * @author lee
 * @version 1.0.0
 */
public class JSONSerializer implements ISerializer {
    private static final JSONSerializer DEFAULT_INSTANCE = new JSONSerializer();
    private String charset = "utf-8";

    public static final JSONSerializer getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    @Override
    public byte[] serialize(Object o) throws SerializeException {
        if (o == null) {
            return new byte[0];
        }
        return JSON.toJSONBytes(o);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T deserialize(byte[] data, Class<T> tpl) throws SerializeException {
        if (data == null || data.length == 0) {
            return null;
        }
        return (T) JSON.parseObject(data, tpl);
    }

    public <T> T deserialize(String json, Class<T> tpl) throws SerializeException {
        if (!StringUtils.hasText(json)) {
            return null;
        }
        return (T) JSON.parseObject(json, tpl);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> deserializeList(byte[] data, Class<?> elementClasses) throws SerializeException {
        if (data == null || data.length == 0) {
            return null;
        }
        List<T> object = null;
        String s = null;

        try {
            s = new String(data, charset);
            object = (List<T>) JSON.parseArray(s, elementClasses);
        } catch (UnsupportedEncodingException e) {
            throw new SerializeException(e.getMessage(), e);
        }
        return object;
    }

    @SuppressWarnings("unchecked")
    public <T> Map<String, T> deserializeMap(byte[] data, Class<?> elementClasses) throws SerializeException {
        if (data == null || data.length == 0) {
            return null;
        }
        Map<String, T> object = null;
        String s = null;

        try {
            s = new String(data, charset);
            object = (Map<String, T>) JSON.parseArray(s, elementClasses);
        } catch (UnsupportedEncodingException e) {
            throw new SerializeException(e.getMessage(), e);
        }
        return object;
    }

    //=============================== getter && setter ============================

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getCharset() {
        return charset;
    }
}
