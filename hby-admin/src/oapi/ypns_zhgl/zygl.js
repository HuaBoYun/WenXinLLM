import request from '@/utils/request'
import { transData } from '@/utils/requestData'
import { typeBbj } from '@/oapi/ypns_zhgl/filePublic'

export function getList(data) {
  return request({
    url: '/centralaudit/api-auth/asset/mgt/getList',
    method: 'post',
    headers: typeBbj,
    data,
  })
}

export function editInfor(data) {
  return request({
    url: '/centralaudit/api-auth/asset/mgt/saveOrUpdate',
    method: 'post',
    headers: typeBbj,
    data: transData(data),
  })
}
export function getInfoDetail(params) {
  return request({
    url: '/centralaudit/api-auth/asset/mgt/' + params.id,
    method: 'get',
    params,
  })
}
export function deleteInfo(params) {
  return request({
    url: '/centralaudit/api-auth/asset/mgt/' + params.id,
    method: 'DELETE',
  })
}
export function downloadTemplateFn() {
  return request({
    url: '/centralaudit/api-auth/asset/mgt/template/download-express',
    method: 'get',
    responseType: 'blob',
    isShowAllData: true,
  })
}
export function downloadAssest(data) {
  return request({
    url: '/centralaudit/api-auth/asset/mgt/download-express',
    method: 'post',
    responseType: 'blob',
    isShowAllData: true,
    data,
    headers: {
      'Content-Type': 'application/json',
    },
  })
}
