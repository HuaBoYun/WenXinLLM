/**
 * NCV65全面预算系统 - 预算体系管理API
 * 
 * @description 预算体系管理功能API接口，包含组织体系、维度管理、指标管理、预算模型等功能
 * @version 1.0.0
 * @author AI Assistant
 * @date 2025-01-08
 * @module budgetSystem
 */

import request from '@/utils/request'

// ==================== 组织体系管理API ====================

/**
 * 创建组织体系
 * @param {Object} data 组织体系数据
 * @returns {Promise} 请求结果
 */
export function createOrganizationStructure(data) {
  return request({
    url: '/glkj/accountant/budget/organization-structure/create',
    method: 'post',
    data
  })
}

/**
 * 查询组织体系详情
 * @param {String} structureId 组织体系ID
 * @returns {Promise} 请求结果
 */
export function getOrganizationStructure(structureId) {
  return request({
    url: `/glkj/accountant/budget/organization-structure/detail/${structureId}`,
    method: 'get'
  })
}

/**
 * 更新组织体系
 * @param {String} structureId 组织体系ID
 * @param {Object} data 更新数据
 * @returns {Promise} 请求结果
 */
export function updateOrganizationStructure(structureId, data) {
  return request({
    url: `/glkj/accountant/budget/organization-structure/update/${structureId}`,
    method: 'put',
    data
  })
}

/**
 * 删除组织体系
 * @param {String} structureId 组织体系ID
 * @returns {Promise} 请求结果
 */
export function deleteOrganizationStructure(structureId) {
  return request({
    url: `/glkj/accountant/budget/organization-structure/delete/${structureId}`,
    method: 'delete'
  })
}

/**
 * 分页查询组织体系列表
 * @param {Number} current 当前�? * @param {Number} size 页大�? * @param {Object} params 查询参数
 * @returns {Promise} 请求结果
 */
export function getOrganizationStructurePage(current, size, params) {
  return request({
    url: '/glkj/accountant/budget/organization-structure/page',
    method: 'post',
    data: { pageNum: current, pageSize: size, ...params }
  })
}

/**
 * 获取组织体系树结�? * @param {String} structureId 组织体系ID
 * @returns {Promise} 请求结果
 */
export function getOrganizationTree(structureId) {
  return request({
    url: `/glkj/accountant/budget/organization-structure/tree/${structureId}`,
    method: 'get'
  })
}

/**
 * 批量删除组织体系
 * @param {Array} ids ID数组
 * @returns {Promise} 请求结果
 */
export function batchDeleteOrganizationStructures(ids) {
  return request({
    url: '/glkj/accountant/budget/organization-structure/batch-delete',
    method: 'delete',
    data: { ids }
  })
}

/**
 * 根据编码查询组织体系
 * @param {String} structureCode 体系编码
 * @returns {Promise} 请求结果
 */
export function getOrganizationStructureByCode(structureCode) {
  return request({
    url: `/glkj/accountant/budget/organization-structure/get-by-code/${structureCode}`,
    method: 'get'
  })
}

/**
 * 获取组织体系统计数据
 * @returns {Promise} 请求结果
 */
export function getOrganizationStructureStats() {
  return request({
    url: '/glkj/accountant/budget/organization-structure/stats',
    method: 'get'
  })
}

// ==================== 维度管理API ====================

/**
 * 创建维度
 * @param {Object} data 维度数据
 * @returns {Promise} 请求结果
 */
export function createDimension(data) {
  return request({
    url: '/glkj/accountant/budget/dimension/create',
    method: 'post',
    data
  })
}

/**
 * 查询维度详情
 * @param {String} dimensionId 维度ID
 * @returns {Promise} 请求结果
 */
export function getDimension(dimensionId) {
  return request({
    url: `/glkj/accountant/budget/dimension/detail/${dimensionId}`,
    method: 'get'
  })
}

/**
 * 更新维度
 * @param {String} dimensionId 维度ID
 * @param {Object} data 更新数据
 * @returns {Promise} 请求结果
 */
export function updateDimension(dimensionId, data) {
  return request({
    url: `/glkj/accountant/budget/dimension/update/${dimensionId}`,
    method: 'put',
    data
  })
}

/**
 * 删除维度
 * @param {String} dimensionId 维度ID
 * @returns {Promise} 请求结果
 */
export function deleteDimension(dimensionId) {
  return request({
    url: `/glkj/accountant/budget/dimension/delete/${dimensionId}`,
    method: 'delete'
  })
}

