import request from '@/utils/request'

// ==================== 基础CRUD操作 ====================

/**
 * 创建合规检查
 */
export function createCompliance(data) {
  return request({
    url: '/accountant/ts/compliance/create',
    method: 'post',
    data
  })
}

/**
 * 更新合规检查
 */
export function updateCompliance(complianceId, data) {
  return request({
    url: `/accountant/ts/compliance/update/${complianceId}`,
    method: 'put',
    data
  })
}

/**
 * 删除合规检查
 */
export function deleteCompliance(complianceId) {
  return request({
    url: `/accountant/ts/compliance/delete/${complianceId}`,
    method: 'delete'
  })
}

/**
 * 获取合规检查详情
 */
export function getComplianceDetail(complianceId) {
  return request({
    url: `/accountant/ts/compliance/detail/${complianceId}`,
    method: 'get'
  })
}

/**
 * 根据编号获取合规检查详情
 */
export function getComplianceByCode(complianceCode) {
  return request({
    url: `/accountant/ts/compliance/detail/code/${complianceCode}`,
    method: 'get'
  })
}

/**
 * 分页查询合规检查
 */
export function getCompliancePage(params) {
  return request({
    url: '/accountant/ts/compliance/page',
    method: 'get',
    params
  })
}

// ==================== 合规检查管理功能 ====================

/**
 * 生成合规检查编号
 */
export function generateComplianceCode() {
  return request({
    url: '/accountant/ts/compliance/generate-code',
    method: 'post'
  })
}

/**
 * 验证合规检查数据
 */
export function validateComplianceData(data) {
  return request({
    url: '/accountant/ts/compliance/validate',
    method: 'post',
    data
  })
}

/**
 * 启动合规检查
 */
export function startComplianceCheck(complianceId) {
  return request({
    url: `/accountant/ts/compliance/start/${complianceId}`,
    method: 'post'
  })
}

/**
 * 暂停合规检查
 */
export function pauseComplianceCheck(complianceId) {
  return request({
    url: `/accountant/ts/compliance/pause/${complianceId}`,
    method: 'post'
  })
}

/**
 * 恢复合规检查
 */
export function resumeComplianceCheck(complianceId) {
  return request({
    url: `/accountant/ts/compliance/resume/${complianceId}`,
    method: 'post'
  })
}

/**
 * 完成合规检查
 */
export function completeComplianceCheck(complianceId, data) {
  return request({
    url: `/accountant/ts/compliance/complete/${complianceId}`,
    method: 'post',
    data
  })
}

/**
 * 取消合规检查
 */
export function cancelComplianceCheck(complianceId, reason) {
  return request({
    url: `/accountant/ts/compliance/cancel/${complianceId}`,
    method: 'post',
    params: { reason }
  })
}

/**
 * 更新检查进度
 */
export function updateCheckProgress(complianceId, progress) {
  return request({
    url: `/accountant/ts/compliance/update-progress/${complianceId}`,
    method: 'post',
    params: { progress }
  })
}

// ==================== 合规规则管理 ====================

/**
 * 配置合规规则
 */
export function configureComplianceRule(complianceId, data) {
  return request({
    url: `/accountant/ts/compliance/configure-rule/${complianceId}`,
    method: 'post',
    data
  })
}

/**
 * 执行合规检查
 */
export function executeComplianceCheck(complianceId) {
  return request({
    url: `/accountant/ts/compliance/execute/${complianceId}`,
    method: 'post'
  })
}

/**
 * 评估合规风险
 */
export function assessComplianceRisk(complianceId) {
  return request({
    url: `/accountant/ts/compliance/assess-risk/${complianceId}`,
    method: 'post'
  })
}

/**
 * 生成合规报告
 */
export function generateComplianceReport(complianceId) {
  return request({
    url: `/accountant/ts/compliance/generate-report/${complianceId}`,
    method: 'post'
  })
}

/**
 * 分析合规结果
 */
export function analyzeComplianceResult(complianceId) {
  return request({
    url: `/accountant/ts/compliance/analyze-result/${complianceId}`,
    method: 'post'
  })
}

// ==================== 整改管理功能 ====================

/**
 * 创建整改计划
 */
export function createRectificationPlan(complianceId, data) {
  return request({
    url: `/accountant/ts/compliance/create-rectification-plan/${complianceId}`,
    method: 'post',
    data
  })
}

/**
 * 更新整改进度
 */
