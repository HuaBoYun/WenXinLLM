import request from '@/utils/request'

/**
 * 查询合并报表列表(分页)
 */
export function getReportList(data) {
  return request({
    url: '/cwgxAi/consolidationReport/consolidatedReport/getReportList',
    method: 'post',
    data
  })
}

/**
 * 查询合并报表列表(不分页)
 */
export function getReportListNoPage(data) {
  return request({
    url: '/cwgxAi/consolidationReport/consolidatedReport/getReportListNoPage',
    method: 'post',
    data
  })
}

/**
 * 根据ID查询合并报表
 */
export function getReportById(data) {
  return request({
    url: '/cwgxAi/consolidationReport/consolidatedReport/getReportById',
    method: 'post',
    data
  })
}

/**
 * 生成合并报表
 */
export function generateReport(data) {
  return request({
    url: '/cwgxAi/consolidationReport/consolidatedReport/generateReport',
    method: 'post',
    data
  })
}

/**
 * 删除合并报表
 */
export function deleteReport(data) {
  return request({
    url: '/cwgxAi/consolidationReport/consolidatedReport/deleteReport',
    method: 'post',
    data
  })
}

/**
 * 确认合并报表
 */
export function confirmReport(data) {
  return request({
    url: '/cwgxAi/consolidationReport/consolidatedReport/confirmReport',
    method: 'post',
    data
  })
}

/**
 * 根据模型ID和期间查询报表类型列表
 */
export function getReportTypeList(data) {
  return request({
    url: '/cwgxAi/consolidationReport/consolidatedReport/getReportTypeList',
    method: 'post',
    data
  })
}

