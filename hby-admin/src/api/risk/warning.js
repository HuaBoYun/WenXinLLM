import request from '@/utils/request'

/**
 * 获取风险预警列表
 * @param {Object} params 查询参数
 */
export function getWarningList(params) {
  return request({
    url: '/riskcontrol/model/warning/list',
    method: 'post',
    data: params
  })
}

/**
 * 获取风险预警详情
 * @param {String} warningId 预警ID
 */
export function getWarningDetail(warningId) {
  return request({
    url: '/riskcontrol/model/warning/detail',
    method: 'post',
    data: { warningId }
  })
}

/**
 * 处理风险预警
 * @param {Object} params 处理参数
 */
export function processWarning(params) {
  return request({
    url: '/riskcontrol/model/warning/process',
    method: 'post',
    data: params
  })
}

/**
 * 批量处理风险预警
 * @param {Object} params 批量处理参数
 */
export function batchProcessWarning(params) {
  return request({
    url: '/riskcontrol/model/warning/batchProcess',
    method: 'post',
    data: params
  })
}

/**
 * 获取预警统计信息
 */
export function getWarningStatistics() {
  return request({
    url: '/riskcontrol/model/warning/statistics',
    method: 'post'
  })
}

/**
 * 获取预警配置
 */
export function getWarningConfig() {
  return request({
    url: '/riskcontrol/model/warning/config',
    method: 'post'
  })
}

/**
 * 更新预警配置
 * @param {Object} config 配置参数
 */
export function updateWarningConfig(config) {
  return request({
    url: '/riskcontrol/model/warning/updateConfig',
    method: 'post',
    data: config
  })
}

/**
 * 生成预警报告
 * @param {Object} params 报告参数
 */
export function generateWarningReport(params) {
  return request({
    url: '/riskcontrol/model/warning/generateReport',
    method: 'post',
    data: params
  })
}

/**
 * 获取模型预警数量
 * @param {String} modelId 模型ID
 */
export function getModelWarningCount(modelId) {
  return request({
    url: '/riskcontrol/model/warning/count',
    method: 'post',
    data: { modelId }
  })
}

/**
 * 获取模型预警结果
 * @param {Object} params 查询参数
 */
export function getModelWarningResults(params) {
  return request({
    url: '/riskcontrol/model/warning/results',
    method: 'post',
    data: params
  })
}

/**
 * 获取预警源数据
 * @param {Object} params 查询参数
 */
export function getWarningSourceData(params) {
  return request({
    url: '/riskcontrol/model/warning/sourceData',
    method: 'post',
    data: params
  })
}

/**
 * 获取预警数据穿透分析
 * @param {Object} params 查询参数
 */
export function getWarningDrillDownData(params) {
  return request({
    url: '/riskcontrol/model/warning/drillDown',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: params
  })
}

/**
 * 导出预警穿透数据
 * @param {Object} params 导出参数
 */
export function exportWarningDrillDownData(params) {
  return request({
    url: '/riskcontrol/model/warning/exportDrillDown',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: params
  })
}

/**
 * 标记预警为已读
 * @param {String} warningId 预警ID
 */
export function markWarningAsRead(warningId) {
  return request({
    url: '/riskcontrol/model/warning/markRead',
    method: 'post',
    data: { warningId }
  })
}

/**
 * 批量标记预警为已读
 * @param {Array} warningIds 预警ID列表
 */
export function batchMarkWarningAsRead(warningIds) {
  return request({
    url: '/riskcontrol/model/warning/batchMarkRead',
    method: 'post',
    data: { warningIds }
  })
}

/**
 * 自动生成预警
 * @param {Object} params 生成参数
 */
export function autoGenerateWarning(params) {
  return request({
    url: '/riskcontrol/model/warning/autoGenerate',
    method: 'post',
    data: params
  })
}

/**
 * 获取预警趋势数据
 * @param {Object} params 查询参数
 */
export function getWarningTrend(params) {
  return request({
    url: '/riskcontrol/model/warning/trend',
    method: 'post',
    data: params
  })
}

/**
 * 获取预警分布数据
 * @param {Object} params 查询参数
 */
export function getWarningDistribution(params) {
  return request({
    url: '/riskcontrol/model/warning/distribution',
    method: 'post',
    data: params
  })
}

/**
 * 导出预警列表
 * @param {Object} params 导出参数
 */
export function exportWarningList(params) {
  return request({
    url: '/riskcontrol/model/warning/export',
    method: 'post',
    data: params,
    responseType: 'blob'
  })
}

/**
 * 删除预警记录
 * @param {String} warningId 预警ID
 */
export function deleteWarning(warningId) {
  return request({
    url: '/riskcontrol/model/warning/delete',
    method: 'post',
    data: { warningId }
  })
}

/**
 * 批量删除预警记录
 * @param {Array} warningIds 预警ID列表
 */
export function batchDeleteWarning(warningIds) {
  return request({
    url: '/riskcontrol/model/warning/batchDelete',
    method: 'post',
    data: { warningIds }
  })
}

/**
 * 获取预警处理历史
 * @param {String} warningId 预警ID
 */
export function getWarningProcessHistory(warningId) {
  return request({
    url: '/riskcontrol/model/warning/processHistory',
    method: 'post',
    data: { warningId }
  })
}

/**
 * 预警升级
 * @param {Object} params 升级参数
 */
export function escalateWarning(params) {
  return request({
    url: '/riskcontrol/model/warning/escalate',
    method: 'post',
    data: params
  })
}

/**
 * 获取预警关联数据
 * @param {String} warningId 预警ID
 */
export function getWarningRelatedData(warningId) {
  return request({
    url: '/riskcontrol/model/warning/relatedData',
    method: 'post',
    data: { warningId }
  })
}

/**
 * 预警反馈
 * @param {Object} params 反馈参数
 */
export function submitWarningFeedback(params) {
  return request({
    url: '/riskcontrol/model/warning/feedback',
    method: 'post',
    data: params
  })
}
