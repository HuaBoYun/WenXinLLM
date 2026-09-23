import request from '@/utils/request'
import { transData } from '@/utils/requestData'

export function getList(data) {
  return request({
    url: '/centralaudit/api-auth/people/leave/getList',
    method: 'post',
    headers: {
      'Content-Type': 'application/json',
    },
    data,
  })
}

export function editInfor(data) {
  return request({
    url: '/centralaudit/api-auth/people/leave/saveOrUpdate',
    method: 'post',
    headers: {
      'Content-Type': 'application/json',
    },
    data: transData(data),
  })
}
export function getInfoDetail(params) {
  return request({
    url: '/centralaudit/api-auth/people/leave/' + params.id,
    method: 'get',
    params,
  })
}
export function deleteInfo(params) {
  return request({
    url: '/centralaudit/api-auth/people/leave/' + params.id,
    method: 'DELETE',
  })
}
export function changeStatus(data) {
  return request({
    url: '/centralaudit/api-auth/common/user/update-on-duty-status',
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: transData(data),
  })
}
