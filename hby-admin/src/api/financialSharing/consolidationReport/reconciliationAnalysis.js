import request from '@/utils/request'

/**
 * 查询差异分析列表
 */
export function getAnalysisList(data) {
  return request({
    url: '/cwgxAi/consolidationReport/reconciliationAnalysis/getAnalysisList',
    method: 'post',
    data
  })
}

/**
 * 根据ID查询差异分析
 */
export function getAnalysisById(data) {
  return request({
    url: '/cwgxAi/consolidationReport/reconciliationAnalysis/getAnalysisById',
    method: 'post',
    data
  })
}

/**
 * 根据对账数据ID查询差异分析
 */
export function getAnalysisByReconciliationId(data) {
  return request({
    url: '/cwgxAi/consolidationReport/reconciliationAnalysis/getAnalysisByReconciliationId',
    method: 'post',
    data
  })
}

/**
 * 新增差异分析
 */
export function saveAnalysis(data) {
  return request({
    url: '/cwgxAi/consolidationReport/reconciliationAnalysis/saveAnalysis',
    method: 'post',
    data
  })
}

/**
 * 修改差异分析
 */
export function updateAnalysis(data) {
  return request({
    url: '/cwgxAi/consolidationReport/reconciliationAnalysis/updateAnalysis',
    method: 'post',
    data
  })
}

/**
 * 删除差异分析
 */
export function deleteAnalysis(data) {
  return request({
    url: '/cwgxAi/consolidationReport/reconciliationAnalysis/deleteAnalysis',
    method: 'post',
    data
  })
}

/**
 * 处理差异
 */
export function handleDifference(data) {
  return request({
    url: '/cwgxAi/consolidationReport/reconciliationAnalysis/handleDifference',
    method: 'post',
    data
  })
}

/**
 * 关闭差异
 */
export function closeDifference(data) {
  return request({
    url: '/cwgxAi/consolidationReport/reconciliationAnalysis/closeDifference',
    method: 'post',
    data
  })
}

/**
 * 统计差异分析
 */
export function getStatistics(data) {
  return request({
    url: '/cwgxAi/consolidationReport/reconciliationAnalysis/getStatistics',
    method: 'post',
    data
  })
}

/**
 * 自动创建差异分析
 */
export function autoCreateAnalysis(data) {
  return request({
    url: '/cwgxAi/consolidationReport/reconciliationAnalysis/autoCreateAnalysis',
    method: 'post',
    data
  })
}

