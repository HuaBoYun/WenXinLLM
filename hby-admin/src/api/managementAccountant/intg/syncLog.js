import request from '@/utils/request'

// API基础路径
const API_BASE_PATH = '/api/intg/sync-log'

// 同步类型常量
export const SYNC_TYPES = {
  FULL: 'FULL',
  INCREMENTAL: 'INCREMENTAL',
  DELTA: 'DELTA',
  REAL_TIME: 'REAL_TIME'
}

// 同步方向常量
export const SYNC_DIRECTIONS = {
  IMPORT: 'IMPORT',
  EXPORT: 'EXPORT',
  BIDIRECTIONAL: 'BIDIRECTIONAL'
}

// 同步模式常量
export const SYNC_MODES = {
  MANUAL: 'MANUAL',
  SCHEDULED: 'SCHEDULED',
  EVENT_DRIVEN: 'EVENT_DRIVEN',
  API_TRIGGERED: 'API_TRIGGERED'
}

// 触发方式常量
export const TRIGGER_TYPES = {
  MANUAL: 'MANUAL',
  CRON: 'CRON',
  EVENT: 'EVENT',
  API: 'API'
}

// 同步状态常量
export const SYNC_STATUS = {
  PENDING: 'PENDING',
  RUNNING: 'RUNNING',
  SUCCESS: 'SUCCESS',
  FAILED: 'FAILED',
  CANCELLED: 'CANCELLED',
  TIMEOUT: 'TIMEOUT'
}

// 执行结果常量
export const EXECUTION_RESULTS = {
  SUCCESS: 'SUCCESS',
  PARTIAL_SUCCESS: 'PARTIAL_SUCCESS',
  FAILED: 'FAILED',
  ERROR: 'ERROR'
}

// 日志级别常量
export const LOG_LEVELS = {
  DEBUG: 'DEBUG',
  INFO: 'INFO',
  WARN: 'WARN',
  ERROR: 'ERROR',
  FATAL: 'FATAL'
}

// 基础CRUD操作

/**
 * 创建同步日志
 * @param {Object} data 同步日志数据
 * @returns {Promise}
 */
export function createSyncLog(data) {
  return request({
    url: API_BASE_PATH,
    method: 'post',
    data
  })
}

/**
 * 更新同步日志
 * @param {String} logId 日志ID
 * @param {Object} data 同步日志数据
 * @returns {Promise}
 */
export function updateSyncLog(logId, data) {
  return request({
    url: `${API_BASE_PATH}/${logId}`,
    method: 'put',
    data
  })
}

/**
 * 删除同步日志
 * @param {String} logId 日志ID
 * @returns {Promise}
 */
export function deleteSyncLog(logId) {
  return request({
    url: `${API_BASE_PATH}/${logId}`,
    method: 'delete'
  })
}

/**
 * 根据ID获取同步日志
 * @param {String} logId 日志ID
 * @returns {Promise}
 */
export function getSyncLogById(logId) {
  return request({
    url: `${API_BASE_PATH}/${logId}`,
    method: 'get'
  })
}

/**
 * 根据编码获取同步日志
 * @param {String} logCode 日志编码
 * @returns {Promise}
 */
export function getSyncLogByCode(logCode) {
  return request({
    url: `${API_BASE_PATH}/code/${logCode}`,
    method: 'get'
  })
}

// 查询操作

/**
 * 分页查询同步日志
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getSyncLogPage(params) {
  return request({
    url: `${API_BASE_PATH}/page`,
    method: 'get',
    params
  })
}

/**
 * 根据系统配置ID查询日志列表
 * @param {String} configId 配置ID
 * @returns {Promise}
 */
export function getSyncLogsByConfigId(configId) {
  return request({
    url: `${API_BASE_PATH}/config/${configId}`,
    method: 'get'
  })
}

/**
 * 根据映射配置ID查询日志列表
 * @param {String} mappingId 映射ID
 * @returns {Promise}
 */
export function getSyncLogsByMappingId(mappingId) {
  return request({
    url: `${API_BASE_PATH}/mapping/${mappingId}`,
    method: 'get'
  })
}

/**
 * 根据任务ID查询日志列表
 * @param {String} taskId 任务ID
 * @returns {Promise}
 */
