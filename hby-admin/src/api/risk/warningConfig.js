/**
 * 预警配置管理API接口
 * @author 示例云
 * @date 2025-09-30
 */

import request from '@/utils/request'

// 基础API路径
const API_BASE = '/api/warning/config'

/**
 * 分页查询预警配置列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getWarningConfigList(params) {
  return request({
    url: `${API_BASE}/list`,
    method: 'post',
    params: params
  })
}

/**
 * 保存预警配置
 * @param {Object} data 配置数据
 * @returns {Promise}
 */
export function saveWarningConfig(data) {
  return request({
    url: `${API_BASE}/save`,
    method: 'post',
    data: data
  })
}

/**
 * 更新预警配置
 * @param {Object} data 配置数据
 * @returns {Promise}
 */
export function updateWarningConfig(data) {
  return request({
    url: `${API_BASE}/update`,
    method: 'post',
    data: data
  })
}

/**
 * 删除预警配置
 * @param {String} configId 配置ID
 * @returns {Promise}
 */
export function deleteWarningConfig(configId) {
  return request({
    url: `${API_BASE}/delete/${configId}`,
    method: 'post'
  })
}

/**
 * 获取预警配置详情
 * @param {String} configId 配置ID
 * @returns {Promise}
 */
export function getWarningConfigDetail(configId) {
  return request({
    url: `${API_BASE}/detail/${configId}`,
    method: 'post'
  })
}

/**
 * 更新配置状态
 * @param {String} configId 配置ID
 * @param {String} isEnabled 是否启用
 * @returns {Promise}
 */
export function updateConfigStatus(configId, isEnabled) {
  return request({
    url: `${API_BASE}/updateStatus`,
    method: 'post',
    params: {
      configId: configId,
      isEnabled: isEnabled
    }
  })
}

/**
 * 批量更新配置状态
 * @param {Array} configIds 配置ID列表
 * @param {String} isEnabled 是否启用
 * @returns {Promise}
 */
export function batchUpdateConfigStatus(configIds, isEnabled) {
  return request({
    url: `${API_BASE}/batchUpdateStatus`,
    method: 'post',
    data: configIds,
    params: {
      isEnabled: isEnabled
    }
  })
}

/**
 * 根据类型获取配置
 * @param {String} configType 配置类型
 * @returns {Promise}
 */
export function getConfigByType(configType) {
  return request({
    url: `${API_BASE}/getByType`,
    method: 'post',
    params: {
      configType: configType
    }
  })
}

/**
 * 验证配置值格式
 * @param {String} configType 配置类型
 * @param {String} configValue 配置值
 * @returns {Promise}
 */
export function validateConfigValue(configType, configValue) {
  return request({
    url: `${API_BASE}/validate`,
    method: 'post',
    params: {
      configType: configType,
      configValue: configValue
    }
  })
}

/**
 * 获取配置模板
 * @param {String} configType 配置类型
 * @returns {Promise}
 */
export function getConfigTemplate(configType) {
  return request({
    url: `${API_BASE}/template/${configType}`,
    method: 'post'
  })
}

/**
 * 导出配置
 * @param {String} configType 配置类型
 * @returns {Promise}
 */
export function exportConfigs(configType) {
  return request({
    url: `${API_BASE}/export`,
    method: 'post',
    params: {
      configType: configType
    }
  })
}

/**
 * 导入配置
 * @param {String} configData 配置数据
 * @returns {Promise}
 */
export function importConfigs(configData) {
  return request({
    url: `${API_BASE}/import`,
    method: 'post',
    params: {
      configData: configData
    }
  })
}

/**
 * 获取所有配置类型
 * @returns {Array}
 */
export function getConfigTypes() {
  return [
    { value: 'NOTIFICATION', label: '通知配置' },
    { value: 'SCHEDULE', label: '调度配置' },
    { value: 'THRESHOLD', label: '阈值配置' }
  ]
}

/**
 * 获取配置状态选项
 * @returns {Array}
 */
export function getConfigStatusOptions() {
  return [
    { value: 'Y', label: '启用' },
    { value: 'N', label: '禁用' }
  ]
}

/**
 * 获取配置类型对应的默认模板
 * @param {String} configType 配置类型
 * @returns {Object}
 */
export function getDefaultConfigTemplate(configType) {
  const templates = {
    'NOTIFICATION': {
      enabled: true,
      smtpHost: 'smtp.company.com',
      smtpPort: 587,
      username: 'warning@company.com',
      recipients: ['admin@company.com']
    },
    'SCHEDULE': {
      frequency: '*/5 * * * *',
      enabled: true,
      maxConcurrency: 5
    },
    'THRESHOLD': {
      engineering: 4000000,
      material: 2000000,
      service: 1000000,
      accountAge: 365,
      urgentRatio: 10
    }
  }
  
  return templates[configType] || {}
}

/**
 * 验证配置值的客户端验证
 * @param {String} configType 配置类型
 * @param {Object} configValue 配置值对象
 * @returns {Object} 验证结果
 */
export function clientValidateConfig(configType, configValue) {
  try {
    switch (configType) {
      case 'NOTIFICATION':
        if (!configValue.smtpHost || !configValue.username) {
          return { valid: false, message: '通知配置缺少必要字段' }
        }
        break
      case 'SCHEDULE':
        if (!configValue.frequency) {
          return { valid: false, message: '调度配置缺少频率设置' }
        }
        break
      case 'THRESHOLD':
        if (Object.keys(configValue).length === 0) {
          return { valid: false, message: '阈值配置不能为空' }
        }
        break
    }
    return { valid: true, message: '验证通过' }
  } catch (error) {
    return { valid: false, message: '配置格式错误' }
  }
}

/**
 * 格式化配置值显示
 * @param {String} configValue JSON字符串
 * @returns {String} 格式化后的字符串
 */
export function formatConfigValue(configValue) {
  try {
    const obj = typeof configValue === 'string' ? JSON.parse(configValue) : configValue
    return JSON.stringify(obj, null, 2)
  } catch (error) {
    return configValue
  }
}
