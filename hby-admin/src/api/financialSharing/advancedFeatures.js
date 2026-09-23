import request from '@/utils/request'

// 账单管理API
export const billManagementApi = {
  // 获取账单列表
  getList(params) {
    return request({
      url: '/cwgxAi/bill/management',
      method: 'get',
      params
    })
  },

  // 账单采集
  collect(formData) {
    return request({
      url: '/cwgxAi/bill/management/collect',
      method: 'post',
      data: formData,
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  },

  // OCR识别
  ocrRecognition(billId, params) {
    return request({
      url: `/cwgxAi/bill/management/${billId}/ocr`,
      method: 'post',
      data: params,
      headers: {
        'Content-Type': 'application/json'
      }
    })
  },

  // 智能稽核
  intelligentAudit(billId, params) {
    return request({
      url: `/cwgxAi/bill/management/${billId}/audit`,
      method: 'post',
      data: params,
      headers: {
        'Content-Type': 'application/json'
      }
    })
  },

  // 账单应用
  applyBill(billId, data) {
    return request({
      url: `/cwgxAi/bill/management/${billId}/apply`,
      method: 'post',
      data,
      headers: {
        'Content-Type': 'application/json'
      }
    })
  },

  // 获取账单详情
  getDetail(billId) {
    return request({
      url: `/cwgxAi/bill/management/${billId}`,
      method: 'get'
    })
  },

  // 删除账单
  delete(billId) {
    return request({
      url: `/cwgxAi/bill/management/${billId}`,
      method: 'delete'
    })
  },

  // 批量处理账单
  batchProcess(data) {
    return request({
      url: '/cwgxAi/bill/management/batch',
      method: 'post',
      data,
      headers: {
        'Content-Type': 'application/json'
      }
    })
  },

  // 获取账单图片
  getBillImage(billId) {
    return request({
      url: `/cwgxAi/bill/management/${billId}/image`,
      method: 'get'
    })
  },

  // 账单统计
  getStatistics(params) {
    return request({
      url: '/cwgxAi/bill/management/statistics',
      method: 'get',
      params
    })
  },

  // 导出账单
  export(params) {
    return request({
      url: '/cwgxAi/bill/management/export',
      method: 'get',
      params
    })
  }
}

// 费控分析API
export const expenseAnalysisApi = {
  // 个人费用分析
  getPersonalAnalysis(params) {
    return request({
      url: '/cwgxAi/expense-analysis/personal',
      method: 'get',
      params
    })
  },

  // 组织费用分析
  getOrganizationAnalysis(params) {
    return request({
      url: '/cwgxAi/expense-analysis/organization',
      method: 'get',
      params
    })
  },

  // 差旅费用分析
  getTravelAnalysis(params) {
    return request({
      url: '/cwgxAi/expense-analysis/travel',
      method: 'get',
      params
    })
  },

  // 项目费用分析
  getProjectAnalysis(params) {
    return request({
      url: '/cwgxAi/expense-analysis/project',
      method: 'get',
      params
    })
  },

  // 费用趋势分析
  getTrendAnalysis(params) {
    return request({
      url: '/cwgxAi/expense-analysis/trend',
      method: 'get',
      params
    })
  },

  // 费用对比分析
  getComparisonAnalysis(params) {
    return request({
      url: '/cwgxAi/expense-analysis/comparison',
      method: 'get',
      params
    })
  },

  // 导出分析报告
  exportReport(params) {
    return request({
      url: '/cwgxAi/expense-analysis/export',
      method: 'get',
      params
    })
  }
}

// 费用预算API
export const budgetApi = {
  // 获取预算列表
  getList(params) {
    return request({
      url: '/cwgxAi/budget',
      method: 'get',
      params
    })
  },

  // 保存预算
  save(data) {
    return request({
      url: '/cwgxAi/budget',
      method: 'post',
      data
    })
  },

  // 删除预算
  delete(budgetId) {
    return request({
      url: `/cwgxAi/budget/${budgetId}`,
      method: 'delete'
    })
  },

  // 获取预算详情
  getDetail(budgetId) {
    return request({
      url: `/cwgxAi/budget/${budgetId}`,
      method: 'get'
    })
  },

  // 提交预算
  submit(budgetId, data) {
    return request({
      url: `/cwgxAi/budget/${budgetId}/submit`,
      method: 'post',
      data
    })
  },

  // 审批预算
  approve(budgetId, data) {
    return request({
      url: `/cwgxAi/budget/${budgetId}/approve`,
      method: 'post',
      data
    })
  },

  // 预算分解
  decompose(budgetId, data) {
    return request({
      url: `/cwgxAi/budget/${budgetId}/decompose`,
      method: 'post',
      data
    })
  },

  // 预算调整
  adjust(budgetId, data) {
    return request({
      url: `/cwgxAi/budget/${budgetId}/adjust`,
      method: 'post',
      data
    })
  },

  // 预算执行分析
  getExecutionAnalysis(budgetId) {
    return request({
      url: `/cwgxAi/budget/${budgetId}/execution`,
      method: 'get'
    })
  },

  // 预算控制检查
  checkControl(data) {
    return request({
      url: '/cwgxAi/budget/control-check',
      method: 'post',
      data
    })
  },

  // 预算占用
  occupy(data) {
    return request({
      url: '/cwgxAi/budget/occupy',
      method: 'post',
      data
    })
  },

  // 预算释放
  release(data) {
    return request({
      url: '/cwgxAi/budget/release',
      method: 'post',
      data
    })
  },

  // 预算统计
  getStatistics(params) {
    return request({
      url: '/cwgxAi/budget/statistics',
      method: 'get',
      params
    })
  },

  // 导出预算
  export(params) {
    return request({
      url: '/cwgxAi/budget/export',
      method: 'get',
      params
    })
  }
}

// 支出合同API
export const contractApi = {
  // 获取合同列表
  getList(params) {
    return request({
      url: '/cwgxAi/contracts',
      method: 'get',
      params
    })
  },

  // 保存合同
  save(data) {
    return request({
      url: '/cwgxAi/contracts',
      method: 'post',
      data,
      headers: {
        'Content-Type': 'application/json'
      }
    })
  },

  // 删除合同
  delete(contractId) {
    return request({
      url: `/cwgxAi/contracts/${contractId}`,
      method: 'delete'
    })
  },

  // 获取合同详情
  getDetail(contractId) {
    return request({
      url: `/cwgxAi/contracts/${contractId}`,
      method: 'get'
    })
  },

  // 提交合同
  submit(contractId, data) {
    return request({
      url: `/cwgxAi/contracts/${contractId}/submit`,
      method: 'post',
      data,
      headers: {
        'Content-Type': 'application/json'
      }
    })
  },

  // 审批合同
  approve(contractId, data) {
    return request({
      url: `/cwgxAi/contracts/${contractId}/approve`,
      method: 'post',
      data,
      headers: {
        'Content-Type': 'application/json'
      }
    })
  },

  // 合同履约
  fulfill(contractId, data) {
    return request({
      url: `/cwgxAi/contracts/${contractId}/fulfill`,
      method: 'post',
      data,
      headers: {
        'Content-Type': 'application/json'
      }
    })
  },

  // 付款申请
  paymentRequest(contractId, data) {
    return request({
      url: `/cwgxAi/contracts/${contractId}/payment-request`,
      method: 'post',
      data,
      headers: {
        'Content-Type': 'application/json'
      }
    })
  },

  // 合同变更
  modify(contractId, data) {
    return request({
      url: `/cwgxAi/contracts/${contractId}/modify`,
      method: 'post',
      data,
      headers: {
        'Content-Type': 'application/json'
      }
    })
  },

  // 合同终止
  terminate(contractId, data) {
    return request({
      url: `/cwgxAi/contracts/${contractId}/terminate`,
      method: 'post',
      data,
      headers: {
        'Content-Type': 'application/json'
      }
    })
  },

  // 获取付款计划
  getPaymentPlan(contractId) {
    return request({
      url: `/cwgxAi/contracts/${contractId}/payment-plan`,
      method: 'get'
    })
  },

  // 获取付款计划（别名，与页面调用一致）
  getPaymentPlans(contractId) {
    return request({
      url: `/cwgxAi/contracts/${contractId}/payment-plans`,
      method: 'get'
    })
  },

  // 合同执行分析
  getExecutionAnalysis(contractId) {
    return request({
      url: `/cwgxAi/contracts/${contractId}/execution-analysis`,
      method: 'get'
    })
  },

  // 批量删除合同
  batchDelete(contractIds) {
    return request({
      url: '/cwgxAi/contracts/batch',
      method: 'delete',
      data: contractIds,
      headers: {
        'Content-Type': 'application/json'
      }
    })
  },

  // 导出合同
  export(params) {
    return request({
      url: '/cwgxAi/contracts/export',
      method: 'get',
      params
    })
  },

  // 合同统计
  getStatistics(params) {
    return request({
      url: '/cwgxAi/contracts/statistics',
      method: 'get',
      params
    })
  }
}

// 私车公用API
export const privateCarApi = {
  // 获取私车档案列表
  getList(params) {
    return request({
      url: '/cwgxAi/private-car',
      method: 'get',
      params
    })
  },

  // 保存私车档案
  save(data) {
    return request({
      url: '/cwgxAi/private-car',
      method: 'post',
      data
    })
  },

  // 删除私车档案
  delete(carId) {
    return request({
      url: `/cwgxAi/private-car/${carId}`,
      method: 'delete'
    })
  },

  // 获取私车档案详情
  getDetail(carId) {
    return request({
      url: `/cwgxAi/private-car/${carId}`,
      method: 'get'
    })
  },

  // 里程录入
  recordMileage(data) {
    return request({
      url: '/cwgxAi/private-car/mileage',
      method: 'post',
      data
    })
  },

  // 获取里程记录
  getMileageRecords(params) {
    return request({
      url: '/cwgxAi/private-car/mileage',
      method: 'get',
      params
    })
  },

  // 补贴计算
  calculateSubsidy(data) {
    return request({
      url: '/cwgxAi/private-car/subsidy/calculate',
      method: 'post',
      data
    })
  },

  // 补贴申请
  applySubsidy(data) {
    return request({
      url: '/cwgxAi/private-car/subsidy/apply',
      method: 'post',
      data
    })
  },

  // 获取补贴记录
  getSubsidyRecords(params) {
    return request({
      url: '/cwgxAi/private-car/subsidy',
      method: 'get',
      params
    })
  },

  // 路径规划
  planRoute(data) {
    return request({
      url: '/cwgxAi/private-car/route/plan',
      method: 'post',
      data
    })
  },

  // 里程统计
  getMileageStatistics(params) {
    return request({
      url: '/cwgxAi/private-car/statistics/mileage',
      method: 'get',
      params
    })
  },

  // 补贴统计
  getSubsidyStatistics(params) {
    return request({
      url: '/cwgxAi/private-car/statistics/subsidy',
      method: 'get',
      params
    })
  },

  // 导出私车公用数据
  export(params) {
    return request({
      url: '/cwgxAi/private-car/export',
      method: 'get',
      params
    })
  }
}

// 多币种API
export const currencyApi = {
  // 获取币种列表
  getList(params) {
    return request({
      url: '/zbgl/currency',
      method: 'get',
      params
    })
  },

  // 保存币种配置
  save(data) {
    return request({
      url: '/zbgl/currency',
      method: 'post',
      data
    })
  },

  // 删除币种
  delete(currencyId) {
    return request({
      url: `/zbgl/currency/${currencyId}`,
      method: 'delete'
    })
  },

  // 获取汇率列表
  getExchangeRates(params) {
    return request({
      url: '/zbgl/currency/exchange-rates',
      method: 'get',
      params
    })
  },

  // 保存汇率
  saveExchangeRate(data) {
    return request({
      url: '/zbgl/currency/exchange-rates',
      method: 'post',
      data
    })
  },

  // 获取实时汇率
  getRealTimeRate(fromCurrency, toCurrency) {
    return request({
      url: '/zbgl/currency/real-time-rate',
      method: 'get',
      params: { fromCurrency, toCurrency }
    })
  },

  // 币种转换
  convert(data) {
    return request({
      url: '/zbgl/currency/convert',
      method: 'post',
      data
    })
  },

  // 异币种核销
  crossCurrencyWriteOff(data) {
    return request({
      url: '/zbgl/currency/cross-writeoff',
      method: 'post',
      data
    })
  },

  // 汇率波动提醒设置
  setRateAlert(data) {
    return request({
      url: '/zbgl/currency/rate-alert',
      method: 'post',
      data
    })
  },

  // 获取汇率波动提醒
  getRateAlerts(params) {
    return request({
      url: '/zbgl/currency/rate-alert',
      method: 'get',
      params
    })
  },

  // 多币种统计
  getStatistics(params) {
    return request({
      url: '/zbgl/currency/statistics',
      method: 'get',
      params
    })
  },

  // 导出多币种数据
  export(params) {
    return request({
      url: '/zbgl/currency/export',
      method: 'get',
      params
    })
  }
}

// 费控看板API
export const dashboardApi = {
  // 获取总览数据
  getOverview(params) {
    return request({
      url: '/cwgxAi/dashboard/overview',
      method: 'get',
      params
    })
  },

  // 获取实时数据
  getRealtime() {
    return request({
      url: '/cwgxAi/dashboard/realtime',
      method: 'get'
    })
  },

  // 获取审批统计
  getApprovalStats(params) {
    return request({
      url: '/cwgxAi/dashboard/approval-stats',
      method: 'get',
      params
    })
  },

  // 获取预算分析
  getBudgetAnalysis(params) {
    return request({
      url: '/cwgxAi/dashboard/budget-analysis',
      method: 'get',
      params
    })
  },

  // 获取费用分析
  getExpenseAnalysis(params) {
    return request({
      url: '/cwgxAi/dashboard/expense-analysis',
      method: 'get',
      params
    })
  },

  // 获取风险监控
  getRiskMonitoring() {
    return request({
      url: '/cwgxAi/dashboard/risk-monitoring',
      method: 'get'
    })
  },

  // 获取系统性能
  getSystemPerformance() {
    return request({
      url: '/cwgxAi/dashboard/system-performance',
      method: 'get'
    })
  },

  // 获取自定义看板配置
  getCustomConfig(userId) {
    return request({
      url: '/cwgxAi/dashboard/custom-config',
      method: 'get',
      params: { userId }
    })
  },

  // 保存自定义看板配置
  saveCustomConfig(data) {
    return request({
      url: '/cwgxAi/dashboard/custom-config',
      method: 'post',
      data
    })
  }
}
