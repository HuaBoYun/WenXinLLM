import request from '@/utils/request'

/**
 * 项目交底API接口
 * @author 示例云开发团队
 * @since 2025-01-06
 */

// ==================== 项目交底管理 ====================

/**
 * 创建项目交底
 * @param {Object} data 交底数据
 * @returns {Promise} 创建结果
 */
export function createProjectBriefing(data) {
  return request({
    url: '/contract/briefing/save',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 查询项目交底列表
 * @param {Object} params 查询参数
 * @returns {Promise} 分页结果
 */
export function getProjectBriefingList(params) {
  return request({
    url: '/contract/briefing/list',
    method: 'post',
    data: params,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 获取项目交底详情
 * @param {String} id 交底ID
 * @returns {Promise} 交底详情
 */
export function getProjectBriefingById(id) {
  return request({
    url: `/contract/briefing/${id}`,
    method: 'get'
  })
}

/**
 * 更新项目交底
 * @param {String} id 交底ID
 * @param {Object} data 更新数据
 * @returns {Promise} 更新结果
 */
export function updateProjectBriefing(id, data) {
  return request({
    url: `/contract/briefing/${id}`,
    method: 'put',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 交底确认
 * @param {String} id 交底ID
 * @param {Object} data 确认数据
 * @returns {Promise} 确认结果
 */
export function confirmProjectBriefing(id, data) {
  return request({
    url: `/contract/briefing/confirm/${id}`,
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 删除项目交底
 * @param {String} id 交底ID
 * @returns {Promise} 删除结果
 */
export function deleteProjectBriefing(id) {
  return request({
    url: `/contract/briefing/${id}`,
    method: 'delete'
  })
}

/**
 * 获取交底模板列表
 * @param {Object} params 查询参数
 * @returns {Promise} 模板列表
 */
export function getBriefingTemplateList(params) {
  return request({
    url: '/contract/briefing/template/list',
    method: 'get',
    params: params
  })
}

/**
 * 上传交底附件
 * @param {FormData} formData 文件数据
 * @returns {Promise} 上传结果
 */
export function uploadBriefingAttachment(formData) {
  return request({
    url: '/contract/briefing/attachment/upload',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 获取交底附件列表
 * @param {Object} params 查询参数
 * @returns {Promise} 附件列表
 */
export function getBriefingAttachmentList(params) {
  return request({
    url: '/contract/briefing/attachment/list',
    method: 'get',
    params: params
  })
}

/**
 * 更新交底附件
 * @param {String} id 附件ID
 * @param {Object} data 更新数据
 * @returns {Promise} 更新结果
 */
export function updateBriefingAttachment(id, data) {
  return request({
    url: `/contract/briefing/attachment/${id}`,
    method: 'put',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 删除交底附件
 * @param {String} id 附件ID
 * @returns {Promise} 删除结果
 */
export function deleteBriefingAttachment(id) {
  return request({
    url: `/contract/briefing/attachment/${id}`,
    method: 'delete'
  })
}

/**
 * 下载交底附件
 * @param {String} filePath 文件路径
 * @returns {Promise} 文件流
 */
export function downloadBriefingAttachment(filePath) {
  return request({
    url: '/contract/briefing/attachment/download',
    method: 'get',
    params: { filePath },
    responseType: 'blob'
  })
}
