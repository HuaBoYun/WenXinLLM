package com.huabo.cybermonitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.cybermonitor.entity.FinancialIndicators;
import com.huabo.cybermonitor.vo.FinancialIndicatorsQueryVO;

import java.util.List;
import java.util.Map;

/**
 * 财务指标服务接口
 * 
 * @author 华博云AI助手
 * @since 2024-12-12
 */
public interface IFinancialIndicatorsService extends IService<FinancialIndicators> {

    // ==================== 基础CRUD操作 ====================

    /**
     * 分页查询财务指标列表
     */
    Map<String, Object> selectFinancialIndicatorsList(FinancialIndicatorsQueryVO queryVO);

    /**
     * 根据ID查询财务指标详情
     */
    FinancialIndicators selectFinancialIndicatorsById(String indicatorId);

    /**
     * 新增财务指标
     */
    boolean insertFinancialIndicators(FinancialIndicators financialIndicators);

    /**
     * 修改财务指标
     */
    boolean updateFinancialIndicators(FinancialIndicators financialIndicators);

    /**
     * 删除财务指标
     */
    boolean deleteFinancialIndicatorsById(String indicatorId);

    /**
     * 批量删除财务指标
     */
    boolean deleteFinancialIndicatorsByIds(List<String> indicatorIds);

    // ==================== 业务查询方法 ====================

    /**
     * 根据企业ID查询财务指标
     */
    List<FinancialIndicators> selectByEnterpriseId(String enterpriseId);

    /**
     * 根据报表年度查询
     */
    List<FinancialIndicators> selectByReportYear(Integer reportYear);

    /**
     * 根据预警等级查询
     */
    List<FinancialIndicators> selectByWarningLevel(String warningLevel);

    /**
     * 查询最新财务指标
     */
    List<FinancialIndicators> selectLatestIndicators(String enterpriseId, Integer limit);

    // ==================== 财务指标计算方法 ====================

    /**
     * 自动计算财务指标
     */
    FinancialIndicators calculateFinancialIndicators(String statementId);

    /**
     * 批量计算财务指标
     */
    Map<String, Object> batchCalculateIndicators(List<String> enterpriseIds, Integer reportYear, Integer reportPeriod);

    /**
     * 重新计算财务指标
     */
    boolean recalculateIndicators(String indicatorId);

    /**
     * 计算盈利能力指标
     */
    Map<String, Object> calculateProfitabilityIndicators(String statementId);

    /**
     * 计算偿债能力指标
     */
    Map<String, Object> calculateSolvencyIndicators(String statementId);

    /**
     * 计算运营能力指标
     */
    Map<String, Object> calculateOperatingAbilityIndicators(String statementId);

    /**
     * 计算发展能力指标
     */
    Map<String, Object> calculateDevelopmentAbilityIndicators(String statementId);

   boolean recalculateFinancialIndicators(String indicatorsId);

    List<FinancialIndicators> selectLatestFinancialIndicators(String enterpriseId, Integer limit);

    public FinancialIndicators calculateFinancialIndicators(String enterpriseId, Integer reportYear, Integer reportPeriod);

    Map<String, Object> batchCalculateFinancialIndicators(List<String> enterpriseIds, Integer reportYear, Integer reportPeriod);

    Map<String, Object> performComprehensiveEvaluation(String enterpriseId, Integer startYear, Integer endYear);

    String getIndicatorTypeLabel(String indicatorType);

    String getQualityLevelLabel(String qualityLevel);

    int deleteExpiredIndicatorsRecords(Integer days);
    /**
     * 计算现金流量指标
     */
    Map<String, Object> calculateCashFlowIndicators(String statementId);

    /**
     * 计算市场价值指标
     */
    Map<String, Object> calculateMarketValueIndicators(String statementId);

    // ==================== 盈利能力分析方法 ====================

