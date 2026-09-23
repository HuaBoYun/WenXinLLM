/**
 * NCV65全面预算系统 - 预算控制API
 * 
 * @description 预算控制功能API接口，包含控制规则、实时控制引擎、预警管理、执行监控等功能
 * @version 1.0.0
 * @author AI Assistant
 * @date 2025-01-08
 * @module budgetControl
 */

import request from '@/utils/request'

// ==================== 控制规则管理API ====================

/**
 * 创建控制规则
 * @param {Object} data 规则数据
 * @returns {Promise} 请求结果
 */
export function createControlRule(data) {
  return request({
    url: '/glkj/accountant/budget/control/rule/create',
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json' }
  })
}

/**
 * 查询控制规则详情
 * @param {String} ruleId 规则ID
 * @returns {Promise} 请求结果
 */
export function getControlRule(ruleId) {
  return request({
    url: `/glkj/accountant/budget/control/rule/detail/${ruleId}`,
    method: 'get'
  })
}

/**
 * 更新控制规则
 * @param {String} ruleId 规则ID
 * @param {Object} data 更新数据
 * @returns {Promise} 请求结果
 */
export function updateControlRule(ruleId, data) {
  return request({
    url: `/glkj/accountant/budget/control/rule/update/${ruleId}`,
    method: 'put',
    data,
    headers: { 'Content-Type': 'application/json' }
  })
}

/**
 * 删除控制规则
 * @param {String} ruleId 规则ID
 * @returns {Promise} 请求结果
 */
export function deleteControlRule(ruleId) {
  return request({
    url: `/glkj/accountant/budget/control/rule/delete/${ruleId}`,
    method: 'delete'
  })
}

/**
 * 分页查询控制规则列表
 * @param {Number} current 当前�?
 * @param {Number} size 页大�?
 * @param {Object} params 查询参数
 * @returns {Promise} 请求结果
 */
export function getControlRulePage(current, size, params) {
  return request({
    url: '/glkj/accountant/budget/control/rule/page',
    method: 'post',
    params: { current, size },
    data: params,
    headers: { 'Content-Type': 'application/json' }
  })
}

/**
 * 启用控制规则
 * @param {String} ruleId 规则ID
 * @returns {Promise} 请求结果
 */
export function enableControlRule(ruleId) {
  return request({
    url: `/glkj/accountant/budget/control/rule/enable/${ruleId}`,
    method: 'post'
  })
}

/**
 * 禁用控制规则
 * @param {String} ruleId 规则ID
 * @returns {Promise} 请求结果
 */
export function disableControlRule(ruleId) {
  return request({
    url: `/glkj/accountant/budget/control/rule/disable/${ruleId}`,
    method: 'post'
  })
}

/**
 * 测试控制规则
 * @param {String} ruleId 规则ID
 * @param {Object} testData 测试数据
 * @returns {Promise} 请求结果
 */
export function testControlRule(ruleId, testData) {
  return request({
    url: `/glkj/accountant/budget/control/rule/validate`,
    method: 'post',
    data: testData,
    headers: { 'Content-Type': 'application/json' }
  })
}

// ==================== 实时控制引擎API ====================

/**
 * 执行预算控制检�?
 * @param {Object} data 控制检查数�?
 * @returns {Promise} 请求结果
 */
export function executeBudgetControl(data) {
  return request({
    url: '/glkj/accountant/budget/control/execute',
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json' }
  })
}

/**
 * 批量执行预算控制检�?
 * @param {Array} dataList 控制检查数据数�?
 * @returns {Promise} 请求结果
 */
export function batchExecuteBudgetControl(dataList) {
  return request({
    url: '/glkj/accountant/budget/control/batch-execute',
    method: 'post',
    data: dataList,
    headers: { 'Content-Type': 'application/json' }
  })
}

/**
 * 获取控制检查结�?
 * @param {String} checkId 检查ID
 * @returns {Promise} 请求结果
 */
export function getControlCheckResult(checkId) {
  return request({
    url: `/glkj/accountant/budget/control/check/${checkId}/result`,
    method: 'get'
  })
}

/**
 * 强制通过控制检�?
 * @param {String} checkId 检查ID
 * @param {Object} data 强制通过数据
 * @returns {Promise} 请求结果
 */
export function forcePassControl(checkId, data) {
  return request({
    url: `/glkj/accountant/budget/control/check/${checkId}/force-pass`,
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json' }
  })
}

/**
 * 获取控制引擎状�?
 * @returns {Promise} 请求结果
 */
export function getControlEngineStatus() {
  return request({
    url: '/glkj/accountant/budget/control/engine/status',
    method: 'get'
  })
}

/**
 * 重启控制引擎
 * @returns {Promise} 请求结果
 */
export function restartControlEngine() {
  return request({
    url: '/glkj/accountant/budget/control/engine/restart',
    method: 'post'
  })
}

// ==================== 预警管理API ====================

/**
 * 创建预警规则
 * @param {Object} data 预警规则数据
 * @returns {Promise} 请求结果
 */
export function createWarningRule(data) {
  return request({
    url: '/glkj/accountant/budget/control/warning/create',
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json' }
  })
}

