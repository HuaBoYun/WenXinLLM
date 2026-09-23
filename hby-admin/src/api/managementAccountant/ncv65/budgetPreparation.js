/**
 * NCV65全面预算系统 - 预算编制管理API
 *
 * @description 预算编制功能API接口，包含任务管理、数据编制、审批流程、预算调整等功能
 * @version 1.0.0
 * @author AI Assistant
 * @date 2025-01-08
 * @module budgetPreparation
 */

import request from '@/utils/request'

const JSON_HEADER = { 'Content-Type': 'application/json;charset=UTF-8' }

// ==================== 预算任务管理API ====================

/**
 * 创建预算任务
 * @param {Object} data 任务数据
 * @returns {Promise} 请求结果
 */
export function createBudgetTask(data) {
  return request({
    url: '/glkj/accountant/budget/task',
    method: 'post',
    data,
    headers: JSON_HEADER
  })
}

/**
 * 查询预算任务详情
 * @param {String} taskId 任务ID
 * @returns {Promise} 请求结果
 */
export function getBudgetTask(taskId) {
  return request({
    url: `/glkj/accountant/budget/task/${taskId}`,
    method: 'get'
  })
}

/**
 * 更新预算任务
 * @param {String} taskId 任务ID
 * @param {Object} data 更新数据
 * @returns {Promise} 请求结果
 */
export function updateBudgetTask(taskId, data) {
  return request({
    url: `/glkj/accountant/budget/task/${taskId}`,
    method: 'put',
    data,
    headers: JSON_HEADER
  })
}

/**
 * 删除预算任务
 * @param {String} taskId 任务ID
 * @returns {Promise} 请求结果
 */
export function deleteBudgetTask(taskId) {
  return request({
    url: `/glkj/accountant/budget/task/${taskId}`,
    method: 'delete'
  })
}

/**
 * 分页查询预算任务列表
 * @param {Number} current 当前�?
 * @param {Number} size 页大�?
 * @param {Object} params 查询参数
 * @returns {Promise} 请求结果
 */
export function getBudgetTaskPage(current, size, params) {
  return request({
    url: '/glkj/accountant/budget/task/page',
    method: 'post',
    params: { current, size },
    data: params,
    headers: JSON_HEADER
  })
}

/**
 * 启动预算任务
 * @param {String} taskId 任务ID
 * @returns {Promise} 请求结果
 */
export function startBudgetTask(taskId) {
  return request({
    url: `/glkj/accountant/budget/task/${taskId}/start`,
    method: 'post'
  })
}

/**
 * 暂停预算任务
 * @param {String} taskId 任务ID
 * @returns {Promise} 请求结果
 */
export function pauseBudgetTask(taskId) {
  return request({
    url: `/glkj/accountant/budget/task/${taskId}/pause`,
    method: 'post'
  })
}

/**
 * 完成预算任务
 * @param {String} taskId 任务ID
 * @returns {Promise} 请求结果
 */
export function completeBudgetTask(taskId) {
  return request({
    url: `/glkj/accountant/budget/task/${taskId}/complete`,
    method: 'post'
  })
}

// ==================== 预算数据编制API ====================

/**
 * 创建预算数据
 * @param {Object} data 预算数据
 * @returns {Promise} 请求结果
 */
export function createBudgetData(data) {
  return request({
    url: '/glkj/accountant/budget/data',
    method: 'post',
    data,
    headers: JSON_HEADER
  })
}

/**
 * 查询预算数据详情
 * @param {String} dataId 数据ID
 * @returns {Promise} 请求结果
 */
export function getBudgetData(dataId) {
  return request({
    url: `/glkj/accountant/budget/data/${dataId}`,
    method: 'get'
  })
}

/**
 * 更新预算数据
 * @param {String} dataId 数据ID
 * @param {Object} data 更新数据
 * @returns {Promise} 请求结果
 */
export function updateBudgetData(dataId, data) {
  return request({
    url: `/glkj/accountant/budget/data/${dataId}`,
    method: 'put',
    data,
    headers: JSON_HEADER
  })
}

/**
 * 删除预算数据
 * @param {String} dataId 数据ID
 * @returns {Promise} 请求结果
 */
export function deleteBudgetData(dataId) {
  return request({
    url: `/glkj/accountant/budget/data/${dataId}`,
    method: 'delete'
  })
}

/**
 * 分页查询预算数据列表
 * @param {Number} current 当前�?
 * @param {Number} size 页大�?
 * @param {Object} params 查询参数
 * @returns {Promise} 请求结果
 */
export function getBudgetDataPage(current, size, params) {
  return request({
    url: '/glkj/accountant/budget/data/page',
    method: 'post',
    params: { current, size },
    data: params,
    headers: JSON_HEADER
  })
}

/**
 * 批量创建预算数据
 * @param {Array} dataList 预算数据数组
 * @returns {Promise} 请求结果
 */
export function batchCreateBudgetData(dataList) {
  return request({
    url: '/glkj/accountant/budget/data/batch',
    method: 'post',
    data: dataList,
    headers: JSON_HEADER
  })
}

/**
 * 批量更新预算数据
 * @param {Array} dataList 预算数据数组
 * @returns {Promise} 请求结果
 */
export function batchUpdateBudgetData(dataList) {
  return request({
    url: '/glkj/accountant/budget/data/batch',
    method: 'put',
    data: dataList,
    headers: JSON_HEADER
  })
}

// ==================== 预算审批流程API ====================

/**
 * 提交预算审批
 * @param {String} budgetId 预算ID
 * @param {Object} data 提交数据
 * @returns {Promise} 请求结果
 */
export function submitBudgetApproval(budgetId, data) {
  return request({
    url: `/glkj/accountant/budget/approval/${budgetId}/submit`,
    method: 'post',
    data,
    headers: JSON_HEADER
  })
}

/**
 * 审批预算
 * @param {String} approvalId 审批ID
 * @param {Object} data 审批数据
 * @returns {Promise} 请求结果
 */
