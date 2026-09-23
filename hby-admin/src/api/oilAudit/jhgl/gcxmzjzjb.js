import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// 列表
export function getList(params) {
  return request({
    url: '/oiaudit/gcxmzjzjb/list',
    method: 'get',
    params: transData(params),
  })
}
// 新增修改
export function saveOrUpdate(params) {
  return request({
    url: '/oiaudit/gcxmzjzjb/saveOrUpdate',
    method: 'post',
    // headers: {
    //   'Content-Type': 'application/json;charset=UTF-8',
    // },
    data: transData(params),
  })
}
// 详情
export function detail(params) {
  return request({
    url: '/oiaudit/gcxmzjzjb/detail',
    method: 'get',
    params: transData(params),
  })
}
// 导出
export function exportList(params) {
  return request({
    url: '/oiaudit/gcxmzjzjb/exportData',
    method: 'get',
    params: transData(params),
  })
}
// 导入
export function importList(params) {
  return request({
    url: '/oiaudit/gcxmzjzjb/importData',
    method: 'get',
    params: transData(params),
  })
}
// 删除
export function deleteItem(params) {
  return request({
    url: '/oiaudit/gcxmzjzjb/delete',
    method: 'GET',
    params: transData(params),
  })
}
