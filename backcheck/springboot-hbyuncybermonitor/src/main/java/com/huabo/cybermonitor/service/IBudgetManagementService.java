package com.huabo.cybermonitor.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.cybermonitor.entity.BudgetManagement;
import com.huabo.cybermonitor.vo.BudgetManagementQueryVo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 预算管理服务接口
 *
 * @author huabo
 * @since 2024-12-12
 */
public interface IBudgetManagementService extends IService<BudgetManagement> {

    /**
     * 分页查询预算管理
     *
     * @param queryVo 查询条件
     * @return 分页结果
     */
    IPage<BudgetManagement> getBudgetManagementPage(BudgetManagementQueryVo queryVo);

    /**
     * 新增预算管理
     *
     * @param budgetManagement 预算管理
     * @return 是否成功
     */
    boolean addBudgetManagement(BudgetManagement budgetManagement);

    /**
     * 修改预算管理
     *
     * @param budgetManagement 预算管理
     * @return 是否成功
     */
    boolean updateBudgetManagement(BudgetManagement budgetManagement);

    /**
     * 删除预算管理
     *
     * @param budgetId 预算ID
     * @param updateBy 更新人
     * @return 是否成功
     */
    boolean deleteBudgetManagement(String budgetId, String updateBy);

    /**
     * 批量删除预算管理
     *
     * @param budgetIds 预算ID列表
     * @param updateBy  更新人
     * @return 是否成功
     */
    boolean batchDeleteBudgetManagement(List<String> budgetIds, String updateBy);

    /**
     * 启动预算编制
     *
     * @param budgetId 预算ID
     * @param updateBy 更新人
     * @return 是否成功
     */
    boolean startBudgetCompilation(String budgetId, String updateBy);

    /**
     * 暂停预算编制
     *
     * @param budgetId 预算ID
     * @param updateBy 更新人
     * @return 是否成功
     */
    boolean pauseBudgetCompilation(String budgetId, String updateBy);

    /**
     * 恢复预算编制
     *
     * @param budgetId 预算ID
     * @param updateBy 更新人
     * @return 是否成功
     */
    boolean resumeBudgetCompilation(String budgetId, String updateBy);

    /**
     * 完成预算编制
     *
     * @param budgetId 预算ID
     * @param updateBy 更新人
     * @return 是否成功
     */
    boolean completeBudgetCompilation(String budgetId, String updateBy);

    /**
     * 取消预算编制
     *
     * @param budgetId 预算ID
     * @param updateBy 更新人
     * @return 是否成功
     */
    boolean cancelBudgetCompilation(String budgetId, String updateBy);

    /**
     * 提交审批
     *
     * @param budgetId 预算ID
     * @param updateBy 更新人
     * @return 是否成功
     */
    boolean submitForApproval(String budgetId, String updateBy);

    /**
     * 初审
     *
     * @param budgetId       预算ID
     * @param approver       审批人
     * @param opinion        审批意见
     * @param approvalResult 审批结果
     * @param updateBy       更新人
     * @return 是否成功
     */
    boolean firstApproval(String budgetId, String approver, String opinion, String approvalResult, String updateBy);

    /**
     * 终审
     *
     * @param budgetId       预算ID
     * @param approver       审批人
     * @param opinion        审批意见
     * @param approvalResult 审批结果
     * @param updateBy       更新人
     * @return 是否成功
     */
    boolean finalApproval(String budgetId, String approver, String opinion, String approvalResult, String updateBy);

    /**
     * 启动预算执行
     *
     * @param budgetId 预算ID
     * @param updateBy 更新人
     * @return 是否成功
     */
    boolean startBudgetExecution(String budgetId, String updateBy);

    /**
     * 暂停预算执行
     *
     * @param budgetId 预算ID
     * @param updateBy 更新人
     * @return 是否成功
     */
    boolean pauseBudgetExecution(String budgetId, String updateBy);

    /**
     * 恢复预算执行
     *
     * @param budgetId 预算ID
     * @param updateBy 更新人
     * @return 是否成功
     */
    boolean resumeBudgetExecution(String budgetId, String updateBy);

