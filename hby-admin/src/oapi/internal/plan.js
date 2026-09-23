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
export function savePlan(params) {
  return request({
    url: '/nkhg/nkcs/plan/save',
    method: 'post',
    data: transData(params),
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