export function approveBudget(approvalId, data) {
  return request({
    url: `/glkj/accountant/budget/approval/${approvalId}/approve`,
    method: 'post',
    data,
    headers: JSON_HEADER
  })
}

/**
 * 拒绝预算
 * @param {String} approvalId 审批ID
 * @param {Object} data 拒绝数据
 * @returns {Promise} 请求结果
 */
export function rejectBudget(approvalId, data) {
  return request({
    url: `/glkj/accountant/budget/approval/${approvalId}/reject`,
    method: 'post',
    data,
    headers: JSON_HEADER
  })
}

/**
 * 撤回预算审批
 * @param {String} approvalId 审批ID
 * @returns {Promise} 请求结果
 */
export function withdrawBudgetApproval(approvalId) {
  return request({
    url: `/glkj/accountant/budget/approval/${approvalId}/withdraw`,
    method: 'post'
  })
}

/**
 * 查询审批流程详情
 * @param {String} approvalId 审批ID
 * @returns {Promise} 请求结果
 */
export function getBudgetApprovalDetail(approvalId) {
  return request({
    url: `/glkj/accountant/budget/approval/${approvalId}`,
    method: 'get'
  })
}

/**
 * 分页查询待审批预算列�?
 * @param {Number} current 当前�?
 * @param {Number} size 页大�?
 * @param {Object} params 查询参数
 * @returns {Promise} 请求结果
 */
export function getPendingApprovalPage(current, size, params) {
  return request({
    url: '/glkj/accountant/budget/approval/pending/page',
    method: 'post',
    params: { current, size },
    data: params,
    headers: JSON_HEADER
  })
}

// ==================== 预算调整API ====================

/**
 * 创建预算调整申请
 * @param {Object} data 调整申请数据
 * @returns {Promise} 请求结果
 */
export function createBudgetAdjustment(data) {
  return request({
    url: '/glkj/accountant/budget/adjustment',
    method: 'post',
    data,
    headers: JSON_HEADER
  })
}

/**
 * 查询预算调整详情
 * @param {String} adjustmentId 调整ID
 * @returns {Promise} 请求结果
 */
export function getBudgetAdjustment(adjustmentId) {
  return request({
    url: `/glkj/accountant/budget/adjustment/${adjustmentId}`,
    method: 'get'
  })
}

/**
 * 更新预算调整申请
 * @param {String} adjustmentId 调整ID
 * @param {Object} data 更新数据
 * @returns {Promise} 请求结果
 */
export function updateBudgetAdjustment(adjustmentId, data) {
  return request({
    url: `/glkj/accountant/budget/adjustment/${adjustmentId}`,
    method: 'put',
    data,
    headers: JSON_HEADER
  })
}

/**
 * 删除预算调整申请
 * @param {String} adjustmentId 调整ID
 * @returns {Promise} 请求结果
 */
export function deleteBudgetAdjustment(adjustmentId) {
  return request({
    url: `/glkj/accountant/budget/adjustment/${adjustmentId}`,
    method: 'delete'
  })
}

/**
 * 分页查询预算调整列表
 * @param {Number} current 当前�?
 * @param {Number} size 页大�?
 * @param {Object} params 查询参数
 * @returns {Promise} 请求结果
 */
export function getBudgetAdjustmentPage(current, size, params) {
  return request({
    url: '/glkj/accountant/budget/adjustment/page',
    method: 'post',
    params: { current, size },
    data: params,
    headers: JSON_HEADER
  })
}

/**
 * 提交预算调整审批
 * @param {String} adjustmentId 调整ID
 * @returns {Promise} 请求结果
 */
export function submitAdjustmentApproval(adjustmentId) {
  return request({
    url: `/glkj/accountant/budget/adjustment/${adjustmentId}/submit`,
    method: 'post'
  })
}

// ==================== 批量计算API ====================

/**
 * 执行批量计算
 * @param {Object} data 计算参数
 * @returns {Promise} 请求结果
 */
export function executeBatchCalculation(data) {
  return request({
    url: '/glkj/accountant/budget/calculation/batch',
    method: 'post',
    data,
    headers: JSON_HEADER
  })
}

/**
 * 查询计算任务状�?
 * @param {String} taskId 任务ID
 * @returns {Promise} 请求结果
 */
export function getCalculationTaskStatus(taskId) {
  return request({
    url: `/glkj/accountant/budget/calculation/task/${taskId}/status`,
    method: 'get'
  })
}

/**
 * 获取计算结果
 * @param {String} taskId 任务ID
 * @returns {Promise} 请求结果
 */
export function getCalculationResult(taskId) {
  return request({
    url: `/glkj/accountant/budget/calculation/task/${taskId}/result`,
    method: 'get'
  })
}

// ==================== 简化函数名导出（兼容前端组件） ====================

// 任务管理相关
export const createTask = createBudgetTask
export const updateTask = updateBudgetTask
export const deleteTask = deleteBudgetTask
export const getTask = getBudgetTask
export const getTaskPage = getBudgetTaskPage
export const startTask = startBudgetTask
export const completeTask = completeBudgetTask
export const cancelTask = (taskId) => request({ url: `/glkj/accountant/budget/task/${taskId}/cancel`, method: 'post' })
export const resetTask = (taskId) => request({ url: `/glkj/accountant/budget/task/${taskId}/pause`, method: 'post' })
// 任务提交/审批/拒绝 - 对应 TaskController 的路径
export const submitTaskApproval = (taskId, comment) => request({ url: `/glkj/accountant/budget/task/submit/${taskId}`, method: 'post', data: comment ? { comment } : {}, headers: JSON_HEADER })
export const approveTaskAction = (taskId, comment) => request({ url: `/glkj/accountant/budget/task/approve/${taskId}`, method: 'post', data: comment ? { comment } : {}, headers: JSON_HEADER })
export const rejectTaskAction = (taskId, comment) => request({ url: `/glkj/accountant/budget/task/reject/${taskId}`, method: 'post', data: comment ? { comment } : {}, headers: JSON_HEADER })

// ==================== 模块API对象定义 ====================

