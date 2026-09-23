package com.huabo.financialdata.config.exception;

import com.huabo.financialdata.config.log.LogFactory;
import com.huabo.financialdata.config.mvn.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 统一异常处理(AOP)
 *
 * @author lee
 * @version 1.0.0
 **/
@RestControllerAdvice
public class BizExceptionAdvice {

    /**
     * JSR303 参数不合法校验
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = ConstraintViolationException.class)
    public ApiResponse constraintViolationException(ConstraintViolationException ex) {
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
        String errorInfo = null;
        if (constraintViolations != null && constraintViolations.size() > 0) {
            List<String> msgList = new ArrayList<>();
            for (ConstraintViolation str : constraintViolations) {
                msgList.add(str.getMessage());
            }
            errorInfo = String.join(",", msgList);
        }

        BizException e = new BizException(BizCode.getBizCodeFromEnum(BizCodeEnumDefault.BAD_REQUEST), errorInfo, ex);
        LogFactory.error("参数校验失败", e);
        return BizExceptionResponseHandler.vaildhandler(e);
    }

    /**
     * JSR303 参数为空校验
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public ApiResponse missingServletRequestParameterException(MissingServletRequestParameterException ex) {
        String parameterName = ex.getParameterName();
        BizException e = new BizException(BizCode.getBizCodeFromEnum(BizCodeEnumDefault.BAD_REQUEST), "MissingServletRequestParameterException 缺少参数：" + parameterName, ex);
        LogFactory.error("缺少参数", e);
        return BizExceptionResponseHandler.vaildhandler(e);

    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ApiResponse methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult errors = ex.getBindingResult();
        String msgStr = "";
        if (errors.hasErrors()) {
            List<String> msgList = new ArrayList<>();
            for (ObjectError error : errors.getAllErrors()) {
                msgList.add(error.getDefaultMessage());
            }
            msgStr = String.join(",", msgList);
        }

        BizException e = new BizException(BizCode.getBizCodeFromEnum(BizCodeEnumDefault.BAD_REQUEST), "Body请求参数异常：" + msgStr, ex);
        return BizExceptionResponseHandler.vaildhandler(e);
    }


    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApiResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        BizException ce = new BizException(BizCode.getBizCodeFromEnum(BizCodeEnumDefault.BAD_REQUEST), "HttpMessageNotReadableException", e);
        LogFactory.error("参数解析失败", ce);
        return BizExceptionResponseHandler.handler(ce);
    }

    /**
     * 405 - Method Not Allowed
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ApiResponse handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        BizException ce = new BizException(BizCode.getBizCodeFromEnum(BizCodeEnumDefault.METHOD_NOT_ALLOWED), "HttpRequestMethodNotSupportedException", e);
        LogFactory.error("不支持当前请求方法", ce);
        return BizExceptionResponseHandler.handler(ce);
    }

    /**
     * 415 - Unsupported Media Type
     */
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ApiResponse handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        BizException ce = new BizException(BizCode.getBizCodeFromEnum(BizCodeEnumDefault.UNSUPPORTED_MEDIA_TYPE), "HttpMediaTypeNotSupportedException", e);
        LogFactory.error("不支持当前媒体类型", ce);
        return BizExceptionResponseHandler.handler(ce);
    }

    /**
     * 500 - Internal Server Error
     * 通用异常返回
     *
     * @return exceptionResponse
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(BizException.class)
    ApiResponse handleBizException(BizException e) {
        LogFactory.error(e.getBizCode().getName(), e);
        int code = e.getBizCode().getCode();
        return ApiResponse.response(code, null, e.getBizCode().getName());
    }

    /**
     * 500 - Internal Server Error
     * 通用异常返回
     *
     * @return exceptionResponse
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(Exception.class)
    ApiResponse handleException(Exception e) {
        LogFactory.error(e.getMessage(), e);
        return BizExceptionResponseHandler.handler(e);
    }

    /**
     * 600 - Request Param Error
     * 处理请求参数异常的问题
     *
     * @return exceptionResponse
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    ApiResponse handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        LogFactory.error(e.getMessage(), e);
        return BizExceptionResponseHandler.handleMethodArgumentTypeMismatchException(e);
    }

}
