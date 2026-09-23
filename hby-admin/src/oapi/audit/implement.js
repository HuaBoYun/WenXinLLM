import request from '@/utils/request'
import { transData } from '@/utils/requestData'

//进场纪要-列表查询
export function approachSummaryList(params) {
  return request({
    url: '/audit/auditImplement/sjss/in_meet_record_list',
    method: 'get',
    params: transData(params),
  })
}

//进场纪要-明细
export function approachSummaryDisp(params) {
  return request({
    url: '/audit/auditImplement/sjss/in_meet_record_disp',
    method: 'get',
    params: transData(params),
  })
}

//进场纪要-新增与修改
export function approachSummarySave(params) {
  return request({
    url: '/audit/auditImplement/sjss/in_meet_record_add',
    method: 'post',
    data: transData(params),
  })
}

//进场纪要-根据主键删除信息
export function approachSummaryDelete(params) {
  return request({
    url: '/audit/auditImplement/sjss/met_delete',
    method: 'get',
    params: transData(params),
  })
}

//进场纪要-作废
export function approachSummaryCancel(params) {
  return request({
    url: '/audit/auditImplement/sjss/met_cancel',
    method: 'get',
    params: transData(params),
  })
}

//进场纪要-导出
export function approachSummaryExport(params) {
  return request({
    url: '/audit/auditImplement/sjss/in_meet_record_export',
    method: 'get',
    params: transData(params),
  })
}

//进场纪要-附件列表
export function inMeetRecordFileList(params) {
  return request({
    url: '/audit/auditImplement/sjss/in_meet_record_file_list',
    method: 'get',
    params: transData(params),
  })
}

//我的任务清单列表
export function myTaskList(params) {
  return request({
    url: '/audit/auditImplement/sjss/check_list_my',
    method: 'get',
    params: transData(params),
  })
}

//任务查看列表
export function myTaskListData(params) {
  return request({
    url: '/audit/pjData/sjss/check_list_my_all',
    method: 'get',
    params: transData(params),
  })
}

//我的任务-完成
export function myTaskFinal(params) {
  return request({
    url: '/audit/auditImplement/sjss/check_final',
    method: 'get',
    params: transData(params),
  })
}

//我的任务-明细
export function myTaskDisp(params) {
  return request({
    url: '/audit/auditImplement/sjss/check_disp',
    method: 'get',
    params: transData(params),
  })
}

//工作日志列表
export function workReportList(params) {
  return request({
    url: '/audit/auditImplement/nbsj/xmgl/workReportList',
    method: 'get',
    params: transData(params),
  })
}

//工作日志-新增与修改
export function workReportSave(params) {
  return request({
    url: '/audit/auditImplement/nbsj/xmgl/workReportSave',
    method: 'post',
    data: transData(params),
  })
}

//工作日志-删除
export function workReportDelete(params) {
  return request({
    url: '/audit/auditImplement/nbsj/xmgl/workReportDelete',
    method: 'get',
    params: transData(params),
  })
}

//工作日志-明细
export function workReportDetail(params) {
  return request({
    url: '/audit/auditImplement/nbsj/xmgl/workReportDetail',
    method: 'get',
    params: transData(params),
  })
}

//工作日志-附件列表
export function workReportFileList(params) {
  return request({
    url: '/audit/auditImplement/nbsj/xmgl/workReport_file_list',
    method: 'get',
    params: transData(params),
  })
}

//我的底稿列表
export function myDraftList(params) {
  return request({
    url: '/audit/auditImplement/sjss/project_standard_listdg',
    method: 'get',
    params: transData(params),
  })
}

//选择底稿列表
export function selectDraftList(params) {
  return request({
    url: '/oiaudit/audit/MyManuscript/getMyManuscriptPage',
    method: 'get',
    params: transData(params),
  })
}

//选择底稿列表2
export function getManuscriptPage(params) {
  return request({
    url: '/oiaudit/audit/auditRectify/issue/getManuscriptPage',
    method: 'get',
    params: transData(params),
  })
}

//选择底稿列表3
export function wtqdManuscriptPage(params) {
  return request({
    url: '/oiaudit/audit/auditRectify/wtqd/getManuscriptPage',
    method: 'get',
    params: transData(params),
  })
}

