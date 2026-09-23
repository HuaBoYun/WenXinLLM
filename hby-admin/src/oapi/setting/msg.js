import request from '@/utils/request'
import { transData } from '@/utils/requestData'

//我的传阅 列表（审计）
export function my_circulation(data) {
  return request({
    url: '/setting/my_circulation',
    method: 'post',
    data: transData(data),
  })
}
//我的传阅 列表（合同）
export function my_circulation1(data) {
  return request({
    url: '/setting/ymWrok/alreadyList',
    method: 'get',
    params: transData(data),
  })
}
//我的待办 列表
export function project_plan_list(data) {
  return request({
    url: '/setting/wddb/project_plan',
    method: 'post',
    data,
  })
}
//我的评价 列表
export function mygrade_list(data) {
  return request({
    url: '/setting/wdpj/t08_mygrade_list',
    method: 'post',
    data: transData(data),
  })
}
//规则方案预警 列表
export function yjfa1(data) {
  return request({
    url: '/setting/wdts/yjfa/1',
    method: 'post',
    data: transData(data),
  })
}
//指标方案预警 列表
export function yjfa2(data) {
  return request({
    url: '/setting/wdts/yjfa/2',
    method: 'post',
    data: transData(data),
  })
}
//模型预警 列表
export function yjfa3(data) {
  return request({
    url: '/setting/wdts/yjfa/3',
    method: 'post',
    data: transData(data),
  })
}
//指示预警 列表
export function zbList(data) {
  return request({
    url: '/setting/zb_list',
    method: 'post',
    data: transData(data),
  })
}
//规则预警 列表
export function gzList(data) {
  return request({
    url: '/setting/gz_list',
    method: 'post',
    data: transData(data),
  })
}
//审批计划详情页、我的传阅查看详情
export function totjspPlanInfo(data) {
  return request({
    url: '/setting/jhgl/to_tjspPlan_info',
    method: 'post',
    data: transData(data),
  })
}
//我的待办 跟踪
export function gzlct(data) {
  return request({
    url: '/setting/gz_lct',
    method: 'post',
    data: transData(data),
  })
}

//合同范本-合同起草-合同变更-办理流程
export function blprocessjc(data) {
  return request({
    url: '/contract/cyhw/blprocessjc',
    method: 'post',
    data: transData(data),
  })
}

//相对方-合同用印-办理流程
export function blprocessyszc(data) {
  return request({
    url: '/contract/cyhw/blprocessyszc',
    method: 'post',
    data: transData(data),
  })
}

//收款管理-办理流程
export function blprocesssk(data) {
  return request({
    url: '/contract/skgl/blprocess',
    method: 'post',
    data: transData(data),
  })
}

//fkgl/blprocess
export function blprocessfk(data) {
  return request({
    url: '/contract/fkgl/blprocess',
    method: 'post',
    data: transData(data),
  })
}

//转发接收人
export function scStatus(data) {
  return request({
    url: '/contract/scStatus',
    method: 'post',
    data: transData(data),
  })
}

//获取流程图
export function getImgSrc(params) {
  return request({
    url: '/audit/nbsjapproval/picture',
    method: 'GET',
    headers: { responseType: 'blob' },
    params: transData(params),
  })
}
//合同借阅-办理流程
export function blprocesshtjy(data) {
  return request({
    url: '/contract/cyhw/blprocesshtjy',
    method: 'post',
    data: transData(data),
  })
}
//抄送事宜
export function my_shiyi(params) {
  return request({
    url: '/setting/ymWrok/copyInfoList',
    method: 'GET',
    params: transData(params),
  })
}
//抄送事宜详情
export function getShiYiInfo(params) {
  return request({
    url: '/setting/ymWrok/copyInfoDetail',
    method: 'GET',
    params: transData(params),
  })
}
//我的传阅详情
export function getCYInfo(params) {
  return request({
    url: '/setting/ymWrok/getInfo',
    method: 'GET',
    params: transData(params),
  })
}
//我的传阅撤回
export function handleCYReback(params) {
  return request({
    url: '/setting/ymWrok/alreadyRecall',
    method: 'GET',
    params: transData(params),
  })
}
//小铃铛数量显示
export function getNumShow(params) {
  return request({
    url: '/setting/ymWrok/getWorkCount',
    method: 'GET',
    params: transData(params),
  })
}
//我发起的-详情
export function getFaqiInfo(params) {
  return request({
    url: '/setting/ymWrok/getEditInfo',
    method: 'GET',
    params: transData(params),
  })
}
//我发起的
export function my_faqi(params) {
  return request({
    url: '/setting/ymWrok/flowLaunch',
    method: 'GET',
    params: transData(params),
  })
}
// 我的代办 列表页面 -- 引迈
export const getToDoList = (data) => {
  return request({
    url: '/setting/ymWrok/getList',
    method: 'get',
    params: transData(data),
  })
}
// 我的代办详情
export const getTodoInfo = (data) => {
  return request({
    url: '/setting/ymWrok/getInfo',
    method: 'get',
    params: transData(data),
  })
}