    /**
     * 盈利能力综合分析
     */
    Map<String, Object> analyzeProfitability(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * ROE分析
     */
    Map<String, Object> analyzeROE(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * ROA分析
     */
    Map<String, Object> analyzeROA(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 利润率分析
     */
    Map<String, Object> analyzeProfitMargin(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 每股收益分析
     */
    Map<String, Object> analyzeEPS(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 盈利能力趋势分析
     */
    Map<String, Object> analyzeProfitabilityTrend(String enterpriseId, Integer startYear, Integer endYear);

    // ==================== 偿债能力分析方法 ====================

    /**
     * 偿债能力综合分析
     */
    Map<String, Object> analyzeSolvency(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 短期偿债能力分析
     */
    Map<String, Object> analyzeShortTermSolvency(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 长期偿债能力分析
     */
    Map<String, Object> analyzeLongTermSolvency(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 资产负债率分析
     */
    Map<String, Object> analyzeAssetLiabilityRatio(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 流动性分析
     */
    Map<String, Object> analyzeLiquidity(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 利息保障倍数分析
     */
    Map<String, Object> analyzeInterestCoverage(String enterpriseId, Integer startYear, Integer endYear);

    // ==================== 运营能力分析方法 ====================

    /**
     * 运营能力综合分析
     */
    Map<String, Object> analyzeOperatingAbility(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 资产周转率分析
     */
    Map<String, Object> analyzeAssetTurnover(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 存货周转分析
     */
    Map<String, Object> analyzeInventoryTurnover(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 应收账款周转分析
     */
    Map<String, Object> analyzeReceivableTurnover(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 现金转换周期分析
     */
    Map<String, Object> analyzeCashConversionCycle(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 运营效率分析
     */
    Map<String, Object> analyzeOperatingEfficiency(String enterpriseId, Integer startYear, Integer endYear);

    // ==================== 发展能力分析方法 ====================

    /**
     * 发展能力综合分析
     */
    Map<String, Object> analyzeDevelopmentAbility(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 增长率分析
     */
    Map<String, Object> analyzeGrowthRate(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 可持续增长率分析
     */
    Map<String, Object> analyzeSustainableGrowthRate(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 技术投入分析
     */
    Map<String, Object> analyzeTechnologyInvestment(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 发展潜力评估
     */
    Map<String, Object> assessDevelopmentPotential(String enterpriseId, Integer startYear, Integer endYear);

    // ==================== 现金流量分析方法 ====================

    /**
     * 现金流量综合分析
     */
    Map<String, Object> analyzeCashFlow(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 现金流量质量分析
     */
    Map<String, Object> analyzeCashFlowQuality(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 现金流量充足性分析
     */
    Map<String, Object> analyzeCashFlowAdequacy(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 现金流量稳定性分析
     */
    Map<String, Object> analyzeCashFlowStability(String enterpriseId, Integer startYear, Integer endYear);

    // ==================== 综合评价分析方法 ====================

    /**
     * 综合财务评价
     */
    Map<String, Object> evaluateComprehensiveFinancial(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 财务绩效评价
     */
    Map<String, Object> evaluateFinancialPerformance(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 同业对比分析
     */
    Map<String, Object> analyzeIndustryComparison(String enterpriseId, String industryCode, Integer reportYear);

    /**
     * 历史趋势分析
     */
    Map<String, Object> analyzeHistoricalTrend(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 财务预警分析
     */
    Map<String, Object> analyzeFinancialWarning(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 综合评分计算
     */
    Map<String, Object> calculateComprehensiveScore(String indicatorId);

    // ==================== 风险评估方法 ====================

    /**
     * 财务风险评估
     */
    Map<String, Object> assessFinancialRisk(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 杠杆风险分析
     */
    Map<String, Object> analyzeLeverageRisk(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 市场风险分析
     */
    Map<String, Object> analyzeMarketRisk(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 信用风险分析
     */
    Map<String, Object> analyzeCreditRisk(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 风险预警设置
     */
    boolean setRiskWarning(String indicatorId, String warningLevel, String warningReason);

    /**
     * 批量风险评估
     */
    Map<String, Object> batchAssessRisk(List<String> enterpriseIds, Integer reportYear);

    // ==================== 统计分析方法 ====================

    /**
     * 按预警等级统计
     */
    List<Map<String, Object>> getWarningLevelStatistics(Integer startYear, Integer endYear);

    /**
     * 按行业对比结果统计
     */
    List<Map<String, Object>> getIndustryComparisonStatistics(Integer startYear, Integer endYear);

    /**
     * 财务指标分布统计
     */
    List<Map<String, Object>> getIndicatorDistributionStatistics(String indicatorType, Integer startYear, Integer endYear);

    /**
     * 财务指标趋势统计
     */
    List<Map<String, Object>> getIndicatorTrendStatistics(String enterpriseId, String indicatorType, Integer startYear, Integer endYear);

    /**
     * 行业排名统计
     */
    List<Map<String, Object>> getIndustryRankingStatistics(String industryCode, Integer reportYear);

    /**
     * 财务指标统计概览
     */
    Map<String, Object> getFinancialIndicatorsStatisticsOverview();

    // ==================== 批量操作方法 ====================

    /**
     * 批量更新预警等级
     */
    boolean batchUpdateWarningLevel(List<String> indicatorIds, String warningLevel);

    /**
     * 批量更新数据状态
     */
    boolean batchUpdateDataStatus(List<String> indicatorIds, String dataStatus);

    /**
     * 批量更新审核状态
     */
    boolean batchUpdateAuditStatus(List<String> indicatorIds, String auditStatus);

    /**
     * 批量导入财务指标
     */
    Map<String, Object> batchImportFinancialIndicators(List<FinancialIndicators> indicators);

    // ==================== 导出功能方法 ====================

    /**
     * 导出财务指标列表
     */
    List<Map<String, Object>> exportFinancialIndicatorsList(FinancialIndicatorsQueryVO queryVO);

    /**
     * 导出财务分析报告
     */
    Map<String, Object> exportFinancialAnalysisReport(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 导出同业对比报告
     */
    Map<String, Object> exportIndustryComparisonReport(String enterpriseId, String industryCode, Integer reportYear);

    /**
     * 导出风险评估报告
     */
    Map<String, Object> exportRiskAssessmentReport(String enterpriseId, Integer startYear, Integer endYear);

    // ==================== 标签转换方法 ====================

    /**
     * 获取预警等级标签
     */
    String getWarningLevelLabel(String warningLevel);

    /**
     * 获取数据状态标签
     */
    String getDataStatusLabel(String dataStatus);

    /**
     * 获取计算方法标签
     */
    String getCalculationMethodLabel(String calculationMethod);

    /**
     * 获取审核状态标签
     */
    String getAuditStatusLabel(String auditStatus);

    /**
     * 获取行业对比标签
     */
    String getIndustryComparisonLabel(String industryComparison);

    // ==================== 数据维护方法 ====================

    /**
     * 删除过期指标记录
     */
    int deleteExpiredIndicatorRecords(Integer days);

    /**
     * 数据清理和优化
     */
    Map<String, Object> cleanAndOptimizeData();

    /**
     * 重新计算所有指标
     */
    boolean recalculateAllIndicators(List<String> indicatorIds);

    /**
     * 重新评估预警等级
     */
    boolean reassessWarningLevels(List<String> indicatorIds);

}
