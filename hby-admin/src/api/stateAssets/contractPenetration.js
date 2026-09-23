import request from '@/utils/request'
import { transData } from '@/utils/requestData'

const BASE = '/monitor/v1/supervision/contract'

// ========== 合同台账 ==========
export function getContractList(data) {
  return request({ url: `${BASE}/list`, method: 'post', data: transData(data) })
}
export function getContractDetail(id) {
  return request({ url: `${BASE}/detail/${id}`, method: 'get' })
}
export function addContract(data) {
  return request({ url: `${BASE}/add`, method: 'post', data: transData(data) })
}
export function updateContract(data) {
  return request({ url: `${BASE}/update`, method: 'post', data: transData(data) })
}
export function deleteContract(id) {
  return request({ url: `${BASE}/delete/${id}`, method: 'post' })
}
export function saveContract(data) {
  return data.id ? updateContract(data) : addContract(data)
}
export function markMajorContract(data) {
  return request({ url: `${BASE}/major/mark`, method: 'post', data: transData(data) })
}
export function getContractStatistics(companyId) {
  return request({ url: `${BASE}/statistics`, method: 'get', params: { companyId } })
}

// ========== 生命周期 ==========
export function getContractLifecycle(id) {
  return request({ url: `${BASE}/lifecycle/${id}`, method: 'get' })
}
export function addLifecycleNode(data) {
  return request({ url: `${BASE}/lifecycle/add`, method: 'post', data: transData(data) })
}
export function updatePaymentProgress(data) {
  return request({ url: `${BASE}/lifecycle/payment`, method: 'post', data: transData(data) })
}
export function getMilestones(id) {
  return request({ url: `${BASE}/lifecycle/milestones/${id}`, method: 'get' })
}

// ========== 审批合规 ==========
export function getComplianceList(data) {
  return request({ url: `${BASE}/compliance/list`, method: 'post', data: transData(data) })
}
export function getApprovalChain(id) {
  return request({ url: `${BASE}/compliance/chain/${id}`, method: 'get' })
}
export function submitRectification(data) {
  return request({ url: `${BASE}/compliance/rectify`, method: 'post', data: transData(data) })
}
export function getComplianceDetail(contractNo) {
  return request({ url: `${BASE}/compliance/detail/${contractNo}`, method: 'get' })
}

// ========== 履行监控 ==========
export function getExecutionList(data) {
  return request({ url: `${BASE}/execution/list`, method: 'post', data: transData(data) })
}
export function getExpiringContracts() {
  return request({ url: `${BASE}/execution/expiring`, method: 'get' })
}

// ========== 纠纷诉讼 ==========
export function getDisputeList(data) {
  return request({ url: `${BASE}/dispute/list`, method: 'post', data: transData(data) })
}
export function getDisputeDetail(id) {
  return request({ url: `${BASE}/dispute/detail/${id}`, method: 'get' })
}
export function addDisputeNode(data) {
  return request({ url: `${BASE}/dispute/node/add`, method: 'post', data: transData(data) })
}
export { getDisputeList as getContractDisputeList }
export function addContractDispute(data) {
  return request({ url: `${BASE}/dispute/add`, method: 'post', data: transData(data) })
}
export function updateContractDispute(data) {
  return request({ url: `${BASE}/dispute/update`, method: 'post', data: transData(data) })
}

// ========== 对方信用 ==========
export function getCounterpartyList(data) {
  return request({ url: `${BASE}/counterparty/list`, method: 'post', data: transData(data) })
}
export function getCounterpartyDetail(name) {
  return request({ url: `${BASE}/counterparty/detail`, method: 'get', params: { name } })
}

// ========== 风险预警 ==========
export function getContractWarningList(data) {
  return request({ url: `${BASE}/warning/list`, method: 'post', data: transData(data) })
}
export function handleContractWarning(data) {
  return request({ url: `${BASE}/warning/handle`, method: 'post', data: transData(data) })
}
export function closeContractWarning(id) {
  return request({ url: `${BASE}/warning/close/${id}`, method: 'post' })
}
export function getContractWarningDetail(warnNo) {
  return request({ url: `${BASE}/warning/detail/${warnNo}`, method: 'get' })
}

// ========== 穿透分析 ==========
export function getContractDrillData(params) {
  return request({ url: `${BASE}/drill/data`, method: 'get', params })
}

