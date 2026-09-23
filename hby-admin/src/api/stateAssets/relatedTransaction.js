import request from '@/utils/request'
import { transData } from '@/utils/requestData'

const BASE = '/monitor/v1/supervision/financial'

export function getRelatedTransactionList(data) {
  return request({ url: `${BASE}/related-transaction/list`, method: 'post', data: transData(data) })
}
export function getRelatedTransactionById(id) {
  return request({ url: `${BASE}/related-transaction/${id}`, method: 'get', params: transData() })
}
export function addRelatedTransaction(data) {
  return request({ url: `${BASE}/related-transaction/save`, method: 'post', data: transData(data) })
}
export function updateRelatedTransaction(data) {
  return request({ url: `${BASE}/related-transaction/save`, method: 'post', data: transData(data) })
}
export function deleteRelatedTransaction(id) {
  return request({ url: `${BASE}/related-transaction/${id}`, method: 'delete', params: transData() })
}
export function identifyRelatedTransactions(data) {
  return request({ url: `${BASE}/related-transaction/identify`, method: 'post', data: transData(data) })
}
export function analyzeRelatedTransactionNetwork(data) {
  return request({ url: `${BASE}/related-transaction/network`, method: 'post', data: transData(data) })
}
export function detectAbnormalRelatedTransactions(data) {
  return request({ url: `${BASE}/related-transaction/abnormal`, method: 'post', data: transData(data) })
}
export function analyzeRelatedTransactionPricing(data) {
  return request({ url: `${BASE}/related-transaction/pricing`, method: 'post', data: transData(data) })
}
export function assessRelatedTransactionFairness(data) {
  return request({ url: `${BASE}/related-transaction/fairness`, method: 'post', data: transData(data) })
}
export function analyzeRelatedTransactionFrequency(data) {
  return request({ url: `${BASE}/related-transaction/frequency`, method: 'post', data: transData(data) })
}
export function calculateRelatedTransactionScale(data) {
  return request({ url: `${BASE}/related-transaction/scale`, method: 'post', data: transData(data) })
}
export function analyzeRelatedTransactionTrend(data) {
  return request({ url: `${BASE}/related-transaction/trend`, method: 'post', data: transData(data) })
}
export function compareRelatedTransactions(data) {
  return request({ url: `${BASE}/related-transaction/compare`, method: 'post', data: transData(data) })
}
export function analyzeRelatedTransactionRisk(data) {
  return request({ url: `${BASE}/related-transaction/risk`, method: 'post', data: transData(data) })
}
export function assessRelatedTransactionImpact(data) {
  return request({ url: `${BASE}/related-transaction/impact`, method: 'post', data: transData(data) })
}
export function analyzeRelatedTransactionConcentration(data) {
  return request({ url: `${BASE}/related-transaction/concentration`, method: 'post', data: transData(data) })
}
export function monitorRelatedTransactionChanges(data) {
  return request({ url: `${BASE}/related-transaction/monitor`, method: 'post', data: transData(data) })
}
export function predictRelatedTransactionPattern(data) {
  return request({ url: `${BASE}/related-transaction/predict`, method: 'post', data: transData(data) })
}
export function analyzeRelatedTransactionCompliance(data) {
  return request({ url: `${BASE}/related-transaction/compliance`, method: 'post', data: transData(data) })
}
export function optimizeRelatedTransactionStructure(data) {
  return request({ url: `${BASE}/related-transaction/optimize`, method: 'post', data: transData(data) })
}
export function simulateRelatedTransactionChange(data) {
  return request({ url: `${BASE}/related-transaction/simulate`, method: 'post', data: transData(data) })
}
export function analyzeRelatedTransactionStability(data) {
  return request({ url: `${BASE}/related-transaction/stability`, method: 'post', data: transData(data) })
}
export function getRelatedTransactionStatistics(data) {
  return request({ url: `${BASE}/related-transaction/statistics`, method: 'post', data: transData(data) })
}
export function generateRelatedTransactionReport(data) {
  return request({ url: `${BASE}/related-transaction/report`, method: 'post', data: transData(data) })
}
export function batchIdentifyRelatedTransactions(data) {
  return request({ url: `${BASE}/related-transaction/batch/identify`, method: 'post', data: transData(data) })
}
export function batchDeleteRelatedTransactions(data) {
  return request({ url: `${BASE}/related-transaction/batch/delete`, method: 'post', data: transData(data) })
}
export function exportRelatedTransactionData(data) {
  return request({ url: `${BASE}/related-transaction/export`, method: 'post', data: transData(data) })
}
export function importRelatedTransactionData(data) {
  return request({ url: `${BASE}/related-transaction/import`, method: 'post', data: transData(data) })
}
export function getRelatedTransactionChartData(data) {
  return request({ url: `${BASE}/related-transaction/chart`, method: 'post', data: transData(data) })
}
export function analyzeRelatedTransactionSensitivity(data) {
  return request({ url: `${BASE}/related-transaction/sensitivity`, method: 'post', data: transData(data) })
}
