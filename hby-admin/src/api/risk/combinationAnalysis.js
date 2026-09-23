/**
 * 指标组合分析API接口
 * @author 示例云
 * @date 2025-09-30
 */

import request from '@/utils/request'

// 基础API路径
const API_BASE = '/api/combination'

/**
 * 获取组合列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getCombinationList(params) {
  return request({
    url: `${API_BASE}/list`,
    method: 'post',
    data: params
  })
}

/**
 * 获取组合详情
 * @param {String} combinationId 组合ID
 * @returns {Promise}
 */
export function getCombinationDetail(combinationId) {
  return request({
    url: `${API_BASE}/detail/${combinationId}`,
    method: 'post'
  })
}

/**
 * 保存组合配置
 * @param {Object} data 组合数据
 * @returns {Promise}
 */
export function saveCombination(data) {
  return request({
    url: `${API_BASE}/save`,
    method: 'post',
    data: JSON.stringify(data),
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

/**
 * 删除组合
 * @param {String} combinationId 组合ID
 * @returns {Promise}
 */
export function deleteCombination(combinationId) {
  return request({
    url: `${API_BASE}/delete/${combinationId}`,
    method: 'post'
  })
}

/**
 * 复制组合
 * @param {Object} data 复制参数
 * @returns {Promise}
 */
export function copyCombination(data) {
  return request({
    url: `${API_BASE}/copy`,
    method: 'post',
    data: data
  })
}

/**
 * 获取可用指标列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getAvailableIndicators(params) {
  return request({
    url: `${API_BASE}/indicators/available`,
    method: 'post',
    data: params
  })
}

/**
 * 添加指标到组合
 * @param {Object} data 指标数据
 * @returns {Promise}
 */
export function addIndicatorToCombination(data) {
  return request({
    url: `${API_BASE}/indicators/add`,
    method: 'post',
    data: JSON.stringify(data),
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

/**
 * 移除组合中的指标
 * @param {String} configId 配置ID
 * @returns {Promise}
 */
export function removeIndicatorFromCombination(configId) {
  return request({
    url: `${API_BASE}/indicators/remove/${configId}`,
    method: 'post'
  })
}

/**
 * 更新指标配置
 * @param {Object} data 配置数据
 * @returns {Promise}
 */
export function updateIndicatorConfig(data) {
  return request({
    url: `${API_BASE}/indicators/update`,
    method: 'post',
    data: JSON.stringify(data),
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

/**
 * 验证指标SQL
 * @param {Object} data 验证数据
 * @returns {Promise}
 */
export function validateIndicatorSql(data) {
  return request({
    url: `${API_BASE}/indicators/validate`,
    method: 'post',
    data: data
  })
}

/**
 * 执行组合分析
 * @param {Object} data 执行参数
 * @returns {Promise}
 */
export function executeCombination(data) {
  return request({
    url: `${API_BASE}/execute`,
    method: 'post',
    data: data
  })
}

/**
 * 获取执行状态
 * @param {String} executionId 执行ID
 * @returns {Promise}
 */
export function getExecutionStatus(executionId) {
  return request({
    url: `${API_BASE}/execution/status/${executionId}`,
    method: 'post'
  })
}

/**
 * 获取执行结果
 * @param {String} executionId 执行ID
 * @returns {Promise}
 */
export function getExecutionResult(executionId) {
  return request({
    url: `${API_BASE}/execution/result/${executionId}`,
    method: 'post'
  })
}

/**
 * 取消执行
 * @param {String} executionId 执行ID
 * @returns {Promise}
 */
export function cancelExecution(executionId) {
  return request({
    url: `${API_BASE}/execution/cancel/${executionId}`,
    method: 'post'
  })
}

/**
 * 获取执行历史
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getExecutionHistory(params) {
  return request({
    url: `${API_BASE}/execution/history`,
    method: 'post',
    params: params
  })
}

/**
 * 获取指标结果详情
 * @param {String} resultId 结果ID
 * @param {Object} params 分页参数
 * @returns {Promise}
 */
export function getIndicatorResultDetail(resultId, params) {
  return request({
    url: `${API_BASE}/result/detail/${resultId}`,
    method: 'post',
    params: params
  })
}

/**
 * 执行交集分析
 * @param {Object} data 分析数据
 * @returns {Promise}
 */
export function executeIntersectionAnalysis(data) {
  return request({
    url: `${API_BASE}/analysis/intersection`,
    method: 'post',
    data: JSON.stringify(data),
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

/**
 * 导出分析结果
 * @param {Object} data 导出数据
 * @returns {Promise}
 */
export function exportAnalysisResult(data) {
  return request({
    url: `${API_BASE}/export`,
    method: 'post',
    data: data,
    responseType: 'blob'
  })
}

/**
 * 执行单个指标
 * @param {Object} data 指标执行数据
 * @returns {Promise}
 */
export function executeIndicator(data) {
  return request({
    url: `${API_BASE}/indicator/execute`,
    method: 'post',
    data: data
  })
}

/**
 * 获取流程图配置
 * @param {String} combinationId 组合ID
 * @returns {Promise}
 */
export function getCombinationFlow(combinationId) {
  return request({
    url: `${API_BASE}/flow/${combinationId}`,
    method: 'post'
  })
}

/**
 * 保存流程图配置
 * @param {Object} data 流程图数据
 * @returns {Promise}
 */
export function saveCombinationFlow(data) {
  return request({
    url: `${API_BASE}/flow/save`,
    method: 'post',
    data: JSON.stringify(data),
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

/**
 * 自动生成流程图
 * @param {String} combinationId 组合ID
 * @returns {Promise}
 */
export function generateCombinationFlow(combinationId) {
  return request({
    url: `${API_BASE}/flow/generate/${combinationId}`,
    method: 'post'
  })
}
