import request from '@/utils/request'

/**
 * 项目经营管理API接口
 * @author 示例云开发团队
 * @since 2025-01-06
 */

// ==================== 收支管理 ====================

/**
 * 创建项目管理记录
 * @param {Object} data 管理数据
 * @returns {Promise} 创建结果
 */
export function createProjectManagement(data) {
  return request({
    url: '/contract/operations/save',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 查询项目管理列表
 * @param {Object} params 查询参数
 * @returns {Promise} 分页结果
 */
export function getProjectManagementList(params) {
  return request({
    url: '/contract/operations/list',
    method: 'post',
    data: params,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 获取项目管理详情
 * @param {String} id 管理ID
 * @returns {Promise} 管理详情
 */
export function getProjectManagementById(id) {
  return request({
    url: `/contract/operations/${id}`,
    method: 'get'
  })
}

/**
 * 更新项目管理记录
 * @param {String} id 管理ID
 * @param {Object} data 更新数据
 * @returns {Promise} 更新结果
 */
export function updateProjectManagement(id, data) {
  return request({
    url: '/contract/operations/save',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 删除项目管理记录
 * @param {String} id 管理ID
 * @returns {Promise} 删除结果
 */
export function deleteProjectManagement(id) {
  return request({
    url: `/contract/operations/${id}`,
    method: 'delete'
  })
}

/**
 * 获取财务管理数据
 * @param {Object} params 查询参数
 * @returns {Promise} 财务数据
 */
export function getFinanceManagementData(params) {
  return request({
    url: '/contract/finance/transaction/page',
    method: 'post',
    data: params,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 保存财务管理数据
 * @param {Object} data 财务数据
 * @returns {Promise} 保存结果
 */
export function saveFinanceManagementData(data) {
  return request({
    url: '/contract/finance/transaction/create',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 获取进度管理数据
 * @param {Object} params 查询参数
 * @returns {Promise} 进度数据
 */
export function getProgressManagementData(params) {
  return request({
    url: '/contract/progress/page',
    method: 'post',
    data: params,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 保存进度管理数据
 * @param {Object} data 进度数据
 * @returns {Promise} 保存结果
 */
export function saveProgressManagementData(data) {
  return request({
    url: '/contract/progress/create',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 获取质量管理数据
 * @param {Object} params 查询参数
 * @returns {Promise} 质量数据
 */
export function getQualityManagementData(params) {
  return request({
    url: '/contract/quality/inspection/page',
    method: 'post',
    data: params,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 保存质量管理数据
 * @param {Object} data 质量数据
 * @returns {Promise} 保存结果
 */
export function saveQualityManagementData(data) {
  return request({
    url: '/contract/quality/inspection/create',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 创建收支记录
 * @param {Object} data 收支数据
 * @returns {Promise} 创建结果
 */
export function createFinanceTransaction(data) {
  return request({
    url: '/contract/finance/transaction/create',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 查询收支记录列表
 * @param {Object} params 查询参数
 * @returns {Promise} 分页结果
 */
export function getFinanceTransactionList(params) {
  return request({
    url: '/contract/finance/transaction/page',
    method: 'post',
    data: params,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 收支审批
 * @param {String} id 记录ID
 * @param {Object} data 审批数据
 * @returns {Promise} 审批结果
 */
export function approveFinanceTransaction(id, data) {
  return request({
    url: `/contract/finance/transaction/approve/${id}`,
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// ==================== 进度管理 ====================

/**
 * 创建进度记录
 * @param {Object} data 进度数据
 * @returns {Promise} 创建结果
 */
export function createProgressRecord(data) {
  return request({
    url: '/contract/progress/create',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 查询进度记录列表
 * @param {Object} params 查询参数
 * @returns {Promise} 分页结果
 */
export function getProgressRecordList(params) {
  return request({
    url: '/contract/progress/page',
    method: 'post',
    data: params,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 更新进度
 * @param {String} id 进度ID
 * @param {Object} data 更新数据
 * @returns {Promise} 更新结果
 */
export function updateProgressRecord(id, data) {
  return request({
    url: `/contract/progress/update/${id}`,
    method: 'put',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// ==================== 质量检查管理 ====================

/**
 * 创建质量检查
 * @param {Object} data 检查数据
 * @returns {Promise} 创建结果
 */
export function createQualityInspection(data) {
  return request({
    url: '/contract/quality/inspection/create',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 查询质量检查列表
 * @param {Object} params 查询参数
 * @returns {Promise} 分页结果
 */
export function getQualityInspectionList(params) {
  return request({
    url: '/contract/quality/inspection/page',
    method: 'post',
    data: params,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 质量问题整改
 * @param {String} id 检查ID
 * @param {Object} data 整改数据
 * @returns {Promise} 整改结果
 */
export function rectifyQualityInspection(id, data) {
  return request({
    url: `/contract/quality/inspection/rectify/${id}`,
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// ==================== 安全检查管理 ====================

/**
 * 创建安全检查
 * @param {Object} data 检查数据
 * @returns {Promise} 创建结果
 */
export function createSafetyInspection(data) {
  return request({
    url: '/contract/safety/inspection/create',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 查询安全检查列表
 * @param {Object} params 查询参数
 * @returns {Promise} 分页结果
 */
export function getSafetyInspectionList(params) {
  return request({
    url: '/contract/safety/inspection/list',
    method: 'post',
    data: params,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// ==================== 项目结算管理 ====================

/**
 * 创建项目结算
 * @param {Object} data 结算数据
 * @returns {Promise} 创建结果
 */
export function createProjectSettlement(data) {
  return request({
    url: '/contract/operations/save',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 查询项目结算列表
 * @param {Object} params 查询参数
 * @returns {Promise} 分页结果
 */
export function getProjectSettlementList(params) {
  return request({
    url: '/contract/operations/list',
    method: 'post',
    data: params,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 结算审核
 * @param {String} id 结算ID
 * @param {Object} data 审核数据
 * @returns {Promise} 审核结果
 */
export function reviewProjectSettlement(id, data) {
  return request({
    url: '/contract/operations/save',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// ==================== 文档归档管理 ====================

/**
 * 创建文档归档
 * @param {Object} data 归档数据
 * @returns {Promise} 创建结果
 */
export function createDocumentArchive(data) {
  return request({
    url: '/contract/archive/document/create',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 查询文档归档列表
 * @param {Object} params 查询参数
 * @returns {Promise} 分页结果
 */
export function getDocumentArchiveList(params) {
  return request({
    url: '/contract/archive/document/list',
    method: 'post',
    data: params,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 获取文档归档详情
 * @param {String} id 文档ID
 * @returns {Promise} 文档详情
 */
export function getDocumentArchiveById(id) {
  return request({
    url: `/contract/archive/document/${id}`,
    method: 'get'
  })
}

/**
 * 更新文档归档
 * @param {Object} data 更新数据（包含id）
 * @returns {Promise} 更新结果
 */
export function updateDocumentArchive(data) {
  return request({
    url: `/contract/archive/document/${data.id}`,
    method: 'put',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 删除文档归档
 * @param {String} id 文档ID
 * @returns {Promise} 删除结果
 */
export function deleteDocumentArchive(id) {
  return request({
    url: `/contract/archive/document/${id}`,
    method: 'delete'
  })
}

/**
 * 文档检索
 * @param {Object} params 检索参数
 * @returns {Promise} 检索结果
 */
export function searchDocumentArchive(params) {
  return request({
    url: '/contract/archive/document/search',
    method: 'get',
    params: params
  })
}

/**
 * 根据项目ID查询文档归档列表
 * @param {String} projectId 项目ID
 * @returns {Promise} 文档列表
 */
export function getDocumentArchiveByProjectId(projectId) {
  return request({
    url: `/contract/archive/document/project/${projectId}`,
    method: 'get'
  })
}

/**
 * 获取文档归档统计信息
 * @returns {Promise} 统计信息
 */
export function getDocumentArchiveStatistics() {
  return request({
    url: '/contract/archive/document/statistics',
    method: 'get'
  })
}
