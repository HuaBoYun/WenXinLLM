import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// ========== 会计政策台账 ==========
export function getAccountingPolicyList(data) {
  return request({
    url: '/monitor/v1/supervision/accounting/policy/list',
    method: 'post',
    data: transData(data),
  })
}

export function getAccountingPolicyDetail(id) {
  return request({
    url: '/monitor/v1/supervision/accounting/policy/' + id,
    method: 'get',
  })
}

export function addAccountingPolicy(data) {
  return request({
    url: '/monitor/v1/supervision/accounting/policy/add',
    method: 'post',
    data: transData(data),
  })
}

export function updateAccountingPolicy(data) {
  return request({
    url: '/monitor/v1/supervision/accounting/policy/update',
    method: 'post',
    data: transData(data),
  })
}

export function deleteAccountingPolicy(id) {
  return request({
    url: '/monitor/v1/supervision/accounting/policy/' + id,
    method: 'delete',
  })
}

export function batchDeleteAccountingPolicy(data) {
  return request({
    url: '/monitor/v1/supervision/accounting/policy/batch/delete',
    method: 'post',
    data: transData(data),
  })
}

export function getAccountingStatistics(companyId) {
  return request({
    url: '/monitor/v1/supervision/accounting/policy/statistics',
    method: 'get',
    params: { companyId },
  })
}

// ========== 会计凭证穿透 ==========
export function getVoucherPenetrationList(data) {
  return request({ url: '/monitor/v1/supervision/accounting/voucher/list', method: 'post', data: transData(data) })
}
export function getVoucherAnomalyList(data) {
  return request({ url: '/monitor/v1/supervision/accounting/voucher/anomaly/list', method: 'post', data: transData(data) })
}

// ========== 账簿穿透 ==========
export function getSubjectBalanceList(data) {
  return request({ url: '/monitor/v1/supervision/accounting/book/balance/list', method: 'post', data: transData(data) })
}
export function getGeneralLedgerList(data) {
  return request({ url: '/monitor/v1/supervision/accounting/book/general/list', method: 'post', data: transData(data) })
}
export function getSubLedgerList(data) {
  return request({ url: '/monitor/v1/supervision/accounting/book/sub/list', method: 'post', data: transData(data) })
}
export function getAuxLedgerList(data) {
  return request({ url: '/monitor/v1/supervision/accounting/book/aux/list', method: 'post', data: transData(data) })
}
export function getDiaryLedgerList(data) {
  return request({ url: '/monitor/v1/supervision/accounting/book/diary/list', method: 'post', data: transData(data) })
}

// ========== 财务报表穿透 ==========
export function getReportList(data) {
  return request({ url: '/monitor/v1/supervision/accounting/report/list', method: 'post', data: transData(data) })
}
export function getReportQualityScore(data) {
  return request({ url: '/monitor/v1/supervision/accounting/report/quality/score', method: 'post', data: transData(data) })
}

// ========== 预算执行监管 ==========
export function getBudgetMonitorList(data) {
  return request({ url: '/monitor/v1/supervision/accounting/budget/monitor/list', method: 'post', data: transData(data) })
}
export function getBudgetDetailList(data) {
  return request({ url: '/monitor/v1/supervision/accounting/budget/detail/list', method: 'post', data: transData(data) })
}
export function getBudgetAdjustList(data) {
  return request({ url: '/monitor/v1/supervision/accounting/budget/adjust/list', method: 'post', data: transData(data) })
}

// ========== 两金压降监控 ==========
export function getTwoGoldOverview(data) {
  return request({ url: '/monitor/v1/supervision/accounting/twogold/overview', method: 'post', data: transData(data) })
}
export function getReceivableList(data) {
  return request({ url: '/monitor/v1/supervision/accounting/twogold/receivable/list', method: 'post', data: transData(data) })
}
export function getInventoryList(data) {
  return request({ url: '/monitor/v1/supervision/accounting/twogold/inventory/list', method: 'post', data: transData(data) })
}
export function getTwoGoldReductionList(data) {
  return request({ url: '/monitor/v1/supervision/accounting/twogold/reduction/list', method: 'post', data: transData(data) })
}

// ========== 财务造假识别 ==========
export function getFraudRiskList(data) {
  return request({ url: '/monitor/v1/supervision/accounting/fraud/risk/list', method: 'post', data: transData(data) })
}
export function getFraudClueList(data) {
  return request({ url: '/monitor/v1/supervision/accounting/fraud/clue/list', method: 'post', data: transData(data) })
}

// ========== 会计估计台账 ==========
export function getEstimateList(data) {
  return request({ url: '/monitor/v1/supervision/accounting/estimate/list', method: 'post', data: transData(data) })
}
export function addEstimate(data) {
  return request({ url: '/monitor/v1/supervision/accounting/estimate/add', method: 'post', data: transData(data) })
}
export function updateEstimate(data) {
  return request({ url: '/monitor/v1/supervision/accounting/estimate/update', method: 'post', data: transData(data) })
}
export function deleteEstimate(id) {
  return request({ url: '/monitor/v1/supervision/accounting/estimate/' + id, method: 'delete' })
}

// ========== 驾驶舱概览 ==========
export function getAccountingDashboard(data) {
  return request({ url: '/monitor/v1/supervision/accounting/dashboard/overview', method: 'post', data: transData(data) })
}

// ========== 预警信息 ==========
export function getAccountingWarningList(data) {
  return request({ url: '/monitor/v1/supervision/accounting/warning/list', method: 'post', data: transData(data) })
}

// ========== 凭证异常统计 ==========
export function getVoucherAnomalyStats(data) {
  return request({ url: '/monitor/v1/supervision/accounting/voucher/anomaly/stats', method: 'post', data: transData(data) })
}

