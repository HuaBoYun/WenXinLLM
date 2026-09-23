import request from '@/utils/request'

const BASE = '/monitor/v1/supervision/industry'
const JSON_HEADER = { 'Content-Type': 'application/json' }

// ========== 行业布局管理 ==========
export function getIndustryLayoutList(data) {
  return request({ url: `${BASE}/layout/list`, method: 'post', data, headers: JSON_HEADER })
}
export function getIndustryLayoutDetail(id) {
  return request({ url: `${BASE}/layout/detail/${id}`, method: 'get' })
}
export function addIndustryLayout(data) {
  return request({ url: `${BASE}/layout/add`, method: 'post', data, headers: JSON_HEADER })
}
export function updateIndustryLayout(data) {
  return request({ url: `${BASE}/layout/update`, method: 'post', data, headers: JSON_HEADER })
}
export function deleteIndustryLayout(id) {
  return request({ url: `${BASE}/layout/${id}`, method: 'delete' })
}
export function saveIndustryLayout(data) {
  return data.id ? updateIndustryLayout(data) : addIndustryLayout(data)
}
export function updateMainBiz(data) {
  return request({ url: `${BASE}/layout/updateMainBiz`, method: 'post', data, headers: JSON_HEADER })
}
export function updateKeyMonitor(data) {
  return request({ url: `${BASE}/layout/updateKeyMonitor`, method: 'post', data, headers: JSON_HEADER })
}

// ========== 行业统计 ==========
export function getIndustryOverview(companyId) {
  return request({ url: `${BASE}/statistics/overview`, method: 'get', params: { companyId } })
}
export function getIndustryDistribution() {
  return request({ url: `${BASE}/statistics/distribution`, method: 'get' })
}
export function getIndustryRevenueTrend() {
  return request({ url: `${BASE}/statistics/revenue-trend`, method: 'get' })
}
export function getIndustryRiskMatrix() {
  return request({ url: `${BASE}/statistics/risk-matrix`, method: 'get' })
}

// ========== 五大行业专项 ==========
export function getEnergyList(data) {
  return request({ url: `${BASE}/energy/list`, method: 'post', data, headers: JSON_HEADER })
}
export function addEnergyMonitor(data) {
  return request({ url: `${BASE}/energy/add`, method: 'post', data, headers: JSON_HEADER })
}
export function getEnergyDetail(id) {
  return request({ url: `${BASE}/energy/detail/${id}`, method: 'get' })
}
export function updateEnergyMonitor(data) {
  return request({ url: `${BASE}/energy/update`, method: 'post', data, headers: JSON_HEADER })
}
export function deleteEnergyMonitor(id) {
  return request({ url: `${BASE}/energy/${id}`, method: 'delete' })
}
export function exportEnergyData(data) {
  return request({ url: `${BASE}/energy/export`, method: 'post', data, headers: JSON_HEADER })
}
export function getEnergyTransitionStats() {
  return request({ url: `${BASE}/energy/transition-stats`, method: 'get' })
}
export function getFinancialList(data) {
  return request({ url: `${BASE}/financial/list`, method: 'post', data, headers: JSON_HEADER })
}
export function addFinancialMonitor(data) {
  return request({ url: `${BASE}/financial/add`, method: 'post', data, headers: JSON_HEADER })
}
export function getFinancialDetail(id) {
  return request({ url: `${BASE}/financial/detail/${id}`, method: 'get' })
}
export function updateFinancialMonitor(data) {
  return request({ url: `${BASE}/financial/update`, method: 'post', data, headers: JSON_HEADER })
}
export function deleteFinancialMonitor(id) {
  return request({ url: `${BASE}/financial/${id}`, method: 'delete' })
}
export function exportFinancialData(data) {
  return request({ url: `${BASE}/financial/export`, method: 'post', data, headers: JSON_HEADER })
}
export function getFinancialRiskIndicators() {
  return request({ url: `${BASE}/financial/risk-indicators`, method: 'get' })
}
export function getManufacturingList(data) {
  return request({ url: `${BASE}/manufacturing/list`, method: 'post', data, headers: JSON_HEADER })
}
export function addManufacturingMonitor(data) {
  return request({ url: `${BASE}/manufacturing/add`, method: 'post', data, headers: JSON_HEADER })
}
export function getManufacturingDetail(id) {
  return request({ url: `${BASE}/manufacturing/detail/${id}`, method: 'get' })
}
export function updateManufacturingMonitor(data) {
  return request({ url: `${BASE}/manufacturing/update`, method: 'post', data, headers: JSON_HEADER })
}
export function deleteManufacturingMonitor(id) {
  return request({ url: `${BASE}/manufacturing/${id}`, method: 'delete' })
}
export function exportManufacturingData(data) {
  return request({ url: `${BASE}/manufacturing/export`, method: 'post', data, headers: JSON_HEADER })
}
export function getManufacturingRdStats() {
  return request({ url: `${BASE}/manufacturing/rd-stats`, method: 'get' })
}
export function getInfrastructureList(data) {
  return request({ url: `${BASE}/infrastructure/list`, method: 'post', data, headers: JSON_HEADER })
}
export function addInfrastructureMonitor(data) {
  return request({ url: `${BASE}/infrastructure/add`, method: 'post', data, headers: JSON_HEADER })
}
export function getInfrastructureDetail(id) {
  return request({ url: `${BASE}/infrastructure/detail/${id}`, method: 'get' })
}
export function updateInfrastructureMonitor(data) {
  return request({ url: `${BASE}/infrastructure/update`, method: 'post', data, headers: JSON_HEADER })
}
export function deleteInfrastructureMonitor(id) {
  return request({ url: `${BASE}/infrastructure/${id}`, method: 'delete' })
}
export function exportInfrastructureData(data) {
  return request({ url: `${BASE}/infrastructure/export`, method: 'post', data, headers: JSON_HEADER })
}
export function getPublicServiceList(data) {
  return request({ url: `${BASE}/publicService/list`, method: 'post', data, headers: JSON_HEADER })
}
export function addPublicServiceMonitor(data) {
  return request({ url: `${BASE}/publicService/add`, method: 'post', data, headers: JSON_HEADER })
}
export function getPublicServiceDetail(id) {
  return request({ url: `${BASE}/publicService/detail/${id}`, method: 'get' })
}
export function updatePublicServiceMonitor(data) {
  return request({ url: `${BASE}/publicService/update`, method: 'post', data, headers: JSON_HEADER })
}
export function deletePublicServiceMonitor(id) {
  return request({ url: `${BASE}/publicService/${id}`, method: 'delete' })
}
export function exportPublicServiceData(data) {
  return request({ url: `${BASE}/publicService/export`, method: 'post', data, headers: JSON_HEADER })
}

