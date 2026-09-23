/* 审计实施-审计承诺书 */

import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// 列表
export function getList(params) {
  return request({
    url: '/oiaudit/letter/getList',
    method: 'get',
    params: transData(params),
  })
}
// 查询附件列表
export function getattList(params) {
  return request({
    url: '/oiaudit/letter/getattList',
    method: 'get',
    params: transData(params),
  })
}
// 新增修改
export function saveOrupdate(params) {
  return request({
    url: '/oiaudit/letter/saveOrupdate',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: transData(params),
  })
}
// 详情
export function getone(params) {
  return request({
    url: '/oiaudit/letter/getone',
    method: 'get',
    params: transData(params),
  })
}
// 删除审计承诺书
export function deleteone(params) {
  return request({
    url: '/oiaudit/letter/deleteone',
    method: 'post',
    params: transData(params),
  })
}
// 删除底稿附件
export function deleteatt(params) {
  return request({
    url: '/oiaudit/letter/deleteatt',
    method: 'post',
    params: transData(params),
  })
}
