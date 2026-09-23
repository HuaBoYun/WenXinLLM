import request from '@/utils/request'

// 工作台管理API
export const workspaceApi = {
  // 获取个人报账工作台数据
  getPersonalWorkspace(userId) {
    return request({
      url: '/cwgxAi/workspace/personal',
      method: 'get',
      params: { userId }
    })
  },

  // 获取财务处理工作台数据
  getFinanceWorkspace(userId) {
    return request({
      url: '/cwgxAi/workspace/finance',
      method: 'get',
      params: { userId }
    })
  },

  // 获取管理决策工作台数据
  getManagementWorkspace(userId) {
    return request({
      url: '/cwgxAi/workspace/management',
      method: 'get',
      params: { userId }
    })
  },

  // 获取待办事项列表
  getTodoList(params) {
    return request({
      url: '/cwgxAi/workspace/todos',
      method: 'get',
      params
    })
  },

  // 处理待办事项
  processTodo(todoId, processData) {
    return request({
      url: `/cwgxAi/workspace/todos/${todoId}/process`,
      method: 'post',
      data: processData
    })
  },

  // 获取工作台配置
  getWorkspaceConfig(userId, workspaceType) {
    return request({
      url: '/cwgxAi/workspace/config',
      method: 'get',
      params: { userId, workspaceType }
    })
  },

  // 保存工作台配置
  saveWorkspaceConfig(config) {
    return request({
      url: '/cwgxAi/workspace/config',
      method: 'post',
      data: config
    })
  },

  // 获取工作台统计数据
  getWorkspaceStatistics(userId, dateRange) {
    return request({
      url: '/cwgxAi/workspace/statistics',
      method: 'get',
      params: { userId, dateRange }
    })
  }
}

// 报销单管理API
export const expenseReportApi = {
  // 查询报销单列表
  getList(params) {
    return request({
      url: '/cwgxAi/expense/reports',
      method: 'get',
      params
    })
  },

  // 保存报销单
  save(data) {
    return request({
      url: '/cwgxAi/expense/reports',
      method: 'post',
      data
    })
  },

  // 删除报销单
  delete(reportId) {
    return request({
      url: `/cwgxAi/expense/reports/${reportId}`,
      method: 'delete'
    })
  },

  // 获取报销单详情
  getDetail(reportId) {
    return request({
      url: `/cwgxAi/expense/reports/${reportId}`,
      method: 'get'
    })
  },

  // 获取报销单明细
  getDetails(reportId) {
    return request({
      url: `/cwgxAi/expense/reports/${reportId}/details`,
      method: 'get'
    })
  },

  // 保存报销单明细
  saveDetails(reportId, details) {
    return request({
      url: `/cwgxAi/expense/reports/${reportId}/details`,
      method: 'post',
      data: details
    })
  },

  // 提交报销单
  submit(reportId, submitData) {
    return request({
      url: `/cwgxAi/expense/reports/${reportId}/submit`,
      method: 'post',
      data: submitData
    })
  },

  // 审批报销单
  approve(reportId, approvalData) {
    return request({
      url: `/cwgxAi/expense/reports/${reportId}/approve`,
      method: 'post',
      data: approvalData
    })
  },

  // 撤回报销单
  withdraw(reportId) {
    return request({
      url: `/cwgxAi/expense/reports/${reportId}/withdraw`,
      method: 'post'
    })
  },

  // 费用分摊
  allocate(reportId, allocationData) {
    return request({
      url: `/cwgxAi/expense/reports/${reportId}/allocate`,
      method: 'post',
      data: allocationData
    })
  },

  // 批量删除报销单
  batchDelete(reportIds) {
    return request({
      url: '/cwgxAi/expense/reports/batch',
      method: 'delete',
      data: reportIds
    })
  },

  // 导出报销单
  export(params) {
    return request({
      url: '/cwgxAi/expense/reports/export',
      method: 'get',
      params
    })
  },

  // 打印报销单
  print(reportId) {
    return request({
      url: `/cwgxAi/expense/reports/${reportId}/print`,
      method: 'get'
    })
  },

  // 获取报销单统计
  getStatistics(params) {
    return request({
      url: '/cwgxAi/expense/reports/statistics',
      method: 'get',
      params
    })
  }
}

