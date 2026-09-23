/**
 * AI对话历史记录API
 */
import request from '@/utils/request'

/**
 * 保存对话历史记录
 * @param {Object} data 历史记录数据
 * @param {String} data.type 历史记录类型：'programming'（编程）或 'writing'（写作/建模）
 */
export function saveChatHistory(data) {
  return request({
    url: '/bigmodel/v1/ai/chat/history/save',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 获取用户的对话历史列表（支持分页，用于滚动加载）
 * @param {String} userId 用户ID
 * @param {String} type 历史记录类型：'programming'（编程）或 'writing'（写作/建模）
 * @param {Number} pageNum 页码（从1开始）
 * @param {Number} pageSize 每页大小
 */
export function getChatHistoryList(userId, type = 'writing', pageNum = 1, pageSize = 20) {
  return request({
    url: `/bigmodel/v1/ai/chat/history/list/${userId}`,
    method: 'get',
    params: { type, pageNum, pageSize }
  })
}

/**
 * 获取单条对话历史详情
 * @param {String} id 历史记录ID
 */
export function getChatHistoryDetail(id) {
  return request({
    url: `/bigmodel/v1/ai/chat/history/detail/${id}`,
    method: 'get'
  })
}

/**
 * 删除对话历史
 * @param {String} id 历史记录ID
 */
export function deleteChatHistory(id) {
  return request({
    url: `/bigmodel/v1/ai/chat/history/delete/${id}`,
    method: 'delete'
  })
}

/**
 * 更新对话历史
 * @param {Object} data 历史记录数据
 * @param {String} data.type 历史记录类型：'programming'（编程）或 'writing'（写作/建模）
 */
export function updateChatHistory(data) {
  return request({
    url: '/bigmodel/v1/ai/chat/history/update',
    method: 'put',
    data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
