package com.huabo.financialdata.config.exception;

/**
 * 通用异常类
 *
 * @author lee
 * @version 1.0.0
 **/
public class BizException extends RuntimeException {

    /**
     * 信息码
     */
    private BizCode bizCode;

    /**
     * 其他自定义信息，如请求参数等便于调试
     */
    private String customMessage;

    public BizCode getBizCode() {
        return bizCode;
    }

    public void setBizCode(BizCode bizCode) {
        if (null == bizCode) {
            this.bizCode = BizCode.getBizCodeFromEnum(BizCodeEnumDefault.SERVICE_UNAVAILABLE);
        } else {
            this.bizCode = bizCode;
        }
    }

    public String getCustomMessage() {
        return customMessage;
    }

    public void setCustomMessage(String customMessage) {
        this.customMessage = customMessage;
    }


    /**
     * 通用异常类构造函数
     *
     * @param message 信息描述
     */
    public BizException(String message) {
        this(message, null);
    }

    /**
     * 通用异常类构造函数
     *
     * @param message 信息描述
     * @param cause   异常内容
     */
    public BizException(String message, Throwable cause) {
        this(null, message, cause);
    }

    /**
     * 通用异常类构造函数
     *
     * @param bizCode 业务代码，BizCode
     * @param cause   异常内容
     */
    public BizException(BizCode bizCode, Throwable cause) {
        this(bizCode, null, cause);

    }

    /**
     * 通用异常类构造函数
     *
     * @param bizCode 业务代码，BizCode
     * @param message 信息描述
     */
    public BizException(BizCode bizCode, String message) {
        this(bizCode, message, null, null);
    }

    /**
     * 通用异常类构造函数
     *
     * @param bizCode 业务代码，BizCode
     * @param message 信息描述
     * @param cause   异常内容
     */
    public BizException(BizCode bizCode, String message, Throwable cause) {
        this(bizCode, message, cause, null);
    }

    /**
     * 通用异常类构造函数
     *
     * @param bizCode       业务代码，BizCode
     * @param message       信息描述
     * @param customMessage 自定义描述信息，如业务数据JSON字符串
     */
    public BizException(BizCode bizCode, String message, String customMessage) {
        this(bizCode, message, null, customMessage);
    }

    /**
     * 通用异常类构造函数
     *
     * @param bizCode       业务代码，BizCode
     * @param message       信息描述
     * @param cause         异常内容
     * @param customMessage 自定义描述信息，如业务数据JSON字符串
     */
    public BizException(BizCode bizCode, String message, Throwable cause, String customMessage) {
        super(message, cause);
        this.setBizCode(bizCode);
        this.setCustomMessage(customMessage);
    }
}
