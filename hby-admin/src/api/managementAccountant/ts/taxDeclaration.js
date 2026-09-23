import request from '@/utils/request'

// 税务申报API基础路径
const API_BASE_PATH = '/accountant/ts/tax-declaration'

// ==================== 基础CRUD操作 ====================

/**
 * 创建税务申报
 * @param {Object} data 申报数据
 */
export function createDeclaration(data) {
  return request({
    url: `${API_BASE_PATH}/create`,
    method: 'post',
    data
  })
}

/**
 * 更新税务申报
 * @param {Object} data 申报数据
 */
export function updateDeclaration(data) {
  return request({
    url: `${API_BASE_PATH}/update`,
    method: 'put',
    data
  })
}

/**
 * 删除税务申报
 * @param {number} tenantId 租户ID
 * @param {number} declarationId 申报ID
 */
export function deleteDeclaration(tenantId, declarationId) {
  return request({
    url: `${API_BASE_PATH}/delete/${tenantId}/${declarationId}`,
    method: 'delete'
  })
}

/**
 * 获取税务申报详情
 * @param {number} tenantId 租户ID
 * @param {number} declarationId 申报ID
 */
export function getDeclarationDetail(tenantId, declarationId) {
  return request({
    url: `${API_BASE_PATH}/detail/${tenantId}/${declarationId}`,
    method: 'get'
  })
}

/**
 * 分页查询税务申报列表
 * @param {Object} params 查询参数
 */
export function getDeclarationPage(params) {
  return request({
    url: `${API_BASE_PATH}/page`,
    method: 'get',
    params
  })
}

// ==================== 申报管理功能 ====================

/**
 * 生成申报计划
 * @param {Object} params 生成参数
 */
export function generateDeclarationPlan(params) {
  return request({
    url: `${API_BASE_PATH}/generate-plan`,
    method: 'post',
    params
  })
}

/**
 * 自动填报申报表
 * @param {number} tenantId 租户ID
 * @param {number} declarationId 申报ID
 */
export function autoFillDeclaration(tenantId, declarationId) {
  return request({
    url: `${API_BASE_PATH}/auto-fill/${tenantId}/${declarationId}`,
    method: 'post'
  })
}

/**
 * 提交申报
 * @param {number} tenantId 租户ID
 * @param {number} declarationId 申报ID
 */
export function submitDeclaration(tenantId, declarationId) {
  return request({
    url: `${API_BASE_PATH}/submit/${tenantId}/${declarationId}`,
    method: 'post'
  })
}

/**
 * 撤回申报
 * @param {number} tenantId 租户ID
 * @param {number} declarationId 申报ID
 */
export function withdrawDeclaration(tenantId, declarationId) {
  return request({
    url: `${API_BASE_PATH}/withdraw/${tenantId}/${declarationId}`,
    method: 'post'
  })
}

/**
 * 审核申报
 * @param {number} tenantId 租户ID
 * @param {number} declarationId 申报ID
 * @param {Object} params 审核参数
 */
export function reviewDeclaration(tenantId, declarationId, params) {
  return request({
    url: `${API_BASE_PATH}/review/${tenantId}/${declarationId}`,
    method: 'post',
    params
  })
}

/**
 * 批量提交申报
 * @param {number} tenantId 租户ID
 * @param {Array} declarationIds 申报ID列表
 */
export function batchSubmitDeclarations(tenantId, declarationIds) {
  return request({
    url: `${API_BASE_PATH}/batch-submit`,
    method: 'post',
    params: { tenantId },
    data: declarationIds
  })
}

/**
 * 批量审核申报
 * @param {number} tenantId 租户ID
 * @param {Array} declarationIds 申报ID列表
 * @param {Object} params 审核参数
 */
export function batchReviewDeclarations(tenantId, declarationIds, params) {
  return request({
    url: `${API_BASE_PATH}/batch-review`,
    method: 'post',
    params: { tenantId, ...params },
    data: declarationIds
  })
}

// ==================== 申报计算功能 ====================

/**
 * 计算税额
 * @param {number} tenantId 租户ID
 * @param {number} declarationId 申报ID
 */
