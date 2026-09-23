import { transData } from '@/utils/requestData'
import request from '@/utils/request'

// 前缀/zbgl

// 账簿管理-分页查询
export function getList(params) {

  return request({
    url: '/zbgl/accBookMgr/list',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
    // data: JSON.stringify(params),
  })
}

// 账簿管理-获取当前登录用户选中的账套数据
export function getSelectedBookInfo(params) {

  console.log(params, 'params')
  return request({
    url: '/zbgl/accBookMgr/getSelectedBookInfo',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
    // data: JSON.stringify(params),
  })
}

// 账簿管理-选择年份账套
export function checkBook(params) {

  return request({
    url: '/zbgl/accBookMgr/checkBook?bookId=' + params.bookId,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
    // data: JSON.stringify(params),
  })
}

// 业务数据-获取业务表-左侧
export function showTableList(params) {
  return request({
    url: '/finance/budata/showTableList',
    method: 'get',
    // headers: {
    //   'Content-Type': 'application/json;charset=UTF-8',
    // },
    params: transData(params),
  })
}

// 业务数据-获取表内容数据-右侧
export function showBusinessDataList(params) {
  return request({
    url: '/finance/budata/showBusinessDataList',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: transData(params),
  })
}

// 业务数据-业务数据详情
export function showBusinessDataDetail(params) {
  return request({
    url: '/finance/budata/showBusinessDataDetail',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: transData(params),
  })
}

// 业务数据-下载导入模板
export function downloadTemplate(params) {
  return request({
    url: '/finance/budata/getExportTemplate',
    method: 'post',
    data: params,
  })
}

// 业务数据-导入Excel数据
export function importExcelData(params) {
  return request({
    url: '/finance/budata/importTemplateData',
    method: 'post',
    headers: {
      'Content-Type': 'multipart/form-data',
    },
    data: params,
  })
}

// 业务数据-获取导入记录
export function getImportRecord(params) {
  return request({
    url: '/finance/budata/getImportRecordList',
    method: 'get',
    params: params,
  })
}

// 业务数据-清除导入数据
export function clearImportData(params) {
  return request({
    url: '/finance/budata/removeImportData',
    method: 'get',
    params: params,
  })
}

// 业务数据-获取导入记录详情
export function getImportRecordDetail(params) {
  return request({
    url: '/finance/budata/getImportRecordDetail',
    method: 'get',
    params: params,
  })
}
