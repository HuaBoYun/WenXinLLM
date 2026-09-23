import request from '@/utils/request'

// ========== 基础CRUD操作 ==========

/**
 * 分页查询产品成本列表
 */
export function getProductCostList(data) {
  return request({
    url: '/cwgxAi/ma/productcost/getList',
    method: 'post',
    data
  })
}

/**
 * 根据ID查询产品成本详情
 */
export function getProductCostById(costingId) {
  return request({
    url: '/cwgxAi/ma/productcost/getById',
    method: 'get',
    params: { costingId }
  })
}

/**
 * 新增产品成本
 */
export function createProductCost(data) {
  return request({
    url: '/cwgxAi/ma/productcost/create',
    method: 'post',
    data
  })
}

/**
 * 修改产品成本
 */
export function updateProductCost(data) {
  return request({
    url: '/cwgxAi/ma/productcost/update',
    method: 'post',
    data
  })
}

/**
 * 删除产品成本
 */
export function deleteProductCost(costingId) {
  return request({
    url: '/cwgxAi/ma/productcost/delete',
    method: 'post',
    params: { costingId }
  })
}

/**
 * 批量删除产品成本
 */
export function batchDeleteProductCost(costingIds) {
  return request({
    url: '/cwgxAi/ma/productcost/batchDelete',
    method: 'post',
    data: costingIds
  })
}

// ========== 产品信息管理 ==========

/**
 * 查询产品信息列表
 */
export function getProductInfoList(data) {
  return request({
    url: '/cwgxAi/ma/productcost/product/getList',
    method: 'post',
    data
  })
}

/**
 * 新增产品信息
 */
export function createProductInfo(data) {
  return request({
    url: '/cwgxAi/ma/productcost/product/create',
    method: 'post',
    data
  })
}

/**
 * 修改产品信息
 */
export function updateProductInfo(data) {
  return request({
    url: '/cwgxAi/ma/productcost/product/update',
    method: 'post',
    data
  })
}

/**
 * 删除产品信息
 */
export function deleteProductInfo(productId) {
  return request({
    url: '/cwgxAi/ma/productcost/product/delete',
    method: 'post',
    params: { productId }
  })
}

/**
 * 查询产品BOM信息
 */
export function getProductBomInfo(productId) {
  return request({
    url: '/cwgxAi/ma/productcost/product/getBomInfo',
    method: 'get',
    params: { productId }
  })
}

/**
 * 更新产品BOM信息
 */
export function updateProductBomInfo(data) {
  return request({
    url: '/cwgxAi/ma/productcost/product/updateBomInfo',
    method: 'post',
    data
  })
}

// ========== 成本核算 ==========

/**
 * 执行成本核算
 */
export function executeCostAccounting(data) {
  return request({
    url: '/cwgxAi/ma/productcost/accounting/execute',
    method: 'post',
    data
  })
}

/**
 * 查询成本核算结果
 */
export function getCostAccountingResults(data) {
  return request({
    url: '/cwgxAi/ma/productcost/accounting/getResults',
    method: 'post',
    data
  })
}

/**
 * 重新计算成本
 */
export function recalculateCost(costingId) {
  return request({
    url: '/cwgxAi/ma/productcost/accounting/recalculate',
    method: 'post',
    params: { costingId }
  })
}

/**
 * 批量成本核算
 */
export function batchCostAccounting(data) {
  return request({
    url: '/cwgxAi/ma/productcost/accounting/batchExecute',
    method: 'post',
    data
  })
}

/**
 * 查询成本核算方法配置
 */
export function getCostingMethodConfig() {
  return request({
    url: '/cwgxAi/ma/productcost/accounting/getMethodConfig',
    method: 'get'
  })
}

/**
 * 更新成本核算方法配置
 */
export function updateCostingMethodConfig(data) {
  return request({
    url: '/cwgxAi/ma/productcost/accounting/updateMethodConfig',
    method: 'post',
    data
  })
}

/**
 * 查询成本核算进度
 */
export function getCostingProgress(costingPeriod) {
  return request({
    url: '/cwgxAi/ma/productcost/accounting/getProgress',
    method: 'get',
    params: { costingPeriod }
  })
}

// ========== 成本分析 ==========

/**
 * 成本构成分析
 */
