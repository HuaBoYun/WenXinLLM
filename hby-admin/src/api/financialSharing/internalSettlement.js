import request from '@/utils/request'

// ==================== 基础CRUD操作 ====================

/**
 * 分页查询内部结算列表
 */
export function getInternalSettlementList(params) {
  return request({
    url: '/cwgxAi/internal-settlement/list',
    method: 'get',
    params
  })
}

/**
 * 查询内部结算详情
 */
export function getInternalSettlementDetail(settlementId) {
  return request({
    url: `/cwgxAi/internal-settlement/${settlementId}`,
    method: 'get'
  })
}

/**
 * 创建内部结算记录
 */
export function createInternalSettlement(data) {
  return request({
    url: '/cwgxAi/internal-settlement',
    method: 'post',
    data
  })
}

/**
 * 更新内部结算记录
 */
export function updateInternalSettlement(settlementId, data) {
  return request({
    url: `/cwgxAi/internal-settlement/${settlementId}`,
    method: 'put',
    data
  })
}

/**
 * 删除内部结算记录
 */
export function deleteInternalSettlement(settlementId) {
  return request({
    url: `/cwgxAi/internal-settlement/${settlementId}`,
    method: 'delete'
  })
}

/**
 * 批量删除内部结算记录
 */
export function batchDeleteInternalSettlement(settlementIds) {
  return request({
    url: '/cwgxAi/internal-settlement/batch',
    method: 'delete',
    data: settlementIds
  })
}

/**
 * 查询内部结算统计信息
 */
export function getInternalSettlementStats(params) {
  return request({
    url: '/cwgxAi/internal-settlement/stats',
    method: 'get',
    params
  })
}

// ==================== 内部交易管理 ====================

/**
 * 分页查询内部交易列表
 */
export function getInternalTransactionList(params) {
  return request({
    url: '/cwgxAi/internal-settlement/transaction/list',
    method: 'get',
    params
  })
}

/**
 * 查询内部交易详情
 */
export function getInternalTransactionDetail(transactionId) {
  return request({
    url: `/cwgxAi/internal-settlement/transaction/${transactionId}`,
    method: 'get'
  })
}

/**
 * 创建内部交易
 */
export function createInternalTransaction(data) {
  return request({
    url: '/cwgxAi/internal-settlement/transaction',
    method: 'post',
    data
  })
}

/**
 * 更新内部交易
 */
export function updateInternalTransaction(transactionId, data) {
  return request({
    url: `/cwgxAi/internal-settlement/transaction/${transactionId}`,
    method: 'put',
    data
  })
}

/**
 * 删除内部交易
 */
export function deleteInternalTransaction(transactionId) {
  return request({
    url: `/cwgxAi/internal-settlement/transaction/${transactionId}`,
    method: 'delete'
  })
}

/**
 * 审批内部交易
 */
export function approveInternalTransaction(transactionId, data) {
  return request({
    url: `/cwgxAi/internal-settlement/transaction/${transactionId}/approve`,
    method: 'post',
    data
  })
}

/**
 * 批量审批内部交易
 */
export function batchApproveInternalTransaction(transactionIds, data) {
  return request({
    url: '/cwgxAi/internal-settlement/transaction/batch-approve',
    method: 'post',
    params: { transactionIds },
    data
  })
}

/**
 * 确认内部交易
 */
export function confirmInternalTransaction(transactionId, data) {
  return request({
    url: `/cwgxAi/internal-settlement/transaction/${transactionId}/confirm`,
    method: 'post',
    data
  })
}

/**
 * 结算内部交易
 */
export function settleInternalTransaction(transactionId, data) {
  return request({
    url: `/cwgxAi/internal-settlement/transaction/${transactionId}/settle`,
    method: 'post',
    data
  })
}

/**
 * 查询内部交易统计
 */
export function getInternalTransactionStats(params) {
  return request({
    url: '/cwgxAi/internal-settlement/transaction/stats',
    method: 'get',
    params
  })
}

// ==================== 转移定价管理 ====================

/**
 * 分页查询转移定价策略列表
 */
export function getTransferPricingList(params) {
  return request({
    url: '/cwgxAi/internal-settlement/pricing/list',
    method: 'get',
    params
  })
}

/**
 * 查询转移定价策略详情
 */
export function getTransferPricingDetail(policyId) {
  return request({
    url: `/cwgxAi/internal-settlement/pricing/${policyId}`,
    method: 'get'
  })
}

/**
 * 创建转移定价策略
 */
