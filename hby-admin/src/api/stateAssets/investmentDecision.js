import request from '@/utils/request'
import { transData } from '@/utils/requestData'

const JSON_HEADERS = { 'Content-Type': 'application/json;charset=UTF-8' }

// 获取投资项目列表
export function getInvestmentProjectsList(data) {
  return request({
    url: '/monitor/v1/supervision/investment/projects/list',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 获取投资统计数据
export function getInvestmentStatistics(data) {
  return request({
    url: '/monitor/v1/supervision/investment/statistics',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 新增投资项目
export function addInvestmentProject(data) {
  return request({
    url: '/monitor/v1/supervision/investment/projects/add',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 更新投资项目
export function updateInvestmentProject(data) {
  return request({
    url: '/monitor/v1/supervision/investment/projects/update',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 删除投资项目
export function deleteInvestmentProject(data) {
  return request({
    url: '/monitor/v1/supervision/investment/projects/delete',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 获取投资项目详情
export function getInvestmentProjectDetail(data) {
  return request({
    url: '/monitor/v1/supervision/investment/projects/detail',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 投资项目审批
export function approveInvestmentProject(data) {
  return request({
    url: '/monitor/v1/supervision/investment/projects/approve',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 投资项目拒绝
export function rejectInvestmentProject(data) {
  return request({
    url: '/monitor/v1/supervision/investment/projects/reject',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 获取审批历史
export function getApprovalHistory(data) {
  return request({
    url: '/monitor/v1/supervision/investment/projects/approval/history',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 投资风险评估
export function assessInvestmentRisk(data) {
  return request({
    url: '/monitor/v1/supervision/investment/risk/assessment',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 获取风险评估报告
export function getRiskAssessmentReport(data) {
  return request({
    url: '/monitor/v1/supervision/investment/risk/report',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 投资效果分析
export function analyzeInvestmentEffect(data) {
  return request({
    url: '/monitor/v1/supervision/investment/effect/analysis',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 获取投资效果报告
export function getInvestmentEffectReport(data) {
  return request({
    url: '/monitor/v1/supervision/investment/effect/report',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 投资进度跟踪
export function trackInvestmentProgress(data) {
  return request({
    url: '/monitor/v1/supervision/investment/progress/track',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 获取投资进度数据
export function getInvestmentProgress(data) {
  return request({
    url: '/monitor/v1/supervision/investment/progress/data',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 投资决策流程监管
export function monitorDecisionProcess(data) {
  return request({
    url: '/monitor/v1/supervision/investment/decision/process',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 获取决策流程数据
export function getDecisionProcessData(data) {
  return request({
    url: '/monitor/v1/supervision/investment/decision/process/data',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 投资合规性检查
export function checkInvestmentCompliance(data) {
  return request({
    url: '/monitor/v1/supervision/investment/compliance/check',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 获取合规检查报告
export function getComplianceReport(data) {
  return request({
    url: '/monitor/v1/supervision/investment/compliance/report',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 投资预警设置
export function setInvestmentAlert(data) {
  return request({
    url: '/monitor/v1/supervision/investment/alert/set',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 获取投资预警列表
export function getInvestmentAlerts(data) {
  return request({
    url: '/monitor/v1/supervision/investment/alert/list',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 批量审批投资项目
export function batchApproveProjects(data) {
  return request({
    url: '/monitor/v1/supervision/investment/projects/batch/approve',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 批量风险评估
export function batchRiskAssessment(data) {
  return request({
    url: '/monitor/v1/supervision/investment/risk/batch/assessment',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 导出投资数据
export function exportInvestmentData(data) {
  return request({
    url: '/monitor/v1/supervision/investment/export',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
    responseType: 'blob',
  })
}

// 导入投资数据
export function importInvestmentData(data) {
  return request({
    url: '/monitor/v1/supervision/investment/import',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 获取投资标的信息
export function getInvestmentTargetInfo(data) {
  return request({
    url: '/monitor/v1/supervision/investment/target/info',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 投资标的风险分析
export function analyzeTargetRisk(data) {
  return request({
    url: '/monitor/v1/supervision/investment/target/risk/analysis',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 获取投资收益分析
export function getInvestmentReturnAnalysis(data) {
  return request({
    url: '/monitor/v1/supervision/investment/return/analysis',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 投资组合分析
export function analyzeInvestmentPortfolio(data) {
  return request({
    url: '/monitor/v1/supervision/investment/portfolio/analysis',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 获取投资趋势分析
export function getInvestmentTrendAnalysis(data) {
  return request({
    url: '/monitor/v1/supervision/investment/trend/analysis',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 投资决策支持
export function getInvestmentDecisionSupport(data) {
  return request({
    url: '/monitor/v1/supervision/investment/decision/support',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 获取投资建议
export function getInvestmentRecommendations(data) {
  return request({
    url: '/monitor/v1/supervision/investment/recommendations',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 投资监控预警
export function monitorInvestmentWarning(data) {
  return request({
    url: '/monitor/v1/supervision/investment/monitoring/warning',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 获取实时投资监控数据
export function getRealTimeMonitoringData(data) {
  return request({
    url: '/monitor/v1/supervision/investment/monitoring/realtime',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 导出投资决策（统一导出接口）
export function exportInvestmentDecision() {
  return request({ url: '/monitor/v1/supervision/export/equity/investmentDecision', method: 'get', responseType: 'blob' })
}
