import request from '@/utils/request'

/**
 * 会计规则管理API
 */

/**
 * 分页查询会计规则
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getAccountingRulePage(data) {
  return request({
    url: '/cwgxAi/accounting-rule/accounting/getList',
    method: 'post',
    data
  })
}

/**
 * 保存或更新会计规则
 * @param {Object} data 保存参数
 * @returns {Promise}
 */
export function saveOrUpdateAccountingRule(data) {
  return request({
    url: '/cwgxAi/accounting-rule/accounting/saveOrUpdate',
    method: 'post',
    data
  })
}

/**
 * 根据ID查询会计规则详情
 * @param {Number} ruleId 规则ID
 * @returns {Promise}
 */
export function getAccountingRuleById(ruleId) {
  return request({
    url: `/cwgxAi/accounting-rule/accounting/${ruleId}`,
    method: 'get'
  })
}

/**
 * 删除会计规则
 * @param {Number} ruleId 规则ID
 * @returns {Promise}
 */
export function deleteAccountingRule(ruleId) {
  return request({
    url: `/cwgxAi/accounting-rule/accounting/${ruleId}`,
    method: 'delete'
  })
}

/**
 * 批量删除会计规则
 * @param {Array} ruleIds 规则ID列表
 * @returns {Promise}
 */
export function batchDeleteAccountingRules(ruleIds) {
  return request({
    url: '/cwgxAi/accounting-rule/accounting/batch',
    method: 'delete',
    data: ruleIds
  })
}

/**
 * 更新规则启用状态
 * @param {Number} ruleId 规则ID
 * @param {Number} isEnabled 是否启用
 * @returns {Promise}
 */
export function updateRuleEnabled(ruleId, isEnabled) {
  return request({
    url: `/cwgxAi/accounting-rule/accounting/${ruleId}/toggle`,
    method: 'put',
    data: { enabled: isEnabled }
  })
}

/**
 * 批量更新规则启用状态
 * @param {Array} ruleIds 规则ID列表
 * @param {Number} isEnabled 是否启用
 * @returns {Promise}
 */
export function batchUpdateRuleEnabled(ruleIds, isEnabled) {
  return request({
    url: `/cwgxAi/accounting-rule/accounting/batch-toggle`,
    method: 'put',
    data: { ruleIds, enabled: isEnabled }
  })
}

/**
 * 检查规则编码是否存在
 * @param {String} ruleCode 规则编码
 * @param {Number} bookId 账簿ID
 * @param {Number} tenantId 租户ID
 * @param {Number} excludeId 排除的ID
 * @returns {Promise}
 */
export function checkRuleCodeExists(ruleCode, bookId, tenantId, excludeId) {
  return request({
    url: '/cwgxAi/accounting-rule/accounting/check-code',
    method: 'get',
    params: { ruleCode, bookId, tenantId, excludeId }
  })
}

/**
 * 根据规则类型查询会计规则列表
 * @param {Number} ruleType 规则类型
 * @param {Number} bookId 账簿ID
 * @param {Number} tenantId 租户ID
 * @returns {Promise}
 */
export function getAccountingRulesByType(ruleType, bookId, tenantId) {
  return request({
    url: '/cwgxAi/accounting-rule/accounting/by-type',
    method: 'get',
    params: { ruleType, bookId, tenantId }
  })
}

/**
 * 根据事项类型查询会计规则列表
 * @param {String} transactionType 事项类型
 * @param {Number} bookId 账簿ID
 * @param {Number} tenantId 租户ID
 * @returns {Promise}
 */
export function getAccountingRulesByTransactionType(transactionType, bookId, tenantId) {
  return request({
    url: '/cwgxAi/accounting-rule/accounting/by-transaction-type',
    method: 'get',
    params: { transactionType, bookId, tenantId }
  })
}

/**
 * 根据启用状态查询会计规则列表
 * @param {Number} isEnabled 是否启用
 * @param {Number} bookId 账簿ID
 * @param {Number} tenantId 租户ID
 * @returns {Promise}
 */
export function getAccountingRulesByEnabled(isEnabled, bookId, tenantId) {
  return request({
    url: '/cwgxAi/accounting-rule/accounting/by-enabled',
    method: 'get',
    params: { isEnabled, bookId, tenantId }
  })
}

/**
 * 获取规则类型列表
 * @param {Number} bookId 账簿ID
 * @param {Number} tenantId 租户ID
 * @returns {Promise}
 */
export function getRuleTypes(bookId, tenantId) {
  return request({
    url: '/cwgxAi/accounting-rule/accounting/rule-types',
    method: 'get',
    params: { bookId, tenantId }
  })
}

/**
 * 获取事项类型列表
 * @param {Number} bookId 账簿ID
 * @param {Number} tenantId 租户ID
 * @returns {Promise}
 */
export function getTransactionTypes(bookId, tenantId) {
  return request({
    url: '/cwgxAi/accounting-rule/accounting/transaction-types',
    method: 'get',
    params: { bookId, tenantId }
  })
}

