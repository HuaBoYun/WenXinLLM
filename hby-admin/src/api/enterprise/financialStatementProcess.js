import request from '@/utils/request'

const JSON_HEADERS = { 'Content-Type': 'application/json;charset=UTF-8' }

// 分页查询财务报表编制流程
export function getFinancialStatementProcessPage(data) {
  return request({
    url: '/monitor/v1/enterprise/financial/statement-process/list',
    method: 'post',
    data,
    headers: JSON_HEADERS
  })
}

// 根据ID获取财务报表编制流程详情
export function getFinancialStatementProcessById(processId) {
  return request({
    url: `/monitor/v1/enterprise/financial/statement-process/${processId}`,
    method: 'get',
    headers: JSON_HEADERS
  })
}

// 新增财务报表编制流程
export function addFinancialStatementProcess(data) {
  return request({
    url: '/monitor/v1/enterprise/financial/statement-process/add',
    method: 'post',
    data,
    headers: JSON_HEADERS
  })
}

// 修改财务报表编制流程
export function updateFinancialStatementProcess(data) {
  return request({
    url: '/monitor/v1/enterprise/financial/statement-process/update',
    method: 'put',
    data,
    headers: JSON_HEADERS
  })
}

// 删除财务报表编制流程
export function deleteFinancialStatementProcess(processId, updateBy) {
  return request({
    url: `/monitor/v1/enterprise/financial/statement-process/${processId}`,
    method: 'delete',
    params: { updateBy },
    headers: JSON_HEADERS
  })
}

// 批量删除财务报表编制流程
export function batchDeleteFinancialStatementProcess(processIds, updateBy) {
  return request({
    url: '/monitor/v1/enterprise/financial/statement-process/batchDelete',
    method: 'delete',
    data: processIds,
    params: { updateBy },
    headers: JSON_HEADERS
  })
}

// 启动编制流程
export function startCompilationProcess(processId, updateBy) {
  return request({
    url: `/monitor/v1/enterprise/financial/statement-process/start/${processId}`,
    method: 'put',
    params: { updateBy },
    headers: JSON_HEADERS
  })
}

// 暂停编制流程
export function pauseCompilationProcess(processId, updateBy) {
  return request({
    url: `/monitor/v1/enterprise/financial/statement-process/pause/${processId}`,
    method: 'put',
    params: { updateBy },
    headers: JSON_HEADERS
  })
}

// 恢复编制流程
export function resumeCompilationProcess(processId, updateBy) {
  return request({
    url: `/monitor/v1/enterprise/financial/statement-process/resume/${processId}`,
    method: 'put',
    params: { updateBy },
    headers: JSON_HEADERS
  })
}

// 完成编制流程
export function completeCompilationProcess(processId, updateBy) {
  return request({
    url: `/monitor/v1/enterprise/financial/statement-process/complete/${processId}`,
    method: 'put',
    params: { updateBy },
    headers: JSON_HEADERS
  })
}

// 取消编制流程
export function cancelCompilationProcess(processId, updateBy) {
  return request({
    url: `/monitor/v1/enterprise/financial/statement-process/cancel/${processId}`,
    method: 'put',
    params: { updateBy },
    headers: JSON_HEADERS
  })
}

// 提交审核
export function submitForAudit(processId, updateBy) {
  return request({
    url: `/monitor/v1/enterprise/financial/statement-process/submitForAudit/${processId}`,
    method: 'put',
    params: { updateBy },
    headers: JSON_HEADERS
  })
}

// 初审
export function firstAudit(processId, auditor, opinion, auditResult, updateBy) {
  return request({
    url: `/monitor/v1/enterprise/financial/statement-process/firstAudit/${processId}`,
    method: 'put',
    params: { auditor, opinion, auditResult, updateBy },
    headers: JSON_HEADERS
  })
}

// 复审
export function secondAudit(processId, auditor, opinion, auditResult, updateBy) {
  return request({
    url: `/monitor/v1/enterprise/financial/statement-process/secondAudit/${processId}`,
    method: 'put',
    params: { auditor, opinion, auditResult, updateBy },
    headers: JSON_HEADERS
  })
}

