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
 * 根据模型ID和期间查询报表类型列表
 */
export function getReportTypeList(data) {
  return request({
    url: '/cwgxAi/consolidationReport/consolidatedReport/getReportTypeList',
    method: 'post',
    data
  })
}

/**
 * 导出合并报表到Excel
 */
export function exportReport(data) {
  return request({
    url: '/cwgxAi/consolidationReport/consolidatedReport/exportReport',
    method: 'post',
    data,
    responseType: 'blob'
  })
}

/**
 * 对比两个期间的合并报表
 */
export function compareReports(data) {
  return request({
    url: '/cwgxAi/consolidationReport/consolidatedReport/compareReports',
    method: 'post',
    data
  })
}

