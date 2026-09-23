import request from '@/utils/request'

// 账户管理模块API接口

// ==================== 账户信息管理 ====================

/**
 * 查询账户列表（不分页）
 */
export function getAccountList(params) {
  return request({
    url: '/qqsk/financial/account/list',
    method: 'get',
    params
  })
}

/**
 * 分页查询账户信息
 */
export function getAccountInfoPage(params) {
  return request({
    url: '/qqsk/financial/account/page',
    method: 'post',
    headers: {
      'Content-Type': 'application/json'
    },
    data: params
  })
}

/**
 * 根据ID查询账户信息
 */
export function getAccountInfo(accountId) {
  return request({
    url: `/qqsk/financial/account/${accountId}`,
    method: 'get'
  })
}

/**
 * 根据账户号码查询账户信息
 */
export function getAccountInfoByNumber(accountNumber, orgId) {
  return request({
    url: `/qqsk/account/info/number/${accountNumber}`,
    method: 'get',
    params: { orgId }
  })
}

/**
 * 根据银行编码查询账户列表
 */
export function getAccountsByBankCode(bankCode, orgId) {
  return request({
    url: `/qqsk/account/info/bank/${bankCode}`,
    method: 'get',
    params: { orgId }
  })
}

/**
 * 根据币种查询账户列表
 */
export function getAccountsByCurrency(currencyCode, orgId) {
  return request({
    url: `/qqsk/account/info/currency/${currencyCode}`,
    method: 'get',
    params: { orgId }
  })
}

/**
 * 查询默认账户
 */
export function getDefaultAccount(currencyCode, orgId) {
  return request({
    url: '/qqsk/account/info/default',
    method: 'get',
    params: { currencyCode, orgId }
  })
}

/**
 * 查询直联账户列表
 */
export function getDirectConnectAccounts(orgId) {
  return request({
    url: '/qqsk/account/info/direct-connect',
    method: 'get',
    params: { orgId }
  })
}

/**
 * 创建账户
 */
export function createAccount(data) {
  return request({
    url: '/qqsk/financial/account',
    method: 'post',
    data
  })
}

/**
 * 获取账户统计
 */
export function getAccountStats(params) {
  return request({
    url: '/qqsk/financial/account/stats',
    method: 'get',
    params
  })
}

/**
 * 获取余额概览
 */
export function getBalanceOverview(params) {
  return request({
    url: '/qqsk/financial/account/balance-overview',
    method: 'get',
    params
  })
}

/**
 * 获取账户状态统计
 */
export function getAccountStatusStats(params) {
  return request({
    url: '/qqsk/financial/account/status-stats',
    method: 'get',
    params
  })
}

/**
 * 获取银行分布
 */
export function getBankDistribution(params) {
  return request({
    url: '/qqsk/financial/account/bank-distribution',
    method: 'get',
    params
  })
}

/**
 * 更新账户信息
 */
export function updateAccount(data) {
  return request({
    url: '/qqsk/financial/account',
    method: 'put',
    data
  })
}

/**
 * 删除账户
 */
export function deleteAccount(accountId, updateUser) {
  return request({
    url: `/qqsk/financial/account/${accountId}`,
    method: 'delete',
    params: { updateUser }
  })
}

/**
 * 批量删除账户
 */
export function batchDeleteAccounts(accountIds, updateUser) {
  return request({
    url: '/qqsk/financial/account/batch',
    method: 'delete',
    params: { accountIds: accountIds.join(','), updateUser }
  })
}

/**
 * 激活账户
 */
export function activateAccount(accountId, updateUser) {
  return request({
    url: `/qqsk/financial/account/${accountId}/activate`,
    method: 'put',
    params: { updateUser }
  })
}

/**
 * 冻结账户
 */
export function freezeAccount(accountId, updateUser) {
  return request({
    url: `/qqsk/financial/account/${accountId}/freeze`,
    method: 'put',
    params: { updateUser }
  })
}

/**
 * 解冻账户
 */
export function unfreezeAccount(accountId, updateUser) {
  return request({
    url: `/qqsk/financial/account/${accountId}/unfreeze`,
    method: 'put',
    params: { updateUser }
  })
}

/**
 * 关闭账户
 */
