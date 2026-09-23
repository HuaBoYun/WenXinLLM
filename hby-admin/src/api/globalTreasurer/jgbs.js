import request from '@/utils/request'

// 监管报送模块API接口

// ==================== 监管机构管理 ====================

// 分页查询监管机构
export function getAuthorityList(query) {
  return request({
    url: '/qqsk/globalTreasurer/regulatory/authority/list',
    method: 'get',
    params: query
  })
}

// 根据ID查询监管机构
export function getAuthorityById(authorityId) {
  return request({
    url: `/qqsk/globalTreasurer/regulatory/authority/${authorityId}`,
    method: 'get'
  })
}

// 新增监管机构
export function addAuthority(data) {
  return request({
    url: '/qqsk/globalTreasurer/regulatory/authority',
    method: 'post',
    data: data,
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

// 修改监管机构
export function updateAuthority(data) {
  return request({
    url: '/qqsk/globalTreasurer/regulatory/authority',
    method: 'put',
    data: data,
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

// 删除监管机构
export function delAuthority(authorityIds) {
  return request({
    url: `/qqsk/globalTreasurer/regulatory/authority/${authorityIds}`,
    method: 'delete'
  })
}

// 根据机构类型查询监管机构
export function getAuthoritiesByType(authorityType) {
  return request({
    url: `/qqsk/globalTreasurer/regulatory/authority/type/${authorityType}`,
    method: 'get'
  })
}

// 查询活跃的监管机构
export function getActiveAuthorities() {
  return request({
    url: '/qqsk/globalTreasurer/regulatory/authority/active',
    method: 'get'
  })
}

// 查询重要监管机构
export function getImportantAuthorities() {
  return request({
    url: '/qqsk/globalTreasurer/regulatory/authority/important',
    method: 'get'
  })
}

// 激活/停用监管机构
export function toggleAuthorityStatus(authorityId, isActive) {
  return request({
    url: `/qqsk/globalTreasurer/regulatory/authority/${authorityId}/status/${isActive}`,
    method: 'put'
  })
}

// 导出监管机构数据
export function exportAuthorityData(query) {
  return request({
    url: '/qqsk/globalTreasurer/regulatory/authority/export',
    method: 'post',
    data: query,
    responseType: 'blob',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

// ==================== 报告模板管理 ====================

// 分页查询报告模板
export function getTemplateList(query) {
  return request({
    url: '/qqsk/globalTreasurer/regulatory/template/list',
    method: 'get',
    params: query
  })
}

// 根据ID查询报告模板
export function getTemplateById(templateId) {
  return request({
    url: `/qqsk/globalTreasurer/regulatory/template/${templateId}`,
    method: 'get'
  })
}

// 新增报告模板
export function addTemplate(data) {
  return request({
    url: '/qqsk/globalTreasurer/regulatory/template',
    method: 'post',
    data: data,
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

// 修改报告模板
export function updateTemplate(data) {
  return request({
    url: '/qqsk/globalTreasurer/regulatory/template',
    method: 'put',
    data: data,
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

// 删除报告模板
export function delTemplate(templateIds) {
  return request({
    url: `/qqsk/globalTreasurer/regulatory/template/${templateIds}`,
    method: 'delete'
  })
}

// 根据监管机构查询模板
export function getTemplatesByAuthority(authorityId) {
  return request({
    url: `/qqsk/globalTreasurer/regulatory/template/authority/${authorityId}`,
    method: 'get'
  })
}

// 查询可使用的模板
export function getUsableTemplates() {
  return request({
    url: '/qqsk/globalTreasurer/regulatory/template/usable',
    method: 'get'
  })
}

// 查询即将过期的模板
export function getExpiringSoonTemplates(days) {
  return request({
    url: `/qqsk/globalTreasurer/regulatory/template/expiring/${days}`,
    method: 'get'
  })
}

// 启用/停用报告模板
export function toggleTemplateStatus(templateId, isEnabled) {
  return request({
    url: `/qqsk/globalTreasurer/regulatory/template/${templateId}/status/${isEnabled}`,
    method: 'put'
  })
}

// 复制报告模板
export function copyTemplate(sourceTemplateId, newTemplateCode, newTemplateName) {
  return request({
    url: `/qqsk/globalTreasurer/regulatory/template/${sourceTemplateId}/copy`,
    method: 'post',
    params: {
      newTemplateCode: newTemplateCode,
      newTemplateName: newTemplateName
    }
  })
}

// 创建模板新版本
export function createTemplateVersion(sourceTemplateId, newVersion) {
  return request({
    url: `/qqsk/globalTreasurer/regulatory/template/${sourceTemplateId}/version`,
    method: 'post',
    params: {
      newVersion: newVersion
    }
  })
}

// ==================== 监管报告管理 ====================

// 分页查询监管报告
export function getReportList(query) {
  return request({
    url: '/qqsk/globalTreasurer/regulatory/report/list',
    method: 'get',
    params: query
  })
}

// 根据ID查询监管报告
export function getReportById(reportId) {
  return request({
    url: `/qqsk/globalTreasurer/regulatory/report/${reportId}`,
    method: 'get'
  })
}

// 新增监管报告
export function addReport(data) {
  return request({
    url: '/qqsk/globalTreasurer/regulatory/report',
    method: 'post',
    data: data
  })
}

// 修改监管报告
export function updateReport(data) {
  return request({
    url: '/qqsk/globalTreasurer/regulatory/report',
    method: 'put',
    data: data
  })
}

// 删除监管报告
export function delReport(reportIds) {
  return request({
    url: `/qqsk/globalTreasurer/regulatory/report/${reportIds}`,
    method: 'delete'
  })
}

// 查询逾期报告
export function getOverdueReports() {
  return request({
    url: '/qqsk/globalTreasurer/regulatory/report/overdue',
    method: 'get'
  })
}

// 查询即将到期的报告
export function getDueSoonReports(days) {
  return request({
    url: `/qqsk/globalTreasurer/regulatory/report/due-soon/${days}`,
    method: 'get'
  })
}

// 查询需要关注的报告
export function getReportsNeedingAttention() {
  return request({
    url: '/qqsk/globalTreasurer/regulatory/report/attention',
    method: 'get'
  })
}

// 生成报告
export function generateReport(reportId) {
  return request({
    url: `/qqsk/globalTreasurer/regulatory/report/${reportId}/generate`,
    method: 'post'
  })
}

// 验证报告
export function validateReport(reportId) {
  return request({
    url: `/qqsk/globalTreasurer/regulatory/report/${reportId}/validate`,
    method: 'post'
  })
}

// 提交报告
export function submitReport(reportId, submissionMethod) {
  return request({
    url: `/qqsk/globalTreasurer/regulatory/report/${reportId}/submit`,
    method: 'post',
    params: {
      submissionMethod: submissionMethod
    }
  })
}

// 接受报告
export function acceptReport(reportId, acknowledgmentNo) {
  return request({
    url: `/qqsk/globalTreasurer/regulatory/report/${reportId}/accept`,
    method: 'post',
    params: {
      acknowledgmentNo: acknowledgmentNo
    }
  })
}

// 拒绝报告
export function rejectReport(reportId, rejectReason) {
  return request({
    url: `/qqsk/globalTreasurer/regulatory/report/${reportId}/reject`,
    method: 'post',
    params: {
      rejectReason: rejectReason
    }
  })
}

// 获取报告统计信息
export function getReportStatistics() {
  return request({
    url: '/qqsk/globalTreasurer/regulatory/report/statistics',
    method: 'get'
  })
}

// 获取报告趋势分析
export function getReportTrends(startDate, endDate) {
  return request({
    url: '/qqsk/globalTreasurer/regulatory/report/trends',
    method: 'get',
    params: {
      startDate: startDate,
      endDate: endDate
    }
  })
}

// 批量生成报告
export function batchGenerateReports(reportIds) {
  return request({
    url: '/qqsk/globalTreasurer/regulatory/report/batch-generate',
    method: 'post',
    data: reportIds,
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

// 批量提交报告
export function batchSubmitReports(reportIds, submissionMethod) {
  return request({
    url: '/qqsk/globalTreasurer/regulatory/report/batch-submit',
    method: 'post',
    data: reportIds,
    params: {
      submissionMethod: submissionMethod
    },
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

// 复制报告
export function copyReport(sourceReportId, newReportNo, newReportName) {
  return request({
    url: `/qqsk/globalTreasurer/regulatory/report/${sourceReportId}/copy`,
    method: 'post',
    params: {
      newReportNo: newReportNo,
      newReportName: newReportName
    }
  })
}

// 获取报告下载链接
export function getReportDownloadUrl(reportId) {
  return request({
    url: `/qqsk/globalTreasurer/regulatory/report/${reportId}/download`,
    method: 'get'
  })
}

// 预览报告内容
export function previewReport(reportId) {
  return request({
    url: `/qqsk/globalTreasurer/regulatory/report/${reportId}/preview`,
    method: 'get'
  })
}

// ==================== 合规检查规则管理 ====================

// 分页查询合规检查规则
export function getRuleList(query) {
  return request({
    url: '/qqsk/globalTreasurer/regulatory/rule/list',
    method: 'get',
    params: query
  })
}

// 分页查询合规检查规则(别名)
export function getComplianceRuleList(query) {
  return request({
    url: '/qqsk/globalTreasurer/regulatory/rule/list',
    method: 'get',
    params: query
  })
}

// 根据ID查询合规检查规则
export function getRuleById(ruleId) {
  return request({
    url: `/qqsk/globalTreasurer/regulatory/rule/${ruleId}`,
    method: 'get'
  })
}

// 新增合规检查规则
export function addRule(data) {
  return request({
    url: '/qqsk/globalTreasurer/regulatory/rule',
    method: 'post',
    headers: { 'Content-Type': 'application/json' },
    data: data
  })
}

// 修改合规检查规则
export function updateRule(data) {
  return request({
    url: '/qqsk/globalTreasurer/regulatory/rule',
    method: 'put',
    headers: { 'Content-Type': 'application/json' },
    data: data
  })
}

// 删除合规检查规则
export function delRule(ruleIds) {
  return request({
    url: `/qqsk/globalTreasurer/regulatory/rule/${ruleIds}`,
    method: 'delete'
  })
}

// 查询可执行的规则
export function getExecutableRules() {
  return request({
    url: '/qqsk/globalTreasurer/regulatory/rule/executable',
    method: 'get'
  })
}

// 启用/停用合规检查规则
export function toggleRuleStatus(ruleId, isEnabled) {
  return request({
    url: `/qqsk/globalTreasurer/regulatory/rule/${ruleId}/status/${isEnabled}`,
    method: 'put'
  })
}

// 执行合规检查
export function executeComplianceCheck(ruleId, checkData) {
  return request({
    url: `/qqsk/globalTreasurer/regulatory/rule/${ruleId}/execute`,
    method: 'post',
    data: checkData
  })
}

// 根据ID查询合规检查规则(别名)
export function getComplianceRuleById(ruleId) {
  return getRuleById(ruleId)
}

// 新增合规检查规则(别名)
export function addComplianceRule(data) {
  return addRule(data)
}

// 修改合规检查规则(别名)
export function updateComplianceRule(data) {
  return updateRule(data)
}

// 删除合规检查规则(别名)
export function delComplianceRule(ruleIds) {
  return delRule(ruleIds)
}

// 启用/停用合规检查规则(别名)
export function toggleComplianceRuleStatus(ruleId, isEnabled) {
  return toggleRuleStatus(ruleId, isEnabled)
}

// 执行合规检查规则(别名)
export function executeComplianceRule(ruleId, checkData) {
  return executeComplianceCheck(ruleId, checkData)
}

// 批量执行合规检查规则
export function batchExecuteComplianceRules(ruleIds) {
  return request({
    url: '/qqsk/globalTreasurer/regulatory/rule/batch-execute',
    method: 'post',
    headers: { 'Content-Type': 'application/json' },
    data: ruleIds
  })
}

// 复制合规检查规则
export function copyComplianceRule(ruleId) {
  return request({
    url: `/qqsk/globalTreasurer/regulatory/rule/${ruleId}/copy`,
    method: 'post'
  })
}

// 导出合规检查规则数据
export function exportComplianceRuleData(query) {
  return request({
    url: '/qqsk/globalTreasurer/regulatory/rule/export',
    method: 'post',
    data: query,
    responseType: 'blob'
  })
}

// ==================== 合规检查结果管理 ====================

// 分页查询合规检查结果
export function getResultList(query) {
  return request({
    url: '/qqsk/globalTreasurer/regulatory/result/list',
    method: 'get',
    params: query
  })
}

// 分页查询合规检查结果(别名)
export function getComplianceResultList(query) {
  return request({
    url: '/qqsk/globalTreasurer/regulatory/result/list',
    method: 'get',
    params: query
  })
}

// 根据ID查询合规检查结果
export function getResultById(resultId) {
  return request({
    url: `/qqsk/globalTreasurer/regulatory/result/${resultId}`,
    method: 'get'
  })
}

// 新增合规检查结果
export function addResult(data) {
  return request({
    url: '/qqsk/globalTreasurer/regulatory/result',
    method: 'post',
    data: data
  })
}

// 修改合规检查结果
export function updateResult(data) {
  return request({
    url: '/qqsk/globalTreasurer/regulatory/result',
    method: 'put',
    data: data
  })
}

// 删除合规检查结果
export function delResult(resultIds) {
  return request({
    url: `/qqsk/globalTreasurer/regulatory/result/${resultIds}`,
    method: 'delete'
  })
}

// 查询需要关注的检查结果
export function getResultsNeedingAttention() {
  return request({
    url: '/qqsk/globalTreasurer/regulatory/result/attention',
    method: 'get'
  })
}

// 查询需要立即处理的检查结果
export function getResultsNeedingImmediateAction() {
  return request({
    url: '/qqsk/globalTreasurer/regulatory/result/immediate',
    method: 'get'
  })
}

// 处理合规检查结果
export function processResult(resultId, actionTaken) {
  return request({
    url: `/qqsk/globalTreasurer/regulatory/result/${resultId}/process`,
    method: 'post',
    params: {
      actionTaken: actionTaken
    }
  })
}

// 解决合规问题
export function resolveResult(resultId) {
  return request({
    url: `/qqsk/globalTreasurer/regulatory/result/${resultId}/resolve`,
    method: 'post'
  })
}

// 升级合规问题
export function escalateResult(resultId) {
  return request({
    url: `/qqsk/globalTreasurer/regulatory/result/${resultId}/escalate`,
    method: 'post'
  })
}

// 根据ID查询合规检查结果(别名)
export function getComplianceResultById(resultId) {
  return getResultById(resultId)
}

// 处理合规检查结果(别名)
export function processComplianceResult(resultId, actionTaken) {
  return processResult(resultId, actionTaken)
}

// 解决合规检查结果(别名)
export function resolveComplianceResult(resultId) {
  return resolveResult(resultId)
}

// 升级合规检查结果(别名)
export function escalateComplianceResult(resultId) {
  return escalateResult(resultId)
}

// 批量处理合规检查结果
export function batchProcessComplianceResults(resultIds) {
  return request({
    url: '/qqsk/globalTreasurer/regulatory/result/batch-process',
    method: 'post',
    headers: { 'Content-Type': 'application/json' },
    data: resultIds
  })
}

// 批量解决合规检查结果
export function batchResolveComplianceResults(resultIds) {
  return request({
    url: '/qqsk/globalTreasurer/regulatory/result/batch-resolve',
    method: 'post',
    headers: { 'Content-Type': 'application/json' },
    data: resultIds
  })
}

// 批量升级合规检查结果
export function batchEscalateComplianceResults(resultIds) {
  return request({
    url: '/qqsk/globalTreasurer/regulatory/result/batch-escalate',
    method: 'post',
    headers: { 'Content-Type': 'application/json' },
    data: resultIds
  })
}

// 导出合规检查结果数据
export function exportComplianceResultData(query) {
  return request({
    url: '/qqsk/globalTreasurer/regulatory/result/export',
    method: 'post',
    data: query,
    responseType: 'blob'
  })
}

// 获取合规检查统计信息
export function getComplianceStatistics() {
  return request({
    url: '/qqsk/globalTreasurer/regulatory/result/statistics',
    method: 'get'
  })
}

// 获取合规检查趋势分析
export function getComplianceTrends(startDate, endDate) {
  return request({
    url: '/qqsk/globalTreasurer/regulatory/result/trends',
    method: 'get',
    params: {
      startDate: startDate,
      endDate: endDate
    }
  })
}

// 导出监管报告数据
export function exportReportData(query) {
  return request({
    url: '/qqsk/globalTreasurer/regulatory/report/export',
    method: 'post',
    data: query,
    responseType: 'blob'
  })
}

// 撤回报告
export function recallReport(reportId) {
  return request({
    url: `/qqsk/globalTreasurer/regulatory/report/${reportId}/recall`,
    method: 'post'
  })
}

// 获取报告操作历史
export function getReportHistory(reportId) {
  return request({
    url: `/qqsk/globalTreasurer/regulatory/report/${reportId}/history`,
    method: 'get'
  })
}

// ==================== 监管数据字典管理 ====================

// 分页查询数据字典
export function getDictionaryList(query) {
  return request({
    url: '/qqsk/globalTreasurer/regulatory/dictionary/list',
    method: 'get',
    params: query
  })
}

// 根据ID查询数据字典
export function getDictionaryById(dictionaryId) {
  return request({
    url: `/qqsk/globalTreasurer/regulatory/dictionary/${dictionaryId}`,
    method: 'get'
  })
}

// 新增数据字典
export function addDictionary(data) {
  return request({
    url: '/qqsk/globalTreasurer/regulatory/dictionary',
    method: 'post',
    data: data
  })
}

// 修改数据字典
export function updateDictionary(data) {
  return request({
    url: '/qqsk/globalTreasurer/regulatory/dictionary',
    method: 'put',
    data: data
  })
}

// 删除数据字典
export function delDictionary(dictionaryIds) {
  return request({
    url: `/qqsk/globalTreasurer/regulatory/dictionary/${dictionaryIds}`,
    method: 'delete'
  })
}

// ==================== 报送任务管理 ====================

// 分页查询报送任务
export function getTaskList(query) {
  return request({
    url: '/qqsk/globalTreasurer/regulatory/task/list',
    method: 'get',
    params: query
  })
}

// 根据ID查询报送任务
export function getTaskById(taskId) {
  return request({
    url: `/qqsk/globalTreasurer/regulatory/task/${taskId}`,
    method: 'get'
  })
}

// 新增报送任务
export function addTask(data) {
  return request({
    url: '/qqsk/globalTreasurer/regulatory/task',
    method: 'post',
    data: data
  })
}

// 修改报送任务
export function updateTask(data) {
  return request({
    url: '/qqsk/globalTreasurer/regulatory/task',
    method: 'put',
    data: data
  })
}

// 删除报送任务
export function delTask(taskIds) {
  return request({
    url: `/qqsk/globalTreasurer/regulatory/task/${taskIds}`,
    method: 'delete'
  })
}

// 执行报送任务
export function executeTask(taskId) {
  return request({
    url: `/qqsk/globalTreasurer/regulatory/task/${taskId}/execute`,
    method: 'post'
  })
}

// 暂停报送任务
export function pauseTask(taskId) {
  return request({
    url: `/qqsk/globalTreasurer/regulatory/task/${taskId}/pause`,
    method: 'post'
  })
}

// 恢复报送任务
export function resumeTask(taskId) {
  return request({
    url: `/qqsk/globalTreasurer/regulatory/task/${taskId}/resume`,
    method: 'post'
  })
}

// 取消报送任务
export function cancelTask(taskId) {
  return request({
    url: `/qqsk/globalTreasurer/regulatory/task/${taskId}/cancel`,
    method: 'post'
  })
}

// ==================== 监管通知管理 ====================

// 分页查询监管通知
export function getNotificationList(query) {
  return request({
    url: '/qqsk/globalTreasurer/regulatory/notification/list',
    method: 'get',
    params: query
  })
}

// 根据ID查询监管通知
export function getNotificationById(notificationId) {
  return request({
    url: `/qqsk/globalTreasurer/regulatory/notification/${notificationId}`,
    method: 'get'
  })
}

// 新增监管通知
export function addNotification(data) {
  return request({
    url: '/qqsk/globalTreasurer/regulatory/notification',
    method: 'post',
    data: data
  })
}

// 修改监管通知
export function updateNotification(data) {
  return request({
    url: '/qqsk/globalTreasurer/regulatory/notification',
    method: 'put',
    data: data
  })
}

// 删除监管通知
export function delNotification(notificationIds) {
  return request({
    url: `/qqsk/globalTreasurer/regulatory/notification/${notificationIds}`,
    method: 'delete'
  })
}

// 发送通知
export function sendNotification(notificationId) {
  return request({
    url: `/qqsk/globalTreasurer/regulatory/notification/${notificationId}/send`,
    method: 'post'
  })
}

// 确认通知
export function acknowledgeNotification(notificationId) {
  return request({
    url: `/qqsk/globalTreasurer/regulatory/notification/${notificationId}/acknowledge`,
    method: 'post'
  })
}
