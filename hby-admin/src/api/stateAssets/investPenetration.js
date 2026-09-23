import request from '@/utils/request'

const JSON_HEADER = { 'Content-Type': 'application/json' }

// ========== 投资项目台账 ==========
export function getInvestProjectList(data) {
  return request({ url: '/monitor/v1/supervision/investment/project/list', method: 'post', data, headers: JSON_HEADER })
}
export function getInvestProjectDetail(id) {
  return request({ url: '/monitor/v1/supervision/investment/project/' + id, method: 'get' })
}
export function addInvestProject(data) {
  return request({ url: '/monitor/v1/supervision/investment/project/add', method: 'post', data, headers: JSON_HEADER })
}
export function updateInvestProject(data) {
  return request({ url: '/monitor/v1/supervision/investment/project/update', method: 'post', data, headers: JSON_HEADER })
}
export function deleteInvestProject(id) {
  return request({ url: '/monitor/v1/supervision/investment/project/' + id, method: 'delete' })
}
export function batchDeleteInvestProject(data) {
  return request({ url: '/monitor/v1/supervision/investment/project/batch/delete', method: 'post', data, headers: JSON_HEADER })
}
export function getInvestStatistics(companyId) {
  return request({ url: '/monitor/v1/supervision/investment/project/statistics', method: 'get', params: { companyId } })
}

// ========== 投资监控驾驶舱 ==========
export function getInvestDashboard(params) {
  return request({ url: '/monitor/v1/supervision/investment/dashboard/overview', method: 'get', params })
}

// ========== 非主业投资分析 ==========
export function getNonMainBizList(data) {
  return request({ url: '/monitor/v1/supervision/investment/non-main-biz/list', method: 'post', data, headers: JSON_HEADER })
}
export function getNonMainBizTrend(params) {
  return request({ url: '/monitor/v1/supervision/investment/non-main-biz/trend', method: 'get', params })
}
export function getNonMainBizStats(params) {
  return request({ url: '/monitor/v1/supervision/investment/non-main-biz/stats', method: 'get', params })
}

// ========== 投后评价管理 ==========
export function getPostEvalList(data) {
  return request({ url: '/monitor/v1/supervision/investment/post-eval/list', method: 'post', data, headers: JSON_HEADER })
}
export function startPostEval(data) {
  return request({ url: '/monitor/v1/supervision/investment/post-eval/start', method: 'post', data, headers: JSON_HEADER })
}
export function updatePostEval(data) {
  return request({ url: '/monitor/v1/supervision/investment/post-eval/update', method: 'post', data, headers: JSON_HEADER })
}

// ========== 投资进度监控 ==========
export function getProgressList(data) {
  return request({ url: '/monitor/v1/supervision/investment/progress/list', method: 'post', data, headers: JSON_HEADER })
}
export function getMilestoneList(data) {
  return request({ url: '/monitor/v1/supervision/investment/progress/milestone/list', method: 'post', data, headers: JSON_HEADER })
}
export function updateMilestone(data) {
  return request({ url: '/monitor/v1/supervision/investment/progress/milestone/update', method: 'post', data, headers: JSON_HEADER })
}

// ========== 投资风险预警管理 ==========
export function getInvestWarningList(data) {
  return request({ url: '/monitor/v1/supervision/investment/warning/list', method: 'post', data, headers: JSON_HEADER })
}
export function dispatchInvestWarning(data) {
  return request({ url: '/monitor/v1/supervision/investment/warning/dispatch', method: 'post', data, headers: JSON_HEADER })
}
export function handleInvestWarning(data) {
  return request({ url: '/monitor/v1/supervision/investment/warning/handle', method: 'post', data, headers: JSON_HEADER })
}

// ========== 合规追踪 ==========
export function getComplianceList(data) {
  return request({ url: '/monitor/v1/supervision/investment/compliance/list', method: 'post', data, headers: JSON_HEADER })
}
export function getComplianceChain(id) {
  return request({ url: '/monitor/v1/supervision/investment/compliance/chain/' + id, method: 'get' })
}
export function dispatchCompliance(data) {
  return request({ url: '/monitor/v1/supervision/investment/compliance/dispatch', method: 'post', data, headers: JSON_HEADER })
}

// ========== 穿透分析 ==========
export function getInvestDrillData(params) {
  return request({ url: '/monitor/v1/supervision/investment/drill/data', method: 'get', params })
}
export function getInvestTypeDistribution(params) {
  return request({ url: '/monitor/v1/supervision/investment/drill/type-dist', method: 'get', params })
}

// ========== 预警规则 ==========
export function getWarningRuleList() {
  return request({ url: '/monitor/v1/supervision/investment/warning/rules', method: 'get' })
}

