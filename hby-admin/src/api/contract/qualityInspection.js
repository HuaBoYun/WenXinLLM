/**
 * 质量检查管理API
 * 根据达梦数据库 quality_inspection 表结构和示例云开发规范
 */
import request from '@/utils/request'

// 质量检查管理API基础路径（通过网关访问，需要/contract前缀）
const API_BASE_PATH = '/contract/quality/inspection'

/**
 * 分页查询质量检查记录
 * @param {Object} params 查询参数
 * @param {Number} params.projectId 项目ID
 * @param {String} params.inspectionNo 检查编号
 * @param {String} params.inspectionName 检查名称
 * @param {Number} params.inspectionType 检查类型(1:自检,2:互检,3:专检,4:验收)
 * @param {Number} params.inspectorId 检查人ID
 * @param {Number} params.checkResult 检查结论(1:合格,2:不合格,3:待整改)
 * @param {Number} params.rectificationStatus 整改状态(1:待整改,2:整改中,3:已整改,4:已验收)
 * @param {String} params.keyword 关键词搜索
 * @param {Number} params.pageNum 页码
 * @param {Number} params.pageSize 每页大小
 * @returns {Promise}
 */
export function getQualityInspectionPage(params) {
  return request({
    url: `${API_BASE_PATH}/page`,
    method: 'post',
    data: params,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 根据ID查询质量检查详情
 * @param {Number} id 质量检查ID
 * @returns {Promise}
 */
export function getQualityInspectionDetail(id) {
  return request({
    url: `${API_BASE_PATH}/detail/${id}`,
    method: 'post'
  })
}

/**
 * 新增质量检查记录
 * @param {Object} data 质量检查数据
 * @param {Number} data.projectId 项目ID
 * @param {Number} data.taskId 任务ID
 * @param {String} data.inspectionNo 检查编号
 * @param {String} data.inspectionName 检查名称
 * @param {Number} data.inspectionType 检查类型(1:自检,2:互检,3:专检,4:验收)
 * @param {String} data.inspectionDate 检查日期
 * @param {Number} data.inspectorId 检查人ID
 * @param {String} data.inspectionScope 检查范围
 * @param {String} data.inspectionStandards 检查标准
 * @param {String} data.inspectionMethods 检查方法
 * @param {String} data.inspectionResults 检查结果
 * @param {Number} data.checkResult 检查结论(1:合格,2:不合格,3:待整改)
 * @param {String} data.identifiedIssues 发现问题
 * @param {String} data.rectificationRequirements 整改要求
 * @param {String} data.rectificationDeadline 整改期限
 * @param {Number} data.rectificationPersonId 整改负责人ID
 * @param {Number} data.rectificationStatus 整改状态(1:待整改,2:整改中,3:已整改,4:已验收)
 * @returns {Promise}
 */
export function createQualityInspection(data) {
  return request({
    url: `${API_BASE_PATH}/create`,
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 更新质量检查记录
 * @param {Object} data 质量检查数据（包含id）
 * @returns {Promise}
 */
export function updateQualityInspection(data) {
  return request({
    url: `${API_BASE_PATH}/update`,
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 删除质量检查记录
 * @param {Number} id 质量检查ID
 * @returns {Promise}
 */
export function deleteQualityInspection(id) {
  return request({
    url: `${API_BASE_PATH}/delete/${id}`,
    method: 'post'
  })
}

/**
 * 批量删除质量检查记录
 * @param {Array} ids 质量检查ID数组
 * @returns {Promise}
 */
export function batchDeleteQualityInspection(ids) {
  return request({
    url: `${API_BASE_PATH}/batch-delete`,
    method: 'post',
    data: { ids },
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 根据项目ID查询质量检查记录
 * @param {Number} projectId 项目ID
 * @returns {Promise}
 */
export function getQualityInspectionByProject(projectId) {
  return request({
    url: `${API_BASE_PATH}/project/${projectId}`,
    method: 'post'
  })
}

/**
 * 整改质量检查问题
 * @param {Number} id 质量检查ID
 * @param {Object} data 整改数据
 * @param {String} data.rectificationDescription 整改说明
 * @param {Number} data.rectificationPersonId 整改负责人ID
 * @returns {Promise}
 */
export function rectifyQualityInspection(id, data) {
  return request({
    url: `${API_BASE_PATH}/rectify/${id}`,
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 获取质量检查统计信息
 * @param {Number} projectId 项目ID（可选）
 * @returns {Promise}
 */
export function getQualityInspectionStatistics(params = {}) {
  return request({
    url: `${API_BASE_PATH}/statistics`,
    method: 'post',
    data: params,
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
    }
  })
}

/**
 * 复查质量检查记录
 * @param {Number} id 质量检查ID
 * @param {Object} data 复查数据
 * @param {Number} data.recheckResult 复查结果(1:合格,2:不合格)
 * @param {String} data.recheckComments 复查意见
 * @param {Number} data.recheckPersonId 复查人ID
 * @returns {Promise}
 */
export function recheckQualityInspection(id, data) {
  return request({
    url: `${API_BASE_PATH}/recheck/${id}`,
    method: 'post',
    data: data
  })
}

/**
 * 获取质量检查类型选项
 * @returns {Array}
 */
export function getInspectionTypeOptions() {
  return [
    { value: 1, label: '自检' },
    { value: 2, label: '互检' },
    { value: 3, label: '专检' },
    { value: 4, label: '验收' }
  ]
}

/**
 * 获取检查结论选项
 * @returns {Array}
 */
export function getCheckResultOptions() {
  return [
    { value: 1, label: '合格' },
    { value: 2, label: '不合格' },
    { value: 3, label: '待整改' }
  ]
}

/**
 * 获取整改状态选项
 * @returns {Array}
 */
export function getRectificationStatusOptions() {
  return [
    { value: 1, label: '待整改' },
    { value: 2, label: '整改中' },
    { value: 3, label: '已整改' },
    { value: 4, label: '已验收' }
  ]
}

/**
 * 获取复查结果选项
 * @returns {Array}
 */
export function getRecheckResultOptions() {
  return [
    { value: 1, label: '合格' },
    { value: 2, label: '不合格' }
  ]
}

export default {
  getQualityInspectionPage,
  getQualityInspectionDetail,
  createQualityInspection,
  updateQualityInspection,
  deleteQualityInspection,
  batchDeleteQualityInspection,
  getQualityInspectionByProject,
  rectifyQualityInspection,
  getQualityInspectionStatistics,
  recheckQualityInspection,
  getInspectionTypeOptions,
  getCheckResultOptions,
  getRectificationStatusOptions,
  getRecheckResultOptions
}
