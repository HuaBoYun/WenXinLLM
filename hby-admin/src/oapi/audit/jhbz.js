import request from '@/utils/request'
import { transData } from '@/utils/requestData'


// 工程结算审计汇总
export function syncConstructionProject(params) {
  return request({
    url: 'oiaudit/audit/settlement/syncConstructionProject',
    method: 'get',
    params: transData(params),
  })
}

// 竣工决算审计汇总
export function syncConstructionProject2(params) {
  return request({
    url: 'oiaudit/audit/completion/syncConstructionProject',
    method: 'get',
    params: transData(params),
  })
}
// 工程结算审计项目汇总列表
export function getSettlementList(params) {
  return request({
    url: 'oiaudit/audit/settlement/getSettlementList',
    method: 'get',
    params: transData(params),
  })
}
// 工程结算审计项目汇总删除
export function deleteSettlement(params) {
  return request({
    url: 'oiaudit/audit/settlement/delete',
    method: 'get',
    params: transData(params),
  })
}
// 新增工程结算审计项目汇总
export function saveOrUpdate(params) {
  return request({
    url: 'oiaudit/audit/settlement/saveOrUpdate',
    method: 'post',
    data: transData(params),
  })
}
// 新增工程结算审计项目汇总导出
export function settlementExportData(params) {
  return request({
    url: 'oiaudit/audit/settlement/exportData',
    method: 'get',
    responseType: 'blob',
    params: transData(params),
  })
}

// 竣工结算审计项目汇总列表
export function getCompletionList(params) {
  return request({
    url: 'oiaudit/audit/completion/getCompletionList',
    method: 'get',
    params: transData(params),
  })
}
// 竣工结算审计项目汇总删除
export function deleteCompletion(params) {
  return request({
    url: 'oiaudit/audit/completion/delete',
    method: 'get',
    params: transData(params),
  })
}

// 竣工结算审计项目汇总导出
export function completionExportData(params) {
  return request({
    url: 'oiaudit/audit/completion/exportData',
    method: 'get',
    responseType: 'blob',
    params: transData(params),
  })
}
// 新增竣工结算审计项目汇总
export function saveCompletion(params) {
  return request({
    url: 'oiaudit/audit/completion/saveOrUpdate',
    method: 'post',
    data: transData(params),
  })
}