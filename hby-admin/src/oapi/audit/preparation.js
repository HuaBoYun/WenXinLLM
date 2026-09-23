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
    url: '/audit/auditReady/sjss/def_cat_detail_zy',
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



//获取审计通知书列表
export function getNoticeList(params) {
  return request({
    url: '/oiaudit/auditReady/sjzb/notice_list',
    method: 'get',
    params: transData(params),
  })
}
// 审计通知书删除
export function deleteNotice(params) {
  return request({
    url: '/oiaudit/auditReady/sjzb/notice_del',
    method: 'get',
    params: transData(params),
  })
}
// 审计通知审批删除
export function deleteNotice1(params) {
  return request({
    url: '/oiaudit/auditReady/sjzb/noticeapr_del',
    method: 'get',
    params: transData(params),
  })
}
// 审计通知书作废
export function cancleNotice(params) {
  return request({
    url: '/oiaudit/auditReady/sjzb/notice_cancel',
    method: 'get',
    params: transData(params),
  })
}
// 审计通知书新增、修改
export function addNotice(params) {
  return request({
    url: '/oiaudit/auditReady/sjzb/notice_add',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: transData(params),
  })
}



// 获取审计通知变更列表
export function getNoticeChangeList(params) {
  return request({
    url: '/oiaudit/auditReady/sjtj/noticechangeList',
    method: 'get',
    params: transData(params),
  })
}
// 审计通知变更列表删除
export function deleteNoticeItem(params) {
  return request({
    url: '/oiaudit/auditReady/sjtj/noticechangeDel',
    method: 'get',
    params: transData(params),
  })
}
// 审计通知变更列表新增
export function NoticechangeAdd(params) {
  return request({
    url: '/oiaudit/auditReady/sjtj/noticechangeAdd',
    method: 'post',
    data: transData(params),
  })
}
// 审计通知变更列表详情
export function NoticechangeDisp(params) {
  return request({
    url: '/oiaudit/auditReady/sjtj/noticechangeDisp',
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
export function reportExport(params, headers) {
  return request({
    url: '/oiaudit/auditReady/expOuterRuleFile',
    method: 'get',
    responseType: 'blob',
    headers: { adviceid: params.adviceid },
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


// 下发操作
export function xiafaListNew(data) {
  return request({
    url: '/setting/distribution/saveDistributionDg',
    method: 'post',
    data,
  })
}

//项目资料详情获取关联经验库删除
export function notice_disp(params) {
  return request({
    url: '/oiaudit/auditReady/sjzb/notice_disp',
    method: 'get',
    params: transData(params),
  })
}

export function sjlxjytzSavePersonInfo(params) {
  return request({
    url: '/oiaudit/audit/proposalNotice/distribute',
    method: 'get',
    params: transData(params),
  })
}


//审计指引详情
export function getCheckDisp(params) {
  return request({
    url: '/oiaudit/auditImplement/sjss/check_disp',
    method: 'get',
    params: transData(params),
  })
}


//获取审计通知审批列表
export function getnoticeaprList(params) {
  return request({
    url: '/oiaudit/auditReady/sjzb/noticeapr_list',
    method: 'get',
    params: transData(params),
  })
}

// 审计通知审批新增修改
export function noticeaprAdd(data) {
  return request({
    url: '/oiaudit/auditReady/sjzb/noticeapr_add',
    method: 'post',
    data,
  })
}

//获取审计通知审批详情
export function noticeaprDisp(params) {
  return request({
    url: '/oiaudit/auditReady/sjzb/noticeapr_disp',
    method: 'get',
    params: transData(params),
  })
}
//获取选择关联的审计通知审批内容
export function noticeaprSplist(params) {
  return request({
    url: '/oiaudit/auditReady/sjzb/noticeapr_splist',
    method: 'get',
    params: transData(params),
  })
}
//增加显示关联变更信息
export function getbglist(params) {
  return request({
    url: '/oiaudit/auditReady/sjzb/getbglist',
    method: 'post',
    data: transData(params),
  })
}
//审计立项建议通知撤回
export function distributionBack(params) {
  return request({
    url: '/setting/distribution/deleteDistribution',
    method: 'post',
    data: transData(params),
  })
}