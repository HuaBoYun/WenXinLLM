import request from '@/utils/request'

/**
 * 维度信息管理API
 */

/**
 * 分页查询维度列表
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getDimensionList(data) {
  return request({
    url: '/groupControl/dimension/getList',
    method: 'post',
    data
  })
}

/**
 * 查询维度详情
 * @param {Object} data 包含dimensionId
 * @returns {Promise}
 */
export function getDimensionDetail(data) {
  return request({
    url: '/groupControl/dimension/detail',
    method: 'post',
    data
  })
}

/**
 * 保存维度信息（新增/修改）
 * @param {Object} data 维度信息
 * @returns {Promise}
 */
export function saveDimension(data) {
  return request({
    url: '/groupControl/dimension/save',
    method: 'post',
    data
  })
}

/**
 * 删除维度
 * @param {Object} data 包含dimensionId
 * @returns {Promise}
 */
export function deleteDimension(data) {
  return request({
    url: '/groupControl/dimension/delete',
    method: 'post',
    data
  })
}

/**
 * 批量删除维度
 * @param {Array} dimensionIds 维度ID数组
 * @returns {Promise}
 */
export function batchDeleteDimension(dimensionIds) {
  return request({
    url: '/groupControl/dimension/batchDelete',
    method: 'post',
    data: { dimensionIds }
  })
}

/**
 * 更新维度状态
 * @param {Object} data 包含dimensionId和status
 * @returns {Promise}
 */
export function updateDimensionStatus(data) {
  return request({
    url: '/groupControl/dimension/updateStatus',
    method: 'post',
    data
  })
}

