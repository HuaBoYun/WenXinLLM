/*
 * @Description: 财务共享 - 存货核算模块 API
 * @Author: system
 * @Date: 2024-12-19
 */
import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// ==================== 存货核算管理 API ====================

/**
 * 分页查询存货核算列表
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getInventoryAccountingPage(data) {
  return request({
    url: '/cwgxAi/inventory/accounting/getList',
    method: 'post',
    data: transData(data),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 根据ID获取存货核算详情
 * @param {String} accountingId 核算ID
 * @returns {Promise}
 */
export function getInventoryAccountingById(accountingId) {
  return request({
    url: `/cwgxAi/inventory/accounting/getById/${accountingId}`,
    method: 'get'
  })
}

/**
 * 执行存货核算
 * @param {Object} data 核算参数
 * @returns {Promise}
 */
export function executeInventoryAccounting(data) {
  return request({
    url: '/cwgxAi/inventory/accounting/execute',
    method: 'post',
    data: transData(data),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 删除存货核算记录
 * @param {String} accountingId 核算ID
 * @returns {Promise}
 */
export function deleteInventoryAccounting(accountingId) {
  return request({
    url: `/cwgxAi/inventory/accounting/delete/${accountingId}`,
    method: 'delete'
  })
}

/**
 * 批量删除存货核算记录
 * @param {Array} accountingIds 核算ID数组
 * @returns {Promise}
 */
export function batchDeleteInventoryAccounting(accountingIds) {
  return request({
    url: '/cwgxAi/inventory/accounting/batchDelete',
    method: 'post',
    data: transData({ accountingIds }),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 获取存货核算统计
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getInventoryAccountingStatistics(params) {
  return request({
    url: '/cwgxAi/inventory/accounting/statistics',
    method: 'get',
    params: transData(params)
  })
}

/**
 * 批量执行存货核算
 * @param {Array} accountingIds 核算ID数组
 * @returns {Promise}
 */
export function batchExecuteInventoryAccounting(accountingIds) {
  return request({
    url: '/cwgxAi/inventory/accounting/batchExecute',
    method: 'post',
    data: transData({ accountingIds }),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 获取存货核算历史
 * @param {String} inventoryId 存货ID
 * @returns {Promise}
 */
export function getInventoryAccountingHistory(inventoryId) {
  return request({
    url: `/cwgxAi/inventory/accounting/history/${inventoryId}`,
    method: 'get'
  })
}

// ==================== 存货计价管理 API ====================

/**
 * 分页查询存货计价列表
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getInventoryValuationPage(data) {
  return request({
    url: '/cwgxAi/inventory/valuation/getList',
    method: 'post',
    data: transData(data),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 保存或更新存货计价方法
 * @param {Object} data 计价数据
 * @returns {Promise}
 */
export function saveOrUpdateInventoryValuation(data) {
  return request({
    url: '/cwgxAi/inventory/valuation/saveOrUpdate',
    method: 'post',
    data: transData(data),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 获取存货计价方法详情
 * @param {String} valuationId 计价ID
 * @returns {Promise}
 */
export function getInventoryValuationById(valuationId) {
  return request({
    url: `/cwgxAi/inventory/valuation/getById/${valuationId}`,
    method: 'get'
  })
}

/**
 * 删除存货计价方法
 * @param {String} valuationId 计价ID
 * @returns {Promise}
 */
export function deleteInventoryValuation(valuationId) {
  return request({
    url: `/cwgxAi/inventory/valuation/delete/${valuationId}`,
    method: 'delete'
  })
}

/**
 * 批量更新存货计价方法
 * @param {Object} data 更新数据
 * @returns {Promise}
 */
export function batchUpdateInventoryValuation(data) {
  return request({
    url: '/cwgxAi/inventory/valuation/batchUpdate',
    method: 'post',
    data: transData(data),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 批量删除存货计价方法
 * @param {Array} valuationIds 计价ID数组
 * @returns {Promise}
 */
export function batchDeleteInventoryValuation(valuationIds) {
  return request({
    url: '/cwgxAi/inventory/valuation/batchDelete',
    method: 'post',
    data: transData({ valuationIds }),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 计算存货成本
 * @param {String} valuationId 计价ID
 * @returns {Promise}
 */
export function calculateInventoryCost(valuationId) {
  return request({
    url: '/cwgxAi/inventory/valuation/calculate',
    method: 'post',
    data: transData({ valuationId }),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 获取仓库列表
 * @returns {Promise}
 */
export function getWarehouseList() {
  return request({
    url: '/cwgxAi/inventory/warehouse/list',
    method: 'get'
  })
}

/**
 * 导出存货计价数据
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function exportInventoryValuation(params) {
  return request({
    url: '/cwgxAi/inventory/valuation/export',
    method: 'post',
    data: transData(params),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// ==================== 成本结转管理 API ====================

/**
 * 分页查询成本结转列表
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getCostTransferPage(data) {
  return request({
    url: '/cwgxAi/inventory/costTransfer/getList',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 执行成本结转
 * @param {Object} data 结转参数
 * @returns {Promise}
 */
export function executeCostTransfer(data) {
  return request({
    url: '/cwgxAi/inventory/costTransfer/execute',
    method: 'post',
    data: transData(data),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 获取成本结转详情
 * @param {String} transferId 结转ID
 * @returns {Promise}
 */
export function getCostTransferById(transferId) {
  return request({
    url: `/cwgxAi/inventory/costTransfer/getById/${transferId}`,
    method: 'get'
  })
}

/**
 * 撤销成本结转
 * @param {String} transferId 结转ID
 * @returns {Promise}
 */
export function revokeCostTransfer(transferId) {
  return request({
    url: `/cwgxAi/inventory/costTransfer/revoke/${transferId}`,
    method: 'post'
  })
}

/**
 * 创建成本结转单
 * @param {Object} data 结转数据
 * @returns {Promise}
 */
export function createCostTransfer(data) {
  return request({
    url: '/cwgxAi/inventory/costTransfer/create',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 批量执行成本结转
 * @param {Array} transferIds 结转ID数组
 * @returns {Promise}
 */
export function batchExecuteCostTransfer(transferIds) {
  return request({
    url: '/cwgxAi/inventory/costTransfer/batchExecute',
    method: 'post',
    data: transData({ transferIds })
  })
}

/**
 * 批量撤销成本结转
 * @param {Array} transferIds 结转ID数组
 * @returns {Promise}
 */
export function batchRevokeCostTransfer(transferIds) {
  return request({
    url: '/cwgxAi/inventory/costTransfer/batchRevoke',
    method: 'post',
    data: transData({ transferIds })
  })
}

/**
 * 获取成本结转统计
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getCostTransferStatistics(params) {
  return request({
    url: '/cwgxAi/inventory/costTransfer/statistics',
    method: 'get',
    params: transData(params)
  })
}

/**
 * 导出成本结转数据
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function exportCostTransfer(params) {
  return request({
    url: '/cwgxAi/inventory/costTransfer/export',
    method: 'post',
    data: transData(params)
  })
}

// ==================== 存货盘点管理 API ====================

/**
 * 分页查询存货盘点列表
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getInventoryCheckPage(data) {
  return request({
    url: '/cwgxAi/inventory/check/getList',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: transData(data)
  })
}

/**
 * 创建存货盘点任务
 * @param {Object} data 盘点数据
 * @returns {Promise}
 */
export function createInventoryCheck(data) {
  return request({
    url: '/cwgxAi/inventory/check/create',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: transData(data)
  })
}

/**
 * 获取存货盘点详情
 * @param {String} checkId 盘点ID
 * @returns {Promise}
 */
export function getInventoryCheckById(checkId) {
  return request({
    url: `/cwgxAi/inventory/check/getById/${checkId}`,
    method: 'get'
  })
}

/**
 * 执行存货盘点
 * @param {String} checkId 盘点ID
 * @returns {Promise}
 */
export function executeInventoryCheck(checkId) {
  return request({
    url: `/cwgxAi/inventory/check/execute/${checkId}`,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 提交盘点结果
 * @param {String} checkId 盘点ID
 * @param {Object} data 盘点结果
 * @returns {Promise}
 */
export function submitInventoryCheckResult(checkId, data) {
  return request({
    url: `/cwgxAi/inventory/check/submit/${checkId}`,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: transData(data)
  })
}

/**
 * 审批盘点结果
 * @param {String} checkId 盘点ID
 * @param {Object} data 审批数据
 * @returns {Promise}
 */
export function approveInventoryCheck(checkId, data) {
  return request({
    url: `/cwgxAi/inventory/check/approve/${checkId}`,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: transData(data)
  })
}

/**
 * 获取盘点结果列表
 * @param {String} checkId 盘点ID
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getInventoryCheckResults(checkId, params) {
  return request({
    url: `/cwgxAi/inventory/check/results/${checkId}`,
    method: 'get',
    params: params
  })
}

/**
 * 处理盘点差异
 * @param {Object} data 处理数据
 * @returns {Promise}
 */
export function processInventoryCheckDifference(data) {
  return request({
    url: '/cwgxAi/inventory/check/process-difference',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: transData(data)
  })
}

/**
 * 批量执行盘点任务
 * @param {Array} checkIds 盘点ID数组
 * @returns {Promise}
 */
export function batchExecuteInventoryCheck(checkIds) {
  return request({
    url: '/cwgxAi/inventory/check/batchExecute',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: transData({ checkIds })
  })
}

/**
 * 获取存货盘点统计
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getInventoryCheckStatistics(params) {
  return request({
    url: '/cwgxAi/inventory/check/statistics',
    method: 'get',
    params: transData(params)
  })
}

/**
 * 导出盘点任务
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function exportInventoryCheck(params) {
  return request({
    url: '/cwgxAi/inventory/check/export',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: transData(params)
  })
}

// ==================== 存货分类管理 API ====================

/**
 * 获取存货分类树
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getInventoryCategoryTree(params) {
  return request({
    url: '/cwgxAi/inventory/category/getTree',
    method: 'get',
    params: transData(params)
  })
}

/**
 * 分页查询存货分类列表
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getInventoryCategoryPage(data) {
  return request({
    url: '/cwgxAi/inventory/category/getList',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 保存或更新存货分类
 * @param {Object} data 分类数据
 * @returns {Promise}
 */
export function saveOrUpdateInventoryCategory(data) {
  return request({
    url: '/cwgxAi/inventory/category/saveOrUpdate',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 删除存货分类
 * @param {String} categoryId 分类ID
 * @returns {Promise}
 */
export function deleteInventoryCategory(categoryId) {
  return request({
    url: `/cwgxAi/inventory/category/delete/${categoryId}`,
    method: 'delete'
  })
}

// ==================== 库存预警管理 API ====================

/**
 * 分页查询库存预警列表
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getInventoryAlertPage(data) {
  return request({
    url: '/cwgxAi/inventory/alert/getList',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: transData(data)
  })
}

/**
 * 保存或更新预警规则
 * @param {Object} data 预警规则数据
 * @returns {Promise}
 */
export function saveOrUpdateInventoryAlert(data) {
  return request({
    url: '/cwgxAi/inventory/alert/saveOrUpdate',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: transData(data)
  })
}

/**
 * 获取预警规则详情
 * @param {String} alertId 预警ID
 * @returns {Promise}
 */
export function getInventoryAlertById(alertId) {
  return request({
    url: `/cwgxAi/inventory/alert/getById/${alertId}`,
    method: 'get'
  })
}

/**
 * 删除预警规则
 * @param {String} alertId 预警ID
 * @returns {Promise}
 */
export function deleteInventoryAlert(alertId) {
  return request({
    url: `/cwgxAi/inventory/alert/delete/${alertId}`,
    method: 'delete'
  })
}

/**
 * 处理预警消息
 * @param {String} alertId 预警ID
 * @param {Object} data 处理数据
 * @returns {Promise}
 */
export function handleInventoryAlert(alertId, data) {
  return request({
    url: `/cwgxAi/inventory/alert/process/${alertId}`,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: transData(data)
  })
}

/**
 * 忽略预警消息
 * @param {String} alertId 预警ID
 * @returns {Promise}
 */
export function ignoreInventoryAlert(alertId) {
  return request({
    url: `/cwgxAi/inventory/alert/ignore/${alertId}`,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 获取预警统计数据
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getInventoryAlertStatistics(data) {
  return request({
    url: '/cwgxAi/inventory/alert/statistics',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: transData(data)
  })
}

/**
 * 批量处理预警
 * @param {Object} data 处理数据
 * @returns {Promise}
 */
export function batchProcessInventoryAlert(data) {
  return request({
    url: '/cwgxAi/inventory/alert/batchProcess',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: transData(data)
  })
}

/**
 * 分页查询预警规则列表
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getInventoryAlertRulePage(data) {
  return request({
    url: '/cwgxAi/inventory/alert/rule/getList',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: transData(data)
  })
}

/**
 * 保存或更新预警规则
 * @param {Object} data 规则数据
 * @returns {Promise}
 */
export function saveOrUpdateInventoryAlertRule(data) {
  return request({
    url: '/cwgxAi/inventory/alert/rule/saveOrUpdate',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: transData(data)
  })
}

/**
 * 删除预警规则
 * @param {Number} ruleId 规则ID
 * @returns {Promise}
 */
export function deleteInventoryAlertRule(ruleId) {
  return request({
    url: `/cwgxAi/inventory/alert/rule/delete/${ruleId}`,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 启用/禁用预警规则
 * @param {Object} data 包含ruleId和status
 * @returns {Promise}
 */
export function toggleInventoryAlertRuleStatus(data) {
  return request({
    url: '/cwgxAi/inventory/alert/rule/toggleStatus',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: transData(data)
  })
}

// ==================== 存货分析管理 API ====================

/**
 * 获取存货分析数据
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getInventoryAnalysis(data) {
  return request({
    url: '/cwgxAi/inventory/analysis/getData',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取存货结构分析
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getInventoryStructureAnalysis(data) {
  return request({
    url: '/cwgxAi/inventory/analysis/structure',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取存货周转率分析
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getInventoryTurnoverAnalysis(data) {
  return request({
    url: '/cwgxAi/inventory/analysis/turnover',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取ABC分类分析
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getInventoryAbcAnalysis(data) {
  return request({
    url: '/cwgxAi/inventory/analysis/abc',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取呆滞库存分析
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getInventorySlowMovingAnalysis(data) {
  return request({
    url: '/cwgxAi/inventory/analysis/slowMoving',
    method: 'post',
    data: transData(data)
  })
}

// ==================== 存货基础数据管理 API ====================

/**
 * 分页查询存货档案列表
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getInventoryMasterPage(data) {
  return request({
    url: '/cwgxAi/inventory/master/getList',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 保存或更新存货档案
 * @param {Object} data 存货数据
 * @returns {Promise}
 */
export function saveOrUpdateInventoryMaster(data) {
  return request({
    url: '/cwgxAi/inventory/master/saveOrUpdate',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取存货档案详情
 * @param {String} inventoryId 存货ID
 * @returns {Promise}
 */
export function getInventoryMasterById(inventoryId) {
  return request({
    url: `/cwgxAi/inventory/master/getById/${inventoryId}`,
    method: 'get'
  })
}

/**
 * 删除存货档案
 * @param {String} inventoryId 存货ID
 * @returns {Promise}
 */
export function deleteInventoryMaster(inventoryId) {
  return request({
    url: `/cwgxAi/inventory/master/delete/${inventoryId}`,
    method: 'delete'
  })
}

/**
 * 批量删除存货档案
 * @param {Array} inventoryIds 存货ID数组
 * @returns {Promise}
 */
export function batchDeleteInventoryMaster(inventoryIds) {
  return request({
    url: '/cwgxAi/inventory/master/batchDelete',
    method: 'post',
    data: transData({ inventoryIds })
  })
}

/**
 * 启用/禁用存货档案
 * @param {String} inventoryId 存货ID
 * @param {Boolean} enabled 是否启用
 * @returns {Promise}
 */
export function updateInventoryMasterStatus(inventoryId, enabled) {
  return request({
    url: `/cwgxAi/inventory/master/updateStatus/${inventoryId}`,
    method: 'post',
    data: transData({ enabled })
  })
}

// ==================== 仓库管理 API ====================

/**
 * 分页查询仓库列表
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getWarehousePage(data) {
  return request({
    url: '/cwgxAi/inventory/warehouse/getPage',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 保存或更新仓库
 * @param {Object} data 仓库数据
 * @returns {Promise}
 */
export function saveOrUpdateWarehouse(data) {
  return request({
    url: '/cwgxAi/inventory/warehouse/saveOrUpdate',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取仓库详情
 * @param {String} warehouseId 仓库ID
 * @returns {Promise}
 */
export function getWarehouseById(warehouseId) {
  return request({
    url: `/cwgxAi/inventory/warehouse/getById/${warehouseId}`,
    method: 'get'
  })
}

/**
 * 删除仓库
 * @param {String} warehouseId 仓库ID
 * @returns {Promise}
 */
export function deleteWarehouse(warehouseId) {
  return request({
    url: `/cwgxAi/inventory/warehouse/delete/${warehouseId}`,
    method: 'delete'
  })
}

// ==================== 导入导出 API ====================

/**
 * 导出存货核算数据
 * @param {Object} data 导出参数
 * @returns {Promise}
 */
export function exportInventoryAccounting(data) {
  return request({
    url: '/cwgxAi/inventory/accounting/export',
    method: 'post',
    data: transData(data),
    responseType: 'blob'
  })
}

/**
 * 导出存货档案数据
 * @param {Object} data 导出参数
 * @returns {Promise}
 */
export function exportInventoryMaster(data) {
  return request({
    url: '/cwgxAi/inventory/master/export',
    method: 'post',
    data: transData(data),
    responseType: 'blob'
  })
}

/**
 * 导入存货档案数据
 * @param {FormData} formData 文件数据
 * @returns {Promise}
 */
export function importInventoryMaster(formData) {
  return request({
    url: '/cwgxAi/inventory/master/import',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 下载存货档案导入模板
 * @returns {Promise}
 */
export function downloadInventoryMasterTemplate() {
  return request({
    url: '/cwgxAi/inventory/master/downloadTemplate',
    method: 'get',
    responseType: 'blob'
  })
}

// ==================== 统计分析 API ====================

/**
 * 获取存货统计数据
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getInventoryStatistics(data) {
  return request({
    url: '/cwgxAi/inventory/statistics/getData',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取存货趋势数据
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getInventoryTrend(data) {
  return request({
    url: '/cwgxAi/inventory/statistics/trend',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取存货成本分布
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getInventoryCostDistribution(data) {
  return request({
    url: '/cwgxAi/inventory/statistics/costDistribution',
    method: 'post',
    data: transData(data)
  })
}

// ==================== 存货分类管理 API ====================

/**
 * 查询存货分类树
 * @returns {Promise}
 */
export function getCategoryTree() {
  return request({
    url: '/cwgxAi/inventory/category/tree',
    method: 'get'
  })
}

/**
 * 查询分类详情
 * @param {Number} categoryId 分类ID
 * @returns {Promise}
 */
export function getCategoryDetail(categoryId) {
  return request({
    url: `/cwgxAi/inventory/category/detail/${categoryId}`,
    method: 'get'
  })
}

/**
 * 新增分类
 * @param {Object} data 分类数据
 * @returns {Promise}
 */
export function addCategory(data) {
  return request({
    url: '/cwgxAi/inventory/category/add',
    method: 'post',
    data: transData(data),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 更新分类
 * @param {Object} data 分类数据
 * @returns {Promise}
 */
export function updateCategory(data) {
  return request({
    url: '/cwgxAi/inventory/category/update',
    method: 'post',
    data: transData(data),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 删除分类
 * @param {Number} categoryId 分类ID
 * @returns {Promise}
 */
export function deleteCategory(categoryId) {
  return request({
    url: `/cwgxAi/inventory/category/delete/${categoryId}`,
    method: 'delete'
  })
}

/**
 * 批量删除分类
 * @param {Array} categoryIds 分类ID数组
 * @returns {Promise}
 */
export function batchDeleteCategory(categoryIds) {
  return request({
    url: '/cwgxAi/inventory/category/batchDelete',
    method: 'post',
    data: transData({ categoryIds }),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 更新分类排序
 * @param {Number} categoryId 分类ID
 * @param {Number} sortOrder 排序号
 * @returns {Promise}
 */
export function updateCategorySort(categoryId, sortOrder) {
  return request({
    url: '/cwgxAi/inventory/category/updateSort',
    method: 'post',
    data: transData({ categoryId, sortOrder }),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
