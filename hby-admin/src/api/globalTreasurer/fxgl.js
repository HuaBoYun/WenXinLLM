import request from '@/utils/request'

// 风险管理模块API接口

// ==================== 风险监控管理 ====================

/**
 * 分页查询风险监控记录
 */
export function getRiskMonitoringPage(params) {
  return request({
    url: '/qqsk/risk-management/monitoring/page',
    method: 'get',
    params
  })
}

/**
 * 根据ID查询风险监控记录
 */
export function getRiskMonitoring(recordId) {
  return request({
    url: `/qqsk/risk-management/monitoring/${recordId}`,
    method: 'get'
  })
}

/**
 * 创建风险监控记录
 */
export function createRiskMonitoring(data) {
  return request({
    url: '/qqsk/risk-management/monitoring',
    method: 'post',
    data
  })
}

/**
 * 更新风险监控记录
 */
export function updateRiskMonitoring(data) {
  return request({
    url: '/qqsk/risk-management/monitoring',
    method: 'put',
    data
  })
}

/**
 * 删除风险监控记录
 */
export function deleteRiskMonitoring(recordId) {
  return request({
    url: `/qqsk/risk-management/monitoring/${recordId}`,
    method: 'delete'
  })
}

/**
 * 批量删除风险监控记录
 */
export function batchDeleteRiskMonitorings(recordIds) {
  return request({
    url: '/qqsk/risk-management/monitoring/batch',
    method: 'delete',
    data: recordIds
  })
}

/**
 * 触发风险警报
 */
export function triggerRiskAlert(recordId, alertMessage, userId) {
  return request({
    url: `/qqsk/risk-management/monitoring/${recordId}/trigger-alert`,
    method: 'post',
    params: {
      alertMessage,
      userId
    }
  })
}

/**
 * 处理风险监控
 */
export function handleRiskMonitoring(recordId, actionTaken, userId) {
  return request({
    url: `/qqsk/risk-management/monitoring/${recordId}/handle`,
    method: 'post',
    params: {
      actionTaken,
      userId
    }
  })
}

/**
 * 获取风险监控概览
 */
export function getRiskMonitoringOverview(orgId) {
  return request({
    url: '/qqsk/risk-management/monitoring/overview',
    method: 'get',
    params: { orgId }
  })
}

/**
 * 获取风险监控统计
 */
export function getRiskMonitoringStatistics(orgId) {
  return request({
    url: '/qqsk/risk-management/monitoring/statistics',
    method: 'get',
    params: { orgId }
  })
}

// ==================== 风险识别管理 ====================

/**
 * 分页查询风险识别记录
 */
export function getRiskIdentificationPage(params) {
  return request({
    url: '/qqsk/risk-management/identification/page',
    method: 'get',
    params
  })
}

/**
 * 根据ID查询风险识别记录
 */
export function getRiskIdentification(identificationId) {
  return request({
    url: `/qqsk/risk-management/identification/${identificationId}`,
    method: 'get'
  })
}

/**
 * 创建风险识别记录
 */
export function createRiskIdentification(data) {
  return request({
    url: '/qqsk/risk-management/identification',
    method: 'post',
    data
  })
}

/**
 * 更新风险识别记录
 */
export function updateRiskIdentification(data) {
  return request({
    url: '/qqsk/risk-management/identification',
    method: 'put',
    data
  })
}

/**
 * 删除风险识别记录
 */
export function deleteRiskIdentification(identificationId) {
  return request({
    url: `/qqsk/risk-management/identification/${identificationId}`,
    method: 'delete'
  })
}

/**
 * 获取风险类型定义
 */
export function getRiskTypes(params) {
  return request({
    url: '/qqsk/risk-management/risk-types',
    method: 'get',
    params
  })
}

/**
 * 创建风险类型
 */
export function createRiskType(data) {
  return request({
    url: '/qqsk/risk-management/risk-types',
    method: 'post',
    data
  })
}

/**
 * 更新风险类型
 */
