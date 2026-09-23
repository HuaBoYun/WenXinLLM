/**
 * 票据贴现管理 API
 * @module api/globalTreasurer-new/billManagement/billDiscount
 */
import request from '@/utils/request'

/**
 * 分页查询票据贴现列表
 * @param {Object} params - 查询参数
 */
export function getBillDiscountPage(params) {
  return request({
    url: '/qqsk/bill/discount/page',
    method: 'post',
    data: params
  })
}

/**
 * 获取票据贴现详情
 * @param {string} discountId - 贴现ID
 */
export function getBillDiscountDetail(discountId) {
  return request({
    url: `/qqsk/bill/discount/detail/${discountId}`,
    method: 'post'
  })
}

/**
 * 创建贴现申请
 * @param {Object} data - 贴现数据
 */
export function createBillDiscount(data) {
  return request({
    url: '/qqsk/bill/discount/save',
    method: 'post',
    data
  })
}

/**
 * 更新贴现申请
 * @param {Object} data - 贴现数据
 */
export function updateBillDiscount(data) {
  return request({
    url: '/qqsk/bill/discount/update',
    method: 'post',
    data
  })
}

/**
 * 删除贴现申请
 * @param {Array} discountIds - 贴现ID数组
 */
export function deleteBillDiscount(discountIds) {
  return request({
    url: '/qqsk/bill/discount/delete',
    method: 'post',
    data: { discountIds }
  })
}

/**
 * 审批贴现申请
 * @param {string} discountId - 贴现ID
 * @param {Object} data - 审批数据
 */
export function approveBillDiscount(discountId, data) {
  return request({
    url: `/qqsk/bill/discount/approve/${discountId}`,
    method: 'post',
    data
  })
}

/**
 * 批量审批贴现申请
 * @param {Object} data - 批量审批数据
 */
export function batchApproveBillDiscount(data) {
  return request({
    url: '/qqsk/bill/discount/batch-approve',
    method: 'post',
    data
  })
}

/**
 * 执行贴现
 * @param {string} discountId - 贴现ID
 */
export function executeBillDiscount(discountId) {
  return request({
    url: `/qqsk/bill/discount/execute/${discountId}`,
    method: 'post'
  })
}

/**
 * 撤销贴现申请
 * @param {string} discountId - 贴现ID
 */
export function cancelBillDiscount(discountId) {
  return request({
    url: `/qqsk/bill/discount/cancel/${discountId}`,
    method: 'post'
  })
}

/**
 * 计算贴现利息
 * @param {Object} params - 计算参数
 */
export function calculateDiscountInterest(params) {
  return request({
    url: '/qqsk/bill/discount/calculate-interest',
    method: 'post',
    data: params
  })
}

/**
 * 导出贴现数据
 * @param {Object} params - 导出参数
 */
export function exportBillDiscount(params) {
  return request({
    url: '/qqsk/bill/discount/export',
    method: 'post',
    data: params,
    responseType: 'blob'
  })
}

/**
 * 获取贴现统计数据
 * @param {Object} params - 统计参数
 */
export function getBillDiscountStatistics(params) {
  return request({
    url: '/qqsk/bill/discount/statistics',
    method: 'post',
    data: params
  })
}

/**
 * 获取可贴现票据列表
 * @param {Object} params - 查询参数
 */
export function getAvailableBillsForDiscount(params) {
  return request({
    url: '/qqsk/bill/discount/available-bills',
    method: 'post',
    data: params
  })
}

