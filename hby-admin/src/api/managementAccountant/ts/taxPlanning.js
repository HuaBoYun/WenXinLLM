import request from '@/utils/request'

// ==================== 基础CRUD操作 ====================

/**
 * 创建税务筹划
 */
export function createPlanning(tenantId, data) {
  return request({
    url: '/accountant/ts/tax-planning/create',
    method: 'post',
    params: { tenantId },
    data
  })
}

/**
 * 更新税务筹划
 */
export function updatePlanning(tenantId, planningId, data) {
  return request({
    url: `/accountant/ts/tax-planning/update/${planningId}`,
    method: 'put',
    params: { tenantId },
    data
  })
}

/**
 * 删除税务筹划
 */
export function deletePlanning(tenantId, planningId) {
  return request({
    url: `/accountant/ts/tax-planning/delete/${planningId}`,
    method: 'delete',
    params: { tenantId }
  })
}

/**
 * 获取税务筹划详情
 */
export function getPlanningDetail(tenantId, planningId) {
  return request({
    url: `/accountant/ts/tax-planning/detail/${planningId}`,
    method: 'get',
    params: { tenantId }
  })
}

/**
 * 根据编号获取税务筹划详情
 */
export function getPlanningByCode(tenantId, planningCode) {
  return request({
    url: `/accountant/ts/tax-planning/detail/by-code/${planningCode}`,
    method: 'get',
    params: { tenantId }
  })
}

/**
 * 分页查询税务筹划
 */
export function getPlanningPage(tenantId, params) {
  return request({
    url: '/accountant/ts/tax-planning/page',
    method: 'get',
    params: { tenantId, ...params }
  })
}

// ==================== 筹划管理功能 ====================

/**
 * 生成筹划编号
 */
export function generatePlanningCode(tenantId) {
  return request({
    url: '/accountant/ts/tax-planning/generate-code',
    method: 'post',
    params: { tenantId }
  })
}

/**
 * 验证筹划数据
 */
export function validatePlanningData(tenantId, data) {
  return request({
    url: '/accountant/ts/tax-planning/validate',
    method: 'post',
    params: { tenantId },
    data
  })
}

/**
 * 计算筹划效益
 */
export function calculatePlanningBenefit(tenantId, planningId) {
  return request({
    url: `/accountant/ts/tax-planning/calculate-benefit/${planningId}`,
    method: 'post',
    params: { tenantId }
  })
}

/**
 * 评估筹划风险
 */
export function assessPlanningRisk(tenantId, planningId) {
  return request({
    url: `/accountant/ts/tax-planning/assess-risk/${planningId}`,
    method: 'post',
    params: { tenantId }
  })
}

/**
 * 分析筹划可行性
 */
export function analyzeFeasibility(tenantId, planningId) {
  return request({
    url: `/accountant/ts/tax-planning/analyze-feasibility/${planningId}`,
    method: 'post',
    params: { tenantId }
  })
}

/**
 * 优化筹划方案
 */
export function optimizePlanningScheme(tenantId, planningId) {
  return request({
    url: `/accountant/ts/tax-planning/optimize-scheme/${planningId}`,
    method: 'post',
    params: { tenantId }
  })
}

/**
 * 比较筹划方案
 */
export function comparePlanningSchemes(tenantId, planningIds) {
  return request({
    url: '/accountant/ts/tax-planning/compare-schemes',
    method: 'post',
    params: { tenantId },
    data: planningIds
  })
}

/**
 * 推荐筹划方案
 */
export function recommendPlanningSchemes(tenantId, criteria) {
  return request({
    url: '/accountant/ts/tax-planning/recommend-schemes',
    method: 'post',
    params: { tenantId },
    data: criteria
  })
}

// ==================== 执行管理功能 ====================

/**
 * 启动筹划执行
 */
export function startPlanningExecution(tenantId, planningId) {
  return request({
    url: `/accountant/ts/tax-planning/start-execution/${planningId}`,
    method: 'post',
    params: { tenantId }
  })
}

/**
 * 暂停筹划执行
 */
export function pausePlanningExecution(tenantId, planningId) {
  return request({
    url: `/accountant/ts/tax-planning/pause-execution/${planningId}`,
    method: 'post',
    params: { tenantId }
  })
}

/**
 * 恢复筹划执行
 */
export function resumePlanningExecution(tenantId, planningId) {
  return request({
    url: `/accountant/ts/tax-planning/resume-execution/${planningId}`,
    method: 'post',
    params: { tenantId }
  })
}

