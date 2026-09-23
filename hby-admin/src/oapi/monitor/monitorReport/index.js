import request from '@/utils/request'
import { transData } from '@/utils/requestData'

//审计报告编制列表
export function report(params) {
  return request({
    url: '/audit/auditReport/manage/report',
    method: 'get',
    params: transData(params),
  })
}

//审计报告编制-新增与修改
export function reportAdd(params) {
  return request({
    url: '/audit/auditReport/manage/report_add',
    method: 'post',
    data: transData(params),
  })
}

//审计报告编制-删除
export function reportDel(params) {
  return request({
    url: '/audit/auditReport/manage/report_del',
    method: 'get',
    params: transData(params),
  })
}

//审计报告编制-明细
export function reportDetail(params) {
  return request({
    url: '/audit/auditReport/manage/report_detail',
    method: 'get',
    params: transData(params),
  })
}

//报告编制-导出
export function reportExport(params) {
  return request({
    url: '/audit/auditReport/manage/report_export',
    method: 'get',
    params: transData(params),
  })
}

//审计报告编制-附件列表
export function reportFileList(params) {
  return request({
    url: '/audit/auditReport/manage/report_file_list',
    method: 'get',
    params: transData(params),
  })
}

//自定义报告-新增与修改
export function zdyAdd(params) {
  return request({
    url: '/audit/auditReport/nkbg/zdy_add',
    method: 'post',
    data: transData(params),
  })
}

//自定义报告-删除
export function zdyDel(params) {
  return request({
    url: '/audit/auditReport/nkbg/zdy_del',
    method: 'get',
    params: transData(params),
  })
}

//自定义报告-明细
export function zdyDetail(params) {
  return request({
    url: '/audit/auditReport/nkbg/zdy_detail',
    method: 'get',
    params: transData(params),
  })
}

//自定义报告列表
export function zdyList(params) {
  return request({
    url: '/audit/auditReport/nkbg/zdy_list',
    method: 'get',
    params: transData(params),
  })
}

//审计建议书-新增与修改
export function auditSuggestAdd(params) {
  return request({
    url: '/audit/auditReport/sjzj/audit_suggest_add',
    method: 'post',
    data: transData(params),
  })
}

//审计建议书-作废
export function auditSuggestCancel(params) {
  return request({
    url: '/audit/auditReport/sjzj/audit_suggest_cancel',
    method: 'get',
    params: transData(params),
  })
}

//审计建议书-明细
export function auditSuggestDetail(params) {
  return request({
    url: '/audit/auditReport/sjzj/audit_suggest_detail',
    method: 'get',
    params: transData(params),
  })
}

//审计建议书列表
export function auditSuggestList(params) {
  return request({
    url: '/audit/auditReport/sjzj/audit_suggest_list',
    method: 'get',
    params: transData(params),
  })
}

//审计报告复核
export function auditFH(params) {
  return request({
    url: '/audit/nbsjapproval/submitReportFhApproval',
    method: 'post',
    data: transData(params),
  })
}
//审计报告审批
export function auditSP(params) {
  return request({
    url: '/audit/nbsjapproval/submitReportSpApproval',
    method: 'post',
    data: transData(params),
  })
}
//审计报告意见征集
export function auditYJZJ(params) {
  return request({
    url: '/audit/nbsjapproval/submitReportZqyjApproval',
    method: 'post',
    data: transData(params),
  })
}

//建议书删除
export function suggestDel(params) {
  return request({
    url: '/audit/auditReport/sjzj/audit_suggest_del',
    method: 'get',
    params: transData(params),
  })
}

//建议书附件删除
export function deleteSuggestFile(params) {
  return request({
    url: '/audit/auditReport/sjzj/audit_suggest_file_del',
    method: 'get',
    params: transData(params),
  })
}
