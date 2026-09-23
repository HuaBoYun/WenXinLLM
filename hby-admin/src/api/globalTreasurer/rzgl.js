import request from '@/utils/request'

// 融资管理模块API接口

// ==================== 融资计划管理 ====================

/**
 * 分页查询融资计划
 */
export function getFinancingPlanPage(params) {
  return request({
    url: '/qqsk/financial/rzgl/financing-plan/list',
    method: 'post',
    data: params
  })
}

/**
 * 根据ID查询融资计划
 */
export function getFinancingPlan(planId) {
  return request({
    url: `/qqsk/financial/rzgl/financing-plan/detail/${planId}`,
    method: 'get'
  })
}

/**
 * 创建融资计划
 */
export function createFinancingPlan(data) {
  return request({
    url: '/qqsk/financial/rzgl/financing-plan/create',
    method: 'post',
    data
  })
}

/**
 * 更新融资计划
 */
export function updateFinancingPlan(data) {
  return request({
    url: '/qqsk/financial/rzgl/financing-plan/update',
    method: 'put',
    data
  })
}

/**
 * 删除融资计划
 */
export function deleteFinancingPlan(planId) {
  return request({
    url: `/qqsk/financial/rzgl/financing-plan/delete/${planId}`,
    method: 'delete'
  })
}

/**
 * 提交融资计划审批
 */
export function submitFinancingPlan(planId) {
  return request({
    url: `/qqsk/financial/rzgl/financing-plan/submit/${planId}`,
    method: 'post'
  })
}

/**
 * 审批通过融资计划
 */
export function approveFinancingPlan(planId, comments) {
  return request({
    url: `/qqsk/financial/rzgl/financing-plan/approve/${planId}`,
    method: 'post',
    data: { comments }
  })
}

/**
 * 审批拒绝融资计划
 */
export function rejectFinancingPlan(planId, comments) {
  return request({
    url: `/qqsk/financial/rzgl/financing-plan/reject/${planId}`,
    method: 'post',
    data: { comments }
  })
}

/**
 * 执行融资计划
 */
export function executeFinancingPlan(planId) {
  return request({
    url: `/qqsk/financial/rzgl/financing-plan/execute/${planId}`,
    method: 'post'
  })
}

/**
 * 取消融资计划
 */
export function cancelFinancingPlan(planId) {
  return request({
    url: `/qqsk/financial/rzgl/financing-plan/cancel/${planId}`,
    method: 'post'
  })
}

/**
 * 复制融资计划
 */
export function copyFinancingPlan(planId) {
  return request({
    url: `/qqsk/financial/rzgl/financing-plan/copy/${planId}`,
    method: 'post'
  })
}

/**
 * 获取融资计划统计数据
 */
export function getFinancingPlanStatistics(params) {
  return request({
    url: '/qqsk/financial/rzgl/financing-plan/statistics',
    method: 'get',
    params
  })
}

/**
 * 获取融资类型分布图表数据
 */
export function getFinancingTypeDistribution(params) {
  return request({
    url: '/qqsk/financial/rzgl/financing-plan/chart/type-distribution',
    method: 'get',
    params
  })
}

/**
 * 获取融资计划执行趋势图表数据
 */
export function getFinancingPlanTrend(params) {
  return request({
    url: '/qqsk/financial/rzgl/financing-plan/chart/trend',
    method: 'get',
    params
  })
}

/**
 * 批量导入融资计划
 */
