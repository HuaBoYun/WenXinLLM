import request from '@/utils/request'

/**
 * 项目预算API接口
 * @author 示例云开发团队
 * @since 2025-01-06
 */

// ==================== 项目预算管理 ====================

/**
 * 创建项目预算
 * @param {Object} data 预算数据
 * @returns {Promise} 创建结果
 */
export function createProjectBudget(data) {
  return request({
    url: '/contract/budget/save',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 查询项目预算列表
 * @param {Object} params 查询参数
 * @returns {Promise} 分页结果
 */
export function getProjectBudgetList(params) {
  return request({
    url: '/contract/budget/list',
    method: 'post',
    data: params,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 获取项目预算详情
 * @param {String} id 预算ID
 * @returns {Promise} 预算详情
 */
export function getProjectBudgetById(id) {
  return request({
    url: `/contract/budget/${id}`,
    method: 'get'
  })
}

/**
 * 更新项目预算
 * @param {Object} data 更新数据（包含id）
 * @returns {Promise} 更新结果
 */
export function updateProjectBudget(data) {
  return request({
    url: '/contract/project/budget/save',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 预算审批
 * @param {String} id 预算ID
 * @param {Object} data 审批数据
 * @returns {Promise} 审批结果
 */
export function approveProjectBudget(id, data) {
  return request({
    url: `/contract/budget/approve/${id}`,
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 删除项目预算
 * @param {Number} id 预算ID
 * @returns {Promise} 删除结果
 */
export function deleteProjectBudget(id) {
  return request({
    url: `/contract/budget/${id}`,
    method: 'delete'
  })
}

/**
 * 批量删除项目预算
 * @param {Array} ids 预算ID列表
 * @returns {Promise} 删除结果
 */
export function batchDeleteProjectBudget(ids) {
  return request({
    url: '/contract/budget/batchDelete',
    method: 'post',
    data: ids,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// ==================== 预算明细管理 ====================

/**
 * 创建预算明细
 * @param {Object} data 明细数据
 * @returns {Promise} 创建结果
 */
export function createBudgetDetail(data) {
  return request({
    url: '/contract/budget/detail/save',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 查询预算明细列表
 * @param {Object} params 查询参数
 * @returns {Promise} 明细列表
 */
export function getBudgetDetailList(params) {
  return request({
    url: '/contract/budget/detail/list',
    method: 'post',
    data: params,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 批量创建预算明细
 * @param {Array} data 明细数据数组
 * @returns {Promise} 创建结果
 */
export function batchCreateBudgetDetail(data) {
  return request({
    url: '/contract/budget/detail/batch',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 更新预算明细
 * @param {String} id 明细ID
 * @param {Object} data 更新数据
 * @returns {Promise} 更新结果
 */
export function updateBudgetDetail(id, data) {
  return request({
    url: `/contract/budget/detail/${id}`,
    method: 'put',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 删除预算明细
 * @param {String} id 明细ID
 * @returns {Promise} 删除结果
 */
export function deleteBudgetDetail(id) {
  return request({
    url: `/contract/budget/detail/${id}`,
    method: 'delete'
  })
}
