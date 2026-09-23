import request from '@/utils/request'

// 电子票据账户配置管理 API接口

/**
 * 分页查询电票账户列表
 */
export function listETicketAccount(params) {
  return request({
    url: '/cwgxAi/basicConfig/eTicketAccount/list',
    method: 'get',
    params
  })
}

/**
 * 搜索电票账户列表
 */
export function searchETicketAccount(data) {
  return request({
    url: '/cwgxAi/basicConfig/eTicketAccount/search',
    method: 'post',
    data
  })
}

/**
 * 根据ID查询电票账户详情
 */
export function getETicketAccount(id) {
  return request({
    url: `/cwgxAi/basicConfig/eTicketAccount/detail?id=${id}`,
    method: 'get'
  })
}

/**
 * 删除电票账户
 */
export function delETicketAccount(id) {
  return request({
    url: `/cwgxAi/basicConfig/eTicketAccount/${id}`,
    method: 'delete'
  })
}

/**
 * 添加电票账户
 */
export function addETicketAccount(data) {
  return request({
    url: '/cwgxAi/basicConfig/eTicketAccount/create',
    method: 'post',
    data
  })
}

/**
 * 更新电票账户
 */
export function updateETicketAccount(data) {
  return request({
    url: '/cwgxAi/basicConfig/eTicketAccount/update',
    method: 'put',
    data
  })
}

/**
 * 获取初始化数据
 */
export function getInitData() {
  return request({
    url: '/cwgxAi/basicConfig/eTicketAccount/initData',
    method: 'get'
  })
}

/**
 * 获取编辑数据
 */
export function getEditData(id) {
  return request({
    url: `/cwgxAi/basicConfig/eTicketAccount/editData?id=${id}`,
    method: 'get'
  })
}

/**
 * 检查账户号码唯一性
 */
export function checkAccountNo(data) {
  return request({
    url: '/cwgxAi/basicConfig/eTicketAccount/checkAccountNo',
    method: 'post',
    data
  })
}

/**
 * 验证银行账户信息
 */
export function validateBankAccount(data) {
  return request({
    url: '/cwgxAi/basicConfig/eTicketAccount/validateAccount',
    method: 'post',
    data
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
 * 获取银行机构选项
 */
export function getBankInstitutions() {
  return request({
    url: '/cwgxAi/basicConfig/eTicketAccount/bankInstitutions',
    method: 'get'
  })
}

/**
 * 获取账户状态选项
 */
export function getAccountStatuses() {
  return request({
    url: '/cwgxAi/basicConfig/eTicketAccount/accountStatuses',
    method: 'get'
  })
}

/**
 * 导出电票账户配置
 */
export function exportETicketAccount(params) {
  return request({
    url: '/cwgxAi/basicConfig/eTicketAccount/export',
    method: 'get',
    params
  })
}

/**
 * 批量导入电票账户配置
 */
export function batchImportETicketAccount(file, updateUser) {
  const formData = new FormData()
  formData.append('file', file)
  return request({
    url: '/cwgxAi/basicConfig/eTicketAccount/import',
    method: 'post',
    data: formData,
    params: { updateUser },
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 获取电票账户统计信息
 */
export function getETicketAccountStatistics() {
  return request({
    url: '/cwgxAi/basicConfig/eTicketAccount/statistics',
    method: 'get'
  })
}