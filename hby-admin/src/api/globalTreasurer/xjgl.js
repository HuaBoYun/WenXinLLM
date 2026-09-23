import request from '@/utils/request'

// 现金管理模块API接口

// ==================== 银行对账单管理 ====================

/**
 * 分页查询银行对账单
 */
export function getBankStatementPage(params) {
  return request({
    url: '/qqsk/cash/bank-statement/page',
    method: 'get',
    params
  })
}

/**
 * 根据ID查询银行对账单
 */
export function getBankStatement(statementId) {
  return request({
    url: `/qqsk/cash/bank-statement/${statementId}`,
    method: 'get'
  })
}

/**
 * 创建银行对账单
 */
export function createBankStatement(data) {
  return request({
    url: '/qqsk/cash/bank-statement',
    method: 'post',
    data
  })
}

/**
 * 更新银行对账单
 */
export function updateBankStatement(data) {
  return request({
    url: '/qqsk/cash/bank-statement',
    method: 'put',
    data
  })
}

/**
 * 删除银行对账单
 */
export function deleteBankStatement(statementId) {
  return request({
    url: `/qqsk/cash/bank-statement/${statementId}`,
    method: 'delete'
  })
}

/**
 * 银行对账
 */
export function reconcileBankStatement(statementId, reconcileUser) {
  return request({
    url: `/qqsk/cash/bank-statement/${statementId}/reconcile`,
    method: 'put',
    params: { reconcileUser }
  })
}

/**
 * 批量银行对账
 */
export function batchReconcileBankStatements(statementIds, reconcileUser) {
  return request({
    url: '/qqsk/cash/bank-statement/batch-reconcile',
    method: 'put',
    data: statementIds,
    params: { reconcileUser }
  })
}

/**
 * 获取未对账记录
 */
export function getUnreconciledStatements(accountId, orgId) {
  return request({
    url: '/qqsk/cash/bank-statement/unreconciled',
    method: 'get',
    params: { accountId, orgId }
  })
}

// ==================== 收款单管理 ====================

/**
 * 分页查询收款单
 */
export function getReceiptPage(params) {
  return request({
    url: '/qqsk/cash/receipt/page',
    method: 'get',
    params
  })
}

/**
 * 根据ID查询收款单
 */
export function getReceipt(receiptId) {
  return request({
    url: `/qqsk/cash/receipt/${receiptId}`,
    method: 'get'
  })
}

/**
 * 根据收款单号查询收款单
 */
export function getReceiptByNo(receiptNo, orgId) {
  return request({
    url: `/qqsk/cash/receipt/no/${receiptNo}`,
    method: 'get',
    params: { orgId }
  })
}

/**
 * 创建收款单
 */
