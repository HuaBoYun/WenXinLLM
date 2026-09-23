import request from '@/utils/request'

// API基础路径
const API_BASE_PATH = '/api/intg/data-mapping'

// 映射类型常量
export const MAPPING_TYPES = {
  TABLE_TO_TABLE: 'TABLE_TO_TABLE',
  FIELD_TO_FIELD: 'FIELD_TO_FIELD',
  OBJECT_TO_OBJECT: 'OBJECT_TO_OBJECT',
  API_TO_API: 'API_TO_API',
  FILE_TO_TABLE: 'FILE_TO_TABLE',
  TABLE_TO_FILE: 'TABLE_TO_FILE',
  CUSTOM: 'CUSTOM'
}

// 映射方向常量
export const MAPPING_DIRECTIONS = {
  IMPORT: 'IMPORT',
  EXPORT: 'EXPORT',
  BIDIRECTIONAL: 'BIDIRECTIONAL'
}

// 转换规则类型常量
export const TRANSFORM_RULE_TYPES = {
  DIRECT: 'DIRECT',
  FORMULA: 'FORMULA',
  LOOKUP: 'LOOKUP',
  SCRIPT: 'SCRIPT',
  CONSTANT: 'CONSTANT'
}

// 映射状态常量
export const MAPPING_STATUS = {
  ACTIVE: 'ACTIVE',
  INACTIVE: 'INACTIVE',
  TESTING: 'TESTING',
  ERROR: 'ERROR'
}

// 基础CRUD操作

/**
 * 创建数据映射
 * @param {Object} data 数据映射数据
 * @returns {Promise}
 */
export function createDataMapping(data) {
  return request({
    url: API_BASE_PATH,
    method: 'post',
    data
  })
}

/**
 * 更新数据映射
 * @param {String} mappingId 映射ID
 * @param {Object} data 数据映射数据
 * @returns {Promise}
 */
export function updateDataMapping(mappingId, data) {
  return request({
    url: `${API_BASE_PATH}/${mappingId}`,
    method: 'put',
    data
  })
}

/**
 * 删除数据映射
 * @param {String} mappingId 映射ID
 * @returns {Promise}
 */
export function deleteDataMapping(mappingId) {
  return request({
    url: `${API_BASE_PATH}/${mappingId}`,
    method: 'delete'
  })
}

/**
 * 根据ID获取数据映射
 * @param {String} mappingId 映射ID
 * @returns {Promise}
 */
export function getDataMappingById(mappingId) {
  return request({
    url: `${API_BASE_PATH}/${mappingId}`,
    method: 'get'
  })
}

/**
 * 根据编码获取数据映射
 * @param {String} mappingCode 映射编码
 * @returns {Promise}
 */
export function getDataMappingByCode(mappingCode) {
  return request({
    url: `${API_BASE_PATH}/code/${mappingCode}`,
    method: 'get'
  })
}

// 查询操作

/**
 * 分页查询数据映射
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getDataMappingPage(params) {
  return request({
    url: `${API_BASE_PATH}/page`,
    method: 'get',
    params
  })
}

/**
 * 根据系统配置ID查询映射列表
 * @param {String} configId 配置ID
 * @returns {Promise}
 */
export function getDataMappingsByConfigId(configId) {
  return request({
    url: `${API_BASE_PATH}/config/${configId}`,
    method: 'get'
  })
}

/**
 * 根据映射类型查询映射列表
 * @param {String} mappingType 映射类型
 * @returns {Promise}
 */
export function getDataMappingsByType(mappingType) {
  return request({
    url: `${API_BASE_PATH}/type/${mappingType}`,
    method: 'get'
  })
}

/**
 * 根据映射方向查询映射列表
 * @param {String} mappingDirection 映射方向
 * @returns {Promise}
 */
export function getDataMappingsByDirection(mappingDirection) {
  return request({
    url: `${API_BASE_PATH}/direction/${mappingDirection}`,
    method: 'get'
  })
}

