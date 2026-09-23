/**
 * NCV65全面预算系统 - 预算分析API
 * 
 * @description 预算分析功能API接口，包含多维分析、差异分析、语义查询、智能推荐等功能
 * @version 1.0.0
 * @author AI Assistant
 * @date 2025-01-08
 * @module budgetAnalysis
 */

import request from '@/utils/request'

// JSON请求头常量 - 解决默认form-urlencoded导致后端@RequestBody解析失败的问题
const JSON_HEADERS = { 'Content-Type': 'application/json;charset=UTF-8' }

// ==================== 预算执行分析API ====================

/**
 * 创建执行记录
 * @param {Object} data 执行数据
 * @returns {Promise} 请求结果
 */
export function createExecution(data) {
  return request({
    url: '/glkj/accountant/execution/create',
    method: 'post',
    headers: JSON_HEADERS,
    data
  })
}

/**
 * 查询执行详情
 * @param {String} executionId 执行ID
 * @returns {Promise} 请求结果
 */
export function getExecutionDetail(executionId) {
  return request({
    url: `/glkj/accountant/execution/detail/${executionId}`,
    method: 'get'
  })
}

/**
 * 分页查询执行列表
 * @param {Object} params 查询参数
 * @returns {Promise} 请求结果
 */
export function getExecutionPage(params) {
  return request({
    url: '/glkj/accountant/execution/page',
    method: 'post',
    headers: JSON_HEADERS,
    data: params
  })
}

/**
 * 获取执行进度分析
 * @param {Object} params 分析参数
 * @returns {Promise} 请求结果
 */
export function getProgressAnalysis(params) {
  return request({
    url: '/glkj/accountant/execution/progress-analysis',
    method: 'post',
    headers: JSON_HEADERS,
    data: params
  })
}

/**
 * 获取执行偏差分析
 * @param {Object} params 分析参数
 * @returns {Promise} 请求结果
 */
export function getVarianceAnalysis(params) {
  return request({
    url: '/glkj/accountant/execution/variance-analysis',
    method: 'post',
    headers: JSON_HEADERS,
    data: params
  })
}

/**
 * 获取执行趋势分析
 * @param {Object} params 分析参数
 * @returns {Promise} 请求结果
 */
export function getTrendAnalysis(params) {
  return request({
    url: '/glkj/accountant/execution/trend-analysis',
    method: 'post',
    headers: JSON_HEADERS,
    data: params
  })
}

/**
 * 获取执行统计信息
 * @returns {Promise} 请求结果
 */
export function getExecutionStatistics() {
  return request({
    url: '/glkj/accountant/execution/statistics',
    method: 'get'
  })
}

/**
 * 生成执行报表
 * @param {Object} params 报表参数
 * @returns {Promise} 请求结果
 */
export function generateExecutionReport(params) {
  return request({
    url: '/glkj/accountant/execution/generate-report',
    method: 'post',
    headers: JSON_HEADERS,
    data: params
  })
}

// ==================== 多维分析API ====================

/**
 * 创建多维分析
 * @param {Object} data 分析配置数据
 * @returns {Promise} 请求结果
 */
export function createMultiDimensionAnalysis(data) {
  return request({
    url: '/glkj/accountant/budget/analysis/multi-dimension',
    method: 'post',
    headers: JSON_HEADERS,
    data
  })
}

/**
 * 执行多维分析
 * @param {String} analysisId 分析ID
 * @param {Object} params 分析参数
 * @returns {Promise} 请求结果
 */
export function executeMultiDimensionAnalysis(analysisId, params) {
  return request({
    url: `/glkj/accountant/budget/analysis/multi-dimension/${analysisId}/execute`,
    method: 'post',
    headers: JSON_HEADERS,
    data: params
  })
}

/**
 * 获取多维分析结果
 * @param {String} analysisId 分析ID
 * @returns {Promise} 请求结果
 */
export function getMultiDimensionAnalysisResult(analysisId) {
  return request({
    url: `/glkj/accountant/budget/analysis/multi-dimension/${analysisId}/result`,
    method: 'get'
  })
}

/**
 * 数据钻取分析
 * @param {Object} data 钻取参数
 * @returns {Promise} 请求结果
 */
export function drillDownAnalysis(data) {
  return request({
    url: '/glkj/accountant/budget/analysis/drill-down',
    method: 'post',
    headers: JSON_HEADERS,
    data
  })
}

/**
 * 数据切片分析
 * @param {Object} data 切片参数
 * @returns {Promise} 请求结果
 */
export function sliceAnalysis(data) {
  return request({
    url: '/glkj/accountant/budget/analysis/slice',
    method: 'post',
    headers: JSON_HEADERS,
    data
  })
}

/**
 * 数据透视分析
 * @param {Object} data 透视参数
 * @returns {Promise} 请求结果
 */
export function pivotAnalysis(data) {
  return request({
    url: '/glkj/accountant/budget/analysis/pivot',
    method: 'post',
    headers: JSON_HEADERS,
    data
  })
}

// ==================== 差异分析API ====================

/**
 * 预实对比分析
 * @param {Object} data 对比参数
 * @returns {Promise} 请求结果
 */
export function budgetActualComparison(data) {
  return request({
    url: '/glkj/accountant/budget/analysis/budget-actual-comparison',
    method: 'post',
    headers: JSON_HEADERS,
    data
  })
}

/**
 * 同期对比分析
 * @param {Object} data 对比参数
 * @returns {Promise} 请求结果
 */
export function periodComparison(data) {
  return request({
    url: '/glkj/accountant/budget/analysis/period-comparison',
    method: 'post',
    headers: JSON_HEADERS,
    data
  })
}

/**
 * 趋势分析
 * @param {Object} data 趋势分析参数
 * @returns {Promise} 请求结果
 */
export function trendAnalysis(data) {
  return request({
    url: '/glkj/accountant/budget/analysis/trend',
    method: 'post',
    headers: JSON_HEADERS,
    data
  })
}

/**
 * 差异原因分析
 * @param {Object} data 差异分析参数
 * @returns {Promise} 请求结果
 */
export function varianceAnalysis(data) {
  return request({
    url: '/glkj/accountant/budget/analysis/variance',
    method: 'post',
    headers: JSON_HEADERS,
    data
  })
}

/**
 * 获取差异分析报告
 * @param {String} analysisId 分析ID
 * @returns {Promise} 请求结果
 */