export function calculateTaxAmount(tenantId, declarationId) {
  return request({
    url: `${API_BASE_PATH}/calculate-tax/${tenantId}/${declarationId}`,
    method: 'post'
  })
}

/**
 * 重新计算税额
 * @param {number} tenantId 租户ID
 * @param {number} declarationId 申报ID
 */
export function recalculateTaxAmount(tenantId, declarationId) {
  return request({
    url: `${API_BASE_PATH}/recalculate-tax/${tenantId}/${declarationId}`,
    method: 'post'
  })
}

/**
 * 验证申报数据
 * @param {number} tenantId 租户ID
 * @param {number} declarationId 申报ID
 */
export function validateDeclarationData(tenantId, declarationId) {
  return request({
    url: `${API_BASE_PATH}/validate-data/${tenantId}/${declarationId}`,
    method: 'post'
  })
}

/**
 * 获取计税依据
 * @param {number} tenantId 租户ID
 * @param {number} declarationId 申报ID
 */
export function getTaxBase(tenantId, declarationId) {
  return request({
    url: `${API_BASE_PATH}/tax-base/${tenantId}/${declarationId}`,
    method: 'get'
  })
}

// ==================== 申报跟踪功能 ====================

/**
 * 获取申报进度
 * @param {number} tenantId 租户ID
 * @param {number} declarationId 申报ID
 */
export function getDeclarationProgress(tenantId, declarationId) {
  return request({
    url: `${API_BASE_PATH}/progress/${tenantId}/${declarationId}`,
    method: 'get'
  })
}

/**
 * 更新申报状态
 * @param {number} tenantId 租户ID
 * @param {number} declarationId 申报ID
 * @param {Object} params 状态参数
 */
export function updateDeclarationStatus(tenantId, declarationId, params) {
  return request({
    url: `${API_BASE_PATH}/update-status/${tenantId}/${declarationId}`,
    method: 'post',
    params
  })
}

/**
 * 获取申报历史
 * @param {number} tenantId 租户ID
 * @param {number} declarationId 申报ID
 */
export function getDeclarationHistory(tenantId, declarationId) {
  return request({
    url: `${API_BASE_PATH}/history/${tenantId}/${declarationId}`,
    method: 'get'
  })
}

/**
 * 获取申报日志
 * @param {number} tenantId 租户ID
 * @param {number} declarationId 申报ID
 */
export function getDeclarationLogs(tenantId, declarationId) {
  return request({
    url: `${API_BASE_PATH}/logs/${tenantId}/${declarationId}`,
    method: 'get'
  })
}

// ==================== 申报提醒功能 ====================

/**
 * 获取待申报列表
 * @param {number} tenantId 租户ID
 * @param {number} limit 限制数量
 */
export function getPendingDeclarations(tenantId, limit = 100) {
  return request({
    url: `${API_BASE_PATH}/pending/${tenantId}`,
    method: 'get',
    params: { limit }
  })
}

/**
 * 获取待审核申报列表
 * @param {number} tenantId 租户ID
 * @param {number} limit 限制数量
 */
export function getPendingReviewDeclarations(tenantId, limit = 100) {
  return request({
    url: `${API_BASE_PATH}/pending-review/${tenantId}`,
    method: 'get',
    params: { limit }
  })
}

/**
 * 获取逾期申报列表
 * @param {number} tenantId 租户ID
 */
export function getOverdueDeclarations(tenantId) {
  return request({
    url: `${API_BASE_PATH}/overdue/${tenantId}`,
    method: 'get'
  })
}

/**
 * 获取即将到期申报列表
 * @param {number} tenantId 租户ID
 * @param {number} days 天数
 */
export function getUpcomingDeclarations(tenantId, days = 7) {
  return request({
    url: `${API_BASE_PATH}/upcoming/${tenantId}`,
    method: 'get',
    params: { days }
  })
}

/**
 * 发送申报提醒
 * @param {number} tenantId 租户ID
 * @param {number} declarationId 申报ID
 */