/**
 * 统计规则数量按类型分组
 * @param {Number} bookId 账簿ID
 * @param {Number} tenantId 租户ID
 * @returns {Promise}
 */
export function countRulesByType(bookId, tenantId) {
  return request({
    url: '/cwgxAi/accounting-rule/accounting/count-by-type',
    method: 'get',
    params: { bookId, tenantId }
  })
}

/**
 * 统计规则数量按事项类型分组
 * @param {Number} bookId 账簿ID
 * @param {Number} tenantId 租户ID
 * @returns {Promise}
 */
export function countRulesByTransactionType(bookId, tenantId) {
  return request({
    url: '/cwgxAi/accounting-rule/accounting/count-by-transaction-type',
    method: 'get',
    params: { bookId, tenantId }
  })
}

/**
 * 测试会计规则执行
 * @param {Number} ruleId 规则ID
 * @param {Object} testData 测试数据
 * @returns {Promise}
 */
export function testAccountingRule(ruleId, testData) {
  return request({
    url: `/cwgxAi/accounting-rule/accounting/test`,
    method: 'post',
    data: testData
  })
}

/**
 * 凭证模板管理API
 */

/**
 * 分页查询凭证模板
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getVoucherTemplatePage(data) {
  return request({
    url: '/cwgxAi/accounting-rule/voucher-template/getList',
    method: 'post',
    data
  })
}

/**
 * 保存或更新凭证模板
 * @param {Object} data 保存参数
 * @returns {Promise}
 */
export function saveOrUpdateVoucherTemplate(data) {
  return request({
    url: '/cwgxAi/accounting-rule/voucher-template/saveOrUpdate',
    method: 'post',
    data
  })
}

/**
 * 根据ID查询凭证模板详情
 * @param {Number} templateId 模板ID
 * @returns {Promise}
 */
export function getVoucherTemplateById(templateId) {
  return request({
    url: `/cwgxAi/accounting-rule/voucher-template/${templateId}`,
    method: 'get'
  })
}

/**
 * 删除凭证模板
 * @param {Number} templateId 模板ID
 * @returns {Promise}
 */
export function deleteVoucherTemplate(templateId) {
  return request({
    url: `/cwgxAi/accounting-rule/voucher-template/${templateId}`,
    method: 'delete'
  })
}

/**
 * 批量删除凭证模板
 * @param {Array} templateIds 模板ID列表
 * @returns {Promise}
 */
export function batchDeleteVoucherTemplates(templateIds) {
  return request({
    url: '/cwgxAi/accounting-rule/voucher-template/batch',
    method: 'delete',
    data: templateIds
  })
}

/**
 * 复制凭证模板
 * @param {Number} templateId 模板ID
 * @param {Object} data 复制参数
 * @returns {Promise}
 */
export function copyVoucherTemplate(templateId, data) {
  return request({
    url: `/cwgxAi/accounting-rule/voucher-template/${templateId}/copy`,
    method: 'post',
    data
  })
}

/**
 * 预览凭证模板
 * @param {Number} templateId 模板ID
 * @param {Object} testData 测试数据
 * @returns {Promise}
 */
export function previewVoucherTemplate(templateId, testData) {
  return request({
    url: `/cwgxAi/accounting-rule/voucher-template/${templateId}/preview`,
    method: 'post',
    data: testData
  })
}

/**
 * 获取凭证模板类型列表
 * @param {Number} bookId 账簿ID
 * @param {Number} tenantId 租户ID
 * @returns {Promise}
 */
export function getVoucherTemplateTypes(bookId, tenantId) {
  return request({
    url: '/cwgxAi/accounting-rule/voucher-template/types',
    method: 'get',
    params: { bookId, tenantId }
  })
}

/**
 * 统计凭证模板数量按类型分组
 * @param {Number} bookId 账簿ID
 * @param {Number} tenantId 租户ID
 * @returns {Promise}
 */
export function countVoucherTemplatesByType(bookId, tenantId) {
  return request({
    url: '/cwgxAi/accounting-rule/voucher-template/count-by-type',
    method: 'get',
    params: { bookId, tenantId }
  })
}

/**
 * 导入凭证模板
 * @param {FormData} formData 包含文件的表单数据
 * @returns {Promise}
 */