export function getVarianceAnalysisReport(analysisId) {
  return request({
    url: `/glkj/accountant/budget/analysis/variance/${analysisId}/report`,
    method: 'get'
  })
}

// ==================== 语义查询API ====================

/**
 * 自然语言查询
 * @param {Object} data 查询数据
 * @returns {Promise} 请求结果
 */
export function naturalLanguageQuery(data) {
  return request({
    url: '/glkj/accountant/budget/analysis/nlp-query',
    method: 'post',
    headers: JSON_HEADERS,
    data
  })
}

/**
 * 解析查询语句
 * @param {String} query 查询语句
 * @returns {Promise} 请求结果
 */
export function parseQuery(query) {
  return request({
    url: '/glkj/accountant/budget/analysis/parse-query',
    method: 'post',
    headers: JSON_HEADERS,
    data: { query }
  })
}

/**
 * 获取查询建议
 * @param {String} partialQuery 部分查询语句
 * @returns {Promise} 请求结果
 */
export function getQuerySuggestions(partialQuery) {
  return request({
    url: '/glkj/accountant/budget/analysis/query-suggestions',
    method: 'get',
    params: { query: partialQuery }
  })
}

/**
 * 保存查询历史
 * @param {Object} data 查询历史数据
 * @returns {Promise} 请求结果
 */
export function saveQueryHistory(data) {
  return request({
    url: '/glkj/accountant/budget/analysis/query-history',
    method: 'post',
    headers: JSON_HEADERS,
    data
  })
}

/**
 * 获取查询历史
 * @param {Number} current 当前�?
 * @param {Number} size 页大�?
 * @returns {Promise} 请求结果
 */
export function getQueryHistory(current, size) {
  return request({
    url: '/glkj/accountant/budget/analysis/query-history/page',
    method: 'post',
    headers: JSON_HEADERS,
    params: { current, size }
  })
}

// ==================== 智能推荐API ====================

/**
 * 获取分析推荐
 * @param {Object} data 推荐参数
 * @returns {Promise} 请求结果
 */
export function getAnalysisRecommendations(data) {
  return request({
    url: '/glkj/accountant/budget/analysis/recommendations',
    method: 'post',
    headers: JSON_HEADERS,
    data
  })
}

/**
 * 获取指标推荐
 * @param {Object} data 推荐参数
 * @returns {Promise} 请求结果
 */
export function getIndicatorRecommendations(data) {
  return request({
    url: '/glkj/accountant/budget/analysis/indicator-recommendations',
    method: 'post',
    headers: JSON_HEADERS,
    data
  })
}

/**
 * 获取维度推荐
 * @param {Object} data 推荐参数
 * @returns {Promise} 请求结果
 */
export function getDimensionRecommendations(data) {
  return request({
    url: '/glkj/accountant/budget/analysis/dimension-recommendations',
    method: 'post',
    headers: JSON_HEADERS,
    data
  })
}

/**
 * 获取异常数据推荐
 * @param {Object} data 推荐参数
 * @returns {Promise} 请求结果
 */
export function getAnomalyRecommendations(data) {
  return request({
    url: '/glkj/accountant/budget/analysis/anomaly-recommendations',
    method: 'post',
    headers: JSON_HEADERS,
    data
  })
}

/**
 * 反馈推荐结果
 * @param {String} recommendationId 推荐ID
 * @param {Object} feedback 反馈数据
 * @returns {Promise} 请求结果
 */
export function feedbackRecommendation(recommendationId, feedback) {
  return request({
    url: `/glkj/accountant/budget/analysis/recommendations/${recommendationId}/feedback`,
    method: 'post',
    headers: JSON_HEADERS,
    data: feedback
  })
}

// ==================== 分析报告API ====================

/**
 * 创建分析报告
 * @param {Object} data 报告数据
 * @returns {Promise} 请求结果
 */
export function createAnalysisReport(data) {
  return request({
    url: '/glkj/accountant/budget/analysis/report',
    method: 'post',
    headers: JSON_HEADERS,
    data
  })
}

/**
 * 查询分析报告详情
 * @param {String} reportId 报告ID
 * @returns {Promise} 请求结果
 */
export function getAnalysisReport(reportId) {
  return request({
    url: `/glkj/accountant/budget/analysis/report/${reportId}`,
    method: 'get'
  })
}

/**
 * 更新分析报告
 * @param {String} reportId 报告ID
 * @param {Object} data 更新数据
 * @returns {Promise} 请求结果
 */
export function updateAnalysisReport(reportId, data) {
  return request({
    url: `/glkj/accountant/budget/analysis/report/${reportId}`,
    method: 'put',
    headers: JSON_HEADERS,
    data
  })
}

/**
 * 删除分析报告
 * @param {String} reportId 报告ID
 * @returns {Promise} 请求结果
 */
export function deleteAnalysisReport(reportId) {
  return request({
    url: `/glkj/accountant/budget/analysis/report/${reportId}`,
    method: 'delete'
  })
}

/**
 * 分页查询分析报告列表
 * @param {Number} current 当前�?
 * @param {Number} size 页大�?
 * @param {Object} params 查询参数
 * @returns {Promise} 请求结果
 */
export function getAnalysisReportPage(current, size, params) {
  return request({
    url: '/glkj/accountant/budget/analysis/report/page',
    method: 'post',
    headers: JSON_HEADERS,
    params: { current, size },
    data: params
  })
}

/**
 * 导出分析报告
 * @param {String} reportId 报告ID
 * @param {String} format 导出格式
 * @returns {Promise} 请求结果
 */
export function exportAnalysisReport(reportId, format = 'excel') {
  return request({
    url: `/glkj/accountant/budget/analysis/report/${reportId}/export`,
    method: 'get',
    params: { format },
    responseType: 'blob'
  })
}

/**
 * 分享分析报告
 * @param {String} reportId 报告ID
 * @param {Object} shareConfig 分享配置
 * @returns {Promise} 请求结果
 */
export function shareAnalysisReport(reportId, shareConfig) {
  return request({
    url: `/glkj/accountant/budget/analysis/report/${reportId}/share`,
    method: 'post',
    headers: JSON_HEADERS,
    data: shareConfig
  })
}

// ==================== 对比分析API补充（为简化导出提供函数定义） ====================

/**
 * 创建预算对比
 * @param {Object} data 对比数据
 * @returns {Promise} 请求结果
 */
