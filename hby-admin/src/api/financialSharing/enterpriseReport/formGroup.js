import request from '@/utils/request'

/**
 * 查询表单组列表
 */
export function getFormGroupList(data) {
  return request({
    url: '/cwgxAi/enterpriseReport/formGroup/getList',
    method: 'post',
    data
  })
}

/**
 * 查询表单组详情
 */
export function getFormGroupDetail(groupId) {
  return request({
    url: '/cwgxAi/enterpriseReport/formGroup/detail',
    method: 'post',
    data: { groupId }
  })
}

/**
 * 保存表单组
 */
export function saveFormGroup(data) {
  return request({
    url: '/cwgxAi/enterpriseReport/formGroup/save',
    method: 'post',
    data
  })
}

/**
 * 删除表单组
 */
export function deleteFormGroup(groupId) {
  return request({
    url: '/cwgxAi/enterpriseReport/formGroup/delete',
    method: 'post',
    data: { groupId }
  })
}

/**
 * 根据目录ID查询表单组列表
 */
export function getFormGroupListByDirectoryId(directoryId) {
  return request({
    url: '/cwgxAi/enterpriseReport/formGroup/getListByDirectoryId',
    method: 'post',
    data: { directoryId }
  })
}

