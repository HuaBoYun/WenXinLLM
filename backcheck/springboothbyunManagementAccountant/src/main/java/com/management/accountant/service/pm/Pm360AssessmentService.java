package com.management.accountant.service.pm;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.entity.pm.Pm360Assessment;

import java.util.List;
import java.util.Map;

/**
 * 360度评估服务接口
 * 提供360度评估的完整业务逻辑
 * 
 * @author 华博云AI助手
 * @since 2024-01-01
 */
public interface Pm360AssessmentService extends IService<Pm360Assessment> {

    /**
     * 分页查询360度评估
     */
    IPage<Pm360Assessment> queryPage(Integer current, Integer size, String assessmentName, 
                                    String assessedUserName, String assessmentType, 
                                    String assessmentStatus, Integer assessmentYear, Long organizationId);

    /**
     * 创建360度评估
     */
    Pm360Assessment create(Pm360Assessment assessment);

    /**
     * 更新360度评估
     */
    Pm360Assessment update(Pm360Assessment assessment);

    /**
     * 删除360度评估
     */
    boolean delete(Long assessmentId);

    /**
     * 启动评估
     */
    boolean startAssessment(Long assessmentId, Map<String, Object> startParams);

    /**
     * 完成评估
     */
    boolean completeAssessment(Long assessmentId, Map<String, Object> completeParams);

    /**
     * 取消评估
     */
    boolean cancelAssessment(Long assessmentId, Map<String, Object> cancelParams);

    /**
     * 提交自评
     */
    boolean submitSelfEvaluation(Long assessmentId, Map<String, Object> evaluationData);

    /**
     * 提交上级评价
     */
    boolean submitSuperiorEvaluation(Long assessmentId, Map<String, Object> evaluationData);

    /**
     * 提交同级评价
     */
    boolean submitPeerEvaluation(Long assessmentId, Map<String, Object> evaluationData);

    /**
     * 提交下级评价
     */
    boolean submitSubordinateEvaluation(Long assessmentId, Map<String, Object> evaluationData);

    /**
     * 提交客户评价
     */
    boolean submitCustomerEvaluation(Long assessmentId, Map<String, Object> evaluationData);

    /**
     * 计算评估结果
     */
    Map<String, Object> calculateAssessmentResult(Long assessmentId, Map<String, Object> calculateParams);

    /**
     * 生成评估报告
     */
    Map<String, Object> generateAssessmentReport(Long assessmentId, Map<String, Object> reportParams);

    /**
     * 获取评估统计
     */
    Map<String, Object> getAssessmentStatistics(String statisticsType, String statisticsPeriod, Long organizationId);

    /**
     * 获取评估分析
     */
    Map<String, Object> getAssessmentAnalysis(Long assessmentId, String analysisType);

    /**
     * 批量操作评估
     */
    boolean batchOperation(Map<String, Object> batchData);

    /**
     * 导入评估数据
     */
    Map<String, Object> importAssessments(Map<String, Object> importData);

    /**
     * 导出评估数据
     */
    Map<String, Object> exportAssessments(Map<String, Object> exportParams);

    /**
     * 复制评估
     */
    Pm360Assessment copyAssessment(Long assessmentId, Map<String, Object> copyParams);

    /**
     * 获取评估模板
     */
    List<Map<String, Object>> getAssessmentTemplates(String templateType);

    /**
     * 应用评估模板
     */
    Pm360Assessment applyAssessmentTemplate(Map<String, Object> templateParams);

    /**
     * 刷新评估缓存
     */
    boolean refreshAssessmentCache(String cacheType);

    /**
     * 获取评估仪表板数据
     */
    Map<String, Object> getAssessmentDashboard(String dashboardType, Long organizationId);

    /**
     * 获取评估进度
     */
    Map<String, Object> getAssessmentProgress(Long assessmentId);

    /**
     * 获取评估参与者
     */
    List<Map<String, Object>> getAssessmentParticipants(Long assessmentId);

    /**
     * 添加评估参与者
     */
    boolean addAssessmentParticipants(Long assessmentId, List<Map<String, Object>> participants);

    /**
     * 移除评估参与者
     */
    boolean removeAssessmentParticipants(Long assessmentId, List<Long> participantIds);

    /**
     * 发送评估通知
     */
    boolean sendAssessmentNotification(Long assessmentId, Map<String, Object> notificationParams);

    /**
     * 获取评估反馈
     */
    List<Map<String, Object>> getAssessmentFeedback(Long assessmentId);

    /**
     * 提交评估反馈
     */
    boolean submitAssessmentFeedback(Long assessmentId, Map<String, Object> feedbackData);

    /**
     * 获取评估历史
     */
    List<Map<String, Object>> getAssessmentHistory(Long assessedUserId, String historyType);

    /**
     * 对比评估结果
     */
    Map<String, Object> compareAssessmentResults(List<Long> assessmentIds, String compareType);

    /**
     * 获取评估趋势
     */
    Map<String, Object> getAssessmentTrends(Long assessedUserId, String trendType, String timePeriod);

    /**
     * 获取评估建议
     */
    List<Map<String, Object>> getAssessmentRecommendations(Long assessmentId);

    /**
     * 生成改进计划
     */
    Map<String, Object> generateImprovementPlan(Long assessmentId, Map<String, Object> planParams);

    /**
     * 跟踪改进进度
     */
    Map<String, Object> trackImprovementProgress(Long assessmentId, Map<String, Object> trackingParams);

    /**
     * 评估校准
     */
    Map<String, Object> calibrateAssessment(Long assessmentId, Map<String, Object> calibrationParams);

    /**
     * 获取评估洞察
     */
    Map<String, Object> getAssessmentInsights(Long assessmentId, String insightType);

    /**
     * 预测评估结果
     */
    Map<String, Object> predictAssessmentResults(Long assessedUserId, Map<String, Object> predictionParams);

    /**
     * 智能评估建议
     */
    List<Map<String, Object>> getIntelligentAssessmentSuggestions(Long assessmentId);

    /**
     * 评估质量检查
     */
    Map<String, Object> checkAssessmentQuality(Long assessmentId);

    /**
     * 评估数据验证
     */
    Map<String, Object> validateAssessmentData(Long assessmentId);

    /**
     * 评估结果审核
     */
    boolean reviewAssessmentResults(Long assessmentId, Map<String, Object> reviewParams);

    /**
     * 评估结果确认
     */
    boolean confirmAssessmentResults(Long assessmentId, Map<String, Object> confirmParams);

    /**
     * 评估结果发布
     */
    boolean publishAssessmentResults(Long assessmentId, Map<String, Object> publishParams);

    /**
     * 评估结果归档
     */
    boolean archiveAssessmentResults(Long assessmentId, Map<String, Object> archiveParams);

    /**
     * 获取我的评估任务
     */
    List<Map<String, Object>> getMyAssessmentTasks(Long userId, String taskType);

    /**
     * 获取待评估列表
     */
    List<Map<String, Object>> getPendingAssessments(Long userId, String assessmentType);

    /**
     * 获取已完成评估列表
     */
    List<Map<String, Object>> getCompletedAssessments(Long userId, String assessmentType);

    /**
     * 评估提醒设置
     */
    boolean setAssessmentReminder(Long assessmentId, Map<String, Object> reminderParams);

    /**
     * 评估权限检查
     */
    Map<String, Object> checkAssessmentPermission(Long assessmentId, Long userId, String permissionType);
}
