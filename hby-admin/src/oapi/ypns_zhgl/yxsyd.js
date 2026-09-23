import request from '@/utils/request'
import { transData } from '@/utils/requestData'

export function getList(data) {
  return request({
    url: '/centralaudit/api-auth/seal/form/getList',
    method: 'post',
    headers: {
      'Content-Type': 'application/json',
    },
    data,
  })
}

export function editInfor(data) {
  return request({
    url: '/centralaudit/api-auth/seal/form/saveOrUpdate',
    method: 'post',
    headers: {
      'Content-Type': 'application/json',
    },
    data: transData(data),
  })
}
export function getInfoDetail(params) {
  return request({
    url: '/centralaudit/api-auth/seal/form/' + params.id,
    method: 'get',
    params,
  })
}
export function deleteInfo(params) {
  return request({
    url: '/centralaudit/api-auth/seal/form/' + params.id,
    method: 'DELETE',
  })
}


export function getYXSYTZList(data) {
  return request({
    url: '/centralaudit/api-auth/seal/form/getAllList',
    method: 'post',
    headers: {
      'Content-Type': 'application/json',
    },
    data,
  })
}
export function YXSYTZListUp(data) {
  return request({
    url: `/centralaudit/api-auth/seal/form/getAllList/moveUp/${data.id}`,
    method: 'post',
    headers: {
      'Content-Type': 'application/json',
    },
    data,
  })
}
export function YXSYTZListDown(data) {
  return request({
    url: `/centralaudit/api-auth/seal/form/getAllList/moveDown/${data.id}`,
    method: 'post',
    headers: {
      'Content-Type': 'application/json',
    },
    data,
  })
}
export function loginGetNewspaper(data) {
  // 使用axios直接调用，绕过全局request拦截器
  const axios = require('axios')
  return axios({
    url: `/centralaudit/api-auth/weekly/remind`,
    method: 'post',
    headers: {
      'Content-Type': 'application/json',
    },
    data: transData(data),
  })
}
export function exportYZSYTZ(data) {
  return request({
    url: `/centralaudit/api-auth/seal/form/download-express`,
    method: 'post',
    responseType: 'blob',
    headers: {
      'Content-Type': 'application/json',
    },
    data,
  })
}