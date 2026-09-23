import request from '@/utils/request'
import { transData } from '@/utils/requestData'
// 列表
export function getList(data) {
  return request({
    url: '/centralaudit/api-auth/conference/apply/info/getList',
    method: 'post',
    headers: {
      'Content-Type': 'application/json',
    },
    data,
  })
}
// 新增、修改
export function editInfor(data) {
  return request({
    url: '/centralaudit/api-auth/conference/mgt/saveOrUpdate',
    method: 'post',
    headers: {
      'Content-Type': 'application/json',
    },
    data: transData(data),
  })
}
// 查询详情
export function getInfoDetail(params) {
  return request({
    url: '/centralaudit/api-auth/conference/mgt/' + params.id,
    method: 'get',
    params,
  })
}
// 删除列表
export function deleteInfo(params) {
  return request({
    url: '/centralaudit/api-auth/conference/mgt/' + params.id,
    method: 'DELETE',
  })
}
