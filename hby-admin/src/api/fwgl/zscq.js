import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// 登记管理-列表-查询
export function getDJGLList(params) {
  return request({
    url: '/fwgl/api-auth/intellectual/property/getList',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
  })
}

//登记管理-新增/更新
export function addDJGL(params) {
  return request({
    url: '/fwgl/api-auth/intellectual/property/saveOrUpdate',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
  })
}

// 登记管理-详情
export function getDJGLDefaultInfo(params) {
  return request({
    url: `/fwgl/api-auth/intellectual/property/${params.id}`,
    method: 'get',
  })
}

// 登记管理-列表-删除
export function deleteDJGLList(params) {
  return request({
    url: `/fwgl/api-auth/intellectual/property/${params.id}`,
    method: 'delete',
  })
}

// 台账管理-列表-查询
export function getTZGLList(params) {
  return request({
    url: '/fwgl/api-auth/intellectual/property/get-all',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
  })
}

//登记管理-导出
export function exportDJGL(params) {
  return request({
    url: '/fwgl/api-auth/intellectual/property/register/download-express',
    method: 'get',
    params,
    responseType: 'blob',
  })
}

//台账管理-导出
export function exportTZGL(params) {
  return request({
    url: '/fwgl/api-auth/intellectual/property/account/download-express',
    method: 'get',
    params,
    responseType: 'blob',
  })
}