//我的底稿-新增与修改
export function myDraftSave(params) {
  return request({
    url: '/audit/auditImplement/sjss/project_standard_dg_add',
    method: 'post',
    data: transData(params),
  })
}

//我的底稿-删除
export function myDraftDelete(params) {
  return request({
    url: '/audit/auditImplement/sjss/project_standard_dg_del',
    method: 'get',
    params: transData(params),
  })
}
// 底稿详情
export function getSheetApprovalInfo(params) {
  return request({
    url: '/audit/nbsjapproval/getTblNbsjSheetApprovalInfo',
    method: 'get',
    params: transData(params),
  })
}

//我的底稿-详情
export function myDraftDetail(params) {
  return request({
    url: '/audit/auditImplement/sjss/project_standard_dg_detail',
    method: 'get',
    params: transData(params),
  })
}
// 我的底稿 - 复核
export function saveDraft(params) {
  return request({
    url: '/audit/nbsjapproval/submitTblNbsjSheetArrpoval',
    method: 'post',
    data: transData(params),
  })
}

//我的底稿-附件列表
export function myDraftFileList(params) {
  return request({
    url: '/audit/auditImplement/sjss/project_standard_dg_file_list',
    method: 'get',
    params: transData(params),
  })
}

//底稿管理-根据项目底稿自动生成编号
export function MyManuscriptAutoCode(params) {
  return request({
    url: '/oiaudit/audit/MyManuscript/autoCode',
    method: 'get',
    params: transData(params),
  })
}

//我的底稿-导出
export function myDraftExport(params) {
  return request({
    url: '/oiaudit/audit/MyManuscript/export',
    method: 'get',
    responseType: 'blob',
    params: transData(params),
  })
}

//疑点管理列表
export function doubtfulList(params) {
  return request({
    url: '/audit/auditImplement/nkgj/dp/dp_list',
    method: 'get',
    params: transData(params),
  })
}

//疑点管理-新增与修改
export function doubtfulSave(params) {
  return request({
    url: '/audit/auditImplement/nkgj/dp_add',
    method: 'post',
    data: transData(params),
  })
}

//疑点管理-删除
export function doubtfulDelete(params) {
  return request({
    url: '/audit/auditImplement/nkgj/dp_del',
    method: 'get',
    params: transData(params),
  })
}

//疑点管理--详情
export function doubtfulDetail(params) {
  return request({
    url: '/audit/auditImplement/nkgj/dp_detail',
    method: 'get',
    params: transData(params),
  })
}

//疑点管理--附件列表
export function dpFileList(params) {
  return request({
    url: '/audit/auditImplement/nkgj/dp_file_list',
    method: 'get',
    params: transData(params),
  })
}

//底稿管理列表
export function draftManageList(params) {
  return request({
    url: '/oiaudit/audit/MyManuscript/dggl_list',
    method: 'get',
    params: transData(params),
  })
}

//底稿管理--详情
export function draftManageDelete(params) {
  return request({
    url: '/audit/auditImplement/sjss/dggl_detail',
    method: 'get',
    params: transData(params),
  })
}

//审计发现列表
export function discoverList(params) {
  return request({
    url: '/oiaudit/auditImplement/xmzl/question_store_list',
    method: 'get',
    params: transData(params),
  })
}

//审计发现-删除
export function discoverDelete(params) {
  return request({
    url: '/audit/auditImplement/xmzl/question_store_del',
    method: 'get',
    params: transData(params),
  })
}

//审计发现-发起整改
export function discoverStatus(params) {
  return request({
    url: '/audit/auditImplement/sjzg/zgfp_fqStatus',
    method: 'get',
    params: transData(params),
  })
}

//事实确认书列表
export function confirmList(params) {
  return request({
    url: '/audit/auditImplement/sjss/confirmation_list',
    method: 'get',
    params: transData(params),
  })
}

//事实确认书-新增与修改
export function confirmSave(params) {
  return request({
    url: '/audit/auditImplement/sjss/confirmation_add',
    method: 'post',
    data: transData(params),
  })
}

