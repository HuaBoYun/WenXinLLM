package com.huabo.file.util;

/**
 * 异常枚举定义
 */
public enum ErrorCodeEnum {
    SUCCESS(200, "处理成功"),
    FAIL(400, "处理失败"),
    ERROR(500, "处理异常"),
    USER_ERROR(10001, "用户已失效！"),

    FILE_UP_FAIL(70001, "文件上传失败！"),
    FILE_NOT_EXIST(70002, "文件不存在"),
    FILE_NOT_EXIST_LOCAL(70003, "local文件不存在"),

    FILE_WRITE_RESP_FAIL(70004, "文件写流失败"),
    FILE_DELETE_FAIL(70005, "文件删除失败"),
    FILE_DELETE_FAIL_LOCAL(70006, "local文件删除失败"),

    FILE_CA_SAVE_FAIL(70007, "文件加密保存失败！"),







    ;

    private final Integer code;
    private final String message;
    private final String system;

    private ErrorCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
        this.system = "BASE";
    }

    public Integer getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public String getSystem() {
        return this.system;
    }
}
