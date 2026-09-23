import request from '@/utils/request'

/**
 * 维度成员管理API
 */

/**
 * 查询成员树
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getDimensionMemberTree(data) {
  return request({
    url: '/groupControl/dimensionMember/getTree',
    method: 'post',
    data
  })
}

/**
 * 查询成员详情
 * @param {Object} data 包含memberId
 * @returns {Promise}
 */
export function getDimensionMemberDetail(data) {
  return request({
    url: '/groupControl/dimensionMember/detail',
    method: 'post',
    data
  })
}

/**
 * 保存成员信息（新增/修改）
 * @param {Object} data 成员信息
 * @returns {Promise}
 */
export function saveDimensionMember(data) {
  return request({
    url: '/groupControl/dimensionMember/save',
    method: 'post',
    data
  })
}

/**
 * 删除成员
 * @param {Object} data 包含memberId
 * @returns {Promise}
 */
export function deleteDimensionMember(data) {
  return request({
    url: '/groupControl/dimensionMember/delete',
    method: 'post',
    data
  })
}

/**
 * 批量删除成员
 * @param {Array} memberIds 成员ID数组
 * @returns {Promise}
 */
export function batchDeleteDimensionMember(memberIds) {
  return request({
    url: '/groupControl/dimensionMember/batchDelete',
    method: 'post',
    data: memberIds
  })
}

/**
 * 移动成员
 * @param {Object} data 包含memberId, targetParentId, sortNo
 * @returns {Promise}
 */
export function moveDimensionMember(data) {
  return request({
    url: '/groupControl/dimensionMember/move',
    method: 'post',
    data
  })
}

/**
 * 更新成员状态
 * @param {Object} data 包含memberId和status
 * @returns {Promise}
 */
export function updateDimensionMemberStatus(data) {
  return request({
    url: '/groupControl/dimensionMember/updateStatus',
    method: 'post',
    data
  })
}

