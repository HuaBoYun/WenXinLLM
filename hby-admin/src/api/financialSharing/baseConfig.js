/**
 * 费控服务基础配置模块API
 * 包含费用项目管理、差旅标准管理、稽核规则管理等功能
 *
 * @author Financial Sharing System
 * @since 2024-12-19
 */

import request from '@/utils/request'

// ========================================
// 费用项目管理API
// ========================================

/**
 * 费用项目管理API
 */
export const expenseItemApi = {
  // 获取费用项目列表
  getList(params) {
    return request({
      url: '/cwgxAi/accounting-rules',
      method: 'get',
      params
    })
  },

  // 保存费用项目
  save(data) {
    return request({
      url: '/cwgxAi/accounting-rules',
      method: 'post',
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      },
      data
    })
  },

  // 删除费用项目
  delete(itemId) {
    return request({
      url: `/cwgxAi/accounting-rules/${itemId}`,
      method: 'delete',
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      }
    })
  },

  // 获取费用项目详情
  getDetail(itemId) {
    return request({
      url: `/cwgxAi/accounting-rules/${itemId}`,
      method: 'get'
    })
  },

  // 获取费用项目树形结构
  getTree(params) {
    return request({
      url: '/cwgxAi/accounting-rules/tree',
      method: 'get',
      params
    })
  },

  // 批量删除费用项目
  batchDelete(itemIds) {
    return request({
      url: '/cwgxAi/accounting-rules/batch',
      method: 'delete',
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      },
      data: itemIds
    })
  },

  // 更新费用项目状态
  updateStatus(itemId, isEnabled) {
    return request({
      url: `/cwgxAi/accounting-rules/${itemId}/status`,
      method: 'put',
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      },
      params: { isEnabled }
    })
  },

  // 测试会计规则
  test(ruleId, testData) {
    return request({
      url: `/cwgxAi/accounting-rules/${ruleId}/test`,
      method: 'post',
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      },
      data: testData
    })
  }
}

// ========================================
// 会计规则管理API
// ========================================

/**
 * 会计规则管理API
 */
export const accountingRuleApi = {
  // 获取会计规则列表
  getList(params) {
    return request({
      url: '/cwgxAi/base-config/accounting-rule',
      method: 'get',
      params
    })
  },

  // 获取会计规则详情
  getDetail(ruleId) {
    return request({
      url: `/cwgxAi/base-config/accounting-rule/${ruleId}`,
      method: 'get'
    })
  },

  // 保存会计规则
  save(data) {
    return request({
      url: '/cwgxAi/base-config/accounting-rule',
      method: 'post',
      data,
      headers: {
        'Content-Type': 'application/json'
      }
    })
  },

  // 删除会计规则
  delete(ruleId) {
    return request({
      url: `/cwgxAi/base-config/accounting-rule/${ruleId}`,
      method: 'delete'
    })
  },

  // 批量删除会计规则
  batchDelete(ids) {
    return request({
      url: '/cwgxAi/base-config/accounting-rule/batch',
      method: 'delete',
      data: ids,
      headers: {
        'Content-Type': 'application/json'
      }
    })
  },

  // 测试会计规则
  test(ruleId, testData) {
    return request({
      url: `/cwgxAi/base-config/accounting-rule/${ruleId}/test`,
      method: 'post',
      data: testData,
      headers: {
        'Content-Type': 'application/json'
      }
    })
  },

  // 更新会计规则状态
  updateStatus(ruleId, isEnabled) {
    return request({
      url: `/cwgxAi/base-config/accounting-rule/${ruleId}/status`,
      method: 'put',
      data: { isEnabled },
      headers: {
        'Content-Type': 'application/json'
      }
    })
  },

  // 导出会计规则
  export(params) {
    return request({
      url: '/cwgxAi/base-config/accounting-rule/export',
      method: 'get',
      params
    })
  }
}

// ========================================
// 差旅标准管理API
// ========================================

/**
 * 差旅标准管理API
 */
