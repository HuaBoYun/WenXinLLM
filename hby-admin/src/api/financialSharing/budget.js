import request from '@/utils/request'

// 预算审批管理API
export const budgetApi = {
  // 获取待审批预算列表
  getApprovalList: (params) => {
    return request({
      url: '/financial/budget/approve/getList',
      method: 'post',
      data: params
    })
  },

  // 获取部门列表
  getDepartmentList: () => {
    return request({
      url: '/financial/budget/approve/getDepartmentList',
      method: 'get'
    })
  },

  // 获取预算详情
  getBudgetDetail: (budgetId) => {
    return request({
      url: `/financial/budget/approve/getDetail/${budgetId}`,
      method: 'get'
    })
  },

  // 获取审批历史
  getApprovalHistory: (budgetId) => {
    return request({
      url: `/financial/budget/approve/getHistory/${budgetId}`,
      method: 'get'
    })
  },

  // 获取审批流程图
  getProcessFlow: (budgetId) => {
    return request({
      url: `/financial/budget/approve/getProcessFlow/${budgetId}`,
      method: 'get'
    })
  },

  // 审批预算
  approveBudget: (data) => {
    return request({
      url: '/financial/budget/approve/approve',
      method: 'post',
      data
    })
  },

  // 批量审批
  batchApprove: (data) => {
    return request({
      url: '/financial/budget/approve/batchApprove',
      method: 'post',
      data
    })
  },

  // 预算分解管理API
  // 获取分解方案
  getDecomposeScheme: (params) => {
    return request({
      url: '/financial/budget/decompose/getScheme',
      method: 'post',
      data: params
    })
  },

  // 创建分解方案
  createDecompose: (data) => {
    return request({
      url: '/financial/budget/decompose/create',
      method: 'post',
      data
    })
  },

  // 计算分解结果
  calculateDecompose: (data) => {
    return request({
      url: '/financial/budget/decompose/calculate',
      method: 'post',
      data
    })
  },

  // 保存分解结果
  saveDecompose: (data) => {
    return request({
      url: '/financial/budget/decompose/save',
      method: 'post',
      data
    })
  },

  // 获取分解历史
  getDecomposeHistory: (params) => {
    return request({
      url: '/financial/budget/decompose/getHistory',
      method: 'post',
      data: params
    })
  },

  // 获取分解维度配置
  getDecomposeDimensions: () => {
    return request({
      url: '/financial/budget/decompose/getDimensions',
      method: 'get'
    })
  },

  // 获取预算分析API
  // 获取仪表板数据
  getDashboardData: (params) => {
    return request({
      url: '/financial/budget/analysis/getDashboardData',
      method: 'post',
      data: params
    })
  },

  // 获取趋势分析数据
  getTrendAnalysis: (params) => {
    return request({
      url: '/financial/budget/analysis/getTrendAnalysis',
      method: 'post',
      data: params
    })
  },

  // 获取对比分析数据
  getComparisonData: (params) => {
    return request({
      url: '/financial/budget/analysis/getComparisonData',
      method: 'post',
      data: params
    })
  },

  // 获取预警列表
  getWarningList: (params) => {
    return request({
      url: '/financial/budget/analysis/getWarningList',
      method: 'post',
      data: params
    })
  },

  // 获取预算执行统计
  getExecutionStats: (params) => {
    return request({
      url: '/financial/budget/analysis/getExecutionStats',
      method: 'post',
      data: params
    })
  },

  // 获取图表数据
  getChartData: (params) => {
    return request({
      url: '/financial/budget/analysis/getChartData',
      method: 'post',
      data: params
    })
  },

  // 导出分析报告
  exportAnalysisReport: (params) => {
    return request({
      url: '/financial/budget/analysis/exportReport',
      method: 'post',
      data: params,
      responseType: 'blob'
    })
  },

  // 预算版本管理API
  // 获取版本列表
  getVersionList: (params) => {
    return request({
      url: '/financial/budget/version/getList',
      method: 'post',
      data: params
    })
  },

  // 获取版本详情
  getVersionDetail: (versionId) => {
    return request({
      url: `/financial/budget/version/getDetail/${versionId}`,
      method: 'get'
    })
  },

  // 保存版本
  saveVersion: (data) => {
    return request({
      url: '/financial/budget/version/save',
      method: 'post',
      data
    })
  },

  // 删除版本
  deleteVersion: (versionId) => {
    return request({
      url: `/financial/budget/version/delete/${versionId}`,
      method: 'delete'
    })
  },

  // 版本回滚
  rollbackVersion: (data) => {
    return request({
      url: '/financial/budget/version/rollback',
      method: 'post',
      data
    })
  },

  // 获取预算执行统计
  getExecutionStats: (params) => {
    return request({
      url: '/financial/budget/analysis/getExecutionStats',
      method: 'post',
      data: params
    })
  },

  // 预算模板管理API
  // 获取模板列表
  getTemplateList: (params) => {
    return request({
      url: '/financial/budget/template/getList',
      method: 'post',
      data: params
    })
  },

  // 获取模板详情
  getTemplateDetail: (templateId) => {
    return request({
      url: `/financial/budget/template/getDetail/${templateId}`,
      method: 'get'
    })
  },

  // 保存模板
  saveTemplate: (data) => {
    return request({
      url: '/financial/budget/template/save',
      method: 'post',
      data
    })
  },

  // 删除模板
  deleteTemplate: (templateId) => {
    return request({
      url: `/financial/budget/template/delete/${templateId}`,
      method: 'delete'
    })
  },

  // 应用模板
  applyTemplate: (data) => {
    return request({
      url: '/financial/budget/template/apply',
      method: 'post',
      data
    })
  },

  // 分享模板
  shareTemplate: (data) => {
    return request({
      url: '/financial/budget/template/share',
      method: 'post',
      data
    })
  }
}

export default budgetApi