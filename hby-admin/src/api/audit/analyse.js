import request from '@/utils/request'
import { transData } from '@/utils/requestData'

//项目情况分析-列表查询
export function projectStatusList (params) {
  return request({
    url: '/audit/nbsjworkSpace/getGkProjectInfo',
    method: 'post',
    data: transData(params),
  })
}
//审计问题分析-列表查询
export function auditQuestionList (params) {
  return request({
    url: '/audit/nbsjworkSpace/getGkQuestionInfo',
    method: 'post',
    data: transData(params),
  })
}
//整改问题分析-列表查询
export function rectificationQuestionList (params) {
  return request({
    url: '/audit/nbsjworkSpace/getGkZgContentInfo',
    method: 'post',
    data: transData(params),
  })
}
export function exportProject (params) {
  return request({
    url: '/audit/nbsjworkSpace/getGkProjectInfoExport',
    method: 'post',
    data: transData(params),
    responseType: 'blob',
  })
}
export function exportAudit (params) {
  return request({
    url: '/audit/nbsjworkSpace/getGkQuestionInfoExport',
    method: 'post',
    data: transData(params),
    responseType: 'blob',
  })
}
export function exportCorrective (params) {
  return request({
    url: '/audit/nbsjworkSpace/getGkZgContentInfoExport',
    method: 'post',
    data: transData(params),
    responseType: 'blob',
  })
}
export function getAnalyseDetailData (params) {
  return request({
    url: '/audit/nbsjworkSpace/getSheetsl',
    method: 'get',
    params: transData(params),
  })
}

//审计情况统计表-列表
export function auditStatisticsGetPage (params) {
  return request({
    url: '/audit/auditStatistics/getPage',
    method: 'get',
    params: transData(params),
  })
}
//审计情况统计表-导出
export function auditStatisticsExport (params) {
  return request({
    url: '/audit/auditStatistics/export',
    method: 'get',
    params: transData(params),
    responseType: 'blob',
  })
}
//审计情况统计表-详情
export function auditStatisticsGetDetail (params) {
  return request({
    url: '/audit/auditStatistics/getDetail',
    method: 'get',
    params: transData(params),
  })
}
//审计情况统计表-删除
export function auditStatisticsRemove (params) {
  return request({
    url: '/audit/auditStatistics/remove',
    method: 'get',
    params: transData(params),
  })
}
//审计情况统计表-新增或修改
export function auditStatisticsMergeInfo (params) {
  return request({
    url: '/audit/auditStatistics/mergeInfo',
    method: 'post',
    params: transData(params),
  })
}


//审计人员情况报表-列表
export function auditPersonnelInfoGetPage (params) {
  return request({
    url: '/audit/auditPersonnelInfo/getPage',
    method: 'get',
    params: transData(params),
  })
}
//审计人员情况报表-导出
export function auditPersonnelInfoExport (params) {
  return request({
    url: '/audit/auditPersonnelInfo/export',
    method: 'get',
    params: transData(params),
    responseType: 'blob',
  })
}
//审计人员情况报表-详情
export function auditPersonnelInfoGetDetail (params) {
  return request({
    url: '/audit/auditPersonnelInfo/getDetail',
    method: 'get',
    params: transData(params),
  })
}
//审计人员情况报表-删除
export function auditPersonnelInfoRemove (params) {
  return request({
    url: '/audit/auditPersonnelInfo/remove',
    method: 'get',
    params: transData(params),
  })
}
//审计人员情况报表-新增或修改
export function auditPersonnelInfoMergeInfo (params) {
  return request({
    url: '/audit/auditPersonnelInfo/mergeInfo',
    method: 'post',
    params: transData(params),
  })
}


