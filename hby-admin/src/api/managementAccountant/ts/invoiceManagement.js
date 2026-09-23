import request from '@/utils/request'

// ==================== 基础CRUD操作 ====================

/**
 * 创建发票
 */
export function createInvoice(data) {
  return request({
    url: '/accountant/ts/invoice-management/create',
    method: 'post',
    data
  })
}

/**
 * 更新发票
 */
export function updateInvoice(data) {
  return request({
    url: '/accountant/ts/invoice-management/update',
    method: 'put',
    data
  })
}

/**
 * 删除发票
 */
export function deleteInvoice(tenantId, invoiceId) {
  return request({
    url: `/accountant/ts/invoice-management/delete/${tenantId}/${invoiceId}`,
    method: 'delete'
  })
}

/**
 * 获取发票详情
 */
export function getInvoiceDetail(tenantId, invoiceId) {
  return request({
    url: `/accountant/ts/invoice-management/detail/${tenantId}/${invoiceId}`,
    method: 'get'
  })
}

/**
 * 分页查询发票列表
 */
export function getInvoicePage(params) {
  return request({
    url: '/accountant/ts/invoice-management/page',
    method: 'get',
    params
  })
}

// ==================== 发票识别功能 ====================

/**
 * 上传发票文件
 */
export function uploadInvoiceFile(data) {
  return request({
    url: '/accountant/ts/invoice-management/upload',
    method: 'post',
    params: data
  })
}

/**
 * OCR识别发票
 */
export function recognizeInvoice(tenantId, invoiceId) {
  return request({
    url: `/accountant/ts/invoice-management/recognize/${tenantId}/${invoiceId}`,
    method: 'post'
  })
}

/**
 * 批量OCR识别发票
 */
export function batchRecognizeInvoices(tenantId, invoiceIds) {
  return request({
    url: '/accountant/ts/invoice-management/batch-recognize',
    method: 'post',
    params: { tenantId },
    data: invoiceIds
  })
}

/**
 * 获取待识别发票列表
 */
export function getPendingOcrInvoices(tenantId, limit = 100) {
  return request({
    url: `/accountant/ts/invoice-management/pending-ocr/${tenantId}`,
    method: 'get',
    params: { limit }
  })
}

/**
 * 更新OCR识别结果
 */
export function updateOcrResult(data) {
  return request({
    url: '/accountant/ts/invoice-management/update-ocr-result',
    method: 'put',
    params: data
  })
}

// ==================== 发票验真功能 ====================

/**
 * 验真发票
 */
export function verifyInvoice(tenantId, invoiceId) {
  return request({
    url: `/accountant/ts/invoice-management/verify/${tenantId}/${invoiceId}`,
    method: 'post'
  })
}

/**
 * 批量验真发票
 */
export function batchVerifyInvoices(tenantId, invoiceIds) {
  return request({
    url: '/accountant/ts/invoice-management/batch-verify',
    method: 'post',
    params: { tenantId },
    data: invoiceIds
  })
}

/**
 * 获取待验真发票列表
 */
export function getPendingVerificationInvoices(tenantId, limit = 100) {
  return request({
    url: `/accountant/ts/invoice-management/pending-verification/${tenantId}`,
    method: 'get',
    params: { limit }
  })
}

/**
 * 更新验真结果
 */
export function updateVerificationResult(data) {
  return request({
    url: '/accountant/ts/invoice-management/update-verification-result',
    method: 'put',
    params: data
  })
}

/**
 * 查询重复发票
 */
export function findDuplicateInvoices(params) {
  return request({
    url: '/accountant/ts/invoice-management/find-duplicates',
    method: 'get',
    params
  })
}

// ==================== 发票归档功能 ====================

/**
 * 归档发票
 */
export function archiveInvoice(tenantId, invoiceId, archivePath) {
  return request({
    url: `/accountant/ts/invoice-management/archive/${tenantId}/${invoiceId}`,
    method: 'post',
    params: { archivePath }
  })
}

/**
 * 批量归档发票
 */
