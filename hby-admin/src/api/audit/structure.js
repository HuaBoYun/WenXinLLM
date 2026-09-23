import request from '@/utils/request'
import { transData } from '@/utils/requestData'

//人员评分-列表查询
export function loadEvaluationData(params) {
  return request({
    url: '/audit/audit/Staffscore/getscoreList',
    method: 'get',
    params: transData(params),
  })
}

//选择人员-列表查询
export function loadPersonData(params) {
  return request({
    url: '/audit/audit/Staffscore/getpjuserList',
    method: 'get',
    params: transData(params),
  })
}

//选择项目-列表查询
export function loadProjectData(params) {
  return request({
    url: '/audit/audit/Staffscore/getpjprojectList',
    method: 'get',
    params: transData(params),
  })
}

//新建人员评价
export function createData(params) {
  return request({
    url: '/audit/audit/Staffscore/mergeScoreInfo',
    method: 'post',
    data: transData(params),
  })
}

//删除人员评价
export function deletePersonInfo(params) {
  return request({
    url: '/audit/audit/Staffscore/delScoreInfo',
    method: 'post',
    data: transData(params),
  })
}

//获取人员评价详情
export function getDefaultPersonInfo(params) {
  return request({
    url: '/audit/audit/Staffscore/getScoreDetail',
    method: 'get',
    params: transData(params),
  })
}
//获取办理人员评价详情
export function getBLDefaultPersonInfo(params) {
  return request({
    url: '/audit/nbsjapproval/getStaffScoreApprovalInfo',
    method: 'get',
    params: transData(params),
  })
}

//修改人员评价
export function editPersonData(params) {
  return request({
    url: '/audit/audit/Staffscore/mergeScoreInfo',
    method: 'post',
    data: transData(params),
  })
}

//删除人员管理附件
export function deletePersonFile(params) {
  return request({
    url: '/setting/deleteStaffFileById',
    method: 'post',
    data: transData(params),
  })
}

//获取人员管理附件
export function personFileList(params) {
  return request({
    url: '/setting/getStaffAttInfo',
    method: 'post',
    data: transData(params),
  })
}
//人员评价审批按钮
export function evaluationShenPi(params) {
  return request({
    url: '/audit/nbsjapproval/submitStaffScoreApproval',
    method: 'post',
    data: transData(params),
  })
}
//人员办理详情
export function getBLDetail(params) {
  return request({
    url: '/audit/nbsjapproval/getAuditUserApprovalInfo',
    method: 'get',
    params: transData(params),
  })
}
//人员办理通过或者驳回按钮
export function handlePersonButtonClick(params) {
  return request({
    url: '/audit/nbsjapproval/dealAuditUserApporvalInfo',
    method: 'post',
    data: transData(params),
  })
}
//人员办理查看流程图
export function getPersonImgData(params) {
  return request({
    url: '/audit/nbsjapproval/getAuditUserApprovalView',
    method: 'get',
    params: transData(params),
  })
}
//评价办理通过或者驳回按钮
export function handleEvaluationsButtonClick(params) {
  return request({
    url: '/audit/nbsjapproval/dealStaffScoreApprovalInfo',
    method: 'post',
    data: transData(params),
  })
}
//评价办理查看流程图
export function getEvaluationImgData(params) {
  return request({
    url: '/audit/nbsjapproval/getStaffScoreApprovalView',
    method: 'get',
    params: transData(params),
  })
}
