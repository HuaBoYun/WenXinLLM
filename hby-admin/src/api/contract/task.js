import request from '@/utils/request'

/**
 * 任务书管理API接口
 * @author 示例云开发团队
 * @since 2025-01-06
 */

// ==================== 任务书管理 ====================

/**
 * 创建任务书
 * @param {Object} data 任务书数据
 * @returns {Promise} 创建结果
 */
export function createTaskAssignment(data) {
  return request({
    url: '/contract/task/save',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 查询任务书列表
 * @param {Object} params 查询参数
 * @returns {Promise} 分页结果
 */
export function getTaskAssignmentList(params) {
  return request({
    url: '/contract/task/list',
    method: 'post',
    data: params,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 获取任务书详情
 * @param {String} id 任务书ID
 * @returns {Promise} 任务书详情
 */
export function getTaskAssignmentById(id) {
  return request({
    url: `/contract/task/${id}`,
    method: 'get'
  })
}

/**
 * 更新任务书
 * @param {String} id 任务书ID
 * @param {Object} data 更新数据
 * @returns {Promise} 更新结果
 */
export function updateTaskAssignment(id, data) {
  return request({
    url: `/contract/task/${id}`,
    method: 'put',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 任务书审核
 * @param {String} id 任务书ID
 * @param {Object} data 审核数据
 * @returns {Promise} 审核结果
 */
export function reviewTaskAssignment(id, data) {
  return request({
    url: `/contract/task/review/${id}`,
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 任务书验收
 * @param {String} id 任务书ID
 * @param {Object} data 验收数据
 * @returns {Promise} 验收结果
 */
export function acceptTaskAssignment(id, data) {
  return request({
    url: `/contract/task/accept/${id}`,
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 删除任务书
 * @param {String} id 任务书ID
 * @returns {Promise} 删除结果
 */
export function deleteTaskAssignment(id) {
  return request({
    url: `/contract/task/${id}`,
    method: 'delete'
  })
}

/**
 * 任务书下达
 * @param {String} id 任务书ID
 * @param {Object} data 下达数据
 * @returns {Promise} 下达结果
 */
export function issueTaskAssignment(id, data) {
  return request({
    url: `/contract/task/issue/${id}`,
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 获取任务书模板
 * @param {Object} params 查询参数
 * @returns {Promise} 模板列表
 */
export function getTaskTemplateList(params) {
  return request({
    url: '/contract/task/template/list',
    method: 'get',
    params: params
  })
}

/**
 * 基于合同生成任务书
 * @param {String} contractId 合同ID
 * @returns {Promise} 生成结果
 */
export function generateTaskFromContract(contractId) {
  return request({
    url: `/contract/task/generate/contract/${contractId}`,
    method: 'post'
  })
}