export function createTransferPricing(data) {
  return request({
    url: '/cwgxAi/internal-settlement/pricing',
    method: 'post',
    data
  })
}

/**
 * 更新转移定价策略
 */
export function updateTransferPricing(policyId, data) {
  return request({
    url: `/cwgxAi/internal-settlement/pricing/${policyId}`,
    method: 'put',
    data
  })
}

/**
 * 删除转移定价策略
 */
export function deleteTransferPricing(policyId) {
  return request({
    url: `/cwgxAi/internal-settlement/pricing/${policyId}`,
    method: 'delete'
  })
}

/**
 * 审批转移定价策略
 */
export function approveTransferPricing(policyId, data) {
  return request({
    url: `/cwgxAi/internal-settlement/pricing/${policyId}/approve`,
    method: 'post',
    data
  })
}

/**
 * 批量审批转移定价策略
 */
export function batchApproveTransferPricing(policyIds, data) {
  return request({
    url: '/cwgxAi/internal-settlement/pricing/batch-approve',
    method: 'post',
    params: { policyIds },
    data
  })
}

/**
 * 价格调整
 */
export function adjustTransferPrice(policyId, data) {
  return request({
    url: `/cwgxAi/internal-settlement/pricing/${policyId}/adjust`,
    method: 'post',
    data
  })
}

/**
 * 查询有效的定价策略
 */
export function getActivePricingPolicies(params) {
  return request({
    url: '/cwgxAi/internal-settlement/pricing/active',
    method: 'get',
    params
  })
}

/**
 * 查询转移定价统计
 */
export function getTransferPricingStats(params) {
  return request({
    url: '/cwgxAi/internal-settlement/pricing/stats',
    method: 'get',
    params
  })
}

// ==================== 利润中心管理 ====================

/**
 * 分页查询利润中心列表
 */
export function getProfitCenterList(params) {
  return request({
    url: '/cwgxAi/internal-settlement/profit-center/list',
    method: 'get',
    params
  })
}

/**
 * 查询利润中心详情
 */
export function getProfitCenterDetail(centerId) {
  return request({
    url: `/cwgxAi/internal-settlement/profit-center/${centerId}`,
    method: 'get'
  })
}

/**
 * 创建利润中心
 */
export function createProfitCenter(data) {
  return request({
    url: '/cwgxAi/internal-settlement/profit-center',
    method: 'post',
    data
  })
}

/**
 * 更新利润中心
 */
export function updateProfitCenter(centerId, data) {
  return request({
    url: `/cwgxAi/internal-settlement/profit-center/${centerId}`,
    method: 'put',
    data
  })
}

/**
 * 删除利润中心
 */
export function deleteProfitCenter(centerId) {
  return request({
    url: `/cwgxAi/internal-settlement/profit-center/${centerId}`,
    method: 'delete'
  })
}

/**
 * 查询利润中心层级结构
 */
export function getProfitCenterTree(params) {
  return request({
    url: '/cwgxAi/internal-settlement/profit-center/tree',
    method: 'get',
    params
  })
}

/**
 * 计算利润中心绩效
 */
export function calculateCenterPerformance(centerId, params) {
  return request({
    url: `/cwgxAi/internal-settlement/profit-center/${centerId}/performance`,
    method: 'get',
    params
  })
}

/**
 * 利润分析
 */
export function analyzeProfitCenter(centerId, data) {
  return request({
    url: `/cwgxAi/internal-settlement/profit-center/${centerId}/analyze`,
    method: 'post',
    data
  })
}

/**
 * 绩效评价
 */
export function evaluateCenterPerformance(centerId, data) {
  return request({
    url: `/cwgxAi/internal-settlement/profit-center/${centerId}/evaluate`,
    method: 'post',
    data
  })
}

/**
 * 查询利润中心统计
 */
export function getProfitCenterStats(params) {
  return request({
    url: '/cwgxAi/internal-settlement/profit-center/stats',
    method: 'get',
    params
  })
}

// ==================== 内部结算处理 ====================

/**
 * 分页查询结算处理列表
 */
export function getSettlementProcessList(params) {
  return request({
    url: '/cwgxAi/internal-settlement/process/list',
    method: 'get',
    params
  })
}

/**
 * 查询结算处理详情
 */
export function getSettlementProcessDetail(settlementId) {
  return request({
    url: `/cwgxAi/internal-settlement/process/${settlementId}`,
    method: 'get'
  })
}

/**
 * 创建结算规则
 */
