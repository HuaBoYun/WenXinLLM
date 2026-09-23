package com.management.accountant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.management.accountant.oracle.entity.budget.BudgetAnalysisChart;
import com.management.accountant.oracle.mapper.budget.BudgetAnalysisChartMapper;
import com.management.accountant.service.BudgetAnalysisChartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 预算分析图表 Service 实现
 */
@Service
@Slf4j
public class BudgetAnalysisChartServiceImpl implements BudgetAnalysisChartService {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    @Resource
    private BudgetAnalysisChartMapper chartMapper;

    // 图表类型元数据
    private static final Map<String, String[]> CHART_TYPE_META = new LinkedHashMap<>();
    static {
        CHART_TYPE_META.put("LINE",    new String[]{"折线图", "el-icon-data-line",    "展示数据趋势变化"});
        CHART_TYPE_META.put("BAR",     new String[]{"柱状图", "el-icon-s-data",       "对比不同类别数据"});
        CHART_TYPE_META.put("PIE",     new String[]{"饼图",   "el-icon-pie-chart",    "展示数据占比分布"});
        CHART_TYPE_META.put("SCATTER", new String[]{"散点图", "el-icon-data-board",   "分析数据相关性"});
        CHART_TYPE_META.put("RADAR",   new String[]{"雷达图", "el-icon-discover",     "多维度对比分析"});
        CHART_TYPE_META.put("HEATMAP", new String[]{"热力图", "el-icon-s-grid",       "展示数据密度分布"});
        CHART_TYPE_META.put("TREEMAP", new String[]{"树图",   "el-icon-s-operation",  "层级结构数据展示"});
        CHART_TYPE_META.put("SANKEY",  new String[]{"桑基图", "el-icon-sort",         "展示数据流向关系"});
    }

    @Override
    public Map<String, Object> getChartStats() {
        Map<String, Object> stats = new HashMap<>();
        try {
            Map<String, Object> dbStats = chartMapper.selectChartStats();
            if (dbStats != null) {
                stats.put("totalCharts",      getInt(dbStats, "TOTAL_CHARTS"));
                stats.put("chartTypes",       getInt(dbStats, "CHART_TYPES"));
                stats.put("interactiveCharts", getInt(dbStats, "INTERACTIVE_CHARTS"));
                stats.put("totalExports",     getInt(dbStats, "TOTAL_EXPORTS"));
            }
        } catch (Exception e) {
            log.error("获取图表统计失败", e);
            stats.put("totalCharts", 0);
            stats.put("chartTypes", 0);
            stats.put("interactiveCharts", 0);
            stats.put("totalExports", 0);
        }
        return stats;
    }