export function updateRectificationProgress(complianceId, progress) {
  return request({
    url: `/accountant/ts/compliance/update-rectification-progress/${complianceId}`,
    method: 'post',
    params: { progress }
  })
}

/**
 * 完成整改
 */
export function completeRectification(complianceId, data) {
  return request({
    url: `/accountant/ts/compliance/complete-rectification/${complianceId}`,
    method: 'post',
    data
  })
}

/**
 * 申请复查
 */
export function requestRecheck(complianceId) {
  return request({
    url: `/accountant/ts/compliance/request-recheck/${complianceId}`,
    method: 'post'
  })
}

/**
 * 执行复查
 */
export function executeRecheck(complianceId) {
  return request({
    url: `/accountant/ts/compliance/execute-recheck/${complianceId}`,
    method: 'post'
  })
}

/**
 * 完成复查
 */
export function completeRecheck(complianceId, data) {
  return request({
    url: `/accountant/ts/compliance/complete-recheck/${complianceId}`,
    method: 'post',
    data
  })
}

// ==================== 查询统计功能 ====================

/**
 * 根据检查类型查询
 */
export function getCompliancesByType(complianceType) {
  return request({
    url: '/accountant/ts/compliance/list/by-type',
    method: 'get',
    params: { complianceType }
  })
}

/**
 * 根据检查状态查询
 */
export function getCompliancesByCheckStatus(checkStatus) {
  return request({
    url: '/accountant/ts/compliance/list/by-check-status',
    method: 'get',
    params: { checkStatus }
  })
}

/**
 * 根据合规状态查询
 */
export function getCompliancesByComplianceStatus(complianceStatus) {
  return request({
    url: '/accountant/ts/compliance/list/by-compliance-status',
    method: 'get',
    params: { complianceStatus }
  })
}

/**
 * 根据风险等级查询
 */
export function getCompliancesByRiskLevel(riskLevel) {
  return request({
    url: '/accountant/ts/compliance/list/by-risk-level',
    method: 'get',
    params: { riskLevel }
  })
}

/**
 * 根据优先级查询
 */
export function getCompliancesByPriority(priority) {
  return request({
    url: '/accountant/ts/compliance/list/by-priority',
    method: 'get',
    params: { priority }
  })
}

/**
 * 根据检查人员查询
 */
export function getCompliancesByChecker(checker) {
  return request({
    url: '/accountant/ts/compliance/list/by-checker',
    method: 'get',
    params: { checker }
  })
}

/**
 * 根据整改状态查询
 */
export function getCompliancesByRectificationStatus(rectificationStatus) {
  return request({
    url: '/accountant/ts/compliance/list/by-rectification-status',
    method: 'get',
    params: { rectificationStatus }
  })
}

/**
 * 查询即将到期的检查
 */
export function getExpiringSoonCompliances(days = 7) {
  return request({
    url: '/accountant/ts/compliance/list/expiring-soon',
    method: 'get',
    params: { days }
  })
}

/**
 * 查询逾期的检查
 */
export function getOverdueCompliances() {
  return request({
    url: '/accountant/ts/compliance/list/overdue',
    method: 'get'
  })
}

/**
 * 查询高风险检查
 */
export function getHighRiskCompliances() {
  return request({
    url: '/accountant/ts/compliance/list/high-risk',
    method: 'get'
  })
}

/**
 * 查询需要整改的检查
 */
export function getNeedRectificationCompliances() {
  return request({
    url: '/accountant/ts/compliance/list/need-rectification',
    method: 'get'
  })
}

// ==================== 统计分析功能 ====================

/**
 * 获取合规检查概览
 */
export function getComplianceOverview() {
  return request({
    url: '/accountant/ts/compliance/overview',
    method: 'get'
  })
}

/**
 * 按检查状态统计数量
 */
export function countCompliancesByCheckStatus() {
  return request({
    url: '/accountant/ts/compliance/stats/by-check-status',
    method: 'get'
  })
}

/**
 * 按合规状态统计数量
 */
export function countCompliancesByComplianceStatus() {
  return request({
    url: '/accountant/ts/compliance/stats/by-compliance-status',
    method: 'get'
  })
}

/**
 * 按检查类型统计数量
 */
export function countCompliancesByType() {
  return request({
    url: '/accountant/ts/compliance/stats/by-type',
    method: 'get'
  })
}

/**
 * 按风险等级统计数量
 */
export function countCompliancesByRiskLevel() {
  return request({
    url: '/accountant/ts/compliance/stats/by-risk-level',
    method: 'get'
  })
}