export function importFinancingPlan(formData) {
  return request({
    url: '/qqsk/financial/rzgl/financing-plan/import',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 下载融资计划导入模板
 */
export function downloadFinancingPlanTemplate() {
  return request({
    url: '/qqsk/financial/rzgl/financing-plan/template',
    method: 'get',
    responseType: 'blob'
  })
}

/**
 * 导出融资计划数据
 */
export function exportFinancingPlan(params) {
  return request({
    url: '/qqsk/financial/rzgl/financing-plan/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

/**
 * 获取融资计划操作历史记录
 */
export function getFinancingPlanHistory(planId) {
  return request({
    url: `/qqsk/financial/rzgl/financing-plan/history/${planId}`,
    method: 'get'
  })
}

// ==================== 融资基础参数管理 ====================

/**
 * 分页查询融资基础参数
 */
export function getFinancingBasicParamsList(params) {
  return request({
    url: '/qqsk/financial/rzgl/financing-config/list',
    method: 'post',
    data: params
  })
}

/**
 * 根据ID查询融资基础参数
 */
export function getFinancingBasicParam(paramId) {
  return request({
    url: `/qqsk/financial/rzgl/financing-config/detail/${paramId}`,
    method: 'get'
  })
}

/**
 * 创建融资基础参数
 */
export function addFinancingBasicParam(data) {
  return request({
    url: '/qqsk/financial/rzgl/financing-config/create',
    method: 'post',
    data
  })
}

/**
 * 更新融资基础参数
 */
export function updateFinancingBasicParam(data) {
  return request({
    url: '/qqsk/financial/rzgl/financing-config/update',
    method: 'put',
    data
  })
}

/**
 * 删除融资基础参数
 */
export function delFinancingBasicParam(paramIds) {
  return request({
    url: `/qqsk/financial/rzgl/financing-config/delete/${paramIds}`,
    method: 'delete'
  })
}

/**
 * 切换融资基础参数状态
 */
export function toggleFinancingBasicParamStatus(paramId, status) {
  return request({
    url: `/qqsk/financial/rzgl/financing-config/updateStatus`,
    method: 'put',
    data: { status }
  })
}

// ==================== 授信申请管理 ====================

/**
 * 分页查询授信申请
 */
export function getCreditApplicationPage(params) {
  return request({
    url: '/qqsk/financial/rzgl/credit/application/page',
    method: 'post',
    data: params
  })
}

/**
 * 根据ID查询授信申请
 */
export function getCreditApplication(applicationId) {
  return request({
    url: `/qqsk/financial/rzgl/credit/application/${applicationId}`,
    method: 'get'
  })
}

/**
 * 创建授信申请
 */
export function createCreditApplication(data) {
  return request({
    url: '/qqsk/financial/rzgl/credit/application',
    method: 'post',
    data
  })
}

/**
 * 更新授信申请
 */
export function updateCreditApplication(data) {
  return request({
    url: '/qqsk/financial/rzgl/credit/application',
    method: 'put',
    data
  })
}

/**
 * 删除授信申请
 */
export function deleteCreditApplication(applicationIds) {
  return request({
    url: `/qqsk/financial/rzgl/credit/application/${applicationIds}`,
    method: 'delete'
  })
}

/**
 * 提交授信申请
 */
export function submitCreditApplication(applicationIds) {
  return request({
    url: `/qqsk/financial/rzgl/credit/application/${applicationIds}/submit`,
    method: 'put'
  })
}

// ==================== 授信合同管理 ====================

/**
 * 分页查询授信合同
 */
export function getCreditContractPage(params) {
  return request({
    url: '/qqsk/financial/rzgl/credit/contract/page',
    method: 'post',
    data: params
  })
}

/**
 * 根据ID查询授信合同
 */
export function getCreditContract(contractId) {
  return request({
    url: `/qqsk/financial/rzgl/credit/contract/${contractId}`,
    method: 'get'
  })
}

/**
 * 创建授信合同
 */
export function createCreditContract(data) {
  return request({
    url: '/qqsk/financial/rzgl/credit/contract',
    method: 'post',
    data
  })
}

/**
 * 更新授信合同
 */
export function updateCreditContract(contractId, data) {
  return request({
    url: `/qqsk/financial/rzgl/credit/contract/${contractId}`,
    method: 'put',
    data
  })
}

/**
 * 删除授信合同
 */
export function deleteCreditContract(contractId) {
  return request({
    url: `/qqsk/financial/rzgl/credit/contract/${contractId}`,
    method: 'delete'
  })
}

/**
 * 签署授信合同
 */
export function signCreditContract(contractId) {
  return request({
    url: `/qqsk/financing/credit/contract/${contractId}/sign`,
    method: 'post'
  })
}

/**
 * 终止授信合同
 */
export function terminateCreditContract(contractId, reason) {
  return request({
    url: `/qqsk/financing/credit/contract/${contractId}/terminate`,
    method: 'post',
    params: { reason }
  })
}

/**
 * 获取授信合同统计信息
 */
export function getCreditContractStatistics(orgId) {
  return request({
    url: '/qqsk/financial/rzgl/credit/contract/statistics',
    method: 'get',
    params: { orgId }
  })
}

/**
 * 获取即将到期的授信合同
 */
export function getExpiringCreditContracts(days, orgId) {
  return request({
    url: '/qqsk/financial/rzgl/credit/contract/expiring',
    method: 'get',
    params: { days, orgId }
  })
}

/**
 * 导出授信合同
 */
export function exportCreditContracts(params) {
  return request({
    url: '/qqsk/financial/rzgl/credit/contract/export',
    method: 'post',
    data: params,
    responseType: 'blob'
  })
}

// ==================== 授信额度管理 ====================

/**
 * 分页查询授信额度
 */
export function getCreditLimitPage(params) {
  return request({
    url: '/qqsk/financial/rzgl/credit/limit/page',
    method: 'post',
    data: params
  })
}

/**
 * 根据ID查询授信额度
 */
export function getCreditLimit(limitId) {
  return request({
    url: `/qqsk/financial/rzgl/credit/limit/${limitId}`,
    method: 'get'
  })
}

/**
 * 创建授信额度
 */
export function createCreditLimit(data) {
  return request({
    url: '/qqsk/financial/rzgl/credit/limit',
    method: 'post',
    data
  })
}

/**
 * 更新授信额度
 */
export function updateCreditLimit(limitId, data) {
  return request({
    url: `/qqsk/financial/rzgl/credit/limit/${limitId}`,
    method: 'put',
    data
  })
}

/**
 * 删除授信额度
 */
export function deleteCreditLimit(limitIds) {
  return request({
    url: `/qqsk/financial/rzgl/credit/limit/${limitIds}`,
    method: 'delete'
  })
}

/**
 * 批量删除授信额度
 */
export function batchDeleteCreditLimit(ids) {
  return request({
    url: '/qqsk/financial/rzgl/credit/limit/batch',
    method: 'delete',
    data: ids
  })
}

/**
 * 根据合同ID查询授信额度
 */
export function getCreditLimitByContract(contractId) {
  return request({
    url: `/qqsk/financial/rzgl/credit/limit/contract/${contractId}`,
    method: 'get'
  })
}

/**
 * 根据公司ID查询授信额度
 */
export function getCreditLimitByCompany(companyId) {
  return request({
    url: `/qqsk/financial/rzgl/credit/limit/company/${companyId}`,
    method: 'get'
  })
}

/**
 * 使用额度
 */
export function useCreditLimit(limitId, amount, purpose) {
  return request({
    url: `/qqsk/financial/rzgl/credit/limit/${limitId}/use`,
    method: 'post',
    params: { amount, purpose }
  })
}

/**
 * 归还额度
 */
export function repayCreditLimit(limitId, amount) {
  return request({
    url: `/qqsk/financial/rzgl/credit/limit/${limitId}/repay`,
    method: 'post',
    params: { amount }
  })
}

/**
 * 冻结额度
 */
export function freezeCreditLimit(limitId, amount, reason) {
  return request({
    url: `/qqsk/financial/rzgl/credit/limit/freeze/${limitId}`,
    method: 'put',
    params: { amount, reason }
  })
}

/**
 * 解冻额度
 */
export function unfreezeCreditLimit(limitId, amount, reason) {
  return request({
    url: `/qqsk/financial/rzgl/credit/limit/unfreeze/${limitId}`,
    method: 'put',
    params: { amount, reason }
  })
}

/**
 * 暂停额度
 */
export function suspendCreditLimit(limitId, reason) {
  return request({
    url: `/qqsk/financial/rzgl/credit/limit/${limitId}/suspend`,
    method: 'post',
    params: { reason }
  })
}

/**
 * 激活额度
 */
export function activateCreditLimit(limitId, reason) {
  return request({
    url: `/qqsk/financial/rzgl/credit/limit/${limitId}/activate`,
    method: 'post',
    params: { reason }
  })
}

/**
 * 取消额度
 */
export function cancelCreditLimit(limitId, reason) {
  return request({
    url: `/qqsk/financial/rzgl/credit/limit/${limitId}/cancel`,
    method: 'post',
    params: { reason }
  })
}

/**
 * 查询即将到期的授信额度
 */
export function getExpiringCreditLimits(days, orgId) {
  return request({
    url: '/qqsk/financial/rzgl/credit/limit/expiring',
    method: 'get',
    params: { days, orgId }
  })
}

/**
 * 获取授信额度统计信息
 */
export function getCreditLimitStatistics(orgId) {
  return request({
    url: '/qqsk/financial/rzgl/credit/limit/statistics',
    method: 'get',
    params: { orgId }
  })
}

/**
 * 导出授信额度
 */
export function exportCreditLimits(params) {
  return request({
    url: '/qqsk/financial/rzgl/credit/limit/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

// ==================== 担保申请管理 ====================

/**
 * 分页查询担保申请
 */
export function getGuaranteeApplicationPage(params) {
  return request({
    url: '/qqsk/financial/rzgl/guarantee/application/page',
    method: 'post',
    data: params
  })
}

/**
 * 根据ID查询担保申请
 */
export function getGuaranteeApplication(applicationId) {
  return request({
    url: `/qqsk/financial/rzgl/guarantee/application/${applicationId}`,
    method: 'get'
  })
}

/**
 * 创建担保申请
 */
export function createGuaranteeApplication(data) {
  return request({
    url: '/qqsk/financial/rzgl/guarantee/application',
    method: 'post',
    data
  })
}

/**
 * 更新担保申请
 */
export function updateGuaranteeApplication(data) {
  return request({
    url: '/qqsk/financial/rzgl/guarantee/application',
    method: 'put',
    data
  })
}

/**
 * 删除担保申请
 */
export function deleteGuaranteeApplication(applicationIds) {
  return request({
    url: `/qqsk/financial/rzgl/guarantee/application/${applicationIds}`,
    method: 'delete'
  })
}

/**
 * 批量删除担保申请
 */
export function batchDeleteGuaranteeApplication(ids) {
  return request({
    url: '/qqsk/financial/rzgl/guarantee/application/batch',
    method: 'delete',
    data: ids
  })
}

/**
 * 提交担保申请审批
 */
export function submitGuaranteeApplication(applicationId) {
  return request({
    url: `/qqsk/financial/rzgl/guarantee/application/${applicationId}/submit`,
    method: 'post'
  })
}

/**
 * 审批通过担保申请
 */
export function approveGuaranteeApplication(applicationId, approvalComments) {
  return request({
    url: `/qqsk/financial/rzgl/guarantee/application/${applicationId}/approve`,
    method: 'post',
    params: { approvalComments }
  })
}

/**
 * 审批拒绝担保申请
 */
export function rejectGuaranteeApplication(applicationId, rejectReason) {
  return request({
    url: `/qqsk/financial/rzgl/guarantee/application/${applicationId}/reject`,
    method: 'post',
    params: { rejectReason }
  })
}

/**
 * 撤回担保申请
 */
export function withdrawGuaranteeApplication(applicationId) {
  return request({
    url: `/qqsk/financial/rzgl/guarantee/application/${applicationId}/withdraw`,
    method: 'post'
  })
}

// ==================== 融资监控分析 ====================

/**
 * 获取融资监控仪表板数据
 */
export function getFinancingDashboardData() {
  return request({
    url: '/qqsk/financial/rzgl/monitoring/dashboard',
    method: 'get'
  })
}

/**
 * 获取融资预警信息
 */
export function getFinancingAlerts(params) {
  return request({
    url: '/qqsk/financial/rzgl/monitoring/alerts/page',
    method: 'post',
    data: params
  })
}

/**
 * 处理融资预警
 */
export function handleFinancingAlert(alertId, data) {
  return request({
    url: `/qqsk/financing/monitoring/alerts/${alertId}/handle`,
    method: 'put',
    data
  })
}

/**
 * 融资需求分析
 */
export function analyzeFinancingNeed(data) {
  return request({
    url: '/qqsk/financial/rzgl/plan/need-analysis',
    method: 'post',
    data
  })
}

// ==================== 银行贷款管理 ====================

/**
 * 分页查询银行贷款
 */
export function getBankLoanPage(params) {
  return request({
    url: '/qqsk/financial/rzgl/bank-loan/list',
    method: 'post',
    data: params
  })
}

/**
 * 根据ID查询银行贷款
 */
export function getBankLoan(loanId) {
  return request({
    url: `/qqsk/financial/rzgl/bank-loan/detail/${loanId}`,
    method: 'get'
  })
}

/**
 * 创建贷款申请
 */
export function createBankLoan(data) {
  return request({
    url: '/qqsk/financial/rzgl/bank-loan/create',
    method: 'post',
    data
  })
}

/**
 * 更新贷款申请
 */
export function updateBankLoan(data) {
  return request({
    url: '/qqsk/financial/rzgl/bank-loan/update',
    method: 'post',
    data
  })
}

/**
 * 删除贷款记录
 */
export function deleteBankLoan(loanId) {
  return request({
    url: `/qqsk/financial/rzgl/bank-loan/delete/${loanId}`,
    method: 'delete'
  })
}

/**
 * 提交贷款申请
 */
export function submitLoanApplication(loanId) {
  return request({
    url: `/qqsk/financing/bank-loan/${loanId}/submit`,
    method: 'post'
  })
}

/**
 * 贷款提取
 */
export function drawdownLoan(loanId, data) {
  return request({
    url: `/qqsk/financing/bank-loan/${loanId}/drawdown`,
    method: 'post',
    data
  })
}

/**
 * 贷款还款
 */
export function repayLoan(loanId, data) {
  return request({
    url: `/qqsk/financing/bank-loan/${loanId}/repay`,
    method: 'post',
    data
  })
}

/**
 * 贷款监控
 */
export function monitorLoan(loanId) {
  return request({
    url: `/qqsk/financing/bank-loan/${loanId}/monitor`,
    method: 'get'
  })
}

/**
 * 获取贷款合同
 */
export function getLoanContract(loanId) {
  return request({
    url: `/qqsk/financing/bank-loan/${loanId}/contract`,
    method: 'get'
  })
}

/**
 * 上传贷款合同
 */
export function uploadLoanContract(loanId, data) {
  return request({
    url: `/qqsk/financing/bank-loan/${loanId}/contract`,
    method: 'post',
    data
  })
}

/**
 * 获取银行贷款统计数据
 */
export function getBankLoanStatistics(params) {
  return request({
    url: '/qqsk/financial/rzgl/bank-loan/statistics',
    method: 'get',
    params
  })
}

/**
 * 获取银行贷款类型分布图表数据
 */
export function getBankLoanTypeDistribution(params) {
  return request({
    url: '/qqsk/financial/rzgl/bank-loan/chart/type-distribution',
    method: 'get',
    params
  })
}

/**
 * 获取银行贷款申请趋势图表数据
 */
export function getBankLoanTrend(params) {
  return request({
    url: '/qqsk/financial/rzgl/bank-loan/chart/trend',
    method: 'get',
    params
  })
}

// ==================== 债券融资管理 ====================

/**
 * 分页查询债券融资
 */
export function getBondFinancingPage(params) {
  return request({
    url: '/qqsk/financial/rzgl/bond-financing/list',
    method: 'post',
    data: params
  })
}

/**
 * 根据ID查询债券融资
 */
export function getBondFinancing(bondId) {
  return request({
    url: `/qqsk/financial/rzgl/bond-financing/detail/${bondId}`,
    method: 'get'
  })
}

/**
 * 创建债券发行计划
 */
export function createBondFinancing(data) {
  return request({
    url: '/qqsk/financial/rzgl/bond-financing/create',
    method: 'post',
    data
  })
}

/**
 * 更新债券发行计划
 */
export function updateBondFinancing(data) {
  return request({
    url: '/qqsk/financial/rzgl/bond-financing/update',
    method: 'put',
    data
  })
}

/**
 * 删除债券记录
 */
export function deleteBondFinancing(bondId) {
  return request({
    url: `/qqsk/financial/rzgl/bond-financing/delete/${bondId}`,
    method: 'delete'
  })
}

/**
 * 债券发行管理
 */
export function issueBond(bondId, data) {
  return request({
    url: `/qqsk/financing/bond/${bondId}/issue`,
    method: 'post',
    data
  })
}

/**
 * 债券付息
 */
export function payBondInterest(bondId, data) {
  return request({
    url: `/qqsk/financing/bond/${bondId}/pay-interest`,
    method: 'post',
    data
  })
}

/**
 * 债券兑付
 */
export function redeemBond(bondId, data) {
  return request({
    url: `/qqsk/financing/bond/${bondId}/redeem`,
    method: 'post',
    data
  })
}

/**
 * 获取债券发行状态
 */
export function getBondIssuanceStatus(bondId) {
  return request({
    url: `/qqsk/financing/bond/${bondId}/status`,
    method: 'get'
  })
}

/**
 * 获取债券融资概览数据
 */
export function getBondFinancingOverview() {
  return request({
    url: '/qqsk/financial/rzgl/bond/overview',
    method: 'get'
  })
}

/**
 * 获取债券类型分布图表数据
 */
export function getBondTypeDistribution() {
  return request({
    url: '/qqsk/financial/rzgl/bond/chart/type-distribution',
    method: 'get'
  })
}

/**
 * 获取债券发行趋势图表数据
 * @param {string} period - 时间周期: 6M/1Y/2Y
 */
export function getBondTrend(period = '6M') {
  return request({
    url: '/qqsk/financial/rzgl/bond/chart/trend',
    method: 'get',
    params: { period }
  })
}

/**
 * 批量删除债券发行
 * @param {Array} issuanceIds - 发行ID数组
 */
export function batchDeleteBondFinancing(issuanceIds) {
  return request({
    url: '/qqsk/financial/rzgl/bond-financing/batch-delete',
    method: 'delete',
    data: issuanceIds
  })
}

/**
 * 提交债券发行审批
 * @param {number} issuanceId - 发行ID
 */
export function submitBondForApproval(issuanceId) {
  return request({
    url: `/qqsk/financial/rzgl/bond-financing/${issuanceId}/submit`,
    method: 'post'
  })
}

/**
 * 审批通过债券发行
 * @param {number} issuanceId - 发行ID
 * @param {string} comments - 审批意见
 */
export function approveBondFinancing(issuanceId, comments) {
  return request({
    url: `/qqsk/financial/rzgl/bond-financing/${issuanceId}/approve`,
    method: 'post',
    params: { comments }
  })
}

/**
 * 审批拒绝债券发行
 * @param {number} issuanceId - 发行ID
 * @param {string} comments - 审批意见
 */
export function rejectBondFinancing(issuanceId, comments) {
  return request({
    url: `/qqsk/financial/rzgl/bond-financing/${issuanceId}/reject`,
    method: 'post',
    params: { comments }
  })
}

/**
 * 确认债券发行
 * @param {number} issuanceId - 发行ID
 * @param {object} data - 发行参数
 */
export function confirmBondIssuance(issuanceId, data) {
  return request({
    url: `/qqsk/financial/rzgl/bond-financing/${issuanceId}/issue`,
    method: 'post',
    data
  })
}

/**
 * 债券付息
 * @param {number} issuanceId - 发行ID
 * @param {object} data - 付息参数
 */
export function payBondInterestNew(issuanceId, data) {
  return request({
    url: `/qqsk/financial/rzgl/bond-financing/${issuanceId}/pay-interest`,
    method: 'post',
    data
  })
}

/**
 * 债券兑付
 * @param {number} issuanceId - 发行ID
 * @param {object} data - 兑付参数
 */
export function redeemBondNew(issuanceId, data) {
  return request({
    url: `/qqsk/financial/rzgl/bond-financing/${issuanceId}/redeem`,
    method: 'post',
    data
  })
}

/**
 * 获取即将到期的债券
 * @param {number} days - 天数
 */
export function getExpiringBonds(days = 30) {
  return request({
    url: '/qqsk/financial/rzgl/bond-financing/expiring',
    method: 'get',
    params: { days }
  })
}

/**
 * 导出债券发行数据
 * @param {object} params - 查询参数
 */
export function exportBondFinancing(params) {
  return request({
    url: '/qqsk/financial/rzgl/bond-financing/export',
    method: 'post',
    data: params,
    responseType: 'blob'
  })
}

/**
 * 下载债券导入模板
 */
export function downloadBondTemplate() {
  return request({
    url: '/qqsk/financial/rzgl/bond-financing/download-template',
    method: 'get',
    responseType: 'blob'
  })
}

/**
 * 批量导入债券发行
 * @param {FormData} formData - 包含文件的FormData
 */
export function batchImportBondFinancing(formData) {
  return request({
    url: '/qqsk/financial/rzgl/bond-financing/batch-import',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

// ==================== 担保合同管理 ====================

/**
 * 分页查询担保合同
 */
export function getGuaranteeContractPage(params) {
  try {
    return request({
      url: '/qqsk/financial/rzgl/guarantee/contract/page',
      method: 'get',
      params
    }).then(response => {
      // 支持多种响应格式
      if (response && typeof response === 'object') {
        // 示例云标准格式: { code: 1, data: { rows: [], total: 0 }, msg: "" }
        if (response.data && response.data.rows && Array.isArray(response.data.rows)) {
          return {
            rows: response.data.rows,
            total: response.data.total || 0
          }
        }
        // tlist格式: { code: 1, data: { tlist: [], totalRecord: 0 }, msg: "" }
        if (response.data && response.data.tlist && Array.isArray(response.data.tlist)) {
          return {
            rows: response.data.tlist,
            total: response.data.totalRecord || 0
          }
        }
        // PageInfo格式: { list: [], total: 0 }
        if (response.list && Array.isArray(response.list)) {
          return {
            rows: response.list,
            total: response.total || 0
          }
        }
        // 直接rows格式: { rows: [], total: 0 }
        if (response.rows && Array.isArray(response.rows)) {
          return {
            rows: response.rows,
            total: response.total || 0
          }
        }
        // 直接数组格式
        if (Array.isArray(response)) {
          return {
            rows: response,
            total: response.length
          }
        }
        // 直接data是数组格式
        if (response.data && Array.isArray(response.data)) {
          return {
            rows: response.data,
            total: response.data.length
          }
        }
      }
      return { rows: [], total: 0 }
    }).catch(error => {
      console.error('查询担保合同失败:', error)
      return { rows: [], total: 0 }
    })
  } catch (error) {
    console.error('查询担保合同异常:', error)
    return Promise.resolve({ rows: [], total: 0 })
  }
}

/**
 * 根据ID查询担保合同
 */
export function getGuaranteeContract(contractId) {
  try {
    return request({
      url: `/qqsk/financial/rzgl/guarantee/contract/${contractId}`,
      method: 'get'
    }).then(response => {
      if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
        return response
      }
      return { code: 0, data: null }
    }).catch(error => {
      console.error('查询担保合同详情失败:', error)
      return { code: 0, data: null }
    })
  } catch (error) {
    console.error('查询担保合同详情异常:', error)
    return Promise.resolve({ code: 0, data: null })
  }
}

/**
 * 创建担保合同
 */
export function createGuaranteeContract(data) {
  try {
    return request({
      url: '/qqsk/financial/rzgl/guarantee/contract',
      method: 'post',
      data
    }).then(response => {
      if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
        return response
      }
      throw new Error(response?.message || '创建失败')
    }).catch(error => {
      console.error('创建担保合同失败:', error)
      throw error
    })
  } catch (error) {
    console.error('创建担保合同异常:', error)
    return Promise.reject(error)
  }
}

/**
 * 更新担保合同
 */
export function updateGuaranteeContract(data) {
  try {
    return request({
      url: '/qqsk/financial/rzgl/guarantee/contract',
      method: 'put',
      data
    }).then(response => {
      if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
        return response
      }
      throw new Error(response?.message || '更新失败')
    }).catch(error => {
      console.error('更新担保合同失败:', error)
      throw error
    })
  } catch (error) {
    console.error('更新担保合同异常:', error)
    return Promise.reject(error)
  }
}

/**
 * 删除担保合同
 */
export function deleteGuaranteeContract(contractIds) {
  try {
    return request({
      url: `/qqsk/financial/rzgl/guarantee/contract/${contractIds}`,
      method: 'delete'
    }).then(response => {
      if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
        return response
      }
      throw new Error(response?.message || '删除失败')
    }).catch(error => {
      console.error('删除担保合同失败:', error)
      throw error
    })
  } catch (error) {
    console.error('删除担保合同异常:', error)
    return Promise.reject(error)
  }
}

/**
 * 提交担保合同审批
 */
export function submitGuaranteeContract(contractId, submitterId) {
  try {
    return request({
      url: `/qqsk/financial/rzgl/guarantee/contract/${contractId}/submit`,
      method: 'post',
      params: { submitterId }
    }).then(response => {
      if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
        return response
      }
      throw new Error(response?.message || '提交失败')
    }).catch(error => {
      console.error('提交担保合同失败:', error)
      throw error
    })
  } catch (error) {
    console.error('提交担保合同异常:', error)
    return Promise.reject(error)
  }
}

/**
 * 审批担保合同
 */
export function approveGuaranteeContract(contractId, approvalStatus, approverId, approvalComments) {
  try {
    return request({
      url: `/qqsk/financial/rzgl/guarantee/contract/${contractId}/approve`,
      method: 'post',
      params: { approvalStatus, approverId, approvalComments }
    }).then(response => {
      if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
        return response
      }
      throw new Error(response?.message || '审批失败')
    }).catch(error => {
      console.error('审批担保合同失败:', error)
      throw error
    })
  } catch (error) {
    console.error('审批担保合同异常:', error)
    return Promise.reject(error)
  }
}

/**
 * 签署担保合同
 */
export function signGuaranteeContract(contractId, signingDate, operatorId) {
  try {
    return request({
      url: `/qqsk/financial/rzgl/guarantee/contract/${contractId}/sign`,
      method: 'post',
      params: { signingDate, operatorId }
    }).then(response => {
      if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
        return response
      }
      throw new Error(response?.message || '签署失败')
    }).catch(error => {
      console.error('签署担保合同失败:', error)
      throw error
    })
  } catch (error) {
    console.error('签署担保合同异常:', error)
    return Promise.reject(error)
  }
}

/**
 * 生效担保合同
 */
export function activateGuaranteeContract(contractId, effectiveDate, operatorId) {
  try {
    return request({
      url: `/qqsk/financial/rzgl/guarantee/contract/${contractId}/activate`,
      method: 'post',
      params: { effectiveDate, operatorId }
    }).then(response => {
      if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
        return response
      }
      throw new Error(response?.message || '生效失败')
    }).catch(error => {
      console.error('生效担保合同失败:', error)
      throw error
    })
  } catch (error) {
    console.error('生效担保合同异常:', error)
    return Promise.reject(error)
  }
}

/**
 * 终止担保合同
 */
export function terminateGuaranteeContract(contractId, terminationDate, terminationReason, operatorId) {
  try {
    return request({
      url: `/qqsk/financial/rzgl/guarantee/contract/${contractId}/terminate`,
      method: 'post',
      params: { terminationDate, terminationReason, operatorId }
    }).then(response => {
      if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
        return response
      }
      throw new Error(response?.message || '终止失败')
    }).catch(error => {
      console.error('终止担保合同失败:', error)
      throw error
    })
  } catch (error) {
    console.error('终止担保合同异常:', error)
    return Promise.reject(error)
  }
}

/**
 * 取消担保合同
 */
export function cancelGuaranteeContract(contractId, operatorId) {
  try {
    return request({
      url: `/qqsk/financial/rzgl/guarantee/contract/${contractId}/cancel`,
      method: 'post',
      params: { operatorId }
    }).then(response => {
      if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
        return response
      }
      throw new Error(response?.message || '取消失败')
    }).catch(error => {
      console.error('取消担保合同失败:', error)
      throw error
    })
  } catch (error) {
    console.error('取消担保合同异常:', error)
    return Promise.reject(error)
  }
}

/**
 * 获取担保合同统计信息
 */
export function getGuaranteeContractStatistics(orgId) {
  try {
    return request({
      url: '/qqsk/financial/rzgl/guarantee/contract/statistics',
      method: 'get',
      params: { orgId }
    }).then(response => {
      if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
        return response.data || response || {}
      }
      return {}
    }).catch(error => {
      console.error('获取担保合同统计信息失败:', error)
      return {}
    })
  } catch (error) {
    console.error('获取担保合同统计信息异常:', error)
    return Promise.resolve({})
  }
}

/**
 * 检查合同编号是否存在
 */
export function checkGuaranteeContractNoExists(contractNo, excludeId) {
  try {
    return request({
      url: '/qqsk/financial/rzgl/guarantee/contract/check-contract-no',
      method: 'get',
      params: { contractNo, excludeId }
    }).then(response => {
      if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
        return response.data || response || { exists: false }
      }
      return { exists: false }
    }).catch(error => {
      console.error('检查合同编号失败:', error)
      return { exists: false }
    })
  } catch (error) {
    console.error('检查合同编号异常:', error)
    return Promise.resolve({ exists: false })
  }
}

