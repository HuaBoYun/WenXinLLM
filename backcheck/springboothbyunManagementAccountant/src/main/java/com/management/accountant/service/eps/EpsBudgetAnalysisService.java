package com.management.accountant.service.eps;

import java.util.List;
import java.util.Map;

/**
 * 预算分析服务接口
 * 
 * @author 华博云
 * @version 3.0.0
 */
public interface EpsBudgetAnalysisService {

    /**
     * 获取预算执行分析
     * 
     * @param versionId 版本ID
     * @param organizationId 组织ID
     * @param subjectId 科目ID
     * @param analysisPeriod 分析期间
     * @param analysisDimension 分析维度
     * @return 执行分析结果
     */
    Map<String, Object> getBudgetExecutionAnalysis(Long versionId, Long organizationId, 
                                                  Long subjectId, String analysisPeriod, 
                                                  String analysisDimension);

    /**
     * 获取预算差异分析
     * 
     * @param versionId 版本ID
     * @param compareVersionId 对比版本ID
     * @param organizationId 组织ID
     * @param subjectId 科目ID
     * @param varianceType 差异类型
     * @return 差异分析结果
     */
    Map<String, Object> getBudgetVarianceAnalysis(Long versionId, Long compareVersionId,
                                                 Long organizationId, Long subjectId, 
                                                 String varianceType);

    /**
     * 获取预算趋势分析
     * 
     * @param versionId 版本ID
     * @param organizationId 组织ID
     * @param subjectId 科目ID
     * @param trendPeriods 趋势周期
     * @param forecastPeriods 预测周期
     * @return 趋势分析结果
     */
    Map<String, Object> getBudgetTrendAnalysis(Long versionId, Long organizationId,
                                              Long subjectId, Integer trendPeriods, 
                                              Integer forecastPeriods);

    /**
     * 获取预算结构分析
     * 
     * @param versionId 版本ID
     * @param analysisDimension 分析维度
     * @param organizationId 组织ID
     * @param hierarchyDepth 层级深度
     * @return 结构分析结果
     */
    Map<String, Object> getBudgetStructureAnalysis(Long versionId, String analysisDimension,
                                                  Long organizationId, Integer hierarchyDepth);

    /**
     * 获取预算对比分析
     * 
     * @param sourceVersionId 源版本ID
     * @param targetVersionId 目标版本ID
     * @param comparisonDimension 对比维度
     * @param organizationId 组织ID
     * @param subjectId 科目ID
     * @return 对比分析结果
     */
    Map<String, Object> getBudgetComparisonAnalysis(Long sourceVersionId, Long targetVersionId,
                                                   String comparisonDimension, Long organizationId, 
                                                   Long subjectId);

    /**
     * 获取预算完成率分析
     * 
     * @param versionId 版本ID
     * @param organizationId 组织ID
     * @param subjectId 科目ID
     * @param statisticsPeriod 统计周期
     * @return 完成率分析结果
     */
    Map<String, Object> getBudgetCompletionRateAnalysis(Long versionId, Long organizationId,
                                                       Long subjectId, String statisticsPeriod);

    /**
     * 获取预算排名分析
     * 
     * @param versionId 版本ID
     * @param rankingDimension 排名维度
     * @param rankingMetric 排名指标
     * @param rankingLimit 排名数量
     * @return 排名分析结果
     */
    Map<String, Object> getBudgetRankingAnalysis(Long versionId, String rankingDimension,
                                                String rankingMetric, Integer rankingLimit);

    /**
     * 获取预算异常分析
     * 
     * @param versionId 版本ID
     * @param anomalyType 异常类型
     * @param anomalyThreshold 异常阈值
     * @param organizationId 组织ID
     * @return 异常分析结果
     */
    Map<String, Object> getBudgetAnomalyAnalysis(Long versionId, String anomalyType,
                                                Double anomalyThreshold, Long organizationId);

