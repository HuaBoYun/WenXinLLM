package com.financial.sharing.util;

import java.util.UUID;

/**
 * UUID工具类 - 兼容层
 * 用于兼容旧代码中使用 com.hbyun.common.util.UUIDUtil 的地方
 * 
 * @author Augment Agent
 * @date 2026-02-04
 */
public class UUIDUtil {
    
    /**
     * 获取UUID字符串（不带横线）
     * @return UUID字符串
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
    
    /**
     * 获取UUID字符串（带横线）
     * @return UUID字符串
     */
    public static String getUUIDWithHyphen() {
        return UUID.randomUUID().toString();
    }
}

