import request from '@/utils/request'

// ==================== 数据源配置管理 ====================

// 分页查询数据源配置
export function queryDataSourcePage(data) {
  return request({
    url: '/cwgxAi/financialSharing/dataSource/api/queryPage',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 根据ID查询数据源配置
export function queryDataSourceById(data) {
  return request({
    url: '/cwgxAi/financialSharing/dataSource/api/queryById',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 保存数据源配置
export function saveDataSource(data) {
  return request({
    url: '/cwgxAi/financialSharing/dataSource/api/saveDataSource',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 删除数据源配置
export function deleteDataSource(data) {
  return request({
    url: '/cwgxAi/financialSharing/dataSource/api/deleteDataSource',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 启用/禁用数据源
export function toggleDataSourceEnabled(data) {
  return request({
    url: '/cwgxAi/financialSharing/dataSource/api/toggleEnabled',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 测试数据源连接
export function testDataSourceConnection(data) {
  return request({
    url: '/cwgxAi/financialSharing/dataSource/api/testConnection',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// ==================== 数据源连接测试（增强版） ====================

// 测试数据库连接（增强版）
export function testDatabaseConnection(data) {
  return request({
    url: '/cwgxAi/financialSharing/dataSourceTest/api/testDatabaseConnection',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 测试API连接（增强版）
export function testApiConnection(data) {
  return request({
    url: '/cwgxAi/financialSharing/dataSourceTest/api/testApiConnection',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 测试文件连接（增强版）
export function testFileConnection(data) {
  return request({
    url: '/cwgxAi/financialSharing/dataSourceTest/api/testFileConnection',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 测试财务共享连接（增强版）
export function testFinancialSharingConnection(data) {
  return request({
    url: '/cwgxAi/financialSharing/dataSourceTest/api/testFinancialSharingConnection',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 批量测试数据源连接
export function batchTestConnection(data) {
  return request({
    url: '/cwgxAi/financialSharing/dataSourceTest/api/batchTestConnection',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 获取数据库表列表
export function getDatabaseTables(data) {
  return request({
    url: '/cwgxAi/financialSharing/dataSourceTest/api/getDatabaseTables',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 获取数据库表结构
export function getTableStructure(data) {
  return request({
    url: '/cwgxAi/financialSharing/dataSourceTest/api/getTableStructure',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 执行测试查询
export function executeTestQuery(data) {
  return request({
    url: '/cwgxAi/financialSharing/dataSourceTest/api/executeTestQuery',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// ==================== 映射规则管理 ====================

// 分页查询映射规则
export function queryMappingRulePage(data) {
  return request({
    url: '/cwgxAi/financialSharing/mappingRule/api/queryPage',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 根据ID查询映射规则
export function queryMappingRuleById(data) {
  return request({
    url: '/cwgxAi/financialSharing/mappingRule/api/queryById',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 保存映射规则
export function saveMappingRule(data) {
  return request({
    url: '/cwgxAi/financialSharing/mappingRule/api/saveMappingRule',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 删除映射规则
export function deleteMappingRule(data) {
  return request({
    url: '/cwgxAi/financialSharing/mappingRule/api/deleteMappingRule',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 启用/禁用映射规则
export function toggleMappingRuleEnabled(data) {
  return request({
    url: '/cwgxAi/financialSharing/mappingRule/api/toggleEnabled',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 复制映射规则
export function copyMappingRule(data) {
  return request({
    url: '/cwgxAi/financialSharing/mappingRule/api/copyMappingRule',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 更新排序号
export function updateMappingRuleSortNo(data) {
  return request({
    url: '/cwgxAi/financialSharing/mappingRule/api/updateSortNo',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// ==================== 映射规则测试相关API ====================

// 测试字段映射
export function testFieldMapping(data) {
  return request({
    url: '/cwgxAi/financialSharing/mappingRuleTest/api/testFieldMapping',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 测试转换规则
export function testTransformRules(data) {
  return request({
    url: '/cwgxAi/financialSharing/mappingRuleTest/api/testTransformRules',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 测试校验规则
export function testValidationRules(data) {
  return request({
    url: '/cwgxAi/financialSharing/mappingRuleTest/api/testValidationRules',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 测试过滤条件
export function testFilterCondition(data) {
  return request({
    url: '/cwgxAi/financialSharing/mappingRuleTest/api/testFilterCondition',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 完整规则测试
export function testCompleteRule(data) {
  return request({
    url: '/cwgxAi/financialSharing/mappingRuleTest/api/testCompleteRule',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 批量测试规则
export function batchTestRules(data) {
  return request({
    url: '/cwgxAi/financialSharing/mappingRuleTest/api/batchTestRules',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 预览映射结果
export function previewMappingResult(data) {
  return request({
    url: '/cwgxAi/financialSharing/mappingRuleTest/api/previewMappingResult',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// ==================== 归集任务相关API ====================

// 分页查询归集任务
export function queryCollectionTaskPage(data) {
  return request({
    url: '/cwgxAi/financialSharing/collectionTask/api/queryPage',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 根据ID查询归集任务
export function queryCollectionTaskById(data) {
  return request({
    url: '/cwgxAi/financialSharing/collectionTask/api/queryById',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 保存归集任务
export function saveCollectionTask(data) {
  return request({
    url: '/cwgxAi/financialSharing/collectionTask/api/saveCollectionTask',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 删除归集任务
export function deleteCollectionTask(data) {
  return request({
    url: '/cwgxAi/financialSharing/collectionTask/api/deleteCollectionTask',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 启用/禁用归集任务
export function toggleCollectionTaskEnabled(data) {
  return request({
    url: '/cwgxAi/financialSharing/collectionTask/api/toggleEnabled',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 立即执行归集任务
export function executeCollectionTask(data) {
  return request({
    url: '/cwgxAi/financialSharing/collectionTask/api/executeTask',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 停止归集任务
export function stopCollectionTask(data) {
  return request({
    url: '/cwgxAi/financialSharing/collectionTask/api/stopTask',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 计算下次执行时间
export function calculateNextExecuteTime(data) {
  return request({
    url: '/cwgxAi/financialSharing/collectionTask/api/calculateNextExecuteTime',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}



// ==================== 归集日志管理 ====================

// 分页查询归集日志
export function queryCollectionLogPage(data) {
  return request({
    url: '/cwgxAi/financialSharing/dataCollection/collectionLog/queryPage',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 根据ID查询归集日志
export function queryCollectionLogById(params) {
  return request({
    url: '/cwgxAi/financialSharing/dataCollection/collectionLog/queryById',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    params
  })
}

// 删除归集日志
export function deleteCollectionLog(params) {
  return request({
    url: '/cwgxAi/financialSharing/dataCollection/collectionLog/delete',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    params
  })
}

// 批量删除归集日志
export function batchDeleteCollectionLog(data) {
  return request({
    url: '/cwgxAi/financialSharing/dataCollection/collectionLog/batchDelete',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 清理历史日志
export function cleanHistoryLogs(params) {
  return request({
    url: '/cwgxAi/financialSharing/dataCollection/collectionLog/cleanHistory',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    params
  })
}


// ==================== 归集监控报表 ====================

// 获取整体监控统计
export function getOverallStatistics() {
  return request({
    url: '/cwgxAi/financialSharing/dataCollection/monitor/getOverallStatistics',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

// 获取任务执行趋势
export function getTaskExecutionTrend(params) {
  return request({
    url: '/cwgxAi/financialSharing/dataCollection/monitor/getTaskExecutionTrend',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    params
  })
}

// 获取任务状态分布
export function getTaskStatusDistribution() {
  return request({
    url: '/cwgxAi/financialSharing/dataCollection/monitor/getTaskStatusDistribution',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

// 获取失败任务TOP10
export function getTopFailedTasks(params) {
  return request({
    url: '/cwgxAi/financialSharing/dataCollection/monitor/getTopFailedTasks',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    params
  })
}

// 获取执行时长统计
export function getExecutionDurationStatistics() {
  return request({
    url: '/cwgxAi/financialSharing/dataCollection/monitor/getExecutionDurationStatistics',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}


// ==================== 数据质量规则管理 ====================

// 分页查询质量规则
export function queryQualityRulePage(data) {
  return request({
    url: '/cwgxAi/financialSharing/dataCollection/qualityRule/queryPage',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 根据ID查询质量规则
export function queryQualityRuleById(data) {
  return request({
    url: '/cwgxAi/financialSharing/dataCollection/qualityRule/queryById',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 保存质量规则
export function saveQualityRule(data) {
  return request({
    url: '/cwgxAi/financialSharing/dataCollection/qualityRule/saveRule',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 删除质量规则
export function deleteQualityRule(data) {
  return request({
    url: '/cwgxAi/financialSharing/dataCollection/qualityRule/deleteRule',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 批量删除质量规则
export function batchDeleteQualityRule(data) {
  return request({
    url: '/cwgxAi/financialSharing/dataCollection/qualityRule/batchDeleteRule',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 启用/禁用质量规则
export function toggleQualityRuleEnabled(data) {
  return request({
    url: '/cwgxAi/financialSharing/dataCollection/qualityRule/toggleEnabled',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 查询所有启用的规则
export function queryEnabledQualityRules() {
  return request({
    url: '/cwgxAi/financialSharing/dataCollection/qualityRule/queryEnabledRules',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

// ==================== 数据质量检查管理 ====================

// 分页查询质量检查记录
export function queryQualityCheckPage(data) {
  return request({
    url: '/cwgxAi/financialSharing/dataCollection/qualityCheck/queryPage',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 根据ID查询质量检查记录
export function queryQualityCheckById(data) {
  return request({
    url: '/cwgxAi/financialSharing/dataCollection/qualityCheck/queryById',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 查询检查明细
export function queryQualityCheckDetails(data) {
  return request({
    url: '/cwgxAi/financialSharing/dataCollection/qualityCheck/queryDetails',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 执行质量检查
export function executeQualityCheck(data) {
  return request({
    url: '/cwgxAi/financialSharing/dataCollection/qualityCheck/executeQualityCheck',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 删除质量检查记录
export function deleteQualityCheck(data) {
  return request({
    url: '/cwgxAi/financialSharing/dataCollection/qualityCheck/deleteCheck',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

// 批量删除质量检查记录
export function batchDeleteQualityCheck(data) {
  return request({
    url: '/cwgxAi/financialSharing/dataCollection/qualityCheck/batchDeleteCheck',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