export function importVoucherTemplates(formData) {
  return request({
    url: '/cwgxAi/accounting-rule/voucher-template/import',
    method: 'post',
    data: formData,
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

/**
 * 导出凭证模板
 * @param {Object} params 导出参数
 * @returns {Promise}
 */
export function exportVoucherTemplates(params) {
  return request({
    url: '/cwgxAi/accounting-rule/voucher-template/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

// ==================== 规则引擎管理 API ====================

/**
 * 执行规则引擎
 * @param {Object} data 执行参数
 * @returns {Promise}
 */
export function executeRuleEngine(data) {
  return request({
    url: '/cwgxAi/accounting-rule/engine/execute',
    method: 'post',
    data
  })
}

/**
 * 获取规则引擎执行状态
 * @param {String} executionId 执行ID
 * @returns {Promise}
 */
export function getRuleEngineExecutionStatus(executionId) {
  return request({
    url: `/cwgxAi/accounting-rule/engine/execution/${executionId}/status`,
    method: 'get'
  })
}

/**
 * 获取规则引擎执行结果
 * @param {String} executionId 执行ID
 * @returns {Promise}
 */
export function getRuleEngineExecutionResult(executionId) {
  return request({
    url: `/cwgxAi/accounting-rule/engine/execution/${executionId}/result`,
    method: 'get'
  })
}

/**
 * 停止规则引擎执行
 * @param {String} executionId 执行ID
 * @returns {Promise}
 */
export function stopRuleEngineExecution(executionId) {
  return request({
    url: `/cwgxAi/accounting-rule/engine/execution/${executionId}/stop`,
    method: 'post'
  })
}

/**
 * 获取规则引擎统计信息
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getRuleEngineStatistics(params) {
  return request({
    url: '/cwgxAi/accounting-rule/engine/statistics',
    method: 'get',
    params
  })
}

// ==================== 规则验证管理 API ====================

/**
 * 验证会计规则
 * @param {Object} data 验证数据
 * @returns {Promise}
 */
export function validateAccountingRules(data) {
  return request({
    url: '/cwgxAi/accounting-rule/validate',
    method: 'post',
    data
  })
}

/**
 * 批量验证会计规则
 * @param {Array} ruleIds 规则ID列表
 * @returns {Promise}
 */
export function batchValidateAccountingRules(ruleIds) {
  return request({
    url: '/cwgxAi/accounting-rule/batch-validate',
    method: 'post',
    data: ruleIds
  })
}

/**
 * 获取规则验证报告
 * @param {String} validationId 验证ID
 * @returns {Promise}
 */
export function getRuleValidationReport(validationId) {
  return request({
    url: `/cwgxAi/accounting-rule/validation/${validationId}/report`,
    method: 'get'
  })
}

// ==================== 规则监控管理 API ====================

/**
 * 获取规则执行监控数据
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getRuleExecutionMonitor(params) {
  return request({
    url: '/cwgxAi/accounting-rule/monitor/execution',
    method: 'get',
    params
  })
}

/**
 * 获取规则性能监控数据
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getRulePerformanceMonitor(params) {
  return request({
    url: '/cwgxAi/accounting-rule/monitor/performance',
    method: 'get',
    params
  })
}

/**
 * 获取规则错误监控数据
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getRuleErrorMonitor(params) {
  return request({
    url: '/cwgxAi/accounting-rule/monitor/error',
    method: 'get',
    params
  })
}

/**
 * 设置规则监控告警
 * @param {Object} data 告警配置
 * @returns {Promise}
 */
export function setRuleMonitorAlert(data) {
  return request({
    url: '/cwgxAi/accounting-rule/monitor/alert',
    method: 'post',
    data
  })
}

// ==================== 规则版本管理 API ====================

/**
 * 获取规则版本列表
 * @param {String} ruleId 规则ID
 * @returns {Promise}
 */
export function getRuleVersionList(ruleId) {
  return request({
    url: `/cwgxAi/accounting-rule/${ruleId}/versions`,
    method: 'get'
  })
}

/**
 * 创建规则版本
 * @param {String} ruleId 规则ID
 * @param {Object} versionData 版本数据
 * @returns {Promise}
 */
export function createRuleVersion(ruleId, versionData) {
  return request({
    url: `/cwgxAi/accounting-rule/${ruleId}/versions`,
    method: 'post',
    data: versionData
  })
}

/**
 * 发布规则版本
 * @param {String} ruleId 规则ID
 * @param {String} versionId 版本ID
 * @returns {Promise}
 */
export function publishRuleVersion(ruleId, versionId) {
  return request({
    url: `/cwgxAi/accounting-rule/${ruleId}/versions/${versionId}/publish`,
    method: 'post'
  })
}

/**
 * 回滚规则版本
 * @param {String} ruleId 规则ID
 * @param {String} versionId 版本ID
 * @returns {Promise}
 */
export function rollbackRuleVersion(ruleId, versionId) {
  return request({
    url: `/cwgxAi/accounting-rule/${ruleId}/versions/${versionId}/rollback`,
    method: 'post'
  })
}

/**
 * 比较规则版本
 * @param {String} ruleId 规则ID
 * @param {String} fromVersionId 源版本ID
 * @param {String} toVersionId 目标版本ID
 * @returns {Promise}
 */
export function compareRuleVersions(ruleId, fromVersionId, toVersionId) {
  return request({
    url: `/cwgxAi/accounting-rule/${ruleId}/versions/compare`,
    method: 'get',
    params: { fromVersionId, toVersionId }
  })
}
