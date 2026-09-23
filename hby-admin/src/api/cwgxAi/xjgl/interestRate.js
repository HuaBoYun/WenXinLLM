import request from '@/utils/request'

// 利率管理 API接口 (cwgxAi版本)

export function getInterestRateList(query) {
  return request({
    url: '/cwgxAi/xjgl/dataRulesManage/interestRate/getList',
    method: 'post',
    data: query
  })
}

export function createInterestRate(data) {
  return request({
    url: '/cwgxAi/xjgl/dataRulesManage/interestRate/create',
    method: 'post',
    data
  })
}

export function updateInterestRate(data) {
  return request({
    url: '/cwgxAi/xjgl/dataRulesManage/interestRate/update',
    method: 'post',
    data
  })
}

export function deleteInterestRate(data) {
  return request({
    url: '/cwgxAi/xjgl/dataRulesManage/interestRate/delete',
    method: 'post',
    data
  })
}

export function batchDeleteInterestRate(data) {
  return request({
    url: '/cwgxAi/xjgl/dataRulesManage/interestRate/batchDelete',
    method: 'post',
    data
  })
}

export function getInterestRateDetail(rateId) {
  return request({
    url: '/cwgxAi/xjgl/dataRulesManage/interestRate/getDetail',
    method: 'post',
    data: { rateId }
  })
}

export function syncInterestRate(data) {
  return request({
    url: '/cwgxAi/xjgl/dataRulesManage/interestRate/sync',
    method: 'post',
    data
  })
}

export function exportInterestRate(data) {
  return request({
    url: '/cwgxAi/xjgl/dataRulesManage/interestRate/export',
    method: 'post',
    data,
    responseType: 'blob'
  })
}

export function importInterestRate(data) {
  return request({
    url: '/cwgxAi/xjgl/dataRulesManage/interestRate/import',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

export function updateInterestRateStatus(data) {
  return request({
    url: '/cwgxAi/xjgl/dataRulesManage/interestRate/updateStatus',
    method: 'post',
    data
  })
}

export function getInterestRateStatistics() {
  return request({
    url: '/cwgxAi/xjgl/dataRulesManage/interestRate/getStatistics',
    method: 'post'
  })
}
