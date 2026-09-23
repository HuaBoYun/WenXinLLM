import request from '@/utils/request'

/**
 * 项目信息登记API接口
 * @author 示例云开发团队
 * @since 2025-01-25
 */

/**
 * 分页查询项目信息登记列表
 * @param {Object} params 查询参数
 * @returns {Promise} 分页结果
 */
export function getProjectInfoRegisterList(params) {
  return request({
    url: '/contract/project/info/register/list',
    method: 'post',
    data: params,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 根据ID获取项目信息详情
 * @param {String} projectId 项目ID
 * @returns {Promise} 项目信息详情
 */
export function getProjectInfoRegisterById(projectId) {
  return request({
    url: `/contract/project/info/register/${projectId}`,
    method: 'get'
  })
}

/**
 * 保存项目信息登记（新增或修改）
 * @param {Object} data 项目信息
 * @returns {Promise} 保存结果
 */
export function saveProjectInfoRegister(data) {
  return request({
    url: '/contract/project/info/register/save',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 删除项目信息登记
 * @param {String} projectId 项目ID
 * @returns {Promise} 删除结果
 */
export function deleteProjectInfoRegister(projectId) {
  return request({
    url: `/contract/project/info/register/${projectId}`,
    method: 'delete'
  })
}

/**
 * 生成项目登记编号
 * @returns {Promise} 登记编号
 */
export function generateRegisterNo() {
  return request({
    url: '/contract/project/info/register/generateRegisterNo',
    method: 'get'
  })
}

/**
 * 批量删除项目信息登记
 * @param {Array} projectIds 项目ID列表
 * @returns {Promise} 删除结果
 */
export function batchDeleteProjectInfoRegister(projectIds) {
  return request({
    url: '/contract/project/info/register/batchDelete',
    method: 'post',
    data: { projectIds },
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 更新项目状态
 * @param {String} projectId 项目ID
 * @param {Number} projectStatus 项目状态
 * @returns {Promise} 更新结果
 */
export function updateProjectStatus(projectId, projectStatus) {
  return request({
    url: '/contract/project/info/register/updateStatus',
    method: 'post',
    data: { projectId, projectStatus },
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 批量更新项目状态
 * @param {Array} projectIds 项目ID列表
 * @param {Number} projectStatus 项目状态
 * @returns {Promise} 更新结果
 */
export function batchUpdateProjectStatus(projectIds, projectStatus) {
  return request({
    url: '/contract/project/info/register/batchUpdateStatus',
    method: 'post',
    data: { projectIds, projectStatus },
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 更新报备状态
 * @param {String} projectId 项目ID
 * @param {Number} reportStatus 报备状态
 * @returns {Promise} 更新结果
 */
export function updateReportStatus(projectId, reportStatus) {
  return request({
    url: '/contract/project/info/register/updateReportStatus',
    method: 'post',
    data: { projectId, reportStatus },
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 获取需要首谈报备的项目列表
 * @returns {Promise} 需要首谈报备的项目列表
 */
export function getFirstTalkReportProjects() {
  return request({
    url: '/contract/project/info/register/firstTalkReportProjects',
    method: 'get'
  })
}

/**
 * 检查项目重复
 * @param {String} projectName 项目名称
 * @param {String} contractorFullName 发包方全称
 * @param {String} excludeProjectId 排除的项目ID
 * @returns {Promise} 重复项目列表
 */
export function checkDuplicateProjects(projectName, contractorFullName, excludeProjectId) {
  return request({
    url: '/contract/project/info/register/checkDuplicate',
    method: 'post',
    data: { projectName, contractorFullName, excludeProjectId },
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 导出项目信息登记列表
 * @param {Object} params 查询参数
 * @returns {Promise} 导出结果
 */
export function exportProjectInfoRegisterList(params) {
  return request({
    url: '/contract/project/info/register/export',
    method: 'post',
    data: params,
    responseType: 'blob',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