export function createReceipt(data) {
  return request({
    url: '/qqsk/cash/receipt',
    method: 'post',
    data: JSON.stringify(data),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 更新收款单
 */
export function updateReceipt(data) {
  return request({
    url: '/qqsk/cash/receipt',
    method: 'put',
    data: JSON.stringify(data),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 删除收款单
 */
export function deleteReceipt(receiptId) {
  return request({
    url: `/qqsk/cash/receipt/${receiptId}`,
    method: 'delete'
  })
}

/**
 * 批量删除收款单
 */
export function batchDeleteReceipt(data) {
  return request({
    url: '/qqsk/cash/receipt/batch',
    method: 'delete',
    data,
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

/**
 * 确认收款单
 */
export function confirmReceipt(receiptId) {
  return request({
    url: `/qqsk/cash/receipt/${receiptId}/confirm`,
    method: 'put'
  })
}

/**
 * 取消收款单
 */
export function cancelReceipt(receiptId) {
  return request({
    url: `/qqsk/cash/receipt/${receiptId}/cancel`,
    method: 'put'
  })
}

/**
 * 生成收款单号
 */
export function generateReceiptNo(orgId) {
  return request({
    url: '/qqsk/cash/receipt/generate-no',
    method: 'get',
    params: { orgId }
  })
}

/**
 * 核销收款单
 */
export function verifyReceipt(receiptId, verifyUser) {
  return request({
    url: `/qqsk/cash/receipt/${receiptId}/verify`,
    method: 'put',
    params: { verifyUser }
  })
}

/**
 * 打印收款单
 */
export function printReceipt(receiptId) {
  return request({
    url: `/qqsk/cash/receipt/${receiptId}/print`,
    method: 'get',
    responseType: 'blob'
  })
}

/**
 * 导出收款单
 */
export function exportReceipt(params) {
  return request({
    url: '/qqsk/cash/receipt/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

/**
 * 导出单个收款单
 */
export function exportReceiptSingle(receiptId) {
  return request({
    url: `/qqsk/cash/receipt/${receiptId}/export`,
    method: 'get',
    responseType: 'blob'
  })
}

// ==================== 付款单管理 ====================

/**
 * 分页查询付款单
 */
export function getPaymentPage(params) {
  return request({
    url: '/qqsk/cash/payment/page',
    method: 'get',
    params
  })
}

/**
 * 根据ID查询付款单
 */
export function getPayment(paymentId) {
  return request({
    url: `/qqsk/cash/payment/${paymentId}`,
    method: 'get'
  })
}

/**
 * 创建付款单
 */
export function createPayment(data) {
  return request({
    url: '/qqsk/cash/payment',
    method: 'post',
    data
  })
}

/**
 * 更新付款单
 */
export function updatePayment(data) {
  return request({
    url: '/qqsk/cash/payment',
    method: 'put',
    data
  })
}

/**
 * 删除付款单
 */
export function deletePayment(paymentId) {
  return request({
    url: `/qqsk/cash/payment/${paymentId}`,
    method: 'delete'
  })
}

/**
 * 批量删除付款单
 */
export function batchDeletePayment(data) {
  return request({
    url: '/qqsk/cash/payment/batch',
    method: 'delete',
    data,
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

/**
 * 提交付款单
 */
export function submitPayment(paymentId) {
  return request({
    url: `/qqsk/cash/payment/${paymentId}/submit`,
    method: 'put'
  })
}

/**
 * 审批付款单
 */
export function approvePayment(paymentId, approved, approvalUser, approvalOpinion) {
  return request({
    url: `/qqsk/cash/payment/${paymentId}/approve`,
    method: 'put',
    params: { approved, approvalUser, approvalOpinion }
  })
}

/**
 * 执行付款
 */
export function executePayment(paymentId) {
  return request({
    url: `/qqsk/cash/payment/${paymentId}/execute`,
    method: 'put'
  })
}

/**
 * 生成付款单号
 */
export function generatePaymentNo(orgId) {
  return request({
    url: '/qqsk/cash/payment/generate-no',
    method: 'get',
    params: { orgId }
  })
}

/**
 * 取消付款单
 */
export function cancelPayment(paymentId) {
  return request({
    url: `/qqsk/cash/payment/${paymentId}/cancel`,
    method: 'put'
  })
}

/**
 * 拒绝付款单
 */
export function rejectPayment(paymentId, rejectUser, rejectReason) {
  return request({
    url: `/qqsk/cash/payment/${paymentId}/reject`,
    method: 'put',
    params: { rejectUser, rejectReason }
  })
}

/**
 * 打印付款单
 */
export function printPayment(paymentId) {
  return request({
    url: `/qqsk/cash/payment/${paymentId}/print`,
    method: 'get',
    responseType: 'blob'
  })
}

/**
 * 导出付款单列表
 */
export function exportPayment(params) {
  return request({
    url: '/qqsk/cash/payment/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

/**
 * 导出单个付款单
 */
export function exportPaymentSingle(paymentId) {
  return request({
    url: `/qqsk/cash/payment/${paymentId}/export`,
    method: 'get',
    responseType: 'blob'
  })
}

// ==================== 转账管理 ====================

/**
 * 分页查询转账记录
 */
export function getTransferPage(params) {
  return request({
    url: '/qqsk/cash/transfer/page',
    method: 'get',
    params
  })
}

/**
 * 创建转账记录
 */
export function createTransfer(data) {
  return request({
    url: '/qqsk/cash/transfer',
    method: 'post',
    data
  })
}

/**
 * 执行转账
 */
export function executeTransfer(transferId, executeUser) {
  return request({
    url: `/qqsk/cash/transfer/${transferId}/execute`,
    method: 'put',
    params: { executeUser }
  })
}

// ==================== 银行日记账管理 ====================

/**
 * 分页查询银行日记账
 */
export function getBankJournalPage(params) {
  return request({
    url: '/qqsk/cash/bank-journal/page',
    method: 'get',
    params
  })
}

/**
 * 创建银行日记账
 */
export function createBankJournal(data) {
  return request({
    url: '/qqsk/cash/bank-journal',
    method: 'post',
    data
  })
}

/**
 * 获取账户余额
 */
export function getAccountBalance(accountId, date) {
  return request({
    url: `/qqsk/cash/bank-journal/balance/${accountId}`,
    method: 'get',
    params: { date }
  })
}

// ==================== 现金日记账管理 ====================

/**
 * 分页查询现金日记账
 */
export function getCashJournalPage(params) {
  return request({
    url: '/qqsk/cash/cash-journal/page',
    method: 'get',
    params
  })
}

/**
 * 创建现金日记账
 */
export function createCashJournal(data) {
  return request({
    url: '/qqsk/cash/cash-journal',
    method: 'post',
    data
  })
}

// ==================== 汇兑损益管理 ====================

/**
 * 计算汇兑损益
 */
export function calculateExchangeGainLoss(params) {
  return request({
    url: '/qqsk/cash/exchange-gain-loss/calculate',
    method: 'post',
    params
  })
}

/**
 * 分页查询汇兑损益记录
 */
export function getExchangeGainLossPage(params) {
  return request({
    url: '/qqsk/cash/exchange-gain-loss/page',
    method: 'get',
    params
  })
}

// ==================== 内部付款管理 ====================

/**
 * 分页查询内部付款单
 */
export function getInternalPaymentPage(params) {
  return request({
    url: '/qqsk/cash/internal-payment/page',
    method: 'get',
    params
  })
}

/**
 * 根据ID查询内部付款单
 */
export function getInternalPayment(paymentId) {
  return request({
    url: `/qqsk/cash/internal-payment/${paymentId}`,
    method: 'get'
  })
}

/**
 * 创建内部付款单
 */
export function createInternalPayment(data) {
  return request({
    url: '/qqsk/cash/internal-payment',
    method: 'post',
    data
  })
}

/**
 * 更新内部付款单
 */
export function updateInternalPayment(data) {
  return request({
    url: '/qqsk/cash/internal-payment',
    method: 'put',
    data
  })
}

/**
 * 删除内部付款单
 */
export function deleteInternalPayment(data) {
  return request({
    url: '/qqsk/cash/internal-payment/batch',
    method: 'delete',
    data
  })
}

/**
 * 提交内部付款单
 */
export function submitInternalPayment(data) {
  return request({
    url: '/qqsk/cash/internal-payment/submit',
    method: 'put',
    data
  })
}

// ==================== 现金缴存管理 ====================

/**
 * 分页查询现金缴存记录
 */
export function getCashDepositPage(params) {
  return request({
    url: '/qqsk/cash/deposit/page',
    method: 'get',
    params
  })
}

/**
 * 根据ID查询现金缴存记录
 */
export function getCashDeposit(depositId) {
  return request({
    url: `/qqsk/cash/deposit/${depositId}`,
    method: 'get'
  })
}

/**
 * 创建现金缴存记录
 */
export function createCashDeposit(data) {
  return request({
    url: '/qqsk/cash/deposit',
    method: 'post',
    data
  })
}

/**
 * 更新现金缴存记录
 */
export function updateCashDeposit(data) {
  return request({
    url: '/qqsk/cash/deposit',
    method: 'put',
    data
  })
}

/**
 * 删除现金缴存记录
 */
export function deleteCashDeposit(data) {
  return request({
    url: '/qqsk/cash/deposit/batch',
    method: 'delete',
    data
  })
}

/**
 * 提交现金缴存记录
 */
export function submitCashDeposit(data) {
  return request({
    url: '/qqsk/cash/deposit/submit',
    method: 'put',
    data
  })
}

// ==================== 现金转出管理 ====================

/**
 * 分页查询现金转出记录
 */
export function getCashWithdrawPage(params) {
  return request({
    url: '/qqsk/cash/withdraw/page',
    method: 'get',
    params
  })
}

/**
 * 根据ID查询现金转出记录
 */
export function getCashWithdraw(withdrawId) {
  return request({
    url: `/qqsk/cash/withdraw/${withdrawId}`,
    method: 'get'
  })
}

/**
 * 创建现金转出记录
 */
export function createCashWithdraw(data) {
  return request({
    url: '/qqsk/cash/withdraw',
    method: 'post',
    data
  })
}

/**
 * 更新现金转出记录
 */
export function updateCashWithdraw(data) {
  return request({
    url: '/qqsk/cash/withdraw',
    method: 'put',
    data
  })
}

/**
 * 删除现金转出记录
 */
export function deleteCashWithdraw(data) {
  return request({
    url: '/qqsk/cash/withdraw/batch',
    method: 'delete',
    data
  })
}

/**
 * 提交现金转出记录
 */
export function submitCashWithdraw(data) {
  return request({
    url: '/qqsk/cash/withdraw/submit',
    method: 'put',
    data
  })
}

// ==================== 内部贷款管理 ====================

/**
 * 分页查询内部贷款付款记录
 */
export function getInternalLoanPaymentPage(params) {
  return request({
    url: '/qqsk/cash/internal-loan-payment/page',
    method: 'get',
    params
  })
}

/**
 * 根据ID查询内部贷款付款记录
 */
export function getInternalLoanPayment(paymentId) {
  return request({
    url: `/qqsk/cash/internal-loan-payment/${paymentId}`,
    method: 'get'
  })
}

/**
 * 创建内部贷款付款记录
 */
export function createInternalLoanPayment(data) {
  return request({
    url: '/qqsk/cash/internal-loan-payment',
    method: 'post',
    data
  })
}

/**
 * 更新内部贷款付款记录
 */
export function updateInternalLoanPayment(data) {
  return request({
    url: '/qqsk/cash/internal-loan-payment',
    method: 'put',
    data
  })
}

/**
 * 删除内部贷款付款记录
 */
export function deleteInternalLoanPayment(data) {
  return request({
    url: '/qqsk/cash/internal-loan-payment/batch',
    method: 'delete',
    data
  })
}

/**
 * 提交内部贷款付款记录
 */
export function submitInternalLoanPayment(data) {
  return request({
    url: '/qqsk/cash/internal-loan-payment/submit',
    method: 'put',
    data
  })
}

// ==================== 通用现金管理API ====================

/**
 * 分页查询现金交易记录（通用）
 */
export function getCashTransactionPage(params) {
  return request({
    url: '/qqsk/cash/transaction/page',
    method: 'get',
    params
  })
}

/**
 * 根据ID查询现金交易记录（通用）
 */
export function getCashTransaction(transactionId) {
  return request({
    url: `/qqsk/cash/transaction/${transactionId}`,
    method: 'get'
  })
}

/**
 * 创建现金交易记录（通用）
 */
export function createCashTransaction(data) {
  return request({
    url: '/qqsk/cash/transaction',
    method: 'post',
    data
  })
}

/**
 * 更新现金交易记录（通用）
 */
export function updateCashTransaction(data) {
  return request({
    url: '/qqsk/cash/transaction',
    method: 'put',
    data
  })
}

/**
 * 删除现金交易记录（通用）
 */
export function deleteCashTransaction(data) {
  return request({
    url: '/qqsk/cash/transaction/batch',
    method: 'delete',
    data
  })
}

/**
 * 提交现金交易记录（通用）
 */
export function submitCashTransaction(data) {
  return request({
    url: '/qqsk/cash/transaction/submit',
    method: 'put',
    data
  })
}

// ==================== 资金调拨管理 ====================

/**
 * 分页查询资金调拨记录
 */
export function getFundTransferPage(params) {
  return request({
    url: '/qqsk/cash/transfer/page',
    method: 'get',
    params
  })
}

/**
 * 创建资金调拨申请
 */
export function createFundTransfer(data) {
  return request({
    url: '/qqsk/cash/transfer',
    method: 'post',
    data
  })
}

/**
 * 更新资金调拨申请
 */
export function updateFundTransfer(data) {
  return request({
    url: '/qqsk/cash/transfer',
    method: 'put',
    data
  })
}

/**
 * 审批资金调拨申请
 */
export function approveFundTransfer(transferId, data) {
  return request({
    url: `/qqsk/cash/transfer/${transferId}/approve`,
    method: 'put',
    data
  })
}

/**
 * 执行资金调拨
 */
export function executeFundTransfer(transferId) {
  return request({
    url: `/qqsk/cash/transfer/${transferId}/execute`,
    method: 'put'
  })
}

/**
 * 取消资金调拨
 */
export function cancelFundTransfer(transferId) {
  return request({
    url: `/qqsk/cash/transfer/${transferId}/cancel`,
    method: 'put'
  })
}

/**
 * 获取可用账户列表
 */
export function getAvailableAccounts(params) {
  return request({
    url: '/qqsk/cash/transfer/available-accounts',
    method: 'get',
    params
  })
}

// ==================== 现金流预测 ====================

/**
 * 获取现金流预测数据
 */
export function getCashFlowForecast(params) {
  return request({
    url: '/qqsk/cash/forecast',
    method: 'get',
    params
  })
}

/**
 * 更新预测设置
 */
export function updateForecastSettings(data) {
  return request({
    url: '/qqsk/cash/forecast/settings',
    method: 'put',
    data
  })
}

/**
 * 刷新预测数据
 */
export function refreshForecastData(params) {
  return request({
    url: '/qqsk/cash/forecast/refresh',
    method: 'post',
    data: params
  })
}

/**
 * 导出预测报告
 */
export function exportForecastReport(params) {
  return request({
    url: '/qqsk/cash/forecast/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

/**
 * 获取预测准确率统计
 */
export function getForecastAccuracy(params) {
  return request({
    url: '/qqsk/cash/forecast/accuracy',
    method: 'get',
    params
  })
}

// ==================== 银行对账管理 ====================

/**
 * 分页查询银行对账记录
 */
export function getBankReconciliationPage(params) {
  return request({
    url: '/qqsk/cash/reconciliation/page',
    method: 'get',
    params
  })
}

/**
 * 创建银行对账
 */
export function createBankReconciliation(data) {
  return request({
    url: '/qqsk/cash/reconciliation',
    method: 'post',
    data
  })
}

/**
 * 开始对账
 */
export function startReconciliation(reconciliationId) {
  return request({
    url: `/qqsk/cash/reconciliation/${reconciliationId}/start`,
    method: 'put'
  })
}

/**
 * 自动对账
 */
export function autoReconciliation(data) {
  return request({
    url: '/qqsk/cash/reconciliation/auto',
    method: 'post',
    data
  })
}

/**
 * 手工匹配
 */
export function manualMatch(data) {
  return request({
    url: '/qqsk/cash/reconciliation/manual-match',
    method: 'post',
    data
  })
}

/**
 * 导入银行对账单
 */
export function importBankStatement(data) {
  return request({
    url: '/qqsk/cash/reconciliation/import',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 获取对账详情
 */
export function getReconciliationDetail(reconciliationId) {
  return request({
    url: `/qqsk/cash/reconciliation/${reconciliationId}/detail`,
    method: 'get'
  })
}

/**
 * 生成银行存款余额调节表
 */
export function generateAdjustmentTable(reconciliationId) {
  return request({
    url: `/qqsk/cash/reconciliation/${reconciliationId}/adjustment-table`,
    method: 'post'
  })
}

/**
 * 导出对账报告
 */
export function exportReconciliationReport(params) {
  return request({
    url: '/qqsk/cash/reconciliation/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

// ==================== 现金流预测统计 ====================

/**
 * 获取现金流预测概览统计
 */
export function getForecastOverview(params) {
  return request({
    url: `/qqsk/cash/forecast/overview`,
    method: 'post',
    data: params
  })
}

/**
 * 获取现金流预测趋势图数据
 */
export function getForecastTrend(params) {
  return request({
    url: `/qqsk/cash/forecast/trend`,
    method: 'post',
    data: params
  })
}

/**
 * 获取银行对账概览统计
 */
export function getReconciliationOverview(params) {
  return request({
    url: `/qqsk/cash/reconciliation/overview`,
    method: 'post',
    data: params
  })
}

/**
 * 获取资金调拨概览统计
 */
export function getTransferStatistics(params) {
  return request({
    url: `/qqsk/cash/transfer/statistics`,
    method: 'post',
    data: params
  })
}

/**
 * 批量审批资金调拨
 */
export function batchApproveTransfer(data) {
  return request({
    url: `/qqsk/cash/transfer/batch-approve`,
    method: 'put',
    data: data
  })
}
