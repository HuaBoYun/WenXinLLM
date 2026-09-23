package com.huabo.fxgl.service.sync;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据模型同步 Helper —— 纯逻辑，无外部依赖，便于单元测试。
 * <p>
 * 负责组合指标 (TBL_COMBINATION_INDICATOR) 到数据模型 (TBL_DATA_MODEL) 的字段映射
 * 与业务过滤规则实现。
 *
 * @author 华博云
 * @since 2026-08-01 (#TASK-2026-08-01-DATA-MODEL-SYNC)
 */
public final class DataModelSyncHelper {

    /** 来源类型常量 */
    public static final String SOURCE_MANUAL = "MANUAL";
    public static final String SOURCE_COMBINATION_SYNC = "COMBINATION_SYNC";
    public static final String SOURCE_TEMPLATE = "TEMPLATE";

    /** 数据模型 modelCode 长度上限 */
    public static final int MODEL_CODE_MAX_LEN = 50;

    private DataModelSyncHelper() {
        // utility class
    }

    /**
     * 将组合指标下的一个指标映射为数据模型字段 Map。
     * <p>字段名冲突处理：
     * <ul>
     *   <li>indicator.sqlContent → model.sqlStatement</li>
     *   <li>indicator.description → model.businessMeaning</li>
     *   <li>indicator.parameterMapping → model.parameterConfig (JSON 字符串化)</li>
     *   <li>combination.category → model.modelType (FINANCE→FINANCIAL / RISK→RISK / 其他→BUSINESS)</li>
     * </ul>
     */
    public static Map<String, Object> mapIndicatorToDataModel(
            Map<String, Object> indicator, Map<String, Object> combination) {
        Map<String, Object> model = new HashMap<>();
        if (indicator == null) {
            return model;
        }
        model.put("modelCode", normalizeModelCode(asString(indicator.get("indicatorCode"))));
        model.put("modelName", asString(indicator.get("indicatorName")));
        model.put("sqlStatement", asString(indicator.get("sqlContent")));
        model.put("businessMeaning", asString(indicator.get("description")));
        model.put("dataSourceId", asString(indicator.get("dataSourceId")));
        model.put("modelType", categoryToModelType(combination == null ? null : asString(combination.get("category"))));
        model.put("parameterConfig", serializeParameterMapping(indicator.get("parameterMapping")));
        model.put("status", "DRAFT");
        model.put("isEnabled", toYN(indicator.get("isEnabled")));
        // 来源追踪
        model.put("sourceType", SOURCE_COMBINATION_SYNC);
        model.put("sourceCombinationId", combination == null ? null : asString(combination.get("combinationId")));
        model.put("sourceIndicatorConfigId", asString(indicator.get("configId")));
        model.put("sourceIndicatorId", asString(indicator.get("indicatorId")));
        return model;
    }

    /**
     * 从指标列表中过滤"可同步"的指标：按 executionOrder 升序排序后去掉最后一个。
     * <p>业务规则：每个组合的最后一个指标为汇总/终结指标，不参与数据模型同步。
     */
    public static List<Map<String, Object>> filterSyncableIndicators(List<Map<String, Object>> indicators) {
        if (indicators == null || indicators.size() <= 1) {
            return new ArrayList<>();
        }
        List<Map<String, Object>> sorted = new ArrayList<>(indicators);
        sorted.sort(Comparator.comparingInt(DataModelSyncHelper::pickExecutionOrder));
        return new ArrayList<>(sorted.subList(0, sorted.size() - 1));
    }

    /**
     * 依据 templateId、combinationConfigId 判定数据模型来源。
     * 优先级：TEMPLATE > COMBINATION_SYNC > MANUAL。
     */
    public static String resolveSourceType(String templateId, String combinationConfigId) {
        if (isNotBlank(templateId)) {
            return SOURCE_TEMPLATE;
        }
        if (isNotBlank(combinationConfigId)) {
            return SOURCE_COMBINATION_SYNC;
        }
        return SOURCE_MANUAL;
    }

    /**
     * 截断超长的 modelCode，保证不超过 50 字符。
     */
    public static String normalizeModelCode(String code) {
        if (code == null) {
            return null;
        }
        if (code.length() <= MODEL_CODE_MAX_LEN) {
            return code;
        }
        return code.substring(0, MODEL_CODE_MAX_LEN);
    }

    // ============= internal helpers =============

    private static String categoryToModelType(String category) {
        if (category == null) {
            return "BUSINESS";
        }
        switch (category.toUpperCase()) {
            case "FINANCE":
                return "FINANCIAL";
            case "FINANCIAL":
                return "FINANCIAL";
            case "RISK":
                return "RISK";
            case "AUDIT":
                return "AUDIT";
            default:
                return "BUSINESS";
        }
    }

    private static String serializeParameterMapping(Object parameterMapping) {
        if (parameterMapping == null) {
            return null;
        }
        if (parameterMapping instanceof CharSequence) {
            String s = parameterMapping.toString().trim();
            return s.isEmpty() ? null : s;
        }
        return JSON.toJSONString(parameterMapping);
    }

    private static String toYN(Object isEnabled) {
        if (isEnabled == null) {
            return "Y";
        }
        if (isEnabled instanceof Boolean) {
            return ((Boolean) isEnabled) ? "Y" : "N";
        }
        String s = isEnabled.toString();
        if ("Y".equalsIgnoreCase(s) || "true".equalsIgnoreCase(s) || "1".equals(s)) {
            return "Y";
        }
        if ("N".equalsIgnoreCase(s) || "false".equalsIgnoreCase(s) || "0".equals(s)) {
            return "N";
        }
        return "Y";
    }

    private static int pickExecutionOrder(Map<String, Object> indicator) {
        Object v = indicator.get("executionOrder");
        if (v instanceof Number) {
            return ((Number) v).intValue();
        }
        if (v instanceof String && !((String) v).isEmpty()) {
            try {
                return Integer.parseInt((String) v);
            } catch (NumberFormatException ignore) {
                return Integer.MAX_VALUE;
            }
        }
        return Integer.MAX_VALUE;
    }

    private static String asString(Object v) {
        return v == null ? null : v.toString();
    }

    private static boolean isNotBlank(String s) {
        return s != null && !s.trim().isEmpty();
    }
}
