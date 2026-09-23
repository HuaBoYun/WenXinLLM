import request from '@/utils/request'
import { downloadFile } from '@/utils/download'

// 科目余额表导出
export function exportAccountBalance(data) {
  return request({
    url: '/cwgxAi/export/account-balance',
    method: 'post',
    data
  })
}

// 总账导出
export function exportGeneralLedger(data) {
  return request({
    url: '/cwgxAi/export/general-ledger',
    method: 'post',
    data
  })
}

// 凭证清单导出
export function exportVoucherList(data) {
  return request({
    url: '/cwgxAi/export/voucher-list',
    method: 'post',
    data
  })
}

// 辅助核算明细导出
export function exportAuxiliaryDetail(data) {
  return request({
    url: '/cwgxAi/export/auxiliary-detail',
    method: 'post',
    data
  })
}

// 明细账导出
export function exportDetailLedger(data) {
  return request({
    url: '/cwgxAi/export/detail-ledger',
    method: 'post',
    data
  })
}

// 日记账导出
export function exportJournal(data) {
  return request({
    url: '/cwgxAi/export/journal',
    method: 'post',
    data
  })
}

// 批量导出
export function batchExport(data) {
  return request({
    url: '/cwgxAi/export/batch',
    method: 'post',
    data
  })
}

// 查询导出任务列表
export function getExportTasks(params) {
  return request({
    url: '/cwgxAi/export/tasks',
    method: 'get',
    params
  })
}

// 查询导出任务详情
export function getExportTaskDetail(taskId) {
  return request({
    url: `/cwgxAi/export/tasks/${taskId}`,
    method: 'get'
  })
}

// 查询导出进度
export function getExportProgress(taskId) {
  return request({
    url: `/cwgxAi/export/progress/${taskId}`,
    method: 'get'
  })
}

// 取消导出任务
export function cancelExportTask(taskId) {
  return request({
    url: `/cwgxAi/export/cancel/${taskId}`,
    method: 'post'
  })
}

// 重新执行导出任务
export function retryExportTask(taskId) {
  return request({
    url: `/cwgxAi/export/retry/${taskId}`,
    method: 'post'
  })
}

// 下载导出文件
export function downloadExportFile(taskId, fileName) {
  const url = `/cwgxAi/export/download/${taskId}`
  downloadFile(url, fileName)
}

// 删除导出任务
export function deleteExportTask(taskId) {
  return request({
    url: `/cwgxAi/export/tasks/${taskId}`,
    method: 'delete'
  })
}

// 获取导出历史统计
export function getExportStatistics(statisticsType) {
  return request({
    url: '/cwgxAi/export/statistics',
    method: 'get',
    params: { statisticsType }
  })
}

// 获取导出模板列表
export function getExportTemplates() {
  return request({
    url: '/cwgxAi/export/templates',
    method: 'get'
  })
}

// ========== 导出参数构建器 ==========

// 构建导出参数
export class ExportParamBuilder {
  constructor() {
    this.params = {
      bookId: null,
      tenantId: null,
      startDate: null,
      endDate: null,
      accountingPeriod: null,
      voucherTypes: [],
      currencyCodes: [],
      includeDetails: false,
      templateId: null,
      exportFormat: 'xlsx'
    }
  }

  setBookId(bookId) {
    this.params.bookId = bookId
    return this
  }

  setTenantId(tenantId) {
    this.params.tenantId = tenantId
    return this
  }

  setDateRange(startDate, endDate) {
    this.params.startDate = startDate
    this.params.endDate = endDate
    return this
  }

  setAccountingPeriod(period) {
    this.params.accountingPeriod = period
    return this
  }

  setVoucherTypes(types) {
    this.params.voucherTypes = types
    return this
  }

  setCurrencies(currencies) {
    this.params.currencyCodes = currencies
    return this
  }

  setIncludeDetails(include) {
    this.params.includeDetails = include
    return this
  }

  setTemplate(templateId) {
    this.params.templateId = templateId
    return this
  }

  setFormat(format) {
    this.params.exportFormat = format
    return this
  }

  build() {
    return { ...this.params }
  }
}

// ========== 导出状态管理 ==========

