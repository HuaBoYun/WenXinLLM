import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// ========== 境外单位台账 ==========
export function getOverseasUnitList(data) {
  return request({
    url: '/monitor/v1/supervision/overseas/unit/list',
    method: 'post',
    data: transData(data),
  })
}

export function getOverseasUnitDetail(id) {
  return request({
    url: '/monitor/v1/supervision/overseas/unit/' + id,
    method: 'get',
  })
}

export function addOverseasUnit(data) {
  return request({
    url: '/monitor/v1/supervision/overseas/unit/add',
    method: 'post',
    data: transData(data),
  })
}

export function updateOverseasUnit(data) {
  return request({
    url: '/monitor/v1/supervision/overseas/unit/update',
    method: 'post',
    data: transData(data),
  })
}

export function deleteOverseasUnit(id) {
  return request({
    url: '/monitor/v1/supervision/overseas/unit/' + id,
    method: 'delete',
  })
}

export function batchDeleteOverseasUnit(data) {
  return request({
    url: '/monitor/v1/supervision/overseas/unit/batch/delete',
    method: 'post',
    data: transData(data),
  })
}

export function getOverseasStatistics(companyId) {
  return request({
    url: '/monitor/v1/supervision/overseas/unit/statistics',
    method: 'get',
    params: { companyId },
  })
}

// ========== 境外投资管理 ==========
export function getOverseasInvestList(data) {
  return request({ url: '/monitor/v1/supervision/overseas/invest/list', method: 'post', data: transData(data) })
}
export function getOverseasInvestDetail(id) {
  return request({ url: '/monitor/v1/supervision/overseas/invest/' + id, method: 'get' })
}
export function getOverseasInvestStats() {
  return request({ url: '/monitor/v1/supervision/overseas/invest/stats', method: 'get' })
}
export function addOverseasInvest(data) {
  return request({ url: '/monitor/v1/supervision/overseas/invest/add', method: 'post', data: transData(data) })
}
export function updateOverseasInvest(data) {
  return request({ url: '/monitor/v1/supervision/overseas/invest/update', method: 'post', data: transData(data) })
}
export function deleteOverseasInvest(id) {
  return request({ url: '/monitor/v1/supervision/overseas/invest/' + id, method: 'delete' })
}
export function batchDeleteOverseasInvest(data) {
  return request({ url: '/monitor/v1/supervision/overseas/invest/batch/delete', method: 'post', data: transData(data) })
}
export function getOverseasInvestApproval(id) {
  return request({ url: '/monitor/v1/supervision/overseas/invest/approval/' + id, method: 'get' })
}

// ========== 境外经营分析 ==========
export function getOverseasOperationStats() {
  return request({ url: '/monitor/v1/supervision/overseas/operation/stats', method: 'get' })
}
export function getOverseasOperationList(data) {
  return request({ url: '/monitor/v1/supervision/overseas/operation/list', method: 'post', data: transData(data) })
}

// ========== 国别风险 ==========
export function getCountryRiskList(data) {
  return request({ url: '/monitor/v1/supervision/overseas/country/risk/list', method: 'post', data: transData(data) })
}
export function getCountryRiskStats() {
  return request({ url: '/monitor/v1/supervision/overseas/country/risk/stats', method: 'get' })
}

// ========== 外汇风险 ==========
export function getOverseasForexList(data) {
  return request({ url: '/monitor/v1/supervision/overseas/forex/list', method: 'post', data: transData(data) })
}
export function getOverseasForexStats() {
  return request({ url: '/monitor/v1/supervision/overseas/forex/stats', method: 'get' })
}

// ========== 境外合规管理 ==========
export function getOverseasComplianceList(data) {
  return request({ url: '/monitor/v1/supervision/overseas/compliance/list', method: 'post', data: transData(data) })
}
export function addOverseasCompliance(data) {
  return request({ url: '/monitor/v1/supervision/overseas/compliance/add', method: 'post', data: transData(data) })
}
export function updateOverseasCompliance(data) {
  return request({ url: '/monitor/v1/supervision/overseas/compliance/update', method: 'post', data: transData(data) })
}
export function deleteOverseasCompliance(id) {
  return request({ url: '/monitor/v1/supervision/overseas/compliance/' + id, method: 'delete' })
}
export function getOverseasViolationList(data) {
  return request({ url: '/monitor/v1/supervision/overseas/compliance/violation/list', method: 'post', data: transData(data) })
}

