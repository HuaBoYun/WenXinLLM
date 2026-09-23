import request from '@/utils/request'
import { transData } from '@/utils/requestData'
import { typeBbj } from '@/oapi/ypns_zhgl/filePublic'

export function getList(params) {
  return request({
    url: '/oiaudit/hxzg/list',
    method: 'get',
    params: transData(params),
  })
}

export function fillInList(params) {
  return request({
    url: '/oiaudit/hxzg/fillInList',
    method: 'get',
    params: transData(params),
  })
}

export function editInfo(data) {
  return request({
    url: '/oiaudit/hxzg/saveOrUpdate',
    method: 'post',
    headers: typeBbj,
    data: transData(data),
  })
}
//查询当前选中列表数据
export function getDetailInfo(params) {
  return request({
    url: '/oiaudit/hxzg/detail',
    method: 'get',
    params: transData(params),
  })
}
// 删除附件
export function deleteFileInfo(params) {
  return request({
    url: '/oiaudit/hxzg/deleteAttach',
    method: 'get',
    params: transData(params),
  })
}
