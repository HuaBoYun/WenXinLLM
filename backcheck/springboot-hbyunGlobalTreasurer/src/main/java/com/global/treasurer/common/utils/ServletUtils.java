package com.global.treasurer.common.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet工具类
 */
public class ServletUtils {
    /**
     * 获取当前请求
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes != null ? attributes.getRequest() : null;
    }
    
    /**
     * 获取当前响应
     */
    public static HttpServletResponse getResponse() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes != null ? attributes.getResponse() : null;
    }
    
    /**
     * 获取请求参数
     */
    public static String getParameter(String name) {
        HttpServletRequest request = getRequest();
        return request != null ? request.getParameter(name) : null;
    }
    
    /**
     * 获取请求参数，如果为空则返回默认值
     */
    public static String getParameter(String name, String defaultValue) {
        String value = getParameter(name);
        return StringUtils.isEmpty(value) ? defaultValue : value;
    }

    /**
     * 获取Integer类型请求参数
     */
    public static Integer getParameterToInt(String name) {
        String value = getParameter(name);
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * 获取Integer类型请求参数，如果为空则返回默认值
     */
    public static Integer getParameterToInt(String name, int defaultValue) {
        Integer value = getParameterToInt(name);
        return value != null ? value : defaultValue;
    }

    /**
     * 获取Boolean类型请求参数
     */
    public static Boolean getParameterToBool(String name) {
        String value = getParameter(name);
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        return Boolean.parseBoolean(value);
    }
}