export function sendDeclarationReminder(tenantId, declarationId) {
  return request({
    url: `${API_BASE_PATH}/send-reminder/${tenantId}/${declarationId}`,
    method: 'post'
  })
}

/**
 * 批量发送申报提醒
 * @param {number} tenantId 租户ID
 * @param {Array} declarationIds 申报ID列表
 */
export function batchSendDeclarationReminders(tenantId, declarationIds) {
  return request({
    url: `${API_BASE_PATH}/batch-send-reminders`,
    method: 'post',
    params: { tenantId },
    data: declarationIds
  })
}

// ==================== 统计分析功能 ====================

/**
 * 获取申报概览
 * @param {number} tenantId 租户ID
 * @param {Object} params 查询参数
 */
export function getDeclarationOverview(tenantId, params = {}) {
  return request({
    url: `${API_BASE_PATH}/overview/${tenantId}`,
    method: 'get',
    params
  })
}

/**
 * 统计申报数量按状态分组
 * @param {number} tenantId 租户ID
 * @param {Object} params 查询参数
 */
export function countDeclarationsByStatus(tenantId, params = {}) {
  return request({
    url: `${API_BASE_PATH}/stats/by-status/${tenantId}`,
    method: 'get',
    params
  })
}

/**
 * 统计申报数量按税种分组
 * @param {number} tenantId 租户ID
 * @param {Object} params 查询参数
 */
export function countDeclarationsByTaxType(tenantId, params = {}) {
  return request({
    url: `${API_BASE_PATH}/stats/by-tax-type/${tenantId}`,
    method: 'get',
    params
  })
}

/**
 * 统计申报数量按类型分组
 * @param {number} tenantId 租户ID
 * @param {Object} params 查询参数
 */
export function countDeclarationsByType(tenantId, params = {}) {
  return request({
    url: `${API_BASE_PATH}/stats/by-type/${tenantId}`,
    method: 'get',
    params
  })
}

/**
 * 统计申报金额按月份分组
 * @param {number} tenantId 租户ID
 * @param {number} year 年份
 */
export function sumTaxAmountByMonth(tenantId, year) {
  return request({
    url: `${API_BASE_PATH}/stats/tax-amount-by-month/${tenantId}`,
    method: 'get',
    params: { year }
  })
}

/**
 * 统计申报金额按税种分组
 * @param {number} tenantId 租户ID
 * @param {Object} params 查询参数
 */
export function sumTaxAmountByTaxType(tenantId, params = {}) {
  return request({
    url: `${API_BASE_PATH}/stats/tax-amount-by-tax-type/${tenantId}`,
    method: 'get',
    params
  })
}

/**
 * 计算申报及时率
 * @param {number} tenantId 租户ID
 * @param {Object} params 查询参数
 */
export function calculateTimelyRate(tenantId, params = {}) {
  return request({
    url: `${API_BASE_PATH}/stats/timely-rate/${tenantId}`,
    method: 'get',
    params
  })
}

/**
 * 计算申报成功率
 * @param {number} tenantId 租户ID
 * @param {Object} params 查询参数
 */
export function calculateSuccessRate(tenantId, params = {}) {
  return request({
    url: `${API_BASE_PATH}/stats/success-rate/${tenantId}`,
    method: 'get',
    params
  })
}

/**
 * 统计申报处理时长
 * @param {number} tenantId 租户ID
 * @param {Object} params 查询参数
 */
export function calculateProcessingTimeStats(tenantId, params = {}) {
  return request({
    url: `${API_BASE_PATH}/stats/processing-time/${tenantId}`,
    method: 'get',
    params
  })
}

/**
 * 获取申报趋势数据
 * @param {number} tenantId 租户ID
 * @param {Object} params 查询参数
 */
export function getDeclarationTrend(tenantId, params = {}) {
  return request({
    url: `${API_BASE_PATH}/stats/declaration-trend/${tenantId}`,
    method: 'get',
    params
  })
}

/**
 * 获取税额趋势数据
 * @param {number} tenantId 租户ID
 * @param {Object} params 查询参数
 */
export function getTaxAmountTrend(tenantId, params = {}) {
  return request({
    url: `${API_BASE_PATH}/stats/tax-amount-trend/${tenantId}`,
    method: 'get',
    params
  })
}

