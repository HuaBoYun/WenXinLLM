import request from '@/utils/request'
import { transData } from '@/utils/requestData'

//修改密码
export function modifyUserPassword(params) {
  return request({
    url: '/setting/user/modifyUserPassWord',
    method: 'post',
    data: transData(params),
  })
}
//计划管理列表
export function planManagePageList(params) {
  return request({
    url: '/audit/audit/planManageController/planManagePageList',
    method: 'get',
    params: transData(params),
  })
}
//审计计划管理列表
export function getAuditPlanPageList(params) {
  return request({
    url: '/audit/auditPlan/getAuditPlanPageList',
    method: 'get',
    params: transData(params),
  })
}
//审计计划管理-自动编号-计划管理
export function getAutoCodeByJhgl(params) {
  return request({
    url: '/audit/auditPlan/getAutoCodeByJhgl',
    method: 'get',
    params: transData(params),
  })
}
export function getPlanProjectListByPlanId(params) {
  return request({
    url: '/audit/auditPlan/getPlanProjectListByPlanId',
    method: 'get',
    params: transData(params),
  })
}
//审计计划管理-根据计划主键查找计划信息
export function getAuditPlanViewDetail(params) {
  return request({
    url: '/audit/auditPlan/getAuditPlanViewDetail',
    method: 'get',
    params: transData(params),
  })
}
//审计计划管理，根据审计计划主键获取审计计划的附件
export function getAuditPlanAttInfo(params) {
  return request({
    url: '/audit/auditPlan/getAuditPlanAttInfo',
    method: 'post',
    data: transData(params),
  })
}
//审计计划管理-根据计划主键删除计划信息
export function deleteAuditPlanByPlanId(params) {
  return request({
    url: '/audit/auditPlan/deleteAuditPlanByPlanId',
    method: 'get',
    params: transData(params),
  })
}
//审计计划中计划项目新增或修改
export function mergePlanProjectManageInfoList(params) {
  return request({
    url: '/audit/auditPlan/mergePlanProjectManageInfo',
    method: 'post',
    data: transData(params),
  })
}
//审计计划上层表单提交
export function mergeAuditPlanInfo(params) {
  return request({
    url: '/audit/auditPlan/mergeAuditPlanInfo',
    method: 'post',
    data: transData(params),
  })
}
//审计计划管理 提交审批
export function submitAuditPlanApproval(params) {
  return request({
    url: '/audit/nbsjapproval/submitAuditPlanApproval',
    method: 'post',
    data: transData(params),
  })
}
//审计计划管理 删除单条
export function removePlanProjectInfo(params) {
  return request({
    url: '/audit/auditPlan/removePlanProjectInfo',
    method: 'post',
    data: transData(params),
  })
}
//审计计划管理-根据附件主键删除附件接口
export function deleteFileById(params) {
  return request({
    url: '/audit/auditPlan/deleteFileById',
    method: 'get',
    params: transData(params),
  })
}
//审计计划查看
export function auditPlanListLook(params) {
  return request({
    url: '/audit/auditPlan/getAuditPlanViewInfo',
    method: 'get',
    params: transData(params),
  })
}
//获取审计计划详情
export function auditPlanViewDetail(params) {
  return request({
    url: '/audit/auditPlan/getAuditPlanViewDetail',
    method: 'get',
    params: transData(params),
  })
}

//新增年度计划
export function createPlan(params) {
  return request({
    url: '/audit/auditPlan/addAutoPlan',
    method: 'post',
    data: transData(params),
  })
}
//审计计划管理-删除决策文件
export function deletetwoFileById(params) {
  return request({
    url: '/audit/auditPlan/deletetwoFileById',
    method: 'get',
    params: transData(params),
  })
}

//导出审计计划项目列表
export function exportPlanProjectListByPlanId(params) {
  return request({
    url: '/audit/auditPlan/exportPlanProjectListByPlanId',
    method: 'post',
    params: transData(params),
    responseType: 'blob',
  })
}