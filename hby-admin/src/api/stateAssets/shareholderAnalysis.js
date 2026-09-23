import request from '@/utils/request'
import { transData } from '@/utils/requestData'

const JSON_HEADERS = { 'Content-Type': 'application/json;charset=UTF-8' }

/**
 * 股东穿透分析API
 */

// 分页查询股东穿透分析列表
export function getShareholderAnalysisList(data) {
  return request({
    url: '/monitor/v1/supervision/equity/shareholder/list',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 根据ID查询股东穿透分析详情
export function getShareholderAnalysisById(id) {
  return request({
    url: `/monitor/v1/supervision/equity/shareholder/${id}`,
    method: 'get',
  })
}

// 新增股东穿透分析
export function addShareholderAnalysis(data) {
  return request({
    url: '/monitor/v1/supervision/equity/shareholder/add',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 更新股东穿透分析
export function updateShareholderAnalysis(data) {
  return request({
    url: '/monitor/v1/supervision/equity/shareholder/update',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 删除股东穿透分析
export function deleteShareholderAnalysis(id) {
  return request({
    url: `/monitor/v1/supervision/equity/shareholder/${id}`,
    method: 'delete',
  })
}

// 执行股东穿透分析
export function performShareholderAnalysis(data) {
  return request({
    url: '/monitor/v1/supervision/equity/shareholder/analyze',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 获取股东层级结构
export function getShareholderHierarchy(data) {
  return request({
    url: '/monitor/v1/supervision/equity/shareholder/hierarchy',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 获取股东穿透路径
export function getShareholderPath(data) {
  return request({
    url: '/monitor/v1/supervision/equity/shareholder/path',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 识别最终股东
export function identifyUltimateShareholders(data) {
  return request({
    url: '/monitor/v1/supervision/equity/shareholder/ultimate',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 分析股东关联关系
export function analyzeShareholderRelations(data) {
  return request({
    url: '/monitor/v1/supervision/equity/shareholder/relations',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 获取股东变更历史
export function getShareholderChangeHistory(data) {
  return request({
    url: '/monitor/v1/supervision/equity/shareholder/history',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 计算股东影响力
export function calculateShareholderInfluence(data) {
  return request({
    url: '/monitor/v1/supervision/equity/shareholder/influence',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 检测股东异常
export function detectShareholderAnomalies(data) {
  return request({
    url: '/monitor/v1/supervision/equity/shareholder/anomalies',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 获取综合统计数据
export function getShareholderStatistics(data) {
  return request({
    url: '/monitor/v1/supervision/equity/shareholder/statistics',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 获取股东分布分析
export function getShareholderDistribution(data) {
  return request({
    url: '/monitor/v1/supervision/equity/shareholder/distribution',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 生成股东穿透报告
export function generateShareholderReport(data) {
  return request({
    url: '/monitor/v1/supervision/equity/shareholder/report',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 批量更新状态
export function batchUpdateShareholderStatus(data) {
  return request({
    url: '/monitor/v1/supervision/equity/shareholder/batch/status',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 批量删除
export function batchDeleteShareholder(data) {
  return request({
    url: '/monitor/v1/supervision/equity/shareholder/batch/delete',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 导出数据
export function exportShareholderData(data) {
  return request({
    url: '/monitor/v1/supervision/equity/shareholder/export',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
    responseType: 'blob',
  })
}

// 导入数据
export function importShareholderData(data) {
  return request({
    url: '/monitor/v1/supervision/equity/shareholder/import',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'multipart/form-data' },
  })
}

// 获取股东网络图数据
export function getShareholderNetworkData(data) {
  return request({
    url: '/monitor/v1/supervision/equity/shareholder/network',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 分析股东集中度
export function analyzeShareholderConcentration(data) {
  return request({
    url: '/monitor/v1/supervision/equity/shareholder/concentration',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 获取股东风险评估
export function getShareholderRiskAssessment(data) {
  return request({
    url: '/monitor/v1/supervision/equity/shareholder/risk',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 股东合规性检查
export function checkShareholderCompliance(data) {
  return request({
    url: '/monitor/v1/supervision/equity/shareholder/compliance',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 获取股东价值评估
export function getShareholderValuation(data) {
  return request({
    url: '/monitor/v1/supervision/equity/shareholder/valuation',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 导出股东穿透分析（统一导出接口）
export function exportShareholderAnalysis() {
  return request({ url: '/monitor/v1/supervision/export/equity/shareholderAnalysis', method: 'get', responseType: 'blob' })
}
