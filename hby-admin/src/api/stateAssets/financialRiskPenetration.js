import request from '@/utils/request'
import { transData } from '@/utils/requestData'

const JSON_HEADERS = { 'Content-Type': 'application/json;charset=UTF-8' }

// ========== 融资记录 ==========
export function getFinancingRecordList(data) {
  return request({
    url: '/monitor/v1/supervision/financial-risk/financing/list',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

export function getFinancingRecordDetail(id) {
  return request({
    url: '/monitor/v1/supervision/financial-risk/financing/' + id,
    method: 'get',
  })
}

export function addFinancingRecord(data) {
  return request({
    url: '/monitor/v1/supervision/financial-risk/financing/add',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

export function updateFinancingRecord(data) {
  return request({
    url: '/monitor/v1/supervision/financial-risk/financing/update',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

export function deleteFinancingRecord(id) {
  return request({
    url: '/monitor/v1/supervision/financial-risk/financing/' + id,
    method: 'delete',
  })
}

export function batchDeleteFinancingRecord(data) {
  return request({
    url: '/monitor/v1/supervision/financial-risk/financing/batch/delete',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

export function getFinancialRiskStatistics(companyId) {
  return request({
    url: '/monitor/v1/supervision/financial-risk/financing/statistics',
    method: 'get',
    params: { companyId },
  })
}

// ========== 担保记录 ==========
export function getGuaranteeRecordList(data) {
  return request({
    url: '/monitor/v1/supervision/financial-risk/financing/guarantee/list',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

export function getGuaranteeRecordDetail(id) {
  return request({
    url: '/monitor/v1/supervision/financial-risk/financing/guarantee/' + id,
    method: 'get',
  })
}

export function addGuaranteeRecord(data) {
  return request({
    url: '/monitor/v1/supervision/financial-risk/financing/guarantee/add',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

export function updateGuaranteeRecord(data) {
  return request({
    url: '/monitor/v1/supervision/financial-risk/financing/guarantee/update',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

export function deleteGuaranteeRecord(id) {
  return request({
    url: '/monitor/v1/supervision/financial-risk/financing/guarantee/' + id,
    method: 'delete',
  })
}

// ========== 委托贷款 ==========
export function getEntrustedLoanList(data) {
  return request({
    url: '/monitor/v1/supervision/financial-risk/entrusted-loan/list',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

export function getEntrustedLoanDetail(id) {
  return request({
    url: '/monitor/v1/supervision/financial-risk/entrusted-loan/' + id,
    method: 'get',
  })
}

export function addEntrustedLoan(data) {
  return request({
    url: '/monitor/v1/supervision/financial-risk/entrusted-loan/add',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

export function updateEntrustedLoan(data) {
  return request({
    url: '/monitor/v1/supervision/financial-risk/entrusted-loan/update',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

export function deleteEntrustedLoan(id) {
  return request({
    url: '/monitor/v1/supervision/financial-risk/entrusted-loan/' + id,
    method: 'delete',
  })
}

export function exportEntrustedLoan(data) {
  return request({
    url: '/monitor/v1/supervision/financial-risk/entrusted-loan/export',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
    responseType: 'blob',
  })
}

export function urgeEntrustedLoan(data) {
  return request({
    url: '/monitor/v1/supervision/financial-risk/entrusted-loan/urge',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// ========== 衍生品业务 ==========
export function getDerivativesList(data) {
  return request({
    url: '/monitor/v1/supervision/financial-risk/derivatives/list',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

export function getDerivativesDetail(id) {
  return request({
    url: '/monitor/v1/supervision/financial-risk/derivatives/' + id,
    method: 'get',
  })
}

export function addDerivatives(data) {
  return request({
    url: '/monitor/v1/supervision/financial-risk/derivatives/add',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

export function updateDerivatives(data) {
  return request({
    url: '/monitor/v1/supervision/financial-risk/derivatives/update',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

export function deleteDerivatives(id) {
  return request({
    url: '/monitor/v1/supervision/financial-risk/derivatives/' + id,
    method: 'delete',
  })
}

export function exportDerivatives(data) {
  return request({
    url: '/monitor/v1/supervision/financial-risk/derivatives/export',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
    responseType: 'blob',
  })
}

// ========== 流动性风险 ==========
export function getLiquidityRiskList(data) {
  return request({
    url: '/monitor/v1/supervision/financial-risk/liquidity/list',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

export function getLiquidityRiskScore(companyId) {
  return request({
    url: '/monitor/v1/supervision/financial-risk/liquidity/score',
    method: 'get',
    params: { companyId },
  })
}

export function getLiquidityStressTest(data) {
  return request({
    url: '/monitor/v1/supervision/financial-risk/liquidity/stress-test',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

export function getFinancingMaturityDistribution(companyId) {
  return request({
    url: '/monitor/v1/supervision/financial-risk/liquidity/maturity-distribution',
    method: 'get',
    params: { companyId },
  })
}

// ========== 金融风险穿透 ==========
export function getFinancialRiskDrillDownTree(data) {
  return request({
    url: '/monitor/v1/supervision/financial-risk/drill-down/tree',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

export function getFinancialRiskDrillDownDetail(nodeId) {
  return request({
    url: '/monitor/v1/supervision/financial-risk/drill-down/detail/' + nodeId,
    method: 'get',
  })
}

export function getFinancialRiskAlertList(data) {
  return request({
    url: '/monitor/v1/supervision/financial-risk/alert/list',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

export function dismissAlert(alertId) {
  return request({
    url: '/monitor/v1/supervision/financial-risk/alert/dismiss/' + alertId,
    method: 'post',
    headers: JSON_HEADERS,
  })
}

// ========== 首页仪表盘 ==========
export function getFinancialRiskDashboard(companyId) {
  return request({
    url: '/monitor/v1/supervision/financial-risk/dashboard',
    method: 'get',
    params: { companyId },
  })
}

export function exportFinancialRiskReport(data) {
  return request({
    url: '/monitor/v1/supervision/financial-risk/report/export',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
    responseType: 'blob',
  })
}
