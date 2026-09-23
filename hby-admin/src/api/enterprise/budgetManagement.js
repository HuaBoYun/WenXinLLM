import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// 分页查询预算管理
export function getBudgetManagementPage(data) {
  return transData({
    url: '/monitor/v1/enterprise/financial/budget/list',
    method: 'post',
    data
  })
}

// 根据ID获取预算管理详情
export function getBudgetManagementById(budgetId) {
  return transData({
    url: `/monitor/v1/enterprise/financial/budget/${budgetId}`,
    method: 'get'
  })
}

// 新增预算管理
export function addBudgetManagement(data) {
  return transData({
    url: '/monitor/v1/enterprise/financial/budget/add',
    method: 'post',
    data
  })
}

// 修改预算管理
export function updateBudgetManagement(data) {
  return transData({
    url: '/monitor/v1/enterprise/financial/budget/update',
    method: 'put',
    data
  })
}

// 删除预算管理
export function deleteBudgetManagement(budgetId, updateBy) {
  return transData({
    url: `/monitor/v1/enterprise/financial/budget/${budgetId}`,
    method: 'delete',
    params: { updateBy }
  })
}

// 批量删除预算管理
export function batchDeleteBudgetManagement(budgetIds, updateBy) {
  return transData({
    url: '/monitor/v1/enterprise/financial/budget/batchDelete',
    method: 'delete',
    data: budgetIds,
    params: { updateBy }
  })
}

// 启动预算编制
export function startBudgetCompilation(budgetId, updateBy) {
  return transData({
    url: `/monitor/v1/enterprise/financial/budget/startCompilation/${budgetId}`,
    method: 'put',
    params: { updateBy }
  })
}

// 暂停预算编制
export function pauseBudgetCompilation(budgetId, updateBy) {
  return transData({
    url: `/monitor/v1/enterprise/financial/budget/pauseCompilation/${budgetId}`,
    method: 'put',
    params: { updateBy }
  })
}

// 恢复预算编制
export function resumeBudgetCompilation(budgetId, updateBy) {
  return transData({
    url: `/monitor/v1/enterprise/financial/budget/resumeCompilation/${budgetId}`,
    method: 'put',
    params: { updateBy }
  })
}

// 完成预算编制
export function completeBudgetCompilation(budgetId, updateBy) {
  return transData({
    url: `/monitor/v1/enterprise/financial/budget/completeCompilation/${budgetId}`,
    method: 'put',
    params: { updateBy }
  })
}

// 取消预算编制
export function cancelBudgetCompilation(budgetId, updateBy) {
  return transData({
    url: `/monitor/v1/enterprise/financial/budget/cancelCompilation/${budgetId}`,
    method: 'put',
    params: { updateBy }
  })
}

// 提交审批
export function submitForApproval(budgetId, updateBy) {
  return transData({
    url: `/monitor/v1/enterprise/financial/budget/submitForApproval/${budgetId}`,
    method: 'put',
    params: { updateBy }
  })
}

// 初审
export function firstApproval(budgetId, approver, opinion, approvalResult, updateBy) {
  return transData({
    url: `/monitor/v1/enterprise/financial/budget/firstApproval/${budgetId}`,
    method: 'put',
    params: { approver, opinion, approvalResult, updateBy }
  })
}

// 终审
export function finalApproval(budgetId, approver, opinion, approvalResult, updateBy) {
  return transData({
    url: `/monitor/v1/enterprise/financial/budget/finalApproval/${budgetId}`,
    method: 'put',
    params: { approver, opinion, approvalResult, updateBy }
  })
}

// 启动预算执行
export function startBudgetExecution(budgetId, updateBy) {
  return transData({
    url: `/monitor/v1/enterprise/financial/budget/startExecution/${budgetId}`,
    method: 'put',
    params: { updateBy }
  })
}

// 暂停预算执行
export function pauseBudgetExecution(budgetId, updateBy) {
  return transData({
    url: `/monitor/v1/enterprise/financial/budget/pauseExecution/${budgetId}`,
    method: 'put',
    params: { updateBy }
  })
}

// 恢复预算执行
export function resumeBudgetExecution(budgetId, updateBy) {
  return transData({
    url: `/monitor/v1/enterprise/financial/budget/resumeExecution/${budgetId}`,
    method: 'put',
    params: { updateBy }
  })
}

// 完成预算执行
export function completeBudgetExecution(budgetId, updateBy) {
  return transData({
    url: `/monitor/v1/enterprise/financial/budget/completeExecution/${budgetId}`,
    method: 'put',
    params: { updateBy }
  })
}

