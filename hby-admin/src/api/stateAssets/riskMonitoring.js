import request from '@/utils/request'
import { transData } from '@/utils/requestData'

/**
 * 风险监控API接口
 * 国资国企穿透式监管系统 - 风险穿透管控模块 - 风险监控功能
 */

// ==================== 基础CRUD操作 ====================

/**
 * 分页查询风险监控列表
 */
export function getRiskMonitoringList(data) {
  return request({
    url: '/monitor/v1/supervision/risk/monitoring/list',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 根据ID获取风险监控详情
 */
export function getRiskMonitoringById(id) {
  return request({
    url: `/monitor/v1/supervision/risk/monitoring/${id}`,
    method: 'get'
  })
}

/**
 * 新增风险监控
 */
export function addRiskMonitoring(data) {
  return request({
    url: '/monitor/v1/supervision/risk/monitoring/add',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 更新风险监控
 */
export function updateRiskMonitoring(data) {
  return request({
    url: '/monitor/v1/supervision/risk/monitoring/update',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 删除风险监控
 */
export function deleteRiskMonitoring(id) {
  return request({
    url: `/monitor/v1/supervision/risk/monitoring/${id}`,
    method: 'delete'
  })
}

// ==================== 实时监控业务 ====================

/**
 * 启动实时监控
 */
export function startRealTimeMonitoring(data) {
  return request({
    url: '/monitor/v1/supervision/risk/monitoring/start-realtime',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 停止实时监控
 */
export function stopRealTimeMonitoring(id) {
  return request({
    url: `/monitor/v1/supervision/risk/monitoring/stop-realtime/${id}`,
    method: 'post'
  })
}

/**
 * 获取活跃监控列表
 */
export function getActiveMonitoring() {
  return request({
    url: '/monitor/v1/supervision/risk/monitoring/active',
    method: 'get'
  })
}

/**
 * 更新监控数据
 */
export function updateMonitoringData(data) {
  return request({
    url: '/monitor/v1/supervision/risk/monitoring/update-data',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 执行监控任务
 */
export function executeMonitoringTask(id) {
  return request({
    url: `/monitor/v1/supervision/risk/monitoring/execute-task/${id}`,
    method: 'post'
  })
}

// ==================== 阈值监控业务 ====================

/**
 * 设置监控阈值
 */
export function setMonitoringThresholds(data) {
  return request({
    url: '/monitor/v1/supervision/risk/monitoring/set-thresholds',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 检查阈值超限
 */
export function checkThresholdExceeded(data) {
  return request({
    url: '/monitor/v1/supervision/risk/monitoring/check-threshold',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取超阈值监控列表
 */
export function getThresholdExceededMonitoring() {
  return request({
    url: '/monitor/v1/supervision/risk/monitoring/threshold-exceeded',
    method: 'get'
  })
}

/**
 * 获取接近阈值的监控列表
 */
export function getNearThresholdMonitoring(data) {
  return request({
    url: '/monitor/v1/supervision/risk/monitoring/near-threshold',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 计算阈值达成率
 */
export function calculateThresholdAchievementRate(data) {
  return request({
    url: '/monitor/v1/supervision/risk/monitoring/threshold-achievement-rate',
    method: 'post',
    data: transData(data)
  })
}

// ==================== 预警管理业务 ====================

/**
 * 触发预警
 */
export function triggerAlert(data) {
  return request({
    url: '/monitor/v1/supervision/risk/monitoring/trigger-alert',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 确认预警
 */
export function confirmAlert(data) {
  return request({
    url: '/monitor/v1/supervision/risk/monitoring/confirm-alert',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取未确认预警列表
 */
export function getUnconfirmedAlerts() {
  return request({
    url: '/monitor/v1/supervision/risk/monitoring/unconfirmed-alerts',
    method: 'get'
  })
}

/**
 * 获取触发预警的监控列表
 */
export function getTriggeredAlerts() {
  return request({
    url: '/monitor/v1/supervision/risk/monitoring/triggered-alerts',
    method: 'get'
  })
}

/**
 * 发送预警通知
 */
export function sendAlertNotification(data) {
  return request({
    url: '/monitor/v1/supervision/risk/monitoring/send-notification',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取预警统计数据
 */
export function getAlertStatistics() {
  return request({
    url: '/monitor/v1/supervision/risk/monitoring/alert-statistics',
    method: 'get'
  })
}

// ==================== 异常检测业务 ====================

/**
 * 执行异常检测
 */
export function performAnomalyDetection(id) {
  return request({
    url: `/monitor/v1/supervision/risk/monitoring/anomaly-detection/${id}`,
    method: 'post'
  })
}

/**
 * 检测数据异常
 */
export function detectDataAnomaly(data) {
  return request({
    url: '/monitor/v1/supervision/risk/monitoring/detect-anomaly',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 分析异常原因
 */
export function analyzeAnomalyCause(data) {
  return request({
    url: '/monitor/v1/supervision/risk/monitoring/analyze-anomaly-cause',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取异常监控列表
 */
export function getAnomalyMonitoring() {
  return request({
    url: '/monitor/v1/supervision/risk/monitoring/anomaly-list',
    method: 'get'
  })
}

// ==================== 趋势分析业务 ====================

/**
 * 分析指标趋势
 */
export function analyzeIndicatorTrend(data) {
  return request({
    url: '/monitor/v1/supervision/risk/monitoring/analyze-trend',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 预测未来趋势
 */
export function predictFutureTrend(data) {
  return request({
    url: '/monitor/v1/supervision/risk/monitoring/predict-trend',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 识别趋势转折点
 */
export function identifyTrendTurningPoints(data) {
  return request({
    url: '/monitor/v1/supervision/risk/monitoring/identify-turning-points',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取趋势分析报告
 */
export function getTrendAnalysisReport(data) {
  return request({
    url: '/monitor/v1/supervision/risk/monitoring/trend-analysis-report',
    method: 'post',
    data: transData(data)
  })
}

// ==================== 监控配置业务 ====================

/**
 * 配置自动监控
 */
export function configureAutoMonitoring(data) {
  return request({
    url: '/monitor/v1/supervision/risk/monitoring/configure-auto',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 更新监控配置
 */
export function updateMonitoringConfiguration(data) {
  return request({
    url: '/monitor/v1/supervision/risk/monitoring/update-configuration',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取监控配置
 */
export function getMonitoringConfiguration(id) {
  return request({
    url: `/monitor/v1/supervision/risk/monitoring/configuration/${id}`,
    method: 'get'
  })
}

// ==================== 统计分析业务 ====================

/**
 * 获取监控综合统计
 */
export function getMonitoringStatistics() {
  return request({
    url: '/monitor/v1/supervision/risk/monitoring/statistics',
    method: 'get'
  })
}

/**
 * 获取企业监控概览
 */
export function getEnterpriseMonitoringOverview(enterpriseId) {
  return request({
    url: `/monitor/v1/supervision/risk/monitoring/enterprise-overview/${enterpriseId}`,
    method: 'get'
  })
}

/**
 * 获取监控类型分布
 */
export function getMonitoringTypeDistribution() {
  return request({
    url: '/monitor/v1/supervision/risk/monitoring/type-distribution',
    method: 'get'
  })
}

/**
 * 获取监控效果统计
 */
export function getMonitoringEffectivenessStatistics() {
  return request({
    url: '/monitor/v1/supervision/risk/monitoring/effectiveness-statistics',
    method: 'get'
  })
}

// ==================== 批量操作业务 ====================

/**
 * 批量更新监控状态
 */
export function batchUpdateMonitoringStatus(data) {
  return request({
    url: '/monitor/v1/supervision/risk/monitoring/batch-update-status',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 批量确认预警
 */
export function batchConfirmAlerts(data) {
  return request({
    url: '/monitor/v1/supervision/risk/monitoring/batch-confirm-alerts',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 批量更新阈值
 */
export function batchUpdateThresholds(data) {
  return request({
    url: '/monitor/v1/supervision/risk/monitoring/batch-update-thresholds',
    method: 'post',
    data: transData(data)
  })
}

// ==================== 导出功能 ====================

/**
 * 导出监控数据
 */
export function exportMonitoringData(data) {
  return request({
    url: '/monitor/v1/supervision/risk/monitoring/export-data',
    method: 'post',
    data: transData(data),
    responseType: 'blob'
  })
}

/**
 * 导出监控报告
 */
export function exportMonitoringReport(data) {
  return request({
    url: '/monitor/v1/supervision/risk/monitoring/export-report',
    method: 'post',
    data: transData(data),
    responseType: 'blob'
  })
}
