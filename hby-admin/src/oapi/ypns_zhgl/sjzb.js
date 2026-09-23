import request from '@/utils/request'
import { transData } from '@/utils/requestData'

export function getList(data) {
  return request({
    url: '/centralaudit/api-auth/weekly/getList',
    method: 'post',
    headers: {
      'Content-Type': 'application/json',
    },
    data,
  })
}

export function editSjzb(data) {
  return request({
    url: '/centralaudit/api-auth/weekly/saveOrUpdate',
    method: 'post',
    headers: {
      'Content-Type': 'application/json',
    },
    data: transData(data),
  })
}
export function getInfoDetail(params) {
  return request({
    url: '/centralaudit/api-auth/weekly/' + params.id,
    method: 'get',
    params,
  })
}
export function deleteInfo(params) {
  return request({
    url: '/centralaudit/api-auth/weekly/' + params.id,
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


export function exportList(data) {
  return request({
    url: `/centralaudit/api-auth/weekly/exportWord?id=${data.id}`,
    method: 'post',
    data,
    responseType: 'blob',
    headers: {
      'Content-Type': 'application/json',
    },
  })
}

