import request from '@/utils/request'
import { transData } from '@/utils/requestData'

/**
 * 风险控制措施API接口
 * 国资国企穿透式监管系统 - 风险穿透管控模块 - 风险控制措施功能
 */

// ==================== 基础CRUD操作 ====================

/**
 * 分页查询风险控制措施列表
 */
export function getRiskControlMeasureList(data) {
  return request({
    url: '/monitor/v1/supervision/risk/control-measure/list',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 根据ID获取风险控制措施详情
 */
export function getRiskControlMeasureById(id) {
  return request({
    url: `/monitor/v1/supervision/risk/control-measure/${id}`,
    method: 'get'
  })
}

/**
 * 新增风险控制措施
 */
export function addRiskControlMeasure(data) {
  return request({
    url: '/monitor/v1/supervision/risk/control-measure/add',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 更新风险控制措施
 */
export function updateRiskControlMeasure(data) {
  return request({
    url: '/monitor/v1/supervision/risk/control-measure/update',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 删除风险控制措施
 */
export function deleteRiskControlMeasure(id) {
  return request({
    url: `/monitor/v1/supervision/risk/control-measure/${id}`,
    method: 'delete'
  })
}

// ==================== 措施制定业务 ====================

/**
 * 制定风险控制措施
 */
export function developControlMeasure(data) {
  return request({
    url: '/monitor/v1/supervision/risk/control-measure/develop',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 生成措施建议
 */
export function generateMeasureRecommendations(data) {
  return request({
    url: '/monitor/v1/supervision/risk/control-measure/generate-recommendations',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 评估措施可行性
 */
export function assessMeasureFeasibility(data) {
  return request({
    url: '/monitor/v1/supervision/risk/control-measure/assess-feasibility',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 优化措施方案
 */
export function optimizeMeasurePlan(data) {
  return request({
    url: '/monitor/v1/supervision/risk/control-measure/optimize-plan',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 确定措施优先级
 */
export function determineMeasurePriority(data) {
  return request({
    url: '/monitor/v1/supervision/risk/control-measure/determine-priority',
    method: 'post',
    data: transData(data)
  })
}

// ==================== 实施管理业务 ====================

/**
 * 启动措施实施
 */
export function startImplementation(data) {
  return request({
    url: '/monitor/v1/supervision/risk/control-measure/start-implementation',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 更新实施进度
 */
export function updateImplementationProgress(data) {
  return request({
    url: '/monitor/v1/supervision/risk/control-measure/update-progress',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 完成措施实施
 */
export function completeImplementation(data) {
  return request({
    url: '/monitor/v1/supervision/risk/control-measure/complete-implementation',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 暂停措施实施
 */
export function suspendImplementation(data) {
  return request({
    url: '/monitor/v1/supervision/risk/control-measure/suspend-implementation',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 恢复措施实施
 */
export function resumeImplementation(data) {
  return request({
    url: '/monitor/v1/supervision/risk/control-measure/resume-implementation',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取实施进度统计
 */
export function getImplementationProgressStatistics() {
  return request({
    url: '/monitor/v1/supervision/risk/control-measure/implementation-progress-statistics',
    method: 'get'
  })
}

// ==================== 进度监控业务 ====================

/**
 * 监控实施进度
 */
export function monitorImplementationProgress(id) {
  return request({
    url: `/monitor/v1/supervision/risk/control-measure/monitor-progress/${id}`,
    method: 'post'
  })
}

/**
 * 检查逾期措施
 */
export function checkOverdueMeasures() {
  return request({
    url: '/monitor/v1/supervision/risk/control-measure/check-overdue',
    method: 'get'
  })
}

/**
 * 获取即将到期的措施
 */
export function getExpiringMeasures(data) {
  return request({
    url: '/monitor/v1/supervision/risk/control-measure/expiring-measures',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 发送进度提醒
 */
export function sendProgressReminder(data) {
  return request({
    url: '/monitor/v1/supervision/risk/control-measure/send-reminder',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 生成进度报告
 */
export function generateProgressReport(data) {
  return request({
    url: '/monitor/v1/supervision/risk/control-measure/generate-progress-report',
    method: 'post',
    data: transData(data)
  })
}

// ==================== 资源管理业务 ====================

/**
 * 分配资源
 */
export function allocateResources(data) {
  return request({
    url: '/monitor/v1/supervision/risk/control-measure/allocate-resources',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 更新资源使用情况
 */
export function updateResourceUsage(data) {
  return request({
    url: '/monitor/v1/supervision/risk/control-measure/update-resource-usage',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 检查预算执行情况
 */
export function checkBudgetExecution(id) {
  return request({
    url: `/monitor/v1/supervision/risk/control-measure/check-budget/${id}`,
    method: 'get'
  })
}

/**
 * 获取超预算措施列表
 */
export function getOverBudgetMeasures() {
  return request({
    url: '/monitor/v1/supervision/risk/control-measure/over-budget',
    method: 'get'
  })
}

/**
 * 分析资源利用效率
 */
export function analyzeResourceUtilizationEfficiency(data) {
  return request({
    url: '/monitor/v1/supervision/risk/control-measure/analyze-resource-efficiency',
    method: 'post',
    data: transData(data)
  })
}

// ==================== 效果评估业务 ====================

/**
 * 评估措施效果
 */
export function evaluateMeasureEffectiveness(id) {
  return request({
    url: `/monitor/v1/supervision/risk/control-measure/evaluate-effectiveness/${id}`,
    method: 'get'
  })
}

/**
 * 计算效果评分
 */
export function calculateEffectivenessScore(data) {
  return request({
    url: '/monitor/v1/supervision/risk/control-measure/calculate-effectiveness-score',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 分析风险降低效果
 */
export function analyzeRiskReductionEffect(id) {
  return request({
    url: `/monitor/v1/supervision/risk/control-measure/analyze-risk-reduction/${id}`,
    method: 'get'
  })
}

/**
 * 对比预期与实际效果
 */
export function compareExpectedVsActualEffect(id) {
  return request({
    url: `/monitor/v1/supervision/risk/control-measure/compare-effect/${id}`,
    method: 'get'
  })
}

/**
 * 获取高效果措施列表
 */
export function getHighEffectivenessMeasures() {
  return request({
    url: '/monitor/v1/supervision/risk/control-measure/high-effectiveness',
    method: 'get'
  })
}

/**
 * 生成效果评估报告
 */
export function generateEffectivenessReport(data) {
  return request({
    url: '/monitor/v1/supervision/risk/control-measure/generate-effectiveness-report',
    method: 'post',
    data: transData(data)
  })
}

// ==================== 审核流程业务 ====================

/**
 * 提交审核
 */
export function submitForReview(data) {
  return request({
    url: '/monitor/v1/supervision/risk/control-measure/submit-review',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 审核措施
 */
export function reviewMeasure(data) {
  return request({
    url: '/monitor/v1/supervision/risk/control-measure/review',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 批准措施
 */
export function approveMeasure(data) {
  return request({
    url: '/monitor/v1/supervision/risk/control-measure/approve',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取待审核措施列表
 */
export function getPendingReviewMeasures() {
  return request({
    url: '/monitor/v1/supervision/risk/control-measure/pending-review',
    method: 'get'
  })
}

/**
 * 获取已审核措施列表
 */
export function getReviewedMeasures() {
  return request({
    url: '/monitor/v1/supervision/risk/control-measure/reviewed',
    method: 'get'
  })
}

// ==================== 统计分析业务 ====================

/**
 * 获取措施综合统计
 */
export function getComprehensiveStatistics() {
  return request({
    url: '/monitor/v1/supervision/risk/control-measure/comprehensive-statistics',
    method: 'get'
  })
}

/**
 * 获取企业措施概览
 */
export function getEnterpriseMeasureOverview(enterpriseId) {
  return request({
    url: `/monitor/v1/supervision/risk/control-measure/enterprise-overview/${enterpriseId}`,
    method: 'get'
  })
}

/**
 * 获取措施类型分布
 */
export function getMeasureTypeDistribution() {
  return request({
    url: '/monitor/v1/supervision/risk/control-measure/type-distribution',
    method: 'get'
  })
}

/**
 * 获取措施效果统计
 */
export function getMeasureEffectivenessStatistics() {
  return request({
    url: '/monitor/v1/supervision/risk/control-measure/effectiveness-statistics',
    method: 'get'
  })
}

// ==================== 批量操作业务 ====================

/**
 * 批量更新措施状态
 */
export function batchUpdateMeasureStatus(data) {
  return request({
    url: '/monitor/v1/supervision/risk/control-measure/batch-update-status',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 批量审核措施
 */
export function batchReviewMeasures(data) {
  return request({
    url: '/monitor/v1/supervision/risk/control-measure/batch-review',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 批量更新实施进度
 */
export function batchUpdateImplementationProgress(data) {
  return request({
    url: '/monitor/v1/supervision/risk/control-measure/batch-update-progress',
    method: 'post',
    data: transData(data)
  })
}

// ==================== 导出业务 ====================

/**
 * 导出措施数据
 */
export function exportMeasureData(data) {
  return request({
    url: '/monitor/v1/supervision/risk/control-measure/export-data',
    method: 'post',
    data: transData(data),
    responseType: 'blob'
  })
}

/**
 * 导出措施报告
 */
export function exportMeasureReport(data) {
  return request({
    url: '/monitor/v1/supervision/risk/control-measure/export-report',
    method: 'post',
    data: transData(data),
    responseType: 'blob'
  })
}

/**
 * 评估有效性
 */
export function evaluateEffectiveness(data) {
  return request({
    url: '/monitor/v1/supervision/risk/control-measure/evaluate-effectiveness',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 更新资源分配
 */
export function updateResourceAllocation(data) {
  return request({
    url: '/monitor/v1/supervision/risk/control-measure/update-resource-allocation',
    method: 'post',
    data: transData(data)
  })
}