export function batchArchiveInvoices(tenantId, invoiceIds, archiveBasePath) {
  return request({
    url: '/accountant/ts/invoice-management/batch-archive',
    method: 'post',
    params: { tenantId, archiveBasePath },
    data: invoiceIds
  })
}

/**
 * 获取待归档发票列表
 */
export function getPendingArchiveInvoices(tenantId, limit = 100) {
  return request({
    url: `/accountant/ts/invoice-management/pending-archive/${tenantId}`,
    method: 'get',
    params: { limit }
  })
}

// ==================== 风险识别功能 ====================

/**
 * 风险评估
 */
export function assessInvoiceRisk(tenantId, invoiceId) {
  return request({
    url: `/accountant/ts/invoice-management/assess-risk/${tenantId}/${invoiceId}`,
    method: 'post'
  })
}

/**
 * 批量风险评估
 */
export function batchAssessInvoiceRisk(tenantId, invoiceIds) {
  return request({
    url: '/accountant/ts/invoice-management/batch-assess-risk',
    method: 'post',
    params: { tenantId },
    data: invoiceIds
  })
}

/**
 * 获取高风险发票列表
 */
export function getHighRiskInvoices(tenantId, riskLevels) {
  return request({
    url: `/accountant/ts/invoice-management/high-risk/${tenantId}`,
    method: 'get',
    params: { riskLevels }
  })
}

// ==================== 统计分析功能 ====================

/**
 * 获取发票统计概览
 */
export function getInvoiceOverview(tenantId, startDate, endDate) {
  return request({
    url: `/accountant/ts/invoice-management/overview/${tenantId}`,
    method: 'get',
    params: { startDate, endDate }
  })
}

/**
 * 统计发票数量按状态分组
 */
export function countInvoicesByStatus(tenantId, startDate, endDate) {
  return request({
    url: `/accountant/ts/invoice-management/count-by-status/${tenantId}`,
    method: 'get',
    params: { startDate, endDate }
  })
}

/**
 * 统计发票数量按类型分组
 */
export function countInvoicesByType(tenantId, startDate, endDate) {
  return request({
    url: `/accountant/ts/invoice-management/count-by-type/${tenantId}`,
    method: 'get',
    params: { startDate, endDate }
  })
}

/**
 * 统计发票金额按月份分组
 */
export function sumAmountByMonth(tenantId, year) {
  return request({
    url: `/accountant/ts/invoice-management/sum-amount-by-month/${tenantId}`,
    method: 'get',
    params: { year }
  })
}

/**
 * 计算OCR识别成功率
 */
export function calculateOcrSuccessRate(tenantId, startDate, endDate) {
  return request({
    url: `/accountant/ts/invoice-management/ocr-success-rate/${tenantId}`,
    method: 'get',
    params: { startDate, endDate }
  })
}

/**
 * 计算验真成功率
 */
export function calculateVerificationSuccessRate(tenantId, startDate, endDate) {
  return request({
    url: `/accountant/ts/invoice-management/verification-success-rate/${tenantId}`,
    method: 'get',
    params: { startDate, endDate }
  })
}

/**
 * 统计风险发票分布
 */
export function countInvoicesByRiskLevel(tenantId, startDate, endDate) {
  return request({
    url: `/accountant/ts/invoice-management/count-by-risk-level/${tenantId}`,
    method: 'get',
    params: { startDate, endDate }
  })
}

/**
 * 计算处理时长统计
 */
export function calculateProcessingTimeStats(tenantId, startDate, endDate) {
  return request({
    url: `/accountant/ts/invoice-management/processing-time-stats/${tenantId}`,
    method: 'get',
    params: { startDate, endDate }
  })
}

/**
 * 计算金额统计
 */
export function calculateAmountStats(tenantId, startDate, endDate) {
  return request({
    url: `/accountant/ts/invoice-management/amount-stats/${tenantId}`,
    method: 'get',
    params: { startDate, endDate }
  })
}

/**
 * 计算税额统计
 */
export function calculateTaxAmountStats(tenantId, startDate, endDate) {
  return request({
    url: `/accountant/ts/invoice-management/tax-amount-stats/${tenantId}`,
    method: 'get',
    params: { startDate, endDate }
  })
}

