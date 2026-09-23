package com.management.accountant.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.management.accountant.oracle.entity.budget.BudgetTrendAnalysis;
import com.management.accountant.oracle.mapper.budget.BudgetTrendAnalysisMapper;
import com.management.accountant.service.BudgetTrendAnalysisService;
import com.management.accountant.util.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * 预算趋势分析Service实现类（真实数据库版本）
 */
@Service
@Slf4j
public class BudgetTrendAnalysisServiceImpl implements BudgetTrendAnalysisService {

    @Resource
    private BudgetTrendAnalysisMapper trendMapper;

    /**
     * 执行趋势分析（分页列表查询）
     * 前端传参：analysisType/dateRange/organizationPath/budgetAccount/trendIndicator/pageNum/pageSize
     */
    @Override
    public Map<String, Object> executeTrendAnalysis(Map<String, Object> params) {
        int pageNum  = params.get("pageNum")  != null ? Integer.parseInt(params.get("pageNum").toString())  : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 20;

        Map<String, Object> queryParams = buildQueryParams(params);

        PageHelper.startPage(pageNum, pageSize);
        List<BudgetTrendAnalysis> list = trendMapper.selectByPage(queryParams);
        PageInfo<BudgetTrendAnalysis> pageInfo = new PageInfo<>(list);

        PageResult<BudgetTrendAnalysis> pageResult = new PageResult<>();
        pageResult.setTlist(list);
        pageResult.setTotalRecord((int) pageInfo.getTotal());
        pageResult.setPageNo(pageNum);
        pageResult.setPageSize(pageSize);
        pageResult.setTotalPage(pageInfo.getPages());

        Map<String, Object> result = new HashMap<>();
        result.put("tlist",       pageResult.getTlist());
        result.put("totalRecord", pageResult.getTotalRecord());
        result.put("pageNo",      pageResult.getPageNo());
        result.put("pageSize",    pageResult.getPageSize());
        result.put("totalPage",   pageResult.getTotalPage());
        // 兼容前端 response.data.list / response.data.total
        result.put("list",  pageResult.getTlist());
        result.put("total", pageResult.getTotalRecord());
        return result;
    }

    @Override
    public Map<String, Object> getTrendChart(Map<String, Object> params) {
        Map<String, Object> queryParams = buildQueryParams(params);
        List<Map<String, Object>> chartData = trendMapper.selectMainChartData(queryParams);

        List<String> xAxis = new ArrayList<>();
        List<Object> avgValues = new ArrayList<>();
        List<Object> maxValues = new ArrayList<>();
        List<Object> growthRates = new ArrayList<>();

        for (Map<String, Object> row : chartData) {
            xAxis.add(row.getOrDefault("PERIOD_LABEL", "").toString());
            avgValues.add(row.getOrDefault("AVG_VALUE", 0));
            maxValues.add(row.getOrDefault("MAX_VALUE", 0));
            growthRates.add(row.getOrDefault("GROWTH_RATE", 0));
        }

        Map<String, Object> result = new HashMap<>();
        result.put("xAxis", xAxis);
        List<Map<String, Object>> series = new ArrayList<>();
        series.add(buildSeries("平均值", avgValues));
        series.add(buildSeries("最大值", maxValues));
        series.add(buildSeries("增长率", growthRates));
        result.put("series", series);
        result.put("legend", Arrays.asList("平均值", "最大值", "增长率"));
        return result;
    }

    @Override
    public Map<String, Object> exportTrendReport(Map<String, Object> params) {
        Map<String, Object> queryParams = buildQueryParams(params);
        List<BudgetTrendAnalysis> list = trendMapper.selectForExport(queryParams);
        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", list.size());
        result.put("exportTime", new Date());
        return result;
    }

    /**
     * 统计卡片数据（真实数据库统计）
     */
    @Override
    public Map<String, Object> getTrendStats() {
        try {
            Map<String, Object> raw = trendMapper.selectStats();
            Map<String, Object> stats = new HashMap<>();
            if (raw != null) {
                stats.put("totalTrends",      toInt(raw.get("TOTAL_COUNT")));
                stats.put("completedTrends",  toInt(raw.get("COMPLETED_COUNT")));
                stats.put("averageGrowthRate", toDouble(raw.get("AVG_GROWTH_RATE")));
                stats.put("forecastAccuracy",  toDouble(raw.get("FORECAST_ACCURACY")));
            } else {
                stats.put("totalTrends", 0);
                stats.put("completedTrends", 0);
                stats.put("averageGrowthRate", 0);
                stats.put("forecastAccuracy", 0);
            }
            return stats;
        } catch (Exception e) {
            log.error("获取趋势统计数据失败", e);
            Map<String, Object> empty = new HashMap<>();
            empty.put("totalTrends", 0);
            empty.put("completedTrends", 0);
            empty.put("averageGrowthRate", 0);
            empty.put("forecastAccuracy", 0);
            return empty;
        }
    }