/**
 * 获取申报效率统计
 * @param {number} tenantId 租户ID
 * @param {Object} params 查询参数
 */
export function getDeclarationEfficiencyStats(tenantId, params = {}) {
  return request({
    url: `${API_BASE_PATH}/stats/efficiency/${tenantId}`,
    method: 'get',
    params
  })
}

/**
 * 获取申报质量统计
 * @param {number} tenantId 租户ID
 * @param {Object} params 查询参数
 */
export function getDeclarationQualityStats(tenantId, params = {}) {
  return request({
    url: `${API_BASE_PATH}/stats/quality/${tenantId}`,
    method: 'get',
    params
  })
}

/**
 * 获取合规性统计
 * @param {number} tenantId 租户ID
 * @param {Object} params 查询参数
 */
export function getComplianceStats(tenantId, params = {}) {
  return request({
    url: `${API_BASE_PATH}/stats/compliance/${tenantId}`,
    method: 'get',
    params
  })
}

/**
 * 获取风险分析
 * @param {number} tenantId 租户ID
 * @param {Object} params 查询参数
 */
export function getRiskAnalysis(tenantId, params = {}) {
  return request({
    url: `${API_BASE_PATH}/stats/risk-analysis/${tenantId}`,
    method: 'get',
    params
  })
}

/**
 * 获取成本分析
 * @param {number} tenantId 租户ID
 * @param {Object} params 查询参数
 */
export function getCostAnalysis(tenantId, params = {}) {
  return request({
    url: `${API_BASE_PATH}/stats/cost-analysis/${tenantId}`,
    method: 'get',
    params
  })
}

// ==================== 批量操作功能 ====================

/**
 * 批量更新申报状态
 * @param {number} tenantId 租户ID
 * @param {Array} declarationIds 申报ID列表
 * @param {string} status 状态
 */
export function batchUpdateStatus(tenantId, declarationIds, status) {
  return request({
    url: `${API_BASE_PATH}/batch-update-status`,
    method: 'post',
    params: { tenantId, status },
    data: declarationIds
  })
}

/**
 * 批量删除申报
 * @param {number} tenantId 租户ID
 * @param {Array} declarationIds 申报ID列表
 */
export function batchDeleteDeclarations(tenantId, declarationIds) {
  return request({
    url: `${API_BASE_PATH}/batch-delete`,
    method: 'post',
    params: { tenantId },
    data: declarationIds
  })
}

/**
 * 批量导入申报
 * @param {number} tenantId 租户ID
 * @param {Array} declarationData 申报数据列表
 */
export function batchImportDeclarations(tenantId, declarationData) {
  return request({
    url: `${API_BASE_PATH}/batch-import`,
    method: 'post',
    params: { tenantId },
    data: declarationData
  })
}

/**
 * 批量导出申报
 * @param {number} tenantId 租户ID
 * @param {Array} declarationIds 申报ID列表
 */
export function batchExportDeclarations(tenantId, declarationIds) {
  return request({
    url: `${API_BASE_PATH}/batch-export`,
    method: 'post',
    params: { tenantId },
    data: declarationIds
  })
}

// ==================== 查询功能 ====================

/**
 * 根据申报编号查询申报
 * @param {number} tenantId 租户ID
 * @param {string} declarationCode 申报编号
 */
export function getDeclarationByCode(tenantId, declarationCode) {
  return request({
    url: `${API_BASE_PATH}/by-code/${tenantId}/${declarationCode}`,
    method: 'get'
  })
}

/**
 * 根据纳税人识别号查询申报列表
 * @param {number} tenantId 租户ID
 * @param {string} taxpayerId 纳税人识别号
 */
export function getDeclarationsByTaxpayerId(tenantId, taxpayerId) {
  return request({
    url: `${API_BASE_PATH}/by-taxpayer/${tenantId}/${taxpayerId}`,
    method: 'get'
  })
}

/**
 * 高级搜索申报
 * @param {Object} params 查询参数
 * @param {Object} searchParams 搜索参数
 */
