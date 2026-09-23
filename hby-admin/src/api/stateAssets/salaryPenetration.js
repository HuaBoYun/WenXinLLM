import request from '@/utils/request'
import { transData } from '@/utils/requestData'
const BASE = '/monitor/v1/supervision/salary'

// ========== 工资总额 ==========
export function getSalaryTotalList(data) {
  return request({ url: `${BASE}/total/list`, method: 'post', data: transData(data) })
}
export function saveSalaryTotal(data) {
  return request({ url: `${BASE}/total/save`, method: 'post', data: transData(data) })
}
export function deleteSalaryTotal(id) {
  return request({ url: `${BASE}/total/delete`, method: 'post', data: transData({ id }) })
}
export function getBudgetVsActual(companyId, year) {
  return request({ url: `${BASE}/total/budget-vs-actual`, method: 'post', data: transData({ companyId, year }) })
}
export function getSalaryTotalTrend(companyId) {
  return request({ url: `${BASE}/total/trend`, method: 'post', data: transData({ companyId }) })
}

// ========== 效益联动 ==========
export function getPerformanceLinkList(data) {
  return request({ url: `${BASE}/performance-link/analysis`, method: 'post', data: transData(data) })
}
export function calcLinkCoefficient(companyId, year) {
  return request({ url: `${BASE}/performance-link/coefficient`, method: 'post', data: transData({ companyId, year }) })
}
export function getLinkHistoryTrend(companyId) {
  return request({ url: `${BASE}/performance-link/trend`, method: 'post', data: transData({ companyId }) })
}

// ========== 高管薪酬 ==========
export function getExecutivePayList(data) {
  return request({ url: `${BASE}/executive/list`, method: 'post', data: transData(data) })
}
export function getExecutiveDetail(execId) {
  return request({ url: `${BASE}/executive/detail`, method: 'post', data: transData({ execId }) })
}
export function getExecutiveBenchmark(companyId) {
  return request({ url: `${BASE}/executive/benchmark`, method: 'post', data: transData({ companyId }) })
}

// ========== 激励计划 ==========
export function getIncentivePlanList(data) {
  return request({ url: `${BASE}/incentive/list`, method: 'post', data: transData(data) })
}
export function saveIncentivePlan(data) {
  return request({ url: `${BASE}/incentive/save`, method: 'post', data: transData(data) })
}
export function getIncentiveProgress(planId) {
  return request({ url: `${BASE}/incentive/progress`, method: 'post', data: transData({ planId }) })
}

// ========== 人工成本 ==========
export function getLaborCostList(data) {
  return request({ url: `${BASE}/labor-cost/analysis`, method: 'post', data: transData(data) })
}
export function getLaborCostTrend(companyId) {
  return request({ url: `${BASE}/labor-cost/trend`, method: 'post', data: transData({ companyId }) })
}
export function getLaborCostBenchmark(companyId) {
  return request({ url: `${BASE}/labor-cost/benchmark`, method: 'post', data: transData({ companyId }) })
}

// ========== 合规检查 ==========
export function runComplianceCheck(companyId) {
  return request({ url: `${BASE}/compliance/check`, method: 'post', data: transData({ companyId }) })
}
export function getComplianceIssueList(data) {
  return request({ url: `${BASE}/compliance/list`, method: 'post', data: transData(data) })
}
export function sendRectification(data) {
  return request({ url: `${BASE}/compliance/rectification`, method: 'post', data: transData(data) })
}

// ========== 监控接口 ==========
export function monitorBudgetExecution(companyId) {
  return request({ url: `${BASE}/monitor/budget-execution`, method: 'post', data: transData({ companyId }) })
}
export function monitorPerformanceLink(companyId) {
  return request({ url: `${BASE}/monitor/performance-link`, method: 'post', data: transData({ companyId }) })
}
export function monitorExecutivePay(companyId) {
  return request({ url: `${BASE}/monitor/executive-pay`, method: 'post', data: transData({ companyId }) })
}

// ========== 风险预警 ==========
export function getSalaryWarningList(data) {
  return request({ url: `${BASE}/warning/list`, method: 'post', data: transData(data) })
}
export function handleSalaryWarning(data) {
  return request({ url: `${BASE}/warning/handle`, method: 'post', data: transData(data) })
}
export function closeSalaryWarning(id) {
  return request({ url: `${BASE}/warning/close`, method: 'post', data: transData({ id }) })
}

// ========== 穿透分析 ==========
export function getSalaryDrillData(params) {
  return request({ url: `${BASE}/drill-down`, method: 'post', data: transData(params) })
}

// ========== 驾驶舱 ==========
export function getSalaryDashboard(companyId) {
  return request({ url: `${BASE}/dashboard`, method: 'post', data: transData({ companyId }) })
}


// ========== 工资总额（兼容 total 页面引用） ==========
export function addSalaryTotal(data) {
  return request({ url: `${BASE}/total/add`, method: 'post', data: transData(data) })
}
export function updateSalaryTotal(data) {
  return request({ url: `${BASE}/total/update`, method: 'post', data: transData(data) })
}
export function batchDeleteSalaryTotal(data) {
  return request({ url: `${BASE}/total/batch/delete`, method: 'post', data: transData(data) })
}
export function exportSalaryTotal(data) {
  return request({ url: `${BASE}/total/export`, method: 'post', data: transData(data) })
}

// ========== 人工成本 CRUD ==========
export function addLaborCost(data) {
  return request({ url: `${BASE}/labor-cost/add`, method: 'post', data: transData(data) })
}
export function updateLaborCost(data) {
  return request({ url: `${BASE}/labor-cost/update`, method: 'post', data: transData(data) })
}
export function deleteLaborCost(id) {
  return request({ url: `${BASE}/labor-cost/delete`, method: 'post', data: transData({ id }) })
}

// ========== 激励计划删除 ==========
export function deleteIncentivePlan(id) {
  return request({ url: `${BASE}/incentive/delete`, method: 'post', data: transData({ id }) })
}

// ========== 高管薪酬导出 ==========
export function exportExecutivePay(data) {
  return request({ url: `${BASE}/executive/export`, method: 'post', data: transData(data) })
}