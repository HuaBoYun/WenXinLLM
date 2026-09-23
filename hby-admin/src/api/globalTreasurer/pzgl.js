import request from '@/utils/request'

// 票证管理模块API接口

// ==================== 票据管理通用接口 ====================

/**
 * 分页查询票据（通用接口）
 */
export function getInstrumentPage(params) {
  return request({
    url: '/qqsk/bill/instrument/list',
    method: 'post',
    data: params
  })
}

/**
 * 根据ID查询票据（通用接口）
 */
export function getInstrument(instrumentId) {
  return request({
    url: `/qqsk/bill/instrument/detail/${instrumentId}`,
    method: 'post'
  })
}

/**
 * 创建票据（通用接口）
 */
export function createInstrument(data) {
  return request({
    url: '/qqsk/bill/instrument/save',
    method: 'post',
    data
  })
}

/**
 * 更新票据（通用接口）
 */
export function updateInstrument(data) {
  return request({
    url: '/qqsk/bill/instrument/update',
    method: 'post',
    data
  })
}

/**
 * 删除票据（通用接口）
 */
export function deleteInstrument(instrumentIds) {
  return request({
    url: '/qqsk/bill/instrument/delete',
    method: 'post',
    data: instrumentIds
  })
}

// ==================== 商业汇票管理 ====================

/**
 * 分页查询商业汇票
 */
export function getCommercialBillPage(params) {
  return request({
    url: '/qqsk/bill/commercial/list',
    method: 'post',
    data: params
  })
}

/**
 * 根据ID查询商业汇票
 */
export function getCommercialBill(billId) {
  return request({
    url: `/qqsk/bill/commercial/detail/${billId}`,
    method: 'post'
  })
}

/**
 * 创建商业汇票
 */
export function createCommercialBill(data) {
  return request({
    url: '/qqsk/bill/commercial/save',
    method: 'post',
    data
  })
}

/**
 * 更新商业汇票
 */
export function updateCommercialBill(data) {
  return request({
    url: '/qqsk/bill/commercial/update',
    method: 'post',
    data
  })
}

/**
 * 删除商业汇票
 */
export function deleteCommercialBill(billIds) {
  return request({
    url: '/qqsk/bill/commercial/delete',
    method: 'post',
    data: billIds
  })
}

/**
 * 汇票开立
 */
export function issueBill(data) {
  return request({
    url: '/qqsk/bill/commercial/issue',
    method: 'post',
    data
  })
}

/**
 * 汇票承兑
 */
export function acceptBill(billId, data) {
  return request({
    url: `/qqsk/bill/commercial/accept/${billId}`,
    method: 'post',
    data
  })
}

/**
 * 汇票背书
 */
export function endorseBill(billId, data) {
  return request({
    url: `/qqsk/bill/instrument/endorse/${billId}`,
    method: 'post',
    data
  })
}

/**
 * 汇票贴现
 */
export function discountBill(billId, data) {
  return request({
    url: `/qqsk/bill/instrument/discount/${billId}`,
    method: 'post',
    data
  })
}

/**
 * 汇票到期处理
 */
export function matureBill(billId) {
  return request({
    url: `/qqsk/bill/commercial/mature/${billId}`,
    method: 'post'
  })
}

// ==================== 银行承兑汇票管理 ====================

/**
 * 分页查询银行承兑汇票
 */
export function getBankAcceptancePage(params) {
  return request({
    url: '/qqsk/bill/bank-acceptance/list',
    method: 'post',
    data: params
  })
}

/**
 * 根据ID查询银行承兑汇票
 */
export function getBankAcceptance(acceptanceId) {
  return request({
    url: `/qqsk/bill/bank-acceptance/detail/${acceptanceId}`,
    method: 'post'
  })
}

/**
 * 创建银行承兑汇票申请
 */
export function createBankAcceptance(data) {
  return request({
    url: '/qqsk/bill/bank-acceptance/save',
    method: 'post',
    data
  })
}

/**
 * 更新银行承兑汇票申请
 */
export function updateBankAcceptance(data) {
  return request({
    url: '/qqsk/bill/bank-acceptance/update',
    method: 'post',
    data
  })
}

/**
 * 删除银行承兑汇票
 */
