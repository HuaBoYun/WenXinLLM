import request from '@/utils/request'
import { transData } from '@/utils/requestData'
// import { get } from 'lodash'

//进场纪要-列表查询
export function getCurrentProjectData(params) {
  return request({
    url: '/audit/auditProject/xmgl/curr_ss_project',
    method: 'get',
    params: transData(params),
  })
}
//审计业务情况表
export function getSJYWQKBInfo(params) {
  return request({
    url: '/audit/nbsjworkSpace/sjgk/sjs_dgwt_list',
    method: 'get',
    params: transData(params),
  })
}

//审计业务情况表导出
export function exploreSJYWQKB(params) {
  return request({
    url: '/audit/nbsjworkSpace/exploredSjsDgwt',
    method: 'get',
    responseType: 'blob',
    data: transData(params),
  })
}
