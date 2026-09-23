/*
 * @Description: 财务共享 - 事项中心 API
 * @Author: system
 * @Date: 2024-12-19
 */
import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// ==================== 业务事项管理 API ====================

/**
 * 分页查询业务事项列表
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getBusinessTransactionPage(data) {
  return request({
    url: '/cwgxAi/transaction/business/getList',
    method: 'post',
    data: transData(data),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 保存或更新业务事项
 * @param {Object} data 业务事项数据
 * @returns {Promise}
 */
export function saveOrUpdateBusinessTransaction(data) {
  return request({
    url: '/cwgxAi/transaction/business/saveOrUpdate',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 根据ID查询业务事项详情
 * @param {Number} transactionId 事项ID
 * @returns {Promise}
 */
export function getBusinessTransactionById(transactionId) {
  return request({
    url: `/cwgxAi/transaction/business/${transactionId}`,
    method: 'get'
  })
}

/**
 * 删除业务事项
 * @param {Number} transactionId 事项ID
 * @returns {Promise}
 */
export function deleteBusinessTransaction(transactionId) {
  return request({
    url: `/cwgxAi/transaction/business/${transactionId}`,
    method: 'delete'
  })
}

/**
 * 批量删除业务事项
 * @param {Array} transactionIds 事项ID列表
 * @returns {Promise}
 */
export function batchDeleteBusinessTransactions(transactionIds) {
  return request({
    url: '/cwgxAi/transaction/business/batch',
    method: 'delete',
    data: transactionIds
  })
}

/**
 * 更新事项状态
 * @param {Number} transactionId 事项ID
 * @param {Number} transactionStatus 事项状态
 * @returns {Promise}
 */
export function updateBusinessTransactionStatus(transactionId, transactionStatus) {
  return request({
    url: `/cwgxAi/transaction/business/${transactionId}/status`,
    method: 'put',
    params: { transactionStatus }
  })
}

/**
 * 批量更新事项状态
 * @param {Array} transactionIds 事项ID列表
 * @param {Number} transactionStatus 事项状态
 * @returns {Promise}
 */
export function batchUpdateBusinessTransactionStatus(transactionIds, transactionStatus) {
  return request({
    url: '/cwgxAi/transaction/business/batch/status',
    method: 'put',
    data: transactionIds,
    params: { transactionStatus }
  })
}

/**
 * 检查事项编号是否存在
 * @param {String} transactionNo 事项编号
 * @param {Number} bookId 账簿ID
 * @param {Number} tenantId 租户ID
 * @param {Number} excludeId 排除的ID
 * @returns {Promise}
 */
export function checkBusinessTransactionNoExists(transactionNo, bookId, tenantId, excludeId) {
  return request({
    url: '/cwgxAi/transaction/business/check-no',
    method: 'get',
    params: {
      transactionNo,
      bookId,
      tenantId,
      excludeId
    }
  })
}

/**
 * 根据事项类型查询业务事项列表
 * @param {String} transactionType 事项类型
 * @param {Number} bookId 账簿ID
 * @param {Number} tenantId 租户ID
 * @returns {Promise}
 */
export function getBusinessTransactionsByType(transactionType, bookId, tenantId) {
  return request({
    url: '/cwgxAi/transaction/business/by-type',
    method: 'get',
    params: {
      transactionType,
      bookId,
      tenantId
    }
  })
}

/**
 * 根据日期范围查询业务事项列表
 * @param {String} startDate 开始日期
 * @param {String} endDate 结束日期
 * @param {Number} bookId 账簿ID
 * @param {Number} tenantId 租户ID
 * @returns {Promise}
 */
export function getBusinessTransactionsByDateRange(startDate, endDate, bookId, tenantId) {
  return request({
    url: '/cwgxAi/transaction/business/by-date-range',
    method: 'get',
    params: {
      startDate,
      endDate,
      bookId,
      tenantId
    }
  })
}

/**
 * 根据状态查询业务事项列表
 * @param {Number} transactionStatus 事项状态
 * @param {Number} bookId 账簿ID
 * @param {Number} tenantId 租户ID
 * @returns {Promise}
 */
export function getBusinessTransactionsByStatus(transactionStatus, bookId, tenantId) {
  return request({
    url: '/cwgxAi/transaction/business/by-status',
    method: 'get',
    params: {
      transactionStatus,
      bookId,
      tenantId
    }
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
    url: '/cwgxAi/transaction/business/types',
    method: 'get',
    params: {
      bookId,
      tenantId
    }
  })
}

/**
 * 获取来源系统列表
 * @param {Number} bookId 账簿ID
 * @param {Number} tenantId 租户ID
 * @returns {Promise}
 */
export function getSourceSystems(bookId, tenantId) {
  return request({
    url: '/cwgxAi/transaction/business/source-systems',
    method: 'get',
    params: {
      bookId,
      tenantId
    }
  })
}

/**
 * 统计事项数量按状态分组
 * @param {Number} bookId 账簿ID
 * @param {Number} tenantId 租户ID
 * @returns {Promise}
 */
export function countTransactionsByStatus(bookId, tenantId) {
  return request({
    url: '/cwgxAi/transaction/business/count-by-status',
    method: 'get',
    params: {
      bookId,
      tenantId
    }
  })
}

/**
 * 统计事项数量按类型分组
 * @param {Number} bookId 账簿ID
 * @param {Number} tenantId 租户ID
 * @returns {Promise}
 */
export function countTransactionsByType(bookId, tenantId) {
  return request({
    url: '/cwgxAi/transaction/business/count-by-type',
    method: 'get',
    params: {
      bookId,
      tenantId
    }
  })
}

// ==================== 事项分录管理 API ====================

/**
 * 分页查询事项分录列表
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getTransactionEntryPage(data) {
  return request({
    url: '/cwgxAi/transaction/entry/getList',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 保存或更新事项分录
 * @param {Object} data 事项分录数据
 * @returns {Promise}
 */
export function saveOrUpdateTransactionEntry(data) {
  return request({
    url: '/cwgxAi/transaction/entry/saveOrUpdate',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 根据ID查询事项分录详情
 * @param {Number} entryId 分录ID
 * @returns {Promise}
 */
export function getTransactionEntryById(entryId) {
  return request({
    url: `/cwgxAi/transaction/entry/${entryId}`,
    method: 'get'
  })
}

/**
 * 删除事项分录
 * @param {Number} entryId 分录ID
 * @returns {Promise}
 */
export function deleteTransactionEntry(entryId) {
  return request({
    url: `/cwgxAi/transaction/entry/${entryId}`,
    method: 'delete'
  })
}

/**
 * 批量删除事项分录
 * @param {Array} entryIds 分录ID列表
 * @returns {Promise}
 */
export function batchDeleteTransactionEntries(entryIds) {
  return request({
    url: '/cwgxAi/transaction/entry/batch',
    method: 'delete',
    data: entryIds
  })
}

/**
 * 根据事项ID删除分录
 * @param {Number} transactionId 事项ID
 * @returns {Promise}
 */
export function deleteTransactionEntriesByTransactionId(transactionId) {
  return request({
    url: `/cwgxAi/transaction/entry/by-transaction/${transactionId}`,
    method: 'delete'
  })
}

/**
 * 检查分录编号是否存在
 * @param {String} entryNo 分录编号
 * @param {Number} bookId 账簿ID
 * @param {Number} tenantId 租户ID
 * @param {Number} excludeId 排除的ID
 * @returns {Promise}
 */
export function checkTransactionEntryNoExists(entryNo, bookId, tenantId, excludeId) {
  return request({
    url: '/cwgxAi/transaction/entry/check-no',
    method: 'get',
    params: {
      entryNo,
      bookId,
      tenantId,
      excludeId
    }
  })
}

/**
 * 根据事项ID查询分录列表
 * @param {Number} transactionId 事项ID
 * @returns {Promise}
 */
export function getTransactionEntriesByTransactionId(transactionId) {
  return request({
    url: `/cwgxAi/transaction/entry/by-transaction/${transactionId}`,
    method: 'get'
  })
}

/**
 * 根据科目ID查询分录列表
 * @param {Number} subjectId 科目ID
 * @param {Number} bookId 账簿ID
 * @param {Number} tenantId 租户ID
 * @returns {Promise}
 */
export function getTransactionEntriesBySubjectId(subjectId, bookId, tenantId) {
  return request({
    url: '/cwgxAi/transaction/entry/by-subject',
    method: 'get',
    params: {
      subjectId,
      bookId,
      tenantId
    }
  })
}

/**
 * 根据币种查询分录列表
 * @param {String} currencyCode 币种编码
 * @param {Number} bookId 账簿ID
 * @param {Number} tenantId 租户ID
 * @returns {Promise}
 */
export function getTransactionEntriesByCurrency(currencyCode, bookId, tenantId) {
  return request({
    url: '/cwgxAi/transaction/entry/by-currency',
    method: 'get',
    params: {
      currencyCode,
      bookId,
      tenantId
    }
  })
}

/**
 * 计算事项分录借贷方合计
 * @param {Number} transactionId 事项ID
 * @returns {Promise}
 */
export function calculateTransactionTotal(transactionId) {
  return request({
    url: `/cwgxAi/transaction/entry/calculate-total/${transactionId}`,
    method: 'get'
  })
}

/**
 * 验证事项分录借贷平衡
 * @param {Number} transactionId 事项ID
 * @returns {Promise}
 */
export function validateTransactionBalance(transactionId) {
  return request({
    url: `/cwgxAi/transaction/entry/validate-balance/${transactionId}`,
    method: 'get'
  })
}

/**
 * 获取科目分录汇总
 * @param {Number} subjectId 科目ID
 * @param {Number} bookId 账簿ID
 * @param {Number} tenantId 租户ID
 * @returns {Promise}
 */
export function getSubjectEntrySummary(subjectId, bookId, tenantId) {
  return request({
    url: '/cwgxAi/transaction/entry/subject-summary',
    method: 'get',
    params: {
      subjectId,
      bookId,
      tenantId
    }
  })
}

/**
 * 获取币种分录汇总
 * @param {String} currencyCode 币种编码
 * @param {Number} bookId 账簿ID
 * @param {Number} tenantId 租户ID
 * @returns {Promise}
 */
export function getCurrencyEntrySummary(currencyCode, bookId, tenantId) {
  return request({
    url: '/cwgxAi/transaction/entry/currency-summary',
    method: 'get',
    params: {
      currencyCode,
      bookId,
      tenantId
    }
  })
}

/**
 * 统计分录数量按科目分组
 * @param {Number} bookId 账簿ID
 * @param {Number} tenantId 租户ID
 * @returns {Promise}
 */
export function countEntriesBySubject(bookId, tenantId) {
  return request({
    url: '/cwgxAi/transaction/entry/count-by-subject',
    method: 'get',
    params: {
      bookId,
      tenantId
    }
  })
}

/**
 * 统计分录金额按币种分组
 * @param {Number} bookId 账簿ID
 * @param {Number} tenantId 租户ID
 * @returns {Promise}
 */
export function sumEntriesByCurrency(bookId, tenantId) {
  return request({
    url: '/cwgxAi/transaction/entry/sum-by-currency',
    method: 'get',
    params: {
      bookId,
      tenantId
    }
  })
}
