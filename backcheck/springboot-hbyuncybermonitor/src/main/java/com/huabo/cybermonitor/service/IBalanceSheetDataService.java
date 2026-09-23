package com.huabo.cybermonitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.cybermonitor.entity.BalanceSheetData;
import com.huabo.cybermonitor.vo.BalanceSheetDataQueryVO;

import java.util.List;
import java.util.Map;

/**
 * 资产负债表数据服务接口
 * 
 * @author 华博云AI助手
 * @since 2024-12-12
 */
public interface IBalanceSheetDataService extends IService<BalanceSheetData> {

    // ==================== 基础CRUD操作 ====================

    /**
     * 分页查询资产负债表数据列表
     */
    Map<String, Object> selectBalanceSheetDataList(BalanceSheetDataQueryVO queryVO);

    /**
     * 根据ID查询资产负债表数据详情
     */
    BalanceSheetData selectBalanceSheetDataById(String balanceSheetId);

    /**
     * 新增资产负债表数据
     */
    boolean insertBalanceSheetData(BalanceSheetData balanceSheetData);

    /**
     * 修改资产负债表数据
     */
    boolean updateBalanceSheetData(BalanceSheetData balanceSheetData);

    /**
     * 删除资产负债表数据
     */
    boolean deleteBalanceSheetDataById(String balanceSheetId);

    /**
     * 批量删除资产负债表数据
     */
    boolean deleteBalanceSheetDataByIds(List<String> balanceSheetIds);

    // ==================== 业务查询方法 ====================

    /**
     * 根据企业ID查询资产负债表数据
     */
    List<BalanceSheetData> selectByEnterpriseId(String enterpriseId);

    /**
     * 根据报表年度查询
     */
    List<BalanceSheetData> selectByReportYear(Integer reportYear);

    /**
     * 查询最新资产负债表数据
     */
    List<BalanceSheetData> selectLatestBalanceSheetData(String enterpriseId, Integer limit);

    // ==================== 资产结构分析方法 ====================

