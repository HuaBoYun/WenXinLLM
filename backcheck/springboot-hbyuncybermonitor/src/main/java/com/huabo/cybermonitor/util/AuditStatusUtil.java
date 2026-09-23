package com.huabo.cybermonitor.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * AUDIT_STATUS 共享字典工具类（单一数据源 SSOT）
 *
 * 适用范围：GZCT_FIN_STATEMENT.AUDIT_STATUS 字段
 *   - 跨模块共用：financialPerformance（前端 evaluationStatus）+ financialAnalysis（前端 auditStatus）
 *   - 同名不同表的 GzctFinancialCompliance.AUDIT_STATUS 不归本类管辖（业务语义不同）
 *
 * 设计原则：
 *   1. normalize：把任意输入（英文大小写 / 历史中文）规范化为标准大写英文 code
 *      —— 入库前必经这一步，杜绝脏中文数据再次写入
 *   2. getChineseLabel：反查中文标签（仅用于日志 / 导出文件，前端不依赖）
 *   3. 字典与前端 src/utils/dict/auditStatusDict.js 必须保持一致
 *      —— 任何一方新增/修改 code 都要同步更新另一方
 *
 * 与前端字典的同步约定：
 *   前端 AUDIT_STATUS_DICT 14 个英文 code ←→ 后端 CODE_LABEL_MAP 14 个英文 code（一一对应）
 *   前端 LEGACY_CN_TO_CODE                 ←→ 后端 LEGACY_CN_TO_CODE（一一对应）
 *
 * @author refactor 2026-06-30 跨模块字典收口
 */
public final class AuditStatusUtil {

    private AuditStatusUtil() {}

    /** 标准英文 code → 中文 label（与前端字典完全对齐） */
    public static final Map<String, String> CODE_LABEL_MAP;

    /** 历史中文 → 标准英文 code 反查表（DB 脏数据兼容） */
    private static final Map<String, String> LEGACY_CN_TO_CODE;

    static {
        Map<String, String> m = new LinkedHashMap<>();
        // 评价语义
        m.put("PENDING", "待评价");
        m.put("EVALUATING", "评价中");
        m.put("EVALUATED", "已评价");
        m.put("FAILED", "评价失败");
        // 审核流程语义
        m.put("DRAFT", "草稿");
        m.put("SUBMITTED", "已提交");
        m.put("REVIEWING", "审核中");
        m.put("APPROVED", "已审核");
        m.put("REJECTED", "已驳回");
        m.put("PUBLISHED", "已发布");
        // 审计语义
        m.put("AUDITED", "已审计");
        m.put("UNAUDITED", "未审计");
        // 分析语义
        m.put("IN_ANALYSIS", "分析中");
        m.put("ANALYZED", "已分析");
        CODE_LABEL_MAP = Collections.unmodifiableMap(m);

        Map<String, String> cn = new HashMap<>();
        cn.put("待评价", "PENDING");
        cn.put("评价中", "EVALUATING");
        cn.put("已评价", "EVALUATED");
        cn.put("评价失败", "FAILED");
        cn.put("草稿", "DRAFT");
        cn.put("已提交", "SUBMITTED");
        cn.put("审核中", "REVIEWING");
        cn.put("已审核", "APPROVED");
        cn.put("已审批", "APPROVED");        // 同义
        cn.put("已驳回", "REJECTED");
        cn.put("已发布", "PUBLISHED");
        cn.put("已审计", "AUDITED");
        cn.put("未审计", "UNAUDITED");
        cn.put("分析中", "IN_ANALYSIS");
        cn.put("已分析", "ANALYZED");
        cn.put("待审核", "PENDING");        // 同义
        LEGACY_CN_TO_CODE = Collections.unmodifiableMap(cn);
    }

    /**
     * 规范化为标准英文 code（入库前必经）
     * <p>
     * 输入：任意大小写英文 / 历史中文 / null / 空串
     * 输出：标准大写英文 code 或 原值（无法识别时保留入参，避免静默吞掉未知值）
     * <p>
     * 设计取舍：未识别值返回原值而非 null，是为了不破坏现有 API 契约（前端会兜底显示"其他"）。
     * 如果调用方要求严格枚举，应该自己再加一层 isKnown() 检查。
     */
    public static String normalize(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        if (trimmed.isEmpty()) return null;
        String upper = trimmed.toUpperCase();
        if (CODE_LABEL_MAP.containsKey(upper)) return upper;
        String mapped = LEGACY_CN_TO_CODE.get(trimmed);
        if (mapped != null) return mapped;
        return trimmed;   // 未知值原样返回（前端 normalize 会兜底为"其他"）
    }

    /** 反查中文标签（仅用于日志 / Excel 导出） */
    public static String getChineseLabel(String code) {
        if (code == null) return "";
        String normalized = normalize(code);
        return CODE_LABEL_MAP.getOrDefault(normalized, normalized);
    }

    /** 校验是否为已知 code（严格枚举模式可用） */
    public static boolean isKnown(String code) {
        if (code == null) return false;
        return CODE_LABEL_MAP.containsKey(code.toUpperCase());
    }
}
