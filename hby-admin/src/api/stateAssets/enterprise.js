/*
 * @Description: 国资国企穿透式监管系统 - 企业信息管理API
 * @Author: System
 * @Date: 2024-01-01
 */
import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// 后端使用@RequestBody，需要JSON格式
const JSON_HEADERS = { 'Content-Type': 'application/json;charset=UTF-8' }

// 企业信息管理相关接口

/**
 * 获取企业列表
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getEnterpriseList(data) {
  return request({
    url: '/monitor/v1/enterprise/info/list',
    method: 'post',
    data: transData(data),
    headers: JSON_HEADERS,
  })
}

/**
 * 获取企业详情
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getEnterpriseDetail(data) {
  return request({
    url: '/monitor/v1/enterprise/info/detail',
    method: 'post',
    data: transData(data),
  })
}

/**
 * 新增企业
 * @param {Object} data 企业信息
 * @returns {Promise}
 */
export function addEnterprise(data) {
  return request({
    url: '/monitor/v1/enterprise/info/add',
    method: 'post',
    data: transData(data),
  })
}

/**
 * 更新企业信息
 * @param {Object} data 企业信息
 * @returns {Promise}
 */
export function updateEnterprise(data) {
  return request({
    url: '/monitor/v1/enterprise/info/update',
    method: 'post',
    data: transData(data),
  })
}

/**
 * 删除企业
 * @param {Object} data 删除参数
 * @returns {Promise}
 */
export function deleteEnterprise(data) {
  return request({
    url: '/monitor/v1/enterprise/info/delete',
    method: 'post',
    data: transData(data),
  })
}

/**
 * 获取母公司列表（用于下拉选择）
 * @returns {Promise}
 */
export function getParentEnterpriseList() {
  return request({
    url: '/monitor/v1/enterprise/info/parent-list',
    method: 'get',
  })
}

// 企业层级关系管理相关接口

/**
 * 获取企业层级关系
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getEnterpriseHierarchy(data) {
  return request({
    url: '/monitor/v1/enterprise/hierarchy/list',
    method: 'post',
    data: transData(data),
  })
}

/**
 * 新增企业层级关系
 * @param {Object} data 层级关系信息
 * @returns {Promise}
 */
export function addEnterpriseHierarchy(data) {
  return request({
    url: '/monitor/v1/enterprise/hierarchy/add',
    method: 'post',
    data: transData(data),
  })
}

/**
 * 更新企业层级关系
 * @param {Object} data 层级关系信息
 * @returns {Promise}
 */
export function updateEnterpriseHierarchy(data) {
  return request({
    url: '/monitor/v1/enterprise/hierarchy/update',
    method: 'post',
    data: transData(data),
  })
}

/**
 * 删除企业层级关系
 * @param {Object} data 删除参数
 * @returns {Promise}
 */
export function deleteEnterpriseHierarchy(data) {
  return request({
    url: '/monitor/v1/enterprise/hierarchy/delete',
    method: 'post',
    data: transData(data),
  })
}

/**
 * 获取企业层级关系图数据
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getEnterpriseHierarchyChart(data) {
  return request({
    url: '/monitor/v1/enterprise/hierarchy/chart',
    method: 'post',
    data: transData(data),
  })
}

// 企业统计分析相关接口

/**
 * 获取企业统计数据
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getEnterpriseStatistics(data) {
  return request({
    url: '/monitor/v1/enterprise/statistics/overview',
    method: 'post',
    data: transData(data),
  })
}

/**
 * 获取企业类型分布
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getEnterpriseTypeDistribution(data) {
  return request({
    url: '/monitor/v1/enterprise/statistics/type-distribution',
    method: 'post',
    data: transData(data),
  })
}

/**
 * 获取企业地区分布
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getEnterpriseRegionDistribution(data) {
  return request({
    url: '/monitor/v1/enterprise/statistics/region-distribution',
    method: 'post',
    data: transData(data),
  })
}

/**
 * 获取企业行业分布
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getEnterpriseIndustryDistribution(data) {
  return request({
    url: '/monitor/v1/enterprise/statistics/industry-distribution',
    method: 'post',
    data: transData(data),
  })
}

// 企业导入导出相关接口

/**
 * 导出企业列表
 * @param {Object} data 导出参数
 * @returns {Promise}
 */
export function exportEnterpriseList(data) {
  return request({
    url: '/monitor/v1/enterprise/info/export',
    method: 'post',
    data: transData(data),
    responseType: 'blob',
  })
}

/**
 * 下载企业导入模板
 * @returns {Promise}
 */
export function downloadEnterpriseTemplate() {
  return request({
    url: '/monitor/v1/enterprise/info/template',
    method: 'get',
    responseType: 'blob',
  })
}

/**
 * 批量导入企业
 * @param {FormData} data 文件数据
 * @returns {Promise}
 */
export function importEnterpriseList(data) {
  return request({
    url: '/monitor/v1/enterprise/info/import',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  })
}

// 企业验证相关接口

/**
 * 验证统一社会信用代码是否重复
 * @param {Object} data 验证参数
 * @returns {Promise}
 */
export function validateCreditCode(data) {
  return request({
    url: '/monitor/v1/enterprise/info/validate-credit-code',
    method: 'post',
    data: transData(data),
  })
}

/**
 * 验证企业名称是否重复
 * @param {Object} data 验证参数
 * @returns {Promise}
 */
export function validateEnterpriseName(data) {
  return request({
    url: '/monitor/v1/enterprise/info/validate-name',
    method: 'post',
    data: transData(data),
  })
}