export function deleteBankAcceptance(acceptanceIds) {
  return request({
    url: '/qqsk/bill/bank-acceptance/delete',
    method: 'post',
    data: acceptanceIds,
    headers: { 'Content-Type': 'application/json' }
  })
}

/**
 * 提交承兑申请
 */
export function submitAcceptanceApplication(acceptanceId) {
  return request({
    url: `/qqsk/bill/bank-acceptance/submit/${acceptanceId}`,
    method: 'post'
  })
}

/**
 * 承兑申请审批
 */
export function approveAcceptanceApplication(acceptanceId, data) {
  return request({
    url: `/qqsk/bill/bank-acceptance/approve/${acceptanceId}`,
    method: 'post',
    data
  })
}

/**
 * 开立承兑汇票
 */
export function issueAcceptance(acceptanceId) {
  return request({
    url: `/qqsk/bill/bank-acceptance/issue/${acceptanceId}`,
    method: 'post'
  })
}

/**
 * 保证金管理
 */
export function manageMargin(acceptanceId, data) {
  return request({
    url: `/qqsk/bill/bank-acceptance/margin/${acceptanceId}`,
    method: 'post',
    data
  })
}

/**
 * 获取保证金信息
 */
export function getMarginInfo(acceptanceId) {
  return request({
    url: `/qqsk/bill/bank-acceptance/margin-info/${acceptanceId}`,
    method: 'post'
  })
}

// ==================== 信用证管理 ====================

/**
 * 分页查询信用证
 */
export function getLetterOfCreditPage(params) {
  return request({
    url: '/qqsk/bill/letters-of-credit/list',
    method: 'post',
    data: params
  })
}

/**
 * 根据ID查询信用证
 */
export function getLetterOfCredit(lcId) {
  return request({
    url: `/qqsk/bill/letters-of-credit/detail/${lcId}`,
    method: 'post'
  })
}

/**
 * 创建信用证申请
 */
export function createLetterOfCredit(data) {
  return request({
    url: '/qqsk/bill/letters-of-credit/save',
    method: 'post',
    data
  })
}

/**
 * 更新信用证申请
 */
export function updateLetterOfCredit(data) {
  return request({
    url: '/qqsk/bill/letters-of-credit/update',
    method: 'post',
    data
  })
}

/**
 * 删除信用证
 */
export function deleteLetterOfCredit(lcIds) {
  return request({
    url: '/qqsk/bill/letters-of-credit/delete',
    method: 'post',
    data: lcIds
  })
}

/**
 * 提交信用证申请
 */
export function submitLCApplication(lcId) {
  return request({
    url: `/qqsk/bill/letters-of-credit/submit/${lcId}`,
    method: 'post'
  })
}

/**
 * 信用证申请审批
 */
export function approveLCApplication(lcId, data) {
  return request({
    url: `/qqsk/bill/letters-of-credit/approve/${lcId}`,
    method: 'post',
    data
  })
}

/**
 * 开立信用证
 */
export function issueLetterOfCredit(lcId) {
  return request({
    url: `/qqsk/bill/letters-of-credit/issue/${lcId}`,
    method: 'post'
  })
}

/**
 * 修改信用证
 */
export function amendLetterOfCredit(lcId, data) {
  return request({
    url: `/qqsk/bill/letters-of-credit/amend/${lcId}`,
    method: 'post',
    data
  })
}

/**
 * 单据处理
 */
export function processDocuments(lcId, data) {
  return request({
    url: `/qqsk/bill/letters-of-credit/documents/${lcId}`,
    method: 'post',
    data
  })
}

/**
 * 获取单据列表
 */
export function getDocumentList(lcId, params) {
  return request({
    url: `/qqsk/bill/letters-of-credit/documents-list/${lcId}`,
    method: 'post',
    data: params
  })
}

// ==================== 保函管理 ====================

/**
 * 分页查询保函
 */
export function getGuaranteePage(params) {
  return request({
    url: '/qqsk/bill/guarantees/list',
    method: 'post',
    data: params
  })
}

/**
 * 根据ID查询保函
 */
export function getGuarantee(guaranteeId) {
  return request({
    url: `/qqsk/bill/guarantees/detail/${guaranteeId}`,
    method: 'post'
  })
}

/**
 * 创建保函申请
 */
export function createGuarantee(data) {
  return request({
    url: '/qqsk/bill/guarantees/save',
    method: 'post',
    data
  })
}

