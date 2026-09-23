import request from '@/utils/request'

// 获取监管驾驶舱统计数据
export function getDashboardStats(params) {
  return request({
    url: '/monitor/v1/supervision/dashboard/overview',
    method: 'get',
    params
  })
}

// 获取股权穿透监管统计
export function getEquityPenetrationStats() {
  return request({
    url: '/api/v1/state-assets/dashboard/equity-stats',
    method: 'get'
  })
}

// 获取资产穿透监管统计
export function getAssetPenetrationStats() {
  return request({
    url: '/api/v1/state-assets/dashboard/asset-stats',
    method: 'get'
  })
}

// 获取财务穿透监管统计
export function getFinancialPenetrationStats() {
  return request({
    url: '/api/v1/state-assets/dashboard/financial-stats',
    method: 'get'
  })
}

// 获取风险穿透管控统计
export function getRiskPenetrationStats() {
  return request({
    url: '/api/v1/state-assets/dashboard/risk-stats',
    method: 'get'
  })
}

// 获取实时预警信息
export function getRealtimeAlerts(params) {
  return request({
    url: '/monitor/v1/supervision/dashboard/realtime-alerts',
    method: 'get',
    params
  })
}

// 获取监管态势全景数据
export function getSupervisionOverview() {
  return request({
    url: '/api/v1/state-assets/dashboard/supervision-overview',
    method: 'get'
  })
}

// 获取企业分布地图数据
export function getEnterpriseDistribution() {
  return request({
    url: '/api/v1/state-assets/dashboard/enterprise-distribution',
    method: 'get'
  })
}

// 获取监管指标趋势数据
export function getSupervisionTrends(params) {
  return request({
    url: '/monitor/v1/supervision/dashboard/supervision-trends',
    method: 'get',
    params
  })
}

// 获取风险预警趋势
export function getRiskAlertTrends(params) {
  return request({
    url: '/api/v1/state-assets/dashboard/risk-alert-trends',
    method: 'get',
    params
  })
}

// 处理预警事件
export function handleAlert(alertId, action) {
  return request({
    url: `/api/v1/state-assets/dashboard/alerts/${alertId}/handle`,
    method: 'post',
    data: { action }
  })
}

// 获取监管任务进度
export function getSupervisionTaskProgress() {
  return request({
    url: '/api/v1/state-assets/dashboard/task-progress',
    method: 'get'
  })
}

// 获取数据质量状态
export function getDataQualityStatus() {
  return request({
    url: '/api/v1/state-assets/dashboard/data-quality',
    method: 'get'
  })
}

// 获取关键绩效指标
export function getKPIData(params) {
  return request({
    url: '/monitor/v1/supervision/dashboard/kpi',
    method: 'get',
    params
  })
}

// 导出监管报告
export function exportSupervisionReport(params) {
  return request({
    url: '/api/v1/state-assets/dashboard/export-report',
    method: 'post',
    data: params,
    responseType: 'blob'
  })
}

// 获取监管效果评估数据
export function getSupervisionEffectiveness() {
  return request({
    url: '/monitor/v1/supervision/dashboard/supervision-effectiveness',
    method: 'get'
  })
}

// 获取政策影响分析数据
export function getPolicyImpactAnalysis(params) {
  return request({
    url: '/monitor/v1/supervision/dashboard/policy-impact',
    method: 'get',
    params
  })
}

// 获取决策支持数据
export function getDecisionSupportData(params) {
  return request({
    url: '/monitor/v1/supervision/dashboard/decision-support',
    method: 'get',
    params
  })
}

// 获取监管工作台数据
export function getSupervisionWorkbench() {
  return request({
    url: '/api/v1/state-assets/dashboard/workbench',
    method: 'get'
  })
}

// 获取待办任务列表
export function getTodoTasks(params) {
  return request({
    url: '/api/v1/state-assets/dashboard/todo-tasks',
    method: 'get',
    params
  })
}

// 更新任务状态
export function updateTaskStatus(taskId, status) {
  return request({
    url: `/api/v1/state-assets/dashboard/tasks/${taskId}/status`,
    method: 'put',
    data: { status }
  })
}

// 获取协同工作数据
export function getCollaborationData() {
  return request({
    url: '/api/v1/state-assets/dashboard/collaboration',
    method: 'get'
  })
}

// 获取知识库检索结果
export function searchKnowledgeBase(params) {
  return request({
    url: '/api/v1/state-assets/dashboard/knowledge-search',
    method: 'get',
    params
  })
}

// 获取企业审计数据
export function getEnterpriseAuditData(data) {
  return request({
    url: '/riskcontrol/enterpriseProfile/dashboard/auditData',
    method: 'post',
    headers: { 'Content-Type': 'application/json' },
    data
  })
}

// 获取企业法律案件数据
export function getEnterpriseLegalData(data) {
  return request({
    url: '/riskcontrol/enterpriseProfile/dashboard/legalData',
    method: 'post',
    headers: { 'Content-Type': 'application/json' },
    data
  })
}
