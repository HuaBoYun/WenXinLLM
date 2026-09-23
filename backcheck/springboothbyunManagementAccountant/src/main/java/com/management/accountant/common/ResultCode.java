package com.management.accountant.common;

/**
 * 响应状态码枚举
 * 符合用友YonBIP V3.0 API接口文档规范
 * 
 * @author 华博云
 * @version 3.0.0
 */
public enum ResultCode {

    // 成功状态码
    SUCCESS(1, "操作成功"),

    // 通用错误：10001-19999
    ERROR(0, "操作失败"),
    PARAM_ERROR(10001, "参数错误"),
    PARAM_MISSING(10002, "参数缺失"),
    PARAM_INVALID(10003, "参数无效"),
    DATA_NOT_FOUND(10004, "数据不存在"),
    VALIDATION_CODE_EXPIRED(10005, "验证码已失效"),
    VALIDATION_CODE_ERROR(10006, "验证码错误"),

    // 认证授权错误：20001-29999
    UNAUTHORIZED(20001, "未授权访问"),
    TOKEN_EXPIRED(20002, "令牌已过期"),
    TOKEN_INVALID(20003, "令牌无效"),
    LOGIN_REQUIRED(20004, "请先登录"),
    PERMISSION_DENIED(20005, "权限不足"),

    // 业务错误：30001-39999
    BUSINESS_ERROR(30001, "业务出现问题"),
    APPROVAL_FAILED(30002, "审批失败"),
    PROJECT_NOT_EXISTS(30003, "当前不存在实施项目"),
    RECTIFICATION_INCOMPLETE(30004, "整改未完成，无法提交"),
    WORKFLOW_IN_PROGRESS(30007, "流程审批中"),
    WORKFLOW_ADJUSTMENT(30008, "流程调整中，请去我的待办提交"),
    WORKFLOW_APPROVED(30009, "流程已通过"),
    WORKFLOW_COMPLETED(30010, "流程已完成"),
    DEPARTMENT_HEAD_NOT_CONFIGURED(30011, "部门负责人需配置"),
    SUPERVISOR_NOT_CONFIGURED(30012, "分管领导需配置"),

    // 系统错误：40001-49999
    SYSTEM_BUSY(40001, "系统繁忙，请稍后重试"),
    SYSTEM_ERROR(40002, "系统异常"),
    DATABASE_ERROR(40003, "数据库异常"),
    NETWORK_ERROR(40004, "网络异常"),
    FILE_UPLOAD_ERROR(40005, "文件上传失败"),
    FILE_DOWNLOAD_ERROR(40006, "文件下载失败"),

    // 数据错误：50001-59999
    DATA_EXISTS(50001, "数据已存在"),
    DATA_NOT_EXISTS(50002, "数据不存在"),
    DATA_INVALID(50003, "数据无效"),
    DATA_EXPIRED(50004, "数据已过期"),

    // 接口错误：60001-69999
    INTERFACE_ERROR(60001, "接口调用异常"),
    INTERFACE_TIMEOUT(60002, "接口调用超时"),
    INTERFACE_NOT_FOUND(60003, "接口不存在"),

    // 权限错误：70001-79999
    NO_PERMISSION(70001, "无权限访问"),
    AUTHORIZATION_SUCCESS(70002, "授权成功"),
    DELETE_SUCCESS(70003, "删除成功"),

    // 审批流程错误：80001-89999
    PROJECT_NOT_STARTED(80001, "该项目未启动，不能实施"),
    PROJECT_COMPLETED(80002, "项目已完成，不能再次实施"),
    PROJECT_INCOMPLETE(80003, "项目不完整，请完善项目"),
    USER_NOT_IN_APPROVAL_FLOW(80004, "该人员未在审批流程信息中"),
    WITHDRAW_FAILED(80005, "撤回失败"),

    // 自定义错误：90001-99999
    TEMPLATE_EMPTY(90001, "模板无内容，禁止使用，请重新选择"),
    DATE_FORMAT_ERROR(90002, "日期格式不正确，保存失败");

    private final Integer code;
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
