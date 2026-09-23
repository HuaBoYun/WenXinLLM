/**
 * AI提示词配置API
 * 用户自定义系统提示词的查询与保存
 */
import request from '@/utils/request'

/**
 * 获取用户的提示词配置
 * @param {String} userId 用户ID
 * @param {String} type 提示词类型：'consult'（AI咨询）等
 */
export function getPromptConfig(userId, type = 'consult') {
  return request({
    url: '/bigmodel/v1/ai/prompt',
    method: 'get',
    params: { userId, type }
  })
}

/**
 * 保存（或更新）用户的提示词配置
 * @param {Object} data 提示词配置
 * @param {String} data.userId 用户ID
 * @param {String} data.promptType 提示词类型，如 'consult'
 * @param {String} data.promptContent 提示词内容
 */
export function savePromptConfig(data) {
  return request({
    url: '/bigmodel/v1/ai/prompt/save',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
