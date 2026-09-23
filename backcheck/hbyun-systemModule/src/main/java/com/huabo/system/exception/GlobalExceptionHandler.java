package com.huabo.system.exception;

import com.huabo.system.utils.JsonBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理器
 * 用于统一处理应用中的异常，确保异常信息被正确记录
 *
 * @author Augment Code AI
 * @date 2025-01-21
 */
@Slf4j
@RestControllerAdvice(basePackages = "com.huabo.system")
@Component("systemGlobalExceptionHandler")
public class GlobalExceptionHandler {

    /**
     * 处理业务异常
     */
    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<JsonBean> serviceException(HttpServletRequest request, ServiceException ex) {
        log.warn("业务异常 msg={}", ex.getMsg(), ex);
        return new ResponseEntity<>(new JsonBean(ex.getCode(), ex.getMsg(), null), HttpStatus.BAD_REQUEST);
    }

    /**
     * 处理空指针异常
     */
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<JsonBean> nullPointerException(HttpServletRequest request, NullPointerException ex) {
        log.error("空指针异常", ex);
        return new ResponseEntity<>(new JsonBean(500, "服务器内部错误", null), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 处理所有其他异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<JsonBean> handleException(HttpServletRequest request, Exception ex) {
        log.error("系统异常 msg={}", ex.getMessage(), ex);
        return new ResponseEntity<>(new JsonBean(500, "服务器内部错误", null), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 处理运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<JsonBean> runtimeException(HttpServletRequest request, RuntimeException ex) {
        log.error("运行时异常 msg={}", ex.getMessage(), ex);
        return new ResponseEntity<>(new JsonBean(500, "服务器内部错误", null), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

