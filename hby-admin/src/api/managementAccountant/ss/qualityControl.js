import request from '@/utils/request'

// 质量管控API接口

/**
 * 分页查询质量管控
 * @param {Object} params 查询参数
 */
export function getQualityControlPage(params) {
  return request({
    url: '/accountant/ss/quality-control/page',
    method: 'get',
    params
  })
}

/**
 * 根据ID查询质量管控
 * @param {Number} qualityId 质量管控ID
 */
export function getQualityControlById(qualityId) {
  return request({
    url: `/accountant/ss/quality-control/${qualityId}`,
    method: 'get'
  })
}

/**
 * 根据编码查询质量管控
 * @param {String} qualityCode 质量管控编码
 * @param {Number} tenantId 租户ID
 */
export function getQualityControlByCode(qualityCode, tenantId = 1) {
  return request({
    url: `/accountant/ss/quality-control/code/${qualityCode}`,
    method: 'get',
    params: { tenantId }
  })
}

/**
 * 创建质量管控
 * @param {Object} data 质量管控数据
 */
export function createQualityControl(data) {
  return request({
    url: '/accountant/ss/quality-control',
    method: 'post',
    data
  })
}

/**
 * 更新质量管控
 * @param {Number} qualityId 质量管控ID
 * @param {Object} data 质量管控数据
 */
export function updateQualityControl(qualityId, data) {
  return request({
    url: `/accountant/ss/quality-control/${qualityId}`,
    method: 'put',
    data
  })
}

/**
 * 删除质量管控
 * @param {Number} qualityId 质量管控ID
 */
export function deleteQualityControl(qualityId) {
  return request({
    url: `/accountant/ss/quality-control/${qualityId}`,
    method: 'delete'
  })
}

/**
 * 批量删除质量管控
 * @param {Array} qualityIds 质量管控ID数组
 */
export function batchDeleteQualityControl(qualityIds) {
  return request({
    url: '/accountant/ss/quality-control/batch',
    method: 'delete',
    data: qualityIds
  })
}

/**
 * 启用质量管控
 * @param {Number} qualityId 质量管控ID
 */
export function enableQualityControl(qualityId) {
  return request({
    url: `/accountant/ss/quality-control/${qualityId}/enable`,
    method: 'put'
  })
}

/**
 * 禁用质量管控
 * @param {Number} qualityId 质量管控ID
 */
export function disableQualityControl(qualityId) {
  return request({
    url: `/accountant/ss/quality-control/${qualityId}/disable`,
    method: 'put'
  })
}

/**
 * 批量启用质量管控
 * @param {Array} qualityIds 质量管控ID数组
 */
export function batchEnableQualityControl(qualityIds) {
  return request({
    url: '/accountant/ss/quality-control/batch/enable',
    method: 'put',
    data: qualityIds
  })
}

/**
 * 批量禁用质量管控
 * @param {Array} qualityIds 质量管控ID数组
 */
export function batchDisableQualityControl(qualityIds) {
  return request({
    url: '/accountant/ss/quality-control/batch/disable',
    method: 'put',
    data: qualityIds
  })
}

/**
 * 开始检测
 * @param {Number} qualityId 质量管控ID
 */
export function startDetection(qualityId) {
  return request({
    url: `/accountant/ss/quality-control/${qualityId}/start-detection`,
    method: 'put'
  })
}

/**
 * 停止检测
 * @param {Number} qualityId 质量管控ID
 */
export function stopDetection(qualityId) {
  return request({
    url: `/accountant/ss/quality-control/${qualityId}/stop-detection`,
    method: 'put'
  })
}

/**
 * 暂停检测
 * @param {Number} qualityId 质量管控ID
 */
export function pauseDetection(qualityId) {
  return request({
    url: `/accountant/ss/quality-control/${qualityId}/pause-detection`,
    method: 'put'
  })
}

/**
 * 恢复检测
 * @param {Number} qualityId 质量管控ID
 */
export function resumeDetection(qualityId) {
  return request({
    url: `/accountant/ss/quality-control/${qualityId}/resume-detection`,
    method: 'put'
  })
}

/**
 * 完成检测
 * @param {Number} qualityId 质量管控ID
 * @param {String} detectionResult 检测结果
 * @param {Number} qualityScore 质量评分
 */
export function completeDetection(qualityId, detectionResult, qualityScore) {
  return request({
    url: `/accountant/ss/quality-control/${qualityId}/complete-detection`,
    method: 'put',
    params: { detectionResult, qualityScore }
  })
}

/**
 * 批量开始检测
 * @param {Array} qualityIds 质量管控ID数组
 */
export function batchStartDetection(qualityIds) {
  return request({
    url: '/accountant/ss/quality-control/batch/start-detection',
    method: 'put',
    data: qualityIds
  })
}

