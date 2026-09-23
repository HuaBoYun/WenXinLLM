/**
 * 电子票据管理 API
 * @module api/globalTreasurer-new/billManagement/electronicBill
 */
import request from '@/utils/request'

/**
 * 分页查询电子票据列表
 * @param {Object} params - 查询参数
 * @param {number} params.pageNum - 页码
 * @param {number} params.pageSize - 每页数量
 * @param {string} [params.billNumber] - 票据号码
 * @param {string} [params.billType] - 票据类型
 * @param {string} [params.billStatus] - 票据状态
 * @param {string} [params.issueDateStart] - 签发日期开始
 * @param {string} [params.issueDateEnd] - 签发日期结束
 */
export function getElectronicBillPage(params) {
  return request({
    url: '/qqsk/bill/electronic/page',
    method: 'post',
    data: params
  })
}

/**
 * 获取电子票据详情
 * @param {string} billId - 票据ID
 */
export function getElectronicBillDetail(billId) {
  return request({
    url: `/qqsk/bill/electronic/detail/${billId}`,
    method: 'post'
  })
}

/**
 * 创建电子票据
 * @param {Object} data - 票据数据
 */
export function createElectronicBill(data) {
  return request({
    url: '/qqsk/bill/electronic/save',
    method: 'post',
    data
  })
}

/**
 * 更新电子票据
 * @param {Object} data - 票据数据
 */
export function updateElectronicBill(data) {
  return request({
    url: '/qqsk/bill/electronic/update',
    method: 'post',
    data
  })
}

/**
 * 删除电子票据
 * @param {Array} billIds - 票据ID数组
 */
export function deleteElectronicBill(billIds) {
  return request({
    url: '/qqsk/bill/electronic/delete',
    method: 'post',
    params: { billIds: billIds.join(',') }
  })
}

/**
 * 数字签名
 * @param {Object} data - 签名数据
 */
export function signElectronicBill(data) {
  return request({
    url: '/qqsk/bill/electronic/sign',
    method: 'post',
    data
  })
}

/**
 * 验证签名
 * @param {Object} data - 验证数据
 */
export function verifyElectronicBill(data) {
  return request({
    url: '/qqsk/bill/electronic/verify',
    method: 'post',
    data
  })
}

/**
 * 追踪票据流转
 * @param {string} billId - 票据ID
 */
export function trackElectronicBill(billId) {
  return request({
    url: `/qqsk/bill/electronic/track/${billId}`,
    method: 'post'
  })
}

/**
 * 获取流转记录
 * @param {string} billId - 票据ID
 */
export function getCirculationRecords(billId) {
  return request({
    url: `/qqsk/bill/electronic/circulation-records/${billId}`,
    method: 'post'
  })
}

/**
 * 导出电子票据数据
 * @param {Object} params - 导出参数
 */
export function exportElectronicBill(params) {
  return request({
    url: '/qqsk/bill/electronic/export',
    method: 'post',
    data: params,
    responseType: 'blob'
  })
}

/**
 * 获取电子票据统计数据
 * @param {Object} params - 统计参数
 */
export function getElectronicBillStatistics(params) {
  return request({
    url: '/qqsk/bill/electronic/statistics',
    method: 'post',
    data: params
  })
}

/**
 * 获取票据趋势分析数据
 * @param {Object} params - 分析参数
 * @param {number} params.days - 天数
 */
export function getBillTrendAnalysis(params) {
  return request({
    url: '/qqsk/bill/electronic/trend-analysis',
    method: 'post',
    data: params
  })
}

/**
 * 背书转让
 * @param {Object} data - 背书数据
 */
export function endorseElectronicBill(data) {
  return request({
    url: '/qqsk/bill/electronic/endorse',
    method: 'post',
    data
  })
}

/**
 * 票据贴现
 * @param {Object} data - 贴现数据
 */
export function discountElectronicBill(data) {
  return request({
    url: '/qqsk/bill/electronic/discount',
    method: 'post',
    data
  })
}