/**
 * 生成合同编号
 */
export function generateGuaranteeContractNo(contractType) {
  try {
    return request({
      url: '/qqsk/financial/rzgl/guarantee/contract/generate-contract-no',
      method: 'get',
      params: { contractType }
    }).then(response => {
      if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
        return response.data || response || { contractNo: '' }
      }
      return { contractNo: '' }
    }).catch(error => {
      console.error('生成合同编号失败:', error)
      return { contractNo: '' }
    })
  } catch (error) {
    console.error('生成合同编号异常:', error)
    return Promise.resolve({ contractNo: '' })
  }
}

/**
 * 导出担保合同
 */
export function exportGuaranteeContracts(params) {
  return request({
    url: '/qqsk/financial/rzgl/guarantee/contract/export',
    method: 'post',
    data: params,
    responseType: 'blob'
  })
}

// ==================== 融资监控分析 ====================

/**
 * 获取融资监控台数据
 */
export function getFinancingMonitoringDashboard(params) {
  return request({
    url: '/qqsk/financial/rzgl/monitoring/dashboard',
    method: 'post',
    data: params
  })
}

/**
 * 融资预警管理 - getFinancingAlerts 函数已在第261行定义，此处删除重复定义
 */

/**
 * 处理融资预警 - handleFinancingAlert 函数已在第272行定义，此处删除重复定义
 */

/**
 * 融资报表生成
 */
export function generateFinancingReport(params) {
  return request({
    url: '/qqsk/financial/rzgl/monitoring/report',
    method: 'post',
    data: params
  })
}

/**
 * 融资趋势分析
 */
export function getFinancingTrendAnalysis(params) {
  return request({
    url: '/qqsk/financial/rzgl/analysis/trend',
    method: 'post',
    data: params
  })
}

/**
 * 融资结构分析
 */
export function getFinancingStructureAnalysis(params) {
  return request({
    url: '/qqsk/financial/rzgl/analysis/structure',
    method: 'post',
    data: params
  })
}

// ==================== 融资成本分析 ====================

/**
 * 融资成本计算（执行成本分析）
 * @param {Object} params - 参数对象
 * @param {number} params.financingId - 融资ID（必填）
 * @param {string} params.analysisDate - 分析日期（必填）
 * @param {string} params.periodType - 周期类型（可选，默认MONTH）
 */
export function calculateFinancingCost(params) {
  return request({
    url: '/qqsk/financial/rzgl/cost/analysis',
    method: 'post',
    params: params  // 使用 params 而不是 data，这样会作为 URL 参数传递
  })
}

/**
 * 融资成本趋势分析
 */
export function getFinancingCostTrend(params) {
  return request({
    url: '/qqsk/financial/rzgl/cost/trend',
    method: 'post',
    data: params
  })
}

/**
 * 融资成本比较分析
 */
export function compareFinancingCost(data) {
  return request({
    url: '/qqsk/financial/rzgl/cost/compare',
    method: 'post',
    data
  })
}

/**
 * 获取市场利率信息
 */
export function getMarketRates(params) {
  return request({
    url: '/qqsk/financial/rzgl/cost/market-rates',
    method: 'post',
    data: params
  })
}

// ==================== 其他融资方式 ====================

/**
 * 分页查询商业票据
 */
export function getCommercialPaperPage(params) {
  return request({
    url: '/qqsk/financial/rzgl/commercial-paper/page',
    method: 'post',
    data: params
  })
}

/**
 * 创建商业票据
 */
export function createCommercialPaper(data) {
  return request({
    url: '/qqsk/financial/rzgl/commercial-paper',
    method: 'post',
    data
  })
}

/**
 * 发行商业票据
 */
export function issueCommercialPaper(paperId, data) {
  return request({
    url: `/qqsk/financing/commercial-paper/${paperId}/issue`,
    method: 'post',
    data
  })
}

/**
 * 分页查询资产证券化
 */
export function getAssetSecuritizationPage(params) {
  return request({
    url: '/qqsk/financial/rzgl/asset-securitization/page',
    method: 'post',
    data: params
  })
}

/**
 * 创建资产证券化项目
 */
export function createAssetSecuritization(data) {
  return request({
    url: '/qqsk/financial/rzgl/asset-securitization',
    method: 'post',
    data
  })
}

/**
 * 分页查询融资租赁
 */
export function getFinancialLeasePage(params) {
  return request({
    url: '/qqsk/financial/rzgl/financial-lease/list',
    method: 'post',
    params: params
  })
}

/**
 * 创建融资租赁
 */
export function createFinancialLease(data) {
  return request({
    url: '/qqsk/financial/rzgl/financial-lease/create',
    method: 'post',
    data
  })
}

/**
 * 更新融资租赁
 */
export function updateFinancialLease(data) {
  return request({
    url: '/qqsk/financial/rzgl/financial-lease/update',
    method: 'post',
    data
  })
}

/**
 * 删除融资租赁
 */
export function deleteFinancialLease(id) {
  return request({
    url: `/qqsk/financial/rzgl/financial-lease/delete/${id}`,
    method: 'post'
  })
}

/**
 * 获取融资租赁详情
 */
export function getFinancialLeaseDetail(id) {
  return request({
    url: '/qqsk/financial/rzgl/financial-lease/detail',
    method: 'get',
    params: { id }
  })
}

/**
 * 提交融资租赁审批
 */
export function submitFinancialLease(id) {
  return request({
    url: `/qqsk/financial/rzgl/financial-lease/${id}/submit`,
    method: 'post'
  })
}

/**
 * 审批融资租赁
 */
export function approveFinancialLease(id, comments) {
  return request({
    url: '/qqsk/financial/rzgl/financial-lease/approve',
    method: 'post',
    params: { id, comments }
  })
}

/**
 * 拒绝融资租赁
 */
export function rejectFinancialLease(id, comments) {
  return request({
    url: '/qqsk/financial/rzgl/financial-lease/reject',
    method: 'post',
    params: { id, comments }
  })
}

/**
 * 激活融资租赁（签约）
 */
export function activateFinancialLease(id) {
  return request({
    url: `/qqsk/financial/rzgl/financial-lease/${id}/activate`,
    method: 'post'
  })
}

/**
 * 获取融资租赁统计概览
 */
export function getFinancialLeaseOverview() {
  return request({
    url: '/qqsk/financial/rzgl/financial-lease/overview',
    method: 'get'
  })
}

/**
 * 获取融资租赁类型分布统计
 */
export function getFinancialLeaseTypeDistribution() {
  return request({
    url: '/qqsk/financial/rzgl/financial-lease/statistics/type-distribution',
    method: 'get'
  })
}

/**
 * 获取融资租赁申请趋势统计
 */
export function getFinancialLeaseTrend(period = '1Y') {
  return request({
    url: '/qqsk/financial/rzgl/financial-lease/statistics/trend',
    method: 'get',
    params: { period }
  })
}

/**
 * 导出融资租赁数据
 */
export function exportFinancialLease(params) {
  return request({
    url: '/qqsk/financial/rzgl/financial-lease/export',
    method: 'post',
    params: params,
    responseType: 'blob'
  })
}

/**
 * 下载融资租赁导入模板
 */
export function downloadFinancialLeaseTemplate() {
  return request({
    url: '/qqsk/financial/rzgl/financial-lease/export/template',
    method: 'get',
    responseType: 'blob'
  })
}

// ==================== 融资决策支持 ====================

/**
 * 融资方案比较
 */
export function compareFinancingSchemes(data) {
  return request({
    url: '/qqsk/financial/rzgl/decision/compare-schemes',
    method: 'post',
    data
  })
}

/**
 * 融资风险评估
 */
export function assessFinancingRisk(data) {
  return request({
    url: '/qqsk/financial/rzgl/decision/risk-assessment',
    method: 'post',
    data
  })
}

// ==================== 授信评估管理API ====================

/**
 * 分页查询授信评估记录
 */
export function getCreditAssessmentPage(query) {
  return request({
    url: '/qqsk/financial/rzgl/credit/assessment/page',
    method: 'post',
    data: query
  })
}

/**
 * 根据ID查询授信评估记录详情
 */
export function getCreditAssessment(assessmentId) {
  return request({
    url: `/qqsk/financial/rzgl/credit/assessment/${assessmentId}`,
    method: 'get'
  })
}

/**
 * 新增授信评估记录
 */
export function createCreditAssessment(data) {
  return request({
    url: '/qqsk/financial/rzgl/credit/assessment',
    method: 'post',
    data: data
  })
}

/**
 * 修改授信评估记录
 */
export function updateCreditAssessment(assessmentId, data) {
  return request({
    url: `/qqsk/financial/rzgl/credit/assessment/${assessmentId}`,
    method: 'put',
    data: data
  })
}

/**
 * 删除授信评估记录
 */
export function deleteCreditAssessment(assessmentIds) {
  return request({
    url: `/qqsk/financial/rzgl/credit/assessment/${assessmentIds}`,
    method: 'delete'
  })
}

/**
 * 提交评估记录
 */
export function submitCreditAssessment(assessmentId, submitter) {
  return request({
    url: `/qqsk/financial/rzgl/credit/assessment/${assessmentId}/submit`,
    method: 'post',
    params: { submitter }
  })
}

/**
 * 批准评估记录
 */
export function approveCreditAssessment(assessmentId, approver) {
  return request({
    url: `/qqsk/financial/rzgl/credit/assessment/${assessmentId}/approve`,
    method: 'post',
    params: { approver }
  })
}

/**
 * 拒绝评估记录
 */
export function rejectCreditAssessment(assessmentId, rejectReason, rejector) {
  return request({
    url: `/qqsk/financial/rzgl/credit/assessment/${assessmentId}/reject`,
    method: 'post',
    params: { rejectReason, rejector }
  })
}

/**
 * 撤回评估记录
 */
export function withdrawCreditAssessment(assessmentId, withdrawReason) {
  return request({
    url: `/qqsk/financial/rzgl/credit/assessment/${assessmentId}/withdraw`,
    method: 'post',
    params: { withdrawReason }
  })
}

/**
 * 复制评估记录
 */
export function copyCreditAssessment(assessmentId, targetCompanyId) {
  return request({
    url: `/qqsk/financial/rzgl/credit/assessment/${assessmentId}/copy`,
    method: 'post',
    params: { targetCompanyId }
  })
}

/**
 * 根据公司ID查询评估历史
 */
export function getCreditAssessmentHistory(companyId, limit = 10) {
  return request({
    url: `/qqsk/financial/rzgl/credit/assessment/company/${companyId}/history`,
    method: 'get',
    params: { limit }
  })
}

/**
 * 查询评估统计信息
 */
export function getCreditAssessmentStatistics() {
  return request({
    url: '/qqsk/financial/rzgl/credit/assessment/statistics',
    method: 'get'
  })
}

/**
 * 查询评估类型统计
 */
export function getCreditAssessmentTypeStatistics() {
  return request({
    url: '/qqsk/financial/rzgl/credit/assessment/statistics/type',
    method: 'get'
  })
}

/**
 * 查询风险等级统计
 */
export function getCreditAssessmentRiskLevelStatistics() {
  return request({
    url: '/qqsk/financial/rzgl/credit/assessment/statistics/risk-level',
    method: 'get'
  })
}

