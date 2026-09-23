/**
 * NCV65全面预算系统 - 系统集成API
 * 
 * @description 系统集成功能API接口，包含ERP集成、BI集成、第三方系统集成等功�?
 * @version 1.0.0
 * @author AI Assistant
 * @date 2025-01-08
 * @module systemIntegration
 */

import request from '@/utils/request'

// ==================== ERP系统集成API ====================

/**
 * 创建ERP集成配置
 * @param {Object} data 集成配置数据
 * @returns {Promise} 请求结果
 */
export function createERPIntegrationConfig(data) {
  return request({
    url: '/glkj/accountant/budget/erp/create',
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

/**
 * 查询ERP集成配置详情
 * @param {String} configId 配置ID
 * @returns {Promise} 请求结果
 */
export function getERPIntegrationConfig(configId) {
  return request({
    url: `/glkj/accountant/budget/erp/detail/${configId}`,
    method: 'get'
  })
}

/**
 * 更新ERP集成配置
 * @param {String} configId 配置ID
 * @param {Object} data 更新数据
 * @returns {Promise} 请求结果
 */
export function updateERPIntegrationConfig(configId, data) {
  return request({
    url: `/glkj/accountant/budget/erp/update/${configId}`,
    method: 'put',
    data,
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

/**
 * 删除ERP集成配置
 * @param {String} configId 配置ID
 * @returns {Promise} 请求结果
 */
export function deleteERPIntegrationConfig(configId) {
  return request({
    url: `/glkj/accountant/budget/erp/delete/${configId}`,
    method: 'delete'
  })
}

/**
 * 分页查询ERP集成配置列表
 * @param {Number} current 当前�?
 * @param {Number} size 页大�?
 * @param {Object} params 查询参数
 * @returns {Promise} 请求结果
 */
export function getERPIntegrationConfigPage(current, size, params) {
  return request({
    url: '/glkj/accountant/budget/erp/page',
    method: 'post',
    data: { pageNum: current, pageSize: size, ...params },
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

/**
 * 测试ERP连接
 * @param {String} configId 配置ID
 * @returns {Promise} 请求结果
 */
export function testERPConnection(configId) {
  return request({
    url: `/glkj/accountant/budget/erp/test/${configId}`,
    method: 'post'
  })
}

/**
 * 同步ERP数据
 * @param {String} configId 配置ID
 * @param {Object} syncParams 同步参数
 * @returns {Promise} 请求结果
 */
export function syncERPData(configId, syncParams) {
  return request({
    url: `/glkj/accountant/budget/erp/sync/${configId}`,
    method: 'post',
    data: syncParams,
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

/**
 * 获取ERP同步日志
 * @param {String} configId 配置ID
 * @param {Number} current 当前�?
 * @param {Number} size 页大�?
 * @returns {Promise} 请求结果
 */
export function getERPSyncLog(configId, current, size) {
  return request({
    url: `/glkj/accountant/budget/erp/history/${configId}`,
    method: 'get'
  })
}

// ==================== BI系统集成API ====================

/**
 * 创建BI集成配置
 * @param {Object} data 集成配置数据
 * @returns {Promise} 请求结果
 */
export function createBIIntegrationConfig(data) {
  return request({
    url: '/glkj/accountant/integration/bi/config',
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

/**
 * 查询BI集成配置详情
 * @param {String} configId 配置ID
 * @returns {Promise} 请求结果
 */
export function getBIIntegrationConfig(configId) {
  return request({
    url: `/glkj/accountant/integration/bi/config/${configId}`,
    method: 'get'
  })
}

/**
 * 更新BI集成配置
 * @param {String} configId 配置ID
 * @param {Object} data 更新数据
 * @returns {Promise} 请求结果
 */
export function updateBIIntegrationConfig(configId, data) {
  return request({
    url: `/glkj/accountant/integration/bi/config/${configId}`,
    method: 'put',
    data,
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

/**
 * 删除BI集成配置
 * @param {String} configId 配置ID
 * @returns {Promise} 请求结果
 */
export function deleteBIIntegrationConfig(configId) {
  return request({
    url: `/glkj/accountant/integration/bi/config/${configId}`,
    method: 'delete'
  })
}

/**
 * 分页查询BI集成配置列表
 * @param {Number} current 当前�?
 * @param {Number} size 页大�?
 * @param {Object} params 查询参数
 * @returns {Promise} 请求结果
 */
export function getBIIntegrationConfigPage(current, size, params) {
  return request({
    url: '/glkj/accountant/integration/bi/config/page',
    method: 'post',
    params: { current, size },
    data: params,
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

/**
 * 测试BI连接
 * @param {String} configId 配置ID
 * @returns {Promise} 请求结果
 */
export function testBIConnection(configId) {
  return request({
    url: `/glkj/accountant/integration/bi/config/${configId}/test-connection`,
    method: 'post'
  })
}

/**
 * 推送数据到BI系统
 * @param {String} configId 配置ID
 * @param {Object} pushParams 推送参�?
 * @returns {Promise} 请求结果
 */
export function pushDataToBI(configId, pushParams) {
  return request({
    url: `/glkj/accountant/integration/bi/config/${configId}/push`,
    method: 'post',
    data: pushParams,
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

// ==================== 第三方系统集成API ====================

/**
 * 创建第三方系统集成配�?
 * @param {Object} data 集成配置数据
 * @returns {Promise} 请求结果
 */
export function createThirdPartyIntegrationConfig(data) {
  return request({
    url: '/glkj/accountant/integration/third-party/config',
    method: 'post',
    data
  })
}

/**
 * 查询第三方系统集成配置详�?
 * @param {String} configId 配置ID
 * @returns {Promise} 请求结果
 */
export function getThirdPartyIntegrationConfig(configId) {
  return request({
    url: `/glkj/accountant/integration/third-party/config/${configId}`,
    method: 'get'
  })
}

/**
 * 更新第三方系统集成配�?
 * @param {String} configId 配置ID
 * @param {Object} data 更新数据
 * @returns {Promise} 请求结果
 */
export function updateThirdPartyIntegrationConfig(configId, data) {
  return request({
    url: `/glkj/accountant/integration/third-party/config/${configId}`,
    method: 'put',
    data
  })
}

/**
 * 删除第三方系统集成配�?
 * @param {String} configId 配置ID
 * @returns {Promise} 请求结果
 */
export function deleteThirdPartyIntegrationConfig(configId) {
  return request({
    url: `/glkj/accountant/integration/third-party/config/${configId}`,
    method: 'delete'
  })
}

/**
 * 分页查询第三方系统集成配置列�?
 * @param {Number} current 当前�?
 * @param {Number} size 页大�?
 * @param {Object} params 查询参数
 * @returns {Promise} 请求结果
 */
export function getThirdPartyIntegrationConfigPage(current, size, params) {
  return request({
    url: '/glkj/accountant/integration/third-party/config/page',
    method: 'post',
    params: { current, size },
    data: params
  })
}

/**
 * 测试第三方系统连�?
 * @param {String} configId 配置ID
 * @returns {Promise} 请求结果
 */
export function testThirdPartyConnection(configId) {
  return request({
    url: `/glkj/accountant/integration/third-party/config/${configId}/test-connection`,
    method: 'post'
  })
}

// ==================== 数据映射API ====================

/**
 * 创建数据映射配置
 * @param {Object} data 映射配置数据
 * @returns {Promise} 请求结果
 */
export function createDataMapping(data) {
  return request({
    url: '/glkj/accountant/integration/data-mapping',
    method: 'post',
    data
  })
}

/**
 * 查询数据映射配置详情
 * @param {String} mappingId 映射ID
 * @returns {Promise} 请求结果
 */
export function getDataMapping(mappingId) {
  return request({
    url: `/glkj/accountant/integration/data-mapping/${mappingId}`,
    method: 'get'
  })
}

/**
 * 更新数据映射配置
 * @param {String} mappingId 映射ID
 * @param {Object} data 更新数据
 * @returns {Promise} 请求结果
 */
export function updateDataMapping(mappingId, data) {
  return request({
    url: `/glkj/accountant/integration/data-mapping/${mappingId}`,
    method: 'put',
    data
  })
}

/**
 * 删除数据映射配置
 * @param {String} mappingId 映射ID
 * @returns {Promise} 请求结果
 */
export function deleteDataMapping(mappingId) {
  return request({
    url: `/glkj/accountant/integration/data-mapping/${mappingId}`,
    method: 'delete'
  })
}

/**
 * 分页查询数据映射配置列表
 * @param {Number} current 当前�?
 * @param {Number} size 页大�?
 * @param {Object} params 查询参数
 * @returns {Promise} 请求结果
 */
export function getDataMappingPage(current, size, params) {
  return request({
    url: '/glkj/accountant/integration/data-mapping/page',
    method: 'post',
    params: { current, size },
    data: params
  })
}

/**
 * 测试数据映射
 * @param {String} mappingId 映射ID
 * @param {Object} testData 测试数据
 * @returns {Promise} 请求结果
 */
export function testDataMapping(mappingId, testData) {
  return request({
    url: `/glkj/accountant/integration/data-mapping/${mappingId}/test`,
    method: 'post',
    data: testData
  })
}

// ==================== 集成监控API ====================

/**
 * 获取集成状态概�?
 * @returns {Promise} 请求结果
 */
export function getIntegrationStatusOverview() {
  return request({
    url: '/glkj/accountant/integration/status/overview',
    method: 'get'
  })
}

/**
 * 分页查询集成日志
 * @param {Number} current 当前�?
 * @param {Number} size 页大�?
 * @param {Object} params 查询参数
 * @returns {Promise} 请求结果
 */
export function getIntegrationLogPage(current, size, params) {
  return request({
    url: '/glkj/accountant/integration/log/page',
    method: 'get',
    params: { current, size, ...params }
  })
}

/**
 * 查询集成日志详情
 * @param {String} logId 日志ID
 * @returns {Promise} 请求结果
 */
export function getIntegrationLogDetail(logId) {
  return request({
    url: `/glkj/accountant/integration/log/${logId}`,
    method: 'get'
  })
}

// ==================== 简化函数名导出（兼容前端组件） ====================

// ERP集成API简化导�?
export const erpIntegrationApi = {
  create: createERPIntegrationConfig,
  update: updateERPIntegrationConfig,
  delete: deleteERPIntegrationConfig,
  get: getERPIntegrationConfig,
  getPage: getERPIntegrationConfigPage,
  test: testERPConnection,
  sync: syncERPData,
  getSyncLog: getERPSyncLog
}

// BI报表管理
export function getBIReports(biId) {
  return request({
    url: `/glkj/accountant/integration/bi/${biId}/reports`,
    method: 'get'
  })
}

export function createBIReport(biId, data) {
  return request({
    url: `/glkj/accountant/integration/bi/${biId}/reports`,
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

export function deleteBIReport(reportId) {
  return request({
    url: `/glkj/accountant/integration/bi/reports/${reportId}`,
    method: 'delete'
  })
}

// BI数据集管理
export function getBIDatasets(biId) {
  return request({
    url: `/glkj/accountant/integration/bi/${biId}/datasets`,
    method: 'get'
  })
}

export function createBIDataset(biId, data) {
  return request({
    url: `/glkj/accountant/integration/bi/${biId}/datasets`,
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

export function deleteBIDataset(datasetId) {
  return request({
    url: `/glkj/accountant/integration/bi/datasets/${datasetId}`,
    method: 'delete'
  })
}

// BI权限管理
export function getBIPermissions(biId) {
  return request({
    url: `/glkj/accountant/integration/bi/${biId}/permissions`,
    method: 'get'
  })
}

export function createBIPermission(biId, data) {
  return request({
    url: `/glkj/accountant/integration/bi/${biId}/permissions`,
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

export function deleteBIPermission(permissionId) {
  return request({
    url: `/glkj/accountant/integration/bi/permissions/${permissionId}`,
    method: 'delete'
  })
}

// BI集成API简化导�?
export const biIntegrationApi = {
  create: createBIIntegrationConfig,
  update: updateBIIntegrationConfig,
  delete: deleteBIIntegrationConfig,
  get: getBIIntegrationConfig,
  getPage: getBIIntegrationConfigPage,
  test: testBIConnection,
  push: pushDataToBI,
  getReports: getBIReports,
  createReport: createBIReport,
  deleteReport: deleteBIReport,
  getDatasets: getBIDatasets,
  createDataset: createBIDataset,
  deleteDataset: deleteBIDataset,
  getPermissions: getBIPermissions,
  createPermission: createBIPermission,
  deletePermission: deleteBIPermission
}

// API接口集成API简化导出
export const apiIntegrationApi = {
  create: (data) => request({ url: '/glkj/accountant/integration/api/config', method: 'post', data, headers: { 'Content-Type': 'application/json;charset=UTF-8' } }),
  update: (id, data) => request({ url: `/glkj/accountant/integration/api/config/${id}`, method: 'put', data, headers: { 'Content-Type': 'application/json;charset=UTF-8' } }),
  delete: (id) => request({ url: `/glkj/accountant/integration/api/config/${id}`, method: 'delete' }),
  test: (id) => request({ url: `/glkj/accountant/integration/api/config/${id}/test`, method: 'post' }),
  copy: (id) => request({ url: `/glkj/accountant/integration/api/config/${id}/copy`, method: 'post' }),
  getLogs: (apiId) => request({ url: `/glkj/accountant/integration/api/${apiId}/logs`, method: 'get' }),
  getMonitor: (apiId) => request({ url: `/glkj/accountant/integration/api/${apiId}/monitor`, method: 'get' }),
  getDoc: (apiId) => request({ url: `/glkj/accountant/integration/api/${apiId}/doc`, method: 'get' }),
  getSettings: () => request({ url: '/glkj/accountant/integration/api/settings', method: 'get' }),
  updateSettings: (data) => request({ url: '/glkj/accountant/integration/api/settings', method: 'put', data, headers: { 'Content-Type': 'application/json;charset=UTF-8' } })
}

// 数据库集成API
export const databaseIntegrationApi = {
  create: (data) => request({ url: '/glkj/accountant/integration/database/config', method: 'post', data, headers: { 'Content-Type': 'application/json;charset=UTF-8' } }),
  update: (id, data) => request({ url: `/glkj/accountant/integration/database/config/${id}`, method: 'put', data, headers: { 'Content-Type': 'application/json;charset=UTF-8' } }),
  delete: (id) => request({ url: `/glkj/accountant/integration/database/config/${id}`, method: 'delete' }),
  test: (id) => request({ url: `/glkj/accountant/integration/database/config/${id}/test-connection`, method: 'post' }),
  sync: (id) => request({ url: `/glkj/accountant/integration/database/config/${id}/sync`, method: 'post' }),
  syncLogs: () => request({ url: '/glkj/accountant/integration/database/sync-logs', method: 'get' }),
  tables: (id) => request({ url: `/glkj/accountant/integration/database/config/${id}/tables`, method: 'get' }),
  query: (id, data) => request({ url: `/glkj/accountant/integration/database/config/${id}/query`, method: 'post', data, headers: { 'Content-Type': 'application/json;charset=UTF-8' } }),
  performance: (id) => request({ url: `/glkj/accountant/integration/database/config/${id}/performance`, method: 'get' }),
  settings: () => request({ url: '/glkj/accountant/integration/database/settings', method: 'get' }),
  updateSettings: (data) => request({ url: '/glkj/accountant/integration/database/settings', method: 'put', data, headers: { 'Content-Type': 'application/json;charset=UTF-8' } })
}

// 文件集成API
export const fileIntegrationApi = {
  upload: (formData) => request({ url: '/glkj/accountant/integration/file/upload', method: 'post', data: formData, headers: { 'Content-Type': 'multipart/form-data' } }),
  delete: (id) => request({ url: `/glkj/accountant/integration/file/${id}`, method: 'delete' }),
  reprocess: (id) => request({ url: `/glkj/accountant/integration/file/${id}/reprocess`, method: 'post' }),
  getLogs: (fileId) => request({ url: `/glkj/accountant/integration/file/${fileId}/logs`, method: 'get' }),
  getErrors: (fileId) => request({ url: `/glkj/accountant/integration/file/${fileId}/errors`, method: 'get' })
}

// 消息队列集成API
export const messageQueueIntegrationApi = {
  create: (data) => request({ url: '/glkj/accountant/integration/mq/config', method: 'post', data, headers: { 'Content-Type': 'application/json;charset=UTF-8' } }),
  update: (id, data) => request({ url: `/glkj/accountant/integration/mq/config/${id}`, method: 'put', data, headers: { 'Content-Type': 'application/json;charset=UTF-8' } }),
  delete: (id) => request({ url: `/glkj/accountant/integration/mq/config/${id}`, method: 'delete' }),
  start: (id) => request({ url: `/glkj/accountant/integration/mq/config/${id}/start`, method: 'post' }),
  stop: (id) => request({ url: `/glkj/accountant/integration/mq/config/${id}/stop`, method: 'post' }),
  purge: (id) => request({ url: `/glkj/accountant/integration/mq/config/${id}/purge`, method: 'post' }),
  test: (id) => request({ url: `/glkj/accountant/integration/mq/config/${id}/test`, method: 'post' }),
  sendMessage: (id, data) => request({ url: `/glkj/accountant/integration/mq/config/${id}/send`, method: 'post', data, headers: { 'Content-Type': 'application/json;charset=UTF-8' } }),
  resendMessage: (messageId) => request({ url: `/glkj/accountant/integration/mq/message/${messageId}/resend`, method: 'post' }),
  getMessages: (id) => request({ url: `/glkj/accountant/integration/mq/config/${id}/messages`, method: 'get' }),
  getConsumers: (id) => request({ url: `/glkj/accountant/integration/mq/config/${id}/consumers`, method: 'get' }),
  getMonitor: (id) => request({ url: `/glkj/accountant/integration/mq/config/${id}/monitor`, method: 'get' })
}

// Web服务集成API
export const webServiceIntegrationApi = {
  create: (data) => request({ url: '/glkj/accountant/integration/webservice/config', method: 'post', data, headers: { 'Content-Type': 'application/json;charset=UTF-8' } }),
  update: (id, data) => request({ url: `/glkj/accountant/integration/webservice/config/${id}`, method: 'put', data, headers: { 'Content-Type': 'application/json;charset=UTF-8' } }),
  delete: (id) => request({ url: `/glkj/accountant/integration/webservice/config/${id}`, method: 'delete' }),
  test: (id) => request({ url: `/glkj/accountant/integration/webservice/config/${id}/test`, method: 'post' }),
  invoke: (id, data) => request({ url: `/glkj/accountant/integration/webservice/config/${id}/invoke`, method: 'post', data })
}

// 云平台集成API
export const cloudIntegrationApi = {
  create: (data) => request({ url: '/glkj/accountant/integration/cloud/config', method: 'post', data, headers: { 'Content-Type': 'application/json;charset=UTF-8' } }),
  update: (id, data) => request({ url: `/glkj/accountant/integration/cloud/config/${id}`, method: 'put', data, headers: { 'Content-Type': 'application/json;charset=UTF-8' } }),
  delete: (id) => request({ url: `/glkj/accountant/integration/cloud/config/${id}`, method: 'delete' }),
  test: (id) => request({ url: `/glkj/accountant/integration/cloud/config/${id}/test-connection`, method: 'post' }),
  sync: (id) => request({ url: `/glkj/accountant/integration/cloud/config/${id}/sync`, method: 'post' })
}

// 数据流集成API - 后端使用@RequestBody接收JSON，需覆盖全局form-urlencoded配置
const jsonHeaders = { 'Content-Type': 'application/json;charset=UTF-8' }
export const dataStreamIntegrationApi = {
  create: (data) => request({ url: '/glkj/accountant/integration/datastream/config', method: 'post', data, headers: jsonHeaders }),
  update: (id, data) => request({ url: `/glkj/accountant/integration/datastream/config/${id}`, method: 'put', data, headers: jsonHeaders }),
  delete: (id) => request({ url: `/glkj/accountant/integration/datastream/config/${id}`, method: 'delete' }),
  start: (id) => request({ url: `/glkj/accountant/integration/datastream/config/${id}/start`, method: 'post' }),
  stop: (id) => request({ url: `/glkj/accountant/integration/datastream/config/${id}/stop`, method: 'post' }),
  restart: (id) => request({ url: `/glkj/accountant/integration/datastream/config/${id}/restart`, method: 'post' }),
  validate: (data) => request({ url: '/glkj/accountant/integration/datastream/config/validate', method: 'post', data, headers: jsonHeaders }),
  getLogs: (id) => request({ url: `/glkj/accountant/integration/datastream/config/${id}/logs`, method: 'get' }),
  getMonitor: (id) => request({ url: `/glkj/accountant/integration/datastream/config/${id}/monitor`, method: 'get' })
}

// 数据映射API
export const dataMappingApi = {
  create: (data) => request({ url: '/glkj/accountant/integration/mapping/config', method: 'post', data, headers: { 'Content-Type': 'application/json;charset=UTF-8' } }),
  update: (id, data) => request({ url: `/glkj/accountant/integration/mapping/config/${id}`, method: 'put', data, headers: { 'Content-Type': 'application/json;charset=UTF-8' } }),
  delete: (id) => request({ url: `/glkj/accountant/integration/mapping/config/${id}`, method: 'delete' }),
  test: (id) => request({ url: `/glkj/accountant/integration/mapping/config/${id}/test`, method: 'post' }),
  execute: (id) => request({ url: `/glkj/accountant/integration/mapping/config/${id}/execute`, method: 'post' }),
  copy: (id) => request({ url: `/glkj/accountant/integration/mapping/config/${id}/copy`, method: 'post' }),
  exportMapping: (id) => request({ url: `/glkj/accountant/integration/mapping/config/${id}/export`, method: 'get' }),
  getLogs: (id) => request({ url: `/glkj/accountant/integration/mapping/config/${id}/logs`, method: 'get' }),
  importMapping: (data) => request({ url: '/glkj/accountant/integration/mapping/import', method: 'post', data, headers: { 'Content-Type': 'application/json;charset=UTF-8' } }),
  getSettings: () => request({ url: '/glkj/accountant/integration/mapping/settings', method: 'get' }),
  saveSettings: (data) => request({ url: '/glkj/accountant/integration/mapping/settings', method: 'post', data, headers: { 'Content-Type': 'application/json;charset=UTF-8' } }),
  batchTest: (ids) => request({ url: '/glkj/accountant/integration/mapping/batch-test', method: 'post', data: { ids }, headers: { 'Content-Type': 'application/json;charset=UTF-8' } })
}

// 集成配置API - 后端使用@RequestBody接收JSON，需覆盖全局form-urlencoded配置
export const integrationConfigApi = {
  create: (data) => request({ url: '/glkj/accountant/integration/config/config', method: 'post', data, headers: jsonHeaders }),
  update: (id, data) => request({ url: `/glkj/accountant/integration/config/config/${id}`, method: 'put', data, headers: jsonHeaders }),
  delete: (id) => request({ url: `/glkj/accountant/integration/config/config/${id}`, method: 'delete' }),
  test: (id) => request({ url: `/glkj/accountant/integration/config/config/${id}/test`, method: 'post' }),
  apply: (id) => request({ url: `/glkj/accountant/integration/config/config/${id}/apply`, method: 'post' }),
  rollback: (id, version) => request({ url: `/glkj/accountant/integration/config/config/${id}/rollback`, method: 'post', data: { version }, headers: jsonHeaders }),
  compare: (id, version) => request({ url: `/glkj/accountant/integration/config/config/${id}/compare`, method: 'post', data: { version }, headers: jsonHeaders }),
  getGlobalSettings: () => request({ url: '/glkj/accountant/integration/config/global-settings', method: 'get' }),
  saveGlobalSettings: (data) => request({ url: '/glkj/accountant/integration/config/global-settings', method: 'post', data, headers: jsonHeaders }),
  importConfig: (data) => request({ url: '/glkj/accountant/integration/config/import', method: 'post', data, headers: jsonHeaders }),
  exportConfig: (params) => request({ url: '/glkj/accountant/integration/config/export', method: 'get', params }),
  batchTest: (ids) => request({ url: '/glkj/accountant/integration/config/batch-test', method: 'post', data: { ids }, headers: jsonHeaders }),
  // 参数管理
  getParams: (configId) => request({ url: `/glkj/accountant/integration/config/${configId}/params`, method: 'get' }),
  addParam: (configId, data) => request({ url: `/glkj/accountant/integration/config/${configId}/params`, method: 'post', data, headers: jsonHeaders }),
  updateParam: (configId, paramId, data) => request({ url: `/glkj/accountant/integration/config/${configId}/params/${paramId}`, method: 'put', data, headers: jsonHeaders }),
  deleteParam: (configId, paramId) => request({ url: `/glkj/accountant/integration/config/${configId}/params/${paramId}`, method: 'delete' }),
  // 规则管理
  getRules: (configId) => request({ url: `/glkj/accountant/integration/config/${configId}/rules`, method: 'get' }),
  addRule: (configId, data) => request({ url: `/glkj/accountant/integration/config/${configId}/rules`, method: 'post', data, headers: jsonHeaders }),
  updateRule: (configId, ruleId, data) => request({ url: `/glkj/accountant/integration/config/${configId}/rules/${ruleId}`, method: 'put', data, headers: jsonHeaders }),
  deleteRule: (configId, ruleId) => request({ url: `/glkj/accountant/integration/config/${configId}/rules/${ruleId}`, method: 'delete' }),
  // 版本历史
  getVersions: (configId) => request({ url: `/glkj/accountant/integration/config/${configId}/versions`, method: 'get' })
}

// 集成监控API简化导出
export const integrationMonitorApi = {
  getOverview: getIntegrationStatusOverview,
  getLogPage: getIntegrationLogPage,
  getLogDetail: getIntegrationLogDetail,
  restart: (id) => request({ url: `/glkj/accountant/integration/monitor/${id}/restart`, method: 'post', headers: jsonHeaders }),
  acknowledgeAlert: (alertId) => request({ url: `/glkj/accountant/integration/monitor/alert/${alertId}/acknowledge`, method: 'post', headers: jsonHeaders }),
  getDetail: (id) => request({ url: `/glkj/accountant/integration/monitor/${id}/detail`, method: 'get' }),
  getAlerts: (id) => request({ url: `/glkj/accountant/integration/monitor/${id}/alerts`, method: 'get' }),
  getLogs: (id) => request({ url: `/glkj/accountant/integration/monitor/${id}/logs`, method: 'get' }),
  getConfig: (id) => request({ url: `/glkj/accountant/integration/monitor/${id}/config`, method: 'get' }),
  getAllAlerts: (params) => request({ url: '/glkj/accountant/integration/monitor/alerts/all', method: 'get', params }),
  getSettings: () => request({ url: '/glkj/accountant/integration/monitor/settings', method: 'get' }),
  saveSettings: (data) => request({ url: '/glkj/accountant/integration/monitor/settings', method: 'post', data, headers: jsonHeaders }),
  exportReport: (data) => request({ url: '/glkj/accountant/integration/monitor/export', method: 'post', data, headers: jsonHeaders })
}

// 统计信息API
export function getIntegrationStats() {
  return request({
    url: '/glkj/accountant/integration/stats',
    method: 'get'
  })
}

// ==================== 集成监控API补充 ====================

/**
 * 获取集成状�?支持参数)
 * @param {Object} params 查询参数
 * @returns {Promise} 请求结果
 */
export function getIntegrationStatusWithParams(params) {
  return request({
    url: '/glkj/accountant/integration/status/list',
    method: 'get',
    params
  })
}

/**
 * 获取性能指标
 * @param {Object} params 查询参数
 * @returns {Promise} 请求结果
 */
export function getIntegrationPerformanceMetrics(params) {
  return request({
    url: '/glkj/accountant/integration/performance-metrics',
    method: 'get',
    params
  })
}

/**
 * 获取最近活动
 * @returns {Promise} 请求结果
 */
export function getIntegrationRecentActivities() {
  return request({
    url: '/glkj/accountant/integration/activities/recent',
    method: 'get'
  })
}

/**
 * 获取通知
 * @returns {Promise} 请求结果
 */
export function getIntegrationNotifications() {
  return request({
    url: '/glkj/accountant/integration/notifications/system',
    method: 'get'
  })
}

/**
 * 标记通知为已读
 * @param {String} notificationId 通知ID
 * @returns {Promise} 请求结果
 */
export function markIntegrationNotificationAsRead(notificationId) {
  return request({
    url: `/glkj/accountant/integration/notifications/${notificationId}/read`,
    method: 'put'
  })
}

// 创建别名方法以兼容页面调�?
export const getPerformanceMetrics = getIntegrationPerformanceMetrics
export const getRecentActivities = getIntegrationRecentActivities
export const getNotifications = getIntegrationNotifications
export const markNotificationAsRead = markIntegrationNotificationAsRead

// 获取集成状�?支持参数)
export function getIntegrationStatus(params) {
  if (params) {
    return getIntegrationStatusWithParams(params)
  }
  return getIntegrationStatusOverview()
}

// 获取监控统计
export function getMonitorStats() {
  return getIntegrationStatusOverview()
}

// 获取告警列表
export function getAlertList(params) {
  return getIntegrationLogPage(1, 20, params)
}

// 系统集成API简化导�?
export const systemIntegrationApi = {
  erp: erpIntegrationApi,
  bi: biIntegrationApi,
  api: apiIntegrationApi,
  database: databaseIntegrationApi,
  file: fileIntegrationApi,
  messageQueue: messageQueueIntegrationApi,
  webService: webServiceIntegrationApi,
  cloud: cloudIntegrationApi,
  dataStream: dataStreamIntegrationApi,
  dataMapping: dataMappingApi,
  config: integrationConfigApi,
  monitor: integrationMonitorApi,
  getStats: getIntegrationStats,
  getIntegrationStats,
  getStatus: getIntegrationStatus,
  getIntegrationStatus,
  getMonitorStats: getMonitorStats,
  getAlertList: getAlertList,
  // 集成监控方法
  getPerformanceMetrics,
  getRecentActivities,
  getNotifications,
  markNotificationAsRead,
  // ERP 列表/统计
  getErpConnectionList: (erpType) => request({ url: '/glkj/accountant/budget/erp/list', method: 'get', params: erpType ? { erpType } : {} }),
  getErpStats: () => request({ url: '/glkj/accountant/budget/erp/stats', method: 'get' }),
  getFieldMappings: (erpId) => request({ url: `/glkj/accountant/budget/erp/field-mapping/${erpId}`, method: 'get' }),
  saveFieldMappings: (erpId, mappings) => request({ url: `/glkj/accountant/budget/erp/field-mapping/${erpId}`, method: 'post', data: mappings, headers: { 'Content-Type': 'application/json;charset=UTF-8' } }),
  // BI 列表/统计
  getBiConnectionList: (biType) => request({ url: '/glkj/accountant/integration/bi/connection/list', method: 'get', params: biType ? { biType } : {} }),
  getBiStats: () => request({ url: '/glkj/accountant/integration/bi/stats', method: 'get' }),
  // API 列表/统计
  getApiList: () => request({ url: '/glkj/accountant/integration/api/list', method: 'get' }),
  getApiStats: () => request({ url: '/glkj/accountant/integration/api/stats', method: 'get' }),
  // 数据库 列表/统计
  getDbConnectionList: () => request({ url: '/glkj/accountant/integration/database/connection/list', method: 'get' }),
  getDbStats: () => request({ url: '/glkj/accountant/integration/database/stats', method: 'get' }),
  // 文件 列表/统计
  getFileList: (params) => request({ url: '/glkj/accountant/integration/file/list', method: 'get', params }),
  getFileStats: () => request({ url: '/glkj/accountant/integration/file/stats', method: 'get' }),
  // 消息队列 列表/统计
  getMqList: () => request({ url: '/glkj/accountant/integration/mq/list', method: 'get' }),
  getMqStats: () => request({ url: '/glkj/accountant/integration/mq/stats', method: 'get' }),
  // Web服务 列表/统计
  getWsList: () => request({ url: '/glkj/accountant/integration/webservice/list', method: 'get' }),
  getWsStats: () => request({ url: '/glkj/accountant/integration/webservice/stats', method: 'get' }),
  // 云平台 列表/统计
  getCloudConnectionList: () => request({ url: '/glkj/accountant/integration/cloud/connection/list', method: 'get' }),
  getCloudStats: () => request({ url: '/glkj/accountant/integration/cloud/stats', method: 'get' }),
  // 数据流 列表/统计
  getStreamList: (params) => request({ url: '/glkj/accountant/integration/datastream/list', method: 'get', params }),
  getStreamStats: () => request({ url: '/glkj/accountant/integration/datastream/stats', method: 'get' }),
  // 数据映射 列表/统计
  getMappingList: () => request({ url: '/glkj/accountant/integration/mapping/list', method: 'get' }),
  getMappingStats: () => request({ url: '/glkj/accountant/integration/mapping/stats', method: 'get' }),
  // 集成配置 列表/统计
  getConfigList: () => request({ url: '/glkj/accountant/integration/config/list', method: 'get' }),
  getConfigStats: () => request({ url: '/glkj/accountant/integration/config/stats', method: 'get' }),
  // 系统通知
  getSystemNotifications: () => request({ url: '/glkj/accountant/integration/notifications/system', method: 'get' }),
  // 获取集成模块列表
  getIntegrationModules: () => request({ url: '/glkj/accountant/integration/modules', method: 'get' }),
  // 获取快速操作列表
  getQuickActions: () => request({ url: '/glkj/accountant/integration/quick-actions', method: 'get' }),
  // 测试模块连接
  testModuleConnection: (moduleId) => request({ url: `/glkj/accountant/integration/modules/${moduleId}/test`, method: 'post' })
}
