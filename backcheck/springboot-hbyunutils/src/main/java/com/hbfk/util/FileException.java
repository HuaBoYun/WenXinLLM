package com.hbfk.util;


/**
 * 自定义异常
 */
public class FileException extends RuntimeException{
    private int errCode;
    private String errorMessage;

    public FileException(String errMessage) {
        super(errMessage);
    }

    public FileException(Throwable throwable) {
        super(throwable);
    }
    public FileException(ErrorCodeEnum error) {
        this.errorMessage = error.getMessage();
        this.errCode = error.getCode();
    }
    public FileException(int errCode, String errMessage) {
        super(errMessage);
        this.errCode = errCode;
    }

    public FileException(String errMessage, Throwable e) {
        super(errMessage, e);
    }

    public FileException(int errCode, String errMessage, Throwable e) {
        super(errMessage, e);
        this.errCode = errCode;
    }

    public int getErrCode() {
        return this.errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
