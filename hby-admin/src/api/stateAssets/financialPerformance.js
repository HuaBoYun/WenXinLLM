import request from '@/utils/request'
import { transData } from '@/utils/requestData'

const BASE = '/monitor/v1/supervision/financial'

// 财务绩效评价API接口

// ==================== 基础CRUD操作 ====================

export function getFinancialPerformanceList(params) {
  return request({ url: `${BASE}/performance/list`, method: 'post', data: transData(params) })
}

export function getFinancialPerformanceDetail(id) {
  return request({ url: `${BASE}/performance/${id}`, method: 'get' })
}

export function createFinancialPerformance(data) {
  return request({ url: `${BASE}/performance/save`, method: 'post', data: transData(data) })
}

export function updateFinancialPerformance(id, data) {
  return request({ url: `${BASE}/performance/save`, method: 'post', data: transData({ ...data, performanceId: id }) })
}

export function deleteFinancialPerformance(id) {
  return request({ url: `${BASE}/performance/${id}`, method: 'delete' })
}

// ==================== 统计分析 ====================

export function getPerformanceStatistics(params) {
  return request({ url: `${BASE}/performance/statistics`, method: 'get', params })
}

export function getPerformanceTrend(params) {
  return request({ url: `${BASE}/performance/trend`, method: 'get', params })
}

export function getPerformanceDistribution(params) {
  return request({ url: `${BASE}/performance/distribution`, method: 'get', params })
}

export function getPerformanceRanking(params) {
  return request({ url: `${BASE}/performance/ranking`, method: 'get', params })
}

// ==================== 绩效评价 ====================

export function executePerformanceEvaluation(data) {
  return request({ url: `${BASE}/performance/evaluate`, method: 'post', data: transData(data) })
}

export function getEvaluationResult(evaluationId) {
  return request({ url: `${BASE}/performance/evaluation/result/${evaluationId}`, method: 'get' })
}

export function getPerformanceIndicators(params) {
  return request({ url: `${BASE}/performance/indicators`, method: 'get', params })
}

export function calculatePerformanceScore(data) {
  return request({ url: `${BASE}/performance/calculate-score`, method: 'post', data: transData(data) })
}

// ==================== 对标分析 ====================

export function executeBenchmarkAnalysis(data) {
  return request({ url: `${BASE}/performance/benchmark`, method: 'post', data: transData(data) })
}

export function exportBenchmarkReport(data) {
  return request({ url: `${BASE}/performance/benchmark/export`, method: 'post', data: transData(data), responseType: 'blob' })
}

export function generateBenchmarkActionPlan(data) {
  return request({ url: `${BASE}/performance/benchmark/action-plan`, method: 'post', data: transData(data) })
}

export function getBenchmarkResult(analysisId) {
  return request({ url: `${BASE}/performance/benchmark/result/${analysisId}`, method: 'get' })
}

export function getIndustryBenchmark(params) {
  return request({ url: `${BASE}/performance/industry-benchmark`, method: 'get', params })
}

export function getPeerComparison(params) {
  return request({ url: `${BASE}/performance/peer-comparison`, method: 'get', params })
}

// ==================== 绩效改进 ====================

export function generateImprovementSuggestions(data) {
  return request({ url: `${BASE}/performance/improvement-suggestions`, method: 'post', data: transData(data) })
}

export function createImprovementPlan(data) {
  return request({ url: `${BASE}/performance/improvement-plan`, method: 'post', data: transData(data) })
}

export function trackImprovementProgress(planId) {
  return request({ url: `${BASE}/performance/improvement/progress/${planId}`, method: 'get' })
}

export function assessImprovementEffect(data) {
  return request({ url: `${BASE}/performance/improvement/assessment`, method: 'post', data: transData(data) })
}

// ==================== 报告生成 ====================

export function generatePerformanceReport(data) {
  return request({ url: `${BASE}/performance/report/generate`, method: 'post', data: transData(data) })
}

export function getPerformanceReports(params) {
  return request({ url: `${BASE}/performance/reports`, method: 'get', params })
}

export function downloadPerformanceReport(reportId) {
  return request({ url: `${BASE}/performance/report/download/${reportId}`, method: 'get', responseType: 'blob' })
}

// ==================== 预警管理 ====================

export function setPerformanceAlert(data) {
  return request({ url: `${BASE}/performance/alert/set`, method: 'post', data: transData(data) })
}

export function getPerformanceAlerts(params) {
  return request({ url: `${BASE}/performance/alerts`, method: 'get', params })
}

export function handlePerformanceAlert(alertId, data) {
  return request({ url: `${BASE}/performance/alert/handle/${alertId}`, method: 'post', data: transData(data) })
}

// ==================== 批量操作 ====================

export function batchEvaluate(data) {
  return request({ url: `${BASE}/performance/batch-evaluate`, method: 'post', data: transData(data) })
}

export function batchBenchmark(data) {
  return request({ url: `${BASE}/performance/batch-benchmark`, method: 'post', data: transData(data) })
}

export function batchDelete(ids) {
  return request({ url: `${BASE}/performance/batch-delete`, method: 'post', data: transData({ ids }) })
}

// ==================== 数据导入导出 ====================

export function exportPerformanceData(params) {
  return request({ url: `${BASE}/performance/export-excel`, method: 'post', data: params, responseType: 'blob' })
}

export function importPerformanceData(formData) {
  return request({ url: `${BASE}/performance/import`, method: 'post', data: formData, headers: { 'Content-Type': 'multipart/form-data' } })
}

export function getImportTemplate() {
  return request({ url: `${BASE}/performance/import-template`, method: 'get', responseType: 'blob' })
}