// 数据管理API
export const budgetDataApi = {
  create: (data) => request({ url: '/glkj/accountant/budget/data/create', method: 'post', data, headers: JSON_HEADER }),
  update: (dataId, data) => request({ url: `/glkj/accountant/budget/data/update/${dataId}`, method: 'put', data, headers: JSON_HEADER }),
  delete: (dataId) => request({ url: `/glkj/accountant/budget/data/delete/${dataId}`, method: 'delete' }),
  get: (dataId) => request({ url: `/glkj/accountant/budget/data/detail/${dataId}`, method: 'get' }),
  getPage: (params) => request({ url: '/glkj/accountant/budget/data/page', method: 'post', data: params, headers: JSON_HEADER }),
  getOrganizations: () => request({ url: '/glkj/accountant/budget/data/organizations', method: 'get' }),
  getBudgetAccounts: () => request({ url: '/glkj/accountant/budget/data/accounts', method: 'get' }),
  batchSave: (dataList) => request({ url: '/glkj/accountant/budget/data/batch-save', method: 'post', data: dataList, headers: JSON_HEADER }),
  batchSubmit: (data) => request({ url: '/glkj/accountant/budget/data/batch-submit', method: 'post', data, headers: JSON_HEADER }),
  batchImport: (dataList) => request({ url: '/glkj/accountant/budget/data/import', method: 'post', data: dataList, headers: JSON_HEADER }),
  export: (params) => request({ url: '/glkj/accountant/budget/data/export', method: 'post', data: params, responseType: 'blob', headers: JSON_HEADER }),
  downloadTemplate: () => request({ url: '/glkj/accountant/budget/data/template', method: 'get', responseType: 'blob' }),
  getHistory: (dataId) => request({ url: `/glkj/accountant/budget/data/history/${dataId}`, method: 'get' }),
  getAuditLog: (dataId) => request({ url: `/glkj/accountant/budget/data/audit/${dataId}`, method: 'get' })
}

// 审批流程相关
export const submitForApproval = submitBudgetApproval
export const approveTask = approveBudget
export const rejectTask = rejectBudget

// 调整管理API
export const budgetAdjustmentApi = {
  create: (data) => request({ url: '/glkj/financial/ncv65/budget/adjustment/create', method: 'post', data, headers: JSON_HEADER }),
  update: (data) => request({ url: `/glkj/financial/ncv65/budget/adjustment/update/${data.adjustmentId}`, method: 'put', data, headers: JSON_HEADER }),
  delete: (id) => request({ url: `/glkj/financial/ncv65/budget/adjustment/delete/${id}`, method: 'delete' }),
  get: (id) => request({ url: `/glkj/financial/ncv65/budget/adjustment/detail/${id}`, method: 'get' }),
  getPage: (params) => request({ url: '/glkj/financial/ncv65/budget/adjustment/page', method: 'post', data: params, headers: JSON_HEADER }),
  getStats: () => request({ url: '/glkj/financial/ncv65/budget/adjustment/stats', method: 'get' }),
  getOrganizations: () => request({ url: '/glkj/financial/ncv65/budget/adjustment/organizations', method: 'get' }),
  getBudgetAccounts: () => request({ url: '/glkj/financial/ncv65/budget/adjustment/accounts', method: 'get' }),
  approve: (data) => request({ url: '/glkj/financial/ncv65/budget/adjustment/approve', method: 'post', data, headers: JSON_HEADER }),
  reject: (id, data) => request({ url: `/glkj/financial/ncv65/budget/adjustment/${id}/reject`, method: 'post', data, headers: JSON_HEADER }),
  batchApprove: (ids) => request({ url: '/glkj/financial/ncv65/budget/adjustment/batch-approve', method: 'post', data: ids, headers: JSON_HEADER }),
  cancel: (id) => request({ url: `/glkj/financial/ncv65/budget/adjustment/${id}/cancel`, method: 'post' }),
  execute: (id) => request({ url: `/glkj/financial/ncv65/budget/adjustment/execute/${id}`, method: 'post' }),
  export: (params) => request({ url: '/glkj/financial/ncv65/budget/adjustment/export', method: 'post', data: params, responseType: 'blob', headers: JSON_HEADER }),
  downloadTemplate: () => request({ url: '/glkj/financial/ncv65/budget/adjustment/template', method: 'get', responseType: 'blob' }),
  submit: submitAdjustmentApproval
}

// 预算科目API
export const budgetAccountApi = {
  create: (data) => request({ url: '/glkj/accountant/budget/account/create', method: 'post', data, headers: { 'Content-Type': 'application/json;charset=UTF-8' } }),
  update: (data) => request({ url: `/glkj/accountant/budget/account/update/${data.accountId || data.id}`, method: 'put', data, headers: { 'Content-Type': 'application/json;charset=UTF-8' } }),
  delete: (id) => request({ url: `/glkj/accountant/budget/account/delete/${id}`, method: 'delete' }),
  getPage: (params) => request({ url: '/glkj/accountant/budget/account/page', method: 'post', data: params, headers: { 'Content-Type': 'application/json;charset=UTF-8' } }),
  getAccountTree: () => request({ url: '/glkj/accountant/budget/account/tree', method: 'get' }),
  getParentAccounts: () => request({ url: '/glkj/accountant/budget/account/parents', method: 'get' }),
  getStats: () => request({ url: '/glkj/accountant/budget/account/stats', method: 'get' }),
  updateStatus: (id, isActive) => request({ url: `/glkj/accountant/budget/account/${id}/status`, method: 'put', data: { isActive }, headers: { 'Content-Type': 'application/json;charset=UTF-8' } }),
  batchValidate: (ids) => request({ url: '/glkj/accountant/budget/account/batch-validate', method: 'post', data: ids, headers: { 'Content-Type': 'application/json;charset=UTF-8' } }),
  export: (params) => request({ url: '/glkj/accountant/budget/account/export', method: 'post', data: params, responseType: 'blob', headers: { 'Content-Type': 'application/json;charset=UTF-8' } }),
  exportSingle: (id) => request({ url: `/glkj/accountant/budget/account/${id}/export`, method: 'get', responseType: 'blob' }),
  importAccounts: (formData) => request({ url: '/glkj/accountant/budget/account/import', method: 'post', data: formData, headers: { 'Content-Type': 'multipart/form-data' } }),
  downloadTemplate: () => request({ url: '/glkj/accountant/budget/account/template', method: 'get', responseType: 'blob' })
}