    /**
     * 完成预算执行
     *
     * @param budgetId 预算ID
     * @param updateBy 更新人
     * @return 是否成功
     */
    boolean completeBudgetExecution(String budgetId, String updateBy);

    /**
     * 预算调整
     *
     * @param budgetId          预算ID
     * @param adjustmentReason  调整原因
     * @param adjustmentContent 调整内容
     * @param updateBy          更新人
     * @return 是否成功
     */
    boolean adjustBudget(String budgetId, String adjustmentReason, String adjustmentContent, String updateBy);

    boolean adjustBudget(String budgetId, String adjustmentReason, String adjustmentContent, BigDecimal adjustmentAmount, String updateBy);
    /**
     * 预算监控
     *
     * @param budgetId 预算ID
     * @param updateBy 更新人
     * @return 是否成功
     */
    boolean monitorBudget(String budgetId, String updateBy);

    /**
     * 绩效评价
     *
     * @param budgetId             预算ID
     * @param performanceEvaluation 绩效评价
     * @param updateBy             更新人
     * @return 是否成功
     */
    boolean evaluatePerformance(String budgetId, String performanceEvaluation, String updateBy);

    /**
     * 批量更新预算状态
     *
     * @param budgetIds 预算ID列表
     * @param status    状态
     * @param updateBy  更新人
     * @return 是否成功
     */
    boolean batchUpdateBudgetStatus(List<String> budgetIds, String status, String updateBy);

    /**
     * 批量更新审批状态
     *
     * @param budgetIds 预算ID列表
     * @param status    状态
     * @param updateBy  更新人
     * @return 是否成功
     */
    boolean batchUpdateApprovalStatus(List<String> budgetIds, String status, String updateBy);

    /**
     * 根据企业ID获取预算管理统计
     *
     * @param enterpriseId 企业ID
     * @return 统计结果
     */
    Map<String, Object> getStatisticsByEnterpriseId(String enterpriseId);

    /**
     * 根据企业ID获取预算类型分布
     *
     * @param enterpriseId 企业ID
     * @return 预算类型分布
     */
    List<Map<String, Object>> getBudgetTypeDistribution(String enterpriseId);

    /**
     * 根据企业ID获取预算状态分布
     *
     * @param enterpriseId 企业ID
     * @return 预算状态分布
     */
    List<Map<String, Object>> getBudgetStatusDistribution(String enterpriseId);

    /**
     * 根据企业ID获取审批状态分布
     *
     * @param enterpriseId 企业ID
     * @return 审批状态分布
     */
    List<Map<String, Object>> getApprovalStatusDistribution(String enterpriseId);

    /**
     * 根据企业ID获取执行率分布
     *
     * @param enterpriseId 企业ID
     * @return 执行率分布
     */
    List<Map<String, Object>> getExecutionRateDistribution(String enterpriseId);

    /**
     * 根据企业ID获取预算执行趋势
     *
     * @param enterpriseId 企业ID
     * @param months       月份数
     * @return 预算执行趋势
     */
    List<Map<String, Object>> getBudgetExecutionTrend(String enterpriseId, Integer months);

    /**
     * 根据企业ID获取预算差异趋势
     *
     * @param enterpriseId 企业ID
     * @param months       月份数
     * @return 预算差异趋势
     */
    List<Map<String, Object>> getBudgetVarianceTrend(String enterpriseId, Integer months);

    /**
     * 根据企业ID获取预算调整统计
     *
     * @param enterpriseId 企业ID
     * @return 预算调整统计
     */
    Map<String, Object> getBudgetAdjustmentStatistics(String enterpriseId);

    /**
     * 根据企业ID获取预算调整趋势
     *
     * @param enterpriseId 企业ID
     * @param months       月份数
     * @return 预算调整趋势
     */
    List<Map<String, Object>> getBudgetAdjustmentTrend(String enterpriseId, Integer months);

    /**
     * 根据企业ID获取部门预算统计
     *
     * @param enterpriseId 企业ID
     * @return 部门预算统计
     */
    List<Map<String, Object>> getDepartmentBudgetStatistics(String enterpriseId);

