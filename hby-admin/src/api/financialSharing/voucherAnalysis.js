import request from '@/utils/request'

// 凭证汇总统计
export function getVoucherSummary(data) {
  return request({
    url: '/cwgxAi/voucher-analysis/summary',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data: data
  })
}

// 凭证数量统计(按状态)
export function getVoucherCountByStatus(data) {
  return request({
    url: '/cwgxAi/voucher-analysis/count-by-status',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data: data
  })
}

// 凭证数量统计(按期间)
export function getVoucherCountByPeriod(data) {
  return request({
    url: '/cwgxAi/voucher-analysis/count-by-period',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data: data
  })
}

// 凭证数量统计(按凭证类型)
export function getVoucherCountByType(data) {
  return request({
    url: '/cwgxAi/voucher-analysis/count-by-type',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data: data
  })
}

// 凭证金额统计
export function getVoucherAmountStatistics(data) {
  return request({
    url: '/cwgxAi/voucher-analysis/amount-statistics',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data: data
  })
}

// 凭证生成趋势分析
export function getVoucherGenerationTrend(data) {
  return request({
    url: '/cwgxAi/voucher-analysis/generation-trend',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data: data
  })
}

// 凭证处理效率分析
export function getVoucherEfficiencyAnalysis(data) {
  return request({
    url: '/cwgxAi/voucher-analysis/efficiency-analysis',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data: data
  })
}

// 异常凭证分析
export function getAbnormalVoucherAnalysis(data) {
  return request({
    url: '/cwgxAi/voucher-analysis/abnormal-analysis',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data: data
  })
}

// 科目使用频次统计
export function getSubjectUsageStatistics(data) {
  return request({
    url: '/cwgxAi/voucher-analysis/subject-usage',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data: data
  })
}

// 凭证复核质量分析
export function getVoucherReviewQualityAnalysis(data) {
  return request({
    url: '/cwgxAi/voucher-analysis/review-quality',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data: data
  })
}