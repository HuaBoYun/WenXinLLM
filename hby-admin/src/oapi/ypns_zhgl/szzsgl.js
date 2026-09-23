import request from '@/utils/request'
import { transData } from '@/utils/requestData'

export function getList(data) {
  return request({
    url: '/centralaudit/api-auth/digital/certificate/getList',
    method: 'post',
    headers: {
      'Content-Type': 'application/json',
    },
    data,
  })
}

export function editInfor(data) {
  return request({
    url: '/centralaudit/api-auth/digital/certificate/saveOrUpdate',
    method: 'post',
    headers: {
      'Content-Type': 'application/json',
    },
    data: transData(data),
  })
}
export function getInfoDetail(params) {
  return request({
    url: '/centralaudit/api-auth/digital/certificate/' + params.id,
    method: 'get',
    params,
  })
}
export function deleteInfo(params) {
  return request({
    url: '/centralaudit/api-auth/digital/certificate/' + params.id,
    method: 'DELETE',
  })
}
