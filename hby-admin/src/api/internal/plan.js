import request from '@/utils/request'
import { transData } from '@/utils/requestData'

//测试方案-主页
export function ctrltestPlanList(params) {
  return request({
    url: '/nkhg/nkcs/plan/ctrltest_plan_list',
    method: 'get',
    params: transData(params),
  })
}

//测试方案-主页-点击编号/点击修改
export function getPlanDetail(params) {
  return request({
    url: '/nkhg/nkcs/plan/detail',
    method: 'get',
    params: transData(params),
  })
}

//测试方案-启动
export function startPlan(params) {
  return request({
    url: '/nkhg/nkcs/plan/start_isStatus',
    method: 'post',
    data: transData(params),
  })
}

//测试方案-删除
export function deletePLan(params) {
  return request({
    url: '/nkhg/nkcs/plan/delete',
    method: 'post',
    data: transData(params),
  })
}

//测试方案-分配-设置人员-选定
export function savePlanUser(params) {
  return request({
    url: '/nkhg/csfa/saveUser',
    method: 'post',
    data: transData(params),
  })
}

//测试方案-新建-编号生成
export function findAutoNumber(params) {
  return request({
    url: '/nkhg/csfa/code/findAutoNumber',
    method: 'get',
    params: transData(params),
  })
}

//测试方案-新建/修改页面-选择测试模板分页列表
export function defTmplList(params) {
  return request({
    url: '/nkhg/csfa/def_tmpl_list',
    method: 'get',
    params: transData(params),
  })
}

//测试方案-添加页面-保存
export function nkcsSaveOrUpdate(params) {
  return request({
    url: '/nkhg/nkcs/plan/saveOrUpdate',
    method: 'post',
    data: transData(params),
    headers: {
      'Content-Type': 'application/json',
    },
  })
}

//测试方案-修改页面-修改保存
export function updatePlan(params) {
  return request({
    url: '/nkhg/nkcs/plan/modify_save',
    method: 'post',
    data: transData(params),
  })
}

//测试方案-分配-左侧树
export function getLeftTree(params) {
  return request({
    url: '/nkhg/csfa/gettree',
    method: 'get',
    params: transData(params),
  })
}

//测试方案-分配-右侧列表
export function getRighLlist(params) {
  return request({
    url: '/nkhg/csfa/def_list',
    method: 'get',
    params: transData(params),
  })
}

//集团测试计划-主页
export function groupCtrltestPlanList(params) {
  return request({
    url: '/nkhg/groupPlan/ctrltest_plan_list',
    method: 'get',
    params: transData(params),
  })
}
//集团测试计划-新建-编号生成
export function groupFindAutoNumber(params) {
  return request({
    url: '/nkhg/groupCode/findAutoNumber',
    method: 'get',
    params: transData(params),
  })
}
//集团测试计划-删除
export function groupDelete(params) {
  return request({
    url: '/nkhg/groupPlan/delete',
    method: 'POST',
    params: transData(params),
  })
}
//集团测试计划-新增修改页面
export function saveOrUpdate(params) {
  return request({
    url: '/nkhg/groupPlan/saveOrUpdate',
    method: 'POST',
    data: transData(params),
    headers: {
      'Content-Type': 'application/json',
    },
  })
}
//集团测试计划-修改页面-修改保存
export function groupModifySave(params) {
  return request({
    url: '/nkhg/groupPlan/modify_save',
    method: 'POST',
    params: transData(params),
  })
}
//集团测试计划-添加页面-保存
export function groupSave(params) {
  return request({
    url: '/nkhg/groupPlan/save',
    method: 'POST',
    params: transData(params),
  })
}
//集团测试计划-详情页面
export function groupDetail(params) {
  return request({
    url: '/nkhg/groupPlan/detail',
    method: 'get',
    params: transData(params),
  })
}
//集团测试计划-下发
export function toIssued(data) {
  return request({
    url: '/nkhg/toIssued',
    method: 'post',
    params: transData(data),
  })
}
//集团测试计划-附件删除
export function delGroupTestPlanAtt(data) {
  return request({
    url: '/nkhg/delGroupTestPlanAtt',
    method: 'post',
    params: transData(data),
  })
}
//集团跟进结果
export function resultPlanList(data) {
  return request({
    url: '/nkhg/groupPlan/resultPlanList',
    method: 'get',
    params: transData(data),
  })
}
//集团跟进结果-内部列表
export function groupResultCountList(data) {
  return request({
    url: '/nkhg/nkcs/statistic/groupResult_count_list',
    method: 'get',
    params: transData(data),
  })
}

//测试方案-确认下发科室负责人
export function confirmIssuance(data) {
  return request({
    url: '/nkhg/nkcs/plan/confirmIssuance',
    method: 'get',
    params: transData(data),
  })
}
//集团方案列表
export function getGroupPlanList(data) {
  return request({
    url: '/nkhg/nkcs/plan/getGroupPlanList',
    method: 'get',
    params: transData(data),
  })
}
//启动
export function startIssued(data) {
  return request({
    url: '/nkhg/startIssued',
    method: 'post',
    data: transData(data),
  })
}
export function tmplCount(data) {
  return request({
    url: '/nkhg/csfa/tmplCount',
    method: 'get',
    params: transData(data),
  })
}

