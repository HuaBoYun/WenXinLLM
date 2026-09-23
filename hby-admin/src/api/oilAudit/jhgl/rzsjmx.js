import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// 列表
export function getList(params) {
  return request({
    url: '/oiaudit/plan/interim/audit/gettdList',
    method: 'get',
    params: transData(params),
  })
}
// 新增修改
export function saveOrUpdate(params) {
  return request({
    url: '/oiaudit/plan/interim/audit/saveOrUpdate',
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
    url: '/oiaudit/plan/interim/audit/detail',
    method: 'get',
    params: transData(params),
  })
}
// 任中审计明细-根据公司id获取项目内容
export function findbyOrgiddetail(params) {
  return request({
    url: '/oiaudit/project/implementPlan/findbyOrgiddetail',
    method: 'get',
    params: transData(params),
  })
}
// 删除
export function deleteItem(params) {
  return request({
    url: '/oiaudit/plan/interim/audit/tbdelete',
    method: 'delete',
    params: transData(params),
  })
}// 导出
export function exportList(params) {
  return request({
    url: '/oiaudit/plan/interim/audit/export',
    method: 'get',
    responseType: 'blob',
    params: transData(params),
  })
}