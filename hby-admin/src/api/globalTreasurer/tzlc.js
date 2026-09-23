import request from '@/utils/request'

// 投资计划管理API
export const investmentPlanApi = {
  // 分页查询投资计划
  getList(params) {
    return request({
      url: '/qqsk/investment/plan/list',
      method: 'get',
      params
    })
  },

  // 获取投资计划详情
  getInfo(planId) {
    return request({
      url: '/qqsk/investment/plan/getById',
      method: 'get',
      params: { planId }
    })
  },

  // 根据计划编号获取投资计划
  getByPlanNo(planNo) {
    return request({
      url: `/qqsk/investment/plan/planNo/${planNo}`,
      method: 'get'
    })
  },

  // 新增投资计划
  add(data) {
    return request({
      url: '/qqsk/investment/plan/save',
      method: 'post',
      data: data
    })
  },

  // 修改投资计划
  update(data) {
    return request({
      url: '/qqsk/investment/plan/save',
      method: 'post',
      data: data
    })
  },

  // 删除投资计划
  delete(planIds) {
    return request({
      url: '/qqsk/investment/plan/delete',
      method: 'post',
      params: { planId: planIds }
    })
  },

  // 提交投资计划
  submit(planId) {
    return request({
      url: '/qqsk/investment/plan/submit',
      method: 'post',
      params: { planId }
    })
  },

  // 审批投资计划
  approve(planId, approvalComments) {
    return request({
      url: '/qqsk/investment/plan/approve',
      method: 'post',
      params: { planId, approvalComments }
    })
  },

  // 拒绝投资计划
  reject(planId, rejectionReason) {
    return request({
      url: '/qqsk/investment/plan/reject',
      method: 'post',
      params: { planId, rejectionReason }
    })
  },

  // 执行投资计划
  execute(planId) {
    return request({
      url: '/qqsk/investment/plan/execute',
      method: 'post',
      params: { planId }
    })
  },

  // 完成投资计划
  complete(planId, completionNotes) {
    return request({
      url: '/qqsk/investment/plan/complete',
      method: 'post',
      params: { planId, completionNotes }
    })
  },

  // 获取统计信息
  getStatistics() {
    return request({
      url: '/qqsk/investment/plan/statistics',
      method: 'get'
    })
  },

  // 获取分析数据
  getAnalysis() {
    return request({
      url: '/qqsk/investment/plan/analysis',
      method: 'get'
    })
  },

  // 获取趋势分析
  getTrend(months = 12) {
    return request({
      url: '/qqsk/investment/plan/trend',
      method: 'get',
      params: { months }
    })
  },

  // 获取风险分析
  getRiskAnalysis() {
    return request({
      url: '/qqsk/investment/plan/risk',
      method: 'get'
    })
  },

  // 获取收益分析
  getReturnAnalysis() {
    return request({
      url: '/qqsk/investment/plan/return',
      method: 'get'
    })
  },

  // 获取优化建议
  getOptimizationSuggestions() {
    return request({
      url: '/qqsk/investment/plan/optimization',
      method: 'get'
    })
  },

  // 批量更新状态
  batchUpdateStatus(planIds, planStatus) {
    return request({
      url: '/qqsk/investment/plan/batch/status',
      method: 'post',
      data: planIds,
      params: { planStatus }
    })
  },

  // 导出数据
  export(exportType) {
    return request({
      url: '/qqsk/investment/plan/export',
      method: 'post',
      params: { exportType }
    })
  },

  // 导入数据
  import(planData) {
    return request({
      url: '/qqsk/investment/plan/import',
      method: 'post',
      data: planData
    })
  },

  // 获取提醒
  getReminders(days = 7) {
    return request({
      url: '/qqsk/investment/plan/reminders',
      method: 'get',
      params: { days }
    })
  },

  // 获取预警
  getAlerts() {
    return request({
      url: '/qqsk/investment/plan/alerts',
      method: 'get'
    })
  },

  // 获取推荐
  getRecommendations() {
    return request({
      url: '/qqsk/investment/plan/recommendations',
      method: 'get'
    })
  }
}