/**
 * 查询预警规则详情
 * @param {String} ruleId 规则ID
 * @returns {Promise} 请求结果
 */
export function getWarningRule(ruleId) {
  return request({
    url: `/glkj/accountant/budget/control/warning/detail/${ruleId}`,
    method: 'get'
  })
}

/**
 * 更新预警规则
 * @param {String} ruleId 规则ID
 * @param {Object} data 更新数据
 * @returns {Promise} 请求结果
 */
export function updateWarningRule(ruleId, data) {
  return request({
    url: `/glkj/accountant/budget/control/warning/update/${ruleId}`,
    method: 'put',
    data,
    headers: { 'Content-Type': 'application/json' }
  })
}

/**
 * 删除预警规则
 * @param {String} ruleId 规则ID
 * @returns {Promise} 请求结果
 */
export function deleteWarningRule(ruleId) {
  return request({
    url: `/glkj/accountant/budget/control/warning/delete/${ruleId}`,
    method: 'delete'
  })
}

/**
 * 分页查询预警规则列表
 * @param {Number} current 当前�?
 * @param {Number} size 页大�?
 * @param {Object} params 查询参数
 * @returns {Promise} 请求结果
 */
export function getWarningRulePage(current, size, params) {
  return request({
    url: '/glkj/accountant/budget/control/warning/page',
    method: 'post',
    params: { current, size, ...params }
  })
}

/**
 * 分页查询预警记录列表
 * @param {Number} current 当前�?
 * @param {Number} size 页大�?
 * @param {Object} params 查询参数
 * @returns {Promise} 请求结果
 */
export function getWarningRecordPage(current, size, params) {
  return request({
    url: '/glkj/accountant/budget/control/warning/record/page',
    method: 'post',
    params: { current, size },
    data: params,
    headers: { 'Content-Type': 'application/json' }
  })
}

/**
 * 处理预警记录
 * @param {String} recordId 记录ID
 * @param {Object} data 处理数据
 * @returns {Promise} 请求结果
 */
export function handleWarningRecord(recordId, data) {
  return request({
    url: `/glkj/accountant/budget/control/warning/record/handle/${recordId}`,
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json' }
  })
}

/**
 * 批量处理预警记录
 * @param {Array} recordIds 记录ID数组
 * @param {Object} data 处理数据
 * @returns {Promise} 请求结果
 */
export function batchHandleWarningRecords(recordIds, data) {
  return request({
    url: `/glkj/accountant/budget/control/warning/batch-process`,
    method: 'post',
    data: { recordIds, ...data },
    headers: { 'Content-Type': 'application/json' }
  })
}

// ==================== 执行监控API ====================

/**
 * 创建监控任务
 * @param {Object} data 监控任务数据
 * @returns {Promise} 请求结果
 */
export function createMonitorTask(data) {
  return request({
    url: '/glkj/accountant/budget/control/monitor/create',
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json' }
  })
}

/**
 * 查询监控任务详情
 * @param {String} taskId 任务ID
 * @returns {Promise} 请求结果
 */
export function getMonitorTask(taskId) {
  return request({
    url: `/glkj/accountant/budget/control/monitor/detail/${taskId}`,
    method: 'get'
  })
}

/**
 * 更新监控任务
 * @param {String} taskId 任务ID
 * @param {Object} data 更新数据
 * @returns {Promise} 请求结果
 */
export function updateMonitorTask(taskId, data) {
  return request({
    url: `/glkj/accountant/budget/control/monitor/update/${taskId}`,
    method: 'put',
    data,
    headers: { 'Content-Type': 'application/json' }
  })
}

/**
 * 删除监控任务
 * @param {String} taskId 任务ID
 * @returns {Promise} 请求结果
 */
export function deleteMonitorTask(taskId) {
  return request({
    url: `/glkj/accountant/budget/control/monitor/delete/${taskId}`,
    method: 'delete'
  })
}

/**
 * 分页查询监控任务列表
 * @param {Number} current 当前�?
 * @param {Number} size 页大�?
 * @param {Object} params 查询参数
 * @returns {Promise} 请求结果
 */
export function getMonitorTaskPage(current, size, params) {
  return request({
    url: '/glkj/accountant/budget/control/monitor/page',
    method: 'post',
    params: { current, size },
    data: params,
    headers: { 'Content-Type': 'application/json' }
  })
}

/**
 * 启动监控任务
 * @param {String} taskId 任务ID
 * @returns {Promise} 请求结果
 */
export function startMonitorTask(taskId) {
  return request({
    url: `/glkj/accountant/budget/control/monitor/start/${taskId}`,
    method: 'post'
  })
}

/**
 * 停止监控任务
 * @param {String} taskId 任务ID
 * @returns {Promise} 请求结果
 */
export function stopMonitorTask(taskId) {
  return request({
    url: `/glkj/accountant/budget/control/monitor/stop/${taskId}`,
    method: 'post'
  })
}

/**
 * 获取监控结果
 * @param {String} taskId 任务ID
 * @param {Object} params 查询参数
 * @returns {Promise} 请求结果
 */
