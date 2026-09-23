import request from '@/utils/request'

// 企业财务模块统一请求方法（后端使用@RequestBody，需要JSON格式）
const JSON_HEADERS = { 'Content-Type': 'application/json;charset=UTF-8' }

// ==================== 财务报表管理 ====================

// 获取财务报表列表
export function getFinancialStatementList(data) {
  return request({
    url: '/monitor/v1/enterprise/financial/statement/list',
    method: 'post',
    headers: JSON_HEADERS,
    data
  })
}

// 获取财务报表详情
export function getFinancialStatementById(id) {
  return request({
    url: `/monitor/v1/enterprise/financial/statement/${id}`,
    method: 'get',
    headers: JSON_HEADERS
  })
}

// 新增财务报表
export function addFinancialStatement(data) {
  return request({
    url: '/monitor/v1/enterprise/financial/statement',
    method: 'post',
    headers: JSON_HEADERS,
    data
  })
}

// 更新财务报表
export function updateFinancialStatement(data) {
  return request({
    url: '/monitor/v1/enterprise/financial/statement',
    method: 'put',
    headers: JSON_HEADERS,
    data
  })
}

// 删除财务报表
export function deleteFinancialStatement(id) {
  return request({
    url: `/monitor/v1/enterprise/financial/statement/${id}`,
    method: 'delete',
    headers: JSON_HEADERS
  })
}

// 提交财务报表
export function submitFinancialStatement(data) {
  return request({
    url: '/monitor/v1/enterprise/financial/statement/submit',
    method: 'post',
    headers: JSON_HEADERS,
    data
  })
}

// 审核财务报表
export function auditFinancialStatement(data) {
  return request({
    url: '/monitor/v1/enterprise/financial/statement/audit',
    method: 'post',
    headers: JSON_HEADERS,
    data
  })
}

// 获取财务报表模板
export function getStatementTemplate(templateType) {
  return request({
    url: `/monitor/v1/enterprise/financial/statement/template/${templateType}`,
    method: 'get',
    headers: JSON_HEADERS,
    responseType: 'blob'
  })
}

// ==================== 预算管理 ====================

// 获取预算列表
export function getBudgetList(data) {
  return request({
    url: '/monitor/v1/enterprise/financial/budget/list',
    method: 'post',
    headers: JSON_HEADERS,
    data
  })
}

// 获取预算详情
export function getBudgetById(id) {
  return request({
    url: `/monitor/v1/enterprise/financial/budget/${id}`,
    method: 'get',
    headers: JSON_HEADERS
  })
}

// 新增预算
export function addBudget(data) {
  return request({
    url: '/monitor/v1/enterprise/financial/budget',
    method: 'post',
    headers: JSON_HEADERS,
    data
  })
}

// 更新预算
export function updateBudget(data) {
  return request({
    url: '/monitor/v1/enterprise/financial/budget',
    method: 'put',
    headers: JSON_HEADERS,
    data
  })
}

// 删除预算
export function deleteBudget(id) {
  return request({
    url: `/monitor/v1/enterprise/financial/budget/${id}`,
    method: 'delete',
    headers: JSON_HEADERS
  })
}

// 提交预算
export function submitBudget(data) {
  return request({
    url: '/monitor/v1/enterprise/financial/budget/submit',
    method: 'post',
    headers: JSON_HEADERS,
    data
  })
}

// 审批预算
export function approveBudget(data) {
  return request({
    url: '/monitor/v1/enterprise/financial/budget/approve',
    method: 'post',
    headers: JSON_HEADERS,
    data
  })
}

// 获取预算执行情况
export function getBudgetExecution(data) {
  return request({
    url: '/monitor/v1/enterprise/financial/budget/execution',
    method: 'post',
    headers: JSON_HEADERS,
    data
  })
}

// 预算调整
export function adjustBudget(data) {
  return request({
    url: '/monitor/v1/enterprise/financial/budget/adjust',
    method: 'post',
    headers: JSON_HEADERS,
    data
  })
}

// 获取预算分析
export function getBudgetAnalysis(data) {
  return request({
    url: '/monitor/v1/enterprise/financial/budget/analysis',
    method: 'post',
    headers: JSON_HEADERS,
    data
  })
}

// 获取预算统计(EnterpriseFinancialController - GZCT_ENTERPRISE_BUDGET表)
export function getBudgetStatistics(data) {
  return request({
    url: '/monitor/v1/enterprise/financial/budget/statistics',
    method: 'post',
    headers: JSON_HEADERS,
    data
  })
}

// 获取预算统计(BudgetManagementController - GZCT_BUDGET_MANAGEMENT表)
export function getBudgetManagementStatistics(enterpriseId) {
  return request({
    url: `/monitor/v1/enterprise/financial/budget/statistics/${enterpriseId}`,
    method: 'get',
    headers: JSON_HEADERS
  })
}

