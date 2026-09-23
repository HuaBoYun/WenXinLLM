/**
 * 债券类别管理API
 * @author 示例云开发团队
 * @since 2026-02-04
 */
import request from '@/utils/request'

/**
 * 分页查询债券类别列表
 */
export function getBondCategoryPage(data) {
  return request({
    url: '/qqsk/financing/bond-category/page',
    method: 'post',
    data
  })
}

/**
 * 根据ID查询债券类别详情
 */
export function getBondCategoryDetail(id) {
  return request({
    url: `/qqsk/financing/bond-category/${id}`,
    method: 'get'
  })
}

/**
 * 新增债券类别
 */
export function addBondCategory(data) {
  return request({
    url: '/qqsk/financing/bond-category/add',
    method: 'post',
    data
  })
}

/**
 * 更新债券类别
 */
export function updateBondCategory(data) {
  return request({
    url: '/qqsk/financing/bond-category/update',
    method: 'put',
    data
  })
}

/**
 * 删除债券类别
 */
export function deleteBondCategory(ids) {
  return request({
    url: `/qqsk/financing/bond-category/${ids}`,
    method: 'delete'
  })
}

/**
 * 切换债券类别状态
 */
export function toggleBondCategoryStatus(data) {
  return request({
    url: '/qqsk/financing/bond-category/toggle-status',
    method: 'put',
    data
  })
}

