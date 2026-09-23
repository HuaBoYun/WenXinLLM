/*
 * @Description: 财务共享 - 基础配置管理 API
 * @Author: system
 * @Date: 2024-12-19
 */
import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// ==================== 影响因素定义 ====================

/**
 * 分页查询影响因素
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getInfluenceFactorPage(data) {
  return request({
    url: '/cwgxAi/common/influence-factor/page',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 根据ID查询影响因素详情
 * @param {Number} id 影响因素ID
 * @returns {Promise}
 */
export function getInfluenceFactorById(id) {
  return request({
    url: `/cwgxAi/common/influence-factor/${id}`,
    method: 'get'
  })
}

/**
 * 根据编码查询影响因素
 * @param {String} factorCode 影响因素编码
 * @param {Number} tenantId 租户ID
 * @param {Number} bookId 账簿ID
 * @returns {Promise}
 */
export function getInfluenceFactorByCode(factorCode, tenantId, bookId) {
  return request({
    url: `/cwgxAi/common/influence-factor/code/${factorCode}`,
    method: 'get',
    params: { tenantId, bookId }
  })
}

/**
 * 根据类型查询影响因素列表
 * @param {Number} factorType 影响因素类型
 * @param {Number} tenantId 租户ID
 * @param {Number} bookId 账簿ID
 * @returns {Promise}
 */
export function getInfluenceFactorsByType(factorType, tenantId, bookId) {
  return request({
    url: `/cwgxAi/common/influence-factor/type/${factorType}`,
    method: 'get',
    params: { tenantId, bookId }
  })
}

/**
 * 保存影响因素
 * @param {Object} data 影响因素数据
 * @returns {Promise}
 */
