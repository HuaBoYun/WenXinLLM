import request from '@/utils/request'
import { transData } from '@/utils/requestData'

const BASE = '/monitor/v1/supervision/asset/concentration'

/**
 * 资产集中度分析API
 */

// 分页查询资产集中度分析列表
export function getAssetConcentrationList(data) {
  return request({ url: `${BASE}/list`, method: 'post', data: transData(data) })
}

// 根据ID查询资产集中度详情
export function getAssetConcentrationById(id) {
  return request({ url: `${BASE}/${id}`, method: 'get' })
}

// 新增资产集中度分析
export function addAssetConcentration(data) {
  return request({ url: `${BASE}/add`, method: 'post', data: transData(data) })
}

// 更新资产集中度分析
export function updateAssetConcentration(data) {
  return request({ url: `${BASE}/update`, method: 'post', data: transData(data) })
}

// 删除资产集中度分析
export function deleteAssetConcentration(id) {
  return request({ url: `${BASE}/${id}`, method: 'delete' })
}

// 计算资产集中度指标
export function calculateConcentrationIndex(data) {
  return request({ url: `${BASE}/calculate`, method: 'post', data: transData(data) })
}

// 分析行业集中度
export function analyzeIndustryConcentration(data) {
  return request({ url: `${BASE}/industry`, method: 'post', data: transData(data) })
}

// 分析地域集中度
export function analyzeRegionalConcentration(data) {
  return request({ url: `${BASE}/regional`, method: 'post', data: transData(data) })
}

// 分析资产类型集中度
export function analyzeAssetTypeConcentration(data) {
  return request({ url: `${BASE}/asset-type`, method: 'post', data: transData(data) })
}

// 分析时间集中度
export function analyzeTemporalConcentration(data) {
  return request({ url: `${BASE}/temporal`, method: 'post', data: transData(data) })
}

// 计算HHI指数
export function calculateHHIIndex(data) {
  return request({ url: `${BASE}/hhi`, method: 'post', data: transData(data) })
}

// 计算CR指数
export function calculateCRIndex(data) {
  return request({ url: `${BASE}/cr`, method: 'post', data: transData(data) })
}

// 分析集中度趋势
export function analyzeConcentrationTrend(data) {
  return request({ url: `${BASE}/trend`, method: 'post', data: transData(data) })
}

// 对比集中度分析
export function compareConcentration(data) {
  return request({ url: `${BASE}/compare`, method: 'post', data: transData(data) })
}

// 评估集中度风险
export function assessConcentrationRisk(data) {
  return request({ url: `${BASE}/risk`, method: 'post', data: transData(data) })
}

// 获取集中度预警
export function getConcentrationAlerts(data) {
  return request({ url: `${BASE}/alerts`, method: 'post', data: transData(data) })
}

// 分析集中度影响因素
export function analyzeConcentrationFactors(data) {
  return request({ url: `${BASE}/factors`, method: 'post', data: transData(data) })
}

// 模拟集中度变化
export function simulateConcentrationChange(data) {
  return request({ url: `${BASE}/simulate`, method: 'post', data: transData(data) })
}

// 优化资产分散度
export function optimizeAssetDispersion(data) {
  return request({ url: `${BASE}/optimize`, method: 'post', data: transData(data) })
}

// 获取集中度基准
export function getConcentrationBenchmark(data) {
  return request({ url: `${BASE}/benchmark`, method: 'post', data: transData(data) })
}

// 分析集中度合规性
export function analyzeConcentrationCompliance(data) {
  return request({ url: `${BASE}/compliance`, method: 'post', data: transData(data) })
}

// 获取集中度统计
export function getConcentrationStatistics(data) {
  return request({ url: `${BASE}/statistics`, method: 'post', data: transData(data) })
}

// 生成集中度报告
export function generateConcentrationReport(data) {
  return request({ url: `${BASE}/report`, method: 'post', data: transData(data) })
}

// 批量计算集中度
export function batchCalculateConcentration(data) {
  return request({ url: `${BASE}/batch/calculate`, method: 'post', data: transData(data) })
}

// 批量删除集中度分析
export function batchDeleteConcentration(data) {
  return request({ url: `${BASE}/batch/delete`, method: 'post', data: transData(data) })
}

// 导出集中度数据
export function exportConcentrationData(data) {
  return request({ url: `${BASE}/export`, method: 'post', data: transData(data), responseType: 'blob' })
}

// 导入集中度数据
export function importConcentrationData(data) {
  return request({ url: `${BASE}/import`, method: 'post', data, headers: { 'Content-Type': 'multipart/form-data' } })
}

// 获取集中度图表数据
export function getConcentrationChartData(data) {
  return request({ url: `${BASE}/chart`, method: 'post', data: transData(data) })
}

// 分析集中度稳定性
export function analyzeConcentrationStability(data) {
  return request({ url: `${BASE}/stability`, method: 'post', data: transData(data) })
}

// 导出资产集中度（统一导出接口）
export function exportAssetConcentration() {
  return request({ url: '/monitor/v1/supervision/export/asset/concentration', method: 'get', responseType: 'blob' })
}
