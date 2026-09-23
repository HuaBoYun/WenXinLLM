import request from '@/utils/request'

// 交易类型管理API

export function getTransactionTypeList(params) {
  return request({
    url: '/cwgxAi/xjgl/financialProductDefinition/transactionTypeManage/getList',
    method: 'post',
    data: params
  })
}

export function createTransactionType(data) {
  return request({
    url: '/cwgxAi/xjgl/financialProductDefinition/transactionTypeManage/create',
    method: 'post',
    data: data
  })
}

export function updateTransactionType(data) {
  return request({
    url: '/cwgxAi/xjgl/financialProductDefinition/transactionTypeManage/update',
    method: 'post',
    data: data
  })
}

export function deleteTransactionType(transactionTypeId) {
  return request({
    url: '/cwgxAi/xjgl/financialProductDefinition/transactionTypeManage/delete',
    method: 'post',
    data: { transactionTypeId: transactionTypeId }
  })
}

export function exportTransactionTypes(params) {
  return request({
    url: '/cwgxAi/xjgl/financialProductDefinition/transactionTypeManage/export',
    method: 'post',
    data: params,
    responseType: 'blob'
  })
}

export function getTransactionTypeStatistics() {
  return request({
    url: '/cwgxAi/xjgl/financialProductDefinition/transactionTypeManage/getStatistics',
    method: 'post'
  })
}

export function sortTransactionTypes(sortData) {
  return request({
    url: '/cwgxAi/xjgl/financialProductDefinition/transactionTypeManage/sort',
    method: 'post',
    data: sortData
  })
}