//事实确认书-删除
export function confirmDelete(params) {
  return request({
    url: '/audit/auditImplement/sjss/confirmation_del',
    method: 'get',
    params: transData(params),
  })
}

//事实确认书--详情
export function confirmDetail(params) {
  return request({
    url: '/audit/auditImplement/sjss/confirmation_detail',
    method: 'get',
    params: transData(params),
  })
}

//离场纪要列表
export function leaveSummaryList(params) {
  return request({
    url: '/audit/auditImplement/sjss/out_meet_record_list',
    method: 'get',
    params: transData(params),
  })
}

//离场纪要-新增与修改
export function leaveSummarySave(params) {
  return request({
    url: '/audit/auditImplement/sjss/out_meet_record_add',
    method: 'post',
    data: transData(params),
  })
}

//离场纪要根据主键删除信息
export function leaveSummaryDelete(params) {
  return request({
    url: '/audit/auditImplement/sjss/lev_delete',
    method: 'get',
    params: transData(params),
  })
}

//离场纪要-作废
export function leaveSummaryCancel(params) {
  return request({
    url: '/audit/auditImplement/sjss/out_met_cancel',
    method: 'get',
    params: transData(params),
  })
}
//离场纪要-明细
export function leaveSummaryDisp(params) {
  return request({
    url: '/audit/auditImplement/sjss/out_meet_record_disp',
    method: 'get',
    params: transData(params),
  })
}
//离场纪要-导出
export function leaveSummaryExport(params) {
  return request({
    url: '/audit/auditImplement/sjss/out_meet_record_export',
    method: 'get',
    params: transData(params),
  })
}

//离场纪要-附件列表
export function leaveSummaryFileList(params) {
  return request({
    url: '/audit/auditImplement/sjss/out_meet_record_file_list',
    method: 'get',
    params: transData(params),
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
    url: '/audit/fileManage/delete',
    method: 'get',
    params: transData(params),
  })
}
//附件删除接口，不删除中间表关系
export function deleteLawFile(params) {
  return request({
    url: '/audit/nbsjworkSpace/gkzk/mag/out_file_del',
    method: 'get',
    params: transData(params),
  })
}
//附件删除接口，不删除中间表关系
export function deleteManageFile(params) {
  return request({
    url: '/audit/nbsjworkSpace/deleteInnerRuleAtt',
    method: 'get',
    params: transData(params),
  })
}

//附件删除接口，不删除中间表关系
export function deleteReportFile(params) {
  return request({
    url: '/audit/auditReport/manage/report_file_del',
    method: 'get',
    params: transData(params),
  })
}
//事实确认书-表格列表详细
export function confirmationQuestionLink(params) {
  return request({
    url: '/audit/auditImplement/sjss/confirmation_question_link',
    method: 'get',
    params: transData(params),
  })
}

//事实确认书-表格列表
export function confirmationQuestionList(params) {
  return request({
    url: '/audit/auditImplement/sjss/confirmation_question_list',
    method: 'get',
    params: transData(params),
  })
}

//选择公司人员列表
export function userList(params) {
  return request({
    url: '/oiaudit/auditImplement/user/user_list',
    method: 'get',
    params: transData(params),
  })
}

//选择公司列表
// export function orgList(params) {
//   return request({
//     url: '/setting/redisorg/findOrganizationByJTTreeAllGS',
//     method: 'post',
//     data: transData(params),
//   })
// }
export function orgList(params) {
  return request({
    url: '/audit/auditImplement/user/org_list',
    method: 'get',
    params: transData(params),
  })
}

//选择部门列表
export function findOrganizationByTreeAllss(params) {
  return request({
    url: '/audit/auditProject/htdl/findOrganizationByTreeAllss',
    method: 'post',
    data: transData(params),
  })
}

//选择部门人员列表
export function user2list(params) {
  return request({
    url: '/setting/baseInfo/getUserList',
    method: 'get',
    params: transData(params),
  })
}

//操作发送至缺陷
export function sendDefect(params) {
  return request({
    url: '/audit/operation/defect/nbsj_yigl',
    method: 'get',
    params: transData(params),
  })
}

