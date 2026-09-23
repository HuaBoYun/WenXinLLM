/**
 * 风险协同预警管理API
 * 用于监管端与企业端的风险协同预警功能
 */
import { transData } from '@/utils/requestData'

// 风险协同记录管理
export const getRiskCollaborationList = (params) => {
  return transData('/api/stateAssets/riskCollaboration/list', 'get', params)
}

export const getRiskCollaborationDetail = (id) => {
  return transData(`/api/stateAssets/riskCollaboration/detail/${id}`, 'get')
}

export const createRiskCollaboration = (data) => {
  return transData('/api/stateAssets/riskCollaboration/create', 'post', data)
}

export const updateRiskCollaboration = (data) => {
  return transData('/api/stateAssets/riskCollaboration/update', 'post', data)
}

export const deleteRiskCollaboration = (id) => {
  return transData(`/api/stateAssets/riskCollaboration/delete/${id}`, 'delete')
}

// 风险协同统计分析
export const getRiskCollaborationStatistics = () => {
  return transData('/api/stateAssets/riskCollaboration/statistics', 'get')
}

export const getRiskCollaborationTrend = (params) => {
  return transData('/api/stateAssets/riskCollaboration/trend', 'get', params)
}

export const getRiskLevelDistribution = () => {
  return transData('/api/stateAssets/riskCollaboration/levelDistribution', 'get')
}

export const getRiskTypeAnalysis = () => {
  return transData('/api/stateAssets/riskCollaboration/typeAnalysis', 'get')
}

// 风险信息共享
export const shareRiskInformation = (data) => {
  return transData('/api/stateAssets/riskCollaboration/share', 'post', data)
}

export const getSharedRiskInfo = (params) => {
  return transData('/api/stateAssets/riskCollaboration/shared', 'get', params)
}

export const updateSharedRiskInfo = (data) => {
  return transData('/api/stateAssets/riskCollaboration/shared/update', 'post', data)
}

export const revokeRiskSharing = (shareId) => {
  return transData(`/api/stateAssets/riskCollaboration/shared/revoke/${shareId}`, 'post')
}

// 协同风险识别
export const executeCollaborativeRiskIdentification = (data) => {
  return transData('/api/stateAssets/riskCollaboration/identification/execute', 'post', data)
}

export const getIdentificationResults = (taskId) => {
  return transData(`/api/stateAssets/riskCollaboration/identification/results/${taskId}`, 'get')
}

export const getIdentificationHistory = (params) => {
  return transData('/api/stateAssets/riskCollaboration/identification/history', 'get', params)
}

export const confirmIdentificationResults = (data) => {
  return transData('/api/stateAssets/riskCollaboration/identification/confirm', 'post', data)
}

// 协同预警机制
export const setCollaborativeWarning = (data) => {
  return transData('/api/stateAssets/riskCollaboration/warning/set', 'post', data)
}

export const getCollaborativeWarnings = (params) => {
  return transData('/api/stateAssets/riskCollaboration/warning/list', 'get', params)
}

export const updateWarningStatus = (data) => {
  return transData('/api/stateAssets/riskCollaboration/warning/update', 'post', data)
}

export const getWarningStatistics = () => {
  return transData('/api/stateAssets/riskCollaboration/warning/statistics', 'get')
}

// 协同应急响应
export const initiateEmergencyResponse = (data) => {
  return transData('/api/stateAssets/riskCollaboration/emergency/initiate', 'post', data)
}

export const getEmergencyResponsePlan = (riskId) => {
  return transData(`/api/stateAssets/riskCollaboration/emergency/plan/${riskId}`, 'get')
}

export const updateResponseProgress = (data) => {
  return transData('/api/stateAssets/riskCollaboration/emergency/progress', 'post', data)
}

export const completeEmergencyResponse = (data) => {
  return transData('/api/stateAssets/riskCollaboration/emergency/complete', 'post', data)
}

