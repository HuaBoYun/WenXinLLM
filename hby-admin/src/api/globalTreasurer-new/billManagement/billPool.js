/**
 * 票据池管理 API
 * @module api/globalTreasurer-new/billManagement/billPool
 */
import request from '@/utils/request'

/**
 * 分页查询票据池列表
 * @param {Object} params - 查询参数
 */
export function getBillPoolPage(params) {
  return request({
    url: '/qqsk/bill/pool/page',
    method: 'post',
    data: params
  })
}

/**
 * 获取票据池详情
 * @param {string} poolId - 票据池ID
 */
export function getBillPoolDetail(poolId) {
  return request({
    url: `/qqsk/bill/pool/detail/${poolId}`,
    method: 'post'
  })
}

/**
 * 创建票据池
 * @param {Object} data - 票据池数据
 */
export function createBillPool(data) {
  return request({
    url: '/qqsk/bill/pool/save',
    method: 'post',
    data
  })
}

/**
 * 更新票据池
 * @param {Object} data - 票据池数据
 */
export function updateBillPool(data) {
  return request({
    url: '/qqsk/bill/pool/update',
    method: 'post',
    data
  })
}

/**
 * 删除票据池
 * @param {Array} poolIds - 票据池ID数组
 */
export function deleteBillPool(poolIds) {
  return request({
    url: '/qqsk/bill/pool/delete',
    method: 'post',
    data: { poolIds }
  })
}

/**
 * 添加票据到池
 * @param {Object} data - 添加数据
 */
export function addBillsToPool(data) {
  return request({
    url: '/qqsk/bill/pool/add-bills',
    method: 'post',
    data
  })
}

/**
 * 从池中移除票据
 * @param {Object} data - 移除数据
 */
export function removeBillsFromPool(data) {
  return request({
    url: '/qqsk/bill/pool/remove-bills',
    method: 'post',
    data
  })
}

/**
 * 获取池内票据
 * @param {string} poolId - 票据池ID
 */
export function getPoolBills(poolId) {
  return request({
    url: `/qqsk/bill/pool/bills/${poolId}`,
    method: 'post'
  })
}

/**
 * 质押融资
 * @param {Object} data - 融资数据
 */
export function pledgeFinancing(data) {
  return request({
    url: '/qqsk/bill/pool/pledge-financing',
    method: 'post',
    data
  })
}

/**
 * 获取融资记录
 * @param {string} poolId - 票据池ID
 */
export function getFinancingRecords(poolId) {
  return request({
    url: `/qqsk/bill/pool/financing-records/${poolId}`,
    method: 'post'
  })
}

/**
 * 导出票据池数据
 * @param {Object} params - 导出参数
 */
export function exportBillPool(params) {
  return request({
    url: '/qqsk/bill/pool/export',
    method: 'post',
    data: params,
    responseType: 'blob'
  })
}

/**
 * 获取票据池统计数据
 * @param {Object} params - 统计参数
 */
export function getBillPoolStatistics(params) {
  return request({
    url: '/qqsk/bill/pool/statistics',
    method: 'post',
    data: params
  })
}

/**
 * 获取可入池票据列表
 * @param {Object} params - 查询参数
 */
export function getAvailableBillsForPool(params) {
  return request({
    url: '/qqsk/bill/pool/available-bills',
    method: 'post',
    data: params
  })
}

/**
 * 冻结票据池
 * @param {string} poolId - 票据池ID
 */
export function freezeBillPool(poolId) {
  return request({
    url: `/qqsk/bill/pool/freeze/${poolId}`,
    method: 'post'
  })
}

/**
 * 关闭票据池
 * @param {string} poolId - 票据池ID
 */
export function closeBillPool(poolId) {
  return request({
    url: `/qqsk/bill/pool/close/${poolId}`,
    method: 'post'
  })
}

/**
 * 生成票据池报告
 * @param {string} poolId - 票据池ID
 */
export function generatePoolReport(poolId) {
  return request({
    url: `/qqsk/bill/pool/report/${poolId}`,
    method: 'post'
  })
}