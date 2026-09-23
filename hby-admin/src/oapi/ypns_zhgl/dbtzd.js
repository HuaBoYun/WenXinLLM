import request from '@/utils/request'
// import { transData } from '@/utils/requestData'

export function getList(data) {
  return request({
    url: '/centralaudit/api-auth/supervision/notice/getList',
    method: 'post',
    headers: {
      'Content-Type': 'application/json',
    },
    data,
  })
}
export function getListBL(data) {
  return request({
    url: '/centralaudit/api-auth/supervision/notice/getBlList',
    method: 'post',
    headers: {
      'Content-Type': 'application/json',
    },
    data,
  })
}
export function tijiao(data) {
  return request({
    url: '/centralaudit/api-auth/supervision/notice/saveBlSubmit',
    method: 'post',
    headers: {
      'Content-Type': 'application/json',
    },
    data,
  })
}

export function editInfor(data) {
  return request({
    url: '/centralaudit/api-auth/supervision/notice/saveOrUpdate',
    method: 'post',
    headers: {
      'Content-Type': 'application/json',
    },
    data,
  })
}
export function getInfoDetail(params) {
  return request({
    url: '/centralaudit/api-auth/supervision/notice/' + params.id,
    method: 'get',
    params,
  })
}
export function deleteInfo(params) {
  return request({
    url: '/centralaudit/api-auth/supervision/notice/' + params.id,
    method: 'DELETE',
  })
}
export function exportList(data) {
  return request({
    url: '/centralaudit/api-auth/supervision/notice/download-express',
    method: 'post',
    responseType: 'blob',
    headers: {
      'Content-Type': 'application/json',
    },
    data
  })
}
