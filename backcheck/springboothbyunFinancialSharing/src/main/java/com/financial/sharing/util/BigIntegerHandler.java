package com.financial.sharing.util;

import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;

/**
 * 大整数处理工具类
 * 用于解决JavaScript长整数精度丢失问题
 *
 * @author 华博云AI助手
 * @date 2025-12-05
 */
@Slf4j
public class BigIntegerHandler {

    /**
     * JavaScript安全整数最大值
     */
    private static final BigInteger MAX_SAFE_INTEGER = new BigInteger("9007199254740991");

    /**
     * JavaScript安全整数最小值
     */
    private static final BigInteger MIN_SAFE_INTEGER = new BigInteger("-9007199254740991");

    /**
     * Long最大值
     */
    private static final BigInteger LONG_MAX_VALUE = new BigInteger(String.valueOf(Long.MAX_VALUE));

    /**
     * Long最小值
     */
    private static final BigInteger LONG_MIN_VALUE = new BigInteger(String.valueOf(Long.MIN_VALUE));

    /**
     * 检查字符串是否为有效的大整数
     * @param numStr 数字字符串
     * @return 是否为有效大整数
     */
    public static boolean isValidBigInteger(String numStr) {
        if (numStr == null || numStr.trim().isEmpty()) {
            return false;
        }

        try {
            String trimmedStr = numStr.trim();
            new BigInteger(trimmedStr);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 安全转换字符串为Long
     * @param numStr 数字字符串
     * @return Long值，转换失败返回null
     */
    public static Long safeParseLong(String numStr) {
        if (numStr == null || numStr.trim().isEmpty()) {
            return null;
        }

        try {
            String trimmedStr = numStr.trim();
            BigInteger bigInt = new BigInteger(trimmedStr);

            // 检查是否超出Long范围
            if (bigInt.compareTo(LONG_MAX_VALUE) > 0 || bigInt.compareTo(LONG_MIN_VALUE) < 0) {
                log.warn("数字 {} 超出Long范围，返回null", trimmedStr);
                return null;
            }

            return bigInt.longValue();
        } catch (NumberFormatException e) {
            log.error("无法解析数字字符串: {}", numStr, e);
            return null;
        }
    }

    /**
     * 安全转换字符串为Long，提供默认值
     * @param numStr 数字字符串
     * @param defaultValue 默认值
     * @return Long值
     */
    public static Long safeParseLong(String numStr, Long defaultValue) {
        Long result = safeParseLong(numStr);
        return result != null ? result : defaultValue;
    }

    /**
     * 检查数字是否超出JavaScript安全范围
     * @param numStr 数字字符串
     * @return 是否超出安全范围
     */
    public static boolean exceedsJavaScriptSafeRange(String numStr) {
        if (!isValidBigInteger(numStr)) {
            return false;
        }

        try {
            BigInteger bigInt = new BigInteger(numStr.trim());
            return bigInt.compareTo(MAX_SAFE_INTEGER) > 0 || bigInt.compareTo(MIN_SAFE_INTEGER) < 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 检查Long是否超出JavaScript安全范围
     * @param num Long数字
     * @return 是否超出安全范围
     */
    public static boolean exceedsJavaScriptSafeRange(Long num) {
        if (num == null) {
            return false;
        }

        BigInteger bigInt = BigInteger.valueOf(num);
        return bigInt.compareTo(MAX_SAFE_INTEGER) > 0 || bigInt.compareTo(MIN_SAFE_INTEGER) < 0;
    }

    /**
     * 格式化大整数用于前端显示
     * @param numStr 数字字符串
     * @return 格式化后的显示字符串
     */
    public static String formatForDisplay(String numStr) {
        if (!isValidBigInteger(numStr)) {
            return numStr;
        }

        String trimmedStr = numStr.trim();

        // 如果超出安全范围，进行截断显示
        if (exceedsJavaScriptSafeRange(trimmedStr)) {
            if (trimmedStr.length() > 12) {
                return trimmedStr.substring(0, 8) + "..." + trimmedStr.substring(trimmedStr.length() - 4);
            }
        }

        return trimmedStr;
    }

    /**
     * 格式化Long用于前端显示
     * @param num Long数字
     * @return 格式化后的显示字符串
     */
    public static String formatForDisplay(Long num) {
        if (num == null) {
            return null;
        }

        String numStr = String.valueOf(num);

        // 如果超出安全范围，进行截断显示
        if (exceedsJavaScriptSafeRange(num)) {
            if (numStr.length() > 12) {
                return numStr.substring(0, 8) + "..." + numStr.substring(numStr.length() - 4);
            }
        }

        return numStr;
    }

    /**
     * 将数字转换为字符串，确保前端不会出现精度丢失
     * @param num 数字对象
     * @return 字符串形式的数字
     */
    public static String toSafeString(Object num) {
        if (num == null) {
            return null;
        }

        if (num instanceof String) {
            String str = ((String) num).trim();
            return isValidBigInteger(str) ? str : str;
        }

        if (num instanceof Number) {
            return num.toString();
        }

        return num.toString();
    }

    /**
     * 记录大整数警告日志
     * @param num 数字
     * @param context 上下文信息
     */
    public static void logLargeNumberWarning(Object num, String context) {
        if (num == null) {
            return;
        }

        boolean isUnsafe = false;
        String numStr = "";

        if (num instanceof String) {
            numStr = (String) num;
            isUnsafe = exceedsJavaScriptSafeRange(numStr);
        } else if (num instanceof Long) {
            numStr = String.valueOf(num);
            isUnsafe = exceedsJavaScriptSafeRange((Long) num);
        }

        if (isUnsafe) {
            log.warn("[大整数精度警告] {} - 检测到超出JavaScript安全范围的整数: {}",
                    context, numStr);
        }
    }

    /**
     * 验证并转换前端传来的ID参数
     * @param idObj 前端传来的ID对象
     * @param paramName 参数名称
     * @return 转换后的Long ID，无效时返回null
     */
    public static Long validateAndConvertId(Object idObj, String paramName) {
        if (idObj == null) {
            return null;
        }

        String idStr = toSafeString(idObj);

        if (idStr == null || idStr.trim().isEmpty()) {
            return null;
        }

        // 验证格式
        if (!isValidBigInteger(idStr)) {
            log.warn("参数 {} 格式无效: {}", paramName, idStr);
            return null;
        }

        // 转换为Long
        Long id = safeParseLong(idStr);
        if (id == null) {
            log.warn("参数 {} 转换Long失败: {}", paramName, idStr);
            return null;
        }

        // 检查是否为大整数
        if (exceedsJavaScriptSafeRange(id)) {
            log.info("参数 {} 为大整数 {}，超出JavaScript安全范围", paramName, id);
        }

        return id;
    }

    /**
     * 将Map中的ID字段转换为字符串，避免前端精度丢失
     * @param map 包含ID字段的Map
     */
    public static void convertIdsToStrings(java.util.Map<String, Object> map) {
        if (map == null) {
            return;
        }

        for (java.util.Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            // 检查是否为ID字段（以Id、ID、id结尾的字段）
            if (key != null && (key.endsWith("Id") || key.endsWith("ID") || key.endsWith("id"))) {
                if (value instanceof Number) {
                    String strValue = toSafeString(value);
                    map.put(key, strValue);

                    // 记录大整数警告
                    logLargeNumberWarning(value, "字段-" + key);
                }
            }
        }
    }

    /**
     * 创建错误响应
     * @param message 错误消息
     * @return JsonBean错误响应
     */
    public static com.hbfk.util.JsonBean createErrorResponse(String message) {
        com.hbfk.util.JsonBean jsonBean = new com.hbfk.util.JsonBean();
        jsonBean.setCode(0);
        jsonBean.setMsg(message);
        jsonBean.setData(null);
        return jsonBean;
    }

    /**
     * 检查ID参数是否有效
     * @param idStr ID字符串
     * @param paramName 参数名称
     * @return 是否有效
     */
    public static boolean isValidIdParameter(String idStr, String paramName) {
        if (idStr == null || idStr.trim().isEmpty()) {
            log.warn("参数 {} 为空", paramName);
            return false;
        }

        if (!isValidBigInteger(idStr)) {
            log.warn("参数 {} 格式无效: {}", paramName, idStr);
            return false;
        }

        return true;
    }

    /**
     * 从Map中安全提取大整数ID，并转换为Long类型
     * 专门处理前端JavaScript精度丢失问题
     *
     * @param paramMap 参数Map
     * @param key 键名
     * @param paramName 参数名称（用于日志）
     * @return Long类型的ID值，如果为空或无效则返回null
     */
    public static Long extractLargeIdFromMap(java.util.Map<String, Object> paramMap, String key, String paramName) {
        if (paramMap == null || !paramMap.containsKey(key)) {
            log.debug("参数Map中不存在键: {}", key);
            return null;
        }

        Object value = paramMap.get(key);
        if (value == null) {
            return null;
        }

        // 如果已经是Long类型，直接返回
        if (value instanceof Long) {
            Long longValue = (Long) value;
            logLargeNumberWarning(longValue, paramName);
            return longValue;
        }

        // 如果是String类型，尝试转换
        String strValue = value.toString().trim();
        if (strValue.isEmpty() || "null".equalsIgnoreCase(strValue)) {
            return null;
        }

        // 验证并转换为Long
        Long result = validateAndConvertId(strValue, paramName);
        if (result != null) {
            // 更新Map中的值为Long类型
            paramMap.put(key, result);
            log.info("从Map中提取并转换大整数ID {}: {} -> {}", paramName, strValue, result);
        }

        return result;
    }

    /**
     * 处理Map中的所有大整数ID字段
     * 自动识别并转换可能存在精度问题的ID字段
     *
     * @param paramMap 参数Map
     */
    public static void processLargeIntegerIds(java.util.Map<String, Object> paramMap) {
        if (paramMap == null) {
            return;
        }

        // 常见的ID字段模式
        java.util.List<String> idFields = java.util.Arrays.asList(
            "id", "ID", "Id",
            "budgetId", "centerId", "userId", "deptId", "orgId",
            "budget_id", "center_id", "user_id", "dept_id", "org_id"
        );

        for (String field : idFields) {
            if (paramMap.containsKey(field)) {
                extractLargeIdFromMap(paramMap, field, "字段-" + field);
            }
        }
    }

    /**
     * 检查两个Long值是否可能因JavaScript精度丢失而不同
     * 用于验证前端传来的值是否被截断
     *
     * @param frontendValue 前端传来的值
     * @param expectedValue 期望的正确值
     * @return 是否可能存在精度丢失
     */
    public static boolean isPossiblePrecisionLoss(Long frontendValue, Long expectedValue) {
        if (frontendValue == null || expectedValue == null) {
            return false;
        }

        // 如果前端值超出了JavaScript安全范围，且与期望值不同，则可能存在精度丢失
        return exceedsJavaScriptSafeRange(frontendValue) && !frontendValue.equals(expectedValue);
    }
}