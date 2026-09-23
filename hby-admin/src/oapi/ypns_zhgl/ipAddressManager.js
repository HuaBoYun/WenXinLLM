import request from '@/utils/request'
import { transData } from '@/utils/requestData'

export function getList(data) {
  return request({
    url: '/centralaudit/api-auth/ip/manage/info/getList',
    method: 'post',
    headers: {
      'Content-Type': 'application/json',
    },
    data,
  })
}

export function editIp(data) {
  return request({
    url: '/centralaudit/api-auth/ip/manage/info/saveOrUpdate',
    method: 'post',
    headers: {
      'Content-Type': 'application/json',
    },
    data: transData(data),
  })
}
export function getInfoDetail(params) {
  return request({
    url: '/centralaudit/api-auth/ip/manage/info/' + params.id,
    method: 'get',
    params,
  })
}
export function deleteInfo(params) {
  return request({
    url: '/centralaudit/api-auth/ip/manage/info/' + params.id,
    method: 'DELETE',
  })
}