/**
 * 根据源表名查询映射列表
 * @param {String} sourceTableName 源表名
 * @returns {Promise}
 */
export function getDataMappingsBySourceTable(sourceTableName) {
  return request({
    url: `${API_BASE_PATH}/source-table/${sourceTableName}`,
    method: 'get'
  })
}

/**
 * 根据目标表名查询映射列表
 * @param {String} targetTableName 目标表名
 * @returns {Promise}
 */
export function getDataMappingsByTargetTable(targetTableName) {
  return request({
    url: `${API_BASE_PATH}/target-table/${targetTableName}`,
    method: 'get'
  })
}

/**
 * 查询启用的映射列表
 * @param {String} configId 配置ID
 * @returns {Promise}
 */
export function getEnabledDataMappings(configId) {
  return request({
    url: `${API_BASE_PATH}/enabled/${configId}`,
    method: 'get'
  })
}

// 映射管理操作

/**
 * 启用数据映射
 * @param {String} mappingId 映射ID
 * @returns {Promise}
 */
export function enableDataMapping(mappingId) {
  return request({
    url: `${API_BASE_PATH}/${mappingId}/enable`,
    method: 'put'
  })
}

/**
 * 禁用数据映射
 * @param {String} mappingId 映射ID
 * @returns {Promise}
 */
export function disableDataMapping(mappingId) {
  return request({
    url: `${API_BASE_PATH}/${mappingId}/disable`,
    method: 'put'
  })
}

/**
 * 批量更新映射状态
 * @param {Array} mappingIds 映射ID列表
 * @param {String} mappingStatus 映射状态
 * @returns {Promise}
 */
export function batchUpdateMappingStatus(mappingIds, mappingStatus) {
  return request({
    url: `${API_BASE_PATH}/batch-update-status`,
    method: 'put',
    params: {
      mappingIds: mappingIds.join(','),
      mappingStatus
    }
  })
}

/**
 * 批量启用映射
 * @param {Array} mappingIds 映射ID列表
 * @returns {Promise}
 */
export function batchEnableMappings(mappingIds) {
  return request({
    url: `${API_BASE_PATH}/batch-enable`,
    method: 'put',
    data: mappingIds
  })
}

/**
 * 批量禁用映射
 * @param {Array} mappingIds 映射ID列表
 * @returns {Promise}
 */
export function batchDisableMappings(mappingIds) {
  return request({
    url: `${API_BASE_PATH}/batch-disable`,
    method: 'put',
    data: mappingIds
  })
}

/**
 * 复制数据映射
 * @param {String} mappingId 映射ID
 * @param {String} newMappingName 新映射名称
 * @returns {Promise}
 */
export function copyDataMapping(mappingId, newMappingName) {
  return request({
    url: `${API_BASE_PATH}/${mappingId}/copy`,
    method: 'post',
    params: {
      newMappingName
    }
  })
}

/**
 * 批量复制映射
 * @param {Array} mappingIds 映射ID列表
 * @param {String} namePrefix 名称前缀
 * @returns {Promise}
 */
export function batchCopyMappings(mappingIds, namePrefix) {
  return request({
    url: `${API_BASE_PATH}/batch-copy`,
    method: 'post',
    data: {
      mappingIds,
      namePrefix
    }
  })
}

// 映射验证操作

/**
 * 验证映射配置
 * @param {Object} dataMapping 数据映射对象
 * @returns {Promise}
 */
export function validateDataMapping(dataMapping) {
  return request({
    url: `${API_BASE_PATH}/validate`,
    method: 'post',
    data: dataMapping
  })
}

/**
 * 验证映射规则
 * @param {String} mappingId 映射ID
 * @returns {Promise}
 */
export function validateMappingRules(mappingId) {
  return request({
    url: `${API_BASE_PATH}/${mappingId}/validate-rules`,
    method: 'post'
  })
}

/**
 * 验证转换脚本
 * @param {String} mappingId 映射ID
 * @returns {Promise}
 */
