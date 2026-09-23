import request from '@/utils/request'

// ==================== 风险评估管理 ====================

export function getRiskAssessmentList(data) {
  return request({ url: '/monitor/v1/enterprise/risk/assessment/list', method: 'post', headers: { 'Content-Type': 'application/json;charset=UTF-8' }, data })
}
export function getRiskAssessmentById(id) {
  return request({ url: `/monitor/v1/enterprise/risk/assessment/${id}`, method: 'get' })
}
export function addRiskAssessment(data) {
  return request({ url: '/monitor/v1/enterprise/risk/assessment', method: 'post', headers: { 'Content-Type': 'application/json;charset=UTF-8' }, data })
}
export function updateRiskAssessment(data) {
  return request({ url: '/monitor/v1/enterprise/risk/assessment', method: 'put', headers: { 'Content-Type': 'application/json;charset=UTF-8' }, data })
}
export function deleteRiskAssessment(id) {
  return request({ url: `/monitor/v1/enterprise/risk/assessment/${id}`, method: 'delete' })
}
export function submitRiskAssessment(data) {
  return request({ url: '/monitor/v1/enterprise/risk/assessment/submit', method: 'post', headers: { 'Content-Type': 'application/json;charset=UTF-8' }, data })
}
export function auditRiskAssessment(data) {
  return request({ url: '/monitor/v1/enterprise/risk/assessment/audit', method: 'post', headers: { 'Content-Type': 'application/json;charset=UTF-8' }, data })
}
export function analyzeRiskAssessment(data) {
  return request({ url: '/monitor/v1/enterprise/risk/assessment/analyze', method: 'post', headers: { 'Content-Type': 'application/json;charset=UTF-8' }, data })
}

// ==================== 风险识别管理 ====================

export function getRiskIdentificationList(data) {
  return request({ url: '/monitor/v1/enterprise/risk/identification/list', method: 'post', headers: { 'Content-Type': 'application/json;charset=UTF-8' }, data })
}
export function addRiskIdentification(data) {
  return request({ url: '/monitor/v1/enterprise/risk/identification', method: 'post', headers: { 'Content-Type': 'application/json;charset=UTF-8' }, data })
}
export function updateRiskIdentification(data) {
  return request({ url: '/monitor/v1/enterprise/risk/identification', method: 'put', headers: { 'Content-Type': 'application/json;charset=UTF-8' }, data })
}
export function deleteRiskIdentification(id) {
  return request({ url: `/monitor/v1/enterprise/risk/identification/${id}`, method: 'delete' })
}
export function analyzeRiskIdentification(data) {
  return request({ url: '/monitor/v1/enterprise/risk/identification/analyze', method: 'post', headers: { 'Content-Type': 'application/json;charset=UTF-8' }, data })
}

// ==================== 风险控制措施 ====================

export function getRiskControlList(data) {
  return request({ url: '/monitor/v1/enterprise/risk/control/list', method: 'post', headers: { 'Content-Type': 'application/json;charset=UTF-8' }, data })
}
export function getRiskControlById(id) {
  return request({ url: `/monitor/v1/enterprise/risk/control/${id}`, method: 'get' })
}
export function addRiskControl(data) {
  return request({ url: '/monitor/v1/enterprise/risk/control', method: 'post', headers: { 'Content-Type': 'application/json;charset=UTF-8' }, data })
}
export function updateRiskControl(data) {
  return request({ url: '/monitor/v1/enterprise/risk/control', method: 'put', headers: { 'Content-Type': 'application/json;charset=UTF-8' }, data })
}
export function deleteRiskControl(id) {
  return request({ url: `/monitor/v1/enterprise/risk/control/${id}`, method: 'delete' })
}
export function implementRiskControl(data) {
  return request({ url: '/monitor/v1/enterprise/risk/control/implement', method: 'post', headers: { 'Content-Type': 'application/json;charset=UTF-8' }, data })
}
export function evaluateControlEffectiveness(data) {
  return request({ url: '/monitor/v1/enterprise/risk/control/evaluate', method: 'post', headers: { 'Content-Type': 'application/json;charset=UTF-8' }, data })
}
export function analyzeRiskControl(data) {
  return request({ url: '/monitor/v1/enterprise/risk/control/analyze', method: 'post', headers: { 'Content-Type': 'application/json;charset=UTF-8' }, data })
}

