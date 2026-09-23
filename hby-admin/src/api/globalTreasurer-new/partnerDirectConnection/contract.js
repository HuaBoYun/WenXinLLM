import request from '@/utils/request'

/**
 * 财资公共模块 - 云连接合同管理 API
 */

// ==================== 云连接合同管理 ====================
export function getCloudConnectionContractPage(params) {
  return request({ url: '/qqsk/settlement/cloud-connection-contract/page', method: 'post', data: params })
}

export function getCloudConnectionContractDetail(id) {
  return request({ url: `/qqsk/settlement/cloud-connection-contract/${id}`, method: 'get' })
}

export function addCloudConnectionContract(params) {
  return request({ url: '/qqsk/settlement/cloud-connection-contract', method: 'post', data: params })
}

export function updateCloudConnectionContract(params) {
  return request({ url: '/qqsk/settlement/cloud-connection-contract', method: 'put', data: params })
}

export function deleteCloudConnectionContract(id) {
  return request({ url: `/qqsk/settlement/cloud-connection-contract/${id}`, method: 'delete' })
}

export function batchDeleteCloudConnectionContract(ids) {
  return request({ url: '/qqsk/settlement/cloud-connection-contract/batch', method: 'delete', data: { ids } })
}

export function getCloudConnectionContractStatistics() {
  return request({ url: '/qqsk/settlement/cloud-connection-contract/statistics', method: 'get' })
}

export function getCloudConnectionContractRenewalAlert() {
  return request({ url: '/qqsk/settlement/cloud-connection-contract/renewal-alert', method: 'get' })
}

export function exportCloudConnectionContract(params) {
  return request({ url: '/qqsk/settlement/cloud-connection-contract/export', method: 'post', data: params })
}

