/**
 * 理财产品定义模块 - 交易类型管理 API
 */
import request from '@/utils/request'

const BASE_URL = '/qqsk/financial/product-definition/transaction-type'

// 分页查询交易类型列表
export function getTransactionTypeList(params) {
  return request({ url: `${BASE_URL}/getList`, method: 'post', params })
}

// 根据ID查询交易类型详情
export function getTransactionTypeById(ID) {
  return request({ url: `${BASE_URL}/getById`, method: 'get', params: { ID } })
}

// 新增交易类型
export function createTransactionType(data) {
  return request({ url: `${BASE_URL}/create`, method: 'post', data })
}

// 修改交易类型
export function updateTransactionType(data) {
  return request({ url: `${BASE_URL}/update`, method: 'post', data })
}

// 删除交易类型
export function deleteTransactionType(ID) {
  return request({ url: `${BASE_URL}/delete`, method: 'post', params: { ID } })
}

// 批量删除交易类型
export function batchDeleteTransactionType(IDs) {
  return request({ url: `${BASE_URL}/batchDelete`, method: 'post', params: { IDs } })
}

// 更新交易类型状态
export function updateTransactionTypeStatus(ID, isEnabled) {
  return request({ url: `${BASE_URL}/updateStatus`, method: 'post', params: { ID, isEnabled } })
}

// 获取启用的交易类型列表
export function getEnabledTransactionTypeList() {
  return request({ url: `${BASE_URL}/getEnabledList`, method: 'get' })
}

// 获取交易类型树形结构
export function getTransactionTypeTree(orgId) {
  return request({ url: `${BASE_URL}/getTree`, method: 'get', params: { orgId } })
}

// 检查交易类型编码唯一性
export function checkTransactionTypeCodeUnique(transactionTypeCode, excludeId) {
  return request({ url: `${BASE_URL}/checkCodeUnique`, method: 'get', params: { transactionTypeCode, excludeId } })
}

