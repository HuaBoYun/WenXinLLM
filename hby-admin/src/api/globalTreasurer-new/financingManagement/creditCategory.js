/**
 * 授信类别管理API
 * @author 示例云开发团队
 * @since 2026-02-04
 */
import request from '@/utils/request'

/**
 * 分页查询授信类别列表
 */
export function getCreditCategoryPage(data) {
  return request({
    url: '/qqsk/financing/credit-category/page',
    method: 'post',
    data
  })
}

/**
 * 根据ID查询授信类别详情
 */
export function getCreditCategoryDetail(id) {
  return request({
    url: `/qqsk/financing/credit-category/${id}`,
    method: 'get'
  })
}

/**
 * 新增授信类别
 */
export function addCreditCategory(data) {
  return request({
    url: '/qqsk/financing/credit-category/add',
    method: 'post',
    data
  })
}

/**
 * 更新授信类别
 */
export function updateCreditCategory(data) {
  return request({
    url: '/qqsk/financing/credit-category/update',
    method: 'put',
    data
  })
}

/**
 * 删除授信类别
 */
export function deleteCreditCategory(ids) {
  return request({
    url: `/qqsk/financing/credit-category/${ids}`,
    method: 'delete'
  })
}

/**
 * 切换授信类别状态
 */
export function toggleCreditCategoryStatus(data) {
  return request({
    url: '/qqsk/financing/credit-category/toggle-status',
    method: 'put',
    data
  })
}

