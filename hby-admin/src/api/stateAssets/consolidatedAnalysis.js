import request from '@/utils/request'
import { transData } from '@/utils/requestData'

const BASE = '/monitor/v1/supervision/financial'

export function getConsolidatedAnalysisList(data) {
  return request({ url: `${BASE}/consolidated/list`, method: 'post', data: transData(data) })
}
export function getConsolidatedAnalysisById(id) {
  return request({ url: `${BASE}/consolidated/${id}`, method: 'get', params: transData() })
}
export function addConsolidatedAnalysis(data) {
  return request({ url: `${BASE}/consolidated/save`, method: 'post', data: transData(data) })
}
export function updateConsolidatedAnalysis(data) {
  return request({ url: `${BASE}/consolidated/save`, method: 'post', data: transData(data) })
}
export function deleteConsolidatedAnalysis(id) {
  return request({ url: `${BASE}/consolidated/${id}`, method: 'delete', params: transData() })
}
export function performConsolidatedAnalysis(data) {
  const payload = typeof data === 'object' ? data : { id: data }
  return request({ url: `${BASE}/consolidated/perform`, method: 'post', data: transData(payload) })
}
export function buildConsolidatedStatement(data) {
  return request({ url: `${BASE}/consolidated/statement`, method: 'post', data: transData(data) })
}
export function analyzeConsolidationScope(data) {
  return request({ url: `${BASE}/consolidated/scope`, method: 'post', data: transData(data) })
}
export function identifyConsolidationEntities(data) {
  return request({ url: `${BASE}/consolidated/entities`, method: 'post', data: transData(data) })
}
export function analyzeConsolidationAdjustments(data) {
  return request({ url: `${BASE}/consolidated/adjustments`, method: 'post', data: transData(data) })
}
export function calculateConsolidatedMetrics(data) {
  return request({ url: `${BASE}/consolidated/metrics`, method: 'post', data: transData(data) })
}
export function analyzeConsolidatedTrend(data) {
  return request({ url: `${BASE}/consolidated/trend`, method: 'post', data: transData(data) })
}
export function compareConsolidatedData(data) {
  return request({ url: `${BASE}/consolidated/compare`, method: 'post', data: transData(data) })
}
export function analyzeConsolidatedRisk(data) {
  return request({ url: `${BASE}/consolidated/risk`, method: 'post', data: transData(data) })
}
export function assessConsolidatedQuality(data) {
  return request({ url: `${BASE}/consolidated/quality`, method: 'post', data: transData(data) })
}
export function detectConsolidatedAnomalies(data) {
  return request({ url: `${BASE}/consolidated/anomalies`, method: 'post', data: transData(data) })
}
export function analyzeConsolidatedStructure(data) {
  return request({ url: `${BASE}/consolidated/structure`, method: 'post', data: transData(data) })
}
export function predictConsolidatedPerformance(data) {
  return request({ url: `${BASE}/consolidated/predict`, method: 'post', data: transData(data) })
}
export function analyzeConsolidatedImpact(data) {
  return request({ url: `${BASE}/consolidated/impact`, method: 'post', data: transData(data) })
}
export function simulateConsolidatedChange(data) {
  return request({ url: `${BASE}/consolidated/simulate`, method: 'post', data: transData(data) })
}
export function optimizeConsolidatedStructure(data) {
  return request({ url: `${BASE}/consolidated/optimize`, method: 'post', data: transData(data) })
}
export function analyzeConsolidatedStability(data) {
  return request({ url: `${BASE}/consolidated/stability`, method: 'post', data: transData(data) })
}
export function assessConsolidatedCompliance(data) {
  return request({ url: `${BASE}/consolidated/compliance`, method: 'post', data: transData(data) })
}
export function getConsolidatedStatistics(data) {
  return request({ url: `${BASE}/consolidated/statistics`, method: 'post', data: transData(data) })
}
export function generateConsolidatedReport(data) {
  return request({ url: `${BASE}/consolidated/report`, method: 'post', data: transData(data) })
}
export function batchPerformConsolidated(data) {
  const payload = Array.isArray(data) ? { ids: data } : data
  return request({ url: `${BASE}/consolidated/batch/perform`, method: 'post', data: transData(payload) })
}
export function batchDeleteConsolidated(data) {
  return request({ url: `${BASE}/consolidated/batch/delete`, method: 'post', data: transData(data) })
}
export function exportConsolidatedData(data) {
  return request({ url: `${BASE}/consolidated/export`, method: 'post', data: transData(data) })
}
export function importConsolidatedData(data) {
  return request({ url: `${BASE}/consolidated/import`, method: 'post', data: transData(data) })
}
export function getConsolidatedChartData(data) {
  return request({ url: `${BASE}/consolidated/chart`, method: 'post', data: transData(data) })
}
export function analyzeConsolidatedSensitivity(data) {
  return request({ url: `${BASE}/consolidated/sensitivity`, method: 'post', data: transData(data) })
}
