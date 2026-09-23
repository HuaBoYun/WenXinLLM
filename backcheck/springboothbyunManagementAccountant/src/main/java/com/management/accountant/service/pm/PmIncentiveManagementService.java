package com.management.accountant.service.pm;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.entity.pm.PmIncentiveManagement;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 激励管理服务接口
 *
 * @author AI Assistant
 * @since 2024-01-01
 */
public interface PmIncentiveManagementService extends IService<PmIncentiveManagement> {

    /**
     * 分页查询激励管理列表
     */
    IPage<PmIncentiveManagement> getIncentiveManagementPage(Page<PmIncentiveManagement> page, 
                                                            String incentiveTitle,
                                                            String incentiveType,
                                                            String incentiveStatus,
                                                            Integer incentiveYear,
                                                            Long targetDeptId,
                                                            Long incentiveOwnerId,
                                                            LocalDateTime startTime,
                                                            LocalDateTime endTime,
                                                            Long tenantId);

    /**
     * 根据激励编码查询
     */
    PmIncentiveManagement getByIncentiveCode(String incentiveCode, Long tenantId);

    /**
     * 根据激励类型查询列表
     */
    List<PmIncentiveManagement> getByIncentiveType(String incentiveType, Long tenantId);

    /**
     * 根据激励状态查询列表
     */
    List<PmIncentiveManagement> getByIncentiveStatus(String incentiveStatus, Long tenantId);

    /**
     * 根据部门ID查询激励列表
     */
    List<PmIncentiveManagement> getByDeptId(Long targetDeptId, Long tenantId);

    /**
     * 根据负责人ID查询激励列表
     */
    List<PmIncentiveManagement> getByOwnerId(Long incentiveOwnerId, Long tenantId);

    /**
     * 根据年度查询激励列表
     */
    List<PmIncentiveManagement> getByYear(Integer incentiveYear, Long tenantId);

    /**
     * 根据季度查询激励列表
     */
    List<PmIncentiveManagement> getByQuarter(Integer incentiveYear, Integer incentiveQuarter, Long tenantId);

    /**
     * 根据月份查询激励列表
     */
    List<PmIncentiveManagement> getByMonth(Integer incentiveYear, Integer incentiveMonth, Long tenantId);

    /**
     * 创建激励方案
     */
    boolean createIncentiveScheme(PmIncentiveManagement incentiveManagement);

    /**
     * 更新激励方案
     */
    boolean updateIncentiveScheme(PmIncentiveManagement incentiveManagement);

    /**
     * 删除激励方案
     */
    boolean deleteIncentiveScheme(Long incentiveId, Long tenantId);

    /**
     * 批量删除激励方案
     */
    boolean batchDeleteIncentiveSchemes(List<Long> incentiveIds, Long tenantId);

    /**
     * 启动激励方案
     */
    boolean startIncentiveScheme(Long incentiveId, Long tenantId);

    /**
     * 暂停激励方案
     */
    boolean suspendIncentiveScheme(Long incentiveId, String reason, Long tenantId);

    /**
     * 完成激励方案
     */
    boolean completeIncentiveScheme(Long incentiveId, Long tenantId);

    /**
     * 取消激励方案
     */
    boolean cancelIncentiveScheme(Long incentiveId, String reason, Long tenantId);

    /**
     * 审批激励方案
     */
    boolean approveIncentiveScheme(Long incentiveId, String approvalComments, Long approverId, String approverName, Long tenantId);

    /**
     * 拒绝激励方案
     */
    boolean rejectIncentiveScheme(Long incentiveId, String approvalComments, Long approverId, String approverName, Long tenantId);

    /**
     * 发放激励
     */
    boolean distributeIncentive(Long incentiveId, String distributionMethod, Long tenantId);

    /**
     * 批量发放激励
     */
    boolean batchDistributeIncentives(List<Long> incentiveIds, String distributionMethod, Long tenantId);

    /**
     * 计算激励金额
     */
    BigDecimal calculateIncentiveAmount(Long incentiveId, Map<String, Object> parameters, Long tenantId);

    /**
     * 批量计算激励金额
     */
    Map<Long, BigDecimal> batchCalculateIncentiveAmounts(List<Long> incentiveIds, Map<String, Object> parameters, Long tenantId);

    /**
     * 查询待审批的激励列表
     */
    List<PmIncentiveManagement> getPendingApproval(Long tenantId);

    /**
     * 查询待发放的激励列表
     */
    List<PmIncentiveManagement> getPendingDistribution(Long tenantId);

    /**
     * 查询需要跟进的激励列表
     */
    List<PmIncentiveManagement> getNeedFollowUp(Long tenantId);

    /**
     * 查询即将到期的激励列表
     */
    List<PmIncentiveManagement> getUpcomingDeadline(LocalDateTime deadline, Long tenantId);

