import request from '@/utils/request'
import { transData } from '@/utils/requestData'

const BASE = '/monitor/v1/supervision/financial'

export function getFinancialRiskStatistics(data) {
  return request({ url: `${BASE}/risk/statistics`, method: 'post', data: transData(data) })
}
export function getFinancialRiskList(data) {
  return request({ url: `${BASE}/risk/list`, method: 'post', data: transData(data) })
}
export function addFinancialRisk(data) {
  return request({ url: `${BASE}/risk/save`, method: 'post', data: transData(data) })
}
export function updateFinancialRisk(data) {
  return request({ url: `${BASE}/risk/save`, method: 'post', data: transData(data) })
}
export function deleteFinancialRisk(data) {
  return request({ url: `${BASE}/risk/delete`, method: 'post', data: transData(data) })
}
export function getFinancialRiskDetail(data) {
  return request({ url: `${BASE}/risk/detail`, method: 'post', data: transData(data) })
}
export function getFinancialRiskCharts(data) {
  return request({ url: `${BASE}/risk/charts`, method: 'post', data: transData(data) })
}
export function identifyFinancialRisk(data) {
  return request({ url: `${BASE}/risk/identify`, method: 'post', data: transData(data) })
}
export function identifyLiquidityRisk(data) {
  return request({ url: `${BASE}/risk/liquidity-risk`, method: 'post', data: transData(data) })
}
export function identifySolvencyRisk(data) {
  return request({ url: `${BASE}/risk/solvency-risk`, method: 'post', data: transData(data) })
}
export function identifyProfitabilityRisk(data) {
  return request({ url: `${BASE}/risk/profitability-risk`, method: 'post', data: transData(data) })
}
export function identifyOperationalRisk(data) {
  return request({ url: `${BASE}/risk/operational-risk`, method: 'post', data: transData(data) })
}
export function identifyMarketRisk(data) {
  return request({ url: `${BASE}/risk/market-risk`, method: 'post', data: transData(data) })
}
export function identifyCreditRisk(data) {
  return request({ url: `${BASE}/risk/credit-risk`, method: 'post', data: transData(data) })
}
export function assessFinancialRisk(data) {
  return request({ url: `${BASE}/risk/assess`, method: 'post', data: transData(data) })
}
export function evaluateRiskLevel(data) {
  return request({ url: `${BASE}/risk/evaluate-level`, method: 'post', data: transData(data) })
}
export function calculateRiskScore(data) {
  return request({ url: `${BASE}/risk/calculate-score`, method: 'post', data: transData(data) })
}
export function analyzeRiskImpact(data) {
  return request({ url: `${BASE}/risk/impact-analysis`, method: 'post', data: transData(data) })
}
export function analyzeRiskProbability(data) {
  return request({ url: `${BASE}/risk/probability-analysis`, method: 'post', data: transData(data) })
}
export function setFinancialRiskAlerts(data) {
  return request({ url: `${BASE}/risk/alerts`, method: 'post', data: transData(data) })
}
export function getFinancialRiskAlerts(data) {
  return request({ url: `${BASE}/risk/alerts/list`, method: 'post', data: transData(data) })
}
export function getFinancialRiskWarningMechanism(data) {
  return request({ url: `${BASE}/risk/warning-mechanism`, method: 'post', data: transData(data) })
}
export function setRiskWarningThresholds(data) {
  return request({ url: `${BASE}/risk/warning-thresholds`, method: 'post', data: transData(data) })
}
export function sendRiskWarningNotifications(data) {
  return request({ url: `${BASE}/risk/warning-notifications`, method: 'post', data: transData(data) })
}
export function predictFinancialRisk(data) {
  return request({ url: `${BASE}/risk/predict`, method: 'post', data: transData(data) })
}
export function predictRiskTrend(data) {
  return request({ url: `${BASE}/risk/trend-prediction`, method: 'post', data: transData(data) })
}
export function performRiskScenarioAnalysis(data) {
  return request({ url: `${BASE}/risk/scenario-analysis`, method: 'post', data: transData(data) })
}
export function monitorFinancialRisk(data) {
  return request({ url: `${BASE}/risk/monitor`, method: 'post', data: transData(data) })
}
export function getRealTimeRiskMonitoring(data) {
  return request({ url: `${BASE}/risk/real-time-monitoring`, method: 'post', data: transData(data) })
}
export function getRiskMonitoringDashboard(data) {
  return request({ url: `${BASE}/risk/monitoring-dashboard`, method: 'post', data: transData(data) })
}
export function mitigateFinancialRisk(data) {
  return request({ url: `${BASE}/risk/mitigate`, method: 'post', data: transData(data) })
}
export function getRiskMitigationStrategies(data) {
  return request({ url: `${BASE}/risk/mitigation-strategies`, method: 'post', data: transData(data) })
}
export function getRiskControlMeasures(data) {
  return request({ url: `${BASE}/risk/control-measures`, method: 'post', data: transData(data) })
}
export function batchIdentifyFinancialRisk(data) {
  return request({ url: `${BASE}/risk/batch-identify`, method: 'post', data: transData(data) })
}
export function batchAssessFinancialRisk(data) {
  return request({ url: `${BASE}/risk/batch-assess`, method: 'post', data: transData(data) })
}
export function batchDeleteFinancialRisk(data) {
  return request({ url: `${BASE}/risk/batch-delete`, method: 'post', data: transData(data) })
}
export function exportFinancialRiskData(data) {
  return request({ url: `${BASE}/risk/export`, method: 'post', data: transData(data), responseType: 'blob' })
}
export function importFinancialRiskData(data) {
  return request({ url: `${BASE}/risk/import`, method: 'post', data: transData(data) })
}
export function generateFinancialRiskReport(data) {
  return request({ url: `${BASE}/risk/generate-report`, method: 'post', data: transData(data) })
}
export function getFinancialRiskSupervisionSuggestions(data) {
  return request({ url: `${BASE}/risk/supervision-suggestions`, method: 'post', data: transData(data) })
}
export function performFinancialRiskStressTest(data) {
  return request({ url: `${BASE}/risk/stress-test`, method: 'post', data: transData(data) })
}
export function getFinancialRiskPerformanceEvaluation(data) {
  return request({ url: `${BASE}/risk/performance-evaluation`, method: 'post', data: transData(data) })
}
export function getFinancialRiskVersionHistory(data) {
  return request({ url: `${BASE}/risk/version-history`, method: 'post', data: transData(data) })
}
export function restoreFinancialRiskVersion(data) {
  return request({ url: `${BASE}/risk/restore-version`, method: 'post', data: transData(data) })
}
