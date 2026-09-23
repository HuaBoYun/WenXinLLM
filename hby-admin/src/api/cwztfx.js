import request from '@/utils/request'
import { transData } from '@/utils/requestData'

export function getYHData(params) {
  return request({
    url: '/audit/auditProject/sjfx/sjxmqk',
    method: 'get',
    params,
  })
}
export function getSJXMData(params) {
  return request({
    url: '/audit/auditProject/plancount',
    method: 'get',
    params,
  })
}
export function getXMZTData(params) {
  return request({
    url: '/audit/auditProject/projectZfcount',
    method: 'get',
    params,
  })
}
export function getBNMYData(params) {
  return request({
    url: '/audit/auditProject/projectYfcount',
    method: 'get',
    params,
  })
}

export function getXMLXData(params) {
  return request({
    url: '/audit/auditProject/sjfx/sjlxxm',
    method: 'get',
    params,
  })
}

export function doDelete(data) {
  return request({
    url: '/departmentManagement/doDelete',
    method: 'post',
    data,
  })
}

// 项目运行情况
export function getProjectRunData(params) {
  return request({
    url: '/oiaudit/analysisReport/projectYxStageCount',
    method: 'get',
    params,
  })
}
// 人员情况
export function getPersonnelData(params) {
  return request({
    url: '/oiaudit/analysisReport/staffStateCount',
    method: 'get',
    params,
  })
}

// 其他数据
export function getOtherData(params) {
  return request({
    url: '/oiaudit/analysisReport/commandPlanData',
    method: 'get',
    params,
  })
}

// 项目运行情况表弹窗数据
export function getProjectRunDataModal(params) {
  return request({
    url: '/oiaudit/operate/gethzList',
    method: 'get',
    params,
  })
}

// 项目运行情况完成选项表弹窗数据
export function getProjectRunDataModalWan(params) {
  return request({
    url: '/oiaudit/operate/gethzListWan',
    method: 'get',
    params,
  })
}

export function getSummary(params) {
  return request({
    url: '/audit/auditControlAnalysis/indicator/analysis_summary',
    method: 'get',
    params,
  })
}

// 获取审计项目数详细数据
export function getSJXMDataDetail(params) {
  return request({
    url: '/audit/auditProject/plancount/detail',
    method: 'get',
    params,
  })
}

// 获取审计项目类型详细数据
export function getAuditTypeDataDetail(params) {
  return request({
    url: '/audit/auditProject/sjfx/sjlxxm/detail',
    method: 'get',
    params,
  })
}