/**
 * 获取发票数量趋势
 */
export function getInvoiceCountTrend(tenantId, startDate, endDate, groupBy = 'day') {
  return request({
    url: `/accountant/ts/invoice-management/count-trend/${tenantId}`,
    method: 'get',
    params: { startDate, endDate, groupBy }
  })
}

/**
 * 获取发票金额趋势
 */
export function getInvoiceAmountTrend(tenantId, startDate, endDate, groupBy = 'day') {
  return request({
    url: `/accountant/ts/invoice-management/amount-trend/${tenantId}`,
    method: 'get',
    params: { startDate, endDate, groupBy }
  })
}

/**
 * 获取处理效率统计
 */
export function getProcessingEfficiencyStats(tenantId, startDate, endDate) {
  return request({
    url: `/accountant/ts/invoice-management/processing-efficiency-stats/${tenantId}`,
    method: 'get',
    params: { startDate, endDate }
  })
}

/**
 * 获取发票质量统计
 */
export function getInvoiceQualityStats(tenantId, startDate, endDate) {
  return request({
    url: `/accountant/ts/invoice-management/quality-stats/${tenantId}`,
    method: 'get',
    params: { startDate, endDate }
  })
}

/**
 * 获取合规性统计
 */
export function getComplianceStats(tenantId, startDate, endDate) {
  return request({
    url: `/accountant/ts/invoice-management/compliance-stats/${tenantId}`,
    method: 'get',
    params: { startDate, endDate }
  })
}

/**
 * 获取风险分析
 */
export function getRiskAnalysis(tenantId, startDate, endDate) {
  return request({
    url: `/accountant/ts/invoice-management/risk-analysis/${tenantId}`,
    method: 'get',
    params: { startDate, endDate }
  })
}

/**
 * 获取成本分析
 */
export function getCostAnalysis(tenantId, startDate, endDate) {
  return request({
    url: `/accountant/ts/invoice-management/cost-analysis/${tenantId}`,
    method: 'get',
    params: { startDate, endDate }
  })
}

// ==================== 批量操作功能 ====================

/**
 * 批量更新发票状态
 */
export function batchUpdateStatus(tenantId, invoiceIds, status) {
  return request({
    url: '/accountant/ts/invoice-management/batch-update-status',
    method: 'post',
    params: { tenantId, status },
    data: invoiceIds
  })
}

/**
 * 批量删除发票
 */
export function batchDeleteInvoices(tenantId, invoiceIds) {
  return request({
    url: '/accountant/ts/invoice-management/batch-delete',
    method: 'delete',
    params: { tenantId },
    data: invoiceIds
  })
}

/**
 * 批量导入发票
 */
export function batchImportInvoices(tenantId, invoiceData) {
  return request({
    url: '/accountant/ts/invoice-management/batch-import',
    method: 'post',
    params: { tenantId },
    data: invoiceData
  })
}

/**
 * 批量导出发票
 */
export function batchExportInvoices(tenantId, invoiceIds) {
  return request({
    url: '/accountant/ts/invoice-management/batch-export',
    method: 'post',
    params: { tenantId },
    data: invoiceIds
  })
}

// ==================== 查询功能 ====================

/**
 * 根据发票代码和号码查询发票
 */
export function getInvoiceByCodeAndNumber(tenantId, invoiceCode, invoiceNumber) {
  return request({
    url: `/accountant/ts/invoice-management/by-code-number/${tenantId}`,
    method: 'get',
    params: { invoiceCode, invoiceNumber }
  })
}

/**
 * 根据销售方税号查询发票列表
 */
export function getInvoicesBySellerTaxNumber(tenantId, sellerTaxNumber) {
  return request({
    url: `/accountant/ts/invoice-management/by-seller-tax-number/${tenantId}`,
    method: 'get',
    params: { sellerTaxNumber }
  })
}

/**
 * 根据购买方税号查询发票列表
 */
