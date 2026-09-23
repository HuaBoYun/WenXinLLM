import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// 风险数量
export function numberRisks(params) {
  return request({
    url: '/riskcontrol/controlHome/numberRisks',
    method: 'get',
    params: transData(params),
  })
}
// 风险事件数量
export function numberEventsRisks(params) {
  return request({
    url: '/riskcontrol/controlHome/numberEventsRisks',
    method: 'get',
    params: transData(params),
  })
}
// 交易对手数量
export function NumberOpponents(params) {
  return request({
    url: '/riskcontrol/controlHome/NumberOpponents',
    method: 'get',
    params: transData(params),
  })
}
// 按单位（公司）统计风险事件数量
export function getRiskeventCountByCompany(params) {
  return request({
    url: '/riskcontrol/riskEvent/getRiskeventCountByCompany',
    method: 'get',
    params: transData(params),
  })
}
// 风险报告类型统计
export function getRiskReportTypeCountByCompany(params) {
  return request({
    url: '/riskcontrol/nbkz/getRiskReportTypeCountByCompany',
    method: 'get',
    params: transData(params),
  })
}
// 风险事件类型
export function getRiskLosseventcategory(params) {
  return request({
    url: '/riskcontrol/riskEvent/getRiskLosseventcategory',
    method: 'get',
    params: transData(params),
  })
}
//风险点统计评估任务完成情况
export function getRiskPointTask(params) {
  return request({
    url: '/riskcontrol/controlHome/getRiskPointTask',
    method: 'get',
    params: transData(params),
  })
}
//风险类型统计风险评估结果
export function getRiskCatnameAnalysis(params) {
  return request({
    url: '/riskcontrol/controlHome/getRiskCatnameAnalysis',
    method: 'get',
    params: transData(params),
  })
}
//风险点统计风险评估结果
export function getRiskAnalysis(params) {
  return request({
    url: '/riskcontrol/controlHome/getRiskAnalysis',
    method: 'get',
    params: transData(params),
  })
}
//风险领域统计风险评估结果
export function getRiskAreasAnalysis(params) {
  return request({
    url: '/riskcontrol/controlHome/getRiskAreasAnalysis',
    method: 'get',
    params: transData(params),
  })
}
//按部门统计风险评估结果
export function getRiskByDepartmentAnalysis(params) {
  return request({
    url: '/riskcontrol/controlHome/getRiskByDepartmentAnalysis',
    method: 'get',
    params: transData(params),
  })
}
//按单位（公司）统计风险评估结果
export function getRiskByCompanyAnalysis(params) {
  return request({
    url: '/riskcontrol/controlHome/getRiskByCompanyAnalysis',
    method: 'get',
    params: transData(params),
  })
}

//列表筛选
export function getRiskCompanyList(params) {
  return request({
    url: '/riskcontrol/controlHome/getRiskCompanyList',
    method: 'get',
    params: transData(params),
  })
}

//全集团一般风险、重大风险趋势图
export function getGroupRiskTrendChart(params) {
  return request({
    url: '/riskcontrol/controlHome/getGroupRiskTrendChart',
    method: 'get',
    params: transData(params),
  })
}
//全集团按年度风险趋势图
export function getAnnualRiskGroup(params) {
  return request({
    url: '/riskcontrol/controlHome/getAnnualRiskGroup',
    method: 'get',
    params: transData(params),
  })
}

// 获取近12个月风险趋势统计
export function getRiskTrendLast12Months(params) {
  return request({
    url: '/riskcontrol/risk/getRiskTrendLast12Months',
    method: 'get',
    params: transData(params),
  })
}
//按公司风险审查统计图
export function getCompanyRiskReview(params) {
  return request({
    url: '/riskcontrol/controlHome/getCompanyRiskReview',
    method: 'get',
    params: transData(params),
  })
}
//各公司报告风险统计图
export function getCompanyRiskResponse(params) {
  return request({
    url: '/riskcontrol/controlHome/getCompanyRiskResponse',
    method: 'get',
    params: transData(params),
  })
}
//按公司风险事件统计图
export function getCompanyRiskEventList(params) {
  return request({
    url: '/riskcontrol/controlHome/getCompanyRiskEventList',
    method: 'get',
    params: transData(params),
  })
}


