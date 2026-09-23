package com.financial.sharing.config;

import com.hbfk.util.JsonBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.sql.SQLException;

/**
 * 财务共享模块全局异常处理器
 * 统一处理系统异常，提供友好的错误响应
 *
 * @author system
 * @since 2024-12-09
 */
@Slf4j
@RestControllerAdvice(basePackages = "com.financial.sharing")
public class FinancialGlobalExceptionHandler {

    /**
     * 处理所有未捕获的异常 - 兜底保护
     */
    @ExceptionHandler(Exception.class)
    public JsonBean handleException(Exception e) {
        log.error("系统异常 - 未捕获异常", e);
        return new JsonBean(0, "系统繁忙，请稍后重试", null);
    }

    /**
     * 处理业务运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public JsonBean handleRuntimeException(RuntimeException e) {
        log.error("业务异常", e);
        String message = e.getMessage();
        // 如果异常消息为空或过长，使用默认消息
        if (message == null || message.trim().isEmpty()) {
            message = "业务处理异常";
        } else if (message.length() > 200) {
            message = message.substring(0, 200) + "...";
        }
        return new JsonBean(0, message, null);
    }

    /**
     * 处理参数异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public JsonBean handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("参数异常: {}", e.getMessage(), e);
        return new JsonBean(0, "参数错误: " + e.getMessage(), null);
    }

    /**
     * 处理空指针异常
     */
    @ExceptionHandler(NullPointerException.class)
    public JsonBean handleNullPointerException(NullPointerException e) {
        log.error("空指针异常", e);
        // 不暴露具体错误信息，避免泄露系统内部结构
        return new JsonBean(0, "系统处理异常，请稍后重试", null);
    }

    /**
     * 处理数据访问异常
     */
    @ExceptionHandler({DataAccessException.class, SQLException.class})
    public JsonBean handleDataAccessException(Exception e) {
        log.error("数据库访问异常", e);
        return new JsonBean(0, "数据操作异常，请稍后重试", null);
    }

    /**
     * 处理方法参数类型不匹配异常
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public JsonBean handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.warn("参数类型不匹配: {}", e.getMessage());
        return new JsonBean(0, "参数格式错误，请检查输入", null);
    }

    /**
     * 处理JSON转换异常
     */
    @ExceptionHandler(HttpMessageConversionException.class)
    public JsonBean handleHttpMessageConversionException(HttpMessageConversionException e) {
        log.warn("JSON转换异常: {}", e.getMessage());
        return new JsonBean(0, "数据格式错误，请检查输入", null);
    }

    /**
     * 处理参数验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public JsonBean handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.warn("参数验证失败: {}", e.getMessage());
        StringBuilder sb = new StringBuilder("参数验证失败:");
        e.getBindingResult().getFieldErrors().forEach(error -> {
            sb.append(error.getField()).append(" ").append(error.getDefaultMessage()).append("; ");
        });
        return new JsonBean(0, sb.toString(), null);
    }

    /**
     * 处理数据库操作相关异常
     */
    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public JsonBean handleDataIntegrityViolationException(org.springframework.dao.DataIntegrityViolationException e) {
        log.error("数据完整性约束异常", e);
        return new JsonBean(0, "数据操作违反完整性约束", null);
    }

    /**
     * 处理MyBatis相关异常
     */
    @ExceptionHandler(org.apache.ibatis.exceptions.PersistenceException.class)
    public JsonBean handlePersistenceException(org.apache.ibatis.exceptions.PersistenceException e) {
        log.error("MyBatis持久化异常", e);
        return new JsonBean(0, "数据持久化异常，请稍后重试", null);
    }
}