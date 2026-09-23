import request from '@/utils/request'
import { transData } from '@/utils/requestData'

const JSON_HEADERS = { 'Content-Type': 'application/json;charset=UTF-8' }

/**
 * 股权变动监控API
 */

// 分页查询股权变动列表
export function getEquityChangesList(data) {
  return request({
    url: '/monitor/v1/supervision/equity/changes/list',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 根据ID查询股权变动详情
export function getEquityChangeById(id) {
  return request({
    url: `/monitor/v1/supervision/equity/changes/${id}`,
    method: 'get',
  })
}

// 新增股权变动记录
export function addEquityChange(data) {
  return request({
    url: '/monitor/v1/supervision/equity/changes/add',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 更新股权变动记录
export function updateEquityChange(data) {
  return request({
    url: '/monitor/v1/supervision/equity/changes/update',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 删除股权变动记录
export function deleteEquityChange(id) {
  return request({
    url: `/monitor/v1/supervision/equity/changes/${id}`,
    method: 'delete',
  })
}

// 获取股权变动统计信息
export function getEquityChangesStatistics(data) {
  return request({
    url: '/monitor/v1/supervision/equity/changes/statistics',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 分析股权变动影响
export function analyzeEquityChangeImpact(data) {
  return request({
    url: '/monitor/v1/supervision/equity/changes/impact/analyze',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 批量分析股权变动
export function batchAnalyzeEquityChanges(data) {
  return request({
    url: '/monitor/v1/supervision/equity/changes/batch/analyze',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 审核股权变动
export function approveEquityChange(data) {
  return request({
    url: '/monitor/v1/supervision/equity/changes/approve',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 拒绝股权变动
export function rejectEquityChange(data) {
  return request({
    url: '/monitor/v1/supervision/equity/changes/reject',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 获取股权变动风险评估
export function assessEquityChangeRisk(data) {
  return request({
    url: '/monitor/v1/supervision/equity/changes/risk/assess',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 获取股权变动趋势分析
export function analyzeEquityChangeTrends(data) {
  return request({
    url: '/monitor/v1/supervision/equity/changes/trends',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 检测异常股权变动
export function detectAbnormalEquityChanges(data) {
  return request({
    url: '/monitor/v1/supervision/equity/changes/abnormal/detect',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 获取股权变动预警
export function getEquityChangeAlerts(data) {
  return request({
    url: '/monitor/v1/supervision/equity/changes/alerts',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 模拟股权变动影响
export function simulateEquityChangeImpact(data) {
  return request({
    url: '/monitor/v1/supervision/equity/changes/simulate',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 获取股权变动建议
export function getEquityChangeRecommendations(data) {
  return request({
    url: '/monitor/v1/supervision/equity/changes/recommendations',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 验证股权变动合规性
export function validateEquityChangeCompliance(data) {
  return request({
    url: '/monitor/v1/supervision/equity/changes/compliance/validate',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 生成股权变动报告
export function generateEquityChangeReport(data) {
  return request({
    url: '/monitor/v1/supervision/equity/changes/report/generate',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 导出股权变动数据
export function exportEquityChangesData(data) {
  return request({
    url: '/monitor/v1/supervision/equity/changes/export',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
    responseType: 'blob',
  })
}

// 导入股权变动数据
export function importEquityChangesData(data) {
  return request({
    url: '/monitor/v1/supervision/equity/changes/import',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'multipart/form-data' },
  })
}

// 获取股权变动历史记录
export function getEquityChangeHistory(data) {
  return request({
    url: '/monitor/v1/supervision/equity/changes/history',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 对比股权变动前后
export function compareEquityChanges(data) {
  return request({
    url: '/monitor/v1/supervision/equity/changes/compare',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 获取股权变动关联分析
export function analyzeEquityChangeRelations(data) {
  return request({
    url: '/monitor/v1/supervision/equity/changes/relations/analyze',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 股权变动实时监控
export function monitorEquityChangesRealtime(data) {
  return request({
    url: '/monitor/v1/supervision/equity/changes/realtime/monitor',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 设置股权变动监控规则
export function setEquityChangeMonitorRules(data) {
  return request({
    url: '/monitor/v1/supervision/equity/changes/monitor/rules/set',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 获取股权变动监控规则
export function getEquityChangeMonitorRules(data) {
  return request({
    url: '/monitor/v1/supervision/equity/changes/monitor/rules/list',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 股权变动智能预测
export function predictEquityChanges(data) {
  return request({
    url: '/monitor/v1/supervision/equity/changes/predict',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 获取股权变动影响评估报告
export function getEquityChangeImpactReport(data) {
  return request({
    url: '/monitor/v1/supervision/equity/changes/impact/report',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 股权变动合规检查
export function checkEquityChangeCompliance(data) {
  return request({
    url: '/monitor/v1/supervision/equity/changes/compliance/check',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 获取股权变动审批流程
export function getEquityChangeApprovalFlow(data) {
  return request({
    url: '/monitor/v1/supervision/equity/changes/approval/flow',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 启动股权变动审批流程
export function startEquityChangeApprovalFlow(data) {
  return request({
    url: '/monitor/v1/supervision/equity/changes/approval/start',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 获取股权变动审批历史
export function getEquityChangeApprovalHistory(data) {
  return request({
    url: '/monitor/v1/supervision/equity/changes/approval/history',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

// 导出股权变动（统一导出接口）
export function exportEquityChanges() {
  return request({ url: '/monitor/v1/supervision/export/equity/changeRecord', method: 'get', responseType: 'blob' })
}
