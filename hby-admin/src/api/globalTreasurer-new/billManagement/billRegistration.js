import request from '@/utils/request'

/**
 * 票据登记管理 API
 * @author 示例云开发团队
 * @since 2025-02-02
 */

/**
 * 分页查询票据登记列表
 * @param {Object} params - 查询参数
 * @returns {Promise}
 */
export function getBillRegistrationPage(params) {
  return request({
    url: '/qqsk/bill/registration/page',
    method: 'post',
    data: params
  })
}

/**
 * 获取票据登记详情
 * @param {Number|String} billId - 票据ID
 * @returns {Promise}
 */
export function getBillRegistrationDetail(billId) {
  return request({
    url: `/qqsk/bill/registration/detail/${billId}`,
    method: 'post'
  })
}

/**
 * 创建票据登记
 * @param {Object} data - 票据登记信息
 * @returns {Promise}
 */
export function createBillRegistration(data) {
  return request({
    url: '/qqsk/bill/registration/save',
    method: 'post',
    data
  })
}

/**
 * 更新票据登记
 * @param {Object} data - 票据登记信息
 * @returns {Promise}
 */
export function updateBillRegistration(data) {
  return request({
    url: '/qqsk/bill/registration/update',
    method: 'post',
    data
  })
}

/**
 * 删除票据登记
 * @param {Array} billIds - 票据ID数组
 * @returns {Promise}
 */
export function deleteBillRegistration(billIds) {
  return request({
    url: '/qqsk/bill/registration/delete',
    method: 'post',
    data: billIds
  })
}

/**
 * 批量导入票据
 * @param {FormData} data - 包含文件的FormData
 * @returns {Promise}
 */
export function batchImportBills(data) {
  return request({
    url: '/qqsk/bill/registration/batch-import',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 作废票据
 * @param {Number|String} billId - 票据ID
 * @param {String} reason - 作废原因
 * @returns {Promise}
 */
export function cancelBill(billId, reason) {
  return request({
    url: `/qqsk/bill/registration/cancel/${billId}`,
    method: 'post',
    data: { reason }
  })
}

/**
 * 导出票据登记数据
 * @param {Object} params - 查询参数
 * @returns {Promise}
 */
export function exportBillRegistration(params) {
  return request({
    url: '/qqsk/bill/registration/export',
    method: 'post',
    data: params,
    responseType: 'blob'
  })
}

/**
 * 获取票据登记统计数据
 * @param {Object} params - 查询参数
 * @returns {Promise}
 */
export function getBillRegistrationStatistics(params) {
  return request({
    url: '/qqsk/bill/registration/statistics',
    method: 'post',
    data: params
  })
}