/**
 * 分页查询维度列表
 * @param {Number} current 当前�? * @param {Number} size 页大�? * @param {Object} params 查询参数
 * @returns {Promise} 请求结果
 */
export function getDimensionPage(current, size, params) {
  return request({
    url: '/glkj/accountant/budget/dimension/page',
    method: 'post',
    data: { pageNum: current, pageSize: size, ...params }
  })
}

/**
 * 获取维度�? * @returns {Promise} 请求结果
 */
export function getDimensionTree() {
  return request({
    url: '/glkj/accountant/budget/dimension/tree',
    method: 'get'
  })
}

/**
 * 获取子维度列�? * @param {String} parentId 父维度ID
 * @returns {Promise} 请求结果
 */
export function getDimensionChildren(parentId) {
  return request({
    url: `/glkj/accountant/budget/dimension/children/${parentId}`,
    method: 'get'
  })
}

/**
 * 获取维度属�? * @param {String} dimensionId 维度ID
 * @returns {Promise} 请求结果
 */
export function getDimensionAttributes(dimensionId) {
  return request({
    url: `/glkj/accountant/budget/dimension/attributes/${dimensionId}`,
    method: 'get'
  })
}

/**
 * 获取维度关联关系
 * @param {String} dimensionId 维度ID
 * @returns {Promise} 请求结果
 */
export function getDimensionRelations(dimensionId) {
  return request({
    url: `/glkj/accountant/budget/dimension/relations/${dimensionId}`,
    method: 'get'
  })
}

/**
 * 批量删除维度
 * @param {Array} ids ID数组
 * @returns {Promise} 请求结果
 */
export function batchDeleteDimensions(ids) {
  return request({
    url: '/glkj/accountant/budget/dimension/batch-delete',
    method: 'delete',
    data: { ids }
  })
}

// ==================== 指标管理API ====================

/**
 * 创建指标
 * @param {Object} data 指标数据
 * @returns {Promise} 请求结果
 */
export function createIndicator(data) {
  return request({
    url: '/glkj/accountant/budget/indicator/create',
    method: 'post',
    data
  })
}

/**
 * 查询指标详情
 * @param {String} indicatorId 指标ID
 * @returns {Promise} 请求结果
 */
export function getIndicator(indicatorId) {
  return request({
    url: `/glkj/accountant/budget/indicator/detail/${indicatorId}`,
    method: 'get'
  })
}

/**
 * 更新指标
 * @param {String} indicatorId 指标ID
 * @param {Object} data 更新数据
 * @returns {Promise} 请求结果
 */
export function updateIndicator(indicatorId, data) {
  return request({
    url: `/glkj/accountant/budget/indicator/update/${indicatorId}`,
    method: 'put',
    data
  })
}

/**
 * 删除指标
 * @param {String} indicatorId 指标ID
 * @returns {Promise} 请求结果
 */
export function deleteIndicator(indicatorId) {
  return request({
    url: `/glkj/accountant/budget/indicator/delete/${indicatorId}`,
    method: 'delete'
  })
}

/**
 * 分页查询指标列表
 * @param {Number} current 当前�? * @param {Number} size 页大�? * @param {Object} params 查询参数
 * @returns {Promise} 请求结果
 */
export function getIndicatorPage(current, size, params) {
  return request({
    url: '/glkj/accountant/budget/indicator/page',
    method: 'post',
    data: { pageNum: current, pageSize: size, ...params }
  })
}

/**
 * 获取指标树结�? * @returns {Promise} 请求结果
 */
export function getIndicatorTree() {
  return request({
    url: '/glkj/accountant/budget/indicator/tree',
    method: 'get'
  })
}

/**
 * 批量删除指标
 * @param {Array} ids ID数组
 * @returns {Promise} 请求结果
 */
export function batchDeleteIndicators(ids) {
  return request({
    url: '/glkj/accountant/budget/indicator/batch-delete',
    method: 'delete',
    data: { ids }
  })
}

// ==================== 预算模型管理API ====================

const BUDGET_MODEL_JSON_HEADER = { 'Content-Type': 'application/json;charset=UTF-8' }

/**
 * 创建预算模型
 * @param {Object} data 模型数据
 * @returns {Promise} 请求结果
 */
