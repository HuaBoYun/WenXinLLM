import request from '@/utils/request'
import { transData } from '@/utils/requestData'

//导入资料 校验
export function ifPmOrLeader(params) {
  return request({
    url: '/audit/auditImplement/sjss/ifPmOrLeader',
    method: 'get',
    params: transData(params),
  })
}

//获取审计指引列表
export function getListZy(params) {
  return request({
    url: '/audit/auditReady/sjyj/def_cat_list_zy',
    method: 'get',
    params: transData(params),
  })
}

//获取审计指引左侧树
export function getLeftTreeZy(params) {
  return request({
    url: '/audit/auditReady/sjgj/getTreeZy',
    method: 'get',
    params: transData(params),
  })
}

//审计指引详情
export function getDetailZy(params) {
  return request({
    url: '/oiaudit/auditReady/sjss/def_cat_detail_zy',
    method: 'get',
    params: transData(params),
  })
}

//审计指引单个删除
export function defCatDel(params) {
  return request({
    url: '/audit/auditReady/sjyj/def_cat_del',
    method: 'get',
    params: transData(params),
  })
}

//获取项目资料列表
export function getDataprojectList(params) {
  return request({
    url: '/audit/auditReady/sjzl/dataproject_list',
    method: 'get',
    params: transData(params),
  })
}
//获取项目资料单个预览
export function getDataprojectView(params) {
  return request({
    url: '/audit/auditReady/sjzl/dataproject_view',
    method: 'get',
    params: transData(params),
  })
}
//项目资料准备新增和修改
export function dataProjectSave(params) {
  return request({
    url: '/audit/auditReady/sjzl/dataproject_save',
    method: 'post',
    data: transData(params),
  })
}
//审计指引-新增与修改
export function defCatAdd(params) {
  return request({
    url: '/audit/auditReady/sjyj/def_cat_add',
    method: 'post',
    data: transData(params),
  })
}
//项目资料单个删除
export function dataProjectDel(params) {
  return request({
    url: '/audit/auditReady/sjzl/dataproject_del',
    method: 'get',
    params: transData(params),
  })
}
//获取项目资料左侧树
export function findOrganizationByTreeAllss(params) {
  return request({
    url: '/audit/auditProject/htdl/findOrganizationByTreeAllss',
    method: 'post',
    data: transData(params),
  })
}
//获取审计资料列表
export function getProjectProposal(params) {
  return request({
    url: '/audit/auditReady/sjzb/project_proposal_yw',
    method: 'get',
    params: transData(params),
  })
}
//前期审计资料--导入资料列表
export function planListPlanIdIn(params) {
  return request({
    url: '/audit/auditReady/sjgd/audit_plan_list_planId_in',
    method: 'get',
    params: transData(params),
  })
}
// 前期审计资料--导入资料选择保存
export function listProjectIdSave(params) {
  return request({
    url: '/audit/auditReady/sjgd/audit_plan_list_planId_in_save',
    method: 'post',
    data: transData(params),
  })
}
// 审计通知书-作废
export function noticeCancel(params) {
  return request({
    url: '/audit/auditReady/sjzb/notice_cancel',
    method: 'GET',
    params: transData(params),
  })
}
// 项目资料准备-附件列表
export function dataprojectFileList(params) {
  return request({
    url: '/audit/auditReady/sjzl/dataproject_file_list',
    method: 'get',
    params: transData(params),
  })
}
//工作日志-附件列表
export function noticeFileList(params) {
  return request({
    url: '/audit/auditReady/sjzb/notice_file_list',
    method: 'GET',
    params: transData(params),
  })
}
//获取审计资料明细
export function getProjectProposalDetail(params) {
  return request({
    url: '/audit/auditReady/sjzb/project_proposal_detail',
    method: 'get',
    params: transData(params),
  })
}

