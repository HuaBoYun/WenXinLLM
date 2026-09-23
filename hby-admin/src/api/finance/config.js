import request from '@/utils/request'

/**
 * 获取财务版本信息列表
 * @returns {Promise}
 */
export function getFVersionList() {
  return request({
    url: '/config/fversion/getList',
    method: 'get'
  })
}

/**
 * 获取财务版本信息分类（一级菜单）
 * @returns {Promise}
 */
export function getFVersionParentList() {
  return request({
    url: '/config/fversion/getParentList',
    method: 'get'
  })
}

/**
 * 获取财务版本信息详情
 * @param {String} fid 财务版本ID
 * @returns {Promise}
 */
export function getFVersionDetail(fid) {
  return request({
    url: '/config/fversion/detail',
    method: 'get',
    params: {
      fid
    }
  })
}

/**
 * 保存财务版本信息
 * @param {Object} data 财务版本数据
 * @returns {Promise}
 */
export function saveFVersion(data) {
  return request({
    url: '/config/fversion/save',
    method: 'post',
    data
  })
}

/**
 * 删除财务版本信息
 * @param {String} fid 财务版本ID
 * @returns {Promise}
 */
export function deleteFVersion(fid) {
  return request({
    url: '/config/fversion/remove',
    method: 'get',
    params: {
      fid
    }
  })
}