export function getSyncLogsByTaskId(taskId) {
  return request({
    url: `${API_BASE_PATH}/task/${taskId}`,
    method: 'get'
  })
}

/**
 * 根据批次号查询日志列表
 * @param {String} batchNumber 批次号
 * @returns {Promise}
 */
export function getSyncLogsByBatchNumber(batchNumber) {
  return request({
    url: `${API_BASE_PATH}/batch/${batchNumber}`,
    method: 'get'
  })
}

/**
 * 根据同步状态查询日志列表
 * @param {String} syncStatus 同步状态
 * @returns {Promise}
 */
export function getSyncLogsByStatus(syncStatus) {
  return request({
    url: `${API_BASE_PATH}/status/${syncStatus}`,
    method: 'get'
  })
}

/**
 * 根据执行结果查询日志列表
 * @param {String} executionResult 执行结果
 * @returns {Promise}
 */
export function getSyncLogsByResult(executionResult) {
  return request({
    url: `${API_BASE_PATH}/result/${executionResult}`,
    method: 'get'
  })
}

/**
 * 查询最近的日志列表
 * @param {Number} limit 限制数量
 * @returns {Promise}
 */
export function getRecentSyncLogs(limit = 10) {
  return request({
    url: `${API_BASE_PATH}/recent`,
    method: 'get',
    params: {
      limit
    }
  })
}

// 日志管理操作

/**
 * 开始同步日志记录
 * @param {String} configId 配置ID
 * @param {String} mappingId 映射ID
 * @param {String} taskId 任务ID
 * @param {String} syncType 同步类型
 * @param {String} syncDirection 同步方向
 * @returns {Promise}
 */
export function startSyncLog(configId, mappingId, taskId, syncType, syncDirection) {
  return request({
    url: `${API_BASE_PATH}/start`,
    method: 'post',
    params: {
      configId,
      mappingId,
      taskId,
      syncType,
      syncDirection
    }
  })
}

/**
 * 完成同步日志记录
 * @param {String} logId 日志ID
 * @param {String} executionResult 执行结果
 * @param {Number} processedRecords 处理记录数
 * @param {Number} successRecords 成功记录数
 * @param {Number} failedRecords 失败记录数
 * @returns {Promise}
 */
export function completeSyncLog(logId, executionResult, processedRecords, successRecords, failedRecords) {
  return request({
    url: `${API_BASE_PATH}/${logId}/complete`,
    method: 'put',
    params: {
      executionResult,
      processedRecords,
      successRecords,
      failedRecords
    }
  })
}

/**
 * 更新同步进度
 * @param {String} logId 日志ID
 * @param {Number} processedRecords 处理记录数
 * @param {Number} successRecords 成功记录数
 * @param {Number} failedRecords 失败记录数
 * @returns {Promise}
 */
export function updateSyncProgress(logId, processedRecords, successRecords, failedRecords) {
  return request({
    url: `${API_BASE_PATH}/${logId}/progress`,
    method: 'put',
    data: {
      processedRecords,
      successRecords,
      failedRecords
    }
  })
}

/**
 * 记录同步错误
 * @param {String} logId 日志ID
 * @param {String} errorCode 错误代码
 * @param {String} errorMessage 错误消息
 * @param {String} errorStack 错误堆栈
 * @returns {Promise}
 */
export function recordSyncError(logId, errorCode, errorMessage, errorStack) {
  return request({
    url: `${API_BASE_PATH}/${logId}/error`,
    method: 'put',
    data: {
      errorCode,
      errorMessage,
      errorStack
    }
  })
}

/**
 * 批量更新日志状态
 * @param {Array} logIds 日志ID列表
 * @param {String} syncStatus 同步状态
 * @returns {Promise}
 */
export function batchUpdateLogStatus(logIds, syncStatus) {
  return request({
    url: `${API_BASE_PATH}/batch-update-status`,
    method: 'put',
    data: {
      logIds,
      syncStatus
    }
  })
}

/**
 * 批量更新归档状态
 * @param {Array} logIds 日志ID列表
 * @param {String} archiveStatus 归档状态
 * @returns {Promise}
 */
export function batchUpdateArchiveStatus(logIds, archiveStatus) {
  return request({
    url: `${API_BASE_PATH}/batch-update-archive`,
    method: 'put',
    data: {
      logIds,
      archiveStatus
    }
  })
}