// 预算分配API
export const budgetAllocationApi = {
  create: (data) => request({ url: '/glkj/accountant/budget/allocation/create', method: 'post', data, headers: JSON_HEADER }),
  update: (data) => request({ url: `/glkj/accountant/budget/allocation/update/${data.allocationId}`, method: 'put', data, headers: JSON_HEADER }),
  delete: (id) => request({ url: `/glkj/accountant/budget/allocation/delete/${id}`, method: 'delete' }),
  getPage: (params) => request({ url: '/glkj/accountant/budget/allocation/page', method: 'post', data: params, headers: JSON_HEADER }),
  getStats: () => request({ url: '/glkj/accountant/budget/allocation/stats', method: 'get' }),
  getDimensions: () => request({ url: '/glkj/accountant/budget/allocation/dimensions', method: 'get' }),
  getAllocationTargets: () => request({ url: '/glkj/accountant/budget/allocation/targets', method: 'get' }),
  getTargetsByDimension: (dimension) => request({ url: `/glkj/accountant/budget/allocation/targets/${dimension}`, method: 'get' }),
  confirm: (id) => request({ url: `/glkj/accountant/budget/allocation/${id}/confirm`, method: 'post' }),
  batchConfirm: (ids) => request({ url: '/glkj/accountant/budget/allocation/batch-confirm', method: 'post', data: ids, headers: JSON_HEADER }),
  recalculate: () => request({ url: '/glkj/accountant/budget/allocation/recalculate', method: 'post' }),
  export: (params) => request({ url: '/glkj/accountant/budget/allocation/export', method: 'post', data: params, responseType: 'blob', headers: JSON_HEADER }),
  exportSingle: (id) => request({ url: `/glkj/accountant/budget/allocation/${id}/export`, method: 'get', responseType: 'blob' })
}

// 预算合并API
export const budgetConsolidationApi = {
  create: (data) => request({ url: '/glkj/accountant/budget/consolidation/create', method: 'post', data, headers: JSON_HEADER }),
  update: (data) => request({ url: `/glkj/accountant/budget/consolidation/update/${data.consolidationId}`, method: 'put', data, headers: JSON_HEADER }),
  delete: (id) => request({ url: `/glkj/accountant/budget/consolidation/delete/${id}`, method: 'delete' }),
  getPage: (params) => request({ url: '/glkj/accountant/budget/consolidation/page', method: 'post', data: params, headers: JSON_HEADER }),
  getStats: () => request({ url: '/glkj/accountant/budget/consolidation/stats', method: 'get' }),
  execute: (id) => request({ url: `/glkj/accountant/budget/consolidation/${id}/execute`, method: 'post' }),
  autoConsolidate: () => request({ url: '/glkj/accountant/budget/consolidation/auto', method: 'post' }),
  validate: (id) => request({ url: `/glkj/accountant/budget/consolidation/${id}/validate`, method: 'post' }),
  batchValidate: (ids) => request({ url: '/glkj/accountant/budget/consolidation/batch-validate', method: 'post', data: ids, headers: JSON_HEADER }),
  export: (params) => request({ url: '/glkj/accountant/budget/consolidation/export', method: 'post', data: params, responseType: 'blob', headers: JSON_HEADER }),
  exportSingle: (id) => request({ url: `/glkj/accountant/budget/consolidation/${id}/export`, method: 'get', responseType: 'blob' })
}

// 任务状态统�?
export function countByTaskStatus() {
  return request({
    url: '/glkj/accountant/budget/task/count/status',
    method: 'get'
  })
}

// 获取我创建的任务
export function getMyCreatedTasks(params) {
  return request({
    url: '/glkj/accountant/budget/task/my/created',
    method: 'get',
    params
  })
}

// 获取分配给我的任�?
export function getMyAssignedTasks(params) {
  return request({
    url: '/glkj/accountant/budget/task/my/assigned',
    method: 'get',
    params
  })
}

// 启用任务
export function enableTask(taskId) {
  return request({
    url: `/glkj/accountant/budget/task/${taskId}/enable`,
    method: 'post'
  })
}

// 禁用任务
export function disableTask(taskId) {
  return request({
    url: `/glkj/accountant/budget/task/${taskId}/disable`,
    method: 'post'
  })
}

// 批量删除任务
export function batchDeleteTasks(taskIds) {
  return request({
    url: '/glkj/accountant/budget/task/batch/delete',
    method: 'post',
    data: taskIds,
    headers: JSON_HEADER
  })
}

// 分配任务
export function assignTask(taskId, assigneeId) {
  return request({
    url: `/glkj/accountant/budget/task/${taskId}/assign`,
    method: 'post',
    data: { assigneeId },
    headers: JSON_HEADER
  })
}

// 复制任务
export function copyTask(taskId) {
  return request({
    url: `/glkj/accountant/budget/task/${taskId}/copy`,
    method: 'post'
  })
}

// 检查任务编码是否存�?
export function checkTaskCodeExists(code) {
  return request({
    url: '/glkj/accountant/budget/task/check/code',
    method: 'get',
    params: { code }
  })
}

