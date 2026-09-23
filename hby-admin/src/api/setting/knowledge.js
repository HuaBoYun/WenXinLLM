import request from '@/utils/request'
import { transData } from '@/utils/requestData'

/**
 * @description: 获取token
 * @param {*}
 * @return {*}
 */
export function getToken(data) {
  return request({
    url: `/setting/getToken?username=${data.username}`,
    method: 'post',
    headers: {
      'Content-Type': 'application/json',
    },
    data: transData(data),
  })
}
/**
 * @description: 验证token是否有效
 * @param {*}
 * @return {*}
 */
export function isValidToken(data) {
  return request({
    url: `/setting/isValidToken?token=${data.token}`,
    method: 'post',
  })
}

/**
 * @description: 知识管理平台对接
 * @param {*}
 * @return {*}
 */
export function knowledge(data) {
  return request({
    url: '/setting/knowledge',
    method: 'post',
    data,
  })
}

/**
 * @description: knowledgeSearch
 * @param {*}
 * @return {*}
 */
export function knowledgeSearch(data) {
  return request({
    url: '/setting/knowledgeSearch',
    method: 'post',
    data,
  })
}

/**
 * @description: 获取文件预览链接
 * @param {*} params
 * @return {*}
 */
export function getFilePreviewUrl(params) {
  return request({
    url: '/setting/getFileUrl',
    method: 'get',
    params: transData(params),
  })
}

