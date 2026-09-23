import request from '@/utils/request'

// ========== 采购项目台账 ==========
export function getProcurementProjectList(data) {
  return request({ url: '/monitor/v1/supervision/procurement/project/list', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}
export function getProcurementProjectDetail(id) {
  return request({ url: '/monitor/v1/supervision/procurement/project/' + id, method: 'get' })
}
export function addProcurementProject(data) {
  return request({ url: '/monitor/v1/supervision/procurement/project/add', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}
export function updateProcurementProject(data) {
  return request({ url: '/monitor/v1/supervision/procurement/project/update', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}
export function deleteProcurementProject(id) {
  return request({ url: '/monitor/v1/supervision/procurement/project/' + id, method: 'delete' })
}
export function batchDeleteProcurementProject(data) {
  return request({ url: '/monitor/v1/supervision/procurement/project/batch/delete', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}
export function getProcurementStatistics(companyId) {
  return request({ url: '/monitor/v1/supervision/procurement/project/statistics', method: 'get', params: { companyId } })
}

// ========== 首页 ==========
export function getProcurementKPI() {
  return request({ url: '/monitor/v1/supervision/procurement/home/kpi', method: 'get' })
}
export function getProcurementTree(params) {
  return request({ url: '/monitor/v1/supervision/procurement/home/tree', method: 'get', params })
}
export function getProcurementWarnings() {
  return request({ url: '/monitor/v1/supervision/procurement/home/warnings', method: 'get' })
}

// ========== 采购驾驶舱 ==========
export function getProcurementDashboard() {
  return request({ url: '/monitor/v1/supervision/procurement/dashboard/overview', method: 'get' })
}
export function getProcurementTrend(params) {
  return request({ url: '/monitor/v1/supervision/procurement/dashboard/trend', method: 'get', params })
}

// ========== 采购台账 ==========
export function getPurchaseRecordList(data) {
  return request({ url: '/monitor/v1/supervision/procurement/purchase-record/list', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}
export function getPurchaseRecordDetail(id) {
  return request({ url: `/monitor/v1/supervision/procurement/purchase-record/${id}`, method: 'get' })
}
export function addPurchaseRecord(data) {
  return request({ url: '/monitor/v1/supervision/procurement/purchase-record/add', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}
export function updatePurchaseRecord(data) {
  return request({ url: '/monitor/v1/supervision/procurement/purchase-record/update', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}
export function deletePurchaseRecord(id) {
  return request({ url: `/monitor/v1/supervision/procurement/purchase-record/${id}`, method: 'delete' })
}
export function exportPurchaseRecords(data) {
  return request({ url: '/monitor/v1/supervision/procurement/purchase-record/export', method: 'post', data, headers: { 'Content-Type': 'application/json' }, responseType: 'blob' })
}

// ========== 供应商档案 ==========
export function getSupplierProfileList(data) {
  return request({ url: '/monitor/v1/supervision/procurement/supplier/list', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}
export function getSupplierProfileDetail(id) {
  return request({ url: `/monitor/v1/supervision/procurement/supplier/${id}`, method: 'get' })
}
export function addSupplierProfile(data) {
  return request({ url: '/monitor/v1/supervision/procurement/supplier/add', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}
export function updateSupplierProfile(data) {
  return request({ url: '/monitor/v1/supervision/procurement/supplier/update', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}
export function blacklistSupplier(id, reason) {
  return request({ url: '/monitor/v1/supervision/procurement/supplier/blacklist', method: 'post', data: { id, reason }, headers: { 'Content-Type': 'application/json' } })
}
export function getSupplierConcentration(companyId) {
  return request({ url: '/monitor/v1/supervision/procurement/supplier/concentration', method: 'get', params: { companyId } })
}
export function deleteSupplier(id) {
  return request({ url: `/monitor/v1/supervision/procurement/supplier/${id}`, method: 'delete' })
}
export function getSupplierList(data) { return getSupplierProfileList(data) }
export function getSupplierDetail(id) { return getSupplierProfileDetail(id) }
export function addSupplier(data) { return addSupplierProfile(data) }
export function updateSupplier(data) { return updateSupplierProfile(data) }

// ========== 关联交易监控 ==========
export function getRelatedTransactionList(data) {
  return request({ url: '/monitor/v1/supervision/procurement/related-transaction/list', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}
export function getRelatedTransactionDetail(id) {
  return request({ url: `/monitor/v1/supervision/procurement/related-transaction/${id}`, method: 'get' })
}
export function getRelatedTransactionStatistics(companyId) {
  return request({ url: '/monitor/v1/supervision/procurement/related-transaction/statistics', method: 'get', params: { companyId } })
}
export function submitRelatedTransactionDisclosure(data) {
  return request({ url: '/monitor/v1/supervision/procurement/related-transaction/disclosure', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}

// ========== 招投标合规 ==========
export function getBiddingComplianceList(data) {
  return request({ url: '/monitor/v1/supervision/procurement/bidding/list', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}
export function getBiddingComplianceDetail(id) {
  return request({ url: `/monitor/v1/supervision/procurement/bidding/${id}`, method: 'get' })
}
export function getBiddingComplianceStatistics(companyId) {
  return request({ url: '/monitor/v1/supervision/procurement/bidding/statistics', method: 'get', params: { companyId } })
}
export function reportBiddingViolation(data) {
  return request({ url: '/monitor/v1/supervision/procurement/bidding/report-violation', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}

// ========== 合同履约追踪 ==========
export function getContractExecutionList(data) {
  return request({ url: '/monitor/v1/supervision/procurement/contract/list', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}
export function getContractExecutionDetail(id) {
  return request({ url: `/monitor/v1/supervision/procurement/contract/${id}`, method: 'get' })
}
export function updateContractPayment(data) {
  return request({ url: '/monitor/v1/supervision/procurement/contract/payment', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}
export function updateContractAcceptance(data) {
  return request({ url: '/monitor/v1/supervision/procurement/contract/acceptance', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}
export function getContractOverdueList(companyId) {
  return request({ url: '/monitor/v1/supervision/procurement/contract/overdue', method: 'get', params: { companyId } })
}

// ========== 采购风险穿透 ==========
export function getProcurementDrillDownTree(data) {
  return request({ url: '/monitor/v1/supervision/procurement/drill-down/tree', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}
export function getProcurementDrillDownDetail(nodeId) {
  return request({ url: `/monitor/v1/supervision/procurement/drill-down/detail/${nodeId}`, method: 'get' })
}
export function getProcurementAlertList(data) {
  return request({ url: '/monitor/v1/supervision/procurement/alert/list', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}
export function dismissProcurementAlert(alertId) {
  return request({ url: `/monitor/v1/supervision/procurement/alert/dismiss/${alertId}`, method: 'post' })
}
export function exportProcurementReport(data) {
  return request({ url: '/monitor/v1/supervision/procurement/report/export', method: 'post', data, headers: { 'Content-Type': 'application/json' }, responseType: 'blob' })
}

// ========== 招标过程监控 ==========
export function getBiddingMonitorList(data) {
  return request({ url: '/monitor/v1/supervision/procurement/bidding-monitor/list', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}
export function getBiddingMonitorDetail(id) {
  return request({ url: `/monitor/v1/supervision/procurement/bidding-monitor/${id}`, method: 'get' })
}
export function addBiddingMonitor(data) {
  return request({ url: '/monitor/v1/supervision/procurement/bidding-monitor/add', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}
export function updateBiddingMonitor(data) {
  return request({ url: '/monitor/v1/supervision/procurement/bidding-monitor/update', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}
export function dispatchBiddingMonitor(data) {
  return request({ url: '/monitor/v1/supervision/procurement/bidding-monitor/dispatch', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}
export function getBidderRelationList(data) {
  return request({ url: '/monitor/v1/supervision/procurement/bidding-monitor/relation/list', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}

// ========== 价格对标 ==========
export function getPriceBenchmarkList(data) {
  return request({ url: '/monitor/v1/supervision/procurement/price-benchmark/list', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}
export function getPriceBenchmarkDetail(id) {
  return request({ url: `/monitor/v1/supervision/procurement/price-benchmark/${id}`, method: 'get' })
}
export function addPriceBenchmark(data) {
  return request({ url: '/monitor/v1/supervision/procurement/price-benchmark/add', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}
export function updatePriceBenchmark(data) {
  return request({ url: '/monitor/v1/supervision/procurement/price-benchmark/update', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}
export function deletePriceBenchmark(id) {
  return request({ url: `/monitor/v1/supervision/procurement/price-benchmark/${id}`, method: 'delete' })
}

// ========== 供应链风险 ==========
export function getSupplyChainRiskList(data) {
  return request({ url: '/monitor/v1/supervision/procurement/supply-chain-risk/list', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}
export function getSupplyChainRiskDetail(id) {
  return request({ url: `/monitor/v1/supervision/procurement/supply-chain-risk/${id}`, method: 'get' })
}
export function addSupplyChainRisk(data) {
  return request({ url: '/monitor/v1/supervision/procurement/supply-chain-risk/add', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}
export function updateSupplyChainRisk(data) {
  return request({ url: '/monitor/v1/supervision/procurement/supply-chain-risk/update', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}
export function getSupplyChainRiskStatistics() {
  return request({ url: '/monitor/v1/supervision/procurement/supply-chain-risk/statistics', method: 'get' })
}
export function deleteSupplyChainRisk(id) {
  return request({ url: `/monitor/v1/supervision/procurement/supply-chain-risk/${id}`, method: 'delete' })
}

// ========== 虚假贸易核查 ==========
export function getFakeTradeList(data) {
  return request({ url: '/monitor/v1/supervision/procurement/fake-trade/list', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}
export function getFakeTradeDetail(id) {
  return request({ url: `/monitor/v1/supervision/procurement/fake-trade/${id}`, method: 'get' })
}
export function addFakeTrade(data) {
  return request({ url: '/monitor/v1/supervision/procurement/fake-trade/add', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}
export function updateFakeTrade(data) {
  return request({ url: '/monitor/v1/supervision/procurement/fake-trade/update', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}
export function deleteFakeTrade(id) {
  return request({ url: `/monitor/v1/supervision/procurement/fake-trade/${id}`, method: 'delete' })
}

// ========== 预警管理 ==========
export function addProcurementAlert(data) {
  return request({ url: '/monitor/v1/supervision/procurement/alert/add', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}
export function handleProcurementAlert(data) {
  return request({ url: '/monitor/v1/supervision/procurement/alert/handle', method: 'post', data, headers: { 'Content-Type': 'application/json' } })
}