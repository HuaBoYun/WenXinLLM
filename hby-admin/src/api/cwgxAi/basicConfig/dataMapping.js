import request from '@/utils/request'

// 数据映射配置管理 API接口 (cwgxAi版本)

/**
 * 分页查询数据映射配置
 */
export function getDataMappingList(params) {
  return request({
    url: '/cwgxAi/basicConfig/mapping/page',
    method: 'get',
    params
  })
}

/**
 * 创建数据映射配置
 */
export function createDataMapping(data) {
  return request({
    url: '/cwgxAi/basicConfig/mapping/create',
    method: 'post',
    data
  })
}

/**
 * 更新数据映射配置
 */
export function updateDataMapping(data) {
  return request({
    url: '/cwgxAi/basicConfig/mapping/update',
    method: 'post',
    data
  })
}

/**
 * 删除数据映射配置
 */
export function deleteDataMapping(data) {
  return request({
    url: `/cwgxAi/basicConfig/mapping/delete/${data.id}`,
    method: 'delete'
  })
}

/**
 * 测试数据映射
 */
export function testDataMapping(data) {
  return request({
    url: '/cwgxAi/basicConfig/mapping/test',
    method: 'post',
    data
  })
}
