import request from '@/utils/request'

// 电票账户配置管理 API接口 (cwgxAi版本)

/**
 * 分页查询电票账户配置
 */
export function getETicketAccountList(params) {
  return request({
    url: '/cwgxAi/basicConfig/eTicketAccount/list',
    method: 'get',
    params
  })
}

/**
 * 创建电票账户配置
 */
export function createETicketAccount(data) {
  return request({
    url: '/cwgxAi/basicConfig/eTicketAccount/create',
    method: 'post',
    data
  })
}

/**
 * 更新电票账户配置
 */
export function updateETicketAccount(data) {
  return request({
    url: '/cwgxAi/basicConfig/eTicketAccount/update',
    method: 'post',
    data
  })
}

/**
 * 删除电票账户配置
 */
export function deleteETicketAccount(id) {
  return request({
    url: `/cwgxAi/basicConfig/eTicketAccount/delete/${id}`,
    method: 'delete'
  })
}

/**
 * 获取票据类型选项
 */
export function getETicketTypes() {
  return request({
    url: '/cwgxAi/basicConfig/eTicketAccount/eTicketTypes',
    method: 'get'
  })
}

/**
 * 同步账户状态
 */
export function syncETicketAccountStatus() {
  return request({
    url: '/cwgxAi/basicConfig/eTicketAccount/syncStatus',
    method: 'post'
  })
}

/**
 * 测试账户连接
 */
export function testETicketAccountConnection(id) {
  return request({
    url: '/cwgxAi/basicConfig/eTicketAccount/testConnection',
    method: 'post',
    data: { id }
  })
}