export function validateTransformScript(mappingId) {
  return request({
    url: `${API_BASE_PATH}/${mappingId}/validate-script`,
    method: 'post'
  })
}

// 映射测试操作

/**
 * 测试映射配置
 * @param {String} mappingId 映射ID
 * @param {Object} testData 测试数据
 * @returns {Promise}
 */
export function testMappingConfig(mappingId, testData) {
  return request({
    url: `${API_BASE_PATH}/${mappingId}/test`,
    method: 'post',
    data: testData
  })
}

/**
 * 批量测试映射
 * @param {Array} mappingIds 映射ID列表
 * @returns {Promise}
 */
export function batchTestMappings(mappingIds) {
  return request({
    url: `${API_BASE_PATH}/batch-test`,
    method: 'post',
    data: mappingIds
  })
}

/**
 * 预览映射结果
 * @param {String} mappingId 映射ID
 * @param {Number} sampleSize 样本大小
 * @returns {Promise}
 */
export function previewMappingResult(mappingId, sampleSize = 10) {
  return request({
    url: `${API_BASE_PATH}/${mappingId}/preview`,
    method: 'get',
    params: {
      sampleSize
    }
  })
}

/**
 * 执行映射转换
 * @param {String} mappingId 映射ID
 * @param {Array} sourceData 源数据
 * @returns {Promise}
 */
export function executeMappingTransform(mappingId, sourceData) {
  return request({
    url: `${API_BASE_PATH}/${mappingId}/transform`,
    method: 'post',
    data: sourceData
  })
}

// 映射执行操作

/**
 * 执行数据映射
 * @param {String} mappingId 映射ID
 * @returns {Promise}
 */
export function executeDataMapping(mappingId) {
  return request({
    url: `${API_BASE_PATH}/${mappingId}/execute`,
    method: 'post'
  })
}

/**
 * 批量执行映射
 * @param {Array} mappingIds 映射ID列表
 * @returns {Promise}
 */
export function batchExecuteMappings(mappingIds) {
  return request({
    url: `${API_BASE_PATH}/batch-execute`,
    method: 'post',
    data: mappingIds
  })
}

/**
 * 调度执行映射
 * @param {String} mappingId 映射ID
 * @param {String} cronExpression Cron表达式
 * @returns {Promise}
 */
export function scheduleDataMapping(mappingId, cronExpression) {
  return request({
    url: `${API_BASE_PATH}/${mappingId}/schedule`,
    method: 'post',
    data: {
      cronExpression
    }
  })
}

/**
 * 停止映射执行
 * @param {String} mappingId 映射ID
 * @returns {Promise}
 */
export function stopMappingExecution(mappingId) {
  return request({
    url: `${API_BASE_PATH}/${mappingId}/stop`,
    method: 'post'
  })
}

// 工具函数

/**
 * 格式化映射类型显示名称
 * @param {String} mappingType 映射类型
 * @returns {String}
 */
export function formatMappingTypeName(mappingType) {
  const typeNames = {
    [MAPPING_TYPES.TABLE_TO_TABLE]: '表到表',
    [MAPPING_TYPES.FIELD_TO_FIELD]: '字段到字段',
    [MAPPING_TYPES.OBJECT_TO_OBJECT]: '对象到对象',
    [MAPPING_TYPES.API_TO_API]: 'API到API',
    [MAPPING_TYPES.FILE_TO_TABLE]: '文件到表',
    [MAPPING_TYPES.TABLE_TO_FILE]: '表到文件',
    [MAPPING_TYPES.CUSTOM]: '自定义'
  }
  return typeNames[mappingType] || mappingType
}

/**
 * 格式化映射方向显示名称
 * @param {String} mappingDirection 映射方向
 * @returns {String}
 */
export function formatMappingDirectionName(mappingDirection) {
  const directionNames = {
    [MAPPING_DIRECTIONS.IMPORT]: '导入',
    [MAPPING_DIRECTIONS.EXPORT]: '导出',
    [MAPPING_DIRECTIONS.BIDIRECTIONAL]: '双向'
  }
  return directionNames[mappingDirection] || mappingDirection
}

