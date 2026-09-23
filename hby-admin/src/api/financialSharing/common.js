/*
 * @Description: 财务共享 - 财务公共模块 API
 * @Author: system
 * @Date: 2024-12-19
 */
import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// ==================== 多币种核算 API ====================

/**
 * 分页查询币种列表
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getCurrencyPage(data) {
  return request({
    url: '/cwgxAi/common/currency/getList',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 保存或更新币种
 * @param {Object} data 币种数据
 * @returns {Promise}
 */
export function saveOrUpdateCurrency(data) {
  return request({
    url: '/cwgxAi/common/currency/saveOrUpdate',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 删除币种
 * @param {Number} currencyId 币种ID
 * @returns {Promise}
 */
export function deleteCurrency(currencyId) {
  return request({
    url: `/cwgxAi/common/currency/${currencyId}`,
    method: 'delete'
  })
}

/**
 * 获取币种详情
 * @param {Number} currencyId 币种ID
 * @returns {Promise}
 */
export function getCurrencyById(currencyId) {
  return request({
    url: `/cwgxAi/common/currency/${currencyId}`,
    method: 'get'
  })
}

/**
 * 获取所有启用的币种
 * @returns {Promise}
 */
export function getActiveCurrencyList() {
  return request({
    url: '/cwgxAi/common/currency/active',
    method: 'get'
  })
}

// ==================== 汇率管理 API ====================

/**
 * 分页查询汇率列表
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getCurrencyRatePage(data) {
  return request({
    url: '/cwgxAi/common/currency-rate/getList',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 保存或更新汇率
 * @param {Object} data 汇率数据
 * @returns {Promise}
 */
export function saveOrUpdateCurrencyRate(data) {
  return request({
    url: '/cwgxAi/common/currency-rate/saveOrUpdate',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 删除汇率
 * @param {Number} rateId 汇率ID
 * @returns {Promise}
 */
export function deleteCurrencyRate(rateId) {
  return request({
    url: `/cwgxAi/common/currency-rate/${rateId}`,
    method: 'delete'
  })
}

/**
 * 获取指定日期的汇率
 * @param {String} fromCurrency 源币种
 * @param {String} toCurrency 目标币种
 * @param {String} rateDate 汇率日期
 * @returns {Promise}
 */
export function getCurrencyRate(fromCurrency, toCurrency, rateDate) {
  return request({
    url: '/cwgxAi/common/currency-rate/get',
    method: 'get',
    params: { fromCurrency, toCurrency, rateDate }
  })
}

/**
 * 批量导入汇率
 * @param {FormData} formData 文件数据
 * @returns {Promise}
 */
export function importCurrencyRates(formData) {
  return request({
    url: '/cwgxAi/common/currency-rate/import',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 从外部系统同步汇率
 * @param {Object} data 同步参数
 * @returns {Promise}
 */
export function syncCurrencyRates(data) {
  return request({
    url: '/cwgxAi/common/currency-rate/sync',
    method: 'post',
    data: transData(data)
  })
}

// ==================== 会计期间管理 API ====================

/**
 * 分页查询会计期间列表
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getAccountingPeriodPage(data) {
  return request({
    url: '/cwgxAi/common/accounting-period/getList',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 保存或更新会计期间
 * @param {Object} data 期间数据
 * @returns {Promise}
 */
export function saveOrUpdateAccountingPeriod(data) {
  return request({
    url: '/cwgxAi/common/accounting-period/saveOrUpdate',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 开启会计期间
 * @param {Number} periodId 期间ID
 * @returns {Promise}
 */
export function openAccountingPeriod(periodId) {
  return request({
    url: `/cwgxAi/common/accounting-period/${periodId}/open`,
    method: 'post'
  })
}

/**
 * 关闭会计期间
 * @param {Number} periodId 期间ID
 * @returns {Promise}
 */
export function closeAccountingPeriod(periodId) {
  return request({
    url: `/cwgxAi/common/accounting-period/${periodId}/close`,
    method: 'post'
  })
}

/**
 * 获取当前会计期间
 * @param {Number} bookId 账簿ID
 * @returns {Promise}
 */
export function getCurrentAccountingPeriod(bookId) {
  return request({
    url: '/cwgxAi/common/accounting-period/current',
    method: 'get',
    params: { bookId }
  })
}

/**
 * 获取期间状态统计
 * @param {Number} bookId 账簿ID
 * @returns {Promise}
 */
export function getAccountingPeriodStatistics(bookId) {
  return request({
    url: '/cwgxAi/common/accounting-period/statistics',
    method: 'get',
    params: { bookId }
  })
}

// ==================== 会计科目管理 API ====================

/**
 * 分页查询会计科目列表
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getAccountSubjectPage(data) {
  return request({
    url: '/cwgxAi/common/account-subject/getList',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取会计科目树形结构
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getAccountSubjectTree(params) {
  return request({
    url: '/cwgxAi/common/account-subject/tree',
    method: 'get',
    params
  })
}

/**
 * 保存或更新会计科目
 * @param {Object} data 科目数据
 * @returns {Promise}
 */
export function saveOrUpdateAccountSubject(data) {
  return request({
    url: '/cwgxAi/common/account-subject/saveOrUpdate',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 删除会计科目
 * @param {Number} subjectId 科目ID
 * @returns {Promise}
 */
export function deleteAccountSubject(subjectId) {
  return request({
    url: `/cwgxAi/common/account-subject/${subjectId}`,
    method: 'delete'
  })
}

/**
 * 启用/停用会计科目
 * @param {Number} subjectId 科目ID
 * @param {Boolean} enabled 是否启用
 * @returns {Promise}
 */
export function toggleAccountSubject(subjectId, enabled) {
  return request({
    url: `/cwgxAi/common/account-subject/${subjectId}/toggle`,
    method: 'post',
    params: { enabled }
  })
}

/**
 * 导入会计科目
 * @param {FormData} formData 文件数据
 * @returns {Promise}
 */
export function importAccountSubjects(formData) {
  return request({
    url: '/cwgxAi/common/account-subject/import',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 导出会计科目
 * @param {Object} params 导出参数
 * @returns {Promise}
 */
export function exportAccountSubjects(params) {
  return request({
    url: '/cwgxAi/common/account-subject/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

// ==================== 辅助核算项管理 API ====================

/**
 * 分页查询辅助核算项列表
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getAuxiliaryItemPage(data) {
  return request({
    url: '/cwgxAi/common/auxiliary-item/getList',
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
    url: '/cwgxAi/common/auxiliary-item/saveOrUpdate',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 删除辅助核算项
 * @param {Number} itemId 辅助核算项ID
 * @returns {Promise}
 */
export function deleteAuxiliaryItem(itemId) {
  return request({
    url: `/cwgxAi/common/auxiliary-item/${itemId}`,
    method: 'delete'
  })
}

/**
 * 获取辅助核算项详情
 * @param {Number} itemId 辅助核算项ID
 * @returns {Promise}
 */
export function getAuxiliaryItemById(itemId) {
  return request({
    url: `/cwgxAi/common/auxiliary-item/${itemId}`,
    method: 'get'
  })
}

/**
 * 根据类型获取辅助核算项列表
 * @param {String} itemType 辅助核算项类型
 * @returns {Promise}
 */
export function getAuxiliaryItemsByType(itemType) {
  return request({
    url: '/cwgxAi/common/auxiliary-item/by-type',
    method: 'get',
    params: { itemType }
  })
}

// ==================== 币种转换计算 API ====================

/**
 * 币种转换计算
 * @param {Object} data 转换参数
 * @returns {Promise}
 */
export function convertCurrency(data) {
  return request({
    url: '/cwgxAi/common/currency/convert',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 批量币种转换计算
 * @param {Object} data 批量转换参数
 * @returns {Promise}
 */
export function batchConvertCurrency(data) {
  return request({
    url: '/cwgxAi/common/currency/batch-convert',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取币种转换历史记录
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getCurrencyConversionHistory(params) {
  return request({
    url: '/cwgxAi/common/currency/conversion-history',
    method: 'get',
    params
  })
}

// ==================== 基础档案管理 API ====================

/**
 * 获取基础档案配置
 * @param {String} archiveType 档案类型
 * @returns {Promise}
 */
export function getArchiveConfig(archiveType) {
  return request({
    url: '/cwgxAi/common/archive/config',
    method: 'get',
    params: { archiveType }
  })
}

/**
 * 保存基础档案配置
 * @param {Object} data 配置数据
 * @returns {Promise}
 */
export function saveArchiveConfig(data) {
  return request({
    url: '/cwgxAi/common/archive/config',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 同步基础档案数据
 * @param {Object} data 同步参数
 * @returns {Promise}
 */
export function syncArchiveData(data) {
  return request({
    url: '/cwgxAi/common/archive/sync',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取档案同步状态
 * @param {String} syncTaskId 同步任务ID
 * @returns {Promise}
 */
export function getArchiveSyncStatus(syncTaskId) {
  return request({
    url: `/cwgxAi/common/archive/sync/status/${syncTaskId}`,
    method: 'get'
  })
}

// ==================== 公共下拉数据 API ====================

/**
 * 获取用户下拉列表
 * @param {Object} params 查询参数 (keyword, deptId, isEnabled)
 * @returns {Promise}
 */
export function getUserDropdownList(params) {
  return request({
    url: '/cwgxAi/common/data/users',
    method: 'get',
    params
  })
}

/**
 * 获取部门下拉列表
 * @param {Object} params 查询参数 (keyword, orgType, isEnabled)
 * @returns {Promise}
 */
export function getDepartmentDropdownList(params) {
  return request({
    url: '/cwgxAi/common/data/departments',
    method: 'get',
    params
  })
}

/**
 * 获取供应商下拉列表
 * @param {Object} params 查询参数 (keyword, isEnabled)
 * @returns {Promise}
 */
export function getSupplierDropdownList(params) {
  return request({
    url: '/cwgxAi/common/data/suppliers',
    method: 'get',
    params
  })
}

/**
 * 获取费用项目下拉列表
 * @param {Object} params 查询参数 (keyword, isEnabled)
 * @returns {Promise}
 */
export function getExpenseItemDropdownList(params) {
  return request({
    url: '/cwgxAi/common/data/expense-items',
    method: 'get',
    params
  })
}

/**
 * 获取会计科目下拉列表
 * @param {Object} params 查询参数 (keyword, subjectType-Integer, isEnabled)
 * @returns {Promise}
 */
export function getAccountSubjectDropdownList(params) {
  return request({
    url: '/cwgxAi/common/data/account-subjects',
    method: 'get',
    params
  })
}

/**
 * 获取项目下拉列表
 * @param {Object} params 查询参数 (keyword, projectStatus, isEnabled)
 * @returns {Promise}
 */
export function getProjectDropdownList(params) {
  return request({
    url: '/cwgxAi/common/data/projects',
    method: 'get',
    params
  })
}

/**
 * 获取合同下拉列表（用于预付款关联合同等场景）
 * @param {Object} params 查询参数 (keyword, contractStatus, supplierId)
 * @returns {Promise}
 */
export function getContractDropdownList(params) {
  return request({
    url: '/cwgxAi/common/data/contracts',
    method: 'get',
    params
  })
}

/**
 * 获取业务单据统计（首页统计卡片数据）
 * @returns {Promise}
 */
export function getBusinessDocumentStats() {
  return request({
    url: '/cwgxAi/common/data/document-stats',
    method: 'get'
  })
}
