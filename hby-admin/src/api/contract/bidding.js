import request from '@/utils/request'

/**
 * 招投标管理API接口
 * @author 示例云开发团队
 * @since 2025-01-06
 */

// ==================== 招投标项目管理 ====================

/**
 * 创建招投标项目
 * @param {Object} data 项目数据
 * @returns {Promise} 创建结果
 */
export function createBiddingProject(data) {
  return request({
    url: '/contract/bidding/save',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 查询招投标项目列表
 * @param {Object} params 查询参数
 * @returns {Promise} 分页结果
 */
export function getBiddingProjectList(params) {
  return request({
    url: '/contract/bidding/list',
    method: 'post',
    data: params,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 获取招投标项目详情
 * @param {String} id 项目ID
 * @returns {Promise} 项目详情
 */
export function getBiddingProjectById(id) {
  return request({
    url: `/contract/bidding/${id}`,
    method: 'get'
  })
}

/**
 * 更新投标项目
 * @param {Object} data 项目数据（包含id）
 * @returns {Promise} 更新结果
 */
export function updateBiddingProject(data) {
  return request({
    url: `/contract/bidding/${data.id}`,
    method: 'put',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 更新投标决策
 * @param {Object} data 决策数据（包含id）
 * @returns {Promise} 更新结果
 */
export function updateBidDecision(data) {
  return request({
    url: '/contract/bidding/updateProjectStatus',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 删除招投标项目
 * @param {String} id 项目ID
 * @returns {Promise} 删除结果
 */
export function deleteBiddingProject(id) {
  return request({
    url: `/contract/bidding/${id}`,
    method: 'delete'
  })
}

/**
 * 批量删除招投标项目
 * @param {Array} ids 项目ID列表
 * @returns {Promise} 删除结果
 */
export function batchDeleteBiddingProjects(ids) {
  return request({
    url: '/contract/bidding/batchDelete',
    method: 'post',
    data: ids,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 更新投标结果
 * @param {String} id 项目ID
 * @param {Object} data 结果数据
 * @returns {Promise} 更新结果
 */
export function updateBidResult(id, data) {
  return request({
    url: `/contract/bidding/markAsWinning/${id}`,
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// ==================== 标书模板管理 ====================

/**
 * 创建标书模板
 * @param {Object} data 模板数据
 * @returns {Promise} 创建结果
 */
export function createBidTemplate(data) {
  return request({
    url: '/contract/bidding/template/save',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 查询标书模板列表
 * @param {Object} params 查询参数
 * @returns {Promise} 分页结果
 */
export function getBidTemplateList(params) {
  return request({
    url: '/contract/bidding/template/list',
    method: 'post',
    data: params,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 获取标书模板详情
 * @param {String} id 模板ID
 * @returns {Promise} 模板详情
 */
export function getBidTemplateById(id) {
  return request({
    url: `/contract/bidding/template/${id}`,
    method: 'get'
  })
}

/**
 * 删除标书模板
 * @param {String} id 模板ID
 * @returns {Promise} 删除结果
 */
export function deleteBidTemplate(id) {
  return request({
    url: `/contract/bidding/template/${id}`,
    method: 'delete'
  })
}

/**
 * 批量删除标书模板
 * @param {Array} ids 模板ID列表
 * @returns {Promise} 删除结果
 */
export function batchDeleteBidTemplate(ids) {
  return request({
    url: '/contract/bidding/template/batchDelete',
    method: 'post',
    data: ids,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 批量删除标书模板（复数形式）
 * @param {Array} ids 模板ID列表
 * @returns {Promise} 删除结果
 */
export function batchDeleteBidTemplates(ids) {
  return batchDeleteBidTemplate(ids)
}

/**
 * 根据类型获取标书模板
 * @param {Number} templateType 模板类型
 * @returns {Promise} 模板列表
 */
export function getBidTemplatesByType(templateType) {
  return request({
    url: `/contract/bidding/template/byType/${templateType}`,
    method: 'get'
  })
}

/**
 * 更新模板状态
 * @param {String} id 模板ID
 * @param {Number} enabled 是否启用
 * @returns {Promise} 更新结果
 */
export function updateTemplateStatus(id, enabled) {
  return request({
    url: `/contract/bidding/template/updateStatus/${id}`,
    method: 'post',
    params: { enabled }
  })
}

/**
 * 复制标书模板
 * @param {String} id 源模板ID
 * @returns {Promise} 复制结果
 */
export function copyBidTemplate(id) {
  return request({
    url: `/contract/bidding/template/copy/${id}`,
    method: 'post'
  })
}

/**
 * 获取模板统计信息
 * @returns {Promise} 统计数据
 */
export function getTemplateStatistics() {
  return request({
    url: '/contract/bidding/template/statistics',
    method: 'get'
  })
}

/**
 * 生成模板编号
 * @returns {Promise} 模板编号
 */
export function generateTemplateNo() {
  return request({
    url: '/contract/bidding/template/generateTemplateNo',
    method: 'get'
  })
}

/**
 * 更新标书模板
 * @param {Object} data 模板数据（包含id）
 * @returns {Promise} 更新结果
 */
export function updateBidTemplate(data) {
  return request({
    url: `/contract/bidding/template/${data.id}`,
    method: 'put',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 更新标书模板状态
 * @param {String} id 模板ID
 * @param {Number} enabled 是否启用
 * @returns {Promise} 更新结果
 */
export function updateBidTemplateStatus(id, enabled) {
  return updateTemplateStatus(id, enabled)
}

/**
 * 下载标书模板
 * @param {String} id 模板ID
 * @returns {Promise} 下载结果
 */
export function downloadBidTemplate(id) {
  return request({
    url: `/contract/bidding/template/download/${id}`,
    method: 'get',
    responseType: 'blob'
  })
}

/**
 * 导出标书模板
 * @param {Object} params 导出参数
 * @returns {Promise} 导出结果
 */
export function exportBidTemplates(params) {
  return request({
    url: '/contract/bidding/template/export',
    method: 'get',
    params: params,
    responseType: 'blob'
  })
}

/**
 * 生成项目编号
 * @returns {Promise} 项目编号
 */
export function generateProjectNo() {
  return request({
    url: '/contract/bidding/generateProjectNo',
    method: 'get'
  })
}

// ==================== 投标文件管理 ====================

/**
 * 上传投标文件
 * @param {FormData} formData 文件数据
 * @returns {Promise} 上传结果
 */
export function uploadBidDocument(formData) {
  return request({
    url: '/contract/bidding/document/upload',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 查询投标文件列表
 * @param {Object} params 查询参数
 * @returns {Promise} 文件列表
 */
export function getBidDocumentList(params) {
  return request({
    url: '/contract/bidding/document/list',
    method: 'get',
    params: params
  })
}

// ==================== 保证金管理 ====================

/**
 * 创建保证金记录
 * @param {Object} data 保证金数据
 * @returns {Promise} 创建结果
 */
export function createGuaranteeDeposit(data) {
  return request({
    url: '/contract/bidding/deposit',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 查询保证金列表
 * @param {Object} params 查询参数
 * @returns {Promise} 保证金列表
 */
export function getGuaranteeDepositList(params) {
  return request({
    url: '/contract/bidding/deposit/page',
    method: 'post',
    data: params,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 获取保证金列表
 * @param {Object} params 查询参数
 * @returns {Promise} 保证金列表
 */
export function getDepositList(params) {
  return request({
    url: '/contract/bidding/deposit/list',
    method: 'get',
    params: params
  })
}

/**
 * 保存保证金
 * @param {Object} data 保证金数据
 * @returns {Promise} 保存结果
 */
export function saveDeposit(data) {
  return request({
    url: '/contract/bidding/deposit/save',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

/**
 * 更新保证金
 * @param {Object} data 保证金数据
 * @returns {Promise} 更新结果
 */
export function updateDeposit(data) {
  return request({
    url: '/contract/bidding/deposit/update',
    method: 'put',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 根据ID获取保证金详情
 * @param {String} id 保证金ID
 * @returns {Promise} 保证金详情
 */
export function getDepositById(id) {
  return request({
    url: `/contract/bidding/deposit/${id}`,
    method: 'get'
  })
}

/**
 * 删除保证金
 * @param {String} id 保证金ID
 * @returns {Promise} 删除结果
 */
export function deleteDeposit(id) {
  return request({
    url: `/contract/bidding/deposit/${id}`,
    method: 'delete'
  })
}

/**
 * 缴纳保证金
 * @param {Object} data 缴纳数据
 * @returns {Promise} 缴纳结果
 */
export function payDeposit(data) {
  return request({
    url: '/contract/bidding/deposit/pay',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

/**
 * 退还保证金
 * @param {Object} data 退还数据
 * @returns {Promise} 退还结果
 */
export function refundDeposit(data) {
  return request({
    url: '/contract/bidding/deposit/refund',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

/**
 * 没收保证金
 * @param {String} id 保证金ID
 * @returns {Promise} 没收结果
 */
export function forfeitDeposit(id) {
  return request({
    url: `/contract/bidding/deposit/forfeit/${id}`,
    method: 'put'
  })
}

/**
 * 获取保证金统计数据
 * @param {Object} params 查询参数
 * @returns {Promise} 统计数据
 */
export function getDepositStatistics(params) {
  return request({
    url: '/contract/bidding/deposit/statistics',
    method: 'get',
    params: params
  })
}

/**
 * 导出保证金数据
 * @param {Object} params 导出参数
 * @returns {Promise} 导出结果
 */
export function exportDepositData(params) {
  return request({
    url: '/contract/bidding/deposit/export',
    method: 'get',
    params: params,
    responseType: 'blob'
  })
}

/**
 * 更新保证金状态
 * @param {String} id 保证金ID
 * @param {Object} data 状态数据
 * @returns {Promise} 更新结果
 */
export function updateDepositStatus(id, data) {
  return request({
    url: `/contract/bidding/deposit/status/${id}`,
    method: 'put',
    data: data,
    headers: {
      'Content-Type': 'application/json'
    }
  })
}
