/*
 * @Date: 2022-03-11 14:51:22
 * @LastEditors: 康某 dev@example.com
 * @LastEditTime: 2022-08-27 11:47:42
 * @FilePath: /hb-admin/src/api/contract/manage.js
 */
import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// //OA url
// export function getOaurl(data) {
//   return request({
//     url: '/setting/oaInfo/getOaurl',
//     method: 'post',
//     params: transData(data),
//   })
// }
//OA 列表
export function flowInfolist(data) {
  return request({
    url: '/setting/oaInfo/flowInfoList',
    method: 'post',
    params: transData(data),
  })
}
//OA 编辑
export function modifyFlowInfo(data) {
  return request({
    url: '/setting/oaInfo/modifyFlowInfo',
    method: 'post',
    params: transData(data),
  })
}

//公文/协同 url
export function getOaurl(data) {
  return request({
    url: '/contract/getOaurl',
    method: 'post',
    data: transData(data),
  })
}

//公文/协同
export function getOAList(data) {
  return request({
    url: '/contract/getOAList',
    method: 'post',
    data: transData(data),
  })
}

export function cyhwUnitOAListSave(data) {
  const { contractId, ...other } = data
  return request({
    url: '/contract/cyhwUnitOAListSave?contractId=' + contractId,
    method: 'post',
    data: transData(other),
  })
}
export function getCyhwUnitOAList(data) {
  return request({
    url: '/contract/getCyhwUnitOAList',
    method: 'post',
    data: transData(data),
  })
}
export function remoceOaDocument(data) {
  return request({
    url: '/contract/remoceOaDocument',
    method: 'post',
    params: transData(data),
  })
}
//合合同移交列表
export function getContractTranList(data) {
  return request({
    url: '/contract/contractTranList',
    method: 'post',
    data: transData(data),
  })
}

//合同移交-查询明细
export function getContractTranInfo(data) {
  return request({
    url: '/contract/contractTranInfo',
    method: 'post',
    data: transData(data),
  })
}

//合同移交-新增修改
export function contractTranListSave(data) {
  return request({
    url: '/contract/contractTranListSave',
    method: 'post',
    data: transData(data),
  })
}

//合同移交-选择合同列表（已用印合同）
export function getSealedContractList(data) {
  return request({
    url: '/contract/sealedContractList',
    method: 'post',
    data: transData(data),
  })
}

//合同移交-选择合同列表（已用印合同）
export function getChooseLendContractList(data) {
  return request({
    url: '/contract/chooseLendContractList',
    method: 'get',
    params: transData(data),
  })
}
/**
 * @description: 合同类型-左侧tree
 * @param {*}
 * @return {*}
 */

