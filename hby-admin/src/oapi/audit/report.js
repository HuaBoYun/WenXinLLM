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
    responseType: 'blob',
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
//新增建议书编号
export function createSuggestCode(params) {
  return request({
    url: '/audit/auditPlan/getAutoCodeBySjjys',
    method: 'post',
    data: transData(params),
  })
}

//报告编制初稿-导出
export function exportReportChuGao(params) {
  return request({
    url: '/audit/auditReport/manage/expReportDoc',
    method: 'get',
    responseType: 'blob',
    params: transData(params),
  })
}

// 审计报告-交换意见稿-删除  jhyjg
export function jhyjgDelete(params) {
  return request({
    url: '/oiaudit/jhyjg/delete',
    method: 'get',
    params: transData(params),
  })
}

// 审计报告-交换意见稿-单个详情
export function jhyjgDetail(params) {
  return request({
    url: '/oiaudit/jhyjg/detail',
    method: 'get',
    params: transData(params),
  })
}

// 审计报告-交换意见稿-列表查询
export function jhyjgList(params) {
  return request({
    url: '/oiaudit/jhyjg/list',
    method: 'get',
    params: transData(params),
  })
}

// 审计报告-交换意见稿-新增
export function jhyjgSaveOrUpdate(params) {
  return request({
    url: '/oiaudit/jhyjg/saveOrUpdate',
    method: 'post',
    data: transData(params),
  })
}

// 审计报告-审理报告-删除
export function slbgDelete(params) {
  return request({
    url: '/oiaudit/slbg/delete',
    method: 'get',
    params: transData(params),
  })
}

// 审计报告-审理报告-单个详情
export function slbgDetail(params) {
  return request({
    url: '/oiaudit/slbg/detail',
    method: 'get',
    params: transData(params),
  })
}

// 审计报告-审理报告-列表查询
export function slbgList(params) {
  return request({
    url: '/oiaudit/slbg/list',
    method: 'get',
    params: transData(params),
  })
}

// 审计报告-审理报告-新增
export function slbgSaveOrUpdate(params) {
  return request({
    url: '/oiaudit/slbg/saveOrUpdate',
    method: 'post',
    data: transData(params),
  })
}

// 审计报告-审计报告定稿-删除
export function sjbgdgDelete(params) {
  return request({
    url: '/oiaudit/sjbgdg/delete',
    method: 'get',
    params: transData(params),
  })
}

// 审计报告-审计报告定稿-单个详情
export function sjbgdgDetail(params) {
  return request({
    url: '/oiaudit/sjbgdg/detail',
    method: 'get',
    params: transData(params),
  })
}

// 审计报告-审计报告定稿-列表查询
export function sjbgdgList(params) {
  return request({
    url: '/oiaudit/sjbgdg/list',
    method: 'get',
    params: transData(params),
  })
}

// 审计报告-审计报告定稿-新增
export function sjbgdgSaveOrUpdate(params) {
  return request({
    url: '/oiaudit/sjbgdg/saveOrUpdate',
    method: 'post',
    data: transData(params),
  })
}

// 审计报告-经济责任审计结果-删除
export function jjzrsjjgbgDelete(params) {
  return request({
    url: '/oiaudit/jjzrsjjgbg/delete',
    method: 'get',
    params: transData(params),
  })
}

// 审计报告-经济责任审计结果-单个详情
export function jjzrsjjgbgDetail(params) {
  return request({
    url: '/oiaudit/jjzrsjjgbg/detail',
    method: 'get',
    params: transData(params),
  })
}

// 审计报告-经济责任审计结果-列表查询
export function jjzrsjjgbgList(params) {
  return request({
    url: '/oiaudit/jjzrsjjgbg/list',
    method: 'get',
    params: transData(params),
  })
}

// 审计报告-经济责任审计结果-新增
export function jjzrsjjgbgSaveOrUpdate(params) {
  return request({
    url: '/oiaudit/jjzrsjjgbg/saveOrUpdate',
    method: 'post',
    data: transData(params),
  })
}

// 审计实施-审计项目追款情况表-删除
export function auditProjectZkDelete(params) {
  return request({
    url: '/oiaudit/audit/auditProjectZk/delete',
    method: 'delete',
    params: transData(params),
  })
}