export function getCostCompositionAnalysis(data) {
  return request({
    url: '/cwgxAi/ma/productcost/analysis/composition',
    method: 'post',
    data
  })
}

/**
 * 成本趋势分析
 */
export function getCostTrendAnalysis(data) {
  return request({
    url: '/cwgxAi/ma/productcost/analysis/trend',
    method: 'post',
    data
  })
}

/**
 * 成本对比分析
 */
export function getCostComparisonAnalysis(data) {
  return request({
    url: '/cwgxAi/ma/productcost/analysis/comparison',
    method: 'post',
    data
  })
}

/**
 * 成本差异分析
 */
export function getCostVarianceAnalysis(data) {
  return request({
    url: '/cwgxAi/ma/productcost/analysis/variance',
    method: 'post',
    data
  })
}

/**
 * 产品成本排名
 */
export function getProductCostRanking(data) {
  return request({
    url: '/cwgxAi/ma/productcost/analysis/ranking',
    method: 'post',
    data
  })
}

/**
 * 成本中心成本汇总
 */
export function getCostCenterSummary(data) {
  return request({
    url: '/cwgxAi/ma/productcost/analysis/costCenterSummary',
    method: 'post',
    data
  })
}

/**
 * 生成成本分析报告
 */
export function generateCostAnalysisReport(data) {
  return request({
    url: '/cwgxAi/ma/productcost/analysis/generateReport',
    method: 'post',
    data
  })
}

// ========== 成本控制 ==========

/**
 * 查询预算执行情况
 */
export function getBudgetExecution(data) {
  return request({
    url: '/cwgxAi/ma/productcost/control/getBudgetExecution',
    method: 'post',
    data
  })
}

/**
 * 查询成本预警信息
 */
export function getCostAlerts(data) {
  return request({
    url: '/cwgxAi/ma/productcost/control/getAlerts',
    method: 'post',
    data
  })
}

/**
 * 查询成本异常数据
 */
export function getCostAnomalies(data) {
  return request({
    url: '/cwgxAi/ma/productcost/control/getAnomalies',
    method: 'post',
    data
  })
}

/**
 * 设置成本预警规则
 */
export function setCostAlertRules(data) {
  return request({
    url: '/cwgxAi/ma/productcost/control/setAlertRules',
    method: 'post',
    data
  })
}

/**
 * 执行成本控制措施
 */
export function executeCostControlMeasures(data) {
  return request({
    url: '/cwgxAi/ma/productcost/control/executeMeasures',
    method: 'post',
    data
  })
}

/**
 * 查询成本控制效果
 */
export function getCostControlEffectiveness(data) {
  return request({
    url: '/cwgxAi/ma/productcost/control/getEffectiveness',
    method: 'post',
    data
  })
}

// ========== BOM管理 ==========

/**
 * 查询BOM列表
 */
export function getBomList(data) {
  return request({
    url: '/cwgxAi/ma/productcost/bom/getList',
    method: 'post',
    data
  })
}

/**
 * 新增BOM
 */
export function createBom(data) {
  return request({
    url: '/cwgxAi/ma/productcost/bom/create',
    method: 'post',
    data
  })
}

/**
 * 修改BOM
 */
export function updateBom(data) {
  return request({
    url: '/cwgxAi/ma/productcost/bom/update',
    method: 'post',
    data
  })
}

/**
 * 删除BOM
 */
export function deleteBom(bomId) {
  return request({
    url: '/cwgxAi/ma/productcost/bom/delete',
    method: 'post',
    params: { bomId }
  })
}

/**
 * 查询BOM明细
 */
export function getBomDetails(bomId) {
  return request({
    url: '/cwgxAi/ma/productcost/bom/getDetails',
    method: 'get',
    params: { bomId }
  })
}

/**
 * 更新BOM明细
 */
export function updateBomDetails(data) {
  return request({
    url: '/cwgxAi/ma/productcost/bom/updateDetails',
    method: 'post',
    data
  })
}

/**
 * BOM版本管理
 */
export function getBomVersions(productId) {
  return request({
    url: '/cwgxAi/ma/productcost/bom/getVersions',
    method: 'get',
    params: { productId }
  })
}

/**
 * 激活BOM版本
 */