// ==================== 风险监控预警 ====================

export function getRiskMonitoringList(data) {
  return request({ url: '/monitor/v1/enterprise/risk/monitoring/list', method: 'post', headers: { 'Content-Type': 'application/json;charset=UTF-8' }, data })
}
export function getRiskMonitoringById(id) {
  return request({ url: `/monitor/v1/enterprise/risk/monitoring/${id}`, method: 'get' })
}
export function addRiskMonitoring(data) {
  return request({ url: '/monitor/v1/enterprise/risk/monitoring', method: 'post', headers: { 'Content-Type': 'application/json;charset=UTF-8' }, data })
}
export function updateRiskMonitoring(data) {
  return request({ url: '/monitor/v1/enterprise/risk/monitoring', method: 'put', headers: { 'Content-Type': 'application/json;charset=UTF-8' }, data })
}
export function deleteRiskMonitoring(id) {
  return request({ url: `/monitor/v1/enterprise/risk/monitoring/${id}`, method: 'delete' })
}
export function getRiskWarningList(data) {
  return request({ url: '/monitor/v1/enterprise/risk/warning/list', method: 'post', headers: { 'Content-Type': 'application/json;charset=UTF-8' }, data })
}
export function handleRiskWarning(data) {
  return request({ url: '/monitor/v1/enterprise/risk/warning/handle', method: 'post', headers: { 'Content-Type': 'application/json;charset=UTF-8' }, data })
}
export function analyzeRiskMonitoring(data) {
  return request({ url: '/monitor/v1/enterprise/risk/monitoring/analyze', method: 'post', headers: { 'Content-Type': 'application/json;charset=UTF-8' }, data })
}

// ==================== 风险事件管理 ====================

export function getRiskIncidentList(data) {
  return request({ url: '/monitor/v1/enterprise/risk/incident/list', method: 'post', headers: { 'Content-Type': 'application/json;charset=UTF-8' }, data })
}
export function getRiskIncidentById(id) {
  return request({ url: `/monitor/v1/enterprise/risk/incident/${id}`, method: 'get' })
}
export function addRiskIncident(data) {
  return request({ url: '/monitor/v1/enterprise/risk/incident', method: 'post', headers: { 'Content-Type': 'application/json;charset=UTF-8' }, data })
}
export function updateRiskIncident(data) {
  return request({ url: '/monitor/v1/enterprise/risk/incident', method: 'put', headers: { 'Content-Type': 'application/json;charset=UTF-8' }, data })
}
export function deleteRiskIncident(id) {
  return request({ url: `/monitor/v1/enterprise/risk/incident/${id}`, method: 'delete' })
}
export function handleRiskIncident(data) {
  return request({ url: '/monitor/v1/enterprise/risk/incident/handle', method: 'post', headers: { 'Content-Type': 'application/json;charset=UTF-8' }, data })
}
export function closeRiskIncident(data) {
  return request({ url: '/monitor/v1/enterprise/risk/incident/close', method: 'post', headers: { 'Content-Type': 'application/json;charset=UTF-8' }, data })
}
export function analyzeRiskIncident(data) {
  return request({ url: '/monitor/v1/enterprise/risk/incident/analyze', method: 'post', headers: { 'Content-Type': 'application/json;charset=UTF-8' }, data })
}

// ==================== 风险分析报告 ====================