export function closeAccount(accountId, updateUser) {
  return request({
    url: `/qqsk/financial/account/${accountId}/close`,
    method: 'put',
    params: { updateUser }
  })
}

/**
 * 设置默认账户
 */
export function setDefaultAccount(accountId, currencyCode, orgId, updateUser) {
  return request({
    url: '/qqsk/financial/account/default',
    method: 'put',
    params: { accountId, currencyCode, orgId, updateUser }
  })
}

/**
 * 更新账户余额
 */
export function updateAccountBalance(accountId, balance, availableBalance, frozenBalance, updateUser) {
  return request({
    url: `/qqsk/account/info/${accountId}/balance`,
    method: 'put',
    params: { balance, availableBalance, frozenBalance, updateUser }
  })
}

/**
 * 验证账户号码是否唯一
 */
export function checkAccountNumberUnique(accountNumber, orgId, excludeId) {
  return request({
    url: '/qqsk/financial/account/check-unique',
    method: 'get',
    params: { accountNumber, orgId, excludeId }
  })
}

/**
 * 统计账户数量
 */
export function countAccounts(params) {
  return request({
    url: '/qqsk/account/info/count',
    method: 'get',
    params
  })
}

// ==================== 开户申请管理 ====================

/**
 * 分页查询开户申请
 */
export function getOpeningApplicationPage(params) {
  return request({
    url: '/qqsk/financial/account-opening/page',
    method: 'post',
    data: params
  })
}

/**
 * 查询开户申请状态统计
 */
export function getOpeningApplicationStatistics() {
  return request({
    url: '/qqsk/financial/account-opening/statistics',
    method: 'get'
  })
}

/**
 * 根据ID查询开户申请
 */
export function getOpeningApplication(applicationId) {
  return request({
    url: `/qqsk/financial/account-opening/${applicationId}`,
    method: 'get'
  })
}

/**
 * 根据申请编号查询开户申请
 */
export function getOpeningApplicationByNo(applicationNo, orgId) {
  return request({
    url: `/qqsk/financial/account-opening/no/${applicationNo}`,
    method: 'get',
    params: { orgId }
  })
}

/**
 * 创建开户申请
 */
export function createOpeningApplication(data) {
  return request({
    url: '/qqsk/financial/account-opening',
    method: 'post',
    data
  })
}

/**
 * 更新开户申请
 */
export function updateOpeningApplication(data) {
  return request({
    url: '/qqsk/financial/account-opening',
    method: 'put',
    data
  })
}

/**
 * 删除开户申请
 */
export function deleteOpeningApplication(applicationId) {
  return request({
    url: `/qqsk/financial/account-opening/${applicationId}`,
    method: 'delete'
  })
}

/**
 * 提交开户申请
 */
export function submitOpeningApplication(applicationId, updateUser) {
  return request({
    url: `/qqsk/financial/account-opening/${applicationId}/submit`,
    method: 'put',
    params: { updateUser }
  })
}

/**
 * 取消开户申请
 */
export function cancelOpeningApplication(applicationId, updateUser) {
  return request({
    url: `/qqsk/financial/account-opening/${applicationId}/cancel`,
    method: 'put',
    params: { updateUser }
  })
}

/**
 * 审批开户申请
 */
export function approveOpeningApplication(applicationId, approvalOpinion, applicationStatus) {
  return request({
    url: `/qqsk/financial/account-opening/${applicationId}/approve`,
    method: 'put',
    params: { approvalOpinion, applicationStatus }
  })
}

/**
 * 查询待审批开户申请列表
 */
export function getPendingOpeningApplications(orgId) {
  return request({
    url: '/qqsk/financial/account-opening/pending',
    method: 'get',
    params: { orgId }
  })
}

/**
 * 生成开户申请编号
 */
export function generateOpeningApplicationNo(orgId) {
  return request({
    url: '/qqsk/financial/account-opening/generate-no',
    method: 'get',
    params: { orgId }
  })
}

// ==================== 销户申请管理 ====================

/**
 * 分页查询销户申请
 */
export function getClosingApplicationPage(params) {
  return request({
    url: '/qqsk/financial/account-closing/page',
    method: 'get',
    params
  })
}

/**
 * 创建销户申请
 */
