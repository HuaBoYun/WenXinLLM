/**
 * 数据协同管理API
 * 用于监管端与企业端的数据协同功能
 */
import { transData } from '@/utils/requestData'

// 数据协同记录管理
export const getDataCollaborationList = (params) => {
  return transData('/api/stateAssets/dataCollaboration/list', 'get', params)
}

export const getDataCollaborationDetail = (id) => {
  return transData(`/api/stateAssets/dataCollaboration/detail/${id}`, 'get')
}

export const createDataCollaboration = (data) => {
  return transData('/api/stateAssets/dataCollaboration/create', 'post', data)
}

export const updateDataCollaboration = (data) => {
  return transData('/api/stateAssets/dataCollaboration/update', 'post', data)
}

export const deleteDataCollaboration = (id) => {
  return transData(`/api/stateAssets/dataCollaboration/delete/${id}`, 'delete')
}

// 数据协同统计分析
export const getCollaborationStatistics = () => {
  return transData('/api/stateAssets/dataCollaboration/statistics', 'get')
}

export const getCollaborationTrend = (params) => {
  return transData('/api/stateAssets/dataCollaboration/trend', 'get', params)
}

export const getCollaborationStatusDistribution = () => {
  return transData('/api/stateAssets/dataCollaboration/statusDistribution', 'get')
}

export const getCollaborationTypeAnalysis = () => {
  return transData('/api/stateAssets/dataCollaboration/typeAnalysis', 'get')
}

// 数据报送协同
export const executeDataSubmission = (data) => {
  return transData('/api/stateAssets/dataCollaboration/submission/execute', 'post', data)
}

export const getSubmissionProgress = (taskId) => {
  return transData(`/api/stateAssets/dataCollaboration/submission/progress/${taskId}`, 'get')
}

export const getSubmissionHistory = (params) => {
  return transData('/api/stateAssets/dataCollaboration/submission/history', 'get', params)
}

export const validateSubmissionData = (data) => {
  return transData('/api/stateAssets/dataCollaboration/submission/validate', 'post', data)
}

// 数据标准统一
export const getDataStandards = () => {
  return transData('/api/stateAssets/dataCollaboration/standards', 'get')
}

export const updateDataStandards = (data) => {
  return transData('/api/stateAssets/dataCollaboration/standards/update', 'post', data)
}

export const validateDataFormat = (data) => {
  return transData('/api/stateAssets/dataCollaboration/standards/validate', 'post', data)
}

export const getDataMapping = (sourceType, targetType) => {
  return transData(`/api/stateAssets/dataCollaboration/standards/mapping/${sourceType}/${targetType}`, 'get')
}

// 数据质量协同
export const executeQualityCheck = (data) => {
  return transData('/api/stateAssets/dataCollaboration/quality/check', 'post', data)
}

export const getQualityReport = (checkId) => {
  return transData(`/api/stateAssets/dataCollaboration/quality/report/${checkId}`, 'get')
}

export const getQualityTrend = (params) => {
  return transData('/api/stateAssets/dataCollaboration/quality/trend', 'get', params)
}

export const handleQualityIssues = (data) => {
  return transData('/api/stateAssets/dataCollaboration/quality/handle', 'post', data)
}

// 数据同步管理
export const executeSyncTask = (data) => {
  return transData('/api/stateAssets/dataCollaboration/sync/execute', 'post', data)
}

export const getSyncStatus = (taskId) => {
  return transData(`/api/stateAssets/dataCollaboration/sync/status/${taskId}`, 'get')
}

export const getSyncHistory = (params) => {
  return transData('/api/stateAssets/dataCollaboration/sync/history', 'get', params)
}

export const configureSyncRules = (data) => {
  return transData('/api/stateAssets/dataCollaboration/sync/rules', 'post', data)
}

// 协同预警管理
export const setCollaborationAlert = (data) => {
  return transData('/api/stateAssets/dataCollaboration/alert/set', 'post', data)
}

export const getCollaborationAlerts = (params) => {
  return transData('/api/stateAssets/dataCollaboration/alert/list', 'get', params)
}

export const handleCollaborationAlert = (data) => {
  return transData('/api/stateAssets/dataCollaboration/alert/handle', 'post', data)
}

export const getAlertStatistics = () => {
  return transData('/api/stateAssets/dataCollaboration/alert/statistics', 'get')
}

// 协同报告生成
export const generateCollaborationReport = (data) => {
  return transData('/api/stateAssets/dataCollaboration/report/generate', 'post', data)
}

export const getCollaborationReports = (params) => {
  return transData('/api/stateAssets/dataCollaboration/report/list', 'get', params)
}

export const downloadCollaborationReport = (reportId) => {
  return transData(`/api/stateAssets/dataCollaboration/report/download/${reportId}`, 'get')
}

// 批量操作
export const batchSyncData = (data) => {
  return transData('/api/stateAssets/dataCollaboration/batch/sync', 'post', data)
}

export const batchQualityCheck = (data) => {
  return transData('/api/stateAssets/dataCollaboration/batch/qualityCheck', 'post', data)
}

export const batchDelete = (ids) => {
  return transData('/api/stateAssets/dataCollaboration/batch/delete', 'post', { ids })
}

// 数据导入导出
export const exportCollaborationData = (params) => {
  return transData('/api/stateAssets/dataCollaboration/export', 'get', params)
}

export const importCollaborationData = (data) => {
  return transData('/api/stateAssets/dataCollaboration/import', 'post', data)
}

export const getImportTemplate = () => {
  return transData('/api/stateAssets/dataCollaboration/import/template', 'get')
}

// 实时监控
export const getRealtimeCollaborationStatus = () => {
  return transData('/api/stateAssets/dataCollaboration/realtime/status', 'get')
}

export const getCollaborationMetrics = () => {
  return transData('/api/stateAssets/dataCollaboration/realtime/metrics', 'get')
}

export const getCollaborationHealth = () => {
  return transData('/api/stateAssets/dataCollaboration/realtime/health', 'get')
}
