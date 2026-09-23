/*
 * @Description: 财务共享 - 专项成本模块 API
 * @Author: system
 * @Date: 2024-12-19
 */
import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// ==================== 专项成本统计 API ====================

/**
 * 获取专项成本统计概览
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getSpecialCostStats(params) {
  return request({
    url: '/cwgxAi/ma/specialcost/stats',
    method: 'get',
    params
  })
}

// ==================== 项目成本 API ====================

/**
 * 分页查询项目成本列表
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getProjectCostPage(data) {
  return request({
    url: '/cwgxAi/ma/specialcost/project/getList',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 保存或更新项目成本
 * @param {Object} data 项目成本数据
 * @returns {Promise}
 */
export function saveOrUpdateProjectCost(data) {
  return request({
    url: '/cwgxAi/ma/specialcost/project/saveOrUpdate',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取项目成本详情
 * @param {Number} projectId 项目ID
 * @returns {Promise}
 */
export function getProjectCostById(projectId) {
  return request({
    url: `/cwgxAi/ma/specialcost/project/${projectId}`,
    method: 'get'
  })
}

/**
 * 删除项目成本
 * @param {Number} projectId 项目ID
 * @returns {Promise}
 */
export function deleteProjectCost(projectId) {
  return request({
    url: `/cwgxAi/ma/specialcost/project/${projectId}`,
    method: 'delete'
  })
}

/**
 * 批量删除项目成本
 * @param {Array} projectIds 项目ID数组
 * @returns {Promise}
 */
export function batchDeleteProjectCost(projectIds) {
  return request({
    url: '/cwgxAi/ma/specialcost/project/batchDelete',
    method: 'delete',
    data: projectIds
  })
}

/**
 * 批量更新项目状态
 * @param {Array} projectIds 项目ID数组
 * @param {Number} status 状态
 * @returns {Promise}
 */
export function batchUpdateProjectStatus(projectIds, status) {
  return request({
    url: '/cwgxAi/ma/specialcost/project/batchUpdateStatus',
    method: 'post',
    data: {
      projectIds,
      status
    }
  })
}

/**
 * 获取项目成本分析数据
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getProjectCostAnalysis(params) {
  return request({
    url: '/cwgxAi/ma/specialcost/project/analysis',
    method: 'get',
    params
  })
}

// ==================== 作业成本 API ====================

/**
 * 分页查询作业成本列表
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getActivityCostPage(data) {
  return request({
    url: '/cwgxAi/ma/specialcost/activity/getList',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 保存或更新作业成本
 * @param {Object} data 作业成本数据
 * @returns {Promise}
 */
export function saveOrUpdateActivityCost(data) {
  return request({
    url: '/cwgxAi/ma/specialcost/activity/saveOrUpdate',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取作业成本详情
 * @param {Number} activityId 作业ID
 * @returns {Promise}
 */
export function getActivityCostById(activityId) {
  return request({
    url: `/cwgxAi/ma/specialcost/activity/${activityId}`,
    method: 'get'
  })
}

/**
 * 删除作业成本
 * @param {Number} activityId 作业ID
 * @returns {Promise}
 */
export function deleteActivityCost(activityId) {
  return request({
    url: `/cwgxAi/ma/specialcost/activity/${activityId}`,
    method: 'delete'
  })
}

/**
 * 获取作业成本分配数据
 * @param {Object} data 分配参数
 * @returns {Promise}
 */
export function getActivityCostAllocation(data) {
  return request({
    url: '/cwgxAi/ma/specialcost/activity/allocation',
    method: 'post',
    data: transData(data)
  })
}

// ==================== 质量成本 API ====================

/**
 * 分页查询质量成本列表
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getQualityCostPage(data) {
  return request({
    url: '/cwgxAi/ma/specialcost/quality/getList',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 保存或更新质量成本
 * @param {Object} data 质量成本数据
 * @returns {Promise}
 */
export function saveOrUpdateQualityCost(data) {
  return request({
    url: '/cwgxAi/ma/specialcost/quality/saveOrUpdate',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取质量成本详情
 * @param {Number} qualityId 质量成本ID
 * @returns {Promise}
 */
export function getQualityCostById(qualityId) {
  return request({
    url: `/cwgxAi/ma/specialcost/quality/${qualityId}`,
    method: 'get'
  })
}

/**
 * 删除质量成本
 * @param {Number} qualityId 质量成本ID
 * @returns {Promise}
 */
export function deleteQualityCost(qualityId) {
  return request({
    url: `/cwgxAi/ma/specialcost/quality/${qualityId}`,
    method: 'delete'
  })
}

/**
 * 获取质量成本分类统计
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getQualityCostCategoryStats(params) {
  return request({
    url: '/cwgxAi/ma/specialcost/quality/categoryStats',
    method: 'get',
    params
  })
}

// ==================== 环境成本 API ====================

/**
 * 分页查询环境成本列表
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getEnvironmentCostPage(data) {
  return request({
    url: '/cwgxAi/ma/specialcost/environment/getList',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 保存或更新环境成本
 * @param {Object} data 环境成本数据
 * @returns {Promise}
 */
export function saveOrUpdateEnvironmentCost(data) {
  return request({
    url: '/cwgxAi/ma/specialcost/environment/saveOrUpdate',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取环境成本详情
 * @param {Number} environmentId 环境成本ID
 * @returns {Promise}
 */
export function getEnvironmentCostById(environmentId) {
  return request({
    url: `/cwgxAi/ma/specialcost/environment/${environmentId}`,
    method: 'get'
  })
}

/**
 * 删除环境成本
 * @param {Number} environmentId 环境成本ID
 * @returns {Promise}
 */
export function deleteEnvironmentCost(environmentId) {
  return request({
    url: `/cwgxAi/ma/specialcost/environment/${environmentId}`,
    method: 'delete'
  })
}

/**
 * 获取环境效益评估数据
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getEnvironmentBenefitAssessment(params) {
  return request({
    url: '/cwgxAi/ma/specialcost/environment/benefitAssessment',
    method: 'get',
    params
  })
}

// ==================== 研发成本 API ====================

/**
 * 分页查询研发成本列表
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getRdCostPage(data) {
  return request({
    url: '/cwgxAi/ma/specialcost/rd/getList',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 保存或更新研发成本
 * @param {Object} data 研发成本数据
 * @returns {Promise}
 */
export function saveOrUpdateRdCost(data) {
  return request({
    url: '/cwgxAi/ma/specialcost/rd/saveOrUpdate',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取研发成本详情
 * @param {Number} rdId 研发成本ID
 * @returns {Promise}
 */
export function getRdCostById(rdId) {
  return request({
    url: `/cwgxAi/ma/specialcost/rd/${rdId}`,
    method: 'get'
  })
}

/**
 * 删除研发成本
 * @param {Number} rdId 研发成本ID
 * @returns {Promise}
 */
export function deleteRdCost(rdId) {
  return request({
    url: `/cwgxAi/ma/specialcost/rd/${rdId}`,
    method: 'delete'
  })
}

/**
 * 获取研发效益评估数据
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getRdBenefitAssessment(params) {
  return request({
    url: '/cwgxAi/ma/specialcost/rd/benefitAssessment',
    method: 'get',
    params
  })
}

/**
 * 获取研发投资回报分析数据
 * @param {Object} data 分析参数
 * @returns {Promise}
 */
export function getRdRoiAnalysis(data) {
  return request({
    url: '/cwgxAi/ma/specialcost/rd/roiAnalysis',
    method: 'post',
    data: transData(data)
  })
}

// ==================== 专项分析 API ====================

/**
 * 获取专项成本综合分析数据
 * @param {Object} data 分析参数
 * @returns {Promise}
 */
export function getSpecialCostAnalysis(data) {
  return request({
    url: '/cwgxAi/ma/specialcost/analysis/comprehensive',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取专项成本综合分析数据（别名）
 * @param {Object} data 分析参数
 * @returns {Promise}
 */
export function getSpecialCostComprehensiveAnalysis(data) {
  return request({
    url: '/cwgxAi/ma/specialcost/analysis/comprehensive',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取专项成本对比分析数据
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getSpecialCostCompareAnalysis(params) {
  return request({
    url: '/cwgxAi/ma/specialcost/analysis/compare',
    method: 'get',
    params
  })
}

/**
 * 导出专项成本分析报告
 * @param {Object} data 导出参数
 * @returns {Promise}
 */
export function exportSpecialCostReport(data) {
  return request({
    url: '/cwgxAi/ma/specialcost/analysis/export',
    method: 'post',
    data: transData(data),
    responseType: 'blob'
  })
}

// ==================== 扩展功能接口 ====================

/**
 * 获取专项成本页面数据（统一分页接口）
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getSpecialCostPage(data) {
  return request({
    url: '/cwgxAi/ma/specialcost/getList',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 保存或更新专项成本（统一保存接口）
 * @param {Object} data 成本数据
 * @returns {Promise}
 */
export function saveOrUpdateSpecialCost(data) {
  return request({
    url: '/cwgxAi/ma/specialcost/saveOrUpdate',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取专项成本详情
 * @param {Number} costId 成本ID
 * @returns {Promise}
 */
export function getSpecialCostById(costId) {
  return request({
    url: `/cwgxAi/ma/specialcost/${costId}`,
    method: 'get'
  })
}

/**
 * 删除专项成本
 * @param {Number} costId 成本ID
 * @returns {Promise}
 */
export function deleteSpecialCost(costId) {
  return request({
    url: `/cwgxAi/ma/specialcost/${costId}`,
    method: 'delete'
  })
}

/**
 * 批量删除专项成本
 * @param {Array} costIds 成本ID数组
 * @returns {Promise}
 */
export function batchDeleteSpecialCost(costIds) {
  return request({
    url: '/cwgxAi/ma/specialcost/batchDelete',
    method: 'delete',
    data: costIds
  })
}

/**
 * 获取专项成本概览数据
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getSpecialCostOverview(params) {
  return request({
    url: '/cwgxAi/ma/specialcost/overview',
    method: 'get',
    params
  })
}

/**
 * 启动项目成本核算
 * @param {Number} projectId 项目ID
 * @param {Object} data 核算参数
 * @returns {Promise}
 */
export function startProjectCostAccounting(projectId, data) {
  return request({
    url: `/cwgxAi/ma/specialcost/project/${projectId}/start`,
    method: 'post',
    data: transData(data)
  })
}

/**
 * 结束项目成本核算
 * @param {Number} projectId 项目ID
 * @param {Object} data 结束参数
 * @returns {Promise}
 */
export function endProjectCostAccounting(projectId, data) {
  return request({
    url: `/cwgxAi/ma/specialcost/project/${projectId}/end`,
    method: 'post',
    data: transData(data)
  })
}

/**
 * 研发成本资本化
 * @param {Number} rdId 研发ID
 * @param {Object} data 资本化参数
 * @returns {Promise}
 */
export function capitalizeRdCost(rdId, data) {
  return request({
    url: `/cwgxAi/ma/specialcost/rd/${rdId}/capitalize`,
    method: 'post',
    data: transData(data)
  })
}

/**
 * 研发成本费用化
 * @param {Number} rdId 研发ID
 * @param {Object} data 费用化参数
 * @returns {Promise}
 */
export function expenseRdCost(rdId, data) {
  return request({
    url: `/cwgxAi/ma/specialcost/rd/${rdId}/expense`,
    method: 'post',
    data: transData(data)
  })
}

/**
 * 质量成本分析
 * @param {Object} data 分析参数
 * @returns {Promise}
 */
export function analyzeQualityCost(data) {
  return request({
    url: '/cwgxAi/ma/specialcost/quality/analyze',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 环境成本评估
 * @param {Object} data 评估参数
 * @returns {Promise}
 */
export function assessEnvironmentalCost(data) {
  return request({
    url: '/cwgxAi/ma/specialcost/environmental/assess',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 作业成本分配
 * @param {Object} data 分配参数
 * @returns {Promise}
 */
export function allocateActivityCost(data) {
  return request({
    url: '/cwgxAi/ma/specialcost/activity/allocate',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 导出专项成本数据
 * @param {Object} data 导出参数
 * @returns {Promise}
 */
export function exportSpecialCostData(data) {
  return request({
    url: '/cwgxAi/ma/specialcost/export',
    method: 'post',
    data: transData(data),
    responseType: 'blob'
  })
}
