package com.global.treasurer.common.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 安全工具类
 */
public class SecurityUtils {
    /**
     * 获取当前用户名
     */
    public static String getUsername() {
        Authentication authentication = getAuthentication();
        if (authentication != null) {
            return authentication.getName();
        }
        return null;
    }
    
    /**
     * 获取当前用户ID
     */
    public static Long getUserId() {
        // 这里可以根据实际情况从认证信息中获取用户ID
        return 1L;
    }

    /**
     * 获取当前用户ID（别名方法）
     */
    public static Long getCurrentUserId() {
        return getUserId();
    }

    /**
     * 获取当前组织ID
     */
    public static Long getOrgId() {
        // 这里可以根据实际情况从认证信息中获取组织ID
        return 1L;
    }
    
    /**
     * 获取认证信息
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
    
    /**
     * 判断是否已认证
     */
    public static boolean isAuthenticated() {
        Authentication authentication = getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }
}
