package com.huabo.cybermonitor.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.cybermonitor.entity.OperationPlan;
import com.huabo.cybermonitor.vo.OperationPlanQueryVo;

import java.util.List;
import java.util.Map;

/**
 * 经营计划管理服务接口
 *
 * @author huabo
 * @since 2024-12-12
 */
public interface IOperationPlanService extends IService<OperationPlan> {

    /**
     * 分页查询经营计划管理
     *
     * @param queryVo 查询条件
     * @return 分页结果
     */
    IPage<OperationPlan> getOperationPlanPage(OperationPlanQueryVo queryVo);

    /**
     * 新增经营计划管理
     *
     * @param operationPlan 经营计划管理
     * @return 是否成功
     */
    boolean addOperationPlan(OperationPlan operationPlan);

    /**
     * 修改经营计划管理
     *
     * @param operationPlan 经营计划管理
     * @return 是否成功
     */
    boolean updateOperationPlan(OperationPlan operationPlan);

    /**
     * 删除经营计划管理
     *
     * @param planId   计划ID
     * @param updateBy 更新人
     * @return 是否成功
     */
    boolean deleteOperationPlan(String planId, String updateBy);

    /**
     * 批量删除经营计划管理
     *
     * @param planIds  计划ID列表
     * @param updateBy 更新人
     * @return 是否成功
     */
    boolean batchDeleteOperationPlan(List<String> planIds, String updateBy);

    /**
     * 启动计划制定
     *
     * @param planId   计划ID
     * @param updateBy 更新人
     * @return 是否成功
     */
    boolean startPlanFormulation(String planId, String updateBy);

    /**
     * 暂停计划制定
     *
     * @param planId   计划ID
     * @param updateBy 更新人
     * @return 是否成功
     */
    boolean pausePlanFormulation(String planId, String updateBy);

    /**
     * 恢复计划制定
     *
     * @param planId   计划ID
     * @param updateBy 更新人
     * @return 是否成功
     */
    boolean resumePlanFormulation(String planId, String updateBy);

    /**
     * 完成计划制定
     *
     * @param planId   计划ID
     * @param updateBy 更新人
     * @return 是否成功
     */
    boolean completePlanFormulation(String planId, String updateBy);

    /**
     * 取消计划制定
     *
     * @param planId   计划ID
     * @param updateBy 更新人
     * @return 是否成功
     */
    boolean cancelPlanFormulation(String planId, String updateBy);

    /**
     * 提交审批
     *
     * @param planId   计划ID
     * @param updateBy 更新人
     * @return 是否成功
     */
    boolean submitForApproval(String planId, String updateBy);

    /**
     * 初审
     *
     * @param planId       计划ID
     * @param approver     审批人
     * @param opinion      审批意见
     * @param approvalResult 审批结果
     * @param updateBy     更新人
     * @return 是否成功
     */
    boolean firstApproval(String planId, String approver, String opinion, String approvalResult, String updateBy);

    /**
     * 终审
     *
     * @param planId       计划ID
     * @param approver     审批人
     * @param opinion      审批意见
     * @param approvalResult 审批结果
     * @param updateBy     更新人
     * @return 是否成功
     */
    boolean finalApproval(String planId, String approver, String opinion, String approvalResult, String updateBy);

    /**
     * 启动计划执行
     *
     * @param planId   计划ID
     * @param updateBy 更新人
     * @return 是否成功
     */
    boolean startPlanExecution(String planId, String updateBy);

    /**
     * 暂停计划执行
     *
     * @param planId   计划ID
     * @param updateBy 更新人
     * @return 是否成功
     */
    boolean pausePlanExecution(String planId, String updateBy);

    /**
     * 恢复计划执行
     *
     * @param planId   计划ID
     * @param updateBy 更新人
     * @return 是否成功
     */
    boolean resumePlanExecution(String planId, String updateBy);

    /**
     * 完成计划执行
     *
     * @param planId   计划ID
     * @param updateBy 更新人
     * @return 是否成功
     */
    boolean completePlanExecution(String planId, String updateBy);