//操作发送至疑点
export function sendDoubtful(params) {
  return request({
    url: '/audit/operation/doubtful/nbsj_yigl',
    method: 'get',
    params: transData(params),
  })
}

//操作发送至底稿
export function sendManuscript(params) {
  return request({
    url: '/audit/operation/manuscript/nbsj_yigl',
    method: 'get',
    params: transData(params),
  })
}

//操作发送至风险
export function sendRisk(params) {
  return request({
    url: '/audit/operation/risk/nbsj_yigl',
    method: 'get',
    params: transData(params),
  })
}

//操作发送至底稿附件 保存附件关系方法
export function sendManuscriptAtt(params) {
  return request({
    url: '/audit/operation/manuscript/att/nbsj_yigl',
    method: 'get',
    params: transData(params),
  })
}

//操作发送至底稿附件 获取底稿列表
export function sendManuscriptGzdg(params) {
  return request({
    url: '/audit/operation/manuscript/gzdg/nbsj_yigl',
    method: 'get',
    params: transData(params),
  })
}

//任务管理-左侧树
export function getTree(params) {
  return request({
    url: '/audit/pjData/sjss/getTree',
    method: 'get',
    params: transData(params),
  })
}

//事实确认书-附件列表
export function confirmationFileList(params) {
  return request({
    url: '/audit/auditImplement/sjss/confirmation_file_list',
    method: 'get',
    params: transData(params),
  })
}

// 我的底稿审批 同意驳回 /nbsjapproval/dealRecordApporvalInfo
export function saveDealRecordApporvalInfo(params) {
  return request({
    url: '/audit/nbsjapproval/dealTblNbsjSheetApporval',
    method: 'post',
    data: transData(params),
  })
}

// 事实确认书 提交审批 /nbsjapproval/submitTblNbsjFactbookArrpoval
export function saveTblNbsjFactbookArrpoval(params) {
  return request({
    url: '/audit/nbsjapproval/submitTblNbsjFactbookArrpoval',
    method: 'post',
    data: transData(params),
  })
}
//事实取人 查看详情 /nbsjapproval/getTblNbsjFactbookApprovalInfo

export function getDetailsById(params) {
  return request({
    url: '/audit/nbsjapproval/getTblNbsjFactbookApprovalInfo',
    method: 'get',
    params: transData(params),
  })
}

// 事实取人 通过驳回 /nbsjapproval/dealTblNbsjFactbookApporval
export function savedealTblNbsjFactbookApporval(params) {
  return request({
    url: '/audit/nbsjapproval/dealTblNbsjFactbookApporval',
    method: 'post',
    data: transData(params),
  })
}
//事实确认书 获取项目名称 /auditProject/xmgl/curr_ss_project

export function getProjectName(params) {
  return request({
    url: '/audit/auditProject/xmgl/curr_ss_project',
    method: 'get',
    params: transData(params),
  })
}
// 项目查看 - 项目方案
export function getProjectPlanDetails(params) {
  return request({
    url: '/audit/auditProject/xmgl/project_detail',
    method: 'get',
    params: transData(params),
  })
}
//项目查看 - 项目小组 /auditProject/xmgl/project_pjteam_list

export function getProjectPlanTableDat(params) {
  return request({
    url: '/audit/auditProject/xmgl/project_pjteam_list',
    method: 'get',
    params: transData(params),
  })
}
// 我的底稿 删除 报告内容问题描述  /auditImplement/sjss/sheet_report_del
export function deleteSheetReport(params) {
  return request({
    url: '/audit/auditImplement/sjss/sheet_report_del',
    method: 'get',
    params: transData(params),
  })
}

// 建议书获取附件列表
export function getDefaultFIleData(params) {
  return request({
    url: '/audit/auditReport/sjzj/audit_suggest_file_list',
    method: 'get',
    params: transData(params),
  })
}

// 底稿判断当前是否为组长
export function whetherLeader(params) {
  return request({
    url: '/audit/auditImplement/sjss/ifPmOrLeader',
    method: 'get',
    params: transData(params),
  })
}

