package com.global.treasurer.annotation;

import java.lang.annotation.*;

/**
 * 灵活的请求体注解
 * 同时支持 application/json 和 application/x-www-form-urlencoded 格式
 * 
 * @author 华博云开发团队
 * @since 2026-02-12
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FlexibleRequestBody {
    
    /**
     * 是否必需
     */
    boolean required() default true;
}

