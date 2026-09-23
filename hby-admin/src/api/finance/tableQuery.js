import request from '@/utils/request'

/**
 * 检查表是否存在
 * @param {String} tableName 表名
 * @returns {Promise}
 */
export function checkTableExists(tableName) {
  return request({
    url: '/finance/table/check',
    method: 'get',
    params: {
      tableName
    }
  })
}

/**
 * 获取表结构信息(包含表备注、字段信息)
 * @param {String} tableName 表名
 * @returns {Promise}
 */
export function getTableStructure(tableName) {
  return request({
    url: '/finance/table/structure',
    method: 'get',
    params: {
      tableName
    }
  })
}

/**
 * 查询表数据
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function getTableData(data) {
  return request({
    url: '/finance/table/data',
    method: 'post',
    data
  })
}

/**
 * 获取已查询表列表
 * @returns {Promise}
 */
export function getQueriedTableList() {
  return request({
    url: '/finance/table/history',
    method: 'get'
  })
}

/**
 * 更新表的分类
 * @param {Object} data 更新参数
 * @returns {Promise}
 */
export function updateTableCategory(data) {
  return request({
    url: '/finance/table/category',
    method: 'post',
    data
  })
}
