import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// ========== 产权登记 ==========
export function getPropertyRightList(data) {
  return request({
    url: '/monitor/v1/supervision/property/right/list',
    method: 'post',
    data: transData(data),
  })
}

export function getPropertyRightDetail(id) {
  return request({
    url: '/monitor/v1/supervision/property/right/' + id,
    method: 'get',
  })
}

export function addPropertyRight(data) {
  return request({
    url: '/monitor/v1/supervision/property/right/add',
    method: 'post',
    data: transData(data),
  })
}

export function updatePropertyRight(data) {
  return request({
    url: '/monitor/v1/supervision/property/right/update',
    method: 'post',
    data: transData(data),
  })
}

export function deletePropertyRight(id) {
  return request({
    url: '/monitor/v1/supervision/property/right/' + id,
    method: 'delete',
  })
}

export function batchDeletePropertyRight(data) {
  return request({
    url: '/monitor/v1/supervision/property/right/batch/delete',
    method: 'post',
    data: data,
    headers: { 'Content-Type': 'application/json' },
  })
}

export function getPropertyStatistics(companyId) {
  return request({
    url: '/monitor/v1/supervision/property/right/statistics',
    method: 'get',
    params: { companyId },
  })
}

// ========== 产权交易 ==========
export function getPropertyTransactionList(data) {
  return request({
    url: '/monitor/v1/supervision/property/transaction/list',
    method: 'post',
    data: transData(data),
  })
}

export function getPropertyTransactionDetail(id) {
  return request({
    url: '/monitor/v1/supervision/property/transaction/' + id,
    method: 'get',
  })
}

export function addPropertyTransaction(data) {
  return request({
    url: '/monitor/v1/supervision/property/transaction/save',
    method: 'post',
    data: transData(data),
  })
}

export function updatePropertyTransaction(data) {
  return request({
    url: '/monitor/v1/supervision/property/transaction/save',
    method: 'post',
    data: transData(data),
  })
}

export function deletePropertyTransaction(id) {
  return request({
    url: '/monitor/v1/supervision/property/transaction/' + id,
    method: 'delete',
  })
}

