import request from '@/utils/request'
import { transData } from '@/utils/requestData'

/**
 * 风险事件API接口
 * 国资国企穿透式监管系统 - 风险穿透管控模块 - 风险事件功能
 */

// ==================== 基础CRUD操作 ====================

/**
 * 分页查询风险事件列表
 */
export function getRiskIncidentList(data) {
  return request({
    url: '/monitor/v1/supervision/risk/incident/list',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 根据ID获取风险事件详情
 */
export function getRiskIncidentById(id) {
  return request({
    url: `/monitor/v1/supervision/risk/incident/${id}`,
    method: 'get'
  })
}

/**
 * 新增风险事件
 */
export function addRiskIncident(data) {
  return request({
    url: '/monitor/v1/supervision/risk/incident/add',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 更新风险事件
 */
export function updateRiskIncident(data) {
  return request({
    url: '/monitor/v1/supervision/risk/incident/update',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 删除风险事件
 */
export function deleteRiskIncident(id) {
  return request({
    url: `/monitor/v1/supervision/risk/incident/${id}`,
    method: 'delete'
  })
}

// ==================== 事件报告业务 ====================

/**
 * 报告风险事件
 */
export function reportRiskIncident(data) {
  return request({
    url: '/monitor/v1/supervision/risk/incident/report',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 生成事件编号
 */
export function generateIncidentNumber(data) {
  return request({
    url: '/monitor/v1/supervision/risk/incident/generate-number',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 验证事件信息
 */
export function validateIncidentInfo(data) {
  return request({
    url: '/monitor/v1/supervision/risk/incident/validate-info',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 自动分类事件
 */
export function autoClassifyIncident(data) {
  return request({
    url: '/monitor/v1/supervision/risk/incident/auto-classify',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 评估事件紧急程度
 */
export function assessIncidentUrgency(data) {
  return request({
    url: '/monitor/v1/supervision/risk/incident/assess-urgency',
    method: 'post',
    data: transData(data)
  })
}

// ==================== 风险评估业务 ====================

/**
 * 评估事件风险等级
 */
export function assessIncidentRiskLevel(data) {
  return request({
    url: '/monitor/v1/supervision/risk/incident/assess-risk-level',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 计算风险评分
 */
export function calculateRiskScore(data) {
  return request({
    url: '/monitor/v1/supervision/risk/incident/calculate-risk-score',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 评估影响程度
 */
export function assessImpactLevel(data) {
  return request({
    url: '/monitor/v1/supervision/risk/incident/assess-impact-level',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 计算发生概率
 */
export function calculateProbability(data) {
  return request({
    url: '/monitor/v1/supervision/risk/incident/calculate-probability',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 分析影响范围
 */
export function analyzeImpactScope(data) {
  return request({
    url: '/monitor/v1/supervision/risk/incident/analyze-impact-scope',
    method: 'post',
    data: transData(data)
  })
}

// ==================== 损失评估业务 ====================

/**
 * 评估经济损失
 */
export function assessEconomicLoss(data) {
  return request({
    url: '/monitor/v1/supervision/risk/incident/assess-economic-loss',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 计算直接损失
 */
export function calculateDirectLoss(data) {
  return request({
    url: '/monitor/v1/supervision/risk/incident/calculate-direct-loss',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 计算间接损失
 */
export function calculateIndirectLoss(data) {
  return request({
    url: '/monitor/v1/supervision/risk/incident/calculate-indirect-loss',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 评估声誉损失
 */
export function assessReputationLoss(data) {
  return request({
    url: '/monitor/v1/supervision/risk/incident/assess-reputation-loss',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 计算恢复成本
 */
export function calculateRecoveryCost(data) {
  return request({
    url: '/monitor/v1/supervision/risk/incident/calculate-recovery-cost',
    method: 'post',
    data: transData(data)
  })
}

// ==================== 应急响应业务 ====================

/**
 * 启动应急响应
 */
export function initiateEmergencyResponse(data) {
  return request({
    url: '/monitor/v1/supervision/risk/incident/initiate-emergency-response',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 确定应急响应等级
 */
export function determineEmergencyResponseLevel(data) {
  return request({
    url: '/monitor/v1/supervision/risk/incident/determine-emergency-level',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 执行应急措施
 */
export function executeEmergencyMeasures(data) {
  return request({
    url: '/monitor/v1/supervision/risk/incident/execute-emergency-measures',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 结束应急响应
 */
export function endEmergencyResponse(data) {
  return request({
    url: '/monitor/v1/supervision/risk/incident/end-emergency-response',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 评估应急响应效果
 */
export function evaluateEmergencyResponseEffectiveness(id) {
  return request({
    url: `/monitor/v1/supervision/risk/incident/evaluate-emergency-effectiveness/${id}`,
    method: 'get'
  })
}

// ==================== 调查处理业务 ====================

/**
 * 开始事件调查
 */
export function startInvestigation(data) {
  return request({
    url: '/monitor/v1/supervision/risk/incident/start-investigation',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 分析事件原因
 */
export function analyzeCauses(data) {
  return request({
    url: '/monitor/v1/supervision/risk/incident/analyze-causes',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 识别根本原因
 */
export function identifyRootCause(data) {
  return request({
    url: '/monitor/v1/supervision/risk/incident/identify-root-cause',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 分配处理责任人
 */
export function assignHandler(data) {
  return request({
    url: '/monitor/v1/supervision/risk/incident/assign-handler',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 开始处理
 */
export function startHandling(data) {
  return request({
    url: '/monitor/v1/supervision/risk/incident/start-handling',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 完成处理
 */
export function completeHandling(data) {
  return request({
    url: '/monitor/v1/supervision/risk/incident/complete-handling',
    method: 'post',
    data: transData(data)
  })
}

// ==================== 预防措施业务 ====================

/**
 * 制定预防措施
 */
export function developPreventiveMeasures(data) {
  return request({
    url: '/monitor/v1/supervision/risk/incident/develop-preventive-measures',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 制定纠正措施
 */
export function developCorrectiveMeasures(data) {
  return request({
    url: '/monitor/v1/supervision/risk/incident/develop-corrective-measures',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 制定改进措施
 */
export function developImprovementMeasures(data) {
  return request({
    url: '/monitor/v1/supervision/risk/incident/develop-improvement-measures',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 跟踪措施实施
 */
export function trackMeasureImplementation(id) {
  return request({
    url: `/monitor/v1/supervision/risk/incident/track-measure-implementation/${id}`,
    method: 'get'
  })
}

/**
 * 评估措施效果
 */
export function evaluateMeasureEffectiveness(id) {
  return request({
    url: `/monitor/v1/supervision/risk/incident/evaluate-measure-effectiveness/${id}`,
    method: 'get'
  })
}

// ==================== 状态管理业务 ====================

/**
 * 更新事件状态
 */
export function updateIncidentStatus(data) {
  return request({
    url: '/monitor/v1/supervision/risk/incident/update-status',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 升级事件
 */
export function escalateIncident(data) {
  return request({
    url: '/monitor/v1/supervision/risk/incident/escalate',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 关闭事件
 */
export function closeIncident(data) {
  return request({
    url: '/monitor/v1/supervision/risk/incident/close',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 重新开启事件
 */
export function reopenIncident(data) {
  return request({
    url: '/monitor/v1/supervision/risk/incident/reopen',
    method: 'post',
    data: transData(data)
  })
}

// ==================== 统计分析业务 ====================

/**
 * 获取事件综合统计
 */
export function getComprehensiveStatistics() {
  return request({
    url: '/monitor/v1/supervision/risk/incident/comprehensive-statistics',
    method: 'get'
  })
}

/**
 * 获取企业事件概览
 */
export function getEnterpriseIncidentOverview(enterpriseId) {
  return request({
    url: `/monitor/v1/supervision/risk/incident/enterprise-overview/${enterpriseId}`,
    method: 'get'
  })
}

/**
 * 获取事件趋势分析
 */
export function getIncidentTrendAnalysis(data) {
  return request({
    url: '/monitor/v1/supervision/risk/incident/trend-analysis',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取事件类型分布
 */
export function getIncidentTypeDistribution() {
  return request({
    url: '/monitor/v1/supervision/risk/incident/type-distribution',
    method: 'get'
  })
}

/**
 * 获取损失统计分析
 */
export function getLossStatisticsAnalysis() {
  return request({
    url: '/monitor/v1/supervision/risk/incident/loss-statistics',
    method: 'get'
  })
}

// ==================== 批量操作业务 ====================

/**
 * 批量更新事件状态
 */
export function batchUpdateIncidentStatus(data) {
  return request({
    url: '/monitor/v1/supervision/risk/incident/batch-update-status',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 批量分配处理人员
 */
export function batchAssignHandler(data) {
  return request({
    url: '/monitor/v1/supervision/risk/incident/batch-assign-handler',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 批量关闭事件
 */
export function batchCloseIncidents(data) {
  return request({
    url: '/monitor/v1/supervision/risk/incident/batch-close',
    method: 'post',
    data: transData(data)
  })
}

// ==================== 导出业务 ====================

/**
 * 导出事件数据
 */
export function exportIncidentData(data) {
  return request({
    url: '/monitor/v1/supervision/risk/incident/export-data',
    method: 'post',
    data: transData(data),
    responseType: 'blob'
  })
}

/**
 * 导出事件报告
 */
export function exportIncidentReport(data) {
  return request({
    url: '/monitor/v1/supervision/risk/incident/export-report',
    method: 'post',
    data: transData(data),
    responseType: 'blob'
  })
}

/**
 * 获取风险事件详情
 */
export function getRiskIncidentDetail(id) {
  return request({
    url: `/monitor/v1/supervision/risk/incident/detail/${id}`,
    method: 'get'
  })
}

/**
 * 保存应急草稿
 */
export function saveEmergencyDraft(data) {
  return request({
    url: '/monitor/v1/supervision/risk/incident/save-emergency-draft',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 开始处理
 */
export function startProcessing(data) {
  return request({
    url: '/monitor/v1/supervision/risk/incident/start-processing',
    method: 'post',
    data: transData(data)
  })
}
