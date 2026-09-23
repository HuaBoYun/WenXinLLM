/**
 * NCV65全面预算系统 - Excel客户端API
 * 
 * @description Excel客户端功能API接口，包含模板管理、数据同步、公式计算等功能
 * @version 1.0.0
 * @author AI Assistant
 * @date 2025-01-08
 * @module excelClient
 */

import request from '@/utils/request'

// ==================== Excel模板管理API ====================

/**
 * 创建Excel模板
 * @param {Object} data 模板数据
 * @returns {Promise} 请求结果
 */
export function createExcelTemplate(data) {
  return request({
    url: '/accountant/excel/template',
    method: 'post',
    data
  })
}

/**
 * 查询Excel模板详情
 * @param {String} templateId 模板ID
 * @returns {Promise} 请求结果
 */
export function getExcelTemplate(templateId) {
  return request({
    url: `/accountant/excel/template/${templateId}`,
    method: 'get'
  })
}

/**
 * 更新Excel模板
 * @param {String} templateId 模板ID
 * @param {Object} data 更新数据
 * @returns {Promise} 请求结果
 */
export function updateExcelTemplate(templateId, data) {
  return request({
    url: `/accountant/excel/template/${templateId}`,
    method: 'put',
    data
  })
}

/**
 * 删除Excel模板
 * @param {String} templateId 模板ID
 * @returns {Promise} 请求结果
 */
export function deleteExcelTemplate(templateId) {
  return request({
    url: `/accountant/excel/template/${templateId}`,
    method: 'delete'
  })
}

/**
 * 分页查询Excel模板列表
 * @param {Number} current 当前�? * @param {Number} size 页大�? * @param {Object} params 查询参数
 * @returns {Promise} 请求结果
 */
export function getExcelTemplatePage(current, size, params) {
  return request({
    url: '/accountant/excel/template/page',
    method: 'post',
    params: { current, size },
    data: params
  })
}

/**
 * 上传Excel模板文件
 * @param {FormData} formData 文件数据
 * @returns {Promise} 请求结果
 */
