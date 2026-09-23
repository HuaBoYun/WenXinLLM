import request from '@/utils/request'

// 产品风控管理API

/**
 * 获取产品风险控制列表
 * @param {Object} params - 查询参数 {pageNo, pageSize, riskControlCode, riskControlName, riskLevel, controlType, monitoringStatus}
 * @returns {Promise}
 */
export function getProductRiskControlList(params) {
  return request({
    url: '/qqsk/financial/productRiskControl/getList',
    method: 'post',
    params: params
  })
}

/**
 * 获取产品风险控制详情
 * @param {Number} riskControlId - 风控策略ID
 * @returns {Promise}
 */
export function getProductRiskControlDetail(riskControlId) {
  return request({
    url: '/qqsk/financial/productRiskControl/getDetail',
    method: 'post',
    params: { riskControlId }
  })
}

/**
 * 保存产品风险控制(新增或更新)
 * @param {Object} data - 风控策略数据
 * @returns {Promise}
 */
export function saveProductRiskControl(data) {
  return request({
    url: '/qqsk/financial/productRiskControl/save',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 删除产品风险控制
 * @param {Number} riskControlId - 风控策略ID
 * @returns {Promise}
 */
export function deleteProductRiskControl(riskControlId) {
  return request({
    url: '/qqsk/financial/productRiskControl/delete',
    method: 'post',
    params: { riskControlId }
  })
}

/**
 * 批量删除产品风险控制
 * @param {Array} riskControlIds - 风控策略ID列表
 * @returns {Promise}
 */
export function batchDeleteProductRiskControls(riskControlIds) {
  return request({
    url: '/qqsk/financial/productRiskControl/batchDelete',
    method: 'post',
    data: { riskControlIds },
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 获取风险控制统计信息
 * @returns {Promise}
 */
export function getProductRiskControlStatistics() {
  return request({
    url: '/qqsk/financial/productRiskControl/getStatistics',
    method: 'post'
  })
}

/**
 * 导出产品风险控制报告
 * @param {Object} params - 导出参数
 * @returns {Promise}
 */
export function exportProductRiskControls(params) {
  return request({
    url: '/qqsk/financial/productRiskControl/export',
    method: 'post',
    params,
    responseType: 'blob'
  })
}