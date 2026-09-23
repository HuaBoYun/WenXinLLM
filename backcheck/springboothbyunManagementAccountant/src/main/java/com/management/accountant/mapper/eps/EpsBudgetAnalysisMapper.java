package com.management.accountant.mapper.eps;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 预算分析数据访问接口
 * 
 * @author 华博云
 * @version 3.0.0
 */
@Mapper
public interface EpsBudgetAnalysisMapper {

    /**
     * 获取预算执行分析数据
     * 
     * @param versionId 版本ID
     * @param organizationId 组织ID
     * @param subjectId 科目ID
     * @param analysisPeriod 分析期间
     * @param analysisDimension 分析维度
     * @return 执行分析数据
     */
    List<Map<String, Object>> selectBudgetExecutionAnalysis(@Param("versionId") Long versionId,
                                                           @Param("organizationId") Long organizationId,
                                                           @Param("subjectId") Long subjectId,
                                                           @Param("analysisPeriod") String analysisPeriod,
                                                           @Param("analysisDimension") String analysisDimension);

    /**
     * 获取预算差异分析数据
     * 
     * @param versionId 版本ID
     * @param compareVersionId 对比版本ID
     * @param organizationId 组织ID
     * @param subjectId 科目ID
     * @param varianceType 差异类型
     * @return 差异分析数据
     */
    List<Map<String, Object>> selectBudgetVarianceAnalysis(@Param("versionId") Long versionId,
                                                          @Param("compareVersionId") Long compareVersionId,
                                                          @Param("organizationId") Long organizationId,
                                                          @Param("subjectId") Long subjectId,
                                                          @Param("varianceType") String varianceType);

    /**
     * 获取预算趋势分析数据
     * 
     * @param versionId 版本ID
     * @param organizationId 组织ID
     * @param subjectId 科目ID
     * @param trendPeriods 趋势周期
     * @return 趋势分析数据
     */
    List<Map<String, Object>> selectBudgetTrendAnalysis(@Param("versionId") Long versionId,
                                                       @Param("organizationId") Long organizationId,
                                                       @Param("subjectId") Long subjectId,
                                                       @Param("trendPeriods") Integer trendPeriods);

    /**
     * 获取预算结构分析数据
     * 
     * @param versionId 版本ID
     * @param analysisDimension 分析维度
     * @param organizationId 组织ID
     * @param hierarchyDepth 层级深度
     * @return 结构分析数据
     */
    List<Map<String, Object>> selectBudgetStructureAnalysis(@Param("versionId") Long versionId,
                                                           @Param("analysisDimension") String analysisDimension,
                                                           @Param("organizationId") Long organizationId,
                                                           @Param("hierarchyDepth") Integer hierarchyDepth);

    /**
     * 获取预算对比分析数据
     * 
     * @param sourceVersionId 源版本ID
     * @param targetVersionId 目标版本ID
     * @param comparisonDimension 对比维度
     * @param organizationId 组织ID
     * @param subjectId 科目ID
     * @return 对比分析数据
     */
    List<Map<String, Object>> selectBudgetComparisonAnalysis(@Param("sourceVersionId") Long sourceVersionId,
                                                            @Param("targetVersionId") Long targetVersionId,
                                                            @Param("comparisonDimension") String comparisonDimension,
                                                            @Param("organizationId") Long organizationId,
                                                            @Param("subjectId") Long subjectId);

    /**
     * 获取预算完成率分析数据
     * 
     * @param versionId 版本ID
     * @param organizationId 组织ID
     * @param subjectId 科目ID
     * @param statisticsPeriod 统计周期
     * @return 完成率分析数据
     */
    List<Map<String, Object>> selectBudgetCompletionRateAnalysis(@Param("versionId") Long versionId,
                                                                @Param("organizationId") Long organizationId,
                                                                @Param("subjectId") Long subjectId,
                                                                @Param("statisticsPeriod") String statisticsPeriod);

