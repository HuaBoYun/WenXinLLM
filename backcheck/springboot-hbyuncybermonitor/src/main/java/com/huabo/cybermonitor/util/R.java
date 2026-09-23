package com.huabo.cybermonitor.util;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.huabo.cybermonitor.common.IErrorCode;
import com.huabo.cybermonitor.common.ResultCode;

import java.io.Serializable;

/**
 * @author lyz
 * @description 返回结果集
 * @date 2022/4/14 11:04
 */
public class R<T> implements Serializable {

    private long result;
    private String msg;
    private T data;
    @JsonIgnore
    private Object exp;

    public static <T> R<T> success() {
        return build(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), null);
    }

    /**
     * @description 返回成功结果集
     * @author lyz
     * @date 2022/4/14 11:04
     */
    public static <T> R<T> success(String code, String message, T data) {
        return build(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    public static <T> R<T> success(String message, T data) {
        return build(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    public static <T> R<T> success(T data) {
        return build(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }
    
    public static R<Boolean> success(boolean b, String message) {
    	return build(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), null);
	}
    
    public static R<Integer> success(Integer count, String message) {
    	return build(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), count);
	}

    /**
     * @description 返回失败结果集
     * @author lyz
     * @date 2022/4/14 11:04
     */
    public static <T> R<T> fail(IErrorCode errorCode) {
        return build(errorCode.getCode(), errorCode.getMessage(), null);
    }

    public static <T> R<T> fail(IErrorCode errorCode, String message) {
        return build(errorCode.getCode(), message, null);
    }

    public static <T> R<T> fail() {
        return fail(ResultCode.SYSTEM_INTERNAL_ERROR);
    }

    public static <T> R<T> fail(String message) {
        return fail(ResultCode.SYSTEM_INTERNAL_ERROR, message);
    }


    private static <T> R<T> build(long code, String message, T data) {
        R<T> r = new R<>();
        r.setResult(code);
        r.setMsg(message);
        r.setData(data);
        return r;
    }

    public long getResult() {
        return result;
    }

    public void setResult(long result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public R exp(Object exp) {
        this.exp = exp;
        return this;
    }

}
