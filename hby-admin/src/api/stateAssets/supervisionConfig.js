import request from '@/utils/request'
import { transData } from '@/utils/requestData'

/**
 * 监管配置API
 */

// 获取监管配置统计数据
export function getSupervisionConfigStatistics() {
  return transData({
    url: '/api/supervision-config/statistics',
    method: 'post',
    data: {}
  })
}

// ==================== 监管规则配置 ====================

// 分页查询监管规则列表
export function getSupervisionRulesList(data) {
  return transData({
    url: '/api/supervision-config/rules/list',
    method: 'post',
    data
  })
}

// 根据ID查询监管规则详情
export function getSupervisionRuleById(id) {
  return transData({
    url: `/api/supervision-config/rules/${id}`,
    method: 'get'
  })
}

// 新增监管规则
export function addSupervisionRule(data) {
  return transData({
    url: '/api/supervision-config/rules/add',
    method: 'post',
    data
  })
}

// 更新监管规则
export function updateSupervisionRule(data) {
  return transData({
    url: '/api/supervision-config/rules/update',
    method: 'post',
    data
  })
}

// 保存监管规则（新增或更新）
export function saveSupervisionRule(data) {
  return transData({
    url: data.id ? '/api/supervision-config/rules/update' : '/api/supervision-config/rules/add',
    method: 'post',
    data
  })
}

// 删除监管规则
export function deleteSupervisionRule(data) {
  return transData({
    url: '/api/supervision-config/rules/delete',
    method: 'post',
    data
  })
}

// 批量删除监管规则
export function batchDeleteSupervisionRules(data) {
  return transData({
    url: '/api/supervision-config/rules/batch-delete',
    method: 'post',
    data
  })
}

// 测试监管规则
export function testSupervisionRule(data) {
  return transData({
    url: '/api/supervision-config/rules/test',
    method: 'post',
    data
  })
}

// 启用/禁用监管规则
export function toggleSupervisionRuleStatus(data) {
  return transData({
    url: '/api/supervision-config/rules/toggle-status',
    method: 'post',
    data
  })
}

// 导出监管规则数据
export function exportSupervisionRulesData(data) {
  return transData({
    url: '/api/supervision-config/rules/export',
    method: 'post',
    data,
    responseType: 'blob'
  })
}

// ==================== 监管指标配置 ====================

// 分页查询监管指标列表
export function getSupervisionIndicatorsList(data) {
  return transData({
    url: '/api/supervision-config/indicators/list',
    method: 'post',
    data
  })
}

// 根据ID查询监管指标详情
export function getSupervisionIndicatorById(id) {
  return transData({
    url: `/api/supervision-config/indicators/${id}`,
    method: 'get'
  })
}

// 新增监管指标
export function addSupervisionIndicator(data) {
  return transData({
    url: '/api/supervision-config/indicators/add',
    method: 'post',
    data
  })
}

// 更新监管指标
export function updateSupervisionIndicator(data) {
  return transData({
    url: '/api/supervision-config/indicators/update',
    method: 'post',
    data
  })
}

// 保存监管指标（新增或更新）
export function saveSupervisionIndicator(data) {
  return transData({
    url: data.id ? '/api/supervision-config/indicators/update' : '/api/supervision-config/indicators/add',
    method: 'post',
    data
  })
}

// 删除监管指标
export function deleteSupervisionIndicator(data) {
  return transData({
    url: '/api/supervision-config/indicators/delete',
    method: 'post',
    data
  })
}

// 批量删除监管指标
export function batchDeleteSupervisionIndicators(data) {
  return transData({
    url: '/api/supervision-config/indicators/batch-delete',
    method: 'post',
    data
  })
}

// 计算监管指标
export function calculateSupervisionIndicator(data) {
  return transData({
    url: '/api/supervision-config/indicators/calculate',
    method: 'post',
    data
  })
}

// 测试指标公式
export function testIndicatorFormula(data) {
  return transData({
    url: '/api/supervision-config/indicators/test-formula',
    method: 'post',
    data
  })
}

// 获取指标计算历史
export function getIndicatorCalculationHistory(data) {
  return transData({
    url: '/api/supervision-config/indicators/calculation-history',
    method: 'post',
    data
  })
}

