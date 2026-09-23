import request from '@/utils/request'

// 云连接合同管理 API
// 网关转发规则：/qqsk/** -> 截取 /qqsk 转发到 /**
const prefix = '/qqsk/settlement/cloud-connection-contract'

// 获取云连接合同列表
// 参考BankInterfaceConfigManage.vue的请求方式，使用GET请求
export function getCloudContractList(query) {
  return request({
    url: `${prefix}/page`,
    method: 'get',
    params: query
  })
}

// 获取云连接合同详情
export function getCloudContractDetail(id) {
  return request({
    url: `${prefix}/detail/${id}`,
    method: 'get'
  })
}

// 创建云连接合同
export function createCloudContract(data) {
  return request({
    url: `${prefix}`,
    method: 'post',
    data,
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

// 更新云连接合同
export function updateCloudContract(data) {
  return request({
    url: `${prefix}`,
    method: 'put',
    data,
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

// 删除云连接合同
export function deleteCloudContract(id) {
  return request({
    url: `${prefix}/delete/${id}`,
    method: 'delete'
  })
}

// 批量删除云连接合同
export function batchDeleteCloudContract(ids) {
  return request({
    url: `${prefix}/batch`,
    method: 'delete',
    params: { ids }
  })
}

// 导出云连接合同
export function exportCloudContract(query) {
  return request({
    url: `${prefix}/export`,
    method: 'post',
    params: query,
    responseType: 'blob'
  })
}

// 获取合同统计数据
export function getCloudContractStatistics() {
  return request({
    url: `${prefix}/statistics`,
    method: 'get'
  })
}

// 合同续约提醒
export function getContractRenewalAlert() {
  return request({
    url: `${prefix}/renewal-alert`,
    method: 'get'
  })
}

// 启用/禁用合同
export function toggleCloudContract(id, enabled) {
  return request({
    url: `${prefix}/toggle/${id}`,
    method: 'put',
    params: { enabled }
  })
}