export function createSettlementProcess(data) {
  return request({
    url: '/cwgxAi/internal-settlement/process',
    method: 'post',
    data
  })
}

/**
 * 更新结算规则
 */
export function updateSettlementProcess(settlementId, data) {
  return request({
    url: `/cwgxAi/internal-settlement/process/${settlementId}`,
    method: 'put',
    data
  })
}

/**
 * 删除结算规则
 */
export function deleteSettlementProcess(settlementId) {
  return request({
    url: `/cwgxAi/internal-settlement/process/${settlementId}`,
    method: 'delete'
  })
}

/**
 * 执行结算
 */
export function executeSettlement(data) {
  return request({
    url: '/cwgxAi/internal-settlement/process/execute',
    method: 'post',
    data
  })
}

/**
 * 生成结算凭证
 */
export function generateSettlementVoucher(settlementId) {
  return request({
    url: `/cwgxAi/internal-settlement/process/${settlementId}/voucher`,
    method: 'post'
  })
}

/**
 * 确认结算结果
 */
export function confirmSettlementResult(settlementId, data) {
  return request({
    url: `/cwgxAi/internal-settlement/process/${settlementId}/confirm`,
    method: 'post',
    data
  })
}

/**
 * 查询结算处理统计
 */
export function getSettlementProcessStats(params) {
  return request({
    url: '/cwgxAi/internal-settlement/process/stats',
    method: 'get',
    params
  })
}

// ==================== 资金管理 ====================

/**
 * 分页查询资金管理列表
 */
export function getFundManagementList(params) {
  return request({
    url: '/cwgxAi/internal-settlement/fund/list',
    method: 'get',
    params
  })
}

/**
 * 查询资金管理详情
 */
export function getFundManagementDetail(allocationId) {
  return request({
    url: `/cwgxAi/internal-settlement/fund/${allocationId}`,
    method: 'get'
  })
}

/**
 * 创建资金调配
 */
export function createFundManagement(data) {
  return request({
    url: '/cwgxAi/internal-settlement/fund',
    method: 'post',
    data
  })
}

/**
 * 更新资金调配
 */
export function updateFundManagement(allocationId, data) {
  return request({
    url: `/cwgxAi/internal-settlement/fund/${allocationId}`,
    method: 'put',
    data
  })
}

/**
 * 删除资金调配
 */
export function deleteFundManagement(allocationId) {
  return request({
    url: `/cwgxAi/internal-settlement/fund/${allocationId}`,
    method: 'delete'
  })
}

/**
 * 审批资金调配
 */
export function approveFundAllocation(allocationId, data) {
  return request({
    url: `/cwgxAi/internal-settlement/fund/${allocationId}/approve`,
    method: 'post',
    data
  })
}

/**
 * 执行资金调配
 */
export function executeFundAllocation(allocationId, data) {
  return request({
    url: `/cwgxAi/internal-settlement/fund/${allocationId}/execute`,
    method: 'post',
    data
  })
}

/**
 * 计算利息
 */
export function calculateInterest(allocationId, params) {
  return request({
    url: `/cwgxAi/internal-settlement/fund/${allocationId}/interest`,
    method: 'get',
    params
  })
}

/**
 * 查询资金池余额
 */
export function getFundPoolBalance(centerId) {
  return request({
    url: `/cwgxAi/internal-settlement/fund/pool/${centerId}/balance`,
    method: 'get'
  })
}

/**
 * 资金效率分析
 */
export function analyzeFundEfficiency(data) {
  return request({
    url: '/cwgxAi/internal-settlement/fund/analyze',
    method: 'post',
    data
  })
}

/**
 * 查询资金管理统计
 */
export function getFundManagementStats(params) {
  return request({
    url: '/cwgxAi/internal-settlement/fund/stats',
    method: 'get',
    params
  })
}

// ==================== 结算分析 ====================

/**
 * 查询结算分析数据
 */
export function getSettlementAnalysis(data) {
  return request({
    url: '/cwgxAi/internal-settlement/analysis',
    method: 'post',
    data
  })
}

/**
 * 结算效率分析
 */
export function analyzeSettlementEfficiency(data) {
  return request({
    url: '/cwgxAi/internal-settlement/analysis/efficiency',
    method: 'post',
    data
  })
}

/**
 * 交易结构分析
 */
export function analyzeTransactionStructure(data) {
  return request({
    url: '/cwgxAi/internal-settlement/analysis/transaction-structure',
    method: 'post',
    data
  })
}

/**
 * 利润贡献分析
 */
