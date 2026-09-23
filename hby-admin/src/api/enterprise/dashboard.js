import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// 后端使用@RequestBody，需要JSON格式
const jsonHeaders = { 'Content-Type': 'application/json;charset=UTF-8' }

// 获取企业管理驾驶舱数据
export function getEnterpriseDashboard(enterpriseId) {
  return request({
    url: `/monitor/v1/enterprise/dashboard/${enterpriseId}`,
    method: 'get',
  })
}

// 获取企业经营概览数据
export function getEnterpriseOverview(data) {
  return request({
    url: '/monitor/v1/enterprise/dashboard/overview',
    method: 'post',
    headers: jsonHeaders,
    data: transData(data),
  })
}

// 获取关键指标数据
export function getKeyIndicators(data) {
  return request({
    url: '/monitor/v1/enterprise/dashboard/indicators',
    method: 'post',
    headers: jsonHeaders,
    data: transData(data),
  })
}

// 获取业务状态监控数据
export function getBusinessStatus(data) {
  return request({
    url: '/monitor/v1/enterprise/dashboard/business-status',
    method: 'post',
    headers: jsonHeaders,
    data: transData(data),
  })
}

// 获取财务指标趋势数据
export function getFinancialTrend(data) {
  return request({
    url: '/monitor/v1/enterprise/dashboard/financial-trend',
    method: 'post',
    headers: jsonHeaders,
    data: transData(data),
  })
}

// 获取风险监控数据
export function getRiskMonitoring(data) {
  return request({
    url: '/monitor/v1/enterprise/dashboard/risk-monitoring',
    method: 'post',
    headers: jsonHeaders,
    data: transData(data),
  })
}

// 获取数据报送状态
export function getDataSubmissionStatus(data) {
  return request({
    url: '/monitor/v1/enterprise/dashboard/submission-status',
    method: 'post',
    headers: jsonHeaders,
    data: transData(data),
  })
}

// 获取企业组织架构数据
export function getOrganizationStructure(enterpriseId) {
  return request({
    url: `/monitor/v1/enterprise/dashboard/organization/${enterpriseId}`,
    method: 'get',
  })
}

// 获取业务分布数据
export function getBusinessDistribution(data) {
  return request({
    url: '/monitor/v1/enterprise/dashboard/business-distribution',
    method: 'post',
    headers: jsonHeaders,
    data: transData(data),
  })
}

// 获取地域分布数据
export function getGeographicalDistribution(data) {
  return request({
    url: '/monitor/v1/enterprise/dashboard/geographical-distribution',
    method: 'post',
    headers: jsonHeaders,
    data: transData(data),
  })
}

// 获取经营计划执行情况
export function getOperatingPlanExecution(data) {
  return request({
    url: '/monitor/v1/enterprise/dashboard/plan-execution',
    method: 'post',
    headers: jsonHeaders,
    data: transData(data),
  })
}

// 获取预算执行情况
export function getBudgetExecution(data) {
  return request({
    url: '/monitor/v1/enterprise/dashboard/budget-execution',
    method: 'post',
    headers: jsonHeaders,
    data: transData(data),
  })
}

// 获取市场表现数据
export function getMarketPerformance(data) {
  return request({
    url: '/monitor/v1/enterprise/dashboard/market-performance',
    method: 'post',
    headers: jsonHeaders,
    data: transData(data),
  })
}

// 获取人力资源概况
export function getHROverview(data) {
  return request({
    url: '/monitor/v1/enterprise/dashboard/hr-overview',
    method: 'post',
    headers: jsonHeaders,
    data: transData(data),
  })
}

// 获取技术创新情况
export function getInnovationStatus(data) {
  return request({
    url: '/monitor/v1/enterprise/dashboard/innovation-status',
    method: 'post',
    headers: jsonHeaders,
    data: transData(data),
  })
}

// 获取供应链状态
export function getSupplyChainStatus(data) {
  return request({
    url: '/monitor/v1/enterprise/dashboard/supply-chain-status',
    method: 'post',
    headers: jsonHeaders,
    data: transData(data),
  })
}

// 获取客户满意度数据
export function getCustomerSatisfaction(data) {
  return request({
    url: '/monitor/v1/enterprise/dashboard/customer-satisfaction',
    method: 'post',
    headers: jsonHeaders,
    data: transData(data),
  })
}

// 获取环保合规状态
export function getEnvironmentalCompliance(data) {
  return request({
    url: '/monitor/v1/enterprise/dashboard/environmental-compliance',
    method: 'post',
    headers: jsonHeaders,
    data: transData(data),
  })
}

// 获取安全生产状态
export function getSafetyProductionStatus(data) {
  return request({
    url: '/monitor/v1/enterprise/dashboard/safety-production',
    method: 'post',
    headers: jsonHeaders,
    data: transData(data),
  })
}

// 获取党建工作情况
export function getPartyBuildingStatus(data) {
  return request({
    url: '/monitor/v1/enterprise/dashboard/party-building',
    method: 'post',
    headers: jsonHeaders,
    data: transData(data),
  })
}

// 获取社会责任履行情况
export function getSocialResponsibility(data) {
  return request({
    url: '/monitor/v1/enterprise/dashboard/social-responsibility',
    method: 'post',
    headers: jsonHeaders,
    data: transData(data),
  })
}

// 刷新驾驶舱数据
export function refreshDashboardData(enterpriseId) {
  return request({
    url: `/monitor/v1/enterprise/dashboard/refresh/${enterpriseId}`,
    method: 'post',
    headers: jsonHeaders,
    data: transData(),
  })
}

// 导出驾驶舱报告
export function exportDashboardReport(data) {
  return request({
    url: '/monitor/v1/enterprise/dashboard/export',
    method: 'post',
    headers: jsonHeaders,
    data: transData(data),
    responseType: 'blob'
  })
}

// 获取驾驶舱配置
export function getDashboardConfig(enterpriseId) {
  return request({
    url: `/monitor/v1/enterprise/dashboard/config/${enterpriseId}`,
    method: 'get',
  })
}

// 保存驾驶舱配置
export function saveDashboardConfig(data) {
  return request({
    url: '/monitor/v1/enterprise/dashboard/config',
    method: 'post',
    headers: jsonHeaders,
    data: transData(data),
  })
}

// 获取实时数据更新状态
export function getDataUpdateStatus(enterpriseId) {
  return request({
    url: `/monitor/v1/enterprise/dashboard/update-status/${enterpriseId}`,
    method: 'get',
  })
}

// 设置数据预警阈值
export function setWarningThreshold(data) {
  return request({
    url: '/monitor/v1/enterprise/dashboard/warning-threshold',
    method: 'post',
    headers: jsonHeaders,
    data: transData(data),
  })
}

// 获取历史对比数据
export function getHistoricalComparison(data) {
  return request({
    url: '/monitor/v1/enterprise/dashboard/historical-comparison',
    method: 'post',
    headers: jsonHeaders,
    data: transData(data),
  })
}

// 获取同行业对比数据
export function getIndustryComparison(data) {
  return request({
    url: '/monitor/v1/enterprise/dashboard/industry-comparison',
    method: 'post',
    headers: jsonHeaders,
    data: transData(data),
  })
}

// 数据报送任务API
export function dataSubmissionTaskApi(data) {
  return request({
    url: '/monitor/v1/enterprise/dashboard/data-submission-task',
    method: 'post',
    headers: jsonHeaders,
    data: transData(data),
  })
}