export function createBudgetModel(data) {
  return request({
    url: '/glkj/accountant/budget/model/create',
    method: 'post',
    data,
    headers: BUDGET_MODEL_JSON_HEADER
  })
}

/**
 * 查询预算模型详情
 * @param {String} modelId 模型ID
 * @returns {Promise} 请求结果
 */
export function getBudgetModel(modelId) {
  return request({
    url: `/glkj/accountant/budget/model/detail/${modelId}`,
    method: 'get'
  })
}

/**
 * 更新预算模型
 * @param {String} modelId 模型ID
 * @param {Object} data 更新数据
 * @returns {Promise} 请求结果
 */
export function updateBudgetModel(modelId, data) {
  return request({
    url: `/glkj/accountant/budget/model/update/${modelId}`,
    method: 'put',
    data,
    headers: BUDGET_MODEL_JSON_HEADER
  })
}

/**
 * 删除预算模型
 * @param {String} modelId 模型ID
 * @returns {Promise} 请求结果
 */
export function deleteBudgetModel(modelId) {
  return request({
    url: `/glkj/accountant/budget/model/delete/${modelId}`,
    method: 'delete'
  })
}

/**
 * 分页查询预算模型列表
 * @param {Number} current 当前�? * @param {Number} size 页大�? * @param {Object} params 查询参数
 * @returns {Promise} 请求结果
 */
export function getBudgetModelPage(current, size, params) {
  return request({
    url: '/glkj/accountant/budget/model/page',
    method: 'post',
    data: { pageNum: current, pageSize: size, ...params },
    headers: BUDGET_MODEL_JSON_HEADER
  })
}

/**
 * 复制预算模型
 * @param {String} modelId 源模型ID
 * @returns {Promise} 请求结果
 */
export function copyBudgetModel(modelId) {
  return request({
    url: `/glkj/accountant/budget/model/copy/${modelId}`,
    method: 'post',
    data: {},
    headers: BUDGET_MODEL_JSON_HEADER
  })
}

/**
 * 应用预算模型
 * @param {String} modelId 模型ID
 * @returns {Promise} 请求结果
 */
export function applyBudgetModel(modelId) {
  return request({
    url: `/glkj/accountant/budget/model/apply/${modelId}`,
    method: 'post',
    data: {},
    headers: BUDGET_MODEL_JSON_HEADER
  })
}

/**
 * 批量删除预算模型
 * @param {Array} ids ID数组
 * @returns {Promise} 请求结果
 */
export function batchDeleteBudgetModels(ids) {
  return request({
    url: '/glkj/accountant/budget/model/batch-delete',
    method: 'post',
    data: ids,
    headers: BUDGET_MODEL_JSON_HEADER
  })
}

/**
 * 批量复制预算模型
 * @param {Array} ids ID数组
 * @returns {Promise} 请求结果
 */
export function batchCopyBudgetModels(ids) {
  return request({
    url: '/glkj/accountant/budget/model/batch-copy',
    method: 'post',
    data: ids,
    headers: BUDGET_MODEL_JSON_HEADER
  })
}

/**
 * 获取模型统计数据
 * @returns {Promise} 请求结果
 */
export function getBudgetModelStats() {
  return request({
    url: '/glkj/accountant/budget/model/stats',
    method: 'get'
  })
}

/**
 * 获取模型类型统计数据（8种模型类型的数量）
 * @returns {Promise} 请求结果
 */
export function getBudgetModelTypeStats() {
  return request({
    url: '/glkj/accountant/budget/model/type-stats',
    method: 'get'
  })
}

/**
 * @param {String} modelId 模型ID
 * @returns {Promise} 请求结果
 */
export function getBudgetModelVersions(modelId) {
  return request({
    url: `/glkj/accountant/budget/model/versions/${modelId}`,
    method: 'get'
  })
}

/**
 * 获取版本详情
 * @param {String} versionId 版本ID
 * @returns {Promise} 请求结果
 */
export function getBudgetModelVersionDetail(versionId) {
  return request({
    url: `/glkj/accountant/budget/model/version/${versionId}/detail`,
    method: 'get'
  })
}

/**
 * 恢复模型版本
 * @param {String} versionId 版本ID
 * @returns {Promise} 请求结果
 */
export function restoreBudgetModelVersion(versionId) {
  return request({
    url: `/glkj/accountant/budget/model/version/${versionId}/restore`,
    method: 'post',
    data: {},
    headers: BUDGET_MODEL_JSON_HEADER
  })
}