// 预算调整
export function adjustBudget(budgetId, adjustmentReason, adjustmentContent, adjustmentAmount, updateBy) {
  return transData({
    url: `/monitor/v1/enterprise/financial/budget/adjust/${budgetId}`,
    method: 'put',
    params: { adjustmentReason, adjustmentContent, adjustmentAmount, updateBy }
  })
}

// 预算监控
export function monitorBudget(budgetId, updateBy) {
  return transData({
    url: `/monitor/v1/enterprise/financial/budget/monitor/${budgetId}`,
    method: 'put',
    params: { updateBy }
  })
}

// 绩效评估
export function evaluatePerformance(budgetId, performanceEvaluationResult, updateBy) {
  return transData({
    url: `/monitor/v1/enterprise/financial/budget/evaluatePerformance/${budgetId}`,
    method: 'put',
    params: { performanceEvaluationResult, updateBy }
  })
}

// 批量更新预算状态
export function batchUpdateBudgetStatus(budgetIds, status, updateBy) {
  return transData({
    url: '/monitor/v1/enterprise/financial/budget/batchUpdateBudgetStatus',
    method: 'put',
    data: budgetIds,
    params: { status, updateBy }
  })
}

// 批量更新审批状态
export function batchUpdateApprovalStatus(budgetIds, status, updateBy) {
  return transData({
    url: '/monitor/v1/enterprise/financial/budget/batchUpdateApprovalStatus',
    method: 'put',
    data: budgetIds,
    params: { status, updateBy }
  })
}

// 批量更新执行状态
export function batchUpdateExecutionStatus(budgetIds, status, updateBy) {
  return transData({
    url: '/monitor/v1/enterprise/financial/budget/batchUpdateExecutionStatus',
    method: 'put',
    data: budgetIds,
    params: { status, updateBy }
  })
}

// 获取预算管理统计
export function getStatisticsByEnterpriseId(enterpriseId) {
  return transData({
    url: `/monitor/v1/enterprise/financial/budget/statistics/${enterpriseId}`,
    method: 'get'
  })
}

// 获取预算类型分布
export function getBudgetTypeDistribution(enterpriseId) {
  return transData({
    url: `/monitor/v1/enterprise/financial/budget/budgetTypeDistribution/${enterpriseId}`,
    method: 'get'
  })
}

// 获取预算状态分布
export function getBudgetStatusDistribution(enterpriseId) {
  return transData({
    url: `/monitor/v1/enterprise/financial/budget/budgetStatusDistribution/${enterpriseId}`,
    method: 'get'
  })
}

// 获取审批状态分布
export function getApprovalStatusDistribution(enterpriseId) {
  return transData({
    url: `/monitor/v1/enterprise/financial/budget/approvalStatusDistribution/${enterpriseId}`,
    method: 'get'
  })
}

// 获取执行状态分布
export function getExecutionStatusDistribution(enterpriseId) {
  return transData({
    url: `/monitor/v1/enterprise/financial/budget/executionStatusDistribution/${enterpriseId}`,
    method: 'get'
  })
}

// 获取执行率分布
export function getExecutionRateDistribution(enterpriseId) {
  return transData({
    url: `/monitor/v1/enterprise/financial/budget/executionRateDistribution/${enterpriseId}`,
    method: 'get'
  })
}

// 获取预算执行趋势
export function getBudgetExecutionTrend(enterpriseId, months = 12) {
  return transData({
    url: `/monitor/v1/enterprise/financial/budget/budgetExecutionTrend/${enterpriseId}`,
    method: 'get',
    params: { months }
  })
}

// 获取预算差异趋势
export function getBudgetVarianceTrend(enterpriseId, months = 12) {
  return transData({
    url: `/monitor/v1/enterprise/financial/budget/budgetVarianceTrend/${enterpriseId}`,
    method: 'get',
    params: { months }
  })
}

// 获取预算调整统计
export function getBudgetAdjustmentStatistics(enterpriseId) {
  return transData({
    url: `/monitor/v1/enterprise/financial/budget/budgetAdjustmentStatistics/${enterpriseId}`,
    method: 'get'
  })
}

// 获取预算调整趋势
export function getBudgetAdjustmentTrend(enterpriseId, months = 12) {
  return transData({
    url: `/monitor/v1/enterprise/financial/budget/budgetAdjustmentTrend/${enterpriseId}`,
    method: 'get',
    params: { months }
  })
}

// 获取部门预算统计
export function getDepartmentBudgetStatistics(enterpriseId) {
  return transData({
    url: `/monitor/v1/enterprise/financial/budget/departmentBudgetStatistics/${enterpriseId}`,
    method: 'get'
  })
}