    /**
     * 查询超期的激励列表
     */
    List<PmIncentiveManagement> getOverdue(LocalDateTime currentTime, Long tenantId);

    /**
     * 添加跟进记录
     */
    boolean addFollowUpRecord(Long incentiveId, String followUpRecord, Long tenantId);

    /**
     * 更新跟进状态
     */
    boolean updateFollowUpStatus(Long incentiveId, String followUpStatus, Long tenantId);

    /**
     * 批量更新跟进状态
     */
    boolean batchUpdateFollowUpStatus(List<Long> incentiveIds, String followUpStatus, Long tenantId);

    /**
     * 发送提醒通知
     */
    boolean sendReminderNotification(Long incentiveId, String notificationType, Long tenantId);

    /**
     * 批量发送提醒通知
     */
    boolean batchSendReminderNotifications(List<Long> incentiveIds, String notificationType, Long tenantId);

    /**
     * 统计激励数据
     */
    Map<String, Object> getIncentiveStatistics(Integer incentiveYear, Long tenantId);

    /**
     * 统计激励状态分布
     */
    List<Map<String, Object>> getIncentiveStatusDistribution(Integer incentiveYear, Long tenantId);

    /**
     * 统计激励类型分布
     */
    List<Map<String, Object>> getIncentiveTypeDistribution(Integer incentiveYear, Long tenantId);

    /**
     * 统计激励完成趋势
     */
    List<Map<String, Object>> getIncentiveCompletionTrend(LocalDateTime startTime, LocalDateTime endTime, Long tenantId);

    /**
     * 统计激励金额分布
     */
    List<Map<String, Object>> getIncentiveAmountDistribution(Integer incentiveYear, Long tenantId);

    /**
     * 统计激励效果分布
     */
    List<Map<String, Object>> getIncentiveEffectivenessDistribution(Integer incentiveYear, Long tenantId);

    /**
     * 查询激励排行榜
     */
    List<Map<String, Object>> getIncentiveRanking(Integer incentiveYear, String rankingType, Integer limit, Long tenantId);

    /**
     * 检查激励编码是否存在
     */
    boolean checkIncentiveCodeExists(String incentiveCode, Long incentiveId, Long tenantId);

    /**
     * 检查时间冲突
     */
    boolean checkTimeConflict(Long incentiveId, Long targetDeptId, String incentiveType, 
                              LocalDateTime startTime, LocalDateTime endTime, Long tenantId);

    /**
     * 计算激励总金额
     */
    BigDecimal calculateTotalIncentiveAmount(Integer incentiveYear, Long tenantId);

    /**
     * 计算部门激励金额
     */
    BigDecimal calculateDeptIncentiveAmount(Long targetDeptId, Integer incentiveYear, Long tenantId);

    /**
     * 查询激励详情（包含关联信息）
     */
    Map<String, Object> getIncentiveDetailWithRelations(Long incentiveId, Long tenantId);

    /**
     * 查询激励历史记录
     */
    List<Map<String, Object>> getIncentiveHistory(Long incentiveId, Long tenantId);

    /**
     * 导出激励数据
     */
    List<Map<String, Object>> exportIncentiveData(Integer incentiveYear, String incentiveType, 
                                                   String incentiveStatus, Long targetDeptId, Long tenantId);

    /**
     * 智能推荐激励方案
     */
    List<Map<String, Object>> getRecommendedIncentiveSchemes(Long targetDeptId, String incentiveType, 
                                                              BigDecimal budgetRange, Long tenantId);

    /**
     * 分析激励效果
     */
    Map<String, Object> analyzeIncentiveEffectiveness(Long incentiveId, Long tenantId);

    /**
     * 生成激励报告数据
     */
    Map<String, Object> generateIncentiveReportData(Integer incentiveYear, String reportType, Long tenantId);

    /**
     * 查询激励优化建议
     */
    List<Map<String, Object>> getIncentiveOptimizationSuggestions(Long incentiveId, Long tenantId);

    /**
     * 复制激励方案
     */
    boolean copyIncentiveScheme(Long sourceIncentiveId, String newIncentiveTitle, Long tenantId);

    /**
     * 激励方案模板管理
     */
    boolean saveAsTemplate(Long incentiveId, String templateName, Long tenantId);

    /**
     * 从模板创建激励方案
     */
    boolean createFromTemplate(Long templateId, String incentiveTitle, Long tenantId);

    /**
     * 智能分析激励趋势
     */
    Map<String, Object> analyzeIncentiveTrends(Integer incentiveYear, Long tenantId);

    /**
     * 预测激励需求
     */
    Map<String, Object> predictIncentiveNeeds(Long targetDeptId, Integer targetYear, Long tenantId);

    /**
     * 优化激励配置
     */
    Map<String, Object> optimizeIncentiveConfiguration(Long incentiveId, Long tenantId);
}
