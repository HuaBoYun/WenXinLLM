import request from '@/utils/request'

/**
 * 预算调整管理API
 */

// 创建调整单
export function createAdjustment(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetAdjustment/createAdjustment',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 修改调整单
export function updateAdjustment(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetAdjustment/updateAdjustment',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 查询调整单列表(分页)
export function getAdjustmentList(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetAdjustment/getAdjustmentList',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 根据ID查询调整单
export function getAdjustmentById(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetAdjustment/getAdjustmentById',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 根据ID查询调整单(包含明细)
export function getAdjustmentWithDetails(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetAdjustment/getAdjustmentWithDetails',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 删除调整单
export function deleteAdjustment(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetAdjustment/deleteAdjustment',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 批量删除调整单
export function batchDeleteAdjustment(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetAdjustment/batchDeleteAdjustment',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 提交调整单
export function submitAdjustment(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetAdjustment/submitAdjustment',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 审批调整单
export function approveAdjustment(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetAdjustment/approveAdjustment',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 执行调整
export function executeAdjustment(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetAdjustment/executeAdjustment',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 撤销调整单
export function withdrawAdjustment(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetAdjustment/withdrawAdjustment',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 查询调整单统计信息
export function getAdjustmentStatistics() {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetAdjustment/getAdjustmentStatistics',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

