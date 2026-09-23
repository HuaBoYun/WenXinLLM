import request from '@/utils/request'

/**
 * 财资公共模块 - 数据规则管理 API
 * 包含：业务规则管理、数据源配置管理、汇率管理、利率管理、市场数据管理
 */

// ==================== 业务规则管理 ====================
export function getBusinessRulePage(params) {
  return request({
    url: '/qqsk/financial/xjgl/dataRulesManage/businessRule/list',
    method: 'post',
    data: params,
    headers: { 'Content-Type': 'application/json' }
  })
}

export function getBusinessRuleDetail(id) {
  return request({ url: '/qqsk/financial/xjgl/dataRulesManage/businessRule/detail', method: 'get', params: { id } })
}

export function addBusinessRule(params) {
  return request({
    url: '/qqsk/financial/xjgl/dataRulesManage/businessRule/add',
    method: 'post',
    data: params,
    headers: { 'Content-Type': 'application/json' }
  })
}

export function updateBusinessRule(params) {
  return request({
    url: '/qqsk/financial/xjgl/dataRulesManage/businessRule/update',
    method: 'post',
    data: params,
    headers: { 'Content-Type': 'application/json' }
  })
}

export function deleteBusinessRule(id) {
  return request({
    url: '/qqsk/financial/xjgl/dataRulesManage/businessRule/delete',
    method: 'post',
    data: { id },
    headers: { 'Content-Type': 'application/json' }
  })
}

export function getBusinessRuleStatistics() {
  return request({ url: '/qqsk/financial/xjgl/dataRulesManage/businessRule/statistics', method: 'get' })
}

