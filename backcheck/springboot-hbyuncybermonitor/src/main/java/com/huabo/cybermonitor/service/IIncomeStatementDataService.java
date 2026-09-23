package com.huabo.cybermonitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.cybermonitor.entity.IncomeStatementData;
import com.huabo.cybermonitor.vo.IncomeStatementDataQueryVO;

import java.util.List;
import java.util.Map;

/**
 * 利润表数据服务接口
 * 
 * @author 华博云AI助手
 * @since 2024-12-12
 */
public interface IIncomeStatementDataService extends IService<IncomeStatementData> {

    // ==================== 基础CRUD操作 ====================

    /**
     * 分页查询利润表数据列表
     */
    Map<String, Object> selectIncomeStatementDataList(IncomeStatementDataQueryVO queryVO);

    /**
     * 根据ID查询利润表数据详情
     */
    IncomeStatementData selectIncomeStatementDataById(String incomeStatementId);

    /**
     * 新增利润表数据
     */
    boolean insertIncomeStatementData(IncomeStatementData incomeStatementData);

    /**
     * 修改利润表数据
     */
    boolean updateIncomeStatementData(IncomeStatementData incomeStatementData);

    /**
     * 删除利润表数据
     */
    boolean deleteIncomeStatementDataById(String incomeStatementId);

    /**
     * 批量删除利润表数据
     */
    boolean deleteIncomeStatementDataByIds(List<String> incomeStatementIds);

    // ==================== 业务查询方法 ====================

    /**
     * 根据企业ID查询利润表数据
     */
    List<IncomeStatementData> selectByEnterpriseId(String enterpriseId);

    /**
     * 根据报表年度查询
     */
    List<IncomeStatementData> selectByReportYear(Integer reportYear);

    /**
     * 查询最新利润表数据
     */
    List<IncomeStatementData> selectLatestIncomeStatementData(String enterpriseId, Integer limit);

    // ==================== 盈利能力分析方法 ====================