/**
 * 批量停止检测
 * @param {Array} qualityIds 质量管控ID数组
 */
export function batchStopDetection(qualityIds) {
  return request({
    url: '/accountant/ss/quality-control/batch/stop-detection',
    method: 'put',
    data: qualityIds
  })
}

/**
 * 批量暂停检测
 * @param {Array} qualityIds 质量管控ID数组
 */
export function batchPauseDetection(qualityIds) {
  return request({
    url: '/accountant/ss/quality-control/batch/pause-detection',
    method: 'put',
    data: qualityIds
  })
}

/**
 * 批量恢复检测
 * @param {Array} qualityIds 质量管控ID数组
 */
export function batchResumeDetection(qualityIds) {
  return request({
    url: '/accountant/ss/quality-control/batch/resume-detection',
    method: 'put',
    data: qualityIds
  })
}

/**
 * 立即执行检测
 * @param {Number} qualityId 质量管控ID
 */
export function executeDetectionImmediately(qualityId) {
  return request({
    url: `/accountant/ss/quality-control/${qualityId}/execute-immediately`,
    method: 'put'
  })
}

/**
 * 重新检测
 * @param {Number} qualityId 质量管控ID
 */
export function reDetection(qualityId) {
  return request({
    url: `/accountant/ss/quality-control/${qualityId}/re-detection`,
    method: 'put'
  })
}

/**
 * 分配负责人
 * @param {Number} qualityId 质量管控ID
 * @param {Number} responsiblePersonId 负责人ID
 * @param {String} responsiblePersonName 负责人姓名
 */
export function assignResponsiblePerson(qualityId, responsiblePersonId, responsiblePersonName) {
  return request({
    url: `/accountant/ss/quality-control/${qualityId}/assign-responsible`,
    method: 'put',
    params: { responsiblePersonId, responsiblePersonName }
  })
}

/**
 * 批量分配负责人
 * @param {Array} qualityIds 质量管控ID数组
 * @param {Number} responsiblePersonId 负责人ID
 * @param {String} responsiblePersonName 负责人姓名
 */
export function batchAssignResponsiblePerson(qualityIds, responsiblePersonId, responsiblePersonName) {
  return request({
    url: '/accountant/ss/quality-control/batch/assign-responsible',
    method: 'put',
    data: { qualityIds, responsiblePersonId, responsiblePersonName }
  })
}

/**
 * 分配检测人员
 * @param {Number} qualityId 质量管控ID
 * @param {Number} inspectorId 检测人员ID
 * @param {String} inspectorName 检测人员姓名
 */
export function assignInspector(qualityId, inspectorId, inspectorName) {
  return request({
    url: `/accountant/ss/quality-control/${qualityId}/assign-inspector`,
    method: 'put',
    params: { inspectorId, inspectorName }
  })
}

/**
 * 批量分配检测人员
 * @param {Array} qualityIds 质量管控ID数组
 * @param {Number} inspectorId 检测人员ID
 * @param {String} inspectorName 检测人员姓名
 */
export function batchAssignInspector(qualityIds, inspectorId, inspectorName) {
  return request({
    url: '/accountant/ss/quality-control/batch/assign-inspector',
    method: 'put',
    data: { qualityIds, inspectorId, inspectorName }
  })
}

/**
 * 设置优先级
 * @param {Number} qualityId 质量管控ID
 * @param {Number} priority 优先级
 * @param {Number} priorityWeight 优先级权重
 */
export function setPriority(qualityId, priority, priorityWeight) {
  return request({
    url: `/accountant/ss/quality-control/${qualityId}/priority`,
    method: 'put',
    params: { priority, priorityWeight }
  })
}

/**
 * 批量设置优先级
 * @param {Array} qualityIds 质量管控ID数组
 * @param {Number} priority 优先级
 * @param {Number} priorityWeight 优先级权重
 */
export function batchSetPriority(qualityIds, priority, priorityWeight) {
  return request({
    url: '/accountant/ss/quality-control/batch/priority',
    method: 'put',
    data: { qualityIds, priority, priorityWeight }
  })
}

/**
 * 生成质量报告
 * @param {Number} qualityId 质量管控ID
 */
export function generateQualityReport(qualityId) {
  return request({
    url: `/accountant/ss/quality-control/${qualityId}/generate-report`,
    method: 'post'
  })
}

/**
 * 批量生成质量报告
 * @param {Array} qualityIds 质量管控ID数组
 */
export function batchGenerateQualityReport(qualityIds) {
  return request({
    url: '/accountant/ss/quality-control/batch/generate-report',
    method: 'post',
    data: qualityIds
  })
}

/**
 * 导入质量管控数据
 * @param {Array} qualityControlList 质量管控数据列表
 */
