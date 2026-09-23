/**
 * CJBDI 客户A投资外部数据查询 API
 */
import request from '@/utils/request'

/**
 * 获取数据类别列表（按分组）
 */
export function getCjbdiCategories() {
  return request({
    url: '/riskcontrol/cjbdi/categories',
    method: 'get'
  })
}

/**
 * 执行外部数据查询
 * @param {Object} data - { companyId, companyName, creditCode, categoryIds, monitorType }
 * timeout 单独设置 180 秒，外部接口（涉诉/失信等）响应较慢，不使用全局默认超时
 */
export function queryCjbdiData(data) {
  return request({
    url: '/riskcontrol/cjbdi/query',
    method: 'post',
    data: data,
    headers: { 'Content-Type': 'application/json' },
    timeout: 180000
  })
}

/**
 * 获取企业某类别的详细数据
 * @param {Object} params - { companyId, categoryId }
 */
export function getCjbdiDetail(params) {
  return request({
    url: '/riskcontrol/cjbdi/detail',
    method: 'get',
    params: params
  })
}

/**
 * 获取企业查询历史
 * @param {Object} params - { companyId, categoryId? }
 */
export function getCjbdiHistory(params) {
  return request({
    url: '/riskcontrol/cjbdi/history',
    method: 'get',
    params: params
  })
}

/**
 * 企业名录搜索（CJBDI 02接口）
 * @param {Object} data - { keyword, page, size }
 */
export function searchCjbdiEnterprise(data) {
  return request({
    url: '/riskcontrol/cjbdi/search',
    method: 'post',
    data: data,
    headers: { 'Content-Type': 'application/json' }
  })
}

/**
 * 查询企业工商基础信息（CJBDI 03接口）
 * @param {Object} data - { companyName, creditCode }
 */
export function queryCjbdiBusinessInfo(data) {
  return request({
    url: '/riskcontrol/cjbdi/businessInfo',
    method: 'post',
    data: data,
    headers: { 'Content-Type': 'application/json' }
  })
}

/**
 * 身份证两要素校验（CJBDI 01接口）
 * @param {Object} data - { name, idCard }
 */
export function verifyCjbdiIdentity(data) {
  return request({
    url: '/riskcontrol/cjbdi/verification',
    method: 'post',
    data: data,
    headers: { 'Content-Type': 'application/json' }
  })
}

/**
 * 批量身份验证（CJBDI 01接口）
 * @param {Object} data - { list: [{name, idCard}] }
 */
export function batchVerifyCjbdiIdentity(data) {
  return request({
    url: '/riskcontrol/cjbdi/verification/batch',
    method: 'post',
    data: data,
    headers: { 'Content-Type': 'application/json' }
  })
}

/**
 * 个人不良记录查询（CJBDI 23接口）
 * @param {Object} data - { name, idCard }
 */
export function queryBadRecord(data) {
  return request({
    url: '/riskcontrol/cjbdi/badRecord',
    method: 'post',
    data: data,
    headers: { 'Content-Type': 'application/json' }
  })
}

/**
 * 企业纠纷查询（CJBDI 24接口）
 * 单次查询指定案件类型，不循环所有类型
 * @param {Object} data - { company, ajlx }
 */
export function queryDisputeCase(data) {
  return request({
    url: '/riskcontrol/cjbdi/dispute/query',
    method: 'post',
    data: data,
    headers: { 'Content-Type': 'application/json' },
    timeout: 60000
  })
}

/**
 * 添加企业到监控名单（CJBDI 04接口）
 * @param {Object} data - { companyNames: [] }
 */
export function addCjbdiMonitor(data) {
  return request({
    url: '/riskcontrol/cjbdi/monitor/add',
    method: 'post',
    data: data,
    headers: { 'Content-Type': 'application/json' }
  })
}

/**
 * 查询监控名单（CJBDI 05接口）
 * @param {Object} params - { pageIndex, pageSize }
 */
export function queryCjbdiMonitor(params) {
  return request({
    url: '/riskcontrol/cjbdi/monitor/query',
    method: 'get',
    params: params
  })
}

/**
 * 从监控名单中删除企业（CJBDI 06接口）
 * @param {Object} data - { companyNames: [] }
 */
export function deleteCjbdiMonitor(data) {
  return request({
    url: '/riskcontrol/cjbdi/monitor/delete',
    method: 'post',
    data: data,
    headers: { 'Content-Type': 'application/json' }
  })
}

/**
 * 从企业监控列表中删除企业（操作本地数据库）
 * @param {Object} data - { companyNames: [] }
 */
export function deleteCjbdiCompanyMonitor(data) {
  return request({
    url: '/riskcontrol/cjbdi/monitor/deleteCompany',
    method: 'post',
    data: data,
    headers: { 'Content-Type': 'application/json' }
  })
}

/**
 * 获取企业监控信息（CJBDI 07接口）
 * @param {Object} params - { queryDate, pageIndex, pageSize }
 */
export function getCjbdiMonitorCases(params) {
  return request({
    url: '/riskcontrol/cjbdi/monitor/cases',
    method: 'get',
    params: params
  })
}

/**
 * 保存企业监控信息（使用统一社会信用代码作为COMPANYID）
 * @param {Object} data - 企业监控数据
 */
export function saveCjbdiCompanyMonitor(data) {
  return request({
    url: '/riskcontrol/cjbdi/monitor/saveCompany',
    method: 'post',
    data: data,
    headers: { 'Content-Type': 'application/json' }
  })
}

/**
 * 查询企业监控列表（从新表）
 * @param {Object} params - { pageIndex, pageSize }
 */
export function queryCjbdiCompanyList(params) {
  return request({
    url: '/riskcontrol/cjbdi/monitor/queryCompanyList',
    method: 'get',
    params: params
  })
}

/**
 * 获取企业监控分组列表
 * @param {Object} data - { }
 */
export function getCjbdiTeamList(data) {
  return request({
    url: '/contract/riskwarning/main',
    method: 'post',
    data: data
  })
}

