/**
 * NCV65全面预算系统 - 主入口文件
 *
 * @description NCV65全面预算系统API接口统一导出
 * @version 1.0.0
 * @author AI Assistant
 * @date 2025-01-08
 */

// 预算体系管理模块
export * from './budgetSystem'

// 预算编制管理模块
export * from './budgetPreparation'

// 预算分析模块
export * from './budgetAnalysis'

// 预算控制模块
export * from './budgetControl'

// 高级功能模块
export * from './advancedFeatures'

// 系统集成模块
export * from './systemIntegration'

// 数据管理模块
export * from './dataManagement'

// 系统管理模块
export * from './systemManagement'

// Excel客户端模块
export * from './excelClient'

// 预算查阅模块
export * from './budgetView'

// 常量定义
export const NCV65_CONSTANTS = {
  // 组织体系类型
  ORGANIZATION_TYPES: {
    SINGLE: 'single',
    MULTI: 'multi',
    HIERARCHICAL: 'hierarchical',
    MATRIX: 'matrix',
    HYBRID: 'hybrid'
  },

  // 控制模式
  CONTROL_MODES: {
    CENTRALIZED: 'centralized',
    DECENTRALIZED: 'decentralized',
    HYBRID: 'hybrid'
  },

  // 预算状态
  BUDGET_STATUS: {
    DRAFT: 'draft',
    SUBMITTED: 'submitted',
    APPROVED: 'approved',
    REJECTED: 'rejected',
    ACTIVE: 'active',
    CLOSED: 'closed'
  },

  // 审批状态
  APPROVAL_STATUS: {
    PENDING: 'pending',
    APPROVED: 'approved',
    REJECTED: 'rejected',
    CANCELLED: 'cancelled'
  },
  
  // 控制规则类型
  CONTROL_RULE_TYPES: {
    SINGLE_INDICATOR: 'single_indicator',
    MULTI_INDICATOR: 'multi_indicator',
    FLEXIBLE: 'flexible',
    FLEXIBLE_BUDGET: 'flexible_budget',
    ROLLING: 'rolling',
    HIERARCHICAL: 'hierarchical',
    WARNING: 'warning'
  },
  
  // 维度类型
  DIMENSION_TYPES: {
    ORGANIZATION: 'organization',
    PRODUCT: 'product',
    PROJECT: 'project',
    CUSTOMER: 'customer',
    SUPPLIER: 'supplier',
    REGION: 'region',
    TIME: 'time',
    CUSTOM: 'custom'
  },
  
  // 指标类型
  INDICATOR_TYPES: {
    REVENUE: 'revenue',
    COST: 'cost',
    EXPENSE: 'expense',
    INVESTMENT: 'investment',
    CASH_FLOW: 'cash_flow',
    OPERATIONAL: 'operational',
    STRATEGIC: 'strategic',
    CUSTOM: 'custom'
  },
  
  // 预算模型类型
  MODEL_TYPES: {
    REVENUE: 'revenue',
    COST: 'cost',
    EXPENSE: 'expense',
    INVESTMENT: 'investment',
    CASH: 'cash',
    COMPREHENSIVE: 'comprehensive',
    CUSTOM: 'custom'
  }
}

// 工具函数
export const NCV65_UTILS = {
  /**
   * 格式化组织体系类型
   */
  formatOrganizationType(type) {
    const typeMap = {
      single: '单一集团',
      multi: '多集团',
      hierarchical: '分级管理',
      matrix: '矩阵式',
      hybrid: '混合式'
    }
    return typeMap[type] || type
  },

  /**
   * 格式化控制模式
   */
  formatControlMode(mode) {
    const modeMap = {
      centralized: '集中式',
      decentralized: '分散式',
      hybrid: '混合式'
    }
    return modeMap[mode] || mode
  },

  /**
   * 格式化预算状态
   */
  formatBudgetStatus(status) {
    const statusMap = {
      draft: '草稿',
      submitted: '已提交',
      approved: '已审批',
      rejected: '已拒绝',
      active: '生效中',
      closed: '已关闭'
    }
    return statusMap[status] || status
  },

  /**
   * 获取状态颜色
   */
  getStatusColor(status) {
    const colorMap = {
      draft: 'info',
      submitted: 'warning',
      approved: 'success',
      rejected: 'danger',
      active: 'success',
      closed: 'info'
    }
    return colorMap[status] || 'info'
  },

  /**
   * 格式化金额
   */
  formatAmount(amount, currency = '¥', precision = 2) {
    if (amount === null || amount === undefined) return '-'
    return `${currency}${Number(amount).toLocaleString('zh-CN', {
      minimumFractionDigits: precision,
      maximumFractionDigits: precision
    })}`
  },

  /**
   * 格式化百分比
   */
  formatPercent(value, precision = 2) {
    if (value === null || value === undefined) return '-'
    return `${(Number(value) * 100).toFixed(precision)}%`
  }
}