export function getMonitorResult(taskId, params) {
  return request({
    url: `/glkj/accountant/budget/control/monitor/detail/${taskId}`,
    method: 'get',
    params
  })
}

/**
 * 获取实时监控数据
 * @param {Object} params 查询参数
 * @returns {Promise} 请求结果
 */
export function getRealTimeMonitorData(params) {
  return request({
    url: '/glkj/accountant/budget/control/monitor/statistics',
    method: 'get',
    params
  })
}

// ==================== 控制日志API ====================

/**
 * 分页查询控制日志
 * @param {Number} current 当前�?
 * @param {Number} size 页大�?
 * @param {Object} params 查询参数
 * @returns {Promise} 请求结果
 */
export function getControlLogPage(current, size, params) {
  return request({
    url: '/glkj/accountant/budget/control/log/page',
    method: 'post',
    params: { current, size },
    data: params,
    headers: { 'Content-Type': 'application/json' }
  })
}

/**
 * 查询控制日志详情
 * @param {String} logId 日志ID
 * @returns {Promise} 请求结果
 */
export function getControlLogDetail(logId) {
  return request({
    url: `/glkj/accountant/budget/control/log/${logId}`,
    method: 'get'
  })
}

/**
 * 导出控制日志
 * @param {Object} params 导出参数
 * @returns {Promise} 请求结果
 */
export function exportControlLog(params) {
  return request({
    url: '/glkj/accountant/budget/control/log/export',
    method: 'post',
    data: params,
    responseType: 'blob',
    headers: { 'Content-Type': 'application/json' }
  })
}

// ==================== 简化函数名导出（兼容前端组件） ====================

// 预算控制API
export const budgetControlApi = {
  create: createControlRule,
  update: updateControlRule,
  delete: deleteControlRule,
  get: getControlRule,
  getPage: (data) => request({
    url: '/glkj/accountant/budget/control/rule/page',
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json' }
  }),
  enable: enableControlRule,
  disable: disableControlRule,
  getUsers: () => request({ url: '/glkj/accountant/budget/control/rule/users', method: 'get' }),
  validate: (data) => request({ url: '/glkj/accountant/budget/control/rule/validate', method: 'post', data, headers: { 'Content-Type': 'application/json' } }),
  getStatistics: () => request({ url: '/glkj/accountant/budget/control/rule/statistics', method: 'get' }),
  export: (data) => request({ url: '/glkj/accountant/budget/control/rule/export', method: 'post', data, responseType: 'blob', headers: { 'Content-Type': 'application/json' } }),
  exportSingle: (id) => request({ url: `/glkj/accountant/budget/control/rule/export/${id}`, method: 'get', responseType: 'blob' }),
  getControlStats: () => request({ url: '/glkj/accountant/budget/control/stats', method: 'get' }),
  getControlHealth: () => request({ url: '/glkj/accountant/budget/control/health', method: 'get' })
}

// 预算预警API
export const budgetWarningApi = {
  create: createWarningRule,
  update: updateWarningRule,
  delete: deleteWarningRule,
  get: getWarningRule,
  getPage: (data) => request({
    url: '/glkj/accountant/budget/control/warning/page',
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json' }
  }),
  trigger: null, // 暂未实现
  handle: handleWarningRecord,
  getUsers: () => request({ url: '/glkj/accountant/budget/control/warning/users', method: 'get' }),
  processWarning: (data) => request({ url: '/glkj/accountant/budget/control/warning/process', method: 'post', data, headers: { 'Content-Type': 'application/json' } }),
  batchProcess: (ids) => request({ url: '/glkj/accountant/budget/control/warning/batch-process', method: 'post', data: { ids }, headers: { 'Content-Type': 'application/json' } }),
  getHistory: (data) => request({ url: '/glkj/accountant/budget/control/warning/record/page', method: 'post', data, headers: { 'Content-Type': 'application/json' } }),
  import: (formData) => request({ url: '/glkj/accountant/budget/control/warning/import', method: 'post', data: formData, headers: { 'Content-Type': 'multipart/form-data' } }),
  getStatistics: () => request({ url: '/glkj/accountant/budget/control/warning/statistics', method: 'get' }),
  export: (data) => request({ url: '/glkj/accountant/budget/control/warning/export', method: 'post', data, responseType: 'blob', headers: { 'Content-Type': 'application/json' } }),
  exportSingle: (id) => request({ url: `/glkj/accountant/budget/control/warning/export/${id}`, method: 'get', responseType: 'blob' })
}

// ==================== 预算执行API补充 ====================

/**
 * 获取预算执行详情
 * @param {String} executionId 执行ID
 * @returns {Promise} 请求结果
 */
export function getBudgetExecutionDetail(executionId) {
  return request({
    url: `/glkj/accountant/execution/detail/${executionId}`,
    method: 'get'
  })
}

/**
 * 获取预算执行分析
 * @param {String} executionId 执行ID
 * @returns {Promise} 请求结果
 */
export function getBudgetExecutionAnalysis(executionId) {
  return request({
    url: `/glkj/accountant/execution/analysis/${executionId}`,
    method: 'get'
  })
}

