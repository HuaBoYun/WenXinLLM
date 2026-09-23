import request from '@/utils/request'

/**
 * 财资公共模块 - 基础配置 API
 * 包含：印鉴档案管理、印鉴组合配置、印鉴类型管理、印鉴使用记录、安全参数配置、第三方账户管理、UKey厂商管理
 */

// ==================== 印鉴档案管理 ====================
export function getSealArchivePage(params) {
  return request({
    url: '/qqsk/financial/basicConfig/sealArchive/list',
    method: 'post',
    data: params,
    headers: { 'Content-Type': 'application/json' }
  })
}

export function getSealArchiveDetail(id) {
  return request({ url: '/qqsk/financial/basicConfig/sealArchive/detail', method: 'get', params: { id } })
}

export function addSealArchive(params) {
  return request({
    url: '/qqsk/financial/basicConfig/sealArchive/add',
    method: 'post',
    data: params,
    headers: { 'Content-Type': 'application/json' }
  })
}

export function updateSealArchive(params) {
  return request({
    url: '/qqsk/financial/basicConfig/sealArchive/update',
    method: 'post',
    data: params,
    headers: { 'Content-Type': 'application/json' }
  })
}

export function deleteSealArchive(id) {
  return request({
    url: '/qqsk/financial/basicConfig/sealArchive/delete',
    method: 'post',
    data: { id },
    headers: { 'Content-Type': 'application/json' }
  })
}

export function getSealArchiveStatistics() {
  return request({ url: '/qqsk/financial/basicConfig/sealArchive/statistics', method: 'get' })
}

export function exportSealArchive(params) {
  return request({
    url: '/qqsk/financial/basicConfig/sealArchive/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

// ==================== 印鉴组合配置 ====================
export function getSealCombinationPage(params) {
  return request({
    url: '/qqsk/financial/basicConfig/sealCombination/list',
    method: 'post',
    data: params,
    headers: { 'Content-Type': 'application/json' }
  })
}

export function getSealCombinationDetail(id) {
  return request({ url: '/qqsk/financial/basicConfig/sealCombination/detail', method: 'get', params: { id } })
}

export function addSealCombination(params) {
  return request({
    url: '/qqsk/financial/basicConfig/sealCombination/add',
    method: 'post',
    data: params,
    headers: { 'Content-Type': 'application/json' }
  })
}

export function updateSealCombination(params) {
  return request({
    url: '/qqsk/financial/basicConfig/sealCombination/update',
    method: 'post',
    data: params,
    headers: { 'Content-Type': 'application/json' }
  })
}

export function deleteSealCombination(id) {
  return request({
    url: '/qqsk/financial/basicConfig/sealCombination/delete',
    method: 'post',
    data: { id },
    headers: { 'Content-Type': 'application/json' }
  })
}

export function exportSealCombination(params) {
  return request({
    url: '/qqsk/financial/basicConfig/sealCombination/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

// ==================== 印鉴类型管理 ====================
export function getSealTypePage(params) {
  return request({
    url: '/qqsk/financial/basicConfig/sealType/list',
    method: 'post',
    data: params,
    headers: { 'Content-Type': 'application/json' }
  })
}

export function getSealTypeDetail(id) {
  return request({ url: '/qqsk/financial/basicConfig/sealType/detail', method: 'get', params: { id } })
}

export function addSealType(params) {
  return request({
    url: '/qqsk/financial/basicConfig/sealType/add',
    method: 'post',
    data: params,
    headers: { 'Content-Type': 'application/json' }
  })
}

export function updateSealType(params) {
  return request({
    url: '/qqsk/financial/basicConfig/sealType/update',
    method: 'post',
    data: params,
    headers: { 'Content-Type': 'application/json' }
  })
}

export function deleteSealType(id) {
  return request({
    url: '/qqsk/financial/basicConfig/sealType/delete',
    method: 'post',
    data: { id },
    headers: { 'Content-Type': 'application/json' }
  })
}

export function updateSealTypeStatus(id, status) {
  return request({
    url: '/qqsk/financial/basicConfig/sealType/updateStatus',
    method: 'post',
    data: { id, status },
    headers: { 'Content-Type': 'application/json' }
  })
}

export function exportSealType(params) {
  return request({
    url: '/qqsk/financial/basicConfig/sealType/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

// ==================== 印鉴使用记录 ====================
export function getSealUsageRecordPage(params) {
  return request({
    url: '/qqsk/financial/basicConfig/sealUsageRecord/list',
    method: 'post',
    data: params,
    headers: { 'Content-Type': 'application/json' }
  })
}

export function getSealUsageRecordDetail(id) {
  return request({ url: '/qqsk/financial/basicConfig/sealUsageRecord/detail', method: 'get', params: { id } })
}

export function getSealUsageRecordStatistics() {
  return request({ url: '/qqsk/financial/basicConfig/sealUsageRecord/statistics', method: 'get' })
}

export function exportSealUsageRecord(params) {
  return request({
    url: '/qqsk/financial/basicConfig/sealUsageRecord/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

