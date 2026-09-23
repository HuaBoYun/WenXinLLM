package com.huabo.cybermonitor.common;


/**
* @description 异常枚举
* @author   lyz
* @date 2022/4/14 10:58
*/
public enum ResultCode implements IErrorCode {

    SUCCESS(200, "成功"),
    BIZ_ERROR(400, "业务逻辑异常"),
    UNAUTHORIZED(401, "登录认证失败"),
    SYSTEM_INTERNAL_ERROR(500, "系统内部错误");

    private long code;
    private String message;

    ResultCode(long code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