export function activateBomVersion(bomId, version) {
  return request({
    url: '/cwgxAi/ma/productcost/bom/activateVersion',
    method: 'post',
    params: { bomId, version }
  })
}

/**
 * BOM成本计算
 */
export function calculateBomCost(bomId) {
  return request({
    url: '/cwgxAi/ma/productcost/bom/calculateCost',
    method: 'get',
    params: { bomId }
  })
}

// ========== 成本报告 ==========

/**
 * 查询成本报告列表
 */
export function getCostReportList(data) {
  return request({
    url: '/cwgxAi/ma/productcost/report/getList',
    method: 'post',
    data
  })
}

/**
 * 生成成本报告
 */
export function generateCostReport(data) {
  return request({
    url: '/cwgxAi/ma/productcost/report/generate',
    method: 'post',
    data
  })
}

/**
 * 下载成本报告
 */
export function downloadCostReport(reportId) {
  return request({
    url: '/cwgxAi/ma/productcost/report/download',
    method: 'get',
    params: { reportId }
  })
}

/**
 * 删除成本报告
 */
export function deleteCostReport(reportId) {
  return request({
    url: '/cwgxAi/ma/productcost/report/delete',
    method: 'post',
    params: { reportId }
  })
}

/**
 * 查询报告模板
 */
export function getReportTemplates() {
  return request({
    url: '/cwgxAi/ma/productcost/report/getTemplates',
    method: 'get'
  })
}

/**
 * 保存报告模板
 */
export function saveReportTemplate(data) {
  return request({
    url: '/cwgxAi/ma/productcost/report/saveTemplate',
    method: 'post',
    data
  })
}

// ========== 数据导入导出 ==========

/**
 * 导出产品成本数据
 */
export function exportProductCostData(data) {
  return request({
    url: '/cwgxAi/ma/productcost/export',
    method: 'post',
    data
  })
}

/**
 * 导入产品成本数据
 */
export function importProductCostData(data) {
  return request({
    url: '/cwgxAi/ma/productcost/import',
    method: 'post',
    data
  })
}

/**
 * 下载导入模板
 */
export function downloadImportTemplate(templateType) {
  return request({
    url: '/cwgxAi/ma/productcost/downloadTemplate',
    method: 'get',
    params: { templateType }
  })
}

/**
 * 验证导入数据
 */
export function validateImportData(data) {
  return request({
    url: '/cwgxAi/ma/productcost/validateImport',
    method: 'post',
    data
  })
}

// ========== 审核流程 ==========

/**
 * 提交审核
 */
export function submitForAudit(costingIds) {
  return request({
    url: '/cwgxAi/ma/productcost/audit/submit',
    method: 'post',
    data: costingIds
  })
}

/**
 * 审核通过
 */
export function approveAudit(data) {
  return request({
    url: '/cwgxAi/ma/productcost/audit/approve',
    method: 'post',
    data
  })
}

/**
 * 审核驳回
 */
export function rejectAudit(data) {
  return request({
    url: '/cwgxAi/ma/productcost/audit/reject',
    method: 'post',
    data
  })
}

/**
 * 查询待审核列表
 */
export function getPendingAuditList(data) {
  return request({
    url: '/cwgxAi/ma/productcost/audit/getPendingList',
    method: 'post',
    data
  })
}

/**
 * 查询审核历史
 */
export function getAuditHistory(costingId) {
  return request({
    url: '/cwgxAi/ma/productcost/audit/getHistory',
    method: 'get',
    params: { costingId }
  })
}

// ========== 凭证生成 ==========

/**
 * 生成成本凭证
 */
export function generateCostVoucher(costingIds) {
  return request({
    url: '/cwgxAi/ma/productcost/voucher/generate',
    method: 'post',
    data: costingIds
  })
}

/**
 * 查询凭证生成状态
 */
export function getVoucherGenerationStatus(data) {
  return request({
    url: '/cwgxAi/ma/productcost/voucher/getStatus',
    method: 'post',
    data
  })
}

/**
 * 重新生成凭证
 */
export function regenerateVoucher(costingId) {
  return request({
    url: '/cwgxAi/ma/productcost/voucher/regenerate',
    method: 'post',
    params: { costingId }
  })
}

// ========== 成本分摊 ==========

/**
 * 执行成本分摊
 */