export function importBusinessRule(file) {
  const formData = new FormData()
  formData.append('file', file)
  return request({
    url: '/qqsk/financial/xjgl/dataRulesManage/businessRule/import',
    method: 'post',
    data: formData,
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

export function exportBusinessRule(params) {
  return request({
    url: '/qqsk/financial/xjgl/dataRulesManage/businessRule/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

// ==================== 数据源配置管理 ====================
export function getDataSourceConfigPage(params) {
  return request({
    url: '/qqsk/financial/dataSourceConfig/list',
    method: 'post',
    data: params,
    headers: { 'Content-Type': 'application/json' }
  })
}

export function getDataSourceConfigDetail(id) {
  return request({ url: '/qqsk/financial/dataSourceConfig/detail', method: 'get', params: { id } })
}

export function addDataSourceConfig(params) {
  return request({
    url: '/qqsk/financial/dataSourceConfig/add',
    method: 'post',
    data: params,
    headers: { 'Content-Type': 'application/json' }
  })
}

export function updateDataSourceConfig(params) {
  return request({
    url: '/qqsk/financial/dataSourceConfig/update',
    method: 'post',
    data: params,
    headers: { 'Content-Type': 'application/json' }
  })
}

export function deleteDataSourceConfig(id) {
  return request({
    url: '/qqsk/financial/dataSourceConfig/delete',
    method: 'post',
    data: { id },
    headers: { 'Content-Type': 'application/json' }
  })
}

export function testDataSourceConnection(id) {
  return request({
    url: '/qqsk/financial/dataSourceConfig/testConnection',
    method: 'post',
    data: { id },
    headers: { 'Content-Type': 'application/json' }
  })
}

export function batchTestDataSourceConnection(ids) {
  return request({
    url: '/qqsk/financial/dataSourceConfig/batchTest',
    method: 'post',
    data: { ids },
    headers: { 'Content-Type': 'application/json' }
  })
}

export function exportDataSourceConfig(params) {
  return request({
    url: '/qqsk/financial/dataSourceConfig/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

// ==================== 汇率管理 ====================
export function getExchangeRatePage(params) {
  return request({
    url: '/qqsk/financial/xjgl/dataRulesManage/exchangeRate/list',
    method: 'post',
    data: params,
    headers: { 'Content-Type': 'application/json' }
  })
}

export function getExchangeRateDetail(id) {
  return request({ url: '/qqsk/financial/xjgl/dataRulesManage/exchangeRate/detail', method: 'get', params: { id } })
}

export function addExchangeRate(params) {
  return request({
    url: '/qqsk/financial/xjgl/dataRulesManage/exchangeRate/add',
    method: 'post',
    data: params,
    headers: { 'Content-Type': 'application/json' }
  })
}

export function updateExchangeRate(params) {
  return request({
    url: '/qqsk/financial/xjgl/dataRulesManage/exchangeRate/update',
    method: 'post',
    data: params,
    headers: { 'Content-Type': 'application/json' }
  })
}

export function deleteExchangeRate(id) {
  return request({
    url: '/qqsk/financial/xjgl/dataRulesManage/exchangeRate/delete',
    method: 'post',
    data: { id },
    headers: { 'Content-Type': 'application/json' }
  })
}

export function batchDeleteExchangeRate(ids) {
  return request({
    url: '/qqsk/financial/xjgl/dataRulesManage/exchangeRate/batchDelete',
    method: 'post',
    data: { ids },
    headers: { 'Content-Type': 'application/json' }
  })
}

export function syncExchangeRate() {
  return request({
    url: '/qqsk/financial/xjgl/dataRulesManage/exchangeRate/sync',
    method: 'post',
    headers: { 'Content-Type': 'application/json' }
  })
}

export function updateExchangeRateStatus(id, status) {
  return request({
    url: '/qqsk/financial/xjgl/dataRulesManage/exchangeRate/updateStatus',
    method: 'post',
    data: { id, status },
    headers: { 'Content-Type': 'application/json' }
  })
}

export function getExchangeRateStatistics() {
  return request({ url: '/qqsk/financial/xjgl/dataRulesManage/exchangeRate/statistics', method: 'get' })
}

export function getExchangeRateTrendAnalysis(params) {
  return request({
    url: '/qqsk/financial/xjgl/dataRulesManage/exchangeRate/trendAnalysis',
    method: 'post',
    data: params,
    headers: { 'Content-Type': 'application/json' }
  })
}

export function exportExchangeRate(params) {
  return request({
    url: '/qqsk/financial/xjgl/dataRulesManage/exchangeRate/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

export function exportExchangeRateTrendReport(params) {
  return request({
    url: '/qqsk/financial/xjgl/dataRulesManage/exchangeRate/exportTrendReport',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

// ==================== 利率管理 ====================
export function getInterestRatePage(params) {
  return request({
    url: '/qqsk/financial/xjgl/dataRulesManage/interestRate/list',
    method: 'post',
    data: params,
    headers: { 'Content-Type': 'application/json' }
  })
}

export function getInterestRateDetail(id) {
  return request({ url: '/qqsk/financial/xjgl/dataRulesManage/interestRate/detail', method: 'get', params: { id } })
}

export function addInterestRate(params) {
  return request({
    url: '/qqsk/financial/xjgl/dataRulesManage/interestRate/create',
    method: 'post',
    data: params,
    headers: { 'Content-Type': 'application/json' }
  })
}

export function updateInterestRate(params) {
  return request({
    url: '/qqsk/financial/xjgl/dataRulesManage/interestRate/update',
    method: 'post',
    data: params,
    headers: { 'Content-Type': 'application/json' }
  })
}

export function deleteInterestRate(id) {
  return request({
    url: '/qqsk/financial/xjgl/dataRulesManage/interestRate/delete',
    method: 'post',
    data: { id },
    headers: { 'Content-Type': 'application/json' }
  })
}

export function batchDeleteInterestRate(ids) {
  return request({
    url: '/qqsk/financial/xjgl/dataRulesManage/interestRate/batchDelete',
    method: 'post',
    data: { ids },
    headers: { 'Content-Type': 'application/json' }
  })
}

export function syncInterestRate() {
  return request({
    url: '/qqsk/financial/xjgl/dataRulesManage/interestRate/sync',
    method: 'post',
    headers: { 'Content-Type': 'application/json' }
  })
}

export function updateInterestRateStatus(id, status) {
  return request({
    url: '/qqsk/financial/xjgl/dataRulesManage/interestRate/updateStatus',
    method: 'post',
    data: { id, status },
    headers: { 'Content-Type': 'application/json' }
  })
}

export function getInterestRateStatistics() {
  return request({ url: '/qqsk/financial/xjgl/dataRulesManage/interestRate/statistics', method: 'get' })
}

export function exportInterestRate(params) {
  return request({
    url: '/qqsk/financial/xjgl/dataRulesManage/interestRate/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

// ==================== 市场数据管理 ====================
export function getMarketDataPage(params) {
  return request({
    url: '/qqsk/financial/treasuryCommon/marketData/list',
    method: 'post',
    data: params,
    headers: { 'Content-Type': 'application/json' }
  })
}

export function getMarketDataDetail(id) {
  return request({ url: '/qqsk/financial/treasuryCommon/marketData/detail', method: 'get', params: { id } })
}

export function addMarketData(params) {
  return request({
    url: '/qqsk/financial/treasuryCommon/marketData/create',
    method: 'post',
    data: params,
    headers: { 'Content-Type': 'application/json' }
  })
}

export function updateMarketData(params) {
  return request({
    url: '/qqsk/financial/treasuryCommon/marketData/update',
    method: 'post',
    data: params,
    headers: { 'Content-Type': 'application/json' }
  })
}

export function deleteMarketData(id) {
  return request({
    url: '/qqsk/financial/treasuryCommon/marketData/delete',
    method: 'post',
    data: { id },
    headers: { 'Content-Type': 'application/json' }
  })
}

export function importMarketData(file) {
  const formData = new FormData()
  formData.append('file', file)
  return request({
    url: '/qqsk/financial/treasuryCommon/marketData/import',
    method: 'post',
    data: formData,
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

export function syncMarketData() {
  return request({
    url: '/qqsk/financial/treasuryCommon/marketData/sync',
    method: 'post',
    headers: { 'Content-Type': 'application/json' }
  })
}

export function exportMarketData(params) {
  return request({
    url: '/qqsk/financial/treasuryCommon/marketData/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

