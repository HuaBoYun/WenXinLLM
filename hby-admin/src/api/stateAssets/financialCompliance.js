import request from '@/utils/request'
import { transData } from '@/utils/requestData'

const BASE = '/monitor/v1/supervision/financial'

// 财务合规监管API接口

// ==================== 基础CRUD操作 ====================

/**
 * 获取财务合规检查列表
 * @param {Object} params - 查询参数
 * @returns {Promise}
 */
export function getFinancialComplianceList(params) {
  return request({ url: `${BASE}/compliance/list`, method: 'post', data: params })
}

/**
 * 获取财务合规检查详情
 * @param {String} id - 检查ID
 * @returns {Promise}
 */
export function getFinancialComplianceDetail(id) {
  return request({ url: `${BASE}/compliance/${id}`, method: 'get' })
}

/**
 * 新增财务合规检查
 * @param {Object} data - 检查数据
 * @returns {Promise}
 */
export function createFinancialCompliance(data) {
  return request({ url: `${BASE}/compliance/save`, method: 'post', data: transData(data) })
}

/**
 * 更新财务合规检查
 * @param {String} id - 检查ID
 * @param {Object} data - 更新数据
 * @returns {Promise}
 */
export function updateFinancialCompliance(id, data) {
  return request({ url: `${BASE}/compliance/save`, method: 'post', data: transData({ ...data, complianceId: id }) })
}

/**
 * 删除财务合规检查
 * @param {String} id - 检查ID
 * @returns {Promise}
 */
export function deleteFinancialCompliance(id) {
  return request({ url: `${BASE}/compliance/${id}`, method: 'delete' })
}

// ==================== 统计分析 ====================

/**
 * 获取财务合规统计数据
 * @param {Object} params - 查询参数
 * @returns {Promise}
 */
export function getComplianceStatistics(params) {
  return request({ url: `${BASE}/compliance/statistics`, method: 'get', params })
}

/**
 * 获取合规趋势分析数据
 * @param {Object} params - 查询参数
 * @returns {Promise}
 */
export function getComplianceTrend(params) {
  return request({ url: `${BASE}/compliance/trend`, method: 'get', params })
}

/**
 * 获取合规分布分析数据
 * @param {Object} params - 查询参数
 * @returns {Promise}
 */
export function getComplianceDistribution(params) {
  return request({ url: `${BASE}/compliance/distribution`, method: 'get', params })
}

/**
 * 获取违规类型统计
 * @param {Object} params - 查询参数
 * @returns {Promise}
 */
export function getViolationTypeStatistics(params) {
  return request({ url: `${BASE}/compliance/violation-types`, method: 'get', params })
}

// ==================== 合规检查 ====================

/**
 * 执行合规检查
 * @param {Object} data - 检查参数
 * @returns {Promise}
 */
export function executeComplianceCheck(data) {
  return request({ url: `${BASE}/compliance/check`, method: 'post', data: transData(data) })
}

/**
 * 获取检查结果
 * @param {String} checkId - 检查ID
 * @returns {Promise}
 */
export function getCheckResult(checkId) {
  return request({ url: `${BASE}/compliance/check/result/${checkId}`, method: 'get' })
}

/**
 * 获取检查项目列表
 * @param {Object} params - 查询参数
 * @returns {Promise}
 */
export function getCheckItems(params) {
  return request({ url: `${BASE}/compliance/check-items`, method: 'get', params })
}

/**
 * 更新检查项目结果
 * @param {String} itemId - 项目ID
 * @param {Object} data - 结果数据
 * @returns {Promise}
 */
export function updateCheckItemResult(itemId, data) {
  return request({ url: `${BASE}/compliance/check-item/${itemId}`, method: 'put', data: transData(data) })
}

// ==================== 制度执行监督 ====================

/**
 * 获取制度执行情况
 * @param {Object} params - 查询参数
 * @returns {Promise}
 */
