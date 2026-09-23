import request from '@/utils/request'

/**
 * 授信申请管理API
 */

// 授信申请分页查询
export function getCreditApplicationPage(data) {
  return request({
    url: '/qqsk/financing/credit-application/page',
    method: 'post',
    data
  })
}

// 授信申请详情查询
export function getCreditApplicationDetail(id) {
  return request({
    url: `/qqsk/financing/credit-application/${id}`,
    method: 'get'
  })
}

// 新增授信申请
export function addCreditApplication(data) {
  return request({
    url: '/qqsk/financing/credit-application/add',
    method: 'post',
    data
  })
}

// 更新授信申请
export function updateCreditApplication(data) {
  return request({
    url: '/qqsk/financing/credit-application/update',
    method: 'put',
    data
  })
}

// 删除授信申请
export function deleteCreditApplication(id) {
  return request({
    url: `/qqsk/financing/credit-application/${id}`,
    method: 'delete'
  })
}

// 提交授信申请
export function submitCreditApplication(id, userId) {
  return request({
    url: `/qqsk/financing/credit-application/submit/${id}`,
    method: 'post',
    params: { userId }
  })
}

// 审批授信申请
export function approveCreditApplication(id, userId, approvalOpinion) {
  return request({
    url: '/qqsk/financing/credit-application/approve',
    method: 'post',
    params: { id, userId, approvalOpinion }
  })
}

// 拒绝授信申请
export function rejectCreditApplication(id, userId, rejectReason) {
  return request({
    url: '/qqsk/financing/credit-application/reject',
    method: 'post',
    params: { id, userId, rejectReason }
  })
}