// 终审
export function finalAudit(processId, auditor, opinion, auditResult, updateBy) {
  return request({
    url: `/monitor/v1/enterprise/financial/statement-process/finalAudit/${processId}`,
    method: 'put',
    params: { auditor, opinion, auditResult, updateBy },
    headers: JSON_HEADERS
  })
}

// 签字确认
export function signatureConfirmation(processId, signatory, updateBy) {
  return request({
    url: `/monitor/v1/enterprise/financial/statement-process/signatureConfirmation/${processId}`,
    method: 'put',
    params: { signatory, updateBy },
    headers: JSON_HEADERS
  })
}

// 质量控制检查
export function qualityControlCheck(processId, updateBy) {
  return request({
    url: `/monitor/v1/enterprise/financial/statement-process/qualityControlCheck/${processId}`,
    method: 'put',
    params: { updateBy },
    headers: JSON_HEADERS
  })
}

// 问题整改
export function issueCorrection(processId, correctiveMeasures, updateBy) {
  return request({
    url: `/monitor/v1/enterprise/financial/statement-process/issueCorrection/${processId}`,
    method: 'put',
    params: { correctiveMeasures, updateBy },
    headers: JSON_HEADERS
  })
}

// 效果验证
export function effectivenessVerification(processId, effectivenessVerification, updateBy) {
  return request({
    url: `/monitor/v1/enterprise/financial/statement-process/effectivenessVerification/${processId}`,
    method: 'put',
    params: { effectivenessVerification, updateBy },
    headers: JSON_HEADERS
  })
}

// 批量更新编制状态
export function batchUpdateCompilationStatus(processIds, status, updateBy) {
  return request({
    url: '/monitor/v1/enterprise/financial/statement-process/batchUpdateCompilationStatus',
    method: 'put',
    data: processIds,
    params: { status, updateBy },
    headers: JSON_HEADERS
  })
}

// 批量更新审核状态
export function batchUpdateAuditStatus(processIds, status, updateBy) {
  return request({
    url: '/monitor/v1/enterprise/financial/statement-process/batchUpdateAuditStatus',
    method: 'put',
    data: processIds,
    params: { status, updateBy },
    headers: JSON_HEADERS
  })
}

// 批量更新质量控制状态
export function batchUpdateQualityControlStatus(processIds, status, updateBy) {
  return request({
    url: '/monitor/v1/enterprise/financial/statement-process/batchUpdateQualityControlStatus',
    method: 'put',
    data: processIds,
    params: { status, updateBy },
    headers: JSON_HEADERS
  })
}

// 获取财务报表编制流程统计
export function getStatisticsByEnterpriseId(enterpriseId) {
  return request({
    url: `/monitor/v1/enterprise/financial/statement-process/statistics/${enterpriseId}`,
    method: 'get',
    headers: JSON_HEADERS
  })
}

// 获取报表类型分布
export function getStatementTypeDistribution(enterpriseId) {
  return request({
    url: `/monitor/v1/enterprise/financial/statement-process/statementTypeDistribution/${enterpriseId}`,
    method: 'get',
    headers: JSON_HEADERS
  })
}

// 获取编制状态分布
export function getCompilationStatusDistribution(enterpriseId) {
  return request({
    url: `/monitor/v1/enterprise/financial/statement-process/compilationStatusDistribution/${enterpriseId}`,
    method: 'get',
    headers: JSON_HEADERS
  })
}

// 获取审核状态分布
export function getAuditStatusDistribution(enterpriseId) {
  return request({
    url: `/monitor/v1/enterprise/financial/statement-process/auditStatusDistribution/${enterpriseId}`,
    method: 'get',
    headers: JSON_HEADERS
  })
}

// 获取质量评分分布
export function getQualityScoreDistribution(enterpriseId) {
  return request({
    url: `/monitor/v1/enterprise/financial/statement-process/qualityScoreDistribution/${enterpriseId}`,
    method: 'get',
    headers: JSON_HEADERS
  })
}

// 获取编制进度趋势
export function getCompilationProgressTrend(enterpriseId, months = 12) {
  return request({
    url: `/monitor/v1/enterprise/financial/statement-process/compilationProgressTrend/${enterpriseId}`,
    method: 'get',
    params: { months },
    headers: JSON_HEADERS
  })
}