    /**
     * 4个图表数据（主图 + 增长率 + 波动率 + 预测）
     */
    @Override
    public Map<String, Object> getTrendChartData(Map<String, Object> params) {
        if (params == null) params = new HashMap<>();
        Map<String, Object> queryParams = buildQueryParams(params);

        // 主趋势图
        List<Map<String, Object>> mainRaw = trendMapper.selectMainChartData(queryParams);
        Map<String, Object> mainChart = buildMainChart(mainRaw);

        // 增长率图
        List<Map<String, Object>> growthRaw = trendMapper.selectGrowthChartData(queryParams);
        Map<String, Object> growthChart = buildSimpleLineChart(growthRaw, "PERIOD_LABEL", "GROWTH_RATE", "增长率");

        // 波动率图
        List<Map<String, Object>> volatilityRaw = trendMapper.selectVolatilityChartData(queryParams);
        Map<String, Object> volatilityChart = buildSimpleBarChart(volatilityRaw, "PERIOD_LABEL", "VOLATILITY_RATE", "波动率");

        // 预测图
        List<Map<String, Object>> forecastRaw = trendMapper.selectForecastChartData(queryParams);
        Map<String, Object> forecastChart = buildForecastChart(forecastRaw);

        Map<String, Object> result = new HashMap<>();
        result.put("mainChart",       mainChart);
        result.put("growthChart",     growthChart);
        result.put("volatilityChart", volatilityChart);
        result.put("forecastChart",   forecastChart);
        return result;
    }

    // ==================== 私有辅助方法 ====================

    private Map<String, Object> buildQueryParams(Map<String, Object> params) {
        Map<String, Object> q = new HashMap<>();
        // 组织：前端字段 organizationPath
        Object org = params.get("organizationPath");
        if (org == null) org = params.get("organizationId");
        if (org != null && !org.toString().isEmpty()) q.put("organizationId", org.toString());
        // 预算科目：前端字段 budgetAccount
        Object acct = params.get("budgetAccount");
        if (acct == null) acct = params.get("accountId");
        if (acct != null && !acct.toString().isEmpty()) q.put("accountId", acct.toString());
        // 分析类型：前端字段 analysisType → trendType
        Object aType = params.get("analysisType");
        if (aType == null) aType = params.get("trendType");
        if (aType != null && !aType.toString().isEmpty()) q.put("trendType", aType.toString());
        // 时间范围：前端字段 dateRange
        Object dr = params.get("dateRange");
        if (dr instanceof List) {
            List<?> range = (List<?>) dr;
            if (range.size() >= 2) {
                if (range.get(0) != null && !range.get(0).toString().isEmpty())
                    q.put("startDate", range.get(0).toString().substring(0, 10));
                if (range.get(1) != null && !range.get(1).toString().isEmpty())
                    q.put("endDate", range.get(1).toString().substring(0, 10));
            }
        }
        return q;
    }

    private Map<String, Object> buildMainChart(List<Map<String, Object>> raw) {
        List<String> xAxis = new ArrayList<>();
        List<Object> avgValues = new ArrayList<>(), maxValues = new ArrayList<>(), growthRates = new ArrayList<>();
        for (Map<String, Object> row : raw) {
            xAxis.add(str(row.get("PERIOD_LABEL")));
            avgValues.add(row.getOrDefault("AVG_VALUE", 0));
            maxValues.add(row.getOrDefault("MAX_VALUE", 0));
            growthRates.add(row.getOrDefault("GROWTH_RATE", 0));
        }
        Map<String, Object> chart = new HashMap<>();
        chart.put("xAxis", xAxis);
        List<Map<String, Object>> series = new ArrayList<>();
        series.add(buildSeries("平均值", avgValues));
        series.add(buildSeries("最大值", maxValues));
        series.add(buildSeries("增长率", growthRates));
        chart.put("series", series);
        return chart;
    }

    private Map<String, Object> buildSimpleLineChart(List<Map<String, Object>> raw,
                                                      String labelKey, String valueKey, String seriesName) {
        List<String> xAxis = new ArrayList<>();
        List<Object> data = new ArrayList<>();
        for (Map<String, Object> row : raw) {
            xAxis.add(str(row.get(labelKey)));
            data.add(row.getOrDefault(valueKey, 0));
        }
        Map<String, Object> chart = new HashMap<>();
        chart.put("xAxis", xAxis);
        chart.put("series", Collections.singletonList(buildSeries(seriesName, data)));
        return chart;
    }

    private Map<String, Object> buildSimpleBarChart(List<Map<String, Object>> raw,
                                                     String labelKey, String valueKey, String seriesName) {
        return buildSimpleLineChart(raw, labelKey, valueKey, seriesName);
    }

    private Map<String, Object> buildForecastChart(List<Map<String, Object>> raw) {
        List<String> xAxis = new ArrayList<>();
        List<Object> history = new ArrayList<>();
        for (Map<String, Object> row : raw) {
            xAxis.add(str(row.get("PERIOD_LABEL")));
            history.add(row.getOrDefault("HISTORY_VALUE", 0));
        }
        // 简单线性预测：取最后3期均值作为下期预测
        List<Object> forecast = new ArrayList<>(Collections.nCopies(history.size(), null));
        if (!history.isEmpty()) {
            double last = toDouble(history.get(history.size() - 1));
            forecast.set(history.size() - 1, last);
        }
        Map<String, Object> chart = new HashMap<>();
        chart.put("xAxis", xAxis);
        List<Map<String, Object>> series = new ArrayList<>();
        series.add(buildSeries("历史值", history));
        series.add(buildSeries("预测值", forecast));
        chart.put("series", series);
        return chart;
    }

    private Map<String, Object> buildSeries(String name, List<Object> data) {
        Map<String, Object> s = new HashMap<>();
        s.put("name", name);
        s.put("data", data);
        return s;
    }

    private int toInt(Object val) {
        if (val == null) return 0;
        try { return Integer.parseInt(val.toString()); } catch (Exception e) { return 0; }
    }

    private double toDouble(Object val) {
        if (val == null) return 0.0;
        try { return Double.parseDouble(val.toString()); } catch (Exception e) { return 0.0; }
    }

    private String str(Object val) {
        return val == null ? "" : val.toString();
    }
}
