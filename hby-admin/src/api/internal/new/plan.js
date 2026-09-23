import request from '@/utils/request'
import { transData } from '@/utils/requestData'

//检查方案-list
export function ctrltestPlanList(params) {
  return request({
    url: '/hggl/api-auth/inspect/plan/getList',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    method: 'post',
    data: transData(params),
  })
}

//测试方案-主页-点击编号/点击修改
export function getPlanDetail(params) {
  return request({
    url: '/hgglext/nkcs/plan/detail',
    method: 'get',
    params: transData(params),
  })
}

//测试方案-启动
export function startPlan(params) {
  return request({
    url: '/hgglext/nkcs/plan/start_isStatus',
    method: 'post',
    data: transData(params),
  })
}

//测试方案-删除
export function deletePLan(params) {
  return request({
    url: '/hgglext/nkcs/plan/delete',
    method: 'post',
    data: transData(params),
  })
}

//测试方案-分配-设置人员-选定
export function savePlanUser(params) {
  return request({
    url: '/hgglext/csfa/saveUser',
    method: 'post',
    data: transData(params),
  })
}

//测试方案-新建-编号生成
export function findAutoNumber(params) {
  return request({
    url: '/hgglext/csfa/code/findAutoNumber',
    method: 'get',
    params: transData(params),
  })
}

//测试方案-新建/修改页面-选择测试模板分页列表
export function defTmplList(params) {
  return request({
    url: '/hgglext/csfa/def_tmpl_list',
    method: 'get',
    params: transData(params),
  })
}

//测试方案-添加页面-保存
export function savePlan(params) {
  return request({
    url: '/hgglext/nkcs/plan/save',
    method: 'post',
    data: transData(params),
  })
}

//测试方案-修改页面-修改保存
export function updatePlan(params) {
  return request({
    url: '/hgglext/nkcs/plan/modify_save',
    method: 'post',
    data: transData(params),
  })
}

//测试方案-分配-左侧树
export function getLeftTree(params) {
  return request({
    url: '/hgglext/csfa/gettree',
    method: 'get',
    params: transData(params),
  })
}

//测试方案-分配-右侧列表
export function getRighLlist(params) {
  return request({
    url: '/hgglext/csfa/def_list',
    method: 'get',
    params: transData(params),
  })
}

//测试方案-删除
export function deleteImp(params) {
  return request({
    url: '/hggl/api-auth/inspect/imp/' + params.id,
    method: 'delete',
    data: transData(params),
  })
}

//问题整改 刪除
export function deleteRectification(params) {
  return request({
    url: '/hggl/api-auth/inspect/rectification/' + params.id,
    method: 'delete',
    data: transData(params),
  })
}

//合规风险 -删除
export function deleteRisk(params) {
  return request({
    url: '/hggl/api-auth/im/risk/' + params.id,
    method: 'delete',
    data: transData(params),
  })
}

//详细
export function getImpInfo(params) {
  return request({
    url: '/hggl/api-auth/inspect/imp/' + params.id,
    method: 'get',
    params: transData(params),
  })
}

//详细
export function getPlanInfo(params) {
  return request({
    url: '/hggl/api-auth/inspect/plan/' + params.id,
    method: 'get',
    params: transData(params),
  })
}

//检查方案-自动编码
export function getPlanNumber(params) {
  return request({
    url: '/hggl/api-auth/inspect/plan/auto-num',
    method: 'get',
    params: transData(params),
  })
}

//详细
export function getRectificationInfo(params) {
  return request({
    url: '/hggl/api-auth/inspect/rectification/' + params.id,
    method: 'get',
    params: transData(params),
  })
}

//合规风险 详情查询
export function getRiskInfo(params) {
  return request({
    url: '/hggl/api-auth/im/risk/' + params.id,
    method: 'get',
    params: transData(params),
  })
}

//修改新增
export function impSaveOrUpdate(params) {
  return request({
    url: '/hggl/api-auth/inspect/imp/saveOrUpdate',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    method: 'post',
    data: transData(params),
  })
}

//检查实施 list
export function impctrltestPlanList(params) {
  return request({
    url: '/hggl/api-auth/inspect/imp/getList',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    method: 'post',
    data: transData(params),
  })
}

//修改新增
export function planSaveOrUpdate(params) {
  return request({
    url: '/hggl/api-auth/inspect/plan/saveOrUpdate',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    method: 'post',
    data: transData(params),
  })
}

//问题整改 新增/更新
export function rectificationSaveOrUpdate(params) {
  return request({
    url: '/hggl/api-auth/inspect/rectification/saveOrUpdate',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    method: 'post',
    data: transData(params),
  })
}

// 问题整改 列表查询
export function rectificationctrltestPlanList(params) {
  return request({
    url: '/hggl/api-auth/inspect/rectification/getList',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    method: 'post',
    data: transData(params),
  })
}

//合规风险 列表查询
export function riskList(params) {
  return request({
    url: '/hggl/api-auth/im/risk/getList',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    method: 'post',
    data: transData(params),
  })
}

//合规风险 新增/更新
export function riskSaveOrUpdate(params) {
  return request({
    url: '/hggl/api-auth/im/risk/saveOrUpdate',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    method: 'post',
    data: transData(params),
  })
}