export function uploadExcelTemplate(formData) {
  return request({
    url: '/accountant/excel/template/upload',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 下载Excel模板
 * @param {String} templateId 模板ID
 * @returns {Promise} 请求结果
 */
export function downloadExcelTemplate(templateId) {
  return request({
    url: `/accountant/excel/template/${templateId}/download`,
    method: 'get',
    responseType: 'blob'
  })
}

/**
 * 发布Excel模板
 * @param {String} templateId 模板ID
 * @returns {Promise} 请求结果
 */
export function publishExcelTemplate(templateId) {
  return request({
    url: `/accountant/excel/template/${templateId}/publish`,
    method: 'post'
  })
}

// ==================== MDarea数据区域管理API ====================

/**
 * 创建MDarea配置
 * @param {Object} data MDarea配置数据
 * @returns {Promise} 请求结果
 */
export function createMDareaConfig(data) {
  return request({
    url: '/accountant/excel/mdarea/config',
    method: 'post',
    data
  })
}

/**
 * 查询MDarea配置详情
 * @param {String} configId 配置ID
 * @returns {Promise} 请求结果
 */
export function getMDareaConfig(configId) {
  return request({
    url: `/accountant/excel/mdarea/config/${configId}`,
    method: 'get'
  })
}

/**
 * 更新MDarea配置
 * @param {String} configId 配置ID
 * @param {Object} data 更新数据
 * @returns {Promise} 请求结果
 */
export function updateMDareaConfig(configId, data) {
  return request({
    url: `/accountant/excel/mdarea/config/${configId}`,
    method: 'put',
    data
  })
}

/**
 * 删除MDarea配置
 * @param {String} configId 配置ID
 * @returns {Promise} 请求结果
 */
export function deleteMDareaConfig(configId) {
  return request({
    url: `/accountant/excel/mdarea/config/${configId}`,
    method: 'delete'
  })
}

/**
 * 分页查询MDarea配置列表
 * @param {Number} current 当前�? * @param {Number} size 页大�? * @param {Object} params 查询参数
 * @returns {Promise} 请求结果
 */
export function getMDareaConfigPage(current, size, params) {
  return request({
    url: '/accountant/excel/mdarea/config/page',
    method: 'post',
    params: { current, size },
    data: params
  })
}

/**
 * 解析Excel中的MDarea区域
 * @param {String} templateId 模板ID
 * @returns {Promise} 请求结果
 */
export function parseMDareaFromExcel(templateId) {
  return request({
    url: `/accountant/excel/mdarea/parse/${templateId}`,
    method: 'post'
  })
}

// ==================== 数据同步API ====================

/**
 * 创建数据同步任务
 * @param {Object} data 同步任务数据
 * @returns {Promise} 请求结果
 */
export function createDataSyncTask(data) {
  return request({
    url: '/accountant/excel/sync/task',
    method: 'post',
    data
  })
}

/**
 * 查询数据同步任务详情
 * @param {String} taskId 任务ID
 * @returns {Promise} 请求结果
 */
export function getDataSyncTask(taskId) {
  return request({
    url: `/accountant/excel/sync/task/${taskId}`,
    method: 'get'
  })
}

/**
 * 分页查询数据同步任务列表
 * @param {Number} current 当前�? * @param {Number} size 页大�? * @param {Object} params 查询参数
 * @returns {Promise} 请求结果
 */
export function getDataSyncTaskPage(current, size, params) {
  return request({
    url: '/accountant/excel/sync/task/page',
    method: 'post',
    params: { current, size },
    data: params
  })
}

/**
 * 执行数据同步
 * @param {String} taskId 任务ID
 * @returns {Promise} 请求结果
 */
export function executeDataSync(taskId) {
  return request({
    url: `/accountant/excel/sync/task/${taskId}/execute`,
    method: 'post'
  })
}

/**
 * 获取数据同步状�? * @param {String} taskId 任务ID
 * @returns {Promise} 请求结果
 */
export function getDataSyncStatus(taskId) {
  return request({
    url: `/accountant/excel/sync/task/${taskId}/status`,
    method: 'get'
  })
}

/**
 * 上传Excel数据
 * @param {FormData} formData 文件数据
 * @returns {Promise} 请求结果
 */
export function uploadExcelData(formData) {
  return request({
    url: '/accountant/excel/sync/upload',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 下载Excel数据
 * @param {Object} params 下载参数
 * @returns {Promise} 请求结果
 */
export function downloadExcelData(params) {
  return request({
    url: '/accountant/excel/sync/download',
    method: 'post',
    data: params,
    responseType: 'blob'
  })
}

// ==================== 公式计算API ====================

/**
 * 创建公式定义
 * @param {Object} data 公式定义数据
 * @returns {Promise} 请求结果
 */
export function createFormulaDefinition(data) {
  return request({
    url: '/accountant/excel/formula/definition',
    method: 'post',
    data
  })
}

/**
 * 查询公式定义详情
 * @param {String} formulaId 公式ID
 * @returns {Promise} 请求结果
 */
export function getFormulaDefinition(formulaId) {
  return request({
    url: `/accountant/excel/formula/definition/${formulaId}`,
    method: 'get'
  })
}

/**
 * 更新公式定义
 * @param {String} formulaId 公式ID
 * @param {Object} data 更新数据
 * @returns {Promise} 请求结果
 */
export function updateFormulaDefinition(formulaId, data) {
  return request({
    url: `/accountant/excel/formula/definition/${formulaId}`,
    method: 'put',
    data
  })
}

/**
 * 删除公式定义
 * @param {String} formulaId 公式ID
 * @returns {Promise} 请求结果
 */
export function deleteFormulaDefinition(formulaId) {
  return request({
    url: `/accountant/excel/formula/definition/${formulaId}`,
    method: 'delete'
  })
}

/**
 * 分页查询公式定义列表
 * @param {Number} current 当前�? * @param {Number} size 页大�? * @param {Object} params 查询参数
 * @returns {Promise} 请求结果
 */
export function getFormulaDefinitionPage(current, size, params) {
  return request({
    url: '/accountant/excel/formula/definition/page',
    method: 'post',
    params: { current, size },
    data: params
  })
}

/**
 * 执行公式计算
 * @param {Object} data 计算参数
 * @returns {Promise} 请求结果
 */
export function executeFormulaCalculation(data) {
  return request({
    url: '/accountant/excel/formula/calculate',
    method: 'post',
    data
  })
}

/**
 * 获取公式计算结果
 * @param {String} calculationId 计算ID
 * @returns {Promise} 请求结果
 */
export function getFormulaCalculationResult(calculationId) {
  return request({
    url: `/accountant/excel/formula/calculation/${calculationId}/result`,
    method: 'get'
  })
}

// ==================== 客户端配置API ====================

/**
 * 获取客户端配�? * @returns {Promise} 请求结果
 */
export function getClientConfig() {
  return request({
    url: '/accountant/excel/client/config',
    method: 'get'
  })
}

/**
 * 更新客户端配�? * @param {Object} data 配置数据
 * @returns {Promise} 请求结果
 */
export function updateClientConfig(data) {
  return request({
    url: '/accountant/excel/client/config',
    method: 'put',
    data
  })
}

/**
 * 检查客户端版本
 * @param {String} currentVersion 当前版本
 * @returns {Promise} 请求结果
 */
export function checkClientVersion(currentVersion) {
  return request({
    url: '/accountant/excel/client/version/check',
    method: 'get',
    params: { currentVersion }
  })
}

/**
 * 下载客户端更新包
 * @param {String} version 版本�? * @returns {Promise} 请求结果
 */
export function downloadClientUpdate(version) {
  return request({
    url: `/accountant/excel/client/update/${version}/download`,
    method: 'get',
    responseType: 'blob'
  })
}