export function getInvoicesByBuyerTaxNumber(tenantId, buyerTaxNumber) {
  return request({
    url: `/accountant/ts/invoice-management/by-buyer-tax-number/${tenantId}`,
    method: 'get',
    params: { buyerTaxNumber }
  })
}

// ==================== 系统维护功能 ====================

/**
 * 获取异常发票列表
 */
export function getAbnormalInvoices(tenantId, abnormalTypes) {
  return request({
    url: `/accountant/ts/invoice-management/abnormal/${tenantId}`,
    method: 'get',
    params: { abnormalTypes }
  })
}

/**
 * 获取需要重试的发票列表
 */
export function getRetryInvoices(tenantId) {
  return request({
    url: `/accountant/ts/invoice-management/retry/${tenantId}`,
    method: 'get'
  })
}

/**
 * 重试处理发票
 */
export function retryProcessInvoice(tenantId, invoiceId) {
  return request({
    url: `/accountant/ts/invoice-management/retry-process/${tenantId}/${invoiceId}`,
    method: 'post'
  })
}

/**
 * 清理过期发票
 */
export function cleanExpiredInvoices(tenantId, expiredDate) {
  return request({
    url: `/accountant/ts/invoice-management/clean-expired/${tenantId}`,
    method: 'delete',
    params: { expiredDate }
  })
}

/**
 * 系统健康检查
 */
export function healthCheck(tenantId) {
  return request({
    url: `/accountant/ts/invoice-management/health-check/${tenantId}`,
    method: 'post'
  })
}

/**
 * 获取系统性能指标
 */
export function getPerformanceMetrics(tenantId) {
  return request({
    url: `/accountant/ts/invoice-management/performance-metrics/${tenantId}`,
    method: 'get'
  })
}

// ==================== 快捷操作 ====================

/**
 * 快速识别并验真发票
 */
export async function quickProcessInvoice(tenantId, invoiceId) {
  try {
    // 先识别
    const recognizeResult = await recognizeInvoice(tenantId, invoiceId)
    if (!recognizeResult.success) {
      return recognizeResult
    }

    // 再验真
    const verifyResult = await verifyInvoice(tenantId, invoiceId)
    return verifyResult
  } catch (error) {
    return { success: false, message: error.message }
  }
}

/**
 * 批量快速处理发票
 */
export async function batchQuickProcessInvoices(tenantId, invoiceIds) {
  try {
    // 批量识别
    const recognizeResult = await batchRecognizeInvoices(tenantId, invoiceIds)
    if (!recognizeResult.success) {
      return recognizeResult
    }

    // 批量验真
    const verifyResult = await batchVerifyInvoices(tenantId, invoiceIds)
    return verifyResult
  } catch (error) {
    return { success: false, message: error.message }
  }
}

/**
 * 获取发票处理进度
 */
export function getInvoiceProcessingProgress(tenantId) {
  return Promise.all([
    getPendingOcrInvoices(tenantId, 1),
    getPendingVerificationInvoices(tenantId, 1),
    getPendingArchiveInvoices(tenantId, 1)
  ]).then(([ocrResult, verifyResult, archiveResult]) => {
    return {
      success: true,
      data: {
        pendingOcr: ocrResult.data?.length || 0,
        pendingVerification: verifyResult.data?.length || 0,
        pendingArchive: archiveResult.data?.length || 0
      }
    }
  })
}

// ==================== 工具函数 ====================

/**
 * 格式化发票类型
 */
export function formatInvoiceType(type) {
  const typeMap = {
    'SPECIAL_VAT': '增值税专用发票',
    'ORDINARY_VAT': '增值税普通发票',
    'ELECTRONIC': '电子发票',
    'RECEIPT': '收据'
  }
  return typeMap[type] || type
}

/**
 * 格式化发票状态
 */
export function formatInvoiceStatus(status) {
  const statusMap = {
    'DRAFT': { text: '草稿', color: 'info' },
    'RECOGNIZED': { text: '已识别', color: 'primary' },
    'VERIFIED': { text: '已验真', color: 'success' },
    'ARCHIVED': { text: '已归档', color: 'success' },
    'INVALID': { text: '作废', color: 'danger' }
  }
  return statusMap[status] || { text: status, color: 'info' }
}