export const travelStandardApi = {
  // 获取差旅标准列表
  getList(params) {
    return request({
      url: '/cwgxAi/travel-standards',
      method: 'get',
      params
    })
  },
  
  // 保存差旅标准
  save(data) {
    return request({
      url: '/cwgxAi/travel-standards',
      method: 'post',
      data,
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      }
    })
  },
  
  // 删除差旅标准
  delete(standardId) {
    return request({
      url: `/cwgxAi/travel-standards/${standardId}`,
      method: 'delete'
    })
  },
  
  // 获取差旅标准详情
  getDetail(standardId) {
    return request({
      url: `/cwgxAi/travel-standards/${standardId}`,
      method: 'get'
    })
  },
  
  // 获取差旅标准明细
  getDetails(standardId) {
    return request({
      url: `/cwgxAi/travel-standards/${standardId}/details`,
      method: 'get'
    })
  },
  
  // 保存差旅标准明细（批量）
  saveDetails(standardId, details) {
    return request({
      url: `/cwgxAi/travel-standards/${standardId}/details`,
      method: 'post',
      data: details,
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      }
    })
  },

  // 保存差旅标准明细（单个）
  saveDetail(data) {
    return request({
      url: '/cwgxAi/travel-standards/details',
      method: 'post',
      data,
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      }
    })
  },

  // 删除差旅标准明细
  deleteDetail(detailId) {
    return request({
      url: `/cwgxAi/travel-standards/details/${detailId}`,
      method: 'delete'
    })
  },
  
  // 批量删除差旅标准
  batchDelete(standardIds) {
    return request({
      url: '/cwgxAi/travel-standards/batch',
      method: 'delete',
      data: standardIds,
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      }
    })
  },
  
  // 更新差旅标准状态
  updateStatus(standardId, isEnabled) {
    return request({
      url: `/cwgxAi/travel-standards/${standardId}/status`,
      method: 'put',
      params: { isEnabled }
    })
  },
  
  // 复制差旅标准
  copy(standardId, copyInfo) {
    return request({
      url: `/cwgxAi/travel-standards/${standardId}/copy`,
      method: 'post',
      data: copyInfo,
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      }
    })
  }
}

// ========================================
// 稽核规则管理API
// ========================================

/**
 * 稽核规则管理API
 */
export const auditRuleApi = {
  // 获取稽核规则列表
  getList(params) {
    return request({
      url: '/cwgxAi/audit/rules',
      method: 'get',
      params
    })
  },

  // 保存稽核规则
  save(data) {
    return request({
      url: '/cwgxAi/audit/rules',
      method: 'post',
      data,
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      }
    })
  },

  // 删除稽核规则
  delete(ruleId) {
    return request({
      url: `/cwgxAi/audit/rules/${ruleId}`,
      method: 'delete'
    })
  },

  // 获取稽核规则详情
  getDetail(ruleId) {
    return request({
      url: `/cwgxAi/audit/rules/${ruleId}`,
      method: 'get'
    })
  },

  // 测试稽核规则
  test(ruleId, testData) {
    return request({
      url: `/cwgxAi/audit/rules/${ruleId}/test`,
      method: 'post',
      data: testData,
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      }
    })
  },

  // 获取稽核规则条件
  getConditions(ruleId) {
    return request({
      url: `/cwgxAi/audit/rules/${ruleId}/conditions`,
      method: 'get'
    })
  },

  // 保存稽核规则条件（批量）
  saveConditions(ruleId, conditions) {
    return request({
      url: `/cwgxAi/audit/rules/${ruleId}/conditions`,
      method: 'post',
      data: conditions,
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      }
    })
  },

  // 保存单个条件
  saveCondition(data) {
    return request({
      url: '/cwgxAi/audit/rules/conditions',
      method: 'post',
      data,
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      }
    })
  },

  // 删除单个条件
  deleteCondition(conditionId) {
    return request({
      url: `/cwgxAi/audit/rules/conditions/${conditionId}`,
      method: 'delete'
    })
  },

  // 批量删除稽核规则
  batchDelete(ruleIds) {
    return request({
      url: '/cwgxAi/audit/rules/batch',
      method: 'delete',
      data: ruleIds,
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      }
    })
  },

  // 更新稽核规则状态
  updateStatus(ruleId, isEnabled) {
    return request({
      url: `/cwgxAi/audit/rules/${ruleId}/status`,
      method: 'put',
      params: { isEnabled }
    })
  },

  // 复制稽核规则
  copy(ruleId, copyInfo) {
    return request({
      url: `/cwgxAi/audit/rules/${ruleId}/copy`,
      method: 'post',
      data: copyInfo,
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      }
    })
  },

  // 获取稽核规则执行日志
  getLogs(ruleId, params) {
    return request({
      url: `/cwgxAi/audit/rules/${ruleId}/logs`,
      method: 'get',
      params
    })
  }
}