export function importQualityControlData(qualityControlList) {
  return request({
    url: '/accountant/ss/quality-control/import',
    method: 'post',
    data: qualityControlList
  })
}

/**
 * 导出质量管控数据
 * @param {Object} queryParams 查询参数
 */
export function exportQualityControlData(queryParams) {
  return request({
    url: '/accountant/ss/quality-control/export',
    method: 'get',
    params: queryParams
  })
}

/**
 * 发送通知
 * @param {Number} qualityId 质量管控ID
 * @param {String} notificationType 通知类型
 * @param {String} message 消息内容
 */
export function sendNotification(qualityId, notificationType, message) {
  return request({
    url: `/accountant/ss/quality-control/${qualityId}/notification`,
    method: 'post',
    params: { notificationType, message }
  })
}

/**
 * 批量发送通知
 * @param {Array} qualityIds 质量管控ID数组
 * @param {String} notificationType 通知类型
 * @param {String} message 消息内容
 */
export function batchSendNotification(qualityIds, notificationType, message) {
  return request({
    url: '/accountant/ss/quality-control/batch/notification',
    method: 'post',
    data: { qualityIds, notificationType, message }
  })
}

// 查询接口

/**
 * 查询待检测的质量管控
 * @param {Number} tenantId 租户ID
 */
export function getPendingDetection(tenantId = 1) {
  return request({
    url: '/accountant/ss/quality-control/pending-detection',
    method: 'get',
    params: { tenantId }
  })
}

/**
 * 查询正在检测的质量管控
 * @param {Number} tenantId 租户ID
 */
export function getInDetection(tenantId = 1) {
  return request({
    url: '/accountant/ss/quality-control/in-detection',
    method: 'get',
    params: { tenantId }
  })
}

/**
 * 查询已完成检测的质量管控
 * @param {Number} tenantId 租户ID
 */
export function getCompletedDetection(tenantId = 1) {
  return request({
    url: '/accountant/ss/quality-control/completed-detection',
    method: 'get',
    params: { tenantId }
  })
}

/**
 * 查询检测失败的质量管控
 * @param {Number} tenantId 租户ID
 */
export function getFailedDetection(tenantId = 1) {
  return request({
    url: '/accountant/ss/quality-control/failed-detection',
    method: 'get',
    params: { tenantId }
  })
}

/**
 * 查询需要改进的质量管控
 * @param {Number} tenantId 租户ID
 */
export function getNeedImprovement(tenantId = 1) {
  return request({
    url: '/accountant/ss/quality-control/need-improvement',
    method: 'get',
    params: { tenantId }
  })
}

/**
 * 查询高风险质量管控
 * @param {Number} tenantId 租户ID
 */
export function getHighRisk(tenantId = 1) {
  return request({
    url: '/accountant/ss/quality-control/high-risk',
    method: 'get',
    params: { tenantId }
  })
}

/**
 * 查询超期未检测的质量管控
 * @param {Number} tenantId 租户ID
 */
export function getOverdueDetection(tenantId = 1) {
  return request({
    url: '/accountant/ss/quality-control/overdue-detection',
    method: 'get',
    params: { tenantId }
  })
}

// 统计接口

/**
 * 统计质量管控数据
 * @param {Number} tenantId 租户ID
 */
export function getQualityControlStatistics(tenantId = 1) {
  return request({
    url: '/accountant/ss/quality-control/statistics',
    method: 'get',
    params: { tenantId }
  })
}

/**
 * 统计质量管控状态分布
 * @param {Number} tenantId 租户ID
 */
export function getQualityStatusDistribution(tenantId = 1) {
  return request({
    url: '/accountant/ss/quality-control/statistics/status-distribution',
    method: 'get',
    params: { tenantId }
  })
}

/**
 * 统计质量管控类型分布
 * @param {Number} tenantId 租户ID
 */
export function getQualityTypeDistribution(tenantId = 1) {
  return request({
    url: '/accountant/ss/quality-control/statistics/type-distribution',
    method: 'get',
    params: { tenantId }
  })
}

/**
 * 统计质量等级分布
 * @param {Number} tenantId 租户ID
 */
export function getQualityLevelDistribution(tenantId = 1) {
  return request({
    url: '/accountant/ss/quality-control/statistics/level-distribution',
    method: 'get',
    params: { tenantId }
  })
}

/**
 * 统计检测状态分布
 * @param {Number} tenantId 租户ID
 */
export function getDetectionStatusDistribution(tenantId = 1) {
  return request({
    url: '/accountant/ss/quality-control/statistics/detection-status-distribution',
    method: 'get',
    params: { tenantId }
  })
}

/**
 * 统计风险等级分布
 * @param {Number} tenantId 租户ID
 */
