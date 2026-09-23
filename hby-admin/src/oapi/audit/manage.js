import request from '@/utils/request'
import { transData } from '@/utils/requestData'

export function findAuditProjectOperationStatusAllList(params) {
  return request({
    url: '/oiaudit/project/weerkly/findAuditProjectOperationStatusAllList',
    method: 'get',
    params: transData(params),
  })
}
export function selectWtzgAuditResultsStatistics(params) {
  return request({
    url: '/oiaudit/wtzg/selectWtzgAuditResultsStatistics',
    method: 'get',
    params: transData(params),
  })
}
export function selectTblYqnsGcxmzjJsdwStatisticalToOne(params) {
  return request({
    url: '/oiaudit/gcxmzj/statistical/selectTblYqnsGcxmzjJsdwStatisticalToOne',
    method: 'post',
    data: transData(params),
  })
}