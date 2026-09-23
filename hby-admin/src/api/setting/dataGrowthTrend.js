import request from '@/utils/request'

/**
 * 获取数据增长趋势
 * @returns {Promise}
 */
export function getDataGrowthTrend() {
  return request({
    url: '/setting/dataGrowthTrend/getList',
    method: 'get',
  })
}

/**
 * 获取风险预警响应率
 * @returns {Promise}
 */
export function getRiskWarningResponse() {
  return request({
    url: '/setting/dataGrowthTrend/getRiskWarningResponse',
    method: 'get',
  })
}

/**
 * 获取基础用户信息列表
 * @returns {Promise}
 */
export function getBasicUserInfo() {
  return request({
    url: '/setting/dataGrowthTrend/getBasicUserInfo',
    method: 'get',
  })
}

/**
 * 获取风险事件处理统计
 * @returns {Promise}
 */
export function getRiskEventHandling() {
  return request({
    url: '/setting/dataGrowthTrend/getRiskEventHandling',
    method: 'get',
  })
}

/**
 * 获取内控测试缺陷程度统计
 * @returns {Promise}
 */
export function getInternalControlDefect() {
  return request({
    url: '/setting/dataGrowthTrend/getInternalControlDefect',
    method: 'get',
  })
}

/**
 * 获取风险、内控、审计相关维度统计
 * @returns {Promise}
 */
export function getRiskAuditDimension() {
  return request({
    url: '/setting/dataGrowthTrend/getRiskAuditDimension',
    method: 'get',
  })
}

/**
 * 获取审计问题数量统计
 * @returns {Promise}
 */
export function getAuditIssueCount() {
  return request({
    url: '/setting/dataGrowthTrend/getAuditIssueCount',
    method: 'get',
  })
}

/**
 * 获取内控缺陷整改跟进统计
 * @returns {Promise}
 */
export function getInternalControlRectification() {
  return request({
    url: '/setting/dataGrowthTrend/getInternalControlRectification',
    method: 'get',
  })
}

/**
 * 获取审计问题整改验证完成率
 * @returns {Promise}
 */
export function getAuditIssueVerificationRate() {
  return request({
    url: '/setting/dataGrowthTrend/getAuditIssueVerificationRate',
    method: 'get',
  })
}