export function createBudgetComparison(data) {
  return request({
    url: '/glkj/accountant/budget/analysis/comparison/execute',
    method: 'post',
    headers: JSON_HEADERS,
    data
  })
}

/**
 * 更新预算对比
 * @param {String} comparisonId 对比ID
 * @param {Object} data 更新数据
 * @returns {Promise} 请求结果
 */
export function updateBudgetComparison(comparisonId, data) {
  return request({
    url: `/glkj/accountant/budget/analysis/comparison/execute`,
    method: 'post',
    headers: JSON_HEADERS,
    data: { ...data, id: comparisonId }
  })
}

/**
 * 删除预算对比
 * @param {String} comparisonId 对比ID
 * @returns {Promise} 请求结果
 */
export function deleteBudgetComparison(comparisonId) {
  return request({
    url: `/glkj/accountant/budget/analysis/comparison/execute`,
    method: 'post',
    headers: JSON_HEADERS,
    data: { id: comparisonId, action: 'delete' }
  })
}

/**
 * 获取预算对比详情
 * @param {String} comparisonId 对比ID
 * @returns {Promise} 请求结果
 */
export function getBudgetComparison(comparisonId) {
  return request({
    url: `/glkj/accountant/budget/analysis/comparison/chart`,
    method: 'post',
    headers: JSON_HEADERS,
    data: { id: comparisonId }
  })
}

/**
 * 分页获取预算对比列表
 * @param {Object} params 查询参数
 * @returns {Promise} 请求结果
 */
export function getBudgetComparisonPage(params) {
  return request({
    url: '/glkj/accountant/budget/analysis/comparison/execute',
    method: 'post',
    headers: JSON_HEADERS,
    data: params
  })
}

/**
 * 执行预算对比
 * @param {String} comparisonId 对比ID
 * @returns {Promise} 请求结果
 */
export function executeBudgetComparison(comparisonId) {
  return request({
    url: `/glkj/accountant/budget/analysis/comparison/execute`,
    method: 'post',
    headers: JSON_HEADERS,
    data: { id: comparisonId }
  })
}

/**
 * 获取预算对比结果
 * @param {String} comparisonId 对比ID
 * @returns {Promise} 请求结果
 */
export function getBudgetComparisonResult(comparisonId) {
  return request({
    url: `/glkj/accountant/budget/analysis/comparison/chart`,
    method: 'post',
    headers: JSON_HEADERS,
    data: { id: comparisonId }
  })
}

// ==================== 差异分析API补充（为简化导出提供函数定义） ====================

/**
 * 创建预算差异分析
 * @param {Object} data 差异数据
 * @returns {Promise} 请求结果
 */
export function createBudgetVariance(data) {
  return request({
    url: '/glkj/accountant/variance/create',
    method: 'post',
    headers: JSON_HEADERS,
    data
  })
}

/**
 * 更新预算差异分析
 * @param {Object} data 更新数据
 * @returns {Promise} 请求结果
 */
export function updateBudgetVariance(data) {
  return request({
    url: '/glkj/accountant/variance/update',
    method: 'put',
    headers: JSON_HEADERS,
    data
  })
}

/**
 * 删除预算差异分析
 * @param {String} varianceId 差异ID
 * @returns {Promise} 请求结果
 */
export function deleteBudgetVariance(varianceId) {
  return request({
    url: `/glkj/accountant/variance/delete/${varianceId}`,
    method: 'delete'
  })
}

/**
 * 批量删除预算差异分析
 * @param {Array} ids 差异ID列表
 * @returns {Promise} 请求结果
 */
export function batchDeleteBudgetVariance(ids) {
  return request({
    url: '/glkj/accountant/variance/batch-delete',
    method: 'post',
    headers: JSON_HEADERS,
    data: { ids }
  })
}

/**
 * 获取预算差异分析详情
 * @param {String} varianceId 差异ID
 * @returns {Promise} 请求结果
 */
export function getBudgetVariance(varianceId) {
  return request({
    url: `/glkj/accountant/variance/detail/${varianceId}`,
    method: 'get'
  })
}

/**
 * 分页获取预算差异分析列表
 * @param {Object} params 查询参数
 * @returns {Promise} 请求结果
 */
export function getBudgetVariancePage(params) {
  return request({
    url: '/glkj/accountant/variance/page',
    method: 'post',
    headers: JSON_HEADERS,
    data: params
  })
}

/**
 * 执行预算差异分析
 * @param {String} varianceId 差异ID
 * @returns {Promise} 请求结果
 */
export function executeBudgetVariance(varianceId) {
  return request({
    url: `/glkj/accountant/variance/analyze`,
    method: 'post',
    headers: JSON_HEADERS,
    data: { id: varianceId }
  })
}

/**
 * 获取预算差异分析结果
 * @param {String} varianceId 差异ID
 * @returns {Promise} 请求结果
 */
export function getBudgetVarianceResult(varianceId) {
  return request({
    url: `/glkj/accountant/variance/report`,
    method: 'post',
    headers: JSON_HEADERS,
    data: { id: varianceId }
  })
}

// ==================== 趋势分析API补充（为简化导出提供函数定义） ====================

/**
 * 创建预算趋势分析
 * @param {Object} data 趋势数据
 * @returns {Promise} 请求结果
 */
export function createBudgetTrend(data) {
  return request({
    url: '/glkj/accountant/budget/analysis/trend/execute',
    method: 'post',
    headers: JSON_HEADERS,
    data
  })
}

/**
 * 更新预算趋势分析
 * @param {String} trendId 趋势ID
 * @param {Object} data 更新数据
 * @returns {Promise} 请求结果
 */
export function updateBudgetTrend(trendId, data) {
  return request({
    url: `/glkj/accountant/budget/analysis/trend/execute`,
    method: 'post',
    headers: JSON_HEADERS,
    data: { ...data, id: trendId }
  })
}

/**
 * 删除预算趋势分析
 * @param {String} trendId 趋势ID
 * @returns {Promise} 请求结果
 */
export function deleteBudgetTrend(trendId) {
  return request({
    url: `/glkj/accountant/budget/analysis/trend/execute`,
    method: 'post',
    headers: JSON_HEADERS,
    data: { id: trendId, action: 'delete' }
  })
}

/**
 * 获取预算趋势分析详情
 * @param {String} trendId 趋势ID
 * @returns {Promise} 请求结果
 */
