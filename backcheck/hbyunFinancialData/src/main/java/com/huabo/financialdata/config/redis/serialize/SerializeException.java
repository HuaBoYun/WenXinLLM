package com.huabo.financialdata.config.redis.serialize;


/**
 * @author lee
 * @version 1.0.0
 */
public class SerializeException extends RuntimeException {

    private static final long serialVersionUID = 4120517444695682961L;

    public SerializeException(String message) {
        super(message);
    }

    public SerializeException(String message, Throwable e) {
        super(message, e);
    }

}
