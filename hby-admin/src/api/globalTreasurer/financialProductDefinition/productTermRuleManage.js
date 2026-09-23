import request from '@/utils/request'

// 产品期限规则管理API
const BASE_URL = '/qqsk/financial/product-definition/term-rule'

/**
 * 分页查询期限规则列表
 */
export function getProductTermRuleList(params) {
  // 使用 FormData 格式
  const formData = new FormData()
  // 直接添加所有参数，让后端处理空值
  Object.keys(params).forEach(key => {
    const value = params[key]
    // 只添加非 undefined 的值
    if (value !== undefined) {
      formData.append(key, value === null ? '' : value)
    }
  })
  return request({
    url: `${BASE_URL}/getList`,
    method: 'post',
    data: formData
  }).then(response => {
    // 处理totalRecord字符串转数字的问题
    if (response && response.data && typeof response.data.totalRecord === 'string') {
      response.data.totalRecord = parseInt(response.data.totalRecord, 10)
    }
    return response
  })
}

/**
 * 根据ID查询期限规则详情
 */
export function getProductTermRuleDetail(termRuleId) {
  return request({
    url: `${BASE_URL}/getById`,
    method: 'get',
    params: { ID: termRuleId }
  })
}

/**
 * 新增期限规则
 */
export function createProductTermRule(data) {
  console.log('API - 新增期限规则请求:', data)
  return request({
    url: `${BASE_URL}/create`,
    method: 'post',
    headers: {
      'Content-Type': 'application/json'
    },
    data
  }).then(response => {
    console.log('API - 新增期限规则响应:', response)
    // 标准化响应格式
    if (response && response.code === 200) {
      response.code = 1
    }
    return response
  }).catch(error => {
    console.error('API - 新增期限规则错误:', error)
    // 尝试从错误响应中提取信息
    if (error.response && error.response.data) {
      console.error('错误详情:', error.response.data)
      throw error.response.data
    }
    throw error
  })
}

/**
 * 更新期限规则
 */
export function updateProductTermRule(data) {
  console.log('API - 更新期限规则请求:', data)
  return request({
    url: `${BASE_URL}/update`,
    method: 'post',
    headers: {
      'Content-Type': 'application/json'
    },
    data
  }).then(response => {
    console.log('API - 更新期限规则响应:', response)
    // 标准化响应格式
    if (response && response.code === 200) {
      response.code = 1
    }
    return response
  }).catch(error => {
    console.error('API - 更新期限规则错误:', error)
    // 尝试从错误响应中提取信息
    if (error.response && error.response.data) {
      console.error('错误详情:', error.response.data)
      throw error.response.data
    }
    throw error
  })
}

/**
 * 保存期限规则（新增或更新）
 */
export function saveProductTermRule(data) {
  return request({
    url: `${BASE_URL}/save`,
    method: 'post',
    data
  })
}

/**
 * 删除期限规则
 */
export function deleteProductTermRule(termRuleId) {
  return request({
    url: `${BASE_URL}/delete`,
    method: 'get',
    params: { ID: termRuleId }
  })
}

/**
 * 批量删除期限规则
 */
export function batchDeleteProductTermRules(termRuleIds) {
  return request({
    url: `${BASE_URL}/batchDelete`,
    method: 'post',
    headers: {
      'Content-Type': 'application/json'
    },
    data: { IDs: termRuleIds.join(',') }
  })
}

/**
 * 查询期限规则统计信息
 */
export function getProductTermRuleStatistics() {
  return request({
    url: `${BASE_URL}/getStatistics`,
    method: 'post'
  })
}

/**
 * 期限计算
 */
export function calculateTerm(params) {
  // 使用 FormData 格式
  const formData = new FormData()
  Object.keys(params).forEach(key => {
    const value = params[key]
    if (value !== undefined) {
      formData.append(key, value === null ? '' : value)
    }
  })
  return request({
    url: `${BASE_URL}/calculate`,
    method: 'post',
    data: formData
  })
}