export function saveInfluenceFactor(data) {
  return request({
    url: '/cwgxAi/common/influence-factor/save',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 更新影响因素
 * @param {Object} data 影响因素数据
 * @returns {Promise}
 */
export function updateInfluenceFactor(data) {
  return request({
    url: '/cwgxAi/common/influence-factor/update',
    method: 'put',
    data: transData(data)
  })
}

/**
 * 删除影响因素
 * @param {Number} id 影响因素ID
 * @returns {Promise}
 */
export function deleteInfluenceFactor(id) {
  return request({
    url: `/cwgxAi/common/influence-factor/${id}`,
    method: 'delete'
  })
}

/**
 * 批量删除影响因素
 * @param {Array} ids 影响因素ID列表
 * @returns {Promise}
 */
export function batchDeleteInfluenceFactors(ids) {
  return request({
    url: '/cwgxAi/common/influence-factor/batch',
    method: 'delete',
    data: ids
  })
}

/**
 * 启用/禁用影响因素
 * @param {Number} id 影响因素ID
 * @param {Number} isEnabled 启用状态(0禁用1启用)
 * @returns {Promise}
 */
export function updateInfluenceFactorStatus(id, isEnabled) {
  return request({
    url: `/cwgxAi/common/influence-factor/${id}/status`,
    method: 'put',
    params: { isEnabled }
  })
}

/**
 * 批量启用/禁用影响因素
 * @param {Array} ids 影响因素ID列表
 * @param {Number} isEnabled 启用状态(0禁用1启用)
 * @returns {Promise}
 */
export function batchUpdateInfluenceFactorStatus(ids, isEnabled) {
  return request({
    url: '/cwgxAi/common/influence-factor/batch/status',
    method: 'put',
    params: { isEnabled },
    data: ids
  })
}

/**
 * 检查编码是否存在
 * @param {String} factorCode 影响因素编码
 * @param {Number} tenantId 租户ID
 * @param {Number} bookId 账簿ID
 * @param {Number} excludeId 排除的ID
 * @returns {Promise}
 */
export function checkInfluenceFactorCodeExists(factorCode, tenantId, bookId, excludeId) {
  return request({
    url: '/zbgl/influence-factor/check-code',
    method: 'get',
    params: { factorCode, tenantId, bookId, excludeId }
  })
}

// ==================== 会计科目配置 ====================

/**
 * 分页查询会计科目
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getAccountSubjectPage(data) {
  return request({
    url: '/cwgxAi/common/subject/getList',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取会计科目树形结构
 * @param {Number} tenantId 租户ID
 * @param {Number} bookId 账簿ID
 * @returns {Promise}
 */
export function getAccountSubjectTree(tenantId, bookId) {
  return request({
    url: '/cwgxAi/common/subject/tree',
    method: 'get',
    params: { tenantId, bookId }
  })
}

/**
 * 根据ID查询会计科目详情
 * @param {Number} subjectId 科目ID
 * @returns {Promise}
 */
export function getAccountSubjectById(subjectId) {
  return request({
    url: `/cwgxAi/common/subject/${subjectId}`,
    method: 'get'
  })
}

/**
 * 根据科目类型查询会计科目
 * @param {Number} subjectType 科目类型
 * @param {Number} bookId 账簿ID
 * @param {Number} tenantId 租户ID
 * @returns {Promise}
 */
export function getAccountSubjectsByType(subjectType, bookId, tenantId) {
  return request({
    url: `/cwgxAi/common/subject/type/${subjectType}`,
    method: 'get',
    params: { bookId, tenantId }
  })
}

/**
 * 根据上级科目ID查询子科目列表
 * @param {Number} parentSubjectId 上级科目ID
 * @param {Number} bookId 账簿ID
 * @param {Number} tenantId 租户ID
 * @returns {Promise}
 */
export function getAccountSubjectsByParentId(parentSubjectId, bookId, tenantId) {
  return request({
    url: '/cwgxAi/common/subject/children',
    method: 'get',
    params: { parentSubjectId, bookId, tenantId }
  })
}

/**
 * 检查科目编码是否存在
 * @param {String} subjectCode 科目编码
 * @param {Number} bookId 账簿ID
 * @param {Number} tenantId 租户ID
 * @param {Number} excludeId 排除的ID
 * @returns {Promise}
 */
export function checkAccountSubjectCodeExists(subjectCode, bookId, tenantId, excludeId) {
  return request({
    url: '/cwgxAi/common/subject/check-code',
    method: 'get',
    params: { subjectCode, bookId, tenantId, excludeId }
  })
}

/**
 * 保存或更新会计科目
 * @param {Object} data 会计科目数据
 * @returns {Promise}
 */
export function saveOrUpdateAccountSubject(data) {
  return request({
    url: '/cwgxAi/common/subject/saveOrUpdate',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 删除会计科目
 * @param {Number} id 会计科目ID
 * @returns {Promise}
 */
export function deleteAccountSubject(id) {
  return request({
    url: `/cwgxAi/common/subject/${id}`,
    method: 'delete'
  })
}

/**
 * 批量删除会计科目
 * @param {Array} ids 会计科目ID列表
 * @returns {Promise}
 */
export function batchDeleteAccountSubjects(ids) {
  return request({
    url: '/cwgxAi/common/subject/batch',
    method: 'delete',
    data: ids
  })
}

/**
 * 启用/禁用会计科目
 * @param {Number} id 会计科目ID
 * @param {Number} isEnabled 启用状态
 * @returns {Promise}
 */
export function updateAccountSubjectStatus(id, isEnabled) {
  return request({
    url: `/cwgxAi/common/subject/${id}/status`,
    method: 'put',
    params: { isEnabled }
  })
}

/**
 * 批量启用/禁用会计科目
 * @param {Array} ids 会计科目ID列表
 * @param {Number} isEnabled 启用状态
 * @returns {Promise}
 */
export function batchUpdateAccountSubjectStatus(ids, isEnabled) {
  return request({
    url: '/cwgxAi/common/subject/batch/status',
    method: 'put',
    params: { isEnabled },
    data: ids
  })
}

// ==================== 辅助核算项管理 ====================

/**
 * 分页查询辅助核算项
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getAuxiliaryItemPage(data) {
  return request({
    url: '/cwgxAi/common/auxiliary/getList',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 保存或更新辅助核算项
 * @param {Object} data 辅助核算项数据
 * @returns {Promise}
 */
export function saveOrUpdateAuxiliaryItem(data) {
  return request({
    url: '/cwgxAi/common/auxiliary/saveOrUpdate',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 根据ID查询辅助核算项详情
 * @param {Number} auxiliaryId 辅助核算项ID
 * @returns {Promise}
 */
export function getAuxiliaryItemById(auxiliaryId) {
  return request({
    url: `/cwgxAi/common/auxiliary/${auxiliaryId}`,
    method: 'get'
  })
}

/**
 * 删除辅助核算项
 * @param {Number} id 辅助核算项ID
 * @returns {Promise}
 */
export function deleteAuxiliaryItem(id) {
  return request({
    url: `/cwgxAi/common/auxiliary/${id}`,
    method: 'delete'
  })
}

/**
 * 批量删除辅助核算项
 * @param {Array} ids 辅助核算项ID列表
 * @returns {Promise}
 */
export function batchDeleteAuxiliaryItems(ids) {
  return request({
    url: '/cwgxAi/common/auxiliary/batch',
    method: 'delete',
    data: ids
  })
}

/**
 * 启用/禁用辅助核算项
 * @param {Number} id 辅助核算项ID
 * @param {Number} isEnabled 启用状态
 * @returns {Promise}
 */
export function updateAuxiliaryItemStatus(id, isEnabled) {
  return request({
    url: `/cwgxAi/common/auxiliary/${id}/status`,
    method: 'put',
    params: { isEnabled }
  })
}

/**
 * 批量启用/禁用辅助核算项
 * @param {Array} ids 辅助核算项ID列表
 * @param {Number} isEnabled 启用状态
 * @returns {Promise}
 */
export function batchUpdateAuxiliaryItemStatus(ids, isEnabled) {
  return request({
    url: '/cwgxAi/common/auxiliary/batch/status',
    method: 'put',
    params: { isEnabled },
    data: ids
  })
}

/**
 * 检查辅助核算项编码是否存在
 * @param {String} auxiliaryCode 辅助核算项编码
 * @param {String} auxiliaryType 辅助核算类型
 * @param {Number} bookId 账簿ID
 * @param {Number} tenantId 租户ID
 * @param {Number} excludeId 排除的ID
 * @returns {Promise}
 */
export function checkAuxiliaryItemCodeExists(auxiliaryCode, auxiliaryType, bookId, tenantId, excludeId) {
  return request({
    url: '/cwgxAi/common/auxiliary/check-code',
    method: 'get',
    params: { auxiliaryCode, auxiliaryType, bookId, tenantId, excludeId }
  })
}

/**
 * 获取辅助核算项树形结构
 * @param {String} auxiliaryType 辅助核算类型
 * @param {Number} bookId 账簿ID
 * @param {Number} tenantId 租户ID
 * @returns {Promise}
 */
export function getAuxiliaryItemTree(auxiliaryType, bookId, tenantId) {
  return request({
    url: '/cwgxAi/common/auxiliary/tree',
    method: 'get',
    params: { auxiliaryType, bookId, tenantId }
  })
}

/**
 * 根据类型查询辅助核算项
 * @param {String} auxiliaryType 辅助核算类型
 * @param {Number} bookId 账簿ID
 * @param {Number} tenantId 租户ID
 * @returns {Promise}
 */
export function getAuxiliaryItemsByType(auxiliaryType, bookId, tenantId) {
  return request({
    url: `/cwgxAi/common/auxiliary/type/${auxiliaryType}`,
    method: 'get',
    params: { bookId, tenantId }
  })
}

/**
 * 根据上级ID查询子项列表
 * @param {Number} parentId 上级ID
 * @param {Number} bookId 账簿ID
 * @param {Number} tenantId 租户ID
 * @returns {Promise}
 */
export function getAuxiliaryItemsByParentId(parentId, bookId, tenantId) {
  return request({
    url: '/cwgxAi/common/auxiliary/children',
    method: 'get',
    params: { parentId, bookId, tenantId }
  })
}

/**
 * 获取辅助核算类型列表
 * @param {Number} bookId 账簿ID
 * @param {Number} tenantId 租户ID
 * @returns {Promise}
 */
export function getAuxiliaryTypes(bookId, tenantId) {
  return request({
    url: '/cwgxAi/common/auxiliary/types',
    method: 'get',
    params: { bookId, tenantId }
  })
}

// ==================== 辅助核算项目 ====================

/**
 * 分页查询辅助核算项目
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getAuxiliaryAccountingPage(data) {
  return request({
    url: '/zbgl/auxiliary-accounting/page',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 保存辅助核算项目
 * @param {Object} data 辅助核算项目数据
 * @returns {Promise}
 */
export function saveAuxiliaryAccounting(data) {
  return request({
    url: '/zbgl/auxiliary-accounting/save',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 更新辅助核算项目
 * @param {Object} data 辅助核算项目数据
 * @returns {Promise}
 */
export function updateAuxiliaryAccounting(data) {
  return request({
    url: '/zbgl/auxiliary-accounting/update',
    method: 'put',
    data: transData(data)
  })
}

/**
 * 删除辅助核算项目
 * @param {Number} id 辅助核算项目ID
 * @returns {Promise}
 */
export function deleteAuxiliaryAccounting(id) {
  return request({
    url: `/zbgl/auxiliary-accounting/${id}`,
    method: 'delete'
  })
}

// ==================== 币种配置 ====================

/**
 * 获取币种列表
 * @param {Number} tenantId 租户ID
 * @returns {Promise}
 */
export function getCurrencyList(tenantId) {
  return request({
    url: '/zbgl/currency/list',
    method: 'get',
    params: { tenantId }
  })
}

/**
 * 保存币种配置
 * @param {Object} data 币种数据
 * @returns {Promise}
 */
export function saveCurrency(data) {
  return request({
    url: '/zbgl/currency/save',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 更新币种配置
 * @param {Object} data 币种数据
 * @returns {Promise}
 */
export function updateCurrency(data) {
  return request({
    url: '/zbgl/currency/update',
    method: 'put',
    data: transData(data)
  })
}

/**
 * 删除币种配置
 * @param {Number} id 币种ID
 * @returns {Promise}
 */
export function deleteCurrency(id) {
  return request({
    url: `/zbgl/currency/${id}`,
    method: 'delete'
  })
}

// ==================== 币种汇率管理 ====================

/**
 * 分页查询币种汇率列表
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getCurrencyRatePage(data) {
  return request({
    url: '/cwgxAi/public/currency/exchangeRate/getList',
    method: 'post',
    data
  })
}

/**
 * 保存或更新币种汇率
 * @param {Object} data 币种汇率数据
 * @returns {Promise}
 */
export function saveOrUpdateCurrencyRate(data) {
  return request({
    url: '/cwgxAi/public/currency/exchangeRate/saveOrUpdate',
    method: 'post',
    data
  })
}

/**
 * 根据ID查询币种汇率详情
 * @param {Number} rateId 汇率ID
 * @returns {Promise}
 */
export function getCurrencyRateById(rateId) {
  return request({
    url: `/cwgxAi/public/currency/exchangeRate/${rateId}`,
    method: 'get'
  })
}

/**
 * 删除币种汇率
 * @param {Number} rateId 汇率ID
 * @returns {Promise}
 */
export function deleteCurrencyRate(rateId) {
  return request({
    url: `/cwgxAi/public/currency/exchangeRate/${rateId}`,
    method: 'delete'
  })
}

/**
 * 批量删除币种汇率
 * @param {Array} rateIds 汇率ID列表
 * @returns {Promise}
 */
export function batchDeleteCurrencyRates(rateIds) {
  return request({
    url: '/cwgxAi/public/currency/exchangeRate/batch',
    method: 'delete',
    data: rateIds
  })
}

/**
 * 启用/禁用币种汇率
 * @param {Number} rateId 汇率ID
 * @param {Number} isEnabled 启用状态(0禁用1启用)
 * @returns {Promise}
 */
export function updateCurrencyRateStatus(rateId, isEnabled) {
  return request({
    url: `/cwgxAi/public/currency/exchangeRate/${rateId}/status`,
    method: 'put',
    params: { isEnabled }
  })
}

/**
 * 批量启用/禁用币种汇率
 * @param {Array} rateIds 汇率ID列表
 * @param {Number} isEnabled 启用状态(0禁用1启用)
 * @returns {Promise}
 */
export function batchUpdateCurrencyRateStatus(rateIds, isEnabled) {
  return request({
    url: '/cwgxAi/public/currency/exchangeRate/batch/status',
    method: 'put',
    data: rateIds,
    params: { isEnabled }
  })
}

/**
 * 检查币种和日期是否存在
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function checkCurrencyAndDateExists(params) {
  return request({
    url: '/cwgxAi/public/currency/exchangeRate/check',
    method: 'get',
    params
  })
}

/**
 * 获取最新汇率
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getLatestCurrencyRate(params) {
  return request({
    url: '/cwgxAi/public/currency/exchangeRate/latest',
    method: 'get',
    params
  })
}

/**
 * 根据日期范围查询汇率
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getCurrencyRatesByDateRange(params) {
  return request({
    url: '/cwgxAi/public/currency/exchangeRate/range',
    method: 'get',
    params
  })
}

/**
 * 获取所有启用的币种列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getEnabledCurrencies(params) {
  return request({
    url: '/cwgxAi/public/currency/exchangeRate/currencies',
    method: 'get',
    params
  })
}

/**
 * 获取本位币信息
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getBaseCurrency(params) {
  return request({
    url: '/cwgxAi/public/currency/exchangeRate/base',
    method: 'get',
    params
  })
}

/**
 * 设置本位币
 * @param {Number} rateId 汇率ID
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function setBaseCurrency(rateId, params) {
  return request({
    url: `/cwgxAi/public/currency/exchangeRate/${rateId}/base`,
    method: 'put',
    params
  })
}

/**
 * 获取币种编码列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getCurrencyRateCodes(params) {
  return request({
    url: '/cwgxAi/public/currency/exchangeRate/codes',
    method: 'get',
    params
  })
}

/**
 * 币种转换
 * @param {Object} params 转换参数
 * @returns {Promise}
 */
export function convertCurrency(params) {
  return request({
    url: '/cwgxAi/public/currency/exchangeRate/convert',
    method: 'get',
    params
  })
}

// ==================== 财务组织管理 ====================

/**
 * 获取组织树形结构
 * @param {Number} tenantId 租户ID
 * @param {Number} bookId 账簿ID
 * @returns {Promise}
 */
export function getOrganizationTree(tenantId, bookId) {
  return request({
    url: '/cwgxAi/common/finance-organization/tree',
    method: 'get',
    params: { tenantId, bookId }
  })
}

/**
 * 根据ID获取组织详情
 * @param {Number} id 组织ID
 * @returns {Promise}
 */
export function getOrganizationById(id) {
  return request({
    url: `/cwgxAi/common/finance-organization/${id}`,
    method: 'get'
  })
}

/**
 * 保存或更新组织
 * @param {Object} data 组织数据
 * @returns {Promise}
 */
export function saveOrUpdateOrganization(data) {
  return request({
    url: '/cwgxAi/common/finance-organization/saveOrUpdate',
    method: 'post',
    data
  })
}

/**
 * 删除组织
 * @param {Number} id 组织ID
 * @returns {Promise}
 */
export function deleteOrganization(id) {
  return request({
    url: `/cwgxAi/common/finance-organization/${id}`,
    method: 'delete'
  })
}

/**
 * 更新组织状态
 * @param {Number} id 组织ID
 * @param {Number} isEnabled 启用状态
 * @returns {Promise}
 */
export function updateOrganizationStatus(id, isEnabled) {
  return request({
    url: '/cwgxAi/common/finance-organization/status',
    method: 'put',
    params: { id, isEnabled }
  })
}

/**
 * 同步组织数据
 * @returns {Promise}
 */
export function syncOrganizations() {
  return request({
    url: '/cwgxAi/common/finance-organization/sync',
    method: 'post'
  })
}
