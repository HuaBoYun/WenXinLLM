import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// ==================== 数据录入 ====================

export function getDataEntryList(data) {
  return request({
    url: '/monitor/v1/enterprise/data/entry/list',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

export function getDataEntryById(id) {
  return request({
    url: '/monitor/v1/enterprise/data/entry/' + id,
    method: 'get'
  })
}

export function addDataEntry(data) {
  return request({
    url: '/monitor/v1/enterprise/data/entry/add',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

export function updateDataEntry(data) {
  return request({
    url: '/monitor/v1/enterprise/data/entry/update',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

export function deleteDataEntry(id) {
  return request({
    url: '/monitor/v1/enterprise/data/entry/' + id,
    method: 'delete',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

export function batchDeleteDataEntry(data) {
  return request({
    url: '/monitor/v1/enterprise/data/entry/batch-delete',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

export function batchSubmitData(data) {
  return request({
    url: '/monitor/v1/enterprise/data/entry/batch-submit',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

export function batchAuditData(data) {
  return request({
    url: '/monitor/v1/enterprise/data/audit/batch-audit',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

export function submitDataEntry(data) {
  return request({
    url: '/monitor/v1/enterprise/data/entry/submit',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

export function auditDataEntry(data) {
  return request({
    url: '/monitor/v1/enterprise/data/entry/audit',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

export function exportDataEntry() {
  return request({
    url: '/monitor/v1/enterprise/data/entry/export',
    method: 'get',
    responseType: 'blob'
  })
}

export function getDataEntryStatistics() {
  return request({
    url: '/monitor/v1/enterprise/data/entry/statistics',
    method: 'get'
  })
}

// ==================== 数据质量 ====================

export function getDataQualityList(data) {
  return request({
    url: '/monitor/v1/enterprise/data/quality/list',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

export function getDataQualityById(id) {
  return request({
    url: '/monitor/v1/enterprise/data/quality/' + id,
    method: 'get'
  })
}

export function deleteDataQuality(id) {
  return request({
    url: '/monitor/v1/enterprise/data/quality/' + id,
    method: 'delete',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

export function addDataQuality(data) {
  return request({
    url: '/monitor/v1/enterprise/data/quality/add',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

export function runDataQualityCheck(data) {
  return request({
    url: '/monitor/v1/enterprise/data/quality/check',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

export function exportDataQuality() {
  return request({
    url: '/monitor/v1/enterprise/data/quality/export',
    method: 'get',
    responseType: 'blob'
  })
}

export function getDataQualityStatistics() {
  return request({
    url: '/monitor/v1/enterprise/data/quality/statistics',
    method: 'get'
  })
}

// ==================== 数据导出 ====================

export function exportData(data) {
  return request({
    url: '/monitor/v1/enterprise/data/export',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    responseType: 'blob'
  })
}

// ==================== 数据导入 ====================

export function importData(data) {
  return request({
    url: '/monitor/v1/enterprise/data/import',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

export function importPreview(data) {
  return request({
    url: '/monitor/v1/enterprise/data/import/preview',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

// ==================== 数据历史 ====================

export function getDataHistoryList(data) {
  return request({
    url: '/monitor/v1/enterprise/data/history/list',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

// ==================== 组织架构 ====================

export function getOrganizationChart(data) {
  return request({
    url: '/monitor/v1/enterprise/data/organization/chart',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

// ==================== 提交进度 ====================

export function getSubmissionProgress(data) {
  return request({
    url: '/monitor/v1/enterprise/data/entry/progress',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

export function getSubmissionLogs(data) {
  return request({
    url: '/monitor/v1/enterprise/data/entry/progress/logs',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

export function downloadSubmissionLog(id) {
  return request({
    url: '/monitor/v1/enterprise/data/entry/progress/logs/download/' + id,
    method: 'get',
    responseType: 'blob'
  })
}

// ==================== 页面统计 ====================

export function getDataStatistics(data) {
  return request({
    url: '/monitor/v1/enterprise/data/entry/statistics',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}


// ========== 数据报送 ==========
// 报送列表：只显示草稿和已退回状态（待报送的数据）
export function getDataSubmissionList(data) {
  return request({
    url: '/monitor/v1/enterprise/data/entry/list',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

export function deleteDataSubmission(id) {
  return request({
    url: '/monitor/v1/enterprise/data/submission/' + id,
    method: 'delete',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

export function submitDataSubmission(data) {
  return request({
    url: '/monitor/v1/enterprise/data/entry/submit',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

export function withdrawDataSubmission(data) {
  return request({
    url: '/monitor/v1/enterprise/data/entry/withdraw',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}


// ========== 数据审核 ==========
// 审核列表：只显示已提交状态（待审核的数据）
export function getDataAuditList(data) {
  return request({
    url: '/monitor/v1/enterprise/data/entry/list',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

export function approveDataAudit(data) {
  return request({
    url: '/monitor/v1/enterprise/data/entry/audit',
    method: 'post',
    data: transData({ ...data, result: 'approved' }),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

export function rejectDataAudit(data) {
  return request({
    url: '/monitor/v1/enterprise/data/entry/audit',
    method: 'post',
    data: transData({ ...data, result: 'rejected' }),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

export function deleteDataAudit(id) {
  return request({
    url: '/monitor/v1/enterprise/data/audit/' + id,
    method: 'delete',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

export function getAuditWorkflow(id) {
  return request({
    url: '/monitor/v1/enterprise/data/audit/workflow/' + id,
    method: 'get'
  })
}

export function getAuditHistory(id) {
  return request({
    url: '/monitor/v1/enterprise/data/audit/history/' + id,
    method: 'get'
  })
}


// ========== 组织架构 ==========
export const organizationStructureApi = {
  getStatistics(enterpriseId) {
    return request({
      url: '/monitor/v1/enterprise/data/entry/statistics',
      method: 'get'
    })
  },
  getOrganizationTree(enterpriseId) {
    return request({
      url: '/monitor/v1/enterprise/hr/department/list',
      method: 'post',
      data: transData({ enterpriseId }),
      headers: { 'Content-Type': 'application/json;charset=UTF-8' }
    })
  },
  getManagementSpanAnalysis(enterpriseId) {
    return request({
      url: '/monitor/v1/enterprise/hr/department/list',
      method: 'post',
      data: transData({ enterpriseId }),
      headers: { 'Content-Type': 'application/json;charset=UTF-8' }
    })
  },
  getOrganizationTypeDistribution(enterpriseId) {
    return request({
      url: '/monitor/v1/enterprise/hr/department/list',
      method: 'post',
      data: transData({ enterpriseId }),
      headers: { 'Content-Type': 'application/json;charset=UTF-8' }
    })
  },
  getStaffDistribution(enterpriseId) {
    return request({
      url: '/monitor/v1/enterprise/hr/employee/list',
      method: 'post',
      data: transData({ enterpriseId }),
      headers: { 'Content-Type': 'application/json;charset=UTF-8' }
    })
  },
  deleteOrganization(orgId) {
    return request({
      url: '/monitor/v1/enterprise/hr/department/' + orgId,
      method: 'delete'
    })
  }
}


// ========== 数据质量扩展 ==========
export function performDataQualityCheck(data) {
  return request({
    url: '/monitor/v1/enterprise/data/quality/check',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

export function cleanData(data) {
  return request({
    url: '/monitor/v1/enterprise/data/quality/check',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

export function getDataQualityReport(data) {
  return request({
    url: '/monitor/v1/enterprise/data/quality/list',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

export function getDataTemplate(templateType) {
  return request({
    url: '/monitor/v1/enterprise/data/entry/export',
    method: 'get',
    responseType: 'blob'
  })
}

export function validateDataFormat(data) {
  return request({
    url: '/monitor/v1/enterprise/data/quality/check',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

export function submitDataAudit(data) {
  return request({
    url: '/monitor/v1/enterprise/data/entry/audit',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

// ==================== 数据验证 ====================
export function validateDataEntry(data) {
  return request({
    url: '/monitor/v1/enterprise/data/entry/validate',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

// ==================== 数据历史 ====================
export function getDataEntryHistory(data) {
  return request({
    url: '/monitor/v1/enterprise/data/entry/history',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

export function exportDataHistory(data) {
  return request({
    url: '/monitor/v1/enterprise/data/entry/history/export',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    responseType: 'blob'
  })
}

// ==================== 复制录入 ====================
export function copyDataEntry(data) {
  return request({
    url: '/monitor/v1/enterprise/data/entry/copy',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

// ==================== 文件上传 ====================
export function uploadFile(file) {
  const formData = new FormData()
  formData.append('file', file)
  return request({
    url: '/monitor/v1/enterprise/data/import/upload',
    method: 'post',
    data: formData,
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

// ==================== 下载模板 ====================
export function downloadTemplate(templateType) {
  return request({
    url: '/monitor/v1/enterprise/data/template/download',
    method: 'get',
    params: { type: templateType },
    responseType: 'blob'
  })
}

// ==================== 数据清洗 ====================
export function performDataClean(data) {
  return request({
    url: '/monitor/v1/enterprise/data/quality/clean',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

// ==================== 质量报告 ====================
export function generateQualityReport(data) {
  return request({
    url: '/monitor/v1/enterprise/data/quality/report',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

// ==================== 数据备份 ====================
export function getDataBackupList(data) {
  return request({
    url: '/monitor/v1/enterprise/data/backup/list',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

export function createDataBackup(data) {
  return request({
    url: '/monitor/v1/enterprise/data/backup/create',
    method: 'post',
    data: transData(data),
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

export function restoreDataBackup(id) {
  return request({
    url: '/monitor/v1/enterprise/data/backup/restore/' + id,
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

export function deleteDataBackup(id) {
  return request({
    url: '/monitor/v1/enterprise/data/backup/' + id,
    method: 'delete',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

export function exportBackupList(enterpriseId) {
  return request({
    url: '/monitor/v1/enterprise/data/backup/export',
    method: 'get',
    params: { enterpriseId },
    responseType: 'blob'
  })
}

// ==================== 数据统计 ====================
export function getDataTypeDistribution(enterpriseId) {
  return request({
    url: '/monitor/v1/enterprise/data/statistics/type-distribution',
    method: 'get',
    params: { enterpriseId }
  })
}

export function getDataTrend(enterpriseId) {
  return request({
    url: '/monitor/v1/enterprise/data/statistics/trend',
    method: 'get',
    params: { enterpriseId }
  })
}

export function getQualityTrend(enterpriseId) {
  return request({
    url: '/monitor/v1/enterprise/data/statistics/quality-trend',
    method: 'get',
    params: { enterpriseId }
  })
}

export function getDashboardStatistics(enterpriseId) {
  return request({
    url: '/monitor/v1/enterprise/data/statistics/dashboard',
    method: 'get',
    params: { enterpriseId }
  })
}
