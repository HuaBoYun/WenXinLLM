import request from '@/utils/request'

// 创建同步任务
export function createSyncTask(data) {
  return request({
    url: '/cwgxAi/data-sync/task/create',
    method: 'post',
    data: data
  })
}

// 执行同步任务
export function executeSyncTask(taskId) {
  return request({
    url: `/cwgxAi/data-sync/task/execute/${taskId}`,
    method: 'post'
  })
}

// 获取同步进度
export function getSyncProgress(taskId) {
  return request({
    url: `/cwgxAi/data-sync/progress/${taskId}`,
    method: 'get'
  })
}

// 取消同步任务
export function cancelSyncTask(taskId) {
  return request({
    url: `/cwgxAi/data-sync/task/cancel/${taskId}`,
    method: 'post'
  })
}

// 重试同步任务
export function retrySyncTask(taskId) {
  return request({
    url: `/cwgxAi/data-sync/task/retry/${taskId}`,
    method: 'post'
  })
}

// 获取同步任务列表
export function getSyncTaskList(params) {
  return request({
    url: '/cwgxAi/data-sync/tasks',
    method: 'get',
    params: params
  })
}

// 获取同步历史记录
export function getSyncHistory(dataSourceId, limit = 10) {
  return request({
    url: '/cwgxAi/data-sync/history',
    method: 'get',
    params: {
      dataSourceId,
      limit
    }
  })
}

// 获取同步统计信息
export function getSyncStatistics(dataSourceId, dateRange) {
  return request({
    url: '/cwgxAi/data-sync/statistics',
    method: 'get',
    params: {
      dataSourceId,
      dateRange
    }
  })
}

// 验证同步配置
export function validateSyncConfig(data) {
  return request({
    url: '/cwgxAi/data-sync/validate',
    method: 'post',
    data: data
  })
}

// 预览同步数据
export function previewSyncData(data, limit = 10) {
  return request({
    url: '/cwgxAi/data-sync/preview',
    method: 'post',
    data: data,
    params: {
      limit
    }
  })
}