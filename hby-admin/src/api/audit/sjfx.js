import request from '@/utils/request'
import { transData } from '@/utils/requestData'

//各公司审计项目数
export function entryNumber(params) {
  return request({
    url: '/audit/auditProject/sjfx/ggsxm',
    method: 'get',
    params: transData(params),
  })
}

//审计类型项目
export function projectType(params) {
  return request({
    url: '/audit/auditProject/sjfx/sjlxxm',
    method: 'get',
    params: transData(params),
  })
}

//审计项目情况表
export function projectSituation(params) {
  return request({
    url: '/audit/auditProject/sjfx/sjxmqk',
    method: 'get',
    params: transData(params),
  })
}

//查询条件年度查询
export function yearInquiry(params) {
  return request({
    url: '/audit/auditProject/sjfx/allyear',
    method: 'get',
    params: transData(params),
  })
}
//统计情况表保存
export function saveSJQKTJB(params) {
  return request({
    url: '/audit/nbsjworkSpace/qzd/situationInsertOrUpdate',
    method: 'POST',
    data: transData(params),
    // headers: {
    //   'Content-Type': 'application/json;charset=UTF-8',
    // },//改变传参格式
  })
}
//统计情况表列表
export function getSJQKTJBList(params) {
  return request({
    url: '/audit/nbsjworkSpace/qzd/getNbsjSituationListPage',
    method: 'POST',
    data: transData(params),
  })
}

//统计情况表列表删除
export function sjqktjbDelete(params) {
  return request({
    url: '/audit/nbsjworkSpace/qzd/situation_del',
    method: 'GET',
    params: transData(params),
  })
}
//统计情况表列表详情
export function sjqktjbDetail(params) {
  return request({
    url: '/audit/nbsjworkSpace/qzd/situation_details',
    method: 'GET',
    params: transData(params),
  })
}
//审计对象库保存
export function saveOBJ(params) {
  return request({
    url: '/audit/nbsjworkSpace/auditObjectLibraryAddOrUpdate',
    method: 'post',
    data: transData(params),
  })
}
//审计对象库列表
export function OBJList(params) {
  return request({
    url: '/audit/nbsjworkSpace/getAuditObjectLibraryPageList',
    method: 'POST',
    data: transData(params),
  })
}
//审计对象库列表删除
export function OBJDelete(params) {
  return request({
    url: '/audit/nbsjworkSpace/deleteAuditObjectLibrary',
    method: 'GET',
    params: transData(params),
  })
}
//审计对象库详情
export function OBJDetail(params) {
  return request({
    url: '/audit/nbsjworkSpace/selectAuditObjectLibraryInfo',
    method: 'GET',
    params: transData(params),
  })
}
//审计对象库审计情况
export function OBJPersonDetail(params) {
  return request({
    url: '/audit/audit/Staffscore/getprojectLisbystaffid',
    method: 'GET',
    params: transData(params),
  })
}

//整改问题一览表
export function getZgwtData(params) {
  return request({
    url: '/audit/auditProject/sjfx/zgwt',
    method: 'get',
    params: transData(params),
  })
}

//审计分析报告数据
export function getSjslData(params) {
  return request({
    url: '/audit/auditProject/sjfx/sjsl',
    method: 'get',
    params: transData(params),
  })
}

//整改问题详细数据
export function getZgwtDetail(params) {
  return request({
    url: '/audit/auditProject/sjfx/zgwt/detail',
    method: 'get',
    params: transData(params),
  })
}

//整改问题一览表详细数据（基于整改清单）
export function getZgwtIssuesDetail(params) {
  return request({
    url: '/audit/auditProject/sjfx/zgwt/issues/detail',
    method: 'get',
    params: transData(params),
  })
}

// ==================== 大屏接口 ====================

// 第1部分: 审计项目列表（新接口）
export function getSJXMData(params) {
  return request({
    url: '/audit/auditProject/sjfx/sjsl',
    method: 'get',
    params: transData(params),
  })
}

// 审计项目计划数统计接口（按被审计单位分组）
export function getPlanCountData(params) {
  return request({
    url: '/audit/auditProject/plancount',
    method: 'get',
    params: transData(params),
  })
}

// 第2部分: 审计项目统计（使用审计类型项目接口）
export function getXMZTData(params) {
  return request({
    url: '/audit/auditProject/sjfx/sjlxxm',
    method: 'get',
    params: transData(params),
  })
}

// 第3部分: 审计项目完成情况
export function getXMLXData(params) {
  return request({
    url: '/audit/auditProject/sjfx/projectCompletion',
    method: 'get',
    params: transData(params),
  })
}

// 第4部分: 项目数趋势变化（使用各公司审计项目数接口）
export function getXMQSData(params) {
  return request({
    url: '/audit/auditProject/sjfx/ggsxm',
    method: 'get',
    params: transData(params),
  })
}

// 第5部分: 问题统计分析
export function getWTZTData(params) {
  return request({
    url: '/audit/auditProject/sjfx/wtzt',
    method: 'get',
    params: transData(params),
  })
}

// 第6部分: 审计项目数一览表（使用审计分析报告数据接口）
export function getXMYLData(params) {
  return request({
    url: '/audit/auditProject/sjfx/sjsl',
    method: 'get',
    params: transData(params),
  })
}

// 第7部分: 整改问题一览表
export function getZGWTData(params) {
  return request({
    url: '/audit/auditProject/sjfx/zgwt',
    method: 'get',
    params: transData(params),
  })
}

// 第8部分: 项目数趋势变化（最近12个月）
export function getProjectTrendData(params) {
  return request({
    url: '/audit/auditProject/sjfx/projectTrend',
    method: 'get',
    params: transData(params),
  })
}

// 第9部分: 整改问题状态统计
export function getIssuesStatusStatisticsData(params) {
  return request({
    url: '/audit/auditProject/sjfx/issuesStatusStatistics',
    method: 'get',
    params: transData(params),
  })
}

// 大屏新接口: 各部门评价项目数统计
export function getDepartmentProjectCount(params) {
  return request({
    url: '/nkhg/screen/statistics/departmentProjectCount',
    method: 'get',
    params: transData(params),
  })
}

// 大屏新接口: 控制有效性数据查询
export function getControlEffectivenessData(params) {
  return request({
    url: '/nkhg/screen/statistics/controlEffectiveness',
    method: 'get',
    params: transData(params),
  })
}

// 大屏新接口: 各部门评价项目数占比统计
export function getDepartmentProjectRatio(params) {
  return request({
    url: '/nkhg/screen/statistics/departmentProjectRatio',
    method: 'get',
    params: transData(params),
  })
}

// 大屏新接口: 缺陷类型分布统计
export function getDefectTypeDistribution(params) {
  return request({
    url: '/nkhg/screen/statistics/defectTypeDistribution',
    method: 'get',
    params: transData(params),
  })
}

// 大屏新接口: 各单位缺陷数量对比分析
export function getDepartmentDefectComparison(params) {
  return request({
    url: '/nkhg/screen/statistics/departmentDefectComparison',
    method: 'get',
    params: transData(params),
  })
}

// 大屏新接口: 本年缺陷项目趋势分析
export function getDefectTrendAnalysis(params) {
  return request({
    url: '/nkhg/screen/statistics/defectTrendAnalysis',
    method: 'get',
    params: transData(params),
  })
}

// 大屏新接口: 缺陷属性分布统计
export function getDefectPropertyDistribution(params) {
  return request({
    url: '/nkhg/screen/statistics/defectPropertyDistribution',
    method: 'get',
    params: transData(params),
  })
}
