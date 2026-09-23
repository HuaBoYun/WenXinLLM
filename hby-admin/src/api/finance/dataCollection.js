import request from '@/utils/request'

/**
 * 启动采集任务
 * @param {Object} data 采集请求参数
 * @returns {Promise}
 */
export function startCollection(data) {
  return request({
    url: '/finance/collection/start',
    method: 'post',
    data
  })
}

/**
 * 暂停采集任务
 * @param {String} taskId 任务ID
 * @returns {Promise}
 */
export function pauseCollection(taskId) {
  return request({
    url: `/finance/collection/pause/${taskId}`,
    method: 'post'
  })
}

/**
 * 恢复采集任务
 * @param {String} taskId 任务ID
 * @returns {Promise}
 */
export function resumeCollection(taskId) {
  return request({
    url: `/finance/collection/resume/${taskId}`,
    method: 'post'
  })
}

/**
 * 取消采集任务
 * @param {String} taskId 任务ID
 * @returns {Promise}
 */
export function cancelCollection(taskId) {
  return request({
    url: `/finance/collection/cancel/${taskId}`,
    method: 'post'
  })
}

/**
 * 获取采集任务状态
 * @param {String} taskId 任务ID
 * @returns {Promise}
 */
export function getCollectionStatus(taskId) {
  return request({
    url: `/finance/collection/status/${taskId}`,
    method: 'get'
  })
}

/**
 * 获取采集任务列表
 * @param {Number} pageNumber 页码
 * @param {Number} pageSize 每页数量
 * @param {String} taskName 任务名称(可选)
 * @param {String} taskStatus 任务状态(可选)
 * @param {String} taskCategory 任务分类(可选)
 * @returns {Promise}
 */
export function getCollectionTaskList(pageNumber, pageSize, taskName, taskStatus, taskCategory) {
  return request({
    url: '/finance/collection/list',
    method: 'get',
    params: {
      pageNumber,
      pageSize,
      taskName,
      taskStatus,
      taskCategory
    }
  })
}

/**
 * 获取采集任务详情
 * @param {String} taskId 任务ID
 * @returns {Promise}
 */
export function getCollectionTaskDetail(taskId) {
  return request({
    url: `/finance/collection/detail/${taskId}`,
    method: 'get'
  })
}

/**
 * 删除采集任务
 * @param {String} taskId 任务ID
 * @returns {Promise}
 */
export function deleteCollectionTask(taskId) {
  return request({
    url: `/finance/collection/delete/${taskId}`,
    method: 'delete'
  })
}

/**
 * 获取采集任务进度详情
 * @param {String} taskId 任务ID
 * @returns {Promise}
 */
export function getCollectionProgress(taskId) {
  return request({
    url: `/finance/collection/progress/${taskId}`,
    method: 'get'
  })
}

/**
 * 数据验证
 * @param {String} taskId 任务ID
 * @returns {Promise}
 */
export function validateCollectionData(taskId) {
  return request({
    url: `/finance/collection/validate/${taskId}`,
    method: 'post'
  })
}

/**
 * 保存定时任务配置
 * @param {Object} data 定时任务配置
 * @returns {Promise}
 */
export function saveScheduleConfig(data) {
  return request({
    url: '/finance/collection/schedule/save',
    method: 'post',
    data
  })
}

/**
 * 获取定时任务配置
 * @param {String} taskId 任务ID
 * @returns {Promise}
 */
export function getScheduleConfig(taskId) {
  return request({
    url: `/finance/collection/schedule/${taskId}`,
    method: 'get'
  })
}

/**
 * 删除定时任务配置
 * @param {String} scheduleId 定时任务ID
 * @returns {Promise}
 */
export function deleteScheduleConfig(scheduleId) {
  return request({
    url: `/finance/collection/schedule/delete/${scheduleId}`,
    method: 'delete'
  })
}

/**
 * 下载离线采集工具
 * @returns {Promise}
 */
export function downloadOfflineTool() {
  return request({
    url: '/finance/config/offline/download',
    method: 'get',
    responseType: 'blob' // 重要:指定响应类型为blob,用于文件下载
  })
}

