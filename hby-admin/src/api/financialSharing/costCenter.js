/*
 * @Description: 财务共享 - 成本中心模块 API
 * @Author: system
 * @Date: 2024-12-19
 */
import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// ==================== 成本中心基础管理 API ====================

/**
 * 分页查询成本中心列表
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getCostCenterPage(data) {
  return request({
    url: '/cwgxAi/ma/costcenter/getList',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 保存或更新成本中心
 * @param {Object} data 成本中心数据
 * @returns {Promise}
 */
export function saveOrUpdateCostCenter(data) {
  return request({
    url: '/cwgxAi/ma/costcenter/saveOrUpdate',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取成本中心详情
 * @param {Number} centerId 成本中心ID
 * @returns {Promise}
 */
export function getCostCenterById(centerId) {
  return request({
    url: `/cwgxAi/ma/costcenter/${centerId}`,
    method: 'get'
  })
}

/**
 * 删除成本中心
 * @param {String|Number} centerId 成本中心ID
 * @returns {Promise}
 */
export function deleteCostCenter(centerId) {
  // 参数验证
  if (centerId === null || centerId === undefined || centerId === '') {
    return Promise.reject(new Error('成本中心ID不能为空'))
  }

  // 转换为字符串以避免大整数精度丢失
  const idStr = String(centerId).trim()

  // 基本格式验证
  if (!/^\d+$/.test(idStr)) {
    return Promise.reject(new Error('成本中心ID必须为数字'))
  }

  if (idStr.startsWith('-') || idStr === '0') {
    return Promise.reject(new Error('成本中心ID必须为正数'))
  }

  // 统一使用字符串形式传递，避免精度丢失
  return request({
    url: `/cwgxAi/ma/costcenter/${idStr}`,
    method: 'delete'
  })
}

/**
 * 批量删除成本中心
 * @param {Array} centerIds 成本中心ID数组
 * @returns {Promise}
 */
export function batchDeleteCostCenter(centerIds) {
  // 参数验证
  if (!Array.isArray(centerIds)) {
    return Promise.reject(new Error('成本中心ID列表必须为数组'))
  }

  if (centerIds.length === 0) {
    return Promise.reject(new Error('请选择要删除的成本中心'))
  }

  // 验证每个ID，统一转换为字符串
  const validatedIds = []
  const invalidIds = []

  centerIds.forEach((id, index) => {
    if (id === null || id === undefined || id === '') {
      invalidIds.push(`位置${index + 1}: ID为空`)
      return
    }

    // 转换为字符串处理，避免大整数精度丢失
    const idStr = String(id).trim()

    // 基本格式验证
    if (!/^\d+$/.test(idStr)) {
      invalidIds.push(`位置${index + 1}: ID "${idStr}" 不是有效数字`)
      return
    }

    if (idStr.startsWith('-') || idStr === '0') {
      invalidIds.push(`位置${index + 1}: ID "${idStr}" 必须为正数`)
      return
    }

    // 统一使用字符串形式，避免精度丢失
    validatedIds.push(idStr)
  })

  if (invalidIds.length > 0) {
    return Promise.reject(new Error(`以下ID无效：${invalidIds.join(', ')}`))
  }

  return request({
    url: '/cwgxAi/ma/costcenter/batchDelete',
    method: 'delete',
    data: validatedIds
  })
}

/**
 * 批量启用/停用成本中心
 * @param {Array} centerIds 成本中心ID数组
 * @param {Number} isEnabled 是否启用(1启用0停用)
 * @returns {Promise}
 */
export function batchUpdateCostCenterStatus(centerIds, isEnabled) {
  return request({
    url: '/cwgxAi/ma/costcenter/batchUpdateStatus',
    method: 'post',
    data: {
      centerIds,
      isEnabled
    }
  })
}

/**
 * 获取成本中心树形结构
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getCostCenterTree(params) {
  return request({
    url: '/cwgxAi/ma/costcenter/tree',
    method: 'get',
    params
  })
}

// ==================== 成本归集 API ====================

/**
 * 分页查询成本归集列表
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getCostCollectionPage(data) {
  const transedData = transData(data)
  console.log('API调用前 - 原始数据:', data)
  console.log('API调用前 - 转换后数据:', transedData)

  return request({
    url: '/cwgxAi/ma/costcenter/collection/getList',
    method: 'post',
    data: transedData
  })
}

/**
 * 获取成本归集列表（别名）
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getCostCollectionList(data) {
  return getCostCollectionPage(data)
}

/**
 * 开始成本归集
 * @param {Object} data 归集参数
 * @returns {Promise}
 */
export function startCostCollection(data) {
  return request({
    url: '/cwgxAi/ma/costcenter/collection/start',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 审核成本归集
 * @param {Number} collectionId 归集ID
 * @param {Object} data 审核数据
 * @returns {Promise}
 */
export function auditCostCollection(collectionId, data) {
  // 参数验证
  if (collectionId === null || collectionId === undefined || collectionId === '') {
    return Promise.reject(new Error('归集ID不能为空'))
  }

  // 转换为字符串以避免大整数精度丢失
  const idStr = String(collectionId).trim()

  // 基本格式验证
  if (!/^\d+$/.test(idStr)) {
    return Promise.reject(new Error('归集ID必须为数字'))
  }

  console.log('审核成本归集 - 原始ID:', collectionId, '类型:', typeof collectionId)
  console.log('审核成本归集 - 转换后字符串ID:', idStr)

  return request({
    url: `/cwgxAi/ma/costcenter/collection/${idStr}/audit`,
    method: 'post',
    data: transData(data),
    // 后端 controller 用了 @RequestBody，期望 JSON
    // 全局默认 Content-Type 是 form-urlencoded（net.config.js），这里覆盖为 JSON
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

/**
 * 取消成本归集
 * @param {Number|String} collectionId 归集ID
 * @returns {Promise}
 */
export function cancelCostCollection(collectionId) {
  // 参数验证
  if (collectionId === null || collectionId === undefined || collectionId === '') {
    return Promise.reject(new Error('归集ID不能为空'))
  }

  // 转换为字符串以避免大整数精度丢失
  const idStr = String(collectionId).trim()

  // 基本格式验证
  if (!/^\d+$/.test(idStr)) {
    return Promise.reject(new Error('归集ID必须为数字'))
  }

  console.log('取消成本归集 - 原始ID:', collectionId, '类型:', typeof collectionId)
  console.log('取消成本归集 - 转换后字符串ID:', idStr)

  return request({
    url: `/cwgxAi/ma/costcenter/collection/${idStr}/cancel`,
    method: 'post'
  })
}

/**
 * 获取成本归集统计数据
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getCostCollectionStats(params) {
  return request({
    url: '/cwgxAi/ma/costcenter/collection/stats',
    method: 'get',
    params
  })
}

/**
 * 获取成本归集详情
 * @param {Number|String} collectionId 归集ID
 * @returns {Promise}
 */
export function getCostCollectionDetail(collectionId) {
  if (collectionId === null || collectionId === undefined || collectionId === '') {
    return Promise.reject(new Error('归集ID不能为空'))
  }

  const idStr = String(collectionId).trim()
  if (!/^\d+$/.test(idStr)) {
    return Promise.reject(new Error('归集ID必须为数字'))
  }

  return request({
    url: `/cwgxAi/ma/costcenter/collection/${idStr}`,
    method: 'get'
  })
}

/**
 * 批量审核成本归集
 * @param {Array} collectionIds 归集ID数组
 * @param {Object} auditData 审核数据
 * @returns {Promise}
 */
export function batchAuditCostCollection(collectionIds, auditData) {
  if (!Array.isArray(collectionIds) || collectionIds.length === 0) {
    return Promise.reject(new Error('请选择要审核的归集记录'))
  }

  return request({
    url: '/cwgxAi/ma/costcenter/collection/batchAudit',
    method: 'post',
    data: {
      collectionIds,
      ...auditData
    },
    // 后端用 @RequestBody，需要 JSON 协议
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

/**
 * 批量取消成本归集
 * @param {Array} collectionIds 归集ID数组
 * @returns {Promise}
 */
export function batchCancelCostCollection(collectionIds) {
  if (!Array.isArray(collectionIds) || collectionIds.length === 0) {
    return Promise.reject(new Error('请选择要取消的归集记录'))
  }

  return request({
    url: '/cwgxAi/ma/costcenter/collection/batchCancel',
    method: 'post',
    data: {
      collectionIds
    }
  })
}

/**
 * 自动归集成本
 * @param {Object} data 自动归集参数
 * @returns {Promise}
 */
export function autoCostCollection(data) {
  return request({
    url: '/cwgxAi/ma/costcenter/collection/autoCollection',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取归集规则
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getCollectionRules(params) {
  return request({
    url: '/cwgxAi/ma/costcenter/collection/rules',
    method: 'get',
    params
  })
}

// ==================== 成本分摊 API ====================

/**
 * 分页查询成本分摊列表
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getCostAllocationPage(data) {
  return request({
    url: '/cwgxAi/ma/costcenter/allocation/getList',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取成本分摊列表（别名）
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getCostAllocationList(data) {
  return getCostAllocationPage(data)
}

/**
 * 开始成本分摊
 * @param {Object} data 分摊参数
 * @returns {Promise}
 */
export function startCostAllocation(data) {
  // 处理大整数精度丢失问题
  const processedData = { ...data }

  // 检查并处理源成本中心ID
  if (processedData.sourceCostCenterId) {
    const sourceIdStr = String(processedData.sourceCostCenterId)
    if (sourceIdStr.length > 15 || !Number.isSafeInteger(Number(sourceIdStr))) {
      processedData.sourceCostCenterId = sourceIdStr
      console.log('成本分摊检测到大ID，使用字符串形式:', sourceIdStr)
    }
  }

  // 检查并处理目标成本中心ID数组
  if (processedData.targetCostCenterIds && Array.isArray(processedData.targetCostCenterIds)) {
    processedData.targetCostCenterIds = processedData.targetCostCenterIds.map(id => {
      const idStr = String(id)
      if (idStr.length > 15 || !Number.isSafeInteger(Number(idStr))) {
        console.log('目标成本中心ID使用字符串形式:', idStr)
        return idStr
      }
      return Number(idStr)
    })
  }

  // 添加调试日志
  console.log('成本分摊请求数据 - 源成本中心ID:', processedData.sourceCostCenterId, '类型:', typeof processedData.sourceCostCenterId)
  console.log('成本分摊请求数据 - 目标成本中心IDs:', processedData.targetCostCenterIds)

  return request({
    url: '/cwgxAi/ma/costcenter/allocation/start',
    method: 'post',
    data: transData(processedData),
    // 后端用 @RequestBody，需要 JSON 协议
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

/**
 * 获取成本分摊详情
 * @param {Number} allocationId 分摊ID
 * @returns {Promise}
 */
export function getCostAllocationDetail(allocationId) {
  return request({
    url: `/cwgxAi/ma/costcenter/allocation/${allocationId}/detail`,
    method: 'get'
  })
}

/**
 * 审核成本分摊
 * @param {Number} allocationId 分摊ID
 * @param {Object} data 审核数据
 * @returns {Promise}
 */
export function auditCostAllocation(allocationId, data) {
  return request({
    url: `/cwgxAi/ma/costcenter/allocation/${allocationId}/audit`,
    method: 'post',
    data: transData(data),
    // 后端用 @RequestBody，需要 JSON 协议
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

/**
 * 取消成本分摊
 * @param {Number} allocationId 分摊ID
 * @returns {Promise}
 */
export function cancelCostAllocation(allocationId) {
  // 参数验证
  if (allocationId === null || allocationId === undefined || allocationId === '') {
    return Promise.reject(new Error('分摊ID不能为空'))
  }

  // 转换为字符串以避免大整数精度丢失
  const idStr = String(allocationId).trim()

  // 基本格式验证
  if (!/^\d+$/.test(idStr)) {
    return Promise.reject(new Error('分摊ID必须为数字'))
  }

  console.log('取消成本分摊 - 原始ID:', allocationId, '类型:', typeof allocationId)
  console.log('取消成本分摊 - 转换后字符串ID:', idStr)

  return request({
    url: `/cwgxAi/ma/costcenter/allocation/${idStr}/cancel`,
    method: 'post'
  })
}

/**
 * 获取成本分摊概览数据
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getCostAllocationOverview(params) {
  return request({
    url: '/cwgxAi/ma/costcenter/allocation/overview',
    method: 'get',
    params
  })
}

/**
 * 获取成本分摊明细列表
 * @param {Number} allocationId 分摊ID
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getAllocationDetailList(allocationId, params) {
  return request({
    url: `/cwgxAi/ma/costcenter/allocation/${allocationId}/details`,
    method: 'get',
    params
  })
}

/**
 * 批量审核成本分摊
 * @param {Array} allocationIds 分摊ID数组
 * @param {Object} auditData 审核数据
 * @returns {Promise}
 */
export function batchAuditCostAllocation(allocationIds, auditData) {
  return request({
    url: '/cwgxAi/ma/costcenter/allocation/batchAudit',
    method: 'post',
    data: {
      allocationIds,
      ...auditData
    },
    // 后端用 @RequestBody，需要 JSON 协议
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

/**
 * 批量分摊成本分摊
 * @param {Array} allocationIds 分摊ID数组
 * @param {Object} allocateData 分摊数据
 * @returns {Promise}
 */
export function batchAllocateCostAllocation(allocationIds, allocateData) {
  return request({
    url: '/cwgxAi/ma/costcenter/allocation/batchAllocate',
    method: 'post',
    data: {
      allocationIds,
      ...allocateData
    },
    // 后端用 @RequestBody，需要 JSON 协议
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

/**
 * 分摊成本分摊
 * @param {Number} allocationId 分摊ID
 * @param {Object} data 分摊数据
 * @returns {Promise}
 */
export function allocateCostAllocation(allocationId, data) {
  return request({
    url: `/cwgxAi/ma/costcenter/allocation/${allocationId}/allocate`,
    method: 'post',
    data: transData(data),
    // 后端用 @RequestBody，需要 JSON 协议
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

/**
 * 批量删除成本分摊
 * @param {Array} allocationIds 分摊ID数组
 * @returns {Promise}
 */
export function batchDeleteCostAllocation(allocationIds) {
  return request({
    url: '/cwgxAi/ma/costcenter/allocation/batchDelete',
    method: 'post',
    data: {
      allocationIds
    },
    // 后端用 @RequestBody，需要 JSON 协议
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

/**
 * 获取分摊规则列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getAllocationRules(params) {
  return request({
    url: '/cwgxAi/ma/costcenter/allocation/rules',
    method: 'get',
    params
  })
}

/**
 * 获取分摊基础数据
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getAllocationBasis(params) {
  return request({
    url: '/cwgxAi/ma/costcenter/allocation/basis',
    method: 'get',
    params
  })
}

// ==================== 成本预算 API ====================

/**
 * 分页查询成本预算列表
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getCostBudgetPage(data) {
  return request({
    url: '/cwgxAi/ma/costcenter/budget/getList',
    method: 'post',
    data: transData(data),
    // 后端用 @RequestBody，需要 JSON 协议
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

/**
 * 分页查询成本预算列表 (别名)
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getCostBudgetList(data) {
  return getCostBudgetPage(data)
}

/**
 * 保存或更新成本预算
 * @param {Object} data 预算数据
 * @returns {Promise}
 */
export function saveOrUpdateCostBudget(data) {
  // 处理大整数精度丢失问题
  const processedData = { ...data }

  // 确保ID字段为字符串类型，避免精度丢失
  if (processedData.budgetId) {
    processedData.budgetId = String(processedData.budgetId)
    console.log('保存成本预算 - budgetId转换为字符串:', processedData.budgetId)
  }

  if (processedData.centerId) {
    processedData.centerId = String(processedData.centerId)
    console.log('保存成本预算 - centerId转换为字符串:', processedData.centerId)
  }

  console.log('保存成本预算 - 最终数据:', processedData)
  return request({
    url: '/cwgxAi/ma/costcenter/budget/saveOrUpdate',
    method: 'post',
    data: transData(processedData),
    // 后端用 @RequestBody，需要 JSON 协议
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

/**
 * 审批成本预算
 * @param {String} budgetId 预算ID
 * @param {Object} data 审批数据
 * @returns {Promise}
 */
export function approveCostBudget(budgetId, data) {
  // 确保budgetId为字符串类型，避免精度丢失
  const idStr = String(budgetId)
  return request({
    url: `/cwgxAi/ma/costcenter/budget/${idStr}/approve`,
    method: 'post',
    data: transData(data)
  })
}

/**
 * 删除成本预算
 * @param {String} budgetId 预算ID
 * @returns {Promise}
 */
export function deleteCostBudget(budgetId) {
  // 参数验证
  if (budgetId === null || budgetId === undefined || budgetId === '') {
    return Promise.reject(new Error('预算ID不能为空'))
  }

  // 直接使用字符串类型，避免大整数精度丢失
  const idStr = String(budgetId).trim()

  console.log('删除成本预算 - 原始ID:', budgetId, '类型:', typeof budgetId)
  console.log('删除成本预算 - 转换后字符串ID:', idStr)

  // 统一使用字符串形式传递，避免精度丢失
  return request({
    url: `/cwgxAi/ma/costcenter/budget/${idStr}`,
    method: 'delete'
  })
}

/**
 * 获取成本预算概览数据
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getCostBudgetOverview(params) {
  return request({
    url: '/cwgxAi/ma/costcenter/budget/overview',
    method: 'get',
    params
  })
}

/**
 * 获取成本预算统计数据
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getCostBudgetStats(params) {
  return request({
    url: '/cwgxAi/ma/costcenter/budget/stats',
    method: 'get',
    params
  })
}

/**
 * 获取成本预算详情
 * @param {String} budgetId 预算ID
 * @returns {Promise}
 */
export function getCostBudgetDetail(budgetId) {
  // 确保budgetId为字符串类型，避免精度丢失
  const idStr = String(budgetId)
  return request({
    url: `/cwgxAi/ma/costcenter/budget/${idStr}`,
    method: 'get'
  })
}

/**
 * 批量审批成本预算
 * @param {Array} budgetIds 预算ID数组
 * @param {Object} auditData 审批数据
 * @returns {Promise}
 */
export function batchApproveCostBudget(budgetIds, auditData) {
  if (!Array.isArray(budgetIds) || budgetIds.length === 0) {
    return Promise.reject(new Error('请选择要审批的预算记录'))
  }

  return request({
    url: '/cwgxAi/ma/costcenter/budget/batchApprove',
    method: 'post',
    data: {
      budgetIds,
      ...auditData
    },
    // 后端用 @RequestBody，需要 JSON 协议
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

/**
 * 获取成本预算监控数据
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getCostBudgetMonitorData(params) {
  return request({
    url: '/cwgxAi/ma/costcenter/budget/monitor',
    method: 'post',
    data: params,
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

/**
 * 获取成本预算分析数据
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getCostBudgetAnalysisData(params) {
  return request({
    url: '/cwgxAi/ma/costcenter/budget/analysis',
    method: 'post',
    data: params,
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

// ==================== 成本控制 API ====================

/**
 * 分页查询成本控制列表
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getCostControlPage(data) {
  return request({
    url: '/cwgxAi/ma/costcenter/control/getList',
    method: 'post',
    data: transData(data),
    // 后端用 @RequestBody，需要 JSON 协议
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

/**
 * 保存或更新成本控制规则
 * @param {Object} data 控制规则数据
 * @returns {Promise}
 */
export function saveOrUpdateCostControl(data) {
  return request({
    url: '/cwgxAi/ma/costcenter/control/saveOrUpdate',
    method: 'post',
    data: transData(data),
    // 后端用 @RequestBody，需要 JSON 协议
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

/**
 * 启用/停用成本控制规则
 * @param {Number} controlId 控制规则ID
 * @param {Number} isEnabled 是否启用(1启用0停用)
 * @returns {Promise}
 */
export function updateCostControlStatus(controlId, isEnabled) {
  return request({
    url: `/cwgxAi/ma/costcenter/control/${controlId}/status`,
    method: 'post',
    data: { isEnabled },
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

/**
 * 批量切换成本控制启用状态（一次请求处理多个）
 * @param {Array<string>} controlIds 控制规则 ID 列表
 * @param {number} isEnabled 1=启用 / 0=停用
 * @returns {Promise}
 */
export function batchUpdateCostControlStatus(controlIds, isEnabled) {
  return request({
    url: '/cwgxAi/ma/costcenter/control/batchUpdateStatus',
    method: 'post',
    data: { controlIds, isEnabled },
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

/**
 * 获取成本控制概览数据
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getCostControlOverview(params) {
  return request({
    url: '/cwgxAi/ma/costcenter/control/overview',
    method: 'get',
    params
  })
}

// ==================== 成本分析 API ====================

/**
 * 获取成本分析数据
 * @param {Object} data 分析参数
 * @returns {Promise}
 */
export function getCostAnalysisData(data) {
  return request({
    url: '/cwgxAi/ma/costcenter/analysis/data',
    method: 'post',
    data: transData(data),
    // 后端用 @RequestBody，需要 JSON 协议
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

/**
 * 获取成本结构分析数据
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getCostStructureAnalysis(params) {
  return request({
    url: '/cwgxAi/ma/costcenter/analysis/structure',
    method: 'get',
    params
  })
}

/**
 * 获取成本趋势分析数据
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getCostTrendAnalysis(params) {
  return request({
    url: '/cwgxAi/ma/costcenter/analysis/trend',
    method: 'get',
    params
  })
}

/**
 * 获取成本对比分析数据
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getCostCompareAnalysis(params) {
  return request({
    url: '/cwgxAi/ma/costcenter/analysis/compare',
    method: 'get',
    params
  })
}

/**
 * 导出成本分析报告
 * @param {Object} data 导出参数
 * @returns {Promise}
 */
export function exportCostAnalysisReport(data) {
  return request({
    url: '/cwgxAi/ma/costcenter/analysis/export',
    method: 'post',
    data: transData(data),
    responseType: 'blob',
    // 后端用 @RequestBody，需要 JSON 协议（导出请求体仍是 JSON，仅响应是二进制）
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

// ==================== 成本归集规则 API ====================

/**
 * 分页查询成本归集规则列表
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getCostCollectionRulePage(data) {
  return request({
    url: '/cwgxAi/ma/costcenter/collection/rule/getList',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 保存或更新成本归集规则
 * @param {Object} data 规则数据
 * @returns {Promise}
 */
export function saveOrUpdateCostCollectionRule(data) {
  return request({
    url: '/cwgxAi/ma/costcenter/collection/rule/saveOrUpdate',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 删除成本归集规则
 * @param {Number} ruleId 规则ID
 * @returns {Promise}
 */
export function deleteCostCollectionRule(ruleId) {
  return request({
    url: `/cwgxAi/ma/costcenter/collection/rule/${ruleId}`,
    method: 'delete'
  })
}

/**
 * 启用/停用成本归集规则
 * @param {Number} ruleId 规则ID
 * @param {Number} isEnabled 是否启用(1启用0停用)
 * @returns {Promise}
 */
export function updateCostCollectionRuleStatus(ruleId, isEnabled) {
  return request({
    url: `/cwgxAi/ma/costcenter/collection/rule/${ruleId}/status`,
    method: 'post',
    data: { isEnabled }
  })
}

// ==================== 成本分摊规则 API ====================

/**
 * 分页查询成本分摊规则列表
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getCostAllocationRulePage(data) {
  return request({
    url: '/cwgxAi/ma/costcenter/allocation/rule/getList',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 保存或更新成本分摊规则
 * @param {Object} data 规则数据
 * @returns {Promise}
 */
export function saveOrUpdateCostAllocationRule(data) {
  return request({
    url: '/cwgxAi/ma/costcenter/allocation/rule/saveOrUpdate',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 删除成本分摊规则
 * @param {Number} ruleId 规则ID
 * @returns {Promise}
 */
export function deleteCostAllocationRule(ruleId) {
  return request({
    url: `/cwgxAi/ma/costcenter/allocation/rule/${ruleId}`,
    method: 'delete'
  })
}

/**
 * 执行成本分摊
 * @param {Object} data 分摊参数
 * @returns {Promise}
 */
export function executeCostAllocation(data) {
  return request({
    url: '/cwgxAi/ma/costcenter/allocation/execute',
    method: 'post',
    data: transData(data)
  })
}

// ==================== 成本预算执行监控 API ====================

/**
 * 获取成本预算执行情况
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getCostBudgetExecution(params) {
  return request({
    url: '/cwgxAi/ma/costcenter/budget/execution',
    method: 'get',
    params
  })
}

/**
 * 获取成本预算差异分析
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getCostBudgetVarianceAnalysis(params) {
  return request({
    url: '/cwgxAi/ma/costcenter/budget/variance',
    method: 'get',
    params
  })
}

/**
 * 调整成本预算
 * @param {String} budgetId 预算ID
 * @param {Object} data 调整数据
 * @returns {Promise}
 */
export function adjustCostBudget(budgetId, data) {
  // 确保budgetId为字符串类型，避免精度丢失
  const idStr = String(budgetId)
  return request({
    url: `/cwgxAi/ma/costcenter/budget/${idStr}/adjust`,
    method: 'post',
    data: transData(data),
    // 后端用 @RequestBody，需要 JSON 协议
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

// ==================== 成本控制预警 API ====================

/**
 * 获取成本控制预警列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getCostControlAlerts(params) {
  return request({
    url: '/cwgxAi/ma/costcenter/control/alerts',
    method: 'get',
    params
  })
}

/**
 * 处理成本控制预警
 * @param {Number} alertId 预警ID
 * @param {Object} data 处理数据
 * @returns {Promise}
 */
export function handleCostControlAlert(alertId, data) {
  return request({
    url: `/cwgxAi/ma/costcenter/control/alert/${alertId}/handle`,
    method: 'post',
    data: transData(data),
    // 后端用 @RequestBody，需要 JSON 协议
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

/**
 * 获取成本控制效果评估
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getCostControlEffectiveness(params) {
  return request({
    url: '/cwgxAi/ma/costcenter/control/effectiveness',
    method: 'get',
    params
  })
}

// ==================== 成本分析报告 API ====================

/**
 * 获取成本差异分析数据
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getCostVarianceAnalysis(params) {
  return request({
    url: '/cwgxAi/ma/costcenter/analysis/variance',
    method: 'get',
    params
  })
}

/**
 * 获取成本绩效评价数据
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getCostPerformanceEvaluation(params) {
  return request({
    url: '/cwgxAi/ma/costcenter/analysis/performance',
    method: 'get',
    params
  })
}

/**
 * 生成成本分析报告
 * @param {Object} data 报告参数
 * @returns {Promise}
 */
export function generateCostAnalysisReport(data) {
  return request({
    url: '/cwgxAi/ma/costcenter/analysis/report/generate',
    method: 'post',
    data: transData(data),
    // 后端用 @RequestBody，需要 JSON 协议
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

// ==================== 导出功能 API ====================

/**
 * 导出成本中心列表
 * @param {Object} params 导出参数
 * @returns {Promise}
 */
export function exportCostCenterList(params) {
  return request({
    url: '/cwgxAi/ma/costcenter/export',
    method: 'get',
    params,
    responseType: 'blob'
  }).then(async response => {
    // axios 拦截器在 blob 模式下返回完整 response 对象 { data, headers, status }
    // 注意：必须用 response.data（真正的 Blob），而不是 response（整个对象）
    const blobData = response.data

    // 错误嗅探：后端异常时会写 application/json 而不是 xlsx
    // 如果 Content-Type 含 json，说明是错误响应，需要解析为 JSON 显示真正错误
    const contentType = (response.headers && (response.headers['content-type'] || response.headers['Content-Type'])) || ''
    if (contentType.indexOf('application/json') !== -1) {
      const errText = await blobData.text()
      let errMsg = '导出失败'
      try {
        const errJson = JSON.parse(errText)
        errMsg = errJson.msg || errJson.message || errMsg
      } catch (e) {
        errMsg = errText || errMsg
      }
      return Promise.reject({ code: 0, message: errMsg })
    }

    // 正常路径：拼装 Excel Blob
    const blob = new Blob([blobData], {
      type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8'
    })

    // 从响应头提取文件名
    let fileName = '成本中心数据.xlsx'
    const contentDisposition = response.headers && response.headers['content-disposition']
    if (contentDisposition) {
      const fileNameMatch = contentDisposition.match(/filename\*=utf-8''(.+?)(?:;|$)/)
      if (fileNameMatch && fileNameMatch[1]) {
        fileName = decodeURIComponent(fileNameMatch[1])
      }
    }

    // 创建下载链接
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.style.display = 'none'
    link.href = url
    link.download = fileName

    // 触发下载
    document.body.appendChild(link)
    link.click()

    // 清理
    document.body.removeChild(link)
    window.URL.revokeObjectURL(url)

    return Promise.resolve({
      code: 1,
      message: '导出成功',
      fileName: fileName
    })
  }).catch(error => {
    console.error('导出成本中心列表失败:', error)
    // error 可能是 axios error，也可能是上面 reject 的 { code, message }
    const msg = (error && (error.message || error.msg)) || '未知错误'
    return Promise.reject({
      code: 0,
      message: '导出失败: ' + msg
    })
  })
}

/**
 * 导出成本归集结果
 * @param {Object} data 导出参数
 * @returns {Promise}
 */
export function exportCostCollectionResult(data) {
  return request({
    url: '/cwgxAi/ma/costcenter/collection/export',
    method: 'post',
    data: transData(data),
    responseType: 'blob'
  })
}

/**
 * 导出成本分摊结果
 * @param {Object} data 导出参数
 * @returns {Promise}
 */
export function exportCostAllocationResult(data) {
  return request({
    url: '/cwgxAi/ma/costcenter/allocation/export',
    method: 'post',
    data: transData(data),
    responseType: 'blob',
    // 后端用 @RequestBody，需要 JSON 协议（导出请求体仍是 JSON，仅响应是二进制）
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

/**
 * 导出成本预算报告
 * @param {Object} data 导出参数
 * @returns {Promise}
 */
export function exportCostBudgetReport(data) {
  return request({
    url: '/cwgxAi/ma/costcenter/budget/export',
    method: 'post',
    data: transData(data),
    responseType: 'blob'
  })
}

// ==================== 通用 API ====================

/**
 * 获取成本中心选项列表（用于下拉框）
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getCostCenterOptions(params) {
  return request({
    url: '/cwgxAi/ma/costcenter/options',
    method: 'get',
    params
  })
}

/**
 * 获取成本中心统计概览
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getCostCenterStats(params) {
  return request({
    url: '/cwgxAi/ma/costcenter/stats',
    method: 'get',
    params
  })
}