// 导出任务状态枚举
export const ExportTaskStatus = {
  PENDING: 'PENDING',
  RUNNING: 'RUNNING',
  COMPLETED: 'COMPLETED',
  FAILED: 'FAILED',
  CANCELLED: 'CANCELLED'
}

// 导出状态映射
export const ExportStatusMap = {
  [ExportTaskStatus.PENDING]: '等待中',
  [ExportTaskStatus.RUNNING]: '执行中',
  [ExportTaskStatus.COMPLETED]: '已完成',
  [ExportTaskStatus.FAILED]: '失败',
  [ExportTaskStatus.CANCELLED]: '已取消'
}

// 导出类型映射
export const ExportTypeMap = {
  'ACCOUNT_BALANCE': '科目余额表',
  'GENERAL_LEDGER': '总账',
  'VOUCHER_LIST': '凭证清单',
  'AUXILIARY_DETAIL': '辅助核算明细',
  'DETAIL_LEDGER': '明细账',
  'JOURNAL': '日记账',
  'BATCH_EXPORT': '批量导出'
}

// 获取状态颜色
export function getStatusColor(status) {
  const colorMap = {
    [ExportTaskStatus.PENDING]: '#909399',
    [ExportTaskStatus.RUNNING]: '#409EFF',
    [ExportTaskStatus.COMPLETED]: '#67C23A',
    [ExportTaskStatus.FAILED]: '#F56C6C',
    [ExportTaskStatus.CANCELLED]: '#909399'
  }
  return colorMap[status] || '#909399'
}

// 获取进度百分比
export function getProgressPercentage(task) {
  if (task.status === ExportTaskStatus.COMPLETED) {
    return 100
  } else if (task.status === ExportTaskStatus.FAILED || task.status === ExportTaskStatus.CANCELLED) {
    return 0
  } else {
    return task.progress || 0
  }
}

// ========== 导出工具函数 ==========

// 格式化文件大小
export function formatFileSize(bytes) {
  if (!bytes) return '0 B'

  const k = 1024
  const sizes = ['B', 'KB', 'MB', 'GB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))

  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
}

// 格式化导出时间
export function formatExportTime(time) {
  if (!time) return '-'

  const date = new Date(time)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  })
}

// 计算导出耗时
export function calculateExportDuration(startTime, endTime) {
  if (!startTime) return '-'

  const start = new Date(startTime)
  const end = endTime ? new Date(endTime) : new Date()
  const duration = end - start

  if (duration < 1000) {
    return `${duration}ms`
  } else if (duration < 60000) {
    return `${(duration / 1000).toFixed(1)}s`
  } else {
    return `${(duration / 60000).toFixed(1)}min`
  }
}

// 生成导出文件名
export function generateExportFileName(exportType, suffix = '') {
  const timestamp = new Date().toISOString().slice(0, 19).replace(/[:-]/g, '')
  const typeText = ExportTypeMap[exportType] || exportType
  return `${typeText}_${suffix}${timestamp}.xlsx`
}

// 验证导出参数
export function validateExportParams(params) {
  const errors = []

  if (!params.bookId) {
    errors.push('账簿ID不能为空')
  }

  if (!params.tenantId) {
    errors.push('租户ID不能为空')
  }

  if (params.startDate && params.endDate && params.startDate > params.endDate) {
    errors.push('开始日期不能大于结束日期')
  }

  return {
    isValid: errors.length === 0,
    errors
  }
}

// 创建导出轮询器
export function createExportPoller(taskId, callback, interval = 2000) {
  let timer = null

  const poll = async () => {
    try {
      const result = await getExportProgress(taskId)
      callback(result)

      // 如果任务完成，停止轮询
      if (result.data.status === ExportTaskStatus.COMPLETED ||
          result.data.status === ExportTaskStatus.FAILED ||
          result.data.status === ExportTaskStatus.CANCELLED) {
        clearInterval(timer)
      }
    } catch (error) {
      console.error('轮询导出进度失败:', error)
      clearInterval(timer)
    }
  }

  timer = setInterval(poll, interval)

  // 立即执行一次
  poll()

  // 返回停止轮询的函数
  return () => clearInterval(timer)
}