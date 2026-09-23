/* 项目评优-审计工作质量评估 */

import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// 列表
export function getList(params) {
  return request({
    url: '/centralaudit/api-auth/project/notice/quality/assessment/getList',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: transData(params),
  })
}

// 新增修改
export function saveOrupdate(params) {
  return request({
    url: '/centralaudit/api-auth/project/notice/quality/assessment/saveOrUpdate',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: transData(params),
  })
}

// 详情
export function getItemDetail(params) {
  return request({
    url: `/centralaudit/api-auth/project/notice/quality/assessment/${params.id}`,
    method: 'get',
    params: transData(params),
  })
}
// 删除
export function deleteItem(params) {
  return request({
    url: `/centralaudit/api-auth/project/notice/quality/assessment/${params.id}`,
    method: 'delete',
    params: transData(params),
  })
}