/**
 * 按优先级统计数量
 */
export function countCompliancesByPriority() {
  return request({
    url: '/accountant/ts/compliance/stats/by-priority',
    method: 'get'
  })
}

/**
 * 按整改状态统计数量
 */
export function countCompliancesByRectificationStatus() {
  return request({
    url: '/accountant/ts/compliance/stats/by-rectification-status',
    method: 'get'
  })
}

/**
 * 获取检查趋势数据
 */
export function getCheckTrend(startDate, endDate, groupBy = 'day') {
  return request({
    url: '/accountant/ts/compliance/trend/check',
    method: 'get',
    params: { startDate, endDate, groupBy }
  })
}

/**
 * 获取合规评分趋势
 */
export function getComplianceScoreTrend(startDate, endDate, groupBy = 'day') {
  return request({
    url: '/accountant/ts/compliance/trend/compliance-score',
    method: 'get',
    params: { startDate, endDate, groupBy }
  })
}

/**
 * 获取风险分布数据
 */
export function getRiskDistribution() {
  return request({
    url: '/accountant/ts/compliance/distribution/risk',
    method: 'get'
  })
}

/**
 * 获取效果评估数据
 */
export function getEffectivenessData() {
  return request({
    url: '/accountant/ts/compliance/effectiveness',
    method: 'get'
  })
}

/**
 * 获取检查排行榜
 */
export function getCheckRanking(rankBy = 'complianceScore', limit = 10) {
  return request({
    url: '/accountant/ts/compliance/ranking',
    method: 'get',
    params: { rankBy, limit }
  })
}

/**
 * 获取检查效率统计
 */
export function getCheckEfficiencyStats() {
  return request({
    url: '/accountant/ts/compliance/efficiency-stats',
    method: 'get'
  })
}

// ==================== 批量操作功能 ====================

/**
 * 批量创建合规检查
 */
export function batchCreateCompliances(data) {
  return request({
    url: '/accountant/ts/compliance/batch/create',
    method: 'post',
    data
  })
}

/**
 * 批量更新检查状态
 */
export function batchUpdateCheckStatus(complianceIds, checkStatus) {
  return request({
    url: '/accountant/ts/compliance/batch/update-check-status',
    method: 'post',
    data: complianceIds,
    params: { checkStatus }
  })
}

/**
 * 批量更新合规状态
 */
export function batchUpdateComplianceStatus(complianceIds, complianceStatus) {
  return request({
    url: '/accountant/ts/compliance/batch/update-compliance-status',
    method: 'post',
    data: complianceIds,
    params: { complianceStatus }
  })
}

/**
 * 批量更新整改状态
 */
export function batchUpdateRectificationStatus(complianceIds, rectificationStatus) {
  return request({
    url: '/accountant/ts/compliance/batch/update-rectification-status',
    method: 'post',
    data: complianceIds,
    params: { rectificationStatus }
  })
}

/**
 * 批量删除合规检查
 */
export function batchDeleteCompliances(complianceIds) {
  return request({
    url: '/accountant/ts/compliance/batch/delete',
    method: 'post',
    data: complianceIds
  })
}

/**
 * 批量归档合规检查
 */
export function batchArchiveCompliances(complianceIds) {
  return request({
    url: '/accountant/ts/compliance/batch/archive',
    method: 'post',
    data: complianceIds
  })
}

/**
 * 批量激活合规检查
 */
export function batchActivateCompliances(complianceIds) {
  return request({
    url: '/accountant/ts/compliance/batch/activate',
    method: 'post',
    data: complianceIds
  })
}

/**
 * 批量导入合规检查
 */
export function batchImportCompliances(data) {
  return request({
    url: '/accountant/ts/compliance/batch/import',
    method: 'post',
    data
  })
}

/**
 * 批量导出合规检查
 */
export function batchExportCompliances(complianceIds) {
  return request({
    url: '/accountant/ts/compliance/batch/export',
    method: 'post',
    data: complianceIds
  })
}

// ==================== 工具功能 ====================

/**
 * 复制合规检查
 */
export function copyCompliance(sourceComplianceId, newComplianceName) {
  return request({
    url: `/accountant/ts/compliance/copy/${sourceComplianceId}`,
    method: 'post',
    params: { newComplianceName }
  })
}

/**
 * 发送合规提醒
 */
export function sendComplianceReminder(complianceId, reminderType) {
  return request({
    url: `/accountant/ts/compliance/send-reminder/${complianceId}`,
    method: 'post',
    params: { reminderType }
  })
}

