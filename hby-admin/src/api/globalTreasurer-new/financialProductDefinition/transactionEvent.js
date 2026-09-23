/**
 * 理财产品定义模块 - 产品交易事件管理 API
 */
import request from '@/utils/request'

const BASE_URL = '/qqsk/financial/product-definition/product-transaction-event'

// 分页查询产品交易事件列表
export function getTransactionEventList(params) {
  return request({ url: `${BASE_URL}/getList`, method: 'post', params })
}

// 根据ID查询产品交易事件详情
export function getTransactionEventById(ID) {
  return request({ url: `${BASE_URL}/getById`, method: 'get', params: { ID } })
}

// 新增产品交易事件
export function createTransactionEvent(data) {
  return request({ url: `${BASE_URL}/create`, method: 'post', data })
}

// 修改产品交易事件
export function updateTransactionEvent(data) {
  return request({ url: `${BASE_URL}/update`, method: 'post', data })
}

// 删除产品交易事件
export function deleteTransactionEvent(ID) {
  return request({ url: `${BASE_URL}/delete`, method: 'post', params: { ID } })
}

// 批量删除产品交易事件
export function batchDeleteTransactionEvent(IDs) {
  return request({ url: `${BASE_URL}/batchDelete`, method: 'post', params: { IDs } })
}

// 更新产品交易事件状态
export function updateTransactionEventStatus(ID, isEnabled) {
  return request({ url: `${BASE_URL}/updateStatus`, method: 'post', params: { ID, isEnabled } })
}

// 获取启用的产品交易事件列表
export function getEnabledTransactionEventList() {
  return request({ url: `${BASE_URL}/getEnabledList`, method: 'get' })
}

// 根据产品类型获取交易事件列表
export function getTransactionEventByProductType(productType) {
  return request({ url: `${BASE_URL}/getByProductType`, method: 'get', params: { productType } })
}

// 检查产品交易事件编码唯一性
export function checkTransactionEventCodeUnique(eventCode, excludeId) {
  return request({ url: `${BASE_URL}/checkCodeUnique`, method: 'get', params: { eventCode, excludeId } })
}