// ========== 政策一致性检查 ==========
export function getConsistencyCheckList(data) {
  return request({ url: '/monitor/v1/supervision/accounting/consistency/list', method: 'post', data: transData(data) })
}

// ========== 估计变更分析 ==========
export function getChangeAnalysisList(data) {
  return request({ url: '/monitor/v1/supervision/accounting/estimate/change/list', method: 'post', data: transData(data) })
}

// ========== 合规评估 ==========
export function getComplianceEvalData(data) {
  return request({ url: '/monitor/v1/supervision/accounting/compliance/eval', method: 'post', data: transData(data) })
}

// ========== 造假细分 ==========
export function getFraudPerfList(data) {
  return request({ url: '/monitor/v1/supervision/accounting/fraud/perf/list', method: 'post', data: transData(data) })
}
export function getFraudRdList(data) {
  return request({ url: '/monitor/v1/supervision/accounting/fraud/rd/list', method: 'post', data: transData(data) })
}
export function getFraudLeverList(data) {
  return request({ url: '/monitor/v1/supervision/accounting/fraud/lever/list', method: 'post', data: transData(data) })
}
export function getFraudClearList(data) {
  return request({ url: '/monitor/v1/supervision/accounting/fraud/clear/list', method: 'post', data: transData(data) })
}
export function getFraudTwogoldList(data) {
  return request({ url: '/monitor/v1/supervision/accounting/fraud/twogold/list', method: 'post', data: transData(data) })
}

// ========== 两金KPI ==========
export function getTwoGoldKpi(data) {
  return request({ url: '/monitor/v1/supervision/accounting/twogold/kpi', method: 'post', data: transData(data) })
}

// ========== 预算KPI ==========
export function getBudgetKpi(data) {
  return request({ url: '/monitor/v1/supervision/accounting/budget/kpi', method: 'post', data: transData(data) })
}

// ========== 别名导出（兼容 policyAndEstimate 页面引用） ==========
export { addAccountingPolicy as addPolicy }
export { updateAccountingPolicy as updatePolicy }
export { getAccountingPolicyList as getPolicyList }

// ========== 造假线索 CRUD ==========
export function addFraudClue(data) {
  return request({ url: '/monitor/v1/supervision/accounting/fraud/clue/add', method: 'post', data: transData(data) })
}
export function updateFraudClue(data) {
  return request({ url: '/monitor/v1/supervision/accounting/fraud/clue/update', method: 'post', data: transData(data) })
}
export function deleteFraudClue(id) {
  return request({ url: '/monitor/v1/supervision/accounting/fraud/clue/' + id, method: 'delete' })
}
export function getFraudClueDetail(id) {
  return request({ url: '/monitor/v1/supervision/accounting/fraud/clue/' + id, method: 'get' })
}
export function updateFraudClueStatus(data) {
  return request({ url: '/monitor/v1/supervision/accounting/fraud/clue/status', method: 'post', data: transData(data) })
}

// ========== 凭证详情/穿透 ==========
export function getVoucherDetail(id) {
  return request({ url: '/monitor/v1/supervision/accounting/voucher/detail/' + id, method: 'get' })
}
export function getVoucherPenetration(data) {
  return request({ url: '/monitor/v1/supervision/accounting/voucher/penetration', method: 'post', data: transData(data) })
}

// ========== 预警详情 ==========
export function getWarningDetail(id) {
  return request({ url: '/monitor/v1/supervision/accounting/warning/detail/' + id, method: 'get' })
}

// ========== 触发：一致性检查 / 合规评估 ==========
export function runConsistencyCheck(data) {
  return request({ url: '/monitor/v1/supervision/accounting/consistency/run', method: 'post', data: transData(data || {}) })
}
export function runComplianceEval(data) {
  return request({ url: '/monitor/v1/supervision/accounting/compliance/run', method: 'post', data: transData(data || {}) })
}

// ========== 估计历史 ==========
export function getEstimateHistory(data) {
  return request({ url: '/monitor/v1/supervision/accounting/estimate/history', method: 'post', data: transData(data) })
}

// ========== 通用导出（返回 blob） ==========
function exportBlob(url, data) {
  return request({ url, method: 'post', data: transData(data || {}), responseType: 'blob' })
}
export function exportVoucher(data) { return exportBlob('/monitor/v1/supervision/accounting/voucher/export', data) }
export function exportBook(data) { return exportBlob('/monitor/v1/supervision/accounting/book/export', data) }
export function exportReport(data) { return exportBlob('/monitor/v1/supervision/accounting/report/export', data) }
export function exportBudget(data) { return exportBlob('/monitor/v1/supervision/accounting/budget/export', data) }
export function exportTwogold(data) { return exportBlob('/monitor/v1/supervision/accounting/twogold/export', data) }
export function exportFraud(data) { return exportBlob('/monitor/v1/supervision/accounting/fraud/export', data) }
export function exportPolicy(data) { return exportBlob('/monitor/v1/supervision/accounting/policy/export', data) }

// ========== 下发整改 ==========
export function issueRectification(data) {
  return request({ url: '/monitor/v1/supervision/accounting/rectification/issue', method: 'post', data: transData(data) })
}
export function getRectificationList(data) {
  return request({ url: '/monitor/v1/supervision/accounting/rectification/list', method: 'post', data: transData(data) })
}

// ========== 核查 ==========
export function submitVerification(data) {
  return request({ url: '/monitor/v1/supervision/accounting/verification/submit', method: 'post', data: transData(data) })
}
export function getVerificationList(data) {
  return request({ url: '/monitor/v1/supervision/accounting/verification/list', method: 'post', data: transData(data) })
}
