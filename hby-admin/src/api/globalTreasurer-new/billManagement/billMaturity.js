/**
 * 票据到期管理 API
 * @module api/globalTreasurer-new/billManagement/billMaturity
 */
import request from '@/utils/request'
import qs from 'qs'

/**
 * 分页查询票据到期列表
 * @param {Object} params - 查询参数
 */
export function getBillMaturityPage(params) {
  return request({
    url: '/qqsk/bill/maturity/page',
    method: 'post',
    data: params
  })
}

/**
 * 获取票据到期详情
 * @param {string} billId - 票据ID
 */
export function getBillMaturityDetail(billId) {
  return request({
    url: `/qqsk/bill/maturity/detail/${billId}`,
    method: 'post'
  })
}

/**
 * 处理到期票据
 * @param {Object} data - 处理数据
 */
export function processBillMaturity(data) {
  return request({
    url: '/qqsk/bill/maturity/process',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 批量处理到期票据
 * @param {Object} data - 批量处理数据
 */
export function batchProcessBillMaturity(data) {
  return request({
    url: '/qqsk/bill/maturity/batch-process',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 发送到期提醒
 * @param {Object} data - 提醒数据
 */
export function sendMaturityReminder(data) {
  return request({
    url: '/qqsk/bill/maturity/reminder',
    method: 'post',
    params: data,
    paramsSerializer: params => qs.stringify(params, { arrayFormat: 'repeat' })
  })
}

/**
 * 获取到期日历数据
 * @param {Object} params - 查询参数
 */
export function getMaturityCalendarData(params) {
  return request({
    url: '/qqsk/bill/maturity/calendar',
    method: 'post',
    data: params
  })
}

/**
 * 获取票据处理历史
 * @param {string} billId - 票据ID
 */
export function getBillHistory(billId) {
  return request({
    url: `/qqsk/bill/history/${billId}`,
    method: 'post'
  })
}

/**
 * 导出到期数据
 * @param {Object} params - 导出参数
 */
export function exportBillMaturity(params) {
  return request({
    url: '/qqsk/bill/maturity/export',
    method: 'post',
    data: params,
    responseType: 'blob'
  })
}

/**
 * 获取到期统计数据
 * @param {Object} params - 统计参数
 */
export function getBillMaturityStatistics(params) {
  return request({
    url: '/qqsk/bill/maturity/statistics',
    method: 'post',
    data: params
  })
}

/**
 * 延期申请
 * @param {Object} data - 延期数据
 */
export function applyExtension(data) {
  return request({
    url: '/qqsk/bill/maturity/extend',
    method: 'post',
    data
  })
}

/**
 * 托收申请
 * @param {Object} data - 托收数据
 */
export function applyCollection(data) {
  return request({
    url: '/qqsk/bill/maturity/collect',
    method: 'post',
    data
  })
}

/**
 * 获取到期趋势数据
 * @param {Object} params - 查询参数
 */
export function getMaturityTrendData(params) {
  return request({
    url: '/qqsk/bill/maturity/trend',
    method: 'post',
    data: params
  })
}