// 投资产品管理API
export const investmentProductApi = {
  // 分页查询投资产品
  getList(params) {
    return request({
      url: '/qqsk/investment/product/list',
      method: 'get',
      params
    })
  },

  // 获取投资产品详情
  getInfo(productId) {
    return request({
      url: '/qqsk/investment/product/getById',
      method: 'get',
      params: { productId }
    })
  },

  // 新增投资产品
  add(data) {
    return request({
      url: '/qqsk/investment/product/save',
      method: 'post',
      data
    })
  },

  // 修改投资产品
  update(data) {
    return request({
      url: '/qqsk/investment/product/save',
      method: 'post',
      data
    })
  },

  // 删除投资产品
  delete(productIds) {
    return request({
      url: '/qqsk/investment/product/delete',
      method: 'post',
      params: { productId: productIds }
    })
  },

  // 上架产品
  launch(productId) {
    return request({
      url: '/qqsk/investment/product/launch',
      method: 'post',
      params: { productId }
    })
  },

  // 下架产品
  suspend(productId, suspendReason) {
    return request({
      url: '/qqsk/investment/product/suspend',
      method: 'post',
      params: { productId, suspendReason }
    })
  },

  // 更新产品净值
  updateNav(productId, netValue) {
    return request({
      url: '/qqsk/investment/product/updateNav',
      method: 'post',
      params: { productId, netValue }
    })
  },

  // 获取统计信息
  getStatistics() {
    return request({
      url: '/qqsk/investment/product/statistics',
      method: 'get'
    })
  },

  // 获取分析数据
  getAnalysis() {
    return request({
      url: '/qqsk/investment/product/analysis',
      method: 'get'
    })
  },

  // 获取推荐
  getRecommendations() {
    return request({
      url: '/qqsk/investment/product/recommendations',
      method: 'get'
    })
  }
}

// 银行理财投资API
export const bankWealthInvestmentApi = {
  // 分页查询银行理财投资
  getList(params) {
    return request({
      url: '/qqsk/investment/bankwealth/list',
      method: 'get',
      params
    })
  },

  // 获取银行理财投资详情 - ✅ 已修复: 使用/getById路径和params传参
  getInfo(investmentId) {
    return request({
      url: '/qqsk/investment/bankwealth/getById',
      method: 'get',
      params: { investmentId }
    })
  },

  // 新增银行理财投资 - ✅ 使用/save路径保持一致
  add(data) {
    return request({
      url: '/qqsk/investment/bankwealth/save',
      method: 'post',
      data
    })
  },

  // 修改银行理财投资 - ✅ 已修复: 改为POST方法,使用/save路径
  update(data) {
    return request({
      url: '/qqsk/investment/bankwealth/save',
      method: 'post',
      data
    })
  },

  // 删除银行理财投资 - ✅ 已修复: 改为POST方法,使用/delete路径和params传参
  delete(investmentId) {
    return request({
      url: '/qqsk/investment/bankwealth/delete',
      method: 'post',
      params: { investmentId }
    })
  },

  // 申购理财产品 - ✅ 使用data传递JSON数据
  subscribe(data) {
    return request({
      url: '/qqsk/investment/bankwealth/subscribe',
      method: 'post',
      data: data
    })
  },

  // 赎回理财产品 - ✅ 已修复: 去掉路径参数,使用params传参
  redeem(investmentId, redeemDate, redeemAmount) {
    return request({
      url: '/qqsk/investment/bankwealth/redeem',
      method: 'post',
      params: { investmentId, redeemDate, redeemAmount }
    })
  },

  // 更新投资估值 - ✅ 已修复: 去掉路径参数,参数名改为newValue
  updateValuation(investmentId, newNetValue) {
    return request({
      url: '/qqsk/investment/bankwealth/updateValuation',
      method: 'post',
      params: { investmentId, newValue: newNetValue }
    })
  },

  // 获取统计信息
  getStatistics() {
    return request({
      url: '/qqsk/investment/bankwealth/statistics',
      method: 'get'
    })
  },

  // 获取分析数据
  getAnalysis() {
    return request({
      url: '/qqsk/investment/bankwealth/analysis',
      method: 'get'
    })
  },

  // 获取提醒
  getReminders(days = 7) {
    return request({
      url: '/qqsk/investment/bankwealth/reminders',
      method: 'get',
      params: { days }
    })
  },

  // 获取预警
  getAlerts() {
    return request({
      url: '/qqsk/investment/bankwealth/alerts',
      method: 'get'
    })
  },

  // 获取推荐
  getRecommendations() {
    return request({
      url: '/qqsk/investment/bankwealth/recommendations',
      method: 'get'
    })
  }
}