    /**
     * 获取预算排名分析数据
     * 
     * @param versionId 版本ID
     * @param rankingDimension 排名维度
     * @param rankingMetric 排名指标
     * @param rankingLimit 排名数量
     * @return 排名分析数据
     */
    List<Map<String, Object>> selectBudgetRankingAnalysis(@Param("versionId") Long versionId,
                                                         @Param("rankingDimension") String rankingDimension,
                                                         @Param("rankingMetric") String rankingMetric,
                                                         @Param("rankingLimit") Integer rankingLimit);

    /**
     * 检测预算异常数据
     * 
     * @param versionId 版本ID
     * @param anomalyType 异常类型
     * @param anomalyThreshold 异常阈值
     * @param organizationId 组织ID
     * @return 异常数据列表
     */
    List<Map<String, Object>> detectBudgetAnomalies(@Param("versionId") Long versionId,
                                                   @Param("anomalyType") String anomalyType,
                                                   @Param("anomalyThreshold") Double anomalyThreshold,
                                                   @Param("organizationId") Long organizationId);

    /**
     * 获取预算分析仪表板数据
     * 
     * @param versionId 版本ID
     * @param organizationId 组织ID
     * @param dashboardType 仪表板类型
     * @return 仪表板数据
     */
    Map<String, Object> selectBudgetAnalysisDashboard(@Param("versionId") Long versionId,
                                                     @Param("organizationId") Long organizationId,
                                                     @Param("dashboardType") String dashboardType);

    /**
     * 获取预算分析图表数据
     * 
     * @param versionId 版本ID
     * @param chartType 图表类型
     * @param chartParams 图表参数
     * @return 图表数据
     */
    List<Map<String, Object>> selectBudgetAnalysisChartData(@Param("versionId") Long versionId,
                                                           @Param("chartType") String chartType,
                                                           @Param("chartParams") String chartParams);

    /**
     * 保存预算分析配置
     * 
     * @param analysisConfig 分析配置
     * @return 保存行数
     */
    int saveBudgetAnalysisConfig(@Param("analysisConfig") Map<String, Object> analysisConfig);

    /**
     * 获取预算分析配置
     * 
     * @param versionId 版本ID
     * @param configType 配置类型
     * @return 分析配置
     */
    Map<String, Object> selectBudgetAnalysisConfig(@Param("versionId") Long versionId,
                                                  @Param("configType") String configType);

    /**
     * 获取预算分析模板
     * 
     * @param templateType 模板类型
     * @return 分析模板列表
     */
    List<Map<String, Object>> selectBudgetAnalysisTemplates(@Param("templateType") String templateType);

    /**
     * 应用预算分析模板
     * 
     * @param templateParams 模板参数
     * @return 应用结果
     */
    int applyBudgetAnalysisTemplate(@Param("templateParams") Map<String, Object> templateParams);

    /**
     * 获取预算分析洞察
     * 
     * @param versionId 版本ID
     * @param insightType 洞察类型
     * @param organizationId 组织ID
     * @return 分析洞察
     */
    List<Map<String, Object>> selectBudgetAnalysisInsights(@Param("versionId") Long versionId,
                                                          @Param("insightType") String insightType,
                                                          @Param("organizationId") Long organizationId);

    /**
     * 获取预算分析建议
     * 
     * @param versionId 版本ID
     * @param recommendationType 建议类型
     * @param organizationId 组织ID
     * @return 分析建议列表
     */
    List<Map<String, Object>> selectBudgetAnalysisRecommendations(@Param("versionId") Long versionId,
                                                                 @Param("recommendationType") String recommendationType,
                                                                 @Param("organizationId") Long organizationId);

    /**
     * 执行自定义分析
     * 
     * @param customParams 自定义参数
     * @return 分析结果
     */
    List<Map<String, Object>> executeCustomAnalysis(@Param("customParams") Map<String, Object> customParams);