export function executeCostAllocation(data) {
  return request({
    url: '/cwgxAi/ma/productcost/allocation/execute',
    method: 'post',
    data
  })
}

/**
 * 查询分摊规则
 */
export function getAllocationRules() {
  return request({
    url: '/cwgxAi/ma/productcost/allocation/getRules',
    method: 'get'
  })
}

/**
 * 设置分摊规则
 */
export function setAllocationRules(data) {
  return request({
    url: '/cwgxAi/ma/productcost/allocation/setRules',
    method: 'post',
    data
  })
}

/**
 * 查询分摊结果
 */
export function getAllocationResults(data) {
  return request({
    url: '/cwgxAi/ma/productcost/allocation/getResults',
    method: 'post',
    data
  })
}

// ========== 成本归集 ==========

/**
 * 执行成本归集
 */
export function executeCostCollection(data) {
  return request({
    url: '/cwgxAi/ma/productcost/collection/execute',
    method: 'post',
    data
  })
}

/**
 * 查询归集规则
 */
export function getCollectionRules() {
  return request({
    url: '/cwgxAi/ma/productcost/collection/getRules',
    method: 'get'
  })
}

/**
 * 设置归集规则
 */
export function setCollectionRules(data) {
  return request({
    url: '/cwgxAi/ma/productcost/collection/setRules',
    method: 'post',
    data
  })
}

/**
 * 查询归集结果
 */
export function getCollectionResults(data) {
  return request({
    url: '/cwgxAi/ma/productcost/collection/getResults',
    method: 'post',
    data
  })
}

// ========== 统计分析 ==========

/**
 * 成本统计概览
 */
export function getCostStatisticsOverview(data) {
  return request({
    url: '/cwgxAi/ma/productcost/statistics/overview',
    method: 'post',
    data
  })
}

/**
 * 成本分布统计
 */
export function getCostDistributionStatistics(data) {
  return request({
    url: '/cwgxAi/ma/productcost/statistics/distribution',
    method: 'post',
    data
  })
}

/**
 * 成本变化趋势
 */
export function getCostChangeTrend(data) {
  return request({
    url: '/cwgxAi/ma/productcost/statistics/changeTrend',
    method: 'post',
    data
  })
}

/**
 * 成本效率分析
 */
export function getCostEfficiencyAnalysis(data) {
  return request({
    url: '/cwgxAi/ma/productcost/statistics/efficiency',
    method: 'post',
    data
  })
}

/**
 * 成本质量分析
 */
export function getCostQualityAnalysis(data) {
  return request({
    url: '/cwgxAi/ma/productcost/statistics/quality',
    method: 'post',
    data
  })
}

// ==================== 扩展功能接口 ====================

/**
 * 获取产品成本页面数据（统一分页接口）
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getProductCostPage(data) {
  return request({
    url: '/cwgxAi/ma/productcost/getList',
    method: 'post',
    data
  })
}

/**
 * 保存或更新产品成本（统一保存接口）
 * @param {Object} data 成本数据
 * @returns {Promise}
 */
export function saveOrUpdateProductCost(data) {
  return request({
    url: '/cwgxAi/ma/productcost/saveOrUpdate',
    method: 'post',
    data
  })
}

/**
 * 获取产品成本概览数据
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getProductCostOverview(params) {
  return request({
    url: '/cwgxAi/ma/productcost/overview',
    method: 'get',
    params
  })
}

/**
 * 计算产品成本
 * @param {Object} data 计算参数
 * @returns {Promise}
 */
export function calculateProductCost(data) {
  return request({
    url: '/cwgxAi/ma/productcost/calculate',
    method: 'post',
    data
  })
}

/**
 * 获取产品信息页面数据
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getProductInfoPage(data) {
  return request({
    url: '/cwgxAi/ma/productcost/product/getList',
    method: 'post',
    data
  })
}

/**
 * 保存或更新产品信息
 * @param {Object} data 产品数据
 * @returns {Promise}
 */
export function saveOrUpdateProductInfo(data) {
  return request({
    url: '/cwgxAi/ma/productcost/product/saveOrUpdate',
    method: 'post',
    data
  })
}

/**
 * 启用/停用产品
 * @param {Number} productId 产品ID
 * @param {Number} isEnabled 是否启用(1启用0停用)
 * @returns {Promise}
 */
