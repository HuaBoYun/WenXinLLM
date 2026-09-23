import request from '@/utils/request'

/**
 * 查询表单目录树形结构
 */
export function getDirectoryTree(data) {
  return request({
    url: '/cwgxAi/enterpriseReport/formDirectory/getTree',
    method: 'post',
    data
  })
}

/**
 * 查询表单目录列表
 */
export function getDirectoryList(data) {
  return request({
    url: '/cwgxAi/enterpriseReport/formDirectory/getList',
    method: 'post',
    data
  })
}

/**
 * 查询表单目录详情
 */
export function getDirectoryDetail(directoryId) {
  return request({
    url: '/cwgxAi/enterpriseReport/formDirectory/detail',
    method: 'post',
    data: { directoryId }
  })
}

/**
 * 保存表单目录
 */
export function saveDirectory(data) {
  return request({
    url: '/cwgxAi/enterpriseReport/formDirectory/save',
    method: 'post',
    data
  })
}

/**
 * 删除表单目录
 */
export function deleteDirectory(directoryId) {
  return request({
    url: '/cwgxAi/enterpriseReport/formDirectory/delete',
    method: 'post',
    data: { directoryId }
  })
}