//审计计划执行及要点完成情况表-列表
export function auditPlanExecutionStatisticsGetPage (params) {
  return request({
    url: '/audit/auditPlanExecutionStatistics/getPage',
    method: 'get',
    params: transData(params),
  })
}
//审计计划执行及要点完成情况表-导出
export function auditPlanExecutionStatisticsExport (params) {
  return request({
    url: '/audit/auditPlanExecutionStatistics/export',
    method: 'get',
    params: transData(params),
    responseType: 'blob',
  })
}
//审计计划执行及要点完成情况表-详情
export function auditPlanExecutionStatisticsGetDetail (params) {
  return request({
    url: '/audit/auditPlanExecutionStatistics/getDetail',
    method: 'get',
    params: transData(params),
  })
}
//审计计划执行及要点完成情况表-删除
export function auditPlanExecutionStatisticsRemove (params) {
  return request({
    url: '/audit/auditPlanExecutionStatistics/remove',
    method: 'get',
    params: transData(params),
  })
}
//审计计划执行及要点完成情况表-新增或修改
export function auditPlanExecutionStatisticsMergeInfo (params) {
  return request({
    url: '/audit/auditPlanExecutionStatistics/mergeInfo',
    method: 'post',
    params: transData(params),
  })
}



//中介机构评价表-列表
export function thirdPartyEvaluationGetPage (params) {
  return request({
    url: '/audit/thirdPartyEvaluation/getPage',
    method: 'get',
    params: transData(params),
  })
}
//中介机构评价表-导出
export function thirdPartyEvaluationExport (params) {
  return request({
    url: '/audit/thirdPartyEvaluation/export',
    method: 'get',
    params: transData(params),
    responseType: 'blob',
  })
}
//中介机构评价表-详情
export function thirdPartyEvaluationGetDetail (params) {
  return request({
    url: '/audit/thirdPartyEvaluation/getDetail',
    method: 'get',
    params: transData(params),
  })
}
//中介机构评价表-删除
export function thirdPartyEvaluationRemove (params) {
  return request({
    url: '/audit/thirdPartyEvaluation/remove',
    method: 'get',
    params: transData(params),
  })
}
//中介机构评价表-新增或修改
export function thirdPartyEvaluationMergeInfo (params) {
  return request({
    url: '/audit/thirdPartyEvaluation/mergeInfo',
    method: 'post',
    params: transData(params),
  })
}



//本年度内部审计发现问题及整改情况-列表
export function currentYearAuditIssuesGetPage (params) {
  return request({
    url: '/audit/currentYearAuditIssues/getPage',
    method: 'get',
    params: transData(params),
  })
}
//本年度内部审计发现问题及整改情况-导出
export function currentYearAuditIssuesExport (params) {
  return request({
    url: '/audit/currentYearAuditIssues/export',
    method: 'get',
    params: transData(params),
    responseType: 'blob',
  })
}
//本年度内部审计发现问题及整改情况-详情
export function currentYearAuditIssuesGetDetail (params) {
  return request({
    url: '/audit/currentYearAuditIssues/getDetail',
    method: 'get',
    params: transData(params),
  })
}
//本年度内部审计发现问题及整改情况-删除
export function currentYearAuditIssuesRemove (params) {
  return request({
    url: '/audit/currentYearAuditIssues/remove',
    method: 'get',
    params: transData(params),
  })
}
//本年度内部审计发现问题及整改情况-新增或修改
export function currentYearAuditIssuesMergeInfo (params) {
  return request({
    url: '/audit/currentYearAuditIssues/mergeInfo',
    method: 'post',
    params: transData(params),
  })
}



//上一年度内部审计发现问题整改情况-列表
export function previousYearAuditIssuesGetPage (params) {
  return request({
    url: '/audit/previousYearAuditIssues/getPage',
    method: 'get',
    params: transData(params),
  })
}
//上一年度内部审计发现问题整改情况-导出
export function previousYearAuditIssuesExport (params) {
  return request({
    url: '/audit/previousYearAuditIssues/export',
    method: 'get',
    params: transData(params),
    responseType: 'blob',
  })
}
//上一年度内部审计发现问题整改情况-详情
export function previousYearAuditIssuesGetDetail (params) {
  return request({
    url: '/audit/previousYearAuditIssues/getDetail',
    method: 'get',
    params: transData(params),
  })
}
//上一年度内部审计发现问题整改情况-删除
export function previousYearAuditIssuesRemove (params) {
  return request({
    url: '/audit/previousYearAuditIssues/remove',
    method: 'get',
    params: transData(params),
  })
}
//上一年度内部审计发现问题整改情况-新增或修改
export function previousYearAuditIssuesMergeInfo (params) {
  return request({
    url: '/audit/previousYearAuditIssues/mergeInfo',
    method: 'post',
    params: transData(params),
  })
}
