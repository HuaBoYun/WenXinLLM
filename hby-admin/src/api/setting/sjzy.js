import request from '@/utils/request'
import { transData } from '@/utils/requestData'


export function sjzyJump(data) {
  return request({
    url: '/setting/guide/getSsoToken',
    method: 'get',
    params: data,
  })
}

export function sjdxTree(data) {
  return request({
    url: '/setting/guide/getGuidanceClassTree',
    method: 'get',
    params: data,
  })
}

export function sjdxList(data) {
  return request({
    url: '/setting/guide/getFilePageInfo',
    method: 'get',
    params: data,
  })
}

export function sjcxffList(data) {
  return request({
    url: '/setting/guide/getFilePageInfo',
    method: 'get',
    params: data,
  })
}

export function sjcxffTree(data) {
  return request({
    url: '/setting/guide/getProcedureClassTree',
    method: 'get',
    params: data,
  })
}
export function fgzdList(data) {
  return request({
    url: '/setting/guide/getFilePageInfo',
    method: 'get',
    params: data,
  })
}

export function fgzdTree(data) {
  return request({
    url: '/setting/guide/getRegulationClassTree',
    method: 'get',
    params: data,
  })
}

export function getGJSUList(data) {
  return request({
    url: '/setting/guide/advancedSearch',
    method: 'get',
    params: data,
  })
}

export function getGJOLDetail(data) {
  return request({
    url: '/setting/guide/getSearchContentUrl',
    method: 'get',
    params: data,
  })
}

export function getWTDXList(data) {
  return request({
    url: '/setting/guide/getQualitativePageInfo',
    method: 'get',
    params: data,
  })
}

export function getWTDXDetail(data) {
  return request({
    url: '/setting/guide/getQualitativeDetail',
    method: 'get',
    params: data,
  })
}
export function exportWord(data) {
  return request({
    url: '/setting/guide/wordPreview',
    method: 'get',
    params: data,
    responseType: 'blob',
  })
}
export function exportExcel(data) {
  return request({
    url: '/setting/guide/excelPreview',
    method: 'get',
    params: data,
    responseType: 'blob',
  })
}
export function exportPdf(data) {
  return request({
    url: '/setting/guide/pdfPreview',
    method: 'get',
    params: data,
    responseType: 'blob',
  })
}
