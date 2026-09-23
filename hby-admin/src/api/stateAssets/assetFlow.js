import request from '@/utils/request'
import { transData } from '@/utils/requestData'

const BASE = '/monitor/v1/supervision/asset/flow'

/**
 * 资产流向追踪API
 */

// 分页查询资产流向列表
export function getAssetFlowList(data) {
  return request({ url: `${BASE}/list`, method: 'post', data: transData(data) })
}

// 根据ID查询资产流向详情
export function getAssetFlowById(id) {
  return request({ url: `${BASE}/${id}`, method: 'get' })
}

// 新增资产流向记录
export function addAssetFlow(data) {
  return request({ url: `${BASE}/add`, method: 'post', data: transData(data) })
}

// 更新资产流向记录
export function updateAssetFlow(data) {
  return request({ url: `${BASE}/update`, method: 'post', data: transData(data) })
}

// 删除资产流向记录
export function deleteAssetFlow(id) {
  return request({ url: `${BASE}/${id}`, method: 'delete' })
}

// 追踪资产流向
export function traceAssetFlow(data) {
  return request({ url: `${BASE}/trace`, method: 'post', data: transData(data) })
}

// 分析资产流向路径
export function analyzeAssetFlowPath(data) {
  return request({ url: `${BASE}/path`, method: 'post', data: transData(data) })
}

// 构建资产流向图
export function buildAssetFlowChart(data) {
  return request({ url: `${BASE}/chart`, method: 'post', data: transData(data) })
}

// 分析资产流向模式
export function analyzeAssetFlowPattern(data) {
  return request({ url: `${BASE}/pattern`, method: 'post', data: transData(data) })
}

// 检测异常资产流向
export function detectAbnormalAssetFlow(data) {
  return request({ url: `${BASE}/abnormal`, method: 'post', data: transData(data) })
}

// 分析资产流向速度
export function analyzeAssetFlowVelocity(data) {
  return request({ url: `${BASE}/velocity`, method: 'post', data: transData(data) })
}

// 计算资产流向强度
export function calculateAssetFlowIntensity(data) {
  return request({ url: `${BASE}/intensity`, method: 'post', data: transData(data) })
}

// 分析资产流向趋势
export function analyzeAssetFlowTrend(data) {
  return request({ url: `${BASE}/trend`, method: 'post', data: transData(data) })
}

// 预测资产流向
export function predictAssetFlow(data) {
  return request({ url: `${BASE}/predict`, method: 'post', data: transData(data) })
}

// 分析资产流向集中度
export function analyzeAssetFlowConcentration(data) {
  return request({ url: `${BASE}/concentration`, method: 'post', data: transData(data) })
}

// 识别资产流向关键节点
export function identifyAssetFlowKeyNodes(data) {
  return request({ url: `${BASE}/key-nodes`, method: 'post', data: transData(data) })
}

// 分析资产流向网络
export function analyzeAssetFlowNetwork(data) {
  return request({ url: `${BASE}/network`, method: 'post', data: transData(data) })
}

// 评估资产流向风险
export function assessAssetFlowRisk(data) {
  return request({ url: `${BASE}/risk`, method: 'post', data: transData(data) })
}

// 监控资产流向实时状态
export function monitorAssetFlowRealtime(data) {
  return request({ url: `${BASE}/realtime`, method: 'post', data: transData(data) })
}

// 分析资产流向周期性
export function analyzeAssetFlowCyclicity(data) {
  return request({ url: `${BASE}/cyclicity`, method: 'post', data: transData(data) })
}

// 对比资产流向
export function compareAssetFlow(data) {
  return request({ url: `${BASE}/compare`, method: 'post', data: transData(data) })
}

// 分析资产流向影响因素
export function analyzeAssetFlowFactors(data) {
  return request({ url: `${BASE}/factors`, method: 'post', data: transData(data) })
}

// 优化资产流向配置
export function optimizeAssetFlowConfiguration(data) {
  return request({ url: `${BASE}/optimize`, method: 'post', data: transData(data) })
}

// 模拟资产流向变化
export function simulateAssetFlowChange(data) {
  return request({ url: `${BASE}/simulate`, method: 'post', data: transData(data) })
}

// 分析资产流向稳定性
export function analyzeAssetFlowStability(data) {
  return request({ url: `${BASE}/stability`, method: 'post', data: transData(data) })
}

// 获取资产流向统计
export function getAssetFlowStatistics(data) {
  return request({ url: `${BASE}/statistics`, method: 'post', data: transData(data) })
}

// 生成资产流向报告
export function generateAssetFlowReport(data) {
  return request({ url: `${BASE}/report`, method: 'post', data: transData(data) })
}

// 批量追踪资产流向
export function batchTraceAssetFlow(data) {
  return request({ url: `${BASE}/batch/trace`, method: 'post', data: transData(data) })
}

// 批量删除资产流向
export function batchDeleteAssetFlow(data) {
  return request({ url: `${BASE}/batch/delete`, method: 'post', data: transData(data) })
}

// 导出资产流向数据
export function exportAssetFlowData(data) {
  return request({ url: `${BASE}/export`, method: 'post', data: transData(data), responseType: 'blob' })
}

// 导入资产流向数据
export function importAssetFlowData(data) {
  return request({ url: `${BASE}/import`, method: 'post', data, headers: { 'Content-Type': 'multipart/form-data' } })
}

// 设置资产流向预警
export function setAssetFlowAlert(data) {
  return request({ url: `${BASE}/alert`, method: 'post', data: transData(data) })
}

// 分析资产流向合规性
export function analyzeAssetFlowCompliance(data) {
  return request({ url: `${BASE}/compliance`, method: 'post', data: transData(data) })
}

// 导出资产流向（统一导出接口）
export function exportAssetFlow() {
  return request({ url: '/monitor/v1/supervision/export/asset/flow', method: 'get', responseType: 'blob' })
}
