package com.huabo.cybermonitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.cybermonitor.entity.FinancialStatement;
import com.huabo.cybermonitor.vo.FinancialStatementQueryVO;

import java.util.List;
import java.util.Map;

/**
 * 财务报表服务接口
 * 
 * @author 华博云AI助手
 * @since 2024-12-12
 */
public interface IFinancialStatementService extends IService<FinancialStatement> {

    // ==================== 基础CRUD操作 ====================

    /**
     * 分页查询财务报表列表
     */
    Map<String, Object> selectFinancialStatementList(FinancialStatementQueryVO queryVO);

    /**
     * 根据ID查询财务报表详情
     */
    FinancialStatement selectFinancialStatementById(String statementId);

    /**
     * 新增财务报表
     */
    boolean insertFinancialStatement(FinancialStatement financialStatement);

    /**
     * 修改财务报表
     */
    boolean updateFinancialStatement(FinancialStatement financialStatement);

    /**
     * 删除财务报表
     */
    boolean deleteFinancialStatementById(String statementId);

    /**
     * 批量删除财务报表
     */
    boolean deleteFinancialStatementByIds(List<String> statementIds);

    // ==================== 业务查询方法 ====================

    /**
     * 根据企业ID查询财务报表
     */
    List<FinancialStatement> selectByEnterpriseId(String enterpriseId);

    /**
     * 根据报表类型查询
     */
    List<FinancialStatement> selectByStatementType(String statementType);

    /**
     * 根据报表年度查询
     */
    List<FinancialStatement> selectByReportYear(Integer reportYear);

    /**
     * 根据审计状态查询
     */
    List<FinancialStatement> selectByAuditStatus(String auditStatus);

    /**
     * 查询需要监管关注的报表
     */
    List<FinancialStatement> selectRegulatoryAttentionStatements(Boolean needRegulatoryAttention);

    /**
     * 查询异常报表
     */
    List<FinancialStatement> selectAnomalyStatements(Boolean hasAnomalies);

    /**
     * 查询最新财务报表
     */
    List<FinancialStatement> selectLatestStatements(String enterpriseId, Integer limit);

    // ==================== 数据质量分析方法 ====================

