import request from '@/utils/request'

// 印鉴档案管理 API接口 (cwgxAi版本)

/**
 * 分页查询印鉴档案
 */
export function getSealArchiveList(params) {
  return request({
    url: '/cwgxAi/basicConfig/seal/list',
    method: 'get',
    params
  })
}

/**
 * 获取印鉴档案详情
 */
export function getSealArchiveDetail(id) {
  return request({
    url: `/cwgxAi/basicConfig/seal/detail?id=${id}`,
    method: 'get'
  })
}

/**
 * 创建印鉴档案
 */
export function createSealArchive(data) {
  return request({
    url: '/cwgxAi/basicConfig/seal/create',
    method: 'post',
    data
  })
}

/**
 * 更新印鉴档案
 */
export function updateSealArchive(data) {
  return request({
    url: '/cwgxAi/basicConfig/seal/update',
    method: 'post',
    data
  })
}

/**
 * 删除印鉴档案
 */
export function deleteSealArchive(id) {
  return request({
    url: `/cwgxAi/basicConfig/seal/delete/${id}`,
    method: 'delete'
  })
}

/**
 * 批量删除印鉴档案
 */
export function batchDeleteSealArchive(ids) {
  return request({
    url: '/cwgxAi/basicConfig/seal/batchDelete',
    method: 'delete',
    data: ids
  })
}

/**
 * 获取印鉴类型选项
 */
export function getSealTypes() {
  return request({
    url: '/cwgxAi/basicConfig/seal/types',
    method: 'get'
  })
}

/**
 * 获取印鉴统计数据
 */
export function getSealStatistics() {
  return request({
    url: '/cwgxAi/basicConfig/seal/statistics',
    method: 'get'
  })
}

/**
 * 上传印鉴图片
 */
export function uploadSealImage(file, sealId) {
  const formData = new FormData()
  formData.append('file', file)
  formData.append('sealId', sealId)
  return request({
    url: '/cwgxAi/basicConfig/seal/uploadImage',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 更新印鉴状态
 */
export function updateSealStatus(data) {
  return request({
    url: '/cwgxAi/basicConfig/seal/updateStatus',
    method: 'post',
    data
  })
}

/**
 * 检查印鉴编码唯一性
 */
export function checkSealCodeUnique(data) {
  return request({
    url: '/cwgxAi/basicConfig/seal/checkCode',
    method: 'post',
    data
  })
}
