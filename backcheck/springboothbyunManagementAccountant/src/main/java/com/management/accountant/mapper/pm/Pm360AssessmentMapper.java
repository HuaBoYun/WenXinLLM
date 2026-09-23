package com.management.accountant.mapper.pm;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.pm.Pm360Assessment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 360度评估数据访问层
 * 提供360度评估的数据库操作方法
 * 
 * @author 华博云AI助手
 * @since 2024-01-01
 */
@Mapper
public interface Pm360AssessmentMapper extends BaseMapper<Pm360Assessment> {

    /**
     * 分页查询360度评估
     */
    IPage<Pm360Assessment> selectAssessmentPage(Page<Pm360Assessment> page, 
                                               @Param("assessmentName") String assessmentName,
                                               @Param("assessedUserName") String assessedUserName,
                                               @Param("assessmentType") String assessmentType,
                                               @Param("assessmentStatus") String assessmentStatus,
                                               @Param("assessmentYear") Integer assessmentYear,
                                               @Param("organizationId") Long organizationId);

    /**
     * 根据被评估人查询评估列表
     */
    List<Pm360Assessment> selectByAssessedUserId(@Param("assessedUserId") Long assessedUserId,
                                                 @Param("assessmentType") String assessmentType,
                                                 @Param("assessmentStatus") String assessmentStatus);

    /**
     * 根据组织查询评估列表
     */
    List<Pm360Assessment> selectByOrganizationId(@Param("organizationId") Long organizationId,
                                                 @Param("assessmentYear") Integer assessmentYear,
                                                 @Param("assessmentType") String assessmentType);

    /**
     * 查询评估统计数据
     */
    Map<String, Object> selectAssessmentStatistics(@Param("organizationId") Long organizationId,
                                                   @Param("assessmentYear") Integer assessmentYear,
                                                   @Param("assessmentType") String assessmentType);

    /**
     * 查询评估完成率统计
     */
    List<Map<String, Object>> selectCompletionRateStatistics(@Param("organizationId") Long organizationId,
                                                             @Param("startTime") LocalDateTime startTime,
                                                             @Param("endTime") LocalDateTime endTime);

    /**
     * 查询评估分数分布
     */
    List<Map<String, Object>> selectScoreDistribution(@Param("organizationId") Long organizationId,
                                                      @Param("assessmentType") String assessmentType,
                                                      @Param("assessmentYear") Integer assessmentYear);

    /**
     * 查询评估趋势数据
     */
    List<Map<String, Object>> selectAssessmentTrends(@Param("assessedUserId") Long assessedUserId,
                                                     @Param("startTime") LocalDateTime startTime,
                                                     @Param("endTime") LocalDateTime endTime);

    /**
     * 查询评估排名
     */
    List<Map<String, Object>> selectAssessmentRanking(@Param("organizationId") Long organizationId,
                                                      @Param("assessmentType") String assessmentType,
                                                      @Param("assessmentYear") Integer assessmentYear,
                                                      @Param("limit") Integer limit);

    /**
     * 查询评估对比数据
     */
    List<Map<String, Object>> selectAssessmentComparison(@Param("assessmentIds") List<Long> assessmentIds,
                                                         @Param("compareType") String compareType);

    /**
     * 查询评估参与者统计
     */
    Map<String, Object> selectParticipantStatistics(@Param("assessmentId") Long assessmentId);

    /**
     * 查询评估维度分析
     */
    List<Map<String, Object>> selectDimensionAnalysis(@Param("assessmentId") Long assessmentId);

    /**
     * 查询评估历史记录
     */
    List<Map<String, Object>> selectAssessmentHistory(@Param("assessedUserId") Long assessedUserId,
                                                      @Param("historyType") String historyType,
                                                      @Param("limit") Integer limit);

    /**
     * 查询待评估任务
     */
    List<Map<String, Object>> selectPendingAssessmentTasks(@Param("userId") Long userId,
                                                           @Param("taskType") String taskType);

    /**
     * 查询已完成评估任务
     */
    List<Map<String, Object>> selectCompletedAssessmentTasks(@Param("userId") Long userId,
                                                             @Param("taskType") String taskType);

    /**
     * 查询评估反馈
     */
    List<Map<String, Object>> selectAssessmentFeedback(@Param("assessmentId") Long assessmentId,
                                                       @Param("feedbackType") String feedbackType);

    /**
     * 查询评估洞察数据
     */
    Map<String, Object> selectAssessmentInsights(@Param("assessmentId") Long assessmentId,
                                                 @Param("insightType") String insightType);

    /**
     * 查询评估基准数据
     */
    Map<String, Object> selectAssessmentBenchmark(@Param("organizationId") Long organizationId,
                                                  @Param("positionId") Long positionId,
                                                  @Param("assessmentType") String assessmentType);

    /**
     * 查询评估异常数据
     */
    List<Map<String, Object>> selectAssessmentAnomalies(@Param("organizationId") Long organizationId,
                                                        @Param("assessmentYear") Integer assessmentYear);

    /**
     * 查询评估质量指标
     */
    Map<String, Object> selectAssessmentQualityMetrics(@Param("assessmentId") Long assessmentId);

    /**
     * 查询评估改进建议
     */
    List<Map<String, Object>> selectImprovementSuggestions(@Param("assessmentId") Long assessmentId,
                                                           @Param("suggestionType") String suggestionType);

    /**
     * 查询评估预测数据
     */
    Map<String, Object> selectAssessmentPrediction(@Param("assessedUserId") Long assessedUserId,
                                                   @Param("predictionType") String predictionType);

