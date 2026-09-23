import request from '@/utils/request'
import { transData } from '@/utils/requestData'
import { typeBbj } from '@/oapi/ypns_zhgl/filePublic'

export function getList(params) {
  return request({
    url: '/oiaudit/wtzg/list',
    method: 'get',
    params: transData(params),
  })
}

// 新增、修改数据
export function editInfo(data) {
  return request({
    url: '/oiaudit/wtzg/saveOrUpdate',
    method: 'post',
    data: transData(data),
    headers: typeBbj,
  })
}

//查询当前选中列表数据
export function getDetailInfor(params) {
  return request({
    url: '/oiaudit/wtzg/detail',
    method: 'get',
    params: transData(params),
  })
}
// 删除当前选中列表数据
export function deleteInfo(params) {
  return request({
    url: '/oiaudit/wtzg/delete',
    method: 'get',
    params: transData(params),
  })
}
// 删除附件
export function deleteFileInfo(params) {
  return request({
    url: '/oiaudit/wtzg/deleteAttach',
    method: 'get',
    params: transData(params),
  })
}

export function deleteYsAttach(params) {
  return request({
    url: '/oiaudit/wtzg/deleteYsAttach',
    method: 'get',
    params: transData(params),
  })
}