// ==================== 系统维护功能 ====================

/**
 * 系统健康检查
 */
export function systemHealthCheck() {
  return request({
    url: '/accountant/ts/compliance/system/health-check',
    method: 'get'
  })
}

/**
 * 数据一致性检查
 */
export function dataConsistencyCheck() {
  return request({
    url: '/accountant/ts/compliance/system/data-consistency-check',
    method: 'get'
  })
}

/**
 * 性能统计
 */
export function performanceStats() {
  return request({
    url: '/accountant/ts/compliance/system/performance-stats',
    method: 'get'
  })
}

/**
 * 清理过期数据
 */
export function cleanupExpiredData(days = 30) {
  return request({
    url: '/accountant/ts/compliance/system/cleanup-expired-data',
    method: 'post',
    params: { days }
  })
}

// ==================== 常量定义 ====================

// 检查状态
export const CHECK_STATUS = {
  DRAFT: 'DRAFT',
  IN_PROGRESS: 'IN_PROGRESS',
  PAUSED: 'PAUSED',
  COMPLETED: 'COMPLETED',
  CANCELLED: 'CANCELLED',
  FAILED: 'FAILED'
}

// 合规状态
export const COMPLIANCE_STATUS = {
  PENDING: 'PENDING',
  COMPLIANT: 'COMPLIANT',
  PARTIALLY_COMPLIANT: 'PARTIALLY_COMPLIANT',
  NON_COMPLIANT: 'NON_COMPLIANT'
}

// 风险等级
export const RISK_LEVEL = {
  LOW: 'LOW',
  MEDIUM: 'MEDIUM',
  HIGH: 'HIGH',
  CRITICAL: 'CRITICAL'
}

// 优先级
export const PRIORITY = {
  LOW: 'LOW',
  NORMAL: 'NORMAL',
  HIGH: 'HIGH',
  URGENT: 'URGENT'
}

// 合规检查类型
export const COMPLIANCE_TYPE = {
  TAX_COMPLIANCE: 'TAX_COMPLIANCE',
  FINANCIAL_COMPLIANCE: 'FINANCIAL_COMPLIANCE',
  REGULATORY_COMPLIANCE: 'REGULATORY_COMPLIANCE',
  INTERNAL_COMPLIANCE: 'INTERNAL_COMPLIANCE',
  EXTERNAL_COMPLIANCE: 'EXTERNAL_COMPLIANCE'
}

// 整改状态
export const RECTIFICATION_STATUS = {
  NOT_REQUIRED: 'NOT_REQUIRED',
  PLANNED: 'PLANNED',
  IN_PROGRESS: 'IN_PROGRESS',
  COMPLETED: 'COMPLETED',
  OVERDUE: 'OVERDUE'
}

// 复查状态
export const RECHECK_STATUS = {
  NOT_REQUIRED: 'NOT_REQUIRED',
  REQUESTED: 'REQUESTED',
  IN_PROGRESS: 'IN_PROGRESS',
  COMPLETED: 'COMPLETED'
}

// 检查方法
export const CHECK_METHOD = {
  MANUAL: 'MANUAL',
  AUTOMATED: 'AUTOMATED',
  HYBRID: 'HYBRID'
}

// 检查频率
export const CHECK_FREQUENCY = {
  DAILY: 'DAILY',
  WEEKLY: 'WEEKLY',
  MONTHLY: 'MONTHLY',
  QUARTERLY: 'QUARTERLY',
  ANNUALLY: 'ANNUALLY',
  ON_DEMAND: 'ON_DEMAND'
}

// ==================== 工具函数 ====================

/**
 * 格式化检查状态
 */
export function formatCheckStatus(status) {
  const statusMap = {
    [CHECK_STATUS.DRAFT]: '草稿',
    [CHECK_STATUS.IN_PROGRESS]: '进行中',
    [CHECK_STATUS.PAUSED]: '已暂停',
    [CHECK_STATUS.COMPLETED]: '已完成',
    [CHECK_STATUS.CANCELLED]: '已取消',
    [CHECK_STATUS.FAILED]: '失败'
  }
  return statusMap[status] || status
}

/**
 * 格式化合规状态
 */
export function formatComplianceStatus(status) {
  const statusMap = {
    [COMPLIANCE_STATUS.PENDING]: '待检查',
    [COMPLIANCE_STATUS.COMPLIANT]: '合规',
    [COMPLIANCE_STATUS.PARTIALLY_COMPLIANT]: '部分合规',
    [COMPLIANCE_STATUS.NON_COMPLIANT]: '不合规'
  }
  return statusMap[status] || status
}

