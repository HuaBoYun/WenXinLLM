package com.huabo.file.util;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 统一JSON返回类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JsonBean<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 程序定义状态码 200成功 400失败
     */
    @Schema(name="程序定义状态码 200成功 400失败 500业务异常")
    private int code;
    /**
     * 必要的提示信息
     */
    @Schema(name="必要的提示信息")
    private String msg;
    /**
     * 业务数据
     */
    @Schema(name="对象")
    private T data;

    public static JsonBean success(Object o) {
        JsonBean json = new JsonBean();
        json.setCode(ErrorCodeEnum.SUCCESS.getCode());
        json.setMsg(ErrorCodeEnum.SUCCESS.getMessage());
        json.setData(o);
        return json;
    }
    public static JsonBean success(String msg) {
        JsonBean json = new JsonBean();
        json.setCode(ErrorCodeEnum.SUCCESS.getCode());
        json.setMsg(msg);
        return json;
    }


    public static JsonBean error(ErrorCodeEnum error) {
        JsonBean json = new JsonBean();
        json.setCode(error.getCode());
        json.setMsg(error.getMessage());
        return json;
    }

}