// 债券投资API
export const bondInvestmentApi = {
  // 分页查询债券投资
  getList(params) {
    return request({
      url: '/qqsk/investment/bond/list',
      method: 'get',
      params
    })
  },

  // 获取债券投资详情
  getInfo(investmentId) {
    return request({
      url: `/qqsk/investment/bond/${investmentId}`,
      method: 'get'
    })
  },

  // 新增债券投资
  add(data) {
    return request({
      url: '/qqsk/investment/bond',
      method: 'post',
      data
    })
  },

  // 修改债券投资
  update(data) {
    return request({
      url: '/qqsk/investment/bond',
      method: 'put',
      data
    })
  },

  // 删除债券投资
  delete(investmentIds) {
    return request({
      url: `/qqsk/investment/bond/${investmentIds}`,
      method: 'delete'
    })
  },

  // 购买债券
  purchase(data) {
    return request({
      url: '/qqsk/investment/bond/purchase',
      method: 'post',
      params: data
    })
  },

  // 出售债券
  sell(investmentId, sellPrice, sellQuantity, sellDate) {
    return request({
      url: `/qqsk/investment/bond/sell/${investmentId}`,
      method: 'post',
      params: { sellPrice, sellQuantity, sellDate }
    })
  },

  // 处理付息
  processCoupon(investmentId, couponAmount, paymentDate) {
    return request({
      url: `/qqsk/investment/bond/coupon/${investmentId}`,
      method: 'post',
      params: { couponAmount, paymentDate }
    })
  },

  // 更新债券估值
  updateValuation(investmentId, newPrice, newYield) {
    return request({
      url: `/qqsk/investment/bond/updateValuation/${investmentId}`,
      method: 'post',
      params: { newPrice, newYield }
    })
  },

  // 获取统计信息
  getStatistics() {
    return request({
      url: '/qqsk/investment/bond/statistics',
      method: 'get'
    })
  },

  // 获取分析数据
  getAnalysis() {
    return request({
      url: '/qqsk/investment/bond/analysis',
      method: 'get'
    })
  },

  // 获取提醒
  getReminders(days = 7) {
    return request({
      url: '/qqsk/investment/bond/reminders',
      method: 'get',
      params: { days }
    })
  },

  // 获取付息提醒
  getCouponReminders(days = 7) {
    return request({
      url: '/qqsk/investment/bond/couponReminders',
      method: 'get',
      params: { days }
    })
  },

  // 获取预警
  getAlerts() {
    return request({
      url: '/qqsk/investment/bond/alerts',
      method: 'get'
    })
  },

  // 获取推荐
  getRecommendations() {
    return request({
      url: '/qqsk/investment/bond/recommendations',
      method: 'get'
    })
  }
}

// 投资监控API
export const investmentMonitoringApi = {
  // 分页查询投资监控
  getList(params) {
    return request({
      url: '/qqsk/investment/monitoring/list',
      method: 'get',
      params
    })
  },

  // 获取投资监控详情
  getInfo(monitoringId) {
    return request({
      url: '/qqsk/investment/monitoring/getById',
      method: 'get',
      params: { monitoringId }
    })
  },

  // 新增投资监控
  add(data) {
    return request({
      url: '/qqsk/investment/monitoring',
      method: 'post',
      data
    })
  },

  // 修改投资监控
  update(data) {
    return request({
      url: '/qqsk/investment/monitoring',
      method: 'put',
      data
    })
  },

  // 删除投资监控
  delete(monitoringIds) {
    return request({
      url: '/qqsk/investment/monitoring/delete',
      method: 'post',
      params: { monitoringId: monitoringIds }
    })
  },

  // 批量删除投资监控
  batchDelete(monitoringIds) {
    return request({
      url: '/qqsk/investment/monitoring/batchDelete',
      method: 'post',
      params: { monitoringIds: monitoringIds },
      paramsSerializer: params => {
        // 使用 repeat 方式序列化数组参数，不带方括号
        // 结果：monitoringIds=1&monitoringIds=2
        return Object.keys(params).map(key => {
          const val = params[key]
          if (Array.isArray(val)) {
            return val.map(v => `${key}=${v}`).join('&')
          }
          return `${key}=${val}`
        }).join('&')
      }
    })
  },

  // 创建监控记录
  createRecord(data) {
    return request({
      url: '/qqsk/investment/monitoring/create',
      method: 'post',
      params: data
    })
  },

  // 更新监控记录
  updateRecord(monitoringId, data) {
    return request({
      url: `/qqsk/investment/monitoring/update/${monitoringId}`,
      method: 'post',
      params: data
    })
  },

  // 处理监控预警
  processAlert(monitoringId, alertType, alertMessage) {
    return request({
      url: `/qqsk/investment/monitoring/alert/${monitoringId}`,
      method: 'post',
      params: { alertType, alertMessage }
    })
  },

  // 生成监控记录
  generateRecords(monitoringDate) {
    return request({
      url: '/qqsk/investment/monitoring/generate',
      method: 'post',
      params: { monitoringDate }
    })
  },

  // 获取统计信息
  getStatistics() {
    return request({
      url: '/qqsk/investment/monitoring/statistics',
      method: 'get'
    })
  },

  // 获取分析数据
  getAnalysis() {
    return request({
      url: '/qqsk/investment/monitoring/analysis',
      method: 'get'
    })
  },

  // 获取趋势分析
  getTrend(months = 12) {
    return request({
      url: '/qqsk/investment/monitoring/trend',
      method: 'get',
      params: { months }
    })
  },

  // 获取仪表盘数据
  getDashboard() {
    return request({
      url: '/qqsk/investment/monitoring/dashboard',
      method: 'get'
    })
  },

  // 获取预警
  getAlerts() {
    return request({
      url: '/qqsk/investment/monitoring/alerts',
      method: 'get'
    })
  },

  // 获取推荐
  getRecommendations() {
    return request({
      url: '/qqsk/investment/monitoring/recommendations',
      method: 'get'
    })
  },

  // 异常检测
  detectAnomaly() {
    return request({
      url: '/qqsk/investment/monitoring/anomaly',
      method: 'get'
    })
  }
}

