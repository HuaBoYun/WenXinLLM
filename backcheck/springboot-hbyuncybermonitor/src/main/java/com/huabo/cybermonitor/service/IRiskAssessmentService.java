package com.huabo.cybermonitor.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.cybermonitor.entity.RiskAssessment;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.vo.RiskAssessmentQueryVO;

/**
 * 风险评估业务接口
 * 
 * @author 华博云AI助手
 * @since 2024-12-12
 */
public interface IRiskAssessmentService extends IService<RiskAssessment> {

    // ==================== 基础业务方法 ====================

    /**
     * 分页查询风险评估
     */
    PageResult<RiskAssessment> selectByPage(RiskAssessmentQueryVO query);

    /**
     * 根据企业ID获取风险评估列表
     */
    List<RiskAssessment> getByEnterpriseId(String enterpriseId);

    /**
     * 根据企业ID和评估年度获取风险评估
     */
    RiskAssessment getByEnterpriseIdAndYear(String enterpriseId, Integer assessmentYear);

    /**
     * 根据企业ID和评估类型获取最新风险评估
     */
    RiskAssessment getLatestByEnterpriseIdAndType(String enterpriseId, String assessmentType);

    /**
     * 保存风险评估
     */
    boolean saveRiskAssessment(RiskAssessment riskAssessment);

    /**
     * 更新风险评估
     */
    boolean updateRiskAssessment(RiskAssessment riskAssessment);

    /**
     * 删除风险评估
     */
    boolean deleteRiskAssessment(String riskAssessmentId);

    // ==================== 风险评估核心业务 ====================

    /**
     * 执行风险评估
     */
    RiskAssessment performRiskAssessment(String enterpriseId, String assessmentType, String assessmentMethod);

    /**
     * 计算综合风险评分
     */
    BigDecimal calculateOverallRiskScore(RiskAssessment riskAssessment);

    /**
     * 计算财务风险评分
     */
    BigDecimal calculateFinancialRiskScore(String enterpriseId, Integer assessmentYear);

    /**
     * 计算经营风险评分
     */
    BigDecimal calculateOperationalRiskScore(String enterpriseId, Integer assessmentYear);

    /**
     * 计算合规风险评分
     */
    BigDecimal calculateComplianceRiskScore(String enterpriseId, Integer assessmentYear);

    /**
     * 计算治理风险评分
     */
    BigDecimal calculateGovernanceRiskScore(String enterpriseId, Integer assessmentYear);

    /**
     * 计算外部风险评分
     */
    BigDecimal calculateExternalRiskScore(String enterpriseId, Integer assessmentYear);

    /**
     * 确定风险等级
     */
    String determineRiskLevel(BigDecimal riskScore);

    /**
     * 分析风险趋势
     */
    String analyzeRiskTrend(String enterpriseId, String riskType);

    // ==================== 风险预警业务 ====================

    /**
     * 检查风险预警
     */
    boolean checkRiskWarning(RiskAssessment riskAssessment);

    /**
     * 触发风险预警
     */
    void triggerRiskWarning(RiskAssessment riskAssessment, String warningLevel, String warningReason);

    /**
     * 获取触发预警的风险评估列表
     */
    List<RiskAssessment> getTriggeredWarnings();

    /**
     * 获取预警统计数据
     */
    Map<String, Object> getWarningStatistics();

    // ==================== 风险因素分析 ====================

    /**
     * 识别风险因素
     */
    List<String> identifyRiskFactors(String enterpriseId, String assessmentType);

    /**
     * 分析风险关联度
     */
    BigDecimal analyzeRiskCorrelation(String enterpriseId, List<String> riskFactors);

    /**
     * 计算风险集中度
     */
    BigDecimal calculateRiskConcentration(String enterpriseId, String riskType);

    /**
     * 评估风险影响程度
     */
    String assessRiskImpact(String enterpriseId, String riskType, BigDecimal riskScore);

    // ==================== 风险应对策略 ====================

    /**
     * 制定风险应对策略
     */
    String developRiskResponseStrategy(RiskAssessment riskAssessment);

    /**
     * 生成风险控制措施建议
     */
    List<String> generateControlMeasureRecommendations(RiskAssessment riskAssessment);

