import request from '@/utils/request'
import { transData } from '@/utils/requestData'

const BASE = '/monitor/v1/supervision/financial'

export function getFundFlowList(params) {
  return request({ url: `${BASE}/fund-flow/list`, method: 'get', params })
}
export function getFundFlowById(id) {
  return request({ url: `${BASE}/fund-flow/${id}`, method: 'get', params: transData() })
}
export function addFundFlow(data) {
  return request({ url: `${BASE}/fund-flow/save`, method: 'post', data: transData(data) })
}
export function updateFundFlow(data) {
  return request({ url: `${BASE}/fund-flow/save`, method: 'post', data: transData(data) })
}
export function deleteFundFlow(id) {
  return request({ url: `${BASE}/fund-flow/${id}`, method: 'delete', params: transData() })
}
export function traceFundFlow(data) {
  return request({ url: `${BASE}/fund-flow/trace`, method: 'post', data: transData(data) })
}
export function analyzeFundFlowPath(data) {
  return request({ url: `${BASE}/fund-flow/path`, method: 'post', data: transData(data) })
}
export function buildFundFlowChart(data) {
  return request({ url: `${BASE}/fund-flow/chart`, method: 'post', data: transData(data) })
}
export function analyzeFundFlowPattern(data) {
  return request({ url: `${BASE}/fund-flow/pattern`, method: 'post', data: transData(data) })
}
export function detectAbnormalFundFlow(data) {
  return request({ url: `${BASE}/fund-flow/abnormal`, method: 'post', data: transData(data) })
}
export function analyzeFundFlowVelocity(data) {
  return request({ url: `${BASE}/fund-flow/velocity`, method: 'post', data: transData(data) })
}
export function calculateFundFlowIntensity(data) {
  return request({ url: `${BASE}/fund-flow/intensity`, method: 'post', data: transData(data) })
}
export function analyzeFundFlowTrend(data) {
  return request({ url: `${BASE}/fund-flow/trend`, method: 'post', data: transData(data) })
}
export function predictFundFlow(data) {
  return request({ url: `${BASE}/fund-flow/predict`, method: 'post', data: transData(data) })
}
export function analyzeFundFlowConcentration(data) {
  return request({ url: `${BASE}/fund-flow/concentration`, method: 'post', data: transData(data) })
}
export function identifyFundFlowKeyNodes(data) {
  return request({ url: `${BASE}/fund-flow/key-nodes`, method: 'post', data: transData(data) })
}
export function analyzeFundFlowNetwork(data) {
  return request({ url: `${BASE}/fund-flow/network`, method: 'post', data: transData(data) })
}
export function assessFundFlowRisk(data) {
  return request({ url: `${BASE}/fund-flow/risk`, method: 'post', data: transData(data) })
}
export function monitorFundFlowRealtime(data) {
  return request({ url: `${BASE}/fund-flow/realtime`, method: 'post', data: transData(data) })
}
export function analyzeFundFlowCyclicity(data) {
  return request({ url: `${BASE}/fund-flow/cyclicity`, method: 'post', data: transData(data) })
}
export function compareFundFlow(data) {
  return request({ url: `${BASE}/fund-flow/compare`, method: 'post', data: transData(data) })
}
export function analyzeFundFlowFactors(data) {
  return request({ url: `${BASE}/fund-flow/factors`, method: 'post', data: transData(data) })
}
export function optimizeFundFlowConfiguration(data) {
  return request({ url: `${BASE}/fund-flow/optimize`, method: 'post', data: transData(data) })
}
export function simulateFundFlowChange(data) {
  return request({ url: `${BASE}/fund-flow/simulate`, method: 'post', data: transData(data) })
}
export function analyzeFundFlowStability(data) {
  return request({ url: `${BASE}/fund-flow/stability`, method: 'post', data: transData(data) })
}
export function analyzeFundFlowCompliance(data) {
  return request({ url: `${BASE}/fund-flow/compliance`, method: 'post', data: transData(data) })
}
export function getFundFlowStatistics(params) {
  return request({ url: `${BASE}/fund-flow/stats`, method: 'get', params })
}
export function generateFundFlowReport(data) {
  return request({ url: `${BASE}/fund-flow/report`, method: 'post', data: transData(data) })
}
export function batchTraceFundFlow(data) {
  return request({ url: `${BASE}/fund-flow/batch/trace`, method: 'post', data: transData(data) })
}
export function batchDeleteFundFlow(data) {
  return request({ url: `${BASE}/fund-flow/batch/delete`, method: 'post', data: transData(data) })
}
export function exportFundFlowData(data) {
  return request({ url: `${BASE}/fund-flow/export`, method: 'post', data: transData(data) })
}
export function importFundFlowData(data) {
  return request({ url: `${BASE}/fund-flow/import`, method: 'post', data: transData(data) })
}
export function setFundFlowAlert(data) {
  return request({ url: `${BASE}/fund-flow/alert`, method: 'post', data: transData(data) })
}
export function analyzeFundFlowSensitivity(data) {
  return request({ url: `${BASE}/fund-flow/sensitivity`, method: 'post', data: transData(data) })
}
export function analyzeFundFlow(data) {
  return request({ url: `${BASE}/fund-flow/analyze`, method: 'post', data: transData(data) })
}
export function getFundFlowAlertList() {
  return request({ url: `${BASE}/fund-flow/alert/list`, method: 'get', params: transData() })
}
export function deleteFundFlowAlert(id) {
  return request({ url: `${BASE}/fund-flow/alert/${id}`, method: 'delete', params: transData() })
}
export function updateMonitorRule(data) {
  return request({ url: `${BASE}/fund-flow/monitor-rule/update`, method: 'post', data: transData(data) })
}
export function deleteMonitorRule(id) {
  return request({ url: `${BASE}/fund-flow/monitor-rule/${id}`, method: 'delete', params: transData() })
}