// 日志分析操作

/**
 * 获取同步趋势分析
 * @param {String} startTime 开始时间
 * @param {String} endTime 结束时间
 * @param {String} granularity 粒度
 * @returns {Promise}
 */
export function getSyncTrendAnalysis(startTime, endTime, granularity = 'day') {
  return request({
    url: `${API_BASE_PATH}/analysis/trend`,
    method: 'get',
    params: {
      startTime,
      endTime,
      granularity
    }
  })
}

/**
 * 获取性能统计分析
 * @param {Array} configIds 配置ID列表
 * @param {String} startTime 开始时间
 * @param {String} endTime 结束时间
 * @returns {Promise}
 */
export function getPerformanceStats(configIds, startTime, endTime) {
  return request({
    url: `${API_BASE_PATH}/analysis/performance`,
    method: 'get',
    params: {
      configIds: configIds.join(','),
      startTime,
      endTime
    }
  })
}

/**
 * 获取错误统计分析
 * @param {String} startTime 开始时间
 * @param {String} endTime 结束时间
 * @returns {Promise}
 */
export function getErrorStats(startTime, endTime) {
  return request({
    url: `${API_BASE_PATH}/analysis/error`,
    method: 'get',
    params: {
      startTime,
      endTime
    }
  })
}

/**
 * 获取数据量统计分析
 * @param {Array} configIds 配置ID列表
 * @param {String} startTime 开始时间
 * @param {String} endTime 结束时间
 * @returns {Promise}
 */
export function getDataVolumeStats(configIds, startTime, endTime) {
  return request({
    url: `${API_BASE_PATH}/analysis/data-volume`,
    method: 'get',
    params: {
      configIds: configIds.join(','),
      startTime,
      endTime
    }
  })
}

/**
 * 获取系统使用统计
 * @param {String} startTime 开始时间
 * @param {String} endTime 结束时间
 * @returns {Promise}
 */
export function getSystemUsageStats(startTime, endTime) {
  return request({
    url: `${API_BASE_PATH}/analysis/system-usage`,
    method: 'get',
    params: {
      startTime,
      endTime
    }
  })
}

// 统计查询操作

/**
 * 统计日志总数
 * @returns {Promise}
 */
export function countSyncLogs() {
  return request({
    url: `${API_BASE_PATH}/count`,
    method: 'get'
  })
}

/**
 * 按同步类型统计日志数量
 * @returns {Promise}
 */
export function countBySyncType() {
  return request({
    url: `${API_BASE_PATH}/count/by-sync-type`,
    method: 'get'
  })
}

/**
 * 按同步方向统计日志数量
 * @returns {Promise}
 */
export function countBySyncDirection() {
  return request({
    url: `${API_BASE_PATH}/count/by-sync-direction`,
    method: 'get'
  })
}

/**
 * 按同步状态统计日志数量
 * @returns {Promise}
 */
export function countBySyncStatus() {
  return request({
    url: `${API_BASE_PATH}/count/by-sync-status`,
    method: 'get'
  })
}

/**
 * 按执行结果统计日志数量
 * @returns {Promise}
 */
export function countByExecutionResult() {
  return request({
    url: `${API_BASE_PATH}/count/by-execution-result`,
    method: 'get'
  })
}

/**
 * 按日期统计日志数量
 * @param {String} startTime 开始时间
 * @param {String} endTime 结束时间
 * @param {String} granularity 粒度
 * @returns {Promise}
 */
export function countByDate(startTime, endTime, granularity = 'day') {
  return request({
    url: `${API_BASE_PATH}/count/by-date`,
    method: 'get',
    params: {
      startTime,
      endTime,
      granularity
    }
  })
}

/**
 * 按系统配置统计日志数量
 * @returns {Promise}
 */
export function countBySystemConfig() {
  return request({
    url: `${API_BASE_PATH}/count/by-system-config`,
    method: 'get'
  })
}

// 工具函数

/**
 * 格式化同步类型显示名称
 * @param {String} syncType 同步类型
 * @returns {String}
 */
export function formatSyncTypeName(syncType) {
  const typeNames = {
    [SYNC_TYPES.FULL]: '全量同步',
    [SYNC_TYPES.INCREMENTAL]: '增量同步',
    [SYNC_TYPES.DELTA]: '差异同步',
    [SYNC_TYPES.REAL_TIME]: '实时同步'
  }
  return typeNames[syncType] || syncType
}