/**
 * 完成筹划执行
 */
export function completePlanningExecution(tenantId, planningId, completionData) {
  return request({
    url: `/accountant/ts/tax-planning/complete-execution/${planningId}`,
    method: 'post',
    params: { tenantId },
    data: completionData
  })
}

/**
 * 取消筹划执行
 */
export function cancelPlanningExecution(tenantId, planningId, reason) {
  return request({
    url: `/accountant/ts/tax-planning/cancel-execution/${planningId}`,
    method: 'post',
    params: { tenantId, reason }
  })
}

/**
 * 更新执行进度
 */
export function updateExecutionProgress(tenantId, planningId, progress) {
  return request({
    url: `/accountant/ts/tax-planning/update-progress/${planningId}`,
    method: 'post',
    params: { tenantId, progress }
  })
}

// ==================== 查询统计功能 ====================

/**
 * 根据筹划类型查询
 */
export function getPlanningsByType(tenantId, planningType) {
  return request({
    url: '/accountant/ts/tax-planning/list/by-type',
    method: 'get',
    params: { tenantId, planningType }
  })
}

/**
 * 根据筹划状态查询
 */
export function getPlanningsByStatus(tenantId, planningStatus) {
  return request({
    url: '/accountant/ts/tax-planning/list/by-status',
    method: 'get',
    params: { tenantId, planningStatus }
  })
}

/**
 * 根据执行状态查询
 */
export function getPlanningsByExecutionStatus(tenantId, executionStatus) {
  return request({
    url: '/accountant/ts/tax-planning/list/by-execution-status',
    method: 'get',
    params: { tenantId, executionStatus }
  })
}

/**
 * 根据风险等级查询
 */
export function getPlanningsByRiskLevel(tenantId, riskLevel) {
  return request({
    url: '/accountant/ts/tax-planning/list/by-risk-level',
    method: 'get',
    params: { tenantId, riskLevel }
  })
}

/**
 * 根据税种查询
 */
export function getPlanningsByTaxType(tenantId, taxType) {
  return request({
    url: '/accountant/ts/tax-planning/list/by-tax-type',
    method: 'get',
    params: { tenantId, taxType }
  })
}

/**
 * 根据责任人查询
 */
export function getPlanningsByResponsiblePerson(tenantId, responsiblePerson) {
  return request({
    url: '/accountant/ts/tax-planning/list/by-responsible-person',
    method: 'get',
    params: { tenantId, responsiblePerson }
  })
}

/**
 * 查询即将到期的筹划
 */
export function getExpiringSoonPlannings(tenantId, days = 7) {
  return request({
    url: '/accountant/ts/tax-planning/list/expiring-soon',
    method: 'get',
    params: { tenantId, days }
  })
}

/**
 * 查询逾期的筹划
 */
export function getOverduePlannings(tenantId) {
  return request({
    url: '/accountant/ts/tax-planning/list/overdue',
    method: 'get',
    params: { tenantId }
  })
}

/**
 * 查询高风险筹划
 */
export function getHighRiskPlannings(tenantId) {
  return request({
    url: '/accountant/ts/tax-planning/list/high-risk',
    method: 'get',
    params: { tenantId }
  })
}

/**
 * 查询高收益筹划
 */
export function getHighBenefitPlannings(tenantId, minBenefit) {
  return request({
    url: '/accountant/ts/tax-planning/list/high-benefit',
    method: 'get',
    params: { tenantId, minBenefit }
  })
}

// ==================== 统计分析功能 ====================

/**
 * 获取筹划概览统计
 */
export function getPlanningOverview(tenantId) {
  return request({
    url: '/accountant/ts/tax-planning/overview',
    method: 'get',
    params: { tenantId }
  })
}

/**
 * 按状态统计筹划数量
 */
export function countPlanningsByStatus(tenantId) {
  return request({
    url: '/accountant/ts/tax-planning/stats/by-status',
    method: 'get',
    params: { tenantId }
  })
}

/**
 * 按类型统计筹划数量
 */
export function countPlanningsByType(tenantId) {
  return request({
    url: '/accountant/ts/tax-planning/stats/by-type',
    method: 'get',
    params: { tenantId }
  })
}

/**
 * 按税种统计筹划数量
 */
export function countPlanningsByTaxType(tenantId) {
  return request({
    url: '/accountant/ts/tax-planning/stats/by-tax-type',
    method: 'get',
    params: { tenantId }
  })
}

