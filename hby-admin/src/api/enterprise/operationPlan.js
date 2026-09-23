import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// 分页查询经营计划管理
export function getOperationPlanPage(data) {
  return request({
    url: '/monitor/v1/enterprise/operation/plan/list',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

// 根据ID获取经营计划管理详情
export function getOperationPlanById(planId) {
  return request({
    url: `/monitor/v1/enterprise/operation/plan/${planId}`,
    method: 'get'
  })
}

// 新增经营计划管理
export function addOperationPlan(data) {
  return request({
    url: '/monitor/v1/enterprise/operation/plan/add',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

// 修改经营计划管理
export function updateOperationPlan(data) {
  return request({
    url: '/monitor/v1/enterprise/operation/plan/update',
    method: 'put',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

// 删除经营计划管理
export function deleteOperationPlan(planId, updateBy) {
  return request({
    url: `/monitor/v1/enterprise/operation/plan/${planId}`,
    method: 'delete',
    params: { updateBy }
  })
}

// 批量删除经营计划管理
export function batchDeleteOperationPlan(planIds, updateBy) {
  return request({
    url: '/monitor/v1/enterprise/operation/plan/batchDelete',
    method: 'delete',
    data: transData(planIds),
    params: { updateBy },
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

// 启动计划制定
export function startPlanFormulation(planId, updateBy) {
  return request({
    url: `/monitor/v1/enterprise/operation/plan/startFormulation/${planId}`,
    method: 'put',
    params: { updateBy }
  })
}

// 暂停计划制定
export function pausePlanFormulation(planId, updateBy) {
  return request({
    url: `/monitor/v1/enterprise/operation/plan/pauseFormulation/${planId}`,
    method: 'put',
    params: { updateBy }
  })
}

// 恢复计划制定
export function resumePlanFormulation(planId, updateBy) {
  return request({
    url: `/monitor/v1/enterprise/operation/plan/resumeFormulation/${planId}`,
    method: 'put',
    params: { updateBy }
  })
}

// 完成计划制定
export function completePlanFormulation(planId, updateBy) {
  return request({
    url: `/monitor/v1/enterprise/operation/plan/completeFormulation/${planId}`,
    method: 'put',
    params: { updateBy }
  })
}

// 取消计划制定
export function cancelPlanFormulation(planId, updateBy) {
  return request({
    url: `/monitor/v1/enterprise/operation/plan/cancelFormulation/${planId}`,
    method: 'put',
    params: { updateBy }
  })
}

// 提交审批
export function submitForApproval(planId, updateBy) {
  return request({
    url: `/monitor/v1/enterprise/operation/plan/submitForApproval/${planId}`,
    method: 'put',
    params: { updateBy }
  })
}

// 初审
export function firstApproval(planId, approver, opinion, approvalResult, updateBy) {
  return request({
    url: `/monitor/v1/enterprise/operation/plan/firstApproval/${planId}`,
    method: 'put',
    params: { approver, opinion, approvalResult, updateBy }
  })
}

// 终审
export function finalApproval(planId, approver, opinion, approvalResult, updateBy) {
  return request({
    url: `/monitor/v1/enterprise/operation/plan/finalApproval/${planId}`,
    method: 'put',
    params: { approver, opinion, approvalResult, updateBy }
  })
}

// 启动计划执行
export function startPlanExecution(planId, updateBy) {
  return request({
    url: `/monitor/v1/enterprise/operation/plan/startExecution/${planId}`,
    method: 'put',
    params: { updateBy }
  })
}

// 暂停计划执行
export function pausePlanExecution(planId, updateBy) {
  return request({
    url: `/monitor/v1/enterprise/operation/plan/pauseExecution/${planId}`,
    method: 'put',
    params: { updateBy }
  })
}

// 恢复计划执行
export function resumePlanExecution(planId, updateBy) {
  return request({
    url: `/monitor/v1/enterprise/operation/plan/resumeExecution/${planId}`,
    method: 'put',
    params: { updateBy }
  })
}

// 完成计划执行
export function completePlanExecution(planId, updateBy) {
  return request({
    url: `/monitor/v1/enterprise/operation/plan/completeExecution/${planId}`,
    method: 'put',
    params: { updateBy }
  })
}

// 计划调整
export function adjustPlan(planId, adjustmentReason, adjustmentContent, updateBy) {
  return request({
    url: `/monitor/v1/enterprise/operation/plan/adjust/${planId}`,
    method: 'put',
    params: { adjustmentReason, adjustmentContent, updateBy }
  })
}

// 计划监控
export function monitorPlan(planId, updateBy) {
  return request({
    url: `/monitor/v1/enterprise/operation/plan/monitor/${planId}`,
    method: 'put',
    params: { updateBy }
  })
}

// 获取计划监控数据
export function getPlanMonitoringData(planId) {
  return request({
    url: `/monitor/v1/enterprise/operation/plan/monitoring/${planId}`,
    method: 'get'
  })
}

// 效果评估
export function evaluateEffectiveness(planId, effectivenessEvaluation, updateBy) {
  return request({
    url: `/monitor/v1/enterprise/operation/plan/evaluateEffectiveness/${planId}`,
    method: 'put',
    params: { effectivenessEvaluation, updateBy }
  })
}

// 批量更新计划状态
export function batchUpdatePlanStatus(planIds, status, updateBy) {
  return request({
    url: '/monitor/v1/enterprise/operation/plan/batchUpdatePlanStatus',
    method: 'put',
    data: transData(planIds),
    params: { status, updateBy },
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

// 批量更新审批状态
export function batchUpdateApprovalStatus(planIds, status, updateBy) {
  return request({
    url: '/monitor/v1/enterprise/operation/plan/batchUpdateApprovalStatus',
    method: 'put',
    data: transData(planIds),
    params: { status, updateBy },
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

// 批量更新执行状态
export function batchUpdateExecutionStatus(planIds, status, updateBy) {
  return request({
    url: '/monitor/v1/enterprise/operation/plan/batchUpdateExecutionStatus',
    method: 'put',
    data: transData(planIds),
    params: { status, updateBy },
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

// 获取经营计划管理统计
export function getStatisticsByEnterpriseId(enterpriseId) {
  return request({
    url: `/monitor/v1/enterprise/operation/plan/statistics/${enterpriseId}`,
    method: 'get'
  })
}

// 获取计划类型分布
export function getPlanTypeDistribution(enterpriseId) {
  return request({
    url: `/monitor/v1/enterprise/operation/plan/planTypeDistribution/${enterpriseId}`,
    method: 'get'
  })
}

// 获取计划状态分布
export function getPlanStatusDistribution(enterpriseId) {
  return request({
    url: `/monitor/v1/enterprise/operation/plan/planStatusDistribution/${enterpriseId}`,
    method: 'get'
  })
}

// 获取审批状态分布
export function getApprovalStatusDistribution(enterpriseId) {
  return request({
    url: `/monitor/v1/enterprise/operation/plan/approvalStatusDistribution/${enterpriseId}`,
    method: 'get'
  })
}

// 获取执行状态分布
export function getExecutionStatusDistribution(enterpriseId) {
  return request({
    url: `/monitor/v1/enterprise/operation/plan/executionStatusDistribution/${enterpriseId}`,
    method: 'get'
  })
}

// 获取执行进度分布
export function getExecutionProgressDistribution(enterpriseId) {
  return request({
    url: `/monitor/v1/enterprise/operation/plan/executionProgressDistribution/${enterpriseId}`,
    method: 'get'
  })
}

// 获取计划执行趋势
export function getPlanExecutionTrend(enterpriseId, months = 12) {
  return request({
    url: `/monitor/v1/enterprise/operation/plan/planExecutionTrend/${enterpriseId}`,
    method: 'get',
    params: { months }
  })
}

// 获取目标达成趋势
export function getTargetAchievementTrend(enterpriseId, months = 12) {
  return request({
    url: `/monitor/v1/enterprise/operation/plan/targetAchievementTrend/${enterpriseId}`,
    method: 'get',
    params: { months }
  })
}

// 获取计划调整统计
export function getPlanAdjustmentStatistics(enterpriseId) {
  return request({
    url: `/monitor/v1/enterprise/operation/plan/planAdjustmentStatistics/${enterpriseId}`,
    method: 'get'
  })
}

// 获取计划调整趋势
export function getPlanAdjustmentTrend(enterpriseId, months = 12) {
  return request({
    url: `/monitor/v1/enterprise/operation/plan/planAdjustmentTrend/${enterpriseId}`,
    method: 'get',
    params: { months }
  })
}

// 获取部门计划统计
export function getDepartmentPlanStatistics(enterpriseId) {
  return request({
    url: `/monitor/v1/enterprise/operation/plan/departmentPlanStatistics/${enterpriseId}`,
    method: 'get'
  })
}

// 获取负责人计划统计
export function getManagerPlanStatistics(enterpriseId) {
  return request({
    url: `/monitor/v1/enterprise/operation/plan/managerPlanStatistics/${enterpriseId}`,
    method: 'get'
  })
}

// 获取审批人员统计
export function getApproverStatistics(enterpriseId) {
  return request({
    url: `/monitor/v1/enterprise/operation/plan/approverStatistics/${enterpriseId}`,
    method: 'get'
  })
}

// 获取制定方法统计
export function getFormulationMethodStatistics(enterpriseId) {
  return request({
    url: `/monitor/v1/enterprise/operation/plan/formulationMethodStatistics/${enterpriseId}`,
    method: 'get'
  })
}

// 获取监控频率统计
export function getMonitoringFrequencyStatistics(enterpriseId) {
  return request({
    url: `/monitor/v1/enterprise/operation/plan/monitoringFrequencyStatistics/${enterpriseId}`,
    method: 'get'
  })
}

// 获取计划执行效率分析
export function getPlanExecutionEfficiencyAnalysis(enterpriseId) {
  return request({
    url: `/monitor/v1/enterprise/operation/plan/planExecutionEfficiencyAnalysis/${enterpriseId}`,
    method: 'get'
  })
}

// 获取目标达成效果分析
export function getTargetAchievementEffectivenessAnalysis(enterpriseId) {
  return request({
    url: `/monitor/v1/enterprise/operation/plan/targetAchievementEffectivenessAnalysis/${enterpriseId}`,
    method: 'get'
  })
}

// 获取执行质量分析
export function getExecutionQualityAnalysis(enterpriseId) {
  return request({
    url: `/monitor/v1/enterprise/operation/plan/executionQualityAnalysis/${enterpriseId}`,
    method: 'get'
  })
}

// 获取创新性分析
export function getInnovationAnalysis(enterpriseId) {
  return request({
    url: `/monitor/v1/enterprise/operation/plan/innovationAnalysis/${enterpriseId}`,
    method: 'get'
  })
}

// 获取计划改进建议
export function getPlanImprovementSuggestions(enterpriseId) {
  return request({
    url: `/monitor/v1/enterprise/operation/plan/planImprovementSuggestions/${enterpriseId}`,
    method: 'get'
  })
}

// 获取成功经验总结
export function getSuccessExperiences(enterpriseId) {
  return request({
    url: `/monitor/v1/enterprise/operation/plan/successExperiences/${enterpriseId}`,
    method: 'get'
  })
}

// 获取失败教训总结
export function getFailureLessons(enterpriseId) {
  return request({
    url: `/monitor/v1/enterprise/operation/plan/failureLessons/${enterpriseId}`,
    method: 'get'
  })
}

// 获取能力提升建议
export function getCapabilityEnhancementSuggestions(enterpriseId) {
  return request({
    url: `/monitor/v1/enterprise/operation/plan/capabilityEnhancementSuggestions/${enterpriseId}`,
    method: 'get'
  })
}

// 获取即将到期的计划
export function getUpcomingPlans(enterpriseId, days = 7) {
  return request({
    url: `/monitor/v1/enterprise/operation/plan/upcomingPlans/${enterpriseId}`,
    method: 'get',
    params: { days }
  })
}

// 获取逾期的计划
export function getOverduePlans(enterpriseId) {
  return request({
    url: `/monitor/v1/enterprise/operation/plan/overduePlans/${enterpriseId}`,
    method: 'get'
  })
}

// 获取待审批的计划
export function getPendingApprovalPlans(enterpriseId) {
  return request({
    url: `/monitor/v1/enterprise/operation/plan/pendingApprovalPlans/${enterpriseId}`,
    method: 'get'
  })
}

// 获取执行异常的计划
export function getAbnormalExecutionPlans(enterpriseId) {
  return request({
    url: `/monitor/v1/enterprise/operation/plan/abnormalExecutionPlans/${enterpriseId}`,
    method: 'get'
  })
}

// 获取需要调整的计划
export function getPlansNeedingAdjustment(enterpriseId) {
  return request({
    url: `/monitor/v1/enterprise/operation/plan/plansNeedingAdjustment/${enterpriseId}`,
    method: 'get'
  })
}

// 导出经营计划管理
export function exportOperationPlan(data) {
  return request({
    url: '/monitor/v1/enterprise/operation/plan/export',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    responseType: 'blob'
  })
}

// 生成经营计划管理报告
export function generateOperationPlanReport(enterpriseId, reportType) {
  return request({
    url: `/monitor/v1/enterprise/operation/plan/generateReport/${enterpriseId}`,
    method: 'get',
    params: { reportType }
  })
}
