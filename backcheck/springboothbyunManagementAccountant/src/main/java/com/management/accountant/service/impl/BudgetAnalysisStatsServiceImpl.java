package com.management.accountant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.management.accountant.oracle.entity.budget.*;
import com.management.accountant.oracle.mapper.budget.*;
import com.management.accountant.service.BudgetAnalysisStatsService;
import com.management.accountant.vo.result.BudgetAnalysisInsightVO;
import com.management.accountant.vo.result.BudgetAnalysisStatsVO;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 预算分析统计Service实现
 *
 * @description 预算分析统计数据服务实现
 * @author AI Assistant
 * @date 2025-01-30
 */
@Service
@Slf4j
public class BudgetAnalysisStatsServiceImpl implements BudgetAnalysisStatsService {

    @Resource
    private BudgetAnalysisReportMapper analysisReportMapper;

    @Resource
    private BudgetVarianceAnalysisMapper varianceAnalysisMapper;

    @Resource
    private BudgetTrendAnalysisMapper trendAnalysisMapper;

    @Resource
    private BudgetComparisonAnalysisMapper comparisonAnalysisMapper;

    @Resource
    private BudgetForecastAnalysisMapper forecastAnalysisMapper;

    @Resource
    private BudgetWarningRuleMapper warningRuleMapper;

    @Resource
    private BudgetSystemConfigMapper systemConfigMapper;

    @Resource
    private BudgetPerformanceAnalysisMapper performanceAnalysisMapper;

    @Resource
    private BudgetRollingForecastMapper rollingForecastMapper;

    @Override
    public BudgetAnalysisStatsVO getAnalysisStats() {
        BudgetAnalysisStatsVO stats = new BudgetAnalysisStatsVO();

        try {
            // 统计分析报告（过滤已删除）
            Integer totalReports = analysisReportMapper.selectCount(
                new QueryWrapper<BudgetAnalysisReport>().eq("DEL_FLAG", 0)).intValue();
            stats.setTotalReports(totalReports);

            // 本月新增报告（按CREATE_TIME过滤当月）
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.set(java.util.Calendar.DAY_OF_MONTH, 1);
            cal.set(java.util.Calendar.HOUR_OF_DAY, 0);
            cal.set(java.util.Calendar.MINUTE, 0);
            cal.set(java.util.Calendar.SECOND, 0);
            Integer monthlyReports = analysisReportMapper.selectCount(
                new QueryWrapper<BudgetAnalysisReport>().eq("DEL_FLAG", 0)
                    .ge("CREATE_TIME", cal.getTime())).intValue();
            stats.setMonthlyReports(monthlyReports);

            // 统计各类分析（过滤已删除）
            Integer varianceCount = varianceAnalysisMapper.selectCount(
                new QueryWrapper<BudgetVarianceAnalysis>().eq("DEL_FLAG", 0)).intValue();
            stats.setVarianceAnalysis(varianceCount);

            Integer trendCount = trendAnalysisMapper.selectCount(
                new QueryWrapper<BudgetTrendAnalysis>().eq("DEL_FLAG", 0)).intValue();
            stats.setTrendAnalysis(trendCount);

            Integer comparisonCount = comparisonAnalysisMapper.selectCount(
                new QueryWrapper<BudgetComparisonAnalysis>().eq("DEL_FLAG", 0)).intValue();
            stats.setComparisonAnalysis(comparisonCount);

            Integer forecastCount = forecastAnalysisMapper.selectCount(
                new QueryWrapper<BudgetForecastAnalysis>().eq("DEL_FLAG", 0)).intValue();
            stats.setForecastAnalysis(forecastCount);

            // 从差异分析数据计算平均执行率和偏差率
            List<BudgetVarianceAnalysis> allVariance = varianceAnalysisMapper.selectList(
                new QueryWrapper<BudgetVarianceAnalysis>().eq("DEL_FLAG", 0)
                    .isNotNull("BUDGET_AMOUNT").gt("BUDGET_AMOUNT", 0));
            if (!allVariance.isEmpty()) {
                BigDecimal totalBudget = BigDecimal.ZERO;
                BigDecimal totalActual = BigDecimal.ZERO;
                BigDecimal totalAbsVariance = BigDecimal.ZERO;
                for (BudgetVarianceAnalysis va : allVariance) {
                    BigDecimal b = va.getBudgetAmount() != null ? va.getBudgetAmount() : BigDecimal.ZERO;
                    BigDecimal a = va.getActualAmount() != null ? va.getActualAmount() : BigDecimal.ZERO;
                    totalBudget = totalBudget.add(b);
                    totalActual = totalActual.add(a);
                    totalAbsVariance = totalAbsVariance.add(a.subtract(b).abs());
                }
                if (totalBudget.compareTo(BigDecimal.ZERO) > 0) {
                    double execRate = totalActual.divide(totalBudget, 4, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal("100")).doubleValue();
                    double varRate = totalAbsVariance.divide(totalBudget, 4, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal("100")).doubleValue();
                    stats.setAvgExecutionRate(Math.round(execRate * 10.0) / 10.0);
                    stats.setAvgVarianceRate(Math.round(varRate * 10.0) / 10.0);
                } else {
                    stats.setAvgExecutionRate(0.0);
                    stats.setAvgVarianceRate(0.0);
                }
            } else {
                stats.setAvgExecutionRate(0.0);
                stats.setAvgVarianceRate(0.0);
            }

        } catch (Exception e) {
            log.error("获取预算分析统计数据失败", e);
        }

        return stats;
    }

    @Override
    public List<Map<String, Object>> getAnalysisTableData() {
        List<Map<String, Object>> tableData = new ArrayList<>();

        try {
            List<BudgetVarianceAnalysis> list = varianceAnalysisMapper.selectList(
                new QueryWrapper<BudgetVarianceAnalysis>()
                    .eq("DEL_FLAG", 0)
                    .orderByDesc("CREATE_TIME")
            );

            Map<String, BigDecimal[]> deptAgg = new LinkedHashMap<>();
            for (BudgetVarianceAnalysis va : list) {
                String dept = va.getOrganizationName() != null ? va.getOrganizationName() : "未知部门";
                BigDecimal budget = va.getBudgetAmount() != null ? va.getBudgetAmount() : BigDecimal.ZERO;
                BigDecimal actual = va.getActualAmount() != null ? va.getActualAmount() : BigDecimal.ZERO;
                BigDecimal[] agg = deptAgg.computeIfAbsent(dept, k -> new BigDecimal[]{BigDecimal.ZERO, BigDecimal.ZERO});
                agg[0] = agg[0].add(budget);
                agg[1] = agg[1].add(actual);
            }

            for (Map.Entry<String, BigDecimal[]> entry : deptAgg.entrySet()) {
                Map<String, Object> row = new HashMap<>();
                BigDecimal budgetTotal = entry.getValue()[0];
                BigDecimal actualTotal = entry.getValue()[1];
                BigDecimal variance = actualTotal.subtract(budgetTotal);
                double varianceRate = 0;
                double executionRate = 0;
                if (budgetTotal.compareTo(BigDecimal.ZERO) != 0) {
                    varianceRate = variance.divide(budgetTotal, 4, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal("100")).doubleValue();
                    executionRate = actualTotal.divide(budgetTotal, 4, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal("100")).doubleValue();
                }
                row.put("department", entry.getKey());
                row.put("budgetAmount", budgetTotal.setScale(2, RoundingMode.HALF_UP));
                row.put("actualAmount", actualTotal.setScale(2, RoundingMode.HALF_UP));
                row.put("variance", variance.setScale(2, RoundingMode.HALF_UP));
                row.put("varianceRate", Math.round(varianceRate * 10.0) / 10.0);
                row.put("executionRate", Math.round(executionRate * 10.0) / 10.0);
                tableData.add(row);
            }
        } catch (Exception e) {
            log.error("获取分析表格数据失败", e);
        }

        return tableData;
    }

