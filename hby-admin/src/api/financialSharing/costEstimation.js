import request from '@/utils/request'

/**
 * 成本估算API接口
 */

// ==================== 基础CRUD接口 ====================

/**
 * 分页查询成本估算列表
 * @param {Object} params 查询参数
 */
export function getCostEstimateList(params) {
  return request({
    url: '/cwgxAi/ma/costestimate/getList',
    method: 'post',
    data: params
  })
}

/**
 * 根据ID查询成本估算详情
 * @param {Number} estimateId 估算ID
 */
export function getCostEstimateById(estimateId) {
  return request({
    url: `/cwgxAi/ma/costestimate/getById/${estimateId}`,
    method: 'get'
  })
}

/**
 * 根据编号查询成本估算
 * @param {String} estimateNo 估算编号
 * @param {Number} tenantId 租户ID
 */
export function getCostEstimateByNo(estimateNo, tenantId) {
  return request({
    url: '/cwgxAi/ma/costestimate/getByNo',
    method: 'get',
    params: { estimateNo, tenantId }
  })
}

/**
 * 新增成本估算
 * @param {Object} data 成本估算数据
 */
export function createCostEstimate(data) {
  return request({
    url: '/cwgxAi/ma/costestimate/create',
    method: 'post',
    data
  })
}

/**
 * 更新成本估算
 * @param {Number} estimateId 估算ID
 * @param {Object} data 成本估算数据
 */
export function updateCostEstimate(estimateId, data) {
  return request({
    url: `/cwgxAi/ma/costestimate/update/${estimateId}`,
    method: 'post',
    data
  })
}

/**
 * 删除成本估算
 * @param {Number} estimateId 估算ID
 */
export function deleteCostEstimate(estimateId) {
  return request({
    url: `/cwgxAi/ma/costestimate/delete/${estimateId}`,
    method: 'post'
  })
}

/**
 * 批量删除成本估算
 * @param {Array} estimateIds 估算ID列表
 */
export function batchDeleteCostEstimate(estimateIds) {
  return request({
    url: '/cwgxAi/ma/costestimate/batchDelete',
    method: 'post',
    data: estimateIds
  })
}

/**
 * 复制成本估算
 * @param {Number} sourceId 源估算ID
 * @param {Object} data 复制参数
 */
export function copyCostEstimate(sourceId, data) {
  return request({
    url: `/cwgxAi/ma/costestimate/copy/${sourceId}`,
    method: 'post',
    data
  })
}

// ==================== 业务功能接口 ====================

/**
 * 执行成本估算
 * @param {Object} data 估算参数
 */
export function executeCostEstimate(data) {
  return request({
    url: '/cwgxAi/ma/costestimate/execute',
    method: 'post',
    data
  })
}

/**
 * 提交成本估算审批
 * @param {Number} estimateId 估算ID
 * @param {Object} data 审批参数
 */
export function submitCostEstimateApproval(estimateId, data) {
  return request({
    url: `/cwgxAi/ma/costestimate/submitApproval/${estimateId}`,
    method: 'post',
    data
  })
}

/**
 * 审批成本估算
 * @param {Number} estimateId 估算ID
 * @param {Object} data 审批参数
 */
export function approveCostEstimate(estimateId, data) {
  return request({
    url: `/cwgxAi/ma/costestimate/approve/${estimateId}`,
    method: 'post',
    data
  })
}

/**
 * 查询产品的最新成本估算
 * @param {Number} productId 产品ID
 * @param {Number} tenantId 租户ID
 */
export function getLatestCostEstimateByProduct(productId, tenantId) {
  return request({
    url: '/cwgxAi/ma/costestimate/getLatestByProduct',
    method: 'get',
    params: { productId, tenantId }
  })
}

/**
 * 查询产品在指定期间的成本估算
 * @param {Number} productId 产品ID
 * @param {String} estimatePeriod 估算期间
 * @param {Number} tenantId 租户ID
 */
export function getCostEstimateByProductAndPeriod(productId, estimatePeriod, tenantId) {
  return request({
    url: '/cwgxAi/ma/costestimate/getByProductAndPeriod',
    method: 'get',
    params: { productId, estimatePeriod, tenantId }
  })
}

// ==================== 统计分析接口 ====================

/**
 * 查询成本估算统计信息
 * @param {Object} params 查询参数
 */
export function getCostEstimateStatistics(params) {
  return request({
    url: '/cwgxAi/ma/costestimate/getStatistics',
    method: 'post',
    data: params
  })
}