// 汇总底稿弹框列表
export function getHZDGTableData(params) {
  return request({
    url: '/audit/auditImplement/sjss/chooseProjectSheet',
    method: 'get',
    params: transData(params),
  })
}

// 审计问题类型数据
export function getSJWTTypeDatas(params) {
  return request({
    url: '/audit/nbsjworkSpace/getNbsjQuestionTypeList',
    method: 'post',
    data: transData(params),
  })
}

// 底稿取证书弹框类型数据
export function getQRSModalList(params) {
  return request({
    url: '/audit/auditImplement/sjss/getNbsjCertificateListPage',
    method: 'post',
    data: transData(params),
  })
}

// 底稿取证书详情
export function getQZSInfo(params) {
  return request({
    url: '/audit/auditImplement/sjss/getNbsjCertificateList',
    method: 'post',
    data: transData(params),
  })
}

//审计取证单-列表数据
export function getImPlementOrder(params) {
  return request({
    url: '/audit/nbsjworkSpace/qzd/getNbsjCertificateListPage',
    method: 'post',
    data: transData(params),
  })
}

//审计取证单-实施信息
export function createImPlementDetail(params) {
  return request({
    url: '/audit/auditProject/xmgl/curr_ss_project',
    method: 'get',
    params: transData(params),
  })
}

//审计取证单-新建
export function createImPlementOrder(params) {
  return request({
    url: '/audit/nbsjworkSpace/qzd/certificateSave',
    method: 'post',
    data: transData(params),
  })
}

//审计取证单-删除
export function deleteImPlementOrder(params) {
  return request({
    url: '/audit/nbsjworkSpace/qzd/certificate_del',
    method: 'get',
    params: transData(params),
  })
}

//审计取证单-明细
export function imPlementOrderDetail(params) {
  return request({
    url: '/audit/nbsjworkSpace/qzd/certificate_detail',
    method: 'get',
    params: transData(params),
  })
}

//审计取证单-附件列表
export function imPlementOrderFiles(params) {
  return request({
    url: '/audit/nbsjworkSpace/qzd/certificate_file_list',
    method: 'get',
    params: transData(params),
  })
}

///////////
/**
 * @description: 审计计划管理-查看办理页面
 * @param {*} data
 * @return {*}
 */
export function getAuditPlanApprovalInfo(data) {
  return request({
    url: '/audit/nbsjapproval/getAuditPlanApprovalInfo',
    method: 'get',
    params: transData(data),
  })
}

export function getProjectApprovalInfo(data) {
  return request({
    url: '/audit/nbsjapproval/getProjectApprovalInfo',
    method: 'get',
    params: transData(data),
  })
}

/**
 * @description: 审计计划管理-办理审批流程，通过或驳回
 * @param {*} data
 * @return {*}
 */
export function dealAuditPlanApporvalInfo(data) {
  return request({
    url: '/audit/nbsjapproval/dealAuditPlanApporvalInfo',
    method: 'post',
    data: transData(data),
  })
}
/**
 * @description: 项目管理-办理审批流程，通过或驳回
 * @param {*} data
 * @return {*}
 */
export function dealProjectApporvalInfo(data) {
  return request({
    url: '/audit/nbsjapproval/dealProjectApporvalInfo',
    method: 'post',
    data: transData(data),
  })
}

// 查看流程图--审计通用
export function pictureUrl(data) {
  return request({
    url: '/audit/nbsjapproval/picture',
    method: 'get',
    params: transData(data),
  })
}

//底稿列表删除
export function doDelete(params) {
  return request({
    url: '/audit/auditImplement/sjss/project_standard_dg_del',
    method: 'get',
    params: transData(params),
  })
}
//审计取证单导出
export function qZDExport(params) {
  return request({
    url: '/audit/nbsjworkSpace/qzd/certificate_export',
    method: 'post',
    responseType: 'blob',
    data: transData(params),
  })
}
//底稿业务单元列表
export function getYWDYlist(params) {
  return request({
    url: '/audit/auditImplement/sjss/findPorgramByUser',
    method: 'get',
    params: transData(params),
  })
}
//底稿关联缺陷列表
export function getBugList(params) {
  return request({
    url: '/audit/auditImplement/sjss/getNbsjBugList',
    method: 'post',
    data: transData(params),
  })
}
//底稿关联缺陷列表
export function getRelateDraftModalList(params) {
  return request({
    url: '/audit/auditImplement/sjss/rwdggl_list',
    method: 'get',
    params: transData(params),
  })
}
//新增疑点管理编号
export function createdpnumberCode(params) {
  return request({
    url: '/audit/auditPlan/getAutoCodeByYdgl',
    method: 'post',
    data: transData(params),
  })
}
//新增我的底稿编号
export function createDrafCode(params) {
  return request({
    url: '/audit/auditPlan/getAutoCodeByWddg',
    method: 'post',
    data: transData(params),
  })
}

