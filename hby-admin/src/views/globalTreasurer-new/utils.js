/*
 * @Date: 2025-09-26 16:00:00
 * @LastEditors: AI Assistant
 * @LastEditTime: 2025-09-26 16:00:00
 * @FilePath: /hb-admin/src/views/globalTreasurer-new/utils.js
 * @Description: 全球司库系统工具函数
 */

import { SUCCESS_CODE, PAGINATION_CONFIG } from './consts'

/**
 * 检查响应是否成功
 * @param {Object} response 响应对象
 * @returns {boolean} 是否成功
 */
export function isResponseSuccess(response) {
  if (!response) return false
  const codes = Array.isArray(SUCCESS_CODE) ? SUCCESS_CODE : [SUCCESS_CODE]
  return codes.includes(response.code)
}

/**
 * 处理API响应数据
 * @param {Object} response 响应对象
 * @returns {Object} 处理后的数据 { list: [], total: 0 }
 */
export function handleResponseData(response) {
  if (!isResponseSuccess(response)) {
    return { list: [], total: 0 }
  }

  // 支持多种数据结构格式
  if (response.data && response.data.list !== undefined) {
    // 标准格式：{ code: 1, data: { list: [], total: 0 } }
    return {
      list: response.data.list || [],
      total: response.data.total || 0
    }
  } else if (response.data && response.data.tlist !== undefined) {
    // PageResult格式：{ code: 1, data: { tlist: [], totalRecord: 0 } }
    return {
      list: response.data.tlist || [],
      total: response.data.totalRecord || 0
    }
  } else if (response.data && Array.isArray(response.data)) {
    // 数组格式：{ code: 1, data: [] }
    return {
      list: response.data || [],
      total: response.data.length || 0
    }
  } else if (response.list !== undefined) {
    // 直接格式：{ code: 1, list: [], total: 0 }
    return {
      list: response.list || [],
      total: response.total || 0
    }
  } else if (response.tlist !== undefined) {
    // 直接PageResult格式：{ code: 1, tlist: [], totalRecord: 0 }
    return {
      list: response.tlist || [],
      total: response.totalRecord || 0
    }
  } else {
    // 兜底处理
    return { list: [], total: 0 }
  }
}

/**
 * 获取错误消息
 * @param {Object} response 响应对象
 * @returns {string} 错误消息
 */
export function getErrorMessage(response) {
  return response?.message || response?.msg || '操作失败'
}

/**
 * 初始化查询表单
 * @param {Object} customQuery 自定义查询参数
 * @returns {Object} 查询表单对象
 */
export function initQueryForm(customQuery = {}) {
  return {
    pageNumber: PAGINATION_CONFIG.pageNumber,
    pageSize: PAGINATION_CONFIG.pageSize,
    ...customQuery
  }
}

/**
 * 重置查询表单
 * @param {Object} queryForm 查询表单对象
 * @param {Object} defaultValues 默认值
 */
export function resetQueryForm(queryForm, defaultValues = {}) {
  const keys = Object.keys(queryForm)
  keys.forEach(key => {
    if (key === 'pageNumber') {
      queryForm[key] = PAGINATION_CONFIG.pageNumber
    } else if (key === 'pageSize') {
      queryForm[key] = PAGINATION_CONFIG.pageSize
    } else if (defaultValues[key] !== undefined) {
      queryForm[key] = defaultValues[key]
    } else if (Array.isArray(queryForm[key])) {
      queryForm[key] = []
    } else if (typeof queryForm[key] === 'object' && queryForm[key] !== null) {
      queryForm[key] = {}
    } else {
      queryForm[key] = ''
    }
  })
}

/**
 * 处理分页大小变化
 * @param {number} val 新的分页大小
 * @param {Object} queryForm 查询表单对象
 * @param {Function} fetchData 数据获取函数
 */
export function handleSizeChange(val, queryForm, fetchData) {
  queryForm.pageSize = val
  queryForm.pageNumber = 1 // 重置到第一页
  fetchData()
}

/**
 * 处理页码变化
 * @param {number} val 新的页码
 * @param {Object} queryForm 查询表单对象
 * @param {Function} fetchData 数据获取函数
 */
export function handleCurrentChange(val, queryForm, fetchData) {
  queryForm.pageNumber = val
  fetchData()
}

