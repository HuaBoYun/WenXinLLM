import request from '@/utils/request'

/**
 * 项目运营管理API接口
 * @author 示例云开发团队
 * @since 2025-01-06
 */

// ==================== 项目运营管理 ====================

/**
 * 创建项目运营管理
 * @param {Object} data 运营数据
 * @returns {Promise} 创建结果
 */
export function createProjectOperations(data) {
  return request({
    url: '/contract/operations/save',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 查询项目运营管理列表
 * @param {Object} params 查询参数
 * @returns {Promise} 分页结果
 */
export function getProjectOperationsList(params) {
  return request({
    url: '/contract/operations/list',
    method: 'post',
    data: params,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 获取项目运营管理详情
 * @param {String} id 运营ID
 * @returns {Promise} 运营详情
 */
export function getProjectOperationsById(id) {
  return request({
    url: `/contract/operations/${id}`,
    method: 'get'
  })
}

/**
 * 更新项目运营管理
 * @param {Object} data 运营数据
 * @returns {Promise} 更新结果
 */
export function updateProjectOperations(data) {
  return request({
    url: '/contract/operations/save',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 删除项目运营管理
 * @param {String} id 运营ID
 * @returns {Promise} 删除结果
 */
export function deleteProjectOperations(id) {
  return request({
    url: `/contract/operations/${id}`,
    method: 'delete'
  })
}

/**
 * 批量删除项目运营管理
 * @param {Array} ids 运营ID列表
 * @returns {Promise} 删除结果
 */
export function batchDeleteProjectOperations(ids) {
  return request({
    url: '/contract/operations/batchDelete',
    method: 'post',
    data: ids,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 根据项目ID查询运营管理列表
 * @param {String} projectId 项目ID
 * @returns {Promise} 运营列表
 */
export function getOperationsByProjectId(projectId) {
  return request({
    url: `/contract/operations/project/${projectId}`,
    method: 'get'
  })
}

/**
 * 根据运营类型查询运营管理列表
 * @param {Number} operationsType 运营类型
 * @returns {Promise} 运营列表
 */
export function getOperationsByType(operationsType) {
  return request({
    url: `/contract/operations/type/${operationsType}`,
    method: 'get'
  })
}

/**
 * 根据运营状态查询运营管理列表
 * @param {Number} operationsStatus 运营状态
 * @returns {Promise} 运营列表
 */
export function getOperationsByStatus(operationsStatus) {
  return request({
    url: `/contract/operations/status/${operationsStatus}`,
    method: 'get'
  })
}

/**
 * 根据风险等级查询运营管理列表
 * @param {Number} riskLevel 风险等级
 * @returns {Promise} 运营列表
 */
export function getOperationsByRiskLevel(riskLevel) {
  return request({
    url: `/contract/operations/risk/${riskLevel}`,
    method: 'get'
  })
}

/**
 * 获取我的项目运营管理列表
 * @param {String} managerId 负责人ID
 * @returns {Promise} 运营列表
 */
export function getMyProjectOperations(managerId) {
  return request({
    url: `/contract/operations/my/${managerId}`,
    method: 'get'
  })
}

/**
 * 获取正常状态的运营管理列表
 * @returns {Promise} 运营列表
 */
export function getNormalOperations() {
  return request({
    url: '/contract/operations/normal',
    method: 'get'
  })
}

/**
 * 获取预警状态的运营管理列表
 * @returns {Promise} 运营列表
 */
export function getWarningOperations() {
  return request({
    url: '/contract/operations/warning',
    method: 'get'
  })
}

/**
 * 获取异常状态的运营管理列表
 * @returns {Promise} 运营列表
 */
export function getAbnormalOperations() {
  return request({
    url: '/contract/operations/abnormal',
    method: 'get'
  })
}

/**
 * 获取高风险的运营管理列表
 * @returns {Promise} 运营列表
 */
export function getHighRiskOperations() {
  return request({
    url: '/contract/operations/highRisk',
    method: 'get'
  })
}

/**
 * 获取盈利的运营管理列表
 * @returns {Promise} 运营列表
 */
export function getProfitableOperations() {
  return request({
    url: '/contract/operations/profitable',
    method: 'get'
  })
}

/**
 * 获取亏损的运营管理列表
 * @returns {Promise} 运营列表
 */
export function getLossOperations() {
  return request({
    url: '/contract/operations/loss',
    method: 'get'
  })
}

/**
 * 搜索项目运营管理
 * @param {String} keyword 关键词
 * @param {Number} limit 限制数量
 * @returns {Promise} 运营列表
 */
export function searchProjectOperations(keyword, limit = 20) {
  return request({
    url: '/contract/operations/search',
    method: 'get',
    params: { keyword, limit }
  })
}

/**
 * 获取运营管理统计数据
 * @param {Object} params 查询参数
 * @returns {Promise} 统计数据
 */
export function getOperationsStatistics(params) {
  return request({
    url: '/contract/operations/statistics',
    method: 'post',
    data: params,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 获取运营类型分布统计
 * @param {Object} params 查询参数
 * @returns {Promise} 类型分布
 */
export function getOperationsTypeDistribution(params) {
  return request({
    url: '/contract/operations/typeDistribution',
    method: 'post',
    data: params,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 获取运营状态分布统计
 * @param {Object} params 查询参数
 * @returns {Promise} 状态分布
 */
export function getOperationsStatusDistribution(params) {
  return request({
    url: '/contract/operations/statusDistribution',
    method: 'post',
    data: params,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 生成运营编号
 * @returns {Promise} 运营编号
 */
export function generateOperationsNo() {
  return request({
    url: '/contract/operations/generateOperationsNo',
    method: 'get'
  })
}

/**
 * 导出运营管理数据
 * @param {Object} params 导出参数
 * @returns {Promise} 导出结果
 */
export function exportOperationsData(params) {
  return request({
    url: '/contract/operations/export',
    method: 'post',
    data: params,
    responseType: 'blob',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 更新运营状态
 * @param {String} id 运营ID
 * @param {Number} status 状态
 * @returns {Promise} 更新结果
 */
export function updateOperationsStatus(id, status) {
  return request({
    url: `/contract/operations/updateStatus/${id}`,
    method: 'post',
    params: { status }
  })
}

/**
 * 批量更新运营状态
 * @param {Array} ids 运营ID列表
 * @param {Number} status 状态
 * @returns {Promise} 更新结果
 */
export function batchUpdateOperationsStatus(ids, status) {
  return request({
    url: '/contract/operations/batchUpdateStatus',
    method: 'post',
    data: { ids, status },
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