// 批量删除预算(BudgetManagementController)
export function batchDeleteBudget(budgetIds, updateBy) {
  return request({
    url: '/monitor/v1/enterprise/financial/budget/batchDelete',
    method: 'delete',
    headers: JSON_HEADERS,
    data: budgetIds,
    params: { updateBy }
  })
}

// 批量更新预算状态(BudgetManagementController)
export function batchUpdateBudgetStatus(budgetIds, status, updateBy) {
  return request({
    url: '/monitor/v1/enterprise/financial/budget/batchUpdateBudgetStatus',
    method: 'put',
    headers: JSON_HEADERS,
    data: budgetIds,
    params: { status, updateBy }
  })
}

// ==================== 成本管理 ====================

// 获取成本核算列表
export function getCostAccountingList(data) {
  return request({
    url: '/monitor/v1/enterprise/financial/cost/list',
    method: 'post',
    headers: JSON_HEADERS,
    data
  })
}

// 获取成本核算详情
export function getCostAccountingById(id) {
  return request({
    url: `/monitor/v1/enterprise/financial/cost/${id}`,
    method: 'get',
    headers: JSON_HEADERS
  })
}

// 新增成本核算
export function addCostAccounting(data) {
  return request({
    url: '/monitor/v1/enterprise/financial/cost',
    method: 'post',
    headers: JSON_HEADERS,
    data
  })
}

// 更新成本核算
export function updateCostAccounting(data) {
  return request({
    url: '/monitor/v1/enterprise/financial/cost',
    method: 'put',
    headers: JSON_HEADERS,
    data
  })
}

// 删除成本核算
export function deleteCostAccounting(id) {
  return request({
    url: `/monitor/v1/enterprise/financial/cost/${id}`,
    method: 'delete',
    headers: JSON_HEADERS
  })
}

// 成本分析
export function analyzeCost(data) {
  return request({
    url: '/monitor/v1/enterprise/financial/cost/analyze',
    method: 'post',
    headers: JSON_HEADERS,
    data
  })
}

// 成本控制
export function controlCost(data) {
  return request({
    url: '/monitor/v1/enterprise/financial/cost/control',
    method: 'post',
    headers: JSON_HEADERS,
    data
  })
}

// 获取成本趋势
export function getCostTrend(data) {
  return request({
    url: '/monitor/v1/enterprise/financial/cost/trend',
    method: 'post',
    headers: JSON_HEADERS,
    data
  })
}

// ==================== 财务分析 ====================

// 获取财务指标分析
export function getFinancialIndicators(data) {
  return request({ url: '/monitor/v1/enterprise/financial/analysis/indicators', method: 'post', headers: JSON_HEADERS, data })
}

// 获取盈利能力分析
export function getProfitabilityAnalysis(data) {
  return request({ url: '/monitor/v1/enterprise/financial/analysis/profitability', method: 'post', headers: JSON_HEADERS, data })
}

// 获取偿债能力分析
export function getSolvencyAnalysis(data) {
  return request({ url: '/monitor/v1/enterprise/financial/analysis/solvency', method: 'post', headers: JSON_HEADERS, data })
}

// 获取运营能力分析
export function getOperatingAnalysis(data) {
  return request({ url: '/monitor/v1/enterprise/financial/analysis/operating', method: 'post', headers: JSON_HEADERS, data })
}

// 获取发展能力分析
export function getDevelopmentAnalysis(data) {
  return request({ url: '/monitor/v1/enterprise/financial/analysis/development', method: 'post', headers: JSON_HEADERS, data })
}

// 财务综合分析
export function getComprehensiveAnalysis(data) {
  return request({ url: '/monitor/v1/enterprise/financial/analysis/comprehensive', method: 'post', headers: JSON_HEADERS, data })
}

// 同行业对比分析
export function getIndustryComparison(data) {
  return request({ url: '/monitor/v1/enterprise/financial/analysis/industry-comparison', method: 'post', headers: JSON_HEADERS, data })
}

// 历史趋势分析
export function getHistoricalTrend(data) {
  return request({ url: '/monitor/v1/enterprise/financial/analysis/historical-trend', method: 'post', headers: JSON_HEADERS, data })
}

// ==================== 财务预警 ====================

// 获取财务预警列表
export function getFinancialWarningList(data) {
  return request({ url: '/monitor/v1/enterprise/financial/warning/list', method: 'post', headers: JSON_HEADERS, data })
}

// 设置预警规则
export function setWarningRules(data) {
  return request({ url: '/monitor/v1/enterprise/financial/warning/rules', method: 'post', headers: JSON_HEADERS, data })
}

// 获取预警规则
export function getWarningRules(data) {
  return request({ url: '/monitor/v1/enterprise/financial/warning/rules', method: 'get', headers: JSON_HEADERS, params: data })
}

