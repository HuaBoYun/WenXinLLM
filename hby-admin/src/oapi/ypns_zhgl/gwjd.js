import request from '@/utils/request'
import { transData } from '@/utils/requestData'
import { typeBbj } from '@/oapi/ypns_zhgl/filePublic'

export function getList(data) {
  return request({
    url: '/centralaudit/api-auth/official/receptions/getList',
    method: 'post',
    headers: typeBbj,
    data,
  })
}

export function edit(data) {
  return request({
    url: '/centralaudit/api-auth/official/receptions/saveOrUpdate',
    method: 'post',
    headers: typeBbj,
    data: transData(data),
  })
}
export function getInfoDetail(params) {
  return request({
    url: '/centralaudit/api-auth/official/receptions/' + params.id,
    method: 'get',
    params,
  })
}
export function deleteInfo(params) {
  return request({
    url: '/centralaudit/api-auth/official/receptions/' + params.id,
    method: 'DELETE',
  })
}
