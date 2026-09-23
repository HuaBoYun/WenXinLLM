package com.huabo.fxgl.util;

import com.huabo.fxgl.entity.TblRiskWarning;

import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 报告数据处理辅助类
 * 统一各种格式报告（Excel/PDF/Word）的数据处理逻辑，避免重复代码
 *
 * @author 华博云开发团队
 * @since 2025-11-15
 */
public class ReportDataHelper {

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /** 预警类型中文映射 */
    private static final Map<String, String> WARNING_TYPE_MAP = new HashMap<>();
    /** 预警级别中文映射 */
    private static final Map<String, String> WARNING_LEVEL_MAP = new HashMap<>();
    /** 预警状态中文映射 */
    private static final Map<String, String> WARNING_STATUS_MAP = new HashMap<>();
    /** 报告类型中文映射 */
    private static final Map<String, String> REPORT_TYPE_MAP = new HashMap<>();
    /** 时间范围中文映射 */
    private static final Map<String, String> TIME_RANGE_MAP = new HashMap<>();

    static {
        WARNING_TYPE_MAP.put("FINANCIAL_RISK", "财务风险");
        WARNING_TYPE_MAP.put("PROCUREMENT_RISK", "采购风险");
        WARNING_TYPE_MAP.put("CREDIT_RISK", "信用风险");
        WARNING_TYPE_MAP.put("COMPLIANCE_RISK", "合规风险");
        WARNING_TYPE_MAP.put("COMBINATION_EXECUTION", "组合执行");
        WARNING_TYPE_MAP.put("DATA_MODEL_EXECUTION", "模型执行");
        WARNING_TYPE_MAP.put("AUTO_GENERATED", "自动生成");
        WARNING_TYPE_MAP.put("MANUAL_CREATED", "手动创建");
        WARNING_TYPE_MAP.put("THRESHOLD", "阈值预警");
        WARNING_TYPE_MAP.put("TREND", "趋势预警");
        WARNING_TYPE_MAP.put("ANOMALY", "异常预警");

        WARNING_LEVEL_MAP.put("HIGH", "高风险");
        WARNING_LEVEL_MAP.put("MEDIUM", "中风险");
        WARNING_LEVEL_MAP.put("LOW", "低风险");
        WARNING_LEVEL_MAP.put("CRITICAL", "严重风险");

        WARNING_STATUS_MAP.put("PENDING", "待处理");
        WARNING_STATUS_MAP.put("PROCESSING", "处理中");
        WARNING_STATUS_MAP.put("PROCESSED", "已处理");
        WARNING_STATUS_MAP.put("IGNORED", "已忽略");

        REPORT_TYPE_MAP.put("SUMMARY", "汇总报告");
        REPORT_TYPE_MAP.put("DETAILED", "详细报告");
        REPORT_TYPE_MAP.put("TREND", "趋势分析报告");
        REPORT_TYPE_MAP.put("PERFORMANCE", "性能分析报告");

        TIME_RANGE_MAP.put("TODAY", "今天");
        TIME_RANGE_MAP.put("WEEK", "本周");
        TIME_RANGE_MAP.put("MONTH", "本月");
        TIME_RANGE_MAP.put("QUARTER", "本季度");
        TIME_RANGE_MAP.put("YEAR", "本年");
        TIME_RANGE_MAP.put("CUSTOM", "自定义");
    }

    public static String getWarningTypeText(String code) {
        if (code == null) return "";
        return WARNING_TYPE_MAP.getOrDefault(code, code);
    }

    public static String getWarningLevelText(String code) {
        if (code == null) return "";
        return WARNING_LEVEL_MAP.getOrDefault(code, code);
    }

    public static String getWarningStatusText(String code) {
        if (code == null) return "";
        return WARNING_STATUS_MAP.getOrDefault(code, code);
    }

    public static String getReportTypeText(String code) {
        if (code == null) return "汇总报告";
        return REPORT_TYPE_MAP.getOrDefault(code, code);
    }

    public static String getTimeRangeText(String code) {
        if (code == null) return "本月";
        return TIME_RANGE_MAP.getOrDefault(code, code);
    }