// 投资限额API
export const investmentLimitApi = {
  // 分页查询投资限额
  getList(params) {
    return request({
      url: '/qqsk/investment/limit/list',
      method: 'get',
      params
    })
  },

  // 获取投资限额详情
  getInfo(limitId) {
    return request({
      url: `/qqsk/investment/limit/${limitId}`,
      method: 'get'
    })
  },

  // 新增投资限额
  add(data) {
    return request({
      url: '/qqsk/investment/limit',
      method: 'post',
      data
    })
  },

  // 修改投资限额
  update(data) {
    return request({
      url: '/qqsk/investment/limit',
      method: 'put',
      data
    })
  },

  // 删除投资限额
  delete(limitIds) {
    return request({
      url: `/qqsk/investment/limit/${limitIds}`,
      method: 'delete'
    })
  },

  // 创建限额
  createLimit(data) {
    return request({
      url: '/qqsk/investment/limit/create',
      method: 'post',
      params: data
    })
  },

  // 激活限额
  activate(limitId) {
    return request({
      url: `/qqsk/investment/limit/activate/${limitId}`,
      method: 'post'
    })
  },

  // 停用限额
  deactivate(limitId, reason) {
    return request({
      url: `/qqsk/investment/limit/deactivate/${limitId}`,
      method: 'post',
      params: { reason }
    })
  },

  // 重置限额使用量
  resetUsage(limitId) {
    return request({
      url: `/qqsk/investment/limit/reset/${limitId}`,
      method: 'post'
    })
  },

  // 调整限额金额
  adjustAmount(limitId, newLimitAmount, adjustmentReason) {
    return request({
      url: `/qqsk/investment/limit/adjust/${limitId}`,
      method: 'post',
      params: { newLimitAmount, adjustmentReason }
    })
  },

  // 延期限额
  extend(limitId, newExpireDate, extensionReason) {
    return request({
      url: `/qqsk/investment/limit/extend/${limitId}`,
      method: 'post',
      params: { newExpireDate, extensionReason }
    })
  },

  // 复制限额
  copy(limitId, newLimitName) {
    return request({
      url: `/qqsk/investment/limit/copy/${limitId}`,
      method: 'post',
      params: { newLimitName }
    })
  },

  // 验证投资限额
  validateInvestment(productType, issuer, riskLevel, amount) {
    return request({
      url: '/qqsk/investment/limit/validate',
      method: 'post',
      params: { productType, issuer, riskLevel, amount }
    })
  },

  // 获取可用投资额度
  getQuota(productType, issuer, riskLevel) {
    return request({
      url: '/qqsk/investment/limit/quota',
      method: 'get',
      params: { productType, issuer, riskLevel }
    })
  },

  // 获取统计信息
  getStatistics() {
    return request({
      url: '/qqsk/investment/limit/statistics',
      method: 'get'
    })
  },

  // 获取分析数据
  getAnalysis() {
    return request({
      url: '/qqsk/investment/limit/analysis',
      method: 'get'
    })
  },

  // 获取仪表盘数据
  getDashboard() {
    return request({
      url: '/qqsk/investment/limit/dashboard',
      method: 'get'
    })
  },

  // 获取预警
  getAlerts() {
    return request({
      url: '/qqsk/investment/limit/alerts',
      method: 'get'
    })
  },

  // 获取推荐
  getRecommendations() {
    return request({
      url: '/qqsk/investment/limit/recommendations',
      method: 'get'
    })
  },

  // 获取提醒
  getReminders(days = 7) {
    return request({
      url: '/qqsk/investment/limit/reminders',
      method: 'get',
      params: { days }
    })
  }
}

