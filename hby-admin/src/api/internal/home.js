import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// 评价跟踪-状态统计
export function evaluationTracking(params) {
  return request({
    url: '/nkhg/home/statistics/evaluation-tracking',
    method: 'get',
    params: transData(params),
  })
}
// 问题发现-缺陷等级统计
export function defectGrade(params) {
  return request({
    url: '/nkhg/home/statistics/problem-discovery/defect-grade',
    method: 'get',
    params: transData(params),
  })
}
// 问题发现-一级流程统计
export function oneLevelProcess(params) {
  return request({
    url: '/nkhg/home/statistics/problem-discovery/one-level-process',
    method: 'get',
    params: transData(params),
  })
}
// 问题发现-年度统计
export function year(params) {
  return request({
    url: '/nkhg/home/statistics/problem-discovery/year',
    method: 'get',
    params: transData(params),
  })
}

// 测试跟踪-状态统计
export function testTracking(params) {
  return request({
    url: '/nkhg/home/statistics/test-tracking',
    // headers: {
    //   'Content-Type': 'application/json;charset=UTF-8',
    // },
    method: 'get',
    params: transData(params),
  })
}

//测试方案-添加页面-保存
// export function savePlan(params) {
//   return request({
//     url: '/nkhg/nkcs/plan/save',
//     method: 'post',
//     data: transData(params),
//   })
// }
// 首页-顶部统计数量
export function getTopcnt(params) {
  return request({
    url: '/hggl/api-auth/inspect/main/topcnt',
    method: 'post',
    data: transData(params),
  })
}

// 首页-合规审查数量
export function getCompcnt(params) {
  return request({
    url: '/hggl/api-auth/inspect/main/compcnt',
    method: 'post',
    data: transData(params),
  })
}

// 内部控制缺陷分布
export function getCharts1(params) {
  return request({
    url: '/nkhg/home/statistics/problem-discovery/one-level-process',
    method: 'get',
    params: transData(params),
  })
}

//内部控制缺陷等级统计
export function getCharts2(params) {
  return request({
    url: '/nkhg/home/statistics/problem-discovery/defect-grade',
    method: 'get',
    params: transData(params),
  })
}

//评价跟踪-状态统计
export function getCharts3(params) {
  return request({
    url: '/nkhg/home/statistics/evaluation-tracking',
    method: 'get',
    params: transData(params),
  })
}

//
export function getCharts4(params) {
  return request({
    url: '/nkhg/home/statistics/test-tracking',
    method: 'get',
    params: transData(params),
  })
}

//问题发现-年度统计
export function getCharts5(params) {
  return request({
    url: '/nkhg/home/statistics/problem-discovery/year',
    method: 'get',
    params: transData(params),
  })
}

//首页-获取公司下拉
export function getAssessCompanyList(params) {
  return request({
    url: '/nkhg/getAssessCompanyList',
    method: 'get',
    params: transData(params),
  })
}

//首页-项目成熟度占比分析
export function getProjectMaturityAnalysis(params) {
  return request({
    url: '/nkhg/home/statistics/getProjectMaturityAnalysis',
    method: 'get',
    params: transData(params),
  })
}

//首页-各单位缺陷数量问题对比
export function getDefectQuantityIssues(params) {
  return request({
    url: '/nkhg/home/statistics/getDefectQuantityIssues',
    method: 'get',
    params: transData(params),
  })
}

//首页-公司项目评价数统计
export function getCompanyProjectEvaluations(params) {
  return request({
    url: '/nkhg/home/statistics/getCompanyProjectEvaluations',
    method: 'get',
    params: transData(params),
  })
}

//首页-各单位缺陷数量问题对比
export function getDefectQuantityIssuesByDep(params) {
  return request({
    url: '/nkhg/home/statistics/getDefectQuantityIssuesByDep',
    method: 'get',
    params: transData(params),
  })
}

//首页-本年度缺陷项目趋势分析
export function getDefectProjectsYearAnalysis(params) {
  return request({
    url: '/nkhg/home/statistics/getDefectProjectsYearAnalysis',
    method: 'get',
    params: transData(params),
  })
}

//首页-年度缺陷类型分布分析
export function getDistributionDefectTypes(params) {
  return request({
    url: '/nkhg/home/statistics/getDistributionDefectTypes',
    method: 'get',
    params: transData(params),
  })
}

// 首页-内控评价结果一览表
export function getEvaluationResultsList(params) {
  return request({
    url: '/nkhg/home/statistics/getEvaluationResultsList',
    method: 'POST',
    data: transData(params),
  })
}