// 处理预警
export function handleWarning(data) {
  return request({ url: '/monitor/v1/enterprise/financial/warning/handle', method: 'post', headers: JSON_HEADERS, data })
}

// ==================== 财务报告 ====================

// 生成财务分析报告
export function generateFinancialReport(data) {
  return request({ url: '/monitor/v1/enterprise/financial/report/generate', method: 'post', headers: JSON_HEADERS, data })
}

// 获取财务报告列表
export function getFinancialReportList(data) {
  return request({ url: '/monitor/v1/enterprise/financial/report/list', method: 'post', headers: JSON_HEADERS, data })
}

// 下载财务报告
export function downloadFinancialReport(reportId) {
  return request({ url: `/monitor/v1/enterprise/financial/report/download/${reportId}`, method: 'get', headers: JSON_HEADERS, responseType: 'blob' })
}

// ==================== 财务统计 ====================

// 获取财务统计概览
export function getFinancialStatistics(data) {
  return request({ url: '/monitor/v1/enterprise/financial/statistics', method: 'post', headers: JSON_HEADERS, data })
}

// 获取收入统计
export function getRevenueStatistics(data) {
  return request({ url: '/monitor/v1/enterprise/financial/statistics/revenue', method: 'post', headers: JSON_HEADERS, data })
}

// 获取利润统计
export function getProfitStatistics(data) {
  return request({ url: '/monitor/v1/enterprise/financial/statistics/profit', method: 'post', headers: JSON_HEADERS, data })
}

// 获取资产统计
export function getAssetStatistics(data) {
  return request({ url: '/monitor/v1/enterprise/financial/statistics/asset', method: 'post', headers: JSON_HEADERS, data })
}

// 获取负债统计
export function getLiabilityStatistics(data) {
  return request({ url: '/monitor/v1/enterprise/financial/statistics/liability', method: 'post', headers: JSON_HEADERS, data })
}

// 获取现金流统计
export function getCashFlowStatistics(data) {
  return request({ url: '/monitor/v1/enterprise/financial/statistics/cashflow', method: 'post', headers: JSON_HEADERS, data })
}

// 执行预算
export function executeBudget(data) {
  return request({ url: '/monitor/v1/enterprise/financial/execute-budget', method: 'post', headers: JSON_HEADERS, data })
}

// 导出财务报表
export function exportFinancialStatement(data) {
  return request({ url: '/monitor/v1/enterprise/financial/export-statement', method: 'post', headers: JSON_HEADERS, data, responseType: 'blob' })
}

// 获取报表版本历史
export function getStatementHistory(statementId) {
  return request({
    url: `/monitor/v1/enterprise/financial/statement/history/${statementId}`,
    method: 'get',
    headers: JSON_HEADERS
  })
}

// 报表对比分析
export function compareFinancialStatement(data) {
  return request({ url: '/monitor/v1/enterprise/financial/statement/compare', method: 'post', headers: JSON_HEADERS, data })
}

// 批量更新报表状态
export function batchUpdateStatementStatus(data) {
  return request({ url: '/monitor/v1/enterprise/financial/statement/batch-update-status', method: 'post', headers: JSON_HEADERS, data })
}

// 批量删除报表
export function batchDeleteStatement(data) {
  return request({ url: '/monitor/v1/enterprise/financial/statement/batch-delete', method: 'post', headers: JSON_HEADERS, data })
}

// 批量审核报表
export function batchAuditStatement(data) {
  return request({ url: '/monitor/v1/enterprise/financial/statement/batch-audit', method: 'post', headers: JSON_HEADERS, data })
}


// ==================== 新增接口 ====================

// 批量处理预警
export function batchHandleWarning(data) {
  return request({
    url: '/monitor/v1/enterprise/financial/warning/batch-handle',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 批量忽略预警
export function batchIgnoreWarning(data) {
  return request({
    url: '/monitor/v1/enterprise/financial/warning/batch-ignore',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 更新财务报告
export function updateFinancialReport(data) {
  return request({
    url: '/monitor/v1/enterprise/financial/report/update',
    method: 'put',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 删除财务报告
export function deleteFinancialReport(reportId) {
  return request({
    url: `/monitor/v1/enterprise/financial/report/${reportId}`,
    method: 'delete',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

// 导出成本数据
export function exportCostData(data) {
  return request({
    url: '/monitor/v1/enterprise/financial/cost/export',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data,
    responseType: 'blob'
  })
}

// 获取预警统计
export function getWarningStatistics(data) {
  return request({
    url: '/monitor/v1/enterprise/financial/warning/statistics',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 获取报告统计
export function getReportStatistics(data) {
  return request({
    url: '/monitor/v1/enterprise/financial/report/statistics',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}
