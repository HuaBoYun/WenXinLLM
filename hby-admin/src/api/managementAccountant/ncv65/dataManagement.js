/**
 * NCV65全面预算系统 - 数据管理API
 * 
 * @description 数据管理功能API接口，包含数据导入导出、数据验证、数据清洗等功能
 * @version 1.0.0
 * @author AI Assistant
 * @date 2025-01-08
 * @module dataManagement
 */

import request from '@/utils/request'

// ==================== 数据导入导出API ====================

/**
 * 创建数据导入导出任务
 * @param {Object} data 任务数据
 * @returns {Promise} 请求结果
 */
export function createDataImportExportTask(data) {
  return request({
    url: '/accountant/data/import-export/task',
    method: 'post',
    data
  })
}

/**
 * 查询数据导入导出任务详情
 * @param {String} taskId 任务ID
 * @returns {Promise} 请求结果
 */
export function getDataImportExportTask(taskId) {
  return request({
    url: `/accountant/data/import-export/task/${taskId}`,
    method: 'get'
  })
}

/**
 * 更新数据导入导出任务
 * @param {String} taskId 任务ID
 * @param {Object} data 更新数据
 * @returns {Promise} 请求结果
 */
export function updateDataImportExportTask(taskId, data) {
  return request({
    url: `/accountant/data/import-export/task/${taskId}`,
    method: 'put',
    data
  })
}

/**
 * 删除数据导入导出任务
 * @param {String} taskId 任务ID
 * @returns {Promise} 请求结果
 */
export function deleteDataImportExportTask(taskId) {
  return request({
    url: `/accountant/data/import-export/task/${taskId}`,
    method: 'delete'
  })
}

/**
 * 分页查询数据导入导出任务列表
 * @param {Number} current 当前�? * @param {Number} size 页大�? * @param {Object} params 查询参数
 * @returns {Promise} 请求结果
 */
export function getDataImportExportTaskPage(current, size, params) {
  return request({
    url: '/accountant/data/import-export/task/page',
    method: 'post',
    params: { current, size },
    data: params
  })
}

/**
 * 上传导入文件
 * @param {FormData} formData 文件数据
 * @returns {Promise} 请求结果
 */
