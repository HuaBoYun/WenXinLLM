import request from '@/utils/request'

/**
 * 统计分析API接口
 * @author 示例云开发团队
 * @since 2025-01-06
 */

// ==================== 统计分析 ====================

/**
 * 项目概览统计
 * @param {Object} params 查询参数
 * @returns {Promise} 统计结果
 */
export function getProjectOverviewStatistics(params) {
  return request({
    url: '/api/statistics/project/overview',
    method: 'get',
    params: params
  })
}

/**
 * 风险评估统计
 * @param {Object} params 查询参数
 * @returns {Promise} 统计结果
 */
export function getRiskAssessmentStatistics(params) {
  return request({
    url: '/api/statistics/risk/summary',
    method: 'get',
    params: params
  })
}

/**
 * 财务收支统计
 * @param {Object} params 查询参数
 * @returns {Promise} 统计结果
 */
export function getFinanceStatistics(params) {
  return request({
    url: '/api/statistics/finance/summary',
    method: 'get',
    params: params
  })
}

/**
 * 进度完成统计
 * @param {Object} params 查询参数
 * @returns {Promise} 统计结果
 */
export function getProgressStatistics(params) {
  return request({
    url: '/api/statistics/progress/summary',
    method: 'get',
    params: params
  })
}

/**
 * 质量安全统计
 * @param {Object} params 查询参数
 * @returns {Promise} 统计结果
 */
export function getQualitySafetyStatistics(params) {
  return request({
    url: '/api/statistics/quality/safety',
    method: 'get',
    params: params
  })
}

/**
 * 招投标统计
 * @param {Object} params 查询参数
 * @returns {Promise} 统计结果
 */
export function getBiddingStatistics(params) {
  return request({
    url: '/api/statistics/bidding/summary',
    method: 'get',
    params: params
  })
}

/**
 * 预算执行统计
 * @param {Object} params 查询参数
 * @returns {Promise} 统计结果
 */
export function getBudgetExecutionStatistics(params) {
  return request({
    url: '/api/statistics/budget/execution',
    method: 'get',
    params: params
  })
}

/**
 * 债权账龄统计
 * @param {Object} params 查询参数
 * @returns {Promise} 统计结果
 */
export function getDebtAgingStatistics(params) {
  return request({
    url: '/api/statistics/debt/aging',
    method: 'get',
    params: params
  })
}

// ==================== 文件管理 ====================

/**
 * 文件上传
 * @param {FormData} formData 文件数据
 * @returns {Promise} 上传结果
 */
export function uploadFile(formData) {
  return request({
    url: '/api/file/upload',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 文件下载
 * @param {String} filePath 文件路径
 * @returns {Promise} 文件流
 */
export function downloadFile(filePath) {
  return request({
    url: '/api/file/download',
    method: 'get',
    params: { filePath },
    responseType: 'blob'
  })
}

/**
 * 文件预览
 * @param {String} filePath 文件路径
 * @returns {Promise} 预览数据
 */
export function previewFile(filePath) {
  return request({
    url: '/api/file/preview',
    method: 'get',
    params: { filePath }
  })
}
