package com.huabo.cybermonitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.cybermonitor.entity.RiskAssessment;
import com.huabo.cybermonitor.vo.RiskAssessmentQueryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 风险评估数据访问接口
 * 
 * @author 华博云AI助手
 * @since 2024-12-12
 */
@Mapper
public interface RiskAssessmentMapper extends BaseMapper<RiskAssessment> {

    // ==================== 基础查询方法 ====================

    /**
     * 根据查询条件获取风险评估列表
     */
    List<RiskAssessment> selectByCondition(@Param("query") RiskAssessmentQueryVO query);

    /**
     * 根据查询条件统计风险评估数量
     */
    Long countByCondition(@Param("query") RiskAssessmentQueryVO query);

    /**
     * 根据企业ID获取风险评估列表
     */
    List<RiskAssessment> selectByEnterpriseId(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID和评估年度获取风险评估
     */
    RiskAssessment selectByEnterpriseIdAndYear(@Param("enterpriseId") String enterpriseId, 
                                               @Param("assessmentYear") Integer assessmentYear);

    /**
     * 根据企业ID和评估类型获取最新风险评估
     */
    RiskAssessment selectLatestByEnterpriseIdAndType(@Param("enterpriseId") String enterpriseId, 
                                                     @Param("assessmentType") String assessmentType);

    // ==================== 风险等级查询 ====================

    /**
     * 根据风险等级获取风险评估列表
     */
    List<RiskAssessment> selectByRiskLevel(@Param("riskLevel") String riskLevel);

    /**
     * 根据综合风险等级统计企业数量
     */
    Long countByOverallRiskLevel(@Param("riskLevel") String riskLevel);

    /**
     * 获取各风险等级的企业分布统计
     */
    List<Map<String, Object>> selectRiskLevelDistribution();

    /**
     * 根据风险评分范围获取风险评估列表
     */
    List<RiskAssessment> selectByRiskScoreRange(@Param("minScore") BigDecimal minScore, 
                                                @Param("maxScore") BigDecimal maxScore);

    // ==================== 预警查询 ====================

    /**
     * 获取触发预警的风险评估列表
     */
    List<RiskAssessment> selectTriggeredWarnings();

    /**
     * 根据预警等级获取风险评估列表
     */
    List<RiskAssessment> selectByWarningLevel(@Param("warningLevel") String warningLevel);

    /**
     * 统计各预警等级的数量
     */
    List<Map<String, Object>> selectWarningLevelStatistics();

    /**
     * 获取最近触发预警的风险评估
     */
    List<RiskAssessment> selectRecentWarnings(@Param("days") Integer days);

    // ==================== 趋势分析 ====================

    /**
     * 根据风险趋势获取风险评估列表
     */
    List<RiskAssessment> selectByRiskTrend(@Param("riskTrend") String riskTrend);

    /**
     * 获取企业风险趋势分析数据
     */
    List<Map<String, Object>> selectRiskTrendAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                      @Param("months") Integer months);

    /**
     * 获取行业风险趋势对比数据
     */
    List<Map<String, Object>> selectIndustryRiskTrendComparison(@Param("industryType") String industryType);

    /**
     * 获取风险变化幅度统计
     */
    List<Map<String, Object>> selectRiskChangeMagnitudeStatistics();

    // ==================== 评估状态查询 ====================

    /**
     * 根据评估状态获取风险评估列表
     */
    List<RiskAssessment> selectByAssessmentStatus(@Param("assessmentStatus") String assessmentStatus);

    /**
     * 获取待审核的风险评估列表
     */
    List<RiskAssessment> selectPendingReview();

    /**
     * 获取已过期的风险评估列表
     */
    List<RiskAssessment> selectExpiredAssessments();

    /**
     * 获取即将到期的风险评估列表
     */
    List<RiskAssessment> selectExpiringAssessments(@Param("days") Integer days);

    // ==================== 风险因素分析 ====================

    /**
     * 根据风险因素数量范围获取风险评估列表
     */
    List<RiskAssessment> selectByRiskFactorCountRange(@Param("minCount") Integer minCount, 
                                                      @Param("maxCount") Integer maxCount);

