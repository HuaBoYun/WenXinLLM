import request from '@/utils/request'

/**
 * 余额管理 API
 */

// 刷新科目余额
export function refreshBalance(data) {
  return request({
    url: '/cwgxAi/balance/refresh',
    method: 'post',
    data: data
  })
}

// 批量刷新科目余额
export function batchRefreshBalance(data) {
  return request({
    url: '/cwgxAi/balance/batchRefresh',
    method: 'post',
    data: data
  })
}

// 查询刷新进度
export function getRefreshProgress(taskId) {
  return request({
    url: `/cwgxAi/balance/refreshProgress/${taskId}`,
    method: 'get'
  })
}

// 分页查询余额列表
export function getBalanceList(params) {
  return request({
    url: '/cwgxAi/balance/getList',
    method: 'post',
    data: params
  })
}

// 创建余额调整申请
export function createBalanceAdjustment(data) {
  return request({
    url: '/cwgxAi/balance/adjustment',
    method: 'post',
    data: data
  })
}

// 查询余额调整申请列表
export function getBalanceAdjustmentList(params) {
  return request({
    url: '/cwgxAi/balance/adjustment/getList',
    method: 'post',
    data: params
  })
}

// 根据ID查询余额调整详情
export function getBalanceAdjustmentDetail(adjustmentId) {
  return request({
    url: `/cwgxAi/balance/adjustment/detail/${adjustmentId}`,
    method: 'get'
  })
}

// 审批余额调整
export function approveBalanceAdjustment(adjustmentId, action, comment) {
  return request({
    url: `/cwgxAi/balance/adjustment/approve/${adjustmentId}`,
    method: 'put',
    params: { action, comment }
  })
}

// 批量审批余额调整
export function batchApproveBalanceAdjustment(adjustmentIds, action, comment) {
  return request({
    url: '/cwgxAi/balance/adjustment/batchApprove',
    method: 'put',
    data: adjustmentIds,
    params: { action, comment }
  })
}

// 撤销余额调整申请
export function cancelBalanceAdjustment(adjustmentId, reason) {
  return request({
    url: `/cwgxAi/balance/adjustment/cancel/${adjustmentId}`,
    method: 'put',
    params: { reason }
  })
}

// 导出余额数据
export function exportBalanceData(params) {
  return request({
    url: '/cwgxAi/balance/export',
    method: 'post',
    data: params
  })
}

// 余额数据校验
export function validateBalanceData(params) {
  return request({
    url: '/cwgxAi/balance/validate',
    method: 'post',
    data: params
  })
}

// 获取余额调整类型列表
export function getBalanceAdjustmentTypes() {
  return request({
    url: '/cwgxAi/balance/adjustment/types',
    method: 'get'
  })
}

// 获取科目余额汇总信息
export function getBalanceSummary(params) {
  return request({
    url: '/cwgxAi/balance/summary',
    method: 'post',
    data: params
  })
}