// 预算公式API
export const budgetFormulaApi = {
  create: (data) => request({ url: '/glkj/accountant/budget/formula', method: 'post', data, headers: JSON_HEADER }),
  update: (data) => request({ url: `/glkj/accountant/budget/formula/${data.formulaId}`, method: 'put', data, headers: JSON_HEADER }),
  delete: (id) => request({ url: `/glkj/accountant/budget/formula/${id}`, method: 'delete' }),
  getPage: (params) => request({ url: '/glkj/accountant/budget/formula/page', method: 'post', data: params, headers: JSON_HEADER }),
  getStats: () => request({ url: '/glkj/accountant/budget/formula/stats', method: 'get' }),
  getCategoryTree: () => request({ url: '/glkj/accountant/budget/formula/category/tree', method: 'get' }),
  getCategories: () => request({ url: '/glkj/accountant/budget/formula/categories', method: 'get' }),
  validateExpression: (expression) => request({ url: '/glkj/accountant/budget/formula/validate/expression', method: 'post', data: { expression }, headers: JSON_HEADER }),
  calculateFormula: (params) => request({ url: '/glkj/accountant/budget/formula/calculate', method: 'post', data: params, headers: JSON_HEADER }),
  validate: (id) => request({ url: `/glkj/accountant/budget/formula/${id}/validate`, method: 'post' }),
  batchValidate: (ids) => request({ url: '/glkj/accountant/budget/formula/batch/validate', method: 'post', data: ids, headers: JSON_HEADER }),
  export: (params) => request({ url: '/glkj/accountant/budget/formula/export', method: 'post', data: params, responseType: 'blob', headers: JSON_HEADER }),
  exportSingle: (id) => request({ url: `/glkj/accountant/budget/formula/${id}/export`, method: 'get', responseType: 'blob' }),
  importFormulas: (data) => request({ url: '/glkj/accountant/budget/formula/import/batch', method: 'post', data, headers: JSON_HEADER })
}

// 预算参数API
const jsonHeader = { 'Content-Type': 'application/json;charset=UTF-8' }
export const budgetParameterApi = {
  create: (data) => request({ url: '/glkj/accountant/budget/parameter/create', method: 'post', data, headers: jsonHeader }),
  update: (data) => request({ url: `/glkj/accountant/budget/parameter/update/${data.parameterId}`, method: 'put', data, headers: jsonHeader }),
  delete: (id) => request({ url: `/glkj/accountant/budget/parameter/delete/${id}`, method: 'delete' }),
  getPage: (params) => request({ url: '/glkj/accountant/budget/parameter/page', method: 'post', data: params, headers: jsonHeader }),
  getCategoryTree: () => request({ url: '/glkj/accountant/budget/parameter/category/tree', method: 'get' }),
  getCategories: () => request({ url: '/glkj/accountant/budget/parameter/categories', method: 'get' }),
  updateStatus: (id, status) => request({ url: `/glkj/accountant/budget/parameter/${id}/status`, method: 'put', data: { parameterStatus: status }, headers: jsonHeader }),
  validateParameter: (params) => request({ url: '/glkj/accountant/budget/parameter/validate', method: 'post', data: params, headers: jsonHeader }),
  resetParameter: (id) => request({ url: `/glkj/accountant/budget/parameter/${id}/reset`, method: 'post' }),
  batchValidate: (ids) => request({ url: '/glkj/accountant/budget/parameter/batch-validate', method: 'post', data: ids, headers: jsonHeader }),
  export: (params) => request({ url: '/glkj/accountant/budget/parameter/export', method: 'post', data: params, headers: jsonHeader, responseType: 'blob' }),
  exportSingle: (id) => request({ url: `/glkj/accountant/budget/parameter/${id}/export`, method: 'get', responseType: 'blob' }),
  importParameters: (formData) => request({ url: '/glkj/accountant/budget/parameter/import', method: 'post', data: formData, headers: { 'Content-Type': 'multipart/form-data' } }),
  downloadTemplate: () => request({ url: '/glkj/accountant/budget/parameter/template', method: 'get', responseType: 'blob' }),
  getById: (id) => request({ url: `/glkj/accountant/budget/parameter/detail/${id}`, method: 'get' })
}

// 预算期间API
export const budgetPeriodApi = {
  create: (data) => request({ url: '/glkj/accountant/budget/period/create', method: 'post', headers: { 'Content-Type': 'application/json' }, data }),
  update: (data) => request({ url: `/glkj/accountant/budget/period/update/${data.periodId}`, method: 'put', headers: { 'Content-Type': 'application/json' }, data }),
  delete: (id) => request({ url: `/glkj/accountant/budget/period/delete/${id}`, method: 'delete' }),
  getPage: (params) => request({ url: '/glkj/accountant/budget/period/page', method: 'post', headers: { 'Content-Type': 'application/json' }, data: params }),
  openPeriod: (id) => request({ url: `/glkj/accountant/budget/period/${id}/open`, method: 'post' }),
  closePeriod: (id) => request({ url: `/glkj/accountant/budget/period/${id}/close`, method: 'post' }),
  updateLockStatus: (id, isLocked) => request({ url: `/glkj/accountant/budget/period/${id}/lock`, method: 'put', headers: { 'Content-Type': 'application/json' }, data: { isLocked } }),
  initYear: (year) => request({ url: '/glkj/accountant/budget/period/init-year', method: 'post', headers: { 'Content-Type': 'application/json' }, data: { year } }),
  closeYear: (year) => request({ url: '/glkj/accountant/budget/period/close-year', method: 'post', headers: { 'Content-Type': 'application/json' }, data: { year } }),
  batchOpen: (ids) => request({ url: '/glkj/accountant/budget/period/batch-open', method: 'post', headers: { 'Content-Type': 'application/json' }, data: ids }),
  batchClose: (ids) => request({ url: '/glkj/accountant/budget/period/batch-close', method: 'post', headers: { 'Content-Type': 'application/json' }, data: ids }),
  setCurrent: (id) => request({ url: `/glkj/accountant/budget/period/${id}/current`, method: 'post' }),
  export: (params) => request({ url: '/glkj/accountant/budget/period/export', method: 'post', headers: { 'Content-Type': 'application/json' }, data: params, responseType: 'blob' }),
  exportSingle: (id) => request({ url: `/glkj/accountant/budget/period/${id}/export`, method: 'get', responseType: 'blob' }),
  getStats: () => request({ url: '/glkj/accountant/budget/period/stats', method: 'get' }),
  getHistory: (periodId) => request({ url: `/glkj/accountant/budget/period/history/list/${periodId}`, method: 'get' }),
  getReport: (periodId) => request({ url: `/glkj/accountant/budget/period/report/data/${periodId}`, method: 'get' }),
  getReportDetail: (params) => request({ url: '/glkj/accountant/budget/period/report/detail', method: 'post', headers: { 'Content-Type': 'application/json' }, data: params })
}