export function analyzeProfitContribution(data) {
  return request({
    url: '/cwgxAi/internal-settlement/analysis/profit-contribution',
    method: 'post',
    data
  })
}

/**
 * 成本效益分析
 */
export function analyzeCostBenefit(data) {
  return request({
    url: '/cwgxAi/internal-settlement/analysis/cost-benefit',
    method: 'post',
    data
  })
}

/**
 * 生成优化建议
 */
export function generateOptimizationSuggestions(data) {
  return request({
    url: '/cwgxAi/internal-settlement/analysis/optimization',
    method: 'post',
    data
  })
}

/**
 * 导出分析报告
 */
export function exportAnalysisReport(data) {
  return request({
    url: '/cwgxAi/internal-settlement/analysis/export',
    method: 'post',
    data
  })
}

// ==================== 报表功能 ====================

/**
 * 查询内部结算报表数据
 */
export function getSettlementReportData(data) {
  return request({
    url: '/cwgxAi/internal-settlement/report/settlement',
    method: 'post',
    data
  })
}

/**
 * 查询内部交易报表数据
 */
export function getTransactionReportData(data) {
  return request({
    url: '/cwgxAi/internal-settlement/report/transaction',
    method: 'post',
    data
  })
}

/**
 * 查询转移定价报表数据
 */
export function getPricingReportData(data) {
  return request({
    url: '/cwgxAi/internal-settlement/report/pricing',
    method: 'post',
    data
  })
}

/**
 * 查询利润中心报表数据
 */
export function getProfitCenterReportData(data) {
  return request({
    url: '/cwgxAi/internal-settlement/report/profit-center',
    method: 'post',
    data
  })
}

/**
 * 查询资金管理报表数据
 */
export function getFundManagementReportData(data) {
  return request({
    url: '/cwgxAi/internal-settlement/report/fund',
    method: 'post',
    data
  })
}

// ==================== 审计功能 ====================

/**
 * 查询审计日志
 */
export function getAuditLogList(params) {
  return request({
    url: '/cwgxAi/internal-settlement/audit/log',
    method: 'get',
    params
  })
}

/**
 * 记录操作日志
 */
export function recordOperationLog(params) {
  return request({
    url: '/cwgxAi/internal-settlement/audit/log',
    method: 'post',
    params
  })
}

/**
 * 查询变更历史
 */
export function getChangeHistory(settlementId) {
  return request({
    url: `/cwgxAi/internal-settlement/audit/${settlementId}/history`,
    method: 'get'
  })
}

// ==================== 维护功能 ====================

/**
 * 数据清理
 */
export function cleanupExpiredData(params) {
  return request({
    url: '/cwgxAi/internal-settlement/maintenance/cleanup',
    method: 'post',
    params
  })
}

/**
 * 数据归档
 */
export function archiveHistoryData(params) {
  return request({
    url: '/cwgxAi/internal-settlement/maintenance/archive',
    method: 'post',
    params
  })
}

/**
 * 重新计算统计数据
 */
export function recalculateStatistics(data) {
  return request({
    url: '/cwgxAi/internal-settlement/maintenance/recalculate',
    method: 'post',
    data
  })
}

/**
 * 数据一致性检查
 */
export function checkDataConsistency(data) {
  return request({
    url: '/cwgxAi/internal-settlement/maintenance/check',
    method: 'post',
    data
  })
}

// ==================== 扩展功能接口 ====================

/**
 * 获取内部结算页面数据（统一分页接口）
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getInternalSettlementPage(data) {
  return request({
    url: '/cwgxAi/ma/internalsettlement/getList',
    method: 'post',
    data
  })
}

/**
 * 保存或更新内部结算（统一保存接口）
 * @param {Object} data 结算数据
 * @returns {Promise}
 */
export function saveOrUpdateInternalSettlement(data) {
  return request({
    url: '/cwgxAi/ma/internalsettlement/saveOrUpdate',
    method: 'post',
    data
  })
}

/**
 * 获取内部结算详情
 * @param {Number} settlementId 结算ID
 * @returns {Promise}
 */
export function getInternalSettlementById(settlementId) {
  return request({
    url: `/cwgxAi/ma/internalsettlement/${settlementId}`,
    method: 'get'
  })
}

/**
 * 获取内部结算概览数据
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getInternalSettlementOverview(params) {
  return request({
    url: '/cwgxAi/ma/internalsettlement/overview',
    method: 'get',
    params
  })
}

/**
 * 获取利润中心页面数据
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getProfitCenterPage(data) {
  return request({
    url: '/cwgxAi/ma/internalsettlement/profitcenter/getList',
    method: 'post',
    data
  })
}

/**
 * 保存或更新利润中心
 * @param {Object} data 利润中心数据
 * @returns {Promise}
 */