/**
 * 查询信用评级统计
 */
export function getCreditAssessmentCreditRatingStatistics() {
  return request({
    url: '/qqsk/financial/rzgl/credit/assessment/statistics/credit-rating',
    method: 'get'
  })
}

/**
 * 查询评估趋势
 */
export function getCreditAssessmentTrend(startDate, endDate) {
  return request({
    url: '/qqsk/financial/rzgl/credit/assessment/statistics/trend',
    method: 'post',
    data: { startDate, endDate }
  })
}

/**
 * 查询高风险评估记录
 */
export function getHighRiskCreditAssessments() {
  return request({
    url: '/qqsk/financial/rzgl/credit/assessment/high-risk',
    method: 'get'
  })
}

/**
 * 查询即将到期的评估记录
 */
export function getExpiringCreditAssessments(days = 30) {
  return request({
    url: '/qqsk/financial/rzgl/credit/assessment/expiring',
    method: 'get',
    params: { days }
  })
}

/**
 * 导出评估记录
 */
export function exportCreditAssessments(query) {
  return request({
    url: '/qqsk/financial/rzgl/credit/assessment/export',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}

/**
 * 执行自动评估
 */
export function performAutoCreditAssessment(companyId, assessmentType) {
  return request({
    url: '/qqsk/financial/rzgl/credit/assessment/auto-assess',
    method: 'post',
    params: { companyId, assessmentType }
  })
}

/**
 * 批量创建定期评估
 */
export function batchCreatePeriodicAssessments() {
  return request({
    url: '/qqsk/financial/rzgl/credit/assessment/batch-create-periodic',
    method: 'post'
  })
}

// ==================== 授信监控管理API ====================

/**
 * 分页查询授信监控预警
 */
export function getCreditMonitoringPage(query) {
  return request({
    url: '/qqsk/financial/rzgl/credit/monitoring/page',
    method: 'post',
    data: query
  })
}

/**
 * 根据ID查询授信监控预警详情
 */
export function getCreditMonitoring(alertId) {
  return request({
    url: `/qqsk/financial/rzgl/credit/monitoring/${alertId}`,
    method: 'get'
  })
}

/**
 * 新增授信监控预警
 */
export function createCreditMonitoring(data) {
  return request({
    url: '/qqsk/financial/rzgl/credit/monitoring',
    method: 'post',
    data: data
  })
}

/**
 * 修改授信监控预警
 */
export function updateCreditMonitoring(alertId, data) {
  return request({
    url: `/qqsk/financial/rzgl/credit/monitoring/${alertId}`,
    method: 'put',
    data: data
  })
}

/**
 * 删除授信监控预警
 */
export function deleteCreditMonitoring(alertIds) {
  return request({
    url: `/qqsk/financial/rzgl/credit/monitoring/${alertIds}`,
    method: 'delete'
  })
}

/**
 * 处理预警
 */
export function handleCreditMonitoring(alertId, handlerId, handleComments) {
  return request({
    url: `/qqsk/financial/rzgl/credit/monitoring/${alertId}/handle`,
    method: 'post',
    params: { handlerId, handleComments }
  })
}

/**
 * 关闭预警
 */
export function closeCreditMonitoring(alertId, handlerId, handleComments) {
  return request({
    url: `/qqsk/financial/rzgl/credit/monitoring/${alertId}/close`,
    method: 'post',
    params: { handlerId, handleComments }
  })
}

/**
 * 重新激活预警
 */
export function reactivateCreditMonitoring(alertId, reason) {
  return request({
    url: `/qqsk/financial/rzgl/credit/monitoring/${alertId}/reactivate`,
    method: 'post',
    params: { reason }
  })
}

/**
 * 批量处理预警
 */
export function batchHandleCreditMonitoring(alertIds, handlerId, handleComments) {
  return request({
    url: '/qqsk/financial/rzgl/credit/monitoring/batch-handle',
    method: 'post',
    params: { alertIds, handlerId, handleComments }
  })
}

/**
 * 批量关闭预警
 */
export function batchCloseCreditMonitoring(alertIds, handlerId, handleComments) {
  return request({
    url: '/qqsk/financial/rzgl/credit/monitoring/batch-close',
    method: 'post',
    params: { alertIds, handlerId, handleComments }
  })
}

/**
 * 查询活跃预警
 */
export function getActiveCreditMonitoring() {
  return request({
    url: '/qqsk/financial/rzgl/credit/monitoring/active',
    method: 'get'
  })
}

/**
 * 查询高级别预警
 */
export function getHighLevelCreditMonitoring() {
  return request({
    url: '/qqsk/financial/rzgl/credit/monitoring/high-level',
    method: 'get'
  })
}

/**
 * 查询严重预警
 */
export function getCriticalCreditMonitoring() {
  return request({
    url: '/qqsk/financial/rzgl/credit/monitoring/critical',
    method: 'get'
  })
}

/**
 * 查询未处理预警
 */
export function getUnhandledCreditMonitoring() {
  return request({
    url: '/qqsk/financial/rzgl/credit/monitoring/unhandled',
    method: 'get'
  })
}

/**
 * 查询预警统计信息
 */
export function getCreditMonitoringStatistics() {
  return request({
    url: '/qqsk/financial/rzgl/credit/monitoring/statistics',
    method: 'get'
  })
}

/**
 * 查询预警类型统计
 */
export function getCreditMonitoringTypeStatistics() {
  return request({
    url: '/qqsk/financial/rzgl/credit/monitoring/statistics/type',
    method: 'get'
  })
}

/**
 * 查询预警级别统计
 */
export function getCreditMonitoringLevelStatistics() {
  return request({
    url: '/qqsk/financial/rzgl/credit/monitoring/statistics/level',
    method: 'get'
  })
}

/**
 * 查询预警状态统计
 */
export function getCreditMonitoringStatusStatistics() {
  return request({
    url: '/qqsk/financial/rzgl/credit/monitoring/statistics/status',
    method: 'get'
  })
}

/**
 * 查询预警趋势
 */
export function getCreditMonitoringTrend(startDate, endDate) {
  return request({
    url: '/qqsk/financial/rzgl/credit/monitoring/statistics/trend',
    method: 'post',
    data: { startDate, endDate }
  })
}

/**
 * 查询处理效率分析
 */
export function getCreditMonitoringEfficiency() {
  return request({
    url: '/qqsk/financial/rzgl/credit/monitoring/statistics/efficiency',
    method: 'get'
  })
}

/**
 * 查询预警质量分析
 */
export function getCreditMonitoringQuality() {
  return request({
    url: '/qqsk/financial/rzgl/credit/monitoring/statistics/quality',
    method: 'get'
  })
}

/**
 * 导出预警记录
 */
export function exportCreditMonitoring(query) {
  return request({
    url: '/qqsk/financial/rzgl/credit/monitoring/export',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}

/**
 * 生成预警报告
 */
export function generateCreditMonitoringReport(startDate, endDate) {
  return request({
    url: '/qqsk/financial/rzgl/credit/monitoring/report',
    method: 'post',
    data: { startDate, endDate }
  })
}

/**
 * 批量生成预警
 */
export function batchGenerateCreditMonitoring() {
  return request({
    url: '/qqsk/financial/rzgl/credit/monitoring/batch-generate',
    method: 'post'
  })
}

/**
 * 自动关闭过期预警
 */
export function autoCloseCreditMonitoring(days = 30) {
  return request({
    url: '/qqsk/financial/rzgl/credit/monitoring/auto-close',
    method: 'post',
    params: { days }
  })
}

/**
 * 融资建议生成
 */
export function generateFinancingAdvice(data) {
  return request({
    url: '/qqsk/financial/rzgl/decision/advice',
    method: 'post',
    data
  })
}

/**
 * 融资可行性分析
 */
export function analyzeFeasibility(data) {
  return request({
    url: '/qqsk/financial/rzgl/decision/feasibility',
    method: 'post',
    data
  })
}

// ==================== 报表和导出 ====================

/**
 * 导出融资台账
 */
export function exportFinancingLedger(params) {
  return request({
    url: '/qqsk/financial/rzgl/export/ledger',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

/**
 * 导出融资统计报表
 */
export function exportFinancingStatistics(params) {
  return request({
    url: '/qqsk/financial/rzgl/export/statistics',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

/**
 * 导出融资成本分析报表
 */
export function exportCostAnalysis(params) {
  return request({
    url: '/qqsk/financial/rzgl/export/cost-analysis',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

// ==================== 融资成本分析管理 ====================

/**
 * 查询融资成本分析列表
 */
export function listCostAnalysis(query) {
  return request({
    url: '/qqsk/financial/rzgl/cost/list',
    method: 'post',
    data: query
  })
}

/**
 * 查询融资成本分析详细
 */
export function getCostAnalysis(analysisId) {
  return request({
    url: '/qqsk/financial/rzgl/cost/detail/' + analysisId,
    method: 'get'
  })
}

/**
 * 新增融资成本分析
 */
export function addCostAnalysis(data) {
  return request({
    url: '/qqsk/financial/rzgl/cost/create',
    method: 'post',
    data: data
  })
}

/**
 * 修改融资成本分析
 */
export function updateCostAnalysis(data) {
  return request({
    url: '/qqsk/financial/rzgl/cost/update',
    method: 'post',
    data: data
  })
}

/**
 * 删除融资成本分析
 */
export function delCostAnalysis(analysisId) {
  return request({
    url: '/qqsk/financial/rzgl/cost/delete/' + analysisId,
    method: 'delete'
  })
}

/**
 * 获取融资成本概览数据
 */
export function getCostOverview(params) {
  return request({
    url: '/qqsk/financial/rzgl/cost/overview',
    method: 'post',
    data: params
  })
}

/**
 * 获取成本分析图表数据
 */
export function getCostChartData(params) {
  return request({
    url: '/qqsk/financial/rzgl/cost/chart',
    method: 'post',
    data: params
  })
}

/**
 * 生成成本分析报告（导出成本报表）
 * @param {Object} params - 参数对象（所有参数可选）
 * @param {number} params.companyId - 公司ID
 * @param {string} params.financingType - 融资类型
 * @param {string} params.currencyCode - 币种
 * @param {string} params.periodType - 周期类型
 * @param {string} params.periodValue - 周期值
 */
export function generateCostReport(params) {
  return request({
    url: '/qqsk/financial/rzgl/cost/export',
    method: 'post',
    params: params  // 使用 params 而不是 data，这样会作为 URL 参数传递
  })
}

/**
 * 获取成本优化建议
 */
export function getCostOptimization(params) {
  return request({
    url: '/qqsk/financial/rzgl/cost/optimization',
    method: 'post',
    data: params
  })
}

// ==================== 报表管理 ====================

/**
 * 分页查询报表模板列表
 */
export function listReportTemplates(params) {
  return request({
    url: '/qqsk/financial/rzgl/report-management/templates',
    method: 'post',
    data: params
  })
}

/**
 * 根据ID查询报表模板详情
 */
export function getReportTemplate(templateId) {
  return request({
    url: `/qqsk/financial/rzgl/report-management/templates/${templateId}`,
    method: 'get'
  })
}

/**
 * 新增报表模板
 */
export function addReportTemplate(data) {
  return request({
    url: '/qqsk/financial/rzgl/report-management/templates/add',
    method: 'post',
    data
  })
}

/**
 * 更新报表模板
 */
export function updateReportTemplate(templateId, data) {
  return request({
    url: `/qqsk/financial/rzgl/report-management/templates/${templateId}`,
    method: 'put',
    data
  })
}

/**
 * 删除报表模板
 */
export function delReportTemplate(templateIds) {
  return request({
    url: `/qqsk/financial/rzgl/report-management/templates/${templateIds}`,
    method: 'delete'
  })
}

/**
 * 更新模板状态
 */
export function updateTemplateStatus(templateId, isEnabled) {
  return request({
    url: `/qqsk/financial/rzgl/report-management/templates/${templateId}/status`,
    method: 'post',
    params: { isEnabled }
  })
}

/**
 * 分发报表
 */
export function distributeReport(recordId, data) {
  return request({
    url: `/qqsk/financial/rzgl/report-management/distribute/${recordId}`,
    method: 'post',
    data
  })
}

/**
 * 导出报表模板
 */
export function exportReportTemplates(params) {
  return request({
    url: '/qqsk/financial/rzgl/report-management/templates/export',
    method: 'post',
    data: params,
    responseType: 'blob'
  })
}

/**
 * 分页查询报表记录列表
 */
export function listReportRecords(params) {
  return request({
    url: '/qqsk/financial/rzgl/report-management/records',
    method: 'post',
    data: params
  })
}

/**
 * 根据ID查询报表记录详情
 */
export function getReportRecord(recordId) {
  return request({
    url: `/qqsk/financing/report-management/records/${recordId}`,
    method: 'get'
  })
}

/**
 * 删除报表记录
 */
export function delReportRecord(recordIds) {
  return request({
    url: `/qqsk/financial/rzgl/report-management/delete/${recordIds}`,
    method: 'delete'
  })
}

/**
 * 更新报表记录
 */
export function updateReportRecord(data) {
  return request({
    url: '/qqsk/financial/rzgl/report-management/update',
    method: 'post',
    data
  })
}

/**
 * 导出报表记录
 */
export function exportReportRecords(params) {
  return request({
    url: '/qqsk/financial/rzgl/report-management/records/export',
    method: 'post',
    data: params,
    responseType: 'blob'
  })
}

/**
 * 生成报表
 */
export function generateReport(data) {
  return request({
    url: '/qqsk/financial/rzgl/report-management/generate',
    method: 'post',
    data
  })
}

/**
 * 重新生成报表
 */
export function regenerateReport(recordId) {
  return request({
    url: `/qqsk/financial/rzgl/report-management/regenerate/${recordId}`,
    method: 'post'
  })
}

/**
 * 取消报表生成
 */
export function cancelReportGeneration(recordId) {
  return request({
    url: `/qqsk/financial/rzgl/report-management/cancel/${recordId}`,
    method: 'post'
  })
}

/**
 * 获取报表生成进度
 */
export function getReportGenerationProgress(recordId) {
  return request({
    url: `/qqsk/financial/rzgl/report-management/progress/${recordId}`,
    method: 'get'
  })
}

/**
 * 批量生成报表
 */
export function batchGenerateReports(data) {
  return request({
    url: '/qqsk/financial/rzgl/report-management/batch-generate',
    method: 'post',
    data
  })
}

/**
 * 下载报表
 */
export function downloadReport(recordId) {
  return request({
    url: `/qqsk/financial/rzgl/report-management/download/${recordId}`,
    method: 'get',
    responseType: 'blob'
  })
}

/**
 * 获取报表文件信息
 */
export function getReportFileInfo(recordId) {
  return request({
    url: `/qqsk/financing/report-management/file-info/${recordId}`,
    method: 'get'
  })
}

/**
 * 批量下载报表
 */
export function batchDownloadReports(recordIds) {
  return request({
    url: '/qqsk/financial/rzgl/report-management/batch-download',
    method: 'post',
    data: recordIds,
    responseType: 'blob'
  })
}

/**
 * 获取报表概览统计
 */
export function getReportOverviewStatistics(params) {
  return request({
    url: '/qqsk/financial/rzgl/report-management/overview',
    method: 'post',
    data: params
  })
}

/**
 * 获取报表类型统计
 */
export function getReportTypeStatistics(params) {
  return request({
    url: '/qqsk/financial/rzgl/report-management/type-statistics',
    method: 'post',
    data: params
  })
}

/**
 * 获取报表图表数据
 */
export function getReportChartData(params) {
  return request({
    url: '/qqsk/financial/rzgl/report-management/chart-data',
    method: 'post',
    data: params
  })
}

/**
 * 生成融资报告
 */
export function generateFinancingDetailReport(params) {
  return request({
    url: '/qqsk/financial/rzgl/report/generate',
    method: 'post',
    data: params
  })
}

/**
 * 获取融资报告列表
 */
export function getFinancingReports(params) {
  return request({
    url: '/qqsk/financial/rzgl/report/list',
    method: 'post',
    data: params
  })
}

/**
 * 下载融资报告
 */
export function downloadFinancingReport(reportId) {
  return request({
    url: `/qqsk/financing/report/${reportId}/download`,
    method: 'get',
    responseType: 'blob'
  })
}

// ==================== 债券发行管理 ====================

/**
 * 债券发行分页查询
 */
export function getBondIssuancePage(params) {
  return request({
    url: '/qqsk/financial/rzgl/bond-financing/list',
    method: 'post',
    data: params
  })
}

/**
 * 创建债券发行
 */
export function createBondIssuance(data) {
  return request({
    url: '/qqsk/financial/rzgl/bond-financing/create',
    method: 'post',
    data
  })
}

/**
 * 更新债券发行
 */
export function updateBondIssuance(data) {
  return request({
    url: '/qqsk/financial/rzgl/bond-financing/update',
    method: 'put',
    data
  })
}

/**
 * 债券发行详情
 */
export function getBondIssuanceDetail(id) {
  return request({
    url: `/qqsk/financial/rzgl/bond-financing/detail/${id}`,
    method: 'get'
  })
}

/**
 * 债券发行审批
 */
export function approveBondIssuance(data) {
  return request({
    url: '/qqsk/financial/rzgl/bond-financing/approve',
    method: 'post',
    data
  })
}

/**
 * 债券发行执行
 */
export function executeBondIssuance(data) {
  return request({
    url: '/qqsk/financial/rzgl/bond-financing/issue',
    method: 'post',
    data
  })
}

/**
 * 债券付息管理
 */
export function manageBondInterest(data) {
  return request({
    url: '/qqsk/financial/rzgl/bond-financing/pay-interest',
    method: 'post',
    data
  })
}

/**
 * 债券兑付管理
 */
export function manageBondRedemption(data) {
  return request({
    url: '/qqsk/financial/rzgl/bond-financing/redeem',
    method: 'post',
    data
  })
}

// ==================== 融资租赁管理 ====================

/**
 * 融资租赁分页查询
 */
export function getLeaseManagementPage(params) {
  return request({
    url: '/qqsk/financial/rzgl/financial-lease/list',
    method: 'post',
    data: params
  })
}

/**
 * 创建融资租赁申请
 */
export function createLeaseApplication(data) {
  return request({
    url: '/qqsk/financial/rzgl/financial-lease/create',
    method: 'post',
    data
  })
}

/**
 * 更新融资租赁申请
 */
export function updateLeaseApplication(data) {
  return request({
    url: '/qqsk/financial/rzgl/financial-lease/update',
    method: 'put',
    data
  })
}

/**
 * 融资租赁详情
 */
export function getLeaseApplicationDetail(id) {
  return request({
    url: `/qqsk/financial/rzgl/financial-lease/detail/${id}`,
    method: 'get'
  })
}

/**
 * 提交融资租赁申请
 */
export function submitLeaseApplication(id) {
  return request({
    url: `/qqsk/financial-lease/${id}/submit`,
    method: 'post'
  })
}

/**
 * 审批融资租赁申请
 */
export function approveLeaseApplication(data) {
  return request({
    url: '/qqsk/financial/rzgl/financial-lease/approve',
    method: 'post',
    data
  })
}

/**
 * 签署租赁合同
 */
export function signLeaseContract(data) {
  return request({
    url: '/qqsk/financial/rzgl/financial-lease/sign-contract',
    method: 'post',
    data
  })
}

/**
 * 租金支付
 */
export function payLeaseRent(data) {
  return request({
    url: '/qqsk/financial/rzgl/financial-lease/pay-rent',
    method: 'post',
    data
  })
}

/**
 * 资产退租
 */
export function returnLeaseAsset(data) {
  return request({
    url: '/qqsk/financial/rzgl/financial-lease/return-asset',
    method: 'post',
    data
  })
}

// ==================== 融资监控管理 ====================

/**
 * 融资监控分页查询
 */
export function getFinancingMonitoringPage(params) {
  return request({
    url: '/qqsk/financial/rzgl/financing-monitoring/list',
    method: 'post',
    data: params
  })
}

/**
 * 融资监控概览
 */
export function getFinancingMonitoringOverview() {
  return request({
    url: '/qqsk/financial/rzgl/financing-monitoring/overview',
    method: 'get'
  })
}

/**
 * 创建融资监控
 */
export function createFinancingMonitoring(data) {
  return request({
    url: '/qqsk/financial/rzgl/financing-monitoring/create',
    method: 'post',
    data
  })
}

/**
 * 更新融资监控
 */
export function updateFinancingMonitoring(data) {
  return request({
    url: '/qqsk/financial/rzgl/financing-monitoring/update',
    method: 'put',
    data
  })
}

/**
 * 融资监控详情
 */
export function getFinancingMonitoringDetail(id) {
  return request({
    url: `/qqsk/financial/rzgl/financing-monitoring/detail/${id}`,
    method: 'get'
  })
}

/**
 * 风险预警列表
 */
export function getRiskAlertList(params) {
  return request({
    url: '/qqsk/financial/rzgl/financing-risk/list',
    method: 'post',
    data: params
  })
}

/**
 * 处理风险预警
 * 后端接口使用 @RequestParam 接收参数，需要使用 params 方式传参
 * @param {Object} data - 包含 monitoringId, handlerName, handleOpinion, alertStatus(HANDLED/CLOSED)
 */
export function processRiskAlert(data) {
  return request({
    url: '/qqsk/financial/rzgl/financing-risk/process',
    method: 'post',
    params: {
      monitoringId: data.monitoringId,
      handlerName: data.handlerName,
      handleOpinion: data.handleOpinion,
      alertStatus: data.alertStatus || 'HANDLED'
    }
  })
}

/**
 * 到期提醒列表
 */
export function getUpcomingPaymentList(params) {
  return request({
    url: '/qqsk/financial/rzgl/financing-repayment/upcoming',
    method: 'post',
    data: params
  })
}

/**
 * 获取融资类型图表数据
 */
export function getFinancingTypeChartData(params) {
  return request({
    url: '/qqsk/financial/rzgl/financing-monitoring/chart/type-distribution',
    method: 'get',
    params
  })
}

/**
 * 获取风险等级图表数据
 */
export function getRiskLevelChartData(params) {
  return request({
    url: '/qqsk/financial/rzgl/financing-monitoring/chart/risk-level',
    method: 'get',
    params
  })
}

/**
 * 获取合规状态图表数据
 */
export function getComplianceChartData(params) {
  return request({
    url: '/qqsk/financial/rzgl/financing-monitoring/chart/compliance',
    method: 'get',
    params
  })
}

/**
 * 刷新监控数据
 */
export function refreshFinancingMonitoringData() {
  return request({
    url: '/qqsk/financial/rzgl/financing-monitoring/refresh',
    method: 'post'
  })
}

/**
 * 导出监控报告
 */
export function exportFinancingMonitoringReport(params) {
  return request({
    url: '/qqsk/financial/rzgl/financing-monitoring/export',
    method: 'post',
    data: params,
    responseType: 'blob'
  })
}

// ==================== 债券类别管理 ====================

/**
 * 分页查询债券类别
 */
export function getBondCategoryPage(params) {
  return request({
    url: '/qqsk/financial/rzgl/bond-category/page',
    method: 'post',
    data: params
  })
}

/**
 * 根据ID查询债券类别
 */
export function getBondCategory(id) {
  return request({
    url: `/qqsk/financing/bond-category/${id}`,
    method: 'get'
  })
}

/**
 * 创建债券类别
 */
export function createBondCategory(data) {
  return request({
    url: '/qqsk/financial/rzgl/bond-category',
    method: 'post',
    data
  })
}

/**
 * 更新债券类别
 */
export function updateBondCategory(data) {
  return request({
    url: '/qqsk/financial/rzgl/bond-category',
    method: 'put',
    data
  })
}

/**
 * 删除债券类别
 */
export function deleteBondCategory(ids) {
  return request({
    url: `/qqsk/financing/bond-category/${ids}`,
    method: 'delete'
  })
}

/**
 * 更新债券类别状态
 */
export function updateBondCategoryStatus(id, status) {
  return request({
    url: `/qqsk/financing/bond-category/${id}/status`,
    method: 'put',
    data: { status }
  })
}

/**
 * 查询所有债券类别（用于下拉选择）
 */
export function getBondCategoryList() {
  return request({
    url: '/qqsk/financial/rzgl/bond-category/list',
    method: 'get'
  })
}

/**
 * 根据父级ID查询子类别
 */
export function getBondCategoryChildren(parentId) {
  return request({
    url: `/qqsk/financing/bond-category/children/${parentId}`,
    method: 'get'
  })
}

// ==================== 授信类别管理 ====================

/**
 * 分页查询授信类别
 */
export function getCreditCategoryPage(params) {
  return request({
    url: '/qqsk/financial/rzgl/credit-category/page',
    method: 'post',
    data: params
  })
}

/**
 * 根据ID查询授信类别
 */
export function getCreditCategory(id) {
  return request({
    url: `/qqsk/financing/credit-category/${id}`,
    method: 'get'
  })
}

/**
 * 创建授信类别
 */
export function createCreditCategory(data) {
  return request({
    url: '/qqsk/financial/rzgl/credit-category',
    method: 'post',
    data
  })
}

/**
 * 更新授信类别
 */
export function updateCreditCategory(data) {
  return request({
    url: '/qqsk/financial/rzgl/credit-category',
    method: 'put',
    data
  })
}

/**
 * 删除授信类别
 */
export function deleteCreditCategory(ids) {
  return request({
    url: `/qqsk/financing/credit-category/${ids}`,
    method: 'delete'
  })
}

/**
 * 更新授信类别状态
 */
export function updateCreditCategoryStatus(id, status) {
  return request({
    url: `/qqsk/financing/credit-category/${id}/status`,
    method: 'put',
    data: { status }
  })
}

/**
 * 查询所有授信类别（用于下拉选择）
 */
export function getCreditCategoryList() {
  return request({
    url: '/qqsk/financial/rzgl/credit-category/list',
    method: 'get'
  })
}

/**
 * 根据父级ID查询子类别
 */
export function getCreditCategoryChildren(parentId) {
  return request({
    url: `/qqsk/financing/credit-category/children/${parentId}`,
    method: 'get'
  })
}

/**
 * 生成监控报告
 */
export function generateMonitoringReport(data) {
  return request({
    url: '/qqsk/financial/rzgl/financing-monitoring/generate-report',
    method: 'post',
    data
  })
}

// ==================== 融资还款管理 ====================

/**
 * 融资还款分页查询
 */
export function getFinancingRepaymentPage(params) {
  return request({
    url: '/qqsk/financial/rzgl/financing-repayment/list',
    method: 'post',
    data: params
  })
}

/**
 * 创建融资还款计划
 */
export function createFinancingRepayment(data) {
  return request({
    url: '/qqsk/financial/rzgl/financing-repayment/create',
    method: 'post',
    data
  })
}

/**
 * 更新融资还款计划
 */
export function updateFinancingRepayment(data) {
  return request({
    url: '/qqsk/financial/rzgl/financing-repayment/update',
    method: 'put',
    data
  })
}

/**
 * 融资还款详情
 */
export function getFinancingRepaymentDetail(id) {
  return request({
    url: `/qqsk/financial/rzgl/financing-repayment/detail/${id}`,
    method: 'get'
  })
}

/**
 * 执行还款
 */
export function executeRepayment(data) {
  return request({
    url: '/qqsk/financial/rzgl/financing-repayment/execute',
    method: 'post',
    data
  })
}

/**
 * 部分还款
 */
export function partialRepayment(data) {
  return request({
    url: '/qqsk/financial/rzgl/financing-repayment/partial',
    method: 'post',
    data
  })
}

/**
 * 还款历史
 */
export function getRepaymentHistory(params) {
  return request({
    url: '/qqsk/financial/rzgl/financing-repayment/history',
    method: 'post',
    data: params
  })
}

/**
 * 展期申请
 */
export function applyExtension(data) {
  return request({
    url: '/qqsk/financial/rzgl/financing-repayment/extension',
    method: 'post',
    data
  })
}

/**
 * 即将到期还款列表
 */
export function getUpcomingRepaymentList(params) {
  return request({
    url: '/qqsk/financial/rzgl/financing-repayment/upcoming',
    method: 'post',
    data: params
  })
}

/**
 * 逾期还款列表
 */
export function getOverdueRepaymentList(params) {
  return request({
    url: '/qqsk/financial/rzgl/financing-repayment/overdue',
    method: 'post',
    data: params
  })
}

/**
 * 还款统计分析
 */
export function getRepaymentStatistics(params) {
  return request({
    url: '/qqsk/financial/rzgl/financing-repayment/statistics',
    method: 'post',
    data: params
  })
}

/**
 * 获取还款凭证
 */
export function getRepaymentReceipt(repaymentId) {
  return request({
    url: `/qqsk/financial/rzgl/financing-repayment/receipt/${repaymentId}`,
    method: 'post'
  })
}

/**
 * 展期申请
 */
export function applyRepaymentExtension(data) {
  return request({
    url: '/qqsk/financial/rzgl/financing-repayment/extension',
    method: 'post',
    data
  })
}

// ==================== 担保物管理 API ====================

/**
 * 分页查询担保物信息
 */
export function getCollateralPage(query) {
  try {
    return request({
      url: '/qqsk/financial/rzgl/guarantee/collateral/page',
      method: 'post',
      data: query
    }).then(response => {
      if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
        // 支持PageInfo格式
        if (response.data && response.data.records) {
          return {
            records: response.data.records || [],
            total: response.data.total || 0,
            current: response.data.current || 1,
            size: response.data.size || 10
          }
        }
        // 支持标准格式
        return {
          records: response.data || [],
          total: response.total || 0,
          current: response.pageNum || 1,
          size: response.pageSize || 10
        }
      }
      return { records: [], total: 0, current: 1, size: 10 }
    }).catch(error => {
      console.error('查询担保物信息失败:', error)
      return { records: [], total: 0, current: 1, size: 10 }
    })
  } catch (error) {
    console.error('查询担保物信息异常:', error)
    return Promise.resolve({ records: [], total: 0, current: 1, size: 10 })
  }
}

/**
 * 根据ID查询担保物详情
 */
export function getCollateral(id) {
  try {
    return request({
      url: `/qqsk/financial/rzgl/guarantee/collateral/${id}`,
      method: 'get'
    }).then(response => {
      if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
        return response.data || response || {}
      }
      return {}
    }).catch(error => {
      console.error('查询担保物详情失败:', error)
      return {}
    })
  } catch (error) {
    console.error('查询担保物详情异常:', error)
    return Promise.resolve({})
  }
}

/**
 * 新增担保物
 */
export function createCollateral(data) {
  try {
    return request({
      url: '/qqsk/financial/rzgl/guarantee/collateral',
      method: 'post',
      data
    }).then(response => {
      if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
        return response.data || response || { success: true }
      }
      return { success: false, message: response.message || '新增失败' }
    }).catch(error => {
      console.error('新增担保物失败:', error)
      return { success: false, message: '新增失败' }
    })
  } catch (error) {
    console.error('新增担保物异常:', error)
    return Promise.resolve({ success: false, message: '新增失败' })
  }
}

/**
 * 更新担保物
 */
export function updateCollateral(data) {
  try {
    return request({
      url: '/qqsk/financial/rzgl/guarantee/collateral',
      method: 'put',
      data
    }).then(response => {
      if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
        return response.data || response || { success: true }
      }
      return { success: false, message: response.message || '更新失败' }
    }).catch(error => {
      console.error('更新担保物失败:', error)
      return { success: false, message: '更新失败' }
    })
  } catch (error) {
    console.error('更新担保物异常:', error)
    return Promise.resolve({ success: false, message: '更新失败' })
  }
}

/**
 * 删除担保物
 */
export function deleteCollateral(id) {
  try {
    return request({
      url: `/qqsk/financial/rzgl/guarantee/collateral/${id}`,
      method: 'delete'
    }).then(response => {
      if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
        return { success: true }
      }
      return { success: false, message: response.message || '删除失败' }
    }).catch(error => {
      console.error('删除担保物失败:', error)
      return { success: false, message: '删除失败' }
    })
  } catch (error) {
    console.error('删除担保物异常:', error)
    return Promise.resolve({ success: false, message: '删除失败' })
  }
}

/**
 * 批量删除担保物
 */
export function batchDeleteCollaterals(ids) {
  try {
    return request({
      url: '/qqsk/financial/rzgl/guarantee/collateral/batch',
      method: 'delete',
      data: ids
    }).then(response => {
      if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
        return { success: true }
      }
      return { success: false, message: response.message || '批量删除失败' }
    }).catch(error => {
      console.error('批量删除担保物失败:', error)
      return { success: false, message: '批量删除失败' }
    })
  } catch (error) {
    console.error('批量删除担保物异常:', error)
    return Promise.resolve({ success: false, message: '批量删除失败' })
  }
}

/**
 * 冻结担保物
 */
export function freezeCollateral(id, reason, updatedBy = 1) {
  try {
    return request({
      url: `/qqsk/financial/rzgl/guarantee/collateral/${id}/freeze`,
      method: 'post',
      params: { reason, updatedBy }
    }).then(response => {
      if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
        return { success: true }
      }
      return { success: false, message: response.message || '冻结失败' }
    }).catch(error => {
      console.error('冻结担保物失败:', error)
      return { success: false, message: '冻结失败' }
    })
  } catch (error) {
    console.error('冻结担保物异常:', error)
    return Promise.resolve({ success: false, message: '冻结失败' })
  }
}

/**
 * 解冻担保物
 */
export function unfreezeCollateral(id, reason, updatedBy = 1) {
  try {
    return request({
      url: `/qqsk/financial/rzgl/guarantee/collateral/${id}/unfreeze`,
      method: 'post',
      params: { reason, updatedBy }
    }).then(response => {
      if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
        return { success: true }
      }
      return { success: false, message: response.message || '解冻失败' }
    }).catch(error => {
      console.error('解冻担保物失败:', error)
      return { success: false, message: '解冻失败' }
    })
  } catch (error) {
    console.error('解冻担保物异常:', error)
    return Promise.resolve({ success: false, message: '解冻失败' })
  }
}

/**
 * 抵押担保物
 */
export function mortgageCollateral(id, contractId, mortgageAmount, updatedBy = 1) {
  try {
    return request({
      url: `/qqsk/financial/rzgl/guarantee/collateral/${id}/mortgage`,
      method: 'post',
      params: { contractId, mortgageAmount, updatedBy }
    }).then(response => {
      if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
        return { success: true }
      }
      return { success: false, message: response.message || '抵押失败' }
    }).catch(error => {
      console.error('抵押担保物失败:', error)
      return { success: false, message: '抵押失败' }
    })
  } catch (error) {
    console.error('抵押担保物异常:', error)
    return Promise.resolve({ success: false, message: '抵押失败' })
  }
}

/**
 * 质押担保物
 */
export function pledgeCollateral(id, contractId, pledgeAmount, updatedBy = 1) {
  try {
    return request({
      url: `/qqsk/financial/rzgl/guarantee/collateral/${id}/pledge`,
      method: 'post',
      params: { contractId, pledgeAmount, updatedBy }
    }).then(response => {
      if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
        return { success: true }
      }
      return { success: false, message: response.message || '质押失败' }
    }).catch(error => {
      console.error('质押担保物失败:', error)
      return { success: false, message: '质押失败' }
    })
  } catch (error) {
    console.error('质押担保物异常:', error)
    return Promise.resolve({ success: false, message: '质押失败' })
  }
}

/**
 * 释放担保物
 */
export function releaseCollateral(id, releaseReason, updatedBy = 1) {
  try {
    return request({
      url: `/qqsk/financial/rzgl/guarantee/collateral/${id}/release`,
      method: 'post',
      params: { releaseReason, updatedBy }
    }).then(response => {
      if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
        return { success: true }
      }
      return { success: false, message: response.message || '释放失败' }
    }).catch(error => {
      console.error('释放担保物失败:', error)
      return { success: false, message: '释放失败' }
    })
  } catch (error) {
    console.error('释放担保物异常:', error)
    return Promise.resolve({ success: false, message: '释放失败' })
  }
}

/**
 * 处置担保物
 */
export function disposeCollateral(id, disposeValue, disposeDate, updatedBy = 1) {
  try {
    return request({
      url: `/qqsk/financial/rzgl/guarantee/collateral/${id}/dispose`,
      method: 'post',
      params: { disposeValue, disposeDate, updatedBy }
    }).then(response => {
      if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
        return { success: true }
      }
      return { success: false, message: response.message || '处置失败' }
    }).catch(error => {
      console.error('处置担保物失败:', error)
      return { success: false, message: '处置失败' }
    })
  } catch (error) {
    console.error('处置担保物异常:', error)
    return Promise.resolve({ success: false, message: '处置失败' })
  }
}

/**
 * 获取担保物概览数据
 */
export function getCollateralOverview() {
  try {
    return request({
      url: '/qqsk/financial/rzgl/guarantee/collateral/overview',
      method: 'get'
    }).then(response => {
      console.log('getCollateralOverview API 原始响应:', response)
      if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
        console.log('getCollateralOverview 返回 data:', response.data)
        return response.data || response || {}
      }
      console.log('getCollateralOverview code 不匹配, code=', response?.code)
      return {}
    }).catch(error => {
      console.error('获取担保物概览数据失败:', error)
      return {}
    })
  } catch (error) {
    console.error('获取担保物概览数据异常:', error)
    return Promise.resolve({})
  }
}

