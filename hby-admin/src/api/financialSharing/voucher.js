/*
 * @Description: 财务共享 - 凭证管理 API
 * @Author: system
 * @Date: 2024-12-19
 */
import request from '@/utils/request'

// ==================== 会计凭证管理 ====================

/**
 * 分页查询会计凭证
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getVoucherPage(data) {
  return request({
    url: '/cwgxAi/voucher/page',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

/**
 * 根据ID查询会计凭证详情
 * @param {Number} id 会计凭证ID
 * @returns {Promise}
 */
export function getVoucherById(id) {
  return request({
    url: `/cwgxAi/voucher/${id}`,
    method: 'get'
  })
}

/**
 * 创建会计凭证
 * @param {Object} data 会计凭证数据
 * @returns {Promise}
 */
export function createVoucher(data) {
  return request({
    url: '/cwgxAi/voucher/create',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

/**
 * 保存会计凭证
 * @param {Object} data 会计凭证数据
 * @returns {Promise}
 */
export function saveVoucher(data) {
  return request({
    url: '/cwgxAi/voucher/save',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

/**
 * 更新会计凭证
 * @param {Object} data 会计凭证数据
 * @returns {Promise}
 */
export function updateVoucher(data) {
  return request({
    url: '/cwgxAi/voucher/update',
    method: 'put',
    data: data
  })
}

/**
 * 删除会计凭证
 * @param {Number} id 会计凭证ID
 * @returns {Promise}
 */
export function deleteVoucher(id) {
  return request({
    url: `/cwgxAi/voucher/${id}`,
    method: 'delete'
  })
}

/**
 * 批量删除会计凭证
 * @param {Array} ids 会计凭证ID列表
 * @returns {Promise}
 */
export function batchDeleteVouchers(ids) {
  return request({
    url: '/cwgxAi/voucher/batch',
    method: 'delete',
    data: ids
  })
}

// ==================== 凭证生成 ====================

/**
 * 根据事项数据生成凭证
 * @param {Array} matterIds 事项数据ID列表
 * @returns {Promise}
 */
export function generateVouchersByMatter(matterIds) {
  return request({
    url: '/cwgxAi/voucher/generate-by-matter',
    method: 'post',
    data: matterIds
  })
}

/**
 * 批量生成凭证
 * @param {Object} data 生成参数
 * @returns {Promise}
 */
export function batchGenerateVouchers(data) {
  return request({
    url: '/cwgxAi/voucher/batch-generate',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

/**
 * 预览凭证生成结果
 * @param {Array} matterIds 事项数据ID列表
 * @returns {Promise}
 */
export function previewVoucherGeneration(matterIds) {
  return request({
    url: '/cwgxAi/voucher/preview-generation',
    method: 'post',
    data: matterIds,
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

/**
 * 获取凭证生成进度
 * @param {String} batchId 批次ID
 * @returns {Promise}
 */
export function getVoucherGenerationProgress(batchId) {
  return request({
    url: `/cwgxAi/voucher/generation-progress/${batchId}`,
    method: 'get'
  })
}

// ==================== 凭证状态管理 ====================

/**
 * 更新凭证状态
 * @param {Number} id 会计凭证ID
 * @param {Number} status 状态
 * @returns {Promise}
 */
export function updateVoucherStatus(id, status) {
  return request({
    url: `/cwgxAi/voucher/${id}/status`,
    method: 'put',
    params: { status }
  })
}

/**
 * 批量更新凭证状态
 * @param {Array} ids 会计凭证ID列表
 * @param {Number} status 状态
 * @returns {Promise}
 */
export function batchUpdateVoucherStatus(ids, status) {
  return request({
    url: '/cwgxAi/voucher/batch/status',
    method: 'put',
    params: { status },
    data: ids
  })
}

/**
 * 过账凭证
 * @param {Array} ids 会计凭证ID列表
 * @returns {Promise}
 */
export function postVouchers(ids) {
  return request({
    url: '/cwgxAi/voucher/post',
    method: 'post',
    data: { voucherIds: ids },
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

/**
 * 反过账凭证
 * @param {Array} ids 会计凭证ID列表
 * @returns {Promise}
 */
export function unpostVouchers(ids) {
  return request({
    url: '/cwgxAi/voucher/unpost',
    method: 'post',
    data: ids,
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

// ==================== 凭证模板管理 ====================

/**
 * 分页查询凭证模板
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getVoucherTemplatePage(data) {
  return request({
    url: '/cwgxAi/voucher-template/getList',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

/**
 * 根据ID查询凭证模板详情
 * @param {Number} id 凭证模板ID
 * @returns {Promise}
 */
export function getVoucherTemplateById(id) {
  return request({
    url: `/cwgxAi/voucher-template/${id}`,
    method: 'get'
  })
}

/**
 * 保存凭证模板
 * @param {Object} data 凭证模板数据
 * @returns {Promise}
 */
export function saveVoucherTemplate(data) {
  return request({
    url: '/cwgxAi/voucher-template/create',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

/**
 * 更新凭证模板
 * @param {Object} data 凭证模板数据
 * @returns {Promise}
 */
export function updateVoucherTemplate(data) {
  return request({
    url: '/cwgxAi/voucher-template/update',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

/**
 * 删除凭证模板
 * @param {Number} id 凭证模板ID
 * @returns {Promise}
 */
export function deleteVoucherTemplate(id) {
  return request({
    url: `/cwgxAi/voucher-template/${id}`,
    method: 'delete'
  })
}

/**
 * 复制凭证模板
 * @param {Number} id 凭证模板ID
 * @returns {Promise}
 */
export function copyVoucherTemplate(id, data = {}) {
  return request({
    url: `/cwgxAi/voucher-template/${id}/copy`,
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

/**
 * 预览凭证模板效果
 * @param {Object} data 模板配置数据
 * @returns {Promise}
 */
export function previewVoucherTemplate(data) {
  return request({
    url: '/cwgxAi/voucher-template/preview',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

// ==================== 模板版本管理 ====================

/**
 * 分页查询模板版本列表
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getTemplateVersionPage(data) {
  return request({
    url: '/cwgxAi/voucher-template/version/page',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

/**
 * 根据ID查询版本详情
 * @param {Number} versionId 版本ID
 * @returns {Promise}
 */
export function getTemplateVersionById(versionId) {
  return request({
    url: `/cwgxAi/voucher-template/version/${versionId}`,
    method: 'get'
  })
}

/**
 * 激活版本
 * @param {Number} versionId 版本ID
 * @returns {Promise}
 */
export function activateTemplateVersion(versionId) {
  return request({
    url: `/cwgxAi/voucher-template/version/${versionId}/activate`,
    method: 'put'
  })
}

/**
 * 回滚到指定版本
 * @param {Number} versionId 版本ID
 * @returns {Promise}
 */
export function rollbackTemplateVersion(versionId) {
  return request({
    url: `/cwgxAi/voucher-template/version/${versionId}/rollback`,
    method: 'post'
  })
}

// ==================== 凭证导入导出 ====================

/**
 * 导入凭证数据
 * @param {FormData} formData 文件数据
 * @returns {Promise}
 */
export function importVouchers(formData) {
  return request({
    url: '/cwgxAi/voucher/import',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 导出凭证数据
 * @param {Object} data 导出参数
 * @returns {Promise}
 */
export function exportVouchers(data) {
  return request({
    url: '/cwgxAi/voucher/export',
    method: 'post',
    data: data,
    responseType: 'blob'
  })
}

/**
 * 下载凭证导入模板
 * @returns {Promise}
 */
export function downloadVoucherImportTemplate() {
  return request({
    url: '/cwgxAi/voucher/import-template',
    method: 'get',
    responseType: 'blob'
  })
}

// ==================== 凭证统计分析 ====================

/**
 * 获取凭证统计数据
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getVoucherStatistics(data) {
  return request({
    url: '/cwgxAi/voucher/statistics',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

/**
 * 获取凭证状态分布
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getVoucherStatusDistribution(data) {
  return request({
    url: '/cwgxAi/voucher/status-distribution',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

/**
 * 获取凭证生成趋势
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getVoucherGenerationTrend(data) {
  return request({
    url: '/cwgxAi/voucher/generation-trend',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

// ==================== 过账日志管理 ====================

/**
 * 获取凭证过账日志
 * @param {String} voucherId 凭证ID
 * @returns {Promise}
 */
export function getVoucherPostingLogs(voucherId) {
  return request({
    url: `/cwgxAi/voucher/${voucherId}/posting-logs`,
    method: 'get'
  })
}

/**
 * 分页查询过账日志
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getPostingLogPage(data) {
  return request({
    url: '/cwgxAi/voucher/posting-log/page',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

/**
 * 分页查询反过账记录
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getUnpostRecordPage(data) {
  return request({
    url: '/cwgxAi/voucher/unpost-record/page',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json'
    }
  })
}