export function updateProductStatus(productId, isEnabled) {
  return request({
    url: `/cwgxAi/ma/productcost/product/${productId}/status`,
    method: 'post',
    data: { isEnabled }
  })
}

/**
 * 获取BOM管理页面数据（别名）
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getBomPage(data) {
  return getBomList(data)
}

/**
 * 保存或更新BOM（扩展接口）
 * @param {Object} data BOM数据
 * @returns {Promise}
 */
export function saveOrUpdateBom(data) {
  return request({
    url: '/cwgxAi/ma/productcost/bom/saveOrUpdate',
    method: 'post',
    data
  })
}

/**
 * 复制BOM（扩展接口）
 * @param {Number} bomId BOM ID
 * @param {Object} data 复制参数
 * @returns {Promise}
 */
export function copyBom(bomId, data) {
  return request({
    url: `/cwgxAi/ma/productcost/bom/${bomId}/copy`,
    method: 'post',
    data
  })
}

/**
 * 审核BOM（扩展接口）
 * @param {Number} bomId BOM ID
 * @param {Object} data 审核数据
 * @returns {Promise}
 */
export function auditBom(bomId, data) {
  return request({
    url: `/cwgxAi/ma/productcost/bom/${bomId}/audit`,
    method: 'post',
    data
  })
}

/**
 * 获取BOM树形结构（扩展接口）
 * @param {Number} bomId BOM ID
 * @returns {Promise}
 */
export function getBomTree(bomId) {
  return request({
    url: `/cwgxAi/ma/productcost/bom/${bomId}/tree`,
    method: 'get'
  })
}

/**
 * 获取成本核算页面数据
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getCostAccountingPage(data) {
  return request({
    url: '/cwgxAi/ma/productcost/accounting/getList',
    method: 'post',
    data
  })
}

/**
 * 保存或更新成本核算
 * @param {Object} data 核算数据
 * @returns {Promise}
 */
export function saveOrUpdateCostAccounting(data) {
  return request({
    url: '/cwgxAi/ma/productcost/accounting/saveOrUpdate',
    method: 'post',
    data
  })
}

/**
 * 获取成本控制页面数据
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getCostControlPage(data) {
  return request({
    url: '/cwgxAi/ma/productcost/control/getList',
    method: 'post',
    data
  })
}

/**
 * 保存或更新成本控制
 * @param {Object} data 控制数据
 * @returns {Promise}
 */
export function saveOrUpdateCostControl(data) {
  return request({
    url: '/cwgxAi/ma/productcost/control/saveOrUpdate',
    method: 'post',
    data
  })
}

/**
 * 获取成本报告页面数据
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getCostReportPage(data) {
  return request({
    url: '/cwgxAi/ma/productcost/report/getList',
    method: 'post',
    data
  })
}

/**
 * 保存或更新成本报告
 * @param {Object} data 报告数据
 * @returns {Promise}
 */
export function saveOrUpdateCostReport(data) {
  return request({
    url: '/cwgxAi/ma/productcost/report/saveOrUpdate',
    method: 'post',
    data
  })
}

/**
 * 生成成本报告（扩展接口）
 * @param {Object} data 报告参数
 * @returns {Promise}
 */
export function generateCostReportExtended(data) {
  return request({
    url: '/cwgxAi/ma/productcost/report/generate',
    method: 'post',
    data
  })
}

/**
 * 获取成本分析页面数据
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getCostAnalysisPage(data) {
  return request({
    url: '/cwgxAi/ma/productcost/analysis/getList',
    method: 'post',
    data
  })
}

/**
 * 保存或更新成本分析
 * @param {Object} data 分析数据
 * @returns {Promise}
 */
export function saveOrUpdateCostAnalysis(data) {
  return request({
    url: '/cwgxAi/ma/productcost/analysis/saveOrUpdate',
    method: 'post',
    data
  })
}

/**
 * 导出产品成本数据（扩展接口，支持blob响应）
 * @param {Object} data 导出参数
 * @returns {Promise}
 */
export function exportProductCostDataBlob(data) {
  return request({
    url: '/cwgxAi/ma/productcost/export',
    method: 'post',
    data,
    responseType: 'blob'
  })
}
