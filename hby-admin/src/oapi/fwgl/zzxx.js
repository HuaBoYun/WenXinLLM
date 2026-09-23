import request from '@/utils/request'
import { transData } from '@/utils/requestData'
export const uploadApi = '/fwgl/api-auth/fileManage/upload'

export function download(params) {
  return request({
    url: '/fwgl/api-auth/fileManage/download',
    method: 'get',
    data: params,
    params: params,
    responseType: 'blob',
  })
}

export function downloads(params) {
  return request({
    url: '/contract/fileManage/download',
    method: 'get',
    data: params,
    params: params,
  })
}

// 法律顾问-列表-删除
export function deleteFile(params) {
  return request({
    url: `/fwgl/api-auth/fileManage/${params.id}`,
    method: 'delete',
  })
}

// 法律顾问-列表-查询
export function getFLGWList(params) {
  return request({
    url: '/fwgl/api-auth/legal/organize/legal/adviser/getList',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
  })
}

//法律顾问-新增/更新
export function addFLGW(params) {
  return request({
    url: '/fwgl/api-auth/legal/organize/legal/adviser/saveOrUpdate',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
  })
}

// 法律顾问-详情
export function getFLGWDefaultInfo(params) {
  return request({
    url: `/fwgl/api-auth/legal/organize/legal/adviser/${params.id}`,
    method: 'get',
  })
}

// 法律顾问-列表-删除
export function deleteFLGWList(params) {
  return request({
    url: `/fwgl/api-auth/legal/organize/legal/adviser/${params.id}`,
    method: 'delete',
  })
}

//法律顾问-工作经历-新增/更新
export function addGZJL(params) {
  return request({
    url: '/fwgl/api-auth/legal/organize/legal/adviser/ext/saveOrUpdate',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
  })
}

// 法律顾问-工作经历-删除
export function deleteGZJLList(params) {
  return request({
    url: `/fwgl/api-auth/legal/organize/legal/adviser/ext/${params.id}`,
    method: 'post',
  })
}

// 法律顾问-列表-查询
export function getFWRYList(params) {
  return request({
    url: '/fwgl/api-auth/legal/organize/legal/personnel/getList',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
  })
}

//法务人员-新增/更新
export function addFWRY(params) {
  return request({
    url: '/fwgl/api-auth/legal/organize/legal/personnel/saveOrUpdate',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
  })
}

//法务人员-工作经历-新增/更新
export function addgfwryzjl(params) {
  return request({
    url: '/fwgl/api-auth/legal/organize/legal/personnel/ext/saveOrUpdate',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
  })
}

// 法务人员-列表-删除
export function deleteFWRYList(params) {
  return request({
    url: `/fwgl/api-auth/legal/organize/legal/personnel/${params.id}`,
    method: 'delete',
  })
}

// 法务人员-详情
export function getFWRYDefaultInfo(params) {
  return request({
    url: `/fwgl/api-auth/legal/organize/legal/personnel/${params.id}`,
    method: 'get',
  })
}

// 法律机关及负责人-列表-查询
export function getFWJGFZRList(params) {
  return request({
    url: '/fwgl/api-auth/legal/organize/legal/organization/getList',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
  })
}

//法律机关及负责人-新增/更新
export function addFWJGFZRL(params) {
  return request({
    url: '/fwgl/api-auth/legal/organize/legal/organization/saveOrUpdate',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
  })
}

// 法律机关及负责人-列表-删除
export function deleteFWJGFZRLList(params) {
  return request({
    url: `/fwgl/api-auth/legal/organize/legal/organization/${params.id}`,
    method: 'delete',
  })
}

// 法律机关及负责人-详情
export function getFWJGFZRLDefaultInfo(params) {
  return request({
    url: `/fwgl/api-auth/legal/organize/legal/organization/${params.id}`,
    method: 'get',
  })
}

// 公司律师-列表-查询
export function getGSLSList(params) {
  return request({
    url: '/fwgl/api-auth/legal/organize/firm/lawyer/getList',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
  })
}

//公司律师-新增/更新
export function addGSLS(params) {
  return request({
    url: '/fwgl/api-auth/legal/organize/firm/lawyer/saveOrUpdate',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
  })
}

// 公司律师-列表-删除
export function deleteGSLSList(params) {
  return request({
    url: `/fwgl/api-auth/legal/organize/firm/lawyer/${params.id}`,
    method: 'delete',
  })
}

// 公司律师-详情
export function getGSLSDefaultInfo(params) {
  return request({
    url: `/fwgl/api-auth/legal/organize/firm/lawyer/${params.id}`,
    method: 'get',
  })
}

// 公司律师-简历-列表-删除
export function deleteJLList(params) {
  return request({
    url: `/fwgl/api-auth/legal/organize/firm/lawyer/ext/${params.id}`,
    method: 'delete',
  })
}

//公司律师-简历-新增/更新
export function addJL(params) {
  return request({
    url: '/fwgl/api-auth/legal/organize/firm/lawyer/ext/saveOrUpdate',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
  })
}

// 法务机构及负责人-年度法律审核情况-列表-删除
export function deletFLSHeList(params) {
  return request({
    url: `/fwgl/api-auth/legal/organize/legal/organization/ext/${params.id}`,
    method: 'delete',
  })
}

//法务机构及负责人-年度法律审核情况-新增/更新
export function addFLSH(params) {
  return request({
    url: '/fwgl/api-auth/legal/organize/legal/organization/ext/saveOrUpdate',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
  })
}

//法律顾问-导出
export function exportFLGW(params) {
  return request({
    url: '/fwgl/api-auth/legal/organize/legal/adviser/download-express',
    method: 'get',
    params,
    responseType: 'blob',
  })
}

//法务机构及负责人-导出
export function exportFWJG(params) {
  return request({
    url: '/fwgl/api-auth/legal/organize/legal/organization/download-express',
    method: 'get',
    params,
    responseType: 'blob',
  })
}

//法务人员-导出
export function exportFWRY(params) {
  return request({
    url: '/fwgl/api-auth/legal/organize/legal/personnel/download-express',
    method: 'get',
    params,
    responseType: 'blob',
  })
}

//公司律师-导出
export function exportGSLS(params) {
  return request({
    url: '/fwgl/api-auth/firm/lawyer/practice/apply/download-express',
    method: 'get',
    params,
    responseType: 'blob',
  })
}

// 人员台账详情
export function rytzpersonnel(params) {
  return request({
    url: `/fwgl/api-auth/firm/lawyer/personnel/${params.id}`,
    method: 'get',
    // responseType: 'blob',
  })
}

// 职业活动-人员详情
export function zyhdDetail(params) {
  return request({
    url: `/fwgl/api-auth/firm/lawyer/personnel/ext/${params.id}`,
    method: 'get',
    // responseType: 'blob',
  })
}

export function downFile(params) {
  return request({
    url: `/fwgl/upload/file/${params.id}`,
    method: 'get',
    // params,
    responseType: 'blob',
  })
}
