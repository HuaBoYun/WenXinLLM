package com.huabo.financialdata.config.redis.entity;


import org.springframework.dao.DataAccessException;

/**
 * RedisService统一异常类
 *
 * @author lee
 * @version 1.0.0
 */
public class RedisServiceException extends DataAccessException {
    private static final long serialVersionUID = -8065928969213617626L;

    public RedisServiceException(Throwable cause) {
        this(cause.getMessage(), cause);
    }

    public RedisServiceException(String msg) {
        super(msg);
    }

    public RedisServiceException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