/**
 * 更新保函申请
 */
export function updateGuarantee(data) {
  return request({
    url: '/qqsk/bill/guarantees/update',
    method: 'post',
    data
  })
}

/**
 * 删除保函
 * @param {string|string[]} guaranteeIds - 保函ID或ID数组
 */
export function deleteGuarantee(guaranteeIds) {
  // 确保传入的是数组格式
  const ids = Array.isArray(guaranteeIds) ? guaranteeIds : [guaranteeIds];
  return request({
    url: '/qqsk/bill/guarantees/delete',
    method: 'post',
    data: ids
  })
}

/**
 * 提交保函申请
 */
export function submitGuaranteeApplication(guaranteeId) {
  return request({
    url: `/qqsk/bill/guarantees/submit/${guaranteeId}`,
    method: 'post'
  })
}

/**
 * 保函申请审批
 */
export function approveGuaranteeApplication(guaranteeId, data) {
  return request({
    url: `/qqsk/bill/guarantees/approve/${guaranteeId}`,
    method: 'post',
    data
  })
}

/**
 * 开立保函
 */
export function issueGuarantee(guaranteeId) {
  return request({
    url: `/qqsk/bill/guarantees/issue/${guaranteeId}`,
    method: 'post'
  })
}

/**
 * 索赔保函
 */
export function claimGuarantee(guaranteeId, data) {
  return request({
    url: `/qqsk/bill/guarantees/claim/${guaranteeId}`,
    method: 'post',
    data
  })
}

/**
 * 解除保函
 */
export function releaseGuarantee(guaranteeId, releaseReason) {
  return request({
    url: `/qqsk/bill/guarantees/release/${guaranteeId}`,
    method: 'post',
    data: { releaseReason }
  })
}

/**
 * 取消保函
 */
export function cancelGuarantee(guaranteeId, cancelReason) {
  return request({
    url: `/qqsk/bill/guarantees/cancel/${guaranteeId}`,
    method: 'post',
    data: { cancelReason }
  })
}

// ==================== 票据查询功能 ====================

/**
 * 票据台账查询（综合查询）
 */
export function getInstrumentLedger(params) {
  return request({
    url: '/qqsk/bill/query/ledger',
    method: 'post',
    data: params
  })
}

/**
 * 票据统计分析
 */
export function getInstrumentStatistics(params) {
  return request({
    url: '/qqsk/bill/query/statistics',
    method: 'post',
    data: params
  })
}

/**
 * 票据到期提醒
 */
export function getMaturityAlerts(params) {
  return request({
    url: '/qqsk/bill/query/maturity-alerts',
    method: 'post',
    data: params
  })
}

/**
 * 票据到期日历
 */
export function getMaturityCalendar(params) {
  return request({
    url: '/qqsk/bill/query/maturity-calendar',
    method: 'post',
    data: params
  })
}

/**
 * 票据报表查询
 */
export function getInstrumentReport(params) {
  return request({
    url: '/qqsk/bill/query/report',
    method: 'post',
    data: params
  })
}

/**
 * 票据详情查询
 */
export function getInstrumentDetail(instrumentId) {
  return request({
    url: `/qqsk/bill/query/detail/${instrumentId}`,
    method: 'post'
  })
}

/**
 * 票据流转历史查询
 */
export function getInstrumentHistory(instrumentId) {
  return request({
    url: `/qqsk/bill/query/history/${instrumentId}`,
    method: 'post'
  })
}

/**
 * 票据状态跟踪
 */
export function trackBillStatus(billId) {
  return request({
    url: `/qqsk/bill/track/${billId}`,
    method: 'post'
  })
}

/**
 * 票据历史记录
 */
export function getBillHistory(billId) {
  return request({
    url: `/qqsk/bill/history/${billId}`,
    method: 'post'
  })
}

// ==================== 票据风险管理 ====================

/**
 * 票据风险评估
 */
export function assessBillRisk(billId) {
  return request({
    url: `/qqsk/bill/risk-assessment/${billId}`,
    method: 'post'
  })
}

/**
 * 风险预警设置
 */
export function setRiskAlert(data) {
  return request({
    url: '/qqsk/bill/risk-alert/save',
    method: 'post',
    data
  })
}

