/*
 * @Author: 康某 dev@example.com
 * @Date: 2022-08-23 23:22:05
 * @LastEditors: 康某 dev@example.com
 * @LastEditTime: 2022-12-25 21:12:52
 * @FilePath: \hb-admin\src\api\risk\riskEvents.js
 * @Description: 风险事件模块 api文件
 */
import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// 事件管理 - 左侧树形结构

export const getRiskEventsTree = (params) => {
  return request({
    url: '/riskcontrol/riskEvent/fxsj/query/risk_query_left',
    method: 'get',
    params: transData(params),
  })
}

// 事件管理 -- 列表数据
export const getRiskListById = (params) => {
  return request({
    // url: '/riskcontrol/riskEvent/disposalmanage',
    url: '/riskcontrol/riskEventZh/disposalmanage',
    method: 'get',
    params: transData(params),
  })
}
// 风险事件库 -- 查看风险详情
export const getFxsjkDetails = (params) => {
  return request({
    url: '/riskcontrol/riskEvent/riskevent_info',
    method: 'get',
    params: transData(params),
  })
}
// 风险事件库 -- 索赔信息详情
export const getFxsjkspDetails = (params) => {
  return request({
    url: '/riskcontrol/riskEvent/to_hsxx_info',
    method: 'get',
    params: transData(params),
  })
}
// 风险事件库 -- 新增
export const addFxsjData = (data) => {
  return request({
    url: '/riskcontrol/riskEvent/riskevent_add',
    method: 'post',
    data: transData(data),
  })
}
// 风险事件库 -- 索赔信息
export const addFxsjspData = (data) => {
  return request({
    url: '/riskcontrol/riskEvent/fxxx_spxx_add',
    method: 'post',
    data: transData(data),
  })
}
// 风险事件库 -- 删除类型
export const deletFxsj = (params) => {
  return request({
    url: '/riskcontrol/riskEvent/riskevent_del',
    method: 'post',
    data: transData(params),
  })
}

// 风险事件库 -- 文件下载
export const downFieldById = (params) => {
  return request({
    url: '/riskcontrol/download',
    method: 'post',
    responseType: 'blob',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    params,
  })
}
// 风险事件库 -- 文件删除
export const deleteFieldById = (params) => {
  console.log(params, "ss")
  return request({
    url: '/riskcontrol/delete',
    method: 'post',
    data: transData(params),
  })
}

// 风险热图 -- 左侧列表
export const riskTreeLeft = (params) => {
  return request({
    url: '/riskcontrol/fxxt/fxsb/risk_left',
    method: 'get',
    params: transData(params),
  })
}
// 风险热图 -- 热图列表
export const pgjgRtList = (data) => {
  return request({
    url: '/riskcontrol/fxxt/fxcl/pgjg_list_rt',
    method: 'get',
    params: transData(data),
  })
}
// 风险事件详情
export const riskeventInfo = (data) => {
  return request({
    url: '/riskcontrol/riskEvent/riskevent_info',
    method: 'post',
    data: transData(data),
  })
}

// 发起整改
export const rectifyAdd = (data) => {
  return request({
    url: '/riskcontrol/riskEvent/rectify_add',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: transData(data),
  })
}
// 风险评估 -评估计划自动生成编号
export const createFindAutoNumber = (params) => {
  return request({
    url: '/riskcontrol/nbkz/code/findAutoNumber',
    method: 'get',
    params: transData(params),
  })
}

export const createFindAutoNumberNew = (params) => {
  return request({
    url: '/riskcontrol/pggl/get_riskpgplan_no',
    method: 'get',
    params: transData(params),
  })
}
export const getGroupPlanCode = (params) => {
  return request({
    url: '/riskcontrol/groupPlan/get_riskpgplan_no',
    method: 'get',
    params: transData(params),
  })
}

export const deleteById = (params) => {
  return request({
    url: '/riskcontrol/attachment/deleteById',
    method: 'get',
    params: transData(params),
  })
}

export const getAutoCode = (params) => {
  return request({
    url: '/riskcontrol/review/riskReviewCode',
    method: 'get',
    params: transData(params),
  })
}
//风险数据导出
export const exportRiskEvent = (params) => {
  return request({
    url: '/riskcontrol/riskEventZh/exportRiskEvent',
    method: 'get',
    params: transData(params),
    responseType: 'blob',
  })
}
//风险数据导出
export const exportRiskEvents = (params) => {
  return request({
    url: '/riskcontrol/riskEventZh/exportRiskEvents',
    method: 'get',
    params: transData(params),
    responseType: 'blob',
  })
}

//风险数据上报
export const reportToLeader = (params) => {
  return request({
    url: '/riskcontrol/riskEventZh/reportToLeader',
    method: 'get',
    params: transData(params),
  })
}
//风险数据更新
export const getMaxVersion = (params) => {
  return request({
    url: '/riskcontrol/riskEventZh/getMaxVersion',
    method: 'post',
    data: transData(params),
  })
}
//查看历史版本
export const getViewHistoricalVersions = (params) => {
  return request({
    url: '/riskcontrol/riskEventZh/getViewHistoricalVersions',
    method: 'post',
    data: transData(params),
  })
}

//风险事件查看
export const disposalmanageMain = (params) => {
  return request({
    url: '/riskcontrol/riskEventZh/disposalmanageMain',
    method: 'post',
    data: transData(params),
  })
}
//风险事件查看历史
export const getViewHistoricalVersionsMain = (params) => {
  return request({
    url: '/riskcontrol/riskEventZh/getViewHistoricalVersionsMain   ',
    method: 'post',
    data: transData(params),
  })
}