    public static String formatDateTime(java.time.LocalDateTime dt) {
        if (dt == null) return "";
        return DATE_TIME_FORMATTER.format(dt);
    }

    public static String formatDate(java.util.Date date) {
        if (date == null) return "";
        return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    /**
     * 格式化 LocalDateTime 为字符串（重载：接收 LocalDateTime）
     */
    public static String formatDate(java.time.LocalDateTime dt) {
        if (dt == null) return "";
        return DATE_TIME_FORMATTER.format(dt);
    }

    /**
     * 计算预警数据的统计信息
     */
    public static Map<String, Integer> calculateStatistics(List<TblRiskWarning> warnings) {
        Map<String, Integer> stats = new LinkedHashMap<>();
        stats.put("total", warnings.size());
        stats.put("high", 0);
        stats.put("medium", 0);
        stats.put("low", 0);
        stats.put("pending", 0);
        stats.put("processed", 0);
        for (TblRiskWarning w : warnings) {
            String level = w.getWarningLevel();
            if ("HIGH".equals(level)) stats.put("high", stats.get("high") + 1);
            else if ("MEDIUM".equals(level)) stats.put("medium", stats.get("medium") + 1);
            else if ("LOW".equals(level)) stats.put("low", stats.get("low") + 1);
            String status = w.getWarningStatus();
            if ("PENDING".equals(status) || "PROCESSING".equals(status)) {
                stats.put("pending", stats.get("pending") + 1);
            } else if ("PROCESSED".equals(status)) {
                stats.put("processed", stats.get("processed") + 1);
            }
        }
        return stats;
    }

    /**
     * 生成建议措施
     */
    public static List<String> generateRecommendations(List<TblRiskWarning> warnings) {
        List<String> recommendations = new ArrayList<>();
        Map<String, Integer> stats = calculateStatistics(warnings);
        int highCount = stats.get("high");
        int pendingCount = stats.get("pending");
        long financialRiskCount = warnings.stream()
                .filter(w -> "FINANCIAL_RISK".equals(w.getWarningType())).count();
        if (highCount > 0) {
            recommendations.add("发现 " + highCount + " 个高风险预警，建议立即组织风险评估会议，制定应对措施。");
        }
        if (pendingCount > 0) {
            recommendations.add("当前有 " + pendingCount + " 个预警待处理，建议在3个工作日内完成处理并记录处理结果。");
        }
        if (financialRiskCount > 0) {
            recommendations.add("存在 " + financialRiskCount + " 个财务风险预警，建议财务部门进行专项审查，确保资金安全。");
        }
        if (warnings.size() > 50) {
            recommendations.add("预警数量较多，建议优化风险管理流程，加强预防性控制措施。");
        }
        if (recommendations.isEmpty()) {
            recommendations.add("当前风险状况良好，请继续保持风险监控，定期开展风险评估。");
        }
        return recommendations;
    }

    /**
     * 🔥 根据报告类型 + 用户勾选，计算最终应包含的内容区块
     * SUMMARY(汇总)     -> STATISTICS + RECOMMENDATIONS
     * DETAILED(详细)    -> STATISTICS + DETAILS + CHARTS + RECOMMENDATIONS
     * TREND(趋势)       -> STATISTICS + TRENDS + CHARTS
     * PERFORMANCE(性能) -> STATISTICS + PERFORMANCE + RECOMMENDATIONS
     * 同时保留用户额外勾选的区块
     */
    public static Set<String> computeIncludes(String reportType, List<String> userIncludes) {
        Set<String> includes = new LinkedHashSet<>();
        // 用户勾选的选项
        if (userIncludes != null) includes.addAll(userIncludes);
        // 根据报告类型强制补齐必备区块
        if ("SUMMARY".equals(reportType)) {
            includes.add("STATISTICS");
            includes.add("RECOMMENDATIONS");
        } else if ("DETAILED".equals(reportType)) {
            includes.add("STATISTICS");
            includes.add("DETAILS");
            includes.add("CHARTS");
            includes.add("RECOMMENDATIONS");
        } else if ("TREND".equals(reportType)) {
            includes.add("STATISTICS");
            includes.add("TRENDS");
            includes.add("CHARTS");
        } else if ("PERFORMANCE".equals(reportType)) {
            includes.add("STATISTICS");
            includes.add("PERFORMANCE");
            includes.add("RECOMMENDATIONS");
        } else {
            // 未知类型，至少显示统计
            includes.add("STATISTICS");
        }
        return includes;
    }

    /**
     * 🔥 聚合时间趋势数据（用于趋势分析区块）
     * 返回按日期分组的预警数量映射
     */
    public static Map<String, Integer> aggregateTrendData(List<TblRiskWarning> warnings) {
        Map<String, Integer> trend = new TreeMap<>();
        for (TblRiskWarning w : warnings) {
            java.time.LocalDateTime time = w.getCreateTime();
            if (time == null) time = w.getWarningTime();
            if (time == null) continue;
            String date = time.toLocalDate().toString();
            trend.merge(date, 1, Integer::sum);
        }
        return trend;
    }

    /**
     * 🔥 生成趋势分析文本描述
     */
    public static List<String> generateTrendInsights(List<TblRiskWarning> warnings) {
        List<String> insights = new ArrayList<>();
        Map<String, Integer> trend = aggregateTrendData(warnings);
        if (trend.isEmpty()) {
            insights.add("暂无时间序列数据，无法进行趋势分析。");
            return insights;
        }
        int total = warnings.size();
        int days = trend.size();
        double avgPerDay = days > 0 ? (double) total / days : 0;
        int maxDaily = trend.values().stream().max(Integer::compareTo).orElse(0);
        int minDaily = trend.values().stream().min(Integer::compareTo).orElse(0);
        String peakDate = trend.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey).orElse("-");

        insights.add(String.format("统计周期内共产生 %d 条预警，覆盖 %d 个日期，日均 %.1f 条。", total, days, avgPerDay));
        insights.add(String.format("单日最高预警量为 %d 条（%s），最低 %d 条。", maxDaily, peakDate, minDaily));

        // 环比趋势（对比前后半段）
        if (days >= 4) {
            List<Map.Entry<String, Integer>> entries = new ArrayList<>(trend.entrySet());
            int half = entries.size() / 2;
            int firstHalfSum = 0, secondHalfSum = 0;
            for (int i = 0; i < half; i++) firstHalfSum += entries.get(i).getValue();
            for (int i = half; i < entries.size(); i++) secondHalfSum += entries.get(i).getValue();
            if (firstHalfSum > 0) {
                double changePct = (secondHalfSum - firstHalfSum) * 100.0 / firstHalfSum;
                String trendWord = changePct > 5 ? "呈上升" : (changePct < -5 ? "呈下降" : "保持平稳");
                insights.add(String.format("近期预警数量%s趋势，环比变化 %.1f%%（前半段 %d 条 vs 后半段 %d 条）。",
                        trendWord, changePct, firstHalfSum, secondHalfSum));
            }
        }
        return insights;
    }

    /**
     * 🔥 计算处理效率数据（用于性能分析区块）
     */
    public static Map<String, Object> calculatePerformance(List<TblRiskWarning> warnings) {
        Map<String, Object> perf = new LinkedHashMap<>();
        int total = warnings.size();
        long processed = warnings.stream().filter(w -> "PROCESSED".equals(w.getWarningStatus())).count();
        long pending = warnings.stream().filter(w ->
                "PENDING".equals(w.getWarningStatus()) || "PROCESSING".equals(w.getWarningStatus())).count();
        long ignored = warnings.stream().filter(w -> "IGNORED".equals(w.getWarningStatus())).count();

        double processedRate = total > 0 ? processed * 100.0 / total : 0;
        double pendingRate = total > 0 ? pending * 100.0 / total : 0;
        double ignoredRate = total > 0 ? ignored * 100.0 / total : 0;

        perf.put("total", total);
        perf.put("processed", processed);
        perf.put("pending", pending);
        perf.put("ignored", ignored);
        perf.put("processedRate", String.format("%.1f%%", processedRate));
        perf.put("pendingRate", String.format("%.1f%%", pendingRate));
        perf.put("ignoredRate", String.format("%.1f%%", ignoredRate));
        return perf;
    }
}