/**
 * 获取组织选项
 * @returns {Promise} 请求结果
 */
export function getBudgetExecutionOrganizations() {
  return request({
    url: '/glkj/accountant/execution/organizations',
    method: 'get'
  })
}

/**
 * 获取账户选项
 * @returns {Promise} 请求结果
 */
export function getBudgetExecutionAccounts() {
  return request({
    url: '/glkj/accountant/execution/accounts',
    method: 'get'
  })
}

/**
 * 检查告�?
 * @returns {Promise} 请求结果
 */
export function checkBudgetExecutionWarnings(data) {
  return request({
    url: '/glkj/accountant/execution/check-warnings',
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json' }
  })
}

/**
 * 导出执行报告
 * @param {Object} params 导出参数
 * @returns {Promise} 请求结果
 */
export function exportBudgetExecutionReport(params) {
  return request({
    url: '/glkj/accountant/execution/export',
    method: 'post',
    data: params,
    responseType: 'blob',
    headers: { 'Content-Type': 'application/json' }
  })
}

/**
 * 导出单个执行记录
 * @param {String} executionId 执行ID
 * @returns {Promise} 请求结果
 */
export function exportBudgetExecutionSingle(executionId) {
  return request({
    url: `/glkj/accountant/execution/export/${executionId}`,
    method: 'get',
    responseType: 'blob'
  })
}

/**
 * 导出执行详情
 * @param {String} executionId 执行ID
 * @returns {Promise} 请求结果
 */
export function exportBudgetExecutionDetail(executionId) {
  return request({
    url: `/glkj/accountant/execution/export-detail/${executionId}`,
    method: 'get',
    responseType: 'blob'
  })
}

/**
 * 导出执行分析
 * @param {Array} ids 执行ID数组
 * @returns {Promise} 请求结果
 */
export function exportBudgetExecutionAnalysis(ids) {
  return request({
    url: '/glkj/accountant/execution/export-analysis',
    method: 'post',
    data: { ids },
    responseType: 'blob',
    headers: { 'Content-Type': 'application/json' }
  })
}

// 预算执行API
export const budgetExecutionApi = {
  create: createMonitorTask,
  update: updateMonitorTask,
  delete: deleteMonitorTask,
  get: getMonitorTask,
  getPage: (data) => request({
    url: '/glkj/accountant/execution/page',
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json' }
  }),
  start: startMonitorTask,
  stop: stopMonitorTask,
  getDetail: getBudgetExecutionDetail,
  getAnalysis: getBudgetExecutionAnalysis,
  getTrendAnalysis: (data) => request({ url: '/glkj/accountant/execution/trend-analysis', method: 'post', data, headers: { 'Content-Type': 'application/json' } }),
  getVarianceAnalysis: (data) => request({ url: '/glkj/accountant/execution/variance-analysis', method: 'post', data, headers: { 'Content-Type': 'application/json' } }),
  getProgressAnalysis: (data) => request({ url: '/glkj/accountant/execution/progress-analysis', method: 'post', data, headers: { 'Content-Type': 'application/json' } }),
  getOrganizations: getBudgetExecutionOrganizations,
  getAccounts: getBudgetExecutionAccounts,
  checkWarnings: checkBudgetExecutionWarnings,
  exportReport: exportBudgetExecutionReport,
  exportSingle: exportBudgetExecutionSingle,
  exportDetail: exportBudgetExecutionDetail,
  exportAnalysis: exportBudgetExecutionAnalysis,
  getResult: getMonitorResult,
  getStatistics: () => request({ url: '/glkj/accountant/execution/statistics', method: 'get' }),
  getBatchAnalysis: (ids) => request({ url: '/glkj/accountant/execution/batch-analysis', method: 'post', data: { ids }, headers: { 'Content-Type': 'application/json' } })
}

// 预算监控API
export const budgetMonitorApi = {
  create: createMonitorTask,
  update: updateMonitorTask,
  delete: deleteMonitorTask,
  get: getMonitorTask,
  getPage: (data) => request({
    url: '/glkj/accountant/budget/control/monitor/page',
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json' }
  }),
  start: startMonitorTask,
  stop: stopMonitorTask,
  getRealTimeData: getRealTimeMonitorData,
  getOrganizations: () => request({ url: '/glkj/accountant/budget/control/monitor/organizations', method: 'get' }),
  getBudgetAccounts: () => request({ url: '/glkj/accountant/budget/control/monitor/accounts', method: 'get' }),
  getUsers: () => request({ url: '/glkj/accountant/budget/control/monitor/users', method: 'get' }),
  testMonitor: (data) => request({ url: '/glkj/accountant/budget/control/monitor/test', method: 'post', data, headers: { 'Content-Type': 'application/json' } }),
  refreshAll: () => request({ url: '/glkj/accountant/budget/control/monitor/refresh-all', method: 'post' }),
  export: (data) => request({ url: '/glkj/accountant/budget/control/monitor/export', method: 'post', data, responseType: 'blob', headers: { 'Content-Type': 'application/json' } }),
  getStatistics: () => request({ url: '/glkj/accountant/budget/control/monitor/statistics', method: 'get' }),
  getAlerts: () => request({ url: '/glkj/accountant/budget/control/monitor/alerts', method: 'get' }),
  exportSingle: (id) => request({ url: `/glkj/accountant/budget/control/monitor/export/${id}`, method: 'get', responseType: 'blob' }),
  import: (formData) => request({ url: '/glkj/accountant/budget/control/monitor/import', method: 'post', data: formData, headers: { 'Content-Type': 'multipart/form-data' } })
}