// 审计实施-审计项目追款情况表-单个详情
export function auditProjectZkDetail(params) {
  return request({
    url: '/oiaudit/audit/auditProjectZk/detail',
    method: 'get',
    params: transData(params),
  })
}

// 审计实施-审计项目追款情况表-列表查询
export function auditProjectZkList(params) {
  return request({
    url: '/oiaudit/sjxmzk/getList',
    method: 'get',
    params: transData(params),
  })
}
// 审计实施-审计项目追款情况表-导出
export function exportProject(params) {
  return request({
    url: '/oiaudit/sjxmzk/export',
    method: 'get',
    params: transData(params),
    responseType: 'blob',
  })
}
// 审计实施-审计项目追款情况表-新增
export function auditProjectZkSaveOrUpdate(data) {
  return request({
    url: '/oiaudit/audit/auditProjectZk/saveOrUpdate',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=utf-8',
    },
    data: transData(data),
  })
}

//附件下载接口
export function download(params) {
  return request({
    url: '/oiaudit/fileManage/download',
    method: 'get',
    responseType: 'blob',
    params: transData(params),
  })
}

//附件删除接口，不删除中间表关系
export function deleteFile(params) {
  return request({
    url: '/oiaudit/fileManage/delete',
    method: 'get',
    params: transData(params),
  })
}

//附件删除接口，不删除中间表关系
export function deleteReportFile(params) {
  return request({
    url: '/oiaudit/auditReport/manage/report_file_del',
    method: 'get',
    params: transData(params),
  })
}

//获取工作记录表
export function getSjgzjlList(params) {
  return request({
    url: '/oiaudit/sjgzjl/list',
    method: 'get',
    params: transData(params),
  })
}
//获取工作记录详情
export function getSjgzjlDetail(params) {
  return request({
    url: '/oiaudit/sjgzjl/detail',
    method: 'get',
    params: transData(params),
  })
}
//获取工作记录表删除
export function sjgzjlDelete(params) {
  return request({
    url: '/oiaudit/sjgzjl/delete',
    method: 'get',
    params: transData(params),
  })
}
//新增工作记录表
export function saveSjgzjl(params) {
  return request({
    url: '/oiaudit/sjgzjl/saveOrUpdate',
    method: 'post',
    data: transData(params),
  })
}

//审计意见书保存
export function sjjysSaveOrUpdate(params) {
  return request({
    url: '/oiaudit/slbg/sjyjs/saveOrupdate',
    method: 'post',
    data: transData(params),
  })
}

//审计意见书列表
export function sjjysList(params) {
  return request({
    url: '/oiaudit/slbg/sjyjs/getList',
    method: 'get',
    params: transData(params),
  })
}

//审计意见书详情
export function sjjysDetail(params) {
  return request({
    url: '/oiaudit/slbg/sjyjs/getone',
    method: 'get',
    params: transData(params),
  })
}

//审计意见书附件
export function getSjjysFileList(params) {
  return request({
    url: '/oiaudit/slbg/sjyjs/getattList',
    method: 'get',
    params: transData(params),
  })
}

//审计意见书附件删除

export function deleteSjjysFileList(params) {
  return request({
    url: '/oiaudit/slbg/sjyjs/deleteatt',
    method: 'post',
    data: transData(params),
  })
}

//审计意见书附件删除

export function deleteSjjys(params) {
  return request({
    url: '/oiaudit/slbg/sjyjs/deleteone',
    method: 'post',
    data: transData(params),
  })
}

export function getHistoryVersion(params) {
  return request({
    url: '/oiaudit/wtzg/getHistoryVersion',
    method: 'get',
    params: transData(params),
  })
}

// 审计结果确认单- 列表查询
export function sjglGetList(params) {
  return request({
    url: '/oiaudit/sjgl/getList',
    method: 'get',
    params: transData(params),
  })
}

//审计审减内容-导出
export function sjsjExport(params) {
  return request({
    url: '/oiaudit/sjsj/export',
    method: 'get',
    responseType: 'blob',
    params: transData(params),
  })
}