//审计发现-导出
export function exportDiscover(params) {
  return request({
    url: '/audit/auditImplement/xmzl/question_store_export',
    method: 'get',
    responseType: 'blob',
    params: transData(params),
  })
}

// 审计实施---审计项目情况表---查询审计项目情况列表
export function projectAllAuditProject(data) {
  return request({
    url: '/oiaudit/audit/execPharse/project/allAuditProject',
    method: 'get',
    params: transData(data),
  })
}

// 审计实施---审计项目情况表---根据ID查询审计项目情况表
export function projectAuditProjectById(data) {
  return request({
    url: '/oiaudit/project/weerkly/getone',
    method: 'get',
    params: transData(data),
  })
}

// 审计实施---审计项目情况表--删除
export function projectDelete(data) {
  return request({
    url: '/oiaudit/audit/execPharse/project/delete',
    method: 'get',
    params: transData(data),
  })
}

// 审计实施---审计项目情况表--新增/更新
export function projectSaveOrUpdate(data) {
  return request({
    url: '/oiaudit/audit/execPharse/project/saveOrUpdate',
    method: 'post',
    // params: transData(params),
    headers: {
      'Content-Type': 'application/json;charset=utf-8',
    },
    data,
  })
}

// 审计实施---现场审查主要内容--删除
export function siteReviewDelete(data) {
  return request({
    url: '/oiaudit/audit/execPharse/siteReview/delete',
    method: 'get',
    params: transData(data),
  })
}

// 审计实施---现场审查主要内容---根据ID查询审计项目情况表
export function siteReviewFindOneById(data) {
  return request({
    url: '/oiaudit/audit/execPharse/siteReview/findOneById',
    method: 'get',
    params: transData(data),
  })
}

// 审计实施---现场审查主要内容---查询列表
export function siteReviewList(data) {
  return request({
    url: '/oiaudit/audit/execPharse/siteReview/findSiteReviewContentListByParam',
    method: 'get',
    params: transData(data),
  })
}
// 审计实施---现场审查主要内容---详情
export function siteReviewDetail(data) {
  return request({
    url: '/oiaudit/audit/execPharse/siteReview/detail',
    method: 'get',
    params: transData(data),
  })
}

// 审计实施---现场审查主要内容--新增/更新
export function siteReviewSaveOrUpdate(data) {
  return request({
    url: '/oiaudit/audit/execPharse/siteReview/saveOrUpdate',
    method: 'post',
    // params: transData(params),
    headers: {
      'Content-Type': 'application/json;charset=utf-8',
    },
    data,
  })
}

// 审计实施---审计工作记录--删除
export function workRecordsDelete(data) {
  return request({
    url: '/oiaudit/audit/workRecords/delete',
    method: 'get',
    params: transData(data),
  })
}

// 审计实施---审计工作记录---根据ID查询审计项目情况表
export function workRecordsDetail(data) {
  return request({
    url: '/oiaudit/audit/workRecords/getRecordsById',
    method: 'get',
    params: transData(data),
  })
}

// 审计实施---审计工作记录--获取工作记录列表
export function workRecordsList(data) {
  return request({
    url: '/oiaudit/audit/workRecords/getRecordsList',
    method: 'get',
    params: transData(data),
  })
}

// 审计实施---审计工作记录--新增/更新
export function workRecordsSaveOrUpdate(params) {
  return request({
    url: '/oiaudit/audit/workRecords/saveOrUpdate',
    method: 'post',
    headers: { 'Content-Type': 'multipart/form-data' },
    data: params,
  })
}

