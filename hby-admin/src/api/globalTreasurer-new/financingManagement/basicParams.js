/**
 * 融资基础参数管理API
 * @author 示例云开发团队
 * @since 2026-02-04
 */
import request from '@/utils/request'

/**
 * 分页查询融资基础参数列表
 */
export function getBasicParamsPage(data) {
  return request({
    url: '/qqsk/financing/basic-params/page',
    method: 'post',
    data
  })
}

/**
 * 根据ID查询融资基础参数详情
 */
export function getBasicParamsDetail(id) {
  return request({
    url: `/qqsk/financing/basic-params/${id}`,
    method: 'get'
  })
}

/**
 * 新增融资基础参数
 */
export function addBasicParams(data) {
  return request({
    url: '/qqsk/financing/basic-params/add',
    method: 'post',
    data
  })
}

/**
 * 更新融资基础参数
 */
export function updateBasicParams(data) {
  return request({
    url: '/qqsk/financing/basic-params/update',
    method: 'put',
    data
  })
}

/**
 * 删除融资基础参数
 */
export function deleteBasicParams(id) {
  return request({
    url: `/qqsk/financing/basic-params/${id}`,
    method: 'delete'
  })
}

/**
 * 切换融资基础参数状态
 */
export function toggleBasicParamsStatus(data) {
  return request({
    url: '/qqsk/financing/basic-params/toggle-status',
    method: 'put',
    data
  })
}

