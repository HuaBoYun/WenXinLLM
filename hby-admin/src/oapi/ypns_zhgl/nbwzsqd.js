import request from '@/utils/request'
import { transData } from '@/utils/requestData'

export function getList(data) {
  return request({
    url: '/centralaudit/api-auth/internal/website/info/getList',
    method: 'post',
    headers: {
      'Content-Type': 'application/json',
    },
    data,
  })
}

export function editIp(data) {
  return request({
    url: '/centralaudit/api-auth/internal/website/info/saveOrUpdate',
    method: 'post',
    headers: {
      'Content-Type': 'application/json',
    },
    data: transData(data),
  })
}
export function getInfoDetail(params) {
  return request({
    url: '/centralaudit/api-auth/internal/website/info/' + params.id,
    method: 'get',
    params: transData(params),
  })
}
export function deleteInfo(data) {
  return request({
    url: '/centralaudit/api-auth/internal/website/info/' + data.id,
    method: 'DELETE',
    data: transData(data),
  })
}