/**
 * 检查担保物编号是否存在
 */
export function checkCollateralNoExists(collateralNo, excludeId) {
  try {
    return request({
      url: '/qqsk/financial/rzgl/guarantee/collateral/check-no',
      method: 'get',
      params: { collateralNo, excludeId }
    }).then(response => {
      if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
        return response.data || response || false
      }
      return false
    }).catch(error => {
      console.error('检查担保物编号失败:', error)
      return false
    })
  } catch (error) {
    console.error('检查担保物编号异常:', error)
    return Promise.resolve(false)
  }
}

/**
 * 生成担保物编号
 */
export function generateCollateralNo(prefix = 'CL') {
  try {
    return request({
      url: '/qqsk/financial/rzgl/guarantee/collateral/generate-no',
      method: 'get',
      params: { prefix }
    }).then(response => {
      if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
        return response.data || response || { collateralNo: '' }
      }
      return { collateralNo: '' }
    }).catch(error => {
      console.error('生成担保物编号失败:', error)
      return { collateralNo: '' }
    })
  } catch (error) {
    console.error('生成担保物编号异常:', error)
    return Promise.resolve({ collateralNo: '' })
  }
}

// ==================== 担保监控管理API ====================

/**
 * 查询担保监控预警列表
 */
export function getGuaranteeMonitoringPage(query) {
  try {
    return request({
      url: '/qqsk/financial/rzgl/guarantee/monitoring/list',
      method: 'post',
      params: query
    }).then(response => {
      try {
        // 支持多种响应格式
        if (response && typeof response === 'object') {
          // 示例云标准格式：{code: 1, data: {total: 3, records: [...]}, msg: "操作成功"}
          if ([200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
            // data 是对象且包含 records 数组
            if (response.data && typeof response.data === 'object' && Array.isArray(response.data.records)) {
              return {
                records: response.data.records,
                total: response.data.total || 0
              }
            }
            // data 是对象且包含 rows 数组
            if (response.data && typeof response.data === 'object' && Array.isArray(response.data.rows)) {
              return {
                records: response.data.rows,
                total: response.data.total || 0
              }
            }
            // data 直接是数组
            if (Array.isArray(response.data)) {
              return {
                records: response.data,
                total: response.total || response.data.length
              }
            }
          }
          // PageInfo格式：{records: [...], total: 100}
          if (response.records && Array.isArray(response.records)) {
            return response
          }
          // 直接数组格式
          if (Array.isArray(response)) {
            return {
              records: response,
              total: response.length
            }
          }
        }
        return {
          records: [],
          total: 0
        }
      } catch (error) {
        console.error('解析担保监控预警列表响应失败:', error)
        return {
          records: [],
          total: 0
        }
      }
    }).catch(error => {
      console.error('查询担保监控预警列表失败:', error)
      return {
        records: [],
        total: 0
      }
    })
  } catch (error) {
    console.error('查询担保监控预警列表异常:', error)
    return Promise.resolve({
      records: [],
      total: 0
    })
  }
}

/**
 * 获取担保监控预警详情
 */
export function getGuaranteeMonitoring(id) {
  try {
    return request({
      url: `/qqsk/financial/rzgl/guarantee/monitoring/${id}`,
      method: 'get'
    }).then(response => {
      try {
        if (response && typeof response === 'object') {
          if ([200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
            return response.data || {}
          }
          if (response.alertId) {
            return response
          }
        }
        return {}
      } catch (error) {
        console.error('解析担保监控预警详情响应失败:', error)
        return {}
      }
    }).catch(error => {
      console.error('获取担保监控预警详情失败:', error)
      return {}
    })
  } catch (error) {
    console.error('获取担保监控预警详情异常:', error)
    return Promise.resolve({})
  }
}

/**
 * 新增担保监控预警
 */
export function createGuaranteeMonitoring(data) {
  try {
    return request({
      url: '/qqsk/financial/rzgl/guarantee/monitoring',
      method: 'post',
      data: data
    }).then(response => {
      try {
        if (response && typeof response === 'object') {
          if ([200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
            return response.data || response
          }
        }
        return response || {}
      } catch (error) {
        console.error('解析新增担保监控预警响应失败:', error)
        return {}
      }
    }).catch(error => {
      console.error('新增担保监控预警失败:', error)
      throw error
    })
  } catch (error) {
    console.error('新增担保监控预警异常:', error)
    return Promise.reject(error)
  }
}

/**
 * 修改担保监控预警
 */
export function updateGuaranteeMonitoring(data) {
  try {
    return request({
      url: '/qqsk/financial/rzgl/guarantee/monitoring',
      method: 'put',
      data: data
    }).then(response => {
      try {
        if (response && typeof response === 'object') {
          if ([200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
            return response.data || response
          }
        }
        return response || {}
      } catch (error) {
        console.error('解析修改担保监控预警响应失败:', error)
        return {}
      }
    }).catch(error => {
      console.error('修改担保监控预警失败:', error)
      throw error
    })
  } catch (error) {
    console.error('修改担保监控预警异常:', error)
    return Promise.reject(error)
  }
}

/**
 * 删除担保监控预警
 */
export function deleteGuaranteeMonitoring(id) {
  try {
    return request({
      url: `/qqsk/financial/rzgl/guarantee/monitoring/${id}`,
      method: 'delete'
    }).then(response => {
      try {
        if (response && typeof response === 'object') {
          if ([200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
            return response.data || response
          }
        }
        return response || {}
      } catch (error) {
        console.error('解析删除担保监控预警响应失败:', error)
        return {}
      }
    }).catch(error => {
      console.error('删除担保监控预警失败:', error)
      throw error
    })
  } catch (error) {
    console.error('删除担保监控预警异常:', error)
    return Promise.reject(error)
  }
}

/**
 * 批量删除担保监控预警
 */
export function batchDeleteGuaranteeMonitorings(ids) {
  try {
    return request({
      url: `/qqsk/financial/rzgl/guarantee/monitoring/${ids.join(',')}`,
      method: 'delete'
    }).then(response => {
      try {
        if (response && typeof response === 'object') {
          if ([200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
            return response.data || response
          }
        }
        return response || {}
      } catch (error) {
        console.error('解析批量删除担保监控预警响应失败:', error)
        return {}
      }
    }).catch(error => {
      console.error('批量删除担保监控预警失败:', error)
      throw error
    })
  } catch (error) {
    console.error('批量删除担保监控预警异常:', error)
    return Promise.reject(error)
  }
}

/**
 * 处理担保监控预警
 */
export function handleGuaranteeMonitoring(id, handlerId, handleOpinion) {
  try {
    return request({
      url: `/qqsk/financial/rzgl/guarantee/monitoring/${id}/handle`,
      method: 'post',
      params: {
        handlerId: handlerId,
        handleComments: handleOpinion
      }
    }).then(response => {
      try {
        if (response && typeof response === 'object') {
          if ([200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
            return response.data || response
          }
        }
        return response || {}
      } catch (error) {
        console.error('解析处理担保监控预警响应失败:', error)
        return {}
      }
    }).catch(error => {
      console.error('处理担保监控预警失败:', error)
      throw error
    })
  } catch (error) {
    console.error('处理担保监控预警异常:', error)
    return Promise.reject(error)
  }
}

/**
 * 关闭担保监控预警
 */
export function closeGuaranteeMonitoring(id, closeReason, handlerId) {
  try {
    return request({
      url: `/qqsk/financial/rzgl/guarantee/monitoring/${id}/close`,
      method: 'post',
      params: {
        closeReason: closeReason,
        handlerId: handlerId
      }
    }).then(response => {
      try {
        if (response && typeof response === 'object') {
          if ([200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
            return response.data || response
          }
        }
        return response || {}
      } catch (error) {
        console.error('解析关闭担保监控预警响应失败:', error)
        return {}
      }
    }).catch(error => {
      console.error('关闭担保监控预警失败:', error)
      throw error
    })
  } catch (error) {
    console.error('关闭担保监控预警异常:', error)
    return Promise.reject(error)
  }
}

/**
 * 重新激活担保监控预警
 */
export function reactivateGuaranteeMonitoring(id, reactivateReason, handlerId) {
  try {
    return request({
      url: `/qqsk/financial/rzgl/guarantee/monitoring/${id}/reactivate`,
      method: 'post',
      params: {
        reactivateReason: reactivateReason,
        handlerId: handlerId
      }
    }).then(response => {
      try {
        if (response && typeof response === 'object') {
          if ([200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
            return response.data || response
          }
        }
        return response || {}
      } catch (error) {
        console.error('解析重新激活担保监控预警响应失败:', error)
        return {}
      }
    }).catch(error => {
      console.error('重新激活担保监控预警失败:', error)
      throw error
    })
  } catch (error) {
    console.error('重新激活担保监控预警异常:', error)
    return Promise.reject(error)
  }
}

/**
 * 批量处理担保监控预警
 */
export function batchHandleGuaranteeMonitorings(ids, handlerId, handleOpinion) {
  try {
    return request({
      url: '/qqsk/financial/rzgl/guarantee/monitoring/batchHandle',
      method: 'post',
      params: {
        ids: ids,
        handlerId: handlerId,
        handleOpinion: handleOpinion
      }
    }).then(response => {
      try {
        if (response && typeof response === 'object') {
          if ([200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
            return response.data || response
          }
        }
        return response || {}
      } catch (error) {
        console.error('解析批量处理担保监控预警响应失败:', error)
        return {}
      }
    }).catch(error => {
      console.error('批量处理担保监控预警失败:', error)
      throw error
    })
  } catch (error) {
    console.error('批量处理担保监控预警异常:', error)
    return Promise.reject(error)
  }
}

/**
 * 批量关闭担保监控预警
 */
export function batchCloseGuaranteeMonitorings(ids, closeReason, handlerId) {
  try {
    return request({
      url: '/qqsk/financial/rzgl/guarantee/monitoring/batchClose',
      method: 'post',
      params: {
        ids: ids,
        closeReason: closeReason,
        handlerId: handlerId
      }
    }).then(response => {
      try {
        if (response && typeof response === 'object') {
          if ([200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
            return response.data || response
          }
        }
        return response || {}
      } catch (error) {
        console.error('解析批量关闭担保监控预警响应失败:', error)
        return {}
      }
    }).catch(error => {
      console.error('批量关闭担保监控预警失败:', error)
      throw error
    })
  } catch (error) {
    console.error('批量关闭担保监控预警异常:', error)
    return Promise.reject(error)
  }
}

/**
 * 获取担保监控预警统计数据
 */
export function getGuaranteeMonitoringStatistics() {
  try {
    return request({
      url: '/qqsk/financial/rzgl/guarantee/monitoring/statistics',
      method: 'get'
    }).then(response => {
      try {
        if (response && typeof response === 'object') {
          if ([200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
            return response.data || {}
          }
          if (response.total_count !== undefined) {
            return response
          }
        }
        return {}
      } catch (error) {
        console.error('解析担保监控预警统计数据响应失败:', error)
        return {}
      }
    }).catch(error => {
      console.error('获取担保监控预警统计数据失败:', error)
      return {}
    })
  } catch (error) {
    console.error('获取担保监控预警统计数据异常:', error)
    return Promise.resolve({})
  }
}

/**
 * 获取担保监控预警趋势数据
 */
export function getGuaranteeMonitoringTrend(startDate, endDate, orgid = 1) {
  try {
    return request({
      url: '/qqsk/financial/rzgl/guarantee/monitoring/trend',
      method: 'get',
      params: {
        startDate: startDate,
        endDate: endDate,
        orgid: orgid
      }
    }).then(response => {
      try {
        if (response && typeof response === 'object') {
          if ([200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
            return response.data || []
          }
          if (Array.isArray(response)) {
            return response
          }
        }
        return []
      } catch (error) {
        console.error('解析担保监控预警趋势数据响应失败:', error)
        return []
      }
    }).catch(error => {
      console.error('获取担保监控预警趋势数据失败:', error)
      return []
    })
  } catch (error) {
    console.error('获取担保监控预警趋势数据异常:', error)
    return Promise.resolve([])
  }
}

/**
 * 获取担保监控预警分布数据
 */
export function getGuaranteeMonitoringDistribution(orgid = 1) {
  try {
    return request({
      url: '/qqsk/financial/rzgl/guarantee/monitoring/distribution',
      method: 'get',
      params: { orgid }
    }).then(response => {
      try {
        if (response && typeof response === 'object') {
          if ([200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
            return response.data || []
          }
          if (Array.isArray(response)) {
            return response
          }
        }
        return []
      } catch (error) {
        console.error('解析担保监控预警分布数据响应失败:', error)
        return []
      }
    }).catch(error => {
      console.error('获取担保监控预警分布数据失败:', error)
      return []
    })
  } catch (error) {
    console.error('获取担保监控预警分布数据异常:', error)
    return Promise.resolve([])
  }
}

/**
 * 检查担保监控预警编号是否存在
 */
export function checkGuaranteeMonitoringNoExists(alertNo, id = null) {
  try {
    return request({
      url: '/qqsk/financial/rzgl/guarantee/monitoring/checkAlertNoExists',
      method: 'get',
      params: { alertNo, id }
    }).then(response => {
      if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
        return response.data || false
      }
      return false
    }).catch(error => {
      console.error('检查担保监控预警编号失败:', error)
      return false
    })
  } catch (error) {
    console.error('检查担保监控预警编号异常:', error)
    return Promise.resolve(false)
  }
}

/**
 * 生成担保监控预警编号
 */
export function generateGuaranteeMonitoringNo(prefix = 'GA') {
  try {
    return request({
      url: '/qqsk/financial/rzgl/guarantee/monitoring/generateAlertNo',
      method: 'get',
      params: { prefix }
    }).then(response => {
      if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
        return response.data || response || { alertNo: '' }
      }
      return { alertNo: '' }
    }).catch(error => {
      console.error('生成担保监控预警编号失败:', error)
      return { alertNo: '' }
    })
  } catch (error) {
    console.error('生成担保监控预警编号异常:', error)
    return Promise.resolve({ alertNo: '' })
  }
}

// ==================== 融资风险监控管理 API ====================

/**
 * 分页查询融资风险监控
 */
export function getFinancingRiskMonitoringPage(query) {
  try {
    return request({
      url: '/qqsk/financial/rzgl/monitoring/list',
      method: 'post',
      data: query
    }).then(response => {
      if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
        let list = []

        // 支持PageInfo格式 (MyBatis-Plus PageHelper)
        if (response.data && response.data.list) {
          list = response.data.list || []
        }
        // 支持records格式
        else if (response.data && response.data.records) {
          list = response.data.records || []
        }
        // 支持标准格式
        else if (response.data) {
          list = Array.isArray(response.data) ? response.data : []
        }

        // 字段映射：将后端字段映射为前端期望的字段
        const mappedList = list.map(item => {
          return {
            ...item,
            // 如果有TblFinancingMonitoring的字段，映射为风险监控字段
            monitoringNo: item.monitoringNo || item.alertType || '',
            riskTypeId: item.riskTypeId || 1,
            riskTypeName: item.riskTypeName || item.alertType || '未知',
            riskLevel: item.riskLevel || item.alertLevel || 'LOW',
            riskDescription: item.riskDescription || item.alertMessage || '',
            monitoringStatus: item.monitoringStatus || item.alertStatus || 'ACTIVE',
            monitoringDate: item.monitoringDate || item.alertDate || new Date(),
            handlerId: item.handlerId,
            handlerName: item.handlerName,
            handleDate: item.handleDate,
            actionTaken: item.actionTaken || item.handleOpinion || '',
            relatedFinancingId: item.relatedFinancingId,
            companyId: item.companyId,
            companyName: item.companyName,
            createdTime: item.createdTime,
            updatedTime: item.updatedTime,
            remark: item.remark
          }
        })

        return {
          records: mappedList,
          total: response.data?.total || response.total || 0,
          current: response.data?.pageNum || response.pageNum || 1,
          size: response.data?.pageSize || response.pageSize || 10
        }
      }
      return { records: [], total: 0, current: 1, size: 10 }
    }).catch(error => {
      console.error('查询融资风险监控失败:', error)
      return { records: [], total: 0, current: 1, size: 10 }
    })
  } catch (error) {
    console.error('查询融资风险监控异常:', error)
    return Promise.resolve({ records: [], total: 0, current: 1, size: 10 })
  }
}

/**
 * 根据ID查询融资风险监控详情
 */
export function getFinancingRiskMonitoring(monitoringId) {
  try {
    return request({
      url: `/qqsk/financial/rzgl/financing-risk/detail/${monitoringId}`,
      method: 'get'
    }).then(response => {
      if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
        return response.data || response || {}
      }
      return {}
    }).catch(error => {
      console.error('查询融资风险监控详情失败:', error)
      return {}
    })
  } catch (error) {
    console.error('查询融资风险监控详情异常:', error)
    return Promise.resolve({})
  }
}

/**
 * 新增融资风险监控
 */
export function createFinancingRiskMonitoring(data) {
  try {
    return request({
      url: '/qqsk/financial/rzgl/financing-risk/create',
      method: 'post',
      data
    }).then(response => {
      if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
        return response
      }
      throw new Error(response?.message || '创建失败')
    }).catch(error => {
      console.error('创建融资风险监控失败:', error)
      throw error
    })
  } catch (error) {
    console.error('创建融资风险监控异常:', error)
    return Promise.reject(error)
  }
}

/**
 * 修改融资风险监控
 */
export function updateFinancingRiskMonitoring(data) {
  try {
    return request({
      url: '/qqsk/financial/rzgl/financing-risk/update',
      method: 'post',
      data
    }).then(response => {
      if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
        return response
      }
      throw new Error(response?.message || '更新失败')
    }).catch(error => {
      console.error('更新融资风险监控失败:', error)
      throw error
    })
  } catch (error) {
    console.error('更新融资风险监控异常:', error)
    return Promise.reject(error)
  }
}

/**
 * 删除融资风险监控
 */
export function deleteFinancingRiskMonitoring(monitoringId) {
  try {
    return request({
      url: `/qqsk/financial/rzgl/financing-risk/delete/${monitoringId}`,
      method: 'delete'
    }).then(response => {
      if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
        return response
      }
      throw new Error(response?.message || '删除失败')
    }).catch(error => {
      console.error('删除融资风险监控失败:', error)
      throw error
    })
  } catch (error) {
    console.error('删除融资风险监控异常:', error)
    return Promise.reject(error)
  }
}

/**
 * 批量删除融资风险监控
 */
export function batchDeleteFinancingRiskMonitorings(monitoringIds) {
  try {
    return request({
      url: '/qqsk/financial/rzgl/risk-monitoring/batch',
      method: 'delete',
      data: monitoringIds
    }).then(response => {
      if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
        return response
      }
      throw new Error(response?.message || '批量删除失败')
    }).catch(error => {
      console.error('批量删除融资风险监控失败:', error)
      throw error
    })
  } catch (error) {
    console.error('批量删除融资风险监控异常:', error)
    return Promise.reject(error)
  }
}

/**
 * 触发风险警报
 */
export function triggerFinancingRiskAlert(monitoringId, alertMessage, updateUser) {
  try {
    return request({
      url: '/qqsk/financial/rzgl/financing-risk/alert',
      method: 'post',
      params: { relatedFinancingId: monitoringId }
    }).then(response => {
      if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
        return response
      }
      throw new Error(response?.message || '触发警报失败')
    }).catch(error => {
      console.error('触发风险警报失败:', error)
      throw error
    })
  } catch (error) {
    console.error('触发风险警报异常:', error)
    return Promise.reject(error)
  }
}

/**
 * 处理风险监控
 */
export function handleFinancingRiskMonitoring(monitoringId, handleOpinion, updateUser) {
  try {
    return request({
      url: '/qqsk/financial/rzgl/financing-risk/process',
      method: 'post',
      params: { monitoringId, handleOpinion }
    }).then(response => {
      if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
        return response
      }
      throw new Error(response?.message || '处理失败')
    }).catch(error => {
      console.error('处理风险监控失败:', error)
      throw error
    })
  } catch (error) {
    console.error('处理风险监控异常:', error)
    return Promise.reject(error)
  }
}

/**
 * 关闭风险监控
 */
export function closeFinancingRiskMonitoring(monitoringId, remark, updateUser) {
  try {
    return request({
      url: `/qqsk/financing/risk-monitoring/${monitoringId}/close`,
      method: 'post',
      params: { remark, updateUser }
    }).then(response => {
      if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
        return response
      }
      throw new Error(response?.message || '关闭失败')
    }).catch(error => {
      console.error('关闭风险监控失败:', error)
      throw error
    })
  } catch (error) {
    console.error('关闭风险监控异常:', error)
    return Promise.reject(error)
  }
}

/**
 * 重新激活风险监控
 */
export function reactivateFinancingRiskMonitoring(monitoringId, remark, updateUser) {
  try {
    return request({
      url: `/qqsk/financing/risk-monitoring/${monitoringId}/reactivate`,
      method: 'post',
      params: { remark, updateUser }
    }).then(response => {
      if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
        return response
      }
      throw new Error(response?.message || '重新激活失败')
    }).catch(error => {
      console.error('重新激活风险监控失败:', error)
      throw error
    })
  } catch (error) {
    console.error('重新激活风险监控异常:', error)
    return Promise.reject(error)
  }
}

/**
 * 获取风险监控统计数据
 */
export function getFinancingRiskMonitoringStatistics(orgId) {
  try {
    return request({
      url: '/qqsk/financial/rzgl/risk-monitoring/statistics',
      method: 'get',
      params: { orgId }
    }).then(response => {
      if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
        return response.data || response || {}
      }
      return {}
    }).catch(error => {
      console.error('获取风险监控统计数据失败:', error)
      return {}
    })
  } catch (error) {
    console.error('获取风险监控统计数据异常:', error)
    return Promise.resolve({})
  }
}

/**
 * 获取风险监控概览数据
 */
export function getFinancingRiskMonitoringOverview(orgId) {
  try {
    return request({
      url: '/qqsk/financial/rzgl/risk-monitoring/overview',
      method: 'get',
      params: { orgId }
    }).then(response => {
      if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
        return response.data || response || {}
      }
      return {}
    }).catch(error => {
      console.error('获取风险监控概览数据失败:', error)
      return {}
    })
  } catch (error) {
    console.error('获取风险监控概览数据异常:', error)
    return Promise.resolve({})
  }
}