// ========== 境外人员安全 ==========
export function getOverseasPersonnelList(data) {
  return request({ url: '/monitor/v1/supervision/overseas/personnel/list', method: 'post', data: transData(data) })
}
export function addOverseasPersonnel(data) {
  return request({ url: '/monitor/v1/supervision/overseas/personnel/add', method: 'post', data: transData(data) })
}
export function updateOverseasPersonnel(data) {
  return request({ url: '/monitor/v1/supervision/overseas/personnel/update', method: 'post', data: transData(data) })
}
export function deleteOverseasPersonnel(id) {
  return request({ url: '/monitor/v1/supervision/overseas/personnel/' + id, method: 'delete' })
}
export function getOverseasPersonnelStats() {
  return request({ url: '/monitor/v1/supervision/overseas/personnel/stats', method: 'get' })
}

// ========== 境外应急事件 ==========
export function getOverseasEmergencyList(data) {
  return request({ url: '/monitor/v1/supervision/overseas/emergency/list', method: 'post', data: transData(data) })
}
export function addOverseasEmergency(data) {
  return request({ url: '/monitor/v1/supervision/overseas/emergency/add', method: 'post', data: transData(data) })
}
export function updateOverseasEmergency(data) {
  return request({ url: '/monitor/v1/supervision/overseas/emergency/update', method: 'post', data: transData(data) })
}
export function deleteOverseasEmergency(id) {
  return request({ url: '/monitor/v1/supervision/overseas/emergency/' + id, method: 'delete' })
}

// ========== 境外应急预案 ==========
export function getEmergencyPlanList(data) {
  return request({ url: '/monitor/v1/supervision/overseas/emergency/plan/list', method: 'post', data: transData(data) })
}
export function addEmergencyPlan(data) {
  return request({ url: '/monitor/v1/supervision/overseas/emergency/plan/add', method: 'post', data: transData(data) })
}
export function updateEmergencyPlan(data) {
  return request({ url: '/monitor/v1/supervision/overseas/emergency/plan/update', method: 'post', data: transData(data) })
}
export function deleteEmergencyPlan(id) {
  return request({ url: '/monitor/v1/supervision/overseas/emergency/plan/' + id, method: 'delete' })
}

// ========== 境外风险预警 ==========
export function getOverseasWarningList(data) {
  return request({ url: '/monitor/v1/supervision/overseas/warning/list', method: 'post', data: transData(data) })
}
export function handleOverseasWarning(data) {
  return request({ url: '/monitor/v1/supervision/overseas/warning/handle', method: 'post', data: transData(data) })
}

// ========== 境外负责人管理 ==========
export function getOverseasLeaderList(data) {
  return request({ url: '/monitor/v1/supervision/overseas/leader/list', method: 'post', data: transData(data) })
}
export function addOverseasLeader(data) {
  return request({ url: '/monitor/v1/supervision/overseas/leader/add', method: 'post', data: transData(data) })
}
export function updateOverseasLeader(data) {
  return request({ url: '/monitor/v1/supervision/overseas/leader/update', method: 'post', data: transData(data) })
}
export function deleteOverseasLeader(id) {
  return request({ url: '/monitor/v1/supervision/overseas/leader/' + id, method: 'delete' })
}

export function saveOverseasCompliance(data) {
  return data.complianceId ? updateOverseasCompliance(data) : addOverseasCompliance(data)
}

export function saveOverseasLeader(data) {
  return data.leaderId ? updateOverseasLeader(data) : addOverseasLeader(data)
}

// ========== 境外首页KPI ==========
export function getOverseasKPI() {
  return request({ url: '/monitor/v1/supervision/overseas/kpi', method: 'get' })
}

// ========== 境外驾驶舱 ==========
export function getOverseasDashboard() {
  return request({ url: '/monitor/v1/supervision/overseas/dashboard', method: 'get' })
}
export function getOverseasDashboardTrend() {
  return request({ url: '/monitor/v1/supervision/overseas/dashboard/trend', method: 'get' })
}
export function getOverseasDashboardCountry() {
  return request({ url: '/monitor/v1/supervision/overseas/dashboard/country', method: 'get' })
}
export function getOverseasDashboardForex() {
  return request({ url: '/monitor/v1/supervision/overseas/dashboard/forex', method: 'get' })
}
export function getOverseasDashboardRisk() {
  return request({ url: '/monitor/v1/supervision/overseas/dashboard/risk', method: 'get' })
}

