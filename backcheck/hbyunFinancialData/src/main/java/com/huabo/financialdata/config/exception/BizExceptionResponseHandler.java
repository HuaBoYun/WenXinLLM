package com.huabo.financialdata.config.exception;

import com.huabo.financialdata.config.mvn.ApiResponse;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * 异常返回信息处理类
 *
 * @author lee
 * @version 1.0.0
 * @description
 **/
public class BizExceptionResponseHandler {

    /**
     * 通过异常错误码及异常信息输出异常返回的基础信息对象
     *
     * @param e BizException
     * @return apiResponse
     */
    @SuppressWarnings("rawtypes")
    public static ApiResponse handler(BizException e) {
        int code = e.getBizCode().getCode();
        String message = e.getBizCode().getName();
        return ApiResponse.response(code, null, message);
    }

    /**
     * JSR-303，Vaild校验一场信息输出
     *
     * @param e BizException
     * @return apiResponse
     */
    public static ApiResponse vaildhandler(BizException e) {
        int code = e.getBizCode().getCode();
        String message = e.getMessage();
        return ApiResponse.response(code, null, message);
    }

    /**
     * 通用错误类的处理
     *
     * @param e BizException
     * @return apiResponse
     */
    @SuppressWarnings("rawtypes")
    public static ApiResponse handler(Exception e) {
        int code = BizCodeEnumDefault.INTERNAL_SERVER_ERROR.getValue();
        String message = BizCodeEnumDefault.INTERNAL_SERVER_ERROR.getName();
        return ApiResponse.response(code, null, message);
    }


    /**
     * 处理请求方法参数问题
     *
     * @param e
     * @return
     */
    public static ApiResponse handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        //在控制范围内的异常正常抛出
        Throwable cause = e.getCause();
        if (cause instanceof NumberFormatException) {
            int code = BizCodeEnumDefault.REQUEST_PARAM_ERROR_NUMBER.getValue();
            String message = BizCodeEnumDefault.REQUEST_PARAM_ERROR_NUMBER.getName();
            return ApiResponse.response(code, null, message);
        } else {
            //控制范围之外的都统一抛出500
            return handler(e);
        }
    }

}
