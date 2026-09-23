import request from '@/utils/request'
import { transData } from '@/utils/requestData'

const BASE = '/monitor/v1/supervision/property'

// ==================== 台账管理 ====================

/** 产权登记列表（分页） */
export function getPropertyList(data) {
  return request({ url: `${BASE}/right/list`, method: 'post', data: transData(data) })
}

/** 新增产权登记 */
export function addProperty(data) {
  return request({ url: `${BASE}/right/add`, method: 'post', data: transData(data) })
}

/** 更新产权登记 */
export function updateProperty(data) {
  return request({ url: `${BASE}/right/update`, method: 'post', data: transData(data) })
}

/** 删除产权登记 */
export function deleteProperty(data) {
  return request({ url: `${BASE}/right/delete`, method: 'post', data: transData(data) })
}

/** 获取产权统计数据 */
export function getPropertyStatistics(params) {
  return request({ url: `${BASE}/right/statistics`, method: 'get', params })
}

/** 批量导入产权数据 */
export function importPropertyData(data) {
  return request({ url: `${BASE}/right/import`, method: 'post', data, headers: { 'Content-Type': 'multipart/form-data' } })
}

/** 导出产权台账 */
export function exportPropertyData(params) {
  return request({ url: `${BASE}/right/export`, method: 'get', params, responseType: 'blob' })
}

// ==================== 股权穿透 ====================

/** 股权层级树（递归结构） */
export function getEquityTree(companyId, depth = 5) {
  return request({ url: `${BASE}/equity/tree/${companyId}`, method: 'get', params: { depth } })
}

/** 股权层级图谱数据 */
export function getEquityGraph(companyId) {
  return request({ url: `${BASE}/equity/graph/${companyId}`, method: 'get' })
}

/** 层级统计概览 */
export function getEquityStats() {
  return request({ url: `${BASE}/equity/stats`, method: 'get' })
}

/** 根据companyId查询企业产权台账详情(含直接子企业) */
export function getEquityRegistryDetail(companyId) {
  return request({ url: `${BASE}/equity/registry/${companyId}`, method: 'get' })
}

// ==================== 变动管理 ====================

/** 产权变动记录列表 */
export function getChangeList(data) {
  return request({ url: `${BASE}/change/list`, method: 'post', data: transData(data) })
}

/** 新增/编辑产权变动登记 */
export function saveChange(data) {
  return request({ url: `${BASE}/change/save`, method: 'post', data: transData(data) })
}

/** 删除产权变动记录 */
export function deleteChange(data) {
  return request({ url: `${BASE}/change/delete`, method: 'post', data: transData(data) })
}

/** 变动时间轴 */
export function getChangeTimeline(companyName) {
  return request({ url: `${BASE}/change/timeline/${encodeURIComponent(companyName)}`, method: 'get' })
}

// ==================== 交易合规 ====================

/** 产权交易列表（分页） */
export function getTransactionList(data) {
  return request({ url: `${BASE}/right/transaction/list`, method: 'post', data: transData(data) })
}

/** 新增产权交易记录 */
export function addTransaction(data) {
  return request({ url: `${BASE}/right/transaction/add`, method: 'post', data: transData(data) })
}

/** 更新产权交易记录 */
export function updateTransaction(data) {
  return request({ url: `${BASE}/right/transaction/update`, method: 'post', data: transData(data) })
}

/** 交易合规统计 */
export function getTransactionComplianceStats(params) {
  return request({ url: `${BASE}/transaction/compliance/stats`, method: 'get', params })
}

// ==================== 参股监控 ====================

/** 三表比对（产权登记/工商登记/财务并表） */
export function getThreeTableCompare(data) {
  return request({ url: `${BASE}/three-table-compare/list`, method: 'post', data: transData(data) })
}

/** 参股企业列表 */
export function getShareholdingList(data) {
  return request({ url: `${BASE}/shareholding/list`, method: 'post', data: transData(data) })
}

/** 参股企业经营分析详情 */
export function getShareholdingAnalysis(companyId) {
  return request({ url: `${BASE}/shareholding/analysis/${companyId}`, method: 'get' })
}

/** 连续亏损企业清单 */
export function getLossCompanyList() {
  return request({ url: `${BASE}/shareholding/loss-list`, method: 'get' })
}

// ==================== 预警管理 ====================

/** 产权预警列表 */
export function getPropertyWarningList(data) {
  return request({ url: `${BASE}/warning/list`, method: 'post', data: transData(data) })
}

/** 处置产权预警 */
export function handlePropertyWarning(data) {
  return request({ url: `${BASE}/warning/handle`, method: 'post', data: transData(data) })
}

/** 关闭产权预警 */
export function closePropertyWarning(data) {
  return request({ url: `${BASE}/warning/close`, method: 'post', data: transData(data) })
}

// ==================== 驾驶舱 ====================

/** 产权驾驶舱综合数据 */
export function getPropertyDashboard(companyId) {
  return request({ url: `${BASE}/dashboard`, method: 'get', params: { companyId } })
}

// ==================== 统一导出接口 ====================

/** 导出产权登记数据 */
export function exportPropertyRight() {
  return request({ url: '/monitor/v1/supervision/export/property/right', method: 'get', responseType: 'blob' })
}

/** 导出产权交易数据 */
export function exportPropertyTransaction() {
  return request({ url: '/monitor/v1/supervision/export/property/transaction', method: 'get', responseType: 'blob' })
}

/** 导出产权变动数据 */
export function exportPropertyChange() {
  return request({ url: '/monitor/v1/supervision/export/property/change', method: 'get', responseType: 'blob' })
}

/** 导出参股企业数据 */
export function exportPropertyShareholding() {
  return request({ url: '/monitor/v1/supervision/export/property/shareholding', method: 'get', responseType: 'blob' })
}


