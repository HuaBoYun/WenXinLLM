import request from '@/utils/request'

// 汇率管理 API接口 (cwgxAi版本)

export function getExchangeRateList(query) {
  return request({
    url: '/cwgxAi/xjgl/dataRulesManage/exchangeRate/getList',
    method: 'post',
    data: query
  })
}

export function createExchangeRate(data) {
  return request({
    url: '/cwgxAi/xjgl/dataRulesManage/exchangeRate/create',
    method: 'post',
    data
  })
}

export function updateExchangeRate(data) {
  return request({
    url: '/cwgxAi/xjgl/dataRulesManage/exchangeRate/update',
    method: 'post',
    data
  })
}

export function deleteExchangeRate(data) {
  return request({
    url: '/cwgxAi/xjgl/dataRulesManage/exchangeRate/delete',
    method: 'post',
    data
  })
}

export function batchDeleteExchangeRate(data) {
  return request({
    url: '/cwgxAi/xjgl/dataRulesManage/exchangeRate/batchDelete',
    method: 'post',
    data
  })
}

export function getExchangeRateDetail(rateId) {
  return request({
    url: '/cwgxAi/xjgl/dataRulesManage/exchangeRate/getDetail',
    method: 'post',
    data: { rateId }
  })
}

export function syncExchangeRate(data) {
  return request({
    url: '/cwgxAi/xjgl/dataRulesManage/exchangeRate/sync',
    method: 'post',
    data
  })
}

export function exportExchangeRate(data) {
  return request({
    url: '/cwgxAi/xjgl/dataRulesManage/exchangeRate/export',
    method: 'post',
    data,
    responseType: 'blob'
  })
}

export function importExchangeRate(data) {
  return request({
    url: '/cwgxAi/xjgl/dataRulesManage/exchangeRate/import',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

export function updateExchangeRateStatus(data) {
  return request({
    url: '/cwgxAi/xjgl/dataRulesManage/exchangeRate/updateStatus',
    method: 'post',
    data
  })
}

export function getExchangeRateStatistics() {
  return request({
    url: '/cwgxAi/xjgl/dataRulesManage/exchangeRate/getStatistics',
    method: 'post'
  })
}

export function getExchangeRateTrendAnalysis(data) {
  return request({
    url: '/cwgxAi/xjgl/dataRulesManage/exchangeRate/getTrendAnalysis',
    method: 'post',
    data
  })
}

export function exportTrendAnalysisReport(data) {
  return request({
    url: '/cwgxAi/xjgl/dataRulesManage/exchangeRate/exportTrendAnalysis',
    method: 'post',
    data,
    responseType: 'blob'
  })
}
