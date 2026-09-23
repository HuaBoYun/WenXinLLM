import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// 列表
export function getList(params) {
  return request({
    url: '/oiaudit/plan/service/requirement/getList',
    method: 'get',
    params: transData(params),
  })
}

export function getAutoNo(params) {
  return request({
    url: '/oiaudit/plan/service/requirement/getAutoNo',
    method: 'get',
    params: transData(params),
  })
}
// 新增修改
export function saveOrUpdate(params) {
  return request({
    url: '/oiaudit/plan/service/requirement/saveOrUpdate',
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
    url: '/oiaudit/plan/service/requirement/detail',
    method: 'get',
    params: transData(params),
  })
}
// 导出
export function exportList(params) {
  return request({
    url: '/oiaudit/plan/service/requirement/export',
    method: 'get',
    responseType: 'blob',
    params: transData(params),
  })
}
// 导入
export function importList(params) {
  return request({
    url: '/oiaudit/plan/service/requirement/import',
    method: 'get',
    params: transData(params),
  })
}
// 删除
export function deleteItem(params) {
  return request({
    url: '/oiaudit/plan/service/requirement/delete',
    method: 'delete',
    params: transData(params),
  })
}
//服务需求表-下发
export function fwxqbXf(data) {
  return request({
    url: '/oiaudit/plan/service/requirement/xf',
    method: 'post',
    headers: {
      "Content-Type": "application/json;charset=utf-8"
    },
    data,
  })
}