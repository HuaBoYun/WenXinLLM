import request from '@/utils/request'

/**
 * 财资公共模块 - 电票账户配置 API
 */

// ==================== 电票账户配置管理 ====================

/**
 * 分页查询电票账户列表
 * @param {Object} params - 查询参数
 * @param {Number} params.page - 页码
 * @param {Number} params.pageSize - 每页数量
 * @param {String} params.accountName - 账户名称
 * @param {String} params.accountType - 账户类型
 * @param {String} params.billType - 票据类型
 * @param {String} params.status - 状态
 */
export function getETicketAccountPage(params) {
  return request({
    url: '/qqsk/financial/basicConfig/eTicketAccount/list',
    method: 'post',
    data: params,
    headers: { 'Content-Type': 'application/json' }
  })
}

/**
 * 获取电票账户详情
 * @param {Number|String} id - 账户ID
 */
export function getETicketAccountDetail(id) {
  return request({
    url: '/qqsk/financial/basicConfig/eTicketAccount/detail',
    method: 'get',
    params: { id }
  })
}

/**
 * 新增电票账户
 * @param {Object} data - 电票账户数据
 */
export function createETicketAccount(data) {
  return request({
    url: '/qqsk/financial/basicConfig/eTicketAccount/add',
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json' }
  })
}

/**
 * 更新电票账户
 * @param {Object} data - 电票账户数据
 */
export function updateETicketAccount(data) {
  return request({
    url: '/qqsk/financial/basicConfig/eTicketAccount/update',
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json' }
  })
}

/**
 * 保存或更新电票账户（统一接口）
 * @param {Object} data - 电票账户数据
 */
export function saveOrUpdateETicketAccount(data) {
  return request({
    url: '/qqsk/financial/basicConfig/eTicketAccount/saveOrUpdate',
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json' }
  })
}

/**
 * 删除电票账户
 * @param {Number|String} id - 账户ID
 */
export function deleteETicketAccount(id) {
  return request({
    url: '/qqsk/financial/basicConfig/eTicketAccount/delete',
    method: 'post',
    data: { id },
    headers: { 'Content-Type': 'application/json' }
  })
}

/**
 * 同步账户状态
 * @param {Number|String} id - 账户ID
 */
export function syncAccountStatus(id) {
  return request({
    url: '/qqsk/financial/basicConfig/eTicketAccount/syncStatus',
    method: 'post',
    data: { id },
    headers: { 'Content-Type': 'application/json' }
  })
}

/**
 * 批量同步账户状态
 * @param {Array} ids - 账户ID数组
 */
export function batchSyncAccountStatus(ids) {
  return request({
    url: '/qqsk/financial/basicConfig/eTicketAccount/batchSync',
    method: 'post',
    data: { ids },
    headers: { 'Content-Type': 'application/json' }
  })
}

/**
 * 导出电票账户配置
 * @param {Object} params - 查询参数
 */
export function exportETicketAccount(params) {
  return request({
    url: '/qqsk/financial/basicConfig/eTicketAccount/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

/**
 * 获取账户统计信息（正常账户数、总授信额度、今日新增数）
 */
export function getETicketAccountStatistics() {
  return request({
    url: '/qqsk/financial/basicConfig/eTicketAccount/statistics',
    method: 'get'
  })
}

/**
 * 获取账户类型下拉数据
 */
export function getAccountTypes() {
  return request({
    url: '/qqsk/financial/basicConfig/eTicketAccount/getAccountTypes',
    method: 'get'
  })
}

/**
 * 获取票据类型下拉数据
 */
export function getBillTypes() {
  return request({
    url: '/qqsk/financial/basicConfig/eTicketAccount/getBillTypes',
    method: 'get'
  })
}