    /**
     * 盈利能力综合分析
     */
    Map<String, Object> analyzeProfitability(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 收入结构分析
     */
    Map<String, Object> analyzeRevenueStructure(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 成本结构分析
     */
    Map<String, Object> analyzeCostStructure(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 费用结构分析
     */
    Map<String, Object> analyzeExpenseStructure(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 利润结构分析
     */
    Map<String, Object> analyzeProfitStructure(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 毛利率分析
     */
    Map<String, Object> analyzeGrossProfitMargin(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 净利润率分析
     */
    Map<String, Object> analyzeNetProfitMargin(String enterpriseId, Integer startYear, Integer endYear);

    // ==================== 收入分析方法 ====================

    /**
     * 营业收入综合分析
     */
    Map<String, Object> analyzeOperatingRevenue(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 收入增长分析
     */
    Map<String, Object> analyzeRevenueGrowth(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 收入季节性分析
     */
    Map<String, Object> analyzeRevenueSeasonality(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 其他收益分析
     */
    Map<String, Object> analyzeOtherIncome(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 投资收益分析
     */
    Map<String, Object> analyzeInvestmentIncome(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 收入质量评估
     */
    Map<String, Object> assessRevenueQuality(String enterpriseId, Integer reportYear);

    // ==================== 成本费用分析方法 ====================

    /**
     * 营业成本分析
     */
    Map<String, Object> analyzeOperatingCost(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 期间费用分析
     */
    Map<String, Object> analyzePeriodExpense(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 销售费用分析
     */
    Map<String, Object> analyzeSellingExpense(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 管理费用分析
     */
    Map<String, Object> analyzeAdministrativeExpense(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 研发费用分析
     */
    Map<String, Object> analyzeRDExpense(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 财务费用分析
     */
    Map<String, Object> analyzeFinancialExpense(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 成本费用控制分析
     */
    Map<String, Object> analyzeCostExpenseControl(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 成本费用优化建议
     */
    Map<String, Object> getCostExpenseOptimizationAdvice(String enterpriseId, Integer reportYear);

    // ==================== 利润分析方法 ====================

    /**
     * 营业利润分析
     */
    Map<String, Object> analyzeOperatingProfit(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 利润总额分析
     */
    Map<String, Object> analyzeTotalProfit(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 净利润分析
     */
    Map<String, Object> analyzeNetProfit(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 利润增长分析
     */
    Map<String, Object> analyzeProfitGrowth(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 利润质量分析
     */
    Map<String, Object> analyzeProfitQuality(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 每股收益分析
     */
    Map<String, Object> analyzeEarningsPerShare(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 利润分配分析
     */
    Map<String, Object> analyzeProfitDistribution(String enterpriseId, Integer startYear, Integer endYear);

    // ==================== 其他综合收益分析方法 ====================

    /**
     * 其他综合收益分析
     */
    Map<String, Object> analyzeOtherComprehensiveIncome(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 综合收益总额分析
     */
    Map<String, Object> analyzeTotalComprehensiveIncome(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 综合收益结构分析
     */
    Map<String, Object> analyzeComprehensiveIncomeStructure(String enterpriseId, Integer startYear, Integer endYear);

    // ==================== 财务比率分析方法 ====================

    /**
     * 盈利能力比率分析
     */
    Map<String, Object> analyzeProfitabilityRatio(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 费用率分析
     */
    Map<String, Object> analyzeExpenseRatio(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 税负分析
     */
    Map<String, Object> analyzeTaxBurden(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 财务比率预警
     */
    Map<String, Object> getFinancialRatioWarning(String enterpriseId, Integer reportYear);

    // ==================== 趋势分析方法 ====================

    /**
     * 收入趋势分析
     */
    Map<String, Object> analyzeRevenueTrend(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 利润趋势分析
     */
    Map<String, Object> analyzeProfitTrend(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 成本费用趋势分析
     */
    Map<String, Object> analyzeCostExpenseTrend(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 盈利能力趋势分析
     */
    Map<String, Object> analyzeProfitabilityTrend(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 经营效率趋势分析
     */
    Map<String, Object> analyzeOperatingEfficiencyTrend(String enterpriseId, Integer startYear, Integer endYear);

    // ==================== 财务比率计算方法 ====================

    /**
     * 自动计算财务比率
     */
    IncomeStatementData calculateFinancialRatios(String incomeStatementId);

    /**
     * 批量计算财务比率
     */
    Map<String, Object> batchCalculateFinancialRatios(List<String> enterpriseIds, Integer reportYear, Integer reportPeriod);

    /**
     * 重新计算财务比率
     */
    boolean recalculateFinancialRatios(String incomeStatementId);

    // ==================== 统计分析方法 ====================

    /**
     * 按数据状态统计
     */
    List<Map<String, Object>> getDataStatusStatistics(Integer startYear, Integer endYear);

    /**
     * 收入规模分布统计
     */
    List<Map<String, Object>> getRevenueScaleDistributionStatistics(Integer startYear, Integer endYear);

    /**
     * 利润水平分布统计
     */
    List<Map<String, Object>> getProfitLevelDistributionStatistics(Integer startYear, Integer endYear);

    /**
     * 盈利能力分布统计
     */
    List<Map<String, Object>> getProfitabilityDistributionStatistics(Integer startYear, Integer endYear);

    /**
     * 利润表统计概览
     */
    Map<String, Object> getIncomeStatementStatisticsOverview();

    // ==================== 批量操作方法 ====================

    /**
     * 批量更新数据状态
     */
    boolean batchUpdateDataStatus(List<String> incomeStatementIds, String dataStatus);

    /**
     * 批量更新审核状态
     */
    boolean batchUpdateAuditStatus(List<String> incomeStatementIds, String auditStatus);

    /**
     * 批量导入利润表数据
     */
    Map<String, Object> batchImportIncomeStatementData(List<IncomeStatementData> incomeStatementDataList);

    // ==================== 导出功能方法 ====================

    /**
     * 导出利润表数据列表
     */
    List<Map<String, Object>> exportIncomeStatementDataList(IncomeStatementDataQueryVO queryVO);

    /**
     * 导出盈利能力分析报告
     */
    Map<String, Object> exportProfitabilityAnalysisReport(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 导出成本费用分析报告
     */
    Map<String, Object> exportCostExpenseAnalysisReport(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 导出利润质量分析报告
     */
    Map<String, Object> exportProfitQualityAnalysisReport(String enterpriseId, Integer startYear, Integer endYear);

    // ==================== 标签转换方法 ====================

    /**
     * 获取数据状态标签
     */
    String getDataStatusLabel(String dataStatus);

    /**
     * 获取数据来源标签
     */
    String getDataSourceLabel(String dataSource);

    /**
     * 获取审核状态标签
     */
    String getAuditStatusLabel(String auditStatus);

    // ==================== 数据维护方法 ====================

    /**
     * 删除过期利润表记录
     */
    int deleteExpiredIncomeStatementRecords(Integer days);

    /**
     * 数据清理和优化
     */
    Map<String, Object> cleanAndOptimizeData();

    /**
     * 重新计算所有比率
     */
    boolean recalculateAllRatios(List<String> incomeStatementIds);

}
