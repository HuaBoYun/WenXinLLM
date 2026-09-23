package com.huabo.cybermonitor.utils;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName MyJsonBean
 * @Description JSON响应Bean
 * @Author ZiYao
 * @Date 2022/4/12 16:25
 * @Version 1.0
 **/
@ApiModel(description = "JSON响应")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyJsonBean<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "响应码")
    private Integer code;

    @ApiModelProperty(value = "响应消息")
    private String msg;

    @ApiModelProperty(value = "响应数据")
    private T data;

    public MyJsonBean(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public MyJsonBean(T data) {
        this.code = 200;
        this.msg = "success";
        this.data = data;
    }

    public static <T> MyJsonBean<T> ok() {
        return new MyJsonBean<>(200, "success");
    }

    public static <T> MyJsonBean<T> ok(T data) {
        return new MyJsonBean<>(200, "success", data);
    }

    public static <T> MyJsonBean<T> ok(String msg, T data) {
        return new MyJsonBean<>(200, msg, data);
    }

    public static <T> MyJsonBean<T> fail() {
        return new MyJsonBean<>(500, "fail");
    }

    public static <T> MyJsonBean<T> fail(String msg) {
        return new MyJsonBean<>(500, msg);
    }

    public static <T> MyJsonBean<T> fail(Integer code, String msg) {
        return new MyJsonBean<>(code, msg);
    }

    public static <T> MyJsonBean<T> fail(Integer code, String msg, T data) {
        return new MyJsonBean<>(code, msg, data);
    }

    public static <T> MyJsonBean<T> success(T data) {
        return new MyJsonBean<>(200, "success", data);
    }

    public static <T> MyJsonBean<T> error(String msg) {
        return new MyJsonBean<>(500, msg);
    }
}