    /**
     * 根据企业ID获取负责人预算统计
     *
     * @param enterpriseId 企业ID
     * @return 负责人预算统计
     */
    List<Map<String, Object>> getManagerBudgetStatistics(String enterpriseId);

    /**
     * 根据企业ID获取审批人员统计
     *
     * @param enterpriseId 企业ID
     * @return 审批人员统计
     */
    List<Map<String, Object>> getApproverStatistics(String enterpriseId);

    /**
     * 根据企业ID获取编制方法统计
     *
     * @param enterpriseId 企业ID
     * @return 编制方法统计
     */
    List<Map<String, Object>> getCompilationMethodStatistics(String enterpriseId);

    /**
     * 根据企业ID获取监控频率统计
     *
     * @param enterpriseId 企业ID
     * @return 监控频率统计
     */
    List<Map<String, Object>> getMonitoringFrequencyStatistics(String enterpriseId);

    /**
     * 根据企业ID获取预算执行效率分析
     *
     * @param enterpriseId 企业ID
     * @return 预算执行效率分析
     */
    List<Map<String, Object>> getBudgetExecutionEfficiencyAnalysis(String enterpriseId);

    /**
     * 根据企业ID获取预算控制效果分析
     *
     * @param enterpriseId 企业ID
     * @return 预算控制效果分析
     */
    List<Map<String, Object>> getBudgetControlEffectivenessAnalysis(String enterpriseId);

    /**
     * 根据企业ID获取风险评估分析
     *
     * @param enterpriseId 企业ID
     * @return 风险评估分析
     */
    List<Map<String, Object>> getRiskAssessmentAnalysis(String enterpriseId);

    /**
     * 根据企业ID获取绩效评价分析
     *
     * @param enterpriseId 企业ID
     * @return 绩效评价分析
     */
    List<Map<String, Object>> getPerformanceEvaluationAnalysis(String enterpriseId);

    /**
     * 根据企业ID获取预算改进建议
     *
     * @param enterpriseId 企业ID
     * @return 预算改进建议
     */
    List<Map<String, Object>> getBudgetImprovementSuggestions(String enterpriseId);

    /**
     * 根据企业ID获取最佳实践案例
     *
     * @param enterpriseId 企业ID
     * @return 最佳实践案例
     */
    List<Map<String, Object>> getBestPracticeCases(String enterpriseId);

    /**
     * 根据企业ID获取能力提升建议
     *
     * @param enterpriseId 企业ID
     * @return 能力提升建议
     */
    List<Map<String, Object>> getCapabilityEnhancementSuggestions(String enterpriseId);

    /**
     * 根据企业ID获取即将到期的预算
     *
     * @param enterpriseId 企业ID
     * @param days         天数
     * @return 即将到期的预算
     */
    List<BudgetManagement> getUpcomingBudgets(String enterpriseId, Integer days);

    /**
     * 根据企业ID获取逾期的预算
     *
     * @param enterpriseId 企业ID
     * @return 逾期的预算
     */
    List<BudgetManagement> getOverdueBudgets(String enterpriseId);

    /**
     * 根据企业ID获取待审批的预算
     *
     * @param enterpriseId 企业ID
     * @return 待审批的预算
     */
    List<BudgetManagement> getPendingApprovalBudgets(String enterpriseId);

    /**
     * 根据企业ID获取执行异常的预算
     *
     * @param enterpriseId 企业ID
     * @return 执行异常的预算
     */
    List<BudgetManagement> getAbnormalExecutionBudgets(String enterpriseId);

    /**
     * 根据企业ID获取需要调整的预算
     *
     * @param enterpriseId 企业ID
     * @return 需要调整的预算
     */
    List<BudgetManagement> getBudgetsNeedingAdjustment(String enterpriseId);

    /**
     * 导出预算管理
     *
     * @param queryVo 查询条件
     * @return 导出结果
     */
    Map<String, Object> exportBudgetManagement(BudgetManagementQueryVo queryVo);

    /**
     * 生成预算管理报告
     *
     * @param enterpriseId 企业ID
     * @param reportType   报告类型
     * @return 报告结果
     */
    Map<String, Object> generateBudgetManagementReport(String enterpriseId, String reportType);
}