/**
 * 格式化OCR状态
 */
export function formatOcrStatus(status) {
  const statusMap = {
    'PENDING': { text: '待识别', color: 'warning' },
    'PROCESSING': { text: '识别中', color: 'primary' },
    'SUCCESS': { text: '识别成功', color: 'success' },
    'FAILED': { text: '识别失败', color: 'danger' }
  }
  return statusMap[status] || { text: status, color: 'info' }
}

/**
 * 格式化验真状态
 */
export function formatVerificationStatus(status) {
  const statusMap = {
    'PENDING': { text: '待验真', color: 'warning' },
    'PROCESSING': { text: '验真中', color: 'primary' },
    'SUCCESS': { text: '验真成功', color: 'success' },
    'FAILED': { text: '验真失败', color: 'danger' }
  }
  return statusMap[status] || { text: status, color: 'info' }
}

/**
 * 格式化风险等级
 */
export function formatRiskLevel(level) {
  const levelMap = {
    'LOW': { text: '低风险', color: 'success' },
    'MEDIUM': { text: '中风险', color: 'warning' },
    'HIGH': { text: '高风险', color: 'danger' },
    'CRITICAL': { text: '严重风险', color: 'danger' }
  }
  return levelMap[level] || { text: level, color: 'info' }
}

/**
 * 格式化业务分类
 */
export function formatBusinessCategory(category) {
  const categoryMap = {
    'PURCHASE': '采购',
    'SALES': '销售',
    'EXPENSE': '费用',
    'ASSET': '资产'
  }
  return categoryMap[category] || category
}

/**
 * 格式化处理状态
 */
export function formatProcessingStatus(status) {
  const statusMap = {
    'PENDING': { text: '待处理', color: 'warning' },
    'PROCESSING': { text: '处理中', color: 'primary' },
    'COMPLETED': { text: '已完成', color: 'success' },
    'FAILED': { text: '处理失败', color: 'danger' }
  }
  return statusMap[status] || { text: status, color: 'info' }
}

/**
 * 格式化金额
 */
export function formatAmount(amount) {
  if (!amount) return '0.00'
  return parseFloat(amount).toLocaleString('zh-CN', {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2
  })
}

/**
 * 格式化文件大小
 */
export function formatFileSize(size) {
  if (!size) return '0 B'
  const units = ['B', 'KB', 'MB', 'GB']
  let index = 0
  while (size >= 1024 && index < units.length - 1) {
    size /= 1024
    index++
  }
  return `${size.toFixed(2)} ${units[index]}`
}

/**
 * 格式化置信度
 */
export function formatConfidence(confidence) {
  if (!confidence) return '0%'
  return `${(parseFloat(confidence) * 100).toFixed(1)}%`
}

/**
 * 验证发票代码格式
 */
export function validateInvoiceCode(code) {
  if (!code) return false
  // 发票代码通常为12位数字
  return /^\d{12}$/.test(code)
}

/**
 * 验证发票号码格式
 */
export function validateInvoiceNumber(number) {
  if (!number) return false
  // 发票号码通常为8位数字
  return /^\d{8}$/.test(number)
}

/**
 * 验证纳税人识别号格式
 */
export function validateTaxNumber(taxNumber) {
  if (!taxNumber) return false
  // 纳税人识别号通常为15位、17位或18位
  return /^[0-9A-Z]{15}$|^[0-9A-Z]{17}$|^[0-9A-Z]{18}$/.test(taxNumber)
}

/**
 * 获取发票状态颜色
 */
export function getInvoiceStatusColor(status) {
  return formatInvoiceStatus(status).color
}

/**
 * 获取风险等级颜色
 */
export function getRiskLevelColor(level) {
  return formatRiskLevel(level).color
}

/**
 * 计算处理进度百分比
 */
