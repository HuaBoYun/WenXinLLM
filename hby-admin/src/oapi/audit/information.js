import request from '@/utils/request'
import { transData } from '@/utils/requestData'

//资料收集模块

// 提交资料-删除
export function planDataDelete(params) {
  return request({
    url: '/oiaudit/dataCollection/planData/delete',
    method: 'delete',
    params: transData(params),
  })
}

// 提交资料-单个详情
export function planDataDetail(params) {
  return request({
    url: '/oiaudit/dataCollection/planData/detail',
    method: 'get',
    params: transData(params),
  })
}

// 提交资料-列表查询
export function planDataList(params) {
  return request({
    url: '/oiaudit/dataCollection/planData/getList',
    method: 'get',
    params: transData(params),
  })
}

// 提交资料-新增
export function planDataSaveOrUpdate(data) {
  return request({
    url: '/oiaudit/dataCollection/planData/saveOrUpdate',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=utf-8',
    },
    data: transData(data),
  })
}

// 工程结算设计付款情况统计-删除
export function designPlanInfoDelete(params) {
  return request({
    url: '/oiaudit/dataCollection/designPlanInfo/delete',
    method: 'delete',
    params: transData(params),
  })
}

// 工程结算设计付款情况统计-列表查询
export function designPlanInfoList(params) {
  return request({
    url: '/oiaudit/dataCollection/designPlanInfo/getList',
    method: 'get',
    params: transData(params),
  })
}

// 工程结算设计付款情况统计-单个详情
export function designPlanInfoDetail(params) {
  return request({
    url: '/oiaudit/dataCollection/designPlanInfo/detail',
    method: 'get',
    params: transData(params),
  })
}

// 工程结算设计付款情况统计-新增
export function designPlanInfoSaveOrUpdate(data) {
  return request({
    url: '/oiaudit/dataCollection/designPlanInfo/saveOrUpdate',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=utf-8',
    },
    data: transData(data),
  })
}

// 调查表-删除
export function investigationDelete(params) {
  return request({
    url: '/oiaudit/dataCollection/investigation/delete',
    method: 'delete',
    params: transData(params),
  })
}

// 调查表-列表查询
export function investigationList(params) {
  return request({
    url: '/oiaudit/dataCollection/investigation/getList',
    method: 'get',
    params: transData(params),
  })
}

// 调查表-单个详情
export function investigationDetail(params) {
  return request({
    url: '/oiaudit/dataCollection/investigation/detail',
    method: 'get',
    params: transData(params),
  })
}

// 调查表-新增
export function investigationSaveOrUpdate(data) {
  return request({
    url: '/oiaudit/dataCollection/investigation/saveOrUpdate',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=utf-8',
    },
    data: transData(data),
  })
}

// 工程结算项目信息情况表-删除
export function balanceProjectDelete(params) {
  return request({
    url: '/oiaudit/dataCollection/balanceProject/delete',
    method: 'delete',
    params: transData(params),
  })
}

// 工程结算项目信息情况表-列表查询
export function balanceProjectList(params) {
  return request({
    url: '/oiaudit/dataCollection/balanceProject/getList',
    method: 'get',
    params: transData(params),
  })
}

// 工程结算项目信息情况表-单个详情
export function balanceProjectDetail(params) {
  return request({
    url: '/oiaudit/dataCollection/balanceProject/detail',
    method: 'get',
    params: transData(params),
  })
}

// 工程结算项目信息情况表-新增
export function balanceProjectSaveOrUpdate(data) {
  return request({
    url: '/oiaudit/dataCollection/balanceProject/saveOrUpdate',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=utf-8',
    },
    data: transData(data),
  })
}


// 分包情况调查表-删除
export function subContractDelete(params) {
  return request({
    url: '/oiaudit/dataCollection/subContract/delete',
    method: 'delete',
    params: transData(params),
  })
}

// 分包情况调查表-列表查询
export function subContractList(params) {
  return request({
    url: '/oiaudit/dataCollection/subContract/getList',
    method: 'get',
    params: transData(params),
  })
}

// 分包情况调查表-单个详情
export function subContractDetail(params) {
  return request({
    url: '/oiaudit/dataCollection/subContract/detail',
    method: 'get',
    params: transData(params),
  })
}

// 分包情况调查表-新增
export function subContractSaveOrUpdate(data) {
  return request({
    url: '/oiaudit/dataCollection/subContract/saveOrUpdate',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=utf-8',
    },
    data: transData(data),
  })
}

//附件下载接口
export function download(params) {
  return request({
    url: '/oiaudit/fileManage/download',
    method: 'get',
    responseType: 'blob',
    params: transData(params),
  })
}

//附件删除接口，不删除中间表关系
export function deleteFile(params) {
  return request({
    url: '/oiaudit/fileManage/delete',
    method: 'get',
    params: transData(params),
  })
}

//附件删除接口，不删除中间表关系
export function deleteReportFile(params) {
  return request({
    url: '/oiaudit/auditReport/manage/report_file_del',
    method: 'get',
    params: transData(params),
  })
}
