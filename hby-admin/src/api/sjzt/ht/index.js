import { transData } from '@/utils/requestData'
import request from '@/utils/request'

//导航
export function getSidebar(params) {
  return request({
    url: '/zsgx/htwk/zsk/sidebar',
    method: 'get',
    params,
  })
}

//合同要素库列表
export function getTermsList(data) {
  return request({
    url: '/zsgx/htwk/terms/getTermsList',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: data,
  })
}

//合同模板库列表
export function getTemplateList(data) {
  return request({
    url: '/zsgx/htwk/template/list',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: data,
  })
}

//合同要素库下载
export function templateDownload(params) {
  return request({
    url: '/zsgx/htwk/template/download',
    method: 'get',
    responseType: 'blob',
    params: transData(params),
  })
}

//合同要素库下载 
export function templateZipDownload(params) {
  return request({
    url: '/zsgx/htwk/template/zipDownload',
    method: 'get',
    responseType: 'blob',
    params: transData(params),
  })
}

// 新建合同要素
export function createTerms(data) {
  return request({
    url: '/zsgx/htwk/terms/createTerms',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: data,
  })
}

// 合同要素详情
export function getTermsInfo(params) {
  return request({
    url: `/zsgx/htwk/terms/getTermsInfo?id=${params}`,
    method: 'post',
    headers: {
      'Content-Type': 'application/json',
    }
  })
}

//合同要素库下载
export function termsDownload(params) {
  return request({
    url: '/zsgx/htwk/terms/download',
    method: 'get',
    responseType: 'blob',
    params: transData(params),
  })
}

//合同要素库下载
export function termsZipDownload(params) {
  return request({
    url: '/zsgx/htwk/terms/zipDownload',
    method: 'get',
    responseType: 'blob',
    params: transData(params),
  })
}

//风险清单导航
export function getReviewCheckList(data) {
  return request({
    url: '/zsgx/htwk/review/getReviewCheckList',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: data,
  })
}

//风险清单配置项树形结构
export function getReviewItemTree(params) {
  return request({
    url: '/zsgx/htwk/review/getReviewItemTree',
    method: 'get',
    params,
  })
}

//风险清单配置项详情
export function getReviewItemInfo(params) {
  return request({
    url: '/zsgx/htwk/review/getReviewItemInfo',
    method: 'get',
    params,
  })
}

//合同模板库
export function getCategoryTree(params) {
  return request({
    url: '/zsgx/htwk/template/getCategoryTree',
    method: 'get',
    params,
  })
}

//法律法规列表
export function getLawRegulationList(data) {
  return request({
    url: '/zsgx/htwk/zsk/getLawRegulationList',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: data,
  })
}

//法律法规下载
export function lawRegulationDownload(params) {
  return request({
    url: '/zsgx/htwk/zsk/lawRegulation/download',
    method: 'get',
    responseType: 'blob',
    params: transData(params),
  })
}

//法律法规批量下载
export function lawRegulationZipDownload(params) {
  return request({
    url: '/zsgx/htwk/zsk/lawRegulation/zipDownload',
    method: 'get',
    responseType: 'blob',
    params: transData(params),
  })
}

//法律案例下载
export function judicialCaseDownload(params) {
  return request({
    url: '/zsgx/htwk/zsk/judicialCase/download',
    method: 'get',
    responseType: 'blob',
    params: transData(params),
  })
}

//法律案例批量下载
export function judicialCaseZipDownload(params) {
  return request({
    url: '/zsgx/htwk/zsk/judicialCase/zipDownload',
    method: 'get',
    responseType: 'blob',
    params: transData(params),
  })
}

//法律案例列表
export function getJudicialCaseList(data) {
  return request({
    url: '/zsgx/htwk/zsk/getJudicialCaseList',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: data,
  })
}

//法律实务列表
export function getLegalPracticeList(data) {
  return request({
    url: '/zsgx/htwk/zsk/getLegalPracticeList',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: data,
  })
}

//法律实务下载
export function legalPracticeDownload(params) {
  return request({
    url: '/zsgx/htwk/zsk/legalPractice/download',
    method: 'get',
    responseType: 'blob',
    params: transData(params),
  })
}

//法律实务批量下载
export function legalPracticeZipDownload(params) {
  return request({
    url: '/zsgx/htwk/zsk/legalPractice/zipDownload',
    method: 'get',
    responseType: 'blob',
    params: transData(params),
  })
}



// /zsgx/htwk/review/getReviewCheckList