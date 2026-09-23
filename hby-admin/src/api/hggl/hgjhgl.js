import request from '@/utils/request'
import { transData } from '@/utils/requestData'
import { method } from 'lodash'


export function getJHGLList(params) {
  return request({
    url: '/hggl/api-auth/im/conference/getList',
    method: 'POST',
    data: transData(params),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  })
}
export function editHGJH(params) {
  return request({
    url: '/hggl/api-auth/im/conference/saveOrUpdate',
    method: 'POST',
    data: transData(params),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  })
}

export function getHGJHDefaultInfo(data) {
  return request({
    url: `/hggl/api-auth/im/conference/${data.id}`,
    method: 'GET',
    params: transData(data),
  })
}

export function deleteFile(params) {
  return request({
    url: `/hggl/api-auth/fileManage/${params.id}`,
    method: 'delete',
  })
}

export function download(params) {
  return request({
    url: `/hggl/api-auth/fileManage/download`,
    method: 'get',
    params,
    responseType: 'blob',
  })
}

export function HGJHDelete(params) {
  return request({
    url: `/hggl/api-auth/im/conference/${params.id}`,
    method: 'delete',
  })
}

export function getHGZRList(params) {
  return request({
    url: '/hggl/api-auth/im/dty/getList',
    method: 'POST',
    data: transData(params),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  })
}

export function editHGZR(params) {
  return request({
    url: '/hggl/api-auth/im/dty/saveOrUpdate',
    method: 'POST',
    data: transData(params),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  })
}

export function getHGZRDefaultInfo(data) {
  return request({
    url: `/hggl/api-auth/im/dty/${data.id}`,
    method: 'GET',
    params: transData(data),
  })
}

export function HGZRDelete(params) {
  return request({
    url: `/hggl/api-auth/im/dty/${params.id}`,
    method: 'delete',
  })
}

export function getXXGLList(params) {
  return request({
    url: '/hggl/api-auth/im/info/getList',
    method: 'POST',
    data: transData(params),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  })
}

export function editXXGL(params) {
  return request({
    url: '/hggl/api-auth/im/info/saveOrUpdate',
    method: 'POST',
    data: transData(params),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  })
}

export function getXXGLDefaultInfo(data) {
  return request({
    url: `/hggl/api-auth/im/info/${data.id}`,
    method: 'GET',
    params: transData(data),
  })
}

export function XXGLDelete(params) {
  return request({
    url: `/hggl/api-auth/im/info/${params.id}`,
    method: 'delete',
  })
}

export function getSCGLList(params) {
  return request({
    url: '/hggl/api-auth/im/manual/getList',
    method: 'POST',
    data: transData(params),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  })
}

export function editSCGL(params) {
  return request({
    url: '/hggl/api-auth/im/manual/saveOrUpdate',
    method: 'POST',
    data: transData(params),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  })
}

export function getSCGLDefaultInfo(data) {
  return request({
    url: `/hggl/api-auth/im/manual/${data.id}`,
    method: 'GET',
    params: transData(data),
  })
}

export function SCGLDelete(params) {
  return request({
    url: `/hggl/api-auth/im/manual/${params.id}`,
    method: 'delete',
  })
}

export function getReportList(params) {
  return request({
    url: '/hggl/api-auth/report/getList',
    method: 'POST',
    data: transData(params),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  })
}

export function editReport(params) {
  return request({
    url: '/hggl/api-auth/report/saveOrUpdate',
    method: 'POST',
    data: transData(params),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  })
}

export function getReportDefaultInfo(data) {
  return request({
    url: `/hggl/api-auth/report/${data.id}`,
    method: 'GET',
    params: transData(data),
  })
}

export function reportDelete(params) {
  return request({
    url: `/hggl/api-auth/report/${params.id}`,
    method: 'delete',
  })
}

//获取部门负责人
export function getOrgMain(params) {
  return request({
    url: `/setting/getOrgMainByOrgid?orgid=${params.orgid}`,
    method: 'post',
    data: transData(params),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  })
}
//导出 合规管理员信息管理
export function exphgglyList(params) {
  return request({
    url: '/hggl/api-auth/im/info/download-express',
    method: 'get',
    params,
    responseType: 'blob'
  })
}
//导出 合规管理员信息管理
export function expzdgwList(params) {
  return request({
    url: '/hggl/api-auth/im/info/download-express',
    method: 'get',
    params,
    responseType: 'blob'
  })
}
//导出 重点岗位合规责任
export function downloadExpress(params) {
  return request({
    url: '/hggl/api-auth/im/dty/download-express',
    method: 'get',
    params,
    responseType: 'blob'
  })
}