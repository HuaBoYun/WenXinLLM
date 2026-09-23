package com.huabo.contract.vo;


import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.huabo.contract.common.IErrorCode;
import com.huabo.contract.common.ResultCode;

/**
 * @author lyz
 * @description 返回结果集
 * @date 2022/4/14 11:04
 */
public class Result<T> implements Serializable {

    private long result;
    private String msg;
    private T data;
    @JsonIgnore
    private Object exp;

    public static <T> Result<T> success() {
        return build(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), null);
    }

    /**
     * @description 返回成功结果集
     * @author lyz
     * @date 2022/4/14 11:04
     */
    public static <T> Result<T> success(String code, String message, T data) {
        return build(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    public static <T> Result<T> success(String message, T data) {
        return build(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    public static <T> Result<T> success(T data) {
        return build(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    /**
     * @description 返回失败结果集
     * @author lyz
     * @date 2022/4/14 11:04
     */
    public static <T> Result<T> fail(IErrorCode errorCode) {
        return build(errorCode.getCode(), errorCode.getMessage(), null);
    }

    public static <T> Result<T> fail(IErrorCode errorCode, String message) {
        return build(errorCode.getCode(), message, null);
    }

    public static <T> Result<T> fail() {
        return fail(ResultCode.SYSTEM_INTERNAL_ERROR);
    }

    public static <T> Result<T> fail(String message) {
        return fail(ResultCode.SYSTEM_INTERNAL_ERROR, message);
    }


    private static <T> Result<T> build(long code, String message, T data) {
        Result<T> r = new Result<>();
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

    public Result exp(Object exp) {
        this.exp = exp;
        return this;
    }

}