    /**
     * 批量更新评估状态
     */
    int batchUpdateAssessmentStatus(@Param("assessmentIds") List<Long> assessmentIds,
                                   @Param("assessmentStatus") String assessmentStatus,
                                   @Param("updatedBy") Long updatedBy,
                                   @Param("updatedTime") LocalDateTime updatedTime);

    /**
     * 批量更新评估分数
     */
    int batchUpdateAssessmentScores(@Param("assessmentScores") List<Map<String, Object>> assessmentScores,
                                   @Param("updatedBy") Long updatedBy,
                                   @Param("updatedTime") LocalDateTime updatedTime);

    /**
     * 批量删除评估
     */
    int batchDeleteAssessments(@Param("assessmentIds") List<Long> assessmentIds,
                              @Param("updatedBy") Long updatedBy,
                              @Param("updatedTime") LocalDateTime updatedTime);

    /**
     * 查询评估模板
     */
    List<Map<String, Object>> selectAssessmentTemplates(@Param("templateType") String templateType,
                                                        @Param("organizationId") Long organizationId);

    /**
     * 查询评估配置
     */
    Map<String, Object> selectAssessmentConfig(@Param("configType") String configType,
                                               @Param("organizationId") Long organizationId);

    /**
     * 查询评估权限
     */
    Map<String, Object> selectAssessmentPermission(@Param("assessmentId") Long assessmentId,
                                                   @Param("userId") Long userId,
                                                   @Param("permissionType") String permissionType);

    /**
     * 查询评估通知设置
     */
    List<Map<String, Object>> selectAssessmentNotificationSettings(@Param("assessmentId") Long assessmentId);

    /**
     * 查询评估提醒
     */
    List<Map<String, Object>> selectAssessmentReminders(@Param("userId") Long userId,
                                                        @Param("reminderType") String reminderType);

    /**
     * 查询评估日历
     */
    List<Map<String, Object>> selectAssessmentCalendar(@Param("userId") Long userId,
                                                       @Param("startDate") LocalDateTime startDate,
                                                       @Param("endDate") LocalDateTime endDate);

    /**
     * 查询评估工作流
     */
    List<Map<String, Object>> selectAssessmentWorkflow(@Param("assessmentId") Long assessmentId);

    /**
     * 查询评估审批记录
     */
    List<Map<String, Object>> selectAssessmentApprovalHistory(@Param("assessmentId") Long assessmentId);

    /**
     * 查询评估归档数据
     */
    List<Map<String, Object>> selectArchivedAssessments(@Param("organizationId") Long organizationId,
                                                        @Param("archiveYear") Integer archiveYear);

    /**
     * 查询评估数据完整性
     */
    Map<String, Object> selectAssessmentDataIntegrity(@Param("assessmentId") Long assessmentId);

    /**
     * 查询评估性能指标
     */
    Map<String, Object> selectAssessmentPerformanceMetrics(@Param("organizationId") Long organizationId,
                                                           @Param("startTime") LocalDateTime startTime,
                                                           @Param("endTime") LocalDateTime endTime);

    /**
     * 查询评估最佳实践
     */
    List<Map<String, Object>> selectAssessmentBestPractices(@Param("organizationId") Long organizationId,
                                                            @Param("practiceType") String practiceType);

    /**
     * 查询评估知识库
     */
    List<Map<String, Object>> selectAssessmentKnowledgeBase(@Param("knowledgeType") String knowledgeType,
                                                            @Param("searchKeyword") String searchKeyword);

    /**
     * 查询评估智能推荐
     */
    List<Map<String, Object>> selectAssessmentRecommendations(@Param("assessmentId") Long assessmentId,
                                                              @Param("recommendationType") String recommendationType);

    /**
     * 查询评估学习路径
     */
    List<Map<String, Object>> selectAssessmentLearningPath(@Param("assessmentId") Long assessmentId,
                                                           @Param("pathType") String pathType);

    /**
     * 查询评估成长轨迹
     */
    List<Map<String, Object>> selectAssessmentGrowthTrack(@Param("assessedUserId") Long assessedUserId,
                                                          @Param("trackType") String trackType);

    /**
     * 查询评估影响因子
     */
    List<Map<String, Object>> selectAssessmentImpactFactors(@Param("assessmentId") Long assessmentId);

    /**
     * 查询评估关联分析
     */
    Map<String, Object> selectAssessmentCorrelationAnalysis(@Param("organizationId") Long organizationId,
                                                            @Param("analysisType") String analysisType);

    /**
     * 查询评估敏感性分析
     */
    Map<String, Object> selectAssessmentSensitivityAnalysis(@Param("assessmentId") Long assessmentId,
                                                            @Param("sensitivityType") String sensitivityType);

    /**
     * 查询评估风险评估
     */
    Map<String, Object> selectAssessmentRiskEvaluation(@Param("assessmentId") Long assessmentId);

    /**
     * 查询评估价值分析
     */
    Map<String, Object> selectAssessmentValueAnalysis(@Param("organizationId") Long organizationId,
                                                      @Param("valueType") String valueType);

    /**
     * 查询评估ROI分析
     */
    Map<String, Object> selectAssessmentROIAnalysis(@Param("organizationId") Long organizationId,
                                                    @Param("analysisYear") Integer analysisYear);

    /**
     * 查询评估成本效益分析
     */
    Map<String, Object> selectAssessmentCostBenefitAnalysis(@Param("organizationId") Long organizationId,
                                                            @Param("analysisType") String analysisType);
}
