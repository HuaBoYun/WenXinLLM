import request from '@/utils/request'

/**
 * 指标信息管理API
 * 用于报表数据管理中的指标信息查询
 */

/**
 * 查询指标信息列表（不分页）
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getIndicatorInfoList(data) {
  return request({
    url: '/cwgxAi/enterpriseReport/indicator/list',
    method: 'post',
    data
  })
}

/**
 * 分页查询指标信息列表
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getIndicatorInfoPage(data) {
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
export function getIndicatorInfoDetail(data) {
  return request({
    url: '/cwgxAi/enterpriseReport/indicator/detail',
    method: 'post',
    data
  })
}

/**
 * 根据报表模板ID查询指标列表
 * @param {Object} data 包含templateId
 * @returns {Promise}
 */
export function getIndicatorsByTemplate(data) {
  return request({
    url: '/cwgxAi/enterpriseReport/indicator/getByTemplate',
    method: 'post',
    data
  })
}

/**
 * 根据指标编码查询指标信息
 * @param {Object} data 包含indicatorCode
 * @returns {Promise}
 */
export function getIndicatorByCode(data) {
  return request({
    url: '/cwgxAi/enterpriseReport/indicator/getByCode',
    method: 'post',
    data
  })
}

