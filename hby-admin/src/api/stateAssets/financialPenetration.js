import request from '@/utils/request'
import { transData } from '@/utils/requestData'

const BASE = '/monitor/v1/supervision/financial'

// ========== 财务报表 ==========
export function getStatementList(data) {
  return request({ url: `${BASE}/statement/list`, method: 'post', data: transData(data) })
}
export function getStatementDrillThrough(params) {
  return request({ url: `${BASE}/statement/drill-through`, method: 'get', params })
}
export function getSubjectDetail(params) {
  return request({ url: `${BASE}/statement/subject-detail`, method: 'get', params })
}

// ========== 财务指标 ==========
export function getFinancialIndicators(companyId, period) {
  return request({ url: `${BASE}/indicator/${companyId}`, method: 'get', params: { period } })
}
export function getBenchmarkList(data) {
  return request({ url: `${BASE}/benchmark/list`, method: 'post', data: transData(data) })
}
export function getIndicatorTrend(companyId, indicator) {
  return request({ url: `${BASE}/indicator/trend`, method: 'get', params: { companyId, indicator } })
}

// ========== 资金流向 ==========
export function getFundFlowList(params) {
  return request({ url: `${BASE}/fund-flow/list`, method: 'get', params })
}
export function getFundFlowStats(params) {
  return request({ url: `${BASE}/fund-flow/stats`, method: 'get', params })
}

// ========== 资金流向穿透 ==========
export function getFundFlowPenetrationList(params) {
  return request({ url: `${BASE}/fund-flow-penetration/list`, method: 'get', params })
}
export function getFundFlowPenetrationStats(companyId) {
  return request({ url: `${BASE}/fund-flow-penetration/stats`, method: 'get', params: { companyId } })
}
export function getFundFlowPenetrationDetail(id) {
  return request({ url: `${BASE}/fund-flow-penetration/detail/${id}`, method: 'get' })
}
export function submitFundFlowDispatch(data) {
  return request({ url: `${BASE}/fund-flow-penetration/dispatch`, method: 'post', data })
}
export function getFundFlowDispatchList(params) {
  return request({ url: `${BASE}/fund-flow-penetration/dispatch/list`, method: 'get', params })
}

// ========== 关联交易 ==========
export function getRelatedPartyList(data) {
  return request({ url: `${BASE}/related-party/list`, method: 'post', data: transData(data) })
}
export function saveRelatedParty(data) {
  return request({ url: `${BASE}/related-party/save`, method: 'post', data: transData(data) })
}
export function deleteRelatedParty(id) {
  return request({ url: `${BASE}/related-party/${id}`, method: 'delete' })
}
export function getRelatedPartyNetwork(params) {
  return request({ url: `${BASE}/related-party/network`, method: 'get', params })
}
export function getRelatedPartyDetail(id) {
  return request({ url: `${BASE}/related-party/detail/${id}`, method: 'get' })
}

// ========== 费用管控 ==========
export function getExpenseMonitorList(data) {
  return request({ url: `${BASE}/monitor/expense/list`, method: 'post', data: transData(data) })
}
export function getExpenseTrend(companyId) {
  return request({ url: `${BASE}/monitor/expense/trend`, method: 'get', params: { companyId } })
}
export function exportExpenseData(data) {
  return request({ url: `${BASE}/monitor/expense/export`, method: 'post', data: transData(data), responseType: 'blob' })
}

// ========== 财务监控 ==========
export function getDebtRatioMonitor(companyId) {
  return request({ url: `${BASE}/monitor/debt-ratio`, method: 'get', params: { companyId } })
}
export function getProfitQualityMonitor(companyId) {
  return request({ url: `${BASE}/monitor/profit-quality`, method: 'get', params: { companyId } })
}
export function getRelatedPartyMonitor(companyId) {
  return request({ url: `${BASE}/monitor/related-party`, method: 'get', params: { companyId } })
}

// ========== 异常检测 ==========
export function getAnomalyList(data) {
  return request({ url: `${BASE}/monitor/anomaly/list`, method: 'post', data: transData(data) })
}
export function getAnomalyStats() {
  return request({ url: `${BASE}/monitor/anomaly/stats`, method: 'get' })
}
export function getAnomalyTrend() {
  return request({ url: `${BASE}/monitor/anomaly/trend`, method: 'get' })
}
export function getAnomalyDetail(id) {
  return request({ url: `${BASE}/monitor/anomaly/detail/${id}`, method: 'get' })
}
export function submitAnomalyDispatch(data) {
  return request({ url: `${BASE}/monitor/anomaly/dispatch`, method: 'post', data: transData(data) })
}
export function detectBenford(companyId, period) {
  return request({ url: `${BASE}/monitor/anomaly/benford`, method: 'get', params: { companyId, period } })
}
export function detectProfitCashflow(companyId) {
  return request({ url: `${BASE}/monitor/anomaly/profit-cashflow`, method: 'get', params: { companyId } })
}
export function ignoreAnomaly(id) {
  return request({ url: `${BASE}/monitor/anomaly/ignore/${id}`, method: 'put' })
}
export function confirmAnomaly(id) {
  return request({ url: `${BASE}/monitor/anomaly/confirm/${id}`, method: 'put' })
}

// ========== 风险预警 ==========
export function getFinancialWarningList(data) {
  return request({ url: `${BASE}/warning/list`, method: 'post', data: transData(data) })
}
export function getFinancialWarningDetail(id) {
  return request({ url: `${BASE}/warning/detail/${id}`, method: 'get' })
}
export function handleFinancialWarning(data) {
  return request({ url: `${BASE}/warning/handle`, method: 'post', data: transData(data) })
}
export function closeFinancialWarning(id) {
  return request({ url: `${BASE}/warning/close/${id}`, method: 'put' })
}

// ========== 穿透分析 ==========
export function getFinancialDrillData(params) {
  return request({ url: `${BASE}/drill-down`, method: 'get', params })
}
export function exportFinancialDrillData(params) {
  return request({ url: `${BASE}/drill-down/export`, method: 'get', params, responseType: 'blob' })
}

// ========== 驾驶舱 ==========
export function getFinancialDashboard() {
  return request({ url: `${BASE}/dashboard`, method: 'get' })
}
export function getFinancialDashboardRadar() {
  return request({ url: `${BASE}/dashboard/radar`, method: 'get' })
}

// ========== 数据同步 ==========
export function syncFinancialData(companyId) {
  return request({ url: `${BASE}/sync`, method: 'post', data: transData({ companyId }) })
}
