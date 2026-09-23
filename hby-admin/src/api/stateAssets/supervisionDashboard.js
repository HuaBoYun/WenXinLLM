import request from '@/utils/request'

// ========== 监管驾驶舱 ==========
export function getDashboardOverview(companyId) {
  return request({
    url: '/monitor/v1/supervision/dashboard/overview',
    method: 'get',
    params: { companyId },
  })
}

export function getDomainStatistics(domainType, companyId) {
  return request({
    url: '/monitor/v1/supervision/dashboard/domain/' + domainType,
    method: 'get',
    params: { companyId },
  })
}