// 预算规则API
export const budgetRuleApi = {
  create: (data) => request({ url: '/glkj/accountant/budget/rule/create', method: 'post', data, headers: JSON_HEADER }),
  update: (data) => request({ url: `/glkj/accountant/budget/rule/update/${data.ruleId}`, method: 'put', data, headers: JSON_HEADER }),
  delete: (id) => request({ url: `/glkj/accountant/budget/rule/delete/${id}`, method: 'delete' }),
  getPage: (params) => request({ url: '/glkj/accountant/budget/rule/page', method: 'post', data: params, headers: JSON_HEADER }),
  getStats: () => request({ url: '/glkj/accountant/budget/rule/stats', method: 'get' }),
  getCategoryTree: () => request({ url: '/glkj/accountant/budget/rule/category/tree', method: 'get' }),
  getCategories: () => request({ url: '/glkj/accountant/budget/rule/categories', method: 'get' }),
  updateStatus: (id, status) => request({ url: `/glkj/accountant/budget/rule/${id}/status`, method: 'put', data: { ruleStatus: status }, headers: JSON_HEADER }),
  validateCondition: (condition) => request({ url: '/glkj/accountant/budget/rule/validate/condition', method: 'post', data: { condition }, headers: JSON_HEADER }),
  testRule: (params) => request({ url: '/glkj/accountant/budget/rule/test', method: 'post', data: params, headers: JSON_HEADER }),
  validate: (id) => request({ url: `/glkj/accountant/budget/rule/${id}/validate`, method: 'post' }),
  batchValidate: (ids) => request({ url: '/glkj/accountant/budget/rule/batch-validate', method: 'post', data: ids, headers: JSON_HEADER }),
  export: (params) => request({ url: '/glkj/accountant/budget/rule/export', method: 'post', data: params, responseType: 'blob', headers: JSON_HEADER }),
  exportSingle: (id) => request({ url: `/glkj/accountant/budget/rule/${id}/export`, method: 'get', responseType: 'blob' })
}


// 预算场景API
export const budgetScenarioApi = {
  create: (data) => request({ url: '/glkj/accountant/budget/scenario/create', method: 'post', data, headers: { 'Content-Type': 'application/json;charset=UTF-8' } }),
  update: (data) => request({ url: `/glkj/accountant/budget/scenario/update/${data.scenarioId}`, method: 'put', data, headers: { 'Content-Type': 'application/json;charset=UTF-8' } }),
  delete: (id) => request({ url: `/glkj/accountant/budget/scenario/delete/${id}`, method: 'delete' }),
  getPage: (params) => request({ url: '/glkj/accountant/budget/scenario/page', method: 'post', data: params, headers: { 'Content-Type': 'application/json;charset=UTF-8' } }),
  detail: (id) => request({ url: `/glkj/accountant/budget/scenario/detail/${id}`, method: 'get' }),
  getBaselineScenarios: () => request({ url: '/glkj/accountant/budget/scenario/baselines', method: 'get' }),
  getAccountOptions: () => request({ url: '/glkj/accountant/budget/scenario/account/options', method: 'get' }),
  calculateScenario: (params) => request({ url: '/glkj/accountant/budget/scenario/calculate', method: 'post', data: params, headers: { 'Content-Type': 'application/json;charset=UTF-8' } }),
  compareScenarios: (params) => request({ url: '/glkj/accountant/budget/scenario/compare', method: 'post', data: params, headers: { 'Content-Type': 'application/json;charset=UTF-8' } }),
  exportCompare: (params) => request({ url: '/glkj/accountant/budget/scenario/compare/export', method: 'post', data: params, responseType: 'blob', headers: { 'Content-Type': 'application/json;charset=UTF-8' } }),
  setBaseline: (id) => request({ url: `/glkj/accountant/budget/scenario/${id}/baseline`, method: 'post' }),
  submitApproval: (id) => request({ url: `/glkj/accountant/budget/scenario/${id}/submit`, method: 'post' }),
  export: (params) => request({ url: '/glkj/accountant/budget/scenario/export', method: 'post', data: params, responseType: 'blob', headers: { 'Content-Type': 'application/json;charset=UTF-8' } }),
  exportSingle: (id) => request({ url: `/glkj/accountant/budget/scenario/${id}/export`, method: 'get', responseType: 'blob' })
}

// 预算模板API
export const budgetTemplateApi = {
  create: (data) => request({ url: '/glkj/accountant/budget/template/create', method: 'post', data, headers: { 'Content-Type': 'application/json;charset=UTF-8' } }),
  update: (data) => request({ url: `/glkj/accountant/budget/template/update/${data.templateId}`, method: 'put', data, headers: { 'Content-Type': 'application/json;charset=UTF-8' } }),
  delete: (id) => request({ url: `/glkj/accountant/budget/template/delete/${id}`, method: 'delete' }),
  copy: (templateId) => request({ url: '/glkj/accountant/budget/template/copy', method: 'post', params: { templateId } }),
  getPage: (params) => request({ url: '/glkj/accountant/budget/template/page', method: 'post', data: params, headers: { 'Content-Type': 'application/json;charset=UTF-8' } }),
  getCategoryTree: () => request({ url: '/glkj/accountant/budget/template/category/tree', method: 'get' }),
  getCategories: () => request({ url: '/glkj/accountant/budget/template/categories', method: 'get' }),
  export: (params) => request({ url: '/glkj/accountant/budget/template/export', method: 'post', data: params, headers: { 'Content-Type': 'application/json;charset=UTF-8' }, responseType: 'blob' }),
  exportSingle: (id) => request({ url: `/glkj/accountant/budget/template/${id}/export`, method: 'get', responseType: 'blob' })
}