// 获取质量评分趋势
export function getQualityScoreTrend(enterpriseId, months = 12) {
  return request({
    url: `/monitor/v1/enterprise/financial/statement-process/qualityScoreTrend/${enterpriseId}`,
    method: 'get',
    params: { months },
    headers: JSON_HEADERS
  })
}

// 获取问题统计
export function getIssueStatistics(enterpriseId) {
  return request({
    url: `/monitor/v1/enterprise/financial/statement-process/issueStatistics/${enterpriseId}`,
    method: 'get',
    headers: JSON_HEADERS
  })
}

// 获取问题趋势
export function getIssueTrend(enterpriseId, months = 12) {
  return request({
    url: `/monitor/v1/enterprise/financial/statement-process/issueTrend/${enterpriseId}`,
    method: 'get',
    params: { months },
    headers: JSON_HEADERS
  })
}

// 获取部门编制统计
export function getDepartmentCompilationStatistics(enterpriseId) {
  return request({
    url: `/monitor/v1/enterprise/financial/statement-process/departmentCompilationStatistics/${enterpriseId}`,
    method: 'get',
    headers: JSON_HEADERS
  })
}

// 获取负责人编制统计
export function getManagerCompilationStatistics(enterpriseId) {
  return request({
    url: `/monitor/v1/enterprise/financial/statement-process/managerCompilationStatistics/${enterpriseId}`,
    method: 'get',
    headers: JSON_HEADERS
  })
}

// 获取审核人员统计
export function getAuditorStatistics(enterpriseId) {
  return request({
    url: `/monitor/v1/enterprise/financial/statement-process/auditorStatistics/${enterpriseId}`,
    method: 'get',
    headers: JSON_HEADERS
  })
}

// 获取模板版本使用统计
export function getTemplateVersionStatistics(enterpriseId) {
  return request({
    url: `/monitor/v1/enterprise/financial/statement-process/templateVersionStatistics/${enterpriseId}`,
    method: 'get',
    headers: JSON_HEADERS
  })
}

// 获取数据来源统计
export function getDataSourceStatistics(enterpriseId) {
  return request({
    url: `/monitor/v1/enterprise/financial/statement-process/dataSourceStatistics/${enterpriseId}`,
    method: 'get',
    headers: JSON_HEADERS
  })
}

// 获取即将到期的编制任务
export function getUpcomingCompilationTasks(enterpriseId, days = 7) {
  return request({
    url: `/monitor/v1/enterprise/financial/statement-process/upcomingCompilationTasks/${enterpriseId}`,
    method: 'get',
    params: { days },
    headers: JSON_HEADERS
  })
}

// 获取逾期的编制任务
export function getOverdueCompilationTasks(enterpriseId) {
  return request({
    url: `/monitor/v1/enterprise/financial/statement-process/overdueCompilationTasks/${enterpriseId}`,
    method: 'get',
    headers: JSON_HEADERS
  })
}

// 获取待审核的编制任务
export function getPendingAuditTasks(enterpriseId) {
  return request({
    url: `/monitor/v1/enterprise/financial/statement-process/pendingAuditTasks/${enterpriseId}`,
    method: 'get',
    headers: JSON_HEADERS
  })
}

// 获取待整改的编制任务
export function getPendingCorrectionTasks(enterpriseId) {
  return request({
    url: `/monitor/v1/enterprise/financial/statement-process/pendingCorrectionTasks/${enterpriseId}`,
    method: 'get',
    headers: JSON_HEADERS
  })
}

// 导出财务报表编制流程
export function exportFinancialStatementProcess(data) {
  return request({
    url: '/monitor/v1/enterprise/financial/statement-process/export',
    method: 'post',
    data,
    headers: JSON_HEADERS
  })
}

// 生成财务报表编制流程报告
export function generateFinancialStatementProcessReport(enterpriseId, reportType) {
  return request({
    url: `/monitor/v1/enterprise/financial/statement-process/generateReport/${enterpriseId}`,
    method: 'get',
    params: { reportType },
    headers: JSON_HEADERS
  })
}