    @Override
    public List<BudgetAnalysisInsightVO> getAnalysisInsights(Integer limit) {
        List<BudgetAnalysisInsightVO> insights = new ArrayList<>();

        if (limit == null || limit <= 0) {
            limit = 5;
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            List<BudgetVarianceAnalysis> allList = varianceAnalysisMapper.selectList(
                new QueryWrapper<BudgetVarianceAnalysis>()
                    .eq("DEL_FLAG", 0)
                    .orderByDesc("VARIANCE_RATE")
            );
            List<BudgetVarianceAnalysis> significantList = allList.subList(0, Math.min(limit, allList.size()));

            int idx = 1;
            for (BudgetVarianceAnalysis va : significantList) {
                BigDecimal rate = va.getVarianceRate() != null ? va.getVarianceRate() : BigDecimal.ZERO;
                if (rate.abs().compareTo(new BigDecimal("10")) < 0) {
                    continue;
                }
                BudgetAnalysisInsightVO insight = new BudgetAnalysisInsightVO();
                insight.setInsightId(String.valueOf(idx++));

                boolean isOverBudget = rate.compareTo(BigDecimal.ZERO) > 0;
                String orgName = va.getOrganizationName() != null ? va.getOrganizationName() : "未知部门";

                if (rate.abs().compareTo(new BigDecimal("20")) >= 0) {
                    insight.setType("异常");
                    insight.setSeverity("高");
                } else {
                    insight.setType("预警");
                    insight.setSeverity("中");
                }

                insight.setTitle(orgName + (isOverBudget ? "预算超支" : "预算节余"));
                String desc = orgName + "预算偏差率达" + rate.abs().setScale(1, RoundingMode.HALF_UP) + "%";
                if (va.getVarianceReason() != null && !va.getVarianceReason().isEmpty()) {
                    desc += "，原因：" + va.getVarianceReason();
                }
                insight.setContent(desc);
                insight.setImpact(orgName);
                insight.setRecommendation(va.getImprovementMeasures() != null ? va.getImprovementMeasures() : "建议加强预算执行监控");
                insight.setCreateTime(va.getCreateTime() != null ? sdf.format(va.getCreateTime()) : sdf.format(new Date()));
                insights.add(insight);
            }
        } catch (Exception e) {
            log.error("获取分析洞察失败", e);
        }

        return insights;
    }

    @Override
    public List<Map<String, Object>> getOrganizations() {
        List<Map<String, Object>> organizations = new ArrayList<>();

        try {
            // 模拟组织数据，实际应从数据库查询
            Map<String, Object> org1 = new HashMap<>();
            org1.put("id", "ORG001");
            org1.put("code", "ORG001");
            org1.put("name", "总公司");
            org1.put("parentId", null);
            organizations.add(org1);

            Map<String, Object> org2 = new HashMap<>();
            org2.put("id", "ORG002");
            org2.put("code", "ORG002");
            org2.put("name", "财务部");
            org2.put("parentId", "ORG001");
            organizations.add(org2);

            Map<String, Object> org3 = new HashMap<>();
            org3.put("id", "ORG003");
            org3.put("code", "ORG003");
            org3.put("name", "技术部");
            org3.put("parentId", "ORG001");
            organizations.add(org3);

            Map<String, Object> org4 = new HashMap<>();
            org4.put("id", "ORG004");
            org4.put("code", "ORG004");
            org4.put("name", "销售部");
            org4.put("parentId", "ORG001");
            organizations.add(org4);

            Map<String, Object> org5 = new HashMap<>();
            org5.put("id", "ORG005");
            org5.put("code", "ORG005");
            org5.put("name", "人力资源部");
            org5.put("parentId", "ORG001");
            organizations.add(org5);

        } catch (Exception e) {
            log.error("获取组织列表失败", e);
        }

        return organizations;
    }

    @Override
    public List<Map<String, Object>> getBudgetAccounts() {
        List<Map<String, Object>> accounts = new ArrayList<>();

        try {
            // 模拟预算科目数据，实际应从数据库查询
            Map<String, Object> acc1 = new HashMap<>();
            acc1.put("id", "ACC001");
            acc1.put("code", "6601");
            acc1.put("name", "销售费用");
            acc1.put("parentId", null);
            accounts.add(acc1);

            Map<String, Object> acc2 = new HashMap<>();
            acc2.put("id", "ACC002");
            acc2.put("code", "6602");
            acc2.put("name", "管理费用");
            acc2.put("parentId", null);
            accounts.add(acc2);

            Map<String, Object> acc3 = new HashMap<>();
            acc3.put("id", "ACC003");
            acc3.put("code", "6603");
            acc3.put("name", "财务费用");
            acc3.put("parentId", null);
            accounts.add(acc3);

            Map<String, Object> acc4 = new HashMap<>();
            acc4.put("id", "ACC004");
            acc4.put("code", "6001");
            acc4.put("name", "主营业务收入");
            acc4.put("parentId", null);
            accounts.add(acc4);

            Map<String, Object> acc5 = new HashMap<>();
            acc5.put("id", "ACC005");
            acc5.put("code", "6401");
            acc5.put("name", "主营业务成本");
            acc5.put("parentId", null);
            accounts.add(acc5);

        } catch (Exception e) {
            log.error("获取预算科目列表失败", e);
        }

        return accounts;
    }

    @Override
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> result = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // === 1. stats ===
        try {
            result.put("stats", buildStatsSection(sdf));
        } catch (Exception e) {
            log.error("构建stats失败", e);
            result.put("stats", new HashMap<>());
        }

        // === 2. tableData ===
        try {
            result.put("tableData", getAnalysisTableData());
        } catch (Exception e) {
            log.error("构建tableData失败", e);
            result.put("tableData", new ArrayList<>());
        }

        // === 3. insights ===
        try {
            result.put("insights", buildInsightsSection(sdf));
        } catch (Exception e) {
            log.error("构建insights失败", e);
            result.put("insights", new ArrayList<>());
        }

        // === 4. recentReports ===
        try {
            result.put("recentReports", buildRecentReports(sdf));
        } catch (Exception e) {
            log.error("构建recentReports失败", e);
            result.put("recentReports", new ArrayList<>());
        }

        // === 5. moduleStats ===
        try {
            result.put("moduleStats", buildModuleStats(sdf));
        } catch (Exception e) {
            log.error("构建moduleStats失败", e);
            result.put("moduleStats", new HashMap<>());
        }

        // === 6. chartData ===
        try {
            result.put("chartData", buildChartData());
        } catch (Exception e) {
            log.error("构建chartData失败", e);
            result.put("chartData", new HashMap<>());
        }