export function getContractTypeTree(data) {
  return request({
    url: '/contract/contract/contractTypeFather',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 合同类型列表
 * @param {*} data
 * @return {*}
 */
export function getContractTypeList(data) {
  return request({
    url: '/contract/contract/typeOfContractList',
    method: 'post',
    data: transData(data),
  })
}
/**
 * @description: 合同类型-新建
 * @param {*} data
 * @return {*}
 */
export function saveContractType(data) {
  return request({
    url: '/contract/contract/saveContractType',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 合同类型-修改
 * @param {*} data
 * @return {*}
 */
export function updateContractType(data) {
  return request({
    url: '/contract/contract/modifyContractType',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 合同类型-删除
 * @param {*} data
 * @return {*}
 */
export function deleteContractType(data) {
  return request({
    url: '/contract/contract/removeContractType',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 合同范本/合同订立/合同变更-列表
 * @param {*} data
 * @return {*}
 */
export function getContractList(data) {
  return request({
    url: '/contract/cwgl/zcgl_mainck',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 合同订立/合同变更-详情
 * @param {*} data
 * @return {*}
 */
export function getContractItem(data) {
  return request({
    url: '/contract/contractToDetail',
    method: 'post',
    data: transData(data),
    // method: 'get',
    // params: transData(data),
  })
}

/**
 * @description: 合同范本-合同类型options
 * @param {*}
 * @return {*}
 */
export function getContractTypes(data) {
  return request({
    url: '/contract/cyhw/choosetContractType',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description:合同范本/合同订立-新建自动生成编号
 * @param {*}
 * @return {*}
 */
export function generateNo(data) {
  return request({
    url: '/contract/cyhw/cyhwUnitAdd',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 合同范本-提交审批
 * @param {*} data
 * @return {*}
 */
export function approveContractSample(data) {
  return request({
    url: '/contract/cyhwjc/tjsp_zcgl',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 合同范本/合同订立/合同变更-办理信息查看
 * @param {*} data
 * @return {*}
 */
export function viewDealInfo(data) {
  return request({
    url: '/contract/htlsjc/to_sptzgl_info',
    // url: '/contract/yszc/to_sphtjy_info',
    // method: 'post',
    // data: transData(data),
    method: 'get',
    params: transData(data),
  })
}

/**
 * @description: 合同借阅 -办理信息查看
 * @param {*} data
 * @return {*}
 */
export function viewDealInfoByJY(data) {
  return request({
    url: '/contract/yszc/to_sphtjy_info',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 合同范本/合同订立/合同变更-办理流程审批查看
 * @param {*} data
 * @return {*}
 */
export function viewDealProcess(data) {
  return request({
    url: '/contract/cyhwjc/approval_process',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 合同范本/合同订立/合同变更-新建、修改
 * @param {*} data
 * @return {*}
 */
export function saveContract(data) {
  return request({
    url: '/contract/cyhwUnitSave',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 合同范本/合同订立/合同变更-检查状态
 * @param {*}
 * @return {*}
 */

export function checkStatus(data) {
  return request({
    url: '/contract/getCyhwUnitStatue',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 合同变更-撤回-检查状态
 * @param {*}
 * @return {*}
 */

export function checkStatusForRecall(data) {
  return request({
    url: '/contract/getCyhwUnitStatueBG',
    method: 'post',
    data,
  })
}

/**
 * @description: 相对方选项
 * @param {*}
 * @return {*}
 */
export function getXdfOptions(data) {
  return request({
    url: '/contract/findHtInfo/windowOpen',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 银行选项
 * @param {*} data
 * @return {*}
 */
export function getBankOptions(data) {
  return request({
    url: '/contract/choiceCounterPartBankInfo',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 执行单位选项 ---选择公司
 * @param {*} data
 * @return {*}
 */
export function getUnitOptions(data) {
  return request({
    // url: '/contract/organ/csfa_findOrganizationByTree',
    url: '/setting/redisorg/findOrganizationByJTTreeAllGS',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 执行部门选项
 * @param {*} data
 * @return {*}
 */
export function getDepartmentOptions(data) {
  return request({
    url: '/contract/findOrganizationByTreeNbkz',
    method: 'post',
    data: transData(data),
  })
}
/**
 * @description: 执行部门选项
 * @param {*} data
 * @return {*}
 */
export function findOrganizationByTreeAllss(data) {
  return request({
    // url: '/contract/findOrganizationByTreeNbkz',
    // url: '/contract/htdl/findOrganizationByTreeAllss',
    url: '/setting/baseInfo/findOrganizationByTreeAllbm',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 立项信息选项
 * @param {*} data
 * @return {*}
 */
export function getSetupInfoOptions(data) {
  return request({
    url: '/contract/form/chooseFormValueElePage',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 执行人-左侧
 * @param {*} data
 * @return {*}
 */
export function getExecutorOptionsTree(data) {
  return request({
    // url: '/contract/htdl/findOrganizationByTreeAllss',
    url: '/setting/baseInfo/findOrganizationByTreeAllbm',
    method: 'post',
    data: transData(data),
  })
}
/**
 * @description: 执行人-右侧
 * @param {*} data
 * @return {*}
 */
export function getExecutorOptionsList(data) {
  return request({
    url: '/contract/htdl/list',
    method: 'post',
    data: transData(data),
  })
}
/**
 * @description: 合同范本/合同订立-删除
 * @param {*} data
 * @return {*}
 */
export function deleteContract(data) {
  return request({
    url: '/contract/cyhwUnitDelete',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 合同订立/合同变更-提交审批
 * @param {*} data
 * @return {*}
 */
export function approveContract(data) {
  return request({
    url: '/contract/htgl/tjsp_zcgl',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 合同阶段信息-新增/修改
 * @param {*} data
 * @return {*}
 */
export function saveJd(data) {
  return request({
    url: '/contract/contract/plan_toSave',
    method: 'post',
    data: transData(data),
  })
}
/**
 * @description: 合同阶段信息-删除
 * @param {*}
 * @return {*}
 */

export function deleteJd(data) {
  return request({
    url: '/contract/contract/plan_toRemove',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 合同阶段信息-详情
 * @param {*} data
 * @return {*}
 */
export function getJdDetail(data) {
  return request({
    url: '/contract/contract/plan_toModify',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 仓储物品-新增/修改
 * @param {*} data
 * @return {*}
 */
export function saveCc(data) {
  return request({
    url: '/contract/contract/information_toSave',
    method: 'post',
    data: transData(data),
  })
}
/**
 * @description: 仓储物品-删除
 * @param {*}
 * @return {*}
 */

export function deleteCc(data) {
  return request({
    url: '/contract/contract/information_toRemove',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 合同订立/合同变更-定时提醒
 * @param {*} data
 * @return {*}
 */
export function remindContract(data) {
  return request({
    url: '/contract/contract/addTipContract',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 合同用印-列表
 * @param {*}
 * @return {*}
 */
export function getContractSealList(data) {
  return request({
    url: '/contract/constract/contractSeal',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 合同用印
 * @param {*} data
 * @return {*}
 */
export function saveContractSeal(data) {
  return request({
    url: '/contract/projectrBudgetModify',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 合同用印-获取详情(已用印时)
 * @param {*} data
 * @return {*}
 */
export function getContractSealDetail(data) {
  return request({
    url: '/contract/projectrBudgetToModify',
    method: 'post',
    data: transData(data),
  })
}
export function getContractSealDetailForAdd(data) {
  return request({
    url: '/contract/projectrBudgetToAdd',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 合同用印-提交审批
 * @param {*} data
 * @return {*}
 */
export function approveContractSeal(data) {
  return request({
    url: '/contract/submitOppsitePartApproval',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 合同用印-办理信息查看
 * @param {*} data
 * @return {*}
 */
export function viewDealInfoForSeal(data) {
  return request({
    url: '/contract/viewOppsiteProcessInfo',
    method: 'get',
    params: transData(data),
  })
}

/**
 * @description: 合同用印-办理流程
 * @param {*} data
 * @return {*}
 */
export function viewDealProcessForSeal(data) {
  return request({
    url: '/contract/viewOppsiteActiviti',
    method: 'get',
    params: transData(data),
  })
}

/**
 * @description:合同变更-撤回
 * @param {*} data
 * @return {*}
 */
export function recallContractModify(data) {
  return request({
    url: '/contract/cyhw/updBasicUnitinspectionStatue',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 合同归档-列表
 * @param {*}
 * @return {*}
 */
export function getContractArchiveList(data) {
  return request({
    url: '/contract/constract/filContractList',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 合同归档-归档
 * @param {*} data
 * @return {*}
 */
export function archiveContract(data) {
  return request({
    url: '/contract/contract/contract_filStatus',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 合同归档-借阅
 * @param {*} data
 * @return {*}
 */
export function saveBorrowContract(data) {
  return request({
    url: '/contract/contract/saveLeadInfo',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 合同归档-借阅列表
 * @param {*} data
 * @return {*}
 */
export function getBorrowHistory(data) {
  return request({
    url: '/contract/contract/lendContractList',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 合同归档-借阅列表-办理信息查看
 * @param {*} data
 * @return {*}
 */
export function viewDealInfoForBorrow(data) {
  return request({
    url: '/contract/yszc/to_sphtjy_info',
    method: 'post',
    data,
  })
}

/**
 * @description: 合同归档-借阅列表-办理流程审批查看
 * @param {*} data
 * @return {*}
 */
export function viewDealProcessForBorrow(data) {
  return request({
    url: '/contract/yszc/approval_processhtjy',
    method: 'post',
    data,
  })
}

/**
 * @description: 附件删除
 * @param {*} data
 * @return {*}
 */
export function deleteAttach(data) {
  return request({
    url: '/contract/contract/deleteFileRelation',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 合同模板
 * @param {*} data
 * @return {*}
 */
export function getContractTemplates(data) {
  return request({
    url: '/contract/contract/findContractTemplate',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: imformationList
 * @param {*} data
 * @return {*}
 */
export function getSubList(data) {
  return request({
    url: '/contract/contractToInfomationDetail',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: attList
 * @param {*} data
 * @return {*}
 */
export function getAttList(data) {
  return request({
    url: '/contract/contractToAttachmentDetail',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: nodeList
 * @param {*} data
 * @return {*}
 */
export function getNodeList(data) {
  return request({
    url: '/contract/contractToPlanNodeDetail',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 用印附件删除
 * @param {*} data
 * @return {*}
 */
export function deleteSealAttach(data) {
  return request({
    url: '/contract/contract/deleteAttachmentByBedgetId',
    method: 'post',
    data: transData(data),
  })
}

/**
 * 根据合同id获取生成水印文件信息
 * @param {*} data
 * @returns
 */
export function getattInfo(data) {
  return request({
    url: '/contract/getattInfo',
    method: 'post',
    data: transData(data),
  })
}
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

//合同归档查看状态
export function checkLendStatus(params) {
  return request({
    url: '/contract/contract/checkLendStatus',
    method: 'get',
    params: transData(params),
  })
}

//合同归档详细
export function getContractDetail(params) {
  return request({
    url: '/contract/contract/getLeadInfo',
    method: 'get',
    params: transData(params),
  })
}

//下载
export function downloadOldFile(params) {
  return request({
    url: '/contract/downloadOldFile',
    method: 'get',
    params: transData(params),
  })
}

//初始合同 下载
export function downloadOldpdfFile(params) {
  return request({
    url: '/contract/downloadOldpdfFile',
    method: 'get',
    params: transData(params),
  })
}

//预览
export function getPrivewAttInfo(params) {
  return request({
    url: '/setting/filePreview/getPrivewAttInfo',
    method: 'get',
    params: transData(params),
  })
}
// 引迈 -- 我发起的-撤回处理
export const ymWorkActionsWithdraw = (params) => {
  return request({
    url: '/setting/ymWrok/actionsWithdraw',
    method: 'get',
    params: transData(params),
  })
}
// 引迈 -- 办理通过
export const ymWorkAudit = (data) => {
  return request({
    url: '/setting/ymWrok/audit',
    method: 'post',
    data: transData(data),
  })
}
// 引迈 -- 我的待办、我发起的-提交审批
export const ymWorkSubmit = (params) => {
  return request({
    url: '/setting/ymWrok/submit',
    method: 'get',
    params: transData(params),
  })
}
// 引迈 -- 我的待办、我发起的-提交、通过时获取下一步审批节点
export const ymWorkCandidates = (params) => {
  return request({
    url: '/setting/ymWrok/candidates',
    method: 'get',
    params: transData(params),
  })
}

//拒绝 退回列表
export const ymWorkRejectList = (params) => {
  return request({
    url: '/setting/ymWrok/rejectList',
    method: 'post',
    params: transData(params),
  })
}

// 引迈 -- 拒绝 /ymWrok/reject
export const ymWorkReject = (data) => {
  return request({
    url: '/setting/ymWrok/reject',
    method: 'post',
    data: transData(data),
  })
}
// 引迈 --- 转审 /ymWrok/transfer
export const ymWorkTransfer = (data) => {
  return request({
    url: '/setting/ymWrok/transfer',
    method: 'post',
    data: transData(data),
  })
}

//合同订立合同变更 提交审批 前  校验接口
export function checkStageInfo(params) {
  return request({
    url: '/contract/htgl/checkStageInfo',
    method: 'get',
    params: transData(params),
  })
}

export function download(params) {
  return request({
    url: '/contract/downloadFtp/upload',
    method: 'get',
    responseType: 'blob',
    params: transData(params),
  })
}

//查看合同正文PDF
export function contractPdfList(data) {
  return request({
    url: '/contract/contractPdfList',
    method: 'post',
    params: transData(data),
  })
}

//删除合同正文PDF
export function deleteContractPdf(data) {
  return request({
    url: '/contract/deleteContractPdf',
    method: 'post',
    params: transData(data),
  })
}

//下载合同正文PDF
export function downContractPdf(data) {
  return request({
    url: '/contract/downContractPdf',
    method: 'get',
    params: transData(data),
    responseType: 'blob',
  })
}

//用印消息推送
export function pushInfo(data) {
  return request({
    url: '/contract/toSendBudgetMessage',
    method: 'post',
    params: transData(data),
  })
}
//用印生成附件
export function submitYYCreateFile(params) {
  return request({
    url: '/contract/submitYYCreateFile',
    method: 'post',
    data: transData(params),
  })
}

//审批节点获取附件接口
export function fileList(params) {
  return request({
    url: '/setting/ymWrok/fileList',
    method: 'get',
    params: transData(params),
  })
}

//审批节点附件下载接口
export function fildDownload(params) {
  return request({
    url: '/setting/ymWrok/fildDownload',
    method: 'get',
    params: transData(params),
    responseType: 'blob',
  })
}

//审批节点附件删除接口
export function fileRemove(params) {
  return request({
    url: '/setting/ymWrok/fileRemove',
    method: 'get',
    params: transData(params),
  })
}

//知会详情列表
export function informInfoList(params) {
  return request({
    url: '/setting/ymWrok/informInfoList',
    method: 'get',
    params: transData(params),
  })
}

//抄送审批人员
export function copyApprovalStaffList(params) {
  return request({
    url: '/setting/ymWrok/copyApprovalStaffList',
    method: 'get',
    params: transData(params),
  })
}

//流程详细信息
export function getFlowTaskInfo(params) {
  return request({
    url: '/setting/ymWrok/getFlowTaskInfo',
    method: 'get',
    params: transData(params),
  })
}

//发起人催办功能接口
export function press(params) {
  return request({
    url: '/setting/ymWrok/press',
    method: 'get',
    params: transData(params),
  })
}

//发起人登录 获取未处理的催办信息
export function loginGetPress(params) {
  return request({
    url: '/setting/ymWrok/loginGetPress',
    method: 'get',
    params: transData(params),
  })
}

//发起人定时  获取未处理的催办信息
export function getPressInfo(params) {
  return request({
    url: '/setting/ymWrok/getPressInfo',
    method: 'get',
    params: transData(params),
  })
}

//发起人定时获取流程办理结果信息
export function getFlowMessage(params) {
  return request({
    url: '/setting/ymWrok/getFlowMessage',
    method: 'get',
    params: transData(params),
  })
}

//流程审批意见列表
export function getFlowTemplateList(params) {
  return request({
    url: '/setting/ymWrok/getFlowTemplateList',
    method: 'get',
    params: transData(params),
  })
}

//流程审批意见删除
export function removeFlowTemplate(params) {
  return request({
    url: '/setting/ymWrok/removeFlowTemplate',
    method: 'get',
    params: transData(params),
  })
}

//流程审批意见详细
export function getFlowTemplateInfo(params) {
  return request({
    url: '/setting/ymWrok/getFlowTemplateInfo',
    method: 'get',
    params: transData(params),
  })
}

//流程审批意见模板修改接口
export function modifyFlowTemplate(params) {
  return request({
    url: '/setting/ymWrok/modifyFlowTemplate',
    method: 'post',
    data: transData(params),
  })
}

//流程审批意见模板保存接口
export function saveFlowTemplate(params) {
  return request({
    url: '/setting/ymWrok/saveFlowTemplate',
    method: 'post',
    data: transData(params),
  })
}

export function fileSure(params) {
  return request({
    url: '/contract/contract/qrstatus',
    method: 'post',
    data: transData(params),
  })
}

//查看审核合同文本列表
export function contractExamList(params) {
  return request({
    url: '/contract/contractExamList',
    method: 'post',
    data: transData(params),
  })
}

//下载审核合同文本
export function downContractExam(data) {
  return request({
    url: '/contract/downContractExam',
    method: 'get',
    params: transData(data),
    responseType: 'blob',
  })
}
//删除审核合同文本
export function deleteContractExam(data) {
  return request({
    url: '/contract/deleteContractExam',
    method: 'post',
    params: transData(data),
  })
}

//批量审批列表
export function getBatchList(params) {
  return request({
    url: '/setting/ymWrok/batchList',
    method: 'get',
    params: transData(params),
  })
}
//获取流程信息
export function batchCandidate(params) {
  return request({
    url: '/setting/ymWrok/batchCandidate',
    method: 'get',
    params: transData(params),
  })
}
//获取候选人
export function batchCandidateUser(params) {
  return request({
    url: '/setting/ymWrok/batchCandidateUser',
    method: 'post',
    data: transData(params),
  })
}
//提交审批
export function batchOperation(params) {
  return request({
    url: '/setting/ymWrok/batchOperation',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: transData(params),
  })
}
//获取加签人
export function getUserList(params) {
  return request({
    url: '/setting/baseInfo/getUserList',
    method: 'get',
    params: transData(params),
  })
}
