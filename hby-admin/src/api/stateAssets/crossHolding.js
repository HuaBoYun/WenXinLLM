import request from '@/utils/request'
import { transData } from '@/utils/requestData'

const BASE = '/monitor/v1/supervision/asset/cross-holding'

/**
 * 交叉持股分析API
 */

// 分页查询交叉持股分析列表
export function getCrossHoldingList(data) {
  return request({ url: `${BASE}/list`, method: 'post', data: transData(data) })
}

// 根据ID查询交叉持股详情
export function getCrossHoldingById(id) {
  return request({ url: `${BASE}/${id}`, method: 'get' })
}

// 新增交叉持股分析
export function addCrossHolding(data) {
  return request({ url: `${BASE}/add`, method: 'post', data: transData(data) })
}

// 更新交叉持股分析
export function updateCrossHolding(data) {
  return request({ url: `${BASE}/update`, method: 'post', data: transData(data) })
}

// 删除交叉持股分析
export function deleteCrossHolding(id) {
  return request({ url: `${BASE}/${id}`, method: 'delete' })
}

// 检测交叉持股关系
export function detectCrossHolding(data) {
  return request({ url: `${BASE}/detect`, method: 'post', data: transData(data) })
}

// 分析交叉持股网络
export function analyzeCrossHoldingNetwork(data) {
  return request({ url: `${BASE}/network`, method: 'post', data: transData(data) })
}

// 识别交叉持股环路
export function identifyCrossHoldingLoops(data) {
  return request({ url: `${BASE}/loops`, method: 'post', data: transData(data) })
}

// 计算交叉持股强度
export function calculateCrossHoldingIntensity(data) {
  return request({ url: `${BASE}/intensity`, method: 'post', data: transData(data) })
}

// 分析交叉持股层级
export function analyzeCrossHoldingLevels(data) {
  return request({ url: `${BASE}/levels`, method: 'post', data: transData(data) })
}

// 评估交叉持股风险
export function assessCrossHoldingRisk(data) {
  return request({ url: `${BASE}/risk`, method: 'post', data: transData(data) })
}

// 分析交叉持股稳定性
export function analyzeCrossHoldingStability(data) {
  return request({ url: `${BASE}/stability`, method: 'post', data: transData(data) })
}

// 检测异常交叉持股
export function detectAnomalousCrossHolding(data) {
  return request({ url: `${BASE}/anomalies`, method: 'post', data: transData(data) })
}

// 分析交叉持股影响
export function analyzeCrossHoldingImpact(data) {
  return request({ url: `${BASE}/impact`, method: 'post', data: transData(data) })
}

// 模拟交叉持股变化
export function simulateCrossHoldingChange(data) {
  return request({ url: `${BASE}/simulate`, method: 'post', data: transData(data) })
}

// 优化交叉持股结构
export function optimizeCrossHoldingStructure(data) {
  return request({ url: `${BASE}/optimize`, method: 'post', data: transData(data) })
}

// 分析交叉持股趋势
export function analyzeCrossHoldingTrend(data) {
  return request({ url: `${BASE}/trend`, method: 'post', data: transData(data) })
}

// 对比交叉持股分析
export function compareCrossHolding(data) {
  return request({ url: `${BASE}/compare`, method: 'post', data: transData(data) })
}

// 获取交叉持股图谱
export function getCrossHoldingChart(data) {
  return request({ url: `${BASE}/chart`, method: 'post', data: transData(data) })
}

// 分析交叉持股集群
export function analyzeCrossHoldingClusters(data) {
  return request({ url: `${BASE}/clusters`, method: 'post', data: transData(data) })
}

// 计算交叉持股密度
export function calculateCrossHoldingDensity(data) {
  return request({ url: `${BASE}/density`, method: 'post', data: transData(data) })
}

// 分析交叉持股传导效应
export function analyzeCrossHoldingContagion(data) {
  return request({ url: `${BASE}/contagion`, method: 'post', data: transData(data) })
}

// 评估交叉持股合规性
export function assessCrossHoldingCompliance(data) {
  return request({ url: `${BASE}/compliance`, method: 'post', data: transData(data) })
}

// 获取交叉持股统计
export function getCrossHoldingStatistics(data) {
  return request({ url: `${BASE}/statistics`, method: 'post', data: transData(data) })
}

// 生成交叉持股报告
export function generateCrossHoldingReport(data) {
  return request({ url: `${BASE}/report`, method: 'post', data: transData(data) })
}

// 批量检测交叉持股
export function batchDetectCrossHolding(data) {
  return request({ url: `${BASE}/batch/detect`, method: 'post', data: transData(data) })
}

// 批量删除交叉持股
export function batchDeleteCrossHolding(data) {
  return request({ url: `${BASE}/batch/delete`, method: 'post', data: transData(data) })
}

// 导出交叉持股数据
export function exportCrossHoldingData(data) {
  return request({ url: `${BASE}/export`, method: 'post', data: transData(data), responseType: 'blob' })
}

// 导入交叉持股数据
export function importCrossHoldingData(data) {
  return request({ url: `${BASE}/import`, method: 'post', data, headers: { 'Content-Type': 'multipart/form-data' } })
}

// 监控交叉持股变化
export function monitorCrossHoldingChanges(data) {
  return request({ url: `${BASE}/monitor`, method: 'post', data: transData(data) })
}

// 导出交叉持股（统一导出接口）
export function exportCrossHolding() {
  return request({ url: '/monitor/v1/supervision/export/asset/crossHolding', method: 'get', responseType: 'blob' })
}
