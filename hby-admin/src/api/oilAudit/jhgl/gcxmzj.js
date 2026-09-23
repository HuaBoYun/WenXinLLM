import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// 列表
export function getList(params) {
  return request({
    url: '/oiaudit/gcxmzj/list',
    method: 'get',
    params: transData(params),
  })
}

// 列表
export function getChoiceList(params) {
  return request({
    url: '/oiaudit/gcxmzj/getChoiceList',
    method: 'get',
    params: transData(params),
  })
}
// 新增修改
export function saveOrUpdate(params) {
  return request({
    url: '/oiaudit/gcxmzj/saveOrUpdate',
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
    url: '/oiaudit/gcxmzj/detail',
    method: 'get',
    params: transData(params),
  })
}
// 导出
export function exportList(params) {
  return request({
    url: '/oiaudit/gcxmzj/exportData',
    method: 'get',
    responseType: 'blob',
    params: transData(params),
  })
}
// 删除
export function deleteItem(params) {
  return request({
    url: '/oiaudit/gcxmzj/delete',
    method: 'GET',
    params: transData(params),
  })
}
