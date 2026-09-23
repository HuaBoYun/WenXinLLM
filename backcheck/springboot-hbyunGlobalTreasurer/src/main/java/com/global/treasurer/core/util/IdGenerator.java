package com.global.treasurer.core.util;

import java.util.UUID;

/**
 * ID生成器工具类
 * 
 * @author HuaBo Cloud
 * @since 2024-01-01
 */
public class IdGenerator {
    /**
     * 生成UUID作为主键ID
     * 
     * @return UUID字符串（去掉横线）
     */
    public static String generateId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 生成带前缀的ID
     * 
     * @param prefix 前缀
     * @return 带前缀的ID
     */
    public static String generateId(String prefix) {
        return prefix + "_" + generateId();
    }

    /**
     * 生成时间戳ID
     * 
     * @return 时间戳字符串
     */
    public static String generateTimestampId() {
        return String.valueOf(System.currentTimeMillis());
    }

    /**
     * 生成带前缀的时间戳ID
     * 
     * @param prefix 前缀
     * @return 带前缀的时间戳ID
     */
    public static String generateTimestampId(String prefix) {
        return prefix + "_" + generateTimestampId();
    }
}