package com.huabo.audit.exception;


import com.huabo.audit.common.IErrorCode;
import com.huabo.audit.common.ResultCode;

/**
 * @author lyz
 * @description 异常类
 * @date 2022/4/14 11:05
 */
public class CommercialException extends RuntimeException {

    private IErrorCode errorCode;
    private String message;

    public CommercialException(IErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
    }

    public CommercialException(IErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.message = message;
    }

    public CommercialException(String message) {
        super(message);
        this.errorCode = ResultCode.SYSTEM_INTERNAL_ERROR;
        this.message = message;
    }

    public CommercialException(String message, Throwable e) {
        super(message, e);
        this.errorCode = ResultCode.SYSTEM_INTERNAL_ERROR;
        this.message = message;
    }

    public IErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(IErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
