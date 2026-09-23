import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// ==================== 经营统计 ====================

// 获取经营统计概览
export function getOperationStatistics(data) {
  return request({
    url: '/monitor/v1/enterprise/operation/statistics',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

// 获取生产统计
export function getProductionStatistics(data) {
  return request({
    url: '/monitor/v1/enterprise/operation/statistics/production',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

// 获取销售统计
export function getSalesStatistics(data) {
  return request({
    url: '/monitor/v1/enterprise/operation/statistics/sales',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

// 获取项目进度
export function getProjectProgress(data) {
  return request({
    url: '/monitor/v1/enterprise/operation/project/progress',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

// ==================== 生产管理 ====================

// 获取生产计划列表
export function getProductionPlanList(data) {
  return request({
    url: '/monitor/v1/enterprise/operation/production/plan/list',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

// 获取生产计划详情
export function getProductionPlanById(id) {
  return request({
    url: `/monitor/v1/enterprise/operation/production/plan/${id}`,
    method: 'get'
  })
}

// 新增生产计划
export function addProductionPlan(data) {
  return request({
    url: '/monitor/v1/enterprise/operation/production/plan/add',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

// 更新生产计划
export function updateProductionPlan(data) {
  return request({
    url: '/monitor/v1/enterprise/operation/production/plan/update',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

// 删除生产计划
export function deleteProductionPlan(id) {
  return request({
    url: `/monitor/v1/enterprise/operation/production/plan/${id}`,
    method: 'delete'
  })
}

// 获取生产计划统计
export function getProductionPlanStatistics(data) {
  return request({
    url: '/monitor/v1/enterprise/operation/production/plan/statistics',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

// ==================== 销售管理 ====================

// 获取销售订单列表
export function getSalesOrderList(data) {
  return request({
    url: '/monitor/v1/enterprise/operation/sales/order/list',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

// 获取销售订单详情
export function getSalesOrderById(id) {
  return request({
    url: `/monitor/v1/enterprise/operation/sales/order/${id}`,
    method: 'get'
  })
}

// 新增销售订单
export function addSalesOrder(data) {
  return request({
    url: '/monitor/v1/enterprise/operation/sales/order/add',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

// 更新销售订单
export function updateSalesOrder(data) {
  return request({
    url: '/monitor/v1/enterprise/operation/sales/order/update',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

// 删除销售订单
export function deleteSalesOrder(id) {
  return request({
    url: `/monitor/v1/enterprise/operation/sales/order/${id}`,
    method: 'delete'
  })
}

// 获取销售订单统计
export function getSalesOrderStatistics(data) {
  return request({
    url: '/monitor/v1/enterprise/operation/sales/order/statistics',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

// ==================== 供应链管理 ====================

// 获取供应商列表
export function getSupplyList(data) {
  return request({
    url: '/monitor/v1/enterprise/operation/supply/list',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

// 获取供应商详情
export function getSupplyById(id) {
  return request({
    url: `/monitor/v1/enterprise/operation/supply/${id}`,
    method: 'get'
  })
}

// 新增供应商
export function addSupply(data) {
  return request({
    url: '/monitor/v1/enterprise/operation/supply/add',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

// 更新供应商
export function updateSupply(data) {
  return request({
    url: '/monitor/v1/enterprise/operation/supply/update',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

// 删除供应商
export function deleteSupply(id) {
  return request({
    url: `/monitor/v1/enterprise/operation/supply/${id}`,
    method: 'delete'
  })
}

// 获取供应链统计
export function getSupplyStatistics(data) {
  return request({
    url: '/monitor/v1/enterprise/operation/supply/statistics',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

// ==================== 业务流程 ====================

// 获取业务流程列表
export function getBusinessProcessList(data) {
  return request({
    url: '/monitor/v1/enterprise/operation/process/list',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

// 获取业务流程详情
export function getBusinessProcessById(id) {
  return request({
    url: `/monitor/v1/enterprise/operation/process/${id}`,
    method: 'get'
  })
}

// 新增业务流程
export function addBusinessProcess(data) {
  return request({
    url: '/monitor/v1/enterprise/operation/process/add',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

// 更新业务流程
export function updateBusinessProcess(data) {
  return request({
    url: '/monitor/v1/enterprise/operation/process/update',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

// 删除业务流程
export function deleteBusinessProcess(id) {
  return request({
    url: `/monitor/v1/enterprise/operation/process/${id}`,
    method: 'delete'
  })
}

// 获取业务流程统计
export function getBusinessProcessStatistics(data) {
  return request({
    url: '/monitor/v1/enterprise/operation/process/statistics',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

// ==================== 绩效管理 ====================

// 获取绩效考核列表
export function getPerformanceAssessmentList(data) {
  return request({
    url: '/monitor/v1/enterprise/operation/performance/assessment/list',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

// 获取绩效考核详情
export function getPerformanceAssessmentById(id) {
  return request({
    url: `/monitor/v1/enterprise/operation/performance/assessment/${id}`,
    method: 'get'
  })
}

// 新增绩效考核
export function addPerformanceAssessment(data) {
  return request({
    url: '/monitor/v1/enterprise/operation/performance/assessment/add',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

// 更新绩效考核
export function updatePerformanceAssessment(data) {
  return request({
    url: '/monitor/v1/enterprise/operation/performance/assessment/update',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

// 删除绩效考核
export function deletePerformanceAssessment(id) {
  return request({
    url: `/monitor/v1/enterprise/operation/performance/assessment/${id}`,
    method: 'delete'
  })
}

// 获取绩效考核统计
export function getPerformanceAssessmentStatistics(data) {
  return request({
    url: '/monitor/v1/enterprise/operation/performance/assessment/statistics',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

// ==================== 市场分析 ====================

// 获取市场分析列表
export function getMarketAnalysisList(data) {
  return request({
    url: '/monitor/v1/enterprise/operation/market/analysis/list',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

// 获取市场分析详情
export function getMarketAnalysisById(id) {
  return request({
    url: `/monitor/v1/enterprise/operation/market/analysis/${id}`,
    method: 'get'
  })
}

// 新增市场分析
export function addMarketAnalysis(data) {
  return request({
    url: '/monitor/v1/enterprise/operation/market/analysis/add',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

// 更新市场分析
export function updateMarketAnalysis(data) {
  return request({
    url: '/monitor/v1/enterprise/operation/market/analysis/update',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

// 删除市场分析
export function deleteMarketAnalysis(id) {
  return request({
    url: `/monitor/v1/enterprise/operation/market/analysis/${id}`,
    method: 'delete'
  })
}

// 获取市场分析统计
export function getMarketAnalysisStatistics(data) {
  return request({
    url: '/monitor/v1/enterprise/operation/market/analysis/statistics',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

// ==================== 项目管理 ====================

// 获取项目列表
export function getProjectManagementList(data) {
  return request({
    url: '/monitor/v1/enterprise/operation/project/list',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

// 获取项目详情
export function getProjectManagementById(id) {
  return request({
    url: `/monitor/v1/enterprise/operation/project/${id}`,
    method: 'get'
  })
}

// 新增项目
export function addProjectManagement(data) {
  return request({
    url: '/monitor/v1/enterprise/operation/project/add',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

// 更新项目
export function updateProjectManagement(data) {
  return request({
    url: '/monitor/v1/enterprise/operation/project/update',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

// 删除项目
export function deleteProjectManagement(id) {
  return request({
    url: `/monitor/v1/enterprise/operation/project/${id}`,
    method: 'delete'
  })
}

// 获取项目统计
export function getProjectManagementStatistics(data) {
  return request({
    url: '/monitor/v1/enterprise/operation/project/statistics',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}


// ==================== 经营报告 ====================

// 获取经营报告数据
export function getOperationReport(data) {
  return request({
    url: '/monitor/v1/enterprise/operation/report',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}
