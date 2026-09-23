import request from '@/utils/request'

/**
 * 财资公共模块 - 基础配置 API
 * 包含：安全参数配置、第三方账户管理、UKey厂商管理
 */

// ==================== 安全参数配置 ====================
export function getSecurityParamPage(params) {
  return request({
    url: '/qqsk/financial/basicConfig/securityParam/list',
    method: 'post',
    data: params,
    headers: { 'Content-Type': 'application/json' }
  })
}

export function getSecurityParamDetail(id) {
  return request({ url: '/qqsk/financial/basicConfig/securityParam/detail', method: 'get', params: { id } })
}

export function saveOrUpdateSecurityParam(params) {
  return request({
    url: '/qqsk/financial/basicConfig/securityParam/saveOrUpdate',
    method: 'post',
    data: params,
    headers: { 'Content-Type': 'application/json' }
  })
}

export function deleteSecurityParam(id) {
  return request({
    url: '/qqsk/financial/basicConfig/securityParam/delete',
    method: 'post',
    data: { id },
    headers: { 'Content-Type': 'application/json' }
  })
}

export function updateSecurityParamStatus(id, status) {
  return request({
    url: '/qqsk/financial/basicConfig/securityParam/updateStatus',
    method: 'post',
    data: { id, status },
    headers: { 'Content-Type': 'application/json' }
  })
}

export function exportSecurityParam(params) {
  return request({
    url: '/qqsk/financial/basicConfig/securityParam/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

// ==================== 第三方账户管理 ====================
export function getThirdPartyAccountPage(params) {
  return request({
    url: '/qqsk/financial/basicConfig/thirdPartyAccount/list',
    method: 'get',
    params
  })
}

export function getThirdPartyAccountDetail(id) {
  return request({ url: '/qqsk/financial/basicConfig/thirdPartyAccount/detail', method: 'get', params: { id } })
}

export function addThirdPartyAccount(params) {
  return request({
    url: '/qqsk/financial/basicConfig/thirdPartyAccount/add',
    method: 'post',
    data: params,
    headers: { 'Content-Type': 'application/json' }
  })
}

export function updateThirdPartyAccount(params) {
  return request({
    url: '/qqsk/financial/basicConfig/thirdPartyAccount/update',
    method: 'post',
    data: params,
    headers: { 'Content-Type': 'application/json' }
  })
}

export function deleteThirdPartyAccount(id) {
  return request({
    url: '/qqsk/financial/basicConfig/thirdPartyAccount/delete',
    method: 'post',
    data: { id },
    headers: { 'Content-Type': 'application/json' }
  })
}

export function testThirdPartyAccountConnection(id) {
  return request({
    url: '/qqsk/financial/basicConfig/thirdPartyAccount/testConnection',
    method: 'post',
    data: { id },
    headers: { 'Content-Type': 'application/json' }
  })
}

export function syncThirdPartyAccount(id) {
  return request({
    url: '/qqsk/financial/basicConfig/thirdPartyAccount/sync',
    method: 'post',
    data: { id },
    headers: { 'Content-Type': 'application/json' }
  })
}

export function batchSyncThirdPartyAccount(ids) {
  return request({
    url: '/qqsk/financial/basicConfig/thirdPartyAccount/batchSync',
    method: 'post',
    data: { ids },
    headers: { 'Content-Type': 'application/json' }
  })
}

export function exportThirdPartyAccount(params) {
  return request({
    url: '/qqsk/financial/basicConfig/thirdPartyAccount/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

// ==================== UKey厂商管理 ====================
export function getUkeyVendorPage(params) {
  return request({
    url: '/qqsk/financial/basicConfig/ukeyVendor/list',
    method: 'get',
    params
  })
}

export function getUkeyVendorDetail(id) {
  return request({ url: '/qqsk/financial/basicConfig/ukeyVendor/detail', method: 'get', params: { id } })
}

export function addUkeyVendor(params) {
  return request({
    url: '/qqsk/financial/basicConfig/ukeyVendor/add',
    method: 'post',
    data: params,
    headers: { 'Content-Type': 'application/json' }
  })
}

export function updateUkeyVendor(params) {
  return request({
    url: '/qqsk/financial/basicConfig/ukeyVendor/update',
    method: 'post',
    data: params,
    headers: { 'Content-Type': 'application/json' }
  })
}

export function deleteUkeyVendor(id) {
  return request({
    url: '/qqsk/financial/basicConfig/ukeyVendor/delete',
    method: 'post',
    data: { id },
    headers: { 'Content-Type': 'application/json' }
  })
}

export function getUkeyVendorStatistics() {
  return request({ url: '/qqsk/financial/basicConfig/ukeyVendor/statistics', method: 'get' })
}

export function certifyUkeyVendor(id) {
  return request({
    url: '/qqsk/financial/basicConfig/ukeyVendor/certify',
    method: 'post',
    data: { id },
    headers: { 'Content-Type': 'application/json' }
  })
}

export function batchCertifyUkeyVendor(ids) {
  return request({
    url: '/qqsk/financial/basicConfig/ukeyVendor/batchCertify',
    method: 'post',
    data: { ids },
    headers: { 'Content-Type': 'application/json' }
  })
}

export function exportUkeyVendor(params) {
  return request({
    url: '/qqsk/financial/basicConfig/ukeyVendor/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

