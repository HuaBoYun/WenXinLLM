import request from '@/utils/request'

// 数字员工API接口

/**
 * 分页查询数字员工列表
 */
export function getDigitalEmployeePage(params) {
  return request({
    url: '/accountant/ss/digitalEmployee/page',
    method: 'get',
    params
  })
}

/**
 * 根据ID查询数字员工详情
 */
export function getDigitalEmployeeById(robotId, tenantId) {
  return request({
    url: `/accountant/ss/digitalEmployee/${robotId}`,
    method: 'get',
    params: { tenantId }
  })
}

/**
 * 根据编码查询数字员工
 */
export function getDigitalEmployeeByCode(robotCode, tenantId) {
  return request({
    url: `/accountant/ss/digitalEmployee/code/${robotCode}`,
    method: 'get',
    params: { tenantId }
  })
}

/**
 * 创建数字员工
 */
export function createDigitalEmployee(data, tenantId) {
  return request({
    url: '/accountant/ss/digitalEmployee',
    method: 'post',
    data,
    params: { tenantId }
  })
}

/**
 * 更新数字员工
 */
export function updateDigitalEmployee(data, tenantId) {
  return request({
    url: '/accountant/ss/digitalEmployee',
    method: 'put',
    data,
    params: { tenantId }
  })
}

/**
 * 删除数字员工
 */
export function deleteDigitalEmployee(robotId, tenantId) {
  return request({
    url: `/accountant/ss/digitalEmployee/${robotId}`,
    method: 'delete',
    params: { tenantId }
  })
}

/**
 * 批量删除数字员工
 */
export function batchDeleteDigitalEmployee(robotIds, tenantId) {
  return request({
    url: '/accountant/ss/digitalEmployee/batch',
    method: 'delete',
    data: robotIds,
    params: { tenantId }
  })
}

/**
 * 激活数字员工
 */
export function activateDigitalEmployee(robotId, tenantId) {
  return request({
    url: `/accountant/ss/digitalEmployee/${robotId}/activate`,
    method: 'post',
    params: { tenantId }
  })
}

/**
 * 停用数字员工
 */
export function deactivateDigitalEmployee(robotId, reason, tenantId) {
  return request({
    url: `/accountant/ss/digitalEmployee/${robotId}/deactivate`,
    method: 'post',
    params: { reason, tenantId }
  })
}

/**
 * 批量激活数字员工
 */
export function batchActivateDigitalEmployee(robotIds, tenantId) {
  return request({
    url: '/accountant/ss/digitalEmployee/batch/activate',
    method: 'post',
    data: robotIds,
    params: { tenantId }
  })
}

/**
 * 批量停用数字员工
 */
export function batchDeactivateDigitalEmployee(robotIds, reason, tenantId) {
  return request({
    url: '/accountant/ss/digitalEmployee/batch/deactivate',
    method: 'post',
    data: robotIds,
    params: { reason, tenantId }
  })
}

/**
 * 启动维护模式
 */
export function startMaintenance(robotId, reason, startTime, endTime, tenantId) {
  return request({
    url: `/accountant/ss/digitalEmployee/${robotId}/maintenance/start`,
    method: 'post',
    params: { reason, startTime, endTime, tenantId }
  })
}

/**
 * 结束维护模式
 */
export function endMaintenance(robotId, result, tenantId) {
  return request({
    url: `/accountant/ss/digitalEmployee/${robotId}/maintenance/end`,
    method: 'post',
    params: { result, tenantId }
  })
}

/**
 * 部署数字员工
 */
export function deployDigitalEmployee(robotId, deploymentEnvironment, deploymentConfig, tenantId) {
  return request({
    url: `/accountant/ss/digitalEmployee/${robotId}/deploy`,
    method: 'post',
    data: deploymentConfig,
    params: { deploymentEnvironment, tenantId }
  })
}

/**
 * 回滚数字员工
 */
export function rollbackDigitalEmployee(robotId, version, reason, tenantId) {
  return request({
    url: `/accountant/ss/digitalEmployee/${robotId}/rollback`,
    method: 'post',
    params: { version, reason, tenantId }
  })
}

/**
 * 升级数字员工
 */
export function upgradeDigitalEmployee(robotId, newVersion, upgradeConfig, tenantId) {
  return request({
    url: `/accountant/ss/digitalEmployee/${robotId}/upgrade`,
    method: 'post',
    data: upgradeConfig,
    params: { newVersion, tenantId }
  })
}

/**
 * 配置数字员工
 */
export function configureDigitalEmployee(robotId, configuration, tenantId) {
  return request({
    url: `/accountant/ss/digitalEmployee/${robotId}/configure`,
    method: 'post',
    data: configuration,
    params: { tenantId }
  })
}

/**
 * 分配任务给数字员工
 */
export function assignTask(robotId, taskId, taskType, taskConfig, tenantId) {
  return request({
    url: `/accountant/ss/digitalEmployee/${robotId}/task/assign`,
    method: 'post',
    data: taskConfig,
    params: { taskId, taskType, tenantId }
  })
}

/**
 * 批量分配任务
 */
export function batchAssignTask(robotIds, taskId, taskType, taskConfig, tenantId) {
  return request({
    url: '/accountant/ss/digitalEmployee/task/batch/assign',
    method: 'post',
    data: { robotIds, taskConfig },
    params: { taskId, taskType, tenantId }
  })
}