export function advancedSearchDeclarations(params, searchParams) {
  return request({
    url: `${API_BASE_PATH}/advanced-search`,
    method: 'post',
    params,
    data: searchParams
  })
}

/**
 * 全文搜索申报
 * @param {Object} params 查询参数
 */
export function fullTextSearchDeclarations(params) {
  return request({
    url: `${API_BASE_PATH}/full-text-search`,
    method: 'get',
    params
  })
}

// ==================== 系统维护功能 ====================

/**
 * 获取异常申报列表
 * @param {number} tenantId 租户ID
 * @param {Array} errorTypes 错误类型列表
 */
export function getAbnormalDeclarations(tenantId, errorTypes) {
  return request({
    url: `${API_BASE_PATH}/abnormal/${tenantId}`,
    method: 'get',
    params: { errorTypes }
  })
}

/**
 * 获取需要重试的申报列表
 * @param {number} tenantId 租户ID
 */
export function getRetryDeclarations(tenantId) {
  return request({
    url: `${API_BASE_PATH}/retry/${tenantId}`,
    method: 'get'
  })
}

/**
 * 重试申报
 * @param {number} tenantId 租户ID
 * @param {number} declarationId 申报ID
 */
export function retryDeclaration(tenantId, declarationId) {
  return request({
    url: `${API_BASE_PATH}/retry/${tenantId}/${declarationId}`,
    method: 'post'
  })
}

/**
 * 清理过期申报
 * @param {number} tenantId 租户ID
 * @param {string} expiredDate 过期日期
 */
export function cleanExpiredDeclarations(tenantId, expiredDate) {
  return request({
    url: `${API_BASE_PATH}/clean-expired/${tenantId}`,
    method: 'post',
    params: { expiredDate }
  })
}

/**
 * 系统健康检查
 * @param {number} tenantId 租户ID
 */
export function healthCheck(tenantId) {
  return request({
    url: `${API_BASE_PATH}/health-check/${tenantId}`,
    method: 'get'
  })
}

/**
 * 获取系统性能指标
 * @param {number} tenantId 租户ID
 */
export function getPerformanceMetrics(tenantId) {
  return request({
    url: `${API_BASE_PATH}/performance-metrics/${tenantId}`,
    method: 'get'
  })
}

// ==================== 常量定义 ====================

// 申报状态
export const DECLARATION_STATUS = {
  DRAFT: 'DRAFT',                    // 草稿
  FILLING: 'FILLING',                // 填报中
  FILLED: 'FILLED',                  // 已填报
  SUBMITTING: 'SUBMITTING',          // 提交中
  SUBMITTED: 'SUBMITTED',            // 已提交
  UNDER_REVIEW: 'UNDER_REVIEW',      // 审核中
  APPROVED: 'APPROVED',              // 已审核
  REJECTED: 'REJECTED',              // 已拒绝
  COMPLETED: 'COMPLETED',            // 已完成
  WITHDRAWN: 'WITHDRAWN',            // 已撤回
  FILL_FAILED: 'FILL_FAILED',        // 填报失败
  SUBMIT_FAILED: 'SUBMIT_FAILED'     // 提交失败
}

// 申报状态标签
export const DECLARATION_STATUS_LABELS = {
  [DECLARATION_STATUS.DRAFT]: '草稿',
  [DECLARATION_STATUS.FILLING]: '填报中',
  [DECLARATION_STATUS.FILLED]: '已填报',
  [DECLARATION_STATUS.SUBMITTING]: '提交中',
  [DECLARATION_STATUS.SUBMITTED]: '已提交',
  [DECLARATION_STATUS.UNDER_REVIEW]: '审核中',
  [DECLARATION_STATUS.APPROVED]: '已审核',
  [DECLARATION_STATUS.REJECTED]: '已拒绝',
  [DECLARATION_STATUS.COMPLETED]: '已完成',
  [DECLARATION_STATUS.WITHDRAWN]: '已撤回',
  [DECLARATION_STATUS.FILL_FAILED]: '填报失败',
  [DECLARATION_STATUS.SUBMIT_FAILED]: '提交失败'
}

