import request from '@/utils/request'

/**
 * 预算数据汇总API
 */

// 执行汇总
export function executeSummary(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetSummary/executeSummary',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 查询汇总记录列表(分页)
export function getSummaryList(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetSummary/getSummaryList',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 根据ID查询汇总记录
export function getSummaryById(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetSummary/getSummaryById',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 根据条件查询汇总记录
export function getSummaryByCondition(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetSummary/getSummaryByCondition',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 删除汇总记录
export function deleteSummary(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetSummary/deleteSummary',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 批量删除汇总记录
export function batchDeleteSummary(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetSummary/batchDeleteSummary',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 重新执行汇总
export function reExecuteSummary(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetSummary/reExecuteSummary',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 查询汇总统计信息
export function getSummaryStatistics() {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetSummary/getSummaryStatistics',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

// 导出汇总数据(返回 xlsx 二进制流, 由调用方自行处理下载)
export function exportSummary(data) {
  return request({
    url: '/cwgxAi/budgetPlanning/budgetSummary/exportSummary',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    responseType: 'blob',
    data
  })
}

