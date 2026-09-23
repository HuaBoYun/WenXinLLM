import request from '@/utils/request'

/**
 * 债权管理API接口
 * @author 示例云开发团队
 * @since 2025-01-06
 */

// ==================== 项目查询 ====================

/**
 * 查询项目列表（用于债权项目选择）
 * @param {Object} params 查询参数
 * @returns {Promise} 项目列表
 */
export function getProjectList(params) {
  return request({
    url: '/contract/project/info/register/list',
    method: 'post',
    data: params,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// ==================== 债权管理 ====================

/**
 * 创建债权记录
 * @param {Object} data 债权数据
 * @returns {Promise} 创建结果
 */
export function createDebtRecord(data) {
  return request({
    url: '/contract/debt/save',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 查询债权列表
 * @param {Object} params 查询参数
 * @returns {Promise} 分页结果
 */
export function getDebtRecordList(params) {
  return request({
    url: '/contract/debt/list',
    method: 'post',
    data: params,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 获取债权详情
 * @param {String} id 债权ID
 * @returns {Promise} 债权详情
 */
export function getDebtRecordById(id) {
  return request({
    url: `/contract/debt/${id}`,
    method: 'get'
  })
}

/**
 * 更新债权记录
 * @param {Object} data 更新数据（包含id）
 * @returns {Promise} 更新结果
 */
export function updateDebtRecord(data) {
  // 验证ID是否有效
  if (!data || !data.id || data.id === 'undefined' || data.id === undefined) {
    return Promise.reject(new Error('更新失败：缺少有效的ID参数'))
  }

  return request({
    url: `/contract/debt/${data.id}`,
    method: 'put',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 债权账龄分析
 * @param {Object} params 查询参数
 * @returns {Promise} 分析结果
 */
export function getDebtAgingAnalysis(params) {
  return request({
    url: '/contract/debt/aging/analysis',
    method: 'get',
    params: params
  })
}

/**
 * 导出账龄分析报告
 * @param {Object} params 导出参数
 * @returns {Promise} 导出结果
 */
export function exportDebtAgingReport(params) {
  return request({
    url: '/contract/debt/aging/export',
    method: 'post',
    data: params,
    responseType: 'blob',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 删除债权记录
 * @param {String} id 债权ID
 * @returns {Promise} 删除结果
 */
export function deleteDebtRecord(id) {
  return request({
    url: `/contract/debt/${id}`,
    method: 'delete'
  })
}

// ==================== 债权收款记录 ====================

/**
 * 创建收款记录
 * @param {Object} data 收款数据
 * @returns {Promise} 创建结果
 */
export function createDebtCollection(data) {
  return request({
    url: '/contract/debt/collection/create',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 查询收款记录列表
 * @param {Object} params 查询参数
 * @returns {Promise} 收款记录列表
 */
export function getDebtCollectionList(params) {
  return request({
    url: '/contract/debt/collection/list',
    method: 'get',
    params: params
  })
}

/**
 * 收款核实
 * @param {String} id 收款记录ID
 * @param {Object} data 核实数据
 * @returns {Promise} 核实结果
 */
export function verifyDebtCollection(id, data) {
  return request({
    url: `/contract/debt/collection/verify/${id}`,
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 更新收款记录
 * @param {Object} data 更新数据（包含id）
 * @returns {Promise} 更新结果
 */
export function updateDebtCollection(data) {
  return request({
    url: `/contract/debt/collection/${data.id}`,
    method: 'put',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 删除收款记录
 * @param {String} id 收款记录ID
 * @returns {Promise} 删除结果
 */
export function deleteDebtCollection(id) {
  return request({
    url: `/contract/debt/collection/${id}`,
    method: 'delete'
  })
}

/**
 * 获取催收数据
 * @param {Object} params 查询参数
 * @returns {Promise} 催收数据
 */
export function getDebtCollectionData(params) {
  return request({
    url: '/contract/debt/collection/data',
    method: 'get',
    params: params
  })
}

/**
 * 保存催收数据
 * @param {Object} data 催收数据
 * @returns {Promise} 保存结果
 */
export function saveDebtCollectionData(data) {
  return request({
    url: '/contract/debt/collection/save',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 获取回收数据
 * @param {Object} params 查询参数
 * @returns {Promise} 回收数据
 */
export function getDebtRecoveryData(params) {
  return request({
    url: '/contract/debt/recovery/data',
    method: 'get',
    params: params
  })
}

/**
 * 保存回收数据
 * @param {Object} data 回收数据
 * @returns {Promise} 保存结果
 */
export function saveDebtRecoveryData(data) {
  return request({
    url: '/contract/debt/recovery/save',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
