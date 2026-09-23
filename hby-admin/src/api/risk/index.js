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

// 风险创建-列表 完成
export function getCreationTestList(params) {
  return request({
    url: '/riskcontrol/risk/testtask_risk_list',
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

//风险基本详情(流程)
export function riskAnalysisDetailFlow(params) {
  return request({
    url: '/riskcontrol/risk/risk_analysis_detail_nr',
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
  console.log("🚀 ~ createRisk ~ params:", params)
  return request({
    url: '/riskcontrol/risk/risk_analysis_add?isflow=1',
    method: 'post',
    headers: {
      'Content-Type': 'application/json',
    },
    data: params,
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

// 风险应对 - 台账
export const fxydListTZ = (data) => {
  return request({
    url: '/riskcontrol/fxxt/fxyd/riskTzlist',
    method: 'post',
    data: transData(data),
  })
}
// 风险应对 - 台账导出
export const exportRiskTzlist = (data) => {
  return request({
    url: '/riskcontrol/fxxt/fxyd/exportRiskTzlist',
    method: 'get',
    params: transData(data),
    responseType: 'blob',
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
// 风险应对-控制措施 - 保存/修改
export const controlSave = (data) => {
  return request({
    url: '/riskcontrol/fxxt/fxyd/strategy_reply_update/update_control_measures',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    method: 'post',
    data: transData(data),
  })
}
// 风险应对-控制措施 - 删除
export const controlDelete = (id) => {
  return request({
    url: `/riskcontrol/fxxt/fxyd/strategy_reply_update/delete_control_measures/${id}`,
    method: 'delete',
  })
}

// 风险应对-获取风险控制点编号
export const getRiskcontrolNo = (data) => {
  return request({
    url: `/riskcontrol/risk/get_riskcontrol_no`,
    method: 'get',
    params: transData(data),
  })
}
// 一体化管控措施更新
export const updateControlResponseplan = (data) => {
  return request({
    url: `/riskcontrol/risk/update_control_Responseplan`,
    method: 'post',
    data: transData(data),
  })
}

//风险识别-风险台帐-列表
export const getRiskLedger = (data) => {
  return request({
    url: `/riskcontrol/risk/getRiskLedger`,
    method: 'get',
    params: transData(data),
  })
}
//风险识别-风险台帐-导出
export const exportRiskLedger = (data) => {
  return request({
    url: `/riskcontrol/risk/exportRiskLedger`,
    method: 'get',
    params: transData(data),
    responseType: 'blob',
  })
}

//集团风险数据库 - 列表查询
export const riskTzlistGroup = (data) => {
  return request({
    url: `/riskcontrol/fxxt/fxyd/riskTzlistGroup`,
    method: 'get',
    params: transData(data),
  })
}
//集团风险数据库 - 导出

export const exportRiskTzlistGroup = (data) => {
  return request({
    url: `/riskcontrol/fxxt/fxyd/exportRiskTzlistGroup`,
    method: 'post',
    data,
    responseType: 'blob',
  })
}

//风险模型库-启用或禁用

export const enableOrDisableRiskModel = (data) => {
  return request({
    url: `/audit/nbsjworkSpace/sjmx/xgStatus`,
    method: 'post',
    data: transData(data),
  })
}

//风险模型库-下发

export const sendRiskModel = (data) => {
  return request({
    url: `/audit/nbsjworkSpace/sjmx/xgry`,
    method: 'post',
    data: transData(data),
  })
}
//风险模型库-取消下发人员

export const cancelSendRiskModel = (data) => {
  return request({
    url: `/audit/nbsjworkSpace/sjmx/deletexgry`,
    method: 'post',
    data: transData(data),
  })
}

//风险模型库-查询下发人员列表

export const querySendRiskModelList = (data) => {
  return request({
    url: `/audit/nbsjworkSpace/sjmx/getxfStafflist`,
    method: 'get',
    params: transData(data),
  })
}
//风险首页--根据下发人查询模型

export const querySendRiskModelListByStaff = (data) => {
  return request({
    url: `/audit/nbsjworkSpace/sjmx/getXflist`,
    method: 'get',
    params: transData(data),
  })
}