/**
 * 获取风险预警列表
 */
export function getRiskAlerts(params) {
  return request({
    url: '/qqsk/bill/risk-alert/list',
    method: 'post',
    data: params
  })
}

/**
 * 处理风险预警
 */
export function handleRiskAlert(alertId, data) {
  return request({
    url: `/qqsk/bill/risk-alert/handle/${alertId}`,
    method: 'post',
    data
  })
}

// ==================== 报表和导出 ====================

/**
 * 导出票据台账
 */
export function exportBillLedger(params) {
  return request({
    url: '/qqsk/bill/export/ledger',
    method: 'post',
    data: params,
    responseType: 'blob'
  })
}

/**
 * 导出票据统计报表
 */
export function exportBillStatistics(params) {
  return request({
    url: '/qqsk/bill/export/statistics',
    method: 'post',
    data: params,
    responseType: 'blob'
  })
}

/**
 * 生成票据报告
 */
export function generateBillReport(params) {
  return request({
    url: '/qqsk/bill/report/generate',
    method: 'post',
    data: params
  })
}

/**
 * 获取票据报告列表
 */
export function getBillReports(params) {
  return request({
    url: '/qqsk/bill/report/list',
    method: 'post',
    data: params
  })
}

/**
 * 下载票据报告
 */
export function downloadBillReport(reportId) {
  return request({
    url: `/qqsk/bill/report/download/${reportId}`,
    method: 'post',
    responseType: 'blob'
  })
}

// ==================== 票据登记管理 ====================

/**
 * 分页查询票据登记
 */
export function getBillRegistrationPage(params) {
  return request({
    url: '/qqsk/bill/registration/list',
    method: 'post',
    data: params
  })
}

/**
 * 获取票据登记详情
 */
export function getBillRegistrationDetail(billId) {
  return request({
    url: `/qqsk/bill/registration/detail/${billId}`,
    method: 'post'
  })
}

/**
 * 创建票据登记
 */
export function createBillRegistration(data) {
  return request({
    url: '/qqsk/bill/registration/save',
    method: 'post',
    data
  })
}

/**
 * 更新票据登记
 */
export function updateBillRegistration(data) {
  return request({
    url: '/qqsk/bill/registration/update',
    method: 'post',
    data
  })
}

/**
 * 删除票据登记
 */
export function deleteBillRegistration(billIds) {
  return request({
    url: '/qqsk/bill/registration/delete',
    method: 'post',
    data: billIds
  })
}

/**
 * 批量导入票据
 */
export function batchImportBills(data) {
  return request({
    url: '/qqsk/bill/registration/batch-import',
    method: 'post',
    data
  })
}

/**
 * 作废票据
 */
export function cancelBill(billId, reason) {
  return request({
    url: `/qqsk/bill/registration/cancel/${billId}`,
    method: 'post',
    data: { reason }
  })
}

// ==================== 票据背书管理 ====================

/**
 * 分页查询票据背书
 */
export function getBillEndorsementPage(params) {
  return request({
    url: '/qqsk/bill/endorsement/list',
    method: 'post',
    data: params
  })
}

/**
 * 获取票据背书详情
 */
export function getBillEndorsementDetail(endorsementId) {
  return request({
    url: `/qqsk/bill/endorsement/detail/${endorsementId}`,
    method: 'post'
  })
}

/**
 * 创建票据背书
 */
export function createBillEndorsement(data) {
  return request({
    url: '/qqsk/bill/endorsement/save',
    method: 'post',
    data
  })
}

/**
 * 更新票据背书
 */
export function updateBillEndorsement(data) {
  return request({
    url: '/qqsk/bill/endorsement/update',
    method: 'post',
    data
  })
}

/**
 * 删除票据背书
 */
export function deleteBillEndorsement(endorsementIds) {
  return request({
    url: '/qqsk/bill/endorsement/delete',
    method: 'post',
    data: endorsementIds
  })
}

/**
 * 审批票据背书
 */
export function approveBillEndorsement(endorsementId, data) {
  return request({
    url: `/qqsk/bill/endorsement/approve/${endorsementId}`,
    method: 'post',
    data
  })
}

/**
 * 执行票据背书
 */
export function executeBillEndorsement(endorsementId) {
  return request({
    url: `/qqsk/bill/endorsement/execute/${endorsementId}`,
    method: 'post'
  })
}