// 预算限额API
export const budgetLimitApi = {
  // 创建预算限额
  create: (data) => request({
    url: '/glkj/accountant/budget/limit/create',
    method: 'post',
    data, headers: { 'Content-Type': 'application/json' } }),

  // 更新预算限额
  update: (limitId, data) => request({
    url: `/glkj/accountant/budget/limit/update/${limitId}`,
    method: 'put',
    data,
    headers: { 'Content-Type': 'application/json' }
  }),

  // 删除预算限额
  delete: (limitId) => request({
    url: `/glkj/accountant/budget/limit/delete/${limitId}`,
    method: 'delete'
  }),

  // 查询限额详情
  get: (limitId) => request({
    url: `/glkj/accountant/budget/limit/detail/${limitId}`,
    method: 'get'
  }),

  // 分页查询限额列表
  getPage: (data) => request({
    url: '/glkj/accountant/budget/limit/page',
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json' }
  }),

  // 启用限额
  enable: (limitId) => request({
    url: `/glkj/accountant/budget/limit/enable/${limitId}`,
    method: 'post'
  }),

  // 停用限额
  disable: (limitId) => request({
    url: `/glkj/accountant/budget/limit/disable/${limitId}`,
    method: 'post'
  }),

  // 检查限额
  check: (data) => request({
    url: '/glkj/accountant/budget/limit/check',
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json' }
  }),
  getOrganizations: () => request({ url: '/glkj/accountant/budget/limit/organizations', method: 'get' }),
  getBudgetAccounts: () => request({ url: '/glkj/accountant/budget/limit/accounts', method: 'get' }),
  calculateUsage: (data) => request({ url: '/glkj/accountant/budget/limit/calculate-usage', method: 'post', data, headers: { 'Content-Type': 'application/json' } }),
  batchAdjust: (ids) => request({ url: '/glkj/accountant/budget/limit/batch/adjust', method: 'post', data: { ids }, headers: { 'Content-Type': 'application/json' } }),
  freeze: (id) => request({ url: `/glkj/accountant/budget/limit/freeze/${id}`, method: 'post' }),
  getStatistics: () => request({ url: '/glkj/accountant/budget/limit/statistics', method: 'get' }),
  export: (data) => request({ url: '/glkj/accountant/budget/limit/export', method: 'post', data, responseType: 'blob', headers: { 'Content-Type': 'application/json' } }),
  exportSingle: (id) => request({ url: `/glkj/accountant/budget/limit/export/${id}`, method: 'get', responseType: 'blob' }),
  // 查询限额操作历史
  getHistory: (limitId) => request({ url: `/glkj/accountant/budget/limit/history/${limitId}`, method: 'get' })
}

