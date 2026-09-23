import request from '@/utils/request'

/**
 * 查询对账数据列表
 */
export function getReconciliationList(data) {
  return request({
    url: '/cwgxAi/consolidationReport/reconciliationData/getReconciliationList',
    method: 'post',
    data
  })
}

/**
 * 根据ID查询对账数据
 */
export function getReconciliationById(data) {
  return request({
    url: '/cwgxAi/consolidationReport/reconciliationData/getReconciliationById',
    method: 'post',
    data
  })
}

/**
 * 新增对账数据
 */
export function saveReconciliation(data) {
  return request({
    url: '/cwgxAi/consolidationReport/reconciliationData/saveReconciliation',
    method: 'post',
    data
  })
}

/**
 * 修改对账数据
 */
export function updateReconciliation(data) {
  return request({
    url: '/cwgxAi/consolidationReport/reconciliationData/updateReconciliation',
    method: 'post',
    data
  })
}

/**
 * 删除对账数据
 */
export function deleteReconciliation(data) {
  return request({
    url: '/cwgxAi/consolidationReport/reconciliationData/deleteReconciliation',
    method: 'post',
    data
  })
}

/**
 * 批量删除对账数据
 */
export function deleteByModelIdAndPeriod(data) {
  return request({
    url: '/cwgxAi/consolidationReport/reconciliationData/deleteByModelIdAndPeriod',
    method: 'post',
    data
  })
}

/**
 * 批量导入对账数据
 */
export function batchImport(data) {
  return request({
    url: '/cwgxAi/consolidationReport/reconciliationData/batchImport',
    method: 'post',
    data
  })
}

/**
 * 统计对账数据
 */
export function getStatistics(data) {
  return request({
    url: '/cwgxAi/consolidationReport/reconciliationData/getStatistics',
    method: 'post',
    data
  })
}

