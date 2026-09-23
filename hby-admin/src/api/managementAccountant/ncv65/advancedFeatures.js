/**
 * NCV65全面预算系统 - 高级功能API
 * 
 * @description 高级功能API接口，包含滚动预算、公式追踪、催报管理、穿透查询、多币种管理等功�?
 * @version 1.0.0
 * @author AI Assistant
 * @date 2025-01-08
 * @module advancedFeatures
 */

import request from '@/utils/request'

// ==================== 滚动预算API ====================

/**
 * 创建滚动预算计划
 * @param {Object} data 滚动预算计划数据
 * @returns {Promise} 请求结果
 */
export function createRollingBudgetPlan(data) {
  return request({
    url: '/glkj/accountant/budget/rolling/create',
    method: 'post',
    data
  })
}

/**
 * 查询滚动预算计划详情
 * @param {String} planId 计划ID
 * @returns {Promise} 请求结果
 */
export function getRollingBudgetPlan(planId) {
  return request({
    url: `/glkj/accountant/budget/rolling/detail/${planId}`,
    method: 'get'
  })
}

/**
 * 更新滚动预算计划
 * @param {String} planId 计划ID
 * @param {Object} data 更新数据
 * @returns {Promise} 请求结果
 */
export function updateRollingBudgetPlan(planId, data) {
  return request({
    url: `/glkj/accountant/budget/rolling/update/${planId}`,
    method: 'put',
    data
  })
}

/**
 * 删除滚动预算计划
 * @param {String} planId 计划ID
 * @returns {Promise} 请求结果
 */
export function deleteRollingBudgetPlan(planId) {
  return request({
    url: `/glkj/accountant/budget/rolling/adjust/${planId}`,
    method: 'post',
    data: { action: 'delete' }
  })
}

/**
 * 分页查询滚动预算计划列表
 * @param {Number} current 当前页
 * @param {Number} size 页大小
 * @param {Object} params 查询参数
 * @returns {Promise} 请求结果
 */
export function getRollingBudgetPlanPage(current, size, params) {
  return request({
    url: '/glkj/accountant/budget/rolling/page',
    method: 'post',
    params: { current, size },
    data: params
  })
}

/**
 * 执行滚动预算
 * @param {String} planId 计划ID
 * @returns {Promise} 请求结果
 */
export function executeRollingBudget(planId) {
  return request({
    url: `/glkj/accountant/budget/rolling/execute/${planId}`,
    method: 'post'
  })
}

/**
 * 获取滚动预算执行记录
 * @param {String} planId 计划ID
 * @returns {Promise} 请求结果
 */
export function getRollingBudgetExecutionRecords(planId) {
  return request({
    url: `/glkj/accountant/budget/rolling/plan/${planId}/executions`,
    method: 'get'
  })
}

// ==================== 公式追踪API ====================

/**
 * 创建公式追踪任务
 * @param {Object} data 追踪任务数据
 * @returns {Promise} 请求结果
 */
export function createFormulaTraceTask(data) {
  return request({
    url: '/glkj/accountant/budget/formula/tracking/create',
    method: 'post',
    data
  })
}

/**
 * 查询公式追踪任务详情
 * @param {String} taskId 任务ID
 * @returns {Promise} 请求结果
 */
export function getFormulaTraceTask(taskId) {
  return request({
    url: `/glkj/accountant/budget/formula/tracking/detail/${taskId}`,
    method: 'get'
  })
}

/**
 * 分页查询公式追踪任务列表
 * @param {Number} current 当前页
 * @param {Number} size 页大小
 * @param {Object} params 查询参数
 * @returns {Promise} 请求结果
 */
export function getFormulaTraceTaskPage(current, size, params) {
  return request({
    url: '/glkj/accountant/budget/formula/tracking/page',
    method: 'post',
    params: { current, size },
    data: params
  })
}

/**
 * 获取公式依赖关系
 * @param {String} formulaId 公式ID
 * @returns {Promise} 请求结果
 */
export function getFormulaDependencies(formulaId) {
  return request({
    url: `/glkj/accountant/budget/formula/tracking/dependencies/${formulaId}`,
    method: 'get'
  })
}

/**
 * 获取公式影响分析
 * @param {String} formulaId 公式ID
 * @returns {Promise} 请求结果
 */
export function getFormulaImpactAnalysis(formulaId) {
  return request({
    url: `/glkj/accountant/budget/formula/tracking/impact/analysis`,
    method: 'post',
    data: { formulaId }
  })
}

/**
 * 执行公式追踪
 * @param {String} taskId 任务ID
 * @returns {Promise} 请求结果
 */
export function executeFormulaTrace(taskId) {
  return request({
    url: `/glkj/accountant/budget/formula/tracking/execute/${taskId}`,
    method: 'post'
  })
}

// ==================== 催报管理API ====================

/**
 * 创建催报策略
 * @param {Object} data 催报策略数据
 * @returns {Promise} 请求结果
 */
export function createReminderStrategy(data) {
  return request({
    url: '/glkj/accountant/budget/reminder/create',
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json' }
  })
}

/**
 * 查询催报策略详情
 * @param {String} strategyId 策略ID
 * @returns {Promise} 请求结果
 */
export function getReminderStrategy(strategyId) {
  return request({
    url: `/glkj/accountant/budget/reminder/detail/${strategyId}`,
    method: 'get'
  })
}

/**
 * 更新催报策略
 * @param {String} strategyId 策略ID
 * @param {Object} data 更新数据
 * @returns {Promise} 请求结果
 */
export function updateReminderStrategy(strategyId, data) {
  return request({
    url: `/glkj/accountant/budget/reminder/update/${strategyId}`,
    method: 'put',
    data
  })
}