// 审计实施---审计督导任务--删除
export function overseeRecordsDelete(data) {
  return request({
    url: '/oiaudit/audit/overseeRecords/delete',
    method: 'get',
    params: transData(data),
  })
}

// 审计实施---审计督导任务---根据ID查询审计项目情况表
export function overseeRecordsDetail(data) {
  return request({
    url: '/oiaudit/audit/overseeRecords/getRecordsById',
    method: 'get',
    params: transData(data),
  })
}

// 审计实施---审计督导任务--获取工作记录列表
export function overseeRecordsList(data) {
  return request({
    url: '/oiaudit/audit/overseeRecords/getRecordsList',
    method: 'get',
    params: transData(data),
  })
}

// 审计实施---审计督导任务--新增/更新
export function overseeRecordsSaveOrUpdate(data, attids) {
  return request({
    url: '/oiaudit/audit/overseeRecords/saveOrUpdate?attids=' + attids,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=utf-8',
    },
    data: transData(data),
  })
}

// 审计实施-审计结果确认单-列表查询
export function resultList(params) {
  return request({
    url: '/oiaudit/result/getList',
    method: 'get',
    params: transData(params),
  })
}
// 审计实施-审计结果确认单-附件查询
export function getattList(params) {
  return request({
    url: '/oiaudit/result/getattList',
    method: 'get',
    params: transData(params),
  })
}

// 审计实施---审计结果确认单--删除
export function resultDeleteone(data) {
  return request({
    url: '/oiaudit/result/deleteone',
    method: 'post',
    params: transData(data),
  })
}

// 审计实施---审计结果确认单---查询详情
export function resultGetone(data) {
  return request({
    url: '/oiaudit/result/getone',
    method: 'get',
    params: transData(data),
  })
}

// 审计实施---审计结果确认单--新增/更新
export function resultSaveOrUpdate(params) {
  return request({
    url: '/oiaudit/result/saveOrupdate',
    method: 'post',
    data: transData(params),
  })
}

// 审计实施---审计承诺书
export function auditCommitmentLetterList(data) { // list
  return request({
    url: '/oiaudit/letter/getList',
    method: 'get',
    params: transData(data),
  })
}

export function auditCommitmentLetterUpdate(data) { // edit
  return request({
    url: '/oiaudit/letter/saveOrupdate',
    method: 'post',
    data: transData(data),
  })
}

export function auditCommitmentLetterDetail(data) { // detail
  return request({
    url: '/oiaudit/letter/getone',
    method: 'get',
    params: transData(data),
  })
}

export function auditCommitmentLetterGetattList(data) { // 附件列表
  return request({
    url: '/oiaudit/letter/getattList',
    method: 'get',
    params: transData(data),
  })
}

export function auditCommitmentLetterDeleteOne(data) { // delete
  return request({
    url: '/oiaudit/letter/deleteone',
    method: 'post',
    // headers: {
    //   'Content-Type': 'application/json;charset=utf-8',
    // },
    params: transData(data),
  })
}

export function auditCommitmentLetterDeleteAtt(data) { // delete 底稿稿件
  return request({
    url: '/oiaudit/letter/deleteatt',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=utf-8',
    },
    data: transData(data),
  })
}


// 审计实施---审计督导报告
export function auitSupervisorReportList(data) { // list
  return request({
    url: '/oiaudit/audit/report/getList',
    method: 'get',
    params: transData(data),
  })
}

export function auitSupervisorReportUpdate(data) { // edit
  return request({
    url: '/oiaudit/audit/report/saveOrUpdateReport',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=utf-8',
    },
    data: transData(data),
  })
}

export function auitSupervisorReportDetail(data) { // detail
  return request({
    url: '/oiaudit/audit/QualityAnalyReport/getRecordsById',
    method: 'get',
    params: transData(data),
  })
}
export function getOne(data) { // detail
  return request({
    url: '/oiaudit/audit/report/getOne',
    method: 'get',
    params: transData(data),
  })
}

export function auitSupervisorReportDelete(data) { // delete
  return request({
    url: '/oiaudit/audit/report/deleteReport',
    method: 'delete',
    params: transData(data),
  })
}