/**
 * 查询成本估算趋势数据
 * @param {Number} productId 产品ID
 * @param {String} startPeriod 开始期间
 * @param {String} endPeriod 结束期间
 * @param {Number} tenantId 租户ID
 */
export function getCostEstimateTrend(productId, startPeriod, endPeriod, tenantId) {
  return request({
    url: '/cwgxAi/ma/costestimate/getTrend',
    method: 'get',
    params: { productId, startPeriod, endPeriod, tenantId }
  })
}

/**
 * 查询成本估算差异分析数据
 * @param {Object} params 查询参数
 */
export function getCostEstimateVarianceAnalysis(params) {
  return request({
    url: '/cwgxAi/ma/costestimate/getVarianceAnalysis',
    method: 'post',
    data: params
  })
}

/**
 * 查询成本估算汇总数据
 * @param {Object} params 查询参数
 * @description 后端使用 @RequestBody，需声明 JSON Content-Type，绕开全局 form-urlencoded 序列化
 */
export function getCostEstimateSummary(params) {
  return request({
    url: '/cwgxAi/ma/costestimate/getSummary',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data: params || {}
  })
}

/**
 * 成本估算模拟计算
 * @param {Object} data 模拟参数
 */
export function simulateCostEstimate(data) {
  return request({
    url: '/cwgxAi/ma/costestimate/simulate',
    method: 'post',
    data
  })
}

/**
 * 查询成本估算预算对比数据
 * @param {Object} params 查询参数
 */
export function getCostEstimateBudgetComparison(params) {
  return request({
    url: '/cwgxAi/ma/costestimate/getBudgetComparison',
    method: 'post',
    data: params
  })
}

/**
 * 查询成本估算准确率统计
 * @param {Object} params 查询参数
 */
export function getCostEstimateAccuracyStatistics(params) {
  return request({
    url: '/cwgxAi/ma/costestimate/getAccuracyStatistics',
    method: 'post',
    data: params
  })
}

/**
 * 查询成本估算成本构成分析
 * @param {Number} estimateId 估算ID
 */
export function getCostEstimateComposition(estimateId) {
  return request({
    url: `/cwgxAi/ma/costestimate/getComposition/${estimateId}`,
    method: 'get'
  })
}

/**
 * 查询成本估算风险评估数据
 * @param {Object} params 查询参数
 */
export function getCostEstimateRiskAssessment(params) {
  return request({
    url: '/cwgxAi/ma/costestimate/getRiskAssessment',
    method: 'post',
    data: params
  })
}

// ==================== 数据管理接口 ====================

/**
 * 批量导入成本估算数据
 * @param {Array|Object} estimateList 成本估算列表或包含 list 字段的对象
 * @description 后端 batchImport 接收 Map<String,Object>，前端建议传 { list: [...] } 结构
 */