/**
 * 删除催报策略
 * @param {String} strategyId 策略ID
 * @returns {Promise} 请求结果
 */
export function deleteReminderStrategy(strategyId) {
  return request({
    url: `/glkj/accountant/budget/reminder/delete/${strategyId}`,
    method: 'delete'
  })
}

/**
 * 分页查询催报策略列表
 * @param {Number} current 当前页
 * @param {Number} size 页大小
 * @param {Object} params 查询参数
 * @returns {Promise} 请求结果
 */
export function getReminderStrategyPage(current, size, params) {
  return request({
    url: '/glkj/accountant/budget/reminder/page',
    method: 'post',
    params: { current, size },
    data: params
  })
}

/**
 * 执行催报
 * @param {String} strategyId 策略ID
 * @returns {Promise} 请求结果
 */
export function executeReminder(strategyId) {
  return request({
    url: `/glkj/accountant/budget/reminder/execute/${strategyId}`,
    method: 'post'
  })
}

/**
 * 分页查询催报执行记录
 * @param {Number} current 当前页
 * @param {Number} size 页大小
 * @param {Object} params 查询参数
 * @returns {Promise} 请求结果
 */
export function getReminderExecutionPage(current, size, params) {
  return request({
    url: '/glkj/accountant/budget/reminder/records',
    method: 'post',
    params: { current, size },
    data: params
  })
}

// ==================== 穿透查询API ====================

/**
 * 创建穿透查�?
 * @param {Object} data 查询数据
 * @returns {Promise} 请求结果
 */
export function createDrillThroughQuery(data) {
  return request({
    url: '/glkj/accountant/budget/drilldown/create',
    method: 'post',
    data
  })
}

/**
 * 执行穿透查询
 * @param {String} queryId 查询ID
 * @param {Object} params 查询参数
 * @returns {Promise} 请求结果
 */
export function executeDrillThroughQuery(queryId, params) {
  return request({
    url: `/glkj/accountant/budget/drilldown/run/${queryId}`,
    method: 'post',
    data: params
  })
}

/**
 * 获取穿透查询结果
 * @param {String} queryId 查询ID
 * @returns {Promise} 请求结果
 */
export function getDrillThroughQueryResult(queryId) {
  return request({
    url: `/glkj/accountant/budget/drilldown/result/${queryId}`,
    method: 'get'
  })
}

/**
 * 分页查询穿透查询列表
 * @param {Number} current 当前页
 * @param {Number} size 页大小
 * @param {Object} params 查询参数
 * @returns {Promise} 请求结果
 */
export function getDrillThroughQueryPage(current, size, params) {
  return request({
    url: '/glkj/accountant/budget/drilldown/list',
    method: 'post',
    params: { current, size },
    data: params
  })
}

// ==================== 多币种管理API ====================

/**
 * 创建币种配置
 * @param {Object} data 币种配置数据
 * @returns {Promise} 请求结果
 */
export function createCurrencyConfig(data) {
  return request({
    url: '/glkj/accountant/budget/multi/currency/create',
    method: 'post',
    data
  })
}

/**
 * 查询币种配置详情
 * @param {String} configId 配置ID
 * @returns {Promise} 请求结果
 */
export function getCurrencyConfig(configId) {
  return request({
    url: `/glkj/accountant/budget/multi/currency/detail/${configId}`,
    method: 'get'
  })
}

/**
 * 更新币种配置
 * @param {String} configId 配置ID
 * @param {Object} data 更新数据
 * @returns {Promise} 请求结果
 */
export function updateCurrencyConfig(configId, data) {
  return request({
    url: `/glkj/accountant/budget/multi/currency/update/${configId}`,
    method: 'put',
    data
  })
}

/**
 * 分页查询币种配置列表
 * @param {Number} current 当前页
 * @param {Number} size 页大小
 * @param {Object} params 查询参数
 * @returns {Promise} 请求结果
 */
export function getCurrencyConfigPage(current, size, params) {
  return request({
    url: '/glkj/accountant/budget/multi/currency/page',
    method: 'post',
    params: { current, size },
    data: params
  })
}

/**
 * 创建汇率
 * @param {Object} data 汇率数据
 * @returns {Promise} 请求结果
 */
export function createExchangeRate(data) {
  return request({
    url: '/glkj/accountant/budget/multi/currency/exchange-rate/update',
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json' }
  })
}

/**
 * 分页查询汇率列表
 * @param {Number} current 当前页
 * @param {Number} size 页大小
 * @param {Object} params 查询参数
 * @returns {Promise} 请求结果
 */
export function getExchangeRatePage(current, size, params) {
  return request({
    url: '/glkj/accountant/budget/multi/currency/exchange-rate/history',
    method: 'post',
    params: { current, size },
    data: params
  })
}

/**
 * 获取最新汇率
 * @param {String} fromCurrency 源币种
 * @param {String} toCurrency 目标币种
 * @returns {Promise} 请求结果
 */
export function getLatestExchangeRate(fromCurrency, toCurrency) {
  return request({
    url: '/glkj/accountant/budget/multi/currency/rate/history',
    method: 'post',
    data: { fromCurrency, toCurrency }
  })
}

/**
 * 币种转换
 * @param {Object} data 转换数据
 * @returns {Promise} 请求结果
 */
export function convertCurrency(data) {
  return request({
    url: '/glkj/accountant/budget/multi/currency/convert',
    method: 'post',
    data
  })
}

// ==================== 高级功能首页API ====================

/**
 * 获取功能统计数据
 * @returns {Promise} 请求结果
 */
export function getFeatureStats() {
  return request({
    url: '/glkj/accountant/advanced/stats',
    method: 'get'
  })
}