/**
 * 格式化同步方向显示名称
 * @param {String} syncDirection 同步方向
 * @returns {String}
 */
export function formatSyncDirectionName(syncDirection) {
  const directionNames = {
    [SYNC_DIRECTIONS.IMPORT]: '导入',
    [SYNC_DIRECTIONS.EXPORT]: '导出',
    [SYNC_DIRECTIONS.BIDIRECTIONAL]: '双向'
  }
  return directionNames[syncDirection] || syncDirection
}

/**
 * 格式化同步状态显示名称
 * @param {String} syncStatus 同步状态
 * @returns {String}
 */
export function formatSyncStatusName(syncStatus) {
  const statusNames = {
    [SYNC_STATUS.PENDING]: '等待中',
    [SYNC_STATUS.RUNNING]: '运行中',
    [SYNC_STATUS.SUCCESS]: '成功',
    [SYNC_STATUS.FAILED]: '失败',
    [SYNC_STATUS.CANCELLED]: '已取消',
    [SYNC_STATUS.TIMEOUT]: '超时'
  }
  return statusNames[syncStatus] || syncStatus
}

/**
 * 格式化执行结果显示名称
 * @param {String} executionResult 执行结果
 * @returns {String}
 */
export function formatExecutionResultName(executionResult) {
  const resultNames = {
    [EXECUTION_RESULTS.SUCCESS]: '成功',
    [EXECUTION_RESULTS.PARTIAL_SUCCESS]: '部分成功',
    [EXECUTION_RESULTS.FAILED]: '失败',
    [EXECUTION_RESULTS.ERROR]: '错误'
  }
  return resultNames[executionResult] || executionResult
}

/**
 * 计算同步成功率
 * @param {Number} successRecords 成功记录数
 * @param {Number} totalRecords 总记录数
 * @returns {Number}
 */
export function calculateSuccessRate(successRecords, totalRecords) {
  if (!totalRecords || totalRecords === 0) return 0
  return Math.round((successRecords / totalRecords) * 100 * 100) / 100
}

/**
 * 格式化执行时长
 * @param {Number} duration 执行时长（毫秒）
 * @returns {String}
 */
export function formatExecutionDuration(duration) {
  if (!duration) return '0秒'
  
  const seconds = Math.floor(duration / 1000)
  const minutes = Math.floor(seconds / 60)
  const hours = Math.floor(minutes / 60)
  
  if (hours > 0) {
    return `${hours}小时${minutes % 60}分钟${seconds % 60}秒`
  } else if (minutes > 0) {
    return `${minutes}分钟${seconds % 60}秒`
  } else {
    return `${seconds}秒`
  }
}

export default {
  // 常量
  SYNC_TYPES,
  SYNC_DIRECTIONS,
  SYNC_MODES,
  TRIGGER_TYPES,
  SYNC_STATUS,
  EXECUTION_RESULTS,
  LOG_LEVELS,
  
  // CRUD操作
  createSyncLog,
  updateSyncLog,
  deleteSyncLog,
  getSyncLogById,
  getSyncLogByCode,
  
  // 查询操作
  getSyncLogPage,
  getSyncLogsByConfigId,
  getSyncLogsByMappingId,
  getSyncLogsByTaskId,
  getSyncLogsByBatchNumber,
  getSyncLogsByStatus,
  getSyncLogsByResult,
  getRecentSyncLogs,
  
  // 日志管理
  startSyncLog,
  completeSyncLog,
  updateSyncProgress,
  recordSyncError,
  batchUpdateLogStatus,
  batchUpdateArchiveStatus,
  
  // 日志分析
  getSyncTrendAnalysis,
  getPerformanceStats,
  getErrorStats,
  getDataVolumeStats,
  getSystemUsageStats,
  
  // 统计查询
  countSyncLogs,
  countBySyncType,
  countBySyncDirection,
  countBySyncStatus,
  countByExecutionResult,
  countByDate,
  countBySystemConfig,
  
  // 工具函数
  formatSyncTypeName,
  formatSyncDirectionName,
  formatSyncStatusName,
  formatExecutionResultName,
  calculateSuccessRate,
  formatExecutionDuration
}