/**
 * 获取风险监控仪表板数据
 */
export function getFinancingRiskMonitoringDashboard(orgId) {
  try {
    return request({
      url: '/qqsk/financial/rzgl/risk-monitoring/dashboard',
      method: 'get',
      params: { orgId }
    }).then(response => {
      if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
        return response.data || response || {}
      }
      return {}
    }).catch(error => {
      console.error('获取风险监控仪表板数据失败:', error)
      return {}
    })
  } catch (error) {
    console.error('获取风险监控仪表板数据异常:', error)
    return Promise.resolve({})
  }
}

/**
 * 获取风险监控趋势数据
 */
export function getFinancingRiskMonitoringTrend(months, orgId) {
  try {
    return request({
      url: '/qqsk/financial/rzgl/risk-monitoring/trend',
      method: 'get',
      params: { months, orgId }
    }).then(response => {
      if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
        return response.data || response || []
      }
      return []
    }).catch(error => {
      console.error('获取风险监控趋势数据失败:', error)
      return []
    })
  } catch (error) {
    console.error('获取风险监控趋势数据异常:', error)
    return Promise.resolve([])
  }
}

/**
 * 按风险类型统计数量
 */
export function countFinancingRiskByType(orgId) {
  try {
    return request({
      url: '/qqsk/financial/rzgl/risk-monitoring/count-by-risk-type',
      method: 'get',
      params: { orgId }
    }).then(response => {
      if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
        return response.data || response || []
      }
      return []
    }).catch(error => {
      console.error('按风险类型统计数量失败:', error)
      return []
    })
  } catch (error) {
    console.error('按风险类型统计数量异常:', error)
    return Promise.resolve([])
  }
}

