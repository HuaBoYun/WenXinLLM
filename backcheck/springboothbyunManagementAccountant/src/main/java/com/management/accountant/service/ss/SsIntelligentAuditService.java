package com.management.accountant.service.ss;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.entity.ss.SsIntelligentAudit;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 智能审核服务接口
 * 
 * @author AI Assistant
 * @since 2025-01-27
 */
public interface SsIntelligentAuditService extends IService<SsIntelligentAudit> {

    /**
     * 分页查询智能审核列表
     */
    IPage<SsIntelligentAudit> getAuditPage(Page<SsIntelligentAudit> page, String auditTitle, String auditType,
                                           String auditStatus, String riskLevel, Long auditorId, Long auditDeptId,
                                           LocalDateTime startTime, LocalDateTime endTime, Long tenantId);

    /**
     * 根据ID查询智能审核详情
     */
    SsIntelligentAudit getAuditById(Long auditId, Long tenantId);

    /**
     * 根据审核编码查询
     */
    SsIntelligentAudit getByAuditCode(String auditCode, Long tenantId);

    /**
     * 根据审核类型查询列表
     */
    List<SsIntelligentAudit> getByAuditType(String auditType, Long tenantId);

    /**
     * 根据审核状态查询列表
     */
    List<SsIntelligentAudit> getByAuditStatus(String auditStatus, Long tenantId);

    /**
     * 根据风险等级查询列表
     */
    List<SsIntelligentAudit> getByRiskLevel(String riskLevel, Long tenantId);

    /**
     * 根据审核人员ID查询列表
     */
    List<SsIntelligentAudit> getByAuditorId(Long auditorId, Long tenantId);

    /**
     * 根据审核部门ID查询列表
     */
    List<SsIntelligentAudit> getByAuditDeptId(Long auditDeptId, Long tenantId);

    /**
     * 根据目标对象查询列表
     */
    List<SsIntelligentAudit> getByTargetObject(Long targetObjectId, String targetObjectType, Long tenantId);

    /**
     * 创建智能审核
     */
    boolean createAudit(SsIntelligentAudit audit);

    /**
     * 更新智能审核
     */
    boolean updateAudit(SsIntelligentAudit audit);

    /**
     * 删除智能审核
     */
    boolean deleteAudit(Long auditId, Long tenantId);

    /**
     * 批量删除智能审核
     */
    boolean batchDeleteAudits(List<Long> auditIds, Long tenantId);

    /**
     * 启动审核
     */
    boolean startAudit(Long auditId, Long auditorId, String auditorName, Long auditDeptId, String auditDeptName, Long tenantId);

    /**
     * 暂停审核
     */
    boolean pauseAudit(Long auditId, String reason, Long tenantId);

    /**
     * 恢复审核
     */
    boolean resumeAudit(Long auditId, Long tenantId);

    /**
     * 完成审核
     */
    boolean completeAudit(Long auditId, String auditResult, String auditConclusion, Long tenantId);

    /**
     * 取消审核
     */
    boolean cancelAudit(Long auditId, String reason, Long tenantId);

    /**
     * 复核审核
     */
    boolean reviewAudit(Long auditId, Long reviewerId, String reviewerName, String reviewComments, String reviewResult, Long tenantId);

    /**
     * 处理审核
     */
    boolean processAudit(Long auditId, Long processorId, String processorName, String processingActions, String processingResult, Long tenantId);

    /**
     * 分配审核人员
     */
    boolean assignAuditor(Long auditId, Long auditorId, String auditorName, Long auditDeptId, String auditDeptName, Long tenantId);

    /**
     * 批量分配审核人员
     */
    boolean batchAssignAuditor(List<Long> auditIds, Long auditorId, String auditorName, Long auditDeptId, String auditDeptName, Long tenantId);

    /**
     * 执行智能审核
     */
    boolean executeIntelligentAudit(Long auditId, Long tenantId);

    /**
     * 批量执行智能审核
     */
    boolean batchExecuteIntelligentAudit(List<Long> auditIds, Long tenantId);

    /**
     * 应用机器学习模型
     */
    boolean applyMlModel(Long auditId, Long mlModelId, String mlModelVersion, Long tenantId);

    /**
     * 异常检测
     */
    boolean detectAnomaly(Long auditId, Long tenantId);

    /**
     * 风险评估
     */
    boolean assessRisk(Long auditId, Long tenantId);

    /**
     * 生成预警
     */
    boolean generateWarning(Long auditId, String warningLevel, String warningMessage, Long tenantId);

    /**
     * 添加跟进记录
     */
    boolean addFollowUpRecord(Long auditId, String followUpRecord, Long tenantId);

    /**
     * 更新跟进状态
     */
    boolean updateFollowUpStatus(Long auditId, String followUpStatus, LocalDateTime nextFollowUpTime, Long tenantId);

