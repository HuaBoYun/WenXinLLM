import request from '@/utils/request'

/**
 * 项目变更管理API接口
 * @author 示例云开发团队
 * @since 2025-01-06
 */

// ==================== 项目变更管理 ====================

/**
 * 创建项目变更
 * @param {Object} data 变更数据
 * @returns {Promise} 创建结果
 */
export function createProjectChange(data) {
  return request({
    url: '/contract/change/save',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 查询项目变更列表
 * @param {Object} params 查询参数
 * @returns {Promise} 分页结果
 */
export function getProjectChangeList(params) {
  return request({
    url: '/contract/change/list',
    method: 'post',
    data: params,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 获取项目变更详情
 * @param {String} id 变更ID
 * @returns {Promise} 变更详情
 */
export function getProjectChangeById(id) {
  return request({
    url: `/contract/change/${id}`,
    method: 'get'
  })
}

/**
 * 更新项目变更
 * @param {Object} data 更新数据（包含id）
 * @returns {Promise} 更新结果
 */
export function updateProjectChange(data) {
  // 验证ID是否有效
  if (!data || !data.id || data.id === 'undefined' || data.id === undefined) {
    return Promise.reject(new Error('更新失败：缺少有效的ID参数'))
  }

  return request({
    url: `/contract/change/${data.id}`,
    method: 'put',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 变更评审
 * @param {String} id 变更ID
 * @param {Object} data 评审数据
 * @returns {Promise} 评审结果
 */
export function reviewProjectChange(id, data) {
  return request({
    url: `/contract/change/review/${id}`,
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 变更审批
 * @param {String} id 变更ID
 * @param {Object} data 审批数据
 * @returns {Promise} 审批结果
 */
export function approveProjectChange(id, data) {
  return request({
    url: `/contract/change/approve/${id}`,
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 变更实施
 * @param {String} id 变更ID
 * @param {Object} data 实施数据
 * @returns {Promise} 实施结果
 */
export function implementProjectChange(id, data) {
  return request({
    url: `/contract/change/implement/${id}`,
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 删除项目变更
 * @param {String} id 变更ID
 * @returns {Promise} 删除结果
 */
export function deleteProjectChange(id) {
  return request({
    url: `/contract/change/${id}`,
    method: 'delete'
  })
}

/**
 * 获取变更实施数据
 * @param {Object} params 查询参数
 * @returns {Promise} 实施数据
 */
export function getProjectChangeImplementationData(params) {
  return request({
    url: '/contract/change/implementation/data',
    method: 'get',
    params: params
  })
}

/**
 * 提交变更实施
 * @param {Object} data 实施数据
 * @returns {Promise} 提交结果
 */
export function submitProjectChangeImplementation(data) {
  return request({
    url: '/contract/change/implementation/submit',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 获取变更审核历史
 * @param {Object} params 查询参数
 * @returns {Promise} 审核历史
 */
export function getProjectChangeReviewHistory(params) {
  return request({
    url: '/contract/change/review/history',
    method: 'get',
    params: params
  })
}

/**
 * 提交变更审核
 * @param {Object} data 审核数据
 * @returns {Promise} 审核结果
 */
export function submitProjectChangeReview(data) {
  return request({
    url: '/contract/change/review/submit',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 获取项目登记列表（用于项目选择）
 * @param {Object} params 查询参数
 * @returns {Promise} 项目列表
 */
export function getProjectList(params) {
  return request({
    url: '/contract/change/projects',
    method: 'post',
    data: params,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