// 预算版本API
export const budgetVersionApi = {
  create: (data) => request({ url: '/glkj/accountant/budget/version/create', method: 'post', data, headers: JSON_HEADER }),
  update: (data) => request({ url: `/glkj/accountant/budget/version/update/${data.versionId}`, method: 'put', data, headers: JSON_HEADER }),
  delete: (id) => request({ url: `/glkj/accountant/budget/version/delete/${id}`, method: 'delete' }),
  getPage: (params) => request({ url: '/glkj/accountant/budget/version/page', method: 'post', data: params, headers: JSON_HEADER }),
  getStats: () => request({ url: '/glkj/accountant/budget/version/stats', method: 'get' }),
  getBaseVersions: () => request({ url: '/glkj/accountant/budget/version/base', method: 'get' }),
  activate: (id) => request({ url: `/glkj/accountant/budget/version/${id}/activate`, method: 'post' }),
  compare: (sourceId, targetId) => request({ url: '/glkj/accountant/budget/version/compare', method: 'post', data: { sourceVersionId: sourceId, targetVersionId: targetId }, headers: JSON_HEADER }),
  exportCompare: (sourceId, targetId) => request({ url: '/glkj/accountant/budget/version/compare/export', method: 'post', data: { sourceVersionId: sourceId, targetVersionId: targetId }, responseType: 'blob', headers: JSON_HEADER }),
  merge: (versionIds) => request({ url: '/glkj/accountant/budget/version/merge', method: 'post', data: versionIds, headers: JSON_HEADER }),
  rollback: (id) => request({ url: `/glkj/accountant/budget/version/${id}/rollback`, method: 'post' }),
  archive: (id) => request({ url: `/glkj/accountant/budget/version/${id}/archive`, method: 'post' }),
  export: (id) => request({ url: `/glkj/accountant/budget/version/${id}/export`, method: 'get', responseType: 'blob' })
}

// 预算工作流API
export const budgetWorkflowApi = {
  create: (data) => request({ url: '/glkj/accountant/budget/workflow/create', method: 'post', data, headers: JSON_HEADER }),
  update: (data) => request({ url: `/glkj/accountant/budget/workflow/update/${data.workflowId}`, method: 'put', data, headers: JSON_HEADER }),
  delete: (id) => request({ url: `/glkj/accountant/budget/workflow/delete/${id}`, method: 'delete' }),
  getPage: (params) => request({ url: '/glkj/accountant/budget/workflow/page', method: 'post', data: params, headers: JSON_HEADER }),
  getStats: () => request({ url: '/glkj/accountant/budget/workflow/stats', method: 'get' }),
  getCategoryTree: () => request({ url: '/glkj/accountant/budget/workflow/category/tree', method: 'get' }),
  getCategories: () => request({ url: '/glkj/accountant/budget/workflow/categories', method: 'get' }),
  deploy: (id) => request({ url: `/glkj/accountant/budget/workflow/${id}/deploy`, method: 'post' }),
  batchDeploy: (ids) => request({ url: '/glkj/accountant/budget/workflow/batch-deploy', method: 'post', data: ids, headers: JSON_HEADER }),
  export: (params) => request({ url: '/glkj/accountant/budget/workflow/export', method: 'post', data: params, responseType: 'blob', headers: JSON_HEADER }),
  exportSingle: (id) => request({ url: `/glkj/accountant/budget/workflow/${id}/export`, method: 'get', responseType: 'blob' })
}

// 维度管理API
export const dimensionApi = {
  create: (data) => request({ url: '/glkj/accountant/budget/dimension/create', method: 'post', data, headers: { 'Content-Type': 'application/json;charset=UTF-8' } }),
  update: (data) => request({ url: `/glkj/accountant/budget/dimension/update/${data.dimensionId}`, method: 'put', data, headers: { 'Content-Type': 'application/json;charset=UTF-8' } }),
  delete: (id) => request({ url: `/glkj/accountant/budget/dimension/delete/${id}`, method: 'delete' }),
  getPage: (params) => request({ url: '/glkj/accountant/budget/dimension/page', method: 'post', data: params, headers: { 'Content-Type': 'application/json;charset=UTF-8' } }),
  getStats: () => request({ url: '/glkj/accountant/budget/dimension/stats', method: 'get' }),
  getDimensionTree: () => request({ url: '/glkj/accountant/budget/dimension/tree', method: 'get' }),
  getParentDimensions: () => request({ url: '/glkj/accountant/budget/dimension/parents', method: 'get' }),
  getDetail: (id) => request({ url: `/glkj/accountant/budget/dimension/detail/${id}`, method: 'get' }),
  getMembers: (id) => request({ url: `/glkj/accountant/budget/dimension/members/${id}`, method: 'get' }),
  saveMembers: (dimensionId, members) => request({ url: `/glkj/accountant/budget/dimension/members/${dimensionId}`, method: 'put', data: members, headers: { 'Content-Type': 'application/json;charset=UTF-8' } }),
  importMembers: (dimensionId, formData) => request({ url: `/glkj/accountant/budget/dimension/members/${dimensionId}/import`, method: 'post', data: formData, headers: { 'Content-Type': 'multipart/form-data' } }),
  deleteMember: (memberId) => request({ url: `/glkj/accountant/budget/dimension/members/item/${memberId}`, method: 'delete' }),
  updateStatus: (id, isActive) => request({ url: `/glkj/accountant/budget/dimension/${id}/status`, method: 'put', data: { isActive }, headers: { 'Content-Type': 'application/json;charset=UTF-8' } }),
  batchValidate: (ids) => request({ url: '/glkj/accountant/budget/dimension/batch-validate', method: 'post', data: ids, headers: { 'Content-Type': 'application/json;charset=UTF-8' } }),
  export: (params) => request({ url: '/glkj/accountant/budget/dimension/export', method: 'post', data: params, responseType: 'blob', headers: { 'Content-Type': 'application/json;charset=UTF-8' } }),
  exportSingle: (id) => request({ url: `/glkj/accountant/budget/dimension/${id}/export`, method: 'get', responseType: 'blob' }),
  downloadTemplate: () => request({ url: '/glkj/accountant/budget/dimension/members/template', method: 'get', responseType: 'blob' }),
  importDimensions: (formData) => request({
    url: '/glkj/accountant/budget/dimension/import/dimensions',
    method: 'post',
    data: formData,
    headers: { 'Content-Type': 'multipart/form-data' }
  }),
  downloadDimensionTemplate: () => request({ url: '/glkj/accountant/budget/dimension/template', method: 'get', responseType: 'blob' })
}