/**
 * 格式化风险等级
 */
export function formatRiskLevel(level) {
  const levelMap = {
    [RISK_LEVEL.LOW]: '低风险',
    [RISK_LEVEL.MEDIUM]: '中等风险',
    [RISK_LEVEL.HIGH]: '高风险',
    [RISK_LEVEL.CRITICAL]: '严重风险'
  }
  return levelMap[level] || level
}

/**
 * 格式化优先级
 */
export function formatPriority(priority) {
  const priorityMap = {
    [PRIORITY.LOW]: '低',
    [PRIORITY.NORMAL]: '普通',
    [PRIORITY.HIGH]: '高',
    [PRIORITY.URGENT]: '紧急'
  }
  return priorityMap[priority] || priority
}

/**
 * 格式化合规检查类型
 */
export function formatComplianceType(type) {
  const typeMap = {
    [COMPLIANCE_TYPE.TAX_COMPLIANCE]: '税务合规',
    [COMPLIANCE_TYPE.FINANCIAL_COMPLIANCE]: '财务合规',
    [COMPLIANCE_TYPE.REGULATORY_COMPLIANCE]: '监管合规',
    [COMPLIANCE_TYPE.INTERNAL_COMPLIANCE]: '内部合规',
    [COMPLIANCE_TYPE.EXTERNAL_COMPLIANCE]: '外部合规'
  }
  return typeMap[type] || type
}

/**
 * 格式化整改状态
 */
export function formatRectificationStatus(status) {
  const statusMap = {
    [RECTIFICATION_STATUS.NOT_REQUIRED]: '无需整改',
    [RECTIFICATION_STATUS.PLANNED]: '计划整改',
    [RECTIFICATION_STATUS.IN_PROGRESS]: '整改中',
    [RECTIFICATION_STATUS.COMPLETED]: '已完成',
    [RECTIFICATION_STATUS.OVERDUE]: '逾期'
  }
  return statusMap[status] || status
}

/**
 * 获取风险等级颜色
 */
export function getRiskLevelColor(level) {
  const colorMap = {
    [RISK_LEVEL.LOW]: 'success',
    [RISK_LEVEL.MEDIUM]: 'warning',
    [RISK_LEVEL.HIGH]: 'danger',
    [RISK_LEVEL.CRITICAL]: 'danger'
  }
  return colorMap[level] || 'info'
}

/**
 * 获取检查状态颜色
 */
export function getCheckStatusColor(status) {
  const colorMap = {
    [CHECK_STATUS.DRAFT]: 'info',
    [CHECK_STATUS.IN_PROGRESS]: 'primary',
    [CHECK_STATUS.PAUSED]: 'warning',
    [CHECK_STATUS.COMPLETED]: 'success',
    [CHECK_STATUS.CANCELLED]: 'danger',
    [CHECK_STATUS.FAILED]: 'danger'
  }
  return colorMap[status] || 'info'
}

/**
 * 获取合规状态颜色
 */
export function getComplianceStatusColor(status) {
  const colorMap = {
    [COMPLIANCE_STATUS.PENDING]: 'info',
    [COMPLIANCE_STATUS.COMPLIANT]: 'success',
    [COMPLIANCE_STATUS.PARTIALLY_COMPLIANT]: 'warning',
    [COMPLIANCE_STATUS.NON_COMPLIANT]: 'danger'
  }
  return colorMap[status] || 'info'
}

/**
 * 验证合规检查数据
 */
export function validateComplianceForm(form) {
  const errors = []

  if (!form.complianceName) {
    errors.push('合规检查名称不能为空')
  }

  if (!form.complianceType) {
    errors.push('合规检查类型不能为空')
  }

  if (!form.checkScope) {
    errors.push('检查范围不能为空')
  }

  if (form.startTime && form.endTime && new Date(form.startTime) > new Date(form.endTime)) {
    errors.push('开始时间不能晚于结束时间')
  }

  return {
    valid: errors.length === 0,
    errors
  }
}

/**
 * 计算合规评分等级
 */
export function getComplianceScoreLevel(score) {
  if (score >= 90) return '优秀'
  if (score >= 80) return '良好'
  if (score >= 60) return '一般'
  return '较差'
}

/**
 * 格式化进度百分比
 */
export function formatProgress(progress) {
  return `${Math.round(progress || 0)}%`
}
