import request from '@/utils/request'
import { transData } from '@/utils/requestData'

const BASE = '/monitor/v1/supervision/financial'

export function getPerformanceConsolidationList(data) {
  return request({ url: `${BASE}/performance-consolidation/list`, method: 'post', data: transData(data) })
}
export function getPerformanceConsolidationById(id) {
  return request({ url: `${BASE}/performance-consolidation/${id}`, method: 'get', params: transData() })
}
export function addPerformanceConsolidation(data) {
  return request({ url: `${BASE}/performance-consolidation/save`, method: 'post', data: transData(data) })
}
export function updatePerformanceConsolidation(data) {
  return request({ url: `${BASE}/performance-consolidation/save`, method: 'post', data: transData(data) })
}
export function deletePerformanceConsolidation(id) {
  return request({ url: `${BASE}/performance-consolidation/${id}`, method: 'delete', params: transData() })
}
export function performConsolidationAnalysis(data) {
  return request({ url: `${BASE}/performance-consolidation/perform`, method: 'post', data: transData(data) })
}
export function buildConsolidatedPerformanceStatement(data) {
  return request({ url: `${BASE}/performance-consolidation/statement`, method: 'post', data: transData(data) })
}
export function analyzePerformanceConsolidationScope(data) {
  return request({ url: `${BASE}/performance-consolidation/scope`, method: 'post', data: transData(data) })
}
export function calculateConsolidatedPerformanceMetrics(data) {
  return request({ url: `${BASE}/performance-consolidation/metrics`, method: 'post', data: transData(data) })
}
export function analyzePerformanceContribution(data) {
  return request({ url: `${BASE}/performance-consolidation/contribution`, method: 'post', data: transData(data) })
}
export function assessPerformanceSynergy(data) {
  return request({ url: `${BASE}/performance-consolidation/synergy`, method: 'post', data: transData(data) })
}
export function analyzePerformanceConsolidationTrend(data) {
  return request({ url: `${BASE}/performance-consolidation/trend`, method: 'post', data: transData(data) })
}
export function compareConsolidatedPerformance(data) {
  return request({ url: `${BASE}/performance-consolidation/compare`, method: 'post', data: transData(data) })
}
export function analyzePerformanceConsolidationRisk(data) {
  return request({ url: `${BASE}/performance-consolidation/risk`, method: 'post', data: transData(data) })
}
export function assessConsolidatedPerformanceQuality(data) {
  return request({ url: `${BASE}/performance-consolidation/quality`, method: 'post', data: transData(data) })
}
export function detectPerformanceConsolidationAnomalies(data) {
  return request({ url: `${BASE}/performance-consolidation/anomalies`, method: 'post', data: transData(data) })
}
export function analyzePerformanceConsolidationStructure(data) {
  return request({ url: `${BASE}/performance-consolidation/structure`, method: 'post', data: transData(data) })
}
export function predictConsolidatedPerformance(data) {
  return request({ url: `${BASE}/performance-consolidation/predict`, method: 'post', data: transData(data) })
}
export function analyzePerformanceConsolidationImpact(data) {
  return request({ url: `${BASE}/performance-consolidation/impact`, method: 'post', data: transData(data) })
}
export function simulatePerformanceConsolidationChange(data) {
  return request({ url: `${BASE}/performance-consolidation/simulate`, method: 'post', data: transData(data) })
}
export function optimizePerformanceConsolidationStructure(data) {
  return request({ url: `${BASE}/performance-consolidation/optimize`, method: 'post', data: transData(data) })
}
export function analyzePerformanceConsolidationStability(data) {
  return request({ url: `${BASE}/performance-consolidation/stability`, method: 'post', data: transData(data) })
}
export function assessPerformanceConsolidationCompliance(data) {
  return request({ url: `${BASE}/performance-consolidation/compliance`, method: 'post', data: transData(data) })
}
export function analyzePerformanceDecomposition(data) {
  return request({ url: `${BASE}/performance-consolidation/decomposition`, method: 'post', data: transData(data) })
}
export function assessPerformanceDrivers(data) {
  return request({ url: `${BASE}/performance-consolidation/drivers`, method: 'post', data: transData(data) })
}
export function getPerformanceConsolidationStatistics(data) {
  return request({ url: `${BASE}/performance-consolidation/statistics`, method: 'post', data: transData(data) })
}
export function generatePerformanceConsolidationReport(data) {
  return request({ url: `${BASE}/performance-consolidation/report`, method: 'post', data: transData(data) })
}
export function batchPerformConsolidation(data) {
  return request({ url: `${BASE}/performance-consolidation/batch/perform`, method: 'post', data: transData(data) })
}
export function batchDeletePerformanceConsolidation(data) {
  return request({ url: `${BASE}/performance-consolidation/batch/delete`, method: 'post', data: transData(data) })
}
export function exportPerformanceConsolidationData(data) {
  return request({ url: `${BASE}/performance-consolidation/export`, method: 'post', data: transData(data), responseType: 'blob' })
}
export function importPerformanceConsolidationData(data) {
  return request({ url: `${BASE}/performance-consolidation/import`, method: 'post', data: transData(data) })
}
export function getPerformanceConsolidationChartData(data) {
  return request({ url: `${BASE}/performance-consolidation/chart`, method: 'post', data: transData(data) })
}
export function analyzePerformanceConsolidationSensitivity(data) {
  return request({ url: `${BASE}/performance-consolidation/sensitivity`, method: 'post', data: transData(data) })
}
export function batchPerformPerformanceAnalysis(data) {
  return request({ url: `${BASE}/performance-consolidation/batch/analysis`, method: 'post', data: transData(data) })
}
export function exportPerformanceData(data) {
  return request({ url: `${BASE}/performance-consolidation/export-data`, method: 'post', data: transData(data), responseType: 'blob' })
}
export function getPerformanceStatistics(data) {
  return request({ url: `${BASE}/performance-consolidation/statistics`, method: 'post', data: transData(data) })
}
export function performPerformanceAnalysis(data) {
  return request({ url: `${BASE}/performance-consolidation/analysis`, method: 'post', data: transData(data) })
}
