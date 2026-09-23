import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// 数据报送任务管理API

/**
 * 分页查询数据报送任务列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getTaskList(params) {
  return transData('/monitor/v1/data/submit/task/list', params)
}

/**
 * 获取数据报送任务详情
 * @param {Object} params 参数
 * @returns {Promise}
 */
export function getTaskDetail(params) {
  return transData('/monitor/v1/data/submit/task/detail', params)
}

/**
 * 新增数据报送任务
 * @param {Object} data 任务数据
 * @returns {Promise}
 */
export function addTask(data) {
  return transData('/monitor/v1/data/submit/task/add', data)
}

/**
 * 更新数据报送任务
 * @param {Object} data 任务数据
 * @returns {Promise}
 */
export function updateTask(data) {
  return transData('/monitor/v1/data/submit/task/update', data)
}

/**
 * 删除数据报送任务
 * @param {Object} params 参数
 * @returns {Promise}
 */
export function deleteTask(params) {
  return transData('/monitor/v1/data/submit/task/delete', params)
}

/**
 * 批量删除数据报送任务
 * @param {Object} params 参数
 * @returns {Promise}
 */
export function batchDeleteTask(params) {
  return transData('/monitor/v1/data/submit/task/batch-delete', params)
}

/**
 * 发布任务
 * @param {Object} params 参数
 * @returns {Promise}
 */
export function publishTask(params) {
  return transData('/monitor/v1/data/submit/task/publish', params)
}

/**
 * 取消任务
 * @param {Object} params 参数
 * @returns {Promise}
 */
export function cancelTask(params) {
  return transData('/monitor/v1/data/submit/task/cancel', params)
}

/**
 * 完成任务
 * @param {Object} params 参数
 * @returns {Promise}
 */
export function completeTask(params) {
  return transData('/monitor/v1/data/submit/task/complete', params)
}

/**
 * 根据任务状态查询任务列表
 * @param {Object} params 参数
 * @returns {Promise}
 */
export function getTasksByStatus(params) {
  return transData('/monitor/v1/data/submit/task/list-by-status', params)
}

/**
 * 根据任务类型查询任务列表
 * @param {Object} params 参数
 * @returns {Promise}
 */
export function getTasksByType(params) {
  return transData('/monitor/v1/data/submit/task/list-by-type', params)
}

/**
 * 根据报送周期查询任务列表
 * @param {Object} params 参数
 * @returns {Promise}
 */
export function getTasksByCycle(params) {
  return transData('/monitor/v1/data/submit/task/list-by-cycle', params)
}

/**
 * 查询活跃的任务列表
 * @returns {Promise}
 */
export function getActiveTasks() {
  return transData('/monitor/v1/data/submit/task/active-tasks', {})
}

/**
 * 查询即将到期的任务列表
 * @param {Object} params 参数
 * @returns {Promise}
 */
export function getExpiringTasks(params) {
  return transData('/monitor/v1/data/submit/task/expiring-tasks', params)
}

/**
 * 获取任务统计信息
 * @returns {Promise}
 */
export function getTaskStatistics() {
  return transData('/monitor/v1/data/submit/task/statistics/overview', {})
}

/**
 * 获取任务类型分布统计
 * @returns {Promise}
 */
export function getTaskTypeDistribution() {
  return transData('/monitor/v1/data/submit/task/statistics/type-distribution', {})
}

/**
 * 获取任务分类分布统计
 * @returns {Promise}
 */
export function getTaskCategoryDistribution() {
  return transData('/monitor/v1/data/submit/task/statistics/category-distribution', {})
}

/**
 * 获取任务周期分布统计
 * @returns {Promise}
 */
export function getTaskCycleDistribution() {
  return transData('/monitor/v1/data/submit/task/statistics/cycle-distribution', {})
}

/**
 * 验证任务名称
 * @param {Object} params 参数
 * @returns {Promise}
 */
export function validateTaskName(params) {
  return transData('/monitor/v1/data/submit/task/validate-name', params)
}

/**
 * 复制任务
 * @param {Object} params 参数
 * @returns {Promise}
 */
export function copyTask(params) {
  return transData('/monitor/v1/data/submit/task/copy', params)
}

/**
 * 导出任务列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function exportTaskList(params) {
  return transData('/monitor/v1/data/submit/task/export', params, {
    responseType: 'blob'
  })
}

/**
 * 下载任务导入模板
 * @returns {Promise}
 */
export function downloadTaskTemplate() {
  return transData('/monitor/v1/data/submit/task/template', {}, {
    responseType: 'blob'
  })
}

