package com.management.accountant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetSensitivityVariable;
import com.management.accountant.oracle.mapper.budget.BudgetSensitivityVariableMapper;
import com.management.accountant.service.BudgetSensitivityAnalysisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 预算敏感性分析Service实现类
 */
@Service
@Slf4j
public class BudgetSensitivityAnalysisServiceImpl implements BudgetSensitivityAnalysisService {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    @Resource
    private BudgetSensitivityVariableMapper sensitivityVariableMapper;


    @Override
    public Map<String, Object> singleFactorAnalysis(Map<String, Object> params) {
        String budgetId = (String) params.get("budgetId");
        String factor = (String) params.get("factor"); // REVENUE, COST, PRICE, VOLUME
        BigDecimal changeRange = params.get("changeRange") != null ? 
            new BigDecimal(params.get("changeRange").toString()) : new BigDecimal("0.1"); // 默认±10%

        if (!StringUtils.hasText(budgetId)) {
            throw new ServiceException("预算ID不能为空");
        }
        if (!StringUtils.hasText(factor)) {
            throw new ServiceException("分析因素不能为空");
        }

        // TODO: 实际的单因素敏感性分析逻辑
        List<Map<String, Object>> sensitivityData = new ArrayList<>();
        
        // 模拟不同变化幅度下的结果
        for (int i = -5; i <= 5; i++) {
            BigDecimal changePercent = changeRange.multiply(new BigDecimal(i));
            Map<String, Object> dataPoint = new HashMap<>();
            dataPoint.put("changePercent", changePercent);
            dataPoint.put("factorValue", new BigDecimal("100").multiply(BigDecimal.ONE.add(changePercent)));
            dataPoint.put("resultValue", new BigDecimal("1000000").multiply(BigDecimal.ONE.add(changePercent.multiply(new BigDecimal("1.5")))));
            sensitivityData.add(dataPoint);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("budgetId", budgetId);
        result.put("factor", factor);
        result.put("changeRange", changeRange);
        result.put("sensitivityData", sensitivityData);
        result.put("sensitivityCoefficient", new BigDecimal("1.5")); // 敏感系数
        result.put("analysisTime", new Date());

        log.info("单因素敏感性分析完成，预算ID: {}, 因素: {}", budgetId, factor);
        return result;
    }

    @Override
    public Map<String, Object> multiFactorAnalysis(Map<String, Object> params) {
        String budgetId = (String) params.get("budgetId");
        @SuppressWarnings("unchecked")
        List<String> factors = (List<String>) params.get("factors");

        if (!StringUtils.hasText(budgetId)) {
            throw new ServiceException("预算ID不能为空");
        }
        if (factors == null || factors.isEmpty()) {
            throw new ServiceException("分析因素列表不能为空");
        }

        // TODO: 实际的多因素敏感性分析逻辑
        List<Map<String, Object>> factorAnalysis = new ArrayList<>();
        
        for (String factor : factors) {
            Map<String, Object> factorData = new HashMap<>();
            factorData.put("factor", factor);
            factorData.put("sensitivityCoefficient", new BigDecimal(Math.random() * 2)); // 模拟敏感系数
            factorData.put("impactLevel", Math.random() > 0.5 ? "HIGH" : "MEDIUM");
            factorAnalysis.add(factorData);
        }

        // 排序：按敏感系数降序
        factorAnalysis.sort((a, b) -> {
            BigDecimal coefA = (BigDecimal) a.get("sensitivityCoefficient");
            BigDecimal coefB = (BigDecimal) b.get("sensitivityCoefficient");
            return coefB.compareTo(coefA);
        });

        Map<String, Object> result = new HashMap<>();
        result.put("budgetId", budgetId);
        result.put("factors", factors);
        result.put("factorAnalysis", factorAnalysis);
        result.put("mostSensitiveFactor", factorAnalysis.get(0).get("factor"));
        result.put("analysisTime", new Date());

        log.info("多因素敏感性分析完成，预算ID: {}, 因素数量: {}", budgetId, factors.size());
        return result;
    }

    @Override
    public Map<String, Object> generateReport(Map<String, Object> params) {
        String budgetId = (String) params.get("budgetId");
        String analysisType = (String) params.get("analysisType"); // SINGLE, MULTI

        if (!StringUtils.hasText(budgetId)) {
            throw new ServiceException("预算ID不能为空");
        }

        // TODO: 实际的报告生成逻辑
        Map<String, Object> report = new HashMap<>();
        report.put("reportId", "REPORT_" + System.currentTimeMillis());
        report.put("budgetId", budgetId);
        report.put("analysisType", analysisType);
        report.put("reportTitle", "预算敏感性分析报告");
        report.put("generateTime", new Date());
        
        // 报告内容
        Map<String, Object> content = new HashMap<>();
        content.put("summary", "本报告分析了预算对关键因素的敏感性");
        content.put("keyFindings", Arrays.asList(
            "收入变化对预算影响最大，敏感系数为1.8",
            "成本变化次之，敏感系数为1.5",
            "建议重点关注收入预测的准确性"
        ));
        content.put("recommendations", Arrays.asList(
            "加强收入预测模型的准确性",
            "建立成本控制预警机制",
            "定期进行敏感性分析更新"
        ));
        
        report.put("content", content);
        report.put("reportUrl", "/reports/sensitivity_" + budgetId + ".pdf");

        log.info("生成敏感性分析报告完成，预算ID: {}", budgetId);
        return report;
    }

    @Override
    public Map<String, Object> getSensitivityStats() {
        List<BudgetSensitivityVariable> all = sensitivityVariableMapper.selectList(new QueryWrapper<>());
        long total = all.size();
        long enabled = all.stream().filter(v -> Integer.valueOf(1).equals(v.getEnabled())).count();
        long highRisk = all.stream().filter(v -> "HIGH".equals(v.getSensitivityLevel())).count();
        long mediumRisk = all.stream().filter(v -> "MEDIUM".equals(v.getSensitivityLevel())).count();

        // 计算最大相关性（用敏感系数最大值代表）
        OptionalDouble maxCoef = all.stream()
                .filter(v -> v.getSensitivityCoefficient() != null)
                .mapToDouble(v -> v.getSensitivityCoefficient().doubleValue())
                .max();
        double maxCorrelation = maxCoef.isPresent() ? Math.min(maxCoef.getAsDouble() * 50, 100) : 0;

        // 稳定性：低风险变量占比
        double stability = total > 0 ? (double)(total - highRisk) / total * 100 : 100;

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalVariables", total);
        stats.put("sensitiveVariables", highRisk + mediumRisk);
        stats.put("maxCorrelation", Math.round(maxCorrelation));
        stats.put("stability", Math.round(stability));
        // 兼容旧字段
        stats.put("totalAnalysis", total);
        stats.put("keyFactors", enabled);
        stats.put("averageSensitivity", total > 0 ? Math.round(maxCorrelation * 0.7) : 0);
        stats.put("highRiskFactors", highRisk);
        return stats;
    }

    @Override
    public Map<String, Object> getSensitivityChartData(Map<String, Object> params) {
        // 查询所有启用的变量，按敏感系数降序排列
        List<BudgetSensitivityVariable> all = sensitivityVariableMapper.selectList(
                new QueryWrapper<BudgetSensitivityVariable>()
                        .eq("ENABLED", 1)
                        .isNotNull("SENSITIVITY_COEFFICIENT")
                        .orderByDesc("SENSITIVITY_COEFFICIENT"));

        // ===== 1. 龙卷风图：负向/正向影响来自数据库 SENSITIVITY_COEFFICIENT =====
        // 负向影响 = -(coef * (MAX_VALUE - BASE_VALUE) / BASE_VALUE * 100)，正向同理
        // 若 BASE_VALUE 为0则直接用 coef * 10 作为百分比
        List<String> yAxisNames = new ArrayList<>();
        List<Double> negData = new ArrayList<>();
        List<Double> posData = new ArrayList<>();
        for (BudgetSensitivityVariable v : all) {
            yAxisNames.add(v.getVariableName());
            BigDecimal coef = v.getSensitivityCoefficient() != null ? v.getSensitivityCoefficient() : BigDecimal.ZERO;
            BigDecimal base = v.getBaseValue();
            BigDecimal max = v.getMaxValue();
            BigDecimal min = v.getMinValue();
            double posImpact, negImpact;
            if (base != null && base.compareTo(BigDecimal.ZERO) != 0 && max != null && min != null) {
                // 正向：(MAX-BASE)/BASE * coef * 100，保留1位小数
                posImpact = coef.doubleValue() * max.subtract(base).divide(base, 4, RoundingMode.HALF_UP).doubleValue() * 100;
                negImpact = coef.doubleValue() * min.subtract(base).divide(base, 4, RoundingMode.HALF_UP).doubleValue() * 100;
            } else {
                posImpact = coef.doubleValue() * 10;
                negImpact = -coef.doubleValue() * 10;
            }
            posData.add(Math.round(posImpact * 10.0) / 10.0);
            negData.add(Math.round(negImpact * 10.0) / 10.0);
        }
        Map<String, Object> sensitivityChart = new HashMap<>();
        sensitivityChart.put("yAxis", yAxisNames);
        List<Map<String, Object>> series = new ArrayList<>();
        Map<String, Object> negSeries = new HashMap<>(); negSeries.put("data", negData); series.add(negSeries);
        Map<String, Object> posSeries = new HashMap<>(); posSeries.put("data", posData); series.add(posSeries);
        sensitivityChart.put("series", series);

        // ===== 2. 相关性热力图：基于数据库字段确定性计算，不使用随机数 =====
        // 相关性规则（完全由数据库字段决定）：
        //   同 VARIABLE_TYPE 的两个变量：相关性 = 0.5 + (coefI + coefJ) / (2 * maxCoef) * 0.4
        //   不同类型但同方向（REVENUE类 vs REVENUE类，COST类 vs COST类）：相关性 = 0.2
        //   收入类 vs 成本类（反向）：相关性 = -0.3 - (coefI + coefJ) / (2 * maxCoef) * 0.2
        //   其他组合：相关性 = (coefI - coefJ) / maxCoef * 0.15
        List<String> varNames = all.stream().map(BudgetSensitivityVariable::getVariableName).collect(Collectors.toList());
        double maxCoef = all.stream()
                .filter(v -> v.getSensitivityCoefficient() != null)
                .mapToDouble(v -> v.getSensitivityCoefficient().doubleValue())
                .max().orElse(1.0);
        Set<String> revenueTypes = new HashSet<>(Arrays.asList("REVENUE", "MARKET_SHARE"));
        Set<String> costTypes = new HashSet<>(Arrays.asList("COST", "INFLATION_RATE", "INTEREST_RATE"));

        List<List<Object>> heatData = new ArrayList<>();
        for (int i = 0; i < all.size(); i++) {
            for (int j = 0; j < all.size(); j++) {
                double corr;
                if (i == j) {
                    corr = 1.0;
                } else {
                    BudgetSensitivityVariable vi = all.get(i);
                    BudgetSensitivityVariable vj = all.get(j);
                    double ci = vi.getSensitivityCoefficient() != null ? vi.getSensitivityCoefficient().doubleValue() : 0;
                    double cj = vj.getSensitivityCoefficient() != null ? vj.getSensitivityCoefficient().doubleValue() : 0;
                    String ti = vi.getVariableType() != null ? vi.getVariableType() : "";
                    String tj = vj.getVariableType() != null ? vj.getVariableType() : "";
                    if (ti.equals(tj)) {
                        // 同类型：正相关，系数越接近相关性越高
                        corr = 0.5 + (ci + cj) / (2 * maxCoef) * 0.4;
                    } else if (revenueTypes.contains(ti) && costTypes.contains(tj)
                            || costTypes.contains(ti) && revenueTypes.contains(tj)) {
                        // 收入类 vs 成本类：负相关
                        corr = -0.3 - (ci + cj) / (2 * maxCoef) * 0.2;
                    } else if ((revenueTypes.contains(ti) && revenueTypes.contains(tj))
                            || (costTypes.contains(ti) && costTypes.contains(tj))) {
                        // 同方向不同类型：弱正相关
                        corr = 0.2 + Math.abs(ci - cj) / maxCoef * 0.1;
                    } else {
                        // 其他：弱相关，由系数差决定方向
                        corr = (ci - cj) / maxCoef * 0.15;
                    }
                    corr = Math.round(corr * 100.0) / 100.0;
                    corr = Math.max(-1.0, Math.min(1.0, corr));
                }
                List<Object> cell = new ArrayList<>();
                cell.add(i); cell.add(j); cell.add(corr);
                heatData.add(cell);
            }
        }
        Map<String, Object> correlationChart = new HashMap<>();
        correlationChart.put("xAxis", varNames);
        correlationChart.put("yAxis", varNames);
        correlationChart.put("data", heatData);

        // ===== 3. 敏感性指数饼图：按 SENSITIVITY_LEVEL 分组统计 =====
        // 查全部（含禁用）做分布统计
        List<BudgetSensitivityVariable> allForLevel = sensitivityVariableMapper.selectList(new QueryWrapper<>());
        Map<String, Long> levelCount = allForLevel.stream().collect(
                Collectors.groupingBy(v -> v.getSensitivityLevel() != null ? v.getSensitivityLevel() : "NONE", Collectors.counting()));
        Map<String, String> levelNames = new LinkedHashMap<>();
        levelNames.put("HIGH", "高敏感"); levelNames.put("MEDIUM", "中敏感");
        levelNames.put("LOW", "低敏感"); levelNames.put("NONE", "不敏感");
        List<Map<String, Object>> indexData = new ArrayList<>();
        levelNames.forEach((k, name) -> {
            long cnt = levelCount.getOrDefault(k, 0L);
            if (cnt > 0) {
                Map<String, Object> item = new HashMap<>();
                item.put("name", name); item.put("value", cnt);
                indexData.add(item);
            }
        });
        Map<String, Object> indexChart = new HashMap<>();
        indexChart.put("data", indexData);

        // ===== 4. 影响程度分布柱状图：同上分组统计 =====
        String[] levels = {"HIGH", "MEDIUM", "LOW", "NONE"};
        String[] levelLabels = {"高影响", "中影响", "低影响", "无影响"};
        List<Long> distData = new ArrayList<>();
        for (String lv : levels) { distData.add(levelCount.getOrDefault(lv, 0L)); }
        Map<String, Object> distributionChart = new HashMap<>();
        distributionChart.put("xAxis", Arrays.asList(levelLabels));
        distributionChart.put("data", distData);

        // ===== 5. 敏感性排名：SENSITIVITY_COEFFICIENT * 50 作为百分比展示 =====
        List<Map<String, Object>> ranking = new ArrayList<>();
        for (BudgetSensitivityVariable v : all) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", v.getId());
            item.put("variableName", v.getVariableName());
            BigDecimal coef = v.getSensitivityCoefficient() != null ? v.getSensitivityCoefficient() : BigDecimal.ZERO;
            item.put("sensitivity", coef.multiply(new BigDecimal("50")).setScale(1, RoundingMode.HALF_UP));
            item.put("sensitivityLevel", v.getSensitivityLevel() != null ? v.getSensitivityLevel() : "NONE");
            ranking.add(item);
        }

        Map<String, Object> chartData = new HashMap<>();
        chartData.put("sensitivityChart", sensitivityChart);
        chartData.put("correlationChart", correlationChart);
        chartData.put("indexChart", indexChart);
        chartData.put("distributionChart", distributionChart);
        chartData.put("ranking", ranking);
        return chartData;
    }
}

