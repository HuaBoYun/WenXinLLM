/*
 * @Description: 财务共享 - 集团绩效平台 - 维度信息管理 API
 * @Author: 示例云开发团队
 * @Date: 2026-01-30
 */
import request from '@/utils/request'
import { transData } from '@/utils/requestData'

/**
 * 分页查询维度列表
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getDimensionList(data) {
  return request({
    url: '/cwgxAi/groupControl/dimension/getList',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 查询维度详情
 * @param {String} dimensionId 维度ID
 * @returns {Promise}
 */
export function getDimensionDetail(dimensionId) {
  return request({
    url: '/cwgxAi/groupControl/dimension/detail',
    method: 'post',
    data: transData({ dimensionId })
  })
}

/**
 * 保存维度信息
 * @param {Object} data 维度数据
 * @returns {Promise}
 */
export function saveDimension(data) {
  return request({
    url: '/cwgxAi/groupControl/dimension/save',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 删除维度
 * @param {String} dimensionId 维度ID
 * @returns {Promise}
 */
export function deleteDimension(dimensionId) {
  return request({
    url: '/cwgxAi/groupControl/dimension/delete',
    method: 'post',
    data: transData({ dimensionId })
  })
}

/**
 * 批量删除维度
 * @param {Array} dimensionIds 维度ID数组
 * @returns {Promise}
 */
export function batchDeleteDimension(dimensionIds) {
  if (!Array.isArray(dimensionIds) || dimensionIds.length === 0) {
    return Promise.reject(new Error('请选择要删除的维度'))
  }

  return request({
    url: '/cwgxAi/groupControl/dimension/batchDelete',
    method: 'post',
    data: transData({ dimensionIds })
  })
}

/**
 * 更新维度状态
 * @param {String} dimensionId 维度ID
 * @param {String} status 状态(ACTIVE/INACTIVE)
 * @returns {Promise}
 */
export function updateDimensionStatus(dimensionId, status) {
  return request({
    url: '/cwgxAi/groupControl/dimension/updateStatus',
    method: 'post',
    data: transData({ dimensionId, status })
  })
}

export default {
  getDimensionList,
  getDimensionDetail,
  saveDimension,
  deleteDimension,
  batchDeleteDimension,
  updateDimensionStatus
}

