/**
 * 票据背书管理 API
 * @module api/globalTreasurer-new/billManagement/billEndorsement
 */
import request from '@/utils/request'

/**
 * 分页查询票据背书列表
 * @param {Object} params - 查询参数
 */
export function getBillEndorsementPage(params) {
  return request({
    url: '/qqsk/bill/endorsement/page',
    method: 'post',
    data: params
  })
}

/**
 * 获取票据背书详情
 * @param {string} endorsementId - 背书ID
 */
export function getBillEndorsementDetail(endorsementId) {
  return request({
    url: `/qqsk/bill/endorsement/detail/${endorsementId}`,
    method: 'post'
  })
}

/**
 * 创建背书申请
 * @param {Object} data - 背书数据
 */
export function createBillEndorsement(data) {
  return request({
    url: '/qqsk/bill/endorsement/save',
    method: 'post',
    data
  })
}

/**
 * 更新背书申请
 * @param {Object} data - 背书数据
 */
export function updateBillEndorsement(data) {
  return request({
    url: '/qqsk/bill/endorsement/update',
    method: 'post',
    data
  })
}

/**
 * 删除背书申请
 * @param {Array} endorsementIds - 背书ID数组
 */
export function deleteBillEndorsement(endorsementIds) {
  return request({
    url: '/qqsk/bill/endorsement/delete',
    method: 'post',
    params: { endorsementIds: endorsementIds.join(',') }
  })
}

/**
 * 审批背书申请
 * @param {string} endorsementId - 背书ID
 * @param {Object} data - 审批数据
 */
export function approveBillEndorsement(endorsementId, data) {
  return request({
    url: `/qqsk/bill/endorsement/approve/${endorsementId}`,
    method: 'post',
    data
  })
}

/**
 * 执行背书
 * @param {string} endorsementId - 背书ID
 */
export function executeBillEndorsement(endorsementId) {
  return request({
    url: `/qqsk/bill/endorsement/execute/${endorsementId}`,
    method: 'post'
  })
}

/**
 * 撤销背书申请
 * @param {string} endorsementId - 背书ID
 * @param {string} reason - 撤销原因
 */
export function cancelBillEndorsement(endorsementId, reason) {
  return request({
    url: `/qqsk/bill/endorsement/cancel/${endorsementId}`,
    method: 'post',
    data: { reason }
  })
}

/**
 * 批量审批背书
 * @param {Object} data - 批量审批数据
 */
export function batchApproveBillEndorsement(data) {
  return request({
    url: '/qqsk/bill/endorsement/batch-approve',
    method: 'post',
    data
  })
}

/**
 * 获取可用于背书的票据列表
 * @param {Object} params - 查询参数
 */
export function getAvailableBillsForEndorsement(params) {
  return request({
    url: '/qqsk/bill/endorsement/available-bills',
    method: 'post',
    data: params
  })
}

