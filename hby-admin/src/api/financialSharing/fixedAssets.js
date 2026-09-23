/*
 * @Description: 财务共享 - 固定资产模块 API
 * @Author: system
 * @Date: 2024-12-19
 */
import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// ==================== 资产卡片管理 API ====================

/**
 * 获取资产卡片列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getAssetCardsList(params) {
  return request({
    url: '/cwgxAi/fixedAssets/assetCards',
    method: 'get',
    params: transData(params)
  })
}

/**
 * 新增资产卡片
 * @param {Object} data 资产数据
 * @returns {Promise}
 */
export function createAssetCard(data) {
  return request({
    url: '/cwgxAi/fixedAssets/assetCards',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 更新资产卡片
 * @param {String} id 资产ID
 * @param {Object} data 资产数据
 * @returns {Promise}
 */
export function updateAssetCard(id, data) {
  return request({
    url: `/cwgxAi/fixedAssets/assetCards/${id}`,
    method: 'put',
    data: transData(data)
  })
}

/**
 * 删除资产卡片
 * @param {String} id 资产ID
 * @returns {Promise}
 */
export function deleteAssetCard(id) {
  return request({
    url: `/cwgxAi/fixedAssets/assetCards/${id}`,
    method: 'delete'
  })
}

/**
 * 获取资产卡片详情
 * @param {String} id 资产ID
 * @returns {Promise}
 */
export function getAssetCardDetail(id) {
  return request({
    url: `/cwgxAi/fixedAssets/assetCards/${id}`,
    method: 'get'
  })
}

/**
 * 批量导入资产卡片
 * @param {FormData} formData 文件数据
 * @returns {Promise}
 */
export function importAssetCards(formData) {
  return request({
    url: '/cwgxAi/fixedAssets/assetCards/import',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 导出资产卡片
 * @param {Object} params 导出参数
 * @returns {Promise}
 */
export function exportAssetCards(params) {
  return request({
    url: '/cwgxAi/fixedAssets/assetCards/export',
    method: 'post',
    data: transData(params),
    responseType: 'blob'
  })
}

// ==================== 折旧管理 API ====================

/**
 * 计提折旧
 * @param {String} period 期间
 * @param {Object} options 计提选项
 * @returns {Promise}
 */
export function calculateDepreciation(period, options = {}) {
  return request({
    url: '/cwgxAi/fixedAssets/depreciation',
    method: 'post',
    params: { period },
    data: transData(options)
  })
}

/**
 * 获取折旧计划
 * @param {String} assetId 资产ID
 * @returns {Promise}
 */
export function getDepreciationSchedule(assetId) {
  return request({
    url: `/cwgxAi/fixedAssets/depreciation/schedule/${assetId}`,
    method: 'get'
  })
}

/**
 * 获取折旧明细
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getDepreciationDetails(params) {
  return request({
    url: '/cwgxAi/fixedAssets/depreciation/details',
    method: 'get',
    params: transData(params)
  })
}

/**
 * 调整折旧
 * @param {String} assetId 资产ID
 * @param {Object} adjustData 调整数据
 * @returns {Promise}
 */
export function adjustDepreciation(assetId, adjustData) {
  return request({
    url: `/cwgxAi/fixedAssets/depreciation/adjust/${assetId}`,
    method: 'post',
    data: transData(adjustData)
  })
}

/**
 * 获取折旧统计
 * @param {Object} params 统计参数
 * @returns {Promise}
 */
export function getDepreciationStatistics(params) {
  return request({
    url: '/cwgxAi/fixedAssets/depreciation/statistics',
    method: 'get',
    params: transData(params)
  })
}

// ==================== 资产变动管理 API ====================

/**
 * 获取资产变动列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getAssetChangesList(params) {
  return request({
    url: '/cwgxAi/fixedAssets/assetChange/list',
    method: 'get',
    params: transData(params)
  })
}

/**
 * 创建资产变动
 * @param {Object} data 变动数据
 * @returns {Promise}
 */
export function createAssetChange(data) {
  return request({
    url: '/cwgxAi/fixedAssets/assetChange',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 审批资产变动
 * @param {String} changeId 变动ID
 * @param {Object} approvalData 审批数据
 * @returns {Promise}
 */
export function approveAssetChange(changeId, approvalData) {
  return request({
    url: `/cwgxAi/fixedAssets/assetChange/approve/${changeId}`,
    method: 'post',
    data: transData(approvalData)
  })
}

/**
 * 获取资产变动详情
 * @param {String} changeId 变动ID
 * @returns {Promise}
 */
export function getAssetChangeDetail(changeId) {
  return request({
    url: `/cwgxAi/fixedAssets/assetChange/${changeId}`,
    method: 'get'
  })
}

// ==================== 资产处置管理 API ====================

/**
 * 获取资产处置列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getAssetDisposalList(params) {
  return request({
    url: '/cwgxAi/fixedAssets/assetDisposal/list',
    method: 'get',
    params: transData(params)
  })
}

/**
 * 创建资产处置
 * @param {Object} data 处置数据
 * @returns {Promise}
 */
export function createAssetDisposal(data) {
  return request({
    url: '/cwgxAi/fixedAssets/assetDisposal',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 审批资产处置
 * @param {String} disposalId 处置ID
 * @param {Object} approvalData 审批数据
 * @returns {Promise}
 */
export function approveAssetDisposal(disposalId, approvalData) {
  return request({
    url: `/cwgxAi/fixedAssets/assetDisposal/approve/${disposalId}`,
    method: 'post',
    data: transData(approvalData)
  })
}

/**
 * 获取资产处置详情
 * @param {String} disposalId 处置ID
 * @returns {Promise}
 */
export function getAssetDisposalDetail(disposalId) {
  return request({
    url: `/cwgxAi/fixedAssets/assetDisposal/${disposalId}`,
    method: 'get'
  })
}

// ==================== 资产盘点管理 API ====================

/**
 * 创建资产盘点任务
 * @param {Object} data 盘点任务数据
 * @returns {Promise}
 */
export function createInventoryTask(data) {
  return request({
    url: '/cwgxAi/fixedAssets/inventory/create',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取盘点任务列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getInventoryTaskList(params) {
  return request({
    url: '/cwgxAi/fixedAssets/inventory/tasks',
    method: 'get',
    params: transData(params)
  })
}

/**
 * 执行盘点任务
 * @param {String} taskId 任务ID
 * @returns {Promise}
 */
export function executeInventoryTask(taskId) {
  return request({
    url: `/cwgxAi/fixedAssets/inventory/execute/${taskId}`,
    method: 'post'
  })
}

/**
 * 获取盘点结果
 * @param {String} taskId 任务ID
 * @returns {Promise}
 */
export function getInventoryResult(taskId) {
  return request({
    url: `/cwgxAi/fixedAssets/inventory/result/${taskId}`,
    method: 'get'
  })
}

/**
 * 提交盘点结果
 * @param {String} taskId 任务ID
 * @param {Object} resultData 结果数据
 * @returns {Promise}
 */
export function submitInventoryResult(taskId, resultData) {
  return request({
    url: `/cwgxAi/fixedAssets/inventory/submit/${taskId}`,
    method: 'post',
    data: transData(resultData)
  })
}

// ==================== 统计分析 API ====================

/**
 * 获取固定资产统计
 * @param {Object} params 统计参数
 * @returns {Promise}
 */
export function getAssetStatistics(params = {}) {
  return request({
    url: '/cwgxAi/fixedAssets/statistics',
    method: 'get',
    params: transData(params)
  })
}

/**
 * 获取资产分类统计
 * @param {Object} params 统计参数
 * @returns {Promise}
 */
export function getAssetCategoryStatistics(params) {
  return request({
    url: '/cwgxAi/fixedAssets/statistics/category',
    method: 'get',
    params: transData(params)
  })
}

/**
 * 获取资产状态统计
 * @param {Object} params 统计参数
 * @returns {Promise}
 */
export function getAssetStatusStatistics(params) {
  return request({
    url: '/cwgxAi/fixedAssets/statistics/status',
    method: 'get',
    params: transData(params)
  })
}

/**
 * 获取折旧趋势分析
 * @param {Object} params 分析参数
 * @returns {Promise}
 */
export function getDepreciationTrend(params) {
  return request({
    url: '/cwgxAi/fixedAssets/statistics/depreciation-trend',
    method: 'get',
    params: transData(params)
  })
}

/**
 * 获取资产价值分析
 * @param {Object} params 分析参数
 * @returns {Promise}
 */
export function getAssetValueAnalysis(params) {
  return request({
    url: '/cwgxAi/fixedAssets/statistics/value-analysis',
    method: 'get',
    params: transData(params)
  })
}

// ==================== 资产类别管理 API ====================

/**
 * 获取资产类别列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getAssetCategoryList(params) {
  return request({
    url: '/cwgxAi/fa/category/getList',
    method: 'post',
    data: transData(params)
  })
}

/**
 * 保存资产类别
 * @param {Object} data 类别数据
 * @returns {Promise}
 */
export function saveAssetCategory(data) {
  return request({
    url: '/cwgxAi/fa/category/saveOrUpdate',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 删除资产类别
 * @param {String} categoryId 类别ID
 * @returns {Promise}
 */
export function deleteAssetCategory(categoryId) {
  return request({
    url: `/cwgxAi/fa/category/delete/${categoryId}`,
    method: 'delete'
  })
}

/**
 * 获取资产类别详情
 * @param {String} categoryId 类别ID
 * @returns {Promise}
 */
export function getAssetCategoryDetail(categoryId) {
  return request({
    url: `/cwgxAi/fa/category/getById/${categoryId}`,
    method: 'get'
  })
}

// ==================== 使用权资产管理 API ====================

/**
 * 获取使用权资产列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getRightOfUseAssetList(params) {
  return request({
    url: '/cwgxAi/fa/rightofuse/getList',
    method: 'post',
    data: transData(params)
  })
}

/**
 * 确认使用权资产
 * @param {Object} data 确认数据
 * @returns {Promise}
 */
export function recognizeRightOfUseAsset(data) {
  return request({
    url: '/cwgxAi/fa/rightofuse/recognize',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 使用权资产折旧计算
 * @param {Object} data 计算参数
 * @returns {Promise}
 */
export function calculateRightOfUseDepreciation(data) {
  return request({
    url: '/cwgxAi/fa/rightofuse/depreciation',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取使用权资产详情
 * @param {String} assetId 资产ID
 * @returns {Promise}
 */
export function getRightOfUseAssetDetail(assetId) {
  return request({
    url: `/cwgxAi/fa/rightofuse/getById/${assetId}`,
    method: 'get'
  })
}

// ==================== 多账簿资产核算 API ====================

/**
 * 多账簿折旧计算
 * @param {Object} data 计算参数
 * @returns {Promise}
 */
export function calculateMultiBookDepreciation(data) {
  return request({
    url: '/cwgxAi/fa/multibook/depreciation',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取多账簿资产信息
 * @param {String} assetId 资产ID
 * @returns {Promise}
 */
export function getMultiBookAssetInfo(assetId) {
  return request({
    url: `/cwgxAi/fa/multibook/getAssetInfo/${assetId}`,
    method: 'get'
  })
}

/**
 * 同步多账簿资产数据
 * @param {Object} data 同步参数
 * @returns {Promise}
 */
export function syncMultiBookAssetData(data) {
  return request({
    url: '/cwgxAi/fa/multibook/sync',
    method: 'post',
    data: transData(data)
  })
}

// ==================== 报表管理 API ====================

/**
 * 生成固定资产报表
 * @param {Object} params 报表参数
 * @returns {Promise}
 */
export function generateAssetReport(params) {
  return request({
    url: '/cwgxAi/fixedAssets/reports/generate',
    method: 'post',
    data: transData(params)
  })
}

/**
 * 获取资产清单报表
 * @param {Object} params 报表参数
 * @returns {Promise}
 */
export function getAssetListReport(params) {
  return request({
    url: '/cwgxAi/fixedAssets/reports/asset-list',
    method: 'get',
    params: transData(params)
  })
}

/**
 * 获取折旧明细报表
 * @param {Object} params 报表参数
 * @returns {Promise}
 */
export function getDepreciationDetailReport(params) {
  return request({
    url: '/cwgxAi/fixedAssets/reports/depreciation-detail',
    method: 'get',
    params: transData(params)
  })
}

/**
 * 获取资产变动报表
 * @param {Object} params 报表参数
 * @returns {Promise}
 */
export function getAssetChangeReport(params) {
  return request({
    url: '/cwgxAi/fixedAssets/reports/asset-change',
    method: 'get',
    params: transData(params)
  })
}

/**
 * 导出报表
 * @param {String} reportType 报表类型
 * @param {Object} params 导出参数
 * @returns {Promise}
 */
export function exportAssetReport(reportType, params) {
  return request({
    url: `/cwgxAi/fixedAssets/reports/export/${reportType}`,
    method: 'post',
    data: transData(params),
    responseType: 'blob'
  })
}