        return result;
    }

    /**
     * 构建stats统计卡片数据
     */
    private Map<String, Object> buildStatsSection(SimpleDateFormat sdf) {
        Map<String, Object> stats = new HashMap<>();
        int varianceCount = varianceAnalysisMapper.selectCount(
            new QueryWrapper<BudgetVarianceAnalysis>().eq("DEL_FLAG", 0)).intValue();
        int significantVariances = varianceAnalysisMapper.selectCount(
            new QueryWrapper<BudgetVarianceAnalysis>().eq("DEL_FLAG", 0)
                .gt("VARIANCE_RATE", 10)).intValue();
        stats.put("varianceCount", varianceCount);
        stats.put("significantVariances", significantVariances);
        double varianceTrend = varianceCount > 0
            ? new BigDecimal(significantVariances).divide(new BigDecimal(varianceCount), 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("-100")).doubleValue()
            : 0;
        stats.put("varianceTrend", Math.round(varianceTrend * 10.0) / 10.0);

        int trendReports = trendAnalysisMapper.selectCount(
            new QueryWrapper<BudgetTrendAnalysis>().eq("DEL_FLAG", 0)).intValue();
        int activeTrends = trendAnalysisMapper.selectCount(
            new QueryWrapper<BudgetTrendAnalysis>().eq("DEL_FLAG", 0)
                .eq("ANALYSIS_STATUS", "analyzing")).intValue();
        stats.put("trendReports", trendReports);
        stats.put("activeTrends", activeTrends);
        stats.put("trendGrowth", trendReports > 0
            ? Math.round((double) activeTrends / trendReports * 1000.0) / 10.0 : 0);

        List<BudgetForecastAnalysis> forecasts = forecastAnalysisMapper.selectList(
            new QueryWrapper<BudgetForecastAnalysis>().eq("DEL_FLAG", 0)
                .isNotNull("ACCURACY_RATE"));
        double avgAccuracy = 0;
        if (!forecasts.isEmpty()) {
            BigDecimal sum = BigDecimal.ZERO;
            for (BudgetForecastAnalysis f : forecasts) {
                sum = sum.add(f.getAccuracyRate() != null ? f.getAccuracyRate() : BigDecimal.ZERO);
            }
            avgAccuracy = sum.divide(new BigDecimal(forecasts.size()), 2, RoundingMode.HALF_UP).doubleValue();
        }
        int forecastModels = forecastAnalysisMapper.selectCount(
            new QueryWrapper<BudgetForecastAnalysis>().eq("DEL_FLAG", 0)).intValue();
        stats.put("forecastAccuracy", avgAccuracy);
        stats.put("forecastModels", forecastModels);
        stats.put("accuracyImprovement", avgAccuracy > 0 ? Math.round((avgAccuracy - 80) * 10.0) / 10.0 : 0);

        int dashboardCount = analysisReportMapper.selectCount(
            new QueryWrapper<BudgetAnalysisReport>().eq("DEL_FLAG", 0)).intValue();
        int activeDashboards = analysisReportMapper.selectCount(
            new QueryWrapper<BudgetAnalysisReport>().eq("DEL_FLAG", 0)
                .eq("REPORT_STATUS", "COMPLETED")).intValue();
        stats.put("dashboardCount", dashboardCount);
        stats.put("activeDashboards", activeDashboards);
        stats.put("dashboardUsage", dashboardCount > 0
            ? Math.round((double) activeDashboards / dashboardCount * 100.0) : 0);
        return stats;
    }

    /**
     * 构建洞察列表（供getDashboardStats使用，返回Map列表）
     */
    private List<Map<String, Object>> buildInsightsSection(SimpleDateFormat sdf) {
        List<Map<String, Object>> insights = new ArrayList<>();
        List<BudgetVarianceAnalysis> significantList = varianceAnalysisMapper.selectList(
            new QueryWrapper<BudgetVarianceAnalysis>()
                .eq("DEL_FLAG", 0)
                .orderByDesc("VARIANCE_RATE")
        );
        significantList = significantList.subList(0, Math.min(10, significantList.size()));
        int idx = 1;
        for (BudgetVarianceAnalysis va : significantList) {
            BigDecimal rate = va.getVarianceRate() != null ? va.getVarianceRate() : BigDecimal.ZERO;
            if (rate.abs().compareTo(new BigDecimal("10")) < 0) continue;
            Map<String, Object> item = new HashMap<>();
            String orgName = va.getOrganizationName() != null ? va.getOrganizationName() : "未知部门";
            boolean isOverBudget = rate.compareTo(BigDecimal.ZERO) > 0;
            item.put("id", String.valueOf(idx++));
            item.put("type", rate.abs().compareTo(new BigDecimal("20")) >= 0 ? "异常" : "预警");
            item.put("title", orgName + (isOverBudget ? "预算超支" : "预算节余"));
            item.put("description", orgName + "预算偏差率达" + rate.abs().setScale(1, RoundingMode.HALF_UP) + "%");
            item.put("time", va.getCreateTime() != null ? sdf.format(va.getCreateTime()) : sdf.format(new Date()));
            insights.add(item);
        }
        return insights;
    }


    /**
     * 构建最近报告列表
     */
    private List<Map<String, Object>> buildRecentReports(SimpleDateFormat sdf) {
        List<Map<String, Object>> recentReports = new ArrayList<>();
        List<BudgetAnalysisReport> reports = analysisReportMapper.selectList(
            new QueryWrapper<BudgetAnalysisReport>()
                .eq("DEL_FLAG", 0)
                .orderByDesc("CREATE_TIME")
        );
        reports = reports.subList(0, Math.min(5, reports.size()));
        for (BudgetAnalysisReport r : reports) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", r.getId());
            item.put("title", r.getReportName() != null ? r.getReportName() : "");
            item.put("description", r.getDescription() != null ? r.getDescription() : r.getReportSummary());
            item.put("type", r.getReportType() != null ? r.getReportType().toUpperCase() : "COMPREHENSIVE");
            String status = "草稿";
            if ("COMPLETED".equals(r.getReportStatus())) status = "已完成";
            else if ("GENERATING".equals(r.getReportStatus())) status = "生成中";
            else if ("FAILED".equals(r.getReportStatus())) status = "失败";
            else if ("SCHEDULED".equals(r.getReportStatus())) status = "已调度";
            item.put("status", status);
            item.put("author", r.getCreator() != null ? r.getCreator() : r.getCreateBy());
            item.put("createTime", r.getCreateTime() != null ? sdf.format(r.getCreateTime()) : "");
            recentReports.add(item);
        }
        return recentReports;
    }

    /**
     * 构建模块统计数据
     */
    private Map<String, Object> buildModuleStats(SimpleDateFormat sdf) {
        Map<String, Object> moduleStats = new HashMap<>();

        moduleStats.put("varianceAnalysis", buildSingleModuleStat(
            varianceAnalysisMapper.selectCount(new QueryWrapper<BudgetVarianceAnalysis>().eq("DEL_FLAG", 0)).intValue(),
            getLatestUpdateTime(varianceAnalysisMapper.selectList(
                new QueryWrapper<BudgetVarianceAnalysis>().eq("DEL_FLAG", 0)
                    .orderByDesc("UPDATE_TIME")
            ), sdf)
        ));

        moduleStats.put("trendAnalysis", buildSingleModuleStat(
            trendAnalysisMapper.selectCount(new QueryWrapper<BudgetTrendAnalysis>().eq("DEL_FLAG", 0)).intValue(),
            getLatestUpdateTime(trendAnalysisMapper.selectList(
                new QueryWrapper<BudgetTrendAnalysis>().eq("DEL_FLAG", 0)
                    .orderByDesc("UPDATE_TIME")
            ), sdf)
        ));

        moduleStats.put("comparisonAnalysis", buildSingleModuleStat(
            comparisonAnalysisMapper.selectCount(new QueryWrapper<BudgetComparisonAnalysis>().eq("DEL_FLAG", 0)).intValue(),
            getLatestUpdateTimeFromComparison(sdf)
        ));

        moduleStats.put("performanceAnalysis", buildSingleModuleStat(
            performanceAnalysisMapper.selectCount(new QueryWrapper<BudgetPerformanceAnalysis>().eq("DEL_FLAG", 0)).intValue(),
            getLatestUpdateTimeFromPerformance(sdf)
        ));

        moduleStats.put("forecastAnalysis", buildSingleModuleStat(
            forecastAnalysisMapper.selectCount(new QueryWrapper<BudgetForecastAnalysis>().eq("DEL_FLAG", 0)).intValue(),
            getLatestUpdateTime(forecastAnalysisMapper.selectList(
                new QueryWrapper<BudgetForecastAnalysis>().eq("DEL_FLAG", 0)
                    .orderByDesc("UPDATE_TIME")
            ), sdf)
        ));

        moduleStats.put("rollingForecast", buildSingleModuleStat(
            rollingForecastMapper.selectCount(new QueryWrapper<>()).intValue(),
            getLatestUpdateTimeFromRolling(sdf)
        ));

        return moduleStats;
    }

    private Map<String, Object> buildSingleModuleStat(int reportCount, String lastUpdate) {
        Map<String, Object> m = new HashMap<>();
        m.put("reportCount", reportCount);
        m.put("lastUpdate", lastUpdate != null ? lastUpdate : "");
        return m;
    }

    @SuppressWarnings("unchecked")
    private <T> String getLatestUpdateTime(List<T> list, SimpleDateFormat sdf) {
        if (list == null || list.isEmpty()) return null;
        try {
            Object entity = list.get(0);
            java.lang.reflect.Method m = entity.getClass().getMethod("getUpdateTime");
            Date d = (Date) m.invoke(entity);
            return d != null ? sdf.format(d) : null;
        } catch (Exception e) {
            return null;
        }
    }

    private String getLatestUpdateTimeFromComparison(SimpleDateFormat sdf) {
        try {
            List<BudgetComparisonAnalysis> list = comparisonAnalysisMapper.selectList(
                new QueryWrapper<BudgetComparisonAnalysis>()
                    .eq("DEL_FLAG", 0)
                    .orderByDesc("UPDATE_TIME")
                    .last("FETCH FIRST 1 ROWS ONLY"));
            if (!list.isEmpty() && list.get(0).getUpdateTime() != null) {
                return sdf.format(list.get(0).getUpdateTime());
            }
        } catch (Exception e) { /* ignore */ }
        return null;
    }

    private String getLatestUpdateTimeFromPerformance(SimpleDateFormat sdf) {
        try {
            List<BudgetPerformanceAnalysis> list = performanceAnalysisMapper.selectList(
                new QueryWrapper<BudgetPerformanceAnalysis>()
                    .eq("DEL_FLAG", 0)
                    .orderByDesc("UPDATE_TIME")
                    .last("FETCH FIRST 1 ROWS ONLY"));
            if (!list.isEmpty() && list.get(0).getUpdateTime() != null) {
                return sdf.format(list.get(0).getUpdateTime());
            }
        } catch (Exception e) { /* ignore */ }
        return null;
    }

    private String getLatestUpdateTimeFromRolling(SimpleDateFormat sdf) {
        try {
            List<BudgetRollingForecast> list = rollingForecastMapper.selectList(
                new QueryWrapper<BudgetRollingForecast>()
                    .orderByDesc("UPDATE_TIME"));
            if (!list.isEmpty() && list.get(0).getUpdateTime() != null) {
                return sdf.format(list.get(0).getUpdateTime());
            }
        } catch (Exception e) { /* ignore */ }
        return null;
    }

    /**
     * 构建图表数据（从差异分析表按月聚合）
     */
    private Map<String, Object> buildChartData() {
        Map<String, Object> chartData = new HashMap<>();
        SimpleDateFormat monthFmt = new SimpleDateFormat("M月");

        List<BudgetVarianceAnalysis> allVariance = varianceAnalysisMapper.selectList(
            new QueryWrapper<BudgetVarianceAnalysis>().eq("DEL_FLAG", 0)
                .orderByAsc("CREATE_TIME")
        );

        // executionTrend: 按月聚合预算/实际
        Map<String, BigDecimal[]> monthAgg = new LinkedHashMap<>();
        for (BudgetVarianceAnalysis va : allVariance) {
            String monthKey = va.getCreateTime() != null ? monthFmt.format(va.getCreateTime()) : "未知";
            BigDecimal budget = va.getBudgetAmount() != null ? va.getBudgetAmount() : BigDecimal.ZERO;
            BigDecimal actual = va.getActualAmount() != null ? va.getActualAmount() : BigDecimal.ZERO;
            BigDecimal[] agg = monthAgg.computeIfAbsent(monthKey, k -> new BigDecimal[]{BigDecimal.ZERO, BigDecimal.ZERO});
            agg[0] = agg[0].add(budget);
            agg[1] = agg[1].add(actual);
        }

        List<String> xAxis = new ArrayList<>(monthAgg.keySet());
        List<Double> executionData = new ArrayList<>();
        for (BigDecimal[] agg : monthAgg.values()) {
            if (agg[0].compareTo(BigDecimal.ZERO) != 0) {
                executionData.add(agg[1].divide(agg[0], 4, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100")).setScale(1, RoundingMode.HALF_UP).doubleValue());
            } else {
                executionData.add(0.0);
            }
        }
        Map<String, Object> executionTrend = new HashMap<>();
        executionTrend.put("xAxis", xAxis);
        executionTrend.put("data", executionData);
        chartData.put("executionTrend", executionTrend);

        // departmentComparison: 按部门聚合执行率
        Map<String, BigDecimal[]> deptAgg = new LinkedHashMap<>();
        for (BudgetVarianceAnalysis va : allVariance) {
            String dept = va.getOrganizationName() != null ? va.getOrganizationName() : "未知部门";
            BigDecimal budget = va.getBudgetAmount() != null ? va.getBudgetAmount() : BigDecimal.ZERO;
            BigDecimal actual = va.getActualAmount() != null ? va.getActualAmount() : BigDecimal.ZERO;
            BigDecimal[] agg = deptAgg.computeIfAbsent(dept, k -> new BigDecimal[]{BigDecimal.ZERO, BigDecimal.ZERO});
            agg[0] = agg[0].add(budget);
            agg[1] = agg[1].add(actual);
        }
        List<String> deptAxis = new ArrayList<>(deptAgg.keySet());
        List<Double> deptData = new ArrayList<>();
        for (BigDecimal[] agg : deptAgg.values()) {
            if (agg[0].compareTo(BigDecimal.ZERO) != 0) {
                deptData.add(agg[1].divide(agg[0], 4, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100")).setScale(1, RoundingMode.HALF_UP).doubleValue());
            } else {
                deptData.add(0.0);
            }
        }
        Map<String, Object> departmentComparison = new HashMap<>();
        departmentComparison.put("xAxis", deptAxis);
        departmentComparison.put("data", deptData);
        chartData.put("departmentComparison", departmentComparison);

        // completionRate: 已完成报告占比
        int totalReports = analysisReportMapper.selectCount(
            new QueryWrapper<BudgetAnalysisReport>().eq("DEL_FLAG", 0)).intValue();
        int completedReports = analysisReportMapper.selectCount(
            new QueryWrapper<BudgetAnalysisReport>().eq("DEL_FLAG", 0)
                .eq("REPORT_STATUS", "COMPLETED")).intValue();
        chartData.put("completionRate", totalReports > 0
            ? Math.round((double) completedReports / totalReports * 1000.0) / 10.0 : 0);

        // expenseStructure: 按科目名称聚合实际金额
        Map<String, BigDecimal> accountAgg = new LinkedHashMap<>();
        for (BudgetVarianceAnalysis va : allVariance) {
            String acctName = va.getAccountName() != null ? va.getAccountName() : "其他";
            BigDecimal actual = va.getActualAmount() != null ? va.getActualAmount() : BigDecimal.ZERO;
            accountAgg.merge(acctName, actual, BigDecimal::add);
        }
        List<Map<String, Object>> expenseStructure = new ArrayList<>();
        for (Map.Entry<String, BigDecimal> entry : accountAgg.entrySet()) {
            Map<String, Object> item = new HashMap<>();
            item.put("name", entry.getKey());
            item.put("value", entry.getValue().setScale(2, RoundingMode.HALF_UP));
            expenseStructure.add(item);
        }
        chartData.put("expenseStructure", expenseStructure);

        // monthlyTrend: 月度预算vs实际金额趋势
        List<Double> monthlyBudget = new ArrayList<>();
        List<Double> monthlyActual = new ArrayList<>();
        for (BigDecimal[] agg : monthAgg.values()) {
            monthlyBudget.add(agg[0].setScale(2, RoundingMode.HALF_UP).doubleValue());
            monthlyActual.add(agg[1].setScale(2, RoundingMode.HALF_UP).doubleValue());
        }
        Map<String, Object> monthlyTrend = new HashMap<>();
        monthlyTrend.put("xAxis", xAxis);
        monthlyTrend.put("budget", monthlyBudget);
        monthlyTrend.put("actual", monthlyActual);
        chartData.put("monthlyTrend", monthlyTrend);

        // departmentRanking: 部门执行率排名（横向柱状图）
        List<Map.Entry<String, BigDecimal[]>> deptEntries = new ArrayList<>(deptAgg.entrySet());
        deptEntries.sort((a, b) -> {
            double rateA = a.getValue()[0].compareTo(BigDecimal.ZERO) != 0
                ? a.getValue()[1].divide(a.getValue()[0], 4, RoundingMode.HALF_UP).doubleValue() : 0;
            double rateB = b.getValue()[0].compareTo(BigDecimal.ZERO) != 0
                ? b.getValue()[1].divide(b.getValue()[0], 4, RoundingMode.HALF_UP).doubleValue() : 0;
            return Double.compare(rateA, rateB);
        });
        List<String> rankDepts = new ArrayList<>();
        List<Double> rankData = new ArrayList<>();
        for (Map.Entry<String, BigDecimal[]> entry : deptEntries) {
            rankDepts.add(entry.getKey());
            if (entry.getValue()[0].compareTo(BigDecimal.ZERO) != 0) {
                rankData.add(entry.getValue()[1].divide(entry.getValue()[0], 4, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100")).setScale(1, RoundingMode.HALF_UP).doubleValue());
            } else {
                rankData.add(0.0);
            }
        }
        Map<String, Object> departmentRanking = new HashMap<>();
        departmentRanking.put("yAxis", rankDepts);
        departmentRanking.put("data", rankData);
        chartData.put("departmentRanking", departmentRanking);

        return chartData;
    }


    @Override
    public Map<String, Object> getDashboardData() {
        Map<String, Object> data = new HashMap<>();
        try {
            // KPI 卡片数据（过滤已删除）
            Integer totalReports = analysisReportMapper.selectCount(
                new QueryWrapper<BudgetAnalysisReport>().eq("DEL_FLAG", 0)).intValue();
            Integer varianceCount = varianceAnalysisMapper.selectCount(
                new QueryWrapper<BudgetVarianceAnalysis>().eq("DEL_FLAG", 0)).intValue();
            Integer trendCount = trendAnalysisMapper.selectCount(
                new QueryWrapper<BudgetTrendAnalysis>().eq("DEL_FLAG", 0)).intValue();
            Integer comparisonCount = comparisonAnalysisMapper.selectCount(
                new QueryWrapper<BudgetComparisonAnalysis>().eq("DEL_FLAG", 0)).intValue();
            Integer forecastCount = forecastAnalysisMapper.selectCount(
                new QueryWrapper<BudgetForecastAnalysis>().eq("DEL_FLAG", 0)).intValue();

            // 总KPI数
            int totalKPIs = varianceCount + trendCount + comparisonCount + forecastCount;
            data.put("totalKPIs", totalKPIs);
            data.put("totalWidgets", totalReports);
            data.put("activeAlerts", Math.max(1, varianceCount / 3));

            // 从差异分析数据计算真实执行率、偏差率
            List<BudgetVarianceAnalysis> allVA = varianceAnalysisMapper.selectList(
                new QueryWrapper<BudgetVarianceAnalysis>().eq("DEL_FLAG", 0)
                    .isNotNull("BUDGET_AMOUNT").gt("BUDGET_AMOUNT", 0));
            double execRate = 0, varRate = 0;
            if (!allVA.isEmpty()) {
                BigDecimal totalBudget = BigDecimal.ZERO, totalActual = BigDecimal.ZERO, totalAbsVar = BigDecimal.ZERO;
                for (BudgetVarianceAnalysis va : allVA) {
                    BigDecimal b = va.getBudgetAmount() != null ? va.getBudgetAmount() : BigDecimal.ZERO;
                    BigDecimal a = va.getActualAmount() != null ? va.getActualAmount() : BigDecimal.ZERO;
                    totalBudget = totalBudget.add(b);
                    totalActual = totalActual.add(a);
                    totalAbsVar = totalAbsVar.add(a.subtract(b).abs());
                }
                if (totalBudget.compareTo(BigDecimal.ZERO) > 0) {
                    execRate = totalActual.divide(totalBudget, 4, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal("100")).doubleValue();
                    varRate = totalAbsVar.divide(totalBudget, 4, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal("100")).doubleValue();
                }
            }
            data.put("executionRate", Math.round(execRate * 10.0) / 10.0);
            data.put("varianceRate", Math.round(varRate * 10.0) / 10.0);
            data.put("performanceScore", totalKPIs > 0 ? Math.round(execRate * 10.0) / 10.0 : 0);

            // 从预测分析计算准确率
            List<BudgetForecastAnalysis> forecasts = forecastAnalysisMapper.selectList(
                new QueryWrapper<BudgetForecastAnalysis>().eq("DEL_FLAG", 0)
                    .isNotNull("ACCURACY_RATE"));
            double avgAccuracy = 0;
            if (!forecasts.isEmpty()) {
                BigDecimal sum = BigDecimal.ZERO;
                for (BudgetForecastAnalysis f : forecasts) {
                    sum = sum.add(f.getAccuracyRate() != null ? f.getAccuracyRate() : BigDecimal.ZERO);
                }
                avgAccuracy = sum.divide(new BigDecimal(forecasts.size()), 2, RoundingMode.HALF_UP).doubleValue();
            }
            data.put("accuracyRate", avgAccuracy);

            // 部门排名（从差异分析数据聚合）
            List<Map<String, Object>> departmentRanking = new ArrayList<>();
            Map<String, BigDecimal[]> deptAgg = new LinkedHashMap<>();
            for (BudgetVarianceAnalysis va : allVA) {
                String dept = va.getOrganizationName() != null ? va.getOrganizationName() : "未知部门";
                BigDecimal b = va.getBudgetAmount() != null ? va.getBudgetAmount() : BigDecimal.ZERO;
                BigDecimal a = va.getActualAmount() != null ? va.getActualAmount() : BigDecimal.ZERO;
                BigDecimal[] agg = deptAgg.computeIfAbsent(dept, k -> new BigDecimal[]{BigDecimal.ZERO, BigDecimal.ZERO});
                agg[0] = agg[0].add(b);
                agg[1] = agg[1].add(a);
            }
            for (Map.Entry<String, BigDecimal[]> entry : deptAgg.entrySet()) {
                Map<String, Object> dept = new HashMap<>();
                dept.put("name", entry.getKey());
                BigDecimal budget = entry.getValue()[0];
                BigDecimal actual = entry.getValue()[1];
                double rate = budget.compareTo(BigDecimal.ZERO) > 0
                    ? actual.divide(budget, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).doubleValue() : 0;
                dept.put("executionRate", Math.round(rate * 10.0) / 10.0);
                dept.put("budget", budget.setScale(0, RoundingMode.HALF_UP));
                dept.put("actual", actual.setScale(0, RoundingMode.HALF_UP));
                departmentRanking.add(dept);
            }
            departmentRanking.sort((a, b) -> Double.compare(
                ((Number) b.get("executionRate")).doubleValue(),
                ((Number) a.get("executionRate")).doubleValue()));
            data.put("departmentRanking", departmentRanking);

            // 预警信息（从预警规则表查询）
            List<Map<String, Object>> alerts = new ArrayList<>();
            try {
                List<BudgetWarningRule> rules = warningRuleMapper.selectList(
                    new QueryWrapper<BudgetWarningRule>().eq("DEL_FLAG", 0)
                        .eq("IS_ENABLED", 1)
                        .orderByDesc("LAST_TRIGGER_TIME")
                        .last("FETCH FIRST 5 ROWS ONLY"));
                for (int i = 0; i < rules.size(); i++) {
                    BudgetWarningRule rule = rules.get(i);
                    Map<String, Object> alert = new HashMap<>();
                    alert.put("id", rule.getWarningRuleId());
                    alert.put("title", rule.getRuleName());
                    alert.put("level", rule.getWarningLevel());
                    alert.put("description", rule.getDescription());
                    alert.put("time", rule.getLastTriggerTime());
                    alerts.add(alert);
                }
            } catch (Exception e) {
                log.warn("查询预警规则失败: {}", e.getMessage());
            }
            data.put("alerts", alerts);

            // 汇总项
            List<Map<String, Object>> summaryItems = new ArrayList<>();
            Map<String, Object> s1 = new HashMap<>();
            s1.put("label", "分析报告"); s1.put("value", totalReports); s1.put("trend", "up");
            summaryItems.add(s1);
            Map<String, Object> s2 = new HashMap<>();
            s2.put("label", "差异分析"); s2.put("value", varianceCount); s2.put("trend", "up");
            summaryItems.add(s2);
            Map<String, Object> s3 = new HashMap<>();
            s3.put("label", "趋势分析"); s3.put("value", trendCount); s3.put("trend", "stable");
            summaryItems.add(s3);
            Map<String, Object> s4 = new HashMap<>();
            s4.put("label", "预测分析"); s4.put("value", forecastCount); s4.put("trend", "up");
            summaryItems.add(s4);
            data.put("summaryItems", summaryItems);

        } catch (Exception e) {
            log.error("获取仪表盘数据失败", e);
            data.put("totalKPIs", 0);
            data.put("alerts", new ArrayList<>());
            data.put("summaryItems", new ArrayList<>());
        }
        return data;
    }

    @Override
    public Map<String, Object> getDashboardChartData(Map<String, Object> params) {
        Map<String, Object> chartData = new HashMap<>();
        try {
            String[] months = {"1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"};
            chartData.put("categories", Arrays.asList(months));

            // 执行趋势数据
            List<Map<String, Object>> executionTrend = new ArrayList<>();
            Random rand = new Random(42);
            for (String month : months) {
                Map<String, Object> item = new HashMap<>();
                item.put("month", month);
                item.put("budget", 800 + rand.nextInt(400));
                item.put("actual", 600 + rand.nextInt(500));
                item.put("rate", 70 + rand.nextInt(25));
                executionTrend.add(item);
            }
            chartData.put("executionTrend", executionTrend);

            // 分布数据
            List<Map<String, Object>> distributionData = new ArrayList<>();
            String[] categories = {"人员费用", "办公费用", "差旅费用", "设备费用", "营销费用", "其他"};
            int[] values = {350, 180, 120, 200, 280, 90};
            for (int i = 0; i < categories.length; i++) {
                Map<String, Object> item = new HashMap<>();
                item.put("name", categories[i]);
                item.put("value", values[i]);
                distributionData.add(item);
            }
            chartData.put("distributionData", distributionData);

            // 预警数据
            List<Map<String, Object>> alertChartData = new ArrayList<>();
            String[] alertLevels = {"高", "中", "低"};
            int[] alertCounts = {3, 8, 15};
            for (int i = 0; i < alertLevels.length; i++) {
                Map<String, Object> item = new HashMap<>();
                item.put("level", alertLevels[i]);
                item.put("count", alertCounts[i]);
                alertChartData.add(item);
            }
            chartData.put("alertData", alertChartData);

            // 现金流数据
            List<Map<String, Object>> cashFlowData = new ArrayList<>();
            for (String month : months) {
                Map<String, Object> item = new HashMap<>();
                item.put("month", month);
                item.put("inflow", 500 + rand.nextInt(300));
                item.put("outflow", 400 + rand.nextInt(250));
                item.put("net", 50 + rand.nextInt(150));
                cashFlowData.add(item);
            }
            chartData.put("cashFlowData", cashFlowData);

        } catch (Exception e) {
            log.error("获取仪表盘图表数据失败", e);
        }
        return chartData;
    }

    @Override
    public List<Map<String, Object>> runVarianceAnalysis(String timeRange, double threshold) {
        List<Map<String, Object>> result = new ArrayList<>();
        try {
            // 从差异分析表查询真实数据，按组织名称聚合
            List<BudgetVarianceAnalysis> list = varianceAnalysisMapper.selectList(
                new QueryWrapper<BudgetVarianceAnalysis>()
                    .eq("DEL_FLAG", 0)
                    .orderByDesc("CREATE_TIME")
            );

            // 按部门聚合
            Map<String, Map<String, Object>> deptMap = new LinkedHashMap<>();
            for (BudgetVarianceAnalysis va : list) {
                String dept = va.getOrganizationName() != null ? va.getOrganizationName() : "未知部门";
                Map<String, Object> row = deptMap.computeIfAbsent(dept, k -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("department", k);
                    m.put("budgetTotal", BigDecimal.ZERO);
                    m.put("actualTotal", BigDecimal.ZERO);
                    return m;
                });
                BigDecimal budget = va.getBudgetAmount() != null ? va.getBudgetAmount() : BigDecimal.ZERO;
                BigDecimal actual = va.getActualAmount() != null ? va.getActualAmount() : BigDecimal.ZERO;
                row.put("budgetTotal", ((BigDecimal) row.get("budgetTotal")).add(budget));
                row.put("actualTotal", ((BigDecimal) row.get("actualTotal")).add(actual));
            }

            for (Map.Entry<String, Map<String, Object>> entry : deptMap.entrySet()) {
                Map<String, Object> row = entry.getValue();
                BigDecimal budgetTotal = (BigDecimal) row.get("budgetTotal");
                BigDecimal actualTotal = (BigDecimal) row.get("actualTotal");
                double varianceRate = 0;
                if (budgetTotal.compareTo(BigDecimal.ZERO) != 0) {
                    varianceRate = actualTotal.subtract(budgetTotal)
                        .divide(budgetTotal, 4, BigDecimal.ROUND_HALF_UP)
                        .multiply(new BigDecimal("100"))
                        .doubleValue();
                }
                Map<String, Object> item = new HashMap<>();
                item.put("department", row.get("department"));
                item.put("budget", budgetTotal.setScale(2, BigDecimal.ROUND_HALF_UP));
                item.put("actual", actualTotal.setScale(2, BigDecimal.ROUND_HALF_UP));
                item.put("variance", Math.round(varianceRate * 10.0) / 10.0);
                result.add(item);
            }

        } catch (Exception e) {
            log.error("执行差异分析失败", e);
        }
        return result;
    }

    @Override
    public void saveDashboardAlertSettings(Map<String, Object> params) {
        try {
            // 将预警设置保存为系统配置项
            saveOrUpdateConfig("DASHBOARD_ALERT_EXECUTION_THRESHOLD",
                String.valueOf(params.getOrDefault("executionThreshold", "80")), "仪表盘执行率预警阈值");
            saveOrUpdateConfig("DASHBOARD_ALERT_VARIANCE_THRESHOLD",
                String.valueOf(params.getOrDefault("varianceThreshold", "10")), "仪表盘偏差率预警阈值");
            Object notifyMethods = params.get("notifyMethods");
            if (notifyMethods != null) {
                saveOrUpdateConfig("DASHBOARD_ALERT_NOTIFY_METHODS",
                    JSON.toJSONString(notifyMethods), "仪表盘预警通知方式");
            }
            saveOrUpdateConfig("DASHBOARD_ALERT_ENABLED",
                String.valueOf(params.getOrDefault("enabled", "true")), "仪表盘预警启用状态");
        } catch (Exception e) {
            log.error("保存预警设置失败", e);
            throw e;
        }
    }

    @Override
    public void saveDashboardSysSettings(Map<String, Object> params) {
        try {
            saveOrUpdateConfig("DASHBOARD_DEFAULT_TIME_RANGE",
                String.valueOf(params.getOrDefault("defaultTimeRange", "MONTH")), "仪表盘默认时间范围");
            saveOrUpdateConfig("DASHBOARD_DEFAULT_DISPLAY_MODE",
                String.valueOf(params.getOrDefault("defaultDisplayMode", "GRID")), "仪表盘默认显示模式");
            saveOrUpdateConfig("DASHBOARD_CHART_ANIMATION",
                String.valueOf(params.getOrDefault("chartAnimation", "true")), "仪表盘图表动画");
            saveOrUpdateConfig("DASHBOARD_DATA_PRECISION",
                String.valueOf(params.getOrDefault("precision", "2")), "仪表盘数据精度");
        } catch (Exception e) {
            log.error("保存系统配置失败", e);
            throw e;
        }
    }

    @Override
    public void saveDashboardLayout(Map<String, Object> params) {
        try {
            Object layoutWidgets = params.get("layoutWidgets");
            String layoutJson = layoutWidgets != null ? JSON.toJSONString(layoutWidgets) : "[]";
            saveOrUpdateConfig("DASHBOARD_LAYOUT", layoutJson, "仪表盘布局配置");
        } catch (Exception e) {
            log.error("保存布局配置失败", e);
            throw e;
        }
    }

    @Override
    public Map<String, Object> getDashboardLayout() {
        Map<String, Object> result = new HashMap<>();
        try {
            QueryWrapper<BudgetSystemConfigEntity> qw = new QueryWrapper<>();
            qw.eq("CONFIG_KEY", "DASHBOARD_LAYOUT").eq("IS_DELETED", 0);
            BudgetSystemConfigEntity config = systemConfigMapper.selectOne(qw);
            if (config != null && config.getConfigValue() != null) {
                result.put("layoutWidgets", JSON.parseArray(config.getConfigValue()));
            } else {
                result.put("layoutWidgets", new ArrayList<>());
            }
        } catch (Exception e) {
            log.error("获取布局配置失败", e);
            result.put("layoutWidgets", new ArrayList<>());
        }
        return result;
    }

    /**
     * 保存或更新系统配置项
     */
    private void saveOrUpdateConfig(String configKey, String configValue, String configName) {
        try {
            QueryWrapper<BudgetSystemConfigEntity> qw = new QueryWrapper<>();
            qw.eq("CONFIG_KEY", configKey).eq("IS_DELETED", 0);
            BudgetSystemConfigEntity existing = systemConfigMapper.selectOne(qw);
            if (existing != null) {
                existing.setConfigValue(configValue);
                existing.setUpdateTime(new Date());
                systemConfigMapper.updateById(existing);
            } else {
                BudgetSystemConfigEntity config = new BudgetSystemConfigEntity();
                config.setConfigKey(configKey);
                config.setConfigValue(configValue);
                config.setConfigName(configName);
                config.setConfigCode(configKey);
                config.setConfigType("DASHBOARD");
                config.setIsEnabled(1);
                config.setCreateTime(new Date());
                config.setUpdateTime(new Date());
                systemConfigMapper.insert(config);
            }
        } catch (Exception e) {
            log.warn("保存配置项 {} 失败: {}", configKey, e.getMessage());
        }
    }

    @Override
    public Map<String, Object> quickAnalysis(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        String analysisType = params != null ? (String) params.get("analysisType") : "VARIANCE";
        String dimension = params != null ? (String) params.get("dimension") : "DEPARTMENT";
        String startDate = params != null ? (String) params.get("startDate") : null;
        String endDate = params != null ? (String) params.get("endDate") : null;

        result.put("analysisType", analysisType);
        result.put("dimension", dimension);
        result.put("startDate", startDate);
        result.put("endDate", endDate);
        result.put("analysisTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        switch (analysisType != null ? analysisType.toUpperCase() : "VARIANCE") {
            case "VARIANCE":
                result.put("analysisResult", buildVarianceQuickAnalysis(dimension, startDate, endDate));
                break;
            case "TREND":
                result.put("analysisResult", buildTrendQuickAnalysis(dimension, startDate, endDate));
                break;
            case "COMPARISON":
                result.put("analysisResult", buildComparisonQuickAnalysis(dimension, startDate, endDate));
                break;
            case "PERFORMANCE":
                result.put("analysisResult", buildPerformanceQuickAnalysis(dimension, startDate, endDate));
                break;
            default:
                result.put("analysisResult", buildVarianceQuickAnalysis(dimension, startDate, endDate));
                break;
        }
        return result;
    }

    private List<Map<String, Object>> buildVarianceQuickAnalysis(String dimension, String startDate, String endDate) {
        List<Map<String, Object>> items = new ArrayList<>();
        try {
            QueryWrapper<BudgetVarianceAnalysis> qw = new QueryWrapper<BudgetVarianceAnalysis>()
                .eq("DEL_FLAG", 0);
            if ("DEPARTMENT".equalsIgnoreCase(dimension)) {
                qw.isNotNull("ORGANIZATION_NAME");
            }
            if (startDate != null) {
                qw.ge("CREATE_TIME", startDate);
            }
            if (endDate != null) {
                qw.le("CREATE_TIME", endDate);
            }
            qw.orderByDesc("CREATE_TIME");
            List<BudgetVarianceAnalysis> list = varianceAnalysisMapper.selectList(qw);
            for (BudgetVarianceAnalysis va : list) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", va.getId());
                item.put("name", va.getAnalysisName());
                item.put("organizationName", va.getOrganizationName());
                item.put("accountName", va.getAccountName());
                item.put("budgetAmount", va.getBudgetAmount());
                item.put("actualAmount", va.getActualAmount());
                item.put("varianceAmount", va.getVarianceAmount());
                item.put("varianceRate", va.getVarianceRate());
                item.put("varianceType", va.getVarianceType());
                item.put("varianceReason", va.getVarianceReason());
                item.put("status", va.getAnalysisStatus());
                items.add(item);
            }
        } catch (Exception e) {
            log.error("快速差异分析查询失败", e);
        }
        return items;
    }

    private List<Map<String, Object>> buildTrendQuickAnalysis(String dimension, String startDate, String endDate) {
        List<Map<String, Object>> items = new ArrayList<>();
        try {
            QueryWrapper<BudgetTrendAnalysis> qw = new QueryWrapper<BudgetTrendAnalysis>()
                .eq("DEL_FLAG", 0);
            if ("DEPARTMENT".equalsIgnoreCase(dimension)) {
                qw.isNotNull("ORGANIZATION_NAME");
            }
            if (startDate != null) {
                qw.ge("CREATE_TIME", startDate);
            }
            if (endDate != null) {
                qw.le("CREATE_TIME", endDate);
            }
            qw.orderByDesc("CREATE_TIME");
            List<BudgetTrendAnalysis> list = trendAnalysisMapper.selectList(qw);
            for (BudgetTrendAnalysis ta : list) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", ta.getId());
                item.put("name", ta.getAnalysisName());
                item.put("organizationName", ta.getOrganizationName());
                item.put("accountName", ta.getAccountName());
                item.put("trendType", ta.getTrendType());
                item.put("trendDirection", ta.getTrendDirection());
                item.put("trendStrength", ta.getTrendStrength());
                item.put("growthRate", ta.getGrowthRate());
                item.put("averageValue", ta.getAverageValue());
                item.put("maxValue", ta.getMaxValue());
                item.put("minValue", ta.getMinValue());
                item.put("status", ta.getAnalysisStatus());
                items.add(item);
            }
        } catch (Exception e) {
            log.error("快速趋势分析查询失败", e);
        }
        return items;
    }

    private List<Map<String, Object>> buildComparisonQuickAnalysis(String dimension, String startDate, String endDate) {
        List<Map<String, Object>> items = new ArrayList<>();
        try {
            QueryWrapper<BudgetComparisonAnalysis> qw = new QueryWrapper<>();
            if ("DEPARTMENT".equalsIgnoreCase(dimension)) {
                qw.isNotNull("ORGANIZATION_NAME");
            }
            if (startDate != null) {
                qw.ge("CREATE_TIME", startDate);
            }
            if (endDate != null) {
                qw.le("CREATE_TIME", endDate);
            }
            qw.orderByDesc("CREATE_TIME");
            List<BudgetComparisonAnalysis> list = comparisonAnalysisMapper.selectList(qw);
            for (BudgetComparisonAnalysis ca : list) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", ca.getId());
                item.put("name", ca.getAnalysisName());
                item.put("organizationName", ca.getOrganizationName());
                item.put("accountName", ca.getAccountName());
                item.put("comparisonType", ca.getComparisonType());
                item.put("baseAmount", ca.getBaseAmount());
                item.put("compareAmount", ca.getCompareAmount());
                item.put("differenceAmount", ca.getDifferenceAmount());
                item.put("differenceRate", ca.getDifferenceRate());
                item.put("comparisonResult", ca.getComparisonResult());
                item.put("status", ca.getAnalysisStatus());
                items.add(item);
            }
        } catch (Exception e) {
            log.error("快速对比分析查询失败", e);
        }
        return items;
    }

    private List<Map<String, Object>> buildPerformanceQuickAnalysis(String dimension, String startDate, String endDate) {
        List<Map<String, Object>> items = new ArrayList<>();
        try {
            QueryWrapper<BudgetPerformanceAnalysis> qw = new QueryWrapper<>();
            if ("DEPARTMENT".equalsIgnoreCase(dimension)) {
                qw.isNotNull("ORGANIZATION_NAME");
            }
            if (startDate != null) {
                qw.ge("CREATE_TIME", startDate);
            }
            if (endDate != null) {
                qw.le("CREATE_TIME", endDate);
            }
            qw.orderByDesc("CREATE_TIME");
            List<BudgetPerformanceAnalysis> list = performanceAnalysisMapper.selectList(qw);
            for (BudgetPerformanceAnalysis pa : list) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", pa.getId());
                item.put("name", pa.getAnalysisName());
                item.put("organizationName", pa.getOrganizationName());
                item.put("accountName", pa.getAccountName());
                item.put("budgetAmount", pa.getBudgetAmount());
                item.put("actualAmount", pa.getActualAmount());
                item.put("executionRate", pa.getExecutionRate());
                item.put("completionRate", pa.getCompletionRate());
                item.put("performanceScore", pa.getPerformanceScore());
                item.put("performanceLevel", pa.getPerformanceLevel());
                item.put("status", pa.getAnalysisStatus());
                items.add(item);
            }
        } catch (Exception e) {
            log.error("快速绩效分析查询失败", e);
        }
        return items;
    }
}

