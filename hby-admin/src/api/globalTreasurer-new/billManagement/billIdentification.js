import request from '@/utils/request'

// 票据标识管理API接口

/**
 * 分页查询票据标识列表
 */
export function getBillIdentificationPage(params) {
  return request({
    url: '/qqsk/bill/identification/page',
    method: 'post',
    data: params
  })
}

/**
 * 获取票据标识详情
 */
export function getBillIdentificationDetail(id) {
  return request({
    url: `/qqsk/bill/identification/detail/${id}`,
    method: 'post'
  })
}

/**
 * 新增票据标识
 */
export function createBillIdentification(data) {
  return request({
    url: '/qqsk/bill/identification/save',
    method: 'post',
    data
  })
}

/**
 * 修改票据标识
 */
export function updateBillIdentification(data) {
  return request({
    url: '/qqsk/bill/identification/update',
    method: 'post',
    data
  })
}

/**
 * 删除票据标识
 */
export function deleteBillIdentification(ids) {
  return request({
    url: '/qqsk/bill/identification/delete',
    method: 'post',
    data: ids
  })
}

/**
 * 获取票据标识树形结构
 */
export function getBillIdentificationTree(params) {
  return request({
    url: '/qqsk/bill/identification/tree',
    method: 'post',
    data: params || {}
  })
}

/**
 * 根据类型查询标识列表
 */
export function getBillIdentificationByType(type) {
  return request({
    url: '/qqsk/bill/identification/listByType',
    method: 'post',
    params: { type }
  })
}

/**
 * 批量启用标识
 */
export function batchEnableBillIdentification(ids) {
  return request({
    url: '/qqsk/bill/identification/batch-enable',
    method: 'post',
    data: ids
  })
}

/**
 * 批量禁用标识
 */
export function batchDisableBillIdentification(ids) {
  return request({
    url: '/qqsk/bill/identification/batch-disable',
    method: 'post',
    data: ids
  })
}

