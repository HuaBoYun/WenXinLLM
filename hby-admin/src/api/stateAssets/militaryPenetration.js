import request from '@/utils/request'

// ========== 军品任务台账（原有） ==========
export function getMilitaryTaskList(data) {
  return request({ url: '/monitor/v1/supervision/military/task/list', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}

export function getMilitaryTaskDetail(id) {
  return request({ url: '/monitor/v1/supervision/military/task/' + id, method: 'get' })
}

export function addMilitaryTask(data) {
  return request({ url: '/monitor/v1/supervision/military/task/add', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}

export function updateMilitaryTask(data) {
  return request({ url: '/monitor/v1/supervision/military/task/update', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}

export function deleteMilitaryTask(id) {
  return request({ url: '/monitor/v1/supervision/military/task/' + id, method: 'delete' })
}

export function batchDeleteMilitaryTask(data) {
  return request({ url: '/monitor/v1/supervision/military/task/batch/delete', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}

export function getMilitaryStatistics(companyId) {
  return request({ url: '/monitor/v1/supervision/military/task/statistics', method: 'get', params: { companyId } })
}

// ========== 首页 ==========
export function getMilitaryKPI() {
  return request({ url: '/monitor/v1/supervision/military/home/kpi', method: 'get' })
}

export function getMilitaryTree(params) {
  return request({ url: '/monitor/v1/supervision/military/home/tree', method: 'get', params })
}

export function getMilitaryWarnings() {
  return request({ url: '/monitor/v1/supervision/military/home/warnings', method: 'get' })
}

// ========== 军品驾驶舱 ==========
export function getMilitaryDashboard() {
  return request({ url: '/monitor/v1/supervision/military/dashboard/overview', method: 'get' })
}

export function getMilitaryTrend(params) {
  return request({ url: '/monitor/v1/supervision/military/dashboard/trend', method: 'get', params })
}

export function getMilitaryDashboardTypeData(params) {
  return request({ url: '/monitor/v1/supervision/military/dashboard/type-distribution', method: 'get', params })
}

export function getMilitaryDashboardGaugeData(params) {
  return request({ url: '/monitor/v1/supervision/military/dashboard/gauge', method: 'get', params })
}

export function getMilitaryDashboardSecretData(params) {
  return request({ url: '/monitor/v1/supervision/military/dashboard/secret-distribution', method: 'get', params })
}

// ========== 任务台账 ==========
export function getTaskRecordList(data) {
  return request({ url: '/monitor/v1/supervision/military/task-record/list', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}

export function getTaskRecordDetail(id) {
  return request({ url: `/monitor/v1/supervision/military/task-record/${id}`, method: 'get' })
}

export function addTaskRecord(data) {
  return request({ url: '/monitor/v1/supervision/military/task-record/add', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}

export function updateTaskRecord(data) {
  return request({ url: '/monitor/v1/supervision/military/task-record/update', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}

export function exportTaskRecords(data) {
  return request({ url: '/monitor/v1/supervision/military/task-record/export', method: 'post', data, headers: { 'Content-Type': 'application/json' }, responseType: 'blob' })
}

// ========== 资质档案 ==========
export function getQualificationList(data) {
  return request({ url: '/monitor/v1/supervision/military/qualification/list', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}

export function getQualificationDetail(id) {
  return request({ url: `/monitor/v1/supervision/military/qualification/${id}`, method: 'get' })
}

export function addQualification(data) {
  return request({ url: '/monitor/v1/supervision/military/qualification/add', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}

export function updateQualification(data) {
  return request({ url: '/monitor/v1/supervision/military/qualification/update', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}

export function getExpiringQualifications(days) {
  return request({ url: '/monitor/v1/supervision/military/qualification/expiring', method: 'get', params: { days } })
}

export function deleteQualification(id) {
  return request({ url: `/monitor/v1/supervision/military/qualification/${id}`, method: 'delete' })
}

export function getQualificationStatistics(companyId) {
  return request({ url: '/monitor/v1/supervision/military/qualification/statistics', method: 'get', params: { companyId } })
}

// ========== 供应链安全 ==========
export function getSupplyChainSecurityList(data) {
  return request({ url: '/monitor/v1/supervision/military/supply-chain/list', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}

export function getSupplyChainDetail(id) {
  return request({ url: `/monitor/v1/supervision/military/supply-chain/${id}`, method: 'get' })
}

export function getSupplyChainStatistics(companyId) {
  return request({ url: '/monitor/v1/supervision/military/supply-chain/statistics', method: 'get', params: { companyId } })
}

export function getForeignDependencyList(companyId) {
  return request({ url: '/monitor/v1/supervision/military/supply-chain/foreign', method: 'get', params: { companyId } })
}

export function getSingleSourceList(companyId) {
  return request({ url: '/monitor/v1/supervision/military/supply-chain/single-source', method: 'get', params: { companyId } })
}

// ========== 分包合规 ==========
export function getSubcontractComplianceList(data) {
  return request({ url: '/monitor/v1/supervision/military/subcontract/list', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}

export function getSubcontractDetail(id) {
  return request({ url: `/monitor/v1/supervision/military/subcontract/${id}`, method: 'get' })
}

export function getSubcontractStatistics(companyId) {
  return request({ url: '/monitor/v1/supervision/military/subcontract/statistics', method: 'get', params: { companyId } })
}

export function reportSubcontractViolation(data) {
  return request({ url: '/monitor/v1/supervision/military/subcontract/report-violation', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}

export function approveSubcontract(data) {
  return request({ url: '/monitor/v1/supervision/military/subcontract/approve', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}

export function getSubcontractViolationList(companyId) {
  return request({ url: '/monitor/v1/supervision/military/subcontract/violations', method: 'get', params: { companyId } })
}

// ========== 合同履约追踪 ==========
export function getMilitaryContractList(data) {
  return request({ url: '/monitor/v1/supervision/military/contract/list', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}

export function getMilitaryContractDetail(id) {
  return request({ url: `/monitor/v1/supervision/military/contract/${id}`, method: 'get' })
}

export function updateContractProgress(data) {
  return request({ url: '/monitor/v1/supervision/military/contract/progress', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}

export function updateContractAcceptance(data) {
  return request({ url: '/monitor/v1/supervision/military/contract/acceptance', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}

export function getMilitaryOverdueContracts(companyId) {
  return request({ url: '/monitor/v1/supervision/military/contract/overdue', method: 'get', params: { companyId } })
}

export function reportContractBreach(data) {
  return request({ url: '/monitor/v1/supervision/military/contract/breach', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}

export function getContractStatistics(companyId) {
  return request({ url: '/monitor/v1/supervision/military/contract/statistics', method: 'get', params: { companyId } })
}

// ========== 军品风险穿透 ==========
export function getMilitaryDrillDownTree(data) {
  return request({ url: '/monitor/v1/supervision/military/drill-down/tree', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}

export function getMilitaryDrillDownDetail(nodeId) {
  return request({ url: `/monitor/v1/supervision/military/drill-down/detail/${nodeId}`, method: 'get' })
}

export function getMilitaryAlertList(data) {
  return request({ url: '/monitor/v1/supervision/military/alert/list', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}

export function dismissMilitaryAlert(alertId) {
  return request({ url: `/monitor/v1/supervision/military/alert/dismiss/${alertId}`, method: 'post' })
}

export function exportMilitaryReport(data) {
  return request({ url: '/monitor/v1/supervision/military/report/export', method: 'post', data, headers: { 'Content-Type': 'application/json' }, responseType: 'blob' })
}

// ========== 预警规则管理 ==========
export function getMilitaryAlertRules() {
  return request({ url: '/monitor/v1/supervision/military/alert/rules', method: 'get' })
}

export function addMilitaryAlertRule(data) {
  return request({ url: '/monitor/v1/supervision/military/alert/rule/add', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}

export function updateMilitaryAlertRule(data) {
  return request({ url: '/monitor/v1/supervision/military/alert/rule/update', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}

// ========== 军品综合统计 ==========
export function getMilitaryOverview() {
  return request({ url: '/monitor/v1/supervision/military/overview', method: 'get' })
}

export function getMilitaryRiskMap(params) {
  return request({ url: '/monitor/v1/supervision/military/risk-map', method: 'get', params })
}

export function getMilitarySecretLevelStatistics() {
  return request({ url: '/monitor/v1/supervision/military/secret-level/statistics', method: 'get' })
}

// ========== 保密管理 ==========
export function getSecurityList(data) {
  return request({ url: '/monitor/v1/supervision/military/security/list', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}
export function getSecurityDetail(id) {
  return request({ url: `/monitor/v1/supervision/military/security/${id}`, method: 'get' })
}
export function addSecurity(data) {
  return request({ url: '/monitor/v1/supervision/military/security/add', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}
export function updateSecurity(data) {
  return request({ url: '/monitor/v1/supervision/military/security/update', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}
export function deleteSecurity(id) {
  return request({ url: `/monitor/v1/supervision/military/security/${id}`, method: 'delete' })
}
export function getSecurityStatistics() {
  return request({ url: '/monitor/v1/supervision/military/security/statistics', method: 'get' })
}

// ========== 质量管理 ==========
export function getQualityList(data) {
  return request({ url: '/monitor/v1/supervision/military/quality/list', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}
export function getQualityDetail(id) {
  return request({ url: `/monitor/v1/supervision/military/quality/${id}`, method: 'get' })
}
export function addQuality(data) {
  return request({ url: '/monitor/v1/supervision/military/quality/add', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}
export function updateQuality(data) {
  return request({ url: '/monitor/v1/supervision/military/quality/update', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}
export function deleteQuality(id) {
  return request({ url: `/monitor/v1/supervision/military/quality/${id}`, method: 'delete' })
}
export function getQualityStatistics() {
  return request({ url: '/monitor/v1/supervision/military/quality/statistics', method: 'get' })
}

// ========== 资产管理 ==========
export function getAssetList(data) {
  return request({ url: '/monitor/v1/supervision/military/asset/list', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}
export function getAssetDetail(id) {
  return request({ url: `/monitor/v1/supervision/military/asset/${id}`, method: 'get' })
}
export function addAsset(data) {
  return request({ url: '/monitor/v1/supervision/military/asset/add', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}
export function updateAsset(data) {
  return request({ url: '/monitor/v1/supervision/military/asset/update', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}
export function deleteAsset(id) {
  return request({ url: `/monitor/v1/supervision/military/asset/${id}`, method: 'delete' })
}
export function getAssetStatistics() {
  return request({ url: '/monitor/v1/supervision/military/asset/statistics', method: 'get' })
}

// ========== 任务台账补充 ==========
export function deleteTaskRecord(id) {
  return request({ url: `/monitor/v1/supervision/military/task-record/${id}`, method: 'delete' })
}

// ========== 供应链补充 ==========
export function addSupplyChain(data) {
  return request({ url: '/monitor/v1/supervision/military/supply-chain/add', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}
export function updateSupplyChain(data) {
  return request({ url: '/monitor/v1/supervision/military/supply-chain/update', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}
export function deleteSupplyChain(id) {
  return request({ url: `/monitor/v1/supervision/military/supply-chain/${id}`, method: 'delete' })
}

// ========== 分包补充 ==========
export function addSubcontract(data) {
  return request({ url: '/monitor/v1/supervision/military/subcontract/add', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}
export function updateSubcontract(data) {
  return request({ url: '/monitor/v1/supervision/military/subcontract/update', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}
export function deleteSubcontract(id) {
  return request({ url: `/monitor/v1/supervision/military/subcontract/${id}`, method: 'delete' })
}

// ========== 合同补充 ==========
export function addMilitaryContract(data) {
  return request({ url: '/monitor/v1/supervision/military/contract/add', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}
export function updateMilitaryContract(data) {
  return request({ url: '/monitor/v1/supervision/military/contract/update', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}
export function deleteMilitaryContract(id) {
  return request({ url: `/monitor/v1/supervision/military/contract/${id}`, method: 'delete' })
}

