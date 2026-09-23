package com.global.treasurer.util;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * API统一返回基类
 * @author zhuhuix
 * @date 2020-04-03
 */
@Getter
@Setter
public class Result<T> implements Serializable {

    /**
     * 是否成功
     */
    private Boolean success;

    /**
     * 错误码
     */
    private String errCode;

    /**
     *  错误信息
     */
    private String errMsg;

    /**
     * 返回数据
     */
    private T module;


    @Override
    public String toString() {
        return "Result{" +
                "success=" + success +
                ", errCode='" + errCode + '\'' +
                ", errMsg='" + errMsg + '\'' +
                ", module=" + module +
                '}';
    }

    public Result<T> ok(T module){
        this.setSuccess(true);
        this.setErrCode("0");
        this.setErrMsg("ok");
        this.setModule(module);
        return this;
    }

    // ==================== 静态工厂方法 ====================

    /**
     * 成功返回（带数据）
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setSuccess(true);
        result.setErrCode("0");
        result.setErrMsg("ok");
        result.setModule(data);
        return result;
    }

    /**
     * 成功返回（无数据）
     */
    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.setSuccess(true);
        result.setErrCode("0");
        result.setErrMsg("ok");
        return result;
    }

    /**
     * 失败返回（带错误码和错误信息）
     */
    public static <T> Result<T> error(String errCode, String errMsg) {
        Result<T> result = new Result<>();
        result.setSuccess(false);
        result.setErrCode(errCode);
        result.setErrMsg(errMsg);
        return result;
    }

    /**
     * 失败返回（只带错误信息）
     */
    public static <T> Result<T> error(String errMsg) {
        Result<T> result = new Result<>();
        result.setSuccess(false);
        result.setErrCode("-1");
        result.setErrMsg(errMsg);
        return result;
    }    public void setSuccess(Boolean success) {
        this.success = success;
    }
    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }
    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
    public void setModule(T module) {
        this.module = module;
    }
}