// ========================================
// 预算管控规则管理API
// ========================================

/**
 * 预算管控规则管理API
 */
export const budgetControlRuleApi = {
  // 获取预算管控规则列表
  getList(params) {
    return request({
      url: '/cwgxAi/budget-control-rules',
      method: 'get',
      params
    })
  },

  // 保存预算管控规则
  save(data) {
    return request({
      url: '/cwgxAi/budget-control-rules',
      method: 'post',
      data,
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      }
    })
  },

  // 删除预算管控规则
  delete(ruleId) {
    return request({
      url: `/cwgxAi/budget-control-rules/${ruleId}`,
      method: 'delete'
    })
  },

  // 获取预算管控规则详情
  getDetail(ruleId) {
    return request({
      url: `/cwgxAi/budget-control-rules/${ruleId}`,
      method: 'get'
    })
  },

  // 获取预算管控规则适用范围
  getScope(ruleId) {
    return request({
      url: `/cwgxAi/budget-control-rules/${ruleId}/scope`,
      method: 'get'
    })
  },

  // 保存预算管控规则适用范围
  saveScope(ruleId, scopes) {
    return request({
      url: `/cwgxAi/budget-control-rules/${ruleId}/scope`,
      method: 'post',
      data: scopes,
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      }
    })
  },

  // 预算控制检查
  check(ruleId, checkData) {
    return request({
      url: `/cwgxAi/budget-control-rules/${ruleId}/check`,
      method: 'post',
      data: checkData,
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      }
    })
  },

  // 批量删除预算管控规则
  batchDelete(ruleIds) {
    return request({
      url: '/cwgxAi/budget-control-rules/batch',
      method: 'delete',
      data: ruleIds
    })
  },

  // 更新预算管控规则状态
  updateStatus(ruleId, isEnabled) {
    return request({
      url: `/cwgxAi/budget-control-rules/${ruleId}/status`,
      method: 'put',
      params: { isEnabled }
    })
  },

  // 复制预算管控规则
  copy(ruleId, copyInfo) {
    return request({
      url: `/cwgxAi/budget-control-rules/${ruleId}/copy`,
      method: 'post',
      data: copyInfo,
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      }
    })
  },

  // 获取预算执行统计
  getStatistics(ruleId, params) {
    return request({
      url: `/cwgxAi/budget-control-rules/${ruleId}/statistics`,
      method: 'get',
      params
    })
  }
}

// ========================================
// 项目配置管理API
// ========================================

/**
 * 项目配置管理API
 */
export const projectConfigApi = {
  // 获取项目配置列表
  getList(params) {
    return request({
      url: '/cwgxAi/financial/project-configs',
      method: 'get',
      params
    })
  },

  // 保存项目配置
  save(data) {
    return request({
      url: '/cwgxAi/financial/project-configs',
      method: 'post',
      data
    })
  },

  // 删除项目配置
  delete(projectId) {
    return request({
      url: `/cwgxAi/financial/project-configs/${projectId}`,
      method: 'delete'
    })
  },

  // 获取项目配置详情
  getDetail(projectId) {
    return request({
      url: `/cwgxAi/financial/project-configs/${projectId}`,
      method: 'get'
    })
  },

  // 获取项目成员列表
  getMembers(projectId) {
    return request({
      url: `/cwgxAi/financial/project-configs/${projectId}/members`,
      method: 'get'
    })
  },

  // 保存项目成员
  saveMembers(projectId, members) {
    return request({
      url: `/cwgxAi/financial/project-configs/${projectId}/members`,
      method: 'post',
      data: members
    })
  },

  // 获取项目预算明细
  getBudget(projectId) {
    return request({
      url: `/cwgxAi/financial/project-configs/${projectId}/budget`,
      method: 'get'
    })
  },

  // 保存项目预算明细
  saveBudget(projectId, budgets) {
    return request({
      url: `/cwgxAi/financial/project-configs/${projectId}/budget`,
      method: 'post',
      data: budgets
    })
  },

  // 批量删除项目配置
  batchDelete(projectIds) {
    return request({
      url: '/cwgxAi/financial/project-configs/batch',
      method: 'delete',
      data: projectIds
    })
  },

  // 更新项目状态
  updateStatus(projectId, projectStatus) {
    return request({
      url: `/cwgxAi/financial/project-configs/${projectId}/status`,
      method: 'put',
      params: { projectStatus }
    })
  },

  // 项目结算
  settlement(projectId, settlementData) {
    return request({
      url: `/cwgxAi/financial/project-configs/${projectId}/settlement`,
      method: 'post',
      data: settlementData
    })
  },

  // 获取项目统计信息
  getStatistics(projectId) {
    return request({
      url: `/cwgxAi/financial/project-configs/${projectId}/statistics`,
      method: 'get'
    })
  }
}

