import request from '@/utils/request'

/**
 * 搜索
 * @param {*} params 
 * @returns 
 */
export function search(params) {
  return request({
    url: '/setting/api/search',
    method: 'get',
    params,
  })
}

/**
 * 招投标
 * @param {*} params 
 * @returns 
 */
export function findbyManage(params) {
  return request({
    url: '/setting/api/findbyManage',
    method: 'get',
    params,
  })
}

/**
 * 债券信息
 * @param {*} params 
 * @returns 
 */
export function zqxxList(params) {
  return request({
    url: '/setting/api/findbyZqxx',
    method: 'get',
    params,
  })
}

/**
 * 招聘信息
 * @param {*} params 
 * @returns 
 */
export function zpxxList(params) {
  return request({
    url: '/setting/api/findbyZP',
    method: 'get',
    params,
  })
}

/**
 * 税务评级
 * @param {*} params 
 * @returns 
 */
export function swpjList(params) {
  return request({
    url: '/setting/api/findbySwpj',
    method: 'get',
    params,
  })
}

/**
 * 抽查检查
 * @param {*} params 
 * @returns 
 */
export function ccjcList(params) {
  return request({
    url: '/setting/api/findbyCcjc',
    method: 'get',
    params,
  })
}

/**
 * 经营异常
 * @param {*} params 
 * @returns 
 */
export function jyycList(params) {
  return request({
    url: '/setting/api/findbyJyyc',
    method: 'get',
    params,
  })
}

/**
 * 行政处罚
 * @param {*} params 
 * @returns 
 */
export function xzcfList(params) {
  return request({
    url: '/setting/api/findbyrisk',
    method: 'get',
    params,
  })
}

/**
 * 行政处罚-信用中国
 * @param {*} params 
 * @returns 
 */
export function xzcfChinaList(params) {
  return request({
    url: '/setting/api/findbyXzcfzg',
    method: 'get',
    params,
  })
}

/**
 * 法律诉讼
 * @param {*} params 
 * @returns 
 */
export function flssList(params) {
  return request({
    url: '/setting/api/findbyFlss',
    method: 'get',
    params,
  })
}

/**
 * 法院公告
 * @param {*} params 
 * @returns 
 */
export function fyggList(params) {
  return request({
    url: '/setting/api/findbyJudirisk',
    method: 'get',
    params,
  })
}

/**
 * 失信人
 * @param {*} params 
 * @returns 
 */
export function sxrList(params) {
  return request({
    url: '/setting/api/findbySxr',
    method: 'get',
    params,
  })
}

/**
 * 被执行人
 * @param {*} params 
 * @returns 
 */
export function bzxrList(params) {
  return request({
    url: '/setting/api/findbyBzxr',
    method: 'get',
    params,
  })
}

/**
 * 开庭公告
 * @param {*} params
 * @returns
 */
export function ktggList(params) {
  return request({
    url: '/setting/api/findbyKtgg',
    method: 'get',
    params,
  })
}

/**
 * 限制高消费
 * @param {*} params
 * @returns
 */
export function xzgxfList(params) {
  return request({
    url: '/setting/api/xzgxf',
    method: 'get',
    params,
  })
}

/**
 * 被执行人
 * @param {*} params
 * @returns
 */
export function hbbzxrList(params) {
  return request({
    url: '/setting/api/hbbzxr',
    method: 'get',
    params,
  })
}

/**
 * 舆情信息
 * @param {*} params
 * @returns
 */
export function yqxxList(params) {
  return request({
    url: '/setting/api/findbyOpein',
    method: 'get',
    params,
  })
}

/**
 * 融资历史
 * @param {*} params 
 * @returns 
 */
export function rzlsList(params) {
  return request({
    url: '/setting/api/findbyRzls',
    method: 'get',
    params,
  })
}

/**
 * 核心团队
 * @param {*} params 
 * @returns 
 */
export function hxtdist(params) {
  return request({
    url: '/setting/api/findbyHxtd',
    method: 'get',
    params,
  })
}

/**
 * 企业业务
 * @param {*} params 
 * @returns 
 */
export function qyywList(params) {
  return request({
    url: '/setting/api/findbyQyxx',
    method: 'get',
    params,
  })
}

/**
 * 投资事件
 * @param {*} params 
 * @returns 
 */
export function tzsjList(params) {
  return request({
    url: '/setting/api/findbyDeve',
    method: 'get',
    params,
  })
}

/**
 * 竞品信息
 * @param {*} params 
 * @returns 
 */
export function jpxxList(params) {
  return request({
    url: '/setting/api/findbyJpxx',
    method: 'get',
    params,
  })
}

/**
 * 企业画像
 * @param {*} params 
 * @returns 
 */
export function qyhxList(params) {
  return request({
    url: '/setting/api/qyhx',
    method: 'get',
    params,
  })
}