export function getRiskLevelDistribution(tenantId = 1) {
  return request({
    url: '/accountant/ss/quality-control/statistics/risk-level-distribution',
    method: 'get',
    params: { tenantId }
  })
}

/**
 * 统计质量管控趋势
 * @param {Object} params 查询参数
 */
export function getQualityControlTrend(params) {
  return request({
    url: '/accountant/ss/quality-control/statistics/trend',
    method: 'get',
    params
  })
}

// 快捷操作函数

/**
 * 快速创建质量管控
 * @param {String} qualityCode 质量管控编码
 * @param {String} qualityName 质量管控名称
 * @param {String} qualityType 质量管控类型
 * @param {String} description 描述
 */
export function quickCreateQualityControl(qualityCode, qualityName, qualityType, description) {
  const data = {
    qualityCode,
    qualityName,
    qualityType,
    description,
    qualityStatus: 'DRAFT',
    detectionStatus: 'PENDING',
    priority: 5,
    priorityWeight: 1.0,
    isEnabled: true,
    tenantId: 1
  }
  return createQualityControl(data)
}

/**
 * 快速启动检测
 * @param {Number} qualityId 质量管控ID
 */
export function quickStartDetection(qualityId) {
  return startDetection(qualityId)
}

/**
 * 快速完成检测
 * @param {Number} qualityId 质量管控ID
 * @param {String} result 检测结果
 * @param {Number} score 质量评分
 */
export function quickCompleteDetection(qualityId, result, score) {
  return completeDetection(qualityId, result, score)
}

/**
 * 获取质量管控概览数据
 * @param {Number} tenantId 租户ID
 */
export function getQualityControlOverview(tenantId = 1) {
  return Promise.all([
    getQualityControlStatistics(tenantId),
    getQualityStatusDistribution(tenantId),
    getQualityTypeDistribution(tenantId),
    getQualityLevelDistribution(tenantId),
    getDetectionStatusDistribution(tenantId),
    getRiskLevelDistribution(tenantId)
  ]).then(results => {
    return {
      statistics: results[0].data,
      statusDistribution: results[1].data,
      typeDistribution: results[2].data,
      levelDistribution: results[3].data,
      detectionStatusDistribution: results[4].data,
      riskLevelDistribution: results[5].data
    }
  })
}

// 工具函数

/**
 * 格式化质量管控状态
 * @param {String} status 状态
 */
export function formatQualityStatus(status) {
  const statusMap = {
    'DRAFT': '草稿',
    'ACTIVE': '活跃',
    'INACTIVE': '非活跃',
    'COMPLETED': '已完成',
    'CANCELLED': '已取消'
  }
  return statusMap[status] || status
}

/**
 * 格式化检测状态
 * @param {String} status 状态
 */
export function formatDetectionStatus(status) {
  const statusMap = {
    'PENDING': '待检测',
    'IN_DETECTION': '检测中',
    'PAUSED': '已暂停',
    'COMPLETED': '已完成',
    'STOPPED': '已停止',
    'FAILED': '检测失败'
  }
  return statusMap[status] || status
}

/**
 * 格式化质量等级
 * @param {String} level 等级
 */
export function formatQualityLevel(level) {
  const levelMap = {
    'EXCELLENT': '优秀',
    'GOOD': '良好',
    'AVERAGE': '一般',
    'POOR': '较差',
    'VERY_POOR': '很差'
  }
  return levelMap[level] || level
}

/**
 * 格式化质量类型
 * @param {String} type 类型
 */
export function formatQualityType(type) {
  const typeMap = {
    'PRODUCT_QUALITY': '产品质量',
    'SERVICE_QUALITY': '服务质量',
    'PROCESS_QUALITY': '过程质量',
    'SYSTEM_QUALITY': '系统质量',
    'DATA_QUALITY': '数据质量',
    'ENVIRONMENT_QUALITY': '环境质量'
  }
  return typeMap[type] || type
}

/**
 * 获取质量等级颜色
 * @param {String} level 等级
 */
export function getQualityLevelColor(level) {
  const colorMap = {
    'EXCELLENT': '#67C23A',
    'GOOD': '#409EFF',
    'AVERAGE': '#E6A23C',
    'POOR': '#F56C6C',
    'VERY_POOR': '#F56C6C'
  }
  return colorMap[level] || '#909399'
}

/**
 * 获取检测状态颜色
 * @param {String} status 状态
 */
export function getDetectionStatusColor(status) {
  const colorMap = {
    'PENDING': '#909399',
    'IN_DETECTION': '#409EFF',
    'PAUSED': '#E6A23C',
    'COMPLETED': '#67C23A',
    'STOPPED': '#F56C6C',
    'FAILED': '#F56C6C'
  }
  return colorMap[status] || '#909399'
}