/**
 * 格式化转换规则类型显示名称
 * @param {String} transformRuleType 转换规则类型
 * @returns {String}
 */
export function formatTransformRuleTypeName(transformRuleType) {
  const typeNames = {
    [TRANSFORM_RULE_TYPES.DIRECT]: '直接映射',
    [TRANSFORM_RULE_TYPES.FORMULA]: '公式转换',
    [TRANSFORM_RULE_TYPES.LOOKUP]: '查找转换',
    [TRANSFORM_RULE_TYPES.SCRIPT]: '脚本转换',
    [TRANSFORM_RULE_TYPES.CONSTANT]: '常量值'
  }
  return typeNames[transformRuleType] || transformRuleType
}

/**
 * 格式化映射状态显示名称
 * @param {String} mappingStatus 映射状态
 * @returns {String}
 */
export function formatMappingStatusName(mappingStatus) {
  const statusNames = {
    [MAPPING_STATUS.ACTIVE]: '启用',
    [MAPPING_STATUS.INACTIVE]: '禁用',
    [MAPPING_STATUS.TESTING]: '测试中',
    [MAPPING_STATUS.ERROR]: '错误'
  }
  return statusNames[mappingStatus] || mappingStatus
}

/**
 * 生成映射配置模板
 * @param {String} mappingType 映射类型
 * @returns {Object}
 */
export function generateMappingTemplate(mappingType) {
  const baseTemplate = {
    mappingType,
    mappingDirection: MAPPING_DIRECTIONS.IMPORT,
    transformRuleType: TRANSFORM_RULE_TYPES.DIRECT,
    mappingStatus: MAPPING_STATUS.ACTIVE,
    isEnabled: true,
    mappingPriority: 5,
    errorHandlingStrategy: 'SKIP',
    defaultValueStrategy: 'NULL'
  }

  switch (mappingType) {
    case MAPPING_TYPES.TABLE_TO_TABLE:
      return {
        ...baseTemplate,
        sourceTableName: '',
        targetTableName: '',
        sourceFieldName: '*',
        targetFieldName: '*'
      }
    case MAPPING_TYPES.FIELD_TO_FIELD:
      return {
        ...baseTemplate,
        sourceTableName: '',
        targetTableName: '',
        sourceFieldName: '',
        targetFieldName: ''
      }
    case MAPPING_TYPES.API_TO_API:
      return {
        ...baseTemplate,
        sourceTableName: 'api_endpoint',
        targetTableName: 'api_endpoint'
      }
    default:
      return baseTemplate
  }
}

export default {
  // 常量
  MAPPING_TYPES,
  MAPPING_DIRECTIONS,
  TRANSFORM_RULE_TYPES,
  MAPPING_STATUS,
  
  // CRUD操作
  createDataMapping,
  updateDataMapping,
  deleteDataMapping,
  getDataMappingById,
  getDataMappingByCode,
  
  // 查询操作
  getDataMappingPage,
  getDataMappingsByConfigId,
  getDataMappingsByType,
  getDataMappingsByDirection,
  getDataMappingsBySourceTable,
  getDataMappingsByTargetTable,
  getEnabledDataMappings,
  
  // 映射管理
  enableDataMapping,
  disableDataMapping,
  batchUpdateMappingStatus,
  batchEnableMappings,
  batchDisableMappings,
  copyDataMapping,
  batchCopyMappings,
  
  // 映射验证
  validateDataMapping,
  validateMappingRules,
  validateTransformScript,
  
  // 映射测试
  testMappingConfig,
  batchTestMappings,
  previewMappingResult,
  executeMappingTransform,
  
  // 映射执行
  executeDataMapping,
  batchExecuteMappings,
  scheduleDataMapping,
  stopMappingExecution,
  
  // 工具函数
  formatMappingTypeName,
  formatMappingDirectionName,
  formatTransformRuleTypeName,
  formatMappingStatusName,
  generateMappingTemplate
}
