import request from '@/utils/request'

// 产品交易事件管理API

export function getProductTransactionEventList(params) {
  return request({
    url: '/cwgxAi/xjgl/financialProductDefinition/productTransactionEventManage/getList',
    method: 'post',
    data: params
  })
}

export function createProductTransactionEvent(data) {
  return request({
    url: '/cwgxAi/xjgl/financialProductDefinition/productTransactionEventManage/create',
    method: 'post',
    data: data
  })
}

export function updateProductTransactionEvent(data) {
  return request({
    url: '/cwgxAi/xjgl/financialProductDefinition/productTransactionEventManage/update',
    method: 'post',
    data: data
  })
}

export function deleteProductTransactionEvent(eventId) {
  return request({
    url: '/cwgxAi/xjgl/financialProductDefinition/productTransactionEventManage/delete',
    method: 'post',
    data: { eventId: eventId }
  })
}

export function exportProductTransactionEvents(params) {
  return request({
    url: '/cwgxAi/xjgl/financialProductDefinition/productTransactionEventManage/export',
    method: 'post',
    data: params,
    responseType: 'blob'
  })
}

export function getProductTransactionEventStatistics() {
  return request({
    url: '/cwgxAi/xjgl/financialProductDefinition/productTransactionEventManage/getStatistics',
    method: 'post'
  })
}

export function getEventMonitorList() {
  return request({
    url: '/cwgxAi/xjgl/financialProductDefinition/productTransactionEventManage/getEventMonitorList',
    method: 'post'
  })
}