    /**
     * 获取预算分析历史
     * 
     * @param versionId 版本ID
     * @param analysisType 分析类型
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 分析历史列表
     */
    List<Map<String, Object>> selectBudgetAnalysisHistory(@Param("versionId") Long versionId,
                                                         @Param("analysisType") String analysisType,
                                                         @Param("startDate") String startDate,
                                                         @Param("endDate") String endDate);

    /**
     * 保存预算分析历史记录
     * 
     * @param historyRecord 历史记录
     * @return 保存行数
     */
    int insertBudgetAnalysisHistory(@Param("historyRecord") Map<String, Object> historyRecord);

    /**
     * 计算预算执行率
     * 
     * @param versionId 版本ID
     * @param organizationId 组织ID
     * @param subjectId 科目ID
     * @param period 期间
     * @return 执行率数据
     */
    Map<String, Object> calculateBudgetExecutionRate(@Param("versionId") Long versionId,
                                                    @Param("organizationId") Long organizationId,
                                                    @Param("subjectId") Long subjectId,
                                                    @Param("period") String period);

    /**
     * 计算预算偏差率
     * 
     * @param versionId 版本ID
     * @param organizationId 组织ID
     * @param subjectId 科目ID
     * @param period 期间
     * @return 偏差率数据
     */
    Map<String, Object> calculateBudgetDeviationRate(@Param("versionId") Long versionId,
                                                    @Param("organizationId") Long organizationId,
                                                    @Param("subjectId") Long subjectId,
                                                    @Param("period") String period);

    /**
     * 分析预算增长趋势
     * 
     * @param versionId 版本ID
     * @param organizationId 组织ID
     * @param subjectId 科目ID
     * @param periods 分析周期数
     * @return 增长趋势数据
     */
    List<Map<String, Object>> analyzeBudgetGrowthTrend(@Param("versionId") Long versionId,
                                                      @Param("organizationId") Long organizationId,
                                                      @Param("subjectId") Long subjectId,
                                                      @Param("periods") Integer periods);

    /**
     * 预测预算趋势
     * 
     * @param versionId 版本ID
     * @param organizationId 组织ID
     * @param subjectId 科目ID
     * @param forecastPeriods 预测周期数
     * @return 预测数据
     */
    List<Map<String, Object>> forecastBudgetTrend(@Param("versionId") Long versionId,
                                                 @Param("organizationId") Long organizationId,
                                                 @Param("subjectId") Long subjectId,
                                                 @Param("forecastPeriods") Integer forecastPeriods);

    /**
     * 分析预算集中度
     * 
     * @param versionId 版本ID
     * @param analysisDimension 分析维度
     * @return 集中度分析数据
     */
    Map<String, Object> analyzeBudgetConcentration(@Param("versionId") Long versionId,
                                                  @Param("analysisDimension") String analysisDimension);

    /**
     * 分析预算分布
     * 
     * @param versionId 版本ID
     * @param distributionDimension 分布维度
     * @return 分布分析数据
     */
    List<Map<String, Object>> analyzeBudgetDistribution(@Param("versionId") Long versionId,
                                                       @Param("distributionDimension") String distributionDimension);

    /**
     * 计算预算相关性
     * 
     * @param versionId 版本ID
     * @param correlationParams 相关性参数
     * @return 相关性分析数据
     */
    List<Map<String, Object>> calculateBudgetCorrelation(@Param("versionId") Long versionId,
                                                        @Param("correlationParams") Map<String, Object> correlationParams);

    /**
     * 分析预算敏感性
     * 
     * @param versionId 版本ID
     * @param sensitivityParams 敏感性参数
     * @return 敏感性分析数据
     */
    List<Map<String, Object>> analyzeBudgetSensitivity(@Param("versionId") Long versionId,
                                                      @Param("sensitivityParams") Map<String, Object> sensitivityParams);

