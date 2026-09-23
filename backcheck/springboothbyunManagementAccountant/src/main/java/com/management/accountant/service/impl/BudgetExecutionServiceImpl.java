package com.management.accountant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetExecution;
import com.management.accountant.oracle.entity.budget.BudgetImprovementSuggestion;
import com.management.accountant.oracle.mapper.budget.BudgetDataMapper;
import com.management.accountant.oracle.mapper.budget.BudgetExecutionMapper;
import com.management.accountant.oracle.mapper.budget.BudgetImprovementSuggestionMapper;
import com.management.accountant.service.BudgetExecutionService;
import com.management.accountant.util.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * 预算执行分析Service实现类
 * 
 * @description 预算执行分析业务实现
 * @author AI Assistant
 * @date 2025-01-04
 */
@Service
@Slf4j
public class BudgetExecutionServiceImpl implements BudgetExecutionService {


    @Resource
    private BudgetExecutionMapper executionMapper;

    @Resource
    private BudgetDataMapper budgetDataMapper;

    @Resource
    private BudgetImprovementSuggestionMapper suggestionMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BudgetExecution create(BudgetExecution execution) {
        // 1. 参数校验
        if (execution == null) {
            throw new ServiceException("执行信息不能为空");
        }
        if (execution.getAmount() == null) {
            throw new ServiceException("金额不能为空");
        }

        // 2. 设置默认值
        if (execution.getExecutionTime() == null) {
            execution.setExecutionTime(new Date());
        }

        // 3. 插入数据库
        int result = executionMapper.insert(execution);
        if (result <= 0) {
            throw new ServiceException("创建执行记录失败");
        }

        log.info("创建执行记录成功，ID: {}", execution.getExecutionId());
        return execution;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BudgetExecution update(BudgetExecution execution) {
        if (execution == null || !StringUtils.hasText(execution.getExecutionId())) {
            throw new ServiceException("执行ID不能为空");
        }
        BudgetExecution existing = executionMapper.selectById(execution.getExecutionId());
        if (existing == null) {
            throw new ServiceException("执行记录不存在：" + execution.getExecutionId());
        }
        int result = executionMapper.updateById(execution);
        if (result <= 0) {
            throw new ServiceException("更新执行记录失败");
        }
        log.info("更新执行记录成功，ID: {}", execution.getExecutionId());
        return executionMapper.selectById(execution.getExecutionId());
    }

    @Override
    public BudgetExecution getById(String executionId) {
        if (!StringUtils.hasText(executionId)) {
            throw new ServiceException("执行ID不能为空");
        }
        return executionMapper.selectById(executionId);
    }

    @Override
    public PageResult<BudgetExecution> getPage(Map<String, Object> params) {
        // 1. 获取分页参数
        int pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 20;

        // 2. 构建查询条件
        QueryWrapper<BudgetExecution> wrapper = new QueryWrapper<>();

        // 预算类型
        if (params.get("budgetType") != null && StringUtils.hasText(params.get("budgetType").toString())) {
            wrapper.eq("BUDGET_TYPE", params.get("budgetType"));
        }

        // 预算ID
        if (params.get("budgetId") != null && StringUtils.hasText(params.get("budgetId").toString())) {
            wrapper.eq("BUDGET_ID", params.get("budgetId"));
        }

        // 业务类型
        if (params.get("businessType") != null && StringUtils.hasText(params.get("businessType").toString())) {
            wrapper.eq("BUSINESS_TYPE", params.get("businessType"));
        }

        // 执行人
        if (params.get("executionUser") != null && StringUtils.hasText(params.get("executionUser").toString())) {
            wrapper.like("EXECUTION_USER_NAME", params.get("executionUser"));
        }

        // 执行时间范围
        if (params.get("startDate") != null) {
            wrapper.ge("EXECUTION_TIME", params.get("startDate"));
        }
        if (params.get("endDate") != null) {
            wrapper.le("EXECUTION_TIME", params.get("endDate"));
        }

        // 排序
        wrapper.orderByDesc("EXECUTION_TIME");

        // 3. 执行分页查询
        Page<BudgetExecution> page = new Page<>(pageNum, pageSize);
        IPage<BudgetExecution> pageResult = executionMapper.selectPage(page, wrapper);

        // 4. 封装返回结果
        PageResult<BudgetExecution> result = new PageResult<>();
        result.setTlist(pageResult.getRecords());
        result.setTotalRecord((int) pageResult.getTotal());
        result.setPageNo(pageNum);
        result.setPageSize(pageSize);

        return result;
    }

    @Override
    public Map<String, Object> getProgressAnalysis(Map<String, Object> params) {
        Map<String, Object> analysis = new HashMap<>();

        try {
            String budgetId = (String) params.get("budgetId");

            QueryWrapper<BudgetExecution> wrapper = new QueryWrapper<>();
            if (StringUtils.hasText(budgetId)) {
                wrapper.eq("BUDGET_ID", budgetId);
            }

            List<BudgetExecution> executions = executionMapper.selectList(wrapper);

            BigDecimal totalExecuted = executions.stream()
                .map(BudgetExecution::getAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal budgetTotal = new BigDecimal("1000000");

            BigDecimal executionRate = BigDecimal.ZERO;
            if (budgetTotal.compareTo(BigDecimal.ZERO) > 0) {
                executionRate = totalExecuted.divide(budgetTotal, 4, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"));
            }

            analysis.put("budgetTotal", budgetTotal);
            analysis.put("totalExecuted", totalExecuted);
            analysis.put("executionRate", executionRate);
            analysis.put("remainingBudget", budgetTotal.subtract(totalExecuted));
            analysis.put("executionCount", executions.size());

        } catch (Exception e) {
            log.error("获取执行进度分析异常", e);
            analysis.put("error", e.getMessage());
        }

        return analysis;
    }

    @Override
    public Map<String, Object> getVarianceAnalysis(Map<String, Object> params) {
        Map<String, Object> analysis = new HashMap<>();

        try {
            String budgetId = (String) params.get("budgetId");

            QueryWrapper<BudgetExecution> wrapper = new QueryWrapper<>();
            if (StringUtils.hasText(budgetId)) {
                wrapper.eq("BUDGET_ID", budgetId);
            }

            List<BudgetExecution> executions = executionMapper.selectList(wrapper);

            BigDecimal actualAmount = executions.stream()
                .map(BudgetExecution::getAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal budgetAmount = new BigDecimal("1000000");

            BigDecimal variance = actualAmount.subtract(budgetAmount);
            BigDecimal varianceRate = BigDecimal.ZERO;
            if (budgetAmount.compareTo(BigDecimal.ZERO) > 0) {
                varianceRate = variance.divide(budgetAmount, 4, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"));
            }

            analysis.put("budgetAmount", budgetAmount);
            analysis.put("actualAmount", actualAmount);
            analysis.put("variance", variance);
            analysis.put("varianceRate", varianceRate);
            analysis.put("varianceType", variance.compareTo(BigDecimal.ZERO) > 0 ? "超支" : "节约");

        } catch (Exception e) {
            log.error("获取执行偏差分析异常", e);
            analysis.put("error", e.getMessage());
        }

        return analysis;
    }

    @Override
    public Map<String, Object> getTrendAnalysis(Map<String, Object> params) {
        Map<String, Object> analysis = new HashMap<>();

        try {
            String budgetId = (String) params.get("budgetId");

            QueryWrapper<BudgetExecution> wrapper = new QueryWrapper<>();
            if (StringUtils.hasText(budgetId)) {
                wrapper.eq("BUDGET_ID", budgetId);
            }
            wrapper.orderByAsc("EXECUTION_TIME");

            List<BudgetExecution> executions = executionMapper.selectList(wrapper);

            Map<String, BigDecimal> monthlyTrend = new LinkedHashMap<>();
            for (BudgetExecution execution : executions) {
                if (execution.getExecutionTime() != null) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(execution.getExecutionTime());
                    String month = String.format("%d-%02d", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1);

                    BigDecimal amount = monthlyTrend.getOrDefault(month, BigDecimal.ZERO);
                    monthlyTrend.put(month, amount.add(execution.getAmount() != null ? execution.getAmount() : BigDecimal.ZERO));
                }
            }

            analysis.put("monthlyTrend", monthlyTrend);
            analysis.put("totalMonths", monthlyTrend.size());

        } catch (Exception e) {
            log.error("获取执行趋势分析异常", e);
            analysis.put("error", e.getMessage());
        }

        return analysis;
    }

    @Override
    public Map<String, Object> getStatistics() {
        Map<String, Object> statistics = new HashMap<>();

        // 总记录数
        QueryWrapper<BudgetExecution> wrapper = new QueryWrapper<>();
        Integer totalCount = executionMapper.selectCount(wrapper).intValue();
        statistics.put("totalCount", totalCount);

        // 按预算类型统计
        QueryWrapper<BudgetExecution> typeWrapper = new QueryWrapper<>();
        typeWrapper.select("BUDGET_TYPE", "COUNT(*) as count")
                   .groupBy("BUDGET_TYPE");
        List<Map<String, Object>> typeStats = executionMapper.selectMaps(typeWrapper);
        statistics.put("typeStatistics", typeStats);

        // 总执行金额
        QueryWrapper<BudgetExecution> amountWrapper = new QueryWrapper<>();
        amountWrapper.select("SUM(AMOUNT) as totalAmount");
        List<Map<String, Object>> amountList = executionMapper.selectMaps(amountWrapper);
        if (!amountList.isEmpty() && amountList.get(0) != null && amountList.get(0).get("totalAmount") != null) {
            statistics.put("totalAmount", amountList.get(0).get("totalAmount"));
        } else {
            statistics.put("totalAmount", BigDecimal.ZERO);
        }

        return statistics;
    }

    @Override
    public Map<String, Object> generateReport(Map<String, Object> params) {
        Map<String, Object> report = new HashMap<>();
        
        try {
            // 获取进度分析
            Map<String, Object> progressAnalysis = getProgressAnalysis(params);
            report.put("progressAnalysis", progressAnalysis);
            
            // 获取偏差分析
            Map<String, Object> varianceAnalysis = getVarianceAnalysis(params);
            report.put("varianceAnalysis", varianceAnalysis);
            
            // 获取趋势分析
            Map<String, Object> trendAnalysis = getTrendAnalysis(params);
            report.put("trendAnalysis", trendAnalysis);
            
            // 获取统计信息
            Map<String, Object> statistics = getStatistics();
            report.put("statistics", statistics);
            
            report.put("reportTime", new Date());
            report.put("reportType", "执行分析报表");
            
        } catch (Exception e) {
            log.error("生成执行报表异常", e);
            report.put("error", e.getMessage());
        }
        
        return report;
    }

    @Override
    public Map<String, Object> getExecutionRate(String budgetId) {
        Map<String, Object> result = new HashMap<>();
        
        if (!StringUtils.hasText(budgetId)) {
            throw new ServiceException("预算ID不能为空");
        }
        
        Map<String, Object> params = new HashMap<>();
        params.put("budgetId", budgetId);
        
        return getProgressAnalysis(params);
    }

    @Override
    public Map<String, Object> getDepartmentExecution(String departmentId) {
        Map<String, Object> result = new HashMap<>();

        QueryWrapper<BudgetExecution> wrapper = new QueryWrapper<>();
        if (StringUtils.hasText(departmentId)) {
            wrapper.eq("BUSINESS_ID", departmentId);
        }

        List<BudgetExecution> executions = executionMapper.selectList(wrapper);

        BigDecimal totalAmount = executions.stream()
            .map(BudgetExecution::getAmount)
            .filter(Objects::nonNull)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        result.put("departmentId", departmentId);
        result.put("executionCount", executions.size());
        result.put("totalAmount", totalAmount);

        return result;
    }

    @Override
    public Map<String, Object> getExecutionStats() {
        Map<String, Object> stats = new HashMap<>();
        try {
            // 从 TBL_BUDGET_DATA 聚合计算真实绩效统计
            Map<String, Object> perfStats = budgetDataMapper.selectPerformanceStats();

            if (perfStats != null && perfStats.get("DATA_COUNT") != null) {
                // 综合绩效得分：基于预算执行率（实际值/预算值*100），上限100
                BigDecimal avgExecRate = perfStats.get("AVG_EXECUTION_RATE") != null
                    ? new BigDecimal(perfStats.get("AVG_EXECUTION_RATE").toString()) : BigDecimal.ZERO;
                int overallScore = Math.min(avgExecRate.setScale(0, RoundingMode.HALF_UP).intValue(), 100);

                // 执行效率：基于实际执行总额与预算总额的比率
                BigDecimal totalBudget = perfStats.get("TOTAL_BUDGET") != null
                    ? new BigDecimal(perfStats.get("TOTAL_BUDGET").toString()) : BigDecimal.ZERO;
                BigDecimal totalActual = perfStats.get("TOTAL_ACTUAL") != null
                    ? new BigDecimal(perfStats.get("TOTAL_ACTUAL").toString()) : BigDecimal.ZERO;
                int efficiency = 0;
                if (totalBudget.compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal effRate = totalActual.divide(totalBudget, 4, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal("100"));
                    efficiency = Math.min(effRate.setScale(0, RoundingMode.HALF_UP).intValue(), 100);
                }

                // 执行准确性：100 - 平均差异率的绝对值
                BigDecimal avgVarianceRate = perfStats.get("AVG_VARIANCE_RATE") != null
                    ? new BigDecimal(perfStats.get("AVG_VARIANCE_RATE").toString()) : BigDecimal.ZERO;
                int accuracy = Math.max(0, new BigDecimal("100").subtract(avgVarianceRate)
                    .setScale(0, RoundingMode.HALF_UP).intValue());

                // 改进率：与上一年度对比计算
                int improvementRate = 0;
                Map<String, Object> prevStats = budgetDataMapper.selectPreviousYearStats();
                if (prevStats != null && prevStats.get("PREV_AVG_VARIANCE_RATE") != null) {
                    BigDecimal prevVarianceRate = new BigDecimal(prevStats.get("PREV_AVG_VARIANCE_RATE").toString());
                    if (prevVarianceRate.compareTo(BigDecimal.ZERO) > 0) {
                        BigDecimal improvement = prevVarianceRate.subtract(avgVarianceRate)
                            .divide(prevVarianceRate, 4, RoundingMode.HALF_UP)
                            .multiply(new BigDecimal("100"));
                        improvementRate = improvement.setScale(0, RoundingMode.HALF_UP).intValue();
                    }
                }

                // 获取执行记录总数
                QueryWrapper<BudgetExecution> wrapper = new QueryWrapper<>();
                int totalCount = executionMapper.selectCount(wrapper).intValue();

                stats.put("overallScore", overallScore);
                stats.put("efficiency", efficiency);
                stats.put("accuracy", accuracy);
                stats.put("improvementRate", improvementRate);
                stats.put("totalExecutions", totalCount);
            } else {
                stats.put("overallScore", 0);
                stats.put("efficiency", 0);
                stats.put("accuracy", 0);
                stats.put("improvementRate", 0);
                stats.put("totalExecutions", 0);
            }
        } catch (Exception e) {
            log.error("获取绩效统计数据失败", e);
            stats.put("overallScore", 0);
            stats.put("efficiency", 0);
            stats.put("accuracy", 0);
            stats.put("improvementRate", 0);
        }
        return stats;
    }

    @Override
    public Map<String, Object> getExecutionChartData(Map<String, Object> params) {
        Map<String, Object> chartData = new HashMap<>();
        try {
            // 从 TBL_BUDGET_DATA 按期间分组查询真实预算值和实际值
            List<Map<String, Object>> periodData = budgetDataMapper.selectBudgetActualByPeriod();

            List<String> categories = new ArrayList<>();
            List<BigDecimal> budgetAmounts = new ArrayList<>();
            List<BigDecimal> executionAmounts = new ArrayList<>();
            List<BigDecimal> executionRates = new ArrayList<>();

            if (periodData != null) {
                for (Map<String, Object> item : periodData) {
                    String period = item.get("BUDGET_PERIOD") != null ? item.get("BUDGET_PERIOD").toString() : "";
                    categories.add(period);
                    budgetAmounts.add(item.get("BUDGET_AMOUNT") != null
                        ? new BigDecimal(item.get("BUDGET_AMOUNT").toString()) : BigDecimal.ZERO);
                    executionAmounts.add(item.get("ACTUAL_AMOUNT") != null
                        ? new BigDecimal(item.get("ACTUAL_AMOUNT").toString()) : BigDecimal.ZERO);
                    executionRates.add(item.get("EXECUTION_RATE") != null
                        ? new BigDecimal(item.get("EXECUTION_RATE").toString()) : BigDecimal.ZERO);
                }
            }

            // --- 雷达图数据：从 TBL_BUDGET_DATA 计算6个维度真实数据 ---
            Map<String, Object> radarChart = new HashMap<>();
            Map<String, Object> radarDims = budgetDataMapper.selectRadarDimensions();
            List<Number> currentValues = new ArrayList<>();
            List<Integer> targetValues = new ArrayList<>();
            if (radarDims != null && radarDims.get("EFFICIENCY") != null) {
                currentValues.add(radarDims.get("EFFICIENCY") != null ? new BigDecimal(radarDims.get("EFFICIENCY").toString()).doubleValue() : 0);
                currentValues.add(radarDims.get("ACCURACY") != null ? new BigDecimal(radarDims.get("ACCURACY").toString()).doubleValue() : 0);
                currentValues.add(radarDims.get("TIMELINESS") != null ? new BigDecimal(radarDims.get("TIMELINESS").toString()).doubleValue() : 0);
                currentValues.add(radarDims.get("COMPLIANCE") != null ? new BigDecimal(radarDims.get("COMPLIANCE").toString()).doubleValue() : 0);
                currentValues.add(radarDims.get("COST_CONTROL") != null ? new BigDecimal(radarDims.get("COST_CONTROL").toString()).doubleValue() : 0);
                currentValues.add(radarDims.get("QUALITY") != null ? new BigDecimal(radarDims.get("QUALITY").toString()).doubleValue() : 0);
            } else {
                for (int i = 0; i < 6; i++) { currentValues.add(0); }
            }
            for (int i = 0; i < 6; i++) { targetValues.add(90); }
            List<Map<String, Object>> radarData = new ArrayList<>();
            Map<String, Object> currentSeries = new HashMap<>();
            currentSeries.put("value", currentValues);
            currentSeries.put("name", "当期绩效");
            radarData.add(currentSeries);
            Map<String, Object> targetSeries = new HashMap<>();
            targetSeries.put("value", targetValues);
            targetSeries.put("name", "目标绩效");
            radarData.add(targetSeries);
            radarChart.put("data", radarData);
            chartData.put("radarChart", radarChart);

            // --- 效率趋势图：按期间的执行率折线 ---
            Map<String, Object> efficiencyChart = new HashMap<>();
            efficiencyChart.put("xAxis", categories);
            List<Integer> effData = new ArrayList<>();
            for (BigDecimal rate : executionRates) {
                effData.add(Math.min(rate.setScale(0, RoundingMode.HALF_UP).intValue(), 100));
            }
            efficiencyChart.put("data", effData);
            chartData.put("efficiencyChart", efficiencyChart);

            // --- 准确性分析饼图：从数据库按差异率区间分布查询 ---
            Map<String, Object> accuracyChart = new HashMap<>();
            List<Map<String, Object>> distData = budgetDataMapper.selectAccuracyDistribution();
            List<Map<String, Object>> pieData = new ArrayList<>();
            if (distData != null) {
                for (Map<String, Object> item : distData) {
                    Map<String, Object> pieItem = new HashMap<>();
                    pieItem.put("name", item.get("ACCURACY_LEVEL") != null ? item.get("ACCURACY_LEVEL").toString() : "");
                    pieItem.put("value", item.get("CNT") != null ? Integer.parseInt(item.get("CNT").toString()) : 0);
                    pieData.add(pieItem);
                }
            }
            if (pieData.isEmpty()) { Map<String, Object> m = new HashMap<>(); m.put("name", "暂无数据"); m.put("value", 1); pieData.add(m); }
            accuracyChart.put("data", pieData);
            chartData.put("accuracyChart", accuracyChart);

            chartData.put("categories", categories);
            chartData.put("budgetAmounts", budgetAmounts);
            chartData.put("executionAmounts", executionAmounts);
            chartData.put("executionRates", executionRates);
        } catch (Exception e) {
            log.error("获取图表数据失败", e);
            chartData.put("radarChart", new HashMap<>());
            chartData.put("efficiencyChart", new HashMap<>());
            chartData.put("accuracyChart", new HashMap<>());
        }
        return chartData;
    }

    @Override
    public Map<String, Object> getExecutionRanking() {
        Map<String, Object> result = new HashMap<>();
        try {
            // 从 TBL_BUDGET_EXECUTION 按用户分组，关联 TBL_BUDGET_DATA 计算真实得分
            List<Map<String, Object>> rankList = executionMapper.selectUserRanking();

            List<Map<String, Object>> rankings = new ArrayList<>();
            for (Map<String, Object> item : rankList) {
                Map<String, Object> rankItem = new HashMap<>();
                rankItem.put("id", item.get("EXECUTION_USER"));
                String userName = item.get("EXECUTION_USER_NAME") != null
                    ? item.get("EXECUTION_USER_NAME").toString() : "未知";
                rankItem.put("organizationName", userName);

                // 使用数据库计算的真实得分（执行金额/预算金额*100）
                BigDecimal score = BigDecimal.ZERO;
                if (item.get("SCORE") != null) {
                    score = new BigDecimal(item.get("SCORE").toString());
                    // 上限100
                    if (score.compareTo(new BigDecimal("100")) > 0) {
                        score = new BigDecimal("100");
                    }
                }
                rankItem.put("score", score.setScale(0, RoundingMode.HALF_UP).intValue());

                BigDecimal totalAmount = item.get("TOTAL_AMOUNT") != null
                    ? new BigDecimal(item.get("TOTAL_AMOUNT").toString()) : BigDecimal.ZERO;
                rankItem.put("totalAmount", totalAmount);

                int execCount = item.get("EXEC_COUNT") != null
                    ? Integer.parseInt(item.get("EXEC_COUNT").toString()) : 0;
                rankItem.put("execCount", execCount);

                rankings.add(rankItem);
            }
            result.put("rankings", rankings);
        } catch (Exception e) {
            log.error("获取绩效排名失败", e);
            result.put("rankings", new ArrayList<>());
        }
        return result;
    }

    @Override
    public Map<String, Object> getImprovementSuggestions() {
        Map<String, Object> result = new HashMap<>();
        try {
            // 从 TBL_BUDGET_IMPROVEMENT_SUGGESTION 表查询真实改进建议
            List<BudgetImprovementSuggestion> suggestionList = suggestionMapper.selectActiveSuggestions();

            List<Map<String, Object>> suggestions = new ArrayList<>();
            for (BudgetImprovementSuggestion s : suggestionList) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", s.getSuggestionId());
                item.put("priority", s.getPriority());
                item.put("title", s.getTitle());
                item.put("description", s.getDescription());
                item.put("category", s.getCategory());
                item.put("status", s.getStatus());
                suggestions.add(item);
            }

            result.put("suggestions", suggestions);
        } catch (Exception e) {
            log.error("获取改进建议失败", e);
            result.put("suggestions", new ArrayList<>());
        }
        return result;
    }

    @Override
    public List<BudgetExecution> list() {
        QueryWrapper<BudgetExecution> wrapper = new QueryWrapper<>();
        return executionMapper.selectList(wrapper);
    }

    @Override
    public List<BudgetExecution> list(QueryWrapper<BudgetExecution> wrapper) {
        return executionMapper.selectList(wrapper);
    }

    @Override
    public List<BudgetExecution> listByIds(Collection<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return new ArrayList<>();
        }
        return executionMapper.selectBatchIds(ids);
    }
}