// 税种类型
export const TAX_TYPES = {
  VAT: 'VAT',                        // 增值税
  CIT: 'CIT',                        // 企业所得税
  IIT: 'IIT',                        // 个人所得税
  BT: 'BT',                          // 营业税
  CT: 'CT',                          // 消费税
  STAMP: 'STAMP',                    // 印花税
  DEED: 'DEED',                      // 契税
  LAND: 'LAND',                      // 土地使用税
  PROPERTY: 'PROPERTY',              // 房产税
  VEHICLE: 'VEHICLE'                 // 车船税
}

// 税种标签
export const TAX_TYPE_LABELS = {
  [TAX_TYPES.VAT]: '增值税',
  [TAX_TYPES.CIT]: '企业所得税',
  [TAX_TYPES.IIT]: '个人所得税',
  [TAX_TYPES.BT]: '营业税',
  [TAX_TYPES.CT]: '消费税',
  [TAX_TYPES.STAMP]: '印花税',
  [TAX_TYPES.DEED]: '契税',
  [TAX_TYPES.LAND]: '土地使用税',
  [TAX_TYPES.PROPERTY]: '房产税',
  [TAX_TYPES.VEHICLE]: '车船税'
}

// 申报类型
export const DECLARATION_TYPES = {
  MONTHLY: 'MONTHLY',                // 月度申报
  QUARTERLY: 'QUARTERLY',            // 季度申报
  YEARLY: 'YEARLY',                  // 年度申报
  SPECIAL: 'SPECIAL'                 // 专项申报
}

// 申报类型标签
export const DECLARATION_TYPE_LABELS = {
  [DECLARATION_TYPES.MONTHLY]: '月度申报',
  [DECLARATION_TYPES.QUARTERLY]: '季度申报',
  [DECLARATION_TYPES.YEARLY]: '年度申报',
  [DECLARATION_TYPES.SPECIAL]: '专项申报'
}

// 优先级
export const PRIORITIES = {
  LOW: 'LOW',                        // 低
  NORMAL: 'NORMAL',                  // 普通
  HIGH: 'HIGH',                      // 高
  URGENT: 'URGENT'                   // 紧急
}

// 优先级标签
export const PRIORITY_LABELS = {
  [PRIORITIES.LOW]: '低',
  [PRIORITIES.NORMAL]: '普通',
  [PRIORITIES.HIGH]: '高',
  [PRIORITIES.URGENT]: '紧急'
}

// ==================== 工具函数 ====================

/**
 * 获取申报状态标签
 * @param {string} status 状态
 */
export function getDeclarationStatusLabel(status) {
  return DECLARATION_STATUS_LABELS[status] || status
}

/**
 * 获取税种标签
 * @param {string} taxType 税种
 */
export function getTaxTypeLabel(taxType) {
  return TAX_TYPE_LABELS[taxType] || taxType
}

/**
 * 获取申报类型标签
 * @param {string} declarationType 申报类型
 */
export function getDeclarationTypeLabel(declarationType) {
  return DECLARATION_TYPE_LABELS[declarationType] || declarationType
}

/**
 * 获取优先级标签
 * @param {string} priority 优先级
 */
export function getPriorityLabel(priority) {
  return PRIORITY_LABELS[priority] || priority
}

/**
 * 格式化申报金额
 * @param {number} amount 金额
 */
export function formatTaxAmount(amount) {
  if (!amount) return '0.00'
  return Number(amount).toLocaleString('zh-CN', {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2
  })
}

/**
 * 计算申报进度百分比
 * @param {string} status 状态
 */
export function calculateDeclarationProgress(status) {
  const progressMap = {
    [DECLARATION_STATUS.DRAFT]: 10,
    [DECLARATION_STATUS.FILLING]: 30,
    [DECLARATION_STATUS.FILLED]: 50,
    [DECLARATION_STATUS.SUBMITTING]: 70,
    [DECLARATION_STATUS.SUBMITTED]: 80,
    [DECLARATION_STATUS.UNDER_REVIEW]: 90,
    [DECLARATION_STATUS.APPROVED]: 100,
    [DECLARATION_STATUS.COMPLETED]: 100
  }
  return progressMap[status] || 0
}