    /**
     * 评估风险应对效果
     */
    BigDecimal evaluateResponseEffectiveness(String riskAssessmentId);

    // ==================== 审核流程业务 ====================

    /**
     * 提交审核
     */
    boolean submitForReview(String riskAssessmentId, String assessor);

    /**
     * 审核风险评估
     */
    boolean reviewRiskAssessment(String riskAssessmentId, String reviewer, String reviewStatus, String reviewComments);

    /**
     * 批准风险评估
     */
    boolean approveRiskAssessment(String riskAssessmentId, String approver, String approvalComments);

    /**
     * 获取待审核的风险评估列表
     */
    List<RiskAssessment> getPendingReview();

    // ==================== 统计分析业务 ====================

    /**
     * 获取风险等级分布统计
     */
    Map<String, Object> getRiskLevelDistribution();

    /**
     * 获取风险趋势分析数据
     */
    List<Map<String, Object>> getRiskTrendAnalysis(String enterpriseId, Integer months);

    /**
     * 获取行业风险对比数据
     */
    List<Map<String, Object>> getIndustryRiskComparison(String enterpriseId, String industryType);

    /**
     * 获取综合统计数据
     */
    Map<String, Object> getComprehensiveStatistics();

    /**
     * 获取企业风险概览
     */
    Map<String, Object> getEnterpriseRiskOverview(String enterpriseId);

    // ==================== 报告生成业务 ====================

    /**
     * 生成风险评估报告
     */
    Map<String, Object> generateAssessmentReport(String enterpriseId, Integer assessmentYear);

    /**
     * 生成风险分析报告
     */
    Map<String, Object> generateRiskAnalysisReport(String enterpriseId, LocalDate startDate, LocalDate endDate);

    /**
     * 生成预警报告
     */
    Map<String, Object> generateWarningReport(String enterpriseId, Integer days);

    // ==================== 数据质量业务 ====================

    /**
     * 验证评估数据质量
     */
    boolean validateAssessmentDataQuality(RiskAssessment riskAssessment);

    /**
     * 计算数据完整性评分
     */
    BigDecimal calculateDataCompletenessScore(RiskAssessment riskAssessment);

    /**
     * 计算数据准确性评分
     */
    BigDecimal calculateDataAccuracyScore(RiskAssessment riskAssessment);

    /**
     * 计算数据及时性评分
     */
    BigDecimal calculateDataTimelinessScore(RiskAssessment riskAssessment);

    /**
     * 计算数据一致性评分
     */
    BigDecimal calculateDataConsistencyScore(RiskAssessment riskAssessment);

    // ==================== 批量操作业务 ====================

    /**
     * 批量执行风险评估
     */
    List<RiskAssessment> batchPerformAssessment(List<String> enterpriseIds, String assessmentType);

    /**
     * 批量更新评估状态
     */
    boolean batchUpdateStatus(List<String> riskAssessmentIds, String status, String updateBy);

    /**
     * 批量审核风险评估
     */
    boolean batchReview(List<String> riskAssessmentIds, String reviewer, String reviewStatus, String reviewComments);

    /**
     * 批量删除风险评估
     */
    boolean batchDelete(List<String> riskAssessmentIds, String updateBy);

    // ==================== 导出业务 ====================

    /**
     * 导出风险评估数据
     */
    List<Map<String, Object>> exportAssessmentData(RiskAssessmentQueryVO query);

    /**
     * 导出风险评估报告
     */
    byte[] exportAssessmentReport(String enterpriseId, Integer assessmentYear, String format);

    // ==================== 标签转换业务 ====================

    /**
     * 转换评估类型标签
     */
    String convertAssessmentTypeLabel(String assessmentType);

    /**
     * 转换评估方法标签
     */
    String convertAssessmentMethodLabel(String assessmentMethod);

    /**
     * 转换评估状态标签
     */
    String convertAssessmentStatusLabel(String assessmentStatus);

    /**
     * 转换风险等级标签
     */
    String convertRiskLevelLabel(String riskLevel);

    /**
     * 转换风险趋势标签
     */
    String convertRiskTrendLabel(String riskTrend);

    /**
     * 转换预警等级标签
     */
    String convertWarningLevelLabel(String warningLevel);

    /**
     * 转换应对策略标签
     */
    String convertResponseStrategyLabel(String responseStrategy);

}