/**
 * 批量导入任务
 * @param {FormData} formData 表单数据
 * @returns {Promise}
 */
export function importTaskList(formData) {
  return transData('/monitor/v1/data/submit/task/import', formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

// 数据报送记录管理API

/**
 * 分页查询数据报送记录列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getRecordList(params) {
  return transData('/monitor/v1/data/submit/record/list', params)
}

/**
 * 获取数据报送记录详情
 * @param {Object} params 参数
 * @returns {Promise}
 */
export function getRecordDetail(params) {
  return transData('/monitor/v1/data/submit/record/detail', params)
}

/**
 * 新增数据报送记录
 * @param {Object} data 记录数据
 * @returns {Promise}
 */
export function addRecord(data) {
  return transData('/monitor/v1/data/submit/record/add', data)
}

/**
 * 更新数据报送记录
 * @param {Object} data 记录数据
 * @returns {Promise}
 */
export function updateRecord(data) {
  return transData('/monitor/v1/data/submit/record/update', data)
}

/**
 * 删除数据报送记录
 * @param {Object} params 参数
 * @returns {Promise}
 */
export function deleteRecord(params) {
  return transData('/monitor/v1/data/submit/record/delete', params)
}

/**
 * 批量删除数据报送记录
 * @param {Object} params 参数
 * @returns {Promise}
 */
export function batchDeleteRecord(params) {
  return transData('/monitor/v1/data/submit/record/batch-delete', params)
}

/**
 * 审核数据报送记录
 * @param {Object} params 参数
 * @returns {Promise}
 */
export function reviewRecord(params) {
  return transData('/monitor/v1/data/submit/record/review', params)
}

/**
 * 获取待审核的记录列表
 * @returns {Promise}
 */
export function getPendingReviewRecords() {
  return transData('/monitor/v1/data/submit/record/pending-review', {})
}

/**
 * 获取数据质量评分低于阈值的记录
 * @param {Object} params 参数
 * @returns {Promise}
 */
export function getLowQualityRecords(params) {
  return transData('/monitor/v1/data/submit/record/low-quality', params)
}

/**
 * 获取报送记录统计信息
 * @returns {Promise}
 */
export function getRecordStatistics() {
  return transData('/monitor/v1/data/submit/record/statistics/overview', {})
}

/**
 * 导出记录列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function exportRecordList(params) {
  return transData('/monitor/v1/data/submit/record/export', params, {
    responseType: 'blob'
  })
}

// 数据协同记录管理API

/**
 * 分页查询数据协同记录列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getCollaborationList(params) {
  return transData('/monitor/v1/data/collaboration/list', params)
}

/**
 * 获取数据协同记录详情
 * @param {Object} params 参数
 * @returns {Promise}
 */
export function getCollaborationDetail(params) {
  return transData('/monitor/v1/data/collaboration/detail', params)
}

/**
 * 获取协同记录统计信息
 * @returns {Promise}
 */
export function getCollaborationStatistics() {
  return transData('/monitor/v1/data/collaboration/statistics/overview', {})
}

/**
 * 获取协同性能统计
 * @returns {Promise}
 */
export function getCollaborationPerformance() {
  return transData('/monitor/v1/data/collaboration/statistics/performance', {})
}

// 系统配置管理API

/**
 * 分页查询系统配置列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getConfigList(params) {
  return transData('/monitor/v1/system/config/list', params)
}

/**
 * 根据配置键获取配置值
 * @param {Object} params 参数
 * @returns {Promise}
 */
export function getConfigValue(params) {
  return transData('/monitor/v1/system/config/value', params)
}

/**
 * 根据配置分组查询配置列表
 * @param {Object} params 参数
 * @returns {Promise}
 */
export function getConfigsByGroup(params) {
  return transData('/monitor/v1/system/config/group', params)
}

/**
 * 更新配置值
 * @param {Object} data 配置数据
 * @returns {Promise}
 */
export function updateConfigValue(data) {
  return transData('/monitor/v1/system/config/update-value', data)
}

/**
 * 新增系统配置
 * @param {Object} data 配置数据
 * @returns {Promise}
 */
export function addConfig(data) {
  return transData('/monitor/v1/system/config/add', data)
}

/**
 * 更新系统配置
 * @param {Object} data 配置数据
 * @returns {Promise}
 */
export function updateConfig(data) {
  return transData('/monitor/v1/system/config/update', data)
}

/**
 * 删除系统配置
 * @param {Object} params 参数
 * @returns {Promise}
 */
export function deleteConfig(params) {
  return transData('/monitor/v1/system/config/delete', params)
}