// ========== 竞争力分析 ==========
export function getCompetitivenessList(data) {
  return request({ url: `${BASE}/competitiveness/list`, method: 'post', data, headers: JSON_HEADER })
}
export function getCompetitivenessRanking() {
  return request({ url: `${BASE}/competitiveness/ranking`, method: 'get' })
}

// ========== 产业协同 ==========
export function getSynergyList() {
  return request({ url: `${BASE}/synergy/list`, method: 'get' })
}
export function getSynergyMatrix() {
  return request({ url: `${BASE}/synergy/matrix`, method: 'get' })
}

// ========== 驾驶舱 ==========
export function getIndustryDashboard() {
  return request({ url: `${BASE}/dashboard/overview`, method: 'get' })
}

// ========== 风险预警 ==========
export function getIndustryWarningList(data) {
  return request({ url: `${BASE}/warning/list`, method: 'post', data, headers: JSON_HEADER })
}
export function handleIndustryWarning(data) {
  return request({ url: `${BASE}/warning/handle`, method: 'post', data, headers: JSON_HEADER })
}
export function batchHandleIndustryWarning(data) {
  return request({ url: `${BASE}/warning/batchHandle`, method: 'post', data, headers: JSON_HEADER })
}
export function exportIndustryWarning(data) {
  return request({ url: `${BASE}/warning/export`, method: 'post', data, headers: JSON_HEADER, responseType: 'blob' })
}

// ========== 首页KPI ==========
export function getIndustryKPI() {
  return request({ url: `${BASE}/kpi`, method: 'get' })
}

// ========== 驾驶舱图表 ==========
export function getIndustryDashboardTrend() {
  return request({ url: `${BASE}/dashboard/trend`, method: 'get' })
}
export function getIndustryDashboardType() {
  return request({ url: `${BASE}/dashboard/type`, method: 'get' })
}
export function getIndustryDashboardGauge() {
  return request({ url: `${BASE}/dashboard/gauge`, method: 'get' })
}
export function getIndustryDashboardMainBiz() {
  return request({ url: `${BASE}/dashboard/mainBiz`, method: 'get' })
}

// ========== 基础设施统计 ==========
export function getInfrastructureStats() {
  return request({ url: `${BASE}/infrastructure/stats`, method: 'get' })
}

// ========== 公共服务统计 ==========
export function getPublicServiceStats() {
  return request({ url: `${BASE}/publicService/stats`, method: 'get' })
}

// ========== 穿透分析 ==========
export function getIndustryDrillData(params) {
  return request({ url: `${BASE}/drill/data`, method: 'get', params })
}
// 穿透分析 - 分层懒加载接口
export function getDrillOverview() {
  return request({ url: `${BASE}/drill/overview`, method: 'get' })
}
export function getDrillGroup(params) {
  return request({ url: `${BASE}/drill/group`, method: 'get', params })
}
export function getDrillSubIndustry(params) {
  return request({ url: `${BASE}/drill/subIndustry`, method: 'get', params })
}
export function getDrillCompany(companyId) {
  return request({ url: `${BASE}/drill/company/${companyId}`, method: 'get' })
}