// 设置指标预警阈值
export function setIndicatorThreshold(data) {
  return transData({
    url: '/api/supervision-config/indicators/set-threshold',
    method: 'post',
    data
  })
}

// 导出监管指标数据
export function exportSupervisionIndicatorsData(data) {
  return transData({
    url: '/api/supervision-config/indicators/export',
    method: 'post',
    data,
    responseType: 'blob'
  })
}

// ==================== 监管流程配置 ====================

// 分页查询监管流程列表
export function getSupervisionProcessesList(data) {
  return transData({
    url: '/api/supervision-config/processes/list',
    method: 'post',
    data
  })
}

// 根据ID查询监管流程详情
export function getSupervisionProcessById(id) {
  return transData({
    url: `/api/supervision-config/processes/${id}`,
    method: 'get'
  })
}

// 新增监管流程
export function addSupervisionProcess(data) {
  return transData({
    url: '/api/supervision-config/processes/add',
    method: 'post',
    data
  })
}

// 更新监管流程
export function updateSupervisionProcess(data) {
  return transData({
    url: '/api/supervision-config/processes/update',
    method: 'post',
    data
  })
}

// 保存监管流程（新增或更新）
export function saveSupervisionProcess(data) {
  return transData({
    url: data.id ? '/api/supervision-config/processes/update' : '/api/supervision-config/processes/add',
    method: 'post',
    data
  })
}

// 删除监管流程
export function deleteSupervisionProcess(data) {
  return transData({
    url: '/api/supervision-config/processes/delete',
    method: 'post',
    data
  })
}

// 批量删除监管流程
export function batchDeleteSupervisionProcesses(data) {
  return transData({
    url: '/api/supervision-config/processes/batch-delete',
    method: 'post',
    data
  })
}

// 设计监管流程
export function designSupervisionProcess(data) {
  return transData({
    url: '/api/supervision-config/processes/design',
    method: 'post',
    data
  })
}

// 部署监管流程
export function deploySupervisionProcess(data) {
  return transData({
    url: '/api/supervision-config/processes/deploy',
    method: 'post',
    data
  })
}

// 启动监管流程实例
export function startSupervisionProcessInstance(data) {
  return transData({
    url: '/api/supervision-config/processes/start-instance',
    method: 'post',
    data
  })
}

// 获取流程实例列表
export function getProcessInstancesList(data) {
  return transData({
    url: '/api/supervision-config/processes/instances',
    method: 'post',
    data
  })
}

// 获取流程任务列表
export function getProcessTasksList(data) {
  return transData({
    url: '/api/supervision-config/processes/tasks',
    method: 'post',
    data
  })
}

// 完成流程任务
export function completeProcessTask(data) {
  return transData({
    url: '/api/supervision-config/processes/complete-task',
    method: 'post',
    data
  })
}

// 导出监管流程数据
export function exportSupervisionProcessesData(data) {
  return transData({
    url: '/api/supervision-config/processes/export',
    method: 'post',
    data,
    responseType: 'blob'
  })
}

// ==================== 监管配置模板 ====================

// 获取监管配置模板列表
export function getSupervisionConfigTemplates(data) {
  return transData({
    url: '/api/supervision-config/templates/list',
    method: 'post',
    data
  })
}

// 应用监管配置模板
export function applySupervisionConfigTemplate(data) {
  return transData({
    url: '/api/supervision-config/templates/apply',
    method: 'post',
    data
  })
}

// 保存为监管配置模板
export function saveAsSupervisionConfigTemplate(data) {
  return transData({
    url: '/api/supervision-config/templates/save',
    method: 'post',
    data
  })
}

// ==================== 监管配置同步 ====================

// 同步监管配置到企业端
export function syncSupervisionConfigToEnterprise(data) {
  return transData({
    url: '/api/supervision-config/sync/to-enterprise',
    method: 'post',
    data
  })
}

// 获取配置同步状态
export function getConfigSyncStatus(data) {
  return transData({
    url: '/api/supervision-config/sync/status',
    method: 'post',
    data
  })
}

// 批量同步监管配置
export function batchSyncSupervisionConfig(data) {
  return transData({
    url: '/api/supervision-config/sync/batch',
    method: 'post',
    data
  })
}