// 风险评估协同
export const executeCollaborativeAssessment = (data) => {
  return transData('/api/stateAssets/riskCollaboration/assessment/execute', 'post', data)
}

export const getAssessmentResults = (assessmentId) => {
  return transData(`/api/stateAssets/riskCollaboration/assessment/results/${assessmentId}`, 'get')
}

export const compareAssessmentResults = (data) => {
  return transData('/api/stateAssets/riskCollaboration/assessment/compare', 'post', data)
}

export const finalizeAssessment = (data) => {
  return transData('/api/stateAssets/riskCollaboration/assessment/finalize', 'post', data)
}

// 风险处置协同
export const createCollaborativeDisposal = (data) => {
  return transData('/api/stateAssets/riskCollaboration/disposal/create', 'post', data)
}

export const getDisposalPlan = (disposalId) => {
  return transData(`/api/stateAssets/riskCollaboration/disposal/plan/${disposalId}`, 'get')
}

export const updateDisposalProgress = (data) => {
  return transData('/api/stateAssets/riskCollaboration/disposal/progress', 'post', data)
}

export const evaluateDisposalEffect = (data) => {
  return transData('/api/stateAssets/riskCollaboration/disposal/evaluate', 'post', data)
}

// 风险监控协同
export const setupCollaborativeMonitoring = (data) => {
  return transData('/api/stateAssets/riskCollaboration/monitoring/setup', 'post', data)
}

export const getMonitoringData = (params) => {
  return transData('/api/stateAssets/riskCollaboration/monitoring/data', 'get', params)
}

export const updateMonitoringRules = (data) => {
  return transData('/api/stateAssets/riskCollaboration/monitoring/rules', 'post', data)
}

export const getMonitoringAlerts = (params) => {
  return transData('/api/stateAssets/riskCollaboration/monitoring/alerts', 'get', params)
}

// 协同通知管理
export const sendCollaborationNotification = (data) => {
  return transData('/api/stateAssets/riskCollaboration/notification/send', 'post', data)
}

export const getNotificationHistory = (params) => {
  return transData('/api/stateAssets/riskCollaboration/notification/history', 'get', params)
}

export const configureNotificationRules = (data) => {
  return transData('/api/stateAssets/riskCollaboration/notification/rules', 'post', data)
}

// 协同报告生成
export const generateCollaborationReport = (data) => {
  return transData('/api/stateAssets/riskCollaboration/report/generate', 'post', data)
}

export const getCollaborationReports = (params) => {
  return transData('/api/stateAssets/riskCollaboration/report/list', 'get', params)
}

export const downloadCollaborationReport = (reportId) => {
  return transData(`/api/stateAssets/riskCollaboration/report/download/${reportId}`, 'get')
}

// 批量操作
export const batchShareRiskInfo = (data) => {
  return transData('/api/stateAssets/riskCollaboration/batch/share', 'post', data)
}

export const batchUpdateWarnings = (data) => {
  return transData('/api/stateAssets/riskCollaboration/batch/warnings', 'post', data)
}

export const batchDelete = (ids) => {
  return transData('/api/stateAssets/riskCollaboration/batch/delete', 'post', { ids })
}

// 数据导入导出
export const exportCollaborationData = (params) => {
  return transData('/api/stateAssets/riskCollaboration/export', 'get', params)
}

export const importCollaborationData = (data) => {
  return transData('/api/stateAssets/riskCollaboration/import', 'post', data)
}

export const getImportTemplate = () => {
  return transData('/api/stateAssets/riskCollaboration/import/template', 'get')
}

// 实时监控
export const getRealtimeCollaborationStatus = () => {
  return transData('/api/stateAssets/riskCollaboration/realtime/status', 'get')
}

export const getCollaborationMetrics = () => {
  return transData('/api/stateAssets/riskCollaboration/realtime/metrics', 'get')
}

export const getCollaborationHealth = () => {
  return transData('/api/stateAssets/riskCollaboration/realtime/health', 'get')
}
