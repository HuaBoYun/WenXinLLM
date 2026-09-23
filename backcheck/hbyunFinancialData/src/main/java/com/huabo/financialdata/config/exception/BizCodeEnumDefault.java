package com.huabo.financialdata.config.exception;

/**
 * 业务代码枚举类
 * 3位数的错误代码继承至HttpStatus
 * 4位数的错误代码为业务码，自定义时可延续3位数的错误类型，如4000为参数不合法，5000为服务器通用异常，
 *
 * @author lee
 * @version 1.0.0
 **/
public enum BizCodeEnumDefault {

    // 1xx Success
    OK(1, "OK"),

    // --- 4xx Client Error ---
    BAD_REQUEST(400000, "Bad Request"),
    METHOD_NOT_ALLOWED(401000, "Method Not Allowed"),
    UNSUPPORTED_MEDIA_TYPE(402000, "Unsupported Media Type"),


    // --- 5xx Server Error ---
    INTERNAL_SERVER_ERROR(500000, "服务器内部异常"),
    NOT_IMPLEMENTED(501000, "未实现"),
    BAD_GATEWAY(502000, "无效网关"),
    SERVICE_UNAVAILABLE(503000, "服务不可用"),
    GATEWAY_TIMEOUT(504000, "网关超时"),
    HTTP_VERSION_NOT_SUPPORTED(505000, "不支持HTTP版本"),

    // --- 6xx Request Param Error ---
    REQUEST_PARAM_ERROR_NUMBER(601000, "请输入正确的数字");

    BizCodeEnumDefault(int code, String name) {
        this.code = code;
        this.name = name;
    }

    private final int code;
    private final String name;

    public int getValue() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }

}
