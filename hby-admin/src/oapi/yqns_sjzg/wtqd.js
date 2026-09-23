import request from '@/utils/request'
import { transData } from '@/utils/requestData'
import { typeBbj } from '@/oapi/ypns_zhgl/filePublic'

export function getList(params) {
  return request({
    url: '/oiaudit/audit/auditRectify/issue/getAllIssueInfo',
    method: 'get',
    params: transData(params),
  })
}

// 新增、修改数据
export function editInfo(data) {
  return request({
    url: '/oiaudit/audit/auditRectify/issue/saveOrUpdate',
    method: 'post',
    data: transData(data),
    headers: typeBbj,
  })
}

// 新增、修改数据
export function assignment(data) {
  return request({
    url: '/oiaudit/audit/auditRectify/assignment',
    method: 'post',
    data: transData(data),
  })
}

//查询当前选中列表数据
export function getDetailInfo(params) {
  return request({
    url: '/oiaudit/audit/auditRectify/issue/getIssueById',
    method: 'get',
    params: transData(params),
  })
}
// 删除当前选中列表数据
export function deleteInfo(params) {
  return request({
    url: '/oiaudit/audit/auditRectify/issue/delete/' + params.id,
    method: 'get',
    params: transData(params),
  })
}
// 删除附件
export function deleteFileInfo(params) {
  return request({
    url: '/oiaudit/audit/auditRectify/issue/deleteAttachFile',
    method: 'get',
    params: transData(params),
  })
}
// 通过审计报告定稿获取被审计单位
export function getAuditOrgList(params) {
  return request({
    url: '/oiaudit/audit/auditRectify/getAuditOrgList',
    method: 'get',
    params: transData(params),
  })
}

export function exportList(params) {
  return request({
    url: '/oiaudit/audit/auditRectify/issue/export',
    method: 'post',
    data: transData(params),
    responseType: 'blob'
  })
}