/**
 * 按风险等级统计筹划数量
 */
export function countPlanningsByRiskLevel(tenantId) {
  return request({
    url: '/accountant/ts/tax-planning/stats/by-risk-level',
    method: 'get',
    params: { tenantId }
  })
}

/**
 * 按执行状态统计筹划数量
 */
export function countPlanningsByExecutionStatus(tenantId) {
  return request({
    url: '/accountant/ts/tax-planning/stats/by-execution-status',
    method: 'get',
    params: { tenantId }
  })
}

/**
 * 获取筹划趋势数据
 */
export function getPlanningTrend(tenantId, startDate, endDate, groupBy = 'month') {
  return request({
    url: '/accountant/ts/tax-planning/trend/planning',
    method: 'get',
    params: { tenantId, startDate, endDate, groupBy }
  })
}

/**
 * 获取节税趋势数据
 */
export function getTaxSavingTrend(tenantId, startDate, endDate, groupBy = 'month') {
  return request({
    url: '/accountant/ts/tax-planning/trend/tax-saving',
    method: 'get',
    params: { tenantId, startDate, endDate, groupBy }
  })
}

/**
 * 获取收益趋势数据
 */
export function getBenefitTrend(tenantId, startDate, endDate, groupBy = 'month') {
  return request({
    url: '/accountant/ts/tax-planning/trend/benefit',
    method: 'get',
    params: { tenantId, startDate, endDate, groupBy }
  })
}

/**
 * 获取风险分布数据
 */
export function getRiskDistribution(tenantId) {
  return request({
    url: '/accountant/ts/tax-planning/distribution/risk',
    method: 'get',
    params: { tenantId }
  })
}

/**
 * 获取效果评估数据
 */
export function getEffectivenessData(tenantId) {
  return request({
    url: '/accountant/ts/tax-planning/effectiveness',
    method: 'get',
    params: { tenantId }
  })
}

/**
 * 获取筹划排行榜
 */
export function getPlanningRanking(tenantId, rankBy = 'tax_saving', limit = 10) {
  return request({
    url: '/accountant/ts/tax-planning/ranking',
    method: 'get',
    params: { tenantId, rankBy, limit }
  })
}

/**
 * 获取筹划效率统计
 */
export function getPlanningEfficiencyStats(tenantId) {
  return request({
    url: '/accountant/ts/tax-planning/efficiency-stats',
    method: 'get',
    params: { tenantId }
  })
}

// ==================== 批量操作功能 ====================

/**
 * 批量创建筹划
 */
export function batchCreatePlannings(tenantId, plannings) {
  return request({
    url: '/accountant/ts/tax-planning/batch/create',
    method: 'post',
    params: { tenantId },
    data: plannings
  })
}

/**
 * 批量更新筹划状态
 */
export function batchUpdatePlanningStatus(tenantId, planningIds, status) {
  return request({
    url: '/accountant/ts/tax-planning/batch/update-status',
    method: 'post',
    params: { tenantId, status },
    data: planningIds
  })
}

/**
 * 批量更新执行状态
 */
export function batchUpdateExecutionStatus(tenantId, planningIds, executionStatus) {
  return request({
    url: '/accountant/ts/tax-planning/batch/update-execution-status',
    method: 'post',
    params: { tenantId, executionStatus },
    data: planningIds
  })
}

/**
 * 批量删除筹划
 */
export function batchDeletePlannings(tenantId, planningIds) {
  return request({
    url: '/accountant/ts/tax-planning/batch/delete',
    method: 'post',
    params: { tenantId },
    data: planningIds
  })
}

/**
 * 批量归档筹划
 */
export function batchArchivePlannings(tenantId, planningIds) {
  return request({
    url: '/accountant/ts/tax-planning/batch/archive',
    method: 'post',
    params: { tenantId },
    data: planningIds
  })
}

/**
 * 批量激活筹划
 */
export function batchActivatePlannings(tenantId, planningIds) {
  return request({
    url: '/accountant/ts/tax-planning/batch/activate',
    method: 'post',
    params: { tenantId },
    data: planningIds
  })
}

/**
 * 批量导入筹划
 */
export function batchImportPlannings(tenantId, planningData) {
  return request({
    url: '/accountant/ts/tax-planning/batch/import',
    method: 'post',
    params: { tenantId },
    data: planningData
  })
}

