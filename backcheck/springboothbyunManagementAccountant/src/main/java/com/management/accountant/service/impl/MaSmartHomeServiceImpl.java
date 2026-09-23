package com.management.accountant.service.impl;

import com.management.accountant.oracle.mapper.MaSmartHomeMapper;
import com.management.accountant.service.MaSmartHomeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * 管理会计智慧首页 - 业务层实现
 * 从现有业务表实时聚合计算KPI数据，无需额外建表
 *
 * @author system
 * @date 2025-01-21
 */
@Service
@Slf4j
public class MaSmartHomeServiceImpl implements MaSmartHomeService {

    @Resource
    private MaSmartHomeMapper maSmartHomeMapper;

    @Override
    public Map<String, Object> getSmartHomeData() {
        Map<String, Object> result = new HashMap<>(8);

        // ===== 1. 从 TBL_NCV_BUDGET_DATA 计算全量指标 =====
        Map<String, Object> currentRates = maSmartHomeMapper.selectBudgetRates();
        if (currentRates == null) currentRates = new HashMap<>();

        BigDecimal execRate = getBd(currentRates, "EXEC_RATE");
        BigDecimal deviationRate = getBd(currentRates, "DEVIATION_RATE");
        BigDecimal costSaveRate = getBd(currentRates, "COST_SAVE_RATE");

        // ===== 2. 从 TBL_BUDGET_TASK 计算编制完成数 & 待处理数 =====
        Map<String, Object> taskCount = maSmartHomeMapper.selectBudgetTaskCount();
        if (taskCount == null) taskCount = new HashMap<>();
        int completedCount = getInt(taskCount, "COMPLETED_COUNT");
        int totalTaskCount = getInt(taskCount, "TOTAL_COUNT");
        int controlPending = maSmartHomeMapper.selectControlPendingCount();

        // ===== 3. 趋势暂置0（无年份对比） =====
        BigDecimal execTrend = BigDecimal.ZERO;
        BigDecimal deviationTrend = BigDecimal.ZERO;
        BigDecimal costTrend = BigDecimal.ZERO;

        // 绩效达标率 = 执行率（简化：与预算执行率一致）
        BigDecimal perfRate = execRate;
        BigDecimal perfTrend = execTrend;
        // 报表完成率 = 任务完成率
        BigDecimal reportRate = totalTaskCount > 0
                ? BigDecimal.valueOf(completedCount).multiply(BigDecimal.valueOf(100))
                    .divide(BigDecimal.valueOf(totalTaskCount), 1, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;
        // 税务合规率暂用固定高值（无独立税务表）
        BigDecimal taxRate = BigDecimal.valueOf(99.0);

        // ===== 4. 组装 welcomeStats =====
        Map<String, Object> welcomeStats = new LinkedHashMap<>(8);
        welcomeStats.put("budgetExecutionRate", execRate.setScale(1, RoundingMode.HALF_UP) + "%");
        welcomeStats.put("budgetPreparationCount", completedCount + "套");
        welcomeStats.put("performanceScore", perfRate.setScale(0, RoundingMode.HALF_UP) + "分");
        welcomeStats.put("taxPendingCount", "本月待处理" + controlPending + "项");
        welcomeStats.put("ledgerCount", "4套");

        // ===== 5. 组装 kpiCards =====
        List<Map<String, Object>> kpiCards = new ArrayList<>(6);
        kpiCards.add(buildKpi("预算执行率", execRate, "%", execRate.intValue(), execTrend));
        kpiCards.add(buildKpi("预算偏差率", deviationRate, "%",
                Math.min(deviationRate.multiply(BigDecimal.valueOf(5)).intValue(), 100), deviationTrend));
        kpiCards.add(buildKpi("成本节约率", costSaveRate, "%",
                Math.min(costSaveRate.multiply(BigDecimal.valueOf(5)).intValue(), 100), costTrend));
        kpiCards.add(buildKpi("绩效达标率", perfRate, "%", perfRate.intValue(), perfTrend));
        kpiCards.add(buildKpi("税务合规率", taxRate, "%", taxRate.intValue(), BigDecimal.ZERO));
        kpiCards.add(buildKpi("报表完成率", reportRate, "%", reportRate.intValue(), BigDecimal.ZERO));

        // ===== 6. 组装 pendingCounts =====
        Map<String, Object> pendingCounts = new HashMap<>(4);
        pendingCounts.put("budgetControl", controlPending);

        result.put("welcomeStats", welcomeStats);
        result.put("kpiCards", kpiCards);
        result.put("pendingCounts", pendingCounts);
        return result;
    }

    private Map<String, Object> buildKpi(String label, BigDecimal value, String unit,
                                         int barWidth, BigDecimal trend) {
        Map<String, Object> kpi = new LinkedHashMap<>(6);
        kpi.put("label", label);
        kpi.put("value", value.setScale(1, RoundingMode.HALF_UP).toPlainString());
        kpi.put("unit", unit);
        kpi.put("barWidth", Math.max(0, Math.min(barWidth, 100)));
        kpi.put("trend", trend.setScale(1, RoundingMode.HALF_UP));
        return kpi;
    }

    private BigDecimal getBd(Map<String, Object> map, String key) {
        Object val = map.get(key);
        if (val == null) return BigDecimal.ZERO;
        if (val instanceof BigDecimal) return (BigDecimal) val;
        try { return new BigDecimal(val.toString()); }
        catch (NumberFormatException e) { return BigDecimal.ZERO; }
    }

    private int getInt(Map<String, Object> map, String key) {
        Object val = map.get(key);
        if (val == null) return 0;
        if (val instanceof Number) return ((Number) val).intValue();
        try { return Integer.parseInt(val.toString()); }
        catch (NumberFormatException e) { return 0; }
    }
}
