package com.global.treasurer.common.annotation;

import java.lang.annotation.*;

/**
 * 操作日志注解
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperationLog {
    
    /**
     * 模块
     */
    String title() default "";
    
    /**
     * 功能
     */
    String businessType() default "";
    
    /**
     * 操作人类别
     */
    String operatorType() default "";
    
    /**
     * 是否保存请求的参数
     */
    boolean isSaveRequestData() default true;
    
    /**
     * 是否保存响应的参数
     */
    boolean isSaveResponseData() default true;
}
