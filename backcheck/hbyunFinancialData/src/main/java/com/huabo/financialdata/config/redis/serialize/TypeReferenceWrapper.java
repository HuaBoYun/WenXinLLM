package com.huabo.financialdata.config.redis.serialize;

import com.fasterxml.jackson.core.type.TypeReference;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author lee
 * @version 1.0.0
 */
public abstract class TypeReferenceWrapper<T> {

    protected final Type type;

    public TypeReferenceWrapper() {
        Type superClass = getClass().getGenericSuperclass();
        if (superClass instanceof Class<?>) {
            // sanity check, should never happen
            throw new IllegalArgumentException("Internal error: TypeReference constructed without actual type information");
        }
        /* 22-Dec-2008, tatu: Not sure if this case is safe -- I suspect
         *   it is possible to make it fail?
         *   But let's deal with specific
         *   case when we know an actual use case, and thereby suitable
         *   workarounds for valid case(s) and/or error to throw
         *   on invalid one(s).
         */
        type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
    }

    protected TypeReference<T> getTypeReferenceForJackson() {
        return new JacksonTypeReference<T>(type) {
        };
    }

    static class JacksonTypeReference<T> extends TypeReference<T> {

        protected final Type typeWrapper;

        protected JacksonTypeReference(Type type) {
            typeWrapper = type;
        }


        @Override
        public Type getType() {
            return typeWrapper;
        }

        @Override
        public int compareTo(TypeReference<T> o) {
            return super.compareTo(o);
        }
    }

}
