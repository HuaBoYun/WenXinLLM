import request from '@/utils/request'
import { transData } from '@/utils/requestData'
import { typeBbj } from '@/oapi/ypns_zhgl/filePublic'

export function getList(params) {
  return request({
    url: '/oiaudit/sjzgtj/list',
    method: 'get',
    params: transData(params),
  })
}

// 新增、修改数据
export function editInfo(data) {
  return request({
    url: '/oiaudit/sjzgtj/saveOrUpdate',
    method: 'post',
    data: transData(data),
    headers: typeBbj,
  })
}

export function getZgtjhzData(data) {
  return request({
    url: '/oiaudit/gzhf/getZgtjhzData',
    method: 'post',
    data: transData(data),
  })
}

export function getStatisticsInfo(params) {
  return request({
    url: '/oiaudit/sjzgtj/getStatisticsInfo',
    method: 'get',
    params: transData(params),
  })
}


//查询当前选中列表数据
export function getDetailInfo(params) {
  return request({
    url: '/oiaudit/sjzgtj/detail',
    method: 'get',
    params: transData(params),
  })
}
// 删除当前选中列表数据
export function deleteInfo(params) {
  return request({
    url: '/oiaudit/sjzgtj/delete',
    method: 'get',
    params: transData(params),
  })
}
// 删除附件
export function deleteFileInfo(params) {
  return request({
    url: '/oiaudit/sjzgtj/deleteZgtjAttach',
    method: 'get',
    params: transData(params),
  })
}
// 删除情况说明附件
export function deleteFileEditInfo(params) {
  return request({
    url: '/oiaudit/sjzgtj/deleteZgqksmAttach',
    method: 'get',
    params: transData(params),
  })
}
