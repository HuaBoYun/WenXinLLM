import request from '@/utils/request'

/**
 * 分页查询风险预警列表
 * @param {Object} params - 查询参数 {pageNo, pageSize, alertType, alertLevel, alertStatus, productName, orgId}
 * @returns {Promise}
 */
export function getRiskAlertList(params) {
  return request({
    url: '/qqsk/financial/product-definition/risk-alert/getList',
    method: 'post',
    params: params
  })
}

/**
 * 获取风险预警统计信息
 * @returns {Promise}
 */
export function getRiskAlertStatistics() {
  return request({
    url: '/qqsk/financial/product-definition/risk-alert/getStatistics',
    method: 'post'
  })
}

/**
 * 生成测试数据
 * @param {Object} data - 生成参数 { count: 生成数量 }
 * @returns {Promise}
 */
export function generateMockRiskAlertData(data) {
  return request({
    url: '/qqsk/financial/product-definition/risk-alert/generateMockData',
    method: 'post',
    data: data
  })
}

/**
 * 处理风险预警
 * @param {Object} data - 处理参数 { alertId: 预警ID, alertStatus: 处理状态, handleRemark: 处理备注 }
 * @returns {Promise}
 */
export function processRiskAlert(data) {
  return request({
    url: '/qqsk/financial/product-definition/risk-alert/process',
    method: 'post',
    data: data
  })
}