// 预算冻结API
export const budgetFreezeApi = {
  // 创建预算冻结
  create: (data) => request({
    url: '/glkj/accountant/budget/freeze/create',
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json' }
  }),

  // 更新预算冻结
  update: (freezeId, data) => request({
    url: `/glkj/accountant/budget/freeze/update/${freezeId}`,
    method: 'put',
    data,
    headers: { 'Content-Type': 'application/json' }
  }),

  // 删除预算冻结
  delete: (freezeId) => request({
    url: `/glkj/accountant/budget/freeze/delete/${freezeId}`,
    method: 'delete'
  }),

  // 查询冻结详情
  get: (freezeId) => request({
    url: `/glkj/accountant/budget/freeze/detail/${freezeId}`,
    method: 'get'
  }),

  // 分页查询冻结列表
  getPage: (data) => request({
    url: '/glkj/accountant/budget/freeze/page',
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json' }
  }),

  // 执行冻结
  freeze: (freezeId) => request({
    url: `/glkj/accountant/budget/freeze/execute/${freezeId}`,
    method: 'post'
  }),

  // 解冻
  unfreeze: (freezeId) => request({
    url: `/glkj/accountant/budget/freeze/unfreeze/${freezeId}`,
    method: 'post'
  }),

  // 批量冻结
  batchFreeze: (data) => request({
    url: '/glkj/accountant/budget/freeze/batch/freeze',
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json' }
  }),

  // 批量解冻
  batchUnfreeze: (data) => request({
    url: '/glkj/accountant/budget/freeze/batch/unfreeze',
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json' }
  }),
  getOrganizations: () => request({ url: '/glkj/accountant/budget/freeze/organizations', method: 'get' }),
  getBudgetAccounts: () => request({ url: '/glkj/accountant/budget/freeze/accounts', method: 'get' }),
  getUsers: () => request({ url: '/glkj/accountant/budget/freeze/users', method: 'get' }),
  validateFreeze: (data) => request({ url: '/glkj/accountant/budget/freeze/validate', method: 'post', data, headers: { 'Content-Type': 'application/json' } }),
  getStatistics: () => request({ url: '/glkj/accountant/budget/freeze/statistics', method: 'get' }),
  export: (data) => request({ url: '/glkj/accountant/budget/freeze/export', method: 'post', data, headers: { 'Content-Type': 'application/json' } }),
  exportSingle: (id) => request({ url: `/glkj/accountant/budget/freeze/export/${id}`, method: 'get' }),
  getHistory: (freezeId) => request({ url: `/glkj/accountant/budget/freeze/history/${freezeId}`, method: 'get' }),
  extendFreeze: (freezeId, data) => request({
    url: `/glkj/accountant/budget/freeze/extend/${freezeId}`,
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json' }
  }),
  importData: (formData) => request({
    url: '/glkj/accountant/budget/freeze/import',
    method: 'post',
    data: formData,
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

// 预算配额API
export const budgetQuotaApi = {
  // 创建预算配额
  create: (data) => request({
    url: '/glkj/accountant/budget/quota/create',
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json' }
  }),

  // 更新预算配额
  update: (quotaId, data) => request({
    url: `/glkj/accountant/budget/quota/update/${quotaId}`,
    method: 'put',
    data,
    headers: { 'Content-Type': 'application/json' }
  }),

  // 删除预算配额
  delete: (quotaId) => request({
    url: `/glkj/accountant/budget/quota/delete/${quotaId}`,
    method: 'delete'
  }),

  // 查询配额详情
  get: (quotaId) => request({
    url: `/glkj/accountant/budget/quota/detail/${quotaId}`,
    method: 'get'
  }),

  // 分页查询配额列表
  getPage: (data) => request({
    url: '/glkj/accountant/budget/quota/page',
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json' }
  }),

  // 分配配额
  allocate: (data) => request({
    url: '/glkj/accountant/budget/quota/allocate',
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json' }
  }),

  // 调整配额
  adjust: (quotaId, data) => request({
    url: `/glkj/accountant/budget/quota/adjust/${quotaId}`,
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json' }
  }),

  // 批量调整配额
  batchAdjust: (data) => request({
    url: '/glkj/accountant/budget/quota/batch/adjust',
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json' }
  }),
  getOrganizations: () => request({ url: '/glkj/accountant/budget/quota/organizations', method: 'get' }),
  getBudgetAccounts: () => request({ url: '/glkj/accountant/budget/quota/accounts', method: 'get' }),
  calculateAllocation: (data) => request({ url: '/glkj/accountant/budget/quota/calculate-allocation', method: 'post', data, headers: { 'Content-Type': 'application/json' } }),
  getStatistics: () => request({ url: '/glkj/accountant/budget/quota/statistics', method: 'get' }),
  export: (data) => request({ url: '/glkj/accountant/budget/quota/export', method: 'post', data, responseType: 'blob', headers: { 'Content-Type': 'application/json' } }),
  exportSingle: (id) => request({ url: `/glkj/accountant/budget/quota/export/${id}`, method: 'get', responseType: 'blob' }),
  import: (formData) => request({ url: '/glkj/accountant/budget/quota/import', method: 'post', data: formData, headers: { 'Content-Type': 'multipart/form-data' } }),
  transfer: (data) => request({ url: '/glkj/accountant/budget/quota/transfer', method: 'post', data, headers: { 'Content-Type': 'application/json' } }),
  getHistory: (quotaId) => request({ url: `/glkj/accountant/budget/quota/history/${quotaId}`, method: 'get' })
}

// 预算释放API
export const budgetReleaseApi = {
  // 创建释放申请
  create: (data) => request({
    url: '/glkj/accountant/budget/release/create',
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json' }
  }),

  // 更新释放申请
  update: (releaseId, data) => request({
    url: `/glkj/accountant/budget/release/update/${releaseId}`,
    method: 'put',
    data,
    headers: { 'Content-Type': 'application/json' }
  }),

  // 删除释放申请
  delete: (releaseId) => request({
    url: `/glkj/accountant/budget/release/delete/${releaseId}`,
    method: 'delete'
  }),

  // 查询释放详情
  get: (releaseId) => request({
    url: `/glkj/accountant/budget/release/detail/${releaseId}`,
    method: 'get'
  }),

  // 分页查询释放列表
  getPage: (data) => request({
    url: '/glkj/accountant/budget/release/page',
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json' }
  }),

  // 执行释放
  release: (releaseId) => request({
    url: `/glkj/accountant/budget/release/execute/${releaseId}`,
    method: 'post'
  }),

  // 审批释放
  approve: (releaseId, data) => request({
    url: `/glkj/accountant/budget/release/approve/${releaseId}`,
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json' }
  }),

  // 拒绝释放
  reject: (releaseId, data) => request({
    url: `/glkj/accountant/budget/release/reject/${releaseId}`,
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json' }
  }),

  // 批量释放
  batchRelease: (data) => request({
    url: '/glkj/accountant/budget/release/batch/release',
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json' }
  }),
  getOrganizations: () => request({ url: '/glkj/accountant/budget/release/organizations', method: 'get' }),
  getBudgetAccounts: () => request({ url: '/glkj/accountant/budget/release/accounts', method: 'get' }),
  getUsers: () => request({ url: '/glkj/accountant/budget/release/users', method: 'get' }),
  execute: (id) => request({ url: `/glkj/accountant/budget/release/execute/${id}`, method: 'post' }),
  validateRelease: (data) => request({ url: '/glkj/accountant/budget/release/validate', method: 'post', data, headers: { 'Content-Type': 'application/json' } }),
  batchApprove: (ids) => request({ url: '/glkj/accountant/budget/release/batch/approve', method: 'post', data: { ids }, headers: { 'Content-Type': 'application/json' } }),
  getStatistics: () => request({ url: '/glkj/accountant/budget/release/statistics', method: 'get' }),
  export: (data) => request({ url: '/glkj/accountant/budget/release/export', method: 'post', data, responseType: 'blob', headers: { 'Content-Type': 'application/json' } }),
  exportSingle: (id) => request({ url: `/glkj/accountant/budget/release/export/${id}`, method: 'get', responseType: 'blob' }),
  import: (formData) => request({ url: '/glkj/accountant/budget/release/import', method: 'post', data: formData, headers: { 'Content-Type': 'multipart/form-data' } })
}

// 预算保留API
export const budgetReserveApi = {
  // 创建保留申请
  create: (data) => request({
    url: '/glkj/accountant/budget/reserve/create',
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json' }
  }),

  // 更新保留申请
  update: (reserveId, data) => request({
    url: `/glkj/accountant/budget/reserve/update/${reserveId}`,
    method: 'put',
    data,
    headers: { 'Content-Type': 'application/json' }
  }),

  // 删除保留申请
  delete: (reserveId) => request({
    url: `/glkj/accountant/budget/reserve/delete/${reserveId}`,
    method: 'delete'
  }),

  // 查询保留详情
  get: (reserveId) => request({
    url: `/glkj/accountant/budget/reserve/detail/${reserveId}`,
    method: 'get'
  }),

  // 分页查询保留列表
  getPage: (data) => request({
    url: '/glkj/accountant/budget/reserve/page',
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json' }
  }),

  // 执行保留
  reserve: (reserveId) => request({
    url: `/glkj/accountant/budget/reserve/execute/${reserveId}`,
    method: 'post'
  }),

  // 取消保留
  cancel: (reserveId) => request({
    url: `/glkj/accountant/budget/reserve/cancel/${reserveId}`,
    method: 'post'
  }),

  // 审批保留
  approve: (reserveId, data) => request({
    url: `/glkj/accountant/budget/reserve/approve/${reserveId}`,
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json' }
  }),

  // 批量保留
  batchReserve: (data) => request({
    url: '/glkj/accountant/budget/reserve/batch/reserve',
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json' }
  }),
  getOrganizations: () => request({ url: '/glkj/accountant/budget/reserve/organizations', method: 'get' }),
  getBudgetAccounts: () => request({ url: '/glkj/accountant/budget/reserve/accounts', method: 'get' }),
  calculateReserve: (data) => request({ url: '/glkj/accountant/budget/reserve/calculate', method: 'post', data, headers: { 'Content-Type': 'application/json' } }),
  useReserve: (data) => request({ url: '/glkj/accountant/budget/reserve/use', method: 'post', data, headers: { 'Content-Type': 'application/json' } }),
  releaseReserve: (data) => request({ url: '/glkj/accountant/budget/reserve/release', method: 'post', data, headers: { 'Content-Type': 'application/json' } }),
  batchRelease: (ids) => request({ url: '/glkj/accountant/budget/reserve/batch/release', method: 'post', data: { ids }, headers: { 'Content-Type': 'application/json' } }),
  getStatistics: () => request({ url: '/glkj/accountant/budget/reserve/statistics', method: 'get' }),
  export: (data) => request({ url: '/glkj/accountant/budget/reserve/export', method: 'post', data, responseType: 'blob', headers: { 'Content-Type': 'application/json' } }),
  exportSingle: (id) => request({ url: `/glkj/accountant/budget/reserve/export/${id}`, method: 'get', responseType: 'blob' }),
  import: (formData) => request({ url: '/glkj/accountant/budget/reserve/import', method: 'post', data: formData, headers: { 'Content-Type': 'multipart/form-data' } }),
  transfer: (data) => request({ url: '/glkj/accountant/budget/reserve/transfer', method: 'post', data, headers: { 'Content-Type': 'application/json' } }),
  getHistory: (reserveId) => request({ url: `/glkj/accountant/budget/reserve/history/${reserveId}`, method: 'get' })
}

// 预算转移API
export const budgetTransferApi = {
  // 创建转移申请
  create: (data) => request({
    url: '/glkj/accountant/budget/transfer/create',
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json' }
  }),

  // 更新转移申请
  update: (transferId, data) => request({
    url: `/glkj/accountant/budget/transfer/update/${transferId}`,
    method: 'put',
    data,
    headers: { 'Content-Type': 'application/json' }
  }),

  // 删除转移申请
  delete: (transferId) => request({
    url: `/glkj/accountant/budget/transfer/delete/${transferId}`,
    method: 'delete'
  }),

  // 查询转移详情
  get: (transferId) => request({
    url: `/glkj/accountant/budget/transfer/detail/${transferId}`,
    method: 'get'
  }),

  // 分页查询转移列表
  getPage: (data) => request({
    url: '/glkj/accountant/budget/transfer/page',
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json' }
  }),

  // 执行转移
  transfer: (transferId) => request({
    url: `/glkj/accountant/budget/transfer/execute/${transferId}`,
    method: 'post'
  }),

  // 审批转移
  approve: (transferId, data) => request({
    url: `/glkj/accountant/budget/transfer/approve/${transferId}`,
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json' }
  }),

  // 拒绝转移
  reject: (transferId, data) => request({
    url: `/glkj/accountant/budget/transfer/reject/${transferId}`,
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json' }
  }),

  // 批量转移
  batchTransfer: (data) => request({
    url: '/glkj/accountant/budget/transfer/batch/transfer',
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json' }
  }),
  getOrganizations: () => request({ url: '/glkj/accountant/budget/transfer/organizations', method: 'get' }),
  getBudgetAccounts: () => request({ url: '/glkj/accountant/budget/transfer/accounts', method: 'get' }),
  getUsers: () => request({ url: '/glkj/accountant/budget/transfer/users', method: 'get' }),
  execute: (id) => request({ url: `/glkj/accountant/budget/transfer/execute/${id}`, method: 'post' }),
  validateTransfer: (data) => request({ url: '/glkj/accountant/budget/transfer/validate', method: 'post', data, headers: { 'Content-Type': 'application/json' } }),
  batchApprove: (ids) => request({ url: '/glkj/accountant/budget/transfer/batch/approve', method: 'post', data: { ids }, headers: { 'Content-Type': 'application/json' } }),
  getStatistics: () => request({ url: '/glkj/accountant/budget/transfer/statistics', method: 'get' }),
  export: (data) => request({ url: '/glkj/accountant/budget/transfer/export', method: 'post', data, responseType: 'blob', headers: { 'Content-Type': 'application/json' } }),
  exportSingle: (id) => request({ url: `/glkj/accountant/budget/transfer/export/${id}`, method: 'get', responseType: 'blob' }),
  import: (formData) => request({ url: '/glkj/accountant/budget/transfer/import', method: 'post', data: formData, headers: { 'Content-Type': 'multipart/form-data' } })
}

// 预算告警API（独立于预警API，服务BudgetAlert.vue页面）
export const budgetAlertApi = {
  create: (data) => request({ url: '/glkj/accountant/budget/alert/create', method: 'post', data, headers: { 'Content-Type': 'application/json' } }),
  update: (alertId, data) => request({ url: `/glkj/accountant/budget/alert/update/${alertId}`, method: 'put', data, headers: { 'Content-Type': 'application/json' } }),
  delete: (alertId) => request({ url: `/glkj/accountant/budget/alert/delete/${alertId}`, method: 'delete' }),
  get: (alertId) => request({ url: `/glkj/accountant/budget/alert/detail/${alertId}`, method: 'get' }),
  getPage: (data) => request({
    url: '/glkj/accountant/budget/alert/page',
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json' }
  }),
  trigger: null,
  handle: (recordId, data) => request({ url: `/glkj/accountant/budget/alert/handle/${recordId}`, method: 'post', data, headers: { 'Content-Type': 'application/json' } }),
  getUsers: () => request({ url: '/glkj/accountant/budget/alert/users', method: 'get' }),
  acknowledgeAlert: (data) => request({ url: '/glkj/accountant/budget/alert/acknowledge', method: 'post', data, headers: { 'Content-Type': 'application/json' } }),
  escalateAlert: (data) => request({ url: '/glkj/accountant/budget/alert/escalate', method: 'post', data, headers: { 'Content-Type': 'application/json' } }),
  getTriggerHistory: (alertId) => request({ url: `/glkj/accountant/budget/alert/trigger-history/${alertId}`, method: 'get' }),
  batchAcknowledge: (ids) => request({ url: '/glkj/accountant/budget/alert/batch-acknowledge', method: 'post', data: { ids }, headers: { 'Content-Type': 'application/json' } }),
  getStatistics: (data) => request({ url: '/glkj/accountant/budget/alert/statistics', method: 'post', data, headers: { 'Content-Type': 'application/json' } }),
  export: (data) => request({ url: '/glkj/accountant/budget/alert/export', method: 'post', data, responseType: 'blob', headers: { 'Content-Type': 'application/json' } }),
  exportSingle: (id) => request({ url: `/glkj/accountant/budget/alert/export/${id}`, method: 'get', responseType: 'blob' }),
  sendNotification: (alertId) => request({ url: `/glkj/accountant/budget/alert/send-notification/${alertId}`, method: 'post' })
}
