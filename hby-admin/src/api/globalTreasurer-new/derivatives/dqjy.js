import request from '@/utils/request'

/**
 * 查询掉期交易列表
 * @param {Object} query 查询参数
 * @returns {Promise}
 */
export function listSwapTransaction(query) {
  return request({
    url: '/qqsk/derivatives/swap/list',
    method: 'get',
    params: query
  })
}

/**
 * 查询掉期交易详细
 * @param {Number} transactionId 掉期交易ID
 * @returns {Promise}
 */
export function getSwapTransaction(transactionId) {
  return request({
    url: '/qqsk/derivatives/swap/' + transactionId,
    method: 'get'
  })
}

/**
 * 新增掉期交易
 * @param {Object} data 掉期交易数据
 * @returns {Promise}
 */
export function addSwapTransaction(data) {
  return request({
    url: '/qqsk/derivatives/swap',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

/**
 * 修改掉期交易
 * @param {Object} data 掉期交易数据
 * @returns {Promise}
 */
export function updateSwapTransaction(data) {
  return request({
    url: '/qqsk/derivatives/swap',
    method: 'put',
    data: data,
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

/**
 * 删除掉期交易
 * @param {Array|Number} transactionIds 掉期交易ID数组或单个ID
 * @returns {Promise}
 */
export function delSwapTransaction(transactionIds) {
  const ids = Array.isArray(transactionIds) ? transactionIds : [transactionIds]
  return request({
    url: '/qqsk/derivatives/swap/delete',
    method: 'post',
    data: { transactionIds: ids },
    transformRequest: [function (data) {
      // 覆盖默认的 Content-Type 序列化，强制使用 JSON
      return JSON.stringify(data)
    }],
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

/**
 * 导出掉期交易
 * @param {Object} query 查询参数
 * @returns {Promise}
 */
export function exportSwapTransaction(query) {
  return request({
    url: '/qqsk/derivatives/swap/export',
    method: 'get',
    params: query
  })
}

/**
 * 获取掉期交易现金流
 * @param {Number} transactionId 掉期交易ID
 * @returns {Promise}
 */
export function getCashflow(transactionId) {
  return request({
    url: '/qqsk/derivatives/swap/cashflow/' + transactionId,
    method: 'get'
  })
}

/**
 * 掉期交易估值
 * @param {Number} transactionId 掉期交易ID
 * @returns {Promise}
 */
export function valuationSwapTransaction(transactionId) {
  return request({
    url: '/qqsk/derivatives/swap/valuation/' + transactionId,
    method: 'post'
  })
}

/**
 * 批量估值掉期交易
 * @param {Array} transactionIds 掉期交易ID数组
 * @returns {Promise}
 */
export function batchValuationSwapTransaction(transactionIds) {
  return request({
    url: '/qqsk/derivatives/swap/valuation/batch',
    method: 'post',
    data: { transactionIds: transactionIds },
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

/**
 * 掉期交易支付处理
 * @param {Object} data 支付数据
 * @returns {Promise}
 */
export function paymentSwapTransaction(data) {
  return request({
    url: '/qqsk/derivatives/swap/payment',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

/**
 * 终止掉期交易
 * @param {Number} transactionId 掉期交易ID
 * @returns {Promise}
 */
export function terminateSwapTransaction(transactionId) {
  return request({
    url: '/qqsk/derivatives/swap/terminate/' + transactionId,
    method: 'post'
  })
}

/**
 * 取消掉期交易
 * @param {Number} transactionId 掉期交易ID
 * @returns {Promise}
 */
export function cancelSwapTransaction(transactionId) {
  return request({
    url: '/qqsk/derivatives/swap/cancel/' + transactionId,
    method: 'post'
  })
}