export function auitSupervisorReportDeleteFileAttach(data) { // delete 附件
  return request({
    url: '/oiaudit/audit/report/deleteFileAttach',
    method: 'post',
    data: transData(data),
  })
}


// 审计实施---质量分析报告
export function qualityAnalysisReportList(data) { // list
  return request({
    url: '/oiaudit/audit/QualityAnalyReport/getRecordsList',
    method: 'get',
    params: transData(data),
  })
}

export function qualityAnalysisReportUpdate(data) { // edit
  return request({
    url: '/oiaudit/audit/QualityAnalyReport/saveOrUpdate',
    method: 'post',
    data: transData(data),
  })
}

export function qualityAnalysisReportDetail(data) { // detail
  return request({
    url: '/oiaudit/audit/QualityAnalyReport/getRecordsById',
    method: 'get',
    params: transData(data),
  })
}

export function qualityAnalysisReportDelete(data) { // delete 删报告
  return request({
    url: '/oiaudit/audit/QualityAnalyReport/delete',
    method: 'get',
    params: transData(data),
  })
}

export function qualityAnalysisReportFileAttachDelete(data) { // delete 删附件
  return request({
    url: '/oiaudit/audit/QualityAnalyReport/deleteFileAttach',
    method: 'get',
    params: transData(data),
  })
}

export function getManuscriptById(data) { // delete 删附件
  return request({
    url: '/oiaudit/audit/MyManuscript/getManuscriptById',
    method: 'get',
    params: transData(data),
  })
}


//新审计发现
export function questionStoreList(data) { // delete 删附件
  return request({
    url: '/oiaudit/audit/MyManuscript/question_store_list',
    method: 'get',
    params: transData(data),
  })
}
//新审计发现-详情
export function questionStoreDetail(data) { // delete 删附件
  return request({
    url: '/oiaudit/audit/MyManuscript/getAuditFindingsById',
    method: 'get',
    params: transData(data),
  })
}


//我的任务--查看关联底稿
export function getMyManuscriptListByType(data) {
  return request({
    url: '/oiaudit/audit/MyManuscript/getMyManuscriptListByTypeId',
    method: 'get',
    params: transData(data),
  })
}


//审计实施-审计项目运行情况-周报列表 、
export function getProjectWeerklyList(data) {
  return request({
    url: '/oiaudit/project/weerkly/getList',
    method: 'get',
    params: transData(data),
  })
}

//审计实施-审计项目运行情况-导出
export function weerklyExport(params) {
  return request({
    url: '/oiaudit/project/weerkly/export',
    method: 'get',
    responseType: 'blob',
    params: transData(params),
  })
}

//审计实施-审计项目运行情况-周报列表选择项目 、
export function getListcCompleted(data) {
  return request({
    url: '/oiaudit/project/implementPlan/getListcCompleted',
    method: 'get',
    params: transData(data),
  })
}
//审计实施-审计项目运行情况-删除 、
export function weerklyDel(data) {
  return request({
    url: '/oiaudit/project/weerkly/delete',
    method: 'get',
    params: transData(data),
  })
}
export const weerklySaveOrUpdate = (data) => request({
  url: '/oiaudit/project/weerkly/saveOrUpdate',
  method: 'post',
  headers: {
    "Content-Type": "application/json;charset=utf-8"
  },
  data,
})

//审计管理-质量分析统计表--列表详情
export function sjglQutlitgetlist(params) {
  return request({
    url: '/oiaudit/project/implementPlan/sjgl/qutlitgetlist',
    method: 'get',
    params: transData(params),
  })
}

//审计管理-二级单位最近一次审计-未委托及预计离任列表数据-列表详情
export function wwtsjGetlist(params) {
  return request({
    url: '/oiaudit/wwtsj/getList',
    method: 'get',
    params: transData(params),
  })
}

//审计管理-二级单位最近一次审计-已委托未实施列表-列表详情
export function ywtsjGetlist(params) {
  return request({
    url: '/oiaudit/ywtsj/getList',
    method: 'get',
    params: transData(params),
  })
}