export function getBudgetTrend(trendId) {
  return request({
    url: `/glkj/accountant/budget/analysis/trend/chart`,
    method: 'post',
    headers: JSON_HEADERS,
    data: { id: trendId }
  })
}

/**
 * 分页获取预算趋势分析列表
 * @param {Object} params 查询参数
 * @returns {Promise} 请求结果
 */
export function getBudgetTrendPage(params) {
  return request({
    url: '/glkj/accountant/budget/analysis/trend/execute',
    method: 'post',
    headers: JSON_HEADERS,
    data: params
  })
}

/**
 * 执行预算趋势分析
 * @param {String} trendId 趋势ID
 * @returns {Promise} 请求结果
 */
export function executeBudgetTrend(trendId) {
  return request({
    url: `/glkj/accountant/budget/analysis/trend/execute`,
    method: 'post',
    headers: JSON_HEADERS,
    data: { id: trendId }
  })
}

/**
 * 获取预算趋势分析结果
 * @param {String} trendId 趋势ID
 * @returns {Promise} 请求结果
 */
export function getBudgetTrendResult(trendId) {
  return request({
    url: `/glkj/accountant/budget/analysis/trend/chart`,
    method: 'post',
    headers: JSON_HEADERS,
    data: { id: trendId }
  })
}

// ==================== 简化函数名导出（兼容前端组件） ====================

// 对比分析API简化导�?
export const comparisonAnalysisApi = {
  create: createBudgetComparison,
  update: updateBudgetComparison,
  delete: deleteBudgetComparison,
  get: getBudgetComparison,
  getPage: getBudgetComparisonPage,
  execute: executeBudgetComparison,
  getResult: getBudgetComparisonResult
}

// 差异分析API简化导出
export const varianceAnalysisApi = {
  create: createBudgetVariance,
  update: updateBudgetVariance,
  delete: deleteBudgetVariance,
  batchDelete: batchDeleteBudgetVariance,
  get: getBudgetVariance,
  getPage: getBudgetVariancePage,
  execute: executeBudgetVariance,
  getResult: getBudgetVarianceResult
}

// 趋势分析API简化导�?
export const trendAnalysisApi = {
  create: createBudgetTrend,
  update: updateBudgetTrend,
  delete: deleteBudgetTrend,
  get: getBudgetTrend,
  getPage: getBudgetTrendPage,
  execute: executeBudgetTrend,
  getResult: getBudgetTrendResult
}

// 多维分析API简化导�?
export const multiDimensionAnalysisApi = {
  create: createBudgetComparison,
  update: updateBudgetComparison,
  delete: deleteBudgetComparison,
  get: getBudgetComparison,
  getPage: getBudgetComparisonPage,
  execute: executeBudgetComparison,
  getResult: getBudgetComparisonResult
}

// 钻取分析API简化导�?
export const drillDownAnalysisApi = {
  create: createBudgetComparison,
  update: updateBudgetComparison,
  delete: deleteBudgetComparison,
  get: getBudgetComparison,
  getPage: getBudgetComparisonPage,
  execute: executeBudgetComparison,
  getResult: getBudgetComparisonResult
}

// 预测分析API简化导�?
export const forecastAnalysisApi = {
  create: createBudgetTrend,
  update: updateBudgetTrend,
  delete: deleteBudgetTrend,
  get: getBudgetTrend,
  getPage: getBudgetTrendPage,
  execute: executeBudgetTrend,
  getResult: getBudgetTrendResult
}

// 敏感性分析API简化导�?
export const sensitivityAnalysisApi = {
  create: createBudgetVariance,
  update: updateBudgetVariance,
  delete: deleteBudgetVariance,
  get: getBudgetVariance,
  getPage: getBudgetVariancePage,
  execute: executeBudgetVariance,
  getResult: getBudgetVarianceResult
}

// 场景分析API简化导�?
export const scenarioAnalysisApi = {
  create: createBudgetComparison,
  update: updateBudgetComparison,
  delete: deleteBudgetComparison,
  get: getBudgetComparison,
  getPage: getBudgetComparisonPage,
  execute: executeBudgetComparison,
  getResult: getBudgetComparisonResult
}

// 关联分析API简化导�?
export const correlationAnalysisApi = {
  create: createBudgetVariance,
  update: updateBudgetVariance,
  delete: deleteBudgetVariance,
  get: getBudgetVariance,
  getPage: getBudgetVariancePage,
  execute: executeBudgetVariance,
  getResult: getBudgetVarianceResult
}

// 异常分析API简化导�?
export const anomalyAnalysisApi = {
  create: createBudgetVariance,
  update: updateBudgetVariance,
  delete: deleteBudgetVariance,
  get: getBudgetVariance,
  getPage: getBudgetVariancePage,
  execute: executeBudgetVariance,
  getResult: getBudgetVarianceResult
}

// 智能分析API简化导�?
export const intelligentAnalysisApi = {
  create: createBudgetTrend,
  update: updateBudgetTrend,
  delete: deleteBudgetTrend,
  get: getBudgetTrend,
  getPage: getBudgetTrendPage,
  execute: executeBudgetTrend,
  getResult: getBudgetTrendResult
}

// 分析报告API简化导�?
export const analysisReportApi = {
  create: createAnalysisReport,
  update: updateAnalysisReport,
  delete: deleteAnalysisReport,
  get: getAnalysisReport,
  getPage: getAnalysisReportPage,
  export: exportAnalysisReport,
  share: shareAnalysisReport
}

// ==================== 差异分析API补充 ====================

/**
 * 获取差异分析结果
 * @param {Object} params 查询参数
 * @returns {Promise} 请求结果
 */
export function getVarianceAnalysisPage(params) {
  return request({
    url: '/glkj/accountant/variance/page',
    method: 'post',
    headers: JSON_HEADERS,
    data: params
  })
}

/**
 * 获取组织选项
 * @returns {Promise} 请求结果
 */
export function getVarianceAnalysisOrganizations() {
  return request({
    url: '/glkj/accountant/variance/organizations',
    method: 'get'
  })
}

/**
 * 获取预算科目选项
 * @returns {Promise} 请求结果
 */
export function getVarianceAnalysisBudgetAccounts() {
  return request({
    url: '/glkj/accountant/variance/budget-accounts',
    method: 'get'
  })
}

/**
 * 获取用户选项
 * @returns {Promise} 请求结果
 */
export function getVarianceAnalysisUsers() {
  return request({
    url: '/glkj/accountant/variance/users',
    method: 'get'
  })
}

