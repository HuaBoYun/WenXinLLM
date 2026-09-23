import request from '@/utils/request'

/**
 * 授信合同管理API
 */

// 授信合同分页查询
export function getCreditContractPage(data) {
  return request({
    url: '/qqsk/financing/credit-contract/page',
    method: 'post',
    data
  })
}

// 授信合同详情查询
export function getCreditContractDetail(id) {
  return request({
    url: `/qqsk/financing/credit-contract/${id}`,
    method: 'get'
  })
}

// 新增授信合同
export function addCreditContract(data) {
  return request({
    url: '/qqsk/financing/credit-contract/add',
    method: 'post',
    data
  })
}

// 更新授信合同
export function updateCreditContract(data) {
  return request({
    url: '/qqsk/financing/credit-contract/update',
    method: 'put',
    data
  })
}

// 删除授信合同
export function deleteCreditContract(id) {
  return request({
    url: `/qqsk/financing/credit-contract/${id}`,
    method: 'delete'
  })
}

// 签署授信合同
export function signCreditContract(id, signDate, userId) {
  return request({
    url: `/qqsk/financing/credit-contract/sign/${id}`,
    method: 'post',
    params: { signDate, userId }
  })
}

// 终止授信合同
export function terminateCreditContract(id, terminateDate, reason, userId) {
  return request({
    url: `/qqsk/financing/credit-contract/terminate/${id}`,
    method: 'post',
    params: { terminateDate, reason, userId }
  })
}

// 导出授信合同
export function exportCreditContract(data) {
  return request({
    url: '/qqsk/financing/credit-contract/export',
    method: 'post',
    data,
    responseType: 'blob'
  })
}