/**
 * 判断申报是否逾期
 * @param {string} deadline 截止日期
 * @param {string} status 状态
 */
export function isDeclarationOverdue(deadline, status) {
  if (!deadline) return false
  if ([DECLARATION_STATUS.COMPLETED, DECLARATION_STATUS.APPROVED].includes(status)) {
    return false
  }
  return new Date(deadline) < new Date()
}

/**
 * 判断申报是否即将到期
 * @param {string} deadline 截止日期
 * @param {number} days 天数
 */
export function isDeclarationUpcoming(deadline, days = 7) {
  if (!deadline) return false
  const deadlineDate = new Date(deadline)
  const upcomingDate = new Date()
  upcomingDate.setDate(upcomingDate.getDate() + days)
  return deadlineDate <= upcomingDate && deadlineDate >= new Date()
}

/**
 * 获取申报状态颜色
 * @param {string} status 状态
 */
export function getDeclarationStatusColor(status) {
  const colorMap = {
    [DECLARATION_STATUS.DRAFT]: '#909399',
    [DECLARATION_STATUS.FILLING]: '#E6A23C',
    [DECLARATION_STATUS.FILLED]: '#409EFF',
    [DECLARATION_STATUS.SUBMITTING]: '#E6A23C',
    [DECLARATION_STATUS.SUBMITTED]: '#409EFF',
    [DECLARATION_STATUS.UNDER_REVIEW]: '#E6A23C',
    [DECLARATION_STATUS.APPROVED]: '#67C23A',
    [DECLARATION_STATUS.REJECTED]: '#F56C6C',
    [DECLARATION_STATUS.COMPLETED]: '#67C23A',
    [DECLARATION_STATUS.WITHDRAWN]: '#909399',
    [DECLARATION_STATUS.FILL_FAILED]: '#F56C6C',
    [DECLARATION_STATUS.SUBMIT_FAILED]: '#F56C6C'
  }
  return colorMap[status] || '#909399'
}

/**
 * 快捷操作：创建月度申报
 * @param {number} tenantId 租户ID
 * @param {string} taxType 税种
 * @param {number} year 年份
 * @param {number} month 月份
 */
export function quickCreateMonthlyDeclaration(tenantId, taxType, year, month) {
  const data = {
    tenantId,
    declarationName: `${TAX_TYPE_LABELS[taxType]}${year}年${month}月申报`,
    taxType,
    declarationType: DECLARATION_TYPES.MONTHLY,
    declarationPeriod: `${year}-${String(month).padStart(2, '0')}`,
    declarationYear: year,
    declarationMonth: month,
    priority: PRIORITIES.NORMAL
  }
  return createDeclaration(data)
}

/**
 * 快捷操作：创建季度申报
 * @param {number} tenantId 租户ID
 * @param {string} taxType 税种
 * @param {number} year 年份
 * @param {number} quarter 季度
 */
export function quickCreateQuarterlyDeclaration(tenantId, taxType, year, quarter) {
  const data = {
    tenantId,
    declarationName: `${TAX_TYPE_LABELS[taxType]}${year}年第${quarter}季度申报`,
    taxType,
    declarationType: DECLARATION_TYPES.QUARTERLY,
    declarationPeriod: `${year}-Q${quarter}`,
    declarationYear: year,
    declarationQuarter: quarter,
    priority: PRIORITIES.NORMAL
  }
  return createDeclaration(data)
}

/**
 * 快捷操作：创建年度申报
 * @param {number} tenantId 租户ID
 * @param {string} taxType 税种
 * @param {number} year 年份
 */
export function quickCreateYearlyDeclaration(tenantId, taxType, year) {
  const data = {
    tenantId,
    declarationName: `${TAX_TYPE_LABELS[taxType]}${year}年度申报`,
    taxType,
    declarationType: DECLARATION_TYPES.YEARLY,
    declarationPeriod: year.toString(),
    declarationYear: year,
    priority: PRIORITIES.HIGH
  }
  return createDeclaration(data)
}