export function createClosingApplication(data) {
  return request({
    url: '/qqsk/financial/account-closing',
    method: 'post',
    data
  })
}

/**
 * 审批销户申请
 */
export function approveClosingApplication(applicationId, approved, approvalUser, approvalOpinion) {
  return request({
    url: `/qqsk/financial/account-closing/${applicationId}/approve`,
    method: 'put',
    params: { approved, approvalUser, approvalOpinion }
  })
}

/**
 * 批量审批销户申请
 */
export function batchApproveClosingApplication(applicationIds, approved, approvalOpinion) {
  return request({
    url: '/qqsk/financial/account-closing/batch-approve',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data: { applicationIds, approved, approvalOpinion }
  })
}

// ==================== 账户变更申请管理 ====================

/**
 * 分页查询变更申请
 */
export function getChangeApplicationPage(params) {
  return request({
    url: '/qqsk/financial/account-change/page',
    method: 'post',
    headers: {
      'Content-Type': 'application/json'
    },
    data: params
  })
}

/**
 * 创建变更申请
 */
export function createChangeApplication(data) {
  return request({
    url: '/qqsk/financial/account-change/',
    method: 'post',
    data
  })
}

/**
 * 更新变更申请
 */
export function updateChangeApplication(data) {
  return request({
    url: '/qqsk/financial/account-change/',
    method: 'put',
    data
  })
}

/**
 * 审批变更申请
 */
export function approveChangeApplication(applicationId, approved, approvalUser, approvalOpinion) {
  return request({
    url: `/qqsk/financial/account-change/${applicationId}/approve`,
    method: 'put',
    params: { applicationStatus: approved, approvalUser, approvalOpinion }
  })
}

/**
 * 删除变更申请
 */
export function deleteChangeApplication(applicationId) {
  return request({
    url: `/qqsk/financial/account-change/${applicationId}`,
    method: 'delete'
  })
}

/**
 * 批量删除变更申请
 */
export function batchDeleteChangeApplications(applicationIds) {
  return request({
    url: '/qqsk/financial/account-change/batch',
    method: 'delete',
    params: { ids: applicationIds },
    paramsSerializer: params => {
      return params.ids.map(id => `ids=${id}`).join('&')
    }
  })
}

// ==================== 账户冻结记录管理 ====================

/**
 * 分页查询冻结记录
 */
export function getFreezeRecordPage(params) {
  return request({
    url: '/qqsk/financial/account-freeze/page',
    method: 'get',
    params
  })
}

/**
 * 创建冻结记录
 */
export function createFreezeRecord(data) {
  return request({
    url: '/qqsk/financial/account-freeze',
    method: 'post',
    data
  })
}

/**
 * 解冻记录
 */
export function unfreezeRecord(recordId, unfreezeUser, unfreeReason) {
  return request({
    url: `/qqsk/financial/account-freeze/${recordId}/unfreeze`,
    method: 'put',
    params: { unfreezeUser, unfreeReason }
  })
}

/**
 * 批量解冻账户的所有冻结记录
 */
export function batchUnfreezeByAccountId(accountId, unfreezeUser, unfreeReason, orgId) {
  return request({
    url: `/qqsk/financial/account-freeze/batch-unfreeze/${accountId}`,
    method: 'put',
    params: { unfreezeUser, unfreeReason, orgId }
  })
}

// ==================== 银企直联授权管理 ====================

/**
 * 分页查询直联授权
 */
export function getDirectConnectAuthPage(params) {
  return request({
    url: '/qqsk/financial/direct-connect-auth/page',
    method: 'get',
    params
  })
}

/**
 * 创建直联授权
 */
export function createDirectConnectAuth(data) {
  return request({
    url: '/qqsk/financial/direct-connect-auth',
    method: 'post',
    data
  })
}

/**
 * 更新直联授权
 */
export function updateDirectConnectAuth(data) {
  return request({
    url: '/qqsk/financial/direct-connect-auth',
    method: 'put',
    data
  })
}

/**
 * 删除直联授权
 */
export function deleteDirectConnectAuth(authId) {
  return request({
    url: `/qqsk/financial/direct-connect-auth/${authId}`,
    method: 'delete'
  })
}

// ==================== 账户检查管理 ====================

/**
 * 分页查询账户检查
 */