// ========================================
// 账单配置管理API
// ========================================

/**
 * 账单配置管理API
 */
export const billConfigApi = {
  // 获取账单配置列表
  getList(params) {
    return request({
      url: '/cwgxAi/bill/configs',
      method: 'get',
      params
    })
  },

  // 保存账单配置
  save(data) {
    return request({
      url: '/cwgxAi/bill/configs',
      method: 'post',
      data,
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      }
    })
  },

  // 删除账单配置
  delete(configId) {
    return request({
      url: `/cwgxAi/bill/configs/${configId}`,
      method: 'delete',
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      }
    })
  },

  // 获取账单配置详情
  getDetail(configId) {
    return request({
      url: `/cwgxAi/bill/configs/${configId}`,
      method: 'get'
    })
  },

  // 获取账单配置字段映射
  getFieldMapping(configId) {
    return request({
      url: `/cwgxAi/bill/configs/${configId}/field-mapping`,
      method: 'get',
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      }
    })
  },

  // 保存账单配置字段映射
  saveFieldMapping(configId, mappings) {
    return request({
      url: `/cwgxAi/bill/configs/${configId}/field-mapping`,
      method: 'post',
      data: mappings,
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      }
    })
  },

  // 获取账单配置稽核规则
  getAuditRules(configId) {
    return request({
      url: `/cwgxAi/bill/configs/${configId}/audit-rules`,
      method: 'get',
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      }
    })
  },

  // 保存账单配置稽核规则
  saveAuditRules(configId, rules) {
    return request({
      url: `/cwgxAi/bill/configs/${configId}/audit-rules`,
      method: 'post',
      data: rules,
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      }
    })
  },

  // 测试账单配置
  test(configId, testData) {
    return request({
      url: `/cwgxAi/bill/configs/${configId}/test`,
      method: 'post',
      data: testData,
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      }
    })
  },

  // 批量删除账单配置
  batchDelete(configIds) {
    return request({
      url: '/cwgxAi/bill/configs/batch',
      method: 'delete',
      data: configIds,
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      }
    })
  },

  // 更新账单配置状态
  updateStatus(configId, isEnabled) {
    return request({
      url: `/cwgxAi/bill/configs/${configId}/status`,
      method: 'put',
      params: { isEnabled },
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      }
    })
  },

  // 复制账单配置
  copy(configId, copyInfo) {
    return request({
      url: `/cwgxAi/bill-configs/${configId}/copy`,
      method: 'post',
      data: copyInfo,
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      }
    })
  },

  // 获取账单配置统计
  getStatistics(configId, params) {
    return request({
      url: `/cwgxAi/bill-configs/${configId}/statistics`,
      method: 'get',
      params
    })
  }
}

// ========================================
// 商旅档案管理API
// ========================================

/**
 * 商旅档案管理API
 */