/**
 * 监控数字员工状态
 */
export function monitorDigitalEmployee(robotId, tenantId) {
  return request({
    url: `/accountant/ss/digitalEmployee/${robotId}/monitor`,
    method: 'get',
    params: { tenantId }
  })
}

/**
 * 健康检查
 */
export function performHealthCheck(robotId, tenantId) {
  return request({
    url: `/accountant/ss/digitalEmployee/${robotId}/healthCheck`,
    method: 'post',
    params: { tenantId }
  })
}

/**
 * 性能评估
 */
export function evaluatePerformance(robotId, startTime, endTime, tenantId) {
  return request({
    url: `/accountant/ss/digitalEmployee/${robotId}/performance/evaluate`,
    method: 'get',
    params: { startTime, endTime, tenantId }
  })
}

/**
 * 设置优先级
 */
export function setPriority(robotId, priority, reason, tenantId) {
  return request({
    url: `/accountant/ss/digitalEmployee/${robotId}/priority/set`,
    method: 'post',
    params: { priority, reason, tenantId }
  })
}

/**
 * 查询统计信息
 */
export function getStatistics(startTime, endTime, tenantId) {
  return request({
    url: '/accountant/ss/digitalEmployee/statistics',
    method: 'get',
    params: { startTime, endTime, tenantId }
  })
}

/**
 * 查询状态分布
 */
export function getStatusDistribution(tenantId) {
  return request({
    url: '/accountant/ss/digitalEmployee/distribution/status',
    method: 'get',
    params: { tenantId }
  })
}

/**
 * 查询类型分布
 */
export function getTypeDistribution(tenantId) {
  return request({
    url: '/accountant/ss/digitalEmployee/distribution/type',
    method: 'get',
    params: { tenantId }
  })
}

/**
 * 查询排行榜
 */
export function getRanking(rankingType, limit, tenantId) {
  return request({
    url: '/accountant/ss/digitalEmployee/ranking',
    method: 'get',
    params: { rankingType, limit, tenantId }
  })
}

/**
 * 查询告警信息
 */
export function getAlerts(alertType, tenantId) {
  return request({
    url: '/accountant/ss/digitalEmployee/alerts',
    method: 'get',
    params: { alertType, tenantId }
  })
}

/**
 * 查询待处理事项
 */
export function getPendingItems(tenantId) {
  return request({
    url: '/accountant/ss/digitalEmployee/pending',
    method: 'get',
    params: { tenantId }
  })
}

/**
 * 检查编码是否存在
 */
export function checkCodeExists(robotCode, robotId, tenantId) {
  return request({
    url: '/accountant/ss/digitalEmployee/check/code',
    method: 'get',
    params: { robotCode, robotId, tenantId }
  })
}

// 快捷操作函数

/**
 * 快速激活数字员工
 */
export function quickActivate(robotId, tenantId) {
  return activateDigitalEmployee(robotId, tenantId)
}

/**
 * 快速停用数字员工
 */
export function quickDeactivate(robotId, tenantId, reason = '手动停用') {
  return deactivateDigitalEmployee(robotId, reason, tenantId)
}

/**
 * 快速健康检查
 */
export function quickHealthCheck(robotId, tenantId) {
  return performHealthCheck(robotId, tenantId)
}

/**
 * 快速性能评估（最近7天）
 */
export function quickPerformanceEvaluate(robotId, tenantId) {
  const endTime = new Date()
  const startTime = new Date(endTime.getTime() - 7 * 24 * 60 * 60 * 1000)
  return evaluatePerformance(robotId, startTime.toISOString(), endTime.toISOString(), tenantId)
}

// 工具函数

/**
 * 格式化数字员工状态
 */
export function formatRobotStatus(status) {
  const statusMap = {
    'ACTIVE': '活跃',
    'INACTIVE': '非活跃',
    'MAINTENANCE': '维护中',
    'RETIRED': '已退役'
  }
  return statusMap[status] || status
}

/**
 * 格式化数字员工类型
 */
export function formatRobotType(type) {
  const typeMap = {
    'RPA': '机器人流程自动化',
    'AI': '人工智能',
    'CHATBOT': '聊天机器人'
  }
  return typeMap[type] || type
}

/**
 * 格式化数字员工分类
 */
export function formatRobotCategory(category) {
  const categoryMap = {
    'DATA_ENTRY': '数据录入',
    'DOCUMENT_PROCESSING': '文档处理',
    'CUSTOMER_SERVICE': '客户服务'
  }
  return categoryMap[category] || category
}

/**
 * 格式化健康状态
 */
export function formatHealthStatus(status) {
  const statusMap = {
    'HEALTHY': '健康',
    'WARNING': '警告',
    'CRITICAL': '严重',
    'UNKNOWN': '未知'
  }
  return statusMap[status] || status
}

/**
 * 获取状态颜色
 */
export function getStatusColor(status) {
  const colorMap = {
    'ACTIVE': 'success',
    'INACTIVE': 'info',
    'MAINTENANCE': 'warning',
    'RETIRED': 'danger'
  }
  return colorMap[status] || 'info'
}

/**
 * 获取健康状态颜色
 */
export function getHealthStatusColor(status) {
  const colorMap = {
    'HEALTHY': 'success',
    'WARNING': 'warning',
    'CRITICAL': 'danger',
    'UNKNOWN': 'info'
  }
  return colorMap[status] || 'info'
}
