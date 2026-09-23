import request from '@/utils/request'
import { transData } from '@/utils/requestData'

const BASE = '/monitor/v1/supervision/financial'

export function getFinancialAnalysisStatistics(data) {
  return request({ url: `${BASE}/analysis/statistics`, method: 'post', data: transData(data) })
}
export function getFinancialAnalysisList(data) {
  return request({ url: `${BASE}/analysis/list`, method: 'post', data: transData(data) })
}
export function addFinancialAnalysis(data) {
  return request({ url: `${BASE}/analysis/save`, method: 'post', data: transData(data) })
}
export function updateFinancialAnalysis(data) {
  return request({ url: `${BASE}/analysis/save`, method: 'post', data: transData(data) })
}
export function deleteFinancialAnalysis(data) {
  return request({ url: `${BASE}/analysis/delete`, method: 'post', data: transData(data) })
}
export function getFinancialAnalysisDetail(data) {
  return request({ url: `${BASE}/analysis/detail`, method: 'post', data: transData(data) })
}
export function getFinancialAnalysisCharts(data) {
  return request({ url: `${BASE}/analysis/charts`, method: 'post', data: transData(data) })
}
export function getFinancialDataPenetrationAnalysis(data) {
  return request({ url: `${BASE}/analysis/data-penetration`, method: 'post', data: transData(data) })
}
export function getFinancialStatementPenetrationAnalysis(data) {
  return request({ url: `${BASE}/analysis/statement-penetration`, method: 'post', data: transData(data) })
}
export function getFinancialIndicatorPenetrationAnalysis(data) {
  return request({ url: `${BASE}/analysis/indicator-penetration`, method: 'post', data: transData(data) })
}
export function getProfitabilityAnalysis(data) {
  return request({ url: `${BASE}/analysis/profitability`, method: 'post', data: transData(data) })
}
export function getSolvencyAnalysis(data) {
  return request({ url: `${BASE}/analysis/solvency`, method: 'post', data: transData(data) })
}
export function getOperatingCapabilityAnalysis(data) {
  return request({ url: `${BASE}/analysis/operating-capability`, method: 'post', data: transData(data) })
}
export function getDevelopmentCapabilityAnalysis(data) {
  return request({ url: `${BASE}/analysis/development-capability`, method: 'post', data: transData(data) })
}
export function getCashFlowAnalysis(data) {
  return request({ url: `${BASE}/analysis/cash-flow`, method: 'post', data: transData(data) })
}
export function getAssetQualityAnalysis(data) {
  return request({ url: `${BASE}/analysis/asset-quality`, method: 'post', data: transData(data) })
}
export function getFinancialStructureAnalysis(data) {
  return request({ url: `${BASE}/analysis/financial-structure`, method: 'post', data: transData(data) })
}
export function getFinancialTrendAnalysis(data) {
  return request({ url: `${BASE}/analysis/trend-analysis`, method: 'post', data: transData(data) })
}
export function getFinancialComparisonAnalysis(data) {
  return request({ url: `${BASE}/analysis/comparison-analysis`, method: 'post', data: transData(data) })
}
export function getIndustryComparisonAnalysis(data) {
  return request({ url: `${BASE}/analysis/industry-comparison`, method: 'post', data: transData(data) })
}
export function getFinancialForecastAnalysis(data) {
  return request({ url: `${BASE}/analysis/forecast-analysis`, method: 'post', data: transData(data) })
}
export function getFinancialAnomalyDetection(data) {
  return request({ url: `${BASE}/analysis/anomaly-detection`, method: 'post', data: transData(data) })
}
export function getFinancialFraudDetection(data) {
  return request({ url: `${BASE}/analysis/fraud-detection`, method: 'post', data: transData(data) })
}
export function getFinancialHealthAssessment(data) {
  return request({ url: `${BASE}/analysis/health-assessment`, method: 'post', data: transData(data) })
}
export function getFinancialRatingAnalysis(data) {
  return request({ url: `${BASE}/analysis/rating-analysis`, method: 'post', data: transData(data) })
}
export function setFinancialAnalysisAlerts(data) {
  return request({ url: `${BASE}/analysis/alerts`, method: 'post', data: transData(data) })
}
export function getFinancialAnalysisAlerts(data) {
  return request({ url: `${BASE}/analysis/alerts/list`, method: 'post', data: transData(data) })
}
export function getFinancialAnalysisRecommendations(data) {
  return request({ url: `${BASE}/analysis/recommendations`, method: 'post', data: transData(data) })
}
export function batchAnalyzeFinancialData(data) {
  return request({ url: `${BASE}/analysis/batch-analyze`, method: 'post', data: transData(data) })
}
export function batchCompareFinancialData(data) {
  return request({ url: `${BASE}/analysis/batch-compare`, method: 'post', data: transData(data) })
}
export function batchDeleteFinancialAnalysis(data) {
  return request({ url: `${BASE}/analysis/batch-delete`, method: 'post', data: transData(data) })
}
export function exportFinancialAnalysisData(data) {
  return request({ url: `${BASE}/analysis/export`, method: 'post', data: transData(data), responseType: 'blob' })
}
export function importFinancialAnalysisData(data) {
  return request({ url: `${BASE}/analysis/import`, method: 'post', data: transData(data) })
}
export function generateFinancialAnalysisReport(data) {
  return request({ url: `${BASE}/analysis/generate-report`, method: 'post', data: transData(data), responseType: 'blob' })
}
export function getFinancialSupervisionSuggestions(data) {
  return request({ url: `${BASE}/analysis/supervision-suggestions`, method: 'post', data: transData(data) })
}
export function performFinancialStressTest(data) {
  return request({ url: `${BASE}/analysis/stress-test`, method: 'post', data: transData(data) })
}
export function performFinancialScenarioAnalysis(data) {
  return request({ url: `${BASE}/analysis/scenario-analysis`, method: 'post', data: transData(data) })
}
export function getFinancialPerformanceEvaluation(data) {
  return request({ url: `${BASE}/analysis/performance-evaluation`, method: 'post', data: transData(data) })
}
export function getFinancialAnalysisVersionHistory(data) {
  return request({ url: `${BASE}/analysis/version-history`, method: 'post', data: transData(data) })
}
export function restoreFinancialAnalysisVersion(data) {
  return request({ url: `${BASE}/analysis/restore-version`, method: 'post', data: transData(data) })
}