    /**
     * 查询待处理的审核列表
     */
    List<SsIntelligentAudit> getPendingAudits(Long tenantId);

    /**
     * 查询需要复核的审核列表
     */
    List<SsIntelligentAudit> getNeedReview(Long tenantId);

    /**
     * 查询需要跟进的审核列表
     */
    List<SsIntelligentAudit> getNeedFollowUp(Long tenantId);

    /**
     * 查询高风险审核列表
     */
    List<SsIntelligentAudit> getHighRiskAudits(Long tenantId);

    /**
     * 查询异常审核列表
     */
    List<SsIntelligentAudit> getAnomalyAudits(Long tenantId);

    /**
     * 查询超时审核列表
     */
    List<SsIntelligentAudit> getOverdueAudits(LocalDateTime currentTime, Long tenantId);

    /**
     * 统计审核数据
     */
    Map<String, Object> getAuditStatistics(LocalDateTime startTime, LocalDateTime endTime, Long tenantId);

    /**
     * 统计审核状态分布
     */
    List<Map<String, Object>> getAuditStatusDistribution(LocalDateTime startTime, LocalDateTime endTime, Long tenantId);

    /**
     * 统计审核类型分布
     */
    List<Map<String, Object>> getAuditTypeDistribution(LocalDateTime startTime, LocalDateTime endTime, Long tenantId);

    /**
     * 统计风险等级分布
     */
    List<Map<String, Object>> getRiskLevelDistribution(LocalDateTime startTime, LocalDateTime endTime, Long tenantId);

    /**
     * 统计审核趋势
     */
    List<Map<String, Object>> getAuditTrend(LocalDateTime startTime, LocalDateTime endTime, Long tenantId);

    /**
     * 统计审核效率
     */
    Map<String, Object> getAuditEfficiency(LocalDateTime startTime, LocalDateTime endTime, Long tenantId);

    /**
     * 统计审核质量
     */
    Map<String, Object> getAuditQuality(LocalDateTime startTime, LocalDateTime endTime, Long tenantId);

    /**
     * 查询审核排行榜
     */
    List<Map<String, Object>> getAuditRanking(String rankingType, LocalDateTime startTime, LocalDateTime endTime, Integer limit, Long tenantId);

    /**
     * 查询审核人员工作量统计
     */
    List<Map<String, Object>> getAuditorWorkload(LocalDateTime startTime, LocalDateTime endTime, Long tenantId);

    /**
     * 查询部门审核统计
     */
    List<Map<String, Object>> getDeptAuditStatistics(LocalDateTime startTime, LocalDateTime endTime, Long tenantId);

    /**
     * 检查审核编码是否存在
     */
    boolean checkAuditCodeExists(String auditCode, Long auditId, Long tenantId);

    /**
     * 检查目标对象是否有进行中的审核
     */
    boolean checkTargetObjectInProgress(Long targetObjectId, String targetObjectType, Long auditId, Long tenantId);

    /**
     * 计算平均审核时长
     */
    BigDecimal calculateAverageAuditDuration(String auditType, LocalDateTime startTime, LocalDateTime endTime, Long tenantId);

    /**
     * 计算审核成功率
     */
    BigDecimal calculateAuditSuccessRate(String auditType, LocalDateTime startTime, LocalDateTime endTime, Long tenantId);

    /**
     * 优化审核规则
     */
    boolean optimizeAuditRules(Long auditRuleId, Long tenantId);

    /**
     * 训练机器学习模型
     */
    boolean trainMlModel(Long trainingDatasetId, Long tenantId);

    /**
     * 评估模型性能
     */
    Map<String, Object> evaluateModelPerformance(Long mlModelId, Long tenantId);

    /**
     * 生成审核报告
     */
    Map<String, Object> generateAuditReport(LocalDateTime startTime, LocalDateTime endTime, String reportType, Long tenantId);

    /**
     * 导出审核数据
     */
    List<SsIntelligentAudit> exportAuditData(String auditType, String auditStatus, String riskLevel, LocalDateTime startTime, LocalDateTime endTime, Long tenantId);

    /**
     * 智能推荐审核规则
     */
    List<Map<String, Object>> recommendAuditRules(Long targetObjectId, String targetObjectType, Long tenantId);

    /**
     * 预测审核风险
     */
    Map<String, Object> predictAuditRisk(Long targetObjectId, String targetObjectType, Long tenantId);

    /**
     * 自动化审核流程
     */
    boolean automateAuditProcess(Long auditId, Long tenantId);

    /**
     * 发送审核通知
     */
    boolean sendAuditNotification(Long auditId, String notificationType, Long tenantId);

    /**
     * 批量发送审核通知
     */
    boolean batchSendAuditNotifications(List<Long> auditIds, String notificationType, Long tenantId);
}