export function uploadImportFile(formData) {
  return request({
    url: '/accountant/data/import/upload',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 预览导入数据
 * @param {String} fileId 文件ID
 * @param {Object} params 预览参数
 * @returns {Promise} 请求结果
 */
export function previewImportData(fileId, params) {
  return request({
    url: `/accountant/data/import/preview/${fileId}`,
    method: 'post',
    data: params
  })
}

/**
 * 执行数据导入
 * @param {String} taskId 任务ID
 * @returns {Promise} 请求结果
 */
export function executeDataImport(taskId) {
  return request({
    url: `/accountant/data/import/task/${taskId}/execute`,
    method: 'post'
  })
}

/**
 * 获取导入进度
 * @param {String} taskId 任务ID
 * @returns {Promise} 请求结果
 */
export function getImportProgress(taskId) {
  return request({
    url: `/accountant/data/import/task/${taskId}/progress`,
    method: 'get'
  })
}

/**
 * 获取导入结果
 * @param {String} taskId 任务ID
 * @returns {Promise} 请求结果
 */
export function getImportResult(taskId) {
  return request({
    url: `/accountant/data/import/task/${taskId}/result`,
    method: 'get'
  })
}

/**
 * 执行数据导出
 * @param {Object} data 导出参数
 * @returns {Promise} 请求结果
 */
export function executeDataExport(data) {
  return request({
    url: '/accountant/data/export/execute',
    method: 'post',
    data,
    responseType: 'blob'
  })
}

/**
 * 获取导出模板
 * @param {String} templateType 模板类型
 * @returns {Promise} 请求结果
 */
export function getExportTemplate(templateType) {
  return request({
    url: `/accountant/data/export/template/${templateType}`,
    method: 'get',
    responseType: 'blob'
  })
}

// ==================== 数据验证API ====================

/**
 * 创建数据验证规则
 * @param {Object} data 验证规则数据
 * @returns {Promise} 请求结果
 */
export function createDataValidationRule(data) {
  return request({
    url: '/accountant/data/validation/rule',
    method: 'post',
    data
  })
}

/**
 * 查询数据验证规则详情
 * @param {String} ruleId 规则ID
 * @returns {Promise} 请求结果
 */
export function getDataValidationRule(ruleId) {
  return request({
    url: `/accountant/data/validation/rule/${ruleId}`,
    method: 'get'
  })
}

/**
 * 更新数据验证规则
 * @param {String} ruleId 规则ID
 * @param {Object} data 更新数据
 * @returns {Promise} 请求结果
 */
export function updateDataValidationRule(ruleId, data) {
  return request({
    url: `/accountant/data/validation/rule/${ruleId}`,
    method: 'put',
    data
  })
}

/**
 * 删除数据验证规则
 * @param {String} ruleId 规则ID
 * @returns {Promise} 请求结果
 */
export function deleteDataValidationRule(ruleId) {
  return request({
    url: `/accountant/data/validation/rule/${ruleId}`,
    method: 'delete'
  })
}

/**
 * 分页查询数据验证规则列表
 * @param {Number} current 当前�? * @param {Number} size 页大�? * @param {Object} params 查询参数
 * @returns {Promise} 请求结果
 */
export function getDataValidationRulePage(current, size, params) {
  return request({
    url: '/accountant/data/validation/rule/page',
    method: 'post',
    params: { current, size },
    data: params
  })
}

/**
 * 执行数据验证
 * @param {Object} data 验证数据
 * @returns {Promise} 请求结果
 */
export function executeDataValidation(data) {
  return request({
    url: '/accountant/data/validation/execute',
    method: 'post',
    data
  })
}

/**
 * 获取数据验证结果
 * @param {String} validationId 验证ID
 * @returns {Promise} 请求结果
 */
export function getDataValidationResult(validationId) {
  return request({
    url: `/accountant/data/validation/${validationId}/result`,
    method: 'get'
  })
}

/**
 * 分页查询数据验证记录
 * @param {Number} current 当前�? * @param {Number} size 页大�? * @param {Object} params 查询参数
 * @returns {Promise} 请求结果
 */
export function getDataValidationRecordPage(current, size, params) {
  return request({
    url: '/accountant/data/validation/record/page',
    method: 'post',
    params: { current, size },
    data: params
  })
}

// ==================== 数据清洗API ====================

/**
 * 创建数据清洗任务
 * @param {Object} data 清洗任务数据
 * @returns {Promise} 请求结果
 */
export function createDataCleaningTask(data) {
  return request({
    url: '/accountant/data/cleaning/task',
    method: 'post',
    data
  })
}

/**
 * 查询数据清洗任务详情
 * @param {String} taskId 任务ID
 * @returns {Promise} 请求结果
 */
export function getDataCleaningTask(taskId) {
  return request({
    url: `/accountant/data/cleaning/task/${taskId}`,
    method: 'get'
  })
}

/**
 * 更新数据清洗任务
 * @param {String} taskId 任务ID
 * @param {Object} data 更新数据
 * @returns {Promise} 请求结果
 */
export function updateDataCleaningTask(taskId, data) {
  return request({
    url: `/accountant/data/cleaning/task/${taskId}`,
    method: 'put',
    data
  })
}

/**
 * 删除数据清洗任务
 * @param {String} taskId 任务ID
 * @returns {Promise} 请求结果
 */
export function deleteDataCleaningTask(taskId) {
  return request({
    url: `/accountant/data/cleaning/task/${taskId}`,
    method: 'delete'
  })
}

/**
 * 分页查询数据清洗任务列表
 * @param {Number} current 当前�? * @param {Number} size 页大�? * @param {Object} params 查询参数
 * @returns {Promise} 请求结果
 */
export function getDataCleaningTaskPage(current, size, params) {
  return request({
    url: '/accountant/data/cleaning/task/page',
    method: 'post',
    params: { current, size },
    data: params
  })
}

/**
 * 执行数据清洗
 * @param {String} taskId 任务ID
 * @returns {Promise} 请求结果
 */
export function executeDataCleaning(taskId) {
  return request({
    url: `/accountant/data/cleaning/task/${taskId}/execute`,
    method: 'post'
  })
}

/**
 * 获取数据清洗进度
 * @param {String} taskId 任务ID
 * @returns {Promise} 请求结果
 */
export function getDataCleaningProgress(taskId) {
  return request({
    url: `/accountant/data/cleaning/task/${taskId}/progress`,
    method: 'get'
  })
}

/**
 * 获取数据清洗结果
 * @param {String} taskId 任务ID
 * @returns {Promise} 请求结果
 */
export function getDataCleaningResult(taskId) {
  return request({
    url: `/accountant/data/cleaning/task/${taskId}/result`,
    method: 'get'
  })
}

// ==================== 数据质量监控API ====================

/**
 * 获取数据质量概览
 * @returns {Promise} 请求结果
 */
export function getDataQualityOverview() {
  return request({
    url: '/accountant/data/quality/overview',
    method: 'get'
  })
}

/**
 * 分页查询数据质量报告
 * @param {Number} current 当前�? * @param {Number} size 页大�? * @param {Object} params 查询参数
 * @returns {Promise} 请求结果
 */
export function getDataQualityReportPage(current, size, params) {
  return request({
    url: '/accountant/data/quality/report/page',
    method: 'post',
    params: { current, size },
    data: params
  })
}

/**
 * 生成数据质量报告
 * @param {Object} data 报告参数
 * @returns {Promise} 请求结果
 */
export function generateDataQualityReport(data) {
  return request({
    url: '/accountant/data/quality/report/generate',
    method: 'post',
    data
  })
}
