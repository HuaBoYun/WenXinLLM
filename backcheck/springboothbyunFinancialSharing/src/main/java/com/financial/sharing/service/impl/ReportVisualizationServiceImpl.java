package com.financial.sharing.service.impl;

import com.financial.sharing.dto.VisualizationQueryParam;
import com.financial.sharing.service.ReportVisualizationService;
import com.hbfk.util.JsonBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

/**
 * 报表可视化服务实现类
 */
@Service
@Slf4j
public class ReportVisualizationServiceImpl implements ReportVisualizationService {

    @Override
    @Transactional(readOnly = true)
    public JsonBean getVisualizationData(VisualizationQueryParam param) {
        try {
            log.info("获取可视化综合数据, param={}", param);

            // TODO: 从数据库查询真实数据,这里返回模拟数据
            Map<String, Object> result = new HashMap<>();

            // 1. 成本结构分析(饼图)
            List<Map<String, Object>> costStructure = getCostStructureData();
            result.put("costStructure", costStructure);

            // 2. 成本趋势分析(折线图)
            List<Map<String, Object>> costTrend = getCostTrendData();
            result.put("costTrend", costTrend);

            // 3. 成本中心对比(柱状图)
            List<Map<String, Object>> centerComparison = getCenterComparisonData();
            result.put("centerComparison", centerComparison);

            // 4. 产品成本排名(横向柱状图)
            List<Map<String, Object>> productRanking = getProductRankingData();
            result.put("productRanking", productRanking);

            // 5. 预算执行进度(仪表盘)
            Map<String, Object> budgetProgress = getBudgetProgressData();
            result.put("budgetProgress", budgetProgress);

            // 6. 成本构成变化(堆叠面积图)
            List<Map<String, Object>> compositionChange = getCompositionChangeData();
            result.put("compositionChange", compositionChange);

            // 7. 部门成本热力图数据
            List<Map<String, Object>> departmentHeatmap = getDepartmentHeatmapData();
            result.put("departmentHeatmap", departmentHeatmap);

            // 8. 关键指标卡片
            Map<String, Object> keyMetrics = getKeyMetricsData();
            result.put("keyMetrics", keyMetrics);

            return new JsonBean(1, "成功", result);
        } catch (Exception e) {
            log.error("获取可视化综合数据失败", e);
            return new JsonBean(0, "获取可视化综合数据失败: " + e.getMessage(), null);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public JsonBean exportVisualizationReport(VisualizationQueryParam param, String exportType, HttpServletResponse response) {
        try {
            log.info("导出可视化报告, exportType={}", exportType);

            // TODO: 实现真实的PDF或Excel导出功能
            // 这里先返回成功消息
            Map<String, Object> result = new HashMap<>();
            result.put("exportType", exportType);
            result.put("fileName", "成本分析报告_" + System.currentTimeMillis() + "." + exportType);
            result.put("downloadUrl", "/api/download/" + result.get("fileName"));
            result.put("message", "报告导出成功");

            return new JsonBean(1, "成功", result);
        } catch (Exception e) {
            log.error("导出可视化报告失败", e);
            return new JsonBean(0, "导出可视化报告失败: " + e.getMessage(), null);
        }
    }

    // ==================== 私有辅助方法 ====================

    /**
     * 获取成本结构数据
     */
    private List<Map<String, Object>> getCostStructureData() {
        List<Map<String, Object>> data = new ArrayList<>();

        String[] categories = {"直接材料", "直接人工", "制造费用", "研发费用", "销售费用", "管理费用"};
        double[] values = {35.5, 25.3, 18.2, 8.5, 7.3, 5.2};

        for (int i = 0; i < categories.length; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("name", categories[i]);
            item.put("value", values[i]);
            data.add(item);
        }
        return data;
    }

    /**
     * 获取成本趋势数据
     */
    private List<Map<String, Object>> getCostTrendData() {
        List<Map<String, Object>> data = new ArrayList<>();

        for (int i = 1; i <= 12; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("month", "2024-" + String.format("%02d", i));
            item.put("预算", 4000000 + (int)(Math.random() * 1000000));
            item.put("实际", 3500000 + (int)(Math.random() * 1000000));
            item.put("同期", 3000000 + (int)(Math.random() * 1000000));
            data.add(item);
        }
        return data;
    }

    /**
     * 获取成本中心对比数据
     */
    private List<Map<String, Object>> getCenterComparisonData() {
        List<Map<String, Object>> data = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("centerName", "成本中心" + i);
            item.put("预算", 5000000 + (int)(Math.random() * 2000000));
            item.put("实际", 4000000 + (int)(Math.random() * 2000000));
            item.put("执行率", new BigDecimal(70 + Math.random() * 30).setScale(2, BigDecimal.ROUND_HALF_UP));
            data.add(item);
        }
        return data;
    }

    /**
     * 获取产品成本排名数据
     */
    private List<Map<String, Object>> getProductRankingData() {
        List<Map<String, Object>> data = new ArrayList<>();

        String[] products = {"产品A", "产品B", "产品C", "产品D", "产品E", "产品F", "产品G", "产品H"};

        for (int i = 0; i < products.length; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("productName", products[i]);
            item.put("unitCost", new BigDecimal(2000 + Math.random() * 3000).setScale(2, BigDecimal.ROUND_HALF_UP));
            item.put("totalCost", (int)(500000 + Math.random() * 500000));
            item.put("variance", new BigDecimal(-10 + Math.random() * 20).setScale(2, BigDecimal.ROUND_HALF_UP));
            data.add(item);
        }

        // 按单位成本降序排序
        data.sort((a, b) -> ((BigDecimal) b.get("unitCost")).compareTo((BigDecimal) a.get("unitCost")));

        return data;
    }

    /**
     * 获取预算执行进度数据
     */
    private Map<String, Object> getBudgetProgressData() {
        Map<String, Object> data = new HashMap<>();
        data.put("totalBudget", 50000000);
        data.put("usedBudget", 42000000);
        data.put("remainingBudget", 8000000);
        data.put("executionRate", 84.0);
        data.put("yearOverYear", 5.2);
        return data;
    }

    /**
     * 获取成本构成变化数据
     */
    private List<Map<String, Object>> getCompositionChangeData() {
        List<Map<String, Object>> data = new ArrayList<>();

        for (int quarter = 1; quarter <= 4; quarter++) {
            Map<String, Object> item = new HashMap<>();
            item.put("quarter", "Q" + quarter);
            item.put("直接材料", 10000000 + (int)(Math.random() * 2000000));
            item.put("直接人工", 8000000 + (int)(Math.random() * 2000000));
            item.put("制造费用", 5000000 + (int)(Math.random() * 1000000));
            item.put("其他费用", 2000000 + (int)(Math.random() * 1000000));
            data.add(item);
        }
        return data;
    }

    /**
     * 获取部门成本热力图数据
     */
    private List<Map<String, Object>> getDepartmentHeatmapData() {
        List<Map<String, Object>> data = new ArrayList<>();

        String[] departments = {"研发部", "生产部", "销售部", "财务部", "人力部", "采购部"};
        String[] months = {"1月", "2月", "3月", "4月", "5月", "6月"};

        for (String dept : departments) {
            for (String month : months) {
                Map<String, Object> item = new HashMap<>();
                item.put("department", dept);
                item.put("month", month);
                item.put("cost", (int)(500000 + Math.random() * 500000));
                data.add(item);
            }
        }
        return data;
    }

    /**
     * 获取关键指标数据
     */
    private Map<String, Object> getKeyMetricsData() {
        Map<String, Object> data = new HashMap<>();
        data.put("totalCost", 42000000);
        data.put("avgCost", new BigDecimal("2500.50"));
        data.put("costVariance", -5.2);
        data.put("budgetExecution", 84.0);
        data.put("productCount", 128);
        data.put("centerCount", 36);
        return data;
    }
}