/**
 * 撤销票据背书
 */
export function cancelBillEndorsement(endorsementId, reason) {
  return request({
    url: `/qqsk/bill/endorsement/cancel/${endorsementId}`,
    method: 'post',
    data: { reason }
  })
}

/**
 * 批量审批票据背书
 */
export function batchApproveBillEndorsement(data) {
  return request({
    url: '/qqsk/bill/endorsement/batch-approve',
    method: 'post',
    data
  })
}

/**
 * 获取可用票据列表
 */
export function getAvailableBillsForEndorsement(params) {
  return request({
    url: '/qqsk/bill/endorsement/available-bills',
    method: 'post',
    data: params
  })
}

// ==================== 票据贴现管理 ====================

/**
 * 分页查询票据贴现
 */
export function getBillDiscountPage(params) {
  return request({
    url: '/qqsk/bill/discount/list',
    method: 'post',
    data: params
  })
}

/**
 * 获取票据贴现详情
 */
export function getBillDiscountDetail(discountId) {
  return request({
    url: `/qqsk/bill/discount/detail/${discountId}`,
    method: 'post'
  })
}

/**
 * 创建票据贴现
 */
export function createBillDiscount(data) {
  return request({
    url: '/qqsk/bill/discount/save',
    method: 'post',
    data
  })
}

/**
 * 更新票据贴现
 */
export function updateBillDiscount(data) {
  return request({
    url: '/qqsk/bill/discount/update',
    method: 'post',
    data
  })
}

/**
 * 删除票据贴现
 */
export function deleteBillDiscount(discountIds) {
  return request({
    url: '/qqsk/bill/discount/delete',
    method: 'post',
    data: discountIds
  })
}

/**
 * 审批票据贴现
 */
export function approveBillDiscount(discountId, data) {
  return request({
    url: `/qqsk/bill/discount/approve/${discountId}`,
    method: 'post',
    data
  })
}

/**
 * 执行票据贴现
 */
export function executeBillDiscount(discountId) {
  return request({
    url: `/qqsk/bill/discount/execute/${discountId}`,
    method: 'post'
  })
}

// ==================== 票据到期管理 ====================

/**
 * 分页查询票据到期
 */
export function getBillMaturityPage(params) {
  return request({
    url: '/qqsk/bill/maturity/list',
    method: 'post',
    data: params
  })
}

/**
 * 获取票据到期详情
 */
export function getBillMaturityDetail(maturityId) {
  return request({
    url: `/qqsk/bill/maturity/detail/${maturityId}`,
    method: 'post'
  })
}

/**
 * 处理票据到期
 */
export function processBillMaturity(data) {
  return request({
    url: '/qqsk/bill/maturity/process',
    method: 'post',
    data
  })
}

/**
 * 发送到期提醒
 */
export function sendMaturityReminder(data) {
  return request({
    url: '/qqsk/bill/maturity/reminder',
    method: 'post',
    data
  })
}

/**
 * 获取到期日历数据
 */
export function getMaturityCalendarData(params) {
  return request({
    url: '/qqsk/bill/maturity/calendar',
    method: 'post',
    data: params
  })
}

// ==================== 票据查询统计 ====================

/**
 * 获取票据统计概览
 */
export function getBillStatisticsOverview(params) {
  return request({
    url: '/qqsk/bill/statistics/overview',
    method: 'post',
    data: params
  })
}

/**
 * 获取票据类型统计
 */
export function getBillTypeStatistics(params) {
  return request({
    url: '/qqsk/bill/statistics/type',
    method: 'post',
    data: params
  })
}

/**
 * 获取票据趋势分析
 */
export function getBillTrendAnalysis(params) {
  return request({
    url: '/qqsk/bill/statistics/trend',
    method: 'post',
    data: params
  })
}

// ==================== 票据风险管理 ====================

/**
 * 分页查询票据风险评估
 */
export function getBillRiskPage(params) {
  return request({
    url: '/qqsk/bill/risk/list',
    method: 'post',
    data: params
  })
}

/**
 * 获取票据风险详情
 */
export function getBillRiskDetail(riskId) {
  return request({
    url: `/qqsk/bill/risk/detail/${riskId}`,
    method: 'post'
  })
}

/**
 * 创建票据风险评估
 */