export function getAccountCheckPage(params) {
  return request({
    url: '/qqsk/financial/account-check/page',
    method: 'post',
    params
  })
}

/**
 * 创建账户检查
 */
export function createAccountCheck(data) {
  return request({
    url: '/qqsk/financial/account-check',
    method: 'post',
    data
  })
}

/**
 * 停止账户检查
 */
export function stopAccountCheck(checkId) {
  return request({
    url: `/qqsk/financial/account-check/${checkId}/stop`,
    method: 'put'
  })
}

/**
 * 重新检查账户
 */
export function recheckAccount(checkId) {
  return request({
    url: `/qqsk/financial/account-check/${checkId}/recheck`,
    method: 'put'
  })
}

/**
 * 删除账户检查记录
 */
export function deleteAccountCheck(checkId) {
  return request({
    url: `/qqsk/financial/account-check/${checkId}`,
    method: 'delete'
  })
}

/**
 * 更新账户检查
 */
export function updateAccountCheck(data) {
  return request({
    url: '/qqsk/financial/account-check',
    method: 'put',
    data
  })
}

// ==================== 账户限额管理 ====================

/**
 * 分页查询账户限额
 */
export function getAccountLimitPage(params) {
  return request({
    url: '/qqsk/financial/account-limit/page',
    method: 'get',
    params
  })
}

/**
 * 创建账户限额
 */
export function createAccountLimit(data) {
  return request({
    url: '/qqsk/financial/account-limit',
    method: 'post',
    data
  })
}

/**
 * 更新账户限额
 */
export function updateAccountLimit(data) {
  return request({
    url: '/qqsk/financial/account-limit',
    method: 'put',
    data
  })
}

/**
 * 删除账户限额
 */
export function deleteAccountLimit(limitId) {
  return request({
    url: `/qqsk/financial/account-limit/${limitId}`,
    method: 'delete'
  })
}

// ==================== U盾信息管理 ====================

/**
 * 分页查询U盾信息
 */
export function getUKeyPage(params) {
  return request({
    url: '/qqsk/financial/ukey-info/page',
    method: 'get',
    params
  })
}

/**
 * 统计U盾各状态数量
 */
export function getUKeyStatusCount() {
  return request({
    url: '/qqsk/financial/ukey-info/status-count',
    method: 'get'
  })
}

/**
 * 创建U盾信息
 */
export function createUKey(data) {
  return request({
    url: '/qqsk/financial/ukey-info',
    method: 'post',
    data
  })
}

/**
 * 更新U盾信息
 */
export function updateUKey(data) {
  return request({
    url: '/qqsk/financial/ukey-info',
    method: 'put',
    data
  })
}

/**
 * 锁定U盾
 */
export function lockUKey(ukeyId) {
  return request({
    url: `/qqsk/financial/ukey-info/${ukeyId}/lock`,
    method: 'put'
  })
}

/**
 * 解锁U盾
 */
export function unlockUKey(ukeyId) {
  return request({
    url: `/qqsk/financial/ukey-info/${ukeyId}/unlock`,
    method: 'put'
  })
}

/**
 * 删除U盾信息
 */
export function deleteUKey(ukeyId) {
  return request({
    url: `/qqsk/financial/ukey-info/${ukeyId}`,
    method: 'delete'
  })
}



/**
 * 根据账户ID查询销户申请
 */
export function getClosingApplicationsByAccountId(accountId) {
  return request({
    url: `/qqsk/treasury/account/closing-applications/account/${accountId}`,
    method: 'get'
  })
}

/**
 * 更新销户申请
 */
export function updateClosingApplication(data) {
  return request({
    url: '/qqsk/financial/account-closing',
    method: 'put',
    data
  })
}

/**
 * 取消销户申请
 */
export function cancelClosingApplication(applicationId, updateUser) {
  return request({
    url: `/qqsk/financial/account-closing/${applicationId}/cancel`,
    method: 'post',
    params: { updateUser }
  })
}

/**
 * 完成销户操作
 */
export function completeClosing(applicationId, closingDate, updateUser) {
  return request({
    url: `/qqsk/financial/account-closing/${applicationId}/complete`,
    method: 'post',
    params: {
      closingDate,
      updateUser
    }
  })
}