export function calculateProcessingProgress(invoice) {
  let progress = 0

  if (invoice.ocrStatus === 'SUCCESS') progress += 33
  if (invoice.verificationStatus === 'SUCCESS') progress += 33
  if (invoice.archiveStatus === 'ARCHIVED') progress += 34

  return Math.min(progress, 100)
}

/**
 * 判断是否为高风险发票
 */
export function isHighRiskInvoice(invoice) {
  return ['HIGH', 'CRITICAL'].includes(invoice.riskLevel)
}

/**
 * 判断是否需要重试
 */
export function needsRetry(invoice) {
  return invoice.retryCount < invoice.maxRetryCount &&
         ['FAILED'].includes(invoice.processingStatus)
}

/**
 * 生成发票摘要信息
 */
export function generateInvoiceSummary(invoice) {
  return {
    code: invoice.invoiceCode,
    number: invoice.invoiceNumber,
    seller: invoice.sellerName,
    buyer: invoice.buyerName,
    amount: formatAmount(invoice.totalAmount),
    date: invoice.invoiceDate,
    status: formatInvoiceStatus(invoice.invoiceStatus).text,
    riskLevel: formatRiskLevel(invoice.riskLevel).text
  }
}

// ==================== 常量定义 ====================

export const INVOICE_TYPES = [
  { value: 'SPECIAL_VAT', label: '增值税专用发票' },
  { value: 'ORDINARY_VAT', label: '增值税普通发票' },
  { value: 'ELECTRONIC', label: '电子发票' },
  { value: 'RECEIPT', label: '收据' }
]

export const INVOICE_STATUSES = [
  { value: 'DRAFT', label: '草稿' },
  { value: 'RECOGNIZED', label: '已识别' },
  { value: 'VERIFIED', label: '已验真' },
  { value: 'ARCHIVED', label: '已归档' },
  { value: 'INVALID', label: '作废' }
]

export const OCR_STATUSES = [
  { value: 'PENDING', label: '待识别' },
  { value: 'PROCESSING', label: '识别中' },
  { value: 'SUCCESS', label: '识别成功' },
  { value: 'FAILED', label: '识别失败' }
]

export const VERIFICATION_STATUSES = [
  { value: 'PENDING', label: '待验真' },
  { value: 'PROCESSING', label: '验真中' },
  { value: 'SUCCESS', label: '验真成功' },
  { value: 'FAILED', label: '验真失败' }
]

export const RISK_LEVELS = [
  { value: 'LOW', label: '低风险' },
  { value: 'MEDIUM', label: '中风险' },
  { value: 'HIGH', label: '高风险' },
  { value: 'CRITICAL', label: '严重风险' }
]

export const BUSINESS_CATEGORIES = [
  { value: 'PURCHASE', label: '采购' },
  { value: 'SALES', label: '销售' },
  { value: 'EXPENSE', label: '费用' },
  { value: 'ASSET', label: '资产' }
]

export const PROCESSING_STATUSES = [
  { value: 'PENDING', label: '待处理' },
  { value: 'PROCESSING', label: '处理中' },
  { value: 'COMPLETED', label: '已完成' },
  { value: 'FAILED', label: '处理失败' }
]

export const ARCHIVE_STATUSES = [
  { value: 'PENDING', label: '待归档' },
  { value: 'ARCHIVED', label: '已归档' },
  { value: 'FAILED', label: '归档失败' }
]

export const PRIORITIES = [
  { value: 'LOW', label: '低' },
  { value: 'NORMAL', label: '普通' },
  { value: 'HIGH', label: '高' },
  { value: 'URGENT', label: '紧急' }
]

// 导出工具对象
export const utils = {
  formatInvoiceType,
  formatInvoiceStatus,
  formatOcrStatus,
  formatVerificationStatus,
  formatRiskLevel,
  formatBusinessCategory,
  formatProcessingStatus,
  formatAmount,
  formatFileSize,
  formatConfidence,
  validateInvoiceCode,
  validateInvoiceNumber,
  validateTaxNumber,
  getInvoiceStatusColor,
  getRiskLevelColor,
  calculateProcessingProgress,
  isHighRiskInvoice,
  needsRetry,
  generateInvoiceSummary
}
