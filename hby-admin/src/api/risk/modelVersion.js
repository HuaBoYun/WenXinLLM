/**
 * 模型版本管理API接口
 * @author 示例云
 * @date 2025-09-30
 */

import request from '@/utils/request'

// 基础API路径
const API_BASE = '/api/model/version'

/**
 * 获取模型版本列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getModelVersionList(params) {
  return request({
    url: `${API_BASE}/list`,
    method: 'post',
    data: params
  })
}

/**
 * 获取版本详情
 * @param {String} versionId 版本ID
 * @returns {Promise}
 */
export function getVersionDetail(versionId) {
  return request({
    url: `${API_BASE}/detail/${versionId}`,
    method: 'post'
  })
}

/**
 * 创建新版本
 * @param {Object} data 版本数据
 * @returns {Promise}
 */
export function createVersion(data) {
  return request({
    url: `${API_BASE}/create`,
    method: 'post',
    data: data
  })
}

/**
 * 更新版本
 * @param {Object} data 版本数据
 * @returns {Promise}
 */
export function updateVersion(data) {
  return request({
    url: `${API_BASE}/update`,
    method: 'post',
    data: data
  })
}

/**
 * 删除版本
 * @param {String} versionId 版本ID
 * @returns {Promise}
 */
export function deleteVersion(versionId) {
  return request({
    url: `${API_BASE}/delete/${versionId}`,
    method: 'post'
  })
}

/**
 * 发布版本
 * @param {String} versionId 版本ID
 * @returns {Promise}
 */
export function publishVersion(versionId) {
  return request({
    url: `${API_BASE}/publish/${versionId}`,
    method: 'post'
  })
}

/**
 * 归档版本
 * @param {String} versionId 版本ID
 * @returns {Promise}
 */
export function archiveVersion(versionId) {
  return request({
    url: `${API_BASE}/archive/${versionId}`,
    method: 'post'
  })
}

/**
 * 回滚到指定版本
 * @param {String} versionId 版本ID
 * @returns {Promise}
 */
export function rollbackToVersion(versionId) {
  return request({
    url: `${API_BASE}/rollback/${versionId}`,
    method: 'post'
  })
}

/**
 * 比较版本
 * @param {String} sourceVersionId 源版本ID
 * @param {String} targetVersionId 目标版本ID
 * @returns {Promise}
 */
export function compareVersions(sourceVersionId, targetVersionId) {
  return request({
    url: `${API_BASE}/compare`,
    method: 'post',
    data: {
      sourceVersionId: sourceVersionId,
      targetVersionId: targetVersionId
    }
  })
}

/**
 * 获取版本比较历史
 * @param {String} modelId 模型ID
 * @returns {Promise}
 */
export function getCompareHistory(modelId) {
  return request({
    url: `${API_BASE}/compare/history/${modelId}`,
    method: 'post'
  })
}

/**
 * 获取版本操作日志
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getVersionLogs(params) {
  return request({
    url: `${API_BASE}/logs`,
    method: 'post',
    data: params
  })
}

/**
 * 添加版本标签
 * @param {String} versionId 版本ID
 * @param {Object} tagData 标签数据
 * @returns {Promise}
 */
export function addVersionTag(versionId, tagData) {
  return request({
    url: `${API_BASE}/tag/add`,
    method: 'post',
    data: {
      versionId: versionId,
      ...tagData
    }
  })
}

/**
 * 删除版本标签
 * @param {String} tagId 标签ID
 * @returns {Promise}
 */
export function deleteVersionTag(tagId) {
  return request({
    url: `${API_BASE}/tag/delete/${tagId}`,
    method: 'post'
  })
}

/**
 * 获取版本标签列表
 * @param {String} versionId 版本ID
 * @returns {Promise}
 */
export function getVersionTags(versionId) {
  return request({
    url: `${API_BASE}/tag/list/${versionId}`,
    method: 'post'
  })
}

/**
 * 提交版本审批
 * @param {String} versionId 版本ID
 * @param {String} approvalType 审批类型
 * @returns {Promise}
 */
export function submitVersionApproval(versionId, approvalType) {
  return request({
    url: `${API_BASE}/approval/submit`,
    method: 'post',
    data: {
      versionId: versionId,
      approvalType: approvalType
    }
  })
}

/**
 * 审批版本
 * @param {String} approvalId 审批ID
 * @param {Object} approvalData 审批数据
 * @returns {Promise}
 */
export function approveVersion(approvalId, approvalData) {
  return request({
    url: `${API_BASE}/approval/approve`,
    method: 'post',
    data: {
      approvalId: approvalId,
      ...approvalData
    }
  })
}

/**
 * 拒绝版本审批
 * @param {String} approvalId 审批ID
 * @param {String} rejectionReason 拒绝原因
 * @returns {Promise}
 */
export function rejectVersionApproval(approvalId, rejectionReason) {
  return request({
    url: `${API_BASE}/approval/reject`,
    method: 'post',
    data: {
      approvalId: approvalId,
      rejectionReason: rejectionReason
    }
  })
}

/**
 * 获取版本审批列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getVersionApprovals(params) {
  return request({
    url: `${API_BASE}/approval/list`,
    method: 'post',
    data: params
  })
}

/**
 * 获取版本状态选项
 * @returns {Array}
 */
export function getVersionStatusOptions() {
  return [
    { value: 'DRAFT', label: '草稿' },
    { value: 'TESTING', label: '测试中' },
    { value: 'PUBLISHED', label: '已发布' },
    { value: 'ARCHIVED', label: '已归档' }
  ]
}

/**
 * 获取标签颜色选项
 * @returns {Array}
 */
export function getTagColorOptions() {
  return [
    { value: '#1890ff', label: '蓝色' },
    { value: '#52c41a', label: '绿色' },
    { value: '#fa8c16', label: '橙色' },
    { value: '#f5222d', label: '红色' },
    { value: '#722ed1', label: '紫色' },
    { value: '#13c2c2', label: '青色' },
    { value: '#8c8c8c', label: '灰色' }
  ]
}

/**
 * 获取审批类型选项
 * @returns {Array}
 */
export function getApprovalTypeOptions() {
  return [
    { value: 'PUBLISH', label: '发布审批' },
    { value: 'ARCHIVE', label: '归档审批' }
  ]
}

/**
 * 获取审批状态选项
 * @returns {Array}
 */
export function getApprovalStatusOptions() {
  return [
    { value: 'PENDING', label: '待审批' },
    { value: 'APPROVED', label: '已通过' },
    { value: 'REJECTED', label: '已拒绝' }
  ]
}
