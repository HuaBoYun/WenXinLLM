import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// ========== 财务报表 ==========
export function getFinanceStatementList(data) {
  return request({
    url: '/monitor/v1/supervision/financial/statement/list',
    method: 'post',
    data: transData(data),
  })
}

export function getFinanceStatementPeriods(params) {
  return request({
    url: '/monitor/v1/supervision/financial/statement/periods',
    method: 'get',
    params,
  })
}

export function getFinanceStatementTypes() {
  return request({
    url: '/monitor/v1/supervision/financial/statement/statement-types',
    method: 'get',
  })
}

export function getFinanceAuditStatuses() {
  return request({
    url: '/monitor/v1/supervision/financial/statement/audit-statuses',
    method: 'get',
  })
}

export function getFinanceStatementDetail(id) {
  return request({
    url: '/monitor/v1/supervision/financial/statement/' + id,
    method: 'get',
  })
}

export function addFinanceStatement(data) {
  return request({
    url: '/monitor/v1/supervision/financial/statement/save',
    method: 'post',
    data: transData(data),
  })
}

export function updateFinanceStatement(data) {
  return request({
    url: '/monitor/v1/supervision/financial/statement/save',
    method: 'post',
    data: transData(data),
  })
}

export function deleteFinanceStatement(id) {
  return request({
    url: '/monitor/v1/supervision/financial/statement/' + id,
    method: 'delete',
  })
}

export function batchDeleteFinanceStatement(data) {
  return request({
    url: '/monitor/v1/supervision/financial/statement/batch-delete',
    method: 'post',
    data: data,
  })
}

export function getFinanceStatistics(companyId) {
  return request({
    url: '/monitor/v1/supervision/financial/statement/statistics',
    method: 'get',
    params: { companyId },
  })
}

// ========== 关联交易 ==========
export function getRelatedTransactionList(data) {
  return request({
    url: '/monitor/v1/supervision/financial/related-party/list',
    method: 'post',
    data: transData(data),
  })
}

export function getRelatedTransactionDetail(id) {
  return request({
    url: '/monitor/v1/supervision/financial/related-party/' + id,
    method: 'get',
  })
}

export function addRelatedTransaction(data) {
  return request({
    url: '/monitor/v1/supervision/financial/related-party/save',
    method: 'post',
    data: transData(data),
  })
}

export function updateRelatedTransaction(data) {
  return request({
    url: '/monitor/v1/supervision/financial/related-party/save',
    method: 'post',
    data: transData(data),
  })
}

export function deleteRelatedTransaction(id) {
  return request({
    url: '/monitor/v1/supervision/financial/related-party/' + id,
    method: 'delete',
  })
}