    /**
     * 计划调整
     *
     * @param planId            计划ID
     * @param adjustmentReason  调整原因
     * @param adjustmentContent 调整内容
     * @param updateBy          更新人
     * @return 是否成功
     */
    boolean adjustPlan(String planId, String adjustmentReason, String adjustmentContent, String updateBy);

    /**
     * 计划监控
     *
     * @param planId   计划ID
     * @param updateBy 更新人
     * @return 是否成功
     */
    boolean monitorPlan(String planId, String updateBy);

    /**
     * 效果评估
     *
     * @param planId              计划ID
     * @param effectivenessEvaluation 效果评估
     * @param updateBy            更新人
     * @return 是否成功
     */
    boolean evaluateEffectiveness(String planId, String effectivenessEvaluation, String updateBy);

    /**
     * 批量更新计划状态
     *
     * @param planIds  计划ID列表
     * @param status   状态
     * @param updateBy 更新人
     * @return 是否成功
     */
    boolean batchUpdatePlanStatus(List<String> planIds, String status, String updateBy);

    /**
     * 批量更新审批状态
     *
     * @param planIds  计划ID列表
     * @param status   状态
     * @param updateBy 更新人
     * @return 是否成功
     */
    boolean batchUpdateApprovalStatus(List<String> planIds, String status, String updateBy);

    /**
     * 批量更新执行状态
     *
     * @param planIds  计划ID列表
     * @param status   状态
     * @param updateBy 更新人
     * @return 是否成功
     */
    boolean batchUpdateExecutionStatus(List<String> planIds, String status, String updateBy);

    /**
     * 根据企业ID获取经营计划管理统计
     *
     * @param enterpriseId 企业ID
     * @return 统计结果
     */
    Map<String, Object> getStatisticsByEnterpriseId(String enterpriseId);

    /**
     * 根据企业ID获取计划类型分布
     *
     * @param enterpriseId 企业ID
     * @return 计划类型分布
     */
    List<Map<String, Object>> getPlanTypeDistribution(String enterpriseId);

    /**
     * 根据企业ID获取计划状态分布
     *
     * @param enterpriseId 企业ID
     * @return 计划状态分布
     */
    List<Map<String, Object>> getPlanStatusDistribution(String enterpriseId);

    /**
     * 根据企业ID获取审批状态分布
     *
     * @param enterpriseId 企业ID
     * @return 审批状态分布
     */
    List<Map<String, Object>> getApprovalStatusDistribution(String enterpriseId);

    /**
     * 根据企业ID获取执行状态分布
     *
     * @param enterpriseId 企业ID
     * @return 执行状态分布
     */
    List<Map<String, Object>> getExecutionStatusDistribution(String enterpriseId);

    /**
     * 根据企业ID获取执行进度分布
     *
     * @param enterpriseId 企业ID
     * @return 执行进度分布
     */
    List<Map<String, Object>> getExecutionProgressDistribution(String enterpriseId);

    /**
     * 根据企业ID获取计划执行趋势
     *
     * @param enterpriseId 企业ID
     * @param months       月份数
     * @return 计划执行趋势
     */
    List<Map<String, Object>> getPlanExecutionTrend(String enterpriseId, Integer months);

    /**
     * 根据企业ID获取目标达成趋势
     *
     * @param enterpriseId 企业ID
     * @param months       月份数
     * @return 目标达成趋势
     */
    List<Map<String, Object>> getTargetAchievementTrend(String enterpriseId, Integer months);

    /**
     * 根据企业ID获取计划调整统计
     *
     * @param enterpriseId 企业ID
     * @return 计划调整统计
     */
    Map<String, Object> getPlanAdjustmentStatistics(String enterpriseId);

    /**
     * 根据企业ID获取计划调整趋势
     *
     * @param enterpriseId 企业ID
     * @param months       月份数
     * @return 计划调整趋势
     */
    List<Map<String, Object>> getPlanAdjustmentTrend(String enterpriseId, Integer months);

    /**
     * 根据企业ID获取部门计划统计
     *
     * @param enterpriseId 企业ID
     * @return 部门计划统计
     */
    List<Map<String, Object>> getDepartmentPlanStatistics(String enterpriseId);

    /**
     * 根据企业ID获取负责人计划统计
     *
     * @param enterpriseId 企业ID
     * @return 负责人计划统计
     */
    List<Map<String, Object>> getManagerPlanStatistics(String enterpriseId);

