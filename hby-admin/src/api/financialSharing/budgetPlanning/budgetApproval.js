import request from '@/utils/request'

/**
 * 预算数据审批API
 */

// 提交审批
export function submitApproval(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetApproval/submitApproval',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 审批通过
export function approveData(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetApproval/approveData',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 审批驳回
export function rejectData(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetApproval/rejectData',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 撤销审批
export function cancelApproval(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetApproval/cancelApproval',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 查询审批记录列表(分页)
export function getApprovalList(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetApproval/getApprovalList',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 查询审批历史
export function getApprovalHistory(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetApproval/getApprovalHistory',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 查询我的待审批列表(分页)
export function getMyPendingList(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetApproval/getMyPendingList',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 查询我的已审批列表(分页)
export function getMyApprovedList(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetApproval/getMyApprovedList',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 批量审批通过
export function batchApprove(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetApproval/batchApprove',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 批量审批驳回
export function batchReject(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetApproval/batchReject',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 查询审批统计信息
export function getApprovalStatistics() {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetApproval/getApprovalStatistics',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