/**
 * 按风险等级统计数量
 */
export function countFinancingRiskByLevel(orgId) {
  try {
    return request({
      url: '/qqsk/financial/rzgl/risk-monitoring/count-by-risk-level',
      method: 'get',
      params: { orgId }
    }).then(response => {
      if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
        return response.data || response || []
      }
      return []
    }).catch(error => {
      console.error('按风险等级统计数量失败:', error)
      return []
    })
  } catch (error) {
    console.error('按风险等级统计数量异常:', error)
    return Promise.resolve([])
  }
}

/**
 * 生成监控编号
 */
export function generateFinancingRiskMonitoringNo(orgId) {
  try {
    return request({
      url: '/qqsk/financial/rzgl/risk-monitoring/generate-no',
      method: 'get',
      params: { orgId }
    }).then(response => {
      if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
        return response.data || response || ''
      }
      return ''
    }).catch(error => {
      console.error('生成监控编号失败:', error)
      return ''
    })
  } catch (error) {
    console.error('生成监控编号异常:', error)
    return Promise.resolve('')
  }
}

/**
 * 检查监控编号是否存在
 */
export function checkFinancingRiskMonitoringNoExists(monitoringNo, orgId, excludeId) {
  try {
    return request({
      url: '/qqsk/financial/rzgl/risk-monitoring/check-no-exists',
      method: 'get',
      params: { monitoringNo, orgId, excludeId }
    }).then(response => {
      if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
        return response.data || response || false
      }
      return false
    }).catch(error => {
      console.error('检查监控编号是否存在失败:', error)
      return false
    })
  } catch (error) {
    console.error('检查监控编号是否存在异常:', error)
    return Promise.resolve(false)
  }
}