    /**
     * 根据企业ID获取审批人员统计
     *
     * @param enterpriseId 企业ID
     * @return 审批人员统计
     */
    List<Map<String, Object>> getApproverStatistics(String enterpriseId);

    /**
     * 根据企业ID获取制定方法统计
     *
     * @param enterpriseId 企业ID
     * @return 制定方法统计
     */
    List<Map<String, Object>> getFormulationMethodStatistics(String enterpriseId);

    /**
     * 根据企业ID获取监控频率统计
     *
     * @param enterpriseId 企业ID
     * @return 监控频率统计
     */
    List<Map<String, Object>> getMonitoringFrequencyStatistics(String enterpriseId);

    /**
     * 根据企业ID获取计划执行效率分析
     *
     * @param enterpriseId 企业ID
     * @return 计划执行效率分析
     */
    List<Map<String, Object>> getPlanExecutionEfficiencyAnalysis(String enterpriseId);

    /**
     * 根据企业ID获取目标达成效果分析
     *
     * @param enterpriseId 企业ID
     * @return 目标达成效果分析
     */
    List<Map<String, Object>> getTargetAchievementEffectivenessAnalysis(String enterpriseId);

    /**
     * 根据企业ID获取执行质量分析
     *
     * @param enterpriseId 企业ID
     * @return 执行质量分析
     */
    List<Map<String, Object>> getExecutionQualityAnalysis(String enterpriseId);

    /**
     * 根据企业ID获取创新性分析
     *
     * @param enterpriseId 企业ID
     * @return 创新性分析
     */
    List<Map<String, Object>> getInnovationAnalysis(String enterpriseId);

    /**
     * 根据企业ID获取计划改进建议
     *
     * @param enterpriseId 企业ID
     * @return 计划改进建议
     */
    List<Map<String, Object>> getPlanImprovementSuggestions(String enterpriseId);

    /**
     * 根据企业ID获取成功经验总结
     *
     * @param enterpriseId 企业ID
     * @return 成功经验总结
     */
    List<Map<String, Object>> getSuccessExperiences(String enterpriseId);

    /**
     * 根据企业ID获取失败教训总结
     *
     * @param enterpriseId 企业ID
     * @return 失败教训总结
     */
    List<Map<String, Object>> getFailureLessons(String enterpriseId);

    /**
     * 根据企业ID获取能力提升建议
     *
     * @param enterpriseId 企业ID
     * @return 能力提升建议
     */
    List<Map<String, Object>> getCapabilityEnhancementSuggestions(String enterpriseId);

    /**
     * 根据企业ID获取即将到期的计划
     *
     * @param enterpriseId 企业ID
     * @param days         天数
     * @return 即将到期的计划
     */
    List<OperationPlan> getUpcomingPlans(String enterpriseId, Integer days);

    /**
     * 根据企业ID获取逾期的计划
     *
     * @param enterpriseId 企业ID
     * @return 逾期的计划
     */
    List<OperationPlan> getOverduePlans(String enterpriseId);

    /**
     * 根据企业ID获取待审批的计划
     *
     * @param enterpriseId 企业ID
     * @return 待审批的计划
     */
    List<OperationPlan> getPendingApprovalPlans(String enterpriseId);

    /**
     * 根据企业ID获取执行异常的计划
     *
     * @param enterpriseId 企业ID
     * @return 执行异常的计划
     */
    List<OperationPlan> getAbnormalExecutionPlans(String enterpriseId);

    /**
     * 根据企业ID获取需要调整的计划
     *
     * @param enterpriseId 企业ID
     * @return 需要调整的计划
     */
    List<OperationPlan> getPlansNeedingAdjustment(String enterpriseId);

    /**
     * 导出经营计划管理
     *
     * @param queryVo 查询条件
     * @return 导出结果
     */
    Map<String, Object> exportOperationPlan(OperationPlanQueryVo queryVo);

    /**
     * 生成经营计划管理报告
     *
     * @param enterpriseId 企业ID
     * @param reportType   报告类型
     * @return 报告结果
     */
    Map<String, Object> generateOperationPlanReport(String enterpriseId, String reportType);
}
