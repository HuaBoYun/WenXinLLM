import request from '@/utils/request'

// 交易类型管理API

/**
 * 分页查询交易类型列表
 */
export function getTransactionTypeList(params) {
  return request({
    url: '/qqsk/financial/product-definition/transaction-type/getList',
    method: 'post',
    params
  })
}

/**
 * 根据ID查询交易类型详情
 */
export function getTransactionTypeDetail(ID) {
  return request({
    url: '/qqsk/financial/product-definition/transaction-type/getById',
    method: 'get',
    params: { ID }
  })
}

/**
 * 新增交易类型
 */
export function createTransactionType(data) {
  return request({
    url: '/qqsk/financial/product-definition/transaction-type/create',
    method: 'post',
    data
  })
}

/**
 * 更新交易类型
 */
export function updateTransactionType(data) {
  return request({
    url: '/qqsk/financial/product-definition/transaction-type/update',
    method: 'post',
    data
  })
}

/**
 * 删除交易类型
 */
export function deleteTransactionType(ID) {
  return request({
    url: '/qqsk/financial/product-definition/transaction-type/delete',
    method: 'post',
    params: { ID }
  })
}

/**
 * 批量删除交易类型
 */
export function batchDeleteTransactionTypes(IDs) {
  return request({
    url: '/qqsk/financial/product-definition/transaction-type/batchDelete',
    method: 'post',
    params: { IDs }
  })
}

/**
 * 更新交易类型状态
 */
export function updateTransactionTypeStatus(ID, isEnabled) {
  return request({
    url: '/qqsk/financial/product-definition/transaction-type/updateStatus',
    method: 'post',
    params: { ID, isEnabled }
  })
}

/**
 * 获取交易类型统计信息
 */
export function getTransactionTypeStatistics() {
  return request({
    url: '/qqsk/financial/product-definition/transaction-type/getStatistics',
    method: 'get'
  })
}

/**
 * 检查编码唯一性
 */
export function checkTransactionTypeCodeUnique(transactionTypeCode, excludeId) {
  return request({
    url: '/qqsk/financial/product-definition/transaction-type/checkCodeUnique',
    method: 'get',
    params: { transactionTypeCode, excludeId }
  })
}

/**
 * 排序
 */
export function sortTransactionTypes(sortData) {
  return request({
    url: '/qqsk/financial/product-definition/transaction-type/sort',
    method: 'post',
    data: sortData
  })
}