/**
 * 导出预算模型
 * @param {String} modelId 模型ID
 * @returns {Promise} 请求结果
 */
export function exportBudgetModel(modelId) {
  return request({
    url: `/glkj/accountant/budget/model/export/${modelId}`,
    method: 'get',
    responseType: 'blob'
  })
}

/**
 * 导入预算模型
 * @param {FormData} formData 包含文件的FormData对象
 * @returns {Promise} 请求结果
 */
export function importBudgetModel(formData) {
  return request({
    url: '/glkj/accountant/budget/model/import',
    method: 'post',
    data: formData,
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

/**
 * @param {Object} params 查询参数
 * @returns {Promise} 请求结果
 */
export function batchExportBudgetModels(params) {
  return request({
    url: '/glkj/accountant/budget/model/batch-export',
    method: 'post',
    data: params,
    headers: BUDGET_MODEL_JSON_HEADER,
    responseType: 'blob'
  })
}

// ==================== 简化函数名导出（兼容前端组件） ====================

// 组织结构API简化导�?
export const organizationStructureApi = {
  create: createOrganizationStructure,
  update: updateOrganizationStructure,
  delete: deleteOrganizationStructure,
  get: getOrganizationStructure,
  getPage: getOrganizationStructurePage,
  getTree: getOrganizationTree
}

// 维度配置API简化导�?
export const dimensionConfigurationApi = {
  create: createDimension,
  update: updateDimension,
  delete: deleteDimension,
  get: getDimension,
  getPage: getDimensionPage,
  getValues: getDimensionValues,
  batchCreateValues: batchCreateDimensionValues
}

// 指标管理API简化导�?
export const indicatorManagementApi = {
  create: createIndicator,
  update: updateIndicator,
  delete: deleteIndicator,
  get: getIndicator,
  getPage: getIndicatorPage,
  getTree: getIndicatorTree
}

// 预算模型API简化导�?
export const budgetModelApi = {
  create: createBudgetModel,
  update: updateBudgetModel,
  delete: deleteBudgetModel,
  get: getBudgetModel,
  getPage: getBudgetModelPage,
  copy: copyBudgetModel,
  batchDelete: batchDeleteBudgetModels,
  batchCopy: batchCopyBudgetModels,
  export: exportBudgetModel,
  import: importBudgetModel,
  batchExport: batchExportBudgetModels,
  getStats: getBudgetModelStats,
  getTypeStats: getBudgetModelTypeStats
}

// 预算科目API简化导�?
export const budgetSubjectApi = {
  create: createIndicator,
  update: updateIndicator,
  delete: deleteIndicator,
  get: getIndicator,
  getPage: getIndicatorPage,
  getTree: getIndicatorTree
}

// 预算期间API简化导�?
export const budgetPeriodApi = {
  create: createDimension,
  update: updateDimension,
  delete: deleteDimension,
  get: getDimension,
  getPage: getDimensionPage
}

// 预算责任中心API简化导�?
export const responsibilityCenterApi = {
  create: createOrganizationStructure,
  update: updateOrganizationStructure,
  delete: deleteOrganizationStructure,
  get: getOrganizationStructure,
  getPage: getOrganizationStructurePage,
  getTree: getOrganizationTree
}

// 预算版本API简化导�?
export const budgetVersionApi = {
  create: createBudgetModel,
  update: updateBudgetModel,
  delete: deleteBudgetModel,
  get: getBudgetModel,
  getPage: getBudgetModelPage,
  copy: copyBudgetModel
}

// 预算分类API简化导�?
export const budgetCategoryApi = {
  create: createIndicator,
  update: updateIndicator,
  delete: deleteIndicator,
  get: getIndicator,
  getPage: getIndicatorPage,
  getTree: getIndicatorTree
}

// 预算层级API简化导�?
export const budgetHierarchyApi = {
  create: createOrganizationStructure,
  update: updateOrganizationStructure,
  delete: deleteOrganizationStructure,
  get: getOrganizationStructure,
  getPage: getOrganizationStructurePage,
  getTree: getOrganizationTree
}

// 预算权限API简化导�?
export const budgetPermissionApi = {
  create: createOrganizationStructure,
  update: updateOrganizationStructure,
  delete: deleteOrganizationStructure,
  get: getOrganizationStructure,
  getPage: getOrganizationStructurePage
}

// 预算配置API简化导�?
export const budgetConfigApi = {
  create: createBudgetModel,
  update: updateBudgetModel,
  delete: deleteBudgetModel,
  get: getBudgetModel,
  getPage: getBudgetModelPage
}

// 预算体系API简化导�?
export const budgetSystemApi = {
  organizationStructure: organizationStructureApi,
  dimensionConfiguration: dimensionConfigurationApi,
  indicatorManagement: indicatorManagementApi,
  budgetModel: budgetModelApi,
  budgetSubject: budgetSubjectApi,
  budgetPeriod: budgetPeriodApi,
  responsibilityCenter: responsibilityCenterApi,
  budgetVersion: budgetVersionApi,
  budgetCategory: budgetCategoryApi,
  budgetHierarchy: budgetHierarchyApi,
  budgetPermission: budgetPermissionApi,
  budgetConfig: budgetConfigApi,

  // ---- DataIntegration.vue 需要的方法 ----
  getDataIntegrationList: (params) => request({ url: '/glkj/accountant/budget/data-integration/list', method: 'post', data: params || {}, headers: { 'Content-Type': 'application/json' } }),
  getIntegrationStats: () => request({ url: '/glkj/accountant/budget/data-integration/stats', method: 'get' }),
  getSourceTypeStats: () => request({ url: '/glkj/accountant/budget/data-integration/source-type-stats', method: 'get' }),
  getMonitorData: () => request({ url: '/glkj/accountant/budget/data-integration/monitor', method: 'get' }),
  getIntegrationSettings: () => request({ url: '/glkj/accountant/budget/data-integration/settings', method: 'get' }),
  saveIntegrationSettings: (data) => request({ url: '/glkj/accountant/budget/data-integration/settings', method: 'post', data, headers: { 'Content-Type': 'application/json' } }),
  getFieldMappings: (integrationId) => request({ url: `/glkj/accountant/budget/data-integration/${integrationId}/field-mappings`, method: 'get' }),
  saveFieldMappings: (integrationId, data) => request({ url: `/glkj/accountant/budget/data-integration/${integrationId}/field-mappings`, method: 'post', data, headers: { 'Content-Type': 'application/json' } }),
  getSyncLogs: (integrationId) => request({ url: `/glkj/accountant/budget/data-integration/${integrationId}/sync-logs`, method: 'get' }),
  syncDataIntegration: (id) => request({ url: `/glkj/accountant/budget/data-integration/${id}/sync`, method: 'post', data: {}, headers: { 'Content-Type': 'application/json' } }),
  testDataIntegration: (id) => request({ url: `/glkj/accountant/budget/data-integration/${id}/test`, method: 'post', data: {}, headers: { 'Content-Type': 'application/json' } }),
  copyDataIntegration: (id) => request({ url: `/glkj/accountant/budget/data-integration/${id}/copy`, method: 'post', data: {}, headers: { 'Content-Type': 'application/json' } }),
  deleteDataIntegration: (id) => request({ url: `/glkj/accountant/budget/data-integration/${id}`, method: 'delete' }),
  testConnection: (form) => request({ url: '/glkj/accountant/budget/data-integration/test-connection', method: 'post', data: form, headers: { 'Content-Type': 'application/json' } }),
  updateDataIntegration: (form) => request({ url: `/glkj/accountant/budget/data-integration/update/${form.integrationId || form.id}`, method: 'put', data: form, headers: { 'Content-Type': 'application/json' } }),
  createDataIntegration: (form) => request({ url: '/glkj/accountant/budget/data-integration/create', method: 'post', data: form, headers: { 'Content-Type': 'application/json' } }),
  syncAllIntegrations: () => request({ url: '/glkj/accountant/budget/data-integration/sync-all', method: 'post', data: {}, headers: { 'Content-Type': 'application/json' } }),

  // ---- BackupRestore.vue 需要的方法 ----
  getBackupList: (params) => request({ url: '/glkj/accountant/budget/backup/list', method: 'post', data: params || {}, headers: { 'Content-Type': 'application/json' } }),
  getBackupStats: () => request({ url: '/glkj/accountant/budget/backup/stats', method: 'get' }),
  getBackupTypeStats: () => request({ url: '/glkj/accountant/budget/backup/type-stats', method: 'get' }),
  getBackupDetail: (id) => request({ url: `/glkj/accountant/budget/backup/${id}/detail`, method: 'get' }),
  downloadBackup: (id) => request({ url: `/glkj/accountant/budget/backup/${id}/download`, method: 'get', responseType: 'blob' }),
  verifyBackup: (id) => request({ url: `/glkj/accountant/budget/backup/${id}/verify`, method: 'post', data: {}, headers: { 'Content-Type': 'application/json' } }),
  copyBackup: (id) => request({ url: `/glkj/accountant/budget/backup/${id}/copy`, method: 'post', data: {}, headers: { 'Content-Type': 'application/json' } }),
  renameBackup: (id, newName) => request({ url: `/glkj/accountant/budget/backup/${id}/rename`, method: 'put', data: { newName }, headers: { 'Content-Type': 'application/json' } }),
  deleteBackup: (id) => request({ url: `/glkj/accountant/budget/backup/${id}`, method: 'delete' }),
  updateBackup: (data) => request({ url: '/glkj/accountant/budget/backup/update', method: 'put', data, headers: { 'Content-Type': 'application/json' } }),
  createBackup: (form) => request({ url: '/glkj/accountant/budget/backup/create', method: 'post', data: form, headers: { 'Content-Type': 'application/json' } }),
  restoreBackup: (data) => request({ url: '/glkj/accountant/budget/backup/restore', method: 'post', data, headers: { 'Content-Type': 'application/json' } }),
  uploadBackup: (formData) => request({ url: '/glkj/accountant/budget/backup/upload', method: 'post', data: formData, headers: { 'Content-Type': 'multipart/form-data' } }),
  // 备份设置接口
  getBackupSettings: () => request({ url: '/glkj/accountant/budget/backup/settings/list', method: 'get' }),
  saveBackupSetting: (setting) => request({ url: '/glkj/accountant/budget/backup/settings/save', method: 'post', data: setting, headers: { 'Content-Type': 'application/json' } }),
  batchUpdateSettings: (settings) => request({ url: '/glkj/accountant/budget/backup/settings/batch-update', method: 'put', data: settings, headers: { 'Content-Type': 'application/json' } }),
  // 定时备份计划接口
  getScheduleList: (params) => request({ url: '/glkj/accountant/budget/backup/schedule/list', method: 'post', data: params || {}, headers: { 'Content-Type': 'application/json' } }),
  createSchedule: (schedule) => request({ url: '/glkj/accountant/budget/backup/schedule/create', method: 'post', data: schedule, headers: { 'Content-Type': 'application/json' } }),
  updateSchedule: (schedule) => request({ url: '/glkj/accountant/budget/backup/schedule/update', method: 'put', data: schedule, headers: { 'Content-Type': 'application/json' } }),
  deleteSchedule: (id) => request({ url: `/glkj/accountant/budget/backup/schedule/${id}`, method: 'delete' }),
  toggleSchedule: (id) => request({ url: `/glkj/accountant/budget/backup/schedule/${id}/toggle`, method: 'post', data: {}, headers: { 'Content-Type': 'application/json' } }),

  // ---- AuditTrail.vue 需要的方法 ----
  getAuditTrailList: (params) => request({ url: '/glkj/accountant/budget/audit-trail/list', method: 'post', data: params || {}, headers: { 'Content-Type': 'application/json' } }),
  getAuditStats: () => request({ url: '/glkj/accountant/budget/audit-trail/stats', method: 'get' }),
  getAuditDetail: (id) => request({ url: `/glkj/accountant/budget/audit-trail/${id}/detail`, method: 'get' }),
  exportAuditLogs: (params) => request({ url: '/glkj/accountant/budget/audit-trail/export', method: 'post', data: params, headers: { 'Content-Type': 'application/json' }, responseType: 'blob' }),
  cleanupAuditLogs: (params) => request({ url: '/glkj/accountant/budget/audit-trail/cleanup', method: 'post', data: params, headers: { 'Content-Type': 'application/json' } }),
  getLogAnalysis: () => request({ url: '/glkj/accountant/budget/audit-trail/analysis', method: 'get' }),
  getAuditSettings: () => request({ url: '/glkj/accountant/budget/audit-trail/settings', method: 'get' }),
  saveAuditSetting: (setting) => request({ url: '/glkj/accountant/budget/audit-trail/settings/save', method: 'post', data: setting, headers: { 'Content-Type': 'application/json' } }),
  deleteAuditSetting: (settingId) => request({ url: `/glkj/accountant/budget/audit-trail/settings/${settingId}`, method: 'delete' }),
  getAuditAlertList: (params) => request({ url: '/glkj/accountant/budget/audit-trail/alerts/list', method: 'post', data: params, headers: { 'Content-Type': 'application/json' } }),
  getAlertStats: () => request({ url: '/glkj/accountant/budget/audit-trail/alerts/stats', method: 'get' }),
  getPendingAlerts: () => request({ url: '/glkj/accountant/budget/audit-trail/alerts/pending', method: 'get' }),
  handleAlert: (alertId, params) => request({ url: `/glkj/accountant/budget/audit-trail/alerts/${alertId}/handle`, method: 'post', data: params, headers: { 'Content-Type': 'application/json' } }),

  // ---- SystemConfiguration.vue 需要的方法 ----
  getSystemConfigurations: () => request({ url: '/glkj/accountant/budget/system-config/list', method: 'get' }),
  getConfigStats: () => request({ url: '/glkj/accountant/budget/system-config/stats', method: 'get' }),
  saveSystemConfigurations: (configs) => request({ url: '/glkj/accountant/budget/system-config/save', method: 'post', data: configs, headers: { 'Content-Type': 'application/json' } }),
  saveCategoryConfiguration: (category, data) => request({ url: `/glkj/accountant/budget/system-config/save/${category}`, method: 'post', data, headers: { 'Content-Type': 'application/json' } }),
  exportSystemConfigurations: () => request({ url: '/glkj/accountant/budget/system-config/export', method: 'get', responseType: 'blob' }),

  // ---- SystemMonitor.vue 需要的方法 ----
  getSystemStats: () => request({ url: '/glkj/accountant/budget/system-monitor/stats', method: 'get' }),
  getServiceStatus: () => request({ url: '/glkj/accountant/budget/system-monitor/service-status', method: 'get' }),
  getDatabaseStatus: () => request({ url: '/glkj/accountant/budget/system-monitor/database-status', method: 'get' }),
  getJvmStatus: () => request({ url: '/glkj/accountant/budget/system-monitor/jvm-status', method: 'get' }),
  getAlertList: (params) => request({ url: '/glkj/accountant/budget/system-monitor/alerts', method: 'get', params: params || {} }),
  resolveAlert: (id) => request({ url: `/glkj/accountant/budget/system-monitor/alerts/${id}/resolve`, method: 'put', data: {} }),
  exportMonitorReport: () => request({ url: '/glkj/accountant/budget/system-monitor/export', method: 'get', responseType: 'blob' }),
  getPerformanceData: (hours) => request({ url: '/glkj/accountant/budget/system-monitor/performance', method: 'get', params: { hours: hours || 24 } }),
  getNetworkTraffic: (hours) => request({ url: '/glkj/accountant/budget/system-monitor/network-traffic', method: 'get', params: { hours: hours || 24 } }),
  getMonitorSettings: () => request({ url: '/glkj/accountant/budget/system-monitor/settings', method: 'get' }),
  saveMonitorSettings: (data) => request({ url: '/glkj/accountant/budget/system-monitor/settings', method: 'post', data, headers: { 'Content-Type': 'application/json;charset=UTF-8' } }),
  getAlertDetail: (id) => request({ url: `/glkj/accountant/budget/system-monitor/alerts/${id}/detail`, method: 'get' }),

  // ---- PermissionConfiguration.vue 需要的方法 ----
  getRolesList: () => request({ url: '/glkj/accountant/budget/permission/roles', method: 'get' }),
  getPermissionStats: () => request({ url: '/glkj/accountant/budget/permission/stats', method: 'get' }),
  getFunctionPermissions: () => request({ url: '/glkj/accountant/budget/permission/functions', method: 'get' }),
  getOrganizationTree: () => request({ url: '/glkj/accountant/budget/permission/organization-tree', method: 'get' }),
  getRolePermissions: (roleId) => request({ url: `/glkj/accountant/budget/permission/role/${roleId}/permissions`, method: 'get' }),
  getRoleDataPermissions: (roleId) => request({ url: `/glkj/accountant/budget/permission/role/${roleId}/data-permissions`, method: 'get' }),
  getRoleFieldPermissions: (roleId) => request({ url: `/glkj/accountant/budget/permission/role/${roleId}/field-permissions`, method: 'get' }),
  getRoleUsers: (roleId) => request({ url: `/glkj/accountant/budget/permission/role/${roleId}/users`, method: 'get' }),
  copyRole: (id) => request({ url: `/glkj/accountant/budget/permission/role/${id}/copy`, method: 'post', data: {}, headers: { 'Content-Type': 'application/json' } }),
  deleteRole: (id) => request({ url: `/glkj/accountant/budget/permission/role/${id}`, method: 'delete' }),
  updateRole: (form) => request({ url: `/glkj/accountant/budget/permission/role/update/${form.permissionId || form.id}`, method: 'put', data: form, headers: { 'Content-Type': 'application/json' } }),
  createRole: (form) => request({ url: '/glkj/accountant/budget/permission/role/create', method: 'post', data: form, headers: { 'Content-Type': 'application/json' } }),
  saveRolePermissions: (data) => request({ url: '/glkj/accountant/budget/permission/role/save-permissions', method: 'post', data, headers: { 'Content-Type': 'application/json' } }),
  getAvailableUsers: (roleId) => request({ url: `/glkj/accountant/budget/permission/role/${roleId}/available-users`, method: 'get' }),
  assignUsersToRole: (roleId, userIds) => request({ url: `/glkj/accountant/budget/permission/role/${roleId}/assign-users`, method: 'post', data: { userIds }, headers: { 'Content-Type': 'application/json' } }),
  removeUserFromRole: (roleId, userId) => request({ url: `/glkj/accountant/budget/permission/role/${roleId}/remove-user/${userId}`, method: 'delete' }),
  batchRemoveUsersFromRole: (roleId, userIds) => request({ url: `/glkj/accountant/budget/permission/role/${roleId}/batch-remove-users`, method: 'post', data: { userIds }, headers: { 'Content-Type': 'application/json' } }),
  exportPermissions: () => request({ url: '/glkj/accountant/budget/permission/export', method: 'get', responseType: 'blob' }),
  importPermissions: (formData) => request({ url: '/glkj/accountant/budget/permission/import', method: 'post', data: formData, headers: { 'Content-Type': 'multipart/form-data' } }),

  // ---- BudgetModel.vue 需要的方法 ----
  restoreBudgetModelVersion: (versionId) => request({ url: `/glkj/accountant/budget/model/version/${versionId}/restore`, method: 'post', data: {} }),

  // ---- 预算体系矩阵数据 ----
  getSystemMatrix: () => request({ url: '/glkj/accountant/budget/system/matrix', method: 'get' }),
}

// 指标管理相关函数
export function enableIndicator(indicatorId) {
  return request({
    url: `/glkj/accountant/budget/indicator/enable/${indicatorId}`,
    method: 'put'
  })
}

export function disableIndicator(indicatorId) {
  return request({
    url: `/glkj/accountant/budget/indicator/disable/${indicatorId}`,
    method: 'put'
  })
}

export function getFullIndicatorTree() {
  return request({
    url: '/glkj/accountant/budget/indicator/tree',
    method: 'get'
  })
}

export function validateFormula(formula) {
  return request({
    url: '/glkj/accountant/budget/indicator/validate-formula',
    method: 'post',
    data: { formula }
  })
}

// 组织体系相关扩展函数
export function enableOrganizationStructure(structureId) {
  return request({
    url: `/glkj/accountant/budget/organization-structure/enable/${structureId}`,
    method: 'put'
  })
}

export function disableOrganizationStructure(structureId) {
  return request({
    url: `/glkj/accountant/budget/organization-structure/disable/${structureId}`,
    method: 'put'
  })
}

export function batchUpdateEnabled(ids, isEnabled) {
  return request({
    url: '/glkj/accountant/budget/organization-structure/batch-update-enabled',
    method: 'put',
    data: { ids, isEnabled }
  })
}

export function copyOrganizationStructure(structureId) {
  return request({
    url: `/glkj/accountant/budget/organization-structure/copy/${structureId}`,
    method: 'post'
  })
}

export function checkStructureCodeExists(structureCode) {
  return request({
    url: `/glkj/accountant/budget/organization-structure/check-code/${structureCode}`,
    method: 'get'
  })
}

// 维度配置相关扩展函数
export function getDimensionValues(dimensionId) {
  return request({
    url: `/glkj/accountant/budget/dimension/values/${dimensionId}`,
    method: 'get'
  })
}

export function batchCreateDimensionValues(dimensionId, values) {
  return request({
    url: `/glkj/accountant/budget/dimension/values/batch/${dimensionId}`,
    method: 'post',
    data: values
  })
}
