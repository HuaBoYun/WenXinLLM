import request from '@/utils/request'

/**
 * 项目结算管理API接口
 * 
 * @author 示例云开发团队
 * @since 2025-01-21
 */

/**
 * 创建项目结算
 * @param {Object} data 结算数据
 */
export function createProjectSettlement(data) {
  return request({
    url: '/contract/settlement/create',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

/**
 * 分页查询项目结算列表
 * @param {Object} params 查询参数
 */
export function getProjectSettlementPage(params) {
  return request({
    url: '/contract/settlement/page',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data: params
  })
}

/**
 * 更新项目结算
 * @param {Object} data 结算数据
 */
export function updateProjectSettlement(data) {
  return request({
    url: '/contract/settlement/update',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

/**
 * 删除项目结算
 * @param {Number} id 结算ID
 */
export function deleteProjectSettlement(id) {
  return request({
    url: `/contract/settlement/delete/${id}`,
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

/**
 * 查询结算详情
 * @param {Number} id 结算ID
 */
export function getProjectSettlementDetail(id) {
  return request({
    url: `/contract/settlement/detail/${id}`,
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

/**
 * 结算审核
 * @param {Number} id 结算ID
 * @param {Object} data 审核数据
 */
export function reviewProjectSettlement(id, data) {
  return request({
    url: `/contract/settlement/review/${id}`,
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

/**
 * 获取项目结算统计信息
 * @param {Number} projectId 项目ID
 */
export function getProjectSettlementStatistics(projectId) {
  return request({
    url: `/contract/settlement/statistics/${projectId}`,
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

/**
 * 获取待审核结算列表
 */
export function getPendingReviewSettlements() {
  return request({
    url: '/contract/settlement/pending-review',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

/**
 * 批量审核结算
 * @param {Object} data 批量审核数据
 */
export function batchReviewSettlements(data) {
  return request({
    url: '/contract/settlement/batch-review',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

/**
 * 生成结算编号
 */
export function generateSettlementNo() {
  return request({
    url: '/contract/settlement/generate-no',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

/**
 * 结算提醒相关API（扩展功能）
 */

/**
 * 创建结算提醒
 * @param {Object} data 提醒数据
 */
export function createSettlementReminder(data) {
  return request({
    url: '/contract/settlement/reminder/create',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

/**
 * 获取结算提醒列表
 * @param {Object} params 查询参数
 */
export function getSettlementReminderList(params) {
  return request({
    url: '/contract/settlement/reminder/list',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data: params
  })
}

/**
 * 更新结算提醒
 * @param {Object} data 提醒数据
 */
export function updateSettlementReminder(data) {
  return request({
    url: '/contract/settlement/reminder/update',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

/**
 * 删除结算提醒
 * @param {Number} id 提醒ID
 */
export function deleteSettlementReminder(id) {
  return request({
    url: `/contract/settlement/reminder/delete/${id}`,
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

/**
 * 处理结算提醒
 * @param {Number} id 提醒ID
 * @param {Object} data 处理数据
 */
export function processSettlementReminder(id, data) {
  return request({
    url: `/contract/settlement/reminder/process/${id}`,
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

/**
 * 批量处理结算提醒
 * @param {Object} data 批量处理数据
 */
export function batchProcessReminders(data) {
  return request({
    url: '/contract/settlement/reminder/batch-process',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}

/**
 * 发送结算提醒通知
 * @param {Object} data 通知数据
 */
export function sendSettlementNotification(data) {
  return request({
    url: '/contract/settlement/reminder/send-notification',
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data
  })
}
