import request from '@/utils/request'
import { transData } from '@/utils/requestData'

const JSON_HEADERS = { 'Content-Type': 'application/json;charset=UTF-8' }

// 获取控制链分析统计数据
export function getControlChainStatistics(data) {
  return request({
    url: '/monitor/v1/supervision/control-chain/statistics',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 获取控制链分析列表
export function getControlChainsList(data) {
  return request({
    url: '/monitor/v1/supervision/control-chain/list',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 新增控制链分析
export function addControlChain(data) {
  return request({
    url: '/monitor/v1/supervision/control-chain/add',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 编辑控制链分析
export function updateControlChain(data) {
  return request({
    url: '/monitor/v1/supervision/control-chain/update',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 删除控制链分析
export function deleteControlChain(data) {
  return request({
    url: '/monitor/v1/supervision/control-chain/delete',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 获取控制链详情
export function getControlChainDetail(data) {
  return request({
    url: '/monitor/v1/supervision/control-chain/detail',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 启动控制链分析
export function startControlChainAnalysis(data) {
  return request({
    url: '/monitor/v1/supervision/control-chain/analyze',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 获取控制链可视化数据
export function getControlChainVisualization(data) {
  return request({
    url: '/monitor/v1/supervision/control-chain/visualization',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 获取控制路径分析
export function getControlPathAnalysis(data) {
  return request({
    url: '/monitor/v1/supervision/control-chain/path-analysis',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 获取实际控制人识别结果
export function getUltimateControllerIdentification(data) {
  return request({
    url: '/monitor/v1/supervision/control-chain/ultimate-controller',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 获取一致行动人识别结果
export function getConcertedActionIdentification(data) {
  return request({
    url: '/monitor/v1/supervision/control-chain/concerted-action',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 获取控制关系强度计算
export function getControlStrengthCalculation(data) {
  return request({
    url: '/monitor/v1/supervision/control-chain/control-strength',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 获取控制链风险评估
export function getControlChainRiskAssessment(data) {
  return request({
    url: '/monitor/v1/supervision/control-chain/risk-assessment',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 获取控制链变化监控
export function getControlChainChangeMonitoring(data) {
  return request({
    url: '/monitor/v1/supervision/control-chain/change-monitoring',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 获取控制链合规性检查
export function getControlChainComplianceCheck(data) {
  return request({
    url: '/monitor/v1/supervision/control-chain/compliance-check',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 批量分析控制链
export function batchAnalyzeControlChains(data) {
  return request({
    url: '/monitor/v1/supervision/control-chain/batch-analyze',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 批量删除控制链
export function batchDeleteControlChains(data) {
  return request({
    url: '/monitor/v1/supervision/control-chain/batch-delete',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 导出控制链数据
export function exportControlChainData(data) {
  return request({
    url: '/monitor/v1/supervision/control-chain/export',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
    responseType: 'blob',
  })
}

// 导入控制链数据
export function importControlChainData(data) {
  return request({
    url: '/monitor/v1/supervision/control-chain/import',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 获取控制链分析报告
export function getControlChainAnalysisReport(data) {
  return request({
    url: '/monitor/v1/supervision/control-chain/analysis-report',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 获取控制链趋势分析
export function getControlChainTrendAnalysis(data) {
  return request({
    url: '/monitor/v1/supervision/control-chain/trend-analysis',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 获取控制链预警信息
export function getControlChainAlerts(data) {
  return request({
    url: '/monitor/v1/supervision/control-chain/alerts',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 设置控制链预警规则
export function setControlChainAlertRules(data) {
  return request({
    url: '/monitor/v1/supervision/control-chain/alert-rules',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 获取控制链智能推荐
export function getControlChainRecommendations(data) {
  return request({
    url: '/monitor/v1/supervision/control-chain/recommendations',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 获取控制链网络分析
export function getControlChainNetworkAnalysis(data) {
  return request({
    url: '/monitor/v1/supervision/control-chain/network-analysis',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 获取控制链影响力分析
export function getControlChainInfluenceAnalysis(data) {
  return request({
    url: '/monitor/v1/supervision/control-chain/influence-analysis',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 获取控制链稳定性分析
export function getControlChainStabilityAnalysis(data) {
  return request({
    url: '/monitor/v1/supervision/control-chain/stability-analysis',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 获取控制链效率评估
export function getControlChainEfficiencyEvaluation(data) {
  return request({
    url: '/monitor/v1/supervision/control-chain/efficiency-evaluation',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 获取控制链优化建议
export function getControlChainOptimizationSuggestions(data) {
  return request({
    url: '/monitor/v1/supervision/control-chain/optimization-suggestions',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 获取控制链监管建议
export function getControlChainSupervisionSuggestions(data) {
  return request({
    url: '/monitor/v1/supervision/control-chain/supervision-suggestions',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 获取控制链历史版本
export function getControlChainVersionHistory(data) {
  return request({
    url: '/monitor/v1/supervision/control-chain/version-history',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 恢复控制链历史版本
export function restoreControlChainVersion(data) {
  return request({
    url: '/monitor/v1/supervision/control-chain/restore-version',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 导出控制链分析（统一导出接口）
export function exportControlChainAnalysis() {
  return request({ url: '/monitor/v1/supervision/export/equity/controlChainAnalysis', method: 'get', responseType: 'blob' })
}