export function generateRiskReport(data) {
  return request({ url: '/monitor/v1/enterprise/risk/report/generate', method: 'post', headers: { 'Content-Type': 'application/json;charset=UTF-8' }, data })
}
export function getRiskReportList(data) {
  return request({ url: '/monitor/v1/enterprise/risk/report/list', method: 'post', headers: { 'Content-Type': 'application/json;charset=UTF-8' }, data })
}
export function downloadRiskReport(reportId) {
  return request({ url: `/monitor/v1/enterprise/risk/report/download/${reportId}`, method: 'get' })
}
export function getRiskTrendAnalysis(data) {
  return request({ url: '/monitor/v1/enterprise/risk/report/trend', method: 'post', headers: { 'Content-Type': 'application/json;charset=UTF-8' }, data })
}
export function getRiskDistributionAnalysis(data) {
  return request({ url: '/monitor/v1/enterprise/risk/report/distribution', method: 'post', headers: { 'Content-Type': 'application/json;charset=UTF-8' }, data })
}
export function analyzeRiskReport(data) {
  return request({ url: '/monitor/v1/enterprise/risk/report/analyze', method: 'post', headers: { 'Content-Type': 'application/json;charset=UTF-8' }, data })
}
export function updateRiskReport(data) {
  return request({ url: '/monitor/v1/enterprise/risk/report', method: 'put', headers: { 'Content-Type': 'application/json;charset=UTF-8' }, data })
}
export function deleteRiskReport(id) {
  return request({ url: `/monitor/v1/enterprise/risk/report/${id}`, method: 'delete' })
}

// ==================== 风险统计分析 ====================

export function getRiskStatistics(data) {
  return request({ url: '/monitor/v1/enterprise/risk/statistics', method: 'post', headers: { 'Content-Type': 'application/json;charset=UTF-8' }, data })
}
export function getRiskLevelStatistics(data) {
  return request({ url: '/monitor/v1/enterprise/risk/statistics/level', method: 'post', headers: { 'Content-Type': 'application/json;charset=UTF-8' }, data })
}
export function getRiskTypeStatistics(data) {
  return request({ url: '/monitor/v1/enterprise/risk/statistics/type', method: 'post', headers: { 'Content-Type': 'application/json;charset=UTF-8' }, data })
}
export function getRiskStatusStatistics(data) {
  return request({ url: '/monitor/v1/enterprise/risk/statistics/status', method: 'post', headers: { 'Content-Type': 'application/json;charset=UTF-8' }, data })
}
export function getControlMeasureStatistics(data) {
  return request({ url: '/monitor/v1/enterprise/risk/statistics/control', method: 'post', headers: { 'Content-Type': 'application/json;charset=UTF-8' }, data })
}

// ==================== 风险矩阵分析 ====================

export function getRiskMatrix(data) {
  return request({ url: '/monitor/v1/enterprise/risk/matrix', method: 'post', headers: { 'Content-Type': 'application/json;charset=UTF-8' }, data })
}
export function updateRiskMatrix(data) {
  return request({ url: '/monitor/v1/enterprise/risk/matrix', method: 'put', headers: { 'Content-Type': 'application/json;charset=UTF-8' }, data })
}
export function analyzeRiskMatrix(data) {
  return request({ url: '/monitor/v1/enterprise/risk/matrix/analyze', method: 'post', headers: { 'Content-Type': 'application/json;charset=UTF-8' }, data })
}

// ==================== 风险知识库 ====================

export function getRiskKnowledgeList(data) {
  return request({ url: '/monitor/v1/enterprise/risk/knowledge/list', method: 'post', headers: { 'Content-Type': 'application/json;charset=UTF-8' }, data })
}
export function addRiskKnowledge(data) {
  return request({ url: '/monitor/v1/enterprise/risk/knowledge', method: 'post', headers: { 'Content-Type': 'application/json;charset=UTF-8' }, data })
}
export function updateRiskKnowledge(data) {
  return request({ url: '/monitor/v1/enterprise/risk/knowledge', method: 'put', headers: { 'Content-Type': 'application/json;charset=UTF-8' }, data })
}
export function deleteRiskKnowledge(id) {
  return request({ url: `/monitor/v1/enterprise/risk/knowledge/${id}`, method: 'delete' })
}
export function searchRiskKnowledge(data) {
  return request({ url: '/monitor/v1/enterprise/risk/knowledge/search', method: 'post', headers: { 'Content-Type': 'application/json;charset=UTF-8' }, data })
}
export function analyzeRiskKnowledge(data) {
  return request({ url: '/monitor/v1/enterprise/risk/knowledge/analyze', method: 'post', headers: { 'Content-Type': 'application/json;charset=UTF-8' }, data })
}

// ==================== 数据提取辅助 ====================

export function extractData(response) {
  if (!response) return null
  if (response.data !== undefined) return response.data
  if (response.result === 200 && response.data !== undefined) return response.data
  return response
}
