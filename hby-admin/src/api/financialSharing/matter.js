/*
 * @Description: 财务共享 - 事项管理 API
 * @Author: system
 * @Date: 2024-12-19
 */
import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// ==================== 事项数据管理 ====================

/**
 * 分页查询事项数据
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getMatterDataPage(data) {
  return request({
    url: '/cwgxAi/matter/transaction/getList',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 根据ID查询事项数据详情
 * @param {Number} id 事项数据ID
 * @returns {Promise}
 */
export function getMatterDataById(id) {
  return request({
    url: `/cwgxAi/matter/transaction/${id}`,
    method: 'get'
  })
}

/**
 * 保存事项数据
 * @param {Object} data 事项数据
 * @returns {Promise}
 */
export function saveMatterData(data) {
  return request({
    url: '/cwgxAi/matter/transaction/save',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 更新事项数据
 * @param {Object} data 事项数据
 * @returns {Promise}
 */
export function updateMatterData(data) {
  return request({
    url: '/cwgxAi/matter/transaction/update',
    method: 'put',
    data: transData(data)
  })
}

/**
 * 删除事项数据
 * @param {Number} id 事项数据ID
 * @returns {Promise}
 */
export function deleteMatterData(id) {
  return request({
    url: `/cwgxAi/matter/transaction/${id}`,
    method: 'delete'
  })
}

/**
 * 批量删除事项数据
 * @param {Array} ids 事项数据ID列表
 * @returns {Promise}
 */
export function batchDeleteMatterData(ids) {
  return request({
    url: '/cwgxAi/matter/transaction/batch-delete',
    method: 'delete',
    data: ids
  })
}

/**
 * 批量导入事项数据
 * @param {FormData} formData 文件数据
 * @returns {Promise}
 */
export function importMatterData(formData) {
  return request({
    url: '/cwgxAi/matter/transaction/import',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 导出事项数据
 * @param {Object} data 导出参数
 * @returns {Promise}
 */
export function exportMatterData(data) {
  return request({
    url: '/cwgxAi/matter/transaction/export',
    method: 'post',
    data: transData(data),
    responseType: 'blob'
  })
}

// ==================== 事项处理 ====================

/**
 * 处理事项数据
 * @param {Array} ids 事项数据ID列表
 * @returns {Promise}
 */
export function processMatterData(ids) {
  return request({
    url: '/cwgxAi/matter/transaction/process',
    method: 'post',
    data: ids
  })
}

/**
 * 批量处理事项数据
 * @param {Object} data 处理参数
 * @returns {Promise}
 */
export function batchProcessMatterData(data) {
  return request({
    url: '/cwgxAi/matter/transaction/batch-process',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 重新处理失败的事项数据
 * @param {Array} ids 事项数据ID列表
 * @returns {Promise}
 */
export function reprocessFailedMatterData(ids) {
  return request({
    url: '/cwgxAi/matter/transaction/reprocess',
    method: 'post',
    data: ids
  })
}

/**
 * 获取事项处理进度
 * @param {String} batchId 批次ID
 * @returns {Promise}
 */
export function getMatterProcessProgress(batchId) {
  return request({
    url: `/cwgxAi/matter/transaction/process-progress/${batchId}`,
    method: 'get'
  })
}

// ==================== 事项状态管理 ====================

/**
 * 更新事项状态
 * @param {Number} id 事项数据ID
 * @param {Number} status 状态
 * @returns {Promise}
 */
export function updateMatterStatus(id, status) {
  return request({
    url: `/cwgxAi/matter/transaction/${id}/status`,
    method: 'put',
    params: { status }
  })
}

/**
 * 批量更新事项状态
 * @param {Array} ids 事项数据ID列表
 * @param {Number} status 状态
 * @returns {Promise}
 */
export function batchUpdateMatterStatus(ids, status) {
  return request({
    url: '/cwgxAi/matter/transaction/batch/status',
    method: 'put',
    params: { status },
    data: ids
  })
}

/**
 * 获取事项状态统计
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getMatterStatusStatistics(data) {
  return request({
    url: '/zbgl/matter-data/status-statistics',
    method: 'post',
    data: transData(data)
  })
}

// ==================== 事项类型管理 ====================

/**
 * 获取事项类型列表
 * @param {Number} tenantId 租户ID
 * @returns {Promise}
 */
export function getMatterTypeList(tenantId) {
  return request({
    url: '/zbgl/matter-type/list',
    method: 'get',
    params: { tenantId }
  })
}

/**
 * 保存事项类型
 * @param {Object} data 事项类型数据
 * @returns {Promise}
 */
export function saveMatterType(data) {
  return request({
    url: '/zbgl/matter-type/save',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 更新事项类型
 * @param {Object} data 事项类型数据
 * @returns {Promise}
 */
export function updateMatterType(data) {
  return request({
    url: '/zbgl/matter-type/update',
    method: 'put',
    data: transData(data)
  })
}

/**
 * 删除事项类型
 * @param {Number} id 事项类型ID
 * @returns {Promise}
 */
export function deleteMatterType(id) {
  return request({
    url: `/zbgl/matter-type/${id}`,
    method: 'delete'
  })
}

// ==================== 事项模板管理 ====================

/**
 * 分页查询事项模板
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getMatterTemplatePage(data) {
  return request({
    url: '/zbgl/matter-template/page',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 根据ID查询事项模板详情
 * @param {Number} id 事项模板ID
 * @returns {Promise}
 */
export function getMatterTemplateById(id) {
  return request({
    url: `/zbgl/matter-template/${id}`,
    method: 'get'
  })
}

/**
 * 保存事项模板
 * @param {Object} data 事项模板数据
 * @returns {Promise}
 */
export function saveMatterTemplate(data) {
  return request({
    url: '/zbgl/matter-template/save',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 更新事项模板
 * @param {Object} data 事项模板数据
 * @returns {Promise}
 */
export function updateMatterTemplate(data) {
  return request({
    url: '/zbgl/matter-template/update',
    method: 'put',
    data: transData(data)
  })
}

/**
 * 删除事项模板
 * @param {Number} id 事项模板ID
 * @returns {Promise}
 */
export function deleteMatterTemplate(id) {
  return request({
    url: `/zbgl/matter-template/${id}`,
    method: 'delete'
  })
}

/**
 * 根据事项类型获取模板
 * @param {String} matterType 事项类型
 * @param {Number} tenantId 租户ID
 * @returns {Promise}
 */
export function getMatterTemplateByType(matterType, tenantId) {
  return request({
    url: '/zbgl/matter-template/by-type',
    method: 'get',
    params: { matterType, tenantId }
  })
}

// ==================== 事项数据验证 API ====================

/**
 * 验证事项数据
 * @param {Object} data 验证参数
 * @returns {Promise}
 */
export function validateMatterData(data) {
  return request({
    url: '/cwgxAi/matter/data/validate',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 批量验证事项数据
 * @param {Array} matterIds 事项ID列表
 * @returns {Promise}
 */
export function batchValidateMatterData(matterIds) {
  return request({
    url: '/cwgxAi/matter/data/batch-validate',
    method: 'post',
    data: matterIds
  })
}

/**
 * 获取数据验证规则
 * @param {String} matterType 事项类型
 * @returns {Promise}
 */
export function getMatterDataValidationRules(matterType) {
  return request({
    url: '/cwgxAi/matter/data/validation-rules',
    method: 'get',
    params: { matterType }
  })
}

/**
 * 保存数据验证规则
 * @param {Object} data 规则数据
 * @returns {Promise}
 */
export function saveMatterDataValidationRules(data) {
  return request({
    url: '/cwgxAi/matter/data/validation-rules',
    method: 'post',
    data: transData(data)
  })
}

// ==================== 事项状态管理 API ====================

/**
 * 更新事项状态
 * @param {String} matterId 事项ID
 * @param {String} status 新状态
 * @param {String} remark 备注
 * @returns {Promise}
 */
export function updateMatterCenterStatus(matterId, status, remark) {
  return request({
    url: `/cwgxAi/matter/data/${matterId}/status`,
    method: 'put',
    data: { status, remark }
  })
}

/**
 * 批量更新事项状态
 * @param {Array} matterIds 事项ID列表
 * @param {String} status 新状态
 * @param {String} remark 备注
 * @returns {Promise}
 */
export function batchUpdateMatterCenterStatus(matterIds, status, remark) {
  return request({
    url: '/cwgxAi/matter/data/batch/status',
    method: 'put',
    data: { matterIds, status, remark }
  })
}

/**
 * 获取事项状态变更历史
 * @param {String} matterId 事项ID
 * @returns {Promise}
 */
export function getMatterStatusHistory(matterId) {
  return request({
    url: `/cwgxAi/matter/data/${matterId}/status-history`,
    method: 'get'
  })
}

/**
 * 获取事项状态统计（事项中心）
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getMatterCenterStatusStatistics(params) {
  return request({
    url: '/cwgxAi/matter/data/status-statistics',
    method: 'get',
    params
  })
}

// ==================== 事项数据处理 API ====================

/**
 * 处理事项数据（事项中心）
 * @param {Object} data 处理参数
 * @returns {Promise}
 */
export function processMatterCenterData(data) {
  return request({
    url: '/cwgxAi/matter/data/process',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 批量处理事项数据（事项中心）
 * @param {Object} data 批量处理参数
 * @returns {Promise}
 */
export function batchProcessMatterCenterData(data) {
  return request({
    url: '/cwgxAi/matter/data/batch-process',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取数据处理状态
 * @param {String} processTaskId 处理任务ID
 * @returns {Promise}
 */
export function getMatterDataProcessStatus(processTaskId) {
  return request({
    url: `/cwgxAi/matter/data/process/status/${processTaskId}`,
    method: 'get'
  })
}

/**
 * 停止数据处理任务
 * @param {String} processTaskId 处理任务ID
 * @returns {Promise}
 */
export function stopMatterDataProcess(processTaskId) {
  return request({
    url: `/cwgxAi/matter/data/process/stop/${processTaskId}`,
    method: 'post'
  })
}

// ==================== 事项数据存储优化 API ====================

/**
 * 优化事项数据存储
 * @param {Object} data 优化参数
 * @returns {Promise}
 */
export function optimizeMatterDataStorage(data) {
  return request({
    url: '/cwgxAi/matter/data/optimize-storage',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取存储优化状态
 * @param {String} optimizeTaskId 优化任务ID
 * @returns {Promise}
 */
export function getMatterDataStorageOptimizeStatus(optimizeTaskId) {
  return request({
    url: `/cwgxAi/matter/data/optimize-storage/status/${optimizeTaskId}`,
    method: 'get'
  })
}

/**
 * 清理过期事项数据
 * @param {Object} data 清理参数
 * @returns {Promise}
 */
export function cleanupExpiredMatterData(data) {
  return request({
    url: '/cwgxAi/matter/data/cleanup',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取存储使用情况统计
 * @returns {Promise}
 */
export function getMatterDataStorageUsage() {
  return request({
    url: '/cwgxAi/matter/data/storage-usage',
    method: 'get'
  })
}
