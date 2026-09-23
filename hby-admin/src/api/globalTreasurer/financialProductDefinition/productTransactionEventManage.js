import request from '@/utils/request'

// 产品交易事件管理API

/**
 * 分页查询产品交易事件列表
 */
export function getProductTransactionEventList(params) {
  return request({
    url: '/qqsk/financial/product-definition/product-transaction-event/getList',
    method: 'post',
    params
  })
}

/**
 * 根据ID查询产品交易事件详情
 */
export function getProductTransactionEventDetail(ID) {
  return request({
    url: '/qqsk/financial/product-definition/product-transaction-event/getById',
    method: 'get',
    params: { ID }
  })
}

/**
 * 新增产品交易事件
 */
export function createProductTransactionEvent(data) {
  return request({
    url: '/qqsk/financial/product-definition/product-transaction-event/create',
    method: 'post',
    data
  })
}

/**
 * 更新产品交易事件
 */
export function updateProductTransactionEvent(data) {
  return request({
    url: '/qqsk/financial/product-definition/product-transaction-event/update',
    method: 'post',
    data
  })
}

/**
 * 删除产品交易事件
 */
export function deleteProductTransactionEvent(ID) {
  return request({
    url: '/qqsk/financial/product-definition/product-transaction-event/delete',
    method: 'post',
    params: { ID }
  })
}

/**
 * 批量删除产品交易事件
 */
export function batchDeleteProductTransactionEvents(IDs) {
  return request({
    url: '/qqsk/financial/product-definition/product-transaction-event/batchDelete',
    method: 'post',
    params: { IDs }
  })
}

/**
 * 更新产品交易事件状态
 */
export function updateProductTransactionEventStatus(ID, isEnabled) {
  return request({
    url: '/qqsk/financial/product-definition/product-transaction-event/updateStatus',
    method: 'post',
    params: { ID, isEnabled }
  })
}

/**
 * 获取产品交易事件统计信息
 */
export function getProductTransactionEventStatistics() {
  return request({
    url: '/qqsk/financial/product-definition/product-transaction-event/getStatistics',
    method: 'get'
  })
}

/**
 * 检查编码唯一性
 */
export function checkEventCodeUnique(eventCode, excludeId) {
  return request({
    url: '/qqsk/financial/product-definition/product-transaction-event/checkCodeUnique',
    method: 'get',
    params: { eventCode, excludeId }
  })
}

/**
 * 获取事件监控列表
 */
export function getEventMonitorList(params) {
  return request({
    url: '/qqsk/financial/product-definition/product-transaction-event/getEventMonitorList',
    method: 'post',
    data: params
  })
}

/**
 * 导出产品交易事件配置
 */
export function exportProductTransactionEvent(params) {
  return request({
    url: '/qqsk/financial/product-definition/product-transaction-event/export',
    method: 'post',
    data: params,
    responseType: 'blob'
  })
}
