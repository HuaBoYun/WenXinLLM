package com.huabo.cybermonitor.common.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName R
 * @Description 通用响应类
 * @Author ZiYao
 * @Date 2022/4/12 16:25
 * @Version 1.0
 **/
@ApiModel(description = "通用响应")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class R<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "响应码")
    private Integer code;

    @ApiModelProperty(value = "响应消息")
    private String msg;

    @ApiModelProperty(value = "响应数据")
    private T data;

    public R(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public R(T data) {
        this.code = 200;
        this.msg = "success";
        this.data = data;
    }

    // Getter and Setter methods (Lombok @Data should generate these)
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
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

    public static <T> R<T> ok() {
        R<T> r = new R<T>();
        r.setCode(200);
        r.setMsg("success");
        return r;
    }

    public static <T> R<T> ok(T data) {
        return new R<T>(200, "success", data);
    }

    public static <T> R<T> ok(String msg, T data) {
        return new R<T>(200, msg, data);
    }

    public static <T> R<T> ok(boolean success, String msg) {
        if (success) {
            R<T> r = new R<T>();
            r.setCode(200);
            r.setMsg(msg);
            return r;
        } else {
            R<T> r = new R<T>();
            r.setCode(500);
            r.setMsg(msg);
            return r;
        }
    }

    public static <T> R<T> ok(Integer code, String msg) {
        R<T> r = new R<T>();
        r.setCode(code);
        r.setMsg(msg);
        return r;
    }

    public static <T> R<T> fail() {
        R<T> r = new R<T>();
        r.setCode(500);
        r.setMsg("fail");
        return r;
    }

    public static <T> R<T> fail(String msg) {
        R<T> r = new R<T>();
        r.setCode(500);
        r.setMsg(msg);
        return r;
    }

    public static <T> R<T> fail(Integer code, String msg) {
        R<T> r = new R<T>();
        r.setCode(code);
        r.setMsg(msg);
        return r;
    }

    public static <T> R<T> fail(Integer code, String msg, T data) {
        return new R<T>(code, msg, data);
    }

    public static <T> R<T> success(T data) {
        return new R<T>(200, "success", data);
    }

    public static <T> R<T> error(String msg) {
        R<T> r = new R<T>();
        r.setCode(500);
        r.setMsg(msg);
        return r;
    }

    public static <T> R<T> error(Integer code, String msg) {
        return new R<>(code, msg);
    }
}