// 审批流程API
export const approvalFlowApi = {
  getPage: (params) => request({ url: '/glkj/accountant/budget/approval/page', method: 'post', data: params, headers: JSON_HEADER }),
  getStats: () => request({ url: '/glkj/accountant/budget/approval/statistics', method: 'get' }),
  getApprovers: () => request({ url: '/glkj/accountant/budget/approval/approvers', method: 'get' }),
  approve: (params) => request({ url: '/glkj/accountant/budget/approval/approve', method: 'post', data: params, headers: JSON_HEADER }),
  reject: (params) => request({ url: '/glkj/accountant/budget/approval/reject', method: 'post', data: params, headers: JSON_HEADER }),
  batchApprove: (params) => request({ url: '/glkj/accountant/budget/approval/batch-approve', method: 'post', data: params, headers: JSON_HEADER }),
  batchReject: (params) => request({ url: '/glkj/accountant/budget/approval/batch-reject', method: 'post', data: params, headers: JSON_HEADER }),
  getHistory: (id) => request({ url: `/glkj/accountant/budget/approval/${id}/history`, method: 'get' }),
  delegate: (params) => request({ url: `/glkj/accountant/budget/approval/${params.flowId || params.approvalId}/delegate`, method: 'post', data: params, headers: JSON_HEADER }),
  recall: (id) => request({ url: `/glkj/accountant/budget/approval/${id}/withdraw`, method: 'post' }),
  urge: (id) => request({ url: `/glkj/accountant/budget/approval/${id}/urge`, method: 'post' }),
  export: (params) => request({ url: '/glkj/accountant/budget/approval/export', method: 'post', data: params, responseType: 'blob', headers: JSON_HEADER }),
  submit: submitBudgetApproval,
  withdraw: withdrawBudgetApproval,
  getDetail: getBudgetApprovalDetail,
  create: (data) => request({ url: '/glkj/accountant/budget/approval/create', method: 'post', data, headers: JSON_HEADER }),
  update: (data) => request({ url: '/glkj/accountant/budget/approval/update', method: 'put', data, headers: JSON_HEADER }),
  delete: (id) => request({ url: `/glkj/accountant/budget/approval/delete/${id}`, method: 'delete' })
}

// 首页统计API
export const budgetPreparationApi = {
  getStats: () => request({ url: '/glkj/accountant/budget/preparation/stats', method: 'get' }),
  getTodos: () => request({ url: '/glkj/accountant/budget/preparation/todos', method: 'get' }),
  getProgress: () => request({ url: '/glkj/accountant/budget/preparation/progress', method: 'get' }),
  getActivities: () => request({ url: '/glkj/accountant/budget/preparation/activities', method: 'get' }),
  getChartData: (period) => request({ url: '/glkj/accountant/budget/preparation/chart', method: 'get', params: { period: period || 'month' } }),
  deferTodo: (taskId, dueDate) => request({ url: `/glkj/accountant/budget/preparation/todos/${taskId}/defer`, method: 'post', params: { dueDate } }),
  delegateTodo: (taskId, assignee) => request({ url: `/glkj/accountant/budget/preparation/todos/${taskId}/delegate`, method: 'post', params: { assignee } })
}
// 期间设置API
export const periodSettingApi = {
  getSetting: () => request({ url: '/glkj/accountant/budget/period/setting/get', method: 'get' }),
  save: (data) => request({ url: '/glkj/accountant/budget/period/setting/save', method: 'post', headers: { 'Content-Type': 'application/json' }, data }),
  reset: () => request({ url: '/glkj/accountant/budget/period/setting/reset', method: 'post' })
}

// 预算年度API
export const budgetYearApi = {
  create: (data) => request({ url: '/glkj/accountant/budget/year/create', method: 'post', headers: { 'Content-Type': 'application/json' }, data }),
  update: (data) => request({ url: `/glkj/accountant/budget/year/update/${data.yearId}`, method: 'put', headers: { 'Content-Type': 'application/json' }, data }),
  delete: (id) => request({ url: `/glkj/accountant/budget/year/delete/${id}`, method: 'delete' }),
  getPage: (params) => request({ url: '/glkj/accountant/budget/year/page', method: 'post', headers: { 'Content-Type': 'application/json' }, data: params }),
  getAll: () => request({ url: '/glkj/accountant/budget/year/list', method: 'get' }),
  openYear: (id) => request({ url: `/glkj/accountant/budget/year/${id}/open`, method: 'post' }),
  closeYear: (id) => request({ url: `/glkj/accountant/budget/year/${id}/close`, method: 'post' }),
  setCurrent: (id) => request({ url: `/glkj/accountant/budget/year/${id}/current`, method: 'post' })
}

// 期间历史API
export const periodHistoryApi = {
  getHistory: (periodId) => request({ url: `/glkj/accountant/budget/period/history/list/${periodId}`, method: 'get' })
}

// 期间报告API
export const periodReportApi = {
  getReportData: (periodId) => request({ url: `/glkj/accountant/budget/period/report/data/${periodId}`, method: 'get' }),
  getDetailPage: (params) => request({ url: '/glkj/accountant/budget/period/report/detail', method: 'post', headers: { 'Content-Type': 'application/json' }, data: params }),
  exportReport: (periodId) => request({ url: `/glkj/accountant/budget/period/report/export/${periodId}`, method: 'get', responseType: 'blob' })
}
