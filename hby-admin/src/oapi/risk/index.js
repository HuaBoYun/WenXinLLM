import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// 风险创建-树
export function getCreationTreeData(data) {
  return request({
    url: '/riskcontrol/risk/risk_left',
    // url: '/riskcontrol/riskEvent/fxsj/query/risk_query_left',
    method: 'get',
    params: transData(data),
  })
}

export function deleteLawRegulations(data) {
  return request({
    url: '/riskcontrol/risk/delete_law_regulations',
    method: 'post',
    data: transData(data),
  })
}

// 风险创建-风险类型判断
export function getCreationTypeJudge(params) {
  return request({
    url: '/riskcontrol/fxxt/fxsb/find_riskcate_bynum',
    method: 'get',
    params: transData(params),
  })
}

// 风险创建-风险类型新建
export function riskTypeToAdd(data) {
  return request({
    url: '/riskcontrol/fxxt/fxsb/risk_type_toAdd',
    method: 'post',
    data: transData(data),
  })
}

// 风险创建-风险类型修改
export function riskEdit(params) {
  return request({
    url: '/riskcontrol/fxxt/fxsb/risk_type_modify',
    method: 'post',
    data: transData(params),
  })
}

// 风险创建-自定义表单获取
export function getDIYForm(params) {
  return request({
    url: '/riskcontrol/nbkz/nkbg/list',
    method: 'get',
    params: transData(params),
  })
}

// 风险创建-新建树节点
export function createTreeDom(params) {
  return request({
    url: '/riskcontrol/fxxt/fxsb/risk_type_add',
    method: 'post',
    data: transData(params),
  })
}

// 风险创建-列表
export function getCreationList(params) {
  return request({
    url: '/riskcontrol/risk/risk_analysis_list',
    method: 'get',
    params: transData(params),
  })
}

//风险基本详情
export function riskAnalysisDetail(params) {
  return request({
    url: '/riskcontrol/risk/risk_analysis_detail',
    method: 'get',
    params: transData(params),
  })
}

// 风险创建-内外规
export function getCreationLawList(params) {
  return request({
    url: '/riskcontrol/risk/risk_analysis_io',
    method: 'get',
    params: transData(params),
  })
}

// 风险创建-流程分类
export function getCreationFlowList(params) {
  return request({
    url: '/riskcontrol/flow/list',
    method: 'get',
    params: transData(params),
  })
}

// 风险创建-删除
export function getCreationDel(params) {
  return request({
    url: '/riskcontrol/risk/risk_analysis_del',
    method: 'post',
    data: transData(params),
  })
}

// 风险创建-删除类型
export function getCreationTypeDel(params) {
  return request({
    url: '/riskcontrol/fxxt/fxsb/risk_type_disp',
    method: 'post',
    data: transData(params),
  })
}

// 风险创建-新建
export function createRisk(params) {
  return request({
    url: '/riskcontrol/risk/risk_analysis_add',
    method: 'post',
    data: transData(params),
  })
}

// 风险创建-导出
export function getCreationExport(params) {
  return request({
    url: '/riskcontrol/greprot/isfile',
    method: 'post',
    data: transData(params),
  })
}

// 风险创建-实际控制人
export function getCreationControlUser(params) {
  return request({
    url: '/riskcontrol/nbkz/user/list',
    method: 'get',
    params: transData(params),
  })
}

// 风险数据库-列表
export function getCreationDataList(params) {
  return request({
    url: '/riskcontrol/risk/risk_fxsjk_list',
    method: 'get',
    params: transData(params),
  })
}

// 风险数据库-导出
export function getCreationDataExport(params) {
  return request({
    url: '/riskcontrol/risk/risk_export',
    method: 'get',
    params: transData(params),
    responseType: 'blob',
  })
}

// 版本管理-列表
export function getCreationVersionList(params) {
  return request({
    url: '/riskcontrol/risk/risk/risk_bbgl_list',
    method: 'post',
    data: transData(params),
  })
}

// 版本管理-历史版本
export function getCreationVersionHistoryList(params) {
  return request({
    url: '/riskcontrol/risk/risk_bbgl_historical_version',
    method: 'get',
    params: transData(params),
  })
}

// 生成报告
export function generateReport(data) {
  return request({
    url: '/riskcontrol/greprot/generateReport',
    method: 'post',
    data: transData(data),
  })
}
// 导出报告
export function download(data) {
  return request({
    url: '/riskcontrol/greprot/download',
    method: 'post',
    data: transData(data),
  })
}
// 风险应对 - 列表
export const fxydList = (data) => {
  return request({
    url: '/riskcontrol/fxxt/fxyd/strategylist',
    method: 'post',
    data: transData(data),
  })
}
// 风险应对 - 保存
export const fxydSave = (data) => {
  return request({
    url: '/riskcontrol/fxxt/fxyd/strategy_reply_update',
    method: 'post',
    data: transData(data),
  })
}
//热力图弹窗的接口
export const riskTzlistGroup = (data) => {
  return request({
    url: '/riskcontrol/fxxt/fxyd/riskTzlistGroup',
    method: 'post',
    data: transData(data),
  })
}
//热力图弹窗的接口(新)
export const getTjfxpgjgList = (data) => {
  return request({
    url: '/riskcontrol/shouye/tjfxpgjgList',
    method: 'post',
    data: transData(data),
  })
}

// 风险点评估任务列表
export const getRiskPointTaskList = (data) => {
  return request({
    url: '/riskcontrol/shouye/getRiskPointTaskList',
    method: 'post',
    data: transData(data),
  })
}

// 获取预警指标数据
export function getYjzbData(data) {
  return request({
    url: '/riskcontrol/controlHome/yjzb',
    method: 'get',
    params: transData(data),
  })
}
// 获取预警指标数据饼状图
export function getYjsyBtData(data) {
  return request({
    url: '/riskcontrol/controlHome/yjsyBt',
    method: 'get',
    params: transData(data),
  })
}
