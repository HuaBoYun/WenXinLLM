/*
 * @Description: 财务共享 - 异构业财事项模块 API
 * @Author: system
 * @Date: 2024-12-19
 */
import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// ==================== 异构系统管理 API ====================

/**
 * 分页查询异构系统列表
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getHeterogeneousSystemPage(data) {
  return request({
    url: '/cwgxAi/heterogeneous/system/getList',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 注册异构业务系统
 * @param {Object} data 系统数据
 * @returns {Promise}
 */
export function registerHeterogeneousSystem(data) {
  return request({
    url: '/cwgxAi/heterogeneous/system/register',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取异构系统详情
 * @param {String} systemId 系统ID
 * @returns {Promise}
 */
export function getHeterogeneousSystemById(systemId) {
  return request({
    url: `/cwgxAi/heterogeneous/system/${systemId}`,
    method: 'get'
  })
}

/**
 * 更新异构系统
 * @param {String} systemId 系统ID
 * @param {Object} data 系统数据
 * @returns {Promise}
 */
export function updateHeterogeneousSystem(systemId, data) {
  return request({
    url: `/cwgxAi/heterogeneous/system/${systemId}`,
    method: 'put',
    data: transData(data)
  })
}

/**
 * 删除异构系统
 * @param {String} systemId 系统ID
 * @returns {Promise}
 */
export function deleteHeterogeneousSystem(systemId) {
  return request({
    url: `/cwgxAi/heterogeneous/system/${systemId}`,
    method: 'delete'
  })
}

/**
 * 测试异构系统连接
 * @param {String} systemId 系统ID
 * @returns {Promise}
 */
export function testSystemConnection(systemId) {
  return request({
    url: `/cwgxAi/heterogeneous/system/test-connection/${systemId}`,
    method: 'post'
  })
}

// ==================== 异构数据同步 API ====================

/**
 * 异构数据同步
 * @param {Object} data 同步参数
 * @returns {Promise}
 */
export function syncHeterogeneousData(data) {
  return request({
    url: '/cwgxAi/heterogeneous/data/sync',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取同步任务状态
 * @param {String} syncTaskId 同步任务ID
 * @returns {Promise}
 */
export function getSyncTaskStatus(syncTaskId) {
  return request({
    url: `/cwgxAi/heterogeneous/data/sync/status/${syncTaskId}`,
    method: 'get'
  })
}

/**
 * 停止同步任务
 * @param {String} syncTaskId 同步任务ID
 * @returns {Promise}
 */
export function stopSyncTask(syncTaskId) {
  return request({
    url: `/cwgxAi/heterogeneous/data/sync/stop/${syncTaskId}`,
    method: 'post'
  })
}

/**
 * 重新启动同步任务
 * @param {String} syncTaskId 同步任务ID
 * @returns {Promise}
 */
export function restartSyncTask(syncTaskId) {
  return request({
    url: `/cwgxAi/heterogeneous/data/sync/restart/${syncTaskId}`,
    method: 'post'
  })
}

/**
 * 获取同步日志
 * @param {String} syncTaskId 同步任务ID
 * @returns {Promise}
 */
export function getSyncTaskLogs(syncTaskId) {
  return request({
    url: `/cwgxAi/heterogeneous/data/sync/logs/${syncTaskId}`,
    method: 'get'
  })
}

// ==================== 数据映射配置 API ====================

/**
 * 获取数据映射配置
 * @param {String} systemId 系统ID
 * @returns {Promise}
 */
export function getDataMapping(systemId) {
  return request({
    url: `/cwgxAi/heterogeneous/system/mapping/${systemId}`,
    method: 'get'
  })
}

/**
 * 保存数据映射配置
 * @param {String} systemId 系统ID
 * @param {Object} mappingConfig 映射配置
 * @returns {Promise}
 */
export function saveDataMapping(systemId, mappingConfig) {
  return request({
    url: `/cwgxAi/heterogeneous/system/mapping/${systemId}`,
    method: 'post',
    data: transData(mappingConfig)
  })
}

/**
 * 验证数据映射配置
 * @param {String} systemId 系统ID
 * @param {Object} mappingConfig 映射配置
 * @returns {Promise}
 */
export function validateDataMapping(systemId, mappingConfig) {
  return request({
    url: `/cwgxAi/heterogeneous/system/mapping/${systemId}/validate`,
    method: 'post',
    data: transData(mappingConfig)
  })
}

/**
 * 导入数据映射配置
 * @param {String} systemId 系统ID
 * @param {FormData} formData 文件数据
 * @returns {Promise}
 */
export function importDataMapping(systemId, formData) {
  return request({
    url: `/cwgxAi/heterogeneous/system/mapping/${systemId}/import`,
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 导出数据映射配置
 * @param {String} systemId 系统ID
 * @returns {Promise}
 */
export function exportDataMapping(systemId) {
  return request({
    url: `/cwgxAi/heterogeneous/system/mapping/${systemId}/export`,
    method: 'get',
    responseType: 'blob'
  })
}

// ==================== 同步规则管理 API ====================

/**
 * 获取同步规则列表
 * @param {String} systemId 系统ID
 * @returns {Promise}
 */
export function getSyncRuleList(systemId) {
  return request({
    url: `/cwgxAi/heterogeneous/system/sync-rules/${systemId}`,
    method: 'get'
  })
}

/**
 * 保存同步规则
 * @param {String} systemId 系统ID
 * @param {Object} data 规则数据
 * @returns {Promise}
 */
export function saveSyncRule(systemId, data) {
  return request({
    url: `/cwgxAi/heterogeneous/system/sync-rules/${systemId}`,
    method: 'post',
    data: transData(data)
  })
}

/**
 * 删除同步规则
 * @param {String} systemId 系统ID
 * @param {Number} ruleId 规则ID
 * @returns {Promise}
 */
export function deleteSyncRule(systemId, ruleId) {
  return request({
    url: `/cwgxAi/heterogeneous/system/sync-rules/${systemId}/${ruleId}`,
    method: 'delete'
  })
}

/**
 * 测试同步规则
 * @param {String} systemId 系统ID
 * @param {Number} ruleId 规则ID
 * @param {Object} testData 测试数据
 * @returns {Promise}
 */
export function testSyncRule(systemId, ruleId, testData) {
  return request({
    url: `/cwgxAi/heterogeneous/system/sync-rules/${systemId}/${ruleId}/test`,
    method: 'post',
    data: transData(testData)
  })
}

// ==================== 同步监控 API ====================

/**
 * 获取同步监控数据
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getSyncMonitorData(params) {
  return request({
    url: '/cwgxAi/heterogeneous/monitor/sync',
    method: 'get',
    params
  })
}

/**
 * 获取系统连接状态
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getSystemConnectionStatus(params) {
  return request({
    url: '/cwgxAi/heterogeneous/monitor/connection',
    method: 'get',
    params
  })
}

/**
 * 获取数据质量报告
 * @param {String} systemId 系统ID
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getDataQualityReport(systemId, params) {
  return request({
    url: `/cwgxAi/heterogeneous/monitor/data-quality/${systemId}`,
    method: 'get',
    params
  })
}

// ==================== 异常处理 API ====================

/**
 * 获取同步异常列表
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getSyncExceptionPage(data) {
  return request({
    url: '/cwgxAi/heterogeneous/exception/page',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 处理同步异常
 * @param {Number} exceptionId 异常ID
 * @param {Object} data 处理数据
 * @returns {Promise}
 */
export function handleSyncException(exceptionId, data) {
  return request({
    url: `/cwgxAi/heterogeneous/exception/${exceptionId}/handle`,
    method: 'post',
    data: transData(data)
  })
}

/**
 * 批量处理同步异常
 * @param {Array} exceptionIds 异常ID列表
 * @param {Object} data 处理数据
 * @returns {Promise}
 */
export function batchHandleSyncExceptions(exceptionIds, data) {
  return request({
    url: '/cwgxAi/heterogeneous/exception/batch-handle',
    method: 'post',
    data: transData({ exceptionIds, ...data })
  })
}

/**
 * 忽略同步异常
 * @param {Number} exceptionId 异常ID
 * @param {String} reason 忽略原因
 * @returns {Promise}
 */
export function ignoreSyncException(exceptionId, reason) {
  return request({
    url: `/cwgxAi/heterogeneous/exception/${exceptionId}/ignore`,
    method: 'post',
    data: { reason }
  })
}