export function batchImportCostEstimate(estimateList) {
  // 兼容历史调用：若直接传数组，包装为 { list: [...] }
  const data = Array.isArray(estimateList) ? { list: estimateList } : (estimateList || {})
  return request({
    url: '/cwgxAi/ma/costestimate/batchImport',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

/**
 * 导出成本估算数据
 * @param {Object} params 查询参数
 */
export function exportCostEstimateData(params) {
  return request({
    url: '/cwgxAi/ma/costestimate/export',
    method: 'post',
    data: params
  })
}

/**
 * 更新成本估算计算结果
 * @param {Number} estimateId 估算ID
 * @param {Object} data 计算结果参数
 */
export function updateCostEstimateCalculationResult(estimateId, data) {
  return request({
    url: `/cwgxAi/ma/costestimate/updateCalculationResult/${estimateId}`,
    method: 'post',
    data
  })
}

/**
 * 查询成本估算版本历史
 * @param {Number} baselineId 基准版本ID
 * @param {Number} tenantId 租户ID
 */
export function getCostEstimateVersionHistory(baselineId, tenantId) {
  return request({
    url: '/cwgxAi/ma/costestimate/getVersionHistory',
    method: 'get',
    params: { baselineId, tenantId }
  })
}

/**
 * 查询待审批的成本估算
 * @param {Number} tenantId 租户ID
 * @param {Number} approver 审批人
 */
export function getPendingApprovalCostEstimate(tenantId, approver) {
  return request({
    url: '/cwgxAi/ma/costestimate/getPendingApproval',
    method: 'get',
    params: { tenantId, approver }
  })
}

/**
 * 生成成本估算编号
 * @param {String} prefix 前缀
 * @param {Number} tenantId 租户ID
 */
export function generateCostEstimateNo(prefix, tenantId) {
  return request({
    url: '/cwgxAi/ma/costestimate/generateNo',
    method: 'get',
    params: { prefix, tenantId }
  })
}

/**
 * 检查成本估算编号是否存在
 * @param {String} estimateNo 估算编号
 * @param {Number} tenantId 租户ID
 * @param {Number} excludeId 排除的ID
 */
export function checkCostEstimateNoExists(estimateNo, tenantId, excludeId) {
  return request({
    url: '/cwgxAi/ma/costestimate/checkNoExists',
    method: 'get',
    params: { estimateNo, tenantId, excludeId }
  })
}

/**
 * 批量更新成本估算状态
 * @param {Array} estimateIds 估算ID列表
 * @param {Number} status 状态
 */
export function batchUpdateCostEstimateStatus(estimateIds, status) {
  return request({
    url: '/cwgxAi/ma/costestimate/batchUpdateStatus',
    method: 'post',
    params: { estimateIds, status }
  })
}

// ==================== 成本估算子模块接口 ====================

/**
 * 成本估算方案管理 - 获取方案列表
 * @param {Object} params 查询参数
 */
export function getCostEstimateSchemeList(params) {
  return request({
    url: '/cwgxAi/ma/costestimate/scheme/getList',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data: params || {}
  })
}

/**
 * 成本估算方案管理 - 创建方案
 * @param {Object} data 方案参数
 */
export function createCostEstimateScheme(data) {
  return request({
    url: '/cwgxAi/ma/costestimate/scheme/create',
    method: 'post',
    data
  })
}

/**
 * 成本估算方案管理 - 更新方案
 * @param {Number} schemeId 方案ID
 * @param {Object} data 方案参数
 */
export function updateCostEstimateScheme(schemeId, data) {
  return request({
    url: `/cwgxAi/ma/costestimate/scheme/update/${schemeId}`,
    method: 'post',
    data
  })
}

/**
 * 成本估算方案管理 - 删除方案
 * @param {String} schemeId 方案ID
 */
export function deleteCostEstimateScheme(schemeId) {
  return request({
    url: '/cwgxAi/ma/costestimate/scheme/delete',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data: { schemeId }
  })
}

/**
 * 成本模拟 - 执行模拟计算
 * @param {Object} data 模拟参数
 */
export function executeCostSimulation(data) {
  return request({
    url: '/cwgxAi/ma/costestimate/simulation/execute',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data: data || {}
  })
}

/**
 * 差异分析 - 执行差异分析
 * @param {Object} data 分析参数
 */
export function executeVarianceAnalysis(data) {
  return request({
    url: '/cwgxAi/ma/costestimate/variance/execute',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data: data || {}
  })
}

/**
 * 预算编制 - 获取预算列表
 * @param {Object} params 查询参数
 */
export function getBudgetPreparationList(params) {
  return request({
    url: '/cwgxAi/ma/costestimate/budget/getList',
    method: 'post',
    data: params
  })
}

/**
 * 预算编制 - 创建预算
 * @param {Object} data 预算参数
 */
export function createBudgetPreparation(data) {
  return request({
    url: '/cwgxAi/ma/costestimate/budget/create',
    method: 'post',
    data
  })
}

/**
 * 成本模型 - 获取模型列表
 * @param {Object} params 查询参数
 */
export function getCostModelList(params) {
  return request({
    url: '/cwgxAi/ma/costestimate/model/getList',
    method: 'post',
    data: params
  })
}

/**
 * 成本模型 - 创建模型
 * @param {Object} data 模型参数
 */
export function createCostModel(data) {
  return request({
    url: '/cwgxAi/ma/costestimate/model/create',
    method: 'post',
    data
  })
}

/**
 * 估算报告 - 获取报告列表
 * @param {Object} params 查询参数
 */
export function getEstimationReportList(params) {
  return request({
    url: '/cwgxAi/ma/costestimate/report/getList',
    method: 'post',
    data: params
  })
}

/**
 * 估算报告 - 生成报告
 * @param {Object} data 报告参数
 */
export function generateEstimationReport(data) {
  return request({
    url: '/cwgxAi/ma/costestimate/report/generate',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data: data || {}
  })
}

// ==================== 扩展功能接口 ====================

/**
 * 获取成本估算页面数据（统一分页接口）
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getCostEstimatePage(data) {
  return request({
    url: '/cwgxAi/ma/costestimate/getList',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data: data || {}
  })
}

/**
 * 保存或更新成本估算（统一保存接口）
 * @param {Object} data 估算数据
 * @returns {Promise}
 */
export function saveOrUpdateCostEstimate(data) {
  return request({
    url: '/cwgxAi/ma/costestimate/saveOrUpdate',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data: data || {}
  })
}

/**
 * 获取成本估算概览数据
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getCostEstimateOverview(params) {
  return request({
    url: '/cwgxAi/ma/costestimate/overview',
    method: 'get',
    params
  })
}

/**
 * 审核成本估算
 * @param {Number} estimateId 估算ID
 * @param {Object} data 审核数据
 * @returns {Promise}
 */
export function auditCostEstimate(estimateId, data) {
  return request({
    url: `/cwgxAi/ma/costestimate/${estimateId}/audit`,
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data: data || {}
  })
}

/**
 * 获取成本模型页面数据
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getCostModelPage(data) {
  return request({
    url: '/cwgxAi/ma/costestimate/model/getList',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data: data || {}
  })
}

/**
 * 保存或更新成本模型
 * @param {Object} data 模型数据
 * @returns {Promise}
 */
export function saveOrUpdateCostModel(data) {
  return request({
    url: '/cwgxAi/ma/costestimate/model/saveOrUpdate',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data: data || {}
  })
}

/**
 * 删除成本模型（真删除，走 mapper.deleteById）
 * @param {String} modelId 模型ID
 * @returns {Promise}
 */
export function deleteCostModel(modelId) {
  return request({
    url: '/cwgxAi/ma/costestimate/model/delete',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data: { modelId }
  })
}

/**
 * 复制成本模型
 * @param {Number} modelId 模型ID
 * @param {Object} data 复制参数
 * @returns {Promise}
 */
export function copyCostModel(modelId, data) {
  return request({
    url: `/cwgxAi/ma/costestimate/model/${modelId}/copy`,
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data: data || {}
  })
}

/**
 * 验证/测试成本模型
 * @param {String} modelId 模型ID
 * @param {Object|String} testData 测试数据
 * @returns {Promise}
 */
export function validateCostModel(modelId, testData) {
  return request({
    url: '/cwgxAi/ma/costestimate/model/validate',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data: { modelId, testData }
  })
}

/**
 * 启用/停用成本模型
 * @param {String} modelId 模型ID
 * @param {Number} isEnabled 是否启用(1启用0停用)
 * @returns {Promise}
 */
export function updateCostModelStatus(modelId, isEnabled) {
  return request({
    url: '/cwgxAi/ma/costestimate/model/status',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data: { modelId, isEnabled }
  })
}

/**
 * 获取预算准备页面数据
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getBudgetPreparationPage(data) {
  return request({
    url: '/cwgxAi/ma/costestimate/budget/preparation/getList',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data: data || {}
  })
}

/**
 * 保存或更新预算准备
 * @param {Object} data 预算数据
 * @returns {Promise}
 */
export function saveOrUpdateBudgetPreparation(data) {
  return request({
    url: '/cwgxAi/ma/costestimate/budget/preparation/saveOrUpdate',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data: data || {}
  })
}

/**
 * 提交预算准备
 * @param {Number} preparationId 准备ID
 * @returns {Promise}
 * @description 后端实际路径为 /budget/preparation/submit，preparationId 通过 body 传递
 */
export function submitBudgetPreparation(preparationId) {
  return request({
    url: '/cwgxAi/ma/costestimate/budget/preparation/submit',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data: { preparationId, budgetId: preparationId }
  })
}

/**
 * 审批预算准备
 * @param {Number} preparationId 准备ID
 * @param {Object} data 审批数据
 * @returns {Promise}
 * @description 后端实际路径为 /budget/preparation/approve
 */
export function approveBudgetPreparation(preparationId, data) {
  return request({
    url: '/cwgxAi/ma/costestimate/budget/preparation/approve',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data: Object.assign({ preparationId, budgetId: preparationId }, data || {})
  })
}

/**
 * 获取成本仿真页面数据
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getCostSimulationPage(data) {
  return request({
    url: '/cwgxAi/ma/costestimate/simulation/getList',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data: data || {}
  })
}

/**
 * 保存或更新成本仿真
 * @param {Object} data 仿真数据
 * @returns {Promise}
 */
export function saveOrUpdateCostSimulation(data) {
  return request({
    url: '/cwgxAi/ma/costestimate/simulation/saveOrUpdate',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data: data || {}
  })
}

/**
 * 运行成本仿真
 * @param {Number} simulationId 仿真ID
 * @param {Object} data 运行参数
 * @returns {Promise}
 * @description 后端实际路径为 /simulation/run，simulationId 通过 body 传递
 */
export function runCostSimulation(simulationId, data) {
  return request({
    url: '/cwgxAi/ma/costestimate/simulation/run',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data: Object.assign({ simulationId }, data || {})
  })
}

/**
 * 获取估算报告页面数据
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getEstimationReportPage(data) {
  return request({
    url: '/cwgxAi/ma/costestimate/report/getList',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data: data || {}
  })
}

/**
 * 保存或更新估算报告
 * @param {Object} data 报告数据
 * @returns {Promise}
 */
export function saveOrUpdateEstimationReport(data) {
  return request({
    url: '/cwgxAi/ma/costestimate/report/saveOrUpdate',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data: data || {}
  })
}

/**
 * 发布估算报告
 * @param {Number} reportId 报告ID
 * @returns {Promise}
 * @description 后端实际路径为 /report/publish，reportId 通过 body 传递
 */
export function publishEstimationReport(reportId) {
  return request({
    url: '/cwgxAi/ma/costestimate/report/publish',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data: { reportId }
  })
}

/**
 * 物理删除估算报告
 * @param {String} reportId 报告ID
 */
export function deleteEstimationReport(reportId) {
  return request({
    url: '/cwgxAi/ma/costestimate/report/delete',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data: { reportId }
  })
}

/**
 * 按报告ID查询完整报告（含 reportContent）
 * @param {String} reportId 报告ID
 */
export function getEstimationReportById(reportId) {
  return request({
    url: '/cwgxAi/ma/costestimate/report/getById',
    method: 'get',
    params: { reportId }
  })
}

/**
 * 获取方案管理页面数据
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getSchemeManagementPage(data) {
  return request({
    url: '/cwgxAi/ma/costestimate/scheme/getList',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data: data || {}
  })
}

/**
 * 保存或更新方案管理
 * @param {Object} data 方案数据
 * @returns {Promise}
 */
export function saveOrUpdateSchemeManagement(data) {
  return request({
    url: '/cwgxAi/ma/costestimate/scheme/saveOrUpdate',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data: data || {}
  })
}

/**
 * 启用/停用方案
 * @param {String} schemeId 方案ID
 * @param {Number} isEnabled 是否启用(1启用0停用)
 * @returns {Promise}
 */
export function activateScheme(schemeId, isEnabled) {
  return request({
    url: '/cwgxAi/ma/costestimate/scheme/activate',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data: { schemeId, isEnabled }
  })
}

/**
 * 获取差异分析页面数据
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getVarianceAnalysisPage(data) {
  return request({
    url: '/cwgxAi/ma/costestimate/variance/getList',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data: data || {}
  })
}

/**
 * 保存或更新差异分析
 * @param {Object} data 分析数据
 * @returns {Promise}
 */
export function saveOrUpdateVarianceAnalysis(data) {
  return request({
    url: '/cwgxAi/ma/costestimate/variance/saveOrUpdate',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data: data || {}
  })
}

/**
 * 导出成本估算数据（扩展接口，支持blob响应）
 * @param {Object} data 导出参数
 * @returns {Promise}
 */
export function exportCostEstimateDataBlob(data) {
  return request({
    url: '/cwgxAi/ma/costestimate/export',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data: data || {},
    responseType: 'blob'
  })
}

/**
 * 根据ID获取预算准备详情
 * @param {Number} budgetId 预算ID
 * @returns {Promise}
 */
export function getBudgetPreparationById(budgetId) {
  return request({
    url: '/cwgxAi/ma/costestimate/budget/getById',
    method: 'get',
    params: { budgetId }
  })
}

/**
 * 获取预算审批历史
 * @param {Number} budgetId 预算ID
 * @returns {Promise}
 */
export function getBudgetApprovalHistory(budgetId) {
  return request({
    url: '/cwgxAi/ma/costestimate/budget/getApprovalHistory',
    method: 'get',
    params: { budgetId }
  })
}

/**
 * 获取审批人列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getApproverList(params) {
  return request({
    url: '/cwgxAi/ma/costestimate/budget/getApproverList',
    method: 'get',
    params
  })
}
