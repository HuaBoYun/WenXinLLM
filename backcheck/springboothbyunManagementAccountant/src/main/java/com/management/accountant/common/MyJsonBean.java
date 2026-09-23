package com.management.accountant.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

/**
 * 统一响应结果类
 * 符合用友YonBIP V3.0 API接口文档规范
 * 
 * @author 华博云
 * @version 3.0.0
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MyJsonBean<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 响应状态码
     */
    private Integer code;

    /**
     * 响应消息
     */
    private String msg;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 时间戳
     */
    private Long timestamp;

    public MyJsonBean() {
        this.timestamp = System.currentTimeMillis();
    }

    public MyJsonBean(Integer code, String msg) {
        this();
        this.code = code;
        this.msg = msg;
    }

    public MyJsonBean(Integer code, String msg, T data) {
        this();
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 成功响应
     */
    public static <T> MyJsonBean<T> success() {
        return new MyJsonBean<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage());
    }

    /**
     * 成功响应带数据
     */
    public static <T> MyJsonBean<T> success(T data) {
        return new MyJsonBean<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    /**
     * 成功响应带消息和数据
     */
    public static <T> MyJsonBean<T> success(String msg, T data) {
        return new MyJsonBean<>(ResultCode.SUCCESS.getCode(), msg, data);
    }

    /**
     * 成功响应带数据与消息（数据在前）
     */
    public static <T> MyJsonBean<T> successData(T data, String msg) {
        return new MyJsonBean<>(ResultCode.SUCCESS.getCode(), msg, data);
    }

    /**
     * 失败响应
     */
    public static <T> MyJsonBean<T> error() {
        return new MyJsonBean<>(ResultCode.ERROR.getCode(), ResultCode.ERROR.getMessage());
    }

    /**
     * 失败响应带消息
     */
    public static <T> MyJsonBean<T> error(String msg) {
        return new MyJsonBean<>(ResultCode.ERROR.getCode(), msg);
    }

    /**
     * 失败响应带状态码和消息
     */
    public static <T> MyJsonBean<T> error(Integer code, String msg) {
        return new MyJsonBean<>(code, msg);
    }

    /**
     * 自定义响应
     */
    public static <T> MyJsonBean<T> result(Integer code, String msg, T data) {
        return new MyJsonBean<>(code, msg, data);
    }

    /**
     * 根据ResultCode枚举返回结果
     */
    public static <T> MyJsonBean<T> result(ResultCode resultCode) {
        return new MyJsonBean<>(resultCode.getCode(), resultCode.getMessage());
    }

    /**
     * 根据ResultCode枚举返回结果带数据
     */
    public static <T> MyJsonBean<T> result(ResultCode resultCode, T data) {
        return new MyJsonBean<>(resultCode.getCode(), resultCode.getMessage(), data);
    }

    /**
     * 判断是否成功
     */
    public boolean isSuccess() {
        return ResultCode.SUCCESS.getCode().equals(this.code);
    }
}