    /**
     * 资产结构综合分析
     */
    Map<String, Object> analyzeAssetStructure(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 流动资产分析
     */
    Map<String, Object> analyzeCurrentAsset(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 非流动资产分析
     */
    Map<String, Object> analyzeNonCurrentAsset(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 固定资产分析
     */
    Map<String, Object> analyzeFixedAsset(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 无形资产分析
     */
    Map<String, Object> analyzeIntangibleAsset(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 投资性资产分析
     */
    Map<String, Object> analyzeInvestmentAsset(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 资产配置优化建议
     */
    Map<String, Object> getAssetAllocationOptimizationAdvice(String enterpriseId, Integer reportYear);

    // ==================== 负债结构分析方法 ====================

    /**
     * 负债结构综合分析
     */
    Map<String, Object> analyzeLiabilityStructure(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 流动负债分析
     */
    Map<String, Object> analyzeCurrentLiability(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 非流动负债分析
     */
    Map<String, Object> analyzeNonCurrentLiability(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 借款结构分析
     */
    Map<String, Object> analyzeBorrowingStructure(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 债务期限结构分析
     */
    Map<String, Object> analyzeDebtMaturityStructure(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 债务管理优化建议
     */
    Map<String, Object> getDebtManagementOptimizationAdvice(String enterpriseId, Integer reportYear);

    // ==================== 所有者权益分析方法 ====================

    /**
     * 所有者权益结构分析
     */
    Map<String, Object> analyzeOwnersEquityStructure(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 股本结构分析
     */
    Map<String, Object> analyzeShareCapitalStructure(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 留存收益分析
     */
    Map<String, Object> analyzeRetainedEarnings(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 少数股东权益分析
     */
    Map<String, Object> analyzeMinorityInterest(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 权益结构优化建议
     */
    Map<String, Object> getEquityStructureOptimizationAdvice(String enterpriseId, Integer reportYear);

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
     * 流动性风险分析
     */
    Map<String, Object> analyzeLiquidityRisk(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 资本结构分析
     */
    Map<String, Object> analyzeCapitalStructure(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 偿债能力预警
     */
    Map<String, Object> getSolvencyWarning(String enterpriseId, Integer reportYear);

    // ==================== 财务比率分析方法 ====================

    /**
     * 财务比率综合分析
     */
    Map<String, Object> analyzeFinancialRatio(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 资产负债率趋势分析
     */
    Map<String, Object> analyzeAssetLiabilityRatioTrend(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 流动比率趋势分析
     */
    Map<String, Object> analyzeCurrentRatioTrend(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 速动比率趋势分析
     */
    Map<String, Object> analyzeQuickRatioTrend(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 权益乘数趋势分析
     */
    Map<String, Object> analyzeEquityMultiplierTrend(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 财务比率预警
     */
    Map<String, Object> getFinancialRatioWarning(String enterpriseId, Integer reportYear);

    // ==================== 资产质量分析方法 ====================

    /**
     * 资产质量综合分析
     */
    Map<String, Object> analyzeAssetQuality(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 应收账款质量分析
     */
    Map<String, Object> analyzeReceivableQuality(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 存货质量分析
     */
    Map<String, Object> analyzeInventoryQuality(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 固定资产质量分析
     */
    Map<String, Object> analyzeFixedAssetQuality(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 投资资产质量分析
     */
    Map<String, Object> analyzeInvestmentAssetQuality(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 资产减值风险评估
     */
    Map<String, Object> assessAssetImpairmentRisk(String enterpriseId, Integer reportYear);

    // ==================== 财务比率计算方法 ====================

    /**
     * 自动计算财务比率
     */
    BalanceSheetData calculateFinancialRatios(String balanceSheetId);

    /**
     * 批量计算财务比率
     */
    Map<String, Object> batchCalculateFinancialRatios(List<String> enterpriseIds, Integer reportYear, Integer reportPeriod);

    /**
     * 重新计算财务比率
     */
    boolean recalculateFinancialRatios(String balanceSheetId);

    // ==================== 统计分析方法 ====================

    /**
     * 按数据状态统计
     */
    List<Map<String, Object>> getDataStatusStatistics(Integer startYear, Integer endYear);

    /**
     * 资产规模分布统计
     */
    List<Map<String, Object>> getAssetScaleDistributionStatistics(Integer startYear, Integer endYear);

    /**
     * 负债水平分布统计
     */
    List<Map<String, Object>> getLiabilityLevelDistributionStatistics(Integer startYear, Integer endYear);

    /**
     * 资产负债率分布统计
     */
    List<Map<String, Object>> getAssetLiabilityRatioDistributionStatistics(Integer startYear, Integer endYear);

    /**
     * 资产负债表统计概览
     */
    Map<String, Object> getBalanceSheetStatisticsOverview();

    // ==================== 批量操作方法 ====================

    /**
     * 批量更新数据状态
     */
    boolean batchUpdateDataStatus(List<String> balanceSheetIds, String dataStatus);

    /**
     * 批量更新审核状态
     */
    boolean batchUpdateAuditStatus(List<String> balanceSheetIds, String auditStatus);

    /**
     * 批量导入资产负债表数据
     */
    Map<String, Object> batchImportBalanceSheetData(List<BalanceSheetData> balanceSheetDataList);

    // ==================== 导出功能方法 ====================

    /**
     * 导出资产负债表数据列表
     */
    List<Map<String, Object>> exportBalanceSheetDataList(BalanceSheetDataQueryVO queryVO);

    /**
     * 导出资产结构分析报告
     */
    Map<String, Object> exportAssetStructureAnalysisReport(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 导出偿债能力分析报告
     */
    Map<String, Object> exportSolvencyAnalysisReport(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 导出资产质量分析报告
     */
    Map<String, Object> exportAssetQualityAnalysisReport(String enterpriseId, Integer startYear, Integer endYear);

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
     * 删除过期资产负债表记录
     */
    int deleteExpiredBalanceSheetRecords(Integer days);

    /**
     * 数据清理和优化
     */
    Map<String, Object> cleanAndOptimizeData();

    /**
     * 重新计算所有比率
     */
    boolean recalculateAllRatios(List<String> balanceSheetIds);

}