//获取审计通知-下发列表
export function getNoticeIssueList(params) {
  return request({
    url: '/audit/auditReady/sjzb/notice_issue_list',
    method: 'get',
    params: transData(params),
  })
}
//获取审计通知书列表
export function getNoticeList(params) {
  return request({
    url: '/audit/auditReady/sjzb/notice_list',
    method: 'get',
    params: transData(params),
  })
}

//获取审计通知书列表
export function getNoticeDefaultData(params) {
  return request({
    url: '/audit/auditReady/sjzb/notice_disp',
    method: 'get',
    params: transData(params),
  })
}

//审计通知书新增和修改
export function noticeAdd(params) {
  return request({
    url: '/audit/auditReady/sjzb/notice_add',
    method: 'post',
    data: transData(params),
  })
}

//提交审批
export function handleTabs(params) {
  return request({
    url: '/audit/nbsjapproval/submitTblAdvicenoteArrpoval',
    method: 'post',
    data: transData(params),
  })
}

//通知书-导出
export function reportExport(params) {
  return request({
    url: '/audit/auditReady/expOuterRuleFile',
    method: 'get',
    responseType: 'blob',
    params: transData(params),
  })
}

//获取当前实施项目
export function currSsProject(params) {
  return request({
    url: '/audit/auditProject/xmgl/curr_ss_project',
    method: 'get',
    params: transData(params),
  })
}

//删除项目资料附件
export function deleteProjectFile(params) {
  return request({
    url: '/audit/auditReady/sjzl/dataproject_file_del',
    method: 'get',
    params: transData(params),
  })
}
//删除通知书附件
export function deleteNoticeFile(params) {
  return request({
    url: '/audit/auditReady/sjzb/notice_file_del',
    method: 'get',
    params: transData(params),
  })
}
//新建通知书编号
export function createNoticeCode(params) {
  return request({
    url: '/audit/auditPlan/getAutoCodeBySjtz',
    method: 'post',
    data: transData(params),
  })
}
//新建项目资料编号
export function createProjectDataCode(params) {
  return request({
    url: '/audit/auditPlan/getAutoCodeByXmzl',
    method: 'post',
    data: transData(params),
  })
}
//项目资料详情获取关联模板库
export function getSJMBKDefaultInfo(params) {
  return request({
    url: '/audit/audit/mb/getMbBydatapreid',
    method: 'post',
    data: transData(params),
  })
}
//项目资料详情获取关联经验库
export function getSJJYKDefaultInfo(params) {
  return request({
    url: '/audit/audit/jyk/getjykBydatapreid',
    method: 'post',
    data: transData(params),
  })
}
//项目资料详情获取关联模板库删除
export function deleteSJMBKTableData(params) {
  return request({
    url: '/audit/audit/mb/delBymbid',
    method: 'post',
    data: transData(params),
  })
}
//项目资料详情获取关联经验库删除
export function deleteSJJYKTableData(params) {
  return request({
    url: '/audit/audit/jyk/deljykbyid',
    method: 'post',
    data: transData(params),
  })
}

//项目资料详情获取关联经验库删除
export function saveFilePerson(params) {
  return request({
    url: '/audit/auditReady/sjzl/xfry',
    method: 'get',
    params: transData(params),
  })
}
//项目资料附件同步
export function tbdata(params) {
  return request({
    url: '/audit/auditReady/sjzl/tbxmfj',
    method: 'post',
    data: transData(params),
  })
}

//审计指引详情
export function getDetailZyAudit(params) {
  return request({
    url: '/audit/auditReady/sjss/def_cat_detail_zy',
    method: 'get',
    params: transData(params),
  })
}
//审计通知下发保存
export function sjtzXF(params) {
  return request({
    url: '/audit/auditReady/sjzb/xftzs',
    method: 'get',
    params: transData(params),
  })
}
//项目资料保存
export function saveFj(params) {
  return request({
    url: '/audit/auditReady/sjzl/saveFj',
    method: 'post',
    data: transData(params),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
