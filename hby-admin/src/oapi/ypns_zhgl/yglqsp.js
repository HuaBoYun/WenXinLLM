import request from '@/utils/request'
import { transData } from '@/utils/requestData'

export function getList(data) {
  return request({
    url: '/centralaudit/api-auth/li/qing/getList',
    method: 'post',
    headers: {
      'Content-Type': 'application/json',
    },
    data,
  })
}

export function edityglqsp(data) {
  return request({
    url: '/centralaudit/api-auth/li/qing/saveOrUpdate',
    method: 'post',
    headers: {
      'Content-Type': 'application/json',
    },
    data: transData(data),
  })
}
export function getInfoDetail(params) {
  return request({
    url: '/centralaudit/api-auth/li/qing/' + params.id,
    method: 'get',
    params,
  })
}
export function deleteInfo(params) {
  return request({
    url: '/centralaudit/api-auth/li/qing/' + params.id,
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

export function downloadAssest(params) {
  return request({
    url: '/centralaudit/api-auth/ip/inventory/download-express',
    method: 'get',
    responseType: 'blob',
    isShowAllData: true,
    params,
  })
}