// 全集团风险领域统计
export function riskCatnameRisks(params) {
  return request({
    url: '/riskcontrol/controlHome/riskCatnameRisks',
    method: 'get',
    params: transData(params),
  })
}

//已审批确认风险数量和未审批确认风险数量
export function riskNumbers(params) {
  return request({
    url: '/riskcontrol/controlHome/riskNumbers',
    method: 'get',
    params: transData(params),
  })
}
//各公司风险数量统计
export function getCountByOrg(params) {
  return request({
    url: '/riskcontrol/controlHome/getCountByOrg',
    method: 'get',
    params: transData(params),
  })
}
//各公司风险报告统计图
export function reportByOrg(params) {
  return request({
    url: '/riskcontrol/controlHome/reportByOrg',
    method: 'get',
    params: transData(params),
  })
}
//集团风险管控措施信息
export function getControlmatrixCount(params) {
  return request({
    url: '/riskcontrol/controlHome/getControlmatrixCount',
    method: 'get',
    params: transData(params),
  })
}
//集团风险管控措施信息
export function getControlmatrixList(params) {
  return request({
    url: '/riskcontrol/controlHome/getControlmatrixList',
    method: 'get',
    params: transData(params),
  })
}
//热力图
export function getFxpgRlt(params) {
  return request({
    url: '/riskcontrol/controlHome/getFxpgRlt',
    method: 'get',
    params: transData(params),
  })
}
//热力图
export function getPgrlt(params) {
  return request({
    url: '/riskcontrol/controlHome/pgrlt',
    method: 'get',
    params: transData(params),
  })
}
//热力图
export function getPgrltxq(params) {
  return request({
    url: '/riskcontrol/controlHome/pgrltxq',
    method: 'get',
    params: transData(params),
  })
}

//热力图
export function getList(params) {
  return request({
    url: '/riskcontrol/statistics/reminder/getList',
    method: 'post',
    data: transData(params),
    headers: {
      'Content-Type': 'application/json',
    },
  })
}

//催办统计-穿透-一体化管控措施
export function getControlPenetrateList(params) {
  return request({
    url: '/riskcontrol/statistics/reminder/control-penetrate/getList',
    method: 'get',
    params: transData(params),
  })
}
//催办统计-穿透-月度评估
export function getMonthlyPenetrateList(params) {
  return request({
    url: '/riskcontrol/statistics/reminder/monthly-penetrate/getList',
    method: 'get',
    params: transData(params),
  })
}

// 获取未读催办列表
export function getUnreadReminderList(params) {
  return request({
    url: '/riskcontrol/reminder/getUnreadReminderList',
    method: 'get',
    params: transData(params),
  })
}

// 获取月度评估情况一览表
export function getMonthlyEvaluationList(params) {
  return request({
    url: '/riskcontrol/reminder/getMonthlyEvaluationList',
    method: 'get',
    params: transData(params),
  })
}

// 获取风险数据库一览表
export function getRiskDatabaseList(params) {
  return request({
    url: '/riskcontrol/reminder/getRiskDatabaseList',
    method: 'get',
    params: transData(params),
  })
}

// 获取风险审查情况分析(近12个月)
export function getRiskReviewAnalysis(params) {
  return request({
    url: '/riskcontrol/reminder/getRiskReviewAnalysis',
    method: 'get',
    params: transData(params),
  })
}

// 获取风险事件数统计
export function getRiskEventCount(params) {
  return request({
    url: '/riskcontrol/reminder/getRiskEventCount',
    method: 'get',
    params: transData(params),
  })
}

// 获取风险措施状态统计
export function getRiskMeasureStatus(params) {
  return request({
    url: '/riskcontrol/reminder/getRiskMeasureStatus',
    method: 'get',
    params: transData(params),
  })
}

// 获取风险完成情况统计
export function getRiskCompletion(params) {
  return request({
    url: '/riskcontrol/reminder/getRiskCompletion',
    method: 'get',
    params: transData(params),
  })
}

// 获取风险top10列表
export function getRiskTopList(params) {
  return request({
    url: '/riskcontrol/getRiskTopList',
    method: 'post',
    data: transData(params),
  })
}

// 更新风险排序
export function updateRiskOrder(params) {
  return request({
    url: '/riskcontrol/fxxt/fxyd/updateRiskOrder',
    method: 'post',
    data: transData(params),

  })
}
