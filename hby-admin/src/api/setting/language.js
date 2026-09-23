import request from '@/utils/request'
// import { transData } from '@/utils/requestData'

/**
 * @description: 获取可翻译语言
 * @param {*}
 * @return {*}
 */
export function getConversionList(data) {
  return request({
    url: '/setting/language/conversion/getList',
    method: 'get',
    params: data,
  })
}
/**
 * @description: 保存/更新语种
 * @param {*}
 * @return {*}
 */
export function saveOrUpdateConversion(data) {
  return request({
    url: '/setting/language/conversion/menger',
    method: 'post',
    data: data,
  })
}

/**
 * @description: 删除语种
 * @param {*}
 * @return {*}
 */
export function removeConversion(data) {
  return request({
    url: '/setting/language/conversion/remove',
    method: 'get',
    params: data,
  })
}

/**
 * @description: 获取翻译短语
 * @param {*}
 * @return {*}
 */
export function getTranslateList(data) {
  return request({
    url: '/setting/language/conversion/getTranslateList',
    method: 'get',
    params: data,
  })
}

/**
 * @description: 删除翻译
 * @param {*}
 * @return {*}
 */
export function removeTranslate(data) {
  return request({
    url: '/setting/language/translate/remove',
    method: 'post',
    data: data,
  })
}

/**
 * @description: 新增/修改翻译
 * @param {*}
 * @return {*}
 */
export function saveOrUpdateTranslate(data) {
  return request({
    url: '/setting/language/translate/menger',
    method: 'post',
    data: data,
  })
}