// 获取负责人预算统计
export function getManagerBudgetStatistics(enterpriseId) {
  return transData({
    url: `/monitor/v1/enterprise/financial/budget/managerBudgetStatistics/${enterpriseId}`,
    method: 'get'
  })
}

// 获取审批人员统计
export function getApproverStatistics(enterpriseId) {
  return transData({
    url: `/monitor/v1/enterprise/financial/budget/approverStatistics/${enterpriseId}`,
    method: 'get'
  })
}

// 获取编制方法统计
export function getCompilationMethodStatistics(enterpriseId) {
  return transData({
    url: `/monitor/v1/enterprise/financial/budget/compilationMethodStatistics/${enterpriseId}`,
    method: 'get'
  })
}

// 获取监控频率统计
export function getMonitoringFrequencyStatistics(enterpriseId) {
  return transData({
    url: `/monitor/v1/enterprise/financial/budget/monitoringFrequencyStatistics/${enterpriseId}`,
    method: 'get'
  })
}

// 获取预算执行效率分析
export function getBudgetExecutionEfficiencyAnalysis(enterpriseId) {
  return transData({
    url: `/monitor/v1/enterprise/financial/budget/budgetExecutionEfficiencyAnalysis/${enterpriseId}`,
    method: 'get'
  })
}

// 获取预算控制效果分析
export function getBudgetControlEffectivenessAnalysis(enterpriseId) {
  return transData({
    url: `/monitor/v1/enterprise/financial/budget/budgetControlEffectivenessAnalysis/${enterpriseId}`,
    method: 'get'
  })
}

// 获取绩效评估分析
export function getPerformanceEvaluationAnalysis(enterpriseId) {
  return transData({
    url: `/monitor/v1/enterprise/financial/budget/performanceEvaluationAnalysis/${enterpriseId}`,
    method: 'get'
  })
}

// 获取预算改进建议
export function getBudgetImprovementSuggestions(enterpriseId) {
  return transData({
    url: `/monitor/v1/enterprise/financial/budget/budgetImprovementSuggestions/${enterpriseId}`,
    method: 'get'
  })
}

// 获取成功经验总结
export function getSuccessExperiences(enterpriseId) {
  return transData({
    url: `/monitor/v1/enterprise/financial/budget/successExperiences/${enterpriseId}`,
    method: 'get'
  })
}

// 获取失败教训总结
export function getFailureLessons(enterpriseId) {
  return transData({
    url: `/monitor/v1/enterprise/financial/budget/failureLessons/${enterpriseId}`,
    method: 'get'
  })
}

// 获取能力提升建议
export function getCapabilityEnhancementSuggestions(enterpriseId) {
  return transData({
    url: `/monitor/v1/enterprise/financial/budget/capabilityEnhancementSuggestions/${enterpriseId}`,
    method: 'get'
  })
}

// 获取即将到期的预算
export function getUpcomingBudgets(enterpriseId, days = 7) {
  return transData({
    url: `/monitor/v1/enterprise/financial/budget/upcomingBudgets/${enterpriseId}`,
    method: 'get',
    params: { days }
  })
}

// 获取逾期的预算
export function getOverdueBudgets(enterpriseId) {
  return transData({
    url: `/monitor/v1/enterprise/financial/budget/overdueBudgets/${enterpriseId}`,
    method: 'get'
  })
}

// 获取待审批的预算
export function getPendingApprovalBudgets(enterpriseId) {
  return transData({
    url: `/monitor/v1/enterprise/financial/budget/pendingApprovalBudgets/${enterpriseId}`,
    method: 'get'
  })
}

// 获取执行异常的预算
export function getAbnormalExecutionBudgets(enterpriseId) {
  return transData({
    url: `/monitor/v1/enterprise/financial/budget/abnormalExecutionBudgets/${enterpriseId}`,
    method: 'get'
  })
}

// 获取需要调整的预算
export function getBudgetsNeedingAdjustment(enterpriseId) {
  return transData({
    url: `/monitor/v1/enterprise/financial/budget/budgetsNeedingAdjustment/${enterpriseId}`,
    method: 'get'
  })
}

// 导出预算管理
export function exportBudgetManagement(data) {
  return transData({
    url: '/monitor/v1/enterprise/financial/budget/export',
    method: 'post',
    data
  })
}

// 生成预算管理报告
export function generateBudgetManagementReport(enterpriseId, reportType) {
  return transData({
    url: `/monitor/v1/enterprise/financial/budget/generateReport/${enterpriseId}`,
    method: 'get',
    params: { reportType }
  })
}