/**
 * 批量导出筹划
 */
export function batchExportPlannings(tenantId, planningIds) {
  return request({
    url: '/accountant/ts/tax-planning/batch/export',
    method: 'post',
    params: { tenantId },
    data: planningIds
  })
}

// ==================== 工具功能 ====================

/**
 * 复制筹划
 */
export function copyPlanning(tenantId, sourcePlanningId, newPlanningName) {
  return request({
    url: `/accountant/ts/tax-planning/copy/${sourcePlanningId}`,
    method: 'post',
    params: { tenantId, newPlanningName }
  })
}

/**
 * 生成筹划报告
 */
export function generatePlanningReport(tenantId, planningId) {
  return request({
    url: `/accountant/ts/tax-planning/generate-report/${planningId}`,
    method: 'post',
    params: { tenantId }
  })
}

/**
 * 发送筹划提醒
 */
export function sendPlanningReminder(tenantId, planningId, reminderType) {
  return request({
    url: `/accountant/ts/tax-planning/send-reminder/${planningId}`,
    method: 'post',
    params: { tenantId, reminderType }
  })
}

// ==================== 系统维护功能 ====================

/**
 * 系统健康检查
 */
export function systemHealthCheck(tenantId) {
  return request({
    url: '/accountant/ts/tax-planning/system/health-check',
    method: 'get',
    params: { tenantId }
  })
}

/**
 * 数据一致性检查
 */
export function dataConsistencyCheck(tenantId) {
  return request({
    url: '/accountant/ts/tax-planning/system/data-consistency-check',
    method: 'get',
    params: { tenantId }
  })
}

/**
 * 性能统计
 */
export function performanceStats(tenantId) {
  return request({
    url: '/accountant/ts/tax-planning/system/performance-stats',
    method: 'get',
    params: { tenantId }
  })
}

/**
 * 清理过期数据
 */
export function cleanupExpiredData(tenantId, days = 90) {
  return request({
    url: '/accountant/ts/tax-planning/system/cleanup-expired-data',
    method: 'post',
    params: { tenantId, days }
  })
}

// ==================== 常量定义 ====================

// 筹划类型
export const PLANNING_TYPES = {
  TAX_REDUCTION: 'TAX_REDUCTION',
  TAX_DEFERRAL: 'TAX_DEFERRAL',
  TAX_EXEMPTION: 'TAX_EXEMPTION',
  TAX_CREDIT: 'TAX_CREDIT',
  STRUCTURE_OPTIMIZATION: 'STRUCTURE_OPTIMIZATION',
  BUSINESS_RESTRUCTURING: 'BUSINESS_RESTRUCTURING',
  INVESTMENT_PLANNING: 'INVESTMENT_PLANNING',
  MERGER_ACQUISITION: 'MERGER_ACQUISITION',
  INTERNATIONAL_PLANNING: 'INTERNATIONAL_PLANNING',
  OTHER: 'OTHER'
}

// 筹划状态
export const PLANNING_STATUS = {
  DRAFT: 'DRAFT',
  UNDER_REVIEW: 'UNDER_REVIEW',
  APPROVED: 'APPROVED',
  REJECTED: 'REJECTED',
  SUSPENDED: 'SUSPENDED',
  CANCELLED: 'CANCELLED',
  ARCHIVED: 'ARCHIVED'
}

// 执行状态
export const EXECUTION_STATUS = {
  NOT_STARTED: 'NOT_STARTED',
  PREPARING: 'PREPARING',
  EXECUTING: 'EXECUTING',
  PAUSED: 'PAUSED',
  COMPLETED: 'COMPLETED',
  FAILED: 'FAILED',
  CANCELLED: 'CANCELLED'
}

// 风险等级
export const RISK_LEVELS = {
  LOW: 'LOW',
  MEDIUM: 'MEDIUM',
  HIGH: 'HIGH',
  CRITICAL: 'CRITICAL'
}

// 优先级
export const PRIORITIES = {
  LOW: 'LOW',
  NORMAL: 'NORMAL',
  HIGH: 'HIGH',
  URGENT: 'URGENT'
}

