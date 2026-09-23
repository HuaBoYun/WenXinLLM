import request from '@/utils/request'

/**
 * 指标信息管理API
 */

/**
 * 分页查询指标列表
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getIndicatorPage(data) {
  return request({
    url: '/cwgxAi/enterpriseReport/indicator/getPage',
    method: 'post',
    data
  })
}

/**
 * 查询指标详情
 * @param {Object} data 包含indicatorId
 * @returns {Promise}
 */
export function getIndicatorDetail(data) {
  return request({
    url: '/cwgxAi/enterpriseReport/indicator/detail',
    method: 'post',
    data
  })
}

/**
 * 保存指标信息（新增/修改）
 * @param {Object} data 指标数据
 * @returns {Promise}
 */
export function saveIndicator(data) {
  return request({
    url: '/cwgxAi/enterpriseReport/indicator/save',
    method: 'post',
    data
  })
}

/**
 * 删除指标
 * @param {Object} data 包含indicatorId
 * @returns {Promise}
 */
export function deleteIndicator(data) {
  return request({
    url: '/cwgxAi/enterpriseReport/indicator/delete',
    method: 'post',
    data
  })
}

/**
 * 批量删除指标
 * @param {Array} indicatorIds 指标ID数组
 * @returns {Promise}
 */
export function batchDeleteIndicator(indicatorIds) {
  return request({
    url: '/cwgxAi/enterpriseReport/indicator/batchDelete',
    method: 'post',
    data: indicatorIds
  })
}

/**
 * 更新指标状态
 * @param {Object} data 包含indicatorId和status
 * @returns {Promise}
 */
export function updateIndicatorStatus(data) {
  return request({
    url: '/cwgxAi/enterpriseReport/indicator/updateStatus',
    method: 'post',
    data
  })
}

