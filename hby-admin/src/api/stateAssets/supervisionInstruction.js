/**
 * 监管指令协同管理API
 * 用于监管端向企业端下发指令及协同管理
 */
import { transData } from '@/utils/requestData'

// 监管指令管理
export const getSupervisionInstructionList = (params) => {
  return transData('/api/stateAssets/supervisionInstruction/list', 'get', params)
}

export const getSupervisionInstructionDetail = (id) => {
  return transData(`/api/stateAssets/supervisionInstruction/detail/${id}`, 'get')
}

export const createSupervisionInstruction = (data) => {
  return transData('/api/stateAssets/supervisionInstruction/create', 'post', data)
}

export const updateSupervisionInstruction = (data) => {
  return transData('/api/stateAssets/supervisionInstruction/update', 'post', data)
}

export const deleteSupervisionInstruction = (id) => {
  return transData(`/api/stateAssets/supervisionInstruction/delete/${id}`, 'delete')
}

// 指令统计分析
export const getInstructionStatistics = () => {
  return transData('/api/stateAssets/supervisionInstruction/statistics', 'get')
}

export const getInstructionTrend = (params) => {
  return transData('/api/stateAssets/supervisionInstruction/trend', 'get', params)
}

export const getInstructionStatusDistribution = () => {
  return transData('/api/stateAssets/supervisionInstruction/statusDistribution', 'get')
}

export const getInstructionTypeAnalysis = () => {
  return transData('/api/stateAssets/supervisionInstruction/typeAnalysis', 'get')
}

// 指令下发管理
export const issueInstruction = (data) => {
  return transData('/api/stateAssets/supervisionInstruction/issue', 'post', data)
}

export const batchIssueInstruction = (data) => {
  return transData('/api/stateAssets/supervisionInstruction/batch/issue', 'post', data)
}

export const getIssuedInstructions = (params) => {
  return transData('/api/stateAssets/supervisionInstruction/issued', 'get', params)
}

export const revokeInstruction = (instructionId, reason) => {
  return transData(`/api/stateAssets/supervisionInstruction/revoke/${instructionId}`, 'post', { reason })
}

// 指令执行跟踪
export const getExecutionProgress = (instructionId) => {
  return transData(`/api/stateAssets/supervisionInstruction/execution/progress/${instructionId}`, 'get')
}

export const updateExecutionProgress = (data) => {
  return transData('/api/stateAssets/supervisionInstruction/execution/update', 'post', data)
}

export const getExecutionHistory = (params) => {
  return transData('/api/stateAssets/supervisionInstruction/execution/history', 'get', params)
}

export const submitExecutionReport = (data) => {
  return transData('/api/stateAssets/supervisionInstruction/execution/report', 'post', data)
}

// 指令完成确认
export const confirmInstructionCompletion = (data) => {
  return transData('/api/stateAssets/supervisionInstruction/confirm/completion', 'post', data)
}

export const rejectInstructionCompletion = (data) => {
  return transData('/api/stateAssets/supervisionInstruction/reject/completion', 'post', data)
}

export const getCompletionReview = (instructionId) => {
  return transData(`/api/stateAssets/supervisionInstruction/completion/review/${instructionId}`, 'get')
}

export const evaluateExecutionEffect = (data) => {
  return transData('/api/stateAssets/supervisionInstruction/evaluate/effect', 'post', data)
}

// 指令模板管理
export const getInstructionTemplates = () => {
  return transData('/api/stateAssets/supervisionInstruction/templates', 'get')
}

export const createInstructionTemplate = (data) => {
  return transData('/api/stateAssets/supervisionInstruction/template/create', 'post', data)
}

export const updateInstructionTemplate = (data) => {
  return transData('/api/stateAssets/supervisionInstruction/template/update', 'post', data)
}

export const deleteInstructionTemplate = (id) => {
  return transData(`/api/stateAssets/supervisionInstruction/template/delete/${id}`, 'delete')
}

// 指令工作流管理
export const getWorkflowDefinitions = () => {
  return transData('/api/stateAssets/supervisionInstruction/workflow/definitions', 'get')
}

export const createWorkflowDefinition = (data) => {
  return transData('/api/stateAssets/supervisionInstruction/workflow/create', 'post', data)
}

export const getWorkflowInstances = (params) => {
  return transData('/api/stateAssets/supervisionInstruction/workflow/instances', 'get', params)
}

export const executeWorkflowTask = (data) => {
  return transData('/api/stateAssets/supervisionInstruction/workflow/execute', 'post', data)
}

// 指令通知管理
export const sendInstructionNotification = (data) => {
  return transData('/api/stateAssets/supervisionInstruction/notification/send', 'post', data)
}

export const getNotificationHistory = (params) => {
  return transData('/api/stateAssets/supervisionInstruction/notification/history', 'get', params)
}

export const configureNotificationRules = (data) => {
  return transData('/api/stateAssets/supervisionInstruction/notification/rules', 'post', data)
}

// 指令预警管理
export const setInstructionAlert = (data) => {
  return transData('/api/stateAssets/supervisionInstruction/alert/set', 'post', data)
}

export const getInstructionAlerts = (params) => {
  return transData('/api/stateAssets/supervisionInstruction/alert/list', 'get', params)
}

export const handleInstructionAlert = (data) => {
  return transData('/api/stateAssets/supervisionInstruction/alert/handle', 'post', data)
}

// 指令报告生成
export const generateInstructionReport = (data) => {
  return transData('/api/stateAssets/supervisionInstruction/report/generate', 'post', data)
}

export const getInstructionReports = (params) => {
  return transData('/api/stateAssets/supervisionInstruction/report/list', 'get', params)
}

export const downloadInstructionReport = (reportId) => {
  return transData(`/api/stateAssets/supervisionInstruction/report/download/${reportId}`, 'get')
}

// 批量操作
export const batchUpdateInstructions = (data) => {
  return transData('/api/stateAssets/supervisionInstruction/batch/update', 'post', data)
}

export const batchConfirmCompletion = (data) => {
  return transData('/api/stateAssets/supervisionInstruction/batch/confirm', 'post', data)
}

export const batchDelete = (ids) => {
  return transData('/api/stateAssets/supervisionInstruction/batch/delete', 'post', { ids })
}

// 数据导入导出
export const exportInstructionData = (params) => {
  return transData('/api/stateAssets/supervisionInstruction/export', 'get', params)
}

export const importInstructionData = (data) => {
  return transData('/api/stateAssets/supervisionInstruction/import', 'post', data)
}

export const getImportTemplate = () => {
  return transData('/api/stateAssets/supervisionInstruction/import/template', 'get')
}

// 实时监控
export const getRealtimeInstructionStatus = () => {
  return transData('/api/stateAssets/supervisionInstruction/realtime/status', 'get')
}

export const getInstructionMetrics = () => {
  return transData('/api/stateAssets/supervisionInstruction/realtime/metrics', 'get')
}

export const getInstructionHealth = () => {
  return transData('/api/stateAssets/supervisionInstruction/realtime/health', 'get')
}
