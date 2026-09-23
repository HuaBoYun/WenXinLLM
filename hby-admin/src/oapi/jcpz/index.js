import request from '@/utils/request'
import { transData } from '@/utils/requestData'

//附件下载接口
export function download(params) {
  return request({
    url: '/oiaudit/fileManage/download',
    method: 'get',
    responseType: 'blob',
    params: transData(params),
  })
}

//附件删除接口，不删除中间表关系
export function deleteFile(params) {
  return request({
    url: '/oiaudit/fileManage/delete',
    method: 'get',
    params: transData(params),
  })
}

//附件删除接口，不删除中间表关系
export function deleteReportFile(params) {
  return request({
    url: '/oiaudit/auditReport/manage/report_file_del',
    method: 'get',
    params: transData(params),
  })
}

//基础配置-模板管理-删除
export function TemplateManagerDelete(params) {
  return request({
    url: '/oiaudit/audit/TemplateManager/delete',
    method: 'get',
    params: transData(params),
  })
}

//基础配置-模板管理-附件-删除（直接删除）
export function TemplateManagerDeleteFileAttach(params) {
  return request({
    url: '/oiaudit/audit/TemplateManager/deleteFileAttach',
    method: 'get',
    params: transData(params),
  })
}

//基础配置-模板管理-单个详情
export function TemplateManagerDetail(params) {
  return request({
    url: '/oiaudit/audit/TemplateManager/getTemplateById',
    method: 'get',
    params: transData(params),
  })
}

//基础配置-模板管理-列表查询
export function TemplateManagerList(params) {
  return request({
    url: '/oiaudit/audit/TemplateManager/getRecordsList',
    method: 'get',
    params: transData(params),
  })
}

//基础配置-模板管理-新增修改
export function TemplateManagerSaveOrUpdate(params) {
  return request({
    url: '/oiaudit/audit/TemplateManager/saveOrUpdate',
    method: 'post',
    params: transData(params),
  })
}
