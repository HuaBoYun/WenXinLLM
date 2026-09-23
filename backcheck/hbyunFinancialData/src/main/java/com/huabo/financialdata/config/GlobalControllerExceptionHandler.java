package com.huabo.financialdata.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.hbfk.util.JsonBean;


/**
 * @author lee
 */
@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public JsonBean handleHttpMessageNotReadableException(MissingServletRequestParameterException ex) {
        logger.error("缺少请求参数,{}", ex.getMessage());
        return new JsonBean(400, "缺少必要的参数", null);
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public JsonBean handleTypeMismatchException(NullPointerException ex) {
        logger.error("空指针异常,{}", ex.getMessage());
        return new JsonBean(500, "空指针异常了", null);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public JsonBean handleUnexpectedServer(Exception ex) {
        logger.error("系统异常：", ex);
        return new JsonBean(500, "系统异常", null);
    }

}