    /**
     * 获取高风险因素统计
     */
    List<Map<String, Object>> selectHighRiskFactorStatistics();

    /**
     * 获取风险关联度分析数据
     */
    List<Map<String, Object>> selectRiskCorrelationAnalysis();

    /**
     * 获取风险集中度分析数据
     */
    List<Map<String, Object>> selectRiskConcentrationAnalysis();

    // ==================== 应对策略分析 ====================

    /**
     * 根据风险应对策略获取风险评估列表
     */
    List<RiskAssessment> selectByResponseStrategy(@Param("responseStrategy") String responseStrategy);

    /**
     * 获取风险应对策略分布统计
     */
    List<Map<String, Object>> selectResponseStrategyDistribution();

    /**
     * 根据责任部门获取风险评估列表
     */
    List<RiskAssessment> selectByResponsibleDepartment(@Param("responsibleDepartment") String responsibleDepartment);

    /**
     * 根据责任人获取风险评估列表
     */
    List<RiskAssessment> selectByResponsiblePerson(@Param("responsiblePerson") String responsiblePerson);

    // ==================== 时间维度查询 ====================

    /**
     * 根据评估日期范围获取风险评估列表
     */
    List<RiskAssessment> selectByAssessmentDateRange(@Param("startDate") LocalDate startDate, 
                                                     @Param("endDate") LocalDate endDate);

    /**
     * 获取按月份统计的风险评估数量
     */
    List<Map<String, Object>> selectMonthlyAssessmentStatistics(@Param("year") Integer year);

    /**
     * 获取按季度统计的风险评估数量
     */
    List<Map<String, Object>> selectQuarterlyAssessmentStatistics(@Param("year") Integer year);

    /**
     * 获取按年度统计的风险评估数量
     */
    List<Map<String, Object>> selectYearlyAssessmentStatistics();

    // ==================== 综合统计分析 ====================

    /**
     * 获取风险评估综合统计数据
     */
    Map<String, Object> selectComprehensiveStatistics();

    /**
     * 获取企业风险评估概览数据
     */
    Map<String, Object> selectEnterpriseRiskOverview(@Param("enterpriseId") String enterpriseId);

    /**
     * 获取风险评估质量分析数据
     */
    List<Map<String, Object>> selectAssessmentQualityAnalysis();

    /**
     * 获取风险评估效果分析数据
     */
    List<Map<String, Object>> selectAssessmentEffectivenessAnalysis();

    // ==================== 对比分析 ====================

    /**
     * 获取同业风险评估对比数据
     */
    List<Map<String, Object>> selectPeerRiskComparison(@Param("enterpriseId") String enterpriseId, 
                                                       @Param("industryType") String industryType);

    /**
     * 获取历史风险评估对比数据
     */
    List<Map<String, Object>> selectHistoricalRiskComparison(@Param("enterpriseId") String enterpriseId, 
                                                             @Param("years") Integer years);

    /**
     * 获取区域风险评估对比数据
     */
    List<Map<String, Object>> selectRegionalRiskComparison(@Param("region") String region);

    // ==================== 导出查询 ====================

    /**
     * 获取风险评估导出数据
     */
    List<Map<String, Object>> selectForExport(@Param("query") RiskAssessmentQueryVO query);

    /**
     * 获取风险评估报告数据
     */
    List<Map<String, Object>> selectForReport(@Param("enterpriseId") String enterpriseId, 
                                              @Param("assessmentYear") Integer assessmentYear);

    // ==================== 批量操作 ====================

    /**
     * 批量更新风险评估状态
     */
    Integer batchUpdateStatus(@Param("ids") List<String> ids, 
                             @Param("status") String status, 
                             @Param("updateBy") String updateBy);

    /**
     * 批量删除风险评估
     */
    Integer batchDelete(@Param("ids") List<String> ids, 
                       @Param("updateBy") String updateBy);

    /**
     * 批量审核风险评估
     */
    Integer batchReview(@Param("ids") List<String> ids, 
                       @Param("reviewStatus") String reviewStatus, 
                       @Param("reviewer") String reviewer, 
                       @Param("reviewComments") String reviewComments);

}