    /**
     * 数据质量综合分析
     */
    Map<String, Object> analyzeDataQuality(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 数据完整性分析
     */
    Map<String, Object> analyzeDataCompleteness(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 数据准确性分析
     */
    Map<String, Object> analyzeDataAccuracy(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 数据及时性分析
     */
    Map<String, Object> analyzeDataTimeliness(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 数据一致性分析
     */
    Map<String, Object> analyzeDataConsistency(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 数据质量评分计算
     */
    Map<String, Object> calculateDataQualityScore(String statementId);

    /**
     * 批量数据质量评估
     */
    Map<String, Object> batchAssessDataQuality(List<String> statementIds);

    // ==================== 财务数据异常检测方法 ====================

    /**
     * 财务数据异常检测
     */
    Map<String, Object> detectFinancialDataAnomalies(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 趋势异常识别
     */
    Map<String, Object> identifyTrendAnomalies(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 数据波动分析
     */
    Map<String, Object> analyzeDataVolatility(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 异常值检测
     */
    Map<String, Object> detectOutliers(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 异常报表标记
     */
    boolean markAnomalyStatement(String statementId, String anomalyDescription);

    /**
     * 批量异常检测
     */
    Map<String, Object> batchDetectAnomalies(List<String> enterpriseIds, Integer reportYear);

    // ==================== 财务风险识别方法 ====================

    /**
     * 财务风险预警分析
     */
    Map<String, Object> analyzeFinancialRiskWarning(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 流动性风险分析
     */
    Map<String, Object> analyzeLiquidityRisk(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 信用风险分析
     */
    Map<String, Object> analyzeCreditRisk(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 市场风险分析
     */
    Map<String, Object> analyzeMarketRisk(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 财务风险等级评估
     */
    Map<String, Object> assessFinancialRiskLevel(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 财务风险传导分析
     */
    Map<String, Object> analyzeFinancialRiskTransmission(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 风险预警设置
     */
    boolean setRiskWarning(String statementId, String riskLevel, String riskFactors);

    // ==================== 合规监管方法 ====================

    /**
     * 财务合规检查
     */
    Map<String, Object> checkFinancialCompliance(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 会计准则合规性检查
     */
    Map<String, Object> checkAccountingStandardsCompliance(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 审计合规性检查
     */
    Map<String, Object> checkAuditCompliance(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 报送合规性检查
     */
    Map<String, Object> checkSubmissionCompliance(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 合规性评分计算
     */
    Map<String, Object> calculateComplianceScore(String statementId);

    /**
     * 批量合规检查
     */
    Map<String, Object> batchCheckCompliance(List<String> statementIds);

    // ==================== 统计分析方法 ====================

    /**
     * 按报表类型统计
     */
    List<Map<String, Object>> getStatementTypeStatistics(Integer startYear, Integer endYear);

    /**
     * 按审计状态统计
     */
    List<Map<String, Object>> getAuditStatusStatistics(Integer startYear, Integer endYear);

    /**
     * 按数据质量等级统计
     */
    List<Map<String, Object>> getDataQualityLevelStatistics(Integer startYear, Integer endYear);

    /**
     * 按风险等级统计
     */
    List<Map<String, Object>> getRiskLevelStatistics(Integer startYear, Integer endYear);

    /**
     * 报表提交趋势分析
     */
    List<Map<String, Object>> getSubmissionTrendAnalysis(Integer startYear, Integer endYear);

    /**
     * 审计机构分布统计
     */
    List<Map<String, Object>> getAuditFirmDistributionStatistics(Integer startYear, Integer endYear);

    /**
     * 财务报表统计概览
     */
    Map<String, Object> getFinancialStatementStatisticsOverview();

    // ==================== 批量操作方法 ====================

    /**
     * 批量更新报表状态
     */
    boolean batchUpdateStatementStatus(List<String> statementIds, String statementStatus);

    /**
     * 批量更新审计状态
     */
    boolean batchUpdateAuditStatus(List<String> statementIds, String auditStatus);

    /**
     * 批量更新数据质量等级
     */
    boolean batchUpdateDataQualityLevel(List<String> statementIds, String dataQualityLevel);

    /**
     * 批量更新风险等级
     */
    boolean batchUpdateRiskLevel(List<String> statementIds, String riskLevel);

    /**
     * 批量更新监管关注标识
     */
    boolean batchUpdateRegulatoryAttention(List<String> statementIds, Boolean needRegulatoryAttention);

    /**
     * 批量导入财务报表
     */
    Map<String, Object> batchImportFinancialStatements(List<FinancialStatement> statements);

    // ==================== 导出功能方法 ====================

    /**
     * 导出财务报表列表
     */
    List<Map<String, Object>> exportFinancialStatementList(FinancialStatementQueryVO queryVO);

    /**
     * 导出数据质量分析报告
     */
    Map<String, Object> exportDataQualityAnalysisReport(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 导出风险分析报告
     */
    Map<String, Object> exportRiskAnalysisReport(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 导出合规检查报告
     */
    Map<String, Object> exportComplianceCheckReport(String enterpriseId, Integer startYear, Integer endYear);

    // ==================== 标签转换方法 ====================

    /**
     * 获取报表类型标签
     */
    String getStatementTypeLabel(String statementType);

    /**
     * 获取期间类型标签
     */
    String getPeriodTypeLabel(String periodType);

    /**
     * 获取报表状态标签
     */
    String getStatementStatusLabel(String statementStatus);

    /**
     * 获取审计状态标签
     */
    String getAuditStatusLabel(String auditStatus);

    /**
     * 获取审计意见标签
     */
    String getAuditOpinionLabel(String auditOpinion);

    /**
     * 获取数据质量等级标签
     */
    String getDataQualityLevelLabel(String dataQualityLevel);

    /**
     * 获取风险等级标签
     */
    String getRiskLevelLabel(String riskLevel);

    /**
     * 获取会计准则标签
     */
    String getAccountingStandardsLabel(String accountingStandards);

    /**
     * 获取货币单位标签
     */
    String getCurrencyLabel(String currency);

    // ==================== 数据维护方法 ====================

    /**
     * 删除过期报表记录
     */
    int deleteExpiredStatementRecords(Integer days);

    /**
     * 数据清理和优化
     */
    Map<String, Object> cleanAndOptimizeData();

    /**
     * 重新计算数据质量评分
     */
    boolean recalculateDataQualityScores(List<String> statementIds);

    /**
     * 重新评估风险等级
     */
    boolean reassessRiskLevels(List<String> statementIds);

}
