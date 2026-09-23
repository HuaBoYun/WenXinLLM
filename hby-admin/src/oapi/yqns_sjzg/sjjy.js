import request from '@/utils/request'
import { transData } from '@/utils/requestData'
import { typeBbj } from '@/oapi/ypns_zhgl/filePublic'

export function getList(params) {
  return request({
    url: '/oiaudit/audit/auditRectify/propose/allAuditPropose',
    method: 'get',
    params: transData(params),
  })
}

// 新增、修改数据
export function editInfo(data) {
  return request({
    url: '/oiaudit/audit/auditRectify/propose/saveOrUpdate',
    method: 'post',
    data: transData(data),
    headers: typeBbj,
  })
}


// 新增、修改数据
export function saveAuditProposeAdopt(data) {
  return request({
    url: '/oiaudit/audit/auditRectify/propose/saveAuditProposeAdopt',
    method: 'post',
    data: transData(data),
    headers: typeBbj,
  })
}
//查询当前选中列表数据
export function getDetailInfo(params) {
  return request({
    url: '/oiaudit/audit/auditRectify/propose/auditProposeById',
    method: 'get',
    params: transData(params),
  })
}
// 删除当前选中列表数据
export function deleteInfo(params) {
  return request({
    url: '/oiaudit/audit/auditRectify/propose/delete/' + params.id,
    method: 'get',
    params: transData(params),
  })
}
