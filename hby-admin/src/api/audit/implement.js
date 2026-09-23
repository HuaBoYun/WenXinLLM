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
    url: '/oiaudit/auditImplement/sjss/check_disp',
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

//我的底稿-新增与修改
export function myDraftSave(params) {
  return request({
    url: '/audit/auditImplement/sjss/project_standard_dg_add',
    method: 'post',
    data: transData(params),
    headers: {
      'Content-Type': 'application/json',
    },
  })
}

//我的底稿-绑定附件
export function newMyDraftSave(params) {
  return request({
    url: '/audit/auditImplement/sjss/project_dgfile_save',
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

//我的底稿-导出
export function myDraftExport(params) {
  return request({
    url: '/audit/auditImplement/sjss/exportSheetWord',
    // url: '/audit/auditImplement/sjss/project_standard_dg_export',
    method: 'get',
    responseType: 'blob',
    params: transData(params),
  })
}

//我的底稿-全量导出
export function myDraftAllExport(params) {
  return request({
    url: '/audit/auditImplement/sjss/project_standard_dg_export',
    method: 'post',
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

//疑点管理-导出
export function doubtfulExport(params) {
  return request({
    url: '/audit/zgzz/exportYdgl',
    method: 'get',
    responseType: 'blob',
    params: transData(params),
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

//疑点管理--附件列表-档案列表跳转专用
export function projectStandardDpLis(params) {
  return request({
    url: '/audit/auditImplement/sjss/project_standard_dp_list',
    method: 'get',
    params: transData(params),
  })
}

//底稿管理列表
export function draftManageList(params) {
  return request({
    url: '/audit/auditImplement/sjss/dggl_list',
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
    url: '/audit/auditImplement/xmzl/question_store_list',
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

//审计发现-发起整改
export function sjzgIsfalse(params) {
  return request({
    url: '/audit/auditImplement/sjzg/isfalse',
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
    url: '/audit/fileManage/download',
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
    url: '/audit/auditImplement/user/user_list',
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
//操作发送至审计取证单
export function sendQZD(params) {
  return request({
    url: '/audit/operation/evidence/nbsj_yigl',
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

//审计发现列表 wgzz汇总
export function discoverListwgzz(params) {
  return request({
    url: '/audit/nbsjzg/summaryAudit_list',
    method: 'get',
    params: transData(params),
  })
}

//测试任务-问题台账列表 wgzz汇总
export function getProblemLedgerListwgzz(params) {
  return request({
    url: '/nkhg/nbkz/csrw/getProblemLedgerList',
    method: 'get',
    params: transData(params),
  })
}

//审计整改-列表
export function getZgList(params) {
  return request({
    url: '/audit/nbsjzg/zg/project_standard_listdg',
    method: 'get',
    params: transData(params),
  })
}

//审计整改-详情
export function zgDetail(params) {
  return request({
    url: '/audit/nbsjzg/zg/project_standard_dg_detail',
    method: 'get',
    params: transData(params),
  })
}

//审计整改-新增与修改
export function zgSave(params) {
  return request({
    url: '/audit/nbsjzg/zg/project_standard_dg_add',
    method: 'post',
    data: transData(params),
  })
}


//新的我的任务-完成
export function newMyTaskFinal(params) {
  return request({
    url: '/oiaudit/project/implementPlan/sjss/my_task_finish',
    method: 'get',
    params: transData(params),
  })
}

//任务分配-工程项目结算汇总查看列表数据 31
export function getGcxmjsListByhz(params) {
  return request({
    url: '/oiaudit/project/implementPlan/rwfp/getGcxmjsListByhz',
    method: 'get',
    params: transData(params),
  })
}

//任务分配-建设项目投资完成情况 32
export function getJsxmtzListByhz(params) {
  return request({
    url: '/oiaudit/project/implementPlan/rwfp/getJsxmtzListByhz',
    method: 'get',
    params: transData(params),
  })
}


export function getMyTaskReviewList(params) {
  return request({
    url: '/oiaudit/audit/myTask/Review/getMyTaskReviewList',
    method: 'get',
    params: transData(params),
  })
}

//我的任务-审查--新增/更新
export function myTasksaveOrUpdate(params) {
  return request({
    url: '/oiaudit/audit/myTask/Review/saveOrUpdate',
    method: 'post',
    data: transData(params),
  })
}

//我的任务-审查--删除（直接删除）
export function myTaskdelete(params) {
  return request({
    url: '/oiaudit/audit/myTask/Review/delete',
    method: 'get',
    params: transData(params),
  })
}
//我的任务-审查- 附件-删除（直接删除）
export function myTaskdeleteFileAttach(params) {
  return request({
    url: '/oiaudit/audit/myTask/Review/deleteFileAttach',
    method: 'get',
    params: transData(params),
  })
}

//我的任务-审查--获取审查附件列表
export function myTaskgetIdbyattlist(params) {
  return request({
    url: '/oiaudit/audit/myTask/Review/getIdbyattlist',
    method: 'get',
    params: transData(params),
  })
}

//我的任务-审查--获取审查单个详情信息
export function myTaskgetRecordsById(params) {
  return request({
    url: '/oiaudit/audit/myTask/Review/getRecordsById',
    method: 'get',
    params: transData(params),
  })
}
//审计取证单下发
export function sjqzdXF(params) {
  return request({
    url: '/audit/auditReport/manage/xfreport',
    method: 'get',
    params: transData(params),
  })
}
//审计取证单-获取盖章附件列表
export function getStampedDocumentList(params) {
  return request({
    url: '/audit/nbsjworkSpace/qzd/getStampedDocumentList',
    method: 'get',
    params: transData(params),
  })
}
//审计取证单-盖章附件保存
export function saveStampedDocument(data) {
  return request({
    url: `/audit/nbsjworkSpace/qzd/saveStampedDocument?certificateId=${data.certificateId}&&attids=${data.attids}`,
    method: 'post',
    data: transData(data),
  })
}
//审计取证单-盖章附件删除
export function removeStampedDocument(params) {
  return request({
    url: `/audit/nbsjworkSpace/qzd/removeStampedDocument?certificateId=${params.certificateId}&&attid=${params.attid}`,
    method: 'post',
    data: transData(params),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  })
}