// 借款单管理API
export const loanApi = {
  // 查询借款单列表
  getList(params) {
    return request({
      url: '/cwgxAi/expense/loans',
      method: 'get',
      params
    })
  },

  // 保存借款单
  save(data) {
    return request({
      url: '/cwgxAi/expense/loans',
      method: 'post',
      data
    })
  },

  // 删除借款单
  delete(loanId) {
    return request({
      url: `/cwgxAi/expense/loans/${loanId}`,
      method: 'delete'
    })
  },

  // 获取借款单详情
  getDetail(loanId) {
    return request({
      url: `/cwgxAi/expense/loans/${loanId}`,
      method: 'get'
    })
  },

  // 提交借款单
  submit(loanId, submitData) {
    return request({
      url: `/cwgxAi/expense/loans/${loanId}/submit`,
      method: 'post',
      data: submitData
    })
  },

  // 审批借款单
  approve(loanId, approvalData) {
    return request({
      url: `/cwgxAi/expense/loans/${loanId}/approve`,
      method: 'post',
      data: approvalData
    })
  },

  // 放款
  disburse(loanId, disburseData) {
    return request({
      url: `/cwgxAi/expense/loans/${loanId}/disburse`,
      method: 'post',
      data: disburseData
    })
  },

  // 还款
  repay(loanId, repayData) {
    return request({
      url: `/cwgxAi/expense/loans/${loanId}/repay`,
      method: 'post',
      data: repayData
    })
  },

  // 借款变更
  modify(loanId, modifyData) {
    return request({
      url: `/cwgxAi/expense/loans/${loanId}/modify`,
      method: 'post',
      data: modifyData
    })
  },

  // 获取借款额度
  getQuota(userId, loanType) {
    return request({
      url: '/cwgxAi/expense/loans/quota',
      method: 'get',
      params: { userId, loanType }
    })
  },

  // 获取借款记录
  getRecords(loanId) {
    return request({
      url: `/cwgxAi/expense/loans/${loanId}/records`,
      method: 'get'
    })
  },

  // 批量删除借款单
  batchDelete(loanIds) {
    return request({
      url: '/cwgxAi/expense/loans/batch',
      method: 'delete',
      data: loanIds
    })
  },

  // 导出借款单
  export(params) {
    return request({
      url: '/cwgxAi/expense/loans/export',
      method: 'get',
      params
    })
  },

  // 获取借款统计
  getStatistics(params) {
    return request({
      url: '/cwgxAi/loans/statistics',
      method: 'get',
      params
    })
  }
}

// 预付款管理API
export const prepaymentApi = {
  // 查询预付款列表
  getList(params) {
    return request({
      url: '/cwgxAi/prepayments',
      method: 'get',
      params
    })
  },

  // 保存预付款
  save(data) {
    return request({
      url: '/cwgxAi/prepayments',
      method: 'post',
      data
    })
  },

  // 删除预付款
  delete(prepaymentId) {
    return request({
      url: `/cwgxAi/prepayments/${prepaymentId}`,
      method: 'delete'
    })
  },

  // 获取预付款详情
  getDetail(prepaymentId) {
    return request({
      url: `/cwgxAi/prepayments/${prepaymentId}`,
      method: 'get'
    })
  },

  // 提交预付款申请
  submit(prepaymentId, submitData) {
    return request({
      url: `/cwgxAi/prepayments/${prepaymentId}/submit`,
      method: 'post',
      data: submitData
    })
  },

  // 审批预付款
  approve(prepaymentId, approvalData) {
    return request({
      url: `/cwgxAi/prepayments/${prepaymentId}/approve`,
      method: 'post',
      data: approvalData
    })
  },

  // 预付款核销
  writeOff(prepaymentId, writeOffData) {
    return request({
      url: `/cwgxAi/prepayments/${prepaymentId}/writeoff`,
      method: 'post',
      data: writeOffData
    })
  },

  // 预付款退款
  refund(prepaymentId, refundData) {
    return request({
      url: `/cwgxAi/prepayments/${prepaymentId}/refund`,
      method: 'post',
      data: refundData
    })
  },

  // 批量删除预付款
  batchDelete(prepaymentIds) {
    return request({
      url: '/cwgxAi/prepayments/batch',
      method: 'delete',
      data: prepaymentIds
    })
  },

  // 导出预付款
  export(params) {
    return request({
      url: '/cwgxAi/prepayments/export',
      method: 'get',
      params
    })
  },

  // 获取预付款统计
  getStatistics(params) {
    return request({
      url: '/cwgxAi/prepayments/statistics',
      method: 'get',
      params
    })
  }
}

// 费用预提管理API
export const provisionApi = {
  // 查询费用预提列表
  getList(params) {
    return request({
      url: '/cwgxAi/expense/provisions',
      method: 'get',
      params
    })
  },

  // 保存费用预提
  save(data) {
    return request({
      url: '/cwgxAi/expense/provisions',
      method: 'post',
      data
    })
  },

  // 删除费用预提
  delete(provisionId) {
    return request({
      url: `/cwgxAi/expense/provisions/${provisionId}`,
      method: 'delete'
    })
  },

  // 获取费用预提详情
  getDetail(provisionId) {
    return request({
      url: `/cwgxAi/expense/provisions/${provisionId}`,
      method: 'get'
    })
  },

  // 提交费用预提
  submit(provisionId, submitData) {
    return request({
      url: `/cwgxAi/expense/provisions/${provisionId}/submit`,
      method: 'post',
      data: submitData
    })
  },

  // 审批费用预提
  approve(provisionId, approvalData) {
    return request({
      url: `/cwgxAi/expense/provisions/${provisionId}/approve`,
      method: 'post',
      data: approvalData
    })
  },

  // 费用预提冲销
  reverse(provisionId, reverseData) {
    return request({
      url: `/cwgxAi/expense/provisions/${provisionId}/reverse`,
      method: 'post',
      data: reverseData
    })
  },

  // 批量删除费用预提
  batchDelete(provisionIds) {
    return request({
      url: '/cwgxAi/expense/provisions/batch',
      method: 'delete',
      data: provisionIds
    })
  },

  // 导出费用预提
  export(params) {
    return request({
      url: '/cwgxAi/expense/provisions/export',
      method: 'get',
      params
    })
  },

  // 获取费用预提统计
  getStatistics(params) {
    return request({
      url: '/cwgxAi/expense/provisions/statistics',
      method: 'get',
      params
    })
  }
}