// ==================== 租赁资产管理 ====================

/**
 * 获取租赁资产列表
 */
export function getLeaseAssetList(leaseId) {
  return request({
    url: '/qqsk/financial/rzgl/lease/asset/list',
    method: 'post',
    params: { leaseId }
  })
}

/**
 * 获取资产详情
 */
export function getLeaseAssetDetail(assetId) {
  return request({
    url: '/qqsk/financial/rzgl/lease/asset/detail',
    method: 'post',
    params: { assetId }
  })
}

/**
 * 保存租赁资产
 */
export function saveLeaseAsset(data) {
  return request({
    url: '/qqsk/financial/rzgl/lease/asset/save',
    method: 'post',
    data
  })
}

/**
 * 删除租赁资产
 */
export function deleteLeaseAsset(assetId) {
  return request({
    url: '/qqsk/financial/rzgl/lease/asset/delete',
    method: 'post',
    params: { assetId }
  })
}

/**
 * 批量删除租赁资产
 */
export function batchDeleteLeaseAssets(assetIds) {
  return request({
    url: '/qqsk/financial/rzgl/lease/asset/batchDelete',
    method: 'post',
    data: assetIds
  })
}

// ==================== 租金计划管理 ====================

/**
 * 获取租金计划列表
 */
export function getLeasePaymentList(leaseId) {
  return request({
    url: '/qqsk/financial/rzgl/lease/payment/list',
    method: 'post',
    params: { leaseId }
  })
}

/**
 * 确认付款
 */
export function confirmLeasePayment(paymentId, paidAmount) {
  return request({
    url: '/qqsk/financial/rzgl/lease/payment/confirm',
    method: 'post',
    params: { paymentId, paidAmount }
  })
}

/**
 * 批量确认付款
 */
export function batchConfirmLeasePayments(paymentIds) {
  return request({
    url: '/qqsk/financial/rzgl/lease/payment/batchConfirm',
    method: 'post',
    data: paymentIds
  })
}

/**
 * 获取租金汇总信息
 */
export function getLeasePaymentSummary(leaseId) {
  return request({
    url: '/qqsk/financial/rzgl/lease/payment/summary',
    method: 'post',
    params: { leaseId }
  })
}

/**
 * 获取逾期租金列表
 */
export function getOverduePayments(leaseId) {
  return request({
    url: '/qqsk/financial/rzgl/lease/payment/overdue',
    method: 'post',
    params: { leaseId }
  })
}

// ==================== 退租管理 ====================

/**
 * 获取退租申请详情
 */
export function getLeaseReturnDetail(leaseId) {
  return request({
    url: '/qqsk/financial/rzgl/lease/return/detail',
    method: 'post',
    params: { leaseId }
  })
}

/**
 * 保存退租申请（草稿）
 */
export function saveLeaseReturn(data) {
  return request({
    url: '/qqsk/financial/rzgl/lease/return/save',
    method: 'post',
    data
  })
}

/**
 * 提交退租申请
 */
export function submitLeaseReturn(returnId) {
  return request({
    url: '/qqsk/financial/rzgl/lease/return/submit',
    method: 'post',
    params: { returnId }
  })
}

/**
 * 审批通过退租申请
 */
export function approveLeaseReturn(returnId, comments) {
  return request({
    url: '/qqsk/financial/rzgl/lease/return/approve',
    method: 'post',
    params: { returnId, comments }
  })
}

/**
 * 拒绝退租申请
 */
export function rejectLeaseReturn(returnId, comments) {
  return request({
    url: '/qqsk/financial/rzgl/lease/return/reject',
    method: 'post',
    params: { returnId, comments }
  })
}

/**
 * 计算退租费用
 */
export function calculateReturnFees(leaseId, returnType, assetDisposal) {
  return request({
    url: '/qqsk/financial/rzgl/lease/return/calculateFees',
    method: 'post',
    params: { leaseId, returnType, assetDisposal }
  })
}

/**
 * 获取待审批退租申请列表
 */
export function getPendingReturnList() {
  return request({
    url: '/qqsk/financial/rzgl/lease/return/pendingList',
    method: 'post'
  })
}

// ==================== 贷款合同管理 ====================

/**
 * 查询贷款合同列表
 */
export function getLoanContractList(params) {
  return request({
    url: '/qqsk/financial/rzgl/bank-loan/contract/list',
    method: 'post',
    data: params
  })
}

/**
 * 查询贷款合同详情
 */
export function getLoanContractDetail(contractId) {
  return request({
    url: `/qqsk/financial/rzgl/bank-loan/contract/detail/${contractId}`,
    method: 'get'
  })
}

/**
 * 保存贷款合同（新增/修改）
 */
export function saveLoanContract(data) {
  return request({
    url: '/qqsk/financial/rzgl/bank-loan/contract/save',
    method: 'post',
    data
  })
}

/**
 * 删除贷款合同
 */
export function deleteLoanContract(contractId) {
  return request({
    url: `/qqsk/financial/rzgl/bank-loan/contract/delete/${contractId}`,
    method: 'delete'
  })
}

/**
 * 更新贷款合同状态
 */
export function updateLoanContractStatus(contractId, status) {
  return request({
    url: `/qqsk/financial/rzgl/bank-loan/contract/status/${contractId}`,
    method: 'post',
    params: { status }
  })
}

/**
 * 根据贷款ID查询合同列表
 */
export function getLoanContractsByLoanId(loanId) {
  return request({
    url: `/qqsk/financial/rzgl/bank-loan/contract/byLoan/${loanId}`,
    method: 'get'
  })
}

// ==================== 还款计划管理 ====================

/**
 * 查询还款计划列表
 */
export function getRepaymentPlanList(params) {
  return request({
    url: '/qqsk/financial/rzgl/bank-loan/repayment/list',
    method: 'post',
    data: params
  })
}

/**
 * 查询还款计划详情
 */
export function getRepaymentPlanDetail(planId) {
  return request({
    url: `/qqsk/financial/rzgl/bank-loan/repayment/detail/${planId}`,
    method: 'get'
  })
}

/**
 * 保存还款计划（新增/修改）
 */
export function saveRepaymentPlan(data) {
  return request({
    url: '/qqsk/financial/rzgl/bank-loan/repayment/save',
    method: 'post',
    data
  })
}

/**
 * 删除还款计划
 */
export function deleteRepaymentPlan(planId) {
  return request({
    url: `/qqsk/financial/rzgl/bank-loan/repayment/delete/${planId}`,
    method: 'delete'
  })
}

/**
 * 自动生成还款计划
 * @param {Object} data - 生成参数
 * @param {string} data.loanId - 贷款ID
 * @param {string} data.contractId - 合同ID（可选）
 * @param {string} data.repaymentMethod - 还款方式：EQUAL_PRINCIPAL(等额本金) / EQUAL_INSTALLMENT(等额本息)
 * @param {number} data.periods - 还款期数
 * @param {number} data.loanAmount - 贷款金额
 * @param {number} data.interestRate - 年利率
 * @param {string} data.startDate - 起始日期
 */
export function generateRepaymentPlans(data) {
  return request({
    url: '/qqsk/financial/rzgl/bank-loan/repayment/generate',
    method: 'post',
    data
  })
}

/**
 * 执行银行贷款还款
 * @param {string} planId - 还款计划ID
 * @param {number} amount - 还款金额
 */
export function executeBankLoanRepayment(planId, amount) {
  return request({
    url: `/qqsk/financial/rzgl/bank-loan/repayment/execute/${planId}`,
    method: 'post',
    params: { amount }
  })
}

/**
 * 根据贷款ID查询还款计划列表
 */
export function getRepaymentPlansByLoanId(loanId) {
  return request({
    url: `/qqsk/financial/rzgl/bank-loan/repayment/byLoan/${loanId}`,
    method: 'get'
  })
}

/**
 * 查询待还款计划
 */
export function getPendingRepaymentPlans(loanId) {
  return request({
    url: `/qqsk/financial/rzgl/bank-loan/repayment/pending/${loanId}`,
    method: 'get'
  })
}

/**
 * 查询逾期还款计划
 */
export function getOverdueRepaymentPlans(loanId) {
  return request({
    url: `/qqsk/financial/rzgl/bank-loan/repayment/overdue/${loanId}`,
    method: 'get'
  })
}

// ==================== 贷款监控管理 ====================

/**
 * 查询贷款监控预警列表
 */
export function getLoanMonitoringList(params) {
  return request({
    url: '/qqsk/financial/rzgl/bank-loan/monitoring/list',
    method: 'post',
    data: params
  })
}

/**
 * 查询贷款监控预警详情
 */
export function getLoanMonitoringDetail(monitoringId) {
  return request({
    url: `/qqsk/financial/rzgl/bank-loan/monitoring/detail/${monitoringId}`,
    method: 'get'
  })
}

/**
 * 新增贷款监控预警
 */
export function addLoanMonitoring(data) {
  return request({
    url: '/qqsk/financial/rzgl/bank-loan/monitoring/add',
    method: 'post',
    data
  })
}

/**
 * 修改贷款监控预警
 */
export function updateLoanMonitoring(data) {
  return request({
    url: '/qqsk/financial/rzgl/bank-loan/monitoring/update',
    method: 'post',
    data
  })
}

/**
 * 删除贷款监控预警
 */
export function deleteLoanMonitoring(monitoringId) {
  return request({
    url: `/qqsk/financial/rzgl/bank-loan/monitoring/delete/${monitoringId}`,
    method: 'delete'
  })
}

/**
 * 处理贷款监控预警
 * @param {string} monitoringId - 监控ID
 * @param {string} handlerId - 处理人ID
 * @param {string} handleOpinion - 处理意见
 * @param {string} status - 处理状态：HANDLED(已处理) / CLOSED(已关闭)
 */
export function handleLoanMonitoring(monitoringId, handlerId, handleOpinion, status) {
  return request({
    url: `/qqsk/financial/rzgl/bank-loan/monitoring/handle/${monitoringId}`,
    method: 'post',
    params: { handlerId, handleOpinion, status }
  })
}

/**
 * 根据贷款ID查询监控预警列表
 */
export function getLoanMonitoringByLoanId(loanId) {
  return request({
    url: `/qqsk/financial/rzgl/bank-loan/monitoring/byLoan/${loanId}`,
    method: 'get'
  })
}

/**
 * 查询待处理预警
 */
export function getPendingLoanMonitoring(loanId) {
  return request({
    url: `/qqsk/financial/rzgl/bank-loan/monitoring/pending/${loanId}`,
    method: 'get'
  })
}

/**
 * 统计待处理预警数量
 */
export function countPendingLoanMonitoring(loanId) {
  return request({
    url: `/qqsk/financial/rzgl/bank-loan/monitoring/count/${loanId}`,
    method: 'get'
  })
}

