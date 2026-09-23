import request from '@/utils/request'

/**
 * 根据版本FID获取所有字段映射
 * @param {String} versionFid 版本FID
 * @returns {Promise}
 */
export function getByVersionFid(versionFid) {
  return request({
    url: `/finance/versionFieldMapping/getByVersionFid/${versionFid}`,
    method: 'get'
  })
}

/**
 * 根据版本FID和源表名获取字段映射
 * @param {String} versionFid 版本FID
 * @param {String} sourceTableName 源表名
 * @returns {Promise}
 */
export function getBySourceTable(versionFid, sourceTableName) {
  return request({
    url: `/finance/versionFieldMapping/getBySourceTable/${versionFid}/${sourceTableName}`,
    method: 'get'
  })
}

/**
 * 根据版本FID和目标表名获取字段映射
 * @param {String} versionFid 版本FID
 * @param {String} targetTableName 目标表名
 * @returns {Promise}
 */
export function getByTargetTable(versionFid, targetTableName) {
  return request({
    url: `/finance/versionFieldMapping/getByTargetTable/${versionFid}/${targetTableName}`,
    method: 'get'
  })
}

/**
 * 保存字段映射
 * @param {Object} data 字段映射数据
 * @returns {Promise}
 */
export function save(data) {
  return request({
    url: '/finance/versionFieldMapping/save',
    method: 'post',
    data
  })
}

/**
 * 批量保存字段映射
 * @param {Array} data 字段映射数据数组
 * @returns {Promise}
 */
export function saveBatch(data) {
  return request({
    url: '/finance/versionFieldMapping/saveBatch',
    method: 'post',
    data
  })
}

/**
 * 删除字段映射
 * @param {String} mappingId 映射ID
 * @returns {Promise}
 */
export function deleteMapping(mappingId) {
  return request({
    url: `/finance/versionFieldMapping/delete/${mappingId}`,
    method: 'post'
  })
}

/**
 * 删除版本的所有字段映射
 * @param {String} versionFid 版本FID
 * @returns {Promise}
 */
export function deleteByVersionFid(versionFid) {
  return request({
    url: `/finance/versionFieldMapping/deleteByVersionFid/${versionFid}`,
    method: 'post'
  })
}

/**
 * 获取版本的所有源表名称
 * @param {String} versionFid 版本FID
 * @returns {Promise}
 */
export function getSourceTableNames(versionFid) {
  return request({
    url: `/finance/versionFieldMapping/getSourceTableNames/${versionFid}`,
    method: 'get'
  })
}

/**
 * 获取版本的所有目标表名称
 * @param {String} versionFid 版本FID
 * @returns {Promise}
 */
export function getTargetTableNames(versionFid) {
  return request({
    url: `/finance/versionFieldMapping/getTargetTableNames/${versionFid}`,
    method: 'get'
  })
}