export function getSystemExecution(params) {
  return request({ url: `${BASE}/compliance/system-execution`, method: 'get', params })
}

/**
 * 检查制度执行偏差
 * @param {Object} data - 检查参数
 * @returns {Promise}
 */
export function checkExecutionDeviation(data) {
  return request({ url: `${BASE}/compliance/execution-deviation`, method: 'post', data: transData(data) })
}

/**
 * 评估制度执行效果
 * @param {Object} data - 评估参数
 * @returns {Promise}
 */
export function assessExecutionEffect(data) {
  return request({ url: `${BASE}/compliance/execution-assessment`, method: 'post', data: transData(data) })
}

/**
 * 生成制度优化建议
 * @param {Object} data - 分析数据
 * @returns {Promise}
 */
export function generateSystemOptimization(data) {
  return request({ url: `${BASE}/compliance/system-optimization`, method: 'post', data: transData(data) })
}

// ==================== 流程合规检查 ====================

/**
 * 检查流程节点合规性
 * @param {Object} data - 检查参数
 * @returns {Promise}
 */
export function checkProcessCompliance(data) {
  return request({ url: `${BASE}/compliance/process-check`, method: 'post', data: transData(data) })
}

/**
 * 获取流程时效分析
 * @param {Object} params - 查询参数
 * @returns {Promise}
 */
export function getProcessTimeAnalysis(params) {
  return request({ url: `${BASE}/compliance/process-time`, method: 'get', params })
}

/**
 * 检查流程权限配置
 * @param {Object} data - 检查参数
 * @returns {Promise}
 */
export function checkProcessPermissions(data) {
  return request({ url: `${BASE}/compliance/process-permissions`, method: 'post', data: transData(data) })
}

/**
 * 验证流程文档完整性
 * @param {Object} data - 验证参数
 * @returns {Promise}
 */
export function validateProcessDocuments(data) {
  return request({ url: `${BASE}/compliance/process-documents`, method: 'post', data: transData(data) })
}

// ==================== 违规行为识别 ====================

/**
 * 识别违规行为
 * @param {Object} data - 识别参数
 * @returns {Promise}
 */
export function identifyViolations(data) {
  return request({ url: `${BASE}/compliance/identify-violations`, method: 'post', data: transData(data) })
}

/**
 * 获取违规行为列表
 * @param {Object} params - 查询参数
 * @returns {Promise}
 */
export function getViolationsList(params) {
  return request({ url: `${BASE}/compliance/violations`, method: 'get', params })
}

/**
 * 认定违规行为
 * @param {String} violationId - 违规ID
 * @param {Object} data - 认定数据
 * @returns {Promise}
 */
export function determineViolation(violationId, data) {
  return request({ url: `${BASE}/compliance/violation/determine/${violationId}`, method: 'post', data: transData(data) })
}

/**
 * 处理违规行为
 * @param {String} violationId - 违规ID
 * @param {Object} data - 处理数据
 * @returns {Promise}
 */
export function handleViolation(violationId, data) {
  return request({ url: `${BASE}/compliance/violation/handle/${violationId}`, method: 'post', data: transData(data) })
}

// ==================== 整改措施跟踪 ====================

/**
 * 创建整改计划
 * @param {Object} data - 整改计划数据
 * @returns {Promise}
 */
export function createRectificationPlan(data) {
  return request({ url: `${BASE}/compliance/rectification/plan`, method: 'post', data: transData(data) })
}

/**
 * 跟踪整改进度
 * @param {String} planId - 计划ID
 * @returns {Promise}
 */
export function trackRectificationProgress(planId) {
  return request({ url: `${BASE}/compliance/rectification/progress/${planId}`, method: 'get' })
}

/**
 * 验收整改成果
 * @param {String} planId - 计划ID
 * @param {Object} data - 验收数据
 * @returns {Promise}
 */
export function acceptRectificationResult(planId, data) {
  return request({ url: `${BASE}/compliance/rectification/accept/${planId}`, method: 'post', data: transData(data) })
}

