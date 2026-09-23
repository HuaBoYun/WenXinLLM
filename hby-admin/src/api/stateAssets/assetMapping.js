import request from '@/utils/request'
import { transData } from '@/utils/requestData'

const BASE = '/monitor/v1/supervision/asset/mapping'

/**
 * 资产映射管理API
 */

// 分页查询资产映射列表
export function getAssetMappingList(data) {
  return request({ url: `${BASE}/list`, method: 'post', data: transData(data) })
}

// 根据ID查询资产映射详情
export function getAssetMappingById(id) {
  return request({ url: `${BASE}/${id}`, method: 'get' })
}

// 新增资产映射
export function addAssetMapping(data) {
  return request({ url: `${BASE}/add`, method: 'post', data: transData(data) })
}

// 更新资产映射
export function updateAssetMapping(data) {
  return request({ url: `${BASE}/update`, method: 'post', data: transData(data) })
}

// 删除资产映射
export function deleteAssetMapping(id) {
  return request({ url: `${BASE}/${id}`, method: 'delete' })
}

// 构建资产映射关系
export function buildAssetMapping(data) {
  return request({ url: `${BASE}/build`, method: 'post', data: transData(data) })
}

// 分析资产关联关系
export function analyzeAssetRelations(data) {
  return request({ url: `${BASE}/relations`, method: 'post', data: transData(data) })
}

// 获取资产映射图谱
export function getAssetMappingChart(data) {
  return request({ url: `${BASE}/chart`, method: 'post', data: transData(data) })
}

// 追踪资产流向
export function traceAssetFlow(data) {
  return request({ url: `${BASE}/trace`, method: 'post', data: transData(data) })
}

// 识别资产集群
export function identifyAssetClusters(data) {
  return request({ url: `${BASE}/clusters`, method: 'post', data: transData(data) })
}

// 检测资产异常
export function detectAssetAnomalies(data) {
  return request({ url: `${BASE}/anomalies`, method: 'post', data: transData(data) })
}

// 分析资产价值分布
export function analyzeAssetValueDistribution(data) {
  return request({ url: `${BASE}/value-distribution`, method: 'post', data: transData(data) })
}

// 获取资产映射统计
export function getAssetMappingStatistics(data) {
  return request({ url: `${BASE}/statistics`, method: 'post', data: transData(data) })
}

// 验证资产映射准确性
export function validateAssetMapping(data) {
  return request({ url: `${BASE}/validate`, method: 'post', data: transData(data) })
}

// 同步资产数据
export function syncAssetData(data) {
  return request({ url: `${BASE}/sync`, method: 'post', data: transData(data) })
}

// 获取资产变更历史
export function getAssetChangeHistory(data) {
  return request({ url: `${BASE}/history`, method: 'post', data: transData(data) })
}

// 分析资产风险
export function analyzeAssetRisk(data) {
  return request({ url: `${BASE}/risk`, method: 'post', data: transData(data) })
}

// 生成资产映射报告
export function generateAssetMappingReport(data) {
  return request({ url: `${BASE}/report`, method: 'post', data: transData(data) })
}

// 批量更新资产映射
export function batchUpdateAssetMapping(data) {
  return request({ url: `${BASE}/batch/update`, method: 'post', data: transData(data) })
}

// 批量删除资产映射
export function batchDeleteAssetMapping(data) {
  return request({ url: `${BASE}/batch/delete`, method: 'post', data: transData(data) })
}

// 导出资产映射数据
export function exportAssetMappingData(data) {
  return request({ url: `${BASE}/export`, method: 'post', data: transData(data), responseType: 'blob' })
}

// 导入资产映射数据
export function importAssetMappingData(data) {
  return request({ url: `${BASE}/import`, method: 'post', data, headers: { 'Content-Type': 'multipart/form-data' } })
}

// 获取资产网络数据
export function getAssetNetworkData(data) {
  return request({ url: `${BASE}/network`, method: 'post', data: transData(data) })
}

// 分析资产依赖关系
export function analyzeAssetDependencies(data) {
  return request({ url: `${BASE}/dependencies`, method: 'post', data: transData(data) })
}

// 优化资产配置
export function optimizeAssetAllocation(data) {
  return request({ url: `${BASE}/optimize`, method: 'post', data: transData(data) })
}

// 评估资产质量
export function assessAssetQuality(data) {
  return request({ url: `${BASE}/quality`, method: 'post', data: transData(data) })
}

// 监控资产状态
export function monitorAssetStatus(data) {
  return request({ url: `${BASE}/monitor`, method: 'post', data: transData(data) })
}

// 导出资产映射（统一导出接口）
export function exportAssetMapping() {
  return request({ url: '/monitor/v1/supervision/export/asset/mapping', method: 'get', responseType: 'blob' })
}