export function saveOrUpdateProfitCenter(data) {
  return request({
    url: '/cwgxAi/ma/internalsettlement/profitcenter/saveOrUpdate',
    method: 'post',
    data
  })
}

/**
 * 删除利润中心（扩展接口）
 * @param {Number} centerId 中心ID
 * @returns {Promise}
 */
export function deleteProfitCenterExtended(centerId) {
  return request({
    url: `/cwgxAi/ma/internalsettlement/profitcenter/${centerId}`,
    method: 'delete'
  })
}

/**
 * 启用/停用利润中心
 * @param {Number} centerId 中心ID
 * @param {Number} isEnabled 是否启用(1启用0停用)
 * @returns {Promise}
 */
export function updateProfitCenterStatus(centerId, isEnabled) {
  return request({
    url: `/cwgxAi/ma/internalsettlement/profitcenter/${centerId}/status`,
    method: 'post',
    data: { isEnabled }
  })
}

/**
 * 获取利润中心绩效数据
 * @param {Number} centerId 中心ID
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getProfitCenterPerformance(centerId, params) {
  return request({
    url: `/cwgxAi/ma/internalsettlement/profitcenter/${centerId}/performance`,
    method: 'get',
    params
  })
}

/**
 * 获取内部交易页面数据
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getInternalTransactionPage(data) {
  return request({
    url: '/cwgxAi/ma/internalsettlement/transaction/getList',
    method: 'post',
    data
  })
}

/**
 * 保存或更新内部交易
 * @param {Object} data 交易数据
 * @returns {Promise}
 */
export function saveOrUpdateInternalTransaction(data) {
  return request({
    url: '/cwgxAi/ma/internalsettlement/transaction/saveOrUpdate',
    method: 'post',
    data
  })
}

/**
 * 审核内部交易
 * @param {Number} transactionId 交易ID
 * @param {Object} data 审核数据
 * @returns {Promise}
 */
export function auditInternalTransaction(transactionId, data) {
  return request({
    url: `/cwgxAi/ma/internalsettlement/transaction/${transactionId}/audit`,
    method: 'post',
    data
  })
}

/**
 * 确认内部交易（扩展接口）
 * @param {Number} transactionId 交易ID
 * @param {Object} data 确认数据
 * @returns {Promise}
 */
export function confirmInternalTransactionExtended(transactionId, data) {
  return request({
    url: `/cwgxAi/ma/internalsettlement/transaction/${transactionId}/confirm`,
    method: 'post',
    data
  })
}

/**
 * 获取转移定价页面数据
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getTransferPricingPage(data) {
  return request({
    url: '/cwgxAi/ma/internalsettlement/transferpricing/getList',
    method: 'post',
    data
  })
}

/**
 * 保存或更新转移定价
 * @param {Object} data 定价数据
 * @returns {Promise}
 */
export function saveOrUpdateTransferPricing(data) {
  return request({
    url: '/cwgxAi/ma/internalsettlement/transferpricing/saveOrUpdate',
    method: 'post',
    data
  })
}

/**
 * 删除转移定价（扩展接口）
 * @param {Number} pricingId 定价ID
 * @returns {Promise}
 */
export function deleteTransferPricingExtended(pricingId) {
  return request({
    url: `/cwgxAi/ma/internalsettlement/transferpricing/${pricingId}`,
    method: 'delete'
  })
}

/**
 * 计算转移定价
 * @param {Object} data 计算参数
 * @returns {Promise}
 */
export function calculateTransferPricing(data) {
  return request({
    url: '/cwgxAi/ma/internalsettlement/transferpricing/calculate',
    method: 'post',
    data
  })
}

/**
 * 应用转移定价
 * @param {Number} pricingId 定价ID
 * @param {Object} data 应用参数
 * @returns {Promise}
 */
export function applyTransferPricing(pricingId, data) {
  return request({
    url: `/cwgxAi/ma/internalsettlement/transferpricing/${pricingId}/apply`,
    method: 'post',
    data
  })
}

/**
 * 导出内部结算数据
 * @param {Object} data 导出参数
 * @returns {Promise}
 */
export function exportInternalSettlementData(data) {
  return request({
    url: '/cwgxAi/ma/internalsettlement/export',
    method: 'post',
    data,
    responseType: 'blob'
  })
}