// 投资合规检查API
export const investmentComplianceApi = {
  // 分页查询投资合规检查
  getList(params) {
    return request({
      url: '/qqsk/investment/compliance/list',
      method: 'get',
      params
    })
  },

  // 获取投资合规检查详情
  getInfo(checkId) {
    return request({
      url: `/qqsk/investment/compliance/${checkId}`,
      method: 'get'
    })
  },

  // 新增投资合规检查
  add(data) {
    return request({
      url: '/qqsk/investment/compliance',
      method: 'post',
      data
    })
  },

  // 修改投资合规检查
  update(data) {
    return request({
      url: '/qqsk/investment/compliance',
      method: 'put',
      data
    })
  },

  // 删除投资合规检查
  delete(checkIds) {
    return request({
      url: `/qqsk/investment/compliance/${checkIds}`,
      method: 'delete'
    })
  },

  // 创建合规检查
  createCheck(data) {
    return request({
      url: '/qqsk/investment/compliance/create',
      method: 'post',
      params: data
    })
  },

  // 执行合规检查
  executeCheck(checkId, data) {
    return request({
      url: `/qqsk/investment/compliance/execute/${checkId}`,
      method: 'post',
      params: data
    })
  },

  // 完成合规检查
  completeCheck(checkId, finalResult, completionNotes) {
    return request({
      url: `/qqsk/investment/compliance/complete/${checkId}`,
      method: 'post',
      params: { finalResult, completionNotes }
    })
  },

  // 重新检查
  recheckCompliance(checkId, recheckReason) {
    return request({
      url: `/qqsk/investment/compliance/recheck/${checkId}`,
      method: 'post',
      params: { recheckReason }
    })
  },

  // 上报合规问题
  reportIssue(checkId, reportDetails, reportUser) {
    return request({
      url: `/qqsk/investment/compliance/report/${checkId}`,
      method: 'post',
      params: { reportDetails, reportUser }
    })
  },

  // 处理纠正措施
  processCorrectiveAction(checkId, actionTaken, actionDate, actionUser) {
    return request({
      url: `/qqsk/investment/compliance/corrective/${checkId}`,
      method: 'post',
      params: { actionTaken, actionDate, actionUser }
    })
  },

  // 关闭合规检查
  closeCheck(checkId, closureReason) {
    return request({
      url: `/qqsk/investment/compliance/close/${checkId}`,
      method: 'post',
      params: { closureReason }
    })
  },

  // 自动执行合规检查
  autoExecute() {
    return request({
      url: '/qqsk/investment/compliance/auto',
      method: 'post'
    })
  },

  // 获取合规率
  getComplianceRate() {
    return request({
      url: '/qqsk/investment/compliance/rate',
      method: 'get'
    })
  },

  // 获取统计信息
  getStatistics() {
    return request({
      url: '/qqsk/investment/compliance/statistics',
      method: 'get'
    })
  },

  // 获取分析数据
  getAnalysis() {
    return request({
      url: '/qqsk/investment/compliance/analysis',
      method: 'get'
    })
  },

  // 获取趋势分析
  getTrend(months = 12) {
    return request({
      url: '/qqsk/investment/compliance/trend',
      method: 'get',
      params: { months }
    })
  },

  // 获取仪表盘数据
  getDashboard() {
    return request({
      url: '/qqsk/investment/compliance/dashboard',
      method: 'get'
    })
  },

  // 获取预警
  getAlerts() {
    return request({
      url: '/qqsk/investment/compliance/alerts',
      method: 'get'
    })
  },

  // 获取推荐
  getRecommendations() {
    return request({
      url: '/qqsk/investment/compliance/recommendations',
      method: 'get'
    })
  },

  // 生成合规报告
  generateReport(startDate, endDate) {
    return request({
      url: '/qqsk/investment/compliance/generateReport',
      method: 'post',
      params: { startDate, endDate }
    })
  }
}

// 默认导出所有API
export default {
  investmentPlanApi,
  investmentProductApi,
  bankWealthInvestmentApi,
  bondInvestmentApi,
  investmentMonitoringApi,
  investmentLimitApi,
  investmentComplianceApi
}