export function exportDrillReport(data) {
  return request({ url: `${BASE}/drill/export`, method: 'post', data: transData(data || {}), responseType: 'blob' })
}

// ========== 驾驶舱 ==========
export function getContractDashboard() {
  return request({ url: `${BASE}/dashboard/overview`, method: 'get' })
}
export function getContractDashboardTrends() {
  return request({ url: `${BASE}/dashboard/trends`, method: 'get' })
}

// ========== 数据同步 ==========
export function syncInternalContracts() {
  return request({ url: `${BASE}/sync/internal`, method: 'post' })
}
export function getDataSourceList() {
  return request({ url: `${BASE}/datasource/list`, method: 'get' })
}

// ========== 导出 ==========
export function exportContracts(data) {
  return request({ url: `${BASE}/export`, method: 'post', data: transData(data || {}), responseType: 'blob' })
}

// ========== 批量导入 ==========
export function importContracts(file) {
  const formData = new FormData()
  formData.append('file', file)
  return request({ url: `${BASE}/import`, method: 'post', data: formData, headers: { 'Content-Type': 'multipart/form-data' } })
}


// ========== 合同台账 ==========
export function getContractRecordList(data) {
  return request({ url: `${BASE}/record/list`, method: 'post', data: transData(data) })
}
export function addContractRecord(data) {
  return request({ url: `${BASE}/record/add`, method: 'post', data: transData(data) })
}
export function updateContractRecord(data) {
  return request({ url: `${BASE}/record/update`, method: 'post', data: transData(data) })
}
export function deleteContractRecord(id) {
  return request({ url: `${BASE}/record/${id}`, method: 'delete' })
}
export function batchDeleteContractRecord(data) {
  return request({ url: `${BASE}/record/batch/delete`, method: 'post', data: transData(data) })
}

// ========== 合同智能审查 ==========
export function getSmartReviewList(data) {
  return request({ url: `${BASE}/review/list`, method: 'post', data: transData(data) })
}
export function getSmartReviewDetail(id) {
  return request({ url: `${BASE}/review/detail/${id}`, method: 'get' })
}
export function addSmartReview(data) {
  return request({ url: `${BASE}/review/add`, method: 'post', data: transData(data) })
}
export function updateSmartReview(data) {
  return request({ url: `${BASE}/review/update`, method: 'post', data: transData(data) })
}
export function deleteSmartReview(id) {
  return request({ url: `${BASE}/review/delete/${id}`, method: 'post' })
}
export function batchDeleteSmartReview(ids) {
  return request({ url: `${BASE}/review/batchDelete`, method: 'post', data: transData({ ids }) })
}

// ========== 案件管理 ==========
export function getCaseList(data) {
  return request({ url: `${BASE}/case/list`, method: 'post', data: transData(data) })
}
export function getCaseDetail(id) {
  return request({ url: `${BASE}/case/detail/${id}`, method: 'get' })
}
export function addCase(data) {
  return request({ url: `${BASE}/case/add`, method: 'post', data: transData(data) })
}
export function updateCase(data) {
  return request({ url: `${BASE}/case/update`, method: 'post', data: transData(data) })
}
export function deleteCase(id) {
  return request({ url: `${BASE}/case/delete/${id}`, method: 'post' })
}
export function getCaseAnalysis(data) {
  return request({ url: `${BASE}/case/analysis`, method: 'post', data: transData(data || {}) })
}

// ========== 审批追踪 ==========
export function getApprovalTrackList(data) {
  return request({ url: `${BASE}/approval/list`, method: 'post', data: transData(data) })
}
export function getApprovalFlow(contractId) {
  return request({ url: `${BASE}/approval/flow/${contractId}`, method: 'get' })
}

// ========== 履行监控 ==========
export function getPerformanceList(data) {
  return request({ url: `${BASE}/performance/list`, method: 'post', data: transData(data) })
}
export function getPerformanceDetail(contractId) {
  return request({ url: `${BASE}/performance/detail/${contractId}`, method: 'get' })
}

// ========== 信用分析 ==========
export function getCreditList(data) {
  return request({ url: `${BASE}/credit/list`, method: 'post', data: transData(data) })
}
export function getCreditDetail(name) {
  return request({ url: `${BASE}/credit/detail`, method: 'get', params: { name } })
}