import request from '@/utils/request'

/**
 * 启动验证任务
 * @param {Object} data 验证请求参数
 * @returns {Promise}
 */
export function startValidation(data) {
  return request({
    url: '/finance/validation/start',
    method: 'post',
    data
  })
}

/**
 * 验证数据完整性
 * @param {String} collectionTaskId 采集任务ID
 * @returns {Promise}
 */
export function validateCompleteness(collectionTaskId) {
  return request({
    url: `/finance/validation/completeness/${collectionTaskId}`,
    method: 'post'
  })
}

/**
 * 验证数据一致性
 * @param {String} collectionTaskId 采集任务ID
 * @returns {Promise}
 */
export function validateConsistency(collectionTaskId) {
  return request({
    url: `/finance/validation/consistency/${collectionTaskId}`,
    method: 'post'
  })
}

/**
 * 验证数据准确性
 * @param {String} collectionTaskId 采集任务ID
 * @returns {Promise}
 */
export function validateAccuracy(collectionTaskId) {
  return request({
    url: `/finance/validation/accuracy/${collectionTaskId}`,
    method: 'post'
  })
}

/**
 * 获取验证报告
 * @param {String} reportId 报告ID
 * @returns {Promise}
 */
export function getValidationReport(reportId) {
  return request({
    url: `/finance/validation/report/${reportId}`,
    method: 'get'
  })
}

/**
 * 获取验证报告列表
 * @param {Number} pageNumber 页码
 * @param {Number} pageSize 每页数量
 * @returns {Promise}
 */
export function getValidationReportList(pageNumber, pageSize) {
  return request({
    url: '/finance/validation/reportList',
    method: 'get',
    params: {
      pageNumber,
      pageSize
    }
  })
}

/**
 * 删除验证报告
 * @param {String} reportId 报告ID
 * @returns {Promise}
 */
export function deleteValidationReport(reportId) {
  return request({
    url: `/finance/validation/delete/${reportId}`,
    method: 'delete'
  })
}

