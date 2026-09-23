/*
 * @Author: 康某 dev@example.com
 * @Date: 2022-08-22 23:07:59
 * @LastEditors: 康某 dev@example.com
 * @LastEditTime: 2022-08-22 23:26:05
 * @FilePath: \hb-admin\src\api\risk\report.js
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// 风险报告制度 -- 编辑保存

export const saveReportData = (params) => {
  return request({
    url: '/riskcontrol/report/nkbg/modify_save',
    method: 'post',
    data: transData(params),
  })
}

export const addReportData = (params) => {
  return request({
    url: '/riskcontrol/report/isExist',
    method: 'post',
    data: transData(params),
  })
}

//报告主列表
export const getReportList = (params) => {
  return request({
    url: '/riskcontrol/nbkz/nkbg/list',
    method: 'get',
    params: transData(params),
  })
}
//报告主列表
export const getReportList1 = (params) => {
  return request({
    url: '/riskcontrol/nbkz/nkbg/companyList',
    method: 'get',
    params: transData(params),
  })
}
//获取报告详情
export const getDefaultReportInfo = (params) => {
  return request({
    url: '/riskcontrol/nbkz/nkbg/to_bg_info',
    method: 'get',
    params: transData(params),
  })
}
//删除报告
export const deleteReport = (params) => {
  return request({
    url: '/riskcontrol/nbkz/nkbg/delete',
    method: 'get',
    params: transData(params),
  })
}
//风险报告导出
export const download = (params) => {
  return request({
    url: '/riskcontrol/greprot/report/download',
    method: 'get',
    params: transData(params),
  })
}
export function download1(params) {
  return request({
    url: '/riskcontrol/greprot/report/download',
    method: 'get',
    params: transData(params),
  })
}
export function reportSB(params) {
  return request({
    url: '/riskcontrol/nbkz/nkbg/reportToLeader',
    method: 'post',
    data: transData(params),
  })
}



// 催办记录列表
export function getCbTableList(params) {
  return request({
    url: '/setting/v1-refopm/reminder/getList',
    method: 'post',
    data: transData(params),
    headers: {
      'Content-Type': 'application/json',
    },
  })
}
// 催办记录新增
export function saveCBInfo(params) {
  return request({
    url: '/setting/v1-refopm/reminder/saveOrUpdate',
    method: 'post',
    data: transData(params),
    headers: {
      'Content-Type': 'application/json',
    },
  })
}
// 催办记录删除
export function deleteCBList(params) {
  return request({
    url: `/setting/v1-refopm/reminder/${params.id}`,
    method: 'DELETE',
    params: transData(params),
  })
}
// 催办记录详情
export function getCBDetail(params) {
  return request({
    url: `/setting/v1-refopm/reminder/${params.id}`,
    method: 'GET',
    params: transData(params),
  })
}

// 催办  停止
export function stopBList(params) {
  return request({
    url: `/setting/v1-refopm/reminder/is-stop-reminder/${params.id}`,
    method: 'PUT',
    params: transData(params),
  })
}

// 催办  开始
export function startBList(params) {
  return request({
    url: `/setting/v1-refopm/reminder/is-start-reminder/${params.id}`,
    method: 'PUT',
    params: transData(params),
  })
}
// 催办
export function handleCuiBan(params) {
  return request({
    url: `/setting/v1-refopm/reminder/do-reminder/${params.id}`,
    method: 'PUT',
    params: transData(params),
  })
}
// 首页催办列表
export function getHomeCBList(params) {
  return request({
    url: `/setting/v1-ref/reminder/getList`,
    method: 'post',
    data: transData(params),
    headers: {
      'Content-Type': 'application/json',
    },
  })
}
// 首页催办列表
export function getHomeCBListDetail(params) {
  return request({
    url: `/setting/ref/reminder/${params.id}`,
    method: 'get',
    params: transData(params),
  })
}

// 访问记录列表
export function getFWlist(params) {
  return request({
    url: '/setting/v1-ref/reminder/access/getList',
    method: 'post',
    data: transData(params),
    headers: {
      'Content-Type': 'application/json',
    },
  })
}

// 首页催办列表
export function getHomeCBList1(params) {
  return request({
    url: `/setting/v1-ref/reminder/getList`,
    method: 'post',
    data: transData(params),
    headers: {
      'Content-Type': 'application/json',
    },
  })
}

// 首页催办列表
export function getHomeCBListDetail1(params) {
  return request({
    url: `/setting/v1-ref/reminder/${params.id}`,
    method: 'get',
    params: transData(params),
  })
}