/**
 * 获取各模块统计数据（任务数+最后更新时间）
 * @returns {Promise} 请求结果
 */
export function getModuleStats() {
  return request({
    url: '/glkj/accountant/advanced/module-stats',
    method: 'get'
  })
}

/**
 * 获取最近活�?
 * @returns {Promise} 请求结果
 */
export function getRecentActivities() {
  return request({
    url: '/glkj/accountant/advanced/activities',
    method: 'get'
  })
}

/**
 * 获取系统通知
 * @returns {Promise} 请求结果
 */
export function getNotifications() {
  return request({
    url: '/glkj/accountant/advanced/notifications',
    method: 'get'
  })
}

/**
 * 标记通知为已�?
 * @param {String} notificationId 通知ID
 * @returns {Promise} 请求结果
 */
export function markNotificationAsRead(notificationId) {
  return request({
    url: `/glkj/accountant/advanced/notifications/${notificationId}/read`,
    method: 'put'
  })
}

// 高级功能API简化导出
export const advancedFeaturesApi = {
  // 高级功能首页方法
  getFeatureStats,
  getModuleStats,
  getRecentActivities,
  getNotifications,
  markNotificationAsRead,

  // ===== 滚动预算 =====
  getRollingBudgetPlanList: (params) => request({ url: '/glkj/accountant/budget/rolling/plan/list', method: 'post', data: params || {}, headers: { 'Content-Type': 'application/json' } }),
  getRollingBudgetStats: () => request({ url: '/glkj/accountant/budget/rolling/stats', method: 'get' }),
  getRollingHistory: (planId) => request({ url: `/glkj/accountant/budget/rolling/plan/${planId}/executions`, method: 'get' }),
  executeRollingBudget: (rollingId) => request({ url: `/glkj/accountant/budget/rolling/execute/${rollingId}`, method: 'post', data: {} }),

  // ===== 公式追踪 =====
  getFormulaTraceTaskList: (params) => request({ url: '/glkj/accountant/budget/formula/tracking/page', method: 'post', data: params || {}, headers: { 'Content-Type': 'application/json' } }),
  getFormulaTraceStats: (params) => request({ url: '/glkj/accountant/budget/formula/tracking/dependencies/stats', method: 'post', data: params || {}, headers: { 'Content-Type': 'application/json' } }),
  getAvailableFormulas: (params) => request({ url: '/glkj/accountant/budget/formula/tracking/chain', method: 'post', data: params || {}, headers: { 'Content-Type': 'application/json' } }),
  getTraceTaskFormulas: (taskId) => request({ url: `/glkj/accountant/budget/formula/tracking/formulas/${taskId}`, method: 'get' }),
  getTraceTaskLogs: (taskId) => request({ url: `/glkj/accountant/budget/formula/tracking/logs/${taskId}`, method: 'get' }),
  createFormulaTrace: (data) => request({ url: '/glkj/accountant/budget/formula/tracking/create', method: 'post', data, headers: { 'Content-Type': 'application/json' } }),
  updateFormulaTrace: (data) => request({ url: '/glkj/accountant/budget/formula/tracking/update', method: 'post', data, headers: { 'Content-Type': 'application/json' } }),
  executeFormulaTrace: (taskId) => request({ url: `/glkj/accountant/budget/formula/tracking/execute/${taskId}`, method: 'post', data: {} }),
  stopFormulaTrace: (taskId) => request({ url: `/glkj/accountant/budget/formula/tracking/stop/${taskId}`, method: 'post', data: {} }),
  copyFormulaTrace: (taskId) => request({ url: `/glkj/accountant/budget/formula/tracking/copy/${taskId}`, method: 'post', data: {} }),
  deleteFormulaTrace: (taskId) => request({ url: `/glkj/accountant/budget/formula/tracking/delete/${taskId}`, method: 'post', data: {} }),
  getFormulaDependencies: (taskId) => request({ url: `/glkj/accountant/budget/formula/tracking/dependencies/${taskId}`, method: 'get' }),
  getFormulaImpactAnalysis: (taskId) => request({ url: '/glkj/accountant/budget/formula/tracking/impact/analysis', method: 'post', data: { taskId }, headers: { 'Content-Type': 'application/json' } }),

  // ===== 催报管理 =====
  getReminderStrategyList: (params) => request({ url: '/glkj/accountant/budget/reminder/list', method: 'post', data: params || {}, headers: { 'Content-Type': 'application/json' } }),
  getReminderStats: () => request({ url: '/glkj/accountant/budget/reminder/stats', method: 'get' }),
  getReminderTargets: (reminderId) => request({ url: `/glkj/accountant/budget/reminder/targets/${reminderId}`, method: 'get' }),
  getReminderRecords: (params) => request({ url: '/glkj/accountant/budget/reminder/records', method: 'post', data: params || {}, headers: { 'Content-Type': 'application/json' } }),
  getReminderReport: () => request({ url: '/glkj/accountant/budget/reminder/report', method: 'get' }),
  getReminderSettings: () => request({ url: '/glkj/accountant/budget/reminder/settings', method: 'get' }),
  saveReminderSettings: (data) => request({ url: '/glkj/accountant/budget/reminder/settings', method: 'post', data, headers: { 'Content-Type': 'application/json' } }),

  // ===== 穿透查询 =====
  getDrillThroughQueryList: (params) => request({ url: '/glkj/accountant/budget/drilldown/list', method: 'post', data: params || {}, headers: { 'Content-Type': 'application/json' } }),
  getDrillThroughQueryStats: (params) => request({ url: '/glkj/accountant/budget/drilldown/stats', method: 'post', data: params || {}, headers: { 'Content-Type': 'application/json' } }),
  getAvailableDimensions: (params) => request({ url: '/glkj/accountant/budget/drilldown/dimensions', method: 'post', data: params || {}, headers: { 'Content-Type': 'application/json' } }),
  getDrillThroughQueryResult: (queryId) => request({ url: `/glkj/accountant/budget/drilldown/result/${queryId}`, method: 'get' }),

  // ===== 多币种管理 =====
  getCurrencyList: (params) => request({ url: '/glkj/accountant/budget/multi/currency/list', method: 'post', data: params || {}, headers: { 'Content-Type': 'application/json' } }),
  getCurrencyStats: (params) => request({ url: '/glkj/accountant/budget/multi/currency/stats', method: 'post', data: params || {}, headers: { 'Content-Type': 'application/json' } }),
  getCurrencyRateHistory: (params) => request({ url: '/glkj/accountant/budget/multi/currency/rate/history', method: 'post', data: params || {}, headers: { 'Content-Type': 'application/json' } }),
  updateCurrencyRate: (currencyCode) => request({ url: '/glkj/accountant/budget/multi/currency/exchange-rate/update', method: 'post', data: { currencyCode }, headers: { 'Content-Type': 'application/json' } }),
  getCurrencyDetail: (currencyId) => request({ url: `/glkj/accountant/budget/multi/currency/detail/${currencyId}`, method: 'get' }),
  getExchangeRateHistoryPage: (params) => request({ url: '/glkj/accountant/budget/multi/currency/exchange-rate/history', method: 'post', data: params || {}, headers: { 'Content-Type': 'application/json' } }),

  // ===== 高级报表 =====
  getAdvancedReportsList: (params) => request({ url: '/glkj/accountant/advanced/reports/list', method: 'post', data: params || {}, headers: { 'Content-Type': 'application/json' } }),
  getAdvancedReportsStats: () => request({ url: '/glkj/accountant/advanced/reports/stats', method: 'get' }),
  getReportSubscribers: (reportId) => request({ url: `/glkj/accountant/advanced/reports/${reportId}/subscribers`, method: 'get' }),
  getReportGenerationHistory: (reportId) => request({ url: `/glkj/accountant/advanced/reports/${reportId}/history`, method: 'get' }),

  // ===== 自动化工作流 =====
  getAutomationWorkflowList: (params) => request({ url: '/glkj/accountant/advanced/workflow/list', method: 'post', data: params || {}, headers: { 'Content-Type': 'application/json' } }),
  getAutomationWorkflowStats: () => request({ url: '/glkj/accountant/advanced/workflow/stats', method: 'get' }),
  getAutomationWorkflowDetail: (id) => request({ url: `/glkj/accountant/advanced/workflow/detail/${id}`, method: 'get' }),
  createAutomationWorkflow: (data) => request({ url: '/glkj/accountant/advanced/workflow/create', method: 'post', data, headers: { 'Content-Type': 'application/json' } }),
  updateAutomationWorkflow: (data) => request({ url: '/glkj/accountant/advanced/workflow/update', method: 'post', data, headers: { 'Content-Type': 'application/json' } }),
  runAutomationWorkflow: (id) => request({ url: `/glkj/accountant/advanced/workflow/${id}/run`, method: 'post', data: {} }),
  stopAutomationWorkflow: (id) => request({ url: `/glkj/accountant/advanced/workflow/${id}/stop`, method: 'post', data: {} }),
  getWorkflowExecutionMonitor: () => request({ url: '/glkj/accountant/advanced/workflow/execution/monitor', method: 'get' }),
  getWorkflowSettings: () => request({ url: '/glkj/accountant/advanced/workflow/settings', method: 'get' }),
  saveWorkflowSettings: (data) => request({ url: '/glkj/accountant/advanced/workflow/settings', method: 'post', data, headers: { 'Content-Type': 'application/json' } }),
  getWorkflowTemplates: () => request({ url: '/glkj/accountant/advanced/workflow/templates', method: 'get' }),
  getWorkflowLogs: (id) => request({ url: `/glkj/accountant/advanced/workflow/${id}/logs`, method: 'get' }),

  // ===== 批量计算 =====
  getBatchCalculationTaskList: (params) => request({ url: '/glkj/accountant/advanced/batch/calculation/list', method: 'post', data: params || {}, headers: { 'Content-Type': 'application/json' } }),
  getBatchCalculationStats: () => request({ url: '/glkj/accountant/advanced/batch/calculation/stats', method: 'get' }),
  getBatchCalculationResults: (taskId) => request({ url: `/glkj/accountant/advanced/batch/calculation/${taskId}/results`, method: 'get' }),
  getBatchCalculationLogs: (taskId) => request({ url: `/glkj/accountant/advanced/batch/calculation/${taskId}/logs`, method: 'get' }),

  // ===== 预算优化 =====
  getBudgetOptimizationList: (params) => request({ url: '/glkj/accountant/advanced/optimization/list', method: 'post', data: params || {}, headers: { 'Content-Type': 'application/json' } }),
  getBudgetOptimizationStats: () => request({ url: '/glkj/accountant/advanced/optimization/stats', method: 'get' }),
  getBudgetOptimizationResults: (taskId) => request({ url: `/glkj/accountant/advanced/optimization/${taskId}/results`, method: 'get' }),
  getBudgetOptimizationLogs: (taskId) => request({ url: `/glkj/accountant/advanced/optimization/${taskId}/logs`, method: 'get' }),

  // ===== 预算模拟 =====
  getBudgetSimulationList: (params) => request({ url: '/glkj/accountant/advanced/simulation/list', method: 'post', data: params || {}, headers: { 'Content-Type': 'application/json' } }),
  getBudgetSimulationStats: () => request({ url: '/glkj/accountant/advanced/simulation/stats', method: 'get' }),
  getBudgetSimulationScenarios: (simulationId) => request({ url: `/glkj/accountant/advanced/simulation/${simulationId}/scenarios`, method: 'get' }),
  getBudgetSimulationLogs: (simulationId) => request({ url: `/glkj/accountant/advanced/simulation/${simulationId}/logs`, method: 'get' }),

  // ===== 协同预算 =====
  getCollaborativeBudgetingList: (params) => request({ url: '/glkj/accountant/advanced/collaboration/list', method: 'post', data: params || {}, headers: { 'Content-Type': 'application/json' } }),
  getCollaborativeBudgetingStats: () => request({ url: '/glkj/accountant/advanced/collaboration/stats', method: 'get' }),
  getCollaborationParticipants: (projectId) => request({ url: `/glkj/accountant/advanced/collaboration/${projectId}/participants`, method: 'get' }),
  getCollaborationActivities: (projectId) => request({ url: `/glkj/accountant/advanced/collaboration/${projectId}/activities`, method: 'get' }),

  // ===== 数据挖掘 =====
  getDataMiningTaskList: (params) => request({ url: '/glkj/accountant/advanced/data-mining/list', method: 'post', data: params || {}, headers: { 'Content-Type': 'application/json' } }),
  getDataMiningStats: () => request({ url: '/glkj/accountant/advanced/data-mining/stats', method: 'get' }),
  getDataMiningResults: (taskId) => request({ url: `/glkj/accountant/advanced/data-mining/${taskId}/results`, method: 'get' }),
  getDataMiningLogs: (taskId) => request({ url: `/glkj/accountant/advanced/data-mining/${taskId}/logs`, method: 'get' }),

  // ===== 智能推荐 =====
  getIntelligentRecommendationList: (params) => request({ url: '/glkj/accountant/budget/intelligent/recommendation/list', method: 'post', data: params || {}, headers: { 'Content-Type': 'application/json' } }),
  getIntelligentRecommendationStats: () => request({ url: '/glkj/accountant/budget/intelligent/recommendation/stats', method: 'post', data: {}, headers: { 'Content-Type': 'application/json' } }),
  generateIntelligentRecommendation: (params) => request({ url: '/glkj/accountant/budget/intelligent/recommendation/generate', method: 'post', data: params || {}, headers: { 'Content-Type': 'application/json' } }),
  getRecommendationFeedback: (recommendationId) => request({ url: `/glkj/accountant/budget/intelligent/recommendation/feedback/${recommendationId}`, method: 'get' }),
  trainModel: (params) => request({ url: '/glkj/accountant/budget/intelligent/recommendation/train/model', method: 'post', data: params || {}, headers: { 'Content-Type': 'application/json' } }),
  getRecommendationAnalysis: (params) => request({ url: '/glkj/accountant/budget/intelligent/recommendation/analysis', method: 'post', data: params || {}, headers: { 'Content-Type': 'application/json' } }),

  // ===== 风险评估 =====
  getRiskAssessmentList: (params) => request({ url: '/glkj/accountant/advanced/risk-assessment/list', method: 'get', params }),
  getRiskAssessmentStats: () => request({ url: '/glkj/accountant/advanced/risk-assessment/stats', method: 'get' }),
  getRiskCategoryStats: () => request({ url: '/glkj/accountant/advanced/risk-assessment/category-stats', method: 'get' }),
  createRiskAssessment: (data) => request({ url: '/glkj/accountant/advanced/risk-assessment/create', method: 'post', data, headers: { 'Content-Type': 'application/json' } }),
  updateRiskAssessment: (data) => request({ url: '/glkj/accountant/advanced/risk-assessment/update', method: 'post', data, headers: { 'Content-Type': 'application/json' } }),
  getRiskAssessmentDetail: (id) => request({ url: `/glkj/accountant/advanced/risk-assessment/detail/${id}`, method: 'get' }),
  exportRiskAssessment: (id) => request({ url: `/glkj/accountant/advanced/risk-assessment/${id}/export`, method: 'get' }),
  copyRiskAssessment: (id) => request({ url: `/glkj/accountant/advanced/risk-assessment/${id}/copy`, method: 'post', data: {} }),

  // ===== 版本比较 =====
  getVersionComparisonList: (params) => request({ url: '/glkj/accountant/advanced/version/comparison/list', method: 'post', data: params || {}, headers: { 'Content-Type': 'application/json' } }),
  getVersionComparisonStats: () => request({ url: '/glkj/accountant/advanced/version/comparison/stats', method: 'get' }),
  getAvailableVersions: () => request({ url: '/glkj/accountant/advanced/version/list', method: 'get' }),
  getVersionComparisonDifferences: (comparisonId) => request({ url: `/glkj/accountant/advanced/version/comparison/${comparisonId}/differences`, method: 'get' }),
  getVersionComparisonLogs: (comparisonId) => request({ url: `/glkj/accountant/advanced/version/comparison/${comparisonId}/logs`, method: 'get' }),
  createVersionComparison: (data) => request({ url: '/glkj/accountant/advanced/version/comparison', method: 'post', data, headers: { 'Content-Type': 'application/json' } }),
  updateVersionComparison: (id, data) => request({ url: `/glkj/accountant/advanced/version/comparison/${id}`, method: 'put', data, headers: { 'Content-Type': 'application/json' } }),
  deleteVersionComparison: (id) => request({ url: `/glkj/accountant/advanced/version/comparison/${id}`, method: 'delete' }),
  copyVersionComparison: (id) => request({ url: `/glkj/accountant/advanced/version/comparison/${id}/copy`, method: 'post', data: {} }),
  exportVersionComparison: (id) => request({ url: `/glkj/accountant/advanced/version/comparison/${id}/export`, method: 'get' }),
  recompareVersions: (id) => request({ url: `/glkj/accountant/advanced/version/comparison/${id}/recompare`, method: 'post', data: {} }),
  mergeVersions: (data) => request({ url: '/glkj/accountant/advanced/version/merge', method: 'post', data, headers: { 'Content-Type': 'application/json' } }),
  rollbackVersion: (data) => request({ url: '/glkj/accountant/advanced/version/rollback', method: 'post', data, headers: { 'Content-Type': 'application/json' } }),

  // ===== 滚动预算 CRUD =====
  createRollingBudgetPlan: (data) => request({ url: '/glkj/accountant/budget/rolling/create', method: 'post', data, headers: { 'Content-Type': 'application/json' } }),
  updateRollingBudgetPlan: (data) => request({ url: `/glkj/accountant/budget/rolling/update/${data.rollingId || data.planId || data.id}`, method: 'put', data, headers: { 'Content-Type': 'application/json' } }),
  deleteRollingBudget: (id) => request({ url: `/glkj/accountant/budget/rolling/delete/${id}`, method: 'delete' }),
  copyRollingBudget: (id) => request({ url: `/glkj/accountant/budget/rolling/${id}/copy`, method: 'post', data: {} }),
  pauseRollingBudget: (id) => request({ url: `/glkj/accountant/budget/rolling/${id}/pause`, method: 'post', data: {} }),
  resumeRollingBudget: (id) => request({ url: `/glkj/accountant/budget/rolling/${id}/resume`, method: 'post', data: {} }),

  // ===== 公式追踪 CRUD =====
  createFormulaTrace: (data) => request({ url: '/glkj/accountant/budget/formula/tracking/create', method: 'post', data, headers: { 'Content-Type': 'application/json' } }),
  updateFormulaTrace: (data) => request({ url: `/glkj/accountant/budget/formula/tracking/update/${data.traceId || data.taskId || data.id}`, method: 'put', data, headers: { 'Content-Type': 'application/json' } }),
  deleteFormulaTrace: (id) => request({ url: `/glkj/accountant/budget/formula/tracking/delete/${id}`, method: 'delete' }),
  copyFormulaTrace: (id) => request({ url: `/glkj/accountant/budget/formula/tracking/copy/${id}`, method: 'post', data: {}, headers: { 'Content-Type': 'application/json' } }),
  executeFormulaTrace: (taskId) => request({ url: `/glkj/accountant/budget/formula/tracking/execute/${taskId}`, method: 'post', data: {}, headers: { 'Content-Type': 'application/json' } }),
  stopFormulaTrace: (taskId) => request({ url: `/glkj/accountant/budget/formula/tracking/stop/${taskId}`, method: 'post', data: {}, headers: { 'Content-Type': 'application/json' } }),
  getTraceTaskLogs: (taskId) => request({ url: `/glkj/accountant/budget/formula/tracking/logs/${taskId}`, method: 'get' }),

  // ===== 催报管理 CRUD =====
  createReminderStrategy: (data) => request({ url: '/glkj/accountant/budget/reminder/create', method: 'post', data, headers: { 'Content-Type': 'application/json' } }),
  updateReminderStrategy: (data) => request({ url: `/glkj/accountant/budget/reminder/update/${data.strategyId || data.reminderId}`, method: 'put', data, headers: { 'Content-Type': 'application/json' } }),
  deleteReminderStrategy: (id) => request({ url: `/glkj/accountant/budget/reminder/delete/${id}`, method: 'delete' }),
  copyReminderStrategy: (id) => request({ url: `/glkj/accountant/budget/reminder/copy/${id}`, method: 'post', data: {}, headers: { 'Content-Type': 'application/json' } }),
  enableReminderStrategy: (id) => request({ url: `/glkj/accountant/budget/reminder/${id}/enable`, method: 'put', data: {} }),
  disableReminderStrategy: (id) => request({ url: `/glkj/accountant/budget/reminder/${id}/disable`, method: 'put', data: {} }),
  sendReminder: (params) => request({ url: `/glkj/accountant/budget/reminder/send`, method: 'post', data: params, headers: { 'Content-Type': 'application/json' } }),
  testReminderStrategy: (id) => request({ url: `/glkj/accountant/budget/reminder/${id}/test`, method: 'post', data: {} }),

  // ===== 穿透查询 CRUD =====
  createDrillThroughQuery: (data) => request({ url: '/glkj/accountant/budget/drilldown/create', method: 'post', data, headers: { 'Content-Type': 'application/json' } }),
  updateDrillThroughQuery: (data) => request({ url: `/glkj/accountant/budget/drilldown/update/${data.queryId || data.id}`, method: 'put', data, headers: { 'Content-Type': 'application/json' } }),
  deleteDrillThroughQuery: (id) => request({ url: `/glkj/accountant/budget/drilldown/delete/${id}`, method: 'delete' }),
  copyDrillThroughQuery: (id) => request({ url: `/glkj/accountant/budget/drilldown/copy/${id}`, method: 'post', data: {} }),
  executeDrillThroughQuery: (queryId) => request({ url: `/glkj/accountant/budget/drilldown/run/${queryId}`, method: 'post', data: {} }),
  stopDrillThroughQuery: (queryId) => request({ url: `/glkj/accountant/budget/drilldown/stop/${queryId}`, method: 'post', data: {} }),
  getDrillThroughQueryLogs: (queryId) => request({ url: `/glkj/accountant/budget/drilldown/logs/${queryId}`, method: 'get' }),
  optimizeDrillThroughQuery: (queryId) => request({ url: `/glkj/accountant/budget/drilldown/optimize/${queryId}`, method: 'post', data: {} }),
  exportDrillThroughQuery: (id) => request({ url: `/glkj/accountant/budget/drilldown/export/${id}`, method: 'get' }),

  // ===== 多币种 CRUD =====
  createCurrency: (data) => request({ url: '/glkj/accountant/budget/multi/currency/create', method: 'post', data, headers: { 'Content-Type': 'application/json' } }),
  updateCurrency: (data) => request({ url: `/glkj/accountant/budget/multi/currency/update/${data.currencyId || data.id}`, method: 'put', data, headers: { 'Content-Type': 'application/json' } }),
  deleteCurrency: (id) => request({ url: `/glkj/accountant/budget/multi/currency/delete/${id}`, method: 'delete', headers: { 'Content-Type': 'application/json' } }),
  enableCurrency: (id) => request({ url: `/glkj/accountant/budget/multi/currency/${id}/enable`, method: 'put', data: {}, headers: { 'Content-Type': 'application/json' } }),
  disableCurrency: (id) => request({ url: `/glkj/accountant/budget/multi/currency/${id}/disable`, method: 'put', data: {}, headers: { 'Content-Type': 'application/json' } }),
  setBaseCurrency: (currencyCode) => request({ url: '/glkj/accountant/budget/multi/currency/base', method: 'put', data: { currencyCode }, headers: { 'Content-Type': 'application/json' } }),

  // ===== 高级报表 CRUD =====
  createAdvancedReport: (data) => request({ url: '/glkj/accountant/advanced/reports/create', method: 'post', data, headers: { 'Content-Type': 'application/json' } }),
  updateAdvancedReport: (data) => request({ url: `/glkj/accountant/advanced/reports/update/${data.reportId || data.id}`, method: 'put', data, headers: { 'Content-Type': 'application/json' } }),
  deleteAdvancedReport: (id) => request({ url: `/glkj/accountant/advanced/reports/delete/${id}`, method: 'delete' }),
  copyAdvancedReport: (id) => request({ url: `/glkj/accountant/advanced/reports/${id}/copy`, method: 'post', data: {} }),
  generateAdvancedReport: (id) => request({ url: `/glkj/accountant/advanced/reports/${id}/generate`, method: 'post', data: {} }),
  downloadGeneratedReport: (id) => request({ url: `/glkj/accountant/advanced/reports/${id}/download`, method: 'get' }),
  exportReportConfig: (id) => request({ url: `/glkj/accountant/advanced/reports/${id}/export-config`, method: 'get' }),
  getReportConfig: (reportId) => request({ url: `/glkj/accountant/advanced/reports/${reportId}/config`, method: 'get' }),
  updateReportSchedule: (reportId, data) => request({ url: `/glkj/accountant/advanced/reports/${reportId}/schedule`, method: 'put', data, headers: { 'Content-Type': 'application/json' } }),
  addReportSubscriber: (reportId, data) => request({ url: `/glkj/accountant/advanced/reports/${reportId}/subscribers`, method: 'post', data, headers: { 'Content-Type': 'application/json' } }),
  removeReportSubscriber: (subscriberId) => request({ url: `/glkj/accountant/advanced/reports/subscribers/${subscriberId}`, method: 'delete' }),

  // ===== 自动化工作流 CRUD =====
  deleteAutomationWorkflow: (id) => request({ url: `/glkj/accountant/advanced/workflow/delete/${id}`, method: 'delete' }),
  copyAutomationWorkflow: (id) => request({ url: `/glkj/accountant/advanced/workflow/${id}/copy`, method: 'post', data: {} }),
  exportAutomationWorkflow: (id) => request({ url: `/glkj/accountant/advanced/workflow/${id}/export`, method: 'get' }),

  // ===== 批量计算 CRUD =====
  createBatchCalculation: (data) => request({ url: '/glkj/accountant/advanced/batch/calculation/create', method: 'post', data, headers: { 'Content-Type': 'application/json' } }),
  updateBatchCalculation: (data) => request({ url: `/glkj/accountant/advanced/batch/calculation/update/${data.calculationId || data.id}`, method: 'put', data, headers: { 'Content-Type': 'application/json' } }),
  deleteBatchCalculation: (id) => request({ url: `/glkj/accountant/advanced/batch/calculation/delete/${id}`, method: 'delete' }),
  copyBatchCalculation: (id) => request({ url: `/glkj/accountant/advanced/batch/calculation/${id}/copy`, method: 'post', data: {} }),
  executeBatchCalculation: (id) => request({ url: `/glkj/accountant/advanced/batch/calculation/${id}/execute`, method: 'post', data: {} }),
  stopBatchCalculation: (id) => request({ url: `/glkj/accountant/advanced/batch/calculation/${id}/stop`, method: 'post', data: {} }),
  retryBatchCalculation: (id) => request({ url: `/glkj/accountant/advanced/batch/calculation/${id}/retry`, method: 'post', data: {} }),
  exportBatchCalculation: (id) => request({ url: `/glkj/accountant/advanced/batch/calculation/${id}/export`, method: 'get' }),

  // ===== 预算优化 CRUD =====
  createBudgetOptimization: (data) => request({ url: '/glkj/accountant/advanced/optimization/create', method: 'post', data, headers: { 'Content-Type': 'application/json' } }),
  updateBudgetOptimization: (data) => request({ url: `/glkj/accountant/advanced/optimization/update/${data.optimizationId || data.id}`, method: 'put', data, headers: { 'Content-Type': 'application/json' } }),
  deleteBudgetOptimization: (id) => request({ url: `/glkj/accountant/advanced/optimization/delete/${id}`, method: 'delete' }),
  copyBudgetOptimization: (id) => request({ url: `/glkj/accountant/advanced/optimization/${id}/copy`, method: 'post', data: {} }),
  runBudgetOptimization: (id) => request({ url: `/glkj/accountant/advanced/optimization/${id}/run`, method: 'post', data: {} }),
  stopBudgetOptimization: (id) => request({ url: `/glkj/accountant/advanced/optimization/${id}/stop`, method: 'post', data: {} }),
  applyBudgetOptimization: (id) => request({ url: `/glkj/accountant/advanced/optimization/${id}/apply`, method: 'post', data: {} }),
  exportBudgetOptimization: (id) => request({ url: `/glkj/accountant/advanced/optimization/${id}/export`, method: 'get' }),

  // ===== 预算模拟 CRUD =====
  createBudgetSimulation: (data) => request({ url: '/glkj/accountant/advanced/simulation/create', method: 'post', data, headers: { 'Content-Type': 'application/json' } }),
  updateBudgetSimulation: (data) => request({ url: `/glkj/accountant/advanced/simulation/update/${data.simulationId || data.id}`, method: 'put', data, headers: { 'Content-Type': 'application/json' } }),
  deleteBudgetSimulation: (id) => request({ url: `/glkj/accountant/advanced/simulation/delete/${id}`, method: 'delete' }),
  copyBudgetSimulation: (id) => request({ url: `/glkj/accountant/advanced/simulation/${id}/copy`, method: 'post', data: {} }),
  runBudgetSimulation: (id) => request({ url: `/glkj/accountant/advanced/simulation/${id}/run`, method: 'post', data: {} }),
  stopBudgetSimulation: (id) => request({ url: `/glkj/accountant/advanced/simulation/${id}/stop`, method: 'post', data: {} }),
  exportBudgetSimulation: (id) => request({ url: `/glkj/accountant/advanced/simulation/${id}/export`, method: 'get' }),

  // ===== 协同预算 CRUD =====
  createCollaborativeProject: (data) => request({ url: '/glkj/accountant/advanced/collaboration/create', method: 'post', data, headers: { 'Content-Type': 'application/json' } }),
  updateCollaborativeProject: (data) => request({ url: `/glkj/accountant/advanced/collaboration/update/${data.projectId || data.id}`, method: 'put', data, headers: { 'Content-Type': 'application/json' } }),
  deleteCollaborativeProject: (id) => request({ url: `/glkj/accountant/advanced/collaboration/delete/${id}`, method: 'delete' }),
  archiveCollaborativeProject: (id) => request({ url: `/glkj/accountant/advanced/collaboration/${id}/archive`, method: 'post', data: {} }),
  exportCollaborativeProject: (id) => request({ url: `/glkj/accountant/advanced/collaboration/${id}/export`, method: 'get' }),
  getCollaborationComments: (projectId) => request({ url: `/glkj/accountant/advanced/collaboration/${projectId}/comments`, method: 'get' }),
  addCollaborationComment: (projectId, data) => request({ url: `/glkj/accountant/advanced/collaboration/${projectId}/comments`, method: 'post', data, headers: { 'Content-Type': 'application/json' } }),
  getCollaborationTypeStats: () => request({ url: '/glkj/accountant/advanced/collaboration/type-stats', method: 'get' }),

  // ===== 数据挖掘 CRUD =====
  createDataMining: (data) => request({ url: '/glkj/accountant/advanced/data-mining/create', method: 'post', data, headers: { 'Content-Type': 'application/json' } }),
  updateDataMining: (data) => request({ url: `/glkj/accountant/advanced/data-mining/update/${data.taskId || data.id}`, method: 'put', data, headers: { 'Content-Type': 'application/json' } }),
  deleteDataMining: (id) => request({ url: `/glkj/accountant/advanced/data-mining/delete/${id}`, method: 'delete' }),
  copyDataMining: (id) => request({ url: `/glkj/accountant/advanced/data-mining/${id}/copy`, method: 'post', data: {} }),
  runDataMining: (id) => request({ url: `/glkj/accountant/advanced/data-mining/${id}/run`, method: 'post', data: {} }),
  stopDataMining: (id) => request({ url: `/glkj/accountant/advanced/data-mining/${id}/stop`, method: 'post', data: {} }),
  exportDataMining: (id) => request({ url: `/glkj/accountant/advanced/data-mining/${id}/export`, method: 'get' }),

  // ===== 智能推荐 CRUD =====
  deleteRecommendation: (id) => request({ url: `/glkj/accountant/budget/intelligent/recommendation/delete/${id}`, method: 'delete' }),
  updateRecommendation: (data) => request({ url: '/glkj/accountant/budget/intelligent/recommendation/update', method: 'post', data: data || {}, headers: { 'Content-Type': 'application/json' } }),
  acceptRecommendation: (id) => request({ url: `/glkj/accountant/budget/intelligent/recommendation/accept/${id}`, method: 'post', data: {} }),
  rejectRecommendation: (id) => request({ url: `/glkj/accountant/budget/intelligent/recommendation/reject/${id}`, method: 'post', data: {} }),
  applyRecommendation: (id) => request({ url: `/glkj/accountant/budget/intelligent/recommendation/apply`, method: 'post', data: { recommendationId: id } }),
  shareRecommendation: (id) => request({ url: `/glkj/accountant/budget/intelligent/recommendation/share`, method: 'post', data: { recommendationId: id } }),

  // ===== 风险评估 CRUD =====
  deleteRiskAssessment: (id) => request({ url: `/glkj/accountant/advanced/risk-assessment/delete/${id}`, method: 'delete' })
}