    /**
     * 生成预算分析报表
     * 
     * @param reportParams 报表参数
     * @return 报表生成结果
     */
    Map<String, Object> generateBudgetAnalysisReport(Map<String, Object> reportParams);

    /**
     * 导出预算分析数据
     * 
     * @param exportParams 导出参数
     * @return 导出结果
     */
    Map<String, Object> exportBudgetAnalysisData(Map<String, Object> exportParams);

    /**
     * 获取预算分析仪表板
     * 
     * @param versionId 版本ID
     * @param organizationId 组织ID
     * @param dashboardType 仪表板类型
     * @return 仪表板数据
     */
    Map<String, Object> getBudgetAnalysisDashboard(Long versionId, Long organizationId, 
                                                  String dashboardType);

    /**
     * 获取预算分析图表数据
     * 
     * @param versionId 版本ID
     * @param chartType 图表类型
     * @param chartParams 图表参数
     * @return 图表数据
     */
    Map<String, Object> getBudgetAnalysisChartData(Long versionId, String chartType, 
                                                  String chartParams);

    /**
     * 保存预算分析配置
     * 
     * @param analysisConfig 分析配置
     * @return 保存结果
     */
    boolean saveBudgetAnalysisConfig(Map<String, Object> analysisConfig);

    /**
     * 获取预算分析配置
     * 
     * @param versionId 版本ID
     * @param configType 配置类型
     * @return 分析配置
     */
    Map<String, Object> getBudgetAnalysisConfig(Long versionId, String configType);

    /**
     * 获取预算分析模板
     * 
     * @param templateType 模板类型
     * @return 分析模板列表
     */
    List<Map<String, Object>> getBudgetAnalysisTemplates(String templateType);

    /**
     * 应用预算分析模板
     * 
     * @param templateParams 模板参数
     * @return 应用结果
     */
    Map<String, Object> applyBudgetAnalysisTemplate(Map<String, Object> templateParams);

    /**
     * 获取预算分析洞察
     * 
     * @param versionId 版本ID
     * @param insightType 洞察类型
     * @param organizationId 组织ID
     * @return 分析洞察
     */
    Map<String, Object> getBudgetAnalysisInsights(Long versionId, String insightType, 
                                                 Long organizationId);

    /**
     * 获取预算分析建议
     * 
     * @param versionId 版本ID
     * @param recommendationType 建议类型
     * @param organizationId 组织ID
     * @return 分析建议列表
     */
    List<Map<String, Object>> getBudgetAnalysisRecommendations(Long versionId, String recommendationType,
                                                              Long organizationId);

    /**
     * 执行自定义分析
     * 
     * @param customParams 自定义参数
     * @return 分析结果
     */
    Map<String, Object> executeCustomAnalysis(Map<String, Object> customParams);

    /**
     * 获取预算分析历史
     * 
     * @param versionId 版本ID
     * @param analysisType 分析类型
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 分析历史列表
     */
    List<Map<String, Object>> getBudgetAnalysisHistory(Long versionId, String analysisType,
                                                      String startDate, String endDate);

    /**
     * 刷新预算分析缓存
     * 
     * @param versionId 版本ID
     * @param cacheType 缓存类型
     * @return 刷新结果
     */
    boolean refreshBudgetAnalysisCache(Long versionId, String cacheType);

    /**
     * 计算预算执行率
     * 
     * @param versionId 版本ID
     * @param organizationId 组织ID
     * @param subjectId 科目ID
     * @param period 期间
     * @return 执行率数据
     */
    Map<String, Object> calculateBudgetExecutionRate(Long versionId, Long organizationId,
                                                    Long subjectId, String period);

    /**
     * 计算预算偏差率
     * 
     * @param versionId 版本ID
     * @param organizationId 组织ID
     * @param subjectId 科目ID
     * @param period 期间
     * @return 偏差率数据
     */
    Map<String, Object> calculateBudgetDeviationRate(Long versionId, Long organizationId,
                                                    Long subjectId, String period);

