import request from '@/utils/request'
import { transData } from '@/utils/requestData'

//评价立项-主页查询
export function getResultList(params) {
  return request({
    url: '/nkhg/nbkz/pjgl/t08_proj_task_result',
    method: 'get',
    params: transData(params),
  })
}

//评价结果-生成报告
export function riskGenerateReport(params) {
  return request({
    url: '/nkhg/nbkz/risk_generate_report',
    method: 'get',
    params: transData(params),
  })
}

//评价结果-预览
export function preview(params) {
  return request({
    url: '/nkhg/nbkz/preview',
    method: 'get',
    params: transData(params),
  })
}

//评价结果-主页-点击结果
export function projInitiate_TaskResult(params) {
  return request({
    url: '/nkhg/nbkz/pjgl/t08_proj_initiate_task_result',
    method: 'get',
    params: transData(params),
  })
}

//评价结果-主页列表-点击评价编号(评价结果-评价)
export function projDispGd(params) {
  return request({
    url: '/nkhg/nbkz/pjgl/t08_proj_disp_gd',
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    params: transData(params),
  })
}

//评价结果-评价归档- 评级校正
export function apprResultDisp(params) {
  return request({
    url: '/nkhg/nbkz/pjgl/t08_appr_result_disp',
    method: 'get',
    params: transData(params),
  })
}

//评价归档- 评级校正保存
export function apprResultModife(params) {
  return request({
    url: '/nkhg/nbkz/pjgl/target_modife',
    method: 'post',
    data: transData(params),
  })
}

//评级校正-添加问题-列表
export function questionList(params) {
  return request({
    url: '/nkhg/nbkz/pjgl/question_list_add',
    method: 'get',
    params: transData(params),
  })
}

//评级校正-添加问题列表-选定添加
export function addProblem(params) {
  return request({
    url: '/nkhg/nbkz/pjgl/add_problem',
    method: 'post',
    params: transData(params),
  })
}

//评价结果-计算分数
export function calculate(params) {
  return request({
    url: '/nkhg/nbkz/pjgl/calculate',
    method: 'post',
    params: transData(params),
  })
}

//评价结果-点击机构-要素明细
export function projOrganDiap(params) {
  return request({
    url: '/nkhg/nbkz/pjgl/t08_proj_organ_diap',
    method: 'get',
    params: transData(params),
  })
}

//评价结果-要素明细-导出word
export function expWordFile(params) {
  return request({
    url: '/nkhg/nbkz/pgjg/expWordFile',
    method: 'get',
    params: transData(params),
  })
}

//评价结果-要素明细-导出excel
export function ysmxExport(params) {
  return request({
    url: '/nkhg/nbkz/pjgl/ysmx_export',
    method: 'get',
    params: transData(params),
    responseType: 'blob',
  })
}
//评价结果-要素明细-详细
export function YSInfo(params) {
  return request({
    url: '/nkhg/nbkz/pjgl/info_pjjg',
    method: 'get',
    params: transData(params),
  })
}
