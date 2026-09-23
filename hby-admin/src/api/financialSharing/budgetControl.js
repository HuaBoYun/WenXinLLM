import request from '@/utils/request'

/**
 * 预算执行控制 - 控制规则管理 API
 */

// 分页查询控制规则
export function queryControlRulePage(data) {
  return request({
    url: '/cwgxAi/financialSharing/budgetControl/controlRule/queryPage',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 根据ID查询控制规则
export function queryControlRuleById(ruleId) {
  return request({
    url: '/cwgxAi/financialSharing/budgetControl/controlRule/queryById',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    params: { ruleId }
  })
}

// 新增控制规则
export function addControlRule(data) {
  return request({
    url: '/cwgxAi/financialSharing/budgetControl/controlRule/add',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 修改控制规则
export function modifyControlRule(data) {
  return request({
    url: '/cwgxAi/financialSharing/budgetControl/controlRule/modify',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 删除控制规则
export function removeControlRule(ruleId) {
  return request({
    url: '/cwgxAi/financialSharing/budgetControl/controlRule/remove',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    params: { ruleId }
  })
}

// 启用/禁用控制规则
export function toggleControlRuleStatus(ruleId, isEnabled) {
  return request({
    url: '/cwgxAi/financialSharing/budgetControl/controlRule/toggleStatus',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    params: { ruleId, isEnabled }
  })
}

// 预算控制检查 (OpenAPI)
export function checkBudget(data) {
  return request({
    url: '/cwgxAi/financialSharing/budgetControl/api/checkBudget',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 预算占用
export function occupyBudget(data) {
  return request({
    url: '/cwgxAi/financialSharing/budgetControl/api/occupyBudget',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 预算释放
export function releaseBudget(data) {
  return request({
    url: '/cwgxAi/financialSharing/budgetControl/api/releaseBudget',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 预算转移
export function transferBudget(fromData, toData) {
  return request({
    url: '/cwgxAi/financialSharing/budgetControl/api/transferBudget',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data: { fromRequest: fromData, toRequest: toData }
  })
}

// 查询预算占用情况
export function queryBudgetOccupancy(params) {
  return request({
    url: '/cwgxAi/financialSharing/budgetControl/api/queryOccupancy',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    params
  })
}

// ==================== 映射配置管理 ====================

// 分页查询映射配置
export function queryMappingConfigPage(data) {
  return request({
    url: '/cwgxAi/financialSharing/budgetControl/mappingConfig/queryPage',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 根据ID查询映射配置
export function queryMappingConfigById(mappingId) {
  return request({
    url: '/cwgxAi/financialSharing/budgetControl/mappingConfig/queryById',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    params: { mappingId }
  })
}

// 新增映射配置
export function addMappingConfig(data) {
  return request({
    url: '/cwgxAi/financialSharing/budgetControl/mappingConfig/add',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 修改映射配置
export function modifyMappingConfig(data) {
  return request({
    url: '/cwgxAi/financialSharing/budgetControl/mappingConfig/modify',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 删除映射配置
export function removeMappingConfig(mappingId) {
  return request({
    url: '/cwgxAi/financialSharing/budgetControl/mappingConfig/remove',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    params: { mappingId }
  })
}

// 根据来源系统查询映射配置
export function queryMappingConfigBySourceSystem(sourceSystem) {
  return request({
    url: '/cwgxAi/financialSharing/budgetControl/mappingConfig/queryBySourceSystem',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    params: { sourceSystem }
  })
}

// ==================== 执行记录管理 ====================

// 分页查询执行记录
export function queryExecutionRecordPage(data) {
  return request({
    url: '/cwgxAi/financialSharing/budgetControl/executionRecord/queryPage',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 根据ID查询执行记录
export function queryExecutionRecordById(recordId) {
  return request({
    url: '/cwgxAi/financialSharing/budgetControl/executionRecord/queryById',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    params: { recordId }
  })
}

// 查询执行记录统计
export function queryExecutionRecordStatistics(data) {
  return request({
    url: '/cwgxAi/financialSharing/budgetControl/executionRecord/queryStatistics',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 导出执行记录
export function exportExecutionRecords(data) {
  return request({
    url: '/cwgxAi/financialSharing/budgetControl/executionRecord/exportRecords',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// ==================== 控制策略配置 ====================

// 分页查询控制策略
export function queryControlStrategyPage(data) {
  return request({
    url: '/cwgxAi/financialSharing/budgetControl/controlStrategy/queryPage',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 根据规则ID查询控制策略
export function queryControlStrategyByRuleId(ruleId) {
  return request({
    url: '/cwgxAi/financialSharing/budgetControl/controlStrategy/queryByRuleId',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    params: { ruleId }
  })
}

// 保存控制策略配置
export function saveControlStrategy(data) {
  return request({
    url: '/cwgxAi/financialSharing/budgetControl/controlStrategy/saveStrategy',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 测试控制策略
export function testControlStrategy(data, testAmount, testOrgId, testSubjectCode, testPeriod) {
  return request({
    url: '/cwgxAi/financialSharing/budgetControl/controlStrategy/testStrategy',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data,
    params: { testAmount, testOrgId, testSubjectCode, testPeriod }
  })
}

// 复制控制策略
export function copyControlStrategy(ruleId, newRuleCode, newRuleName) {
  return request({
    url: '/cwgxAi/financialSharing/budgetControl/controlStrategy/copyStrategy',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    params: { ruleId, newRuleCode, newRuleName }
  })
}

// ==================== 预算占用查询 ====================

// 分页查询预算占用情况
export function queryOccupancyPage(data) {
  return request({
    url: '/cwgxAi/financialSharing/budgetControl/api/queryOccupancyPage',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 查询预算占用统计
export function queryOccupancyStatistics(data) {
  return request({
    url: '/cwgxAi/financialSharing/budgetControl/api/queryOccupancyStatistics',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 查询预算占用趋势
export function queryOccupancyTrend(data) {
  return request({
    url: '/cwgxAi/financialSharing/budgetControl/api/queryOccupancyTrend',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// ==================== 预算释放管理 ====================

// 批量释放预算
export function batchReleaseBudget(data) {
  return request({
    url: '/cwgxAi/financialSharing/budgetControl/api/batchReleaseBudget',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 查询释放记录
export function queryReleaseRecords(data) {
  return request({
    url: '/cwgxAi/financialSharing/budgetControl/api/queryReleaseRecords',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 获取释放记录统计
export function getReleaseRecordStatistics(data) {
  return request({
    url: '/cwgxAi/financialSharing/budgetControl/api/getReleaseRecordStatistics',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// ==================== 预算转移管理 ====================

// 执行预算转移
export function transferBudgetEnhanced(data) {
  return request({
    url: '/cwgxAi/financialSharing/budgetControl/api/transferBudgetEnhanced',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 批量转移预算
export function batchTransferBudget(data) {
  return request({
    url: '/cwgxAi/financialSharing/budgetControl/api/batchTransferBudget',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 查询转移记录
export function queryTransferRecords(data) {
  return request({
    url: '/cwgxAi/financialSharing/budgetControl/api/queryTransferRecords',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 获取转移记录统计
export function getTransferRecordStatistics(data) {
  return request({
    url: '/cwgxAi/financialSharing/budgetControl/api/getTransferRecordStatistics',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// ==================== 控制分析报表 ====================

// 获取控制效果分析
export function getControlEffectAnalysis(data) {
  return request({
    url: '/cwgxAi/financialSharing/controlAnalysis/api/getControlEffectAnalysis',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 获取预算使用趋势分析
export function getBudgetUsageTrend(data) {
  return request({
    url: '/cwgxAi/financialSharing/controlAnalysis/api/getBudgetUsageTrend',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 获取异常控制分析
export function getAbnormalControlAnalysis(data) {
  return request({
    url: '/cwgxAi/financialSharing/controlAnalysis/api/getAbnormalControlAnalysis',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 获取控制结果分布
export function getControlResultDistribution(data) {
  return request({
    url: '/cwgxAi/financialSharing/controlAnalysis/api/getControlResultDistribution',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 获取组织控制排名
export function getOrgControlRanking(data) {
  return request({
    url: '/cwgxAi/financialSharing/controlAnalysis/api/getOrgControlRanking',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 获取科目控制排名
export function getSubjectControlRanking(data) {
  return request({
    url: '/cwgxAi/financialSharing/controlAnalysis/api/getSubjectControlRanking',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// ==================== 预警配置管理 ====================

// 分页查询预警配置
export function queryWarningConfigPage(data) {
  return request({
    url: '/cwgxAi/financialSharing/warningConfig/api/queryPage',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 根据ID查询预警配置
export function queryWarningConfigById(data) {
  return request({
    url: '/cwgxAi/financialSharing/warningConfig/api/queryById',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 保存预警配置
export function saveWarningConfig(data) {
  return request({
    url: '/cwgxAi/financialSharing/warningConfig/api/saveConfig',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 删除预警配置
export function deleteWarningConfig(data) {
  return request({
    url: '/cwgxAi/financialSharing/warningConfig/api/deleteConfig',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 启用/禁用预警配置
export function toggleWarningConfigEnabled(data) {
  return request({
    url: '/cwgxAi/financialSharing/warningConfig/api/toggleEnabled',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 测试预警配置
export function testWarningConfig(data) {
  return request({
    url: '/cwgxAi/financialSharing/warningConfig/api/testConfig',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 发送预警消息
export function sendWarningMessage(data) {
  return request({
    url: '/cwgxAi/financialSharing/warningConfig/api/sendWarningMessage',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}
