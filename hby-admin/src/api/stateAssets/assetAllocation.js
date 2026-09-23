import request from '@/utils/request'
import { transData } from '@/utils/requestData'

const BASE = '/monitor/v1/supervision/asset/allocation'

// 获取资产配置统计数据
export function getAssetAllocationStatistics(data) {
  return request({ url: `${BASE}/statistics`, method: 'post', data: transData(data) })
}

// 获取资产配置列表
export function getAssetAllocationsList(data) {
  return request({ url: `${BASE}/list`, method: 'post', data: transData(data) })
}

// 新增资产配置
export function addAssetAllocation(data) {
  return request({ url: `${BASE}/add`, method: 'post', data: transData(data) })
}

// 编辑资产配置
export function updateAssetAllocation(data) {
  return request({ url: `${BASE}/update`, method: 'post', data: transData(data) })
}

// 删除资产配置
export function deleteAssetAllocation(data) {
  return request({ url: `${BASE}/delete`, method: 'post', data: transData(data) })
}

// 获取资产配置详情
export function getAssetAllocationDetail(data) {
  return request({ url: `${BASE}/detail`, method: 'post', data: transData(data) })
}

// 获取资产配置图表数据
export function getAssetAllocationCharts(data) {
  return request({ url: `${BASE}/charts`, method: 'post', data: transData(data) })
}

// 资产配置结构分析
export function getAssetAllocationStructureAnalysis(data) {
  return request({ url: `${BASE}/structure-analysis`, method: 'post', data: transData(data) })
}

// 资产配置合理性评估
export function getAssetAllocationRationalityAssessment(data) {
  return request({ url: `${BASE}/rationality-assessment`, method: 'post', data: transData(data) })
}

// 资产配置优化建议
export function getAssetAllocationOptimizationSuggestions(data) {
  return request({ url: `${BASE}/optimization-suggestions`, method: 'post', data: transData(data) })
}

// 资产配置效率分析
export function getAssetAllocationEfficiencyAnalysis(data) {
  return request({ url: `${BASE}/efficiency-analysis`, method: 'post', data: transData(data) })
}

// 资产配置风险分散度分析
export function getAssetAllocationRiskDispersionAnalysis(data) {
  return request({ url: `${BASE}/risk-dispersion-analysis`, method: 'post', data: transData(data) })
}

// 资产配置趋势分析
export function getAssetAllocationTrendAnalysis(data) {
  return request({ url: `${BASE}/trend-analysis`, method: 'post', data: transData(data) })
}

// 资产配置对比分析
export function getAssetAllocationComparisonAnalysis(data) {
  return request({ url: `${BASE}/comparison-analysis`, method: 'post', data: transData(data) })
}

// 资产配置预警设置
export function setAssetAllocationAlerts(data) {
  return request({ url: `${BASE}/alerts`, method: 'post', data: transData(data) })
}

// 获取资产配置预警信息
export function getAssetAllocationAlerts(data) {
  return request({ url: `${BASE}/alerts/list`, method: 'post', data: transData(data) })
}

// 资产配置智能推荐
export function getAssetAllocationRecommendations(data) {
  return request({ url: `${BASE}/recommendations`, method: 'post', data: transData(data) })
}

// 批量优化资产配置
export function batchOptimizeAssetAllocations(data) {
  return request({ url: `${BASE}/batch-optimize`, method: 'post', data: transData(data) })
}

// 批量评估资产配置
export function batchAssessAssetAllocations(data) {
  return request({ url: `${BASE}/batch-assess`, method: 'post', data: transData(data) })
}

// 批量删除资产配置
export function batchDeleteAssetAllocations(data) {
  return request({ url: `${BASE}/batch-delete`, method: 'post', data: transData(data) })
}

// 导出资产配置数据
export function exportAssetAllocationData(data) {
  return request({ url: `${BASE}/export`, method: 'post', data: transData(data), responseType: 'blob' })
}

// 导入资产配置数据
export function importAssetAllocationData(data) {
  return request({ url: `${BASE}/import`, method: 'post', data: transData(data) })
}

// 生成资产配置分析报告
export function generateAssetAllocationReport(data) {
  return request({ url: `${BASE}/generate-report`, method: 'post', data: transData(data) })
}

// 获取资产配置监管建议
export function getAssetAllocationSupervisionSuggestions(data) {
  return request({ url: `${BASE}/supervision-suggestions`, method: 'post', data: transData(data) })
}

// 资产配置压力测试
export function performAssetAllocationStressTest(data) {
  return request({ url: `${BASE}/stress-test`, method: 'post', data: transData(data) })
}

// 资产配置情景分析
export function performAssetAllocationScenarioAnalysis(data) {
  return request({ url: `${BASE}/scenario-analysis`, method: 'post', data: transData(data) })
}

// 资产配置绩效评估
export function getAssetAllocationPerformanceEvaluation(data) {
  return request({ url: `${BASE}/performance-evaluation`, method: 'post', data: transData(data) })
}

// 资产配置基准对比
export function getAssetAllocationBenchmarkComparison(data) {
  return request({ url: `${BASE}/benchmark-comparison`, method: 'post', data: transData(data) })
}

// 资产配置历史版本
export function getAssetAllocationVersionHistory(data) {
  return request({ url: `${BASE}/version-history`, method: 'post', data: transData(data) })
}

// 恢复资产配置历史版本
export function restoreAssetAllocationVersion(data) {
  return request({ url: `${BASE}/restore-version`, method: 'post', data: transData(data) })
}

// 导出资产配置（统一导出接口）
export function exportAssetAllocation() {
  return request({ url: '/monitor/v1/supervision/export/asset/allocation', method: 'get', responseType: 'blob' })
}