// 税种类型
export const TAX_TYPES = {
  VAT: 'VAT',
  CORPORATE_INCOME_TAX: 'CORPORATE_INCOME_TAX',
  INDIVIDUAL_INCOME_TAX: 'INDIVIDUAL_INCOME_TAX',
  BUSINESS_TAX: 'BUSINESS_TAX',
  CONSUMPTION_TAX: 'CONSUMPTION_TAX',
  STAMP_TAX: 'STAMP_TAX',
  PROPERTY_TAX: 'PROPERTY_TAX',
  LAND_USE_TAX: 'LAND_USE_TAX',
  VEHICLE_TAX: 'VEHICLE_TAX',
  OTHER: 'OTHER'
}

// ==================== 工具函数 ====================

/**
 * 获取筹划类型标签
 */
export function getPlanningTypeLabel(type) {
  const labels = {
    TAX_REDUCTION: '减税筹划',
    TAX_DEFERRAL: '延税筹划',
    TAX_EXEMPTION: '免税筹划',
    TAX_CREDIT: '税收抵免',
    STRUCTURE_OPTIMIZATION: '结构优化',
    BUSINESS_RESTRUCTURING: '业务重组',
    INVESTMENT_PLANNING: '投资筹划',
    MERGER_ACQUISITION: '并购筹划',
    INTERNATIONAL_PLANNING: '国际筹划',
    OTHER: '其他'
  }
  return labels[type] || type
}

/**
 * 获取筹划状态标签
 */
export function getPlanningStatusLabel(status) {
  const labels = {
    DRAFT: '草稿',
    UNDER_REVIEW: '审核中',
    APPROVED: '已审批',
    REJECTED: '已拒绝',
    SUSPENDED: '已暂停',
    CANCELLED: '已取消',
    ARCHIVED: '已归档'
  }
  return labels[status] || status
}

/**
 * 获取执行状态标签
 */
export function getExecutionStatusLabel(status) {
  const labels = {
    NOT_STARTED: '未开始',
    PREPARING: '准备中',
    EXECUTING: '执行中',
    PAUSED: '已暂停',
    COMPLETED: '已完成',
    FAILED: '执行失败',
    CANCELLED: '已取消'
  }
  return labels[status] || status
}

/**
 * 获取风险等级标签
 */
export function getRiskLevelLabel(level) {
  const labels = {
    LOW: '低风险',
    MEDIUM: '中风险',
    HIGH: '高风险',
    CRITICAL: '极高风险'
  }
  return labels[level] || level
}

/**
 * 获取优先级标签
 */
export function getPriorityLabel(priority) {
  const labels = {
    LOW: '低',
    NORMAL: '普通',
    HIGH: '高',
    URGENT: '紧急'
  }
  return labels[priority] || priority
}

/**
 * 获取税种标签
 */
export function getTaxTypeLabel(type) {
  const labels = {
    VAT: '增值税',
    CORPORATE_INCOME_TAX: '企业所得税',
    INDIVIDUAL_INCOME_TAX: '个人所得税',
    BUSINESS_TAX: '营业税',
    CONSUMPTION_TAX: '消费税',
    STAMP_TAX: '印花税',
    PROPERTY_TAX: '房产税',
    LAND_USE_TAX: '土地使用税',
    VEHICLE_TAX: '车船税',
    OTHER: '其他'
  }
  return labels[type] || type
}

/**
 * 格式化金额
 */
export function formatAmount(amount) {
  if (!amount) return '0.00'
  return Number(amount).toLocaleString('zh-CN', {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2
  })
}

/**
 * 格式化百分比
 */
export function formatPercentage(value) {
  if (!value) return '0.00%'
  return (Number(value) * 100).toFixed(2) + '%'
}

/**
 * 计算筹划进度
 */
export function calculatePlanningProgress(status) {
  const progressMap = {
    DRAFT: 10,
    UNDER_REVIEW: 30,
    APPROVED: 50,
    PREPARING: 60,
    EXECUTING: 80,
    COMPLETED: 100,
    REJECTED: 0,
    SUSPENDED: 40,
    CANCELLED: 0,
    FAILED: 0
  }
  return progressMap[status] || 0
}

/**
 * 判断筹划是否逾期
 */
export function isPlanningOverdue(endTime, status) {
  if (!endTime || ['COMPLETED', 'CANCELLED', 'ARCHIVED'].includes(status)) {
    return false
  }
  return new Date(endTime) < new Date()
}

/**
 * 判断筹划是否即将到期
 */
export function isPlanningExpiringSoon(endTime, days = 7) {
  if (!endTime) return false
  const targetDate = new Date()
  targetDate.setDate(targetDate.getDate() + days)
  return new Date(endTime) <= targetDate && new Date(endTime) >= new Date()
}
