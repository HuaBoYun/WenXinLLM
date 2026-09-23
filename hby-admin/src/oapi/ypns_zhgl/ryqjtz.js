import request from '@/utils/request'
import { transData } from '@/utils/requestData'

export function getList(data) {
  return request({
    url: '/centralaudit/api-auth/common/user/get-all-list',
    method: 'post',
    headers: {
      'Content-Type': 'application/json',
    },
    data,
  })
}

export function getListTZ(data) {
  return request({
    url: '/centralaudit/api-auth/people/leave/getAllList',
    method: 'post',
    headers: {
      'Content-Type': 'application/json',
    },
    data,
  })
}

export function exportList(data) {
  return request({
    url: '/centralaudit/api-auth/common/user/get-all-list/download-express',
    method: 'post',
    responseType: 'blob',
    data,
    headers: {
      'Content-Type': 'application/json',
    },
  })
}
export function exportListQJ(data) {
  return request({
    url: '/centralaudit/api-auth/people/leave/download-express',
    method: 'post',
    responseType: 'blob',
    headers: {
      'Content-Type': 'application/json',
    },
    data
  })
}