export const travelArchiveApi = {
  // 获取商旅档案列表
  getList(params) {
    return request({
      url: '/cwgxAi/travel-archives',
      method: 'get',
      params
    })
  },

  // 保存商旅档案
  save(data) {
    return request({
      url: '/cwgxAi/travel-archives',
      method: 'post',
      data,
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      }
    })
  },

  // 删除商旅档案
  delete(archiveId) {
    return request({
      url: `/cwgxAi/travel-archives/${archiveId}`,
      method: 'delete'
    })
  },

  // 获取商旅档案详情
  getDetail(archiveId) {
    return request({
      url: `/cwgxAi/travel-archives/${archiveId}`,
      method: 'get'
    })
  },

  // 获取商旅档案价格信息
  getPrices(archiveId) {
    return request({
      url: `/cwgxAi/travel-archives/${archiveId}/prices`,
      method: 'get'
    })
  },

  // 保存商旅档案价格信息
  savePrices(archiveId, prices) {
    return request({
      url: `/cwgxAi/travel-archives/${archiveId}/prices`,
      method: 'post',
      data: prices,
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      }
    })
  },

  // 获取商旅档案合作协议
  getAgreements(archiveId) {
    return request({
      url: `/cwgxAi/travel-archives/${archiveId}/agreements`,
      method: 'get'
    })
  },

  // 保存商旅档案合作协议
  saveAgreements(archiveId, agreements) {
    return request({
      url: `/cwgxAi/travel-archives/${archiveId}/agreements`,
      method: 'post',
      data: agreements,
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      }
    })
  },

  // 批量删除商旅档案
  batchDelete(archiveIds) {
    return request({
      url: '/cwgxAi/travel-archives/batch',
      method: 'delete',
      data: archiveIds
    })
  },

  // 更新商旅档案状态
  updateStatus(archiveId, isEnabled) {
    return request({
      url: `/cwgxAi/travel-archives/${archiveId}/status`,
      method: 'put',
      params: { isEnabled }
    })
  },

  // 商旅档案评价
  evaluation(archiveId, evaluation) {
    return request({
      url: `/cwgxAi/travel-archives/${archiveId}/evaluation`,
      method: 'post',
      data: evaluation,
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      }
    })
  },

  // 获取商旅档案统计
  getStatistics(archiveId, params) {
    return request({
      url: `/cwgxAi/travel-archives/${archiveId}/statistics`,
      method: 'get',
      params
    })
  }
}

// ========================================
// 通用标准管理API
// ========================================

/**
 * 通用标准管理API
 */
export const generalStandardApi = {
  // 获取通用标准列表
  getList(params) {
    return request({
      url: '/cwgxAi/general-standards',
      method: 'get',
      params
    })
  },

  // 保存通用标准
  save(data) {
    return request({
      url: '/cwgxAi/general-standards',
      method: 'post',
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      },
      data
    })
  },

  // 删除通用标准
  delete(standardId) {
    return request({
      url: `/cwgxAi/general-standards/${standardId}`,
      method: 'delete'
    })
  },

  // 获取通用标准详情
  getDetail(standardId) {
    return request({
      url: `/cwgxAi/general-standards/${standardId}`,
      method: 'get'
    })
  },

  // 获取通用标准分级配置
  getLevels(standardId) {
    return request({
      url: `/cwgxAi/general-standards/${standardId}/levels`,
      method: 'get'
    })
  },

  // 保存通用标准分级配置
  saveLevels(standardId, levels) {
    return request({
      url: `/cwgxAi/general-standards/${standardId}/levels`,
      method: 'post',
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      },
      data: levels
    })
  },

  // 获取通用标准适用条件
  getConditions(standardId) {
    return request({
      url: `/cwgxAi/general-standards/${standardId}/conditions`,
      method: 'get'
    })
  },

  // 保存通用标准适用条件
  saveConditions(standardId, conditions) {
    return request({
      url: `/cwgxAi/general-standards/${standardId}/conditions`,
      method: 'post',
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      },
      data: conditions
    })
  },

  // 批量删除通用标准
  batchDelete(standardIds) {
    return request({
      url: '/cwgxAi/general-standards/batch',
      method: 'delete',
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      },
      data: standardIds
    })
  },

  // 更新通用标准状态
  updateStatus(standardId, isEnabled) {
    return request({
      url: `/cwgxAi/general-standards/${standardId}/status`,
      method: 'put',
      params: { isEnabled }
    })
  },

  // 复制通用标准
  copy(standardId, copyInfo) {
    return request({
      url: `/cwgxAi/general-standards/${standardId}/copy`,
      method: 'post',
      data: copyInfo
    })
  },

  // 标准计算
  calculate(standardId, calculateData) {
    return request({
      url: `/cwgxAi/general-standards/${standardId}/calculate`,
      method: 'post',
      data: calculateData
    })
  },

  // 获取通用标准使用统计
  getUsageStatistics(standardId, params) {
    return request({
      url: `/cwgxAi/general-standards/${standardId}/usage-statistics`,
      method: 'get',
      params
    })
  }
}

// ========================================
// 报账参数配置API
// ========================================

/**
 * 报账参数配置API
 */