    /**
     * 进行预算情景分析
     * 
     * @param versionId 版本ID
     * @param scenarioParams 情景参数
     * @return 情景分析数据
     */
    List<Map<String, Object>> performBudgetScenarioAnalysis(@Param("versionId") Long versionId,
                                                           @Param("scenarioParams") Map<String, Object> scenarioParams);

    /**
     * 分析预算风险
     * 
     * @param versionId 版本ID
     * @param riskParams 风险参数
     * @return 风险分析数据
     */
    List<Map<String, Object>> analyzeBudgetRisk(@Param("versionId") Long versionId,
                                               @Param("riskParams") Map<String, Object> riskParams);

    /**
     * 优化预算配置
     * 
     * @param versionId 版本ID
     * @param optimizationParams 优化参数
     * @return 优化建议数据
     */
    List<Map<String, Object>> optimizeBudgetAllocation(@Param("versionId") Long versionId,
                                                      @Param("optimizationParams") Map<String, Object> optimizationParams);

    /**
     * 生成预算分析摘要
     * 
     * @param versionId 版本ID
     * @param summaryParams 摘要参数
     * @return 分析摘要数据
     */
    Map<String, Object> generateBudgetAnalysisSummary(@Param("versionId") Long versionId,
                                                     @Param("summaryParams") Map<String, Object> summaryParams);

    /**
     * 计算预算绩效指标
     * 
     * @param versionId 版本ID
     * @param performanceParams 绩效参数
     * @return 绩效指标数据
     */
    List<Map<String, Object>> calculateBudgetPerformanceMetrics(@Param("versionId") Long versionId,
                                                               @Param("performanceParams") Map<String, Object> performanceParams);

    /**
     * 分析预算效率
     * 
     * @param versionId 版本ID
     * @param efficiencyParams 效率参数
     * @return 效率分析数据
     */
    List<Map<String, Object>> analyzeBudgetEfficiency(@Param("versionId") Long versionId,
                                                     @Param("efficiencyParams") Map<String, Object> efficiencyParams);

    /**
     * 评估预算质量
     * 
     * @param versionId 版本ID
     * @param qualityParams 质量参数
     * @return 质量评估数据
     */
    Map<String, Object> evaluateBudgetQuality(@Param("versionId") Long versionId,
                                             @Param("qualityParams") Map<String, Object> qualityParams);

    /**
     * 监控预算健康度
     * 
     * @param versionId 版本ID
     * @param healthParams 健康度参数
     * @return 健康度监控数据
     */
    Map<String, Object> monitorBudgetHealth(@Param("versionId") Long versionId,
                                           @Param("healthParams") Map<String, Object> healthParams);

    /**
     * 刷新预算分析缓存
     * 
     * @param versionId 版本ID
     * @param cacheType 缓存类型
     * @return 刷新结果
     */
    int refreshBudgetAnalysisCache(@Param("versionId") Long versionId,
                                  @Param("cacheType") String cacheType);

    /**
     * 获取预算分析统计信息
     * 
     * @param versionId 版本ID
     * @return 统计信息
     */
    Map<String, Object> selectBudgetAnalysisStatistics(@Param("versionId") Long versionId);

    /**
     * 获取预算分析性能指标
     * 
     * @param versionId 版本ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 性能指标
     */
    Map<String, Object> selectBudgetAnalysisPerformance(@Param("versionId") Long versionId,
                                                       @Param("startDate") String startDate,
                                                       @Param("endDate") String endDate);

    /**
     * 清理预算分析缓存
     * 
     * @param versionId 版本ID
     * @return 清理结果
     */
    int cleanupBudgetAnalysisCache(@Param("versionId") Long versionId);

    /**
     * 重建预算分析索引
     * 
     * @param versionId 版本ID
     * @return 重建结果
     */
    int rebuildBudgetAnalysisIndex(@Param("versionId") Long versionId);
}
