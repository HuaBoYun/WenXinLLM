import request from '@/utils/request'
import { transData } from '@/utils/requestData'

const BASE = '/monitor/v1/supervision/asset/quality'

// 获取资产质量统计数据
export function getAssetQualityStatistics(data) {
  return request({ url: `${BASE}/statistics`, method: 'post', data: transData(data) })
}

// 获取资产质量列表
export function getAssetQualityList(data) {
  return request({ url: `${BASE}/list`, method: 'post', data: transData(data) })
}

// 新增资产质量评估
export function addAssetQuality(data) {
  return request({ url: `${BASE}/add`, method: 'post', data: transData(data) })
}

// 编辑资产质量评估
export function updateAssetQuality(data) {
  return request({ url: `${BASE}/update`, method: 'post', data: transData(data) })
}

// 删除资产质量评估
export function deleteAssetQuality(data) {
  return request({ url: `${BASE}/delete`, method: 'post', data: transData(data) })
}

// 获取资产质量详情
export function getAssetQualityDetail(data) {
  return request({ url: `${BASE}/detail`, method: 'post', data: transData(data) })
}

// 获取资产质量图表数据
export function getAssetQualityCharts(data) {
  return request({ url: `${BASE}/charts`, method: 'post', data: transData(data) })
}

// 资产质量指标监控
export function getAssetQualityIndicatorMonitoring(data) {
  return request({ url: `${BASE}/indicator-monitoring`, method: 'post', data: transData(data) })
}

// 资产收益率分析
export function getAssetReturnRateAnalysis(data) {
  return request({ url: `${BASE}/return-rate-analysis`, method: 'post', data: transData(data) })
}

// 资产周转率分析
export function getAssetTurnoverRateAnalysis(data) {
  return request({ url: `${BASE}/turnover-rate-analysis`, method: 'post', data: transData(data) })
}

// 资产减值风险识别
export function getAssetImpairmentRiskIdentification(data) {
  return request({ url: `${BASE}/impairment-risk-identification`, method: 'post', data: transData(data) })
}

// 潜在减值资产识别
export function getPotentialImpairmentAssetIdentification(data) {
  return request({ url: `${BASE}/potential-impairment-identification`, method: 'post', data: transData(data) })
}

// 资产质量趋势分析
export function getAssetQualityTrendAnalysis(data) {
  return request({ url: `${BASE}/trend-analysis`, method: 'post', data: transData(data) })
}

// 资产质量变化趋势预测
export function getAssetQualityChangeTrendPrediction(data) {
  return request({ url: `${BASE}/change-trend-prediction`, method: 'post', data: transData(data) })
}

// 资产质量评估模型
export function getAssetQualityAssessmentModel(data) {
  return request({ url: `${BASE}/assessment-model`, method: 'post', data: transData(data) })
}

// 资产质量评分计算
export function calculateAssetQualityScore(data) {
  return request({ url: `${BASE}/calculate-score`, method: 'post', data: transData(data) })
}

// 资产质量等级评定
export function evaluateAssetQualityLevel(data) {
  return request({ url: `${BASE}/evaluate-level`, method: 'post', data: transData(data) })
}

// 资产质量对比分析
export function getAssetQualityComparisonAnalysis(data) {
  return request({ url: `${BASE}/comparison-analysis`, method: 'post', data: transData(data) })
}

// 资产质量基准对比
export function getAssetQualityBenchmarkComparison(data) {
  return request({ url: `${BASE}/benchmark-comparison`, method: 'post', data: transData(data) })
}

// 资产质量预警设置
export function setAssetQualityAlerts(data) {
  return request({ url: `${BASE}/alerts`, method: 'post', data: transData(data) })
}

// 获取资产质量预警信息
export function getAssetQualityAlerts(data) {
  return request({ url: `${BASE}/alerts/list`, method: 'post', data: transData(data) })
}

// 处理预警
export function handleAssetQualityAlert(data) {
  return request({ url: `${BASE}/alerts/handle`, method: 'post', data: transData(data) })
}

// 查看预警详情
export function getAssetQualityAlertDetail(data) {
  return request({ url: `${BASE}/alerts/detail`, method: 'post', data: transData(data) })
}

// 资产质量监控设置
export function setAssetQualityMonitoring(data) {
  return request({ url: `${BASE}/monitoring`, method: 'post', data: transData(data) })
}

// 获取资产质量监控信息
export function getAssetQualityMonitoring(data) {
  return request({ url: `${BASE}/monitoring/list`, method: 'post', data: transData(data) })
}

// 批量评估资产质量
export function batchAssessAssetQuality(data) {
  return request({ url: `${BASE}/batch-assess`, method: 'post', data: transData(data) })
}

// 批量监控资产质量
export function batchMonitorAssetQuality(data) {
  return request({ url: `${BASE}/batch-monitor`, method: 'post', data: transData(data) })
}

// 批量删除资产质量评估
export function batchDeleteAssetQuality(data) {
  return request({ url: `${BASE}/batch-delete`, method: 'post', data: transData(data) })
}

// 导出资产质量数据
export function exportAssetQualityData(data) {
  return request({ url: `${BASE}/export`, method: 'post', data: transData(data), responseType: 'blob' })
}

// 导入资产质量数据
export function importAssetQualityData(data) {
  return request({ url: `${BASE}/import`, method: 'post', data: transData(data) })
}

// 生成资产质量分析报告
export function generateAssetQualityReport(data) {
  return request({ url: `${BASE}/generate-report`, method: 'post', data: transData(data) })
}

// 获取资产质量监管建议
export function getAssetQualitySupervisionSuggestions(data) {
  return request({ url: `${BASE}/supervision-suggestions`, method: 'post', data: transData(data) })
}

// 资产质量压力测试
export function performAssetQualityStressTest(data) {
  return request({ url: `${BASE}/stress-test`, method: 'post', data: transData(data) })
}

// 资产质量情景分析
export function performAssetQualityScenarioAnalysis(data) {
  return request({ url: `${BASE}/scenario-analysis`, method: 'post', data: transData(data) })
}

// 资产质量绩效评估
export function getAssetQualityPerformanceEvaluation(data) {
  return request({ url: `${BASE}/performance-evaluation`, method: 'post', data: transData(data) })
}

// 资产质量历史版本
export function getAssetQualityVersionHistory(data) {
  return request({ url: `${BASE}/version-history`, method: 'post', data: transData(data) })
}

// 恢复资产质量历史版本
export function restoreAssetQualityVersion(data) {
  return request({ url: `${BASE}/restore-version`, method: 'post', data: transData(data) })
}

// 导出资产质量（统一导出接口）
export function exportAssetQuality() {
  return request({ url: '/monitor/v1/supervision/export/asset/quality', method: 'get', responseType: 'blob' })
}
