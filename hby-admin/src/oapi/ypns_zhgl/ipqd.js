import request from '@/utils/request'
import { transData } from '@/utils/requestData'

export function getList(data) {
  return request({
    url: '/centralaudit/api-auth/ip/inventory/getList',
    method: 'post',
    headers: {
      'Content-Type': 'application/json',
    },
    data,
  })
}

export function editIp(data) {
  return request({
    url: '/centralaudit/api-auth/ip/inventory/saveOrUpdate',
    method: 'post',
    headers: {
      'Content-Type': 'application/json',
    },
    data: transData(data),
  })
}
export function getInfoDetail(params) {
  return request({
    url: '/centralaudit/api-auth/ip/inventory/' + params.id,
    method: 'get',
    params,
  })
}
export function deleteInfo(params) {
  return request({
    url: '/centralaudit/api-auth/ip/inventory/' + params.id,
    method: 'DELETE',
  })
}

export function downloadTemplateFn() {
  return request({
    url: '/centralaudit/api-auth/ip/inventory/template/download-express',
    method: 'get',
    responseType: 'blob',
    isShowAllData: true,
  })
}

export function downloadAssest(data) {
  return request({
    url: '/centralaudit/api-auth/ip/inventory/download-express',
    method: 'post',
    responseType: 'blob',
    isShowAllData: true,
    data,
    headers: {
      'Content-Type': 'application/json',
    },
  })
}

