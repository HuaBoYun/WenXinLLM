package com.huabo.cybermonitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.cybermonitor.entity.RiskControlMeasure;
import com.huabo.cybermonitor.vo.RiskControlMeasureQueryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 风险控制措施数据访问接口
 * 
 * @author 华博云AI助手
 * @since 2024-12-12
 */
@Mapper
public interface RiskControlMeasureMapper extends BaseMapper<RiskControlMeasure> {

    // ==================== 基础查询方法 ====================

    /**
     * 根据查询条件获取风险控制措施列表
     */
    List<RiskControlMeasure> selectByCondition(@Param("query") RiskControlMeasureQueryVO query);

    /**
     * 根据查询条件统计风险控制措施数量
     */
    Long countByCondition(@Param("query") RiskControlMeasureQueryVO query);

    /**
     * 根据企业ID获取风险控制措施列表
     */
    List<RiskControlMeasure> selectByEnterpriseId(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据风险评估ID获取风险控制措施列表
     */
    List<RiskControlMeasure> selectByRiskAssessmentId(@Param("riskAssessmentId") String riskAssessmentId);

    /**
     * 根据措施类型获取风险控制措施列表
     */
    List<RiskControlMeasure> selectByMeasureType(@Param("measureType") String measureType);

    // ==================== 措施状态查询 ====================

    /**
     * 根据措施状态获取风险控制措施列表
     */
    List<RiskControlMeasure> selectByMeasureStatus(@Param("measureStatus") String measureStatus);

    /**
     * 获取计划中的风险控制措施列表
     */
    List<RiskControlMeasure> selectPlannedMeasures();

    /**
     * 获取实施中的风险控制措施列表
     */
    List<RiskControlMeasure> selectInProgressMeasures();

    /**
     * 获取已完成的风险控制措施列表
     */
    List<RiskControlMeasure> selectCompletedMeasures();

    /**
     * 获取已暂停的风险控制措施列表
     */
    List<RiskControlMeasure> selectSuspendedMeasures();

    // ==================== 优先级查询 ====================

    /**
     * 根据优先级获取风险控制措施列表
     */
    List<RiskControlMeasure> selectByPriority(@Param("priority") String priority);

    /**
     * 获取高优先级的风险控制措施列表
     */
    List<RiskControlMeasure> selectHighPriorityMeasures();

    /**
     * 获取关键优先级的风险控制措施列表
     */
    List<RiskControlMeasure> selectCriticalPriorityMeasures();

    /**
     * 获取优先级分布统计
     */
    List<Map<String, Object>> selectPriorityDistribution();

    // ==================== 实施进度查询 ====================

    /**
     * 根据实施进度范围获取风险控制措施列表
     */
    List<RiskControlMeasure> selectByImplementationProgressRange(@Param("minProgress") BigDecimal minProgress, 
                                                                 @Param("maxProgress") BigDecimal maxProgress);

    /**
     * 获取实施进度统计
     */
    List<Map<String, Object>> selectImplementationProgressStatistics();

    /**
     * 获取逾期的风险控制措施列表
     */
    List<RiskControlMeasure> selectOverdueMeasures();

    /**
     * 获取即将到期的风险控制措施列表
     */
    List<RiskControlMeasure> selectExpiringMeasures(@Param("days") Integer days);

    // ==================== 责任信息查询 ====================

    /**
     * 根据责任部门获取风险控制措施列表
     */
    List<RiskControlMeasure> selectByResponsibleDepartment(@Param("responsibleDepartment") String responsibleDepartment);

    /**
     * 根据责任人获取风险控制措施列表
     */
    List<RiskControlMeasure> selectByResponsiblePerson(@Param("responsiblePerson") String responsiblePerson);

    /**
     * 根据监督人员获取风险控制措施列表
     */
    List<RiskControlMeasure> selectBySupervisor(@Param("supervisor") String supervisor);

    /**
     * 获取责任部门分布统计
     */
    List<Map<String, Object>> selectResponsibleDepartmentDistribution();

    // ==================== 预算和成本查询 ====================

    /**
     * 根据预算金额范围获取风险控制措施列表
     */
    List<RiskControlMeasure> selectByBudgetAmountRange(@Param("minAmount") BigDecimal minAmount, 
                                                       @Param("maxAmount") BigDecimal maxAmount);

    /**
     * 根据实际支出范围获取风险控制措施列表
     */
    List<RiskControlMeasure> selectByActualExpenditureRange(@Param("minExpenditure") BigDecimal minExpenditure, 
                                                            @Param("maxExpenditure") BigDecimal maxExpenditure);

    /**
     * 获取预算执行分析数据
     */
    List<Map<String, Object>> selectBudgetExecutionAnalysis();

    /**
     * 获取超预算的风险控制措施列表
     */
    List<RiskControlMeasure> selectOverBudgetMeasures();

    // ==================== 效果评估查询 ====================

    /**
     * 根据效果等级获取风险控制措施列表
     */
    List<RiskControlMeasure> selectByEffectivenessLevel(@Param("effectivenessLevel") String effectivenessLevel);

    /**
     * 根据效果评分范围获取风险控制措施列表
     */
    List<RiskControlMeasure> selectByEffectivenessScoreRange(@Param("minScore") BigDecimal minScore, 
                                                             @Param("maxScore") BigDecimal maxScore);

    /**
     * 获取效果评估统计数据
     */
    List<Map<String, Object>> selectEffectivenessStatistics();

    /**
     * 获取高效果的风险控制措施列表
     */
    List<RiskControlMeasure> selectHighEffectivenessMeasures();

    // ==================== 风险降低效果查询 ====================

    /**
     * 根据预期风险降低程度范围获取风险控制措施列表
     */
    List<RiskControlMeasure> selectByExpectedRiskReductionRange(@Param("minReduction") BigDecimal minReduction, 
                                                                @Param("maxReduction") BigDecimal maxReduction);

    /**
     * 根据实际风险降低程度范围获取风险控制措施列表
     */
    List<RiskControlMeasure> selectByActualRiskReductionRange(@Param("minReduction") BigDecimal minReduction, 
                                                              @Param("maxReduction") BigDecimal maxReduction);

    /**
     * 获取风险降低效果分析数据
     */
    List<Map<String, Object>> selectRiskReductionEffectivenessAnalysis();

    /**
     * 获取风险降低目标达成情况
     */
    List<Map<String, Object>> selectRiskReductionTargetAchievement();

    // ==================== 监控信息查询 ====================

    /**
     * 获取需要监控的风险控制措施列表
     */
    List<RiskControlMeasure> selectRequiringMonitoring();

    /**
     * 根据监控频率获取风险控制措施列表
     */
    List<RiskControlMeasure> selectByMonitoringFrequency(@Param("monitoringFrequency") String monitoringFrequency);

    /**
     * 根据监控状态获取风险控制措施列表
     */
    List<RiskControlMeasure> selectByMonitoringStatus(@Param("monitoringStatus") String monitoringStatus);

    /**
     * 获取监控统计数据
     */
    List<Map<String, Object>> selectMonitoringStatistics();

    // ==================== 审核状态查询 ====================

    /**
     * 根据审核状态获取风险控制措施列表
     */
    List<RiskControlMeasure> selectByReviewStatus(@Param("reviewStatus") String reviewStatus);

    /**
     * 获取待审核的风险控制措施列表
     */
    List<RiskControlMeasure> selectPendingReview();

    /**
     * 获取已审核的风险控制措施列表
     */
    List<RiskControlMeasure> selectReviewed();

    /**
     * 获取已批准的风险控制措施列表
     */
    List<RiskControlMeasure> selectApproved();

    // ==================== 时间维度查询 ====================

    /**
     * 根据计划开始日期范围获取风险控制措施列表
     */
    List<RiskControlMeasure> selectByPlannedStartDateRange(@Param("startDate") LocalDate startDate, 
                                                           @Param("endDate") LocalDate endDate);

    /**
     * 根据实际完成日期范围获取风险控制措施列表
     */
    List<RiskControlMeasure> selectByActualCompletionDateRange(@Param("startDate") LocalDate startDate, 
                                                               @Param("endDate") LocalDate endDate);

    /**
     * 获取按月统计的措施完成情况
     */
    List<Map<String, Object>> selectMonthlyCompletionStatistics(@Param("year") Integer year);

    /**
     * 获取按季度统计的措施实施情况
     */
    List<Map<String, Object>> selectQuarterlyImplementationStatistics(@Param("year") Integer year);

    // ==================== 综合统计分析 ====================

    /**
     * 获取风险控制措施综合统计数据
     */
    Map<String, Object> selectComprehensiveStatistics();

    /**
     * 获取企业风险控制措施概览数据
     */
    Map<String, Object> selectEnterpriseMeasureOverview(@Param("enterpriseId") String enterpriseId);

    /**
     * 获取措施类型分布统计
     */
    List<Map<String, Object>> selectMeasureTypeDistribution();

    /**
     * 获取措施分类分布统计
     */
    List<Map<String, Object>> selectMeasureCategoryDistribution();

    // ==================== 对比分析 ====================

    /**
     * 获取同业措施对比数据
     */
    List<Map<String, Object>> selectPeerMeasureComparison(@Param("enterpriseId") String enterpriseId, 
                                                          @Param("industryType") String industryType);

    /**
     * 获取历史措施对比数据
     */
    List<Map<String, Object>> selectHistoricalMeasureComparison(@Param("enterpriseId") String enterpriseId, 
                                                                @Param("years") Integer years);

    /**
     * 获取措施效果对比分析
     */
    List<Map<String, Object>> selectMeasureEffectivenessComparison();

    // ==================== 导出查询 ====================

    /**
     * 获取风险控制措施导出数据
     */
    List<Map<String, Object>> selectForExport(@Param("query") RiskControlMeasureQueryVO query);

    /**
     * 获取措施实施报告数据
     */
    List<Map<String, Object>> selectForImplementationReport(@Param("enterpriseId") String enterpriseId, 
                                                            @Param("startDate") LocalDate startDate, 
                                                            @Param("endDate") LocalDate endDate);

    // ==================== 批量操作 ====================

    /**
     * 批量更新措施状态
     */
    Integer batchUpdateMeasureStatus(@Param("ids") List<String> ids, 
                                    @Param("status") String status, 
                                    @Param("updateBy") String updateBy);

    /**
     * 批量审核措施
     */
    Integer batchReviewMeasures(@Param("ids") List<String> ids, 
                               @Param("reviewStatus") String reviewStatus, 
                               @Param("reviewer") String reviewer, 
                               @Param("reviewComments") String reviewComments);

    /**
     * 批量更新实施进度
     */
    Integer batchUpdateImplementationProgress(@Param("ids") List<String> ids, 
                                             @Param("progress") BigDecimal progress, 
                                             @Param("updateBy") String updateBy);

}
