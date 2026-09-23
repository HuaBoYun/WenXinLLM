package com.huabo.financialdata.config.redis.serialize;

import java.util.List;

/**
 * @author lee
 * @version 1.0.0
 */
public interface ISerializer {

    byte[] serialize(Object o) throws SerializeException;

    <T> T deserialize(byte[] data, Class<T> tpl) throws SerializeException;

    <T> List<T> deserializeList(byte[] data, Class<?> elementClasses) throws SerializeException;

}
