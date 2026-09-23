import request from '@/utils/request'
import { transData } from '@/utils/requestData'

const JSON_HEADERS = { 'Content-Type': 'application/json;charset=UTF-8' }

/**
 * 股权结构管理API
 */

// 分页查询股权结构列表
export function getEquityStructureList(data) {
  return request({
    url: '/monitor/v1/supervision/equity/structure/list',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 根据ID查询股权结构详情
export function getEquityStructureById(id) {
  return request({
    url: `/monitor/v1/supervision/equity/structure/${id}`,
    method: 'get',
  })
}

// 新增股权结构
export function addEquityStructure(data) {
  return request({
    url: '/monitor/v1/supervision/equity/structure/add',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 更新股权结构
export function updateEquityStructure(data) {
  return request({
    url: '/monitor/v1/supervision/equity/structure/update',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 删除股权结构
export function deleteEquityStructure(id) {
  return request({
    url: `/monitor/v1/supervision/equity/structure/delete/${id}`,
    method: 'delete',
  })
}

// 构建股权结构图
export function buildEquityStructureChart(data) {
  return request({
    url: '/monitor/v1/supervision/equity/structure/chart',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 获取股权穿透图谱数据
export function getEquityPenetrationChart(data) {
  return request({
    url: '/monitor/v1/supervision/equity/penetration/chart',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 执行股权结构深度分析
export function analyzeEquityStructure(data) {
  return request({
    url: '/monitor/v1/supervision/equity/structure/analyze',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 分析股权结构变化
export function analyzeEquityStructureChanges(data) {
  return request({
    url: '/monitor/v1/supervision/equity/structure/changes',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 获取股权结构层级
export function getEquityStructureLevels(data) {
  return request({
    url: '/monitor/v1/supervision/equity/structure/levels',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 计算股权比例
export function calculateEquityRatios(data) {
  return request({
    url: '/monitor/v1/supervision/equity/structure/ratios',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 识别控制关系
export function identifyControlRelations(data) {
  return request({
    url: '/monitor/v1/supervision/equity/structure/control',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 分析股权稳定性
export function analyzeEquityStability(data) {
  return request({
    url: '/monitor/v1/supervision/equity/structure/stability',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 检测股权异常
export function detectEquityAnomalies(data) {
  return request({
    url: '/monitor/v1/supervision/equity/structure/anomalies',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 获取股权结构对比
export function compareEquityStructures(data) {
  return request({
    url: '/monitor/v1/supervision/equity/structure/compare',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 模拟股权结构变更
export function simulateEquityChanges(data) {
  return request({
    url: '/monitor/v1/supervision/equity/structure/simulate',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 评估股权结构风险
export function assessEquityRisk(data) {
  return request({
    url: '/monitor/v1/supervision/equity/structure/risk',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 获取股权结构建议
export function getEquityRecommendations(data) {
  return request({
    url: '/monitor/v1/supervision/equity/structure/recommendations',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 验证股权结构合规性
export function validateEquityCompliance(data) {
  return request({
    url: '/monitor/v1/supervision/equity/structure/compliance',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 获取股权结构统计
export function getEquityStatistics(data) {
  return request({
    url: '/monitor/v1/supervision/equity/structure/statistics',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 生成股权结构报告
export function generateEquityReport(data) {
  return request({
    url: '/monitor/v1/supervision/equity/structure/report',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 批量更新股权信息
export function batchUpdateEquity(data) {
  return request({
    url: '/monitor/v1/supervision/equity/structure/batch/update',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 批量删除股权记录
export function batchDeleteEquity(data) {
  return request({
    url: '/monitor/v1/supervision/equity/structure/batch/delete',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 导出股权结构数据
export function exportEquityData(data) {
  return request({
    url: '/monitor/v1/supervision/equity/structure/export',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
    responseType: 'blob',
  })
}

// 导入股权结构数据
export function importEquityData(data) {
  return request({
    url: '/monitor/v1/supervision/equity/structure/import',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'multipart/form-data' },
  })
}

// 获取股权结构树形数据
export function getEquityTreeData(data) {
  return request({
    url: '/monitor/v1/supervision/equity/structure/tree',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 分析股权集中度
export function analyzeEquityConcentration(data) {
  return request({
    url: '/monitor/v1/supervision/equity/structure/concentration',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 获取股权变更历史
export function getEquityChangeHistory(data) {
  return request({
    url: '/monitor/v1/supervision/equity/structure/history',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 股权结构优化建议
export function optimizeEquityStructure(data) {
  return request({
    url: '/monitor/v1/supervision/equity/structure/optimize',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 获取股权穿透树（递归嵌套结构，支持层级控制）
export function getEquityPenetrationTree(data) {
  return request({
    url: '/monitor/v1/supervision/equity/structure/penetration/tree',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 导出股权结构（统一导出接口）
export function exportEquityStructure() {
  return request({ url: '/monitor/v1/supervision/export/equity/structure', method: 'get', responseType: 'blob' })
}