export function createBillRiskAssessment(data) {
  return request({
    url: '/qqsk/bill/risk/assessment/save',
    method: 'post',
    data
  })
}

/**
 * 更新票据风险评估
 */
export function updateBillRiskAssessment(data) {
  return request({
    url: '/qqsk/bill/risk/assessment/update',
    method: 'post',
    data
  })
}

/**
 * 获取票据风险预警
 */
export function getBillRiskAlerts(params) {
  return request({
    url: '/qqsk/bill/risk/alerts',
    method: 'post',
    data: params
  })
}

/**
 * 处置票据风险
 */
export function disposeBillRisk(data) {
  return request({
    url: '/qqsk/bill/risk/dispose',
    method: 'post',
    data
  })
}

// ==================== 电子票据管理 ====================

/**
 * 分页查询电子票据
 */
export function getElectronicBillPage(params) {
  return request({
    url: '/qqsk/bill/electronic/list',
    method: 'post',
    data: params
  })
}

/**
 * 获取电子票据详情
 */
export function getElectronicBillDetail(billId) {
  return request({
    url: `/qqsk/bill/electronic/detail/${billId}`,
    method: 'post'
  })
}

/**
 * 创建电子票据
 */
export function createElectronicBill(data) {
  return request({
    url: '/qqsk/bill/electronic/save',
    method: 'post',
    data
  })
}

/**
 * 更新电子票据
 */
export function updateElectronicBill(data) {
  return request({
    url: '/qqsk/bill/electronic/update',
    method: 'post',
    data
  })
}

/**
 * 删除电子票据
 */
export function deleteElectronicBill(billIds) {
  return request({
    url: '/qqsk/bill/electronic/delete',
    method: 'post',
    data: billIds
  })
}

/**
 * 数字签名电子票据
 */
export function signElectronicBill(data) {
  return request({
    url: '/qqsk/bill/electronic/sign',
    method: 'post',
    data
  })
}

/**
 * 验证电子票据
 */
export function verifyElectronicBill(data) {
  return request({
    url: '/qqsk/bill/electronic/verify',
    method: 'post',
    data
  })
}

/**
 * 追踪电子票据流转
 */
export function trackElectronicBill(billId) {
  return request({
    url: `/qqsk/bill/electronic/track/${billId}`,
    method: 'post'
  })
}

// ==================== 票据池管理 ====================

/**
 * 分页查询票据池
 */
export function getBillPoolPage(params) {
  return request({
    url: '/qqsk/bill/pool/list',
    method: 'post',
    data: params
  })
}

/**
 * 获取票据池详情
 */
export function getBillPoolDetail(poolId) {
  return request({
    url: `/qqsk/bill/pool/detail/${poolId}`,
    method: 'post'
  })
}

/**
 * 创建票据池
 */
export function createBillPool(data) {
  return request({
    url: '/qqsk/bill/pool/save',
    method: 'post',
    data
  })
}

/**
 * 更新票据池
 */
export function updateBillPool(data) {
  return request({
    url: '/qqsk/bill/pool/update',
    method: 'post',
    data
  })
}

/**
 * 删除票据池
 */
export function deleteBillPool(poolIds) {
  return request({
    url: '/qqsk/bill/pool/delete',
    method: 'post',
    data: poolIds
  })
}

/**
 * 添加票据到池
 */
export function addBillsToPool(data) {
  return request({
    url: '/qqsk/bill/pool/add-bills',
    method: 'post',
    data
  })
}

/**
 * 从池中移除票据
 */
export function removeBillsFromPool(data) {
  return request({
    url: '/qqsk/bill/pool/remove-bills',
    method: 'post',
    data
  })
}

/**
 * 质押融资
 */
export function pledgeFinancing(data) {
  return request({
    url: '/qqsk/bill/pool/pledge-financing',
    method: 'post',
    data
  })
}

/**
 * 获取票据池内票据
 */
export function getBillPoolBills(poolId) {
  return request({
    url: `/qqsk/bill/pool/bills/${poolId}`,
    method: 'post'
  })
}

/**
 * 获取票据池融资记录
 */
export function getBillPoolFinancingRecords(poolId) {
  return request({
    url: `/qqsk/bill/pool/financing-records/${poolId}`,
    method: 'post'
  })
}
