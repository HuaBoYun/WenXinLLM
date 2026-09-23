import request from '@/utils/request'
import { transData } from '@/utils/requestData'

//项目情况分析-列表查询
export function projectStatusList(params) {
  return request({
    url: '/audit/nbsjworkSpace/getGkProjectInfo',
    method: 'post',
    data: transData(params),
  })
}
//审计问题分析-列表查询
export function auditQuestionList(params) {
  return request({
    url: '/audit/nbsjworkSpace/getGkQuestionInfo',
    method: 'post',
    data: transData(params),
  })
}
//整改问题分析-列表查询
export function rectificationQuestionList(params) {
  return request({
    url: '/audit/nbsjworkSpace/getGkZgContentInfo',
    method: 'post',
    data: transData(params),
  })
}
export function exportProject(params) {
  return request({
    url: '/audit/nbsjworkSpace/getGkProjectInfoExport',
    method: 'post',
    data: transData(params),
    responseType: 'blob',
  })
}
export function exportAudit(params) {
  return request({
    url: '/audit/nbsjworkSpace/getGkQuestionInfoExport',
    method: 'post',
    data: transData(params),
    responseType: 'blob',
  })
}
export function exportCorrective(params) {
  return request({
    url: '/audit/nbsjworkSpace/getGkZgContentInfoExport',
    method: 'post',
    data: transData(params),
    responseType: 'blob',
  })
}
export function getAnalyseDetailData(params) {
  return request({
    url: '/audit/nbsjworkSpace/getSheetsl',
    method: 'get',
    params: transData(params),
  })
}
