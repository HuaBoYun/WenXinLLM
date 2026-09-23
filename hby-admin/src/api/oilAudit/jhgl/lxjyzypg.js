import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// 列表
export function getList(params) {
  return request({
    url: '/oiaudit/plan/project/evaluation/gettdList',
    method: 'get',
    params: transData(params),
  })
}
// 新增修改
export function saveOrUpdate(params) {
  return request({
    url: '/oiaudit/plan/project/evaluation/saveOrUpdate?attids=' + params.attids,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: transData(params),
  })
}
// 详情
export function detail(params) {
  return request({
    url: '/oiaudit/plan/project/evaluation/detail',
    method: 'get',
    params: transData(params),
  })
}
// 导出
export function exportList(params) {
  return request({
    url: '/oiaudit/plan/project/evaluation/export',
    method: 'get',
    responseType: 'blob',
    params: transData(params),
  })
}
// 导入
export function importFile(params) {
  return request({
    url: '/oiaudit/plan/project/evaluation/import',
    method: 'post',
    data: transData(params),
  })
}
// 删除
export function deleteItem(params) {
  return request({
    url: '/oiaudit/plan/project/evaluation/tbdelete',
    method: 'DELETE',
    params: transData(params),
  })
}

export function getPlanData(params) {
  return request({
    url: '/oiaudit/plan/project/suggestion/getList',
    method: 'get',
    params: transData(params),
  })
}
