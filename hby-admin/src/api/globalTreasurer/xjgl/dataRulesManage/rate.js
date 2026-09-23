import request from '@/utils/request'

export function getExchangeRateList(query) {
  return request({
    url: '/qqsk/financial/xjgl/dataRulesManage/exchangeRate/getList',
    method: 'post',
    params: query
  })
}

export function createExchangeRate(data) {
  return request({
    url: '/qqsk/financial/xjgl/dataRulesManage/exchangeRate/create',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function updateExchangeRate(data) {
  return request({
    url: '/qqsk/financial/xjgl/dataRulesManage/exchangeRate/update',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function deleteExchangeRate(data) {
  return request({
    url: '/qqsk/financial/xjgl/dataRulesManage/exchangeRate/delete',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function batchDeleteExchangeRate(data) {
  return request({
    url: '/qqsk/financial/xjgl/dataRulesManage/exchangeRate/batchDelete',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function getExchangeRateDetail(rateId) {
  return request({
    url: '/qqsk/financial/xjgl/dataRulesManage/exchangeRate/getDetail',
    method: 'post',
    data: { rateId },
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function syncExchangeRate(data) {
  return request({
    url: '/qqsk/financial/xjgl/dataRulesManage/exchangeRate/sync',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function exportExchangeRate(data) {
  return request({
    url: '/qqsk/financial/xjgl/dataRulesManage/exchangeRate/export',
    method: 'post',
    data,
    responseType: 'blob',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function importExchangeRate(data) {
  return request({
    url: '/qqsk/financial/xjgl/dataRulesManage/exchangeRate/import',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

export function updateExchangeRateStatus(data) {
  return request({
    url: '/qqsk/financial/xjgl/dataRulesManage/exchangeRate/updateStatus',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function getExchangeRateStatistics() {
  return request({
    url: '/qqsk/financial/xjgl/dataRulesManage/exchangeRate/getStatistics',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function getExchangeRateTrendAnalysis(data) {
  return request({
    url: '/qqsk/financial/xjgl/dataRulesManage/exchangeRate/getTrendAnalysis',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function exportTrendAnalysisReport(data) {
  return request({
    url: '/qqsk/financial/xjgl/dataRulesManage/exchangeRate/exportTrendAnalysis',
    method: 'post',
    data,
    responseType: 'blob',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// 利率管理API接口
export function getInterestRateList(query) {
  return request({
    url: '/qqsk/financial/xjgl/dataRulesManage/interestRate/getList',
    method: 'post',
    params: query
  })
}

export function createInterestRate(data) {
  return request({
    url: '/qqsk/financial/xjgl/dataRulesManage/interestRate/create',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function updateInterestRate(data) {
  return request({
    url: '/qqsk/financial/xjgl/dataRulesManage/interestRate/update',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function deleteInterestRate(data) {
  return request({
    url: '/qqsk/financial/xjgl/dataRulesManage/interestRate/delete',
    method: 'post',
    params: data
  })
}

export function batchDeleteInterestRate(data) {
  return request({
    url: '/qqsk/financial/xjgl/dataRulesManage/interestRate/batchDelete',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function getInterestRateDetail(rateId) {
  return request({
    url: '/qqsk/financial/xjgl/dataRulesManage/interestRate/getDetail',
    method: 'post',
    data: { rateId },
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function syncInterestRate(data) {
  return request({
    url: '/qqsk/financial/xjgl/dataRulesManage/interestRate/sync',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function exportInterestRate(data) {
  return request({
    url: '/qqsk/financial/xjgl/dataRulesManage/interestRate/export',
    method: 'post',
    data,
    responseType: 'blob',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function importInterestRate(data) {
  return request({
    url: '/qqsk/financial/xjgl/dataRulesManage/interestRate/import',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

export function updateInterestRateStatus(data) {
  return request({
    url: '/qqsk/financial/xjgl/dataRulesManage/interestRate/updateStatus',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function getInterestRateStatistics() {
  return request({
    url: '/qqsk/financial/xjgl/dataRulesManage/interestRate/getStatistics',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// ==================== 汇率配置管理API ====================
export function getExchangeRateConfigList(query) {
  return request({
    url: '/qqsk/financial/xjgl/dataRulesManage/exchangeRateConfig/list',
    method: 'post',
    data: query,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function createExchangeRateConfig(data) {
  return request({
    url: '/qqsk/financial/xjgl/dataRulesManage/exchangeRateConfig/create',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function updateExchangeRateConfig(data) {
  return request({
    url: '/qqsk/financial/xjgl/dataRulesManage/exchangeRateConfig/update',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function deleteExchangeRateConfig(data) {
  return request({
    url: '/qqsk/financial/xjgl/dataRulesManage/exchangeRateConfig/delete',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function batchDeleteExchangeRateConfig(data) {
  return request({
    url: '/qqsk/financial/xjgl/dataRulesManage/exchangeRateConfig/batchDelete',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function getExchangeRateConfigDetail(id) {
  return request({
    url: '/qqsk/financial/xjgl/dataRulesManage/exchangeRateConfig/detail',
    method: 'get',
    params: { id }
  })
}

export function syncExchangeRateConfig(data) {
  return request({
    url: '/qqsk/financial/xjgl/dataRulesManage/exchangeRateConfig/sync',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function updateExchangeRateConfigStatus(data) {
  return request({
    url: '/qqsk/financial/xjgl/dataRulesManage/exchangeRateConfig/updateStatus',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function getExchangeRateConfigStatistics() {
  return request({
    url: '/qqsk/financial/xjgl/dataRulesManage/exchangeRateConfig/statistics',
    method: 'get'
  })
}

export function exportExchangeRateConfig(params) {
  return request({
    url: '/qqsk/financial/xjgl/dataRulesManage/exchangeRateConfig/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}