export const expenseParameterApi = {
  // 获取报账参数配置列表
  getList(params) {
    return request({
      url: '/cwgxAi/expense/parameters',
      method: 'get',
      params
    })
  },

  // 保存报账参数配置
  save(data) {
    return request({
      url: '/cwgxAi/expense/parameters',
      method: 'post',
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      },
      data
    })
  },

  // 删除报账参数配置
  delete(parameterId) {
    return request({
      url: `/cwgxAi/expense/parameters/${parameterId}`,
      method: 'delete'
    })
  },

  // 获取报账参数配置详情
  getDetail(parameterId) {
    return request({
      url: `/cwgxAi/expense/parameters/${parameterId}`,
      method: 'get'
    })
  },

  // 获取报账参数配置选项
  getOptions(parameterId) {
    return request({
      url: `/cwgxAi/expense/parameters/${parameterId}/options`,
      method: 'get'
    })
  },

  // 保存报账参数配置选项
  saveOptions(parameterId, options) {
    return request({
      url: `/cwgxAi/expense/parameters/${parameterId}/options`,
      method: 'post',
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      },
      data: options
    })
  },

  // 获取报账参数配置权限
  getPermissions(parameterId) {
    return request({
      url: `/cwgxAi/expense/parameters/${parameterId}/permissions`,
      method: 'get'
    })
  },

  // 保存报账参数配置权限
  savePermissions(parameterId, permissions) {
    return request({
      url: `/cwgxAi/expense/parameters/${parameterId}/permissions`,
      method: 'post',
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      },
      data: permissions
    })
  },

  // 批量删除报账参数配置
  batchDelete(parameterIds) {
    return request({
      url: '/cwgxAi/expense/parameters/batch',
      method: 'delete',
      data: parameterIds
    })
  },

  // 更新报账参数配置状态
  updateStatus(parameterId, isEnabled) {
    return request({
      url: `/cwgxAi/expense/parameters/${parameterId}/status`,
      method: 'put',
      params: { isEnabled }
    })
  },

  // 重置报账参数配置为默认值
  reset(parameterId) {
    return request({
      url: `/cwgxAi/expense/parameters/${parameterId}/reset`,
      method: 'put'
    })
  },

  // 验证报账参数配置值
  validate(parameterId, validateData) {
    return request({
      url: `/cwgxAi/expense/parameters/${parameterId}/validate`,
      method: 'post',
      data: validateData
    })
  },

  // 导出报账参数配置
  export(params) {
    return request({
      url: '/cwgxAi/expense/parameters/export',
      method: 'get',
      params
    })
  },

  // 导入报账参数配置
  import(importData) {
    return request({
      url: '/cwgxAi/expense-parameters/import',
      method: 'post',
      data: importData
    })
  }
}

// ========================================
// 代理委托管理API
// ========================================

/**
 * 代理委托管理API
 */
export const proxyDelegationApi = {
  // 获取代理委托列表
  getList(params) {
    return request({
      url: '/cwgxAi/proxy-delegations',
      method: 'get',
      params
    })
  },

  // 保存代理委托
  save(data) {
    return request({
      url: '/cwgxAi/proxy-delegations',
      method: 'post',
      data
    })
  },

  // 删除代理委托
  delete(delegationId) {
    return request({
      url: `/cwgxAi/proxy-delegations/${delegationId}`,
      method: 'delete'
    })
  },

  // 获取代理委托详情
  getDetail(delegationId) {
    return request({
      url: `/cwgxAi/proxy-delegations/${delegationId}`,
      method: 'get'
    })
  },

  // 获取代理委托权限范围
  getPermissions(delegationId) {
    return request({
      url: `/cwgxAi/proxy-delegations/${delegationId}/permissions`,
      method: 'get'
    })
  },

  // 保存代理委托权限范围
  savePermissions(delegationId, permissions) {
    return request({
      url: `/cwgxAi/proxy-delegations/${delegationId}/permissions`,
      method: 'post',
      data: permissions
    })
  },

  // 获取代理委托历史记录
  getHistory(delegationId) {
    return request({
      url: `/cwgxAi/proxy-delegations/${delegationId}/history`,
      method: 'get'
    })
  },

  // 批量删除代理委托
  batchDelete(delegationIds) {
    return request({
      url: '/cwgxAi/proxy-delegations/batch',
      method: 'delete',
      data: delegationIds
    })
  },

  // 激活代理委托
  activate(delegationId) {
    return request({
      url: `/cwgxAi/proxy-delegations/${delegationId}/activate`,
      method: 'put'
    })
  },

  // 停用代理委托
  deactivate(delegationId) {
    return request({
      url: `/cwgxAi/proxy-delegations/${delegationId}/deactivate`,
      method: 'put'
    })
  },

  // 暂停代理委托
  suspend(delegationId, suspendInfo) {
    return request({
      url: `/cwgxAi/proxy-delegations/${delegationId}/suspend`,
      method: 'put',
      data: suspendInfo
    })
  },

  // 恢复代理委托
  resume(delegationId) {
    return request({
      url: `/cwgxAi/proxy-delegations/${delegationId}/resume`,
      method: 'put'
    })
  },

  // 获取我的代理委托
  getMyDelegations(params) {
    return request({
      url: '/cwgxAi/proxy-delegations/my-delegations',
      method: 'get',
      params
    })
  },

  // 获取代理委托统计
  getStatistics(delegationId, params) {
    return request({
      url: `/cwgxAi/proxy-delegations/${delegationId}/statistics`,
      method: 'get',
      params
    })
  }
}