export function updateRiskType(data) {
  return request({
    url: '/qqsk/risk-management/risk-types',
    method: 'put',
    data
  })
}

/**
 * 获取风险识别规则
 */
export function getIdentificationRules(params) {
  return request({
    url: '/qqsk/risk-management/identification-rules',
    method: 'get',
    params
  })
}

/**
 * 创建风险识别规则
 */
export function createIdentificationRule(data) {
  return request({
    url: '/qqsk/risk-management/identification-rules',
    method: 'post',
    data
  })
}

// ==================== 风险评估管理 ====================

/**
 * 分页查询风险评估记录
 */
export function getRiskAssessmentPage(params) {
  return request({
    url: '/qqsk/risk-management/assessment/page',
    method: 'get',
    params
  })
}

/**
 * 根据ID查询风险评估记录
 */
export function getRiskAssessment(assessmentId) {
  return request({
    url: `/qqsk/risk-management/assessment/${assessmentId}`,
    method: 'get'
  })
}

/**
 * 创建风险评估
 */
export function createRiskAssessment(data) {
  return request({
    url: '/qqsk/risk-management/assessment',
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

/**
 * 更新风险评估
 */
export function updateRiskAssessment(data) {
  return request({
    url: '/qqsk/risk-management/assessment',
    method: 'put',
    data,
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

/**
 * 删除风险评估记录
 */
export function deleteRiskAssessment(assessmentId) {
  return request({
    url: `/qqsk/risk-management/assessment/${assessmentId}`,
    method: 'delete'
  })
}

/**
 * 风险评估计算
 */
export function calculateRiskAssessment(data) {
  return request({
    url: '/qqsk/risk-management/assessment/calculate',
    method: 'post',
    data
  })
}

/**
 * 获取风险评估模型
 */
export function getAssessmentModels(params) {
  return request({
    url: '/qqsk/risk-management/assessment-models',
    method: 'get',
    params
  })
}

/**
 * 创建风险评估模型
 */
export function createAssessmentModel(data) {
  return request({
    url: '/qqsk/risk-management/assessment-models',
    method: 'post',
    data
  })
}

/**
 * 风险评估引擎执行
 */
export function executeAssessmentEngine(data) {
  return request({
    url: '/qqsk/risk-management/assessment/engine',
    method: 'post',
    data
  })
}

// ==================== 风险监控预警 ====================

/**
 * 获取风险监控仪表盘
 */
export function getRiskMonitoringDashboard(params) {
  return request({
    url: '/qqsk/risk-management/monitoring/dashboard',
    method: 'get',
    params
  })
}

/**
 * 分页查询风险监控指标
 */
export function getRiskMonitoringIndicators(params) {
  return request({
    url: '/qqsk/risk-management/monitoring/indicators',
    method: 'get',
    params
  })
}

/**
 * 创建监控指标
 */
export function createMonitoringIndicator(data) {
  return request({
    url: '/qqsk/risk-management/monitoring/indicators',
    method: 'post',
    data
  })
}

/**
 * 更新监控指标
 */
export function updateMonitoringIndicator(data) {
  return request({
    url: '/qqsk/risk-management/monitoring/indicators',
    method: 'put',
    data
  })
}

/**
 * 删除监控指标
 */
export function deleteMonitoringIndicator(indicatorId) {
  return request({
    url: `/qqsk/risk-management/monitoring/indicators/${indicatorId}`,
    method: 'delete'
  })
}

/**
 * 获取预警规则列表
 */
export function getAlertRules(params) {
  return request({
    url: '/qqsk/risk-management/monitoring/alert-rules',
    method: 'get',
    params
  })
}

/**
 * 创建预警规则
 */
export function createAlertRule(data) {
  return request({
    url: '/qqsk/risk-management/monitoring/alert-rules',
    method: 'post',
    data
  })
}

/**
 * 更新预警规则
 */
export function updateAlertRule(data) {
  return request({
    url: '/qqsk/risk-management/monitoring/alert-rules',
    method: 'put',
    data
  })
}

/**
 * 实时监控数据获取
 */
export function getRealTimeMonitoring(params) {
  return request({
    url: '/qqsk/risk-management/monitoring/real-time',
    method: 'get',
    params
  })
}

/**
 * 获取风险预警列表
 */
export function getRiskAlerts(params) {
  return request({
    url: '/qqsk/risk-management/monitoring/alerts',
    method: 'get',
    params
  })
}

/**
 * 处理风险预警
 */
export function handleRiskAlert(alertId, data) {
  return request({
    url: `/qqsk/risk-management/monitoring/alerts/${alertId}/handle`,
    method: 'post',
    data
  })
}

// ==================== 风险控制措施 ====================

/**
 * 分页查询风险控制策略
 */
export function getRiskControlStrategies(params) {
  return request({
    url: '/qqsk/risk-management/control/strategies',
    method: 'get',
    params
  })
}

/**
 * 创建风险控制策略
 */
export function createControlStrategy(data) {
  return request({
    url: '/qqsk/risk-management/control/strategies',
    method: 'post',
    data
  })
}

/**
 * 更新风险控制策略
 */
export function updateControlStrategy(data) {
  return request({
    url: '/qqsk/risk-management/control/strategies',
    method: 'put',
    data
  })
}

/**
 * 删除风险控制策略
 */
export function deleteControlStrategy(strategyId) {
  return request({
    url: `/qqsk/risk-management/control/strategies/${strategyId}`,
    method: 'delete'
  })
}

/**
 * 获取限额控制设置
 */
export function getLimitControls(params) {
  return request({
    url: '/qqsk/risk-management/control/limits',
    method: 'get',
    params
  })
}

/**
 * 创建限额控制
 */
export function createLimitControl(data) {
  return request({
    url: '/qqsk/risk-management/control/limits',
    method: 'post',
    data
  })
}

/**
 * 更新限额控制
 */
export function updateLimitControl(data) {
  return request({
    url: '/qqsk/risk-management/control/limits',
    method: 'put',
    data
  })
}

/**
 * 限额使用情况查询
 */
export function getLimitUsage(limitId, params) {
  return request({
    url: `/qqsk/risk-management/control/limits/${limitId}/usage`,
    method: 'get',
    params
  })
}

/**
 * 执行风险控制措施
 */
export function executeControlMeasures(data) {
  return request({
    url: '/qqsk/risk-management/control/execute',
    method: 'post',
    data
  })
}

/**
 * 获取控制措施执行记录
 */
export function getControlExecutionRecords(params) {
  return request({
    url: '/qqsk/risk-management/control/execution-records',
    method: 'get',
    params
  })
}

// ==================== 合规管理 ====================

/**
 * 分页查询合规检查记录
 */
export function getComplianceChecks(params) {
  return request({
    url: '/qqsk/risk-management/compliance/checks',
    method: 'get',
    params
  })
}

/**
 * 创建合规检查
 */
export function createComplianceCheck(data) {
  return request({
    url: '/qqsk/risk-management/compliance/checks',
    method: 'post',
    data
  })
}

/**
 * 更新合规检查
 */
export function updateComplianceCheck(data) {
  return request({
    url: '/qqsk/risk-management/compliance/checks',
    method: 'put',
    data
  })
}

/**
 * 执行合规检查
 */
export function executeComplianceCheck(checkId) {
  return request({
    url: `/qqsk/risk-management/compliance/checks/${checkId}/execute`,
    method: 'post'
  })
}

/**
 * 获取违规处理记录
 */
export function getViolationHandlingRecords(params) {
  return request({
    url: '/qqsk/risk-management/compliance/violations',
    method: 'get',
    params
  })
}

/**
 * 创建违规处理记录
 */
export function createViolationHandling(data) {
  return request({
    url: '/qqsk/risk-management/compliance/violations',
    method: 'post',
    data
  })
}

/**
 * 处理违规事件
 */
export function handleViolation(violationId, data) {
  return request({
    url: `/qqsk/risk-management/compliance/violations/${violationId}/handle`,
    method: 'post',
    data
  })
}

/**
 * 生成合规报告
 */
export function generateComplianceReport(params) {
  return request({
    url: '/qqsk/risk-management/compliance/report',
    method: 'post',
    data: params
  })
}

/**
 * 获取合规报告列表
 */
export function getComplianceReports(params) {
  return request({
    url: '/qqsk/risk-management/compliance/reports',
    method: 'get',
    params
  })
}

// ==================== 风险分析和报告 ====================

/**
 * 风险趋势分析
 */
export function getRiskTrendAnalysis(params) {
  return request({
    url: '/qqsk/risk-management/analysis/trend',
    method: 'get',
    params
  })
}

/**
 * 风险分布分析
 */
export function getRiskDistributionAnalysis(params) {
  return request({
    url: '/qqsk/risk-management/assessment/analysis/distribution',
    method: 'get',
    params
  })
}

/**
 * 风险关联性分析
 */
export function getRiskCorrelationAnalysis(params) {
  return request({
    url: '/qqsk/risk-management/analysis/correlation',
    method: 'get',
    params
  })
}

/**
 * 压力测试分析
 */
export function getStressTestAnalysis(data) {
  return request({
    url: '/qqsk/risk-management/analysis/stress-test',
    method: 'post',
    data
  })
}

/**
 * 情景分析
 */
export function getScenarioAnalysis(data) {
  return request({
    url: '/qqsk/risk-management/analysis/scenario',
    method: 'post',
    data
  })
}

/**
 * 生成风险报告
 */
export function generateRiskReport(params) {
  return request({
    url: '/qqsk/risk-management/report/generate',
    method: 'post',
    data: params
  })
}

/**
 * 获取风险报告列表
 */
export function getRiskReports(params) {
  return request({
    url: '/qqsk/risk-management/report/list',
    method: 'get',
    params
  })
}

/**
 * 下载风险报告
 */
export function downloadRiskReport(reportId) {
  return request({
    url: `/qqsk/risk-management/report/${reportId}/download`,
    method: 'get',
    responseType: 'blob'
  })
}

// ==================== 风险配置管理 ====================

/**
 * 获取风险参数配置
 */
export function getRiskParameters(params) {
  return request({
    url: '/qqsk/risk-management/config/parameters',
    method: 'get',
    params
  })
}

/**
 * 更新风险参数配置
 */
export function updateRiskParameters(data) {
  return request({
    url: '/qqsk/risk-management/config/parameters',
    method: 'put',
    data
  })
}

/**
 * 获取风险模型配置
 */
export function getRiskModelConfig(params) {
  return request({
    url: '/qqsk/risk-management/config/models',
    method: 'get',
    params
  })
}

/**
 * 更新风险模型配置
 */
export function updateRiskModelConfig(data) {
  return request({
    url: '/qqsk/risk-management/config/models',
    method: 'put',
    data
  })
}

/**
 * 风险系统健康检查
 */
export function riskSystemHealthCheck() {
  return request({
    url: '/qqsk/risk-management/config/health-check',
    method: 'get'
  })
}

// ==================== 导出功能 ====================

/**
 * 导出风险识别报表
 */
export function exportRiskIdentification(params) {
  return request({
    url: '/qqsk/risk-management/export/identification',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

/**
 * 导出风险评估报表
 */
export function exportRiskAssessment(params) {
  return request({
    url: '/qqsk/risk-management/export/assessment',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

/**
 * 导出风险监控报表
 */
export function exportRiskMonitoring(data) {
  return request({
    url: '/qqsk/risk-management/monitoring/export',
    method: 'post',
    data,
    responseType: 'blob'
  })
}

/**
 * 导出合规检查报表
 */
export function exportComplianceCheck(params) {
  return request({
    url: '/qqsk/risk-management/export/compliance',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

// ==================== 缺失的API函数 ====================

/**
 * 获取风险类型列表
 * @param {Object} params 查询参数
 */
export function listRiskType(params) {
  return request({
    url: '/qqsk/risk-management/risk-types/list',
    method: 'get',
    params
  })
}

/**
 * 根据ID查询风险类型
 * @param {Number} typeId 类型ID
 */
export function getRiskType(typeId) {
  return request({
    url: `/qqsk/risk-management/risk-types/${typeId}`,
    method: 'get'
  })
}

/**
 * 添加风险类型
 * @param {Object} data 类型数据
 */
export function addRiskType(data) {
  return request({
    url: '/qqsk/risk-management/risk-types',
    method: 'post',
    data
  })
}

/**
 * 删除风险类型
 * @param {Number} typeId 类型ID
 */
export function delRiskType(typeId) {
  return request({
    url: `/qqsk/risk-management/risk-types/${typeId}`,
    method: 'delete'
  })
}

/**
 * 切换风险类型状态
 * @param {Number} typeId 类型ID
 * @param {Number} status 状态
 */
export function toggleRiskTypeStatus(typeId, status) {
  return request({
    url: `/qqsk/risk-management/risk-types/${typeId}/status`,
    method: 'put',
    params: { status }
  })
}

/**
 * 获取风险统计信息
 * @param {Object} params 查询参数
 */
export function getRiskStatistics(params) {
  return request({
    url: '/qqsk/risk-management/risk-types/statistics',
    method: 'get',
    params
  })
}

/**
 * 导出风险类型数据
 * @param {Object} params 查询参数
 */
export function exportRiskType(params) {
  return request({
    url: '/qqsk/risk-management/risk-types/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

export function batchAssessRiskIdentification(data) {
  return request({
    url: '/qqsk/risk-management/identification/batch-assess',
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

export function closeRiskIdentification(identificationId, data) {
  return request({
    url: `/qqsk/risk-management/identification/${identificationId}/close`,
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

export function getRiskIdentificationHistory(identificationId) {
  return request({
    url: `/qqsk/risk-management/identification/${identificationId}/history`,
    method: 'get'
  })
}

export function importRiskAssessment(file) {
  const formData = new FormData()
  formData.append('file', file)
  return request({
    url: '/qqsk/risk-management/assessment/import',
    method: 'post',
    data: formData,
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

export function generateRiskAssessmentReport(assessmentId) {
  return request({
    url: `/qqsk/risk-management/assessment/${assessmentId}/report`,
    method: 'get',
    responseType: 'blob'
  })
}

export function getRiskAssessmentHistory(assessmentId) {
  return request({
    url: `/qqsk/risk-management/assessment/${assessmentId}/history`,
    method: 'get'
  })
}

// ==================== 风险控制管理 ====================

/**
 * 分页查询风险控制记录
 */
export function getRiskControlPage(params) {
  return request({
    url: '/qqsk/risk-management/control/page',
    method: 'get',
    params
  })
}

/**
 * 根据ID查询风险控制记录
 */
export function getRiskControl(controlId) {
  return request({
    url: `/qqsk/risk-management/control/${controlId}`,
    method: 'get'
  })
}

/**
 * 创建风险控制记录
 */
export function createRiskControl(data) {
  return request({
    url: '/qqsk/risk-management/control',
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

/**
 * 更新风险控制记录
 */
export function updateRiskControl(data) {
  return request({
    url: '/qqsk/risk-management/control',
    method: 'put',
    data,
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

/**
 * 删除风险控制记录
 */
export function deleteRiskControl(controlId) {
  return request({
    url: `/qqsk/risk-management/control/${controlId}`,
    method: 'delete'
  })
}

/**
 * 导出风险控制报表
 */
export function exportRiskControl(params) {
  return request({
    url: '/qqsk/risk-management/export/control',
    method: 'get',
    params,
    responseType: 'blob'
  })
}
