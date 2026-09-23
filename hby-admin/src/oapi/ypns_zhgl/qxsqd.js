import request from '@/utils/request'
import { transData } from '@/utils/requestData'
import { typeBbj } from '@/oapi/ypns_zhgl/filePublic'

export function getList(data) {
  return request({
    url: '/centralaudit/api-auth/authority/apply/getList',
    method: 'post',
    headers: typeBbj,
    data,
  })
}

export function editInfor(data) {
  return request({
    url: '/centralaudit/api-auth/authority/apply/saveOrUpdate',
    method: 'post',
    headers: typeBbj,
    data: transData(data),
  })
}
export function getInfoDetail(params) {
  return request({
    url: '/centralaudit/api-auth/authority/apply/' + params.id,
    method: 'get',
    params,
  })
}
export function deleteInfo(params) {
  return request({
    url: '/centralaudit/api-auth/authority/apply/' + params.id,
    method: 'DELETE',
  })
}