    /**
     * 分析预算增长趋势
     * 
     * @param versionId 版本ID
     * @param organizationId 组织ID
     * @param subjectId 科目ID
     * @param periods 分析周期数
     * @return 增长趋势数据
     */
    Map<String, Object> analyzeBudgetGrowthTrend(Long versionId, Long organizationId,
                                               Long subjectId, Integer periods);

    /**
     * 预测预算趋势
     * 
     * @param versionId 版本ID
     * @param organizationId 组织ID
     * @param subjectId 科目ID
     * @param forecastPeriods 预测周期数
     * @return 预测数据
     */
    Map<String, Object> forecastBudgetTrend(Long versionId, Long organizationId,
                                          Long subjectId, Integer forecastPeriods);

    /**
     * 分析预算集中度
     * 
     * @param versionId 版本ID
     * @param analysisDimension 分析维度
     * @return 集中度分析数据
     */
    Map<String, Object> analyzeBudgetConcentration(Long versionId, String analysisDimension);

    /**
     * 分析预算分布
     * 
     * @param versionId 版本ID
     * @param distributionDimension 分布维度
     * @return 分布分析数据
     */
    Map<String, Object> analyzeBudgetDistribution(Long versionId, String distributionDimension);

    /**
     * 计算预算相关性
     * 
     * @param versionId 版本ID
     * @param correlationParams 相关性参数
     * @return 相关性分析数据
     */
    Map<String, Object> calculateBudgetCorrelation(Long versionId, Map<String, Object> correlationParams);

    /**
     * 分析预算敏感性
     * 
     * @param versionId 版本ID
     * @param sensitivityParams 敏感性参数
     * @return 敏感性分析数据
     */
    Map<String, Object> analyzeBudgetSensitivity(Long versionId, Map<String, Object> sensitivityParams);

    /**
     * 进行预算情景分析
     * 
     * @param versionId 版本ID
     * @param scenarioParams 情景参数
     * @return 情景分析数据
     */
    Map<String, Object> performBudgetScenarioAnalysis(Long versionId, Map<String, Object> scenarioParams);

    /**
     * 分析预算风险
     * 
     * @param versionId 版本ID
     * @param riskParams 风险参数
     * @return 风险分析数据
     */
    Map<String, Object> analyzeBudgetRisk(Long versionId, Map<String, Object> riskParams);

    /**
     * 优化预算配置
     * 
     * @param versionId 版本ID
     * @param optimizationParams 优化参数
     * @return 优化建议数据
     */
    Map<String, Object> optimizeBudgetAllocation(Long versionId, Map<String, Object> optimizationParams);

    /**
     * 生成预算分析摘要
     * 
     * @param versionId 版本ID
     * @param summaryParams 摘要参数
     * @return 分析摘要数据
     */
    Map<String, Object> generateBudgetAnalysisSummary(Long versionId, Map<String, Object> summaryParams);

    /**
     * 计算预算绩效指标
     * 
     * @param versionId 版本ID
     * @param performanceParams 绩效参数
     * @return 绩效指标数据
     */
    Map<String, Object> calculateBudgetPerformanceMetrics(Long versionId, Map<String, Object> performanceParams);

    /**
     * 分析预算效率
     * 
     * @param versionId 版本ID
     * @param efficiencyParams 效率参数
     * @return 效率分析数据
     */
    Map<String, Object> analyzeBudgetEfficiency(Long versionId, Map<String, Object> efficiencyParams);

    /**
     * 评估预算质量
     * 
     * @param versionId 版本ID
     * @param qualityParams 质量参数
     * @return 质量评估数据
     */
    Map<String, Object> evaluateBudgetQuality(Long versionId, Map<String, Object> qualityParams);

    /**
     * 监控预算健康度
     * 
     * @param versionId 版本ID
     * @param healthParams 健康度参数
     * @return 健康度监控数据
     */
    Map<String, Object> monitorBudgetHealth(Long versionId, Map<String, Object> healthParams);
}