// ========================================
// 移动设置配置API
// ========================================

/**
 * 移动设置配置API
 */
export const mobileSettingsApi = {
  // 获取移动设置配置列表
  getList(params) {
    return request({
      url: '/cwgxAi/mobile-settings',
      method: 'get',
      params
    })
  },

  // 保存移动设置配置
  save(data) {
    return request({
      url: '/cwgxAi/mobile-settings',
      method: 'post',
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      },
      data
    })
  },

  // 删除移动设置配置
  delete(settingId) {
    return request({
      url: `/cwgxAi/mobile-settings/${settingId}`,
      method: 'delete'
    })
  },

  // 获取移动设置配置详情
  getDetail(settingId) {
    return request({
      url: `/cwgxAi/mobile-settings/${settingId}`,
      method: 'get'
    })
  },

  // 获取移动设置配置分组
  getGroups() {
    return request({
      url: '/cwgxAi/mobile-settings/groups',
      method: 'get'
    })
  },

  // 获取移动设置配置按分组
  getByGroup(groupId) {
    return request({
      url: `/cwgxAi/mobile-settings/by-group/${groupId}`,
      method: 'get'
    })
  },

  // 获取移动设置配置模板
  getTemplates(params) {
    return request({
      url: '/cwgxAi/mobile-settings/templates',
      method: 'get',
      params
    })
  },

  // 保存移动设置配置模板
  saveTemplate(data) {
    return request({
      url: '/cwgxAi/mobile-settings/templates',
      method: 'post',
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      },
      data
    })
  },

  // 删除移动设置配置模板
  deleteTemplate(templateId) {
    return request({
      url: `/cwgxAi/mobile-settings/templates/${templateId}`,
      method: 'delete'
    })
  },

  // 应用移动设置配置模板
  applyTemplate(applyData) {
    return request({
      url: '/cwgxAi/mobile-settings/apply-template',
      method: 'post',
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      },
      data: applyData
    })
  },

  // 批量删除移动设置配置
  batchDelete(settingIds) {
    return request({
      url: '/cwgxAi/mobile-settings/batch',
      method: 'delete',
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      },
      data: settingIds
    })
  },

  // 更新移动设置配置状态
  updateStatus(settingId, isEnabled) {
    return request({
      url: `/cwgxAi/mobile-settings/${settingId}/status`,
      method: 'put',
      params: { isEnabled }
    })
  },

  // 重置移动设置配置为默认值
  reset(settingId) {
    return request({
      url: `/cwgxAi/mobile-settings/${settingId}/reset`,
      method: 'put'
    })
  },

  // 批量重置移动设置配置
  batchReset(resetData) {
    return request({
      url: '/cwgxAi/mobile-settings/batch-reset',
      method: 'put',
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      },
      data: resetData
    })
  },

  // 同步移动设置配置到移动端
  sync(syncData) {
    return request({
      url: '/cwgxAi/mobile-settings/sync',
      method: 'post',
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      },
      data: syncData
    })
  },

  // 获取移动设置配置使用统计
  getUsageStatistics(settingId, params) {
    return request({
      url: `/cwgxAi/mobile-settings/${settingId}/usage-statistics`,
      method: 'get',
      params
    })
  },

  // 测试移动设置配置
  test(settingId, testData) {
    return request({
      url: `/cwgxAi/mobile-settings/${settingId}/test`,
      method: 'post',
      data: testData,
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      }
    })
  }
}