/**
 * 查询销户申请统计数据
 */
export function getClosingApplicationStatistics(orgId) {
  return request({
    url: '/qqsk/treasury/account/closing-applications/statistics',
    method: 'get',
    params: { orgId }
  })
}

/**
 * 查询月度销户申请统计
 */
export function getClosingApplicationMonthlyStatistics(year, orgId) {
  return request({
    url: '/qqsk/treasury/account/closing-applications/monthly-statistics',
    method: 'get',
    params: { year, orgId }
  })
}

/**
 * 查询销户申请趋势数据
 */
export function getClosingApplicationTrend(startDate, endDate, orgId) {
  return request({
    url: '/qqsk/treasury/account/closing-applications/trend',
    method: 'get',
    params: { startDate, endDate, orgId }
  })
}

/**
 * 导出销户申请数据
 */
export function exportClosingApplications(params) {
  return request({
    url: '/qqsk/treasury/account/closing-applications/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

// ==================== 全球司库-账户信息管理 (新增) ====================

/**
 * 分页查询账户信息
 */
export function getGtAccountInfoPage(params) {
  return request({
    url: '/qqsk/financial/account/page',
    method: 'post',
    data: params
  })
}

/**
 * 获取全球司库账户统计数据
 */
export function getGtAccountStats(params) {
  return request({
    url: '/qqsk/financial/account/stats',
    method: 'get',
    params
  })
}

/**
 * 获取全球司库余额概览
 */
export function getGtBalanceOverview(params) {
  return request({
    url: '/qqsk/financial/account/balance-overview',
    method: 'get',
    params
  })
}

/**
 * 获取全球司库账户状态统计
 */
export function getGtAccountStatusStats(params) {
  return request({
    url: '/qqsk/financial/account/status-stats',
    method: 'get',
    params
  })
}

/**
 * 获取全球司库银行分布
 */
export function getGtBankDistribution(params) {
  return request({
    url: '/qqsk/financial/account/bank-distribution',
    method: 'get',
    params
  })
}

/**
 * 新增账户
 */
export function createGtAccount(data) {
  return request({
    url: '/qqsk/financial/account',
    method: 'post',
    data
  })
}

/**
 * 修改账户
 */
export function updateGtAccount(data) {
  return request({
    url: '/qqsk/financial/account',
    method: 'put',
    data
  })
}

/**
 * 删除账户
 */
export function deleteGtAccount(accountId, userId) {
  return request({
    url: `/qqsk/financial/account/${accountId}`,
    method: 'delete',
    params: { userId }
  })
}

/**
 * 批量删除账户
 */
export function batchDeleteGtAccounts(accountIds, userId) {
  return request({
    url: '/qqsk/financial/account/batch',
    method: 'delete',
    data: { accountIds, userId }
  })
}

/**
 * 激活账户
 */
export function activateGtAccount(accountId, userId) {
  return request({
    url: `/qqsk/financial/account/${accountId}/activate`,
    method: 'put',
    params: { userId }
  })
}

/**
 * 冻结账户
 */
export function freezeGtAccount(accountId, userId) {
  return request({
    url: `/qqsk/financial/account/${accountId}/freeze`,
    method: 'put',
    params: { userId }
  })
}

/**
 * 解冻账户
 */
export function unfreezeGtAccount(accountId, userId) {
  return request({
    url: `/qqsk/financial/account/${accountId}/unfreeze`,
    method: 'put',
    params: { userId }
  })
}

/**
 * 关闭账户
 */
export function closeGtAccount(accountId, userId) {
  return request({
    url: `/qqsk/financial/account/${accountId}/close`,
    method: 'put',
    params: { userId }
  })
}

/**
 * 设置默认账户
 */
export function setDefaultGtAccount(data) {
  return request({
    url: '/qqsk/financial/account/default',
    method: 'put',
    data
  })
}

// ==================== 账户冻结管理 (新增) ====================

/**
 * 分页查询冻结记录
 */
export function getAccountFreezePage(params) {
  return request({
    url: '/qqsk/financial/account-freeze/page',
    method: 'get',
    params
  })
}

/**
 * 获取冻结统计数据
 */
export function getAccountFreezeStatistics() {
  return request({
    url: '/qqsk/financial/account-freeze/statistics',
    method: 'get'
  })
}

/**
 * 根据ID查询冻结记录详情
 */
export function getAccountFreezeById(id) {
  return request({
    url: `/qqsk/financial/account-freeze/${id}`,
    method: 'get'
  })
}

/**
 * 新增冻结记录
 */
export function createAccountFreeze(data) {
  return request({
    url: '/qqsk/financial/account-freeze/',
    method: 'post',
    data
  })
}

/**
 * 解冻操作
 */
export function unfreezeAccountRecord(id, params) {
  return request({
    url: `/qqsk/financial/account-freeze/${id}/unfreeze`,
    method: 'put',
    params
  })
}

/**
 * 删除冻结记录
 */
export function deleteAccountFreeze(id) {
  return request({
    url: `/qqsk/financial/account-freeze/${id}`,
    method: 'delete'
  })
}

/**
 * 批量解冻
 */
export function batchUnfreezeAccounts(data) {
  return request({
    url: '/qqsk/financial/account-freeze/batch-unfreeze',
    method: 'put',
    data,
    headers: { 'Content-Type': 'application/json' }
  })
}

/**
 * 导出冻结记录
 */
export function exportAccountFreeze(params) {
  return request({
    url: '/qqsk/financial/account-freeze/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

// ==================== 账户限额管理 (特殊功能) ====================

/**
 * 暂停限额配置
 */
export function suspendAccountLimit(limitId) {
  return request({
    url: `/qqsk/financial/account-limit/${limitId}/suspend`,
    method: 'put'
  })
}

/**
 * 激活限额配置
 */
export function activateAccountLimit(limitId) {
  return request({
    url: `/qqsk/financial/account-limit/${limitId}/activate`,
    method: 'put'
  })
}

// ==================== 直连授权管理 (特殊功能) ====================

/**
 * 测试银行连接
 */
export function testBankConnection(authId) {
  return request({
    url: `/qqsk/financial/direct-connect-auth/${authId}/test-connection`,
    method: 'post'
  })
}

/**
 * 同步连接状态
 */
export function syncConnectionStatus(authId) {
  return request({
    url: `/qqsk/financial/direct-connect-auth/${authId}/sync-status`,
    method: 'post'
  })
}

/**
 * 暂停直联授权
 */
export function suspendDirectConnectAuth(authId) {
  return request({
    url: `/qqsk/financial/direct-connect-auth/${authId}/approve`,
    method: 'put',
    params: { authStatus: 'SUSPENDED' }
  })
}

/**
 * 激活直联授权
 */
export function activateDirectConnectAuth(authId) {
  return request({
    url: `/qqsk/financial/direct-connect-auth/${authId}/approve`,
    method: 'put',
    params: { authStatus: 'ACTIVE' }
  })
}

/**
 * 批量删除直联授权
 */
export function batchDeleteDirectConnectAuth(authIds) {
  return request({
    url: '/qqsk/financial/direct-connect-auth/batch',
    method: 'delete',
    data: { authIds },
    headers: { 'Content-Type': 'application/json' }
  })
}

/**
 * 导出直联授权数据
 */
export function exportDirectConnectAuth(params) {
  return request({
    url: '/qqsk/financial/direct-connect-auth/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

/**
 * 批量删除账户限额
 */
export function batchDeleteAccountLimits(limitIds) {
  return request({
    url: '/qqsk/financial/account-limit/batch',
    method: 'delete',
    data: { limitIds },
    headers: { 'Content-Type': 'application/json' }
  })
}

/**
 * 导出账户限额数据
 */
export function exportAccountLimits(params) {
  return request({
    url: '/qqsk/financial/account-limit/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

/**
 * 批量删除U盾信息
 */
export function batchDeleteUKeys(ukeyIds) {
  return request({
    url: '/qqsk/financial/ukey-info/batch',
    method: 'delete',
    data: { ukeyIds },
    headers: { 'Content-Type': 'application/json' }
  })
}

/**
 * 导出U盾信息
 */
export function exportUKeyInfo(params) {
  return request({
    url: '/qqsk/financial/ukey-info/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

/**
 * 导出账户信息
 */
export function exportAccountInfo(params) {
  return request({
    url: '/qqsk/financial/account/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}