/**
 * 格式化日期
 * @param {string|Date} date 日期
 * @param {string} format 格式
 * @returns {string} 格式化后的日期
 */
export function formatDate(date, format = 'yyyy-MM-dd HH:mm:ss') {
  if (!date) return ''
  
  const d = new Date(date)
  if (isNaN(d.getTime())) return ''

  const year = d.getFullYear()
  const month = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  const hours = String(d.getHours()).padStart(2, '0')
  const minutes = String(d.getMinutes()).padStart(2, '0')
  const seconds = String(d.getSeconds()).padStart(2, '0')

  return format
    .replace('yyyy', year)
    .replace('MM', month)
    .replace('dd', day)
    .replace('HH', hours)
    .replace('mm', minutes)
    .replace('ss', seconds)
}

/**
 * 格式化金额
 * @param {number} amount 金额
 * @param {string} unit 单位 (yuan, wan, yi)
 * @param {number} precision 精度
 * @returns {string} 格式化后的金额
 */
export function formatAmount(amount, unit = 'yuan', precision = 2) {
  if (!amount && amount !== 0) return '0'
  
  let value = Number(amount)
  let suffix = '元'
  
  switch (unit) {
    case 'wan':
      value = value / 10000
      suffix = '万元'
      break
    case 'yi':
      value = value / 100000000
      suffix = '亿元'
      break
    default:
      suffix = '元'
  }
  
  return value.toFixed(precision) + suffix
}

/**
 * 获取状态标签类型
 * @param {string} status 状态值
 * @param {Object} statusMap 状态映射
 * @returns {string} 标签类型
 */
export function getStatusTagType(status, statusMap = {}) {
  const defaultMap = {
    'NORMAL': 'success',
    'ACTIVE': 'success',
    'SUCCESS': 'success',
    'APPROVED': 'success',
    'COMPLETED': 'success',
    'PENDING': 'warning',
    'PROCESSING': 'warning',
    'REVIEW': 'warning',
    'FAILED': 'danger',
    'REJECTED': 'danger',
    'CANCELLED': 'danger',
    'FROZEN': 'danger',
    'DISABLED': 'info',
    'DRAFT': 'info'
  }
  
  const map = { ...defaultMap, ...statusMap }
  return map[status] || 'default'
}

/**
 * 获取状态显示名称
 * @param {string} status 状态值
 * @param {Array} statusOptions 状态选项
 * @returns {string} 状态名称
 */
export function getStatusName(status, statusOptions = []) {
  const option = statusOptions.find(item => item.value === status)
  return option ? option.label : status
}

/**
 * 深拷贝对象
 * @param {any} obj 要拷贝的对象
 * @returns {any} 拷贝后的对象
 */
export function deepClone(obj) {
  if (obj === null || typeof obj !== 'object') return obj
  if (obj instanceof Date) return new Date(obj.getTime())
  if (obj instanceof Array) return obj.map(item => deepClone(item))
  if (typeof obj === 'object') {
    const clonedObj = {}
    for (const key in obj) {
      if (obj.hasOwnProperty(key)) {
        clonedObj[key] = deepClone(obj[key])
      }
    }
    return clonedObj
  }
}

/**
 * 防抖函数
 * @param {Function} func 要防抖的函数
 * @param {number} delay 延迟时间
 * @returns {Function} 防抖后的函数
 */
export function debounce(func, delay = 300) {
  let timeoutId
  return function (...args) {
    clearTimeout(timeoutId)
    timeoutId = setTimeout(() => func.apply(this, args), delay)
  }
}

/**
 * 节流函数
 * @param {Function} func 要节流的函数
 * @param {number} delay 延迟时间
 * @returns {Function} 节流后的函数
 */
export function throttle(func, delay = 300) {
  let lastTime = 0
  return function (...args) {
    const now = Date.now()
    if (now - lastTime >= delay) {
      lastTime = now
      func.apply(this, args)
    }
  }
}

/**
 * 生成唯一ID
 * @returns {string} 唯一ID
 */
export function generateId() {
  return Date.now().toString(36) + Math.random().toString(36).substr(2)
}

/**
 * 验证表单
 * @param {Object} formRef 表单引用
 * @returns {Promise<boolean>} 验证结果
 */
export function validateForm(formRef) {
  return new Promise((resolve) => {
    formRef.validate((valid) => {
      resolve(valid)
    })
  })
}