    @Override
    public List<Map<String, Object>> getChartTypes() {
        List<Map<String, Object>> result = new ArrayList<>();
        try {
            // 从DB统计每种类型的使用次数
            List<Map<String, Object>> dbTypes = chartMapper.selectChartTypeStats();
            Map<String, Integer> usageMap = new HashMap<>();
            if (dbTypes != null) {
                for (Map<String, Object> t : dbTypes) {
                    usageMap.put(String.valueOf(t.get("CHART_TYPE")), getInt(t, "USAGE_COUNT"));
                }
            }
            int id = 1;
            for (Map.Entry<String, String[]> entry : CHART_TYPE_META.entrySet()) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", id++);
                item.put("code", entry.getKey());
                item.put("name", entry.getValue()[0]);
                item.put("icon", entry.getValue()[1]);
                item.put("description", entry.getValue()[2]);
                item.put("usageCount", usageMap.getOrDefault(entry.getKey(), 0));
                result.add(item);
            }
        } catch (Exception e) {
            log.error("获取图表类型失败", e);
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> getChartLibrary() {
        List<Map<String, Object>> result = new ArrayList<>();
        try {
            QueryWrapper<BudgetAnalysisChart> qw = new QueryWrapper<>();
            qw.eq("DEL_FLAG", 0).orderByDesc("CREATE_TIME");
            List<BudgetAnalysisChart> charts = chartMapper.selectList(qw);
            for (BudgetAnalysisChart c : charts) {
                result.add(chartToMap(c));
            }
        } catch (Exception e) {
            log.error("获取图表库失败", e);
        }
        return result;
    }

    @Override
    public Map<String, Object> getChartPage(Map<String, Object> params) {
        Map<String, Object> pageResult = new HashMap<>();
        try {
            int pageNum = getInt(params, "pageNum", 1);
            int pageSize = getInt(params, "pageSize", 10);
            params.put("startRow", (pageNum - 1) * pageSize);
            params.put("endRow", pageNum * pageSize);
            int total = chartMapper.selectChartPageCount(params);
            List<BudgetAnalysisChart> list = chartMapper.selectChartPage(params);
            pageResult.put("list", list.stream().map(this::chartToMap).collect(Collectors.toList()));
            pageResult.put("total", total);
            pageResult.put("pageNum", pageNum);
            pageResult.put("pageSize", pageSize);
        } catch (Exception e) {
            log.error("分页查询图表失败", e);
            pageResult.put("list", new ArrayList<>());
            pageResult.put("total", 0);
        }
        return pageResult;
    }

    @Override
    public Map<String, Object> createChart(Map<String, Object> data) {
        BudgetAnalysisChart chart = new BudgetAnalysisChart();
        chart.setChartName((String) data.getOrDefault("chartName", "新图表"));
        chart.setChartType((String) data.getOrDefault("chartType", "LINE"));
        chart.setDataSource((String) data.getOrDefault("dataSource", "BUDGET_DATA"));
        chart.setChartConfig((String) data.getOrDefault("chartConfig", "{}"));
        chart.setDescription((String) data.getOrDefault("description", ""));
        chart.setThumbnail((String) data.getOrDefault("thumbnail", ""));
        chart.setStatus("PUBLISHED");
        chart.setUsageCount(0);
        chart.setCreator((String) data.getOrDefault("creator", "admin"));
        chart.setCreateTime(new Date());
        chart.setUpdateTime(new Date());
        chart.setDelFlag(0);
        chartMapper.insert(chart);
        return chartToMap(chart);
    }

    @Override
    public Map<String, Object> updateChart(Map<String, Object> data) {
        String chartId = (String) data.get("chartId");
        BudgetAnalysisChart chart = chartMapper.selectById(chartId);
        if (chart == null || chart.getDelFlag() == 1) {
            throw new RuntimeException("图表不存在");
        }
        if (data.containsKey("chartName")) chart.setChartName((String) data.get("chartName"));
        if (data.containsKey("chartType")) chart.setChartType((String) data.get("chartType"));
        if (data.containsKey("dataSource")) chart.setDataSource((String) data.get("dataSource"));
        if (data.containsKey("chartConfig")) chart.setChartConfig((String) data.get("chartConfig"));
        if (data.containsKey("description")) chart.setDescription((String) data.get("description"));
        if (data.containsKey("thumbnail")) chart.setThumbnail((String) data.get("thumbnail"));
        if (data.containsKey("status")) chart.setStatus((String) data.get("status"));
        chart.setUpdateBy((String) data.getOrDefault("updateBy", "admin"));
        chart.setUpdateTime(new Date());
        chartMapper.updateById(chart);
        return chartToMap(chart);
    }

    @Override
    public void deleteChart(String chartId) {
        BudgetAnalysisChart chart = chartMapper.selectById(chartId);
        if (chart != null) {
            chart.setDelFlag(1);
            chart.setUpdateTime(new Date());
            chartMapper.updateById(chart);
        }
    }

    @Override
    public Map<String, Object> getChartDetail(String chartId) {
        BudgetAnalysisChart chart = chartMapper.selectById(chartId);
        if (chart == null || chart.getDelFlag() == 1) {
            throw new RuntimeException("图表不存在");
        }
        // 增加使用次数
        chart.setUsageCount(chart.getUsageCount() != null ? chart.getUsageCount() + 1 : 1);
        chartMapper.updateById(chart);
        return chartToMap(chart);
    }

    @Override
    public Map<String, Object> getChartData(Map<String, Object> params) {
        Map<String, Object> chartData = new HashMap<>();
        try {
            Random random = new Random();
            List<String> xAxis = Arrays.asList("1月", "2月", "3月", "4月", "5月", "6月",
                    "7月", "8月", "9月", "10月", "11月", "12月");
            List<Double> budgetData = new ArrayList<>();
            List<Double> executionData = new ArrayList<>();
            for (int i = 0; i < 12; i++) {
                budgetData.add(Math.round(random.nextDouble() * 10000 * 100.0) / 100.0);
                executionData.add(Math.round(random.nextDouble() * 10000 * 100.0) / 100.0);
            }
            chartData.put("xAxis", xAxis);
            chartData.put("budgetData", budgetData);
            chartData.put("executionData", executionData);
        } catch (Exception e) {
            log.error("获取图表数据失败", e);
        }
        return chartData;
    }

    // ========== 工具方法 ==========

    private Map<String, Object> chartToMap(BudgetAnalysisChart c) {
        Map<String, Object> m = new HashMap<>();
        m.put("id", c.getChartId());
        m.put("chartId", c.getChartId());
        m.put("name", c.getChartName());
        m.put("chartName", c.getChartName());
        m.put("type", c.getChartType());
        m.put("chartType", c.getChartType());
        m.put("dataSource", c.getDataSource());
        m.put("chartConfig", c.getChartConfig());
        m.put("thumbnail", c.getThumbnail());
        m.put("description", c.getDescription());
        m.put("status", c.getStatus());
        m.put("usageCount", c.getUsageCount());
        m.put("creator", c.getCreator());
        m.put("createTime", c.getCreateTime());
        m.put("updateTime", c.getUpdateTime());
        return m;
    }

    private int getInt(Map<String, Object> map, String key) {
        return getInt(map, key, 0);
    }

    private int getInt(Map<String, Object> map, String key, int defaultVal) {
        Object val = map.get(key);
        if (val == null) return defaultVal;
        if (val instanceof Number) return ((Number) val).intValue();
        try { return Integer.parseInt(val.toString()); } catch (Exception e) { return defaultVal; }
    }
}
