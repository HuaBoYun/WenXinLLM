/**
 * 计费管理相关接口
 * @author huabo
 * @date 2026-04-24
 */
import request from '@/utils/request'

// ==================== 费用标准管理 ====================

/**
 * 获取费用标准树形列表
 * @param {Object} params - { moduletype: '模块类型' }
 */
export function getFeeStandardList(params) {
  return request({
    url: '/setting/system/fee/standard/list',
    method: 'get',
    params,
  })
}

/**
 * 新增/修改费用标准
 * @param {Object} data - { rightId, feeType, feeAmount, remark }
 */
export function saveFeeStandard(data) {
  return request({
    url: '/setting/system/fee/standard/save',
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json' },
  })
}

// ==================== 个人费用管理 ====================

/**
 * 查询个人费用明细
 * @param {Object} data - { startTime, endTime, moduleType, rightId, pageNum, pageSize }
 */
export function getPersonalFeeRecords(data) {
  return request({
    url: '/setting/system/fee/record/personal',
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json' },
  })
}

/**
 * 导出费用数据
 * @param {Object} data - same as query params
 */
export function exportFeeRecords(data) {
  return request({
    url: '/setting/system/fee/record/export',
    method: 'post',
    data,
    responseType: 'blob',
    headers: { 'Content-Type': 'application/json' },
  })
}

// ==================== 统计费用管理 ====================

/**
 * 统计费用查询（支持个人/公司/集团三维度）
 * @param {Object} data - { dimension, startTime, endTime, moduleType, pageNum, pageSize }
 */
export function queryFeeStatistics(data) {
  return request({
    url: '/setting/system/fee/statistics/query',
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json' },
  })
}

/**
 * 穿透查询（大模块下钻到小模块）
 * @param {Object} data - { moduleType, dimension, startTime, endTime }
 */
export function drilldownFeeStatistics(data) {
  return request({
    url: '/setting/system/fee/statistics/drilldown',
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json' },
  })
}

// ==================== 余额与充值管理 ====================

/**
 * 查询公司余额
 * @param {Object} params - { companyOrgId }
 */
export function queryFeeBalance(params) {
  return request({
    url: '/setting/system/fee/balance/query',
    method: 'get',
    params,
  })
}

/**
 * 登录后余额检查
 * 返回码：1=正常，20010=云端版余额不足（提示），40010=离线版余额不足（拦截）
 */
export function checkFeeBalance() {
  return request({
    url: '/setting/system/fee/balance/check',
    method: 'get',
  })
}

/**
 * 密钥充值
 * @param {Object} data - { licenseKey }
 */
export function rechargeLicense(data) {
  return request({
    url: '/setting/system/fee/license/recharge',
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json' },
  })
}

/**
 * 查询充值记录列表
 * @param {Object} params - { companyOrgId, pageNum, pageSize }
 */
export function getLicenseRecords(params) {
  return request({
    url: '/setting/system/fee/license/records',
    method: 'get',
    params,
  })
}

/**
 * 生成密钥（仅云端管理员可用）
 * @param {Object} data - { companyOrgId, rechargeAmount, expireTime }
 */
export function generateLicense(data) {
  return request({
    url: '/setting/system/fee/license/generate',
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json' },
  })
}


// ==================== 密钥购买管理 ====================

export function submitLicenseOrder(data) {
  return request({ url: '/setting/system/fee/order/submit', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}

export function getLicenseOrderList(params) {
  return request({ url: '/setting/system/fee/order/list', method: 'get', params })
}

export function getLicenseOrderDetail(params) {
  return request({ url: '/setting/system/fee/order/detail', method: 'get', params })
}

export function approveLicenseOrder(data) {
  return request({ url: '/setting/system/fee/order/approve', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}

export function generateOrderKey(data) {
  return request({ url: '/setting/system/fee/order/generateKey', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}

export function exportLicenseOrders(params) {
  return request({ url: '/setting/system/fee/order/export', method: 'get', params, responseType: 'blob' })
}
