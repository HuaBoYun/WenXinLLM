import request from '@/utils/request'

/**
 * 启动转换任务
 * @param {Object} data 转换请求参数
 * @returns {Promise}
 */
export function startTransform(data) {
  return request({
    url: '/finance/transform/start',
    method: 'post',
    data
  })
}

/**
 * 暂停转换任务
 * @param {String} taskId 任务ID
 * @returns {Promise}
 */
export function pauseTransform(taskId) {
  return request({
    url: `/finance/transform/pause/${taskId}`,
    method: 'post'
  })
}

/**
 * 恢复转换任务
 * @param {String} taskId 任务ID
 * @returns {Promise}
 */
export function resumeTransform(taskId) {
  return request({
    url: `/finance/transform/resume/${taskId}`,
    method: 'post'
  })
}

/**
 * 取消转换任务
 * @param {String} taskId 任务ID
 * @returns {Promise}
 */
export function cancelTransform(taskId) {
  return request({
    url: `/finance/transform/cancel/${taskId}`,
    method: 'post'
  })
}

/**
 * 获取转换任务状态
 * @param {String} taskId 任务ID
 * @returns {Promise}
 */
export function getTransformStatus(taskId) {
  return request({
    url: `/finance/transform/status/${taskId}`,
    method: 'get'
  })
}

/**
 * 获取转换任务列表
 * @param {Number} pageNumber 页码
 * @param {Number} pageSize 每页数量
 * @returns {Promise}
 */
export function getTransformTaskList(pageNumber, pageSize) {
  return request({
    url: '/finance/transform/list',
    method: 'get',
    params: {
      pageNumber,
      pageSize
    }
  })
}

/**
 * 获取转换任务详情
 * @param {String} taskId 任务ID
 * @returns {Promise}
 */
export function getTransformTaskDetail(taskId) {
  return request({
    url: `/finance/transform/detail/${taskId}`,
    method: 'get'
  })
}

/**
 * 删除转换任务
 * @param {String} taskId 任务ID
 * @returns {Promise}
 */
export function deleteTransformTask(taskId) {
  return request({
    url: `/finance/transform/delete/${taskId}`,
    method: 'delete'
  })
}

/**
 * 获取采集任务的源表列表
 * @param {String} taskId 采集任务ID
 * @returns {Promise}
 */
export function getSourceTables(taskId) {
  return request({
    url: `/finance/transform/collection/tables/${taskId}`,
    method: 'get'
  })
}

/**
 * 获取源表字段列表
 * @param {String} taskId 采集任务ID
 * @param {String} tableName 表名
 * @returns {Promise}
 */
export function getSourceFields(taskId, tableName) {
  return request({
    url: `/finance/transform/collection/fields/${taskId}/${tableName}`,
    method: 'get'
  })
}

/**
 * 获取系统表列表
 * @returns {Promise}
 */
export function getSystemTables() {
  return request({
    url: '/finance/transform/system/tables',
    method: 'get'
  })
}

/**
 * 获取系统表字段列表
 * @param {String} tableName 表名
 * @returns {Promise}
 */
export function getSystemFields(tableName) {
  return request({
    url: `/finance/transform/system/fields/${tableName}`,
    method: 'get'
  })
}

/**
 * 验证字段类型匹配
 * @param {Object} data 验证参数
 * @returns {Promise}
 */
export function validateTypeConversion(data) {
  return request({
    url: '/finance/transform/validate',
    method: 'post',
    params: {
      sourceType: data.sourceType,
      targetType: data.targetType,
      sampleData: data.sampleData ? data.sampleData.join(',') : ''
    }
  })
}

/**
 * 创建转换任务
 * @param {Object} data 转换任务配置
 * @returns {Promise}
 */
export function createTransformTask(data) {
  return request({
    url: '/finance/transform/create',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data
  })
}

/**
 * 执行转换任务
 * @param {String} taskId 任务ID
 * @returns {Promise}
 */
export function executeTransformTask(taskId) {
  return request({
    url: `/finance/transform/execute/${taskId}`,
    method: 'post'
  })
}

/**
 * 获取可用算法列表
 * @returns {Promise}
 */
export function getAvailableAlgorithms() {
  return request({
    url: '/finance/transform/algorithm/available',
    method: 'get'
  })
}

/**
 * 保存字段映射配置
 * @param {Array} mappings 字段映射数组
 * @param {String} transformTaskId 转化任务ID
 * @returns {Promise}
 */
export function saveFieldMappings(mappings, transformTaskId) {
  return request({
    url: '/finance/transform/mapping/save',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    params: {
      transformTaskId  // URL参数
    },
    data: mappings  // 请求体只包含mappings数组
  })
}

/**
 * 保存算法配置
 * @param {Array} configs 算法配置数组
 * @param {String} transformTaskId 转化任务ID
 * @returns {Promise}
 */
export function saveAlgorithmConfigs(configs, transformTaskId) {
  return request({
    url: '/finance/transform/algorithm/save',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    params: {
      transformTaskId  // URL参数
    },
    data: configs  // 请求体只包含configs数组
  })
}

/**
 * 根据采集任务ID查询关联的财务版本信息
 * @param {String} collectionTaskId 采集任务ID
 * @returns {Promise}
 */
export function getFinanceVersionByCollectionTask(collectionTaskId) {
  return request({
    url: `/finance/transform/version/query/${collectionTaskId}`,
    method: 'get'
  })
}

/**
 * 根据财务版本FID查询转换配置信息
 * @param {String} versionFid 财务版本FID
 * @returns {Promise}
 */
export function getTransformConfigByVersion(versionFid) {
  return request({
    url: `/finance/transform/config/query/${versionFid}`,
    method: 'get'
  })
}