/**
 * 评估整改效果
 * @param {String} planId - 计划ID
 * @param {Object} data - 评估数据
 * @returns {Promise}
 */
export function assessRectificationEffect(planId, data) {
  return request({ url: `${BASE}/compliance/rectification/assess/${planId}`, method: 'post', data: transData(data) })
}

// ==================== 报告生成 ====================

/**
 * 生成合规检查报告
 * @param {Object} data - 报告参数
 * @returns {Promise}
 */
export function generateComplianceReport(data) {
  return request({ url: `${BASE}/compliance/report/generate`, method: 'post', data: transData(data) })
}

/**
 * 获取合规报告列表
 * @param {Object} params - 查询参数
 * @returns {Promise}
 */
export function getComplianceReports(params) {
  return request({ url: `${BASE}/compliance/reports`, method: 'get', params })
}

/**
 * 下载合规报告
 * @param {String} reportId - 报告ID
 * @returns {Promise}
 */
export function downloadComplianceReport(reportId) {
  return request({ url: `${BASE}/compliance/report/download/${reportId}`, method: 'get', responseType: 'blob' })
}

// ==================== 预警管理 ====================

/**
 * 设置合规预警
 * @param {Object} data - 预警配置
 * @returns {Promise}
 */
export function setComplianceAlert(data) {
  return request({ url: `${BASE}/compliance/alert/set`, method: 'post', data: transData(data) })
}

/**
 * 获取合规预警列表
 * @param {Object} params - 查询参数
 * @returns {Promise}
 */
export function getComplianceAlerts(params) {
  return request({ url: `${BASE}/compliance/alerts`, method: 'get', params })
}

/**
 * 处理合规预警
 * @param {String} alertId - 预警ID
 * @param {Object} data - 处理数据
 * @returns {Promise}
 */
export function handleComplianceAlert(alertId, data) {
  return request({ url: `${BASE}/compliance/alert/handle/${alertId}`, method: 'post', data: transData(data) })
}

// ==================== 批量操作 ====================

/**
 * 批量合规检查
 * @param {Object} data - 批量检查数据
 * @returns {Promise}
 */
export function batchComplianceCheck(data) {
  return request({ url: `${BASE}/compliance/batch-check`, method: 'post', data: transData(data) })
}

/**
 * 批量分析
 * @param {Object} data - 批量分析数据
 * @returns {Promise}
 */
export function batchComplianceAnalyze(data) {
  return request({ url: `${BASE}/compliance/batch-analyze`, method: 'post', data: transData(data) })
}

/**
 * 批量处理违规
 * @param {Object} data - 批量处理数据
 * @returns {Promise}
 */
export function batchHandleViolations(data) {
  return request({ url: `${BASE}/compliance/batch-handle`, method: 'post', data: transData(data) })
}

/**
 * 批量删除
 * @param {Array} ids - ID数组
 * @returns {Promise}
 */
export function batchDelete(ids) {
  return request({ url: `${BASE}/compliance/batch-delete`, method: 'post', data: transData({ ids }) })
}

// ==================== 数据导入导出 ====================

/**
 * 导出合规数据
 * @param {Object} params - 导出参数
 * @returns {Promise}
 */
export function exportComplianceData(params) {
  return request({ url: `${BASE}/compliance/export`, method: 'get', params, responseType: 'blob' })
}

/**
 * 导入合规数据
 * @param {FormData} formData - 文件数据
 * @returns {Promise}
 */
export function importComplianceData(formData) {
  return request({ url: `${BASE}/compliance/import`, method: 'post', data: formData, headers: { 'Content-Type': 'multipart/form-data' } })
}

/**
 * 获取导入模板
 * @returns {Promise}
 */
export function getImportTemplate() {
  return request({ url: `${BASE}/compliance/import-template`, method: 'get', responseType: 'blob' })
}
