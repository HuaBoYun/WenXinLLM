import request from '@/utils/request'

// 电子票据账户配置管理 API接口

/**
 * 分页查询电票账户列表
 */
export function listETicketAccount(params) {
  return request({
    url: '/qqsk/financial/basicConfig/eTicketAccount/list',
    method: 'get',
    params
  })
}

/**
 * 分页查询电票账户列表(POST方式)
 */
export function getETicketAccountPage(params) {
  return request({
    url: '/qqsk/financial/basicConfig/eTicketAccount/page',
    method: 'post',
    data: params,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 根据ID查询电票账户
 */
export function getETicketAccount(id) {
  return request({
    url: `/qqsk/financial/basicConfig/eTicketAccount/detail?id=${id}`,
    method: 'get'
  })
}

/**
 * 创建电票账户
 */
export function createETicketAccount(data) {
  return request({
    url: '/qqsk/financial/basicConfig/eTicketAccount/create',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 更新电票账户
 */
export function updateETicketAccount(data) {
  return request({
    url: '/qqsk/financial/basicConfig/eTicketAccount/update',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 删除电票账户
 */
export function deleteETicketAccount(id) {
  return request({
    url: `/qqsk/financial/basicConfig/eTicketAccount/${id}`,
    method: 'delete'
  })
}

/**
 * 批量删除电票账户
 */
export function batchDeleteETicketAccount(ids) {
  return request({
    url: '/qqsk/financial/basicConfig/eTicketAccount/batch',
    method: 'delete',
    data: ids,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 同步账户状态
 */
export function syncAccountStatus(id) {
  return request({
    url: `/qqsk/financial/basicConfig/eTicketAccount/sync-status/${id}`,
    method: 'post'
  })
}