/**
 * 更新差异原因
 * @param {Object} data 差异原因数据
 * @returns {Promise} 请求结果
 */
export function updateVarianceAnalysisReason(data) {
  return request({
    url: '/glkj/accountant/variance/reason',
    method: 'put',
    headers: JSON_HEADERS,
    data
  })
}

/**
 * 导出差异分析
 * @param {Object} params 导出参数
 * @returns {Promise} 请求结果
 */
export function exportVarianceAnalysisReport(params) {
  return request({
    url: '/glkj/accountant/variance/export',
    method: 'post',
    headers: JSON_HEADERS,
    data: params,
    responseType: 'blob'
  })
}

/**
 * 导出单个差异
 * @param {String} varianceId 差异ID
 * @returns {Promise} 请求结果
 */
export function exportVarianceAnalysisSingle(varianceId) {
  return request({
    url: `/glkj/accountant/variance/export/${varianceId}`,
    method: 'get',
    responseType: 'blob'
  })
}

// 注意：getVarianceAnalysis 已在第71行定义，用于执行偏差分析
// 这里不再创建别名，直接在导出对象中使用完整方法名

// 预算分析API简化导出
export const budgetAnalysisApi = {
  comparison: comparisonAnalysisApi,
  variance: varianceAnalysisApi,
  trend: trendAnalysisApi,
  multiDimension: multiDimensionAnalysisApi,
  drillDown: drillDownAnalysisApi,
  forecast: forecastAnalysisApi,
  sensitivity: sensitivityAnalysisApi,
  scenario: scenarioAnalysisApi,
  correlation: correlationAnalysisApi,
  anomaly: anomalyAnalysisApi,
  intelligent: intelligentAnalysisApi,
  report: analysisReportApi,

  // ---- 差异分析页面专用方法 ----
  getVarianceAnalysisPage,
  getVarianceAnalysisOrganizations,
  getVarianceAnalysisBudgetAccounts,
  getVarianceAnalysisUsers,
  updateVarianceAnalysisReason,
  exportVarianceAnalysisReport,
  exportVarianceAnalysisSingle,

  // ---- ComparisonAnalysis.vue 需要的方法 ----
  getComparisonAnalysis: (params) => request({
    url: '/glkj/accountant/budget/analysis/comparison/execute',
    method: 'post',
    headers: JSON_HEADERS,
    data: params
  }),
  getOrganizations: () => request({
    url: '/glkj/accountant/budget/analysis/comparison/organizations',
    method: 'get'
  }),
  getBudgetAccounts: () => request({
    url: '/glkj/accountant/budget/analysis/comparison/budget-accounts',
    method: 'get'
  }),
  exportComparisonAnalysis: (params) => request({
    url: '/glkj/accountant/budget/analysis/comparison/export',
    method: 'post',
    headers: JSON_HEADERS,
    data: params
  }),
  exportSingleComparison: (id) => request({
    url: `/glkj/accountant/budget/analysis/comparison/export`,
    method: 'post',
    headers: JSON_HEADERS,
    data: { id }
  }),
  createBudgetComparison: (data) => request({
    url: '/glkj/accountant/budget/analysis/comparison/execute',
    method: 'post',
    headers: JSON_HEADERS,
    data: { ...data, comparisonName: data.comparisonName || data.analysisName }
  }),
  getBudgetComparison: (id) => request({
    url: '/glkj/accountant/budget/analysis/comparison/chart',
    method: 'post',
    headers: JSON_HEADERS,
    data: { id }
  }),
  deleteComparison: (id) => request({
    url: '/glkj/accountant/budget/analysis/comparison/delete',
    method: 'post',
    headers: JSON_HEADERS,
    data: { id }
  }),
  updateComparison: (data) => request({
    url: '/glkj/accountant/budget/analysis/comparison/update',
    method: 'post',
    headers: JSON_HEADERS,
    data
  }),
  batchComparison: (ids) => request({
    url: '/glkj/accountant/budget/analysis/comparison/batch',
    method: 'post',
    headers: JSON_HEADERS,
    data: { ids }
  }),

  // ---- TrendAnalysis.vue 需要的方法 ----
  getTrendOrganizations: () => request({
    url: '/glkj/accountant/budget/analysis/trend/organizations',
    method: 'get'
  }),
  getTrendBudgetAccounts: () => request({
    url: '/glkj/accountant/budget/analysis/trend/budget-accounts',
    method: 'get'
  }),
  getTrendAnalysis: (params) => request({
    url: '/glkj/accountant/budget/analysis/trend/list',
    method: 'post',
    headers: JSON_HEADERS,
    data: params
  }),
  createTrendAnalysis: (params) => request({
    url: '/glkj/accountant/budget/analysis/trend/create',
    method: 'post',
    headers: JSON_HEADERS,
    data: params
  }),
  exportTrendAnalysis: (params) => request({
    url: '/glkj/accountant/budget/analysis/trend/export',
    method: 'post',
    headers: JSON_HEADERS,
    data: params,
    responseType: 'blob'
  }),
  exportSingleTrend: (id) => request({
    url: `/glkj/accountant/budget/analysis/trend/export`,
    method: 'post',
    headers: JSON_HEADERS,
    data: { id },
    responseType: 'blob'
  }),
  updateTrendAnalysis: (data) => request({
    url: '/glkj/accountant/budget/analysis/trend/update',
    method: 'post',
    headers: JSON_HEADERS,
    data
  }),
  deleteTrendAnalysis: (id) => request({
    url: '/glkj/accountant/budget/analysis/trend/delete',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data: { id }
  }),

  // ---- PerformanceAnalysis.vue 需要的方法 ----
  getExecutionPage,
  createExecution,
  generateExecutionReport,
  getExecutionDetail,
  updateExecution: (data) => request({
    url: '/glkj/accountant/execution/update',
    method: 'post',
    headers: JSON_HEADERS,
    data
  }),
  exportPerformanceAnalysis: (params) => request({
    url: '/glkj/accountant/execution/export',
    method: 'post',
    headers: JSON_HEADERS,
    data: params,
    responseType: 'blob'
  }),
  exportSinglePerformance: (id) => request({
    url: `/glkj/accountant/execution/export/${id}`,
    method: 'get',
    responseType: 'blob'
  }),

  // ---- ForecastAnalysis.vue 需要的方法 ----
  getForecastAnalysis: (params) => request({
    url: '/glkj/accountant/budget/analysis/forecast/execute',
    method: 'post',
    headers: JSON_HEADERS,
    data: params
  }),
  exportForecastAnalysis: (params) => request({
    url: '/glkj/accountant/budget/analysis/forecast/export',
    method: 'post',
    headers: JSON_HEADERS,
    data: params,
    responseType: 'blob'
  }),
  validateModel: (modelId) => request({
    url: `/glkj/accountant/budget/analysis/forecast/validate/${modelId}`,
    method: 'post',
  headers: JSON_HEADERS
  }),
  trainModel: (data) => request({
    url: '/glkj/accountant/budget/analysis/forecast/train',
    method: 'post',
    headers: JSON_HEADERS,
    data
  }),
  createForecast: (data) => request({
    url: '/glkj/accountant/budget/analysis/forecast/create',
    method: 'post',
    headers: JSON_HEADERS,
    data
  }),
  getForecastBudgetAccounts: () => request({
    url: '/glkj/accountant/budget/analysis/forecast/accounts',
    method: 'get'
  }),
  updateForecast: (data) => request({
    url: '/glkj/accountant/budget/analysis/forecast/update',
    method: 'put',
    headers: JSON_HEADERS,
    data
  }),
  deleteForecast: (id) => request({
    url: `/glkj/accountant/budget/analysis/forecast/delete/${id}`,
    method: 'delete'
  }),

  // ---- ScenarioAnalysis.vue 需要的方法 ----
  getScenarioAnalysis: (params) => request({
    url: '/glkj/accountant/budget/scenario/analysis/page',
    method: 'post',
    headers: JSON_HEADERS,
    data: params
  }),
  createScenario: (data) => request({
    url: '/glkj/accountant/budget/scenario/analysis/create',
    method: 'post',
    headers: JSON_HEADERS,
    data
  }),
  exportScenarioAnalysis: (params) => request({
    url: '/glkj/accountant/budget/scenario/analysis/export',
    method: 'post',
    headers: JSON_HEADERS,
    data: params,
    responseType: 'blob'
  }),
  copyScenario: (id) => request({
    url: `/glkj/accountant/budget/scenario/analysis/copy/${id}`,
    method: 'post',
  headers: JSON_HEADERS
  }),

  // ---- SensitivityAnalysis.vue 需要的方法 ----
  analyzeSensitivity: (params) => request({
    url: '/glkj/accountant/budget/sensitivity/analysis/execute',
    method: 'post',
    headers: JSON_HEADERS,
    data: params
  }),
  exportSensitivityAnalysis: (params) => request({
    url: '/glkj/accountant/budget/sensitivity/analysis/export',
    method: 'post',
    headers: JSON_HEADERS,
    data: params,
    responseType: 'blob'
  }),

  // ---- AnalysisReport.vue 需要的方法 ----
  getAnalysisReports: (params) => request({
    url: '/glkj/accountant/budget/analysis/report/page',
    method: 'post',
    headers: JSON_HEADERS,
    data: params
  }),
  createAnalysisReport,
  getReportStats: () => request({
    url: '/glkj/accountant/budget/analysis/report/stats',
    method: 'get'
  }),
  getReportTemplates: () => request({
    url: '/glkj/accountant/budget/analysis/report/templates',
    method: 'get'
  }),
  batchExportReports: (ids) => request({
    url: '/glkj/accountant/budget/analysis/report/batch-export',
    method: 'post',
    headers: JSON_HEADERS,
    data: { ids }
  }),
  quickGenerateReport: (templateId) => request({
    url: `/glkj/accountant/budget/analysis/report/quick-generate/${templateId}`,
    method: 'post',
    headers: JSON_HEADERS
  }),
  downloadReport: (id) => request({
    url: `/glkj/accountant/budget/analysis/report/${id}/export`,
    method: 'get',
    responseType: 'blob'
  }),
  regenerateReport: (id) => request({
    url: `/glkj/accountant/budget/analysis/report/regenerate/${id}`,
    method: 'post',
    headers: JSON_HEADERS
  }),
  copyReport: (id) => request({
    url: `/glkj/accountant/budget/analysis/report/copy/${id}`,
    method: 'post',
    headers: JSON_HEADERS
  }),
  deleteReport: (reportId) => request({
    url: `/glkj/accountant/budget/analysis/report/delete/${reportId}`,
    method: 'delete'
  }),
  // 更新报告
  updateReport: (reportId, data) => request({
    url: `/glkj/accountant/budget/analysis/report/${reportId}`,
    method: 'put',
    headers: JSON_HEADERS,
    data
  }),
  // 分享报告
  shareReport: (reportId, data) => request({
    url: `/glkj/accountant/budget/analysis/report/${reportId}/share`,
    method: 'post',
    headers: JSON_HEADERS,
    data
  }),
  // 获取分享记录
  getReportShares: (reportId) => request({
    url: `/glkj/accountant/budget/analysis/report/${reportId}/shares`,
    method: 'get'
  }),
  // 创建定时生成任务
  createSchedule: (data) => request({
    url: '/glkj/accountant/budget/analysis/report/schedule',
    method: 'post',
    headers: JSON_HEADERS,
    data
  }),
  // 获取定时任务列表
  getReportSchedules: (reportId) => request({
    url: `/glkj/accountant/budget/analysis/report/${reportId}/schedules`,
    method: 'get'
  }),
  // 更新定时任务状态
  updateScheduleStatus: (scheduleId, status) => request({
    url: `/glkj/accountant/budget/analysis/report/schedule/${scheduleId}/status`,
    method: 'put',
    headers: JSON_HEADERS,
    data: { status }
  }),
  // 删除定时任务
  deleteSchedule: (scheduleId) => request({
    url: `/glkj/accountant/budget/analysis/report/schedule/${scheduleId}`,
    method: 'delete'
  }),
  // 模板分页查询
  getTemplatePage: (params) => request({
    url: '/glkj/accountant/budget/analysis/report/template/page',
    method: 'post',
    headers: JSON_HEADERS,
    data: params
  }),
  // 创建模板
  createTemplate: (data) => request({
    url: '/glkj/accountant/budget/analysis/report/template',
    method: 'post',
    headers: JSON_HEADERS,
    data
  }),
  // 更新模板
  updateTemplate: (templateId, data) => request({
    url: `/glkj/accountant/budget/analysis/report/template/${templateId}`,
    method: 'put',
    headers: JSON_HEADERS,
    data
  }),
  // 删除模板
  deleteTemplate: (templateId) => request({
    url: `/glkj/accountant/budget/analysis/report/template/${templateId}`,
    method: 'delete'
  }),

  // ---- AnalysisDashboard.vue 需要的方法 ----
  exportDashboard: (params) => request({
    url: '/glkj/accountant/budget/analysis/report/export',
    method: 'post',
    headers: JSON_HEADERS,
    data: params,
    responseType: 'blob'
  }),

  // ---- RollingForecast.vue 需要的方法 ----
  getRollingForecast: (params) => request({
    url: '/glkj/accountant/budget/rolling/page',
    method: 'post',
    headers: JSON_HEADERS,
    data: params
  }),
  // 启动滚动预测：创建一条新的滚动预算记录
  startRollingForecast: (data) => request({
    url: '/glkj/accountant/budget/rolling/create',
    method: 'post',
    headers: JSON_HEADERS,
    data
  }),
  // 导出滚动预测：导出列表数据（后端返回JSON，前端自行处理）
  exportRollingForecast: (params) => request({
    url: '/glkj/accountant/budget/rolling/page',
    method: 'post',
    headers: JSON_HEADERS,
    data: { ...params, pageNum: 1, pageSize: 1000 }
  }),
  // 应用配置：更新滚动预算配置（通过 update 接口）
  updateRollingConfig: (data) => request({
    url: `/glkj/accountant/budget/rolling/update/${data.rollingId || data.id || 'default'}`,
    method: 'put',
    headers: JSON_HEADERS,
    data
  }),
  // 获取滚动预算详情
  getRollingDetail: (id) => request({
    url: `/glkj/accountant/budget/rolling/detail/${id}`,
    method: 'get'
  }),
  // 获取组织选项（复用差异分析的组织接口）
  getOrganizations: () => request({
    url: '/glkj/accountant/variance/organizations',
    method: 'get'
  }),
  // 执行滚动（对指定ID执行滚动操作）
  executeRolling: (rollingId) => request({
    url: `/glkj/accountant/budget/rolling/execute/${rollingId}`,
    method: 'post',
    headers: JSON_HEADERS,
    data: {}
  }),
  // 获取执行历史记录
  getRollingExecutionRecords: (rollingId) => request({
    url: `/glkj/accountant/budget/rolling/plan/${rollingId}/executions`,
    method: 'get'
  }),

  // ---- ForecastAnalysis.vue 补充方法 ----
  validateForecast: (id) => request({
    url: `/glkj/accountant/budget/analysis/forecast/validate-result/${id}`,
    method: 'post',
  headers: JSON_HEADERS
  }),
  exportSingleForecast: (id) => request({
    url: `/glkj/accountant/budget/analysis/forecast/export/${id}`,
    method: 'get',
    responseType: 'blob'
  }),

  // ---- ScenarioAnalysis.vue 补充方法 ----
  exportSingleScenario: (id) => request({
    url: `/glkj/accountant/budget/scenario/analysis/export/${id}`,
    method: 'get',
    responseType: 'blob'
  }),
  deleteScenario: (id) => request({
    url: `/glkj/accountant/budget/scenario/analysis/delete/${id}`,
    method: 'delete'
  }),
  updateScenario: (data) => request({
    url: '/glkj/accountant/budget/scenario/analysis/update',
    method: 'put',
    headers: JSON_HEADERS,
    data
  }),
  getOrganizations: () => request({
    url: '/glkj/accountant/budget/scenario/analysis/organizations',
    method: 'get'
  }),

  // ---- RollingForecast.vue 补充方法 ----
  applyAdjustment: (id) => request({
    url: `/glkj/accountant/budget/rolling/adjustment/apply/${id}`,
    method: 'post',
  headers: JSON_HEADERS
  }),
  revertAdjustment: (id) => request({
    url: `/glkj/accountant/budget/rolling/adjustment/revert/${id}`,
    method: 'post',
  headers: JSON_HEADERS
  }),
  ignoreAnomaly: (id) => request({
    url: `/glkj/accountant/budget/rolling/anomaly/ignore/${id}`,
    method: 'post',
  headers: JSON_HEADERS
  }),
  exportSingleRolling: (id) => request({
    url: `/glkj/accountant/budget/rolling/export/${id}`,
    method: 'get',
    responseType: 'blob'
  }),

  // ---- index.vue 仪表盘数据 ----
  getDashboardStats: () => request({
    url: '/glkj/accountant/budget/analysis/dashboard/stats',
    method: 'get'
  }),

  // ---- 各页面统计数据API ----
  getVarianceStats: () => request({
    url: '/glkj/accountant/variance/stats',
    method: 'get'
  }),
  getVarianceDetail: (id) => request({
    url: `/glkj/accountant/variance/detail/${id}`,
    method: 'get'
  }),
  getVarianceChartData: (params) => request({
    url: '/glkj/accountant/variance/chart-data',
    method: 'post',
    headers: JSON_HEADERS,
    data: params
  }),
  getComparisonStats: () => request({
    url: '/glkj/accountant/budget/analysis/comparison/stats',
    method: 'get'
  }),
  getComparisonChartData: (params) => request({
    url: '/glkj/accountant/budget/analysis/comparison/chart-data',
    method: 'post',
    headers: JSON_HEADERS,
    data: params
  }),
  getTrendStats: () => request({
    url: '/glkj/accountant/budget/analysis/trend/stats',
    method: 'get'
  }),
  getTrendChartData: (params) => request({
    url: '/glkj/accountant/budget/analysis/trend/chart-data',
    method: 'post',
    headers: JSON_HEADERS,
    data: params
  }),
  getPerformanceStats: () => request({
    url: '/glkj/accountant/execution/stats',
    method: 'get'
  }),
  getPerformanceChartData: (params) => request({
    url: '/glkj/accountant/execution/chart-data',
    method: 'post',
    headers: JSON_HEADERS,
    data: params
  }),
  getPerformanceRanking: () => request({
    url: '/glkj/accountant/execution/ranking',
    method: 'get'
  }),
  getImprovementSuggestions: () => request({
    url: '/glkj/accountant/execution/suggestions',
    method: 'get'
  }),
  getForecastStats: () => request({
    url: '/glkj/accountant/budget/analysis/forecast/stats',
    method: 'get'
  }),
  getForecastChartData: (params) => request({
    url: '/glkj/accountant/budget/analysis/forecast/chart-data',
    method: 'post',
    headers: JSON_HEADERS,
    data: params
  }),
  getForecastModelInfo: () => request({
    url: '/glkj/accountant/budget/analysis/forecast/model-info',
    method: 'get'
  }),
  getRollingStats: () => request({
    url: '/glkj/accountant/budget/rolling/stats',
    method: 'get'
  }),
  getRollingChartData: (params) => request({
    url: '/glkj/accountant/budget/rolling/chart-data',
    method: 'post',
    headers: JSON_HEADERS,
    data: params
  }),
  getScenarioStats: () => request({
    url: '/glkj/accountant/budget/scenario/analysis/stats',
    method: 'get'
  }),
  getScenarioChartData: (params) => request({
    url: '/glkj/accountant/budget/scenario/analysis/chart-data',
    method: 'post',
    headers: JSON_HEADERS,
    data: params
  }),
  // ---- 场景变量 CRUD ----
  getVariableList: (params) => request({
    url: '/glkj/accountant/budget/scenario/variable/page',
    method: 'post',
    headers: JSON_HEADERS,
    data: params
  }),
  createVariable: (params) => request({
    url: '/glkj/accountant/budget/scenario/variable/create',
    method: 'post',
    headers: JSON_HEADERS,
    data: params
  }),
  updateVariable: (params) => request({
    url: '/glkj/accountant/budget/scenario/variable/update',
    method: 'put',
    headers: JSON_HEADERS,
    data: params
  }),
  deleteVariable: (id) => request({
    url: `/glkj/accountant/budget/scenario/variable/delete/${id}`,
    method: 'delete'
  }),
  getSensitivityStats: () => request({
    url: '/glkj/accountant/budget/sensitivity/analysis/stats',
    method: 'get'
  }),
  getSensitivityChartData: (params) => request({
    url: '/glkj/accountant/budget/sensitivity/analysis/chart-data',
    method: 'post',
    headers: JSON_HEADERS,
    data: params || {}
  }),

  // ---- SensitivityAnalysis.vue 敏感性变量 CRUD ----
  getSensitivityVariablePage: (params) => request({
    url: '/glkj/accountant/budget/sensitivity/variable/page',
    method: 'post',
    headers: JSON_HEADERS,
    data: params
  }),
  createSensitivityVariable: (data) => request({
    url: '/glkj/accountant/budget/sensitivity/variable/create',
    method: 'post',
    headers: JSON_HEADERS,
    data
  }),
  updateSensitivityVariable: (data) => request({
    url: '/glkj/accountant/budget/sensitivity/variable/update',
    method: 'put',
    headers: JSON_HEADERS,
    data
  }),
  deleteSensitivityVariable: (id) => request({
    url: `/glkj/accountant/budget/sensitivity/variable/delete/${id}`,
    method: 'delete'
  }),
  updateSensitivityVariableEnabled: (id, enabled) => request({
    url: `/glkj/accountant/budget/sensitivity/variable/enabled/${id}`,
    method: 'put',
    params: { enabled }
  }),
  getDashboardData: () => request({
    url: '/glkj/accountant/budget/analysis/dashboard/data',
    method: 'get'
  }),
  getDashboardChartData: (params) => request({
    url: '/glkj/accountant/budget/analysis/dashboard/chart-data',
    method: 'post',
    headers: JSON_HEADERS,
    data: params
  }),
  getReportStats: () => request({
    url: '/glkj/accountant/budget/analysis/report/stats',
    method: 'get'
  }),
  getReportTemplates: () => request({
    url: '/glkj/accountant/budget/analysis/report/templates',
    method: 'get'
  }),
  getChartStats: () => request({
    url: '/glkj/accountant/budget/analysis/chart/stats',
    method: 'get'
  }),
  getChartPage: (params) => request({
    url: '/glkj/accountant/budget/analysis/chart/page',
    method: 'post',
    headers: JSON_HEADERS,
    data: params
  }),
  getChartTypes: () => request({
    url: '/glkj/accountant/budget/analysis/chart/types',
    method: 'get'
  }),
  getChartLibrary: () => request({
    url: '/glkj/accountant/budget/analysis/chart/library',
    method: 'get'
  }),
  exportChart: (params) => request({
    url: '/glkj/accountant/budget/analysis/chart/export',
    method: 'post',
    headers: JSON_HEADERS,
    data: params,
    responseType: 'blob'
  }),
  deleteChart: (id) => request({
    url: `/glkj/accountant/budget/analysis/chart/delete/${id}`,
    method: 'delete'
  }),
  createChart: (data) => request({
    url: '/glkj/accountant/budget/analysis/chart/create',
    method: 'post',
    headers: JSON_HEADERS,
    data
  }),
  updateChart: (data) => request({
    url: '/glkj/accountant/budget/analysis/chart/update',
    method: 'put',
    headers: JSON_HEADERS,
    data
  }),
  getChartDetail: (id) => request({
    url: `/glkj/accountant/budget/analysis/chart/detail/${id}`,
    method: 'get'
  }),
  getChartData: (params) => request({
    url: '/glkj/accountant/budget/analysis/chart/chart-data',
    method: 'post',
    headers: JSON_HEADERS,
    data: params
  }),
  // ---- Dashboard 快捷操作接口 ----
  runVarianceAnalysis: (params) => request({
    url: '/glkj/accountant/budget/analysis/dashboard/variance-analysis',
    method: 'post',
    headers: JSON_HEADERS,
    data: params
  }),
  saveAlertSettings: (params) => request({
    url: '/glkj/accountant/budget/analysis/dashboard/alert-settings',
    method: 'post',
    headers: JSON_HEADERS,
    data: params
  }),
  saveSysSettings: (params) => request({
    url: '/glkj/accountant/budget/analysis/dashboard/sys-settings',
    method: 'post',
    headers: JSON_HEADERS,
    data: params
  }),
  saveLayout: (params) => request({
    url: '/glkj/accountant/budget/analysis/dashboard/layout',
    method: 'post',
    headers: JSON_HEADERS,
    data: params
  }),
  getLayout: () => request({
    url: '/glkj/accountant/budget/analysis/dashboard/layout',
    method: 'get'
  }),

  // ---- 快速分析 ----
  quickAnalysis: (params) => request({
    url: '/glkj/accountant/budget/analysis/dashboard/quick-analysis',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data